package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.MeterExceptionListModel;
import com.apcpdcl.departmentapp.models.MeterExceptionModel;
import com.apcpdcl.departmentapp.models.MeterExceptionsFullModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.sqlite.MeterChangeDatabaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 28-03-2018.
 */

public class LMMeterChangeDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_new_request)
    Button btn_new_request;
    @BindView(R.id.btn_pending)
    Button btn_pending;
    @BindView(R.id.btn_offline_list)
    Button btn_offline_list;
    private MeterChangeDatabaseHandler dbManager;
    private ProgressDialog pDialog;
    private String lmcode, sectionCode, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_meter_change_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        lmcode = prefs.getString("LMCode", "");
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        sectionCode = prefs.getString("Section_Code", "");
//        userId = prefs.getString("UserName", "");
        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        dbManager = new MeterChangeDatabaseHandler(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbManager.getMeterChangeRequestsCount() > 0) {
            btn_offline_list.setVisibility(View.VISIBLE);
        } else {
            btn_offline_list.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_offline_list)
    void navigateToOfflineList() {
        Intent intent = new Intent(this, OfflineMeterChangeListActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.btn_new_request)
    void navigateToEdit() {
        Intent in = new Intent(this, MeterChangeEntryFormActivity.class);
        startActivity(in);

    }

    @OnClick(R.id.btn_pending)
    void navigateToPending() {
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            //getExceptionNameCount();
            getExceptionList();
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
        }
    }

    /* *
     *SAP ISU Get Current Month Exceptions
     * */
    private void getExceptionList() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);

        HttpEntity entity;
        try {
            JSONObject requestObjPayLoad = new JSONObject();
            try {
                requestObjPayLoad = new JSONObject();
                requestObjPayLoad.put("Method_Name", "ServConnList");
                requestObjPayLoad.put("UserId", userId);//userId
                requestObjPayLoad.put("SectionCode", sectionCode);//sectionCode
            } catch (Exception e) {

            }
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/MeterReplacement/GetServiceConnectionList/DEV", headers, entity,"application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");
                        String successStr = jsonObject.getString("success");
                        MeterExceptionsFullModel meterExceptionsFullModel = new MeterExceptionsFullModel();
                        ArrayList<MeterExceptionListModel> meterExceptionListArrModels = new ArrayList<>();
                        ArrayList<String> keys = new ArrayList<>();
                        keys.add("MS");
                        if(successStr.equalsIgnoreCase("True")){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if(jsonArray.length()>0 && jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.optJSONObject(i);
                                    MeterExceptionListModel exceptionListModel = new Gson().fromJson(json.toString(),
                                            MeterExceptionListModel.class);
                                    meterExceptionListArrModels.add(exceptionListModel);
                                }
                                meterExceptionsFullModel.setKeys(keys);
                                meterExceptionsFullModel.setMeterExceptionListModels(meterExceptionListArrModels);
                                Intent intent = new Intent(LMMeterChangeDashBoardActivity.this, MeterExceptionListActivity.class);
                                intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
                                startActivity(intent);
                            }

                        }


//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.optJSONObject(i);
//                            MeterExceptionModel registrationModel = new Gson().fromJson(json.toString(),
//                                    MeterExceptionModel.class);
//                            meterExceptionModels.add(registrationModel);
//                        }
//                        meterExceptionsFullModel.setKeys(keys);
//                        meterExceptionsFullModel.setMeterExceptionModels(meterExceptionModelArrayList);
//                        Intent intent = new Intent(LMMeterChangeDashBoardActivity.this, MeterExceptionListActivity.class);
//                        intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
//                        startActivity(intent);
//                        Iterator<String> iter = jsonObject.keys();
//                        ArrayList<MeterExceptionModel> meterExceptionModelArrayList = new ArrayList<>();
//                        while (iter.hasNext()) {
//                            String key = iter.next();
//                            keys.add(key);
//                            try {
//                                JSONArray jsonArray = jsonObject.getJSONArray(key);
//                                MeterExceptionModel meterExceptionModel = new MeterExceptionModel();
//                                ArrayList<MeterExceptionModel> meterExceptionModels = new ArrayList<>();
//                                for (int i = 0; i < jsonArray.length(); i++) {
//                                    JSONObject json = jsonArray.optJSONObject(i);
//                                    MeterExceptionModel registrationModel = new Gson().fromJson(json.toString(),
//                                            MeterExceptionModel.class);
//                                    meterExceptionModels.add(registrationModel);
//                                }
//                                meterExceptionModel.setMeterExceptionModels(meterExceptionModels);
//                                meterExceptionModelArrayList.add(meterExceptionModel);
//                            } catch (JSONException e) {
//                                // Something went wrong!
//                            }
//                        }
//                        meterExceptionsFullModel.setKeys(keys);
//                        meterExceptionsFullModel.setMeterExceptionModels(meterExceptionModelArrayList);
//                        Intent intent = new Intent(LMMeterChangeDashBoardActivity.this, MeterExceptionListActivity.class);
//                        intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
//                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    Utility.showCustomOKOnlyDialog(LMMeterChangeDashBoardActivity.this, error.getLocalizedMessage());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    /* *
     *Get Current Month Exceptions
     * */
    private void getExceptionNameCount() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("LMCODE", lmcode);
        client.post(Constants.GET_CUURENT_MONTH_EXCEPTIONS, requestParams, new AsyncHttpResponseHandler() {
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
                    Intent intent = new Intent(LMMeterChangeDashBoardActivity.this, MeterExceptionListActivity.class);
                    intent.putExtra(MeterExceptionsFullModel.class.getSimpleName(), meterExceptionsFullModel);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                Utility.showCustomOKOnlyDialog(LMMeterChangeDashBoardActivity.this, error.getLocalizedMessage());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        });
    }
}
