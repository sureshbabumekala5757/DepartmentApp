package com.apcpdcl.departmentapp.activities;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.apcpdcl.departmentapp.BuildConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 28-03-2018.
 */

public class AeDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.iv_notifications)
    ImageView iv_notifications;

    @BindView(R.id.ll_tarif_plan)
    LinearLayout ll_tarif_plan;

    @BindView(R.id.ll_geo_tagging)
    LinearLayout ll_geo_tagging;
    @BindView(R.id.iv_geo_tagging)
    ImageView iv_geo_tagging;
    @BindView(R.id.tv_geo_tagging)
    TextView tv_geo_tagging;

    @BindView(R.id.ll_sub_station)
    LinearLayout ll_sub_station;
    @BindView(R.id.iv_sub_station)
    ImageView iv_sub_station;
    @BindView(R.id.tv_sub_station)
    TextView tv_sub_station;

    @BindView(R.id.ll_meter_change)
    LinearLayout ll_meter_change;
    @BindView(R.id.iv_meter_change)
    ImageView iv_meter_change;
    @BindView(R.id.tv_meter_change)
    TextView tv_meter_change;

    @BindView(R.id.ll_ccc_complaints)
    LinearLayout ll_ccc_complaints;
    @BindView(R.id.iv_ccc_complaints)
    ImageView iv_ccc_complaints;
    @BindView(R.id.tv_ccc_complaints)
    TextView tv_ccc_complaints;

    @BindView(R.id.ll_chk_request)
    LinearLayout ll_chk_request;
    @BindView(R.id.iv_chk_request)
    ImageView iv_chk_request;
    @BindView(R.id.tv_chk_request)
    TextView tv_chk_request;
    @BindView(R.id.tv_reports)
    TextView tv_reports;


    @BindView(R.id.ll_service_details)
    LinearLayout ll_service_details;
    @BindView(R.id.iv_service_details)
    ImageView iv_service_details;
    @BindView(R.id.tv_service_details)
    TextView tv_service_details;

    @BindView(R.id.ll_oms)
    LinearLayout ll_oms;
    @BindView(R.id.iv_oms)
    ImageView iv_oms;
    @BindView(R.id.tv_oms)
    TextView tv_oms;

    @BindView(R.id.ll_feeder)
    LinearLayout ll_feeder;
    @BindView(R.id.iv_feeder)
    ImageView iv_feeder;
    @BindView(R.id.tv_feeder)
    TextView tv_feeder;

    @BindView(R.id.ll_lc_operations)
    LinearLayout ll_lc_operations;
    @BindView(R.id.iv_lc_operations)
    ImageView iv_lc_operations;
    @BindView(R.id.tv_lc_operations)
    TextView tv_lc_operations;

    @BindView(R.id.ll_dc_operations)
    LinearLayout ll_dc_operations;
    @BindView(R.id.iv_dc_operations)
    ImageView iv_dc_operations;
    @BindView(R.id.tv_dc_operations)
    TextView tv_dc_operations;

    @BindView(R.id.ll_survey)
    LinearLayout ll_survey;
    @BindView(R.id.iv_survey)
    ImageView iv_survey;
    @BindView(R.id.tv_survey)
    TextView tv_survey;

    @BindView(R.id.ll_eeps)
    LinearLayout ll_eeps;
    @BindView(R.id.iv_eeps)
    ImageView iv_eeps;
    @BindView(R.id.tv_eeps)
    TextView tv_eeps;

    @BindView(R.id.ll_dc_operations_mats)
    LinearLayout ll_dc_operations_mats;
    @BindView(R.id.iv_dc_operations_mats)
    ImageView iv_dc_operations_mats;
    @BindView(R.id.tv_dc_operations_mats)
    TextView tv_dc_operations_mats;


    @BindView(R.id.ll_dtr_tracking)
    LinearLayout ll_dtr_tracking;
    @BindView(R.id.iv_dtr_tracking)
    ImageView iv_dtr_tracking;
    @BindView(R.id.tv_dtr_tracking)
    TextView tv_dtr_tracking;

    @BindView(R.id.ll_dtr_geotagging)
    LinearLayout ll_dtr_geotagging;
    @BindView(R.id.iv_dtr_geotagging)
    ImageView iv_dtr_geotagging;
    @BindView(R.id.tv_dtr_geotagging)
    TextView tv_dtr_geotagging;
    @BindView(R.id.logo)
    ImageView iv_logo;

    @BindView(R.id.ll_polesdtrs)
    LinearLayout ll_polesdtrs;
    @BindView(R.id.iv_polesdtrs)
    ImageView iv_polesdtrs;
    @BindView(R.id.tv_polesdtrs)
    TextView tv_polesdtrs;

    @BindView(R.id.ll_itequipment)
    LinearLayout ll_itequipment;
    @BindView(R.id.iv_itequipment)
    ImageView iv_itequipment;
    @BindView(R.id.tv_itequipment)
    TextView tv_itequipment;

    @BindView(R.id.ll_seal_bits)
    LinearLayout ll_seal_bits;
    @BindView(R.id.iv_seal_bits)
    ImageView iv_seal_bits;
    @BindView(R.id.tv_seal_bits)
    TextView tv_seal_bits;


    @BindView(R.id.ll_load)
    LinearLayout ll_load;

    @BindView(R.id.ll_Add_pole_data)
    LinearLayout ll_Add_pole_data;
    @BindView(R.id.iv_add_pole_data)
    ImageView iv_add_pole_data;
    @BindView(R.id.tv_add_pole_data)
    TextView tv_add_pole_data;

    @BindView(R.id.ll_ctmeters)
    LinearLayout ll_ctmeters;
    @BindView(R.id.iv_ctmeters)
    ImageView iv_ctmeters;
    @BindView(R.id.tv_ctmeters)
    TextView tv_ctmeters;

    private ProgressDialog pDialog;
    private DownloadManager downloadManager;
    private long downloadReference;
    private File file;
    private boolean doubleBackToExitPressedOnce = false;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ae_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_version).setTitle("Version " + BuildConfig.VERSION_NAME);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        menu.findItem(R.id.action_user).setTitle(prefs.getString("UserName", ""));
        return super.onPrepareOptionsMenu(menu);
    }

    /*Initialize Views*/
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        userName = prefs.getString("UserName", "");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.call_service));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askPermissions();
        }
        Utility.getMeterMake(this);
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        if (Utility.isNetworkAvailable(this)) {
            RequestParams params = new RequestParams();
            params.put("APK", "LMAPP");
            params.put("VERSION", BuildConfig.VERSION_NAME);
            invokeAPKCheckWebService(params);
       /*     String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (Utility.getSharedPrefStringData(this, Constants.FCM_TOKEN).equalsIgnoreCase("")) {
                Utility.sendToken(this);
            } else if (!Utility.getSharedPrefStringData(this, Constants.FCM_TOKEN).equalsIgnoreCase(refreshedToken)) {
                Utility.updateToken(this);
            }*/
        } else {
            //Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }

        //new PollingDialogClass().getPollingData(this, Utility.getSharedPrefStringData(this, Constants.USER_NAME));
    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

  /*  @OnClick({R.id.logo})
    void GeoLatlong() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(AeDashBoardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(AeDashBoardActivity

                        .this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude_V = location.getLongitude();
        double latitude_V = location.getLatitude();
        Toast.makeText(getApplicationContext(),String.valueOf(longitude_V+":"+latitude_V),Toast.LENGTH_LONG).show();
    }*/


    @OnClick({R.id.ll_dc_operations_mats, R.id.iv_dc_operations_mats, R.id.tv_dc_operations_mats})
    void navigateToMatsDCList() {
        Intent in = new Intent(getApplicationContext(), MatsDCListDashBoardActivity.class);
        startActivity(in);
    }
    @OnClick({R.id.ll_dtr_tracking, R.id.iv_dtr_tracking, R.id.tv_dtr_tracking})
    void navigateToDtrTracking() {
        Intent in = new Intent(getApplicationContext(), DTRTrackingActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_dtr_geotagging, R.id.iv_dtr_geotagging, R.id.tv_dtr_geotagging})
    void navigateToDtrComplaintsGeotagging() {
        Intent in = new Intent(getApplicationContext(), PendingDTRComplaintsActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_polesdtrs, R.id.iv_polesdtrs, R.id.tv_polesdtrs})
    void navigateTopolesDtrs() {
        Intent in = new Intent(getApplicationContext(), PolesDtrsActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_itequipment, R.id.iv_itequipment, R.id.tv_itequipment})
    void navigateToEquipment() {
        Intent in = new Intent(getApplicationContext(), ITEquipmentActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_seal_bits, R.id.iv_seal_bits, R.id.tv_seal_bits})
    void navigateToSealBits() {
        Intent in = new Intent(getApplicationContext(), SealBitsActivity.class);
        startActivity(in);
    }

    /*Check Latest version API*/
    private void invokeAPKCheckWebService(RequestParams params) {
        pDialog = new ProgressDialog(AeDashBoardActivity.this);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setMessage("Checking for updates...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.APK_CHK, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");
                    final String LVersion = obj.getString("LVERSION");
                    switch (status) {
                        case "TRUE":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            break;
                        case "FALSE":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            //start downloading the file using the download manager
                            String fileName = "Apcpdcl_DepartmentApp_" + LVersion + ".apk";
                            file = new File(Environment.getExternalStorageDirectory() + "/download/", fileName);

                            // Utility.openWebPage(LMDashBoardActivity.this,url);
                            if (Utility.isNetworkAvailable(AeDashBoardActivity.this)) {
                                Uri download_Uri = Uri.parse(Constants.DOWNLOAD_PATH+ LVersion + ".apk");
                                pDialog = new ProgressDialog(AeDashBoardActivity.this);
                                pDialog.setMessage("Please Wait APCPDCL DepartmentApp latest version downloading...");
                                pDialog.setCancelable(false);
                                pDialog.show();
                                downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                DownloadManager.Request request = new DownloadManager.Request(download_Uri);
                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                                request.setAllowedOverRoaming(true);
                                if (Environment.getExternalStorageState() == null) {
                                    file = new File(Environment.getDataDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/Apcpdcl_DepartmentApp_" + LVersion + ".apk");
                                } else if (Environment.getExternalStorageState() != null) {
                                    file = new File(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/Apcpdcl_DepartmentApp_" + LVersion + ".apk");
                                }
                                request.setDestinationInExternalFilesDir(AeDashBoardActivity.this, Environment.DIRECTORY_DOWNLOADS, "Apcpdcl_DepartmentApp_" + LVersion + ".apk");
                                downloadReference = downloadManager.enqueue(request);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                            }

                            break;
                        default:
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
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
                switch (statusCode) {
                    case 404:
                        Toast.makeText(getApplicationContext(), "Unable to Connect Server", Toast.LENGTH_LONG).show();
                        break;
                    case 500:
                        Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
                Utility.showLog("error", error.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Utility.callLogout(this, pDialog);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.getMeterMake(this);
        //Broadcast receiver for the download manager
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downLoadReciever, filter);
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(downLoadReciever);
        super.onDestroy();
    }


    @OnClick({R.id.ll_geo_tagging, R.id.iv_geo_tagging, R.id.tv_geo_tagging})
    void navigateToSearch() {
        if (Utility.isLocationEnabled(this)) {
            Intent in = new Intent(getApplicationContext(), GeoDashBoardActivity.class);
            startActivity(in);
        } else {
            displayLocationSettingsRequest();
        }
    }
    @OnClick(R.id.ll_load)
    void navigateToLoadForecast() {
        Intent in = new Intent(getApplicationContext(), LoadForecastDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.ll_tarif_plan)
    void openPdfFile() {

        Intent in = new Intent(getApplicationContext(), TeluguTariffPlanActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_survey, R.id.iv_survey, R.id.tv_survey})
    void navigateToSurveys() {
        Intent in = new Intent(getApplicationContext(), SurveyDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.iv_notifications})
    void navigateToNotifications() {
        Intent in = new Intent(getApplicationContext(), NotificationsListActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.tv_reports)
    void openWeb() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constants.FROM, Constants.AE_DASHBOARD);
        startActivity(intent);
    }

    @OnClick({R.id.ll_meter_change, R.id.iv_meter_change, R.id.tv_meter_change})
    void navigateToMeterChange() {
        Intent in = new Intent(getApplicationContext(), MeterChangeDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_oms, R.id.iv_oms, R.id.tv_oms})
    void navigateToOMSDashBoard() {
        Intent in = new Intent(getApplicationContext(), OMSDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_feeder, R.id.iv_feeder, R.id.tv_feeder})
    void navigateToFeederOutage() {
        Intent in = new Intent(getApplicationContext(), FeederOutagesActivity.class);
        in.putExtra(Constants.FROM, "false");
        startActivity(in);
    }

    @OnClick({R.id.ll_lc_operations, R.id.iv_lc_operations, R.id.tv_lc_operations})
    void navigateToLCOperations() {
        Intent in = new Intent(getApplicationContext(), AELCRequestListActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_dc_operations, R.id.iv_dc_operations, R.id.tv_dc_operations})
    void navigateToDCListTransactions() {
        Intent in = new Intent(getApplicationContext(), AEDcListTrackingActivity.class);
        in.putExtra(Constants.USER_ID, userName);
        startActivity(in);
    }

    @OnClick({R.id.ll_sub_station, R.id.iv_sub_station, R.id.tv_sub_station})
    void navigateToEdit() {
        if (Utility.isLocationEnabled(this)) {
           /* Intent in = new Intent(getApplicationContext(), SubOperationsActivity.class);
            startActivity(in);*/
            Intent intent = new Intent(this, SubStationListActivity.class);
            startActivity(intent);
        } else {
            displayLocationSettingsRequest();
        }
    }

    @OnClick({R.id.ll_ccc_complaints, R.id.iv_ccc_complaints, R.id.tv_ccc_complaints})
    void navigateToComplaintsList() {
        Intent in = new Intent(getApplicationContext(), ComplaintsListActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_chk_request, R.id.iv_chk_request, R.id.tv_chk_request})
    void navigateToCheckReading() {
        Intent in = new Intent(getApplicationContext(), CheckReadingActivity.class);
        in.putExtra(Constants.FROM, AeDashBoardActivity.class.getSimpleName());
        startActivity(in);
    }

    @OnClick({R.id.ll_service_details, R.id.iv_service_details, R.id.tv_service_details})
    void navigateToServiceDetails() {
        Intent in = new Intent(getApplicationContext(), ServiceDetailsActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_eeps, R.id.iv_eeps, R.id.tv_eeps})
    void navigateToEepsList() {
        Intent in = new Intent(getApplicationContext(), EepsUnitActivity.class);
        in.putExtra(Constants.FROM, AeDashBoardActivity.class.getSimpleName());
        startActivity(in);
    }
    //Pole Data
    @OnClick({R.id.ll_Add_pole_data, R.id.iv_add_pole_data, R.id.tv_add_pole_data})
    void navigateToAddPoleData() {
        Intent in = new Intent(getApplicationContext(), AddPoleData.class);
        startActivity(in);
    }
    //CT METER
    @OnClick({R.id.ll_ctmeters, R.id.iv_ctmeters, R.id.tv_ctmeters})
    void navigateToCTmeters() {
        Intent in = new Intent(getApplicationContext(), CTMeterServiceListActivity.class);
        //in.putExtra("ACTION", "meterChange");
        startActivity(in);
    }
    //CT METER end

    public void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(AeDashBoardActivity.this, GPSTrackerActivity.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Utility.showLog("PendingIntent", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Utility.showLog("Location", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    private BroadcastReceiver downLoadReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            try {
                SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
                if (!Utility.isValueNullOrEmpty(preferences.toString())) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Utility.clearDatabase(AeDashBoardActivity.this);
            if (downloadReference == referenceId) {
                //start the installation of the latest version
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                /*Need to here dismiss the dialog*/
                pDialog.dismiss();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | installIntent.FLAG_ACTIVITY_CLEAR_TASK);
                    installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
                            "application/vnd.android.package-archive");
                } else {
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | installIntent.FLAG_ACTIVITY_CLEAR_TASK);
                    installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
                            "application/vnd.android.package-archive");
                }
                startActivity(installIntent);

            }
        }
    };


    /*Ask Multiple Permissions*/
    private void askPermissions() {
        int cameraPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storagePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int coarseLocPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int phonePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE);
        int readPhonePerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE);
        int readSMSPerm = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        List<String> permissionsList = new ArrayList<>();
        if (cameraPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.CAMERA);
        }
        if (storagePerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (fineLocPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarseLocPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (phonePerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.CALL_PHONE);
        }
        if (readPhonePerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.READ_PHONE_STATE);
        }
        if (readSMSPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (!permissionsList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsList.toArray
                    (new String[permissionsList.size()]), Constants.REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void getDeviceId() {
      /*  TelephonyManager manager = (TelephonyManager) getSystemService(GPSTrackerActivity.TELEPHONY_SERVICE);
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
        }
    }
}
