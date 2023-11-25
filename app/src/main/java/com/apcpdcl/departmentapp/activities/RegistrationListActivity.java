package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
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

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.RegistrationListAdapter;
import com.apcpdcl.departmentapp.models.RegistrationModel;
import com.apcpdcl.departmentapp.models.ServiceDetailsModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 24-03-2018.
 */

public class RegistrationListActivity extends AppCompatActivity {
    @BindView(R.id.lv_reg)
    ListView lv_reg;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    private ArrayList<RegistrationModel> registrationModels;
    private RegistrationListAdapter registrationListAdapter;
    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String sectionCode, userId;

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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        sectionCode = prefs.getString("Section_Code", "");
//        userId = prefs.getString("USERID", "");
        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        pDialog = new ProgressDialog(RegistrationListActivity.this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //getRegistrationNumbers();
            getPendingRegNumbers();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    public void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(RegistrationListActivity.this, GPSTrackerActivity.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Utility.showLog("PendingIntent", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Utility.showLog("Location", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
    public void getServiceDetails(String serviceNumber) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + Constants.GEO_REG_DETAILS + "/" + serviceNumber);
        JSONObject requestObjPayLoad = null;
        HttpEntity entity;
        try {
            try {
                requestObjPayLoad = new JSONObject();
                requestObjPayLoad.put("RegID", serviceNumber);
            } catch (Exception e) {

            }
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/AESearchToTagLocation/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");

                        if(jsonObject.getString("success").equalsIgnoreCase("true")){
//                            ServiceDetailsModel serviceDetailsModel = new Gson().fromJson(jsonObject.toString(),
//                                    ServiceDetailsModel.class);
                            ServiceDetailsModel serviceDetailsModel = new ServiceDetailsModel(
                                    jsonObject.getString("category"),
                                    jsonObject.getString("load"),
                                    jsonObject.getString("consumeraddress"),
                                    jsonObject.getString("consumername"),
                                    jsonObject.getString("registrationdate"),
                                    jsonObject.getString("registrationid"),
                                    jsonObject.getString("longitude"),
                                    jsonObject.getString("latitude"),
                                    "",
                                    "",
                                    ""
                            );

                            Intent intent = new Intent(RegistrationListActivity.this, GPSTrackerActivity.class);
                            intent.putExtra(Constants.SERVICE_DETAILS, serviceDetailsModel);
                            startActivity(intent);
                        }
//                        if (jsonObject.has("RESPONSE")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
//                            if (jsonArray.length() > 0) {
//                                JSONObject json = jsonArray.optJSONObject(0);
//                                if (json.has("STATUS")) {
//                                    Utility.showCustomOKOnlyDialog(LMRegistrationListActivity.this, json.optString("STATUSMSG"));
//                                    et_search.setText("");
//                                } else {
//                                    ServiceDetailsModel serviceDetailsModel = new Gson().fromJson(json.toString(),
//                                            ServiceDetailsModel.class);
//                                    Intent intent = new Intent(LMRegistrationListActivity.this, NewConnectionReleaseActivity.class);
//                                    intent.putExtra(Constants.SERVICE_DETAILS, serviceDetailsModel);
//                                    startActivity(intent);
//                                }
//
//                            }
//                        }
                    } catch (JSONException e) {
                        Utility.showCustomOKOnlyDialog(RegistrationListActivity.this,
                                "Something went wrong please try again later...");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(RegistrationListActivity.this, "Something went wrong please try again later...");
                }
            });
        }catch (Exception e){

        }
    }
    public void getServiceDetails1(String serviceNumber) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + Constants.REG_DETAILS + "/" + serviceNumber);
        client.get(Constants.URL + Constants. REG_DETAILS + "/" + serviceNumber, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("RESPONSE")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
                        if (jsonArray.length() > 0) {
                            JSONObject json = jsonArray.optJSONObject(0);
                            if (json.has("STATUS")) {
                                Utility.showCustomOKOnlyDialog(RegistrationListActivity.this, json.optString("STATUSMSG"));
                                et_search.setText("");
                            } else {
                                ServiceDetailsModel serviceDetailsModel = new Gson().fromJson(json.toString(),
                                        ServiceDetailsModel.class);
                                Intent intent = new Intent(RegistrationListActivity.this, GPSTrackerActivity.class);
                                intent.putExtra(Constants.SERVICE_DETAILS, serviceDetailsModel);
                                startActivity(intent);
                            }

                        }
                    }
                } catch (JSONException e) {
                    Utility.showCustomOKOnlyDialog(RegistrationListActivity.this,
                            "Something went wrong please try again later...");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(RegistrationListActivity.this, "Something went wrong please try again later...");
            }
        });
    }
    /*
     *Get Service Numbers
     * */
    private void getPendingRegNumbers() {
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + Constants.GEO_REG_LIST + "/" + sectionCode);
        client.setTimeout(50000);
        HttpEntity entity;
        JSONObject requestObjPayLoad = null;
        try {
            try{
                requestObjPayLoad = new JSONObject();
                requestObjPayLoad.put("Userid",userId);//S4_SROHIT
            }catch (Exception e){

            }
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/AGLServicesGetRegisterationIDs/DEV",headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");
                        if(jsonObject.getString("success").equalsIgnoreCase("true")) {
                            if (jsonObject.has("data")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                registrationModels = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.optJSONObject(i);
//                                    RegistrationModel registrationModel = new Gson().fromJson(json.toString(),
//                                            RegistrationModel.class);
                                    registrationModels.add(new RegistrationModel(json.getString("registrationid")));
                                    //Log.w("#AV", registrationModel.getRegno().toString());
                                }

                                if (registrationModels.size() > 0) {

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
                        }else{
                            Utility.showCustomOKOnlyDialog(RegistrationListActivity.this, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
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
                    Utility.showCustomOKOnlyDialog(RegistrationListActivity.this, "Currently Server is down, Please try again.");
                }
            });
        }catch (Exception e){
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }
    /*
     *Get Service Numbers
     * */
    private void getRegistrationNumbers() {
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + Constants.REG_LIST + "/" + sectionCode);
        client.setTimeout(50000);
        client.get(Constants.URL + Constants.REG_LIST + "/" + sectionCode, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                /*if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }*/
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("REGNO")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("REGNO");
                        registrationModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.optJSONObject(i);
                            RegistrationModel registrationModel = new Gson().fromJson(json.toString(),
                                    RegistrationModel.class);
                            registrationModels.add(registrationModel);

                        }

                        if (registrationModels.size() > 0) {

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
                Utility.showCustomOKOnlyDialog(RegistrationListActivity.this, "Currently Server is down, Please try again.");
            }
        });
    }
    /*
     *Set Service List DAta to ListView
     * */
    private void setListData() {
        registrationListAdapter = new RegistrationListAdapter(this, registrationModels);

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
//        if (Utility.isValueNullOrEmpty(et_search.getText().toString()) || et_search.getText().toString().length() < 20) {
//            Utility.showCustomOKOnlyDialog(this, "Please enter valid 20 digits Registration Number.");
//        } else {
//            if (et_search.getText().toString().substring(0, 5).equalsIgnoreCase(sectionCode)) {
                if (Utility.isNetworkAvailable(this)) {
                    getServiceDetails(et_search.getText().toString());
                } else {
                    Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this,
                            R.string.no_internet));
                }
//            } else {
//                Utility.showCustomOKOnlyDialog(this,
//                        "This Registration Number is not related to you.");
//            }
//        }
    }

 /*   @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (Utility.isLocationEnabled(RegistrationListActivity.this)) {
            if (Utility.isNetworkAvailable(this)) {
                getServiceDetails(registrationModels.get(i).getRegno());
            } else {
                Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this,
                        R.string.no_internet));
            }
        } else {
            displayLocationSettingsRequest(RegistrationListActivity.this);
        }
    }*/
}
