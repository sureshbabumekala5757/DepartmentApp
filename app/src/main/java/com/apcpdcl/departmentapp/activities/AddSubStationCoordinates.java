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
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddSubStationCoordinates extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.btn_corner_one)
    Button btn_corner_one;
    @BindView(R.id.btn_corner_two)
    Button btn_corner_two;
    @BindView(R.id.btn_corner_three)
    Button btn_corner_three;
    @BindView(R.id.btn_corner_four)
    Button btn_corner_four;
    @BindView(R.id.btn_corner_center)
    Button btn_corner_center;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.ll_corner_one)
    LinearLayout ll_corner_one;
    @BindView(R.id.ll_corner_two)
    LinearLayout ll_corner_two;
    @BindView(R.id.ll_corner_three)
    LinearLayout ll_corner_three;
    @BindView(R.id.ll_corner_four)
    LinearLayout ll_corner_four;
    @BindView(R.id.ll_corner_center)
    LinearLayout ll_corner_center;

    @BindView(R.id.tv_latitude_one)
    TextView tv_latitude_one;
    @BindView(R.id.tv_longitude_one)
    TextView tv_longitude_one;

    @BindView(R.id.tv_latitude_two)
    TextView tv_latitude_two;
    @BindView(R.id.tv_longitude_two)
    TextView tv_longitude_two;

    @BindView(R.id.tv_latitude_three)
    TextView tv_latitude_three;
    @BindView(R.id.tv_longitude_three)
    TextView tv_longitude_three;

    @BindView(R.id.tv_latitude_four)
    TextView tv_latitude_four;
    @BindView(R.id.tv_longitude_four)
    TextView tv_longitude_four;

    @BindView(R.id.tv_latitude_center)
    TextView tv_latitude_center;
    @BindView(R.id.tv_longitude_center)
    TextView tv_longitude_center;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean mIsCornerOne = false;
    private boolean mIsCornerTwo = false;
    private boolean mIsCornerThree = false;
    private boolean mIsCornerFour = false;
    private boolean mIsCornerCenter = false;
    //protected Location mLastLocation;
    private String sectionCode = "";
    private String ssCode = "";
    private String ssName = "";
    private ProgressDialog prgDialog;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_substation_coordinates_activity);
        ButterKnife.bind(this);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ssCode = extras.getString("SS_CODE");
            ssName = extras.getString("SS_NAME");
        }
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);
        prgDialog.setMessage("Fetching Location...");
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
    }

    @OnClick(R.id.btn_corner_one)
    void fetchCornerOneCoordinates() {
        buildGoogleApiClient();
        mIsCornerOne = true;
        checkPermissionsAndCallServiceToGetLatAndLng();
    }

    @OnClick(R.id.btn_corner_two)
    void fetchCornerTwoCoordinates() {
        buildGoogleApiClient();
        mIsCornerTwo = true;
        checkPermissionsAndCallServiceToGetLatAndLng();
    }

    @OnClick(R.id.btn_corner_three)
    void fetchCornerThreeCoordinates() {
        buildGoogleApiClient();
        mIsCornerThree = true;
        checkPermissionsAndCallServiceToGetLatAndLng();
    }

    @OnClick(R.id.btn_corner_four)
    void fetchCornerFourCoordinates() {
        buildGoogleApiClient();
        mIsCornerFour = true;
        checkPermissionsAndCallServiceToGetLatAndLng();
    }

    @OnClick(R.id.btn_corner_center)
    void fetchCornerCenterCoordinates() {
        buildGoogleApiClient();
        mIsCornerCenter = true;
        checkPermissionsAndCallServiceToGetLatAndLng();
    }

    @OnClick(R.id.btn_submit)
    void postData() {
        if (btn_corner_center.getVisibility() == View.GONE &&
                btn_corner_four.getVisibility() == View.GONE &&
                btn_corner_three.getVisibility() == View.GONE &&
                btn_corner_two.getVisibility() == View.GONE &&
                btn_corner_one.getVisibility() == View.GONE) {
            if (Utility.isNetworkAvailable(this)) {
                saveCoordinates();
            } else {
                Utility.showCustomOKOnlyDialog(this, "Please Check Your Internet Connection and Try Again");
            }
        } else {
            Utility.showCustomOKOnlyDialog(this, "Please add all coordinates to submit the details.");
        }
    }

    private void saveCoordinates() {
        prgDialog.show();
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");

        String data = sectionCode + "|" + ssCode + "|" + ssName + "|" +
                tv_longitude_center.getText().toString() + "|" + tv_latitude_center.getText().toString() + "|" +
                tv_longitude_one.getText().toString() + "|" + tv_latitude_one.getText().toString() + "|" +
                tv_longitude_two.getText().toString() + "|" + tv_latitude_two.getText().toString() + "|" +
                tv_longitude_three.getText().toString() + "|" + tv_latitude_three.getText().toString() + "|" +
                tv_longitude_four.getText().toString() + "|" + tv_latitude_four.getText().toString();

        RequestParams params = new RequestParams();
        params.put("DATA", data);

        Utility.showLog("DATA", data);
        Utility.showLog("Url", Constants.URL + Constants.SUBSTATION_COORDINATES);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.URL + Constants.SUBSTATION_COORDINATES, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);

                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("STATUS")) {
                        if (jsonObject.optString("STATUS").equalsIgnoreCase("ERROR")) {
                            if (jsonObject.has("MSG")) {
                                Utility.showCustomOKOnlyDialog(AddSubStationCoordinates.this, jsonObject.optString("MSG"));
                            }
                        } else {
                            if (jsonObject.has("MSG")) {
                                SubStationListActivity.getInstance().updateList(ssCode);
                                showCustomOKOnlyDialog(jsonObject.optString("MSG"));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Utility.showCustomOKOnlyDialog(AddSubStationCoordinates.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(AddSubStationCoordinates.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(AddSubStationCoordinates.this, error.getMessage());
                        break;
                }
            }
        });
    }

    private void setLatLong(Location latLong) {
        if (mIsCornerOne) {
            mIsCornerOne = false;
            btn_corner_one.setVisibility(View.GONE);
            ll_corner_one.setVisibility(View.VISIBLE);
            tv_latitude_one.setText(String.format("%s", latLong.getLatitude()));
            tv_longitude_one.setText(String.format("%s", latLong.getLongitude()));
        } else if (mIsCornerTwo) {
            mIsCornerTwo = false;
            btn_corner_two.setVisibility(View.GONE);
            ll_corner_two.setVisibility(View.VISIBLE);
            tv_latitude_two.setText(String.format("%s", latLong.getLatitude()));
            tv_longitude_two.setText(String.format("%s", latLong.getLongitude()));
        } else if (mIsCornerThree) {
            btn_corner_three.setVisibility(View.GONE);
            ll_corner_three.setVisibility(View.VISIBLE);
            tv_latitude_three.setText(String.format("%s", latLong.getLatitude()));
            tv_longitude_three.setText(String.format("%s", latLong.getLongitude()));
            mIsCornerThree = false;
        } else if (mIsCornerFour) {
            mIsCornerFour = false;
            btn_corner_four.setVisibility(View.GONE);
            ll_corner_four.setVisibility(View.VISIBLE);
            tv_latitude_four.setText(String.format("%s", latLong.getLatitude()));
            tv_longitude_four.setText(String.format("%s", latLong.getLongitude()));
        } else if (mIsCornerCenter) {
            mIsCornerCenter = false;
            btn_corner_center.setVisibility(View.GONE);
            ll_corner_center.setVisibility(View.VISIBLE);
            tv_latitude_center.setText(String.format("%s", latLong.getLatitude()));
            tv_longitude_center.setText(String.format("%s", latLong.getLongitude()));
        }
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mFusedLocationClient.flushLocations();
        }
        if (prgDialog != null && prgDialog.isShowing()){
            prgDialog.dismiss();
        }
    }

    @SuppressLint("HardwareIds")
    private void getDeviceId() {
       /* TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (Utility.isMarshmallowOS()) {
            PackageManager pm = this.getPackageManager();
            int hasWritePerm = pm.checkPermission(
                    Manifest.permission.READ_PHONE_STATE,
                    this.getPackageName());

            if (hasWritePerm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        Constants.MY_PERMISSIONS_REQUEST_IMEI);
            } else {
                assert manager != null;
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                Utility.showLog("IMEI", manager.getDeviceId());
            }
        } else {
            assert manager != null;
            Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
            Utility.showLog("IMEI", manager.getDeviceId());
        }
*/

        String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER,IMEI_NUMBER);
        Utility.showLog("IMEI", IMEI_NUMBER);

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
               /* int hasCoarseLocationPerm = pm.checkPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        getPackageName());*/
            if (hasFineLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.MY_PERMISSIONS_REQUEST_LOCATION_SINGLE);
                } else {
                    if (Utility.isLocationEnabled(this)) {
                        if (Utility.isNetworkAvailable(this)) {
                            //createLocationRequest();
                            prgDialog.show();
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        } else {
                            showCustomOKOnlyDialog(Utility.getResourcesString(this, R.string.no_internet));
                        }
                    } else {
                        createLocationRequest();
                    }
                }
            } else {
                if (Utility.isLocationEnabled(this)) {
                    if (Utility.isNetworkAvailable(this)) {
                        prgDialog.show();
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    } else {
                        showCustomOKOnlyDialog(Utility.getResourcesString(this, R.string.no_internet));
                    }
                } else {
                    createLocationRequest();
                }
            }
        } else {
            showCustomOKOnlyDialog(
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void createLocationRequest() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        Utility.showLog("LocationSettingsStatusCodes.RESOLUTION_REQUIRED",
                                "LocationSettingsStatusCodes.RESOLUTION_REQUIRED" + LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(AddSubStationCoordinates.this, REQUEST_CHECK_SETTINGS);
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
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_IMEI:
               /* if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager manager = (TelephonyManager) getSystemService(AddSubStationCoordinates.TELEPHONY_SERVICE);
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
    protected void onResume() {
        super.onResume();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
         /*   if (locationResult.getLocations().size() > 0){
                setLatLong(locationResult.getLocations().get(0));
            }*/
            for (Location location : locationResult.getLocations()) {
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                setLatLong(location);
            }
        }

    };

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
