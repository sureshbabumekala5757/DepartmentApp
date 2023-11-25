package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class PendingDTRCompliantsAllDetailsActivity extends AppCompatActivity {

    String strCompID, strSectionName, strSectionPhonenum, strCompType, str_statustype, str_staffdesig, strSection_Code, strFailSecID, strFaultyDTRNo, strRemarks;
    EditText et_complaintID, et_secname, et_secphone, et_staffname, et_staffphone, et_remarks, et_faildtrnum, et_faildtrsec;
    Button btn_submit;
    private RadioGroup radioGroup, cmptyperadioGroup;
    LinearLayout ll_dtrfailure, ll_remarks;
    Spinner spn_desig;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    public SharedPreferences prefs;
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtrcomp_alldetails);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        et_complaintID = findViewById(R.id.et_complaintID);
        et_secname = findViewById(R.id.et_secname);
        et_secphone = findViewById(R.id.et_secphone);
        et_staffname = findViewById(R.id.et_staffname);
        et_staffphone = findViewById(R.id.et_staffphone);
        et_remarks = findViewById(R.id.et_remarks);
        et_faildtrnum = findViewById(R.id.et_faildtrnum);
        et_faildtrsec = findViewById(R.id.et_faildtrsec);

        ll_dtrfailure = findViewById(R.id.ll_dtrfailure);
        ll_remarks = findViewById(R.id.ll_remarks);
        spn_desig = findViewById(R.id.spn_desig);

        btn_submit = findViewById(R.id.btn_submit);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        cmptyperadioGroup = (RadioGroup) findViewById(R.id.cmptyperadioGroup);
        cmptyperadioGroup.clearCheck();

        prefs = getSharedPreferences("loginPrefs", 0);
        strSection_Code = prefs.getString("Section_Code", "");

        pDialog = new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strCompID = (String) bd.get("COMPID");
            strSectionName = (String) bd.get("SECNAME");
            strSectionPhonenum = (String) bd.get("PHONENUM");
        }
        try {
            et_complaintID.setText(strCompID);
            et_secname.setText(strSectionName);
            et_secphone.setText(strSectionPhonenum);
            setDesignationSpinnerData();


            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbn_dtrFailure) {
                        ll_dtrfailure.setVisibility(View.VISIBLE);
                        ll_remarks.setVisibility(View.GONE);
                        str_statustype = "DTR_F";
                        strFailSecID = et_faildtrsec.getText().toString();
                        strFaultyDTRNo = et_faildtrnum.getText().toString();
                        strRemarks = "";
                    } else if (checkedId == R.id.rbn_fuseofcall) {
                        ll_remarks.setVisibility(View.VISIBLE);
                        ll_dtrfailure.setVisibility(View.GONE);
                        str_statustype = "FOC";
                        strFailSecID = "";
                        strFaultyDTRNo = "";
                        strRemarks = et_remarks.getText().toString();
                    } else {
                    }
                }

            });

            cmptyperadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbn_rural) {
                        strCompType = "1";
                    } else if (checkedId == R.id.rbn_urban) {
                        strCompType = "2";
                    } else {
                    }
                }

            });

            spn_desig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                    if (position != 0) {
                        str_staffdesig = parent.getItemAtPosition(position).toString();
                    } else {
                        str_staffdesig = "";
                    }
                }

                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("COMPLAINT_NO", strCompID);
                            jsonObject.put("COMPLAINT_TYPE", strCompType);
                            jsonObject.put("SECTION_CODE", strSection_Code);
                            jsonObject.put("STATUS_TYPE", str_statustype);
                            jsonObject.put("FAILURE_SECTION_ID", strFailSecID);
                            jsonObject.put("FAULTY_DTR_NO", strFaultyDTRNo);
                            jsonObject.put("STAFF_NAME", et_staffname.getText().toString());
                            jsonObject.put("STAFF_DESIG", str_staffdesig);
                            jsonObject.put("STAFF_PHONE", et_staffphone.getText().toString());
                            jsonObject.put("REMARKS", strRemarks);
                            invokeWebService(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void invokeWebService(JSONObject jsonObject) {
        pDialog.show();
        pDialog.setMessage("Please Wait");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            client.post(this, "http://10.50.55.28:8888/dtr_tracking/rest/mobileservices/dtr/complaint_status_updation", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject obj = new JSONObject(response);
                        String str_status = obj.getString("STATUS");
                        if (str_status.equalsIgnoreCase("TRUE")) {
                            String str_response = obj.getString("RESPONSE");
                            Toast.makeText(getApplicationContext(), str_response, Toast.LENGTH_LONG).show();
                        } else if (str_status.equalsIgnoreCase("FALSE")) {
                            String str_Error = obj.getString("ERROR");
                            Toast.makeText(getApplicationContext(), str_Error, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(PendingDTRCompliantsAllDetailsActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void setDesignationSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("FM");
        list.add("SLI");
        list.add("LI");
        list.add("LM");
        list.add("ALM");
        list.add("JLM");

        SpinnerAdapter desigAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_desig.setAdapter(desigAdapter);
    }
}
