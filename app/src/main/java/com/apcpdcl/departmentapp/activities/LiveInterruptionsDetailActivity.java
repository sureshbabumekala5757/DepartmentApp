package com.apcpdcl.departmentapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.models.InterruptionModel;
import com.apcpdcl.departmentapp.models.ReasonsModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class LiveInterruptionsDetailActivity extends AppCompatActivity {


    @BindView(R.id.tv_sub_station)
    TextView tv_sub_station;
    @BindView(R.id.tv_feeder_name)
    TextView tv_feeder_name;
    @BindView(R.id.tv_meter_no)
    TextView tv_meter_no;
    @BindView(R.id.tv_time_of_acc)
    TextView tv_time_of_acc;
    @BindView(R.id.spn_relay)
    Spinner spn_relay;
    @BindView(R.id.spn_inter_rea)
    Spinner spn_inter_rea;
    @BindView(R.id.spn_fault)
    Spinner spn_fault;
    @BindView(R.id.spn_sub_fault)
    Spinner spn_sub_fault;
    @BindView(R.id.et_time)
    EditText et_time;
    @BindView(R.id.et_remarks)
    EditText et_remarks;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    public ProgressDialog pDialog;
    private String sectionCode;
    private InterruptionModel interruptionModel;
    private String mRelay = "";
    private String l1_ID = "";
    private String mInterruption = "";
    private String l2_ID = "";
    private String mFault = "";
    private String l3_ID = "";
    private String mSubFault = "";
    private String l4_ID = "";
    private String mCurrentDate = "";
    private String UserName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_interruption_detail_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        UserName = prefs.getString("UserName", "");
        pDialog = new ProgressDialog(this);
        if (getIntent().hasExtra(InterruptionModel.class.getSimpleName())) {
            interruptionModel = (InterruptionModel) getIntent().getSerializableExtra(InterruptionModel.class.getSimpleName());
        }
        setData();
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getRelayStatusData("0");

        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    private void setData() {
        tv_sub_station.setText(interruptionModel.getSubstationName());
        tv_feeder_name.setText(interruptionModel.getFeederName());
        tv_meter_no.setText(interruptionModel.getMeterNo());
        tv_time_of_acc.setText(interruptionModel.getEventOccur());
        nowDate();
    }

    private void nowDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat requiredFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String date = requiredFormat.format(calendar.getTimeInMillis());
        et_time.setText(date);
    }

    @OnClick(R.id.et_time)
    void openPopup() {
        dateTimePopup();
    }

    @OnClick(R.id.btn_submit)
    void saveDetails() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                pDialog.show();
                postDetailsAPI(getJSON());
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }
    }

    private JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("relayStatusId", l1_ID);
            jsonObject.put("relayStatusName", mRelay);
            jsonObject.put("interruptionReasonId", l2_ID);
            jsonObject.put("interruptionReasonName", mInterruption);
            jsonObject.put("falutCategoryId", l3_ID);
            jsonObject.put("falutCategoryName", mFault);
            jsonObject.put("falutSubCategoryId", l4_ID);
            jsonObject.put("falutSubCategoryName", mSubFault);
            jsonObject.put("restorationTime", et_time.getText().toString());
            jsonObject.put("remarks", et_remarks.getText().toString());
            jsonObject.put("userId", UserName);
            jsonObject.put("transId", interruptionModel.getTransId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /*GET RELAY STATUS DATA*/
    private void getRelayStatusData(final String flag) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        JSONObject requestParams = new JSONObject();
        HttpEntity entity;
        try {
            requestParams.put("flag", flag);
            try {
                entity = new StringEntity(requestParams.toString());
                client.post(this, Constants.OUT_AGE_REASONS, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utility.showLog("onSuccess", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                                if (jsonObject.has("RESPONSE")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
                                    ArrayList<ReasonsModel> reasonsModels = new ArrayList<>();
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject json = jsonArray.optJSONObject(i);
                                        ReasonsModel reasonsModel = new Gson().fromJson(json.toString(),
                                                ReasonsModel.class);
                                        reasonsModels.add(reasonsModel);
                                    }
                                    if (reasonsModels.size() > 0) {
                                        if (flag.equalsIgnoreCase("0")) {
                                            setRelayStatusSpinnerData(reasonsModels);
                                            getRelayStatusData("1");
                                        } else {
                                            setInterruptionReasonsSpinnerData(reasonsModels);
                                            if (pDialog != null && pDialog.isShowing()) {
                                                pDialog.dismiss();
                                            }
                                        }
                                    }
                                }
                            } else {
                                Utility.showCustomOKOnlyDialog(LiveInterruptionsDetailActivity.this, jsonObject.optString("ERROR"));
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
                        Utility.showCustomOKOnlyDialog(LiveInterruptionsDetailActivity.this, error.getMessage());
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*GET RELAY STATUS DATA*/
    private void postDetailsAPI(JSONObject jsonObject) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            Utility.showLog("jsonObject", jsonObject.toString());
            client.post(this, Constants.SECTION_WISE_LIVEINTR_UPDATEREPORT, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {
                            LiveInterruptionsListActivity.getInstance().updateList(interruptionModel.getTransId());
                            Utility.showCustomOKOnlyDialog(LiveInterruptionsDetailActivity.this, "Restoration Date Updated Successfully.");
                            finish();
                        } else {
                            Utility.showCustomOKOnlyDialog(LiveInterruptionsDetailActivity.this, jsonObject.optString("ERROR"));
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
                    Utility.showCustomOKOnlyDialog(LiveInterruptionsDetailActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setRelayStatusSpinnerData(final ArrayList<ReasonsModel> reasonsModels) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < reasonsModels.size(); i++) {
            list.add(reasonsModels.get(i).getRelayInduction());
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_relay.setAdapter(newlineAdapter);
        spn_relay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
               /* TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    mRelay = parent.getItemAtPosition(position).toString();
                    l1_ID = "" + reasonsModels.get(position - 1).getL1_ID();
                } else {
                    mRelay = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setInterruptionReasonsSpinnerData(final ArrayList<ReasonsModel> reasonsModels) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < reasonsModels.size(); i++) {
            list.add(reasonsModels.get(i).getInterruptionType());
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_inter_rea.setAdapter(newlineAdapter);
        spn_inter_rea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    mInterruption = parent.getItemAtPosition(position).toString();
                    l2_ID = "" + reasonsModels.get(position - 1).getL2_ID();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("flag", "2");
                        jsonObject.put("l2_ID", l2_ID);
                        getFaultCategoryData(jsonObject, true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mInterruption = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setCategoryFaultData(final ArrayList<ReasonsModel> reasonsModels) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < reasonsModels.size(); i++) {
            list.add(reasonsModels.get(i).getFaultCategory());
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_fault.setAdapter(newlineAdapter);
        spn_fault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    mFault = parent.getItemAtPosition(position).toString();
                    l3_ID = "" + reasonsModels.get(position - 1).getL3_ID();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("flag", "3");
                        jsonObject.put("l2_ID", l2_ID);
                        jsonObject.put("l3_ID", l3_ID);
                        getFaultCategoryData(jsonObject, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mFault = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void setSubCategoryFaultData(final ArrayList<ReasonsModel> reasonsModels) {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < reasonsModels.size(); i++) {
            list.add(reasonsModels.get(i).getFaultSubCategory());
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_sub_fault.setAdapter(newlineAdapter);
        spn_sub_fault.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                /*TextView selectedText = (TextView) parent.findViewById(R.id.tvSpinner);
                selectedText.setTypeface(selectedText.getTypeface(), Typeface.BOLD);*/
                if (position != 0) {
                    mSubFault = parent.getItemAtPosition(position).toString();
                    l4_ID = "" + reasonsModels.get(position - 1).getL4_ID();
                } else {
                    mSubFault = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /*GET RELAY STATUS DATA*/
    private void getFaultCategoryData(JSONObject requestParams, final boolean isCategory) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);

        HttpEntity entity;

        try {
            entity = new StringEntity(requestParams.toString());
            client.post(this, Constants.OUT_AGE_REASONS, entity, "application/json", new AsyncHttpResponseHandler() {
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
                                ArrayList<ReasonsModel> reasonsModels = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject json = jsonArray.optJSONObject(i);
                                    ReasonsModel reasonsModel = new Gson().fromJson(json.toString(),
                                            ReasonsModel.class);
                                    reasonsModels.add(reasonsModel);
                                }
                                if (reasonsModels.size() > 0) {
                                    if (isCategory) {
                                        setCategoryFaultData(reasonsModels);
                                    } else {
                                        setSubCategoryFaultData(reasonsModels);

                                    }
                                }
                            }
                        } else {
                            Utility.showCustomOKOnlyDialog(LiveInterruptionsDetailActivity.this, jsonObject.optString("ERROR"));
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
                    Utility.showCustomOKOnlyDialog(LiveInterruptionsDetailActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void dateTimePopup() {
        final SimpleDateFormat showDf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final Dialog dialog = new Dialog(LiveInterruptionsDetailActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_time_layout);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button btn_done = (Button) dialog.findViewById(R.id.btn_done);
        Button btn_now = (Button) dialog.findViewById(R.id.btn_now);
        final SeekBar sb_hour = (SeekBar) dialog.findViewById(R.id.sb_hour);
        final SeekBar sb_minutes = (SeekBar) dialog.findViewById(R.id.sb_minutes);
        final MaterialCalendarView widget = (MaterialCalendarView) dialog.findViewById(R.id.calendarView);
        final TextView tv_time = (TextView) dialog.findViewById(R.id.tv_time);
        widget.setTopbarVisible(true);
        Calendar calendar = Calendar.getInstance();
        widget.setSelectedDate(calendar);
        widget.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(calendar)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        sb_hour.setProgress(calendar.get(Calendar.HOUR_OF_DAY));
        sb_minutes.setProgress(calendar.get(Calendar.MINUTE));
        setTime(sb_hour, sb_minutes, tv_time);
        CalendarDay date = widget.getSelectedDate();
        mCurrentDate = showDf.format(date.getDate());
        widget.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

            }
        });
        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay cal = widget.getSelectedDate();
                mCurrentDate = showDf.format(cal.getDate());
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_time.setText(mCurrentDate + " " + tv_time.getText().toString());
                dialog.dismiss();
            }
        });
        btn_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                widget.setSelectedDate(calendar);
                widget.state().edit()
                        .setFirstDayOfWeek(Calendar.MONDAY)
                        .setMinimumDate(calendar)
                        .setCalendarDisplayMode(CalendarMode.MONTHS)
                        .commit();
                CalendarDay date = widget.getSelectedDate();
                mCurrentDate = showDf.format(date.getDate());
                sb_hour.setProgress(calendar.get(Calendar.HOUR_OF_DAY));
                sb_minutes.setProgress(calendar.get(Calendar.MINUTE));
                setTime(sb_hour, sb_minutes, tv_time);
            }
        });
        sb_hour.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTime(sb_hour, sb_minutes, tv_time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_minutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTime(sb_hour, sb_minutes, tv_time);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.show();
    }

    private void setTime(SeekBar sb_hour, SeekBar sb_minutes, TextView tv_time) {
        String time = "";
        if (sb_hour.getProgress() < 9 && sb_minutes.getProgress() < 9) {
            time = "0" + sb_hour.getProgress() + ":0" + sb_minutes.getProgress();
        } else if (sb_hour.getProgress() < 9) {
            time = "0" + sb_hour.getProgress() + ":" + sb_minutes.getProgress();
        } else if (sb_minutes.getProgress() < 9) {
            time = sb_hour.getProgress() + ":0" + sb_minutes.getProgress();
        } else {
            time = sb_hour.getProgress() + ":" + sb_minutes.getProgress();
        }
        tv_time.setText(time);
    }

    private boolean validateFields() {
        if (mRelay.equalsIgnoreCase("")) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Relay Status");
            return false;
        } else if (mInterruption.equalsIgnoreCase("")) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Interruption Reasons");
            return false;
        } else if (mFault.equalsIgnoreCase("")) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Fault Category");
            return false;
        } else if (mSubFault.equalsIgnoreCase("")) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Fault Sub-Category");
            return false;
        } else if (!validateTime()) {
            Utility.showCustomOKOnlyDialog(this, "Estimated Restoration Time must be greater than Current Time");
            return false;
        }
        return true;
    }

    private boolean validateTime() {
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(et_time.getText().toString());
            Date now = Calendar.getInstance().getTime();// Parse into Date object
            return date.getTime() > now.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


}
