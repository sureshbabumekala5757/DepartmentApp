package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.customviews.LCDatePickerFragment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class LCRequestFormActivity extends AppCompatActivity {

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
    @BindView(R.id.et_from)
    EditText et_from;
    @BindView(R.id.et_to)
    EditText et_to;
    @BindView(R.id.et_lc_reason)
    EditText et_lc_reason;
    @BindView(R.id.spn_reason)
    Spinner spn_reason;
    @BindView(R.id.cb_ab_opened)
    CheckBox cb_ab_opened;
    @BindView(R.id.cb_er_provided)
    CheckBox cb_er_provided;
    @BindView(R.id.tv_footer)
    TextView tv_footer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lc_request_form_activity);
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
        et_desig.setText(designation);
        et_name.setText(prefs.getString("NAME", ""));
        pDialog = new ProgressDialog(this);
        setReasonSpinnerData();
        et_date.setText(Utility.nowDate());
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


    @OnClick(R.id.btn_confirm)
    void navigateToLiveInterruptions() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                pDialog.show();
                postLCRequestForm(getJSON());
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }
    }

    private void postLCRequestForm(JSONObject json) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(json.toString());
            Utility.showLog("jsonObject", json.toString());
            client.post(this, Constants.LC_LM_REQUEST_FORM_INSERT, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                            Utility.showToastMessage(LCRequestFormActivity.this, jsonObject.optString("RESPONSE"));
                            finish();
                        } else {
                            Utility.showCustomOKOnlyDialog(LCRequestFormActivity.this, jsonObject.optString("ERROR"));
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
                    Utility.showCustomOKOnlyDialog(LCRequestFormActivity.this, error.getMessage());
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
            Utility.showCustomOKOnlyDialog(this, "Please Select LC Required Date.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_from.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select LC From Time.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_to.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select LC To Time.");
            return false;
        } else if (Utility.isValueNullOrEmpty(reason)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select LC Reason.");
            return false;
        } else if (reason.equalsIgnoreCase("Others") &&
                Utility.isValueNullOrEmpty(et_lc_reason.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter LC Reason.");
            return false;
        } else if (!cb_ab_opened.isChecked()) {
            Utility.showCustomOKOnlyDialog(this, "Please Confirm there is no back feeding.");
            return false;
    /*    } else if (!cb_er_provided.isChecked()) {
            Utility.showCustomOKOnlyDialog(this, "Please Confirm Earth Rod Provided.");
            return false;*/
        }
        return true;
    }

    @OnClick(R.id.et_date)
    void openDatePicker() {
        LCDatePickerFragment date = new LCDatePickerFragment(et_date, et_from, et_to, false);
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt(Constants.YEAR, calender.get(Calendar.YEAR));
        args.putInt(Constants.MONTH, calender.get(Calendar.MONTH));
        args.putInt(Constants.DAY, calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(LCDatePickerFragment.ondate);
        date.show(getSupportFragmentManager(), Constants.DATE_PICKER);
    }

    @OnClick(R.id.et_from)
    void openFromTimePicker() {
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
                            Utility.showToastMessage(LCRequestFormActivity.this, "From Time must be greater than or equal to Current Time.");
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    @OnClick(R.id.et_to)
    void openTimePicker() {
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (hourOfDay < mFromHour || (hourOfDay == mFromHour && minute <= mFromMinute)) {
                            Utility.showToastMessage(LCRequestFormActivity.this, "LC To Time must be greater than LC From Time.");
                        } else {
                            et_to.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                    : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                    : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                    : hourOfDay + ":" + minute);
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private boolean sameDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String date = requiredFormat.format(c.getTimeInMillis());
        return et_date.getText().toString().equalsIgnoreCase(date);
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
                                    setSubStationSpinnerData(lcRequestDropDownModels);
                                    setFeederSpinnerData(null);

                                }
                            }
                        }
                    } else {
                        Utility.showCustomOKOnlyDialog(LCRequestFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(LCRequestFormActivity.this, error.getMessage());
            }
        });
    }/*GET DROP DOWN DATA*/

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
                        Utility.showCustomOKOnlyDialog(LCRequestFormActivity.this, jsonObject.optString("ERROR"));
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
                Utility.showCustomOKOnlyDialog(LCRequestFormActivity.this, error.getMessage());
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
        list.add("--Select--");
        int same_section = 0;
        if (lcRequestDropDownModels != null) {
            for (int i = 0; i < lcRequestDropDownModels.size(); i++) {
                list.add(lcRequestDropDownModels.get(i).getSECNAME() + "(" + lcRequestDropDownModels.get(i).getSECCD() + ")");
                if (sectionCode.equalsIgnoreCase(lcRequestDropDownModels.get(i).getSECCD())) {
                    same_section = i;
                }
            }
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_section.setAdapter(newlineAdapter);
        spn_section.setSelection(same_section + 1);
        spn_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    sectionNAme = lcRequestDropDownModels.get(position - 1).getSECNAME();
                    sec_code = "" + lcRequestDropDownModels.get(position - 1).getSECCD();
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("SEC_CODE", sec_code);
                    getDropDownData(requestParams, false, Constants.SUB_STATION_DETAILS);
                } else {
                    sectionNAme = "";
                    sec_code = "";
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
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
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
        list.add("--Select--");
        list.add("Tree Cutting");
        list.add("Line Snap");
        list.add("Pin Insulator Failure");
        list.add("Disc Failure");
        list.add("Others");

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


    private JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sectionId", sectionCode);
            jsonObject.put("requestedSectionId", sec_code);
            jsonObject.put("substationName", ssName);
            jsonObject.put("feederName", feederName);
            jsonObject.put("feederCode", feederCode);
            jsonObject.put("lcRequestedId", UserName);
            jsonObject.put("lcRequestedName", et_name.getText().toString());
            jsonObject.put("lcRequestedDesg", designation);
            jsonObject.put("lcRequestedDate", et_date.getText().toString());
            jsonObject.put("lcFromTime", et_from.getText().toString());
            jsonObject.put("lcToTime", et_to.getText().toString());
            jsonObject.put("lmMobileNo", lmMobileNo);
            jsonObject.put("ssoMobileNo", ssoMobileNo);
            jsonObject.put("noBackFeeding", "Y");
            /*jsonObject.put("earthrodProvided", "Y");*/


            if (reason.equalsIgnoreCase("Others")) {
                jsonObject.put("reasonForLc", et_lc_reason.getText().toString());
            } else {
                jsonObject.put("reasonForLc", reason);
            }
            jsonObject.put("enteredIp", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
