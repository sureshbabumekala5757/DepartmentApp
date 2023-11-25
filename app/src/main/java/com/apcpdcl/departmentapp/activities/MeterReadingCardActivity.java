package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.ServiceDetailsModel;
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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 22-03-2018.
 */

public class MeterReadingCardActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    @BindView(R.id.btn_get_details)
    Button btn_get_details;
    @BindView(R.id.ll_details)
    LinearLayout ll_details;
    @BindView(R.id.ll_form)
    LinearLayout ll_form;
    @BindView(R.id.et_service_number)
    EditText et_service_number;
    @BindView(R.id.ll_fetch_location)
    LinearLayout ll_fetch_location;
    @BindView(R.id.ll_lat)
    LinearLayout ll_lat;
    @BindView(R.id.ll_long)
    LinearLayout ll_long;
    @BindView(R.id.tv_lat)
    TextView tv_lat;
    @BindView(R.id.tv_long)
    TextView tv_long;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.tv_service_no)
    TextView tv_service_no;
    @BindView(R.id.tv_consumer_name)
    TextView tv_consumer_name;
    @BindView(R.id.tv_category_type)
    TextView tv_category_type;
    @BindView(R.id.tv_load)
    TextView tv_load;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.ll_map)
    LinearLayout ll_map;
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private ServiceDetailsModel serviceDetailsModel;
    private String from;
    ProgressDialog pDialog;
    private String sectionCode = "";
    private String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter_reading_card);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.FROM)) {
            from = intent.getStringExtra(Constants.FROM);
            toolbar_title.setText(from);
            ll_main.setVisibility(View.VISIBLE);
            ll_form.setVisibility(View.VISIBLE);
            ll_details.setVisibility(View.GONE);
        }

        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        userId = prefs.getString("UserName", "");
        pDialog = new ProgressDialog(MeterReadingCardActivity.this);
        buildGoogleApiClient();
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        if (intent.hasExtra(Constants.SERVICE_DETAILS)) {
            serviceDetailsModel = (ServiceDetailsModel) intent.getSerializableExtra(Constants.SERVICE_DETAILS);
            prePopulateData();
        }
        et_service_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 5 && !charSequence.toString().equals(sectionCode)) {
                    Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this,
                            et_service_number.getText().toString() + " Service Number is not related to you");
                    et_service_number.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void getDeviceId() {
       /* TelephonyManager manager = (TelephonyManager) getSystemService(MeterReadingCardActivity.TELEPHONY_SERVICE);
        if (Utility.isMarshmallowOS()) {
            PackageManager pm = this.getPackageManager();
            int hasWritePerm = pm.checkPermission(
                    Manifest.permission.READ_PHONE_STATE,
                    this.getPackageName());

            if (hasWritePerm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        Constants.MY_PERMISSIONS_REQUEST_IMEI);
            } else {
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                Utility.showLog("IMEI", manager.getDeviceId());
            }
        } else {
            Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
            Utility.showLog("IMEI", manager.getDeviceId());
        }*/
        String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER,IMEI_NUMBER);
        Utility.showLog("IMEI", IMEI_NUMBER);

    }

    @OnClick(R.id.btn_submit)
    void postData() {

        if (Utility.isNetworkAvailable(this)) {
            if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
                getDeviceId();
            } else {
                postServiceDetails();
            }
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this,
                    R.string.no_internet));
        }
    }

    private void postServiceDetails() {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("REGNO", serviceDetailsModel.getREGNO());
            requestParams.put("ADDRESS", serviceDetailsModel.getAddress());
            requestParams.put("HOST", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
            requestParams.put("GEOPARTCD", serviceDetailsModel.getREGNO().substring(0, 5));
            requestParams.put("LONGITUDE", "" + tv_long.getText().toString());
            requestParams.put("LATITUDE", "" + tv_lat.getText().toString());
            requestParams.put("USERID", "" + userId);
            HttpEntity entity;
            try {
                entity = new StringEntity(requestParams.toString());
                Utility.showLog("Params", requestParams.toString());
                Utility.showLog("URL", Constants.URL + Constants.LATLONGINPUT);
                client.post(this, Constants.URL + Constants.LATLONGINPUT, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utility.showLog("onSuccess", response);
                /*if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }*/
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("status")) {
                                if (jsonObject.optString("status").equalsIgnoreCase("S")) {
                                    showCustomOKOnlyDialog("Updated Successfully");
                                } else {
                                    Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this, jsonObject.optString("error"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        Utility.showLog("error", error.toString());
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this, "Something Went Wrong, Please Try Again.");
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void prePopulateData() {
        toolbar_title.setText(Utility.getResourcesString(this, R.string.tagg_location));
        ll_form.setVisibility(View.GONE);
        ll_details.setVisibility(View.VISIBLE);
        ll_main.setVisibility(View.VISIBLE);
        tv_service_no.setText(serviceDetailsModel.getREGNO());
        tv_consumer_name.setText(serviceDetailsModel.getConsumerName());
        tv_category_type.setText(serviceDetailsModel.getCategory());
        tv_load.setText(serviceDetailsModel.getLoad());
        tv_address.setText(serviceDetailsModel.getAddress());
        if (mMap != null) {
            if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getLatitude()) && !serviceDetailsModel.getLatitude().equalsIgnoreCase("0")) {
                mLastLocation = new Location(LocationManager.GPS_PROVIDER);
                mLastLocation.setLatitude(Double.parseDouble(serviceDetailsModel.getLatitude()));
                mLastLocation.setLongitude(Double.parseDouble(serviceDetailsModel.getLongitude()));
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
                        Intent intent = new Intent(MeterReadingCardActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                        return false;
                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Intent intent = new Intent(MeterReadingCardActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                    }
                });
                ll_map.setVisibility(View.VISIBLE);
                ll_lat.setVisibility(View.VISIBLE);
                ll_long.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
            }
        }

    }

    @OnClick(R.id.ll_fetch_location)
    void fetchLatLang() {
        checkPermissionsAndCallServiceToGetLatAndLng();
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
                            status.startResolutionForResult(MeterReadingCardActivity.this, REQUEST_CHECK_SETTINGS);
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
                Utility.showToastMessage(this, Utility.getResourcesString(MeterReadingCardActivity.this, R.string.no_geocoder_available));
            } else {
                mMap.clear();
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
                        Intent intent = new Intent(MeterReadingCardActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                        return false;
                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Intent intent = new Intent(MeterReadingCardActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                    }
                });
                ll_map.setVisibility(View.VISIBLE);
                ll_lat.setVisibility(View.VISIBLE);
                ll_long.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                //ll_fetch_location.setVisibility(View.GONE);
            }
            buildGoogleApiClient();

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
                    TelephonyManager manager = (TelephonyManager) getSystemService(MeterReadingCardActivity.TELEPHONY_SERVICE);
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
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ll_fetch_location.performClick();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    public void showCustomOKOnlyDialog(String message) {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(this);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_ok);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);

            TextView txt_msz = (TextView) dialog_confirm.findViewById(R.id.txt_heading);
            txt_msz.setText(message);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_confirm.dismiss();
                    tv_lat.setText("");
                    tv_long.setText("");
                    finish();
                }
            });
            dialog_confirm.show();
            dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Constants.isDialogOpen = false;
                }
            });
        }
    }

    @OnClick(R.id.btn_get_details)
    void implementSearch() {
        if (Utility.isValueNullOrEmpty(et_service_number.getText().toString()) || et_service_number.getText().toString().length() < 20) {
            Utility.showCustomOKOnlyDialog(this, "Please enter valid 20 digits Registration Number.");
        } else {
            if (et_service_number.getText().toString().substring(0, 5).equalsIgnoreCase(sectionCode)) {
                if (Utility.isNetworkAvailable(this)) {
                    getServiceDetails(et_service_number.getText().toString());
                } else {
                    Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this,
                            R.string.no_internet));
                }
            } else {
                Utility.showCustomOKOnlyDialog(this, "This Registration Number is not related to you.");
            }
        }
    }

    private void getServiceDetails(String serviceNumber) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + Constants.REG_DETAILS + "/" + serviceNumber);
        client.get(Constants.URL + Constants.REG_DETAILS + "/" + serviceNumber, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("RESPONSE")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
                        if (jsonArray.length() > 0) {
                            JSONObject json = jsonArray.optJSONObject(0);
                            if (json.has("STATUS")) {
                                Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this,
                                        json.optString("STATUSMSG"));
                                et_service_number.setText("");
                            } else {
                                serviceDetailsModel = new Gson().fromJson(json.toString(),
                                        ServiceDetailsModel.class);
                                if (from.equalsIgnoreCase("Edit") &&
                                        (serviceDetailsModel.getLongitude().equalsIgnoreCase("0") &&
                                                serviceDetailsModel.getLatitude().equalsIgnoreCase("0"))) {
                                    Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this,
                                            "Location Not tagged yet for this service.");
                                } else if (from.equalsIgnoreCase("Search") &&
                                        (!serviceDetailsModel.getLongitude().equalsIgnoreCase("0") &&
                                                !serviceDetailsModel.getLatitude().equalsIgnoreCase("0"))) {
                                    Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this,
                                            "Location Already tagged for this service.");
                                } else {
                                    prePopulateData();
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this,
                            "Something went wrong please try again later...");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(MeterReadingCardActivity.this,
                        "Something went wrong please try again later...");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (serviceDetailsModel != null) {
            if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getLatitude()) && !serviceDetailsModel.getLatitude().equalsIgnoreCase("0")) {
                mLastLocation = new Location(LocationManager.GPS_PROVIDER);
                mLastLocation.setLatitude(Double.parseDouble(serviceDetailsModel.getLatitude()));
                mLastLocation.setLongitude(Double.parseDouble(serviceDetailsModel.getLongitude()));
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
                        Intent intent = new Intent(MeterReadingCardActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                        return false;
                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Intent intent = new Intent(MeterReadingCardActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                    }
                });
                ll_map.setVisibility(View.VISIBLE);
                ll_lat.setVisibility(View.VISIBLE);
                ll_long.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
            }
        }
    }
}
