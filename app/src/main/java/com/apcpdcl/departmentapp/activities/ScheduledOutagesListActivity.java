package com.apcpdcl.departmentapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.OutagesListAdapter;
import com.apcpdcl.departmentapp.models.OutagesModel;
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

public class ScheduledOutagesListActivity extends AppCompatActivity {
    @BindView(R.id.lv_outages)
    ListView lv_outages;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.et_restore_date)
    EditText et_restore_date;
    @BindView(R.id.ll_header)
    LinearLayout ll_header;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private ArrayList<OutagesModel> outagesModelArrayList;

    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String sectionCode;
    private String userID;
    private String status = "";

    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private boolean isRestore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_outages_list);
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
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        userID = prefs.getString("UserName", "");
        pDialog = new ProgressDialog(ScheduledOutagesListActivity.this);
        //et_date.setText(Utility.nowDate());


    }


    /*GET SCHEDULED OUTAGES DATA*/
    private void getScheduledOutagesData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        final JSONObject requestParams = new JSONObject();
        HttpEntity entity;
        try {
            requestParams.put("sectionId", sectionCode);
            requestParams.put("schedOutageFromDate", et_date.getText().toString());
            requestParams.put("schedOutageToDate", et_restore_date.getText().toString());
            try {
                entity = new StringEntity(requestParams.toString());
                client.post(this, Constants.SCHEDULED_POWER_OUTAGE_LIST_REPORT, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utility.showLog("JSON", requestParams.toString());
                        Utility.showLog("onSuccess", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                                if (jsonObject.has("RESPONSE")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
                                    outagesModelArrayList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json = jsonArray.optJSONObject(i);
                                        OutagesModel interruptionModel = new Gson().fromJson(json.toString(),
                                                OutagesModel.class);
                                        outagesModelArrayList.add(interruptionModel);
                                    }
                                    if (outagesModelArrayList.size() > 0) {
                                        setListData();
                                    } else {
                                        if (pDialog != null && pDialog.isShowing()) {
                                            pDialog.dismiss();
                                        }
                                        ll_header.setVisibility(View.GONE);
                                        lv_outages.setVisibility(View.GONE);
                                        tv_no_data.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                if (pDialog != null && pDialog.isShowing()) {
                                    pDialog.dismiss();
                                }
                                Utility.showCustomOKOnlyDialog(ScheduledOutagesListActivity.this, jsonObject.optString("ERROR"));
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
                        Utility.showCustomOKOnlyDialog(ScheduledOutagesListActivity.this, error.getMessage());
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
        if (outagesModelArrayList.size() > 0) {
            OutagesListAdapter outagesListAdapter = new OutagesListAdapter(this, outagesModelArrayList);
            lv_outages.setAdapter(outagesListAdapter);
            Utility.setListViewHeightBasedOnChildren(lv_outages);
            lv_outages.setVisibility(View.VISIBLE);
            ll_header.setVisibility(View.VISIBLE);
            tv_no_data.setVisibility(View.GONE);
        } else {
            ll_header.setVisibility(View.GONE);
            lv_outages.setVisibility(View.GONE);
            tv_no_data.setVisibility(View.VISIBLE);
        }
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    @OnClick(R.id.et_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                1);
        datePickerDialog.show();

    }
    @OnClick(R.id.btn_submit)
    void getData() {
        if (Utility.isValueNullOrEmpty(et_date.getText().toString())||
                Utility.isValueNullOrEmpty(et_restore_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please select dates to get Data");
        }else {
            if (Utility.isNetworkAvailable(this)) {
                pDialog.show();
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                getScheduledOutagesData();
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }
    }

    @OnClick(R.id.et_restore_date)
    void openRestoreDatePicker() {
        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showToastMessage(this,
                    "Please Select Outage Date first");
        } else {
            isRestore = true;
            final Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH) + 1,
                    1);
            c.set(mYear, (mMonth - 1), mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();

        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            if (isRestore) {
                et_restore_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1).toUpperCase() + "-" + year);
                isRestore = false;
            } else {
                et_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1).toUpperCase() + "-" + year);
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;
                et_restore_date.setText("");
            }
        }

    };


}
