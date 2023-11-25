package com.apcpdcl.departmentapp.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.os.StrictMode;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.apcpdcl.departmentapp.BuildConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
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


import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.AbstractModel;
import com.apcpdcl.departmentapp.models.MeterExceptionModel;
import com.apcpdcl.departmentapp.models.MeterExceptionsFullModel;
import com.apcpdcl.departmentapp.models.ProfileModel;
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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LMDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.ll_tarif_plan)
    LinearLayout ll_tarif_plan;

    @BindView(R.id.ll_dc_operations)
    LinearLayout ll_dc_operations;
    @BindView(R.id.iv_dc_operations)
    ImageView iv_dc_operations;
    @BindView(R.id.tv_dc_operations)
    TextView tv_dc_operations;

    @BindView(R.id.ll_dc_operations_mats)
    LinearLayout ll_dc_operations_mats;
    @BindView(R.id.iv_dc_operations_mats)
    ImageView iv_dc_operations_mats;
    @BindView(R.id.tv_dc_operations_mats)
    TextView tv_dc_operations_mats;


    @BindView(R.id.ll_meter_change)
    LinearLayout ll_meter_change;
    @BindView(R.id.iv_meter_change)
    ImageView iv_meter_change;
    @BindView(R.id.tv_meter_change)
    TextView tv_meter_change;

    @BindView(R.id.ll_new_req)
    LinearLayout ll_new_req;
    @BindView(R.id.iv_new_req)
    ImageView iv_new_req;
    @BindView(R.id.tv_new_req)
    TextView tv_new_req;

    @BindView(R.id.ll_aadhaar)
    LinearLayout ll_aadhaar;
    @BindView(R.id.iv_aadhaar)
    ImageView iv_aadhaar;
    @BindView(R.id.tv_aadhaar)
    TextView tv_aadhaar;

    @BindView(R.id.ll_chk_request)
    LinearLayout ll_chk_request;
    @BindView(R.id.iv_chk_request)
    ImageView iv_chk_request;
    @BindView(R.id.tv_chk_request)
    TextView tv_chk_request;


    @BindView(R.id.ll_ccc_complaints)
    LinearLayout ll_ccc_complaints;
    @BindView(R.id.iv_ccc_complaints)
    ImageView iv_ccc_complaints;
    @BindView(R.id.tv_ccc_complaints)
    TextView tv_ccc_complaints;


    @BindView(R.id.ll_geo_tagging)
    LinearLayout ll_geo_tagging;
    @BindView(R.id.iv_geo_tagging)
    ImageView iv_geo_tagging;
    @BindView(R.id.tv_geo_tagging)
    TextView tv_geo_tagging;

    @BindView(R.id.ll_tong_tester)
    LinearLayout ll_tong_tester;
    @BindView(R.id.iv_tong_tester)
    ImageView iv_tong_tester;
    @BindView(R.id.tv_tong_tester)
    TextView tv_tong_tester;

    @BindView(R.id.ll_service_details)
    LinearLayout ll_service_details;
    @BindView(R.id.iv_service_details)
    ImageView iv_service_details;
    @BindView(R.id.tv_service_details)
    TextView tv_service_details;

    @BindView(R.id.ll_lc_operations)
    LinearLayout ll_lc_operations;
    @BindView(R.id.iv_lc_operations)
    ImageView iv_lc_operations;
    @BindView(R.id.tv_lc_operations)
    TextView tv_lc_operations;

    @BindView(R.id.ll_to_be)
    LinearLayout ll_to_be;
    @BindView(R.id.iv_to_be)
    ImageView iv_to_be;
    @BindView(R.id.tv_to_be)
    TextView tv_to_be;

    @BindView(R.id.ll_dashboard)
    LinearLayout ll_dashboard;

    @BindView(R.id.ll_five)
    LinearLayout ll_five;
    @BindView(R.id.ll_four)
    LinearLayout ll_four;
    @BindView(R.id.ll_three)
    LinearLayout ll_three;
    @BindView(R.id.ll_two)
    LinearLayout ll_two;
    @BindView(R.id.ll_one)
    LinearLayout ll_one;

    @BindView(R.id.ll_feeder)
    LinearLayout ll_feeder;
    @BindView(R.id.iv_feeder)
    ImageView iv_feeder;
    @BindView(R.id.tv_feeder)
    TextView tv_feeder;

    @BindView(R.id.ll_interruptions)
    LinearLayout ll_interruptions;

    @BindView(R.id.iv_notifications)
    ImageView iv_notifications;


    @BindView(R.id.ll_survey)
    LinearLayout ll_survey;
    @BindView(R.id.iv_survey)
    ImageView iv_survey;
    @BindView(R.id.tv_survey)
    TextView tv_survey;

    @BindView(R.id.ll_seal_bits)
    LinearLayout ll_seal_bits;
    @BindView(R.id.iv_seal_bits)
    ImageView iv_seal_bits;
    @BindView(R.id.tv_seal_bits)
    TextView tv_seal_bits;

    @BindView(R.id.ll_eeps)
    LinearLayout ll_eeps;
    @BindView(R.id.iv_eeps)
    ImageView iv_eeps;
    @BindView(R.id.tv_eeps)
    TextView tv_eeps;

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
    private Dialog changepwdDialog;
    private Dialog updateProfileDialog;
    private EditText oldpwd, newpwd, confirmpwd;
    private String strOldPwd;
    private String strNewPwd;
    private String strConfirmPwd;
    private String userName;
    private String lmcode;
    private ProfileModel mProfileModel;
    private boolean doubleBackToExitPressedOnce = false;
    private File file;
    private String refreshedToken = "";
    private String sectionCode = "";
    private AbstractModel abstractModel;
    private MeterExceptionsFullModel meterExceptionsFullModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_dashboard_activity);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
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
        //  refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        lmcode = prefs.getString("LMCode", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askPermissions();
        }
        //Log.e("refreshedToken", refreshedToken);
        pDialog = new ProgressDialog(LMDashBoardActivity.this);
        pDialog.setCancelable(false);
        Utility.getMeterMake(this);
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        String designation = prefs.getString("DESIG", "");
        if (/*designation.equalsIgnoreCase("LM")
                ||*/ designation.equalsIgnoreCase("JLM")
                || designation.equalsIgnoreCase("ALM")) {
            ll_tong_tester.setVisibility(View.GONE);
        } else {
            ll_tong_tester.setVisibility(View.VISIBLE);
        }
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            String strFirst = (String) bd.get("firstLogin");
            if (strFirst != null && strFirst.equals("1")) {
                changePasswordPopup();
            }
        }

        if (Utility.isNetworkAvailable(this)) {
            RequestParams params = new RequestParams();
            params.put("APK", "LMAPP");
            params.put("VERSION", BuildConfig.VERSION_NAME);
            //invokeAPKCheckWebService(params);
            //sendToken();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }
        //Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.call_service));
       // new PollingDialogClass().getPollingData(this, lmcode);

    }

    @OnClick(R.id.ll_dashboard)
    void openPopup() {
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please Wait...");
            getAbstractCount();
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
        }
    }

    @OnClick(R.id.ll_interruptions)
    void navigateToInterruptionsReport() {
        Intent in = new Intent(this, InterruptionsListActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_survey, R.id.iv_survey, R.id.tv_survey})
    void navigateToSurveys() {
        Intent in = new Intent(getApplicationContext(), SurveyDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_eeps, R.id.iv_eeps, R.id.tv_eeps})
    void navigateToEepsList() {
        Intent in = new Intent(getApplicationContext(), EepsUnitActivity.class);
        in.putExtra(Constants.FROM, LMDashBoardActivity.class.getSimpleName());
        startActivity(in);
    }

    @OnClick({R.id.iv_notifications})
    void navigateToNotifications() {
        Intent in = new Intent(getApplicationContext(), NotificationsListActivity.class);
        startActivity(in);
    }
    @OnClick({R.id.ll_seal_bits, R.id.iv_seal_bits, R.id.tv_seal_bits})
    void navigateToSealBits() {
        Intent in = new Intent(getApplicationContext(), SealBitsActivity.class);
        startActivity(in);
    }
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
                    if ("FALSE".equals(status)) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        //start downloading the file using the download manager
                        String fileName = "Apcpdcl_DepartmentApp_" + LVersion + ".apk";
                        file = new File(Environment.getExternalStorageDirectory() + "/download/", fileName);

                        // Utility.openWebPage(LMDashBoardActivity.this,url);
                        if (Utility.isNetworkAvailable(LMDashBoardActivity.this)) {
                            Uri download_Uri = Uri.parse(Constants.DOWNLOAD_PATH + LVersion + ".apk");
                            // Uri download_Uri = Uri.parse("http://59.144.184.186:9090/Files/DepartmentApp_5.1.apk");
                            pDialog = new ProgressDialog(LMDashBoardActivity.this);
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
                            request.setDestinationInExternalFilesDir(LMDashBoardActivity.this, Environment.DIRECTORY_DOWNLOADS, "Apcpdcl_DepartmentApp_" + LVersion + ".apk");
                            downloadReference = downloadManager.enqueue(request);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                        }
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_version).setTitle("Version " + BuildConfig.VERSION_NAME);
        //menu.findItem(R.id.action_user).setTitle(userName);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_changepassword:
                changePasswordPopup();
                return true;
            case R.id.action_update_profile:
                if (Utility.isNetworkAvailable(this)) {
                    getData();
                } else {
                    Utility.showCustomOKOnlyDialog(this, "Please Check Your Internet Connection and Try Again");
                }
                return true;
            case R.id.action_logout:
                Utility.callLogout(this, pDialog);
                return true;
            case R.id.action_version:
                return true;

            default:
                return super.onOptionsItemSelected(item);
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

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(downLoadReciever);
        super.onDestroy();
    }


    private void getData() {
        RequestParams params = new RequestParams();
        Log.e("LMCODE", lmcode);
        Utility.showLog("Url", Constants.URL + Constants.PROFILE);
        params.put("LMCODE", lmcode);
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.URL + Constants.PROFILE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("MSG")) {
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, jsonObject.optString("MSG"));
                    } else {
                        mProfileModel = new Gson().fromJson(jsonObject.toString(), ProfileModel.class);
                        updateProfilePopup();
                    }
                } catch (JSONException e) {
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
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    private void changePasswordPopup() {

        changepwdDialog = new Dialog(LMDashBoardActivity.this);
        changepwdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changepwdDialog.setContentView(R.layout.changepwd_dialog);
        Window window = changepwdDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        oldpwd = (EditText) changepwdDialog.findViewById(R.id.oldpwdet);
        newpwd = (EditText) changepwdDialog.findViewById(R.id.newpwdet);
        confirmpwd = (EditText) changepwdDialog.findViewById(R.id.confirmpwdet);
        Button submit = (Button) changepwdDialog.findViewById(R.id.submitbtn);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strOldPwd = oldpwd.getText().toString();
                strNewPwd = newpwd.getText().toString();
                strConfirmPwd = confirmpwd.getText().toString();

                if (Utility.isValueNullOrEmpty(strOldPwd)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Old Password", Toast.LENGTH_LONG).show();
                } else if (Utility.isValueNullOrEmpty(strNewPwd)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your New Password", Toast.LENGTH_LONG).show();
                } else if (Utility.isValueNullOrEmpty(strConfirmPwd)) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Confirm Password", Toast.LENGTH_LONG).show();
                } else if (!strNewPwd.equals(strConfirmPwd)) {
                    Toast.makeText(getApplicationContext(), "Both New and Confirm Passwords must be same", Toast.LENGTH_LONG).show();
                } else {
                    if (Utility.isNetworkAvailable(LMDashBoardActivity.this)) {
                        RequestParams params = new RequestParams();
                        params.put("USER", userName);
                        params.put("OLDPWD", strOldPwd);
                        params.put("NEWPWD", strNewPwd);
                        //invokeChangepwdWebService(params);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                        changepwdDialog.dismiss();
                    }
                }

            }
        });
        changepwdDialog.show();
    }

    private void abstractListPopup() {
        String date = new SimpleDateFormat("MMM-yy", Locale.getDefault()).format(new Date());
        final Dialog dialog = new Dialog(LMDashBoardActivity.this);
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
            if (Integer.parseInt(abstractModel.getCheckReading_Count()) < 25) {
                tv_pending.setText("" + (25 - Integer.parseInt(abstractModel.getCheckReading_Count())));
            } else {
                tv_pending.setText("0");
            }


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
                if (Utility.isNetworkAvailable(LMDashBoardActivity.this)) {
                    getAgeWiseException();
                } else {
                    Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this,
                            Utility.getResourcesString(LMDashBoardActivity.this, R.string.no_internet));
                }

            }
        });
        tv_rc_paid_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LMDashBoardActivity.this, RCPaidListActivity.class);
                startActivity(intent);
            }
        });
        tv_dc_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LMDashBoardActivity.this, DC_AbstractListActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void updateProfilePopup() {
        updateProfileDialog = new Dialog(LMDashBoardActivity.this);
        updateProfileDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        updateProfileDialog.setContentView(R.layout.dialog_update_profile_layout);
        Window window = updateProfileDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv_code = (TextView) updateProfileDialog.findViewById(R.id.tv_code);
        TextView tv_designation = (TextView) updateProfileDialog.findViewById(R.id.tv_designation);
        TextView tv_section_name = (TextView) updateProfileDialog.findViewById(R.id.tv_section_name);
        final EditText et_name = (EditText) updateProfileDialog.findViewById(R.id.et_name);
        final EditText et_mobile = (EditText) updateProfileDialog.findViewById(R.id.et_mobile);
        Button btn_update = (Button) updateProfileDialog.findViewById(R.id.btn_update);
        tv_code.setText(lmcode);
        et_name.setText(mProfileModel.getNAME());
        et_mobile.setText(mProfileModel.getMOBILE());
        if (Utility.isValueNullOrEmpty(mProfileModel.getDESIGNATION())) {
            tv_designation.setText("NA");
        } else {
            tv_designation.setText(mProfileModel.getDESIGNATION());
        }
        tv_section_name.setText(mProfileModel.getSECTION());

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields(et_name, et_mobile)) {
                    if (Utility.isNetworkAvailable(LMDashBoardActivity.this)) {
                        postData(et_name, et_mobile);
                    } else {
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Please Check Your Internet Connection and Try Again");
                    }
                }
            }
        });
        updateProfileDialog.show();
    }

    private boolean validateFields(EditText et_name, EditText et_mobile) {
        if (Utility.isValueNullOrEmpty(et_name.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Name cannot be empty");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_mobile.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Mobile Number cannot be empty");
            return false;
        } else if (!et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("9") &&
                !et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("8") &&
                !et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("7") &&
                !et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("6")) {
            Utility.showCustomOKOnlyDialog(this, "Enter valid mobile number");
            return false;
        } else if (et_mobile.getText().toString().length() < 10) {
            Utility.showCustomOKOnlyDialog(this, "Enter valid mobile number");
            return false;
        }
        return true;
    }

    private void postData(EditText et_name, EditText et_mobile) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("LMCODE", lmcode);
            jsonObject.put("NAME", et_name.getText().toString());
            jsonObject.put("MOBILE", et_mobile.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        Log.e("PROFILE", jsonObject.toString());
        Utility.showLog("Url", Constants.URL + Constants.UPDATE_PROFILE);
        params.put("PROFILE", jsonObject.toString());
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.URL + Constants.UPDATE_PROFILE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = "";
                    if (jsonObject.has("STATUS")) {
                        status = jsonObject.optString("STATUS");
                    }
                    if (!status.equalsIgnoreCase("Fail")) {
                        updateProfileDialog.dismiss();
                    }
                    if (jsonObject.has("MSG")) {
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, jsonObject.optString("MSG"));
                    }
                } catch (JSONException e) {
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
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    private void invokeChangepwdWebService(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.PWD_CHANGE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");
                    switch (status) {
                        case "TRUE":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Your Password Changed Successfully", Toast.LENGTH_LONG).show();
                            changepwdDialog.dismiss();
                            break;
                        case "ERROR":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), obj.getString("MSG"), Toast.LENGTH_LONG).show();

                            break;
                        case "FALSE":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), obj.getString("MSG"), Toast.LENGTH_LONG).show();
                            break;
                        default:
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection and Try Again", Toast.LENGTH_LONG).show();
                        break;
                }
                Log.e("error", error.toString());
            }
        });
    }

    @OnClick(R.id.ll_tarif_plan)
    void openPdfFile() {

        Intent in = new Intent(getApplicationContext(), TeluguTariffPlanActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_dc_operations, R.id.iv_dc_operations, R.id.tv_dc_operations})
    void navigateToSearch() {
        Intent in = new Intent(getApplicationContext(), Home.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_dc_operations_mats, R.id.iv_dc_operations_mats, R.id.tv_dc_operations_mats})
    void navigateToMatsDCList() {
        Intent in = new Intent(getApplicationContext(), MatsDCListDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_service_details, R.id.iv_service_details, R.id.tv_service_details})
    void navigateToServiceDetails() {
        Intent in = new Intent(getApplicationContext(), ServiceDetailsActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_to_be, R.id.iv_to_be, R.id.tv_to_be})
    void navigateToConnectionsToBeReleased() {
        Intent in = new Intent(getApplicationContext(), NewConnectionsWebActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_lc_operations, R.id.iv_lc_operations, R.id.tv_lc_operations})
    void navigateToLCOperations() {
        Intent in = new Intent(getApplicationContext(), LMLCDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_meter_change, R.id.iv_meter_change, R.id.tv_meter_change})
    void navigateToMeterChange() {
        Intent in = new Intent(getApplicationContext(), LMMeterChangeDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_new_req, R.id.iv_new_req, R.id.tv_new_req})
    void navigateToNewConnection() {
        Intent in = new Intent(getApplicationContext(), NewConnectionDashBoardActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_aadhaar, R.id.iv_aadhaar, R.id.tv_aadhaar})
    void navigateToAadhar() {
        Intent in = new Intent(getApplicationContext(), AadharMobileActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_ccc_complaints, R.id.iv_ccc_complaints, R.id.tv_ccc_complaints})
    void navigateToComplaintsList() {
        Intent in = new Intent(getApplicationContext(), ComplaintsListActivity.class);
        startActivity(in);
    }

    @OnClick({R.id.ll_geo_tagging, R.id.iv_geo_tagging, R.id.tv_geo_tagging})
    void navigateToGeoDashBoard() {
        if (Utility.isLocationEnabled(this)) {
            Intent in = new Intent(getApplicationContext(), GeoDashBoardActivity.class);
            startActivity(in);
        } else {
            displayLocationSettingsRequest();
        }
    }

    @OnClick({R.id.ll_chk_request, R.id.iv_chk_request, R.id.tv_chk_request})
    void navigateToCheckReading() {
        if (Utility.isLocationEnabled(this)) {
            Intent in = new Intent(getApplicationContext(), CheckReadingActivity.class);
            in.putExtra(Constants.FROM, LMDashBoardActivity.class.getSimpleName());
            startActivity(in);
        } else {
            displayLocationSettingsRequest();
        }

    }

    @OnClick({R.id.ll_feeder, R.id.iv_feeder, R.id.tv_feeder})
    void navigateToFeederOutage() {
        Intent in = new Intent(getApplicationContext(), FeederOutagesActivity.class);
        in.putExtra(Constants.FROM, "false");
        startActivity(in);
    }

    @OnClick({R.id.ll_tong_tester, R.id.iv_tong_tester, R.id.tv_tong_tester})
    void navigateToTongTester() {
        Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(in);
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

                SharedPreferences preferences1 = getSharedPreferences("homePrefs", Context.MODE_PRIVATE);
                if (!Utility.isValueNullOrEmpty(preferences1.toString())) {
                    SharedPreferences.Editor editor1 = preferences1.edit();
                    editor1.clear();
                    editor1.apply();
                }


                SharedPreferences preferences2 = getSharedPreferences("OperatingAllPrefs", Context.MODE_PRIVATE);
                if (!Utility.isValueNullOrEmpty(preferences2.toString())) {
                    SharedPreferences.Editor editor2 = preferences2.edit();
                    editor2.clear();
                    editor2.apply();
                }

                SharedPreferences preferences3 = getSharedPreferences("operatingPrefs", Context.MODE_PRIVATE);
                if (!Utility.isValueNullOrEmpty(preferences3.toString())) {
                    SharedPreferences.Editor editor3 = preferences3.edit();
                    editor3.clear();
                    editor3.apply();
                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            Utility.clearDatabase(LMDashBoardActivity.this);
            if (downloadReference == referenceId) {
                //start the installation of the latest version
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                /*Need to here dismiss the dialog*/
                pDialog.dismiss();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
                            "application/vnd.android.package-archive");
                } else {
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    installIntent.setDataAndType(downloadManager.getUriForDownloadedFile(downloadReference),
                            "application/vnd.android.package-archive");
                }
                startActivity(installIntent);

            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
        userName = lprefs.getString("UserName", "");
        lmcode = lprefs.getString("LMCode", "");
        String designation = lprefs.getString("DESIG", "");


        if (Utility.isValueNullOrEmpty(designation)) {
            Intent i = new Intent(LMDashBoardActivity.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }


        //Broadcast receiver for the download manager
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downLoadReciever, filter);

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
                            status.startResolutionForResult(LMDashBoardActivity.this, GPSTrackerActivity.REQUEST_CHECK_SETTINGS);
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



    /* *
     *Get Agewise Exceptions
     * */
    private void getAgeWiseException() {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("LMCODE", lmcode);
        client.post(Constants.AGEWISE_ASL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    MeterExceptionsFullModel meterExceptionsFullModel = new MeterExceptionsFullModel();
                    Iterator<String> iter = jsonObject.keys();
                    ArrayList<String> keys = new ArrayList<>();
                    ArrayList<MeterExceptionModel> meterExceptionModelArrayList = new ArrayList<>();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        keys.add(key);
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray(key);
                            MeterExceptionModel meterExceptionModel = new MeterExceptionModel();
                            ArrayList<MeterExceptionModel> meterExceptionModels = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.optJSONObject(i);
                                MeterExceptionModel registrationModel = new Gson().fromJson(json.toString(),
                                        MeterExceptionModel.class);
                                meterExceptionModels.add(registrationModel);
                            }
                            meterExceptionModel.setMeterExceptionModels(meterExceptionModels);
                            meterExceptionModelArrayList.add(meterExceptionModel);
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }
                    meterExceptionsFullModel.setKeys(keys);
                    meterExceptionsFullModel.setMeterExceptionModels(meterExceptionModelArrayList);
                    Intent intent = new Intent(LMDashBoardActivity.this, MeterExceptionListActivity.class);
                    intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
                    startActivity(intent);
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
            }
        });
    }

    /* *
     *Get Abstract Count
     * */
    private void getAbstractCount() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("LMCODE", lmcode);
        client.post(Constants.GET_ABSTRACT_COUNT, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                if (!Utility.isValueNullOrEmpty(response)) {
                    abstractModel = new Gson().fromJson(response,
                            AbstractModel.class);
                    //getExceptionNameCount();
                    abstractListPopup();
                } else {
                    Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, "Something went wrong! Please try again...");
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.getLocalizedMessage());
                Utility.showCustomOKOnlyDialog(LMDashBoardActivity.this, error.getLocalizedMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
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
              /*  if (grantResults.length > 0
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
