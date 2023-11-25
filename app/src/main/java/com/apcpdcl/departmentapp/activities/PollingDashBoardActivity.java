package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
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

public class PollingDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_new_pole)
    Button btn_new_pole;
    @BindView(R.id.btn_existing)
    Button btn_existing;

    private ProgressDialog pDialog;
    private DownloadManager downloadManager;
    private long downloadReference;
    private File file;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polling_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    /*Initialize Views*/
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        //Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.call_service));
        // tv_reports.setPaintFlags(tv_reports.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askPermissions();
        }
        if (Utility.isNetworkAvailable(this)) {
            RequestParams params = new RequestParams();
            params.put("APK", "LMAPP");
            params.put("VERSION", BuildConfig.VERSION_NAME);
            invokeAPKCheckWebService(params);
      /*      String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (Utility.getSharedPrefStringData(this, Constants.FCM_TOKEN).equalsIgnoreCase("")) {
                Utility.sendToken(this);
            } else if (!Utility.getSharedPrefStringData(this, Constants.FCM_TOKEN).equalsIgnoreCase(refreshedToken)) {
                Utility.updateToken(this);
            }*/
        } else {
            //Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }

        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
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

    /*Check Latest version API*/
    private void invokeAPKCheckWebService(RequestParams params) {
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
                            if (Utility.isNetworkAvailable(PollingDashBoardActivity.this)) {
                                Uri download_Uri = Uri.parse(Constants.DOWNLOAD_PATH+ LVersion + ".apk");
                                pDialog = new ProgressDialog(PollingDashBoardActivity.this);
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
                                request.setDestinationInExternalFilesDir(PollingDashBoardActivity.this, Environment.DIRECTORY_DOWNLOADS, "Apcpdcl_DepartmentApp_" + LVersion + ".apk");
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_version).setTitle("Version " + BuildConfig.VERSION_NAME);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        menu.findItem(R.id.action_user).setTitle(prefs.getString("UserName", ""));
        return super.onPrepareOptionsMenu(menu);
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
        //Broadcast receiver for the download manager
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downLoadReciever, filter);
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(downLoadReciever);
        super.onDestroy();
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
            Utility.clearDatabase(PollingDashBoardActivity.this);
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
        int cameraPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int storagePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int coarseLocPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fineLocPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int phonePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int readPhonePerm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int readSMSPerm = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        List<String> permissionsList = new ArrayList<>();
        if (cameraPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CAMERA);
        }
        if (storagePerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (fineLocPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarseLocPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (phonePerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.CALL_PHONE);
        }
        if (readPhonePerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (readSMSPerm != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.RECEIVE_SMS);
        }
        if (!permissionsList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsList.toArray
                    (new String[permissionsList.size()]), Constants.REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

    private void getDeviceId() {
       /* TelephonyManager manager = (TelephonyManager) getSystemService(GPSTrackerActivity.TELEPHONY_SERVICE);
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

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_IMEI) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager manager = (TelephonyManager) getSystemService(GPSTrackerActivity.TELEPHONY_SERVICE);
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                Utility.showLog("IMEI", manager.getDeviceId());
            }
        }
    }

    @OnClick(R.id.btn_existing)
    void navigateToEdit() {
        Intent in = new Intent(this, ExistingPollListActivity.class);
        startActivity(in);

    }

    @OnClick(R.id.btn_new_pole)
    void navigateToPending() {
        Intent in = new Intent(this, NewPollActivity.class);
        startActivity(in);
    }
}
