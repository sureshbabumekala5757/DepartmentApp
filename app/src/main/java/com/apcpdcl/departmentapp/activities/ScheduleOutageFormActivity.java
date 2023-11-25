package com.apcpdcl.departmentapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.models.LCRequestDropDownModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class ScheduleOutageFormActivity extends AppCompatActivity {

    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.spn_ss_name)
    Spinner spn_ss_name;
    @BindView(R.id.spn_feeder)
    Spinner spn_feeder;
    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.et_restore_date)
    EditText et_restore_date;
    @BindView(R.id.et_from)
    EditText et_from;
    @BindView(R.id.et_to)
    EditText et_to;
    @BindView(R.id.et_lc_reason)
    EditText et_lc_reason;
    @BindView(R.id.spn_reason)
    Spinner spn_reason;
    @BindView(R.id.tv_footer)
    TextView tv_footer;
    @BindView(R.id.et_duration)
    EditText et_duration;
    @BindView(R.id.ll_duration)
    LinearLayout ll_duration;
    private String ssName = "";
    private String ssCode = "";
    private String feederCode = "";
    private String feederName = "";
    private String sectionNAme = "";
    private String sec_code = "";
    private String reason = "";
    private String sectionCode = "";
    private String UserName = "";
    private String designation = "";
    private String ssoMobileNo = "";
    private String lmMobileNo = "";

    public ProgressDialog pDialog;
    private int mHour, mMinute;
    private int mFromHour, mFromMinute;
    private ArrayList<String> list;
    private int minutes = -1;
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private boolean isRestore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_outage_form_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tv_footer.setSelected(true);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        UserName = prefs.getString("UserName", "");
        designation = prefs.getString("USERTYPE", "");
        lmMobileNo = prefs.getString("MOBILE", "");
        pDialog = new ProgressDialog(this);
        setReasonSpinnerData();
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            RequestParams requestParams = new RequestParams();
            requestParams.put("SEC_CODE", sectionCode);
            getDropDownData(requestParams, false, Constants.SUB_STATION_DETAILS);
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this, R.string.no_internet));
        }
    }


    @OnClick(R.id.btn_confirm)
    void navigateToLiveInterruptions() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                pDialog.show();
                postScheduledOutage(getJSON());
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }
    }

    private void postScheduledOutage(JSONObject json) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(json.toString());
            Utility.showLog("jsonObject", json.toString());
            client.post(this, Constants.SCHEDULED_POWER_OUTAGE_INSERT, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                            Utility.showToastMessage(ScheduleOutageFormActivity.this, jsonObject.optString("RESPONSE"));
                            finish();
                        } else {
                            Utility.showCustomOKOnlyDialog(ScheduleOutageFormActivity.this, jsonObject.optString("ERROR"));
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
                    Utility.showCustomOKOnlyDialog(ScheduleOutageFormActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private boolean validateFields() {
        if (ssName.equalsIgnoreCase("")) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Substation.");
            return false;
        } else if (feederCode.equalsIgnoreCase("")) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Feeder Name.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Scheduled Outage Date.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_from.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Scheduled Outage Time.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_restore_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Scheduled Restore Date.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_to.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Scheduled Restore Time.");
            return false;
        } else if (Utility.isValueNullOrEmpty(reason)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Scheduled Outage Reason.");
            return false;
        }
        return true;
    }

    @OnClick(R.id.et_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                1);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
        setDuration();
    }

    @OnClick(R.id.et_restore_date)
    void openRestoreDatePicker() {

        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showToastMessage(ScheduleOutageFormActivity.this,
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
            setDuration();
        }
    }

    @OnClick(R.id.et_from)
    void openFromTimePicker() {
        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showToastMessage(ScheduleOutageFormActivity.this,
                    "Please Select Outage Date first");

        } else if (Utility.isValueNullOrEmpty(et_restore_date.getText().toString())) {
            Utility.showToastMessage(ScheduleOutageFormActivity.this,
                    "Please Select Restore Date first");
        } else {
            Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (!sameDate()) {
                                mFromHour = hourOfDay;
                                mFromMinute = minute;
                                et_from.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                        : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                        : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                        : hourOfDay + ":" + minute);
                                et_to.setText("");
                            } else if (hourOfDay > mHour || (hourOfDay == mHour && minute >= mMinute)) {
                                mFromHour = hourOfDay;
                                mFromMinute = minute;
                                et_from.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                        : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                        : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                        : hourOfDay + ":" + minute);
                                et_to.setText("");
                            } else {
                                Utility.showToastMessage(ScheduleOutageFormActivity.this, "Outage Time must be greater than or equal to Current Time.");
                            }
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            if (isRestore) {
                et_restore_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                et_to.setText("");
                isRestore = false;
            } else {
                et_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;
                et_restore_date.setText("");
            }
            setDuration();
        }

    };

    @OnClick(R.id.et_to)
    void openTimePicker() {
        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showToastMessage(ScheduleOutageFormActivity.this,
                    "Please Select Outage Date first");
        } else if (Utility.isValueNullOrEmpty(et_restore_date.getText().toString())) {
            Utility.showToastMessage(ScheduleOutageFormActivity.this,
                    "Please Select Restore Date first");
        } else if (Utility.isValueNullOrEmpty(et_from.getText().toString())) {
            Utility.showToastMessage(ScheduleOutageFormActivity.this,
                    "Please Select Outage Time first");
        } else {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            if (et_date.getText().toString().equalsIgnoreCase(et_restore_date.getText().toString()) && (hourOfDay < mFromHour || (hourOfDay == mFromHour && minute <= mFromMinute))) {
                                Utility.showToastMessage(ScheduleOutageFormActivity.this, "Restore Time must be greater than Outage Time.");
                            } else {
                                et_to.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                        : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                        : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                        : hourOfDay + ":" + minute);
                                setDuration();
                            }
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }

    private boolean sameDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String date = requiredFormat.format(c.getTimeInMillis());
        return et_date.getText().toString().equalsIgnoreCase(date);
    }

    /*GET DROP DOWN DATA*/
    private void getDropDownData(RequestParams requestParams, final boolean isFeeder, String url) {
        Utility.showLog("url", url);
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        client.post(this, url, requestParams, new AsyncHttpResponseHandler() {
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
                            ArrayList<LCRequestDropDownModel> lcRequestDropDownModels = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.optJSONObject(i);
                                LCRequestDropDownModel lcRequestDropDownModel = new Gson().fromJson(json.toString(),
                                        LCRequestDropDownModel.class);
                                lcRequestDropDownModels.add(lcRequestDropDownModel);
                            }
                            if (lcRequestDropDownModels.size() > 0) {
                                if (isFeeder) {
                                    setFeederSpinnerData(lcRequestDropDownModels);
                                } else {
                                    setSubStationSpinnerData(lcRequestDropDownModels);
                                    setFeederSpinnerData(null);

                                }
                            }
                        }
                    } else {
                        Utility.showCustomOKOnlyDialog(ScheduleOutageFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(ScheduleOutageFormActivity.this, error.getMessage());
            }
        });
    }

    private void setFeederSpinnerData(final ArrayList<LCRequestDropDownModel> lcRequestDropDownModels) {
        list = new ArrayList<>();
        list.add("--Select--");
        if (lcRequestDropDownModels != null) {
            for (int i = 0; i < lcRequestDropDownModels.size(); i++) {
                list.add(lcRequestDropDownModels.get(i).getFeederName()+"-"+  lcRequestDropDownModels.get(i).getFeederCode());
            }
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_feeder.setAdapter(newlineAdapter);
        spn_feeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                /*selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    //feederName = parent.getItemAtPosition(position).toString();
                    feederName =  "" + lcRequestDropDownModels.get(position - 1).getFeederName();
                    feederCode = "" + lcRequestDropDownModels.get(position - 1).getFeederCode();
                } else {
                    feederName = "";
                    feederCode = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private void setSubStationSpinnerData(final ArrayList<LCRequestDropDownModel> lcRequestDropDownModels) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        if (lcRequestDropDownModels != null) {
            for (int i = 0; i < lcRequestDropDownModels.size(); i++) {
                list.add(lcRequestDropDownModels.get(i).getSSNAME());
            }
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_ss_name.setAdapter(newlineAdapter);
        spn_ss_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                /*selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    ssName = parent.getItemAtPosition(position).toString();
                    ssCode = "" + lcRequestDropDownModels.get(position - 1).getSSCODE();
                    ssoMobileNo = "" + lcRequestDropDownModels.get(position - 1).getSSMOBILE();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("SC_CODE", ssCode);
                    getDropDownData(requestParams, true, Constants.FEEDER_DETAILS);

                } else {
                    ssName = "";
                    ssCode = "";
                    ssoMobileNo = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setReasonSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");/*
        list.add("Accident");
        list.add("Animal/Bird Dead On Line");
        list.add("Conductor Snapped");
        list.add("Cross Arm Damaged");/*
        list.add("Feeder Circuit Break Fault");
        list.add("Feeder CT Fault");
        list.add("Flood / Torentential Rain");
        list.add("Force Measure");
        list.add("Jumper cut");
        list.add("Pole Fallen");
        list.add("Grid Maintenance");
        list.add("Disc / Pin Insulator Flash over");
        list.add("Feeder Failure");*/

        list.add("Substation Maintenance");
        list.add("System Improvement Work");
        list.add("Maintenance");
        list.add("Failure of grid supply");
        list.add("Heavy Lighting / Rain");
        list.add("Transformer Failure");
        list.add("Tree Branches/ Tree Fallen On Line");
        list.add("Miscellaneous");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_reason.setAdapter(newlineAdapter);
        spn_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {

                TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                /*selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position == list.indexOf("Others")) {
                    et_lc_reason.setVisibility(View.VISIBLE);
                    reason = parent.getItemAtPosition(position).toString();
                } else if (position != 0) {
                    reason = parent.getItemAtPosition(position).toString();
                    et_lc_reason.setVisibility(View.GONE);
                } else {
                    et_lc_reason.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sectionId", sectionCode);
            jsonObject.put("sectionName", sec_code);
            jsonObject.put("substationId", ssCode);
            jsonObject.put("substationName", ssName);
            jsonObject.put("feederName", feederName);
            jsonObject.put("feederCode", feederCode);
            jsonObject.put("scheduledOutageTime", et_date.getText().toString() + " " + et_from.getText().toString() + ":00");
            jsonObject.put("scheduledRestoreTime", et_restore_date.getText().toString() + " " + et_to.getText().toString() + ":00");
            jsonObject.put("durationInMins", minutes);
            jsonObject.put("outageType", "Scheduled");
            jsonObject.put("outageReason", reason);
            jsonObject.put("ipAddress", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void setDuration() {
        if (!Utility.isValueNullOrEmpty(et_date.getText().toString()) &&
                !Utility.isValueNullOrEmpty(et_from.getText().toString()) &&
                !Utility.isValueNullOrEmpty(et_restore_date.getText().toString()) &&
                !Utility.isValueNullOrEmpty(et_to.getText().toString())) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

            try {
                Date date1 = format.parse(et_date.getText().toString() + " " + et_from.getText().toString());

                Date date2 = format.parse(et_restore_date.getText().toString() + " " + et_to.getText().toString());

                long difference = date2.getTime() - date1.getTime();

                minutes = (int) TimeUnit.MILLISECONDS.toMinutes(difference);
                et_duration.setText(minutes + " Minutes");
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

}
