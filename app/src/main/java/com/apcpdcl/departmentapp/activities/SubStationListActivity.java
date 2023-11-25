package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SubStationListAdapter;
import com.apcpdcl.departmentapp.interfaces.IUpdateList;
import com.apcpdcl.departmentapp.models.SubStationModel;
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
import com.loopj.android.http.RequestParams;

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

public class SubStationListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, IUpdateList {
    @BindView(R.id.lv_reg)
    ListView lv_reg;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    private ArrayList<SubStationModel> subStationModels;
    private SubStationListAdapter subStationListAdapter;
    private android.app.ProgressDialog pDialog;
    private String sectionCode = "";
    private static IUpdateList iUpdateList;
    public static IUpdateList getInstance(){
        return iUpdateList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_substation_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        android.content.SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        ButterKnife.bind(this);
        iUpdateList = this;
        pDialog = new ProgressDialog(SubStationListActivity.this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getSubStations();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
        if (!Utility.isLocationEnabled(SubStationListActivity.this)) {
            displayLocationSettingsRequest(SubStationListActivity.this);
        }
        lv_reg.setOnItemClickListener(this);
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
                       /* try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(SubStationListActivity.this, GPSTrackerActivity.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Utility.showLog("PendingIntent", "PendingIntent unable to execute request.");
                        }*/
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Utility.showLog("Location", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
    /*
     *Set Sub-Staions Data to ListView
     * */
    private void setListData() {
        subStationListAdapter = new SubStationListAdapter(this, subStationModels);
        lv_reg.setAdapter(subStationListAdapter);
        implementsSearch();
    }

    /*
     *Get SUB-STATIONS
     * */
    private void getSubStations() {
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + Constants.SUBSTATION_LIST);
        Utility.showLog("sectionCode", sectionCode);
        RequestParams params = new RequestParams();
        params.put("SECTION", sectionCode);
        client.setTimeout(50000);
        client.post(Constants.URL + Constants.SUBSTATION_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("SSLIST")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("SSLIST");
                        subStationModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.optJSONObject(i);
                            SubStationModel registrationModel = new Gson().fromJson(json.toString(),
                                    SubStationModel.class);
                            subStationModels.add(registrationModel);
                        }

                        if (subStationModels.size() > 0) {
                            setListData();
                        } else {
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            tv_no_data.setVisibility(View.VISIBLE);
                            lv_reg.setVisibility(View.GONE);
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
            }
        });
    }

    //IMPLEMENT SEARCH
    private void implementsSearch() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (subStationListAdapter != null)
                    subStationListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.iv_search)
    void implementSearch() {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (subStationModels.get(i).getEXISTS().equalsIgnoreCase("YES")){
            Utility.showCustomOKOnlyDialog(this,"Co-ordinates already added to "+subStationModels.get(i).getSSNAME());
        }else {
            if (Utility.isLocationEnabled(SubStationListActivity.this)) {
                Intent intent = new Intent(this, AddSubStationCoordinates.class);
                intent.putExtra("SS_NAME", subStationModels.get(i).getSSNAME());
                intent.putExtra("SS_CODE", subStationModels.get(i).getSSCODE());
                startActivity(intent);
            } else {
                displayLocationSettingsRequest(SubStationListActivity.this);
            }
        }
    }

    @Override
    public void updateList(String id) {
        for (int i = 0; i < subStationModels.size(); i++) {
            if (id.equalsIgnoreCase(subStationModels.get(i).getSSCODE())) {
                SubStationModel subStationModel = subStationModels.get(i);
                subStationModel.setEXISTS("YES");
                subStationModels.remove(i);
                subStationModels.add(i,subStationModel);
                subStationListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
