package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class TongTesterActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    EditText et_dtrserialnumber, et_remarks, et_checkdate, et_dtrmake;
    Button submitbtn, calendarbtn;
    LinearLayout whole_Layout;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    Spinner dtrstructurecodespinner, dtractionspinner, dtrcapacityspinner, dtrphasespinner, ltfeedersspinner;
    String msg, sDtrCode, str_dtrAction, strDate, str_dtrCapacity, sec_code, userName, str_serialnum, data,
            str_dtrMake, str_remarks, str_dtrPhase, str_ltFeeders, total_strRYBN,strMsg;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    List<String> dtrcodelist = new ArrayList<String>();
    List<String> dtrcaplist = new ArrayList<>();
    List<String> singlephasedtrcaplist = new ArrayList<>();
    List<String> threephasedtrcaplist = new ArrayList<>();
    List<String> rybn_list = new ArrayList<>();
    LinearLayout ll_fetch_location;
    RelativeLayout rl_latitude;
    RelativeLayout rl_longitude;
    TextView tv_latitude;
    TextView tv_longitude;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private GoogleMap mMap;
    private LinearLayout ll_map;

    ListAdapter listAdapter;
    SingleListAdapter singlelistAdapter;
    ListView rybn_listview;
    List<String> et_rList = new ArrayList<>();
    List<String> et_yList = new ArrayList<>();
    List<String> et_bList = new ArrayList<>();
    List<String> et_nList = new ArrayList<>();
    List<String> str_rybnList = new ArrayList<>();

    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_transformer_tongtester);
        init();
    }

    /*Initialize Views*/
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        buildGoogleApiClient();
        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
        sec_code = lprefs.getString("Section_Code", "");
        userName = lprefs.getString("UserName", "");
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);

        pDialog = new ProgressDialog(this);
        et_dtrserialnumber = (EditText) findViewById(R.id.et_dtrserialnumber);
        et_remarks = (EditText) findViewById(R.id.et_remarks);
        et_checkdate = (EditText) findViewById(R.id.et_checkdate);
        et_dtrmake = (EditText) findViewById(R.id.et_dtrmake);


        submitbtn = (Button) findViewById(R.id.submitbtn);
        submitbtn.setOnClickListener(this);
        whole_Layout = (LinearLayout) findViewById(R.id.whole_Layout);
        rybn_listview = (ListView) findViewById(R.id.rybn_listview);


        calendarbtn = (Button) findViewById(R.id.calendarbtn);
        calendarbtn.setOnClickListener(this);
        et_checkdate.setOnClickListener(this);
        tv_latitude = (TextView) findViewById(R.id.tv_latitude);
        tv_longitude = (TextView) findViewById(R.id.tv_longitude);
        ll_fetch_location = (LinearLayout) findViewById(R.id.ll_fetch_location);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        rl_latitude = (RelativeLayout) findViewById(R.id.rl_latitude);
        rl_longitude = (RelativeLayout) findViewById(R.id.rl_longitude);
        ll_fetch_location.setOnClickListener(this);

        dtrstructurecodespinner = (Spinner) findViewById(R.id.dtrstructurecodespinner);
        dtractionspinner = (Spinner) findViewById(R.id.dtractionspinner);
        dtrphasespinner = (Spinner) findViewById(R.id.dtrphasespinner);
        ltfeedersspinner = (Spinner) findViewById(R.id.ltfeedersspinner);
        dtrcapacityspinner = (Spinner) findViewById(R.id.dtrcapacityspinner);


        List<String> actionlist = new ArrayList<String>();
        actionlist.add("Select");
        actionlist.add("No Action Required");
        actionlist.add("Load Balance");
        actionlist.add("DTR Enhancement");
        actionlist.add("New DTR Required");

        ArrayAdapter<String> actionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, actionlist);
        actionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dtractionspinner.setAdapter(actionAdapter);

        dtractionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                str_dtrAction = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        try {
            if (objNetworkReceiver.hasInternetConnection(this)) {
                RequestParams params = new RequestParams();
                params.put("LM_CODE", userName);
                dtrcodelist.add("Select");
                invokedtrcodeFetchWebService(params);


                dtrstructurecodespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                        sDtrCode = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

          /*  } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();*/
            }
            List<String> phaselist = new ArrayList<String>();
            phaselist.add("Select");
            phaselist.add("1");
            phaselist.add("3");

            ArrayAdapter<String> phaseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phaselist);
            phaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dtrphasespinner.setAdapter(phaseAdapter);

            dtrphasespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                    et_rList.clear();
                    et_yList.clear();
                    et_bList.clear();
                    et_nList.clear();
                    rybn_list.clear();
                    str_dtrPhase = parent.getItemAtPosition(position).toString();
                    if (str_dtrPhase.equalsIgnoreCase("Select")) {
                        ltfeedersspinner.setEnabled(false);
                        dtrcapacityspinner.setEnabled(false);
                        rybn_listview.setVisibility(View.GONE);
                    } else {
                        ltfeedersspinner.setEnabled(true);
                        dtrcapacityspinner.setEnabled(true);
                        try {
                            if (objNetworkReceiver.hasInternetConnection(TongTesterActivity.this)) {
                                RequestParams params = new RequestParams();
                                invokedtrcapFetchWebService(params);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    ltfeedersspinner.setSelection(0);
                }

                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });


            List<String> ltfeederslist = new ArrayList<String>();
            ltfeederslist.add("Select");
            ltfeederslist.add("1");
            ltfeederslist.add("2");
            ltfeederslist.add("3");
            ltfeederslist.add("4");

            ArrayAdapter<String> feedersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ltfeederslist);
            feedersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ltfeedersspinner.setAdapter(feedersAdapter);

            ltfeedersspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                    str_ltFeeders = parent.getItemAtPosition(position).toString();
                    ltFeederSelection();
                }

                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (objNetworkReceiver.hasInternetConnection(this)) {
                RequestParams params = new RequestParams();
                invokedtrcapFetchWebService(params);
                dtrcaplist.add("Select");

                ArrayAdapter<String> capacityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dtrcaplist);
                capacityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dtrcapacityspinner.setAdapter(capacityAdapter);

                dtrcapacityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                        str_dtrCapacity = parent.getItemAtPosition(position).toString();
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });


            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {
        if (view == calendarbtn || view == et_checkdate) {
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

        if (view == submitbtn) {
            str_serialnum = et_dtrserialnumber.getText().toString();
            strDate = et_checkdate.getText().toString();
            str_remarks = et_remarks.getText().toString();
            str_dtrMake = et_dtrmake.getText().toString();
            str_rybnList.clear();
            if (Utils.IsNullOrBlank(sDtrCode)) {
                Toast.makeText(getApplicationContext(), "Please select DTR Code", Toast.LENGTH_LONG).show();
            } else if (sDtrCode.equals("Select")) {
                Toast.makeText(getApplicationContext(), "Please select DTR Code", Toast.LENGTH_LONG).show();
            } else if (Utils.IsNullOrBlank(str_serialnum)) {
                Toast.makeText(getApplicationContext(), "Please enter serial number", Toast.LENGTH_LONG).show();
            } else if (str_serialnum.length() > 12) {
                Toast.makeText(getApplicationContext(), "Please enter 12 digit serial number", Toast.LENGTH_LONG).show();
            } else if (str_dtrPhase.equals("Select")) {
                Toast.makeText(getApplicationContext(), "Please select DTR Phase", Toast.LENGTH_LONG).show();
            } else if (str_dtrCapacity.equals("Select")) {
                Toast.makeText(getApplicationContext(), "Please select DTR Capacity", Toast.LENGTH_LONG).show();
            } else if (str_dtrMake.equals("Select")) {
                Toast.makeText(getApplicationContext(), "Please enter DTR Make", Toast.LENGTH_LONG).show();
            } else if (str_ltFeeders.equals("Select")) {
                Toast.makeText(getApplicationContext(), "Please select LT Feeders", Toast.LENGTH_LONG).show();
            } else if (Utils.IsNullOrBlank(strDate)) {
                Toast.makeText(getApplicationContext(), "Please select Check Reading Date", Toast.LENGTH_LONG).show();
            } else if (str_dtrAction.equals("Select")) {
                Toast.makeText(getApplicationContext(), "Please select DTR Action", Toast.LENGTH_LONG).show();
            } else if (Utils.IsNullOrBlank(str_remarks)) {
                Toast.makeText(getApplicationContext(), "Please enter remarks", Toast.LENGTH_LONG).show();
            } else if (Utils.IsNullOrBlank(tv_latitude.getText().toString())) {
                Toast.makeText(getApplicationContext(), "Please Tag Location", Toast.LENGTH_LONG).show();
            } else {

                if (str_dtrPhase.equalsIgnoreCase("3")) {
                    for (int i = 0; i < n; i++) {
                        str_rybnList.add(et_rList.get(i) + "|" + et_yList.get(i) + "|" + et_bList.get(i) + "|" + et_nList.get(i));
                    }
                    total_strRYBN = Utils.removeLastChar(Utils.generate_string(str_rybnList));
                    msg = userName + "|" + sDtrCode + "|" + str_serialnum + "|" + str_dtrPhase + "|" + str_dtrMake + "|" + str_dtrCapacity + "|" + str_ltFeeders + "|" +
                            strDate + "|" + str_dtrAction + "|" + str_remarks + "|" + tv_latitude.getText().toString() + "|" + tv_longitude.getText().toString()
                            + total_strRYBN;
                } else if (str_dtrPhase.equalsIgnoreCase("1")) {
                    for (int i = 0; i < n; i++) {
                        str_rybnList.add(et_rList.get(i) + "|" + et_nList.get(i));
                    }
                    total_strRYBN = Utils.removeLastChar(Utils.generate_string(str_rybnList));
                    msg = userName + "|" + sDtrCode + "|" + str_serialnum + "|" + str_dtrPhase + "|" + str_dtrMake + "|" + str_dtrCapacity + "|" + str_ltFeeders + "|" +
                            strDate + "|" + str_dtrAction + "|" + str_remarks + "|" + tv_latitude.getText().toString() + "|" + tv_longitude.getText().toString()
                            + total_strRYBN;
                }

                if (objNetworkReceiver.hasInternetConnection(this)) {
                    RequestParams params = new RequestParams();
                    params.put("DATA", msg);
                    invokeSubmitWebService(params);

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (view == ll_fetch_location) {
            checkPermissionsAndCallServiceToGetLatAndLng();
        }

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

    private void invokedtrcodeFetchWebService(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + Constants.TONG_DTRLIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) obj.get("DTRLIST");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strDTRCD = jsonObject.getString("DTRCD");
                        dtrcodelist.add(strDTRCD);
                    }
                    ArrayAdapter<String> dtrcodeAdapter = new ArrayAdapter<String>(TongTesterActivity.this, android.R.layout.simple_spinner_item, dtrcodelist);
                    dtrcodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dtrstructurecodespinner.setAdapter(dtrcodeAdapter);

                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Toast.makeText(getApplicationContext(), "Unable to Connect Server", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
                        break;
                }
                Log.e("error", error.toString());
            }
        });
    }

    private void invokedtrcapFetchWebService(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + Constants.TONG_CAPLIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("DTRCAPError", obj.toString());
                    String status = obj.getString("STATUS");
                    if (status.equals("SUCCESS")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        data = obj.getString("DATA");
                        String[] arr = data.split("\\|");
                        dtrcaplist.clear();

                        for (int i = 0; i < arr.length; i++) {
                            String strDTRCAP = arr[i];
                            dtrcaplist.add(strDTRCAP);
                        }
                        singlephasedtrcaplist = dtrcaplist.subList(0, 3);
                        threephasedtrcaplist = dtrcaplist.subList(3, dtrcaplist.size());
                        selectCapList();


                    } else if (status.equals("ERROR")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Please try Again", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Toast.makeText(getApplicationContext(), "Unable to Connect Server", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
                        break;
                }
                Log.e("error", error.toString());
            }
        });
    }


    private void invokeSubmitWebService(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + Constants.TONG_SAVE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d("save", obj.toString());
                    String status = obj.getString("STATUS");


                    if (status.equals("SUCCESS")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Saved Successfully.", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(getApplicationContext(), LMDashBoardActivity.class));
                        finish();

                    } else if (status.equals("ERROR")) {
                         strMsg = obj.getString("MSG");
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();

                    } else if (status.equalsIgnoreCase("FAIL")) {
                         strMsg = obj.getString("MSG");
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();

                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Toast.makeText(getApplicationContext(), "Unable to Connect Server", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
                        break;
                }
                Log.e("error", error.toString());
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        et_checkdate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onResume() {
        super.onResume();

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
                Utility.showToastMessage(this, Utility.getResourcesString(TongTesterActivity.this, R.string.no_geocoder_available));
            } else {
                mMap.clear();
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
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(TongTesterActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                        return false;
                    }
                });
                ll_map.setVisibility(View.VISIBLE);
                rl_latitude.setVisibility(View.VISIBLE);
                rl_latitude.setVisibility(View.VISIBLE);
                rl_longitude.setVisibility(View.VISIBLE);
                submitbtn.setVisibility(View.VISIBLE);
                //ll_fetch_location.setVisibility(View.GONE);
                buildGoogleApiClient();
            }


        } else {
            checkPermissionsAndCallServiceToGetLatAndLng();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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
                 /* if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager manager = (TelephonyManager) getSystemService(GPSTrackerActivity.TELEPHONY_SERVICE);
                    Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                    Utility.showLog("IMEI", manager.getDeviceId());
                }*/
                String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER,IMEI_NUMBER);
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

    public void ltFeederSelection() {
        if (str_dtrPhase.equalsIgnoreCase("1")) {
            if (!str_ltFeeders.equalsIgnoreCase("Select")) {
                rybn_listview.setVisibility(View.VISIBLE);
                n = Integer.parseInt(str_ltFeeders);
                try {
                    /*LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, n * 150);
                    rybn_listview.setLayoutParams(mParam);*/

                    singlelistAdapter = new SingleListAdapter(TongTesterActivity.this);
                    rybn_listview.setAdapter(singlelistAdapter);
                    Utility.setListViewHeightBasedOnChildren(rybn_listview);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                rybn_listview.setVisibility(View.GONE);
            }
        } else if (str_dtrPhase.equalsIgnoreCase("3")) {
            if (!str_ltFeeders.equalsIgnoreCase("Select")) {
                rybn_listview.setVisibility(View.VISIBLE);
                n = Integer.parseInt(str_ltFeeders);
                try {
                    /*LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, n * 150);
                    rybn_listview.setLayoutParams(mParam);*/

                    listAdapter = new ListAdapter(TongTesterActivity.this);
                    rybn_listview.setAdapter(listAdapter);
                    Utility.setListViewHeightBasedOnChildren(rybn_listview);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                rybn_listview.setVisibility(View.GONE);
            }
        }


    }

    public void selectCapList() {

        if (str_dtrPhase.equalsIgnoreCase("1")) {
            ArrayAdapter<String> capacityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, singlephasedtrcaplist);
            capacityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dtrcapacityspinner.setAdapter(capacityAdapter);

            dtrcapacityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                    str_dtrCapacity = parent.getItemAtPosition(position).toString();

                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        } else if (str_dtrPhase.equalsIgnoreCase("3")) {
            ArrayAdapter<String> capacityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, threephasedtrcaplist);
            capacityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dtrcapacityspinner.setAdapter(capacityAdapter);

            dtrcapacityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                    str_dtrCapacity = parent.getItemAtPosition(position).toString();

                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        }
    }

    public class ListAdapter extends BaseAdapter {

        private Context mContext;
        String enteredString_r, enteredString_y, enteredString_b, enteredString_n;
        boolean bool_rybn = false;
        EditText et_r, et_y, et_b, et_n;
        private HashMap<Integer, String> et_rItems = new HashMap<Integer, String>();
        private HashMap<Integer, String> et_yItems = new HashMap<Integer, String>();
        private HashMap<Integer, String> et_bItems = new HashMap<Integer, String>();
        private HashMap<Integer, String> et_nItems = new HashMap<Integer, String>();

        public ListAdapter(Context context) {
            super();
            mContext = context;
        }

        public int getCount() {
            return n;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rybn_listitem, null);

            et_r = (EditText) view.findViewById(R.id.et_r);
            et_y = (EditText) view.findViewById(R.id.et_y);
            et_b = (EditText) view.findViewById(R.id.et_b);
            et_n = (EditText) view.findViewById(R.id.et_n);

            try {
                if (!bool_rybn) {
                    enteredString_r = "0";
                    et_rItems.put(position, enteredString_r);
                    et_rList.add(et_rItems.get(position));
                    et_rList.removeAll(Arrays.asList(null, ""));
                }
                et_r.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            enteredString_r = ((EditText) v).getText().toString();
                            if (Utils.IsNullOrBlank(enteredString_r)) {
                                enteredString_r = "0";
                            }
                            bool_rybn = true;
                            if (bool_rybn) {
                                et_rList.set(position, enteredString_r);
                                bool_rybn = false;
                            }
                        }
                    }
                });

            } catch (Exception e) {
            }


            try {
                if (!bool_rybn) {
                    enteredString_y = "0";
                    et_yItems.put(position, enteredString_y);
                    et_yList.add(et_yItems.get(position));
                    et_yList.removeAll(Arrays.asList(null, ""));
                }
                et_y.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            enteredString_y = ((EditText) v).getText().toString();
                            if (Utils.IsNullOrBlank(enteredString_y)) {
                                enteredString_y = "0";
                            }
                            bool_rybn = true;
                            if (bool_rybn) {
                                et_yList.set(position, enteredString_y);
                                bool_rybn = false;
                            }
                        }
                    }
                });

            } catch (Exception e) {
            }


            try {
                if (!bool_rybn) {
                    enteredString_b = "0";
                    et_bItems.put(position, enteredString_b);
                    et_bList.add(et_bItems.get(position));
                    et_bList.removeAll(Arrays.asList(null, ""));
                }
                et_b.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            enteredString_b = ((EditText) v).getText().toString();
                            if (Utils.IsNullOrBlank(enteredString_b)) {
                                enteredString_b = "0";
                            }
                            bool_rybn = true;
                            if (bool_rybn) {
                                et_bList.set(position, enteredString_b);
                                bool_rybn = false;
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (!bool_rybn) {
                    enteredString_n = "0";
                    et_nItems.put(position, enteredString_n);
                    et_nList.add(et_nItems.get(position));
                    et_nList.removeAll(Arrays.asList(null, ""));
                }
                et_n.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            enteredString_n = ((EditText) v).getText().toString();
                            if (Utils.IsNullOrBlank(enteredString_n)) {
                                enteredString_n = "0";
                            }
                            bool_rybn = true;
                            if (bool_rybn) {
                                et_nList.set(position, enteredString_n);
                                bool_rybn = false;
                            }
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


            return view;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
    }


    public class SingleListAdapter extends BaseAdapter {
        private Context mContext;
        String enteredString_r, enteredString_n;
        boolean bool_rybn_single = false;
        EditText et_r, et_n;
        LinearLayout y_layout, b_layout;
        private HashMap<Integer, String> et_rItems = new HashMap<Integer, String>();
        private HashMap<Integer, String> et_nItems = new HashMap<Integer, String>();

        public SingleListAdapter(Context context) {
            super();
            mContext = context;
        }

        public int getCount() {
            return n;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.rybn_listitem, null);

            et_r = (EditText) view.findViewById(R.id.et_r);
            et_n = (EditText) view.findViewById(R.id.et_n);

            y_layout = (LinearLayout) view.findViewById(R.id.y_layout);
            b_layout = (LinearLayout) view.findViewById(R.id.b_layout);

            y_layout.setVisibility(View.GONE);
            b_layout.setVisibility(View.GONE);


            try {
                if (!bool_rybn_single) {
                    enteredString_r = "0";
                    et_rItems.put(position, enteredString_r);
                    et_rList.add(et_rItems.get(position));
                    et_rList.removeAll(Arrays.asList(null, ""));
                }
                et_r.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            enteredString_r = ((EditText) v).getText().toString();
                            if (Utils.IsNullOrBlank(enteredString_r)) {
                                enteredString_r = "0";
                            }
                            bool_rybn_single = true;
                            if (bool_rybn_single) {
                                et_rList.set(position, enteredString_r);
                                bool_rybn_single = false;
                            }
                        }
                    }
                });

            } catch (Exception e) {
            }


            try {
                if (!bool_rybn_single) {
                    enteredString_n = "0";
                    et_nItems.put(position, enteredString_n);
                    et_nList.add(et_nItems.get(position));
                    et_nList.removeAll(Arrays.asList(null, ""));
                }
                et_n.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            enteredString_n = ((EditText) v).getText().toString();
                            if (Utils.IsNullOrBlank(enteredString_n)) {
                                enteredString_n = "0";
                            }
                            bool_rybn_single = true;
                            if (bool_rybn_single) {
                                et_nList.set(position, enteredString_n);
                                bool_rybn_single = false;
                            }
                        }
                    }
                });

            } catch (Exception e) {
            }

            return view;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
    }


}
