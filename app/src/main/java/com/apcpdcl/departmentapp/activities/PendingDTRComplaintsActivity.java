package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.WindowManager;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.DTRComplaintsRecyclerviewAdapter;
import com.apcpdcl.departmentapp.models.DTrComplaint;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class PendingDTRComplaintsActivity extends AppCompatActivity {

    private RecyclerView dtrcomplaints_recyclerview;
    private DTRComplaintsRecyclerviewAdapter dtrcomplaints_Adapter;
    RecyclerView.LayoutManager recylerViewLayoutManager;
    ProgressDialog progressDialog;
    private ArrayList<DTrComplaint> complaintsList;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();

    public SharedPreferences prefs;
    String strSection_Code,sUserId,costCenterCode;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_dtrcomplaints);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

//        prefs = getSharedPreferences("loginPrefs", 0);
//        strSection_Code = prefs.getString("Section_Code", "");
        strSection_Code = AppPrefs.getInstance(this).getString("SECTIONCODE", "");
        sUserId = AppPrefs.getInstance(this).getString("USERID", "");
        costCenterCode = AppPrefs.getInstance(PendingDTRComplaintsActivity.this).getString("COSTCENTER","");

        dtrcomplaints_recyclerview = (RecyclerView) findViewById(R.id.dtrcomplaints_recyclerview);
        recylerViewLayoutManager = new LinearLayoutManager(PendingDTRComplaintsActivity.this);
        dtrcomplaints_recyclerview.setLayoutManager(recylerViewLayoutManager);

        pDialog = new ProgressDialog(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                //invokeDtrComplaintsWebService();
                getDTRComplaints();
            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void invokeDtrComplaintsWebService() {
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        // client.get("http://103.231.215.245:8080/CscSapService/CCC2SAP/CCCDetails/"+strSection_Code,
        // new AsyncHttpResponseHandler() {
        client.get("http://112.133.252.110:8080/CscSapService/CCC2SAP/CCCDetails/" + strSection_Code,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            JSONObject obj = new JSONObject(response);
                            // JSONArray array = new JSONArray();
                            // JSONObject obj1 = new JSONObject(response);
                            // obj1.put("ComplId","2209026156");
                            // obj1.put("SecName","CHANDARLAPADU");
                            // obj1.put("PhoneNo","9502666399");
                            // obj1.put("Status","HDTRUPDATED");
                            // obj1.put("Trandt","05-Sep-2022 13:40:00");
                            // array.put(obj1);
                            // JSONObject obj = new JSONObject(response);
                            // obj.put("COMDETAILS",array);
                            // JSONObject obj =
                            // {"COMDETAILS":[{"ComplId":"2209026156","SecName":"CHANDARLAPADU","PhoneNo":"9502666399","Status":"HDTRUPDATED"},{"ComplId":"2209036221","SecName":"CHANDARLAPADU","PhoneNo":"7993834485","Status":"HDTRUPDATED"},{"ComplId":"2209036229","SecName":"CHANDARLAPADU","PhoneNo":"6305459478","Status":"HDTRUPDATED"}]};
                            JSONArray compArray = obj.getJSONArray("COMDETAILS");
                            if (compArray.length() > 0) {
                                complaintsList = new ArrayList<>();
                                for (int i = 0; i < compArray.length(); i++) {
                                    JSONObject json = compArray.optJSONObject(i);
                                    DTrComplaint dTrComplaint = new Gson().fromJson(json.toString(),
                                            DTrComplaint.class);
                                    complaintsList.add(dTrComplaint);
                                }
                            }
                            if (complaintsList.size() > 0) {
                                dtrcomplaints_Adapter = new DTRComplaintsRecyclerviewAdapter(
                                        PendingDTRComplaintsActivity.this, complaintsList);
                                dtrcomplaints_recyclerview.setAdapter(dtrcomplaints_Adapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "There are no records", Toast.LENGTH_LONG)
                                        .show();
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
                        Utility.showCustomOKOnlyDialog(PendingDTRComplaintsActivity.this,
                                Utility.getResourcesString(PendingDTRComplaintsActivity.this, R.string.err_session));
                        Utility.showLog("error", error.toString());
                    }
                });
    }

    /* *
     *Get DTR Complaints List
     * */
    private void getDTRComplaints() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);

        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+ AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
        StringEntity entity = null;
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("bname", sUserId);
            jsonParams.put("type", "DTR");
            entity = new StringEntity(jsonParams.toString());
        }catch (Exception e){

        }
        client.post(this, ServiceConstants.USER_DTR_COMPLAINTLIST,headers,entity,"application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                Utility.showLog("onSuccess", responseStr);
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    JSONObject resposneObj = new JSONObject(responseStr);
                    resposneObj = resposneObj.getJSONObject("response");
                    String status = resposneObj.getString("success");
                    if(status.equalsIgnoreCase("True")){
                        if (resposneObj.has("data")) {
                            JSONArray jsonArray = resposneObj.getJSONArray("data");
                            if(jsonArray != null && jsonArray.length()>0){
                                complaintsList = new ArrayList<>();
                                JSONObject singleResponseObj = null;
                                for (int i = 0; i < jsonArray.length() ; i++) {
                                    singleResponseObj = jsonArray.getJSONObject(i);
                                    if (singleResponseObj.getString("complaint_status").trim().equalsIgnoreCase("Healthy DTR Updated")){
                                        if (singleResponseObj != null && singleResponseObj.length() > 0) {
                                            DTrComplaint dTrComplaint = new DTrComplaint(
                                                    singleResponseObj.getString("complaint_id"),
                                                    costCenterCode,
                                                    singleResponseObj.getString("complaint_creation"),
                                                    "",
                                                    singleResponseObj.getString("complaint_status")
                                            );
                                            complaintsList.add(dTrComplaint);
                                        }
                                    }
                                }
                            }
                            if (complaintsList.size() > 0) {
                                dtrcomplaints_Adapter = new DTRComplaintsRecyclerviewAdapter(
                                        PendingDTRComplaintsActivity.this, complaintsList);
                                dtrcomplaints_recyclerview.setAdapter(dtrcomplaints_Adapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "There are no records", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }else{

                        }


                    }else {
                        Toast.makeText(getApplicationContext(), "There are no records", Toast.LENGTH_LONG)
                                .show();
                    }

                } catch (JSONException e) {
                    //dismissDialog();
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //dismissDialog();
                Utility.showCustomOKOnlyDialog(PendingDTRComplaintsActivity.this, error.getMessage());
            }
        });
    }

}