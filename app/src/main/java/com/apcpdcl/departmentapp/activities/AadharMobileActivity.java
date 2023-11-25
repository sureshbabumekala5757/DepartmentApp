package com.apcpdcl.departmentapp.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AadharMobileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_servicenum, et_editaadharnumber, et_editmobilenumber;
    Button submitbtn, aadharupdatebtn, mobileupdatebtn;
    TextView aadhar_txt, mobile_txt;
    RelativeLayout aadhar_titleLayout, mobile_titleLayout, ll_editmobilenumber, ll_editaadharnumber;
    LinearLayout whole_Layout;
    String sec_code, str_servicenumber, upToNCharacters, msg, mobilenum, aadharnum, strTCodeValue, userName, paacd,
            str_modifiedaadharnum, str_modifiedmobilenum;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    //SAPISU
    private String mobileNumber, uscNumber, bpNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_aadharmobile_activity);
        init();
    }

    /*Initialize Views*/
    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pDialog = new ProgressDialog(this);
        et_servicenum = (EditText) findViewById(R.id.et_servicenum);
        et_editaadharnumber = (EditText) findViewById(R.id.et_editaadharnumber);
        et_editmobilenumber = (EditText) findViewById(R.id.et_editmobilenumber);

        submitbtn = (Button) findViewById(R.id.submitbtn);
        aadharupdatebtn = (Button) findViewById(R.id.aadharupdatebtn);
        mobileupdatebtn = (Button) findViewById(R.id.mobileupdatebtn);

        aadhar_txt = (TextView) findViewById(R.id.aadhar_txt);
        mobile_txt = (TextView) findViewById(R.id.mobile_txt);

        aadhar_titleLayout = (RelativeLayout) findViewById(R.id.aadhar_titleLayout);
        mobile_titleLayout = (RelativeLayout) findViewById(R.id.mobile_titleLayout);
        ll_editmobilenumber = (RelativeLayout) findViewById(R.id.ll_editmobilenumber);
        ll_editaadharnumber = (RelativeLayout) findViewById(R.id.ll_editaadharnumber);
        whole_Layout = (LinearLayout) findViewById(R.id.whole_Layout);


        aadhar_titleLayout.setOnClickListener(this);
        mobile_titleLayout.setOnClickListener(this);
        mobile_titleLayout.performClick();
        submitbtn.setOnClickListener(this);
        mobileupdatebtn.setOnClickListener(this);
        aadharupdatebtn.setOnClickListener(this);
        et_servicenum.setOnClickListener(this);


//        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
//        sec_code = lprefs.getString("Section_Code", "");
//        userName = lprefs.getString("UserName", "");
        userName = AppPrefs.getInstance(getApplicationContext()).getString("USERNAME", "");
        sec_code = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        //initView();

//        et_servicenum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 5 && !charSequence.toString().equals(sec_code)) {
//                    Utility.showCustomOKOnlyDialog(AadharMobileActivity.this,
//                            et_servicenum.getText().toString() + " Service Number is not related to you");
//                    et_servicenum.setText("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });

        et_editaadharnumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_editaadharnumber.setText("");
                return false;
            }
        });

        et_editmobilenumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_editmobilenumber.setText("");
                return false;
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == et_servicenum) {
        }
        if (view == aadhar_titleLayout) {
            // initView();
        }
        if (view == mobile_titleLayout) {
            mobile_titleLayout.setBackgroundColor(Color.parseColor("#3F51B5"));
            aadhar_titleLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            mobile_txt.setTextColor(Color.parseColor("#ffffff"));
            aadhar_txt.setTextColor(Color.parseColor("#3F51B5"));
            ll_editaadharnumber.setVisibility(View.GONE);
            ll_editmobilenumber.setVisibility(View.VISIBLE);
        }
        if (view == submitbtn) {
            str_servicenumber = et_servicenum.getText().toString();

            if (Utils.IsNullOrBlank(str_servicenumber)) {
                Toast.makeText(getApplicationContext(), "Please enter Service Number", Toast.LENGTH_LONG).show();
            }
//            else if (str_servicenumber.length() != 13) {
//                Toast.makeText(getBaseContext(), "Enter 13 digit Service Number", Toast.LENGTH_LONG).show();
//            }
            else {
                upToNCharacters = str_servicenumber.substring(0, Math.min(str_servicenumber.length(), 5));
//                if (upToNCharacters.equalsIgnoreCase(sec_code)) {
                if (objNetworkReceiver.hasInternetConnection(this)) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {

                    }
//                        RequestParams params = new RequestParams();
//                        params.put("T_CODE_VALUE", str_servicenumber);
                    JSONObject requestObj = new JSONObject();
                    try {
                        if(str_servicenumber.length()== 10) {
                            requestObj.put("uscno", "");
                            requestObj.put("bp_number", str_servicenumber);
                        }else {
                            requestObj.put("uscno", str_servicenumber);
                            requestObj.put("bp_number", "");
                        }
                        invokeAadharFetchWebService(requestObj);
                    } catch (Exception e) {

                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Please enter valid service number belongs to your section code.", Toast.LENGTH_LONG).show();
//                }
            }
        }
        if (view == aadharupdatebtn) {

            String str_aadharnum = et_editaadharnumber.getText().toString();

            if (str_aadharnum.contains("*")) {
                str_aadharnum = aadharnum;
                if (str_aadharnum.length() < 12) {
                    Toast.makeText(getApplicationContext(), "Aadhar Number must be 12 digits", Toast.LENGTH_LONG).show();
                } else if (!Verhoeff.validateVerhoeff(str_aadharnum)) {
                    Toast.makeText(getApplicationContext(), "Invalid Aadhar Number", Toast.LENGTH_LONG).show();
                } else if (str_aadharnum.equalsIgnoreCase(aadharnum)) {
                    Toast.makeText(getApplicationContext(), "This Aadhar number already exist", Toast.LENGTH_LONG).show();
                } else {
                    strTCodeValue = str_servicenumber + "|" + str_aadharnum + "|" + paacd + "|" + userName;
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                        RequestParams params = new RequestParams();
                        params.put("T_CODE_VALUE", strTCodeValue);
                        invokeAadharWebService(params);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                if (Utils.IsNullOrBlank(str_aadharnum)) {
                    Toast.makeText(getApplicationContext(), "Please enter Aadhar Number", Toast.LENGTH_LONG).show();
                } else if (str_aadharnum.length() < 12) {
                    Toast.makeText(getApplicationContext(), "Aadhar Number must be 12 digits", Toast.LENGTH_LONG).show();
                } else if (!Verhoeff.validateVerhoeff(str_aadharnum)) {
                    Toast.makeText(getApplicationContext(), "Invalid Aadhar Number", Toast.LENGTH_LONG).show();
                } else if (str_aadharnum.equalsIgnoreCase(aadharnum)) {
                    Toast.makeText(getApplicationContext(), "This Aadhar number already exist", Toast.LENGTH_LONG).show();
                } else {
                    strTCodeValue = str_servicenumber + "|" + str_aadharnum + "|" + paacd + "|" + userName;
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                        RequestParams params = new RequestParams();
                        params.put("T_CODE_VALUE", strTCodeValue);
                        invokeAadharWebService(params);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
        if (view == mobileupdatebtn) {
            String str_mobileno = et_editmobilenumber.getText().toString();

            if (str_mobileno.contains("*")) {
                str_mobileno = mobilenum;
                Pattern pattern = Pattern.compile("^([0-9])\\1*$");
                Matcher matcher = pattern.matcher(str_mobileno);
                if (str_mobileno.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Mobile Number must be 10 digits", Toast.LENGTH_LONG).show();
                } else if (str_mobileno.equals(mobilenum)) {
                    Toast.makeText(getApplicationContext(), "This mobile number already exist.", Toast.LENGTH_LONG).show();
                } else if (!str_mobileno.startsWith("9") && !str_mobileno.startsWith("8") && !str_mobileno.startsWith("7") && !str_mobileno.startsWith("6")) {
                    Toast.makeText(getApplicationContext(), "please enter valid Mobile Nmuber", Toast.LENGTH_LONG).show();
                } else if (matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Please enter valid Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    strTCodeValue = str_servicenumber + "|" + str_mobileno + "|" + paacd + "|" + userName;
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
//                        RequestParams params = new RequestParams();
//                        params.put("T_CODE_VALUE", strTCodeValue);
                        JSONObject requestObj = new JSONObject();
                        try {
                            requestObj.put("uscno", uscNumber);
                            requestObj.put("mobile_no", str_mobileno);
                            invokeMobileWebService(requestObj);
                        } catch (Exception e) {

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Pattern pattern = Pattern.compile("^([0-9])\\1*$");
                Matcher matcher = pattern.matcher(str_mobileno);
                if (Utils.IsNullOrBlank(str_mobileno)) {
                    Toast.makeText(getApplicationContext(), "Please enter Mobile Number", Toast.LENGTH_LONG).show();
                } else if (str_mobileno.length() != 10) {
                    Toast.makeText(getApplicationContext(), "Mobile Number must be 10 digits", Toast.LENGTH_LONG).show();
                } else if (str_mobileno.equals(mobilenum)) {
                    Toast.makeText(getApplicationContext(), "This mobile number already exist.", Toast.LENGTH_LONG).show();
                } else if (!str_mobileno.startsWith("9") && !str_mobileno.startsWith("8") && !str_mobileno.startsWith("7") && !str_mobileno.startsWith("6")) {
                    Toast.makeText(getApplicationContext(), "please enter valid Mobile Nmuber", Toast.LENGTH_LONG).show();
                } else if (matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Please enter valid Mobile Number", Toast.LENGTH_LONG).show();
                } else {
                    strTCodeValue = str_servicenumber + "|" + str_mobileno + "|" + paacd + "|" + userName;
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
//                        RequestParams params = new RequestParams();
//                        params.put("T_CODE_VALUE", strTCodeValue);
                        JSONObject requestObj = new JSONObject();
                        try {
                            requestObj.put("uscno", uscNumber);
                            requestObj.put("mobile_no", str_mobileno);
                            invokeMobileWebService(requestObj);
                        } catch (Exception e) {

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void initView() {
        aadhar_titleLayout.setBackgroundColor(Color.parseColor("#3F51B5"));
        mobile_titleLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        aadhar_txt.setTextColor(Color.parseColor("#ffffff"));
        mobile_txt.setTextColor(Color.parseColor("#3F51B5"));
        ll_editmobilenumber.setVisibility(View.GONE);
        ll_editaadharnumber.setVisibility(View.VISIBLE);
    }

    private void invokeAadharFetchWebService(JSONObject requestObjPayLoad) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            //client.post(Constants.URL + Constants.AADHAAR_FETCH, params, new AsyncHttpResponseHandler() {
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/ISU/GetMobileDetails", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    try {
                        JSONObject obj = new JSONObject(responseStr);
                        obj = obj.getJSONObject("response");
                        String success = obj.getString("success");

                        if (success.equals("True")) {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            whole_Layout.setVisibility(View.VISIBLE);
                            mobilenum = obj.getString("tel_no");
                            uscNumber = obj.getString("scn_no");
                            bpNumber = obj.getString("bp_number");

                            //str_modifiedaadharnum = aadharnum.replaceAll("\\w(?=\\w{3})", "*");
                            // str_modifiedmobilenum = mobilenum.replaceAll("\\w(?=\\w{3})", "*");
                            str_modifiedmobilenum = mobilenum;

                            et_editaadharnumber.setText(str_modifiedaadharnum);
                            et_editmobilenumber.setText(mobilenum);


                        } else if (success.equals("False")) {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            msg = obj.getString("message");
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void invokeAadharWebService(RequestParams params) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL + Constants.AADHAAR_UPDATE, params, new AsyncHttpResponseHandler() {
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
                            msg = obj.getString("FLAG1");
                            Toast.makeText(getApplicationContext(), "Aadhar number will be verified and updated soon.", Toast.LENGTH_LONG).show();
                            hide_WholeLayout();
                            break;
                        case "ERROR":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            msg = obj.getString("FLAG1");
                            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                            break;
                        case "FALSE":
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            msg = obj.getString("FLAG1");
                            Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_LONG).show();
                            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    private void invokeMobileWebService(JSONObject requestObjPayLoad) {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
//        client.post(Constants.URL + Constants.MOBILE_UPDATE, params, new AsyncHttpResponseHandler() {
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/MobileNumberUpdation/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    try {
                        JSONObject obj = new JSONObject(responseStr);
                        obj = obj.getJSONObject("response");
                        String status = obj.getString("success");
                        switch (status) {
                            case "true":
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                //msg = obj.getString("message");
                                Toast.makeText(getApplicationContext(), "Mobile number has been updated successfully.", Toast.LENGTH_LONG).show();
                                hide_WholeLayout();
                                break;
                            case "False":
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                msg = obj.getString("message");
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void hide_WholeLayout() {
        et_servicenum.setText("");
        whole_Layout.setVisibility(View.GONE);
    }
}
