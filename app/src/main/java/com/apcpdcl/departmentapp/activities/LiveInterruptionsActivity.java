package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.interfaces.IUpdateList;
import com.apcpdcl.departmentapp.models.InterruptionModel;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class LiveInterruptionsActivity extends AppCompatActivity implements IUpdateList {


    @BindView(R.id.tv_section)
    TextView tv_section;
    @BindView(R.id.tv_interruptions)
    TextView tv_interruptions;
    @BindView(R.id.tv_updated)
    TextView tv_updated;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_inter_total)
    TextView tv_inter_total;
    @BindView(R.id.tv_updated_total)
    TextView tv_updated_total;
    @BindView(R.id.tv_balance_total)
    TextView tv_balance_total;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.ll_main_view)
    LinearLayout ll_main_view;
    public ProgressDialog pDialog;
    private String sectionCode;
    private static IUpdateList iUpdateList;
    private InterruptionModel interruptionModel;

    public static IUpdateList getInstance() {
        return iUpdateList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_interruptions_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        iUpdateList = this;
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        pDialog = new ProgressDialog(this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getLiveInterruptionsAbstractData();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    /*GET LIVE INTERRUPTIONS ABSTRACT DATA*/
    private void getLiveInterruptionsAbstractData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        JSONObject requestParams = new JSONObject();
        HttpEntity entity;
        try {
            requestParams.put("sectionId", sectionCode);
            // requestParams.put("sectionId", "51115");
            try {
                entity = new StringEntity(requestParams.toString());
                client.post(this, Constants.GET_LIVE_INTERRUPTIONS_ABSTRACT, entity, "application/json", new AsyncHttpResponseHandler() {
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
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject json = jsonArray.optJSONObject(i);
                                            interruptionModel = new Gson().fromJson(json.toString(),
                                                    InterruptionModel.class);
                                            setData();
                                        }
                                    } else {

                                        ll_main_view.setVisibility(View.GONE);
                                        tv_no_data.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Utility.showCustomOKOnlyDialog(LiveInterruptionsActivity.this, jsonObject.optString("ERROR"));
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
                        Utility.showCustomOKOnlyDialog(LiveInterruptionsActivity.this, error.getMessage());
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
        ll_main_view.setVisibility(View.VISIBLE);
        tv_no_data.setVisibility(View.GONE);
        tv_section.setText(interruptionModel.getSectionName());
        tv_interruptions.setText("" + interruptionModel.getCumilativeFrdInts());
        tv_inter_total.setText("" + interruptionModel.getCumilativeFrdInts());

        tv_updated.setText("" + interruptionModel.getUpdateReasons());
        tv_updated_total.setText("" + interruptionModel.getUpdateReasons());

        tv_balance.setText("" + interruptionModel.getBalanceReasons());
        tv_balance_total.setText("" + interruptionModel.getBalanceReasons());

    }


    @Override
    public void updateList(String id) {
        if (Utility.isNetworkAvailable(this)) {
            getLiveInterruptionsAbstractData();
        }
    }

    @OnClick(R.id.tv_balance)
    void navigateToList() {
        if (interruptionModel.getBalanceReasons() > 0) {
            Intent in = new Intent(getApplicationContext(), LiveInterruptionsListActivity.class);
            startActivity(in);
        }
    }

}
