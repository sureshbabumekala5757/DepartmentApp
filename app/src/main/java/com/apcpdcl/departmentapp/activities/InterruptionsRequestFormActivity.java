package com.apcpdcl.departmentapp.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class InterruptionsRequestFormActivity extends AppCompatActivity {

    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    @BindView(R.id.spn_ss_name)
    Spinner spn_ss_name;
    @BindView(R.id.spn_section)
    Spinner spn_section;
    @BindView(R.id.spn_feeder)
    Spinner spn_feeder;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_desig)
    EditText et_desig;
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
    @BindView(R.id.et_duration)
    EditText et_duration;
    @BindView(R.id.spn_reason)
    Spinner spn_reason;

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
    private int minutes = -1;
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private boolean isRestore = false;
    public ProgressDialog pDialog;
    private int mHour, mMinute;
    private int mFromHour, mFromMinute;
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interruptions_request_form_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        UserName = prefs.getString("UserName", "");
        designation = prefs.getString("USERTYPE", "");
        lmMobileNo = prefs.getString("MOBILE", "");
        ssName = prefs.getString("SSNAME", "");
        ssCode = prefs.getString("SSCODE", "");
        et_desig.setText(designation);
        // et_name.setText(prefs.getString("NAME", ""));
        et_name.setText(UserName);
        pDialog = new ProgressDialog(this);
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getSections();
            getReasonsData();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this, R.string.no_internet));
        }
    }

    private void getDeviceId() {
        /*TelephonyManager manager = (TelephonyManager) getSystemService(NewConnectionReleaseActivity.TELEPHONY_SERVICE);
        if (Utility.isMarshmallowOS()) {
            PackageManager pm = this.getPackageManager();
            int hasWritePerm = pm.checkPermission(
                    Manifest.permission.READ_PHONE_STATE,
                    this.getPackageName());

            if (hasWritePerm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        Constants.MY_PERMISSIONS_REQUEST_IMEI);
            } else {
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                Utility.showLog("IMEI", manager.getDeviceId());
            }
        } else {
            Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
            Utility.showLog("IMEI", manager.getDeviceId());
        }
*/
        String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER,IMEI_NUMBER);
        Utility.showLog("IMEI", IMEI_NUMBER);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_IMEI:
               /* if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager manager = (TelephonyManager) getSystemService(NewConnectionReleaseActivity.TELEPHONY_SERVICE);
                    Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                    Utility.showLog("IMEI", manager.getDeviceId());
                }*/
                String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER,IMEI_NUMBER);
                Utility.showLog("IMEI", IMEI_NUMBER);
                break;
        }
    }

    @OnClick(R.id.btn_confirm)
    void navigateToLiveInterruptions() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                pDialog.show();
                postInterruptionRequestForm(getJSON());
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }
    }

    private void postInterruptionRequestForm(JSONObject json) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(json.toString());
            Utility.showLog("jsonObject", json.toString());
            client.post(this, Constants.INTERRUPTIONS_FORM,
                    entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            Utility.showLog("onSuccess", response);
                            if (pDialog != null && pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                                    Utility.showToastMessage(InterruptionsRequestFormActivity.this, jsonObject.optString("RESPONSE"));
                                    finish();
                                } else {
                                    Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, jsonObject.optString("ERROR"));
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
                            Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, error.getMessage());
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
            Utility.showCustomOKOnlyDialog(this, "Please Select Interruption From Date.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_from.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Interruption From Time.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_restore_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Interruption To Date.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_to.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Interruption To Time.");
            return false;
        } else if (Utility.isValueNullOrEmpty(reason)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Interruption Reason.");
            return false;
        } else if (reason.equalsIgnoreCase("Others") &&
                Utility.isValueNullOrEmpty(et_lc_reason.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Interruption Reason.");
            return false;
        }
        return true;
    }

    @OnClick(R.id.et_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
        setDuration();
    }

    @OnClick(R.id.et_restore_date)
    void openRestoreDatePicker() {

        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showToastMessage(this,
                    "Please Select From Date first");
        } else {
            isRestore = true;
            final Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH), c.get(Calendar.DATE));
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            c.set(mYear, (mMonth - 1), mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
            setDuration();
        }
    }

    @OnClick(R.id.et_from)
    void openFromTimePicker() {
        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showToastMessage(this,
                    "Please Select From Date first");
        } else {
            Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            mFromHour = hourOfDay;
                            mFromMinute = minute;
                            et_from.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                    : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                    : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                    : hourOfDay + ":" + minute);
                            et_to.setText("");
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
                if (dayOfMonth < 10) {
                    et_restore_date.setText("0" + dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                } else {
                    et_restore_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                }
                et_to.setText("");
                isRestore = false;
            } else {
                if (dayOfMonth < 10) {
                    et_date.setText("0" + dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                } else {
                    et_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                }
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
            Utility.showToastMessage(InterruptionsRequestFormActivity.this,
                    "Please Select From Date first");
        } else if (Utility.isValueNullOrEmpty(et_from.getText().toString())) {
            Utility.showToastMessage(InterruptionsRequestFormActivity.this,
                    "Please Select From Time first");
        } else if (Utility.isValueNullOrEmpty(et_restore_date.getText().toString())) {
            Utility.showToastMessage(InterruptionsRequestFormActivity.this,
                    "Please Select To Date first");
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
                                Utility.showToastMessage(InterruptionsRequestFormActivity.this, "To Time must be greater than From Time.");
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

    /*GET DROP DOWN DATA*/
    private void getDropDownData(RequestParams requestParams, final boolean isFeeder, String url) {
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
                                    setFeederSpinnerData(null);

                                }
                            }
                        }
                    } else {
                        Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, error.getMessage());
            }
        });
    }

    /*GET REASONS DATA*/
    private void getReasonsData() {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        client.get(this, "http://59.145.127.110:8088/mobileapp_oms/rest/mobileomsservices/fdrInterruptionsReasonsList", new AsyncHttpResponseHandler() {
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
                        ArrayList<String> reasons = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.optJSONObject(i);
                            if (json.has("reasonName")) {
                                reasons.add(json.optString("reasonName"));
                            }

                        }
                        if (reasons.size() > 0) {
                            setReasonSpinnerData(reasons);
                        }

                    } else {
                        Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, error.getMessage());
            }
        });
    }

    /*GET SECTIONS DATA*/
    private void getSections() {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("SEC_CODE", sectionCode);
        client.post(this, Constants.DIV_SECTIONS, requestParams, new AsyncHttpResponseHandler() {
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
                                setSectionSpinnerData(lcRequestDropDownModels);
                                setFeederSpinnerData(null);
                                setSubStationSpinnerData();
                            }
                        }
                    } else {
                        Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(InterruptionsRequestFormActivity.this, error.getMessage());
            }
        });
    }

    private void setFeederSpinnerData(final ArrayList<LCRequestDropDownModel> lcRequestDropDownModels) {
        list = new ArrayList<>();
        list.add("--Select--");
        if (lcRequestDropDownModels != null) {
            for (int i = 0; i < lcRequestDropDownModels.size(); i++) {
                list.add(lcRequestDropDownModels.get(i).getFeederName());
            }
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_feeder.setAdapter(newlineAdapter);
        spn_feeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    feederName = parent.getItemAtPosition(position).toString();
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

    private void setSectionSpinnerData(final ArrayList<LCRequestDropDownModel> lcRequestDropDownModels) {
        list = new ArrayList<>();
        if (lcRequestDropDownModels != null) {
            for (int i = 0; i < lcRequestDropDownModels.size(); i++) {
                if (sectionCode.equalsIgnoreCase(lcRequestDropDownModels.get(i).getSECCD())) {
                    list.add(lcRequestDropDownModels.get(i).getSECNAME() + "(" + lcRequestDropDownModels.get(i).getSECCD() + ")");
                    sectionNAme = lcRequestDropDownModels.get(i).getSECNAME();
                    sec_code = lcRequestDropDownModels.get(i).getSECCD();
                }
            }
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_section.setAdapter(newlineAdapter);
    }

    private void setSubStationSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();

        list.add(ssName);

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_ss_name.setAdapter(newlineAdapter);
        spn_ss_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                RequestParams requestParams = new RequestParams();
                requestParams.put("SC_CODE", ssCode);
                getDropDownData(requestParams, true, Constants.FEEDER_DETAILS);


            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setReasonSpinnerData(final ArrayList<String> list) {

        list.add(0, "--Select--");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_reason.setAdapter(newlineAdapter);
        spn_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
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

    private void setDuration() {
        if (!Utility.isValueNullOrEmpty(et_date.getText().toString()) &&
                !Utility.isValueNullOrEmpty(et_from.getText().toString()) &&
                !Utility.isValueNullOrEmpty(et_to.getText().toString())) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

            try {
                Date date1 = format.parse(et_date.getText().toString() + " " + et_from.getText().toString());

                Date date2 = format.parse(et_restore_date.getText().toString() + " " + et_to.getText().toString());

                long difference = date2.getTime() - date1.getTime();

                minutes = (int) TimeUnit.MILLISECONDS.toMinutes(difference);
                int hours = minutes / 60; //since both are ints, you get an int
                int minute = minutes % 60;
                if (hours < 10 && minute < 10) {
                    et_duration.setText("0" + hours + ":" + "0" + minute);
                } else if (hours >= 10 && minute < 10) {
                    et_duration.setText(hours + ":" + "0" + minute);
                } else if (hours < 10 && minute >= 10) {
                    et_duration.setText("0" + hours + ":" + minute);
                } else {
                    et_duration.setText(hours + ":" + minute);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    private JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestedId", UserName);
            jsonObject.put("requestedName", et_name.getText().toString());
            jsonObject.put("requestedDesg", designation);
            jsonObject.put("sectionId", sectionCode);
            jsonObject.put("sectionName", sectionNAme);
            jsonObject.put("substationName", ssName);
            jsonObject.put("feederName", feederName);
            jsonObject.put("feederCode", feederCode);
            jsonObject.put("fromTime", et_date.getText().toString() + " " + et_from.getText().toString());
            jsonObject.put("toTime", et_restore_date.getText().toString() + " " + et_to.getText().toString());
            jsonObject.put("duration", et_duration.getText().toString());
            jsonObject.put("reasonName", reason);

            jsonObject.put("enteredIp", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
