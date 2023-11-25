package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.FailureDcList;
import com.apcpdcl.departmentapp.models.ReportsList;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.sqlite.DatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.NetworkFailureDcListDatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.ReportsDataBaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
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

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 01-02-2018.
 */

public class PaymentDone extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    TextView prnumtxt, prdatetxt, amnttxt;
    String strPtpr, strPrdate, strAmount, strVerifyRadio, strScno, totalString, strPreditno, strPreditdate, streditAmount, strDiscDate, strDate, strDeviceId, lmcode;
    private RadioGroup payradioGroup;
    private Button verifybtn, calendarbtn;
    private LinearLayout entryeditLayout, donedetailsLayout;
    EditText prnumtxtedit, prdatetxtedit, amnttxtedit;
    RadioButton matchradio, mismatchradio;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    NetworkFailureDcListDatabaseHandler db = new NetworkFailureDcListDatabaseHandler(this);
    SharedPreferences operatingPreferences;
    SharedPreferences.Editor operatingPrefsEditor;
    ProgressDialog pDialog;
    Calendar myCalendar;
    ImageView home_imageView;

    DatePickerDialog.OnDateSetListener date;
    ReportsDataBaseHandler rdb;
    public static final int REQUEST_CODE_PHONE_STATE_READ = 1;
    private int checkedPermission = PackageManager.PERMISSION_DENIED;
    AlertDialog.Builder alertBuilder;
    AlertDialog alert;
    private LinearLayout ll_fetch_location, ll_map, ll_lat, ll_long;
    private TextView tv_lat, tv_long;

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String mLat, mLong;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentdone);

        rdb = new ReportsDataBaseHandler(PaymentDone.this);
        SharedPreferences loginprefs = getSharedPreferences("loginPrefs", 0);
        lmcode = loginprefs.getString("LMCode", "");


        prnumtxt = (TextView) findViewById(R.id.prnumtxt);
        prdatetxt = (TextView) findViewById(R.id.prdatetxt);
        amnttxt = (TextView) findViewById(R.id.amnttxt);
        verifybtn = (Button) findViewById(R.id.verifyPayDoneBtn);
        calendarbtn = (Button) findViewById(R.id.calendarbtn);
        entryeditLayout = (LinearLayout) findViewById(R.id.entryeditLayout);
        donedetailsLayout = (LinearLayout) findViewById(R.id.donedetails);
        home_imageView = (ImageView) findViewById(R.id.home);
        tv_lat = (TextView) findViewById(R.id.tv_lat);
        tv_long = (TextView) findViewById(R.id.tv_long);
        ll_fetch_location = (LinearLayout) findViewById(R.id.ll_fetch_location);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        ll_lat = (LinearLayout) findViewById(R.id.ll_lat);
        ll_long = (LinearLayout) findViewById(R.id.ll_long);
        ll_fetch_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndCallServiceToGetLatAndLng();
            }
        });
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strPtpr = (String) bd.get("PTPRNO");
            strPrdate = (String) bd.get("PRDT");
            strAmount = (String) bd.get("AMT");
            strScno = (String) bd.get("SCNO");
            strDiscDate = (String) bd.get("DiscDate");
            mLat = (String) bd.get("LAT");
            mLong = (String) bd.get("LONG");
        }
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);
        buildGoogleApiClient();
        prnumtxtedit = (EditText) findViewById(R.id.prnumtxtedit);
        prdatetxtedit = (EditText) findViewById(R.id.prdatetxtedit);
        amnttxtedit = (EditText) findViewById(R.id.amnttxtedit);

        matchradio = (RadioButton) findViewById(R.id.matchradio);
        mismatchradio = (RadioButton) findViewById(R.id.mismatchradio);
        payradioGroup = (RadioGroup) findViewById(R.id.verifyRadio);
        payradioGroup.clearCheck();
        pDialog = new ProgressDialog(PaymentDone.this);


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

        calendarbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog dpr = new DatePickerDialog(PaymentDone.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                dpr.getDatePicker().setMaxDate(System.currentTimeMillis());
                dpr.show();
            }
        });
        home_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentDone.this, Home.class);
                i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

            }
        });

        if (!objNetworkReceiver.hasInternetConnection(PaymentDone.this)) {
            Toast.makeText(getApplicationContext(), "No NetWork Conection", Toast.LENGTH_LONG).show();
            ((RadioButton) payradioGroup.getChildAt(1)).setChecked(true);
            matchradio.setVisibility(View.GONE);
            donedetailsLayout.setVisibility(View.GONE);
            entryeditLayout.setVisibility(View.VISIBLE);
            verifybtn.setVisibility(View.VISIBLE);
            strVerifyRadio = "MisMatched";

            SharedPreferences prefs = getSharedPreferences("OperatingAllPrefs", 0);
            strScno = prefs.getString("Service_Number", "");

        } else {
            prnumtxt.setText(strPtpr);
            prdatetxt.setText(strPrdate);
            amnttxt.setText(strAmount);

            if (IsNullOrBlank(strPtpr) || IsNullOrBlank(strPrdate) || IsNullOrBlank(strAmount)) {
                ((RadioButton) payradioGroup.getChildAt(1)).setChecked(true);
                matchradio.setVisibility(View.GONE);
                donedetailsLayout.setVisibility(View.GONE);
                entryeditLayout.setVisibility(View.VISIBLE);
                verifybtn.setVisibility(View.VISIBLE);
                strVerifyRadio = "MisMatched";
                SharedPreferences prefs = getSharedPreferences("OperatingAllPrefs", 0);
                strScno = prefs.getString("Service_Number", "");
                Toast.makeText(getApplicationContext(), "No Payment Found for the Current Month", Toast.LENGTH_LONG).show();
            }
        }


        matchradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entryeditLayout.setVisibility(View.GONE);
                verifybtn.setVisibility(View.VISIBLE);
                strVerifyRadio = "Matched";

            }
        });
        mismatchradio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entryeditLayout.setVisibility(View.VISIBLE);
                verifybtn.setVisibility(View.VISIBLE);
                strVerifyRadio = "MisMatched";

            }
        });


        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strVerifyRadio.equals("Matched")) {
                    totalString = "V" + "|" + strScno + "|" + strDiscDate + "|" + strDeviceId+ "|" + lmcode;
                    rdb.addReportsList(new ReportsList(strScno, "V", strDate));
                } else if (strVerifyRadio.equals("MisMatched")) {
                    strPreditno = prnumtxtedit.getText().toString();
                    strPreditdate = prdatetxtedit.getText().toString();
                    streditAmount = amnttxtedit.getText().toString();

                    if (IsNullOrBlank(strPreditno)) {
                        Toast.makeText(getApplicationContext(), "Please Enter Perminent Number", Toast.LENGTH_LONG).show();
                    } else if (IsNullOrBlank(strPreditdate)) {
                        Toast.makeText(getApplicationContext(), "Please Enter Perminent Date", Toast.LENGTH_LONG).show();
                    } else if (IsNullOrBlank(streditAmount)) {
                        Toast.makeText(getApplicationContext(), "Please Enter Amount", Toast.LENGTH_LONG).show();
                    } else {
                        totalString = "P" + "|" + strScno + "|" + strPreditno + "|" + strPreditdate + "|" + streditAmount + "|" + strDiscDate + "|" + strDeviceId + "|" + lmcode;
                        rdb.addReportsList(new ReportsList(strScno, "P", strDate));
                    }
                }
                checkConnectivity();
            }
        });

    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        prdatetxtedit.setText(sdf.format(myCalendar.getTime()));
    }

    public void checkConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (Utility.isValueNullOrEmpty(tv_lat.getText().toString())) {
                Utility.showCustomOKOnlyDialog(this, "Please Tag Location");
            } else {
                if (tv_lat.getText().toString().equalsIgnoreCase(mLat) && tv_long.getText().toString().equalsIgnoreCase(mLong)) {
                    totalString = totalString + "|0|0";
                } else {
                    totalString = totalString + "|" + tv_lat.getText().toString() + "|" + tv_long.getText().toString();
                }
                RequestParams params = new RequestParams();
                params.put("DATA", totalString);
                invokeDetaillVerifyWebService(params);
            }
        } else {
            operatingPreferences = getSharedPreferences("operatingPrefs", MODE_PRIVATE);
            operatingPrefsEditor = operatingPreferences.edit();
            operatingPrefsEditor.putString("StrActivity", "OperatingDetails");
            operatingPrefsEditor.apply();
            db.addFailureDcList(new FailureDcList(totalString));
            //  startActivity(new Intent(PaymentDone.this, Operating.class));
            Toast.makeText(getApplicationContext(), "Saved in your Device and pushed to server once network is Available", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    public void invokeDetaillVerifyWebService(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.UPDATE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");
                    switch (status) {
                        case "SUCCESS":
                            Toast.makeText(getApplicationContext(), "Your values saved Successfully", Toast.LENGTH_LONG).show();
                          /*  Intent i = new Intent(PaymentDone.this, Operating.class);
                            startActivity(i);*/
                            DatabaseHandler db = new DatabaseHandler(PaymentDone.this);
                            db.updateDCList(strScno, tv_lat.getText().toString(), tv_long.getText().toString());
                            finish();
                            break;
                        case "ERROR":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), obj.getString("MESSAGE"), Toast.LENGTH_LONG).show();
                            break;
                        case "FAIL":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), obj.getString("MESSAGE"), Toast.LENGTH_LONG).show();
                            break;
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
                Log.e("error", error.toString());
            }
        });
    }

    public boolean IsNullOrBlank(String Input) {
        if (Input == null)
            return true;
        else
            return Input.trim() == "" || Input.trim().length() == 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        strDate = df.format(c);

        TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);

        checkedPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (Build.VERSION.SDK_INT >= 23 && checkedPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                if (alert != null) {
                    if (alert.isShowing()) {
                        alert.dismiss();
                    }
                }
                alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(false);
                alertBuilder.setTitle("Permission");
                alertBuilder.setMessage("permission is necessary for this event!!!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(PaymentDone.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PHONE_STATE_READ);
                        checkedPermission = PackageManager.PERMISSION_GRANTED;
                    }
                });
                alert = alertBuilder.create();
                alert.show();
            }
        } else {
            checkedPermission = PackageManager.PERMISSION_GRANTED;
        }
        if (checkedPermission != PackageManager.PERMISSION_DENIED) {
            strDeviceId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PHONE_STATE_READ:
                if (grantResults.length > 0 && permissions[0] == Manifest.permission.READ_PHONE_STATE) {
                    checkedPermission = PackageManager.PERMISSION_GRANTED;
                }
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
        if (!Utility.isValueNullOrEmpty(mLat) && !mLat.equalsIgnoreCase("0")) {
            tv_lat.setText(mLat);
            tv_long.setText(mLong);
            mLastLocation = new Location(LocationManager.GPS_PROVIDER);
            mLastLocation.setLatitude(Double.parseDouble(mLat));
            mLastLocation.setLongitude(Double.parseDouble(mLong));
            tv_lat.setText(String.format("%s", mLastLocation.getLatitude()));
            tv_long.setText(String.format("%s", mLastLocation.getLongitude()));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .title("(" + tv_lat.getText().toString() + "," + tv_long.getText().toString() + ")"));
            //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(PaymentDone.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(PaymentDone.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                }
            });
            ll_map.setVisibility(View.VISIBLE);
            ll_lat.setVisibility(View.VISIBLE);
            ll_long.setVisibility(View.VISIBLE);
            buildGoogleApiClient();

        }
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
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(PaymentDone.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
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
                Utility.showToastMessage(this, Utility.getResourcesString(PaymentDone.this, R.string.no_geocoder_available));
            } else {
                tv_lat.setText(String.format("%s", mLastLocation.getLatitude()));
                tv_long.setText(String.format("%s", mLastLocation.getLongitude()));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .title("(" + tv_lat.getText().toString() + "," + tv_long.getText().toString() + ")"));
                //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(PaymentDone.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                        return false;
                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Intent intent = new Intent(PaymentDone.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                    }
                });
                ll_map.setVisibility(View.VISIBLE);
                ll_lat.setVisibility(View.VISIBLE);
                ll_long.setVisibility(View.VISIBLE);
                buildGoogleApiClient();
            }
        } else {
            checkPermissionsAndCallServiceToGetLatAndLng();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
