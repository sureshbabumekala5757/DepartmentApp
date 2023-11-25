package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.RCPaidListAdapter;
import com.apcpdcl.departmentapp.models.RCPaidModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RCPaidListActivity extends AppCompatActivity {

    @BindView(R.id.lv_reg)
    ListView lv_reg;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    private ArrayList<RCPaidModel> rcPaidModels;
    private RCPaidListAdapter registrationListAdapter;
    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String lmcode = "";
    private String sUserName = "";
    private String sUserCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    /*Initialize Views*/
    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar_title.setText("RC Paid List");
        toolbar_title.setText("Disconnection List");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        lmcode = prefs.getString("LMCode", "");
        //Shared Data
        sUserName = AppPrefs.getInstance(this).getString("USERNAME", "");
        sUserCode = AppPrefs.getInstance(this).getString("USERID", "");
        pDialog = new ProgressDialog(this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            JSONObject requestObj = new JSONObject();
            try{
                requestObj.put("LineManCode",sUserCode);
            }catch (Exception e){

            }
            getRcPaidList(requestObj);
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
        /*et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 5 && !charSequence.toString().equals(sectionCode)) {
                    Utility.showCustomOKOnlyDialog(RegistrationListActivity.this,
                            et_search.getText().toString() + " Service Number is not related to you");
                    et_search.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });*/
    }

    /*
     *Set Service List DAta to ListView
     * */
    private void setListData() {
        registrationListAdapter = new RCPaidListAdapter(this, rcPaidModels);
        lv_reg.setAdapter(registrationListAdapter);
        implementsSearch();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    //IMPLEMENT SEARCH
    private void implementsSearch() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (registrationListAdapter != null)
                    registrationListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @OnClick(R.id.iv_search)
    void implementSearch() {
        implementsSearch();
    }


    /* *
     *Get RC Paid List
     * */
    private void getRcPaidList(JSONObject requestObj) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(50000);
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("LMCODE", lmcode);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/SAPISU/RCPaidList/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                //        client.post(Constants.RC_LIST, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray finalJSArr = new JSONArray();
                        if (jsonObject != null && jsonObject.length() > 0) {
                            if (jsonObject.optString("success").equalsIgnoreCase("True")) {

                                JSONArray dataArr = jsonObject.getJSONArray("response");

                                if (dataArr != null && dataArr.length() > 0) {
                                    rcPaidModels = new ArrayList<>();
                                    for (int i = 0; i < dataArr.length(); i++) {
                                        JSONObject jsonObject1 = dataArr.optJSONObject(i);
                                        JSONObject conResObj = new JSONObject();
                                        conResObj.put("CMUSCNO", jsonObject1.getString("bpno"));
                                        conResObj.put("CMCELL", jsonObject1.getString("mobile"));
                                        conResObj.put("CMFDRCD", jsonObject1.getString("feeder_name"));
                                        conResObj.put("CMDTRCD", jsonObject1.getString("dtr_name"));
                                        conResObj.put("CMDRNUM", jsonObject1.getString("address"));
                                        conResObj.put("CMPOLENUM", jsonObject1.getString("poleno"));
                                        conResObj.put("CMSTREET", "");
                                        conResObj.put("BLCLRDT", jsonObject1.getString("pr_dt"));
                                        conResObj.put("BLDISCDT", jsonObject1.getString("disconenctionDt"));
                                        conResObj.put("DCList_Pending_Amt", jsonObject1.getString("dc_amount"));
                                        conResObj.put("PTPRDT", jsonObject1.getString("pr_dt"));
                                        conResObj.put("PTPRNO", jsonObject1.getString("pr_no"));
                                        conResObj.put("PTCOUNTER", jsonObject1.getString("pr_counter"));
                                        conResObj.put("PTBLAMT", jsonObject1.getString("bill_amt"));
                                        conResObj.put("PTMISAMT", jsonObject1.getString("oth_amt"));
                                        conResObj.put("PTRECONCHG", jsonObject1.getString("rc_amt"));
                                        conResObj.put("TOT", jsonObject1.getString("bal_due"));
                                        conResObj.put("BPNUMBER", jsonObject1.getString("uscno"));
                                        finalJSArr.put(conResObj);
                                    }

                                    //JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
                                    for (int i = 0; i < finalJSArr.length(); i++) {
                                        JSONObject json = finalJSArr.optJSONObject(i);
                                        RCPaidModel registrationModel = new Gson().fromJson(json.toString(),
                                                RCPaidModel.class);
                                        rcPaidModels.add(registrationModel);
                                    }
                                    if (rcPaidModels.size() > 0) {
                                        setListData();
                                    } else {
                                        if (pDialog != null && pDialog.isShowing()) {
                                            pDialog.dismiss();
                                        }
                                        tv_no_data.setVisibility(View.VISIBLE);
                                        lv_reg.setVisibility(View.GONE);
                                        rl_search.setVisibility(View.GONE);
                                    }
                                }
                            } else
                                Toast.makeText(RCPaidListActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
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
                    Utility.showCustomOKOnlyDialog(RCPaidListActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
