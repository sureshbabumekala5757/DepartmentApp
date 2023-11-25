package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.LiveInterruptionsListAdapter;
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
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class LiveInterruptionsListActivity extends AppCompatActivity implements IUpdateList {


    @BindView(R.id.lv_interruptions)
    ListView lv_interruptions;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    public ProgressDialog pDialog;
    private String sectionCode;
    private static IUpdateList iUpdateList;
    private ArrayList<InterruptionModel> interruptionModels;
    private  LiveInterruptionsListAdapter liveInterruptionsListAdapter;

    public static IUpdateList getInstance() {
        return iUpdateList;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_interruptions_list_activity);
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

    private void setListViewData() {
        liveInterruptionsListAdapter = new LiveInterruptionsListAdapter(this, interruptionModels);
        lv_interruptions.setAdapter(liveInterruptionsListAdapter);
    }

    /*GET LIVE INTERRUPTIONS ABSTRACT DATA*/
    private void getLiveInterruptionsAbstractData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        JSONObject requestParams = new JSONObject();
        HttpEntity entity;
        try {
            requestParams.put("sectionId", sectionCode);
            try {
                entity = new StringEntity(requestParams.toString());
                client.post(this, Constants.GET_LIVE_INTERRUPTIONS_REPORT, entity, "application/json", new AsyncHttpResponseHandler() {
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
                                    interruptionModels = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json = jsonArray.optJSONObject(i);
                                        InterruptionModel interruptionModel = new Gson().fromJson(json.toString(),
                                                InterruptionModel.class);
                                        interruptionModels.add(interruptionModel);
                                    }
                                    if (interruptionModels.size() > 0) {
                                        setListViewData();
                                    }else {
                                        tv_no_data.setVisibility(View.VISIBLE);
                                        lv_interruptions.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                Utility.showCustomOKOnlyDialog(LiveInterruptionsListActivity.this, jsonObject.optString("ERROR"));
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
                        Utility.showCustomOKOnlyDialog(LiveInterruptionsListActivity.this, error.getMessage());
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateList(String id) {
        for (int i = 0; i < interruptionModels.size(); i++) {
            if (id.equalsIgnoreCase(interruptionModels.get(i).getTransId())) {
                interruptionModels.remove(i);
                if (interruptionModels.size() > 0) {
                    liveInterruptionsListAdapter.notifyDataSetChanged();
                }else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    lv_interruptions.setVisibility(View.GONE);
                }
                break;
            }
        }
        LiveInterruptionsActivity.getInstance().updateList(id);
    }
}
