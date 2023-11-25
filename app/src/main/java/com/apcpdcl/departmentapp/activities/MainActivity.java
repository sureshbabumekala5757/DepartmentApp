package com.apcpdcl.departmentapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {

    Button loginBtn;
    EditText et_username, et_password;
    ProgressDialog pDialog;
    String strSuccess, strUserName, strPassword, strSection_Code, strLmcode;
    public SharedPreferences prefs, loginPreferences;
    SharedPreferences.Editor loginPrefsEditor;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    TextView forgotpwdtxt;
    String msg;
    ImageView logolmda;
    private Dialog mDialog;
    private String isLoginFirst = "FALSE";
    private EditText et_dialog_username, et_dialog_otp,et_dialog_password,et_dialog_conf_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        et_username = (EditText) findViewById(R.id.login_username);
        et_password = (EditText) findViewById(R.id.login_pwd);
        forgotpwdtxt = (TextView) findViewById(R.id.forgotpwdtxt);
        pDialog = new ProgressDialog(MainActivity.this);
        loginBtn = (Button) findViewById(R.id.login);
        logolmda = (ImageView) findViewById(R.id.logolmda);
//        prefs = getSharedPreferences("loginPrefs", 0);
//        strUserName = prefs.getString("UserName", "");
//        strPassword = prefs.getString("Password", "");
//        strSection_Code = prefs.getString("Section_Code", "");
//        strLmcode = prefs.getString("LMCode", "");
//        String strUser = prefs.getString("USER", "");
//        String strType = prefs.getString("USERTYPE", "");
        /*showing screen after splash regarding logout**/
//        if (!IsNullOrBlank(strType) && (strType.equalsIgnoreCase("LM") || strType.equalsIgnoreCase("LMOPN"))) {
//            Intent i = new Intent(getApplicationContext(), LMDashBoardActivity.class);
//            startActivity(i);
//        } else if (!IsNullOrBlank(strType) && strType.equalsIgnoreCase("SS")) {
//            Intent i = new Intent(getApplicationContext(), SSDashBoardActivity.class);
//            startActivity(i);
//        } else if (!IsNullOrBlank(strType) && strType.equalsIgnoreCase("AEOPN")) {
//            Intent i = new Intent(getApplicationContext(), AeDashBoardActivity.class);
//            startActivity(i);
//        } else if (!IsNullOrBlank(strType) && strType.equalsIgnoreCase("AAO") || !IsNullOrBlank(strType) && strType.equalsIgnoreCase("JAO") || !IsNullOrBlank(strType) && strType.equalsIgnoreCase("SA") || !IsNullOrBlank(strType) && strType.equalsIgnoreCase("JA")) {
//            Intent i = new Intent(getApplicationContext(), AAODashBoardActivity.class);
//            startActivity(i);
//        } else if (!IsNullOrBlank(strType) && strType.equalsIgnoreCase("ADEOPN")) {
//            Intent i = new Intent(getApplicationContext(), ADEDashBoardActivity.class);
//            startActivity(i);
//        } else if (!IsNullOrBlank(strType) && strType.equalsIgnoreCase("SURVEY")) {
//            Intent i = new Intent(getApplicationContext(), PollingDashBoardActivity.class);
//            startActivity(i);
//        } else if (!IsNullOrBlank(strType) && (strType.equalsIgnoreCase("DEEA") || strType.equalsIgnoreCase("DEELT") || strType.equalsIgnoreCase("DEECT") || strType.equalsIgnoreCase("DEOPN")
//                || strType.equalsIgnoreCase("SEDPE") || strType.equalsIgnoreCase("SEOPN"))) {
//            Intent i = new Intent(getApplicationContext(), DEDashBoardActivity.class);
//            startActivity(i);
//        } else if (!IsNullOrBlank(strType) && (strType.equalsIgnoreCase("DEEA") || strType.equalsIgnoreCase("DEOPN")
//                || strType.equalsIgnoreCase("SEDPE") || strType.equalsIgnoreCase("SEOPN"))) {
//            Intent i = new Intent(getApplicationContext(), DEDashBoardActivity.class);
//            startActivity(i);
//        } else {
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (IsNullOrBlank(et_username.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your UserName", Toast.LENGTH_LONG).show();
                    } else if (IsNullOrBlank(et_password.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your Password", Toast.LENGTH_LONG).show();
                    } else {
                        if (objNetworkReceiver.hasInternetConnection(MainActivity.this)) {
                            RequestParams params = new RequestParams();
                            params.put("USER", et_username.getText().toString());
                            params.put("PWD", et_password.getText().toString());
                            params.put("LOGINTYPE", "LM");

                            //invokeLoginWebService(params);
                            try{
                                JSONObject requestJSONObj = new JSONObject();
                                requestJSONObj.put("user_id",et_username.getText().toString());
                                requestJSONObj.put("password",et_password.getText().toString());
                                loginCheckWebService(requestJSONObj);
                            }catch(Exception e){
                               e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
//        }
        forgotpwdtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPasswordDialog();
            }
        });

        logolmda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent1 = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent1);*/


            }
        });

    }

    private void invokeForgotpwdWebService(JSONObject requestObj) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        String sURL = "";
        if(AppPrefs.getInstance(getApplicationContext()).getString("OTPGETTING","").equalsIgnoreCase("TRUE")){
            sURL = ServiceConstants.USER_FORGOT_PWD;
        }else{
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
                    String sMessage = responseObj.getString("message");
                    switch (status) {
                        case "True":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            //String sMessage = responseObj.getString("message");
                            if(sMessage.equalsIgnoreCase("OTP Sent")){
                                AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING","TRUE");
                                et_dialog_otp.setVisibility(View.VISIBLE);
                                et_dialog_password.setVisibility(View.VISIBLE);
                                et_dialog_conf_password.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), sMessage+" Sucssfully to "+requestObj.getString("user_id"), Toast.LENGTH_LONG).show();
                            }else {
                                AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING","FALSE");
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

                            AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING","FALSE");
                            et_dialog_otp.setVisibility(View.GONE);
                            et_dialog_password.setVisibility(View.GONE);
                            et_dialog_conf_password.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), sMessage, Toast.LENGTH_LONG).show();
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
                AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING","FALSE");
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

    /*******************************SAP login*************************************************************/
    private void loginCheckWebService(JSONObject params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        final String userName = et_username.getText().toString();
        final String password = et_password.getText().toString();
        //final String encoded = Base64.getEncoder().encodeToString((user + ':' + password).getBytes(StandardCharsets.UTF_8));
        final String  userAuth = android.util.Base64.encodeToString((userName + ':' + password).getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);

        AsyncHttpClient client = new AsyncHttpClient();
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+userAuth)};
        HttpEntity entity;
        try {
            entity = new StringEntity(params.toString());
            //StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
            client.post(getApplicationContext(), ServiceConstants.USER_LOGIN,headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("response", responseStr);
                    try {

//                        JSONObject obj = new JSONObject(responseStr);
//                        obj = obj.getJSONObject("response");
//                        String status = obj.getString("success");

                        JSONObject responseObj = new JSONObject(responseStr);
                        responseObj = responseObj.getJSONObject("response");
                        String status = responseObj.getString("success");
                        switch (status) {
                            case "True":
                                Utility.setSharedPrefStringData(MainActivity.this, Constants.USER_NAME, et_username.getText().toString());
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                String sUserName = "";
                                String sPassword = "";
                                String sUserDesignation = "";
                                String sUserCode = "";
                                String sUserSectionCode = "";
                                String sUserSectionName = "";
                                String sCostCenter = "";
                                String sSSName = "";
                                String sSSCode = "";
                                String sUnitName = "";
                                String sMobileNumber = "";
                                isLoginFirst = "TRUE";

                                if (!responseObj.equals("") && responseObj != null) {
                                    sUserName = responseObj.getString("user_id");
                                    sPassword = et_password.getText().toString();
                                    sUserDesignation = responseObj.getString("designation");
                                    sUserCode = responseObj.getString("user_id");
                                    sUserSectionCode = responseObj.getString("section_code");
                                    sUserSectionName = responseObj.getString("section_name");
                                    sCostCenter = responseObj.getString("cost_centre");
                                    sMobileNumber = "";
                                    AppPrefs.getInstance(MainActivity.this).putString("USERNAME", sUserName);
                                    AppPrefs.getInstance(MainActivity.this).putString("PASSWORD", sPassword);
                                    AppPrefs.getInstance(MainActivity.this).putString("SECTIONCODE", sUserSectionCode);
                                    AppPrefs.getInstance(MainActivity.this).putString("DESIG", sUserDesignation);
                                    AppPrefs.getInstance(MainActivity.this).putString("SECTIONNAME", sUserSectionName);
                                    AppPrefs.getInstance(MainActivity.this).putString("USERID", sUserCode);
                                    AppPrefs.getInstance(MainActivity.this).putString("COSTCENTER", sCostCenter);
                                    AppPrefs.getInstance(MainActivity.this).putString("MOBILE", sMobileNumber);
                                    AppPrefs.getInstance(MainActivity.this).putString("UNITNAME", sUserCode);
                                    AppPrefs.getInstance(MainActivity.this).putString("USER_AUTH", userAuth);
                                    AppPrefs.getInstance(MainActivity.this).putString("FIRST_LOGIN", isLoginFirst);
                                    Intent in = new Intent(getApplicationContext(), DashboardActivity.class);
                                    in.putExtra("firstLogin", "0");
                                    startActivity(in);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Please check your Login Details", Toast.LENGTH_LONG).show();
                                }
//                                String usertype = obj.getString("designation");
//                                if (usertype.equalsIgnoreCase("SS")) {
//                                    String sectioncode = obj.getString("Section_Code");
//                                    String lmcode = obj.getString("SSLMCODE");
//                                    String strFirstLogin = obj.getString("FIRSTLOGIN");
//                                    String strdesig = obj.getString("DESIG");
//                                    String strAeUser = obj.getString("AEUSER");
//                                    String aeopn_usertype = obj.getString("USERTYPE");
//                                    String strpwd = obj.getString("AEPWD");
//                                    String name = obj.getString("NAME");
//                                    String mobile = obj.getString("MOBILE");
//                                    String sscode = obj.getString("SSCODE");
//                                    String ssname = obj.getString("SSNAME");
//                                    String unitname = obj.getString("UNITNAME");
//                                    strSuccess = "Login successful!";
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("UserName", et_username.getText().toString());
//                                        loginPrefsEditor.putString("Password", et_password.getText().toString());
//                                        loginPrefsEditor.putString("Section_Code", sectioncode);
//                                        loginPrefsEditor.putString("LMCode", lmcode);
//                                        loginPrefsEditor.putString("DESIG", strdesig);
//                                        loginPrefsEditor.putString("AEUSER", strAeUser);
//                                        loginPrefsEditor.putString("USERTYPE", usertype);
//                                        loginPrefsEditor.putString("AEPWD", strpwd);
//                                        loginPrefsEditor.putString("NAME", name);
//                                        loginPrefsEditor.putString("MOBILE", mobile);
//                                        loginPrefsEditor.putString("SSCODE", sscode);
//                                        loginPrefsEditor.putString("SSNAME", ssname);
//                                        loginPrefsEditor.putString("UNITNAME", unitname);
//                                        loginPrefsEditor.apply();
//                                        Intent in = new Intent(getApplicationContext(), SSDashBoardActivity.class);
//                                        startActivity(in);
//                                    }
//                                } else if (usertype.equalsIgnoreCase("AEOPN")) {
//                                    String aeopn_sectioncode = obj.getString("Section_Code");
//                                    String aeopn_user = obj.getString("USER");
//                                    String aeopn_usertype = obj.getString("USERTYPE");
//                                    String unitname = obj.getString("UNITNAME");
//
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
//                                        loginPrefsEditor.putString("USER", aeopn_user);
//                                        loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
//                                        loginPrefsEditor.putString("DESIG", aeopn_usertype);
//                                        loginPrefsEditor.putString("UserName", et_username.getText().toString());
//                                        loginPrefsEditor.putString("UNITNAME", unitname);
//                                        loginPrefsEditor.apply();
//                                        Intent in = new Intent(getApplicationContext(), AeDashBoardActivity.class);
//                                        startActivity(in);
//                                    }
//                                } else if (usertype.equalsIgnoreCase("SURVEY")) {
//
//                                    String aeopn_user = obj.getString("USER");
//                                    String aeopn_usertype = obj.getString("USERTYPE");
//                                    String unitname = obj.getString("UNITNAME");
//
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("USER", aeopn_user);
//                                        loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
//                                        loginPrefsEditor.putString("UserName", et_username.getText().toString());
//                                        loginPrefsEditor.putString("UNITNAME", unitname);
//                                        loginPrefsEditor.apply();
//                                        Intent in = new Intent(getApplicationContext(), PollingDashBoardActivity.class);
//                                        startActivity(in);
//                                    }
//                                } else if (usertype.equalsIgnoreCase("DEEA") || usertype.equalsIgnoreCase("DEELT") ||usertype.equalsIgnoreCase("DEECT") || usertype.equalsIgnoreCase("DEOPN") ||
//                                        usertype.equalsIgnoreCase("SEDPE") || usertype.equalsIgnoreCase("SEOPN")) {
//                                    String aeopn_sectioncode = obj.getString("section_code");
//                                    String aeopn_user = obj.getString("user_id");
//                                    String aeopn_usertype = obj.getString("designation");
//                                    String name = obj.getString("designation");
//                                    String userId = obj.getString("user_id");
//                                    //String mobile = obj.getString("MOBILE");
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
//                                        loginPrefsEditor.putString("USER", aeopn_user);
//                                        loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
//                                        loginPrefsEditor.putString("UserName", et_username.getText().toString());
//                                        loginPrefsEditor.putString("USERID", userId);
//                                        loginPrefsEditor.apply();
//                                        Intent in = new Intent(getApplicationContext(), DEDashBoardActivity.class);
//                                        startActivity(in);
//                                    }
//                                } else if (usertype.equalsIgnoreCase("DEMP")) {
//                                    String aeopn_sectioncode = obj.getString("Section_Code");
//                                    String aeopn_user = obj.getString("USER");
//                                    String aeopn_usertype = obj.getString("USERTYPE");
//                                    String name = obj.getString("NAME");
//                                    String mobile = obj.getString("MOBILE");
//
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
//                                        loginPrefsEditor.putString("USER", aeopn_user);
//                                        loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
//                                        loginPrefsEditor.putString("NAME", name);
//                                        loginPrefsEditor.putString("MOBILE", mobile);
//                                        loginPrefsEditor.apply();
//                                        Intent in = new Intent(getApplicationContext(), MPDashBoardActivity.class);
//                                        startActivity(in);
//                                    }
//                                } else if (usertype.equalsIgnoreCase("LM") || usertype.equalsIgnoreCase("LMOPN")) {
//                                    String sectioncode = obj.getString("section_code");
//                                    String lmcode = obj.getString("user_id");
//                                    String strFirstLogin = "";
//                                    String strdesig = obj.getString("designation");
//                                    String strAeUser = "";
//                                    String aeopn_usertype = obj.getString("designation");
//                                    String strpwd = "";
//                                    String name = obj.getString("designation");
//                                    String mobile = "";
//                                    String unitname = obj.getString("section_code");
//                                    String userId = obj.getString("user_id");
//
//
//                                    strSuccess = "Login successful!";
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("UserName", et_username.getText().toString());
//                                        loginPrefsEditor.putString("Password", et_password.getText().toString());
//                                        loginPrefsEditor.putString("Section_Code", sectioncode);
//                                        loginPrefsEditor.putString("LMCode", lmcode);
//                                        loginPrefsEditor.putString("DESIG", strdesig);
//                                        loginPrefsEditor.putString("AEUSER", strAeUser);
//                                        loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
//                                        loginPrefsEditor.putString("AEPWD", strpwd);
//                                        loginPrefsEditor.putString("NAME", name);
//                                        loginPrefsEditor.putString("MOBILE", mobile);
//                                        loginPrefsEditor.putString("UNITNAME", unitname);
//                                        loginPrefsEditor.putString("USERID", userId);
//                                        loginPrefsEditor.putString("USER_AUTH", userAuth);
//                                        loginPrefsEditor.apply();
//                                        AppPrefs.getInstance(MainActivity.this).putString("USER_AUTH", userAuth);
//                                        Intent in = new Intent(getApplicationContext(), LMDashBoardActivity.class);
//                                        in.putExtra("firstLogin", strFirstLogin);
//                                        startActivity(in);
//                                    }
//                                } else if (usertype.equalsIgnoreCase("AAO") || usertype.equalsIgnoreCase("JAO") || usertype.equalsIgnoreCase("SA") || usertype.equalsIgnoreCase("JA")) {
//                                    String sectioncode = obj.getString("Section_Code");
//                                    String lmcode = obj.getString("LMCODE");
//                                    String strdesig = obj.getString("DESIG");
//                                    String aeopn_usertype = obj.getString("USERTYPE");
//                                    // String name = obj.getString("NAME");
//                                    String mobile = obj.getString("MOBILE");
//                                    strSuccess = "Login successful!";
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("UserName", et_username.getText().toString());
//                                        loginPrefsEditor.putString("Password", et_password.getText().toString());
//                                        loginPrefsEditor.putString("Section_Code", sectioncode);
//                                        loginPrefsEditor.putString("LMCode", lmcode);
//                                        loginPrefsEditor.putString("DESIG", strdesig);
//                                        loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
//                                        //loginPrefsEditor.putString("NAME", name);
//                                        loginPrefsEditor.putString("MOBILE", mobile);
//                                        loginPrefsEditor.apply();
//                                        Intent in = new Intent(getApplicationContext(), AAODashBoardActivity.class);
//                                        startActivity(in);
//                                    }
//                                    //Utility.showCustomOKOnlyDialog(MainActivity.this, "AAO Login doesn't Support");
//                                } else if (usertype.equalsIgnoreCase("ADEOPN")) {
//                                    String aeopn_sectioncode = obj.getString("Section_Code");
//                                    String aeopn_user = obj.getString("USER");
//                                    String aeopn_usertype = obj.getString("USERTYPE");
//                               /* String name = obj.getString("NAME");
//                                String mobile = obj.getString("MOBILE");*/
//                                    if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
//                                        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
//                                        loginPrefsEditor = loginPreferences.edit();
//                                        loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
//                                        loginPrefsEditor.putString("USER", aeopn_user);
//                                        loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
//                                        loginPrefsEditor.putString("UserName", et_username.getText().toString());
//                                /*    loginPrefsEditor.putString("NAME", name);
//                                    loginPrefsEditor.putString("MOBILE", mobile);*/
//                                        loginPrefsEditor.apply();
//                                        Intent in = new Intent(getApplicationContext(), ADEDashBoardActivity.class);
//                                        startActivity(in);
//                                    }
//                                    //Utility.showCustomOKOnlyDialog(MainActivity.this, "ADE Login doesn't Support");
//                                }
                                break;
//                            case "ERROR":
//                                if (pDialog != null && pDialog.isShowing()) {
//                                    pDialog.dismiss();
//                                }
//                                msg = obj.getString("MSG");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                break;
//                            case "False":
//                                if (pDialog != null && pDialog.isShowing()) {
//                                    pDialog.dismiss();
//                                }
//                                msg = obj.getString("message");
//                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
//                                break;
                            default:
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                Toast.makeText(getApplicationContext(), "Please check your Login Details", Toast.LENGTH_LONG).show();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.e("error", error.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*********************************************************************************************************/
    private void invokeLoginWebService(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        // client.addHeader("version", BuildConfig.VERSION_NAME);
        //"http://59.144.184.186:9090/DcList/mobileAction/login/details"
        //client.post(Constants.URL + "login/details", params, new AsyncHttpResponseHandler() {
        client.post(Constants.LOGIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");
                    switch (status) {
                        case "TRUE":
                            Utility.setSharedPrefStringData(MainActivity.this, Constants.USER_NAME, et_username.getText().toString());
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            String usertype = obj.getString("designation");
                            if (usertype.equalsIgnoreCase("SS")) {
                                String sectioncode = obj.getString("Section_Code");
                                String lmcode = obj.getString("SSLMCODE");
                                String strFirstLogin = obj.getString("FIRSTLOGIN");
                                String strdesig = obj.getString("DESIG");
                                String strAeUser = obj.getString("AEUSER");
                                String aeopn_usertype = obj.getString("USERTYPE");
                                String strpwd = obj.getString("AEPWD");
                                String name = obj.getString("NAME");
                                String mobile = obj.getString("MOBILE");
                                String sscode = obj.getString("SSCODE");
                                String ssname = obj.getString("SSNAME");
                                String unitname = obj.getString("UNITNAME");
                                strSuccess = "Login successful!";
                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("UserName", et_username.getText().toString());
                                    loginPrefsEditor.putString("Password", et_password.getText().toString());
                                    loginPrefsEditor.putString("Section_Code", sectioncode);
                                    loginPrefsEditor.putString("LMCode", lmcode);
                                    loginPrefsEditor.putString("DESIG", strdesig);
                                    loginPrefsEditor.putString("AEUSER", strAeUser);
                                    loginPrefsEditor.putString("USERTYPE", usertype);
                                    loginPrefsEditor.putString("AEPWD", strpwd);
                                    loginPrefsEditor.putString("NAME", name);
                                    loginPrefsEditor.putString("MOBILE", mobile);
                                    loginPrefsEditor.putString("SSCODE", sscode);
                                    loginPrefsEditor.putString("SSNAME", ssname);
                                    loginPrefsEditor.putString("UNITNAME", unitname);
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), SSDashBoardActivity.class);
                                    startActivity(in);
                                }
                            } else if (usertype.equalsIgnoreCase("AEOPN")) {
                                String aeopn_sectioncode = obj.getString("Section_Code");
                                String aeopn_user = obj.getString("USER");
                                String aeopn_usertype = obj.getString("USERTYPE");
                                String unitname = obj.getString("UNITNAME");

                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
                                    loginPrefsEditor.putString("USER", aeopn_user);
                                    loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
                                    loginPrefsEditor.putString("DESIG", aeopn_usertype);
                                    loginPrefsEditor.putString("UserName", et_username.getText().toString());
                                    loginPrefsEditor.putString("UNITNAME", unitname);
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), AeDashBoardActivity.class);
                                    startActivity(in);
                                }
                            } else if (usertype.equalsIgnoreCase("SURVEY")) {

                                String aeopn_user = obj.getString("USER");
                                String aeopn_usertype = obj.getString("USERTYPE");
                                String unitname = obj.getString("UNITNAME");

                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("USER", aeopn_user);
                                    loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
                                    loginPrefsEditor.putString("UserName", et_username.getText().toString());
                                    loginPrefsEditor.putString("UNITNAME", unitname);
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), PollingDashBoardActivity.class);
                                    startActivity(in);
                                }
                            } else if (usertype.equalsIgnoreCase("DEEA") || usertype.equalsIgnoreCase("DEELT") || usertype.equalsIgnoreCase("DEECT") || usertype.equalsIgnoreCase("DEOPN") ||
                                    usertype.equalsIgnoreCase("SEDPE") || usertype.equalsIgnoreCase("SEOPN")) {
                                String aeopn_sectioncode = obj.getString("section_code");
                                String aeopn_user = obj.getString("USER");
                                String aeopn_usertype = obj.getString("USERTYPE");
                               /* String name = obj.getString("NAME");
                                String mobile = obj.getString("MOBILE");*/
                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
                                    loginPrefsEditor.putString("USER", aeopn_user);
                                    loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
                                    loginPrefsEditor.putString("UserName", et_username.getText().toString());
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), DEDashBoardActivity.class);
                                    startActivity(in);
                                }
                            } else if (usertype.equalsIgnoreCase("DEMP")) {
                                String aeopn_sectioncode = obj.getString("Section_Code");
                                String aeopn_user = obj.getString("USER");
                                String aeopn_usertype = obj.getString("USERTYPE");
                                String name = obj.getString("NAME");
                                String mobile = obj.getString("MOBILE");

                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
                                    loginPrefsEditor.putString("USER", aeopn_user);
                                    loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
                                    loginPrefsEditor.putString("NAME", name);
                                    loginPrefsEditor.putString("MOBILE", mobile);
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), MPDashBoardActivity.class);
                                    startActivity(in);
                                }
                            } else if (usertype.equalsIgnoreCase("LM") || usertype.equalsIgnoreCase("LMOPN")) {
                                String sectioncode = obj.getString("Section_Code");
                                String lmcode = obj.getString("LMCODE");
                                String strFirstLogin = obj.getString("FIRSTLOGIN");
                                String strdesig = obj.getString("DESIG");
                                String strAeUser = obj.getString("AEUSER");
                                String aeopn_usertype = obj.getString("USERTYPE");
                                String strpwd = obj.getString("AEPWD");
                                String name = obj.getString("NAME");
                                String mobile = obj.getString("MOBILE");
                                String unitname = obj.getString("UNITNAME");
                                strSuccess = "Login successful!";
                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("UserName", et_username.getText().toString());
                                    loginPrefsEditor.putString("Password", et_password.getText().toString());//No need
                                    loginPrefsEditor.putString("Section_Code", sectioncode);
                                    loginPrefsEditor.putString("LMCode", lmcode);
                                    loginPrefsEditor.putString("DESIG", strdesig);
                                    loginPrefsEditor.putString("AEUSER", strAeUser);//No need
                                    loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
                                    loginPrefsEditor.putString("AEPWD", strpwd);//No need
                                    loginPrefsEditor.putString("NAME", name);
                                    loginPrefsEditor.putString("MOBILE", mobile);//No need or ?
                                    loginPrefsEditor.putString("UNITNAME", unitname);
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), LMDashBoardActivity.class);
                                    in.putExtra("firstLogin", strFirstLogin);
                                    startActivity(in);
                                }
                            } else if (usertype.equalsIgnoreCase("AAO") || usertype.equalsIgnoreCase("JAO") || usertype.equalsIgnoreCase("SA") || usertype.equalsIgnoreCase("JA")) {
                                String sectioncode = obj.getString("Section_Code");
                                String lmcode = obj.getString("LMCODE");
                                String strdesig = obj.getString("DESIG");
                                String aeopn_usertype = obj.getString("USERTYPE");
                                // String name = obj.getString("NAME");
                                String mobile = obj.getString("MOBILE");
                                strSuccess = "Login successful!";
                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("UserName", et_username.getText().toString());
                                    loginPrefsEditor.putString("Password", et_password.getText().toString());
                                    loginPrefsEditor.putString("Section_Code", sectioncode);
                                    loginPrefsEditor.putString("LMCode", lmcode);
                                    loginPrefsEditor.putString("DESIG", strdesig);
                                    loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
                                    //loginPrefsEditor.putString("NAME", name);
                                    loginPrefsEditor.putString("MOBILE", mobile);
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), AAODashBoardActivity.class);
                                    startActivity(in);
                                }
                                //Utility.showCustomOKOnlyDialog(MainActivity.this, "AAO Login doesn't Support");
                            } else if (usertype.equalsIgnoreCase("ADEOPN")) {
                                String aeopn_sectioncode = obj.getString("Section_Code");
                                String aeopn_user = obj.getString("USER");
                                String aeopn_usertype = obj.getString("USERTYPE");
                               /* String name = obj.getString("NAME");
                                String mobile = obj.getString("MOBILE");*/
                                if (!(IsNullOrBlank(et_username.getText().toString()) && IsNullOrBlank(et_password.getText().toString()))) {
                                    loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                                    loginPrefsEditor = loginPreferences.edit();
                                    loginPrefsEditor.putString("Section_Code", aeopn_sectioncode);
                                    loginPrefsEditor.putString("USER", aeopn_user);
                                    loginPrefsEditor.putString("USERTYPE", aeopn_usertype);
                                    loginPrefsEditor.putString("UserName", et_username.getText().toString());
                                /*    loginPrefsEditor.putString("NAME", name);
                                    loginPrefsEditor.putString("MOBILE", mobile);*/
                                    loginPrefsEditor.apply();
                                    Intent in = new Intent(getApplicationContext(), ADEDashBoardActivity.class);
                                    startActivity(in);
                                }
                                //Utility.showCustomOKOnlyDialog(MainActivity.this, "ADE Login doesn't Support");
                            }
                            break;
                        case "ERROR":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            msg = obj.getString("MSG");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            break;
                        case "FALSE":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            msg = obj.getString("MSG");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            Toast.makeText(getApplicationContext(), "Please check your Login Details", Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.e("error", error.toString());
            }
        });
    }

    public boolean IsNullOrBlank(String Input) {
        return Input == null || Input.trim().equals("") || Input.trim().length() == 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //SHOW DIALOG
    private void showForgotPasswordDialog() {
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
                Utility.hideSoftKeyboard(MainActivity.this, et_dialog_username);
                if (objNetworkReceiver.hasInternetConnection(MainActivity.this)) {
                    if (IsNullOrBlank(et_dialog_username.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Please Enter your UserName", Toast.LENGTH_LONG).show();
                        return;
                    } else {
//                        RequestParams params = new RequestParams();
//                        params.put("USERNAME", et_username.getText().toString());
                        JSONObject requestObj = new JSONObject();
                        try{
                            if(AppPrefs.getInstance(getApplicationContext()).getString("OTPGETTING","").equalsIgnoreCase("TRUE")){
                                if (IsNullOrBlank(et_dialog_otp.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Please Enter your OTP", Toast.LENGTH_LONG).show();
                                    return;
                                }else if (IsNullOrBlank(et_dialog_password.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Please Enter your Password", Toast.LENGTH_LONG).show();
                                    return;
                                }else if (IsNullOrBlank(et_dialog_conf_password.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Please Enter your Confirm Password", Toast.LENGTH_LONG).show();
                                    return;
                                }else if(!et_dialog_password.getText().toString().equals(et_dialog_conf_password.getText().toString())){
                                    Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_LONG).show();
                                    return;
                                }else {
                                    requestObj.put("user_id", et_dialog_username.getText().toString());
                                    requestObj.put("otp", et_dialog_otp.getText().toString());
                                    requestObj.put("password", et_dialog_password.getText().toString());
                                }
                            }else {
                                requestObj.put("user_id",et_dialog_username.getText().toString());
                            }
                            invokeForgotpwdWebService(requestObj);
                        }catch (Exception e){

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
                AppPrefs.getInstance(getApplicationContext()).putString("OTPGETTING","FALSE");
                mDialog.dismiss();

            }
        });
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utility.hideSoftKeyboard(MainActivity.this, et_username);
            }
        });

        mDialog.show();
    }

}
