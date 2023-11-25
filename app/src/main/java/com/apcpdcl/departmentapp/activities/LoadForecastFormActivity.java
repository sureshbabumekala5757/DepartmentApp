package com.apcpdcl.departmentapp.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
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
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class LoadForecastFormActivity extends AppCompatActivity {

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
    EditText et_date;/*
    @BindView(R.id.spn_time)
    Spinner spn_time;*/
    @BindView(R.id.et_load_entry)
    EditText et_load_entry;
    @BindView(R.id.et_consumption)
    EditText et_consumption;
    @BindView(R.id.et_prev_consumption)
    EditText et_prev_consumption;
    @BindView(R.id.et_nature)
    EditText et_nature;
    @BindView(R.id.ll_load)
    LinearLayout ll_load;
    @BindView(R.id.rl_load_entry)
    RelativeLayout rl_load_entry;
    @BindView(R.id.iv_load_entry)
    ImageView iv_load_entry;
    @BindView(R.id.el_load)
    ExpandableRelativeLayout el_load;

    private String ssName = "";
    private String ssCode = "";
    private String feederCode = "";
    private String feederName = "";
    private String sectionNAme = "";

    private String sectionCode = "";
    private String UserName = "";
    private String designation = "";/*
    private String ssoMobileNo = "";
    private String lmMobileNo = "";
    private int minutes = -1;
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private boolean isRestore = false;
    private String sec_code = "";
    private String reason = "";
    private int mFromHour, mFromMinute;*/

    public ProgressDialog pDialog;
    private ArrayList<String> list;
    private String mNature = "";
    private String sec_code = "";
    private String time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_forecast_form_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        UserName = prefs.getString("UserName", "");
        designation = prefs.getString("USERTYPE", "");
        //lmMobileNo = prefs.getString("MOBILE", "");
/*        ssName = prefs.getString("SSNAME", "");
        ssCode = prefs.getString("SSCODE", "");*/
        et_desig.setText(designation);
        // et_name.setText(prefs.getString("NAME", ""));
        et_name.setText(UserName);
        pDialog = new ProgressDialog(this);
        final Calendar c = Calendar.getInstance();
        long now = System.currentTimeMillis() - 1000;
        c.setTimeInMillis(now + (1000 * 60 * 60 * 24));
        et_date.setText(c.get(Calendar.DATE) + "-" + Utility.getMonthFormat(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR));

        loadEntryPopup();
        Utility.setExpandableButtonAnimators(el_load, iv_load_entry);
        rl_load_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                el_load.toggle();
            }
        });
        //loadEntryPopup();
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getSections();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this, R.string.no_internet));
        }
    }

    private void getDeviceId() {
       /* TelephonyManager manager = (TelephonyManager) getSystemService(NewConnectionReleaseActivity.TELEPHONY_SERVICE);
        if (Utility.isMarshmallowOS()) {
            PackageManager pm = this.getPackageManager();
            int hasWritePerm = pm.checkPermission(
                    Manifest.permission.READ_PHONE_STATE,
                    this.getPackageName());

            if (hasWritePerm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
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
                postLoadForecastAPI(getJSON());
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }
    }

    private void postLoadForecastAPI(JSONObject json) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(json.toString());
            Utility.showLog("jsonObject", json.toString());
            //client.post(this, "http://59.145.127.110:8088/mobileapp_oms/rest/mobileomsservices/loadforecastEntryForm",
            client.post(this, "http://59.145.127.110:8088/mobileapp_oms/rest/mobileomsservices/loadforecastEntryForm",
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
                                    Utility.showToastMessage(LoadForecastFormActivity.this, jsonObject.optString("RESPONSE"));
                                    finish();
                                } else {
                                    Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, jsonObject.optString("ERROR"));
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
                            Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, error.getMessage());
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
            Utility.showCustomOKOnlyDialog(this, "Please Select Date.");
            return false;
    /*    } else if (Utility.isValueNullOrEmpty(time)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Time.");
            return false;*/
        } else if (Utility.isValueNullOrEmpty(mNature)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Nature of the Load.");
            return false;
    /*    } else if (Utility.isValueNullOrEmpty(et_consumption.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Consumption.");
            return false;
        } else if (!Utility.isValueNullOrEmpty(et_consumption.getText().toString()) &&
                Integer.parseInt(et_consumption.getText().toString()) > 220) {
            Utility.showCustomOKOnlyDialog(this, "Consumption should be in between 0 to 220.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_prev_consumption.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Previous Day Consumption.");
            return false;
        } else if (!Utility.isValueNullOrEmpty(et_prev_consumption.getText().toString()) &&
                Integer.parseInt(et_prev_consumption.getText().toString()) > 220) {
            Utility.showCustomOKOnlyDialog(this, "Previous Day Consumption should be in between 0 to 220.");
            return false;*/
        }
        return true;
    }

    @OnClick(R.id.et_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
  /*      DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DATE));
        long now = System.currentTimeMillis() - 1000;
        datePickerDialog.getDatePicker().setMaxDate(now);
        datePickerDialog.getDatePicker().setMinDate(now-(1000 * 60 * 60 * 24));
        datePickerDialog.show();*/
    }

    @OnClick(R.id.et_load_entry)
    void openLoadentryPopup() {
        // loadEntryPopup();
    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            et_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
       /*     mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;*/
        }

    };

    /*   @OnClick(R.id.et_to)
       void openTimePicker() {
           if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
               Utility.showToastMessage(LoadForecastFormActivity.this,
                       "Please Select Date first");
           } else {
               final Calendar c = Calendar.getInstance();
               mHour = c.get(Calendar.HOUR_OF_DAY);
               mMinute = c.get(Calendar.MINUTE);
               CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(this,
                       new TimePickerDialog.OnTimeSetListener() {

                           @Override
                           public void onTimeSet(TimePicker view, int hourOfDay,
                                                 int minute) {
   *//*
                            et_to.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                    : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                    : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                    : hourOfDay + ":" + minute);*//*


                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
*/
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
                                    setSubStationSpinnerData(lcRequestDropDownModels);
                                    setFeederSpinnerData(null);

                                }
                            }
                        }
                    } else {
                        Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, error.getMessage());
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
                                setSubStationSpinnerData(null);
                            }
                        }
                    } else {
                        Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, error.getMessage());
            }
        });
    }

    /*GET SECTIONS DATA*/
    private void getNatureOfLoad() {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("feederId", feederCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpEntity entity;
        try {
            entity = new StringEntity(requestParams.toString());
            client.post(this, Constants.NATURE_OF_LOAD, entity, "application/json", new AsyncHttpResponseHandler() {
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
                                JSONObject json = jsonArray.optJSONObject(0);
                                mNature = json.optString("natureofLoad");
                                et_nature.setText(mNature);
                            }
                        } else {
                            Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, jsonObject.optString("ERROR"));
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
                    Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                    if (Utility.isNetworkAvailable(LoadForecastFormActivity.this)) {
                        getNatureOfLoad();
                    }
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
        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                RequestParams requestParams = new RequestParams();
                requestParams.put("SEC_CODE", sectionCode);
                getDropDownData(requestParams, false, Constants.SUB_STATION_DETAILS);


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
               /*  TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
               selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    ssName = parent.getItemAtPosition(position).toString();
                    ssCode = "" + lcRequestDropDownModels.get(position - 1).getSSCODE();
                    //  ssoMobileNo = "" + lcRequestDropDownModels.get(position - 1).getSSMOBILE();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("SC_CODE", ssCode);
                    getDropDownData(requestParams, true, Constants.FEEDER_DETAILS);

                } else {
                    ssName = "";
                    ssCode = "";
                    // ssoMobileNo = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private void loadEntryPopup() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("00:00");
        list.add("00:30");
        list.add("01:00");
        list.add("01:30");
        list.add("02:00");
        list.add("02:30");
        list.add("03:00");
        list.add("03:30");
        list.add("04:00");
        list.add("04:30");
        list.add("05:00");
        list.add("05:30");
        list.add("06:00");
        list.add("06:30");
        list.add("07:00");
        list.add("07:30");
        list.add("08:00");
        list.add("08:30");
        list.add("09:00");
        list.add("09:30");
        list.add("10:00");
        list.add("10:30");
        list.add("11:00");
        list.add("11:30");
        list.add("12:00");
        list.add("12:30");
        list.add("13:00");
        list.add("13:30");
        list.add("14:00");
        list.add("14:30");
        list.add("15:00");
        list.add("15:30");
        list.add("16:00");
        list.add("16:30");
        list.add("17:00");
        list.add("17:30");
        list.add("18:00");
        list.add("18:30");
        list.add("19:00");
        list.add("19:30");
        list.add("20:00");
        list.add("20:30");
        list.add("21:00");
        list.add("21:30");
        list.add("22:00");
        list.add("22:30");
        list.add("23:00");
        list.add("23:30");

        LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < list.size(); i++) {
            View myView = linflater.inflate(R.layout.load_entry_item, null);
            TextView tv_load = (TextView) myView.findViewById(R.id.tv_load);
            final EditText et_load = (EditText) myView.findViewById(R.id.et_load);
            et_load.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!Utility.isValueNullOrEmpty(et_load.getText().toString()) && Integer.parseInt(et_load.getText().toString()) > 220) {
                        et_load.setText("");
                        Utility.showCustomOKOnlyDialog(LoadForecastFormActivity.this, "Consumption should be less than 220 kwh");
                    }

                }
            });
            tv_load.setText(list.get(i));
            myView.setId(i);
            ll_load.addView(myView);
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
            jsonObject.put("feederId", feederCode);
            jsonObject.put("loadForeCastDate", et_date.getText().toString() + " " + time);
            jsonObject.put("consumption", et_consumption.getText().toString());
            jsonObject.put("prevConsumption", et_prev_consumption.getText().toString());
            jsonObject.put("natureofLoad", mNature);

            jsonObject.put("enteredIp", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
