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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.RequestedLCListAdapter;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.customviews.LCDatePickerFragment;
import com.apcpdcl.departmentapp.models.LCRequestModel;
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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 24-03-2018.
 */

public class RequestedLCListActivity extends AppCompatActivity {

    @BindView(R.id.lv_lc_req)
    ListView lv_lc_req;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.rl_date)
    RelativeLayout rl_date;
    @BindView(R.id.iv_cal)
    ImageView iv_cal;
    @BindView(R.id.spn_status)
    Spinner spn_status;
    private String status = "";
    private ArrayList<LCRequestModel> lcRequestModels;
    private ArrayList<LCRequestModel> lcRequestModels_filter;
    private RequestedLCListAdapter aelcListAdapter;
    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String lmCode;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_request_list);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    /*Initialize Views*/
    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title.setText("Requested LC's");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        lmCode = prefs.getString("LMCode", "");
        userID = prefs.getString("UserName", "");
        pDialog = new ProgressDialog(RequestedLCListActivity.this);
        et_date.setText(Utility.nowDate());
        setStatusSpinnerData();
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getLCRequestsData();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
        et_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Utility.isNetworkAvailable(RequestedLCListActivity.this)) {
                    pDialog.show();
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    getLCRequestsData();
                } else {
                    Utility.showCustomOKOnlyDialog(RequestedLCListActivity.this,
                            Utility.getResourcesString(RequestedLCListActivity.this,
                                    R.string.no_internet));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /*GET LIVE INTERRUPTIONS ABSTRACT DATA*/
    private void getLCRequestsData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        JSONObject requestParams = new JSONObject();
        HttpEntity entity;
        try {
            requestParams.put("lcRequestedId", lmCode);
            requestParams.put("lcRequestedDate", et_date.getText().toString().toUpperCase());
            try {
                entity = new StringEntity(requestParams.toString());
                client.post(this, Constants.LC_TRACKING_STATUS, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utility.showLog("onSuccess", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                                if (jsonObject.has("RESPONSE")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
                                    lcRequestModels = new ArrayList<>();
                                    lcRequestModels_filter = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json = jsonArray.optJSONObject(i);
                                        LCRequestModel interruptionModel = new Gson().fromJson(json.toString(),
                                                LCRequestModel.class);
                                        lcRequestModels.add(interruptionModel);
                                        lcRequestModels_filter.add(interruptionModel);
                                    }
                                    if (lcRequestModels.size() > 0) {
                                        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
                                            setListData();
                                        } else {
                                            updateListByDate();
                                        }
                                    } else {
                                        if (pDialog != null && pDialog.isShowing()) {
                                            pDialog.dismiss();
                                        }
                                        tv_no_data.setVisibility(View.VISIBLE);
                                        lv_lc_req.setVisibility(View.GONE);
                                    }
                                }
                            } else {
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                Utility.showCustomOKOnlyDialog(RequestedLCListActivity.this, jsonObject.optString("ERROR"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Utility.showLog("Exception", e.getLocalizedMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        Utility.showLog("error", error.toString());
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Utility.showCustomOKOnlyDialog(RequestedLCListActivity.this, error.getMessage());
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     *Set Service List DAta to ListView
     * */
    private void setListData() {
        if (lcRequestModels_filter.size() > 0) {
            aelcListAdapter = new RequestedLCListAdapter(this, lcRequestModels_filter, userID, userID);
            lv_lc_req.setAdapter(aelcListAdapter);
            Utility.setListViewHeightBasedOnChildren(lv_lc_req);
            lv_lc_req.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
        } else {
            lv_lc_req.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }

    }

    @OnClick({R.id.et_date, R.id.rl_date, R.id.iv_cal})
    void openDatePicker() {
        LCDatePickerFragment date = new LCDatePickerFragment(et_date);
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt(Constants.YEAR, calender.get(Calendar.YEAR));
        args.putInt(Constants.MONTH, calender.get(Calendar.MONTH));
        args.putInt(Constants.DAY, calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(LCDatePickerFragment.ondate);
        date.show(getSupportFragmentManager(), Constants.DATE_PICKER);
    }

    private void updateListByDate() {
        lcRequestModels_filter = new ArrayList<>();
        if (status.equalsIgnoreCase("") || status.equalsIgnoreCase("all")) {
            for (int i = 0; i < lcRequestModels.size(); i++) {
                if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
                    lcRequestModels_filter.add(lcRequestModels.get(i));
                } else if (lcRequestModels.get(i).getLcRequestedDate().equalsIgnoreCase(et_date.getText().toString())) {
                    lcRequestModels_filter.add(lcRequestModels.get(i));

                }


            }
        } else {
            for (int i = 0; i < lcRequestModels.size(); i++) {
                if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
                    if (lcRequestModels.get(i).getLcTrackingStatus().equalsIgnoreCase(status)) {
                        lcRequestModels_filter.add(lcRequestModels.get(i));
                    }
                } else if (lcRequestModels.get(i).getLcRequestedDate().equalsIgnoreCase(et_date.getText().toString())
                        && lcRequestModels.get(i).getLcTrackingStatus().equalsIgnoreCase(status)) {
                    lcRequestModels_filter.add(lcRequestModels.get(i));
                }
            }
        }

        setListData();
    }

    private void setStatusSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("ALL");
        list.add("PENDING at AE");
        list.add("APPROVED");
        list.add("REJECTED");
        list.add("ISSUED");
        list.add("RETURNED");
        list.add("CLOSED");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_status.setAdapter(newlineAdapter);
        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                status = parent.getItemAtPosition(position).toString();
                if (lcRequestModels != null && lcRequestModels.size() > 0) {
                    updateListByDate();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

}
