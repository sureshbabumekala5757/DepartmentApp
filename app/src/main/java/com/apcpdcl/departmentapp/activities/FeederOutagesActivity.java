package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.FeederOutageAdapter;
import com.apcpdcl.departmentapp.models.FeederOutageModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeederOutagesActivity extends AppCompatActivity {

    @BindView(R.id.rl_main)
    RelativeLayout rl_main;
    @BindView(R.id.rv_feeder_outages)
    RecyclerView rv_feeder_outages;

    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    public ProgressDialog pDialog;
    private String sectionCode;
    private ArrayList<FeederOutageModel> feederOutageModels;
    private String isSubStation= "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeder_outages_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (getIntent().getExtras().containsKey(Constants.FROM)){
            isSubStation= getIntent().getExtras().getString(Constants.FROM);
        }
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        if (isSubStation.equalsIgnoreCase("true")){
            sectionCode = prefs.getString("SSCODE", "");
        }else {
            sectionCode = prefs.getString("Section_Code", "");
        }
        Utility.showLog("sectionCode",sectionCode);
        pDialog = new ProgressDialog(this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getLiveInterruptionsData();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void getLiveInterruptionsData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        JSONObject requestParams = new JSONObject();
        HttpEntity entity;
        try {
            requestParams.put("sectionId", sectionCode);
            requestParams.put("substation", isSubStation);
            // requestParams.put("sectionId", "56331");
            try {
                entity = new StringEntity(requestParams.toString());
                client.post(this, Constants.GET_LIVE_INTERRUPTIONS__FDR_REPORT, entity,
                             "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utility.showLog("onSuccess", response);
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                                if (jsonObject.has("RESPONSE")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
                                    if (jsonArray.length() > 0) {
                                        feederOutageModels = new ArrayList<>();
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject json = jsonArray.optJSONObject(i);
                                            FeederOutageModel feederOutageModel
                                                    = new Gson().fromJson(json.toString(),
                                                    FeederOutageModel.class);
                                            feederOutageModels.add(feederOutageModel);
                                        }
                                        if (feederOutageModels.size()>0){
                                            setData();
                                        }else {
                                            rl_main.setVisibility(View.GONE);
                                            tv_no_data.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        rl_main.setVisibility(View.GONE);
                                        tv_no_data.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Utility.showCustomOKOnlyDialog(FeederOutagesActivity.this, jsonObject.optString("ERROR"));
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
                        Utility.showCustomOKOnlyDialog(FeederOutagesActivity.this, error.getMessage());
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(this);
        rv_feeder_outages.setLayoutManager(recylerViewLayoutManager);
        FeederOutageAdapter adapter= new FeederOutageAdapter(this, feederOutageModels);
        //consumer_Adapter.setClickListener(Operating.this);
        rv_feeder_outages.setAdapter(adapter);
    }

}
