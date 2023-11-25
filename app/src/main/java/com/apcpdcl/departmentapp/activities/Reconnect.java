package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Reconnect extends AppCompatActivity {

    @BindView(R.id.textSCNo)
    TextView textSCNo;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textCat)
    TextView textCat;
    @BindView(R.id.textLoad)
    TextView textLoad;
    @BindView(R.id.textRCFee)
    TextView textRCFee;
    @BindView(R.id.textDue)
    TextView textDue;
    @BindView(R.id.textLastPaid)
    TextView textLastPaid;
    @BindView(R.id.textDCDate)
    TextView textDCDate;

    @BindView(R.id.textkwh)
    TextView textKwh;

    @BindView(R.id.textkvah)
    TextView textKvah;

    @BindView(R.id.textphase)
    TextView textPhase;

    @BindView(R.id.ll_kvah)
    LinearLayout llKvah;

    @BindView(R.id.lltextkvah)
    LinearLayout lltextkvah;

    @BindView(R.id.editKWH)
    EditText editKWH;
    @BindView(R.id.editKVAH)
    EditText editKVAH;

    @BindView(R.id.spinRemarks)
    AppCompatSpinner spinRemarks;

    @BindView(R.id.layoutRemarks)
    LinearLayout layoutRemarks;

    private Toolbar mToolbar;

    private double dcAmount = 0;
    private JSONObject jsonObject;

    private ProgressDialog pDialog;
    private String sPhase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconnect);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);

        pDialog = new ProgressDialog(this);

        savedInstanceState = getIntent().getExtras();
        if (savedInstanceState != null && savedInstanceState.containsKey("OBJ")) {
            try {
                jsonObject = new JSONObject(savedInstanceState.getString("OBJ"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (jsonObject != null && jsonObject.length() > 0) {
            if(jsonObject.optString("phase").equalsIgnoreCase("1")){
                lltextkvah.setVisibility(View.GONE);
                llKvah.setVisibility(View.GONE);
            }
            textSCNo.setText(jsonObject.optString("bpno"));
            textName.setText(jsonObject.optString("Name"));
            textCat.setText(jsonObject.optString("rate_cat"));
            textLoad.setText(jsonObject.optString("cont_load"));
            textRCFee.setText(jsonObject.optString("rc_amt"));
            textDue.setText(jsonObject.optString("dc_amount"));
            textLastPaid.setText(jsonObject.optString("pr_dt"));
            textDCDate.setText(jsonObject.optString("disconenctionDt"));
            textKwh.setText(jsonObject.optString("disc_read_kwh"));
            if(jsonObject.optString("phase").equalsIgnoreCase("3")) {
                textKvah.setText(jsonObject.optString("disc_read_kvh"));
            }
            textPhase.setText(jsonObject.optString("phase"));
            dcAmount = Double.parseDouble(jsonObject.optString("dc_amount"));

            if (dcAmount < 50) {
                layoutRemarks.setVisibility(View.GONE);
            }
        }

        ArrayList<String> remarksList = new ArrayList<>();
        remarksList.add("AE Recommendation");
        remarksList.add("ADE Recommendation");
        remarksList.add("DE Recommendation");
        remarksList.add("SE Recommendation");
        remarksList.add("SE Recommendation");
        remarksList.add("Govt. Service");
        remarksList.add("Dispute/Under correspondence");
        remarksList.add("Court cases");
        remarksList.add("Others");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.auto_item, R.id.tv_item, remarksList);
        spinRemarks.setAdapter(arrayAdapter);
    }

    public void reConnect(View view) {
        double value = 0;

        if(textKwh.getText().toString().equalsIgnoreCase("") || textKwh.getText().toString() == null)
            textKwh.setText("0");
        if(textKvah.getText().toString().equalsIgnoreCase("") || textKvah.getText().toString() == null)
            textKvah.setText("0");

        if (editKWH.getText().toString().length() > 0) {
            value = Double.parseDouble(editKWH.getText().toString());
            if (value == 0 || value <= Double.parseDouble(textKwh.getText().toString()) ) {
                Toast.makeText(this, "Enter valid KWH", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(this, "Enter KWH", Toast.LENGTH_SHORT).show();
            return;
        }
        //If phase 3
        if(textPhase.getText().toString().equalsIgnoreCase("3")){
            if (editKVAH.getText().toString().length() > 0) {
                value = Double.parseDouble(editKVAH.getText().toString());
                if (value == 0 || value <= Double.parseDouble(textKvah.getText().toString()) ) {
                    Toast.makeText(this, "Enter valid KVAH", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "Enter KVAH", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String remarks = spinRemarks.getSelectedItem().toString();
        if (dcAmount > 0)
            remarks = "";

        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            JSONObject requestObj = new JSONObject();
            try {
                requestObj.put("bpno", jsonObject.optString("bpno"));
                requestObj.put("recon_kwh", editKWH.getText().toString());
                requestObj.put("recon_kvah", editKVAH.getText().toString());
                requestObj.put("recon_remarks", remarks);
                requestObj.put("prno", jsonObject.optString("pr_no"));
                saveReconnection(requestObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    /* *
     *Get Reconnection List
     * */
    private void saveReconnection(JSONObject requestObjVal) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjVal.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/SAPISU/Reconnection/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        if (jsonObject != null && jsonObject.length() > 0) {
                            JSONObject responseObj = jsonObject.optJSONObject("response");
                            if (responseObj != null && responseObj.length() > 0) {
                                if (responseObj.optString("success").equalsIgnoreCase("True")) {
                                    editKWH.setText("");
                                    editKVAH.setText("");
                                    spinRemarks.setSelection(0);
                                    Toast.makeText(Reconnect.this, responseObj.optString("message"), Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent in = new Intent(Reconnect.this, ReconnectionList.class);
                                    startActivity(in);
                                } else
                                    Toast.makeText(Reconnect.this, responseObj.optString("message"), Toast.LENGTH_SHORT).show();
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
                    Utility.showCustomOKOnlyDialog(Reconnect.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}