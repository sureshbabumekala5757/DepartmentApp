package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckReadingActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    EditText et_servicenum, et_checkdate, et_kvah, et_kwh, et_distribution_code, et_scno, et_meterslno, et_meterseal1, et_meterseal2;
    TextView tv_consumername, tv_cat, tv_type, tv_date, tv_distribution_name, tv_section_name, tv_kwh,tv_kvah;
    Button submitbtn, calendarbtn, savebtn;
    LinearLayout whole_Layout;
    RelativeLayout ll_kwh, ll_kvah, rl_correctcategory;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Spinner statusSpinner, spn_meter_make, spn_correct_category;
    String sec_code, str_servicenumber, upToNCharacters, msg, str_section_name, str_distribution_name, str_name, str_cat, str_billingtype,
            str_billdate, str_kwhreading, str_kvhreading, strStatus, str_kwh, str_kvah, strTCodeValue, userName,userID, str_Status,sPhase;
    LinearLayout ll_fetch_location;
    RelativeLayout rl_latitude;
    RelativeLayout rl_longitude;
    TextView tv_latitude;
    TextView tv_longitude;
    private ArrayList<String> meterMakeList;
    private ArrayList<String> meterCategoryList;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private GoogleMap mMap;
    private LinearLayout ll_map;
    private String strLat = "0";
    private String strLong = "0";
    private String from = "";
    private String selectedMeterMakeStr, selectedcategoryStr, supplyutilStr, meterLocationStr, theftMalStr, adlVvailableStr, irdaStr;
    ImageView ctMeter_iv;
    Bitmap photo_CTM;
    String base64_ctmimg;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    RadioGroup rg_meter_location, rg_adl_available, rg_theft_mal, rg_supply_util, rg_irda;
    ContentValues values;
    Uri imageUri;
    String imageurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_checkreading_activity);
        ctMeter_iv = findViewById(R.id.ctMeter_iv);
        init();
    }

    //Capture the CTMeter Image
    public void captureCTMImg(View view) {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, 100);
            values = new ContentValues();
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


//        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
//            photo_CTM = (Bitmap) data.getExtras().get("data");
//            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
//                    photo_CTM, 450,600, true);
//            ctMeter_iv.setImageBitmap(resizedBitmap);
//            base64_ctmimg = ImageUtil.convert(resizedBitmap);

//        }


        if (requestCode == 100)
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    ctMeter_iv.setImageBitmap(thumbnail);
                    imageurl = getRealPathFromURI(imageUri);

                    Bitmap bm = BitmapFactory.decodeFile(imageurl);

                    bm = Bitmap.createScaledBitmap(
                            bm, 1024, 768, false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    base64_ctmimg = Base64.encodeToString(b, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /*Initialize Views*/
    private void init() {
        from = getIntent().getStringExtra(Constants.FROM);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        buildGoogleApiClient();
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);
        pDialog = new ProgressDialog(this);
        et_servicenum = (EditText) findViewById(R.id.et_servicenum);
        String cDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        et_distribution_code = findViewById(R.id.et_distribution_code);
        et_scno = findViewById(R.id.et_scno);

        et_checkdate = (EditText) findViewById(R.id.et_checkdate);
        et_kvah = (EditText) findViewById(R.id.et_kvah);
        et_kwh = (EditText) findViewById(R.id.et_kwh);
        et_meterslno = findViewById(R.id.et_meterslno);
        tv_distribution_name = findViewById(R.id.tv_distribution_name);
        tv_section_name = findViewById(R.id.tv_section_name);
        tv_consumername = (TextView) findViewById(R.id.tv_consumername);
        tv_cat = (TextView) findViewById(R.id.tv_cat);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_latitude = (TextView) findViewById(R.id.tv_latitude);
        tv_longitude = (TextView) findViewById(R.id.tv_longitude);
        tv_kwh = (TextView) findViewById(R.id.tv_kwh);
        tv_kvah = (TextView) findViewById(R.id.tv_kvah);
        ll_fetch_location = (LinearLayout) findViewById(R.id.ll_fetch_location);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        rl_latitude = (RelativeLayout) findViewById(R.id.rl_latitude);
        rl_longitude = (RelativeLayout) findViewById(R.id.rl_longitude);
        whole_Layout = (LinearLayout) findViewById(R.id.whole_Layout);
        ll_kwh = (RelativeLayout) findViewById(R.id.ll_kwh);
        ll_kvah = (RelativeLayout) findViewById(R.id.ll_kvah);
        submitbtn = (Button) findViewById(R.id.submitbtn);
        calendarbtn = (Button) findViewById(R.id.calendarbtn);
        savebtn = (Button) findViewById(R.id.savebtn);
        statusSpinner = (Spinner) findViewById(R.id.statusspinner);
        spn_meter_make = findViewById(R.id.spn_meter_make);
        //New values added on 18-10-2022
        et_meterseal1 = findViewById(R.id.et_meterseal1);
        et_meterseal2 = findViewById(R.id.et_meterseal2);
        rg_meter_location = findViewById(R.id.rg_meter_location);
        rg_adl_available = findViewById(R.id.rg_adl_available);
        rg_theft_mal = findViewById(R.id.rg_theft_mal);
        rg_supply_util = findViewById(R.id.rg_supply_util);
        rg_irda = findViewById(R.id.rg_irda);
        rl_correctcategory = findViewById(R.id.rl_correctcategory);
        spn_correct_category = (Spinner) findViewById(R.id.spn_correct_category);

        submitbtn.setOnClickListener(this);
        calendarbtn.setOnClickListener(this);
        savebtn.setOnClickListener(this);
        //et_distribution_code.setOnClickListener(this);
        ll_fetch_location.setOnClickListener(this);
//        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
//        sec_code = lprefs.getString("Section_Code", "");// 1, 9, 6, 4
//        userName = lprefs.getString("UserName", "");
//        userID = lprefs.getString("USERID", "");

        userName = AppPrefs.getInstance(getApplicationContext()).getString("USERNAME", "");
        sec_code = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        userID = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        et_checkdate.setText(cDate);

        String circleCode = "1";

        et_distribution_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    if (charSequence.toString().equals("1") || charSequence.toString().equals("4") || charSequence.toString().equals("9") || charSequence.toString().equals("6")) {

                    } else {
                        Utility.showCustomOKOnlyDialog(CheckReadingActivity.this,
                                " Invalid Service Number");
                        et_distribution_code.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


    }

    //RB Supply Util
    public void onRBSupplyUtilClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.supply_util_yes:
                if (checked) {
                    // Pirates are the best
                    //supplyutilStr = "YES";
                    rl_correctcategory.setVisibility(View.GONE);
                }
                break;
            case R.id.supply_util_no:
                if (checked) {
                    // Ninjas rule
                    //supplyutilStr = "NO";
                    rl_correctcategory.setVisibility(View.VISIBLE);
                    bindCategoryData();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (view == calendarbtn) {
            myCalendar = Calendar.getInstance();
            date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }

            };
            DatePickerDialog dpr = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH));
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            dpr.getDatePicker().setMaxDate(System.currentTimeMillis());
            dpr.show();
        }

        if (view == savebtn) {
            if (str_billingtype.equalsIgnoreCase("KVAH") || sPhase.equalsIgnoreCase("3")) {
                str_kwh = et_kwh.getText().toString();
                str_kvah = et_kvah.getText().toString();
                if (!strStatus.equalsIgnoreCase("02") && !strStatus.equalsIgnoreCase("03") && !strStatus.equalsIgnoreCase("05") && !strStatus.equalsIgnoreCase("06") && !strStatus.equalsIgnoreCase("11")) {
                    if (Utils.IsNullOrBlank(str_kwh)) {
                        Toast.makeText(getApplicationContext(), "Please Enter KWH Value", Toast.LENGTH_LONG).show();
                    } else if (Utils.IsNullOrBlank(str_kvah) && !strStatus.equalsIgnoreCase("05")) {
                        Toast.makeText(getApplicationContext(), "Please Enter KVAH Value", Toast.LENGTH_LONG).show();
                    } else if (Float.parseFloat(str_kwhreading) > Float.parseFloat(str_kwh) && Float.parseFloat(str_kvhreading) > Float.parseFloat(str_kvah)) {
                        // callAlert("Please enter both values greater than previous values");
                        callReadingEntryService();
                    } else if (Float.parseFloat(str_kwhreading) > Float.parseFloat(str_kwh)) {
                        // callAlert("Please enter KWH value greater than previous KWH");
                        callReadingEntryService();
                    } else if (Float.parseFloat(str_kvhreading) > Float.parseFloat(str_kvah)) {
                        // callAlert("Please enter KVAH value greater than previous KVAH");
                        callReadingEntryService();
                    } else {
                        callReadingEntryService();
                    }
                } else {
                    callReadingEntryService();
                }
            } else if (str_billingtype.equalsIgnoreCase("KWH") || sPhase.equalsIgnoreCase("1")) {
                str_kvah = "";
                str_kwh = et_kwh.getText().toString();
                if (!strStatus.equalsIgnoreCase("02") && !strStatus.equalsIgnoreCase("03") && !strStatus.equalsIgnoreCase("05") && !strStatus.equalsIgnoreCase("06") && !strStatus.equalsIgnoreCase("11")) {
                    if (Utils.IsNullOrBlank(str_kwh)) {
                        Toast.makeText(getApplicationContext(), "Please Enter KWH Value", Toast.LENGTH_LONG).show();
                    } else if (Float.parseFloat(str_kwhreading) > Float.parseFloat(str_kwh)) {
                        //callAlert("Please enter KWH value greater than previous KWH");
                        callReadingEntryService();
                    } else {
                        callReadingEntryService();
                    }
                } else {
                    callReadingEntryService();
                }

            }
        }
        if (view == submitbtn) {
            String distributionCode = et_distribution_code.getText().toString().trim();
            String scNo = et_scno.getText().toString().trim();
            if (!scNo.equalsIgnoreCase("")) {
                int number = Integer.valueOf(scNo);
                scNo = String.format("%06d", number); //000001
            }
//            str_servicenumber = distributionCode + scNo;
            str_servicenumber = et_servicenum.getText().toString();
            if (Utils.IsNullOrBlank(str_servicenumber)) {
                Toast.makeText(getApplicationContext(), "Please enter Service Number", Toast.LENGTH_LONG).show();
            }
//            else if (str_servicenumber.length() != 13) {
//                Toast.makeText(getBaseContext(), "Enter 13 digit Service Number", Toast.LENGTH_LONG).show();
//            }
            else if (objNetworkReceiver.hasInternetConnection(this)) {
//                RequestParams params = new RequestParams();
//                params.put("T_CODE", str_servicenumber);
//                invokeReadingFetchWebService(params);
                JSONObject requestObj = new JSONObject();
                try {
                    if (str_servicenumber.length() == 10) {
                        requestObj.put("uscno", "");
                        requestObj.put("bp_number", str_servicenumber);
                    } else {
                        requestObj.put("uscno", str_servicenumber);
                        requestObj.put("bp_number", "");
                    }
                    featchReadingdetils(requestObj);
                } catch (Exception e) {

                }


            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();

            }
        }
        if (view == ll_fetch_location) {
            checkPermissionsAndCallServiceToGetLatAndLng();
        }
    }

    private void featchReadingdetils(JSONObject requestObj) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/AEServiceDetails/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    try {
                        JSONObject obj = new JSONObject(responseStr);
                        String status = obj.getString("success");
                        obj = obj.getJSONObject("data");
                        Log.e("Errorfetch", obj.toString());


                        if (status.trim().equals("True")) {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            whole_Layout.setVisibility(View.VISIBLE);
                            str_section_name = obj.getString("section").trim();
                            str_distribution_name = obj.getString("distribution").trim();
                            str_name = obj.getString("name");
                            str_cat = obj.getString("category");
                            str_billingtype = obj.getString("phase");
                            sPhase = obj.getString("phase");
                            str_billdate = obj.getString("last_billed_date");
                            str_kwhreading = obj.getString("premise");
                            str_kvhreading = obj.getString("premise");
                            AppPrefs.getInstance(getApplicationContext()).putString("checkReadingBpno",obj.getString("bpno"));
                            AppPrefs.getInstance(getApplicationContext()).putString("checkReadSNOIsSolar",obj.getString("is_solar"));

                            if (obj.has("LAT")) {
                                strLat = obj.getString("LAT");
                            }
                            if (obj.has("LONG")) {
                                strLong = obj.getString("LONG");
                            }
                            setValues();
                        } else {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        e.printStackTrace();
                        Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, e.getLocalizedMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, error.getLocalizedMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
//    private void invokeReadingFetchWebService(RequestParams params) {
//        pDialog.show();
//        pDialog.setMessage("Please wait...");
//
//        AsyncHttpClient client = new AsyncHttpClient();
//
//        client.post(Constants.URL + Constants.CHECK_READING_DETAILS, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONObject obj = new JSONObject(response);
//                    Log.e("Errorfetch", obj.toString());
//                    String status = obj.getString("STATUS");
//
//                    if (status.equals("TRUE")) {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//                        whole_Layout.setVisibility(View.VISIBLE);
//                        str_section_name = obj.getString("SECNAME");
//                        str_distribution_name = obj.getString("DISNAME");
//                        str_name = obj.getString("NAME");
//                        str_cat = obj.getString("CAT");
//                        str_billingtype = obj.getString("BILLTYPE");
//                        str_billdate = obj.getString("BILLDATE");
//                        str_kwhreading = obj.getString("KWHREADING");
//                        str_kvhreading = obj.getString("KVAHREADING");
//
//                        if (obj.has("LAT")) {
//                            strLat = obj.getString("LAT");
//                        }
//                        if (obj.has("LONG")) {
//                            strLong = obj.getString("LONG");
//                        }
//                        setValues();
//                    } else if (status.equals("ERROR")) {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//                        msg = obj.getString("FLAG1");
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//
//                    } else if (status.equalsIgnoreCase("FALSE")) {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//                        msg = obj.getString("FLAG1");
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//
//                    } else {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
//                    }
//                } catch (Exception e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.dismiss();
//                    }
//                    e.printStackTrace();
//                    Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, e.getLocalizedMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Throwable error, String content) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//                Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, error.getLocalizedMessage());
//            }
//        });
//    }

    private void saveChangeReadingWS(JSONObject requestObj) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/SaveCheckReadingDetails/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    try {
                        JSONObject obj = new JSONObject(responseStr);
                        String status = obj.getString("Success");
                        et_scno.setText("");
                        et_meterslno.setText("");
                        //et_checkdate.setText("");
                        et_kwh.setText("");
                        et_kvah.setText("");
                        et_meterseal1.setText("");
                        et_meterseal2.setText("");

                        if (status.trim().equals("True")) {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            hide_WholeLayout();
                            Toast.makeText(getApplicationContext(), "Check Reading Saved Successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            //msg = obj.getString("FLAG1");
                            Toast.makeText(getApplicationContext(), "Check Reading Already Submitted / Failed to Save Check Reading", Toast.LENGTH_LONG).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, e.getLocalizedMessage());
                    }
                }


                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, error.getLocalizedMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void invokeReadingEntryWebService(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + Constants.CHECK_READING, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");
                    et_scno.setText("");
                    et_meterslno.setText("");
                    //et_checkdate.setText("");
                    et_kwh.setText("");
                    et_kvah.setText("");
                    et_meterseal1.setText("");
                    et_meterseal2.setText("");

                    if (status.equals("SUCCESS")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        hide_WholeLayout();
                        Toast.makeText(getApplicationContext(), "Check Reading Saved Successfully.", Toast.LENGTH_LONG).show();
                    } else if (status.equals("ERROR")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        //msg = obj.getString("FLAG1");
                        Toast.makeText(getApplicationContext(), "Check Reading Already Submitted / Failed to Save Check Reading", Toast.LENGTH_LONG).show();

                    } else if (status.equalsIgnoreCase("FALSE")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        // msg = obj.getString("FLAG1");
                        Toast.makeText(getApplicationContext(), "Check Reading Already Submitted / Failed to Save Check Reading", Toast.LENGTH_LONG).show();

                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, e.getLocalizedMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(CheckReadingActivity.this, error.getLocalizedMessage());
            }
        });
    }


    public void hide_WholeLayout() {
        et_servicenum.setText("");
        //et_distribution_code.setText("");
        et_scno.setText("");
        et_meterslno.setText("");
        //et_checkdate.setText("");
        et_kwh.setText("");
        et_kvah.setText("");
        et_meterseal1.setText("");
        et_meterseal2.setText("");
        rg_supply_util.clearCheck();
        rg_theft_mal.clearCheck();
        rg_adl_available.clearCheck();
        rg_irda.clearCheck();
        rg_meter_location.clearCheck();
        whole_Layout.setVisibility(View.GONE);
        recreate();
    }


    public void setValues() {
        tv_section_name.setText(str_section_name);
        tv_distribution_name.setText(str_distribution_name);
        tv_consumername.setText(str_name);
        tv_cat.setText(str_cat);
        tv_type.setText(str_billingtype);
        tv_date.setText(str_billdate);
        if (!Utility.isValueNullOrEmpty(strLat) && !strLat.equalsIgnoreCase("0") && mMap != null) {
            mLastLocation = new Location(LocationManager.GPS_PROVIDER);
            mLastLocation.setLatitude(Double.parseDouble(strLat));
            mLastLocation.setLongitude(Double.parseDouble(strLong));
            tv_latitude.setText(String.format("%s", mLastLocation.getLatitude()));
            tv_longitude.setText(String.format("%s", mLastLocation.getLongitude()));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .title("(" + tv_latitude.getText().toString() + "," + tv_longitude.getText().toString() + ")"));
            //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);


            /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(LatLng point) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(point.latitude, point.longitude))
                            .title("New Marker");
                    mMap.addMarker(marker);
                    System.out.println(point.latitude + "---" + point.longitude);
                    Toast.makeText(getApplicationContext(),point.latitude + "---" + point.longitude,Toast.LENGTH_LONG).show();
                }
            });*/
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(CheckReadingActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(CheckReadingActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                }
            });
            ll_map.setVisibility(View.VISIBLE);
            rl_latitude.setVisibility(View.VISIBLE);
            rl_longitude.setVisibility(View.VISIBLE);
            savebtn.setVisibility(View.VISIBLE);
            buildGoogleApiClient();

        }

        List<String> list = new ArrayList<String>();

        list.add("Select");
        list.add("LIVE");
        list.add("STRUCK UP");
        list.add("UDC");
        list.add("DOOR LOCK");
        list.add("MEETER NOT EXISTING");
        list.add("NIL CONSUMPTION");
        list.add("BURNT");
        list.add("BILLSTOP");

//        list.add("DISMANTEL");
//        list.add("READING NOT FURNISHED");

//        list.add("LIVE");
//        list.add("UDC");
//        list.add("BILLSTOP");
//        list.add("BURNT");
//        list.add("STRUCKUP");
//        list.add("DOOR LOCK");

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                str_Status = parent.getItemAtPosition(position).toString();
                if (str_Status.equalsIgnoreCase("Select")) {
                    strStatus = "";
                } else if (str_Status.equalsIgnoreCase("LIVE")) {
                    strStatus = "01";
                } else if (str_Status.equalsIgnoreCase("STRUCK UP")) {
                    strStatus = "02";
                } else if (str_Status.equalsIgnoreCase("UDC")) {
                    strStatus = "03";
                } else if (str_Status.equalsIgnoreCase("DOOR LOCK")) {
                    strStatus = "05";
                } else if (str_Status.equalsIgnoreCase("MEETER NOT EXISTING")) {
                    strStatus = "06";
                } else if (str_Status.equalsIgnoreCase("NIL CONSUMPTION")) {
                    strStatus = "09";
                } else if (str_Status.equalsIgnoreCase("BURNT")) {
                    strStatus = "11";
                } else if (str_Status.equalsIgnoreCase("BILLSTOP")) {
                    strStatus = "99";
                }
//                else if (str_Status.equalsIgnoreCase("DISMANTEL")) {
//                    strStatus = "14";
//                }else if (str_Status.equalsIgnoreCase("MEETER NOT EXISTING")) {
//                    strStatus = "06";
//                } else if (str_Status.equalsIgnoreCase("READING NOT FURNISHED")) {
//                    strStatus = "08";
//                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        if (sPhase.equalsIgnoreCase("3") && AppPrefs.getInstance(getApplicationContext()).getString("checkReadSNOIsSolar","").equalsIgnoreCase("N")) {
            ll_kvah.setVisibility(View.VISIBLE);
        } else if (sPhase.equalsIgnoreCase("1")) {
            ll_kvah.setVisibility(View.GONE);
        }
        if(AppPrefs.getInstance(getApplicationContext()).getString("checkReadSNOIsSolar","").equalsIgnoreCase("Y")){
            tv_kwh.setText("Export KWH");
        }

        bindMeterMakeData();//Meter Make data load
    }

    //Bind Category Data
    private void bindCategoryData() {
        //Add String meterMakeStrArray to meterMakeArrayList
        meterCategoryList = new ArrayList<>();
        String[] meterMakeStrArray = getResources().getStringArray(R.array.category);
        meterCategoryList = new ArrayList<>(Arrays.asList(meterMakeStrArray));
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, meterCategoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_correct_category.setAdapter(adapter);
        spn_correct_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedcategoryStr = (String) meterCategoryList.get(position);
                if (!selectedcategoryStr.equalsIgnoreCase("--Select--")) {

                } else {
                    selectedcategoryStr = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Bind Meter Make Data
    private void bindMeterMakeData() {
        //Add String meterMakeStrArray to meterMakeArrayList
        meterMakeList = new ArrayList<>();
        String[] meterMakeStrArray = getResources().getStringArray(R.array.meter_make);
        meterMakeList = new ArrayList<>(Arrays.asList(meterMakeStrArray));
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, meterMakeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_meter_make.setAdapter(adapter);
        spn_meter_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMeterMakeStr = (String) meterMakeList.get(position);
                if (!selectedMeterMakeStr.equalsIgnoreCase("--Select--")) {

                } else {
                    selectedMeterMakeStr = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void callReadingEntryService() {
        String strDate = et_checkdate.getText().toString();
        //Get Meter Location State
        if (rg_meter_location.getCheckedRadioButtonId() != -1) {
            // hurray at-least on radio button is checked.
            int selectedMl = rg_meter_location.getCheckedRadioButtonId();
            RadioButton meterLocationButton = (RadioButton) findViewById(selectedMl);
            meterLocationStr = meterLocationButton.getText().toString();
        } else {
            // pls select at-least one radio button.. since id is -1 means no button is check
            meterLocationStr = "";
        }


        //Get AddL State
        if (rg_adl_available.getCheckedRadioButtonId() != -1) {
            // hurray at-least on radio button is checked.
            int selectedadl = rg_adl_available.getCheckedRadioButtonId();
            RadioButton adlButton = (RadioButton) findViewById(selectedadl);
            adlVvailableStr = adlButton.getText().toString();
        } else {
            // pls select at-least one radio button.. since id is -1 means no button is check
            adlVvailableStr = "";
        }

        //Get Teft/Mal State
        if (rg_theft_mal.getCheckedRadioButtonId() != -1) {
            // hurray at-least on radio button is checked.
            int selectedtheftMal = rg_theft_mal.getCheckedRadioButtonId();
            RadioButton theftMalButton = (RadioButton) findViewById(selectedtheftMal);
            theftMalStr = theftMalButton.getText().toString();
        } else {
            // pls select at-least one radio button.. since id is -1 means no button is check
            theftMalStr = "";
        }


        //Get Supplyutil State
        if (rg_supply_util.getCheckedRadioButtonId() != -1) {
            // hurray at-least on radio button is checked.
            int selectedSupplyutil = rg_supply_util.getCheckedRadioButtonId();
            RadioButton supplyUtilButton = (RadioButton) findViewById(selectedSupplyutil);
            supplyutilStr = supplyUtilButton.getText().toString();
        } else {
            // pls select at-least one radio button.. since id is -1 means no button is check
            supplyutilStr = "";
        }


        //Get ird State
        if (rg_irda.getCheckedRadioButtonId() != -1) {
            // hurray at-least on radio button is checked.
            int selectedIrdaval = rg_irda.getCheckedRadioButtonId();
            RadioButton irdaButton = (RadioButton) findViewById(selectedIrdaval);
            irdaStr = irdaButton.getText().toString();
        } else {
            // pls select at-least one radio button.. since id is -1 means no button is check
            irdaStr = "";
        }


        //Toast.makeText(getApplicationContext(), meterLocationStr+":meterLocationStr "+adlVvailableStr+":adlVvailableStr  "+theftMalStr+":theftMalStr  "+supplyutilStr+":supplyutilStr", Toast.LENGTH_LONG).show();
        if (Utils.IsNullOrBlank(strDate)) {
            Toast.makeText(getApplicationContext(), "Please Select Check Reading Date", Toast.LENGTH_LONG).show();
        } else if (strStatus.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Select Status", Toast.LENGTH_LONG).show();
        } else if (selectedMeterMakeStr.equalsIgnoreCase("") && !strStatus.equalsIgnoreCase("05") && !strStatus.equalsIgnoreCase("06") && !strStatus.equalsIgnoreCase("11")) {
            Toast.makeText(getApplicationContext(), "Please select Meter Make", Toast.LENGTH_LONG).show();
        } else if (et_meterslno.getText().toString().equalsIgnoreCase("") && !strStatus.equalsIgnoreCase("05") && !strStatus.equalsIgnoreCase("06") && !strStatus.equalsIgnoreCase("11")) {
            Toast.makeText(getApplicationContext(), "Please enter Meter SLNo", Toast.LENGTH_LONG).show();
        }
//        else if ((et_meterseal1.getText().toString().trim().length() < 6) && !strStatus.equalsIgnoreCase("05")) {
//            Toast.makeText(getApplicationContext(), "Please enter Meter Seal 1 (Minimum 6 characters)", Toast.LENGTH_LONG).show();
//        } else if (et_meterseal1.getText().toString().equalsIgnoreCase("") && !strStatus.equalsIgnoreCase("05")) {
//            Toast.makeText(getApplicationContext(), "Please enter Meter Seal 1", Toast.LENGTH_LONG).show();
//        }
//        else if (et_meterseal2.getText().toString().trim().length() < 6) {
//            Toast.makeText(getApplicationContext(), "Please enter the Meter Seal 2 at least 6 characters", Toast.LENGTH_LONG).show();
//        }
        else if (meterLocationStr.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select Meter Location Inside/Outside", Toast.LENGTH_LONG).show();
        } else if (adlVvailableStr.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select Additional Load Available or Not", Toast.LENGTH_LONG).show();
        } else if (theftMalStr.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select Theft/Malpractice Noticed or Not", Toast.LENGTH_LONG).show();
        } else if (supplyutilStr.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select Utilizing Supply in Correct Category or Not", Toast.LENGTH_LONG).show();
        } else if (supplyutilStr.equalsIgnoreCase("N") && selectedcategoryStr.equalsIgnoreCase("")) {

            Toast.makeText(getApplicationContext(), "Please select Correct Category", Toast.LENGTH_LONG).show();

        } else if (irdaStr.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select Meter Is IrDA Meter or Not", Toast.LENGTH_LONG).show();
        } else if (tv_latitude.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please TagLocation", Toast.LENGTH_LONG).show();
        } else if (Utility.isValueNullOrEmpty(base64_ctmimg)) {
            Toast.makeText(getApplicationContext(), "Please take photo of Meter", Toast.LENGTH_LONG).show();
        } else {
            String newFields;
            String meterSeal1 = "null";
            String meterSeal2 = "null";
            String meterSlnoStr = et_meterslno.getText().toString().trim();

            if (!et_meterseal1.getText().toString().equalsIgnoreCase("")) {
                meterSeal1 = et_meterseal1.getText().toString().trim();
            }
            if (!et_meterseal2.getText().toString().equalsIgnoreCase("")) {
                meterSeal2 = et_meterseal2.getText().toString().trim();
            }
            //Check if Status is Door Lock
            if (str_kwh.equalsIgnoreCase("")) {
                str_kwh = "0";
            }
            if (str_kvah.equalsIgnoreCase("")) {
                str_kvah = "0";
            }
            if (selectedMeterMakeStr.equalsIgnoreCase("")) {
                selectedMeterMakeStr = "0";
            }
            if (meterSlnoStr.equalsIgnoreCase("")) {
                meterSlnoStr = "0";
            }

            //Radio Buttons Checking
            //Adl
            if (adlVvailableStr.equalsIgnoreCase("Yes"))
                adlVvailableStr = "Y";
            else
                adlVvailableStr = "N";
            //theftMalStr
            if (theftMalStr.equalsIgnoreCase("Yes"))
                theftMalStr = "Y";
            else
                theftMalStr = "N";
            //supplyutilStr
            if (supplyutilStr.equalsIgnoreCase("Yes"))
                supplyutilStr = "Y";
            else
                supplyutilStr = "N";
            //irdaStr
            if (irdaStr.equalsIgnoreCase("Yes"))
                irdaStr = "Y";
            else
                irdaStr = "N";

            //End Check if Status is Door Lock

            if (supplyutilStr.equalsIgnoreCase("N")) {
                newFields = meterSeal1 + "|" + meterSeal2 + "|" + meterLocationStr + "|" + adlVvailableStr + "|" + theftMalStr + "|" + supplyutilStr + "|" + selectedcategoryStr;
            } else {
                newFields = meterSeal1 + "|" + meterSeal2 + "|" + meterLocationStr + "|" + adlVvailableStr + "|" + theftMalStr + "|" + supplyutilStr + "|null";

            }
            if (strLat.equalsIgnoreCase(tv_latitude.getText().toString()) && strLong.equalsIgnoreCase(tv_longitude.getText().toString())) {
                strTCodeValue = str_servicenumber + "|" + str_kwh + "|" + str_kvah + "|" + strDate + "|" + strStatus
                        + "|" + str_billingtype + "|" + userName + "|" + str_billdate + "|" + str_kwhreading + "|" + str_kvhreading + "|" + tv_latitude.getText().toString() + "|" + tv_longitude.getText().toString() + "|" + selectedMeterMakeStr + "|" + meterSlnoStr
                        + "|" + newFields + "|" + base64_ctmimg + "|" + irdaStr;
            } else {
                strTCodeValue = str_servicenumber + "|" + str_kwh + "|" + str_kvah + "|" + strDate + "|" + strStatus
                        + "|" + str_billingtype + "|" + userName + "|" + str_billdate + "|" + str_kwhreading + "|" + str_kvhreading + "|" + tv_latitude.getText().toString() + "|" + tv_longitude.getText().toString() + "|" + selectedMeterMakeStr + "|" + meterSlnoStr
                        + "|" + newFields + "|" + base64_ctmimg + "|" + irdaStr;
            }
            Log.e("str_value", strTCodeValue);
            if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
//                RequestParams params = new RequestParams();
//                params.put("T_CODE", strTCodeValue);
//                invokeReadingEntryWebService(params);
                DateFormat df = new SimpleDateFormat("HH:mm");
                String currentTime = df.format(Calendar.getInstance().getTime());
//                Date currentTime = Calendar.getInstance().getTime();
                JSONObject requestObj = new JSONObject();
                try {
                    requestObj.put("USCNO", str_servicenumber);
                    requestObj.put("BPNO", AppPrefs.getInstance(getApplicationContext()).getString("checkReadingBpno", ""));
                    requestObj.put("MtrMake", selectedMeterMakeStr);
                    requestObj.put("MtrSlNo", meterSlnoStr);
                    requestObj.put("IsSolar", AppPrefs.getInstance(getApplicationContext()).getString("checkReadSNOIsSolar", ""));
                    if (AppPrefs.getInstance(getApplicationContext()).getString("checkReadSNOIsSolar", "").equalsIgnoreCase("N")){
                        requestObj.put("Kwh", str_kwh);
                        requestObj.put("ExpKwh", "");
                    }else{
                        requestObj.put("Kwh", "");
                        requestObj.put("ExpKwh", str_kwh);
                    }
                    requestObj.put("Kvah", str_kvah);
                    requestObj.put("Rmd", "");
                    //requestObj.put("ExpKwh", "");
                    requestObj.put("ExpKvah", "");
                    requestObj.put("MtrStatus", strStatus);
                    requestObj.put("MtrLocation", meterLocationStr);
                    requestObj.put("Additional_load_available", adlVvailableStr);
                    requestObj.put("Theft_malpractice_noticed", theftMalStr);
                    requestObj.put("Utilizing_supply_in_correct_category", supplyutilStr);
                    requestObj.put("Correct_categroy", selectedcategoryStr);
                    requestObj.put("Is_Irda_meter", irdaStr);
                    requestObj.put("Meter_photo", base64_ctmimg);
                    requestObj.put("Date", strDate);
                    requestObj.put("Time", currentTime);
                    requestObj.put("UserId", userID);
                    saveChangeReadingWS(requestObj);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_checkdate.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utility.showLog("1 LocationServices mLastLocation", "LocationServices mLastLocation");
            checkPermissionsAndCallServiceToGetLatAndLng();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Utility.showLog("Geocoder mLastLocation", "Geocoder mLastLocation");
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Utility.showToastMessage(this, Utility.getResourcesString(CheckReadingActivity.this, R.string.no_geocoder_available));
            } else {
                tv_latitude.setText(String.format("%s", mLastLocation.getLatitude()));
                tv_longitude.setText(String.format("%s", mLastLocation.getLongitude()));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .title("(" + tv_latitude.getText().toString() + "," + tv_longitude.getText().toString() + ")"));
                //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(CheckReadingActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                        return false;
                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Intent intent = new Intent(CheckReadingActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                    }
                });
                ll_map.setVisibility(View.VISIBLE);
                rl_latitude.setVisibility(View.VISIBLE);
                rl_longitude.setVisibility(View.VISIBLE);
                savebtn.setVisibility(View.VISIBLE);
                //  ll_fetch_location.setVisibility(View.GONE);
                buildGoogleApiClient();
            }


        } else {
            checkPermissionsAndCallServiceToGetLatAndLng();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /*
     *CHECK PERMISSIONS AND CALL SERVICE TO GET CITY BASED ON LAT AND LNG
     */
    private void checkPermissionsAndCallServiceToGetLatAndLng() {
        if (Utility.isNetworkAvailable(this)) {
            if (Utility.isMarshmallowOS()) {
                PackageManager pm = getPackageManager();
                int hasFineLocationPerm = pm.checkPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        getPackageName());
                int hasCoarseLocationPerm = pm.checkPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        getPackageName());
                if (hasFineLocationPerm != PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            Constants.MY_PERMISSIONS_REQUEST_LOCATION);
                } else if (hasCoarseLocationPerm == PackageManager.PERMISSION_GRANTED &&
                        hasFineLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.MY_PERMISSIONS_REQUEST_LOCATION_SINGLE);
                } else if (hasCoarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            Constants.MY_PERMISSIONS_REQUEST_LOCATION_SINGLE);
                } else {
                    pDialog.show();
                    if (Utility.isLocationEnabled(this)) {
                        if (Utility.isNetworkAvailable(this)) {
                            createLocationRequest();
                        } else {
                            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
                        }
                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        createLocationRequest();
                    }
                }
            } else {
                pDialog.show();
                if (Utility.isLocationEnabled(this)) {
                    if (Utility.isNetworkAvailable(this)) {
                        createLocationRequest();
                    } else {
                        Utility.showCustomOKOnlyDialog(this,
                                Utility.getResourcesString(this,
                                        R.string.no_internet));
                    }
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    createLocationRequest();
                }
            }
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    protected void createLocationRequest() {
        mGoogleApiClient.connect();
        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                // final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Utility.showLog("LocationSettingsStatusCodes.SUCCESS",
                                "LocationSettingsStatusCodes.SUCCESS" + LocationSettingsStatusCodes.SUCCESS);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //checkPermissionsAndCallServiceToGetLatAndLng();
                        mGoogleApiClient.connect();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        Utility.showLog("LocationSettingsStatusCodes.RESOLUTION_REQUIRED",
                                "LocationSettingsStatusCodes.RESOLUTION_REQUIRED" + LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_IMEI:
              /*  if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager manager = (TelephonyManager) getSystemService(GPSTrackerActivity.TELEPHONY_SERVICE);
                    Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                    Utility.showLog("IMEI", manager.getDeviceId());
                }*/
                String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, IMEI_NUMBER);
                Utility.showLog("IMEI", IMEI_NUMBER);
                break;
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndCallServiceToGetLatAndLng();
                }
                break;
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION_SINGLE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndCallServiceToGetLatAndLng();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void callAlert(String Title) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CheckReadingActivity.this);
        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder
                .setMessage("Click yes would you like to continue")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                try {
                                    callReadingEntryService();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}