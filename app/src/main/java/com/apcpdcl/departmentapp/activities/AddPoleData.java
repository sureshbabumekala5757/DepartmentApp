package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.models.DTRModel;
import com.apcpdcl.departmentapp.models.DistributionModel;
import com.apcpdcl.departmentapp.models.PoleModel;
import com.apcpdcl.departmentapp.services.LocationService;
import com.apcpdcl.departmentapp.services.UploadService;
import com.apcpdcl.departmentapp.sqlite.PoleDataDataBaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.apcpdcl.departmentapp.services.UploadService.ACTION_UPLOAD;

public class AddPoleData extends AppCompatActivity implements View.OnClickListener {
    //Progress Dialog
    private ProgressDialog pDialog;
    //ScrollView
    private ScrollView scrollView;
    //Linear Layout
    private LinearLayout layoutAdd, ll_pole_data, ll_uscno_data, ll_dtr_data;
    //Spiners
    private Spinner spn_distribution, spn_dtr, spn_pole_no;
    //Pole EditText
    private EditText et_polelno, et_pole_name;
    private TextView tv_section_code, tv_location_accuracy, tv_dtr_location_accuracy, tv_dtr_long, tv_dtr_lat;
    //Buttons
    private Button savePoleDataBtn, addUscnoBtn, saveDtrDataBtn;
    private ImageView location_imv, location_dtr_imv;
    //RaioGroup
    private RadioGroup rg_pole_uscno_check;
    //ArrList Vars
    private ArrayList<String> distributionList;
    private ArrayList<String> dtrList;
    private ArrayList<String> poleList;
    private ArrayList<DTRModel> dtrListArr = new ArrayList<>();
    private ArrayList<DistributionModel> distributionListArr = new ArrayList<>();
    private HashMap<String, String> hashDTR = new HashMap<>();
    public HashMap<String, DistributionModel> hashDistribution = new HashMap<>();
    //DB
    private PoleDataDataBaseHandler poleDB;
    //SharedPreferences
    public SharedPreferences poleSPreferences;
    SharedPreferences.Editor polePrefsEditor;
    //String Vars
    private String sSelectedDisCode, sSelectedDtrCode, sSelectedPoleCode;
    private String sSelectedDisName, sSelectedDtrName, sSelectedPoleName;
    private String checkedStr, userName, sectionCode, sPoleNo, sPoleName, strAccuracy,sectionName;
    //Location
    //private FusedLocationProviderClient fusedLocationClient;
    LocationManager locationManager;
    private String latitude, longitude, strLat, strLon;
    private static final int REQUEST_CODE_CHECK_SETTINGS = 1;
    public static final int LOCATION_UPDATE_INTERVAL = 2000;
    public static final int LOCATION_UPDATE_FASTEST_INTERVAL = 2000;

    //public Handler messageHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pole_data);

        pDialog = new ProgressDialog(this);
        //Location Update
        startLocationUpdates();
        // Find my layout
        scrollView = findViewById(R.id.scrollView);
        layoutAdd = findViewById(R.id.layoutAdd);
        ll_pole_data = findViewById(R.id.ll_pole_data);
        ll_dtr_data = findViewById(R.id.ll_dtr_data);
        ll_uscno_data = findViewById(R.id.ll_uscno_data);
        //Spiners
        spn_distribution = findViewById(R.id.spn_distribution);
        spn_dtr = findViewById(R.id.spn_dtr);
        spn_pole_no = findViewById(R.id.spn_pole_no);
        //Get polePref
        poleSPreferences = getSharedPreferences("POLEPref", MODE_PRIVATE);
//        polePrefsEditor = poleSPreferences.edit();
//        polePrefsEditor.putInt("POLENO",0);
//        polePrefsEditor.apply();
//        int val = poleSPreferences.getInt("POLENO",0);
//        String sPNo = "P"+String.valueOf(val);
        //PoleNo
        et_polelno = findViewById(R.id.et_polelno);
//        et_polelno.setText(sPNo);
        //PoleName
        et_pole_name = findViewById(R.id.et_pole_name);
        //Display Section code/name
        tv_section_code = findViewById(R.id.tv_section_code);
        tv_location_accuracy = findViewById(R.id.tv_location_accuracy);
        tv_dtr_lat = findViewById(R.id.tv_dtr_lat);
        tv_dtr_long = findViewById(R.id.tv_dtr_long);
        tv_dtr_location_accuracy = findViewById(R.id.tv_dtr_location_accuracy);

        //Radiogroup
        rg_pole_uscno_check = findViewById(R.id.rg_pole_uscno_check);
        //Buttons
        location_imv = findViewById(R.id.location_imv);
        savePoleDataBtn = findViewById(R.id.savePoleDataBtn);
        location_dtr_imv = findViewById(R.id.location_imv1);
        saveDtrDataBtn = findViewById(R.id.saveDtrDataBtn);
        addUscnoBtn = findViewById(R.id.addUscnoBtn);
        //Get Username and Sectioncode
        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
        userName = lprefs.getString("UserName", "");
        sectionCode = lprefs.getString("Section_Code", "");
        //Set Section code
        //poleSPreferences.getString("SECTIONNAME","");
        tv_section_code.setText(poleSPreferences.getString("SECTIONNAME", ""));
        //Button click Listeners
        savePoleDataBtn.setOnClickListener(this);
        addUscnoBtn.setOnClickListener(this);
        location_imv.setOnClickListener(this);
        saveDtrDataBtn.setOnClickListener(this);
        location_dtr_imv.setOnClickListener(this);
        //Disable Button
        savePoleDataBtn.setEnabled(false);
        savePoleDataBtn.setClickable(false);
        savePoleDataBtn.setBackgroundColor(Color.GRAY);

        //Disable Button
        saveDtrDataBtn.setEnabled(false);
        saveDtrDataBtn.setClickable(false);
        saveDtrDataBtn.setBackgroundColor(Color.GRAY);

        //Local DB Handler
        poleDB = new PoleDataDataBaseHandler(this);
/******************************************SAMPLE DATA************************************************************************/
        //Staticly add DTR And Distributions
//        dtrListArr = new ArrayList<>();
//        dtrListArr.add(new DTRModel("DTR1", "dt123456781"));
//        dtrListArr.add(new DTRModel("DTR2", "dt123456782"));
//        dtrListArr.add(new DTRModel("DTR3", "dt123456783"));
//        dtrListArr.add(new DTRModel("DTR4", "dt123456784"));
//        dtrListArr.add(new DTRModel("DTR5", "dt123456785"));
//        dtrListArr.add(new DTRModel("DTR6", "dt123456786"));
        //poleDB.insertDTRData(dtrListArr,"1234");

//        distributionListArr = new ArrayList<>();
//        distributionListArr.add(new DistributionModel("Distribution1", "123456781"));
//        distributionListArr.add(new DistributionModel("Distribution2", "123456782"));
//        distributionListArr.add(new DistributionModel("Distribution3", "123456783"));
//        distributionListArr.add(new DistributionModel("Distribution4", "123456784"));
//        distributionListArr.add(new DistributionModel("Distribution5", "123456785"));
//        distributionListArr.add(new DistributionModel("Distribution6", "123456786"));
        //

/******************************************************************************************************************/
        //Check network connection and data available in local or not
        //If not call the services
//        if (Utility.isNetworkAvailable(this)) {
//            //Service calls for Distribution and DTR Data
//            if (poleDB.getSectionCount(sectionCode,"DIS") > 0) {
//                Log.d("DATA","Already Avilable");
//                bindDistributionData();
//            } else {
//                //Call services
//                getDistributionWS(sectionCode);
//            }
//            if (poleDB.getSectionCount(sectionCode,"DTR") > 0) {
//                Log.d("DATA","Already Avilable");
//                bindDtrData();
//            } else {
//                //Call services
//                getDtrWS(sectionCode);
//            }
//
//        } else {
//            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
//        }

        //Get checked value
        if (rg_pole_uscno_check.getCheckedRadioButtonId() != -1) {
            // hurray at-least on radio button is checked.
            int checkedVal = rg_pole_uscno_check.getCheckedRadioButtonId();
            RadioButton addDataButton = (RadioButton) findViewById(checkedVal);
            checkedStr = addDataButton.getText().toString();
        } else {
            // pls select at-least one radio button.. since id is -1 means no button is check
            checkedStr = "";
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utility.isNetworkAvailable(this)) {
            //Service calls for Distribution and DTR Data
            if (poleDB.getSectionCount(sectionCode, "DIS") > 0) {
                Log.d("DATA", "Already Avilable");
                bindDistributionData();
                bindDtrData();
                getPoleUscNoWS(sectionCode);
                //Upload Service Call
                try {
                    if (!UploadService.isIntentServiceRunning) {
                        Intent intent = new Intent(this, UploadService.class);
                        intent.setAction(ACTION_UPLOAD);
                        startService(intent);
                    }
                } catch (Exception e) {

                }
            } else {
                //Call services
                getDistributionWS(sectionCode);
            }

        } else {
            bindDistributionData();
            bindDtrData();
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    //RB Data add checked
    public void onaddDataCheckClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.rb_dtr_data:
                if (checked) {
                    strAccuracy = "";
                    startLocationUpdates();
                    layoutAdd.removeAllViews();
                    ll_dtr_data.setVisibility(View.VISIBLE);
                    ll_pole_data.setVisibility(View.GONE);
                    ll_uscno_data.setVisibility(View.GONE);
                    addUscnoBtn.setVisibility(View.GONE);
                    checkedStr = "DTR";
                }
                break;
            case R.id.rb_pole_data:
                if (checked) {
                    strAccuracy = "";
                    startLocationUpdates();
                    layoutAdd.removeAllViews();
                    ll_dtr_data.setVisibility(View.GONE);
                    ll_pole_data.setVisibility(View.VISIBLE);
                    ll_uscno_data.setVisibility(View.GONE);
                    addUscnoBtn.setVisibility(View.GONE);
                    checkedStr = "Pole";
                }
                break;
            case R.id.rb_uscno_data:
                if (checked) {
                    ll_dtr_data.setVisibility(View.GONE);
                    ll_pole_data.setVisibility(View.GONE);
                    ll_uscno_data.setVisibility(View.VISIBLE);
                    addUscnoBtn.setVisibility(View.VISIBLE);
                    checkedStr = "USCNO";
                    ArrayList<PoleModel> poleListLDB = poleDB.getPoleData(sSelectedDtrCode);
                    bindPoleData(poleListLDB);
                    addUsc();
                    //addUscData();
                }
                break;
        }
    }

    //    Bind Distrubution data
    private void bindDistributionData() {
        //Add String meterMakeStrArray to meterMakeArrayList
        distributionList = new ArrayList<>();
        distributionList.add("--Select--");
        ArrayList<DistributionModel> distributionListLDB = poleDB.getDistributionData(sectionCode);
        for (DistributionModel distribution : distributionListLDB) {
            distributionList.add(distribution.getDistributionName());
        }
        //String[] meterMakeStrArray = getResources().getStringArray(R.array.distributions);
        //distributionList = new ArrayList<>(Arrays.asList(meterMakeStrArray));
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, distributionList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_distribution.setAdapter(adapter);
        spn_distribution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSelectedDisCode = (String) distributionList.get(position);
                if (!sSelectedDisCode.equalsIgnoreCase("--Select--")) {
                    DistributionModel obj = distributionListLDB.get(position - 1);
                    sSelectedDisCode = obj.getDistributionCode();
                    sSelectedDisName = obj.getDistributionName();
                } else {
                    sSelectedDisCode = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //    Bind DTR data
    private void bindDtrData() {
        //Add String meterMakeStrArray to meterMakeArrayList
        dtrList = new ArrayList<>();
        dtrList.add("--Select--");
        ArrayList<DTRModel> dtrListLDB = poleDB.getDTRData(sectionCode);
        for (DTRModel dtr : dtrListLDB) {
            dtrList.add(dtr.getDTRCode()+"-"+dtr.getDTRName());
        }
//        String[] meterMakeStrArray = getResources().getStringArray(R.array.dtr);
//        dtrList = new ArrayList<>(Arrays.asList(meterMakeStrArray));
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, dtrList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_dtr.setAdapter(adapter);
        spn_dtr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSelectedDtrCode = (String) dtrList.get(position);

                if (!sSelectedDtrCode.equalsIgnoreCase("--Select--")) {

                    DTRModel obj = dtrListLDB.get(position - 1);
                    sSelectedDtrCode = obj.getDTRCode();
                    sSelectedDtrName = obj.getDTRName();
                    String sLastPoleNo = poleDB.getlastPoleNo(sSelectedDtrCode);
                    if (!sLastPoleNo.equalsIgnoreCase("")) {
                        int ilastPoleNo = Integer.parseInt(sLastPoleNo.substring(1)) + 1;
                        et_polelno.setText("P" + ilastPoleNo);
                    } else {
                        et_polelno.setText("P" + 0);
                    }
                    int pCount = poleDB.getPoleDataCount(sSelectedDtrCode);
                    ArrayList<PoleModel> poleListLDB = poleDB.getPoleData(sSelectedDtrCode);
                    if (checkedStr.equalsIgnoreCase("USCNO") && pCount > 0) {

                        bindPoleData(poleListLDB);
                    } else if (checkedStr.equalsIgnoreCase("USCNO") && pCount == 0) {
                        Toast.makeText(getApplicationContext(), "Please enter pole data for " + sSelectedDtrCode+ " first.", Toast.LENGTH_LONG).show();
                        poleListLDB.clear();
                        bindPoleData(poleListLDB);
                    }
                } else {
                    sSelectedDtrCode = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //    Bind Pole data
    private void bindPoleData(ArrayList<PoleModel> poleListLDB) {
        //Add String meterMakeStrArray to meterMakeArrayList
        poleList = new ArrayList<>();
        poleList.add("--Select--");
        if (poleListLDB.size() > 0) {
            for (PoleModel dtr : poleListLDB) {
                poleList.add(dtr.getPoleName());
            }
        }
//        String[] meterMakeStrArray = getResources().getStringArray(R.array.dtr);
//        dtrList = new ArrayList<>(Arrays.asList(meterMakeStrArray));
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, poleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_pole_no.setAdapter(adapter);
        spn_pole_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSelectedPoleCode = (String) poleList.get(position);

                if (!sSelectedPoleCode.equalsIgnoreCase("--Select--")) {
                    PoleModel obj = poleListLDB.get(position - 1);
                    sSelectedPoleCode = obj.getPoleCode();
                    sSelectedPoleName = obj.getPoleName();
                    addUscData(sSelectedDisCode,sSelectedDtrCode, sSelectedPoleCode);
                } else {
                    sSelectedPoleCode = "";
                    addUsc();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        //DTR data Save Fun
        if (view == saveDtrDataBtn) {
            if (!isLocationEnabled()) {
                enableLocationSettings();
            } else {
                if (sSelectedDisCode.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please select Distribution Name", Toast.LENGTH_LONG).show();
                    return;
                } else if (sSelectedDtrCode.equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(), "Please select DTR Name", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    poleDB.updateDtrdata(sectionCode, sSelectedDtrCode, strLat, strLon);
                    saveDtrDataBtn.setEnabled(false);
                    saveDtrDataBtn.setClickable(false);
                    saveDtrDataBtn.setBackgroundColor(Color.GRAY);
                    Toast.makeText(getApplicationContext(), "DTR Coordinates Saved Successfully.", Toast.LENGTH_LONG).show();
                }

            }
        }
        //Pole data Save fun
        if (view == savePoleDataBtn) {
            int dtrCount = poleDB.getDtrDataCount(sectionCode, sSelectedDtrCode);
            if (dtrCount <= 0) {
                Toast.makeText(getApplicationContext(), "Please capture DTR Coordinate first", Toast.LENGTH_LONG).show();
                return;
            }
            //If Pole data save Button click is Submit
            sPoleNo = et_polelno.getText().toString();
            sPoleName = et_pole_name.getText().toString();
            //startLocationUpdates();
            if (sSelectedDisCode.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select Distribution Name", Toast.LENGTH_LONG).show();
                return;
            } else if (sSelectedDtrCode.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select DTR Name", Toast.LENGTH_LONG).show();
                return;
            } else if (sPoleNo.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please enter Pole Number", Toast.LENGTH_LONG).show();
                return;
            } else if (sPoleName.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please enter Pole Name", Toast.LENGTH_LONG).show();
                return;
            } else {
                //
//                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                    OnGPS();
//                } else {
                if (!isLocationEnabled()) {
                    enableLocationSettings();
                } else {
                    //getLocationUpdates();
                    boolean bInsertStatus = poleDB.insertPoleData(sSelectedDisCode, sSelectedDtrCode, sPoleNo, sPoleName, strLat, strLon);
                    if (bInsertStatus) {
                        Toast.makeText(getApplicationContext(), "Pole Data Saved Successfully.", Toast.LENGTH_LONG).show();
                        String sLastPoleNo = poleDB.getlastPoleNo(sSelectedDtrCode);
                        if (!sLastPoleNo.equalsIgnoreCase("")) {
                            int ilastPoleNo = Integer.parseInt(sLastPoleNo.substring(1)) + 1;
                            et_polelno.setText("P" + ilastPoleNo);
                        } else {
                            et_polelno.setText("P" + 0);
                        }
                        //et_polelno.setText("");
                        et_pole_name.setText("");
                        savePoleDataBtn.setEnabled(false);
                        savePoleDataBtn.setClickable(false);
                        savePoleDataBtn.setBackgroundColor(Color.GRAY);
                    } else {

                    }

                    //stopLocationUpdates();
                }
            }
        }
        //If USCNO save Button click is Submit
        if (view == addUscnoBtn) {
            int dtrCount = poleDB.getDtrDataCount(sectionCode, sSelectedDtrCode);
            if (dtrCount <= 0) {
                Toast.makeText(getApplicationContext(), "Please first add DTR Data.", Toast.LENGTH_LONG).show();
                return;
            }
            if (sSelectedDisCode.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select Distribution Name", Toast.LENGTH_LONG).show();
                return;
            } else if (sSelectedDtrCode.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select DTR Name", Toast.LENGTH_LONG).show();
                return;
            } else if (sSelectedPoleCode.equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please select Pole Name", Toast.LENGTH_LONG).show();
                return;
            } else {
                //Get the selected pole data
                ArrayList<PoleModel> poleData = poleDB.getSinglePoleData(sSelectedDtrCode, sSelectedPoleCode);
                ArrayList<DTRModel> dtrModelData = poleDB.getSingleDtrData(sectionCode, sSelectedDtrCode);
                String sPoleCode = "";
                String sPoleName = "";
                String sPoleLat = "";
                String sPoleLong = "";
                String finalData = "";
                String sDTRLat = "";
                String sDTRLong = "";
                JSONObject poleDataObject = null;
                if (poleData.size() > 0) {
                    sPoleCode = poleData.get(0).getPoleCode();
                    sPoleName = poleData.get(0).getPoleName();
                    sPoleLat = poleData.get(0).getPoleLat();
                    sPoleLong = poleData.get(0).getPoleLong();
                }
                //Dtr Data local db
                if (dtrModelData.size() > 0) {
                    sDTRLat = dtrModelData.get(0).getDTRLat();
                    sDTRLong = dtrModelData.get(0).getDTRLong();
                }

                //Array variables
                ArrayList<String> uscnoArryList = new ArrayList<>();
                JSONArray uscNoArray = new JSONArray();
                JSONArray serviceNoArray = new JSONArray();
                //Get the all entered uscno data in arrays
                if (layoutAdd.getChildCount() > 0) {
                    View childView = null;
                    EditText editText = null;
                    String scNo = "";
                    try {

                        for (int i = 1; i <= layoutAdd.getChildCount(); i++) {
                            childView = layoutAdd.findViewWithTag(i);
                            if (childView != null) {
                                editText = childView.findViewById(R.id.editVal);
                                if (editText != null && !editText.getText().toString().equalsIgnoreCase("")) {
                                    Log.d("USC " + i, editText.getText().toString());
                                    int number = Integer.valueOf(editText.getText().toString());
                                    scNo = String.format("%06d", number);
                                    uscnoArryList.add(scNo);
                                    uscNoArray.put(scNo);
                                    scNo = sSelectedDisCode + scNo;
                                    serviceNoArray.put(scNo);
                                }
                            }
                        }
                        if (isDuplicate(serviceNoArray)) {
                            Toast.makeText(getApplicationContext(), "Same USCNO provided", Toast.LENGTH_LONG).show();
                            scrollView.post(new Runnable() {
                                public void run() {
                                    scrollView.scrollTo(0, 0);
                                }
                            });
                            return;
                        }

                        finalData = sectionCode + "|" + sectionName + "|" + sSelectedDisCode + "|" + sSelectedDisName + "|" + sSelectedDtrCode + "|" + sSelectedDtrName
                                + "|" + sPoleName + "|" + sPoleCode + "|" + sPoleLat + "-" + sPoleLong + "|" + userName + "|" + serviceNoArray.toString()+ "|" + sDTRLat + "|" + sDTRLong;
                    } catch (Exception e) {

                    }
                    try {
                        poleDataObject = new JSONObject();
                        poleDataObject.put("DISTRIBUTIONCODE", sSelectedDisCode);
                        poleDataObject.put("DTRCODE", sSelectedDtrCode);
                        poleDataObject.put("POLECODE", sPoleCode);
                        poleDataObject.put("POLENAME", sPoleName);
                        poleDataObject.put("POLE_USCNOS_DATA", uscNoArray.toString());
                        poleDataObject.put("POLE_SERVICENUMBER_DATA", serviceNoArray.toString());
                    } catch (Exception e) {

                    }
                    RequestParams params = new RequestParams();
                    params.put("DATA", finalData);
                    //Get pole data avilable or not
                    int val = poleDB.recordIsAvilable(sSelectedDtrCode, sPoleCode);

                    if (val > 0) {
                        poleDB.updateUSCNOData(sSelectedDtrCode, sPoleCode, finalData, poleDataObject);
                        poleDB.updateStatus(sSelectedDtrCode, sPoleCode, "0");
                        try {
                            if (Utility.isNetworkAvailable(this)) {
                                if (!UploadService.isIntentServiceRunning) {
                                    Intent intent = new Intent(this, UploadService.class);
                                    intent.setAction(ACTION_UPLOAD);
                                    startService(intent);
                                }
                                //uploadPoleData(params);
                            }
                        } catch (Exception e) {

                        }
                    } else {
                        poleDB.insertUSCNOData(userName, sSelectedDisCode, sSelectedDtrCode, sPoleCode, sPoleName, finalData, poleDataObject);
                        try {
                            if (Utility.isNetworkAvailable(this)) {
                                if (!UploadService.isIntentServiceRunning) {
                                    Intent intent = new Intent(this, UploadService.class);
                                    intent.setAction(ACTION_UPLOAD);
                                    startService(intent);
                                }
                                //uploadPoleData(params);
                            }
                        } catch (Exception e) {

                        }
                    }
                    Toast.makeText(getApplicationContext(), "Service Numbers Saved Successfully.", Toast.LENGTH_LONG).show();
                    addUsc();
//                addUscData(sSelectedDtrCode,sSelectedPoleCode);
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.scrollTo(0, 0);
                        }
                    });
                }
            }
        }
        //Location image Clicking
        if (view == location_imv) {
            strAccuracy = "";
            startLocationUpdates();
        }
        //Location image Clicking
        if (view == location_dtr_imv) {
            strAccuracy = "";
            startLocationUpdates();
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(
                    AddPoleData.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    AddPoleData.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_CHECK_SETTINGS);
            } else {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) {
                    double lat = locationGPS.getLatitude();
                    double longi = locationGPS.getLongitude();
                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(longi);
//                showLocation.setText("Your Location: " + "
//                        " + "Latitude: " + latitude + "
//                " + "Longitude: " + longitude);
                    Toast.makeText(this, "Your Location: Latitude: " + latitude + " Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Add USCNO layout
    private void addUsc() {
        layoutAdd.removeAllViews();
        for (int i = 1; i <= 30; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.add_uscno_item, layoutAdd, false);

            TextView textView = view.findViewById(R.id.textField);
            EditText editText = view.findViewById(R.id.editVal);

            textView.setText("USCNO " + i);
            //editText.setHint("USCNO " + i);
            editText.setTag(i);

            layoutAdd.addView(view);
        }
    }

    //Add USCNO layout
    private void addUscData(String distCode,String dCode, String pCode) {

        layoutAdd.removeAllViews();
        //POLE_USCNOS_DATA

        try {
            JSONArray arrayData = new JSONArray();

            String sUscnoData = poleDB.getPoleUscnoData(distCode,dCode, pCode);
            if (!sUscnoData.equalsIgnoreCase("")) {

                JSONObject jsonObject = new JSONObject(sUscnoData);
                String s1 = jsonObject.getString("POLE_USCNOS_DATA");
                arrayData = new JSONArray(s1);


                Log.d("ABCD", arrayData.toString());
            } else {
                sUscnoData = "";
            }
            ArrayList<String> listdata = new ArrayList<>();
            if (arrayData != null) {

                //Iterating JSON array
                for (int i = 0; i < arrayData.length(); i++) {

                    //Adding each element of JSON array into ArrayList
                    listdata.add(arrayData.getString(i));
                }
            }
//        JSONObject object = new JSONObject(sUscnoData);
//        JSONArray array= new JSONObject(data).getJSONArray("result");
            for (int i = 1; i <= 30; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.add_uscno_item, layoutAdd, false);

                TextView textView = view.findViewById(R.id.textField);
                EditText editText = view.findViewById(R.id.editVal);

                textView.setText("USCNO " + i);
                //editText.setHint("USCNO " + i);
                if (!sUscnoData.equalsIgnoreCase("")) {
                    if (i <= arrayData.length())
                        editText.setText(arrayData.get(i - 1).toString());
                }
                editText.setTag(i);

                layoutAdd.addView(view);
            }
        } catch (JSONException err) {
            Log.d("Error", err.toString());
        }
    }

//    public void onSave(View view) {

//        if (layoutAdd.getChildCount() > 0) {
//            View childView = null;
//            EditText editText = null;
//            for (int i = 1; i <= layoutAdd.getChildCount(); i++) {
//                childView = layoutAdd.findViewWithTag(i);
//                if (childView != null) {
//                    editText = childView.findViewById(R.id.editVal);
//                    if (editText != null)
//                        Log.d("USC " + i, editText.getText().toString());
//                }
//            }
//            addUsc();
//            scrollView.post(new Runnable() {
//                public void run() {
//                    scrollView.scrollTo(0, 0);
//                }
//            });
//        }
//    }

//    public String generatePoleNo() {
//
//        int nSerialNo = poleSPreferences.getInt("POLENO", 0) + 1;
//        String sPN = "P" + String.valueOf(nSerialNo);
////        String strSerial = String.format("%0" + 4 + "d", (nSerialNo));
//        return sPN;
//    }

    /***************************************Location*****************************************/
    private void startLocationUpdates() {
        try {
            if (isLocationEnabled())
                getLocationUpdates();
            else {

                enableLocationSettings();

            }
        } catch (Exception e) {
        }
    }

    //Location
    private void enableLocationSettings() {

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(LOCATION_UPDATE_INTERVAL)
                .setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices
                .getSettingsClient(this)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(this, (LocationSettingsResponse response) -> {
                    // startUpdatingLocation(...);
                    Toast.makeText(this, "Your Location: response: " + response.toString(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(this, ex -> {
                    if (ex instanceof ResolvableApiException) {
                        // Location settings are NOT satisfied,  but this can be fixed  by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),  and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) ex;
                            resolvable.startResolutionForResult(AddPoleData.this, REQUEST_CODE_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                });
    }

    private void getLocationUpdates() {
        // This registers messageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, new IntentFilter("my-location"));

        Intent startServiceIntent = new Intent(this, LocationService.class);
        startServiceIntent.setAction(Constants.START_FOREGROUND_ACTION);
        //startServiceIntent.putExtra("MESSENGER", new Messenger(messageHandler));
        startService(startServiceIntent);
    }

    // Handling the received Intents for the "my-location" event
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            strLat = intent.getStringExtra("latitude");
            strLon = intent.getStringExtra("longitude");
            strAccuracy = intent.getStringExtra("accuracy");
            if (checkedStr.equalsIgnoreCase("Pole")) {
                tv_location_accuracy.setText(strAccuracy);
            } else if (checkedStr.equalsIgnoreCase("DTR")) {
                tv_dtr_location_accuracy.setText(strAccuracy);
                tv_dtr_lat.setText(strLat);
                tv_dtr_long.setText(strLon);
            }
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            if (!strAccuracy.equalsIgnoreCase("")) {
                if (Double.parseDouble(strAccuracy) < 15) {
                    if (checkedStr.equalsIgnoreCase("Pole")) {
                        savePoleDataBtn.setEnabled(true);
                        savePoleDataBtn.setClickable(true);
                        savePoleDataBtn.setBackgroundColor(Color.BLUE);
                    } else if (checkedStr.equalsIgnoreCase("DTR")) {
                        saveDtrDataBtn.setEnabled(true);
                        saveDtrDataBtn.setClickable(true);
                        saveDtrDataBtn.setBackgroundColor(Color.BLUE);
                    }
                    LocalBroadcastManager.getInstance(AddPoleData.this).unregisterReceiver(messageReceiver);
                    stopLocationUpdates();
                } else {
                    if (checkedStr.equalsIgnoreCase("Pole")) {
                        savePoleDataBtn.setEnabled(false);
                        savePoleDataBtn.setClickable(false);
                        savePoleDataBtn.setBackgroundColor(Color.GRAY);
                    } else if (checkedStr.equalsIgnoreCase("DTR")) {
                        saveDtrDataBtn.setEnabled(false);
                        saveDtrDataBtn.setClickable(false);
                        saveDtrDataBtn.setBackgroundColor(Color.GRAY);
                    }
                }
            }
        }
    };

//    public class MessageHandler extends Handler {
//        @Override
//        public void handleMessage(Message message) {
//            Bundle bundle = message.getData();
//            if (bundle != null) {
//                strLat = bundle.getString("latitude");
//                strLon = bundle.getString("longitude");
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(AddPoleData.this).unregisterReceiver(messageReceiver);

        stopLocationUpdates();
    }


    public void stopLocationUpdates() {
        try {
            Intent stopServiceIntent = new Intent(this, LocationService.class);
            stopServiceIntent.setAction(Constants.STOP_FOREGROUND_ACTION);
            startService(stopServiceIntent);
        } catch (Exception e) {

        }
    }


    private boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
//            if (Activity.RESULT_OK == resultCode) {
//                //user clicked OK, you can startUpdatingLocation(...);
//                getLocationUpdates();
//            } else {
//                //user clicked cancel: informUserImportanceOfLocationAndPresentRequestAgain();
//                //showToast(getString(R.string.location_required), 2);
//                enableLocationSettings();
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    /*********************************************************************************************/
    /*************************************Section Based Distribution List ************************************************/
    //Service Call for Distribution finding
    private void getDistributionWS(String sectionCode) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        JSONObject paramObject = new JSONObject();
        try {
            paramObject.put("SECTION_CODE", sectionCode);
            paramObject.put("SERVICE_TYPE", "NS");
        } catch (JSONException e) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            e.printStackTrace();
        }
        try {
            StringEntity entity = new StringEntity(paramObject.toString());
            AsyncHttpClient client = new AsyncHttpClient();

            client.post(this, "http://112.133.252.110:8080/SBWS/rest/mobilebillingservices/pc_sbm/datafetch", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("STATUS").equalsIgnoreCase("TRUE")) {
                            JSONObject responseObj = jsonObject.getJSONObject("RESPONSE");
                            if (responseObj != null && responseObj.length() > 0) {
                                JSONObject typeObj = responseObj.getJSONObject("TYPE");
                                if (typeObj != null && typeObj.length() > 0) {
                                    JSONArray distributionArr = typeObj.getJSONArray("DISTRIBUTION");
                                    if (distributionArr != null && distributionArr.length() > 0) {
                                        String strItem = "";
                                        String[] values = null;
                                        hashDistribution = new HashMap<>();
                                        distributionListArr = new ArrayList<>();

                                        for (int i = 0; i < distributionArr.length(); i++) {
                                            strItem = distributionArr.getString(i);
                                            if (strItem != null && strItem.contains("|")) {
                                                values = strItem.split("\\|");
                                                if (values != null && values.length >= 2) {
                                                    DistributionModel distributionModel = new DistributionModel(values[1], values[0]);
                                                    hashDistribution.put(values[1], distributionModel);
                                                    distributionListArr.add(distributionModel);
                                                }
                                            }
                                        }
                                        boolean bInsertStatus = poleDB.insertDistributionData(distributionListArr, sectionCode);
                                        if (bInsertStatus) {
                                            bindDistributionData();
                                            if (Utility.isNetworkAvailable(getApplicationContext())) {
                                                getDtrWS(sectionCode);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Locally data insertion problem", Toast.LENGTH_LONG).show();
                                        }


                                    }

                                }
                            }
                        }
                    } catch (Exception e) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        e.printStackTrace();
                        Utility.showCustomOKOnlyDialog(AddPoleData.this, e.getLocalizedMessage());
                    }
                }


                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(AddPoleData.this, error.getLocalizedMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            e.printStackTrace();
        }
    }

    //Service Call for DTR data feaching
    private void getDtrWS(String sectionCode) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        RequestParams params = new RequestParams();
        params.put("SEC_CODE", sectionCode);
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + "PoleDetails/DTR", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("STATUS").equalsIgnoreCase("TRUE") && jsonObject != null) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        poleSPreferences = getSharedPreferences("POLEPref", MODE_PRIVATE);
                        polePrefsEditor = poleSPreferences.edit();
                        polePrefsEditor.putString("SECTIONNAME", jsonObject.getString("SECTION_NAME"));
                        polePrefsEditor.apply();
                        tv_section_code.setText(jsonObject.getString("SECTION_NAME"));
                        sectionName = jsonObject.getString("SECTION_NAME");
                        dtrListArr = new ArrayList<>();
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                // store each object in JSONObject
                                JSONObject explrObject = jsonArray.getJSONObject(i);

                                // get field value from JSONObject using get() method
                                //System.out.println(explrObject.get("DTSTRCD"));

                                dtrListArr.add(new DTRModel(explrObject.get("DTSTRCD").toString(), explrObject.get("DTSTRLOCATION").toString()));
                            }
                            //System.out.println(dtrListArr.toString());
                            boolean bInsertStatas = poleDB.insertDTRData(dtrListArr, sectionCode, "", "");
                            if (bInsertStatas) {
                                bindDtrData();
                                if (Utility.isNetworkAvailable(getApplicationContext())) {
                                    getPoleUscNoWS(sectionCode);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Locally data insertion problem", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                } catch (Exception e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    e.printStackTrace();
                    Utility.showCustomOKOnlyDialog(AddPoleData.this, e.getLocalizedMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(AddPoleData.this, error.getLocalizedMessage());
            }
        });
    }

    //Service Call for Live Polewith Uscno data feaching
    private void getPoleUscNoWS(String sectionCode) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        RequestParams params = new RequestParams();
        params.put("SEC_CODE", sectionCode);
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + "PoleDetails/PoleDetailFetch", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    JSONObject jsonObject = new JSONObject(response);
                    String sResSectionCode;
                    String sResSectionName;
                    String sResDisCode;
                    String sResDisName;
                    String sResDtrCode;
                    String sResDtrLat;
                    String sResDtrLong;
                    String sResDtrName;
                    String sResPoleName;
                    String sResPoleCode;
                    String sResPoleLat;
                    String sResPoleLong;
                    String sResfinalData;
                    JSONArray arrJUscNo = new JSONArray();
                    JSONObject objRespoleDataObject;
                    JSONArray finalUscno;
                    ArrayList<DTRModel> dtrResDataModel;

                    if (jsonObject.getString("STATUS").equalsIgnoreCase("TRUE") && jsonObject != null) {

                        for (int i = 0; i < jsonObject.length(); i++) {
                            sResSectionCode = jsonObject.optString("SECTION_CODE");
                            sResSectionName = jsonObject.optString("SECTION_NAME");
                            sResDisCode = jsonObject.optString("DIST_CODE");
                            sResDisName = jsonObject.optString("DIST_NAME");
                            sResDtrCode = jsonObject.optString("DTR_CODE");
                            sResDtrLat = jsonObject.optString("DTR_LAT");
                            sResDtrLong = jsonObject.optString("DTR_LONG");
                            sResDtrName = jsonObject.optString("DTR_NAME");
                            sResPoleName = jsonObject.optString("POLE_NAME");
                            sResPoleCode = jsonObject.optString("POLE_NO");
                            String poleLatLong = jsonObject.optString("LAT_LONG");
                            String[] arrOfStr = poleLatLong.split("-");
                            sResPoleLat = arrOfStr[0];
                            sResPoleLong = arrOfStr[1];
                            String sArrJUscNo = jsonObject.getString("USCNO");
                            arrJUscNo = new JSONArray(sArrJUscNo);
                            finalUscno = new JSONArray();
                            for (int j = 0; j < arrJUscNo.length(); j++) {
                                finalUscno.put(arrJUscNo.get(j).toString().substring(7, 13));
                            }

                            sResfinalData = sResSectionCode + "|" + sResSectionName + "|" + sResDisCode + "|" + sResDisName + "|" + sResDtrCode + "|" + sResDtrName
                                    + "|" + sResPoleName + "|" + sResPoleCode + "|" + sResPoleLat + "-" + sResPoleLong + "|" + userName + "|" + arrJUscNo.toString()+ "|" + sResDtrLat+ "|" + sResDtrLong;


                            objRespoleDataObject = new JSONObject();
                            objRespoleDataObject.put("DISTRIBUTIONCODE", sResDisCode);
                            objRespoleDataObject.put("DTRCODE", sResDtrCode);
                            objRespoleDataObject.put("POLECODE", sResPoleCode);
                            objRespoleDataObject.put("POLENAME", sResPoleName);
                            objRespoleDataObject.put("POLE_USCNOS_DATA", finalUscno.toString());
                            objRespoleDataObject.put("POLE_SERVICENUMBER_DATA", arrJUscNo.toString());
                            dtrResDataModel = new ArrayList<>();
                            dtrResDataModel.add(new DTRModel(sResDtrCode, sResDtrCode));
                            //Data Insertion
                            int val = poleDB.recordIsAvilable(sResDtrCode, sResPoleCode);

                            if (val > 0) {
                                poleDB.updateUSCNOData(sResDtrCode, sResPoleCode, sResfinalData, objRespoleDataObject);
                                //poleDB.updateStatus(sResDtrCode, sResPoleCode, "0");
                            } else {
                                poleDB.insertDTRData(dtrResDataModel, sResSectionCode, sResDtrLat, sResDtrLong);
                                poleDB.insertUSCNOData(userName, sResDisCode, sResDtrCode, sResPoleCode, sResPoleName, sResfinalData, objRespoleDataObject);
                                poleDB.insertPoleData(sResDisCode, sResDtrCode, sResPoleCode, sResPoleName, sResPoleLat, sResPoleLong);
                            }
                        }
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//                        dtrListArr = new ArrayList<>();
//                        if (jsonArray != null && jsonArray.length() > 0) {
//                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "New Section", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    e.printStackTrace();
                    Utility.showCustomOKOnlyDialog(AddPoleData.this, e.getLocalizedMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(AddPoleData.this, error.getLocalizedMessage());
            }
        });
    }

    /*********************************************************************************************************************/
    private void uploadPoleData(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + "PoleDetails/savePole", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");

                    if (status.equals("TRUE")) {
                        Toast.makeText(getApplicationContext(), "Pole Data Saved Successfully.", Toast.LENGTH_LONG).show();
                        poleDB.updateStatus(sSelectedDtrCode, sSelectedPoleCode, "1");
                    } else if (status.equals("ERROR")) {

                        Toast.makeText(getApplicationContext(), "Pole Data Already Submitted / Failed to Save Pole Data", Toast.LENGTH_LONG).show();

                    } else if (status.equalsIgnoreCase("FALSE")) {

                        Toast.makeText(getApplicationContext(), "Pole Data Already Submitted / Failed to Save Pole Data", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Utility.showCustomOKOnlyDialog(context, e.getLocalizedMessage());
                    Log.d("Error", "exception " + e.getLocalizedMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                //Utility.showCustomOKOnlyDialog(context, error.getLocalizedMessage());
                Log.d("Error", "exception " + error.getLocalizedMessage());
            }
        });
    }

    /*********************************************************************************************************************/
    public boolean isDuplicate(JSONArray uscNoArry) {
        try {
            for (int i = 0; i < uscNoArry.length(); i++) {
                for (int j = i + 1; j < uscNoArry.length(); j++) {

                    if (uscNoArry.get(i).equals(uscNoArry.get(j))) {
                        // got the duplicate element
                        return true;
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;

    }
}