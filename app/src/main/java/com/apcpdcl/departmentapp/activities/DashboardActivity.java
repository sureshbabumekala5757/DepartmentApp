package com.apcpdcl.departmentapp.activities;

import static com.apcpdcl.departmentapp.utils.Utils.IsNullOrBlank;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.BuildConfig;
import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.DashBoardAdapter;
import com.apcpdcl.departmentapp.models.AbstractModel;
import com.apcpdcl.departmentapp.models.DashBoard;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity implements DashBoardAdapter.OnItemClick {
    //Reports button
    @BindView(R.id.ll_reports)
    LinearLayout ll_reports;
    private ProgressDialog pDialog;
    private AbstractModel abstractModel;
    private RecyclerView rv_DBTabs;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    private String sUserName, sUserCode, sec_code;

    private DashBoardAdapter dashBoardAdapter;
    ArrayList<DashBoard> myList;
    ArrayList<String> title;
    ArrayList<String> imageUrl;
    private Dialog mDialog;
    private File file;
    private DownloadManager downloadManager;
    private long downloadReference;
    private EditText et_dialog_username, et_dialog_otp, et_dialog_password, et_dialog_conf_password;
    EditText et_username, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        init();
    }

    /*Initialize Views*/
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //Shared Data
        sUserName = AppPrefs.getInstance(this).getString("USERNAME", "");
        sUserCode = AppPrefs.getInstance(this).getString("USERID", "");
        sec_code = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");

        rv_DBTabs = findViewById(R.id.rv_DBTabs);
        title = new ArrayList<>();
        imageUrl = new ArrayList<>();
        myList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askPermissions();
        }

//        convertJsonObj();
        convertJsonArrayFromObj();
        dashBoardAdapter = new DashBoardAdapter(myList, this, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rv_DBTabs.setLayoutManager(layoutManager);
        rv_DBTabs.setHasFixedSize(true);
        rv_DBTabs.setAdapter(dashBoardAdapter);
        if (Utility.isNetworkAvailable(this)) {
            JSONObject requestObj = new JSONObject();
            try {
                requestObj.put("app_name", "DeptApp");
                invokeAPKCheckWS(requestObj);
            } catch (Exception e) {

            }

            //sendToken();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.ll_reports)
    void openPopup() {
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please Wait...");
            JSONObject requestObj = new JSONObject();
            try {
                requestObj.put("SecCode", sec_code);
            } catch (Exception e) {

            }
            getAbstractCount(requestObj);
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
        }
    }

    /*Check Latest version API*/
    private void invokeAPKCheckWS(JSONObject requestObj) {
        pDialog.show();
        pDialog.setMessage("Checking for updates...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/SAPISU/APKVersionCheck/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject responseObj = new JSONObject(response);
                        responseObj = responseObj.getJSONObject("response");
                        //String status = responseObj.getString("STATUS");

                        final String forceUpdate = responseObj.getString("force_update");
                        final String LVersion = responseObj.getString("apk_version");

                        float fAppVersion = Float.parseFloat(LVersion);
                        float fApkVersion = Float.parseFloat(BuildConfig.VERSION_NAME);

                        String strApkUrl = responseObj.getString("apk_Url");

                        if (fAppVersion > fApkVersion ) {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            if (!strApkUrl.isEmpty()) {
                                UpdateAPK updateApp = new UpdateAPK();
                                updateApp.setContext(DashboardActivity.this);
                                updateApp.execute(strApkUrl);
                            }
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.dismiss();
//                            }
//                            //start downloading the file using the download manager
//                            String fileName = "Apcpdcl_DepartmentApp_" + LVersion + ".apk";
//                            file = new File(Environment.getExternalStorageDirectory() + "/download/", fileName);
//
//                            // Utility.openWebPage(LMDashBoardActivity.this,url);
//                            if (Utility.isNetworkAvailable(DashboardActivity.this)) {
//                                Uri download_Uri = Uri.parse(responseObj.getString("apk_Url"));
//                                // Uri download_Uri = Uri.parse("http://59.144.184.186:9090/Files/DepartmentApp_5.1.apk");
//                                pDialog = new ProgressDialog(DashboardActivity.this);
//                                pDialog.setMessage("Please Wait APCPDCL DepartmentApp latest version downloading...");
//                                pDialog.setCancelable(false);
//                                pDialog.show();
//                                downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                                DownloadManager.Request request = new DownloadManager.Request(download_Uri);
//                                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//                                request.setAllowedOverRoaming(true);
//                                if (Environment.getExternalStorageState() == null) {
//                                    file = new File(Environment.getDataDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/Apcpdcl_DepartmentApp_" + LVersion + ".apk");
//                                } else if (Environment.getExternalStorageState() != null) {
//                                    file = new File(Environment.getExternalStorageDirectory().toString() + "/" + Environment.DIRECTORY_DOWNLOADS + "/Apcpdcl_DepartmentApp_" + LVersion + ".apk");
//                                }
//                                request.setDestinationInExternalFilesDir(DashboardActivity.this, Environment.DIRECTORY_DOWNLOADS, "Apcpdcl_DepartmentApp_" + LVersion + ".apk");
//                                downloadReference = downloadManager.enqueue(request);
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
//                            }
                        } else {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
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
                            Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            break;
                    }
                    Utility.showLog("error", error.toString());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /* *
     *Get Abstract Count
     * */
    private void getAbstractCount(JSONObject requestObj) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(50000);
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("LMCODE", sUserCode);
//        client.post(Constants.GET_ABSTRACT_COUNT, requestParams, new AsyncHttpResponseHandler() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/SAPISU/DCListAbstract/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String strResponse) {
                    Utility.showLog("onSuccess", strResponse);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }

                    try {
                        JSONObject responseObj = new JSONObject(strResponse);

                        if( ( responseObj.length() > 0 ) && responseObj != null ) {
                            JSONArray responseArray = responseObj.optJSONArray("response");
                            JSONObject conResObj = new JSONObject();
                            if(responseArray.length()>0){
                                for (int i = 0; i < responseArray.length(); i++) {
                                    JSONObject  userLevelObj= responseArray.optJSONObject(i);
                                    if (userLevelObj != null && userLevelObj.length() > 0) {
                                        if(userLevelObj.optString("userid").equalsIgnoreCase(sUserCode)){
                                            conResObj = new JSONObject();
                                            conResObj.put("DSL_count", userLevelObj.getString("slopencount"));
                                            conResObj.put("DSL_amount", userLevelObj.getString("slopenamt"));

                                            conResObj.put("DNS_count", userLevelObj.getString("nsopencount"));
                                            conResObj.put("DNS_amount", userLevelObj.getString("nsopenamt"));

                                            conResObj.put("CheckReading_Count", userLevelObj.getString("cr_completed"));
//                            conResObj.put("CheckReading_Count", responseObj.getString("cr_target"));

                                            conResObj.put("DTotal_count", Integer.parseInt(userLevelObj.getString("slopencount")) + Integer.parseInt(userLevelObj.getString("nsopencount")));
                                            conResObj.put("DTotal_Amount", Double.parseDouble(userLevelObj.getString("slopenamt"))+Double.parseDouble(userLevelObj.getString("nsopenamt")));
                                        }
                                    }
                                }
                                //abstractModel =new AbstractModel();
                                if (conResObj != null && conResObj.length() > 0) {
                                    abstractModel = new Gson().fromJson(conResObj.toString(),
                                            AbstractModel.class);
                                    //getExceptionNameCount();
                                    abstractListPopup();
                                } else {
                                    Utility.showCustomOKOnlyDialog(DashboardActivity.this, "Something went wrong! Please try again...");
                                }
                            }

                        }else{

                        }

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.getLocalizedMessage());
                    Utility.showCustomOKOnlyDialog(DashboardActivity.this, error.getLocalizedMessage());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void abstractListPopup() {
        String date = new SimpleDateFormat("MMM-yy", Locale.getDefault()).format(new Date());
        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_todo_list);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button submit = (Button) dialog.findViewById(R.id.btn_ok);
        ImageView iv_cancel = (ImageView) dialog.findViewById(R.id.iv_cancel);
        TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
        tv_title.setText("Reports for " + date);
        TextView tv_target = (TextView) dialog.findViewById(R.id.tv_target);
        TextView tv_completed = (TextView) dialog.findViewById(R.id.tv_completed);
        TextView tv_pending = (TextView) dialog.findViewById(R.id.tv_pending);
        TextView tv_dc_click = (TextView) dialog.findViewById(R.id.tv_dc_click);
        TextView tv_exceptions = (TextView) dialog.findViewById(R.id.tv_exceptions);
        TextView tv_rc_paid_list = (TextView) dialog.findViewById(R.id.tv_rc_paid_list);
        TextView tv_services_col = (TextView) dialog.findViewById(R.id.tv_services_col);
        TextView tv_amount_col = (TextView) dialog.findViewById(R.id.tv_amount_col);
        TextView tv_services_all_col = (TextView) dialog.findViewById(R.id.tv_services_all_col);
        TextView tv_total_col = (TextView) dialog.findViewById(R.id.tv_total_col);
        TextView tv_services_col_ns = (TextView) dialog.findViewById(R.id.tv_services_col_ns);
        TextView tv_total_col_ns = (TextView) dialog.findViewById(R.id.tv_total_col_ns);
        tv_dc_click.setText("DC-List Reports for " + date);
        if (!Utility.isValueNullOrEmpty(abstractModel.getCheckReading_Count())) {
            tv_completed.setText(abstractModel.getCheckReading_Count());
            if (Integer.parseInt(abstractModel.getCheckReading_Count().trim()) < 25) {
                tv_pending.setText("" + (25 - Integer.parseInt(abstractModel.getCheckReading_Count())));
            }else {
                tv_pending.setText("0");
            }
        } else {
            tv_completed.setText("0");
            tv_pending.setText("25");
        }
        if (!Utility.isValueNullOrEmpty(abstractModel.getDSL_amount())) {
            tv_total_col.setText(abstractModel.getDSL_amount());
        }
        if (!Utility.isValueNullOrEmpty(abstractModel.getDSL_count())) {
            tv_services_all_col.setText(abstractModel.getDSL_count());
        }
        if (!Utility.isValueNullOrEmpty(abstractModel.getDNS_count())) {
            tv_services_col_ns.setText(abstractModel.getDNS_count());
        }
        if (!Utility.isValueNullOrEmpty(abstractModel.getDNS_amount())) {
            tv_total_col_ns.setText(abstractModel.getDNS_amount());
        }
        if (!Utility.isValueNullOrEmpty(abstractModel.getDTotal_count())) {
            tv_services_col.setText(abstractModel.getDTotal_count());
        }
        if (!Utility.isValueNullOrEmpty(abstractModel.getDTotal_Amount())) {
            tv_amount_col.setText(abstractModel.getDTotal_Amount());
        }
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_exceptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkAvailable(DashboardActivity.this)) {
                    //getAgeWiseException();
                } else {
                    Utility.showCustomOKOnlyDialog(DashboardActivity.this,
                            Utility.getResourcesString(DashboardActivity.this, R.string.no_internet));
                }

            }
        });
        tv_rc_paid_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, RCPaidListActivity.class);
                startActivity(intent);
            }
        });
        tv_dc_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, DC_AbstractListActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

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

    private void convertJsonArrayFromObj() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            String sDesignation = AppPrefs.getInstance(this).getString("DESIG", "");
            if (sDesignation.contains("SS"))
                sDesignation = "SS";
            if (sDesignation.contains("LM") || sDesignation.contains("LI") || sDesignation.contains("ALM"))
                sDesignation = "LM";
            if (sDesignation.contains("EA"))
                sDesignation = "EA";
//            if (sDesignation.contains("ADE") || sDesignation.contains("DEE"))
//                sDesignation = "AE";
            if (sDesignation.contains("AE") || sDesignation.contains("ADE") || sDesignation.contains("DEE"))
                sDesignation = "AE";
            if (obj.has(sDesignation)) {
                JSONArray userArray = obj.getJSONArray(sDesignation);
                for (int i = 0; i < userArray.length(); i++) {
                    JSONObject userDetail = userArray.getJSONObject(i);
                    String boardTitle = userDetail.getString("title");
                    String boardImage = userDetail.getString("imageUrl");
                    myList.add(new DashBoard(boardTitle, boardImage));
                }
            } else {
                myList.add(new DashBoard(sDesignation, ""));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("DashboardReports.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(ArrayList<DashBoard> board) {
        String title = String.valueOf(board.get(0));
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_version).setTitle("Version " + BuildConfig.VERSION_NAME);
        menu.findItem(R.id.action_user).setTitle(sUserName);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_changepassword:
                changePasswordPopup();
                return true;
//            case R.id.action_update_profile:
//                if (Utility.isNetworkAvailable(this)) {
//                    getData();
//                } else {
//                    Utility.showCustomOKOnlyDialog(this, "Please Check Your Internet Connection and Try Again");
//                }
//                return true;
            case R.id.action_logout:
                Utility.callLogout(this, pDialog);
                return true;
            case R.id.action_version:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changePasswordPopup() {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_forgot_password_layout);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(true);

        et_dialog_username = (EditText) mDialog.findViewById(R.id.et_username);
        et_dialog_otp = (EditText) mDialog.findViewById(R.id.et_otp);
        et_dialog_password = (EditText) mDialog.findViewById(R.id.et_password);
        et_dialog_conf_password = (EditText) mDialog.findViewById(R.id.et_conf_password);
        ImageView iv_cancel = (ImageView) mDialog.findViewById(R.id.iv_cancel);


        Button btn_proceed = (Button) mDialog.findViewById(R.id.btn_proceed);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.hideSoftKeyboard(DashboardActivity.this, et_dialog_username);
                if (objNetworkReceiver.hasInternetConnection(DashboardActivity.this)) {
                    if (IsNullOrBlank(et_dialog_username.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your UserName", Toast.LENGTH_LONG).show();
                        return;
                    } else {
//                        RequestParams params = new RequestParams();
//                        params.put("USERNAME", et_username.getText().toString());
                        JSONObject requestObj = new JSONObject();
                        try {
                            if (AppPrefs.getInstance(getApplicationContext()).getString("OTPGETTING", "").equalsIgnoreCase("TRUE")) {
                                if (IsNullOrBlank(et_dialog_otp.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Please Enter your OTP", Toast.LENGTH_LONG).show();
                                    return;
                                } else if (IsNullOrBlank(et_dialog_password.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Please Enter your Password", Toast.LENGTH_LONG).show();
                                    return;
                                } else if (IsNullOrBlank(et_dialog_conf_password.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Please Enter your Confirm Password", Toast.LENGTH_LONG).show();
                                    return;
                                } else if (!et_dialog_password.getText().toString().equals(et_dialog_conf_password.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_LONG).show();
                                    return;
                                } else {
                                    requestObj.put("user_id", et_dialog_username.getText().toString());
                                    requestObj.put("otp", et_dialog_otp.getText().toString());
                                    requestObj.put("password", et_dialog_password.getText().toString());
                                }
                            } else {
                                requestObj.put("user_id", et_dialog_username.getText().toString());
                            }
                            invokeForgotpwdWebService(requestObj);
                        } catch (Exception e) {

                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }

            }
        });
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING", "FALSE");
                mDialog.dismiss();

            }
        });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utility.hideSoftKeyboard(DashboardActivity.this, et_dialog_username);
            }
        });

        mDialog.show();
    }

    private void invokeForgotpwdWebService(JSONObject requestObj) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        String sURL = "";
        if (AppPrefs.getInstance(getApplicationContext()).getString("OTPGETTING", "").equalsIgnoreCase("TRUE")) {
            sURL = ServiceConstants.USER_FORGOT_PWD;
        } else {
            sURL = ServiceConstants.USER_FORGOT_PWD_OTPREQUEST;
        }
        AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic c2ItZWVmY2FiYTUtOGMzNy00MGQzLTgwNTEtMTI1M2NlNjlmMTA3IWI2Mzk0fGl0LXJ0LWFwY3BjZGNsLXRlc3QtazVxb3FtNXkhYjExNDoxMWZiYTg2Zi1hNWY3LTRlOGYtYWRkYy1hM2QzYTNlNDFmNDYkVDJrWERDNlhvS3BSRnktcVNXSFdXOVI3Ti1QS1NRZ3pfcFNNWXBmalpSRT0=")};
            //Utility.showLog("URL", Constants.URL + Constants.GEO_LATLONGINPUT);
            client.post(getApplicationContext(), sURL, headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Log.e("response", responseStr);
                    try {
                        JSONObject responseObj = new JSONObject(responseStr);
                        responseObj = responseObj.getJSONObject("response");
                        String status = responseObj.getString("success");
                        switch (status) {
                            case "True":
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                String sMessage = responseObj.getString("message");
                                if (sMessage.equalsIgnoreCase("OTP Sent")) {
                                    AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING", "TRUE");
                                    et_dialog_otp.setVisibility(View.VISIBLE);
                                    et_dialog_password.setVisibility(View.VISIBLE);
                                    et_dialog_conf_password.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), sMessage + " Sucssfully to " + requestObj.getString("user_id"), Toast.LENGTH_LONG).show();
                                } else {
                                    AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING", "FALSE");
                                    et_dialog_otp.setVisibility(View.GONE);
                                    et_dialog_password.setVisibility(View.GONE);
                                    et_dialog_conf_password.setVisibility(View.GONE);
                                    mDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), sMessage, Toast.LENGTH_LONG).show();
                                }
                                break;
                            case "False":
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING", "FALSE");
                                et_dialog_otp.setVisibility(View.GONE);
                                et_dialog_password.setVisibility(View.GONE);
                                et_dialog_conf_password.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                                break;

                        }
                    } catch (Exception e) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING", "FALSE");
                    et_dialog_otp.setVisibility(View.GONE);
                    et_dialog_password.setVisibility(View.GONE);
                    et_dialog_conf_password.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("error", error.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class UpdateAPK extends AsyncTask<String, Integer, String> {
        private ProgressDialog progressDialog;
        private Context mContext;

        void setContext(Activity context) {
            mContext = context;
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(mContext);
                    progressDialog.setMessage(getString(R.string.apk_download));
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                if (!args[0].startsWith("https"))
                    args[0] = "https://" + args[0];
                URL url = new URL(args[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                int lengthOfFile = httpURLConnection.getContentLength();

                String strPath = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath();
                File file = new File(strPath);
                boolean isCreate = file.mkdirs();
                File outputFile = new File(file, "ApcpdclDeptApp.apk");
                if (outputFile.exists()) {
                    boolean isDelete = outputFile.delete();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

                InputStream inputStream = httpURLConnection.getInputStream();

                byte[] buffer = new byte[1024];
                int len1;
                long total = 0;
                while ((len1 = inputStream.read(buffer)) != -1) {
                    total += len1;
                    fileOutputStream.write(buffer, 0, len1);
                    publishProgress((int) ((total * 100) / lengthOfFile));
                }
                fileOutputStream.close();
                inputStream.close();
                if (progressDialog != null)
                    progressDialog.dismiss();
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog != null)
                progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (progressDialog != null) {
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(100);
                progressDialog.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog != null)
                progressDialog.dismiss();
            if (result == null)
            Toast.makeText(getApplicationContext(),"APK download error",Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(getApplicationContext(),"APK downloaded successfully",Toast.LENGTH_SHORT).show();
                installApk();
            }
        }

        private void installApk() {
            try {
                String PATH = Objects.requireNonNull(mContext.getExternalFilesDir(null)).getAbsolutePath();
                File file = new File(PATH + "/ApcpdclDeptApp.apk");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= 24) {
                    Uri downloaded_apk = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
                    intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                    List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        mContext.grantUriPermission(mContext.getApplicationContext().getPackageName() + ".provider", downloaded_apk, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}