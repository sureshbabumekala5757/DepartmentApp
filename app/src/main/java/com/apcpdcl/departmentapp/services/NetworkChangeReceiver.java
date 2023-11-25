package com.apcpdcl.departmentapp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.apcpdcl.departmentapp.models.FailureDcList;
import com.apcpdcl.departmentapp.models.MeterChangeEntryModel;
import com.apcpdcl.departmentapp.sqlite.MeterChangeDatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.NetworkFailureDcListDatabaseHandler;
import com.apcpdcl.departmentapp.sqlite.NetworkFailureMatsDcListDatabaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 27-12-2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    String strTotalDcList, strActivity;
    private ArrayList<FailureDcList> failureDcList = new ArrayList<FailureDcList>();
    private ArrayList<String> strfailureDcList = new ArrayList<String>();
    NetworkFailureDcListDatabaseHandler db;
    NetworkFailureMatsDcListDatabaseHandler matsDB;
    private ArrayList<MeterChangeEntryModel> meterChangeModels = new ArrayList<>();
    private MeterChangeDatabaseHandler meterChangeDatabaseHandler;
    private Context mContext;


    @Override
    public void onReceive(final Context mContext, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            this.mContext = mContext;
            pushDataToServer();
            pushMatsDataToServer();
            pushMeterChangeDataToServer();
        }
    }

    private void pushDataToServer() {
        db = new NetworkFailureDcListDatabaseHandler(mContext);
        SharedPreferences prefs = mContext.getSharedPreferences("operatingPrefs", 0);
        strActivity = prefs.getString("StrActivity", "");
        try {
            failureDcList.clear();
            failureDcList.addAll(db.getAllFailuredclists());
            for (int i = 0; i < failureDcList.size(); i++) {
                strfailureDcList.add(failureDcList.get(i).getTotalString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strfailureDcList != null && strfailureDcList.size() > 0) {
            strTotalDcList = generate_string(strfailureDcList);
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (strActivity.equals("OperatingDetails")) {
                    RequestParams params = new RequestParams();
                    params.put("DATA", strTotalDcList);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(Constants.UPDATE, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString("STATUS");
                                if (status.equals("SUCCESS")) {
                                    db.clearTable();
                                    strfailureDcList.clear();
                                    //Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                                    if (strActivity.equals("OperatingDetails")) {
                                        mContext.unregisterReceiver(new NetworkChangeReceiver());
                                    }
                                    SharedPreferences preferences = mContext.getSharedPreferences("operatingPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error, String content) {
                            Log.e("error", error.toString());
                        }
                    });
                }
            }
        }
    }

    private void pushMatsDataToServer() {
        matsDB = new NetworkFailureMatsDcListDatabaseHandler(mContext);
        SharedPreferences prefs = mContext.getSharedPreferences("operatingPrefs", 0);
        strActivity = prefs.getString("StrActivity", "");
        try {
            failureDcList.clear();
            failureDcList.addAll(db.getAllFailuredclists());
            for (int i = 0; i < failureDcList.size(); i++) {
                strfailureDcList.add(failureDcList.get(i).getTotalString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (strfailureDcList != null && strfailureDcList.size() > 0) {
            strTotalDcList = generate_string(strfailureDcList);
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (strActivity.equals("OperatingDetails")) {
                    RequestParams params = new RequestParams();
                    params.put("DATA", strTotalDcList);
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(Constants.MATS_UPDATE, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString("STATUS");
                                if (status.equals("SUCCESS")) {
                                    matsDB.clearTable();
                                    strfailureDcList.clear();
                                    //Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                                    if (strActivity.equals("OperatingDetails")) {
                                        mContext.unregisterReceiver(new NetworkChangeReceiver());
                                    }
                                    SharedPreferences preferences = mContext.getSharedPreferences("operatingPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.clear();
                                    editor.apply();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error, String content) {
                            Log.e("error", error.toString());
                        }
                    });
                }
            }
        }
    }

    private static String generate_string(ArrayList<String> list) {
        String geneated_string_value = "";
        for (String v : list) {
            if (!geneated_string_value.equals("")) {
                geneated_string_value = geneated_string_value + "||" + v;
            } else {
                geneated_string_value = v;
            }

        }
        return geneated_string_value;
    }

    private void getMeterChangeModel(String serviceNo, String msg, String status) {
        MeterChangeEntryModel meterChangeModel;
        for (int i = 0; i < meterChangeModels.size(); i++) {
            if (meterChangeModels.get(i).getServiceNo().equalsIgnoreCase(serviceNo)) {
                meterChangeModel = meterChangeModels.get(i);
                meterChangeModel.setMsg(msg);
                meterChangeModel.setStatus(status);
                meterChangeDatabaseHandler.updateTotalModel(meterChangeModel);
                break;
            }
        }
    }

    private void pushMeterChangeDataToServer() {
        meterChangeDatabaseHandler = new MeterChangeDatabaseHandler(mContext);
        meterChangeModels.clear();
        meterChangeModels.addAll(meterChangeDatabaseHandler.getAllMeterChangeDetails());
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            for (int i = 0; i < meterChangeModels.size(); i++) {
                if (!meterChangeModels.get(i).getStatus().equalsIgnoreCase("F")) {
                    RequestParams params = new RequestParams();
                    params.put("INPUT", meterChangeModels.get(i).getFinalDataString());
                    Utility.showLog("INPUT", meterChangeModels.get(i).getFinalDataString());
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post(Constants.URL + Constants.POST_DETAILS, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            Utility.showLog("onSuccess", response);
                            Utility.showLog("response", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                MeterChangeEntryModel meterChangeModel = new MeterChangeEntryModel();
                                meterChangeModel.setServiceNo(jsonObject.optString("USCNO"));
                                if (jsonObject.has("RQSTATUS")) {
                                    if (jsonObject.optString("RQSTATUS").equalsIgnoreCase("S")) {
                                        meterChangeDatabaseHandler.deleteContact(meterChangeModel);
                                    } else {
                                        getMeterChangeModel(meterChangeModel.getServiceNo(),
                                                jsonObject.optString("MSG"),
                                                jsonObject.optString("RQSTATUS"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Utility.showLog("JSONException", e.getLocalizedMessage());
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error, String content) {
                            Utility.showLog("error", error.toString());
                        }
                    });
                }
            }
        }
    }
}
