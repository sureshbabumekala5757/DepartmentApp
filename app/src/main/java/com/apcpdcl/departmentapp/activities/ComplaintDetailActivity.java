package com.apcpdcl.departmentapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.models.ComplaintModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 21-04-2018.
 */
public class ComplaintDetailActivity extends AppCompatActivity {
    @BindView(R.id.et_remarks)
    EditText et_remarks;
    @BindView(R.id.tv_complaint_id)
    TextView tv_complaint_id;
    @BindView(R.id.tv_service_no)
    TextView tv_service_no;
    @BindView(R.id.tv_consumer_name)
    TextView tv_consumer_name;
    @BindView(R.id.tv_mobile_no)
    TextView tv_mobile_no;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_category)
    TextView tv_category;
    @BindView(R.id.tv_complaint_type)
    TextView tv_complaint_type;
    @BindView(R.id.tv_sub_complaint_type)
    TextView tv_sub_complaint_type;
    @BindView(R.id.tv_add_info)
    TextView tv_add_info;
    @BindView(R.id.tv_reg_date)
    TextView tv_reg_date;
    @BindView(R.id.tv_completion_date)
    TextView tv_completion_date;
    @BindView(R.id.tv_resolution)
    TextView tv_resolution;
    @BindView(R.id.rg_status)
    RadioGroup rg_status;
    @BindView(R.id.btn_submit)
    TextView btn_submit;
    @BindView(R.id.sv_main)
    ScrollView sv_main;

    @BindView(R.id.complaint_date)
    EditText complaint_date;
    @BindView(R.id.complaint_time)
    EditText complaint_time;

    @BindView(R.id.spn_supply_failure)
    Spinner spn_supply_failure;
    public ProgressDialog pDialog;
    public Toolbar mToolbar;
    private String status = "";
    private ComplaintModel complaintModel;
    private ArrayList<String> supplyFailureArrayList;
    private String supplyFailureStr;
    private String svRegisteredDate;

    private int minutes = -1;
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private int mHour, mMinute;
    private int mFromHour, mFromMinute;
    private boolean isRestore = false;
    private Date dateVal = null;
    private String complaintId, userID, cType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        init();
        //Add String supplyFailureStrArray to supplyFailureArrayList
        supplyFailureArrayList = new ArrayList<>();
        String[] supplyFailureStrArray = getResources().getStringArray(R.array.supply_failure);
        supplyFailureArrayList = new ArrayList<>(Arrays.asList(supplyFailureStrArray));

        bindSupplyFailuredata(supplyFailureArrayList);
    }

    /*Initialize Views*/
    private void init() {

        userID = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        pDialog = new ProgressDialog(this);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.COMPLAINT_DATA)) {
//            complaintModel = (ComplaintModel) intent.getSerializableExtra(Constants.COMPLAINT_DATA);
            complaintId = intent.getStringExtra(Constants.COMPLAINT_DATA);
            cType = intent.getStringExtra("DTRC");
        }


        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_resolved) {
                    status = "YES";
                } else {
                    status = "NO";
                }
                // status = (String) ((RadioButton) findViewById(checkedId)).getText();
                Utility.showLog("status", status);
            }

        });
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            JSONObject requestObj = new JSONObject();
            try{
                requestObj.put("ComplaintID",complaintId);
                getComplaintDetails(requestObj);
            }catch (Exception e){
                e.printStackTrace();
            }
           // getComplaintDetails();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    /***************************************************************************************************** Start*/
    @OnClick(R.id.complaint_time)
    void openFromTimePicker() {
        if (Utility.isValueNullOrEmpty(complaint_date.getText().toString())) {
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
                            complaint_time.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                    : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                    : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                    : hourOfDay + ":" + minute);
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

                isRestore = false;
            } else {
                if (dayOfMonth < 10) {
                    complaint_date.setText("0" + dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                } else {
                    complaint_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                }
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;

            }
            //setDuration();
        }

    };

    @OnClick(R.id.complaint_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(dateVal.getTime());
        datePickerDialog.show();
        //setDuration();
    }


    private void setDuration() {
        if (!Utility.isValueNullOrEmpty(complaint_date.getText().toString()) &&
                !Utility.isValueNullOrEmpty(complaint_time.getText().toString())) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");

            try {

                Date date1 = format.parse(complaint_date.getText().toString() + " " + complaint_time.getText().toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    /**************************************************************************************************** End*/
    private void bindSupplyFailuredata(ArrayList supplyFailureArrayList) {


        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, supplyFailureArrayList);
        spn_supply_failure.setAdapter(adapter);
        spn_supply_failure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    supplyFailureStr = "";
                    et_remarks.setVisibility(View.GONE);
                } else {
                    supplyFailureStr = (String) supplyFailureArrayList.get(position);
                    if (supplyFailureStr.equalsIgnoreCase("Supply Failure - Other")) {
                        et_remarks.setVisibility(View.VISIBLE);
                    } else {
                        et_remarks.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getComplaintDetails(JSONObject requestObjVal) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("COMPLAINTID", complaintModel.getComplaint_Id());
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjVal.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/ConsumerApp/SAPISU/CustomerCareComplaintStatus/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                Utility.showLog("onSuccess", responseStr);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {

                    JSONObject responseObj = new JSONObject(responseStr);
                    responseObj = responseObj.getJSONObject("response");
                    String status = responseObj.getString("success");
                    if(status.equalsIgnoreCase("True")){
                        //JSONObject dataObj = responseObj.getJSONObject("data");
                        if(responseObj.length()>0 && responseObj != null){
                            complaintModel = new ComplaintModel(
                                    "",
                                    "",
                                    responseObj.getString("remark"),
                                    responseObj.getString("complaintsubtype"),
                                    responseObj.getString("complainttype"),
                                    responseObj.getString("phonenumber"),
                                    responseObj.getString("ratecategory"),
                                    responseObj.getString("Feeder"),
                                    responseObj.getString("Dtr"),
                                    responseObj.getString("Pole"),
                                    responseObj.getString("consumerdetails"),
                                    responseObj.getString("consumeraddress"),
                                    responseObj.getString("consumername"),
                                    responseObj.getString("bpid"),
                                    responseObj.getString("complaintid"),
                                    responseObj.getString("registereddate"),
                                    responseObj.getString("toberectifiedtime"),
                                    ""
                            );
                            if (responseObj.has("phoneNumber")) {
                                complaintModel.setConsumer_Phone_No(responseObj.optString("phonenumber"));
                            }
                            if (responseObj.has("registereddate")) {
//                                complaintModel.setCreated_Date_Time(dataObj.optString("zcompRegDt"));
                                svRegisteredDate = responseObj.optString("registereddate");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    dateVal = format.parse(svRegisteredDate);
                                    System.out.println(dateVal);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (responseObj.has("toberectifiedtime")) {
                                complaintModel.setTo_be_completion_Date_Time(responseObj.optString("toberectifiedtime"));
                            }
                            if (responseObj.has("complaintstatus")) {
                                complaintModel.setResolution_Flag(responseObj.optString("complaintstatus"));
                            }
                        }
                        prePopulateData();
                    }
//                    if (jsonObject.has("T_CODE_RESPONSE")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("T_CODE_RESPONSE");
//                        if (jsonArray.length() > 0) {
//                            JSONObject json = jsonArray.getJSONObject(0);
//                            if (json.has("phoneNumber")) {
//                                complaintModel.setConsumer_Phone_No(json.optString("phoneNumber"));
//                            }
//                            if (json.has("registeredDate")) {
//                                complaintModel.setCreated_Date_Time(json.optString("registeredDate"));
//                                svRegisteredDate = json.optString("registeredDate");
//                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//                                try {
//                                    dateVal = format.parse(svRegisteredDate);
//                                    System.out.println(dateVal);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            if (json.has("toberectifiedTime")) {
//                                complaintModel.setTo_be_completion_Date_Time(json.optString("toberectifiedTime"));
//                            }
//                            if (json.has("complaintStatus")) {
//                                complaintModel.setResolution_Flag(json.optString("complaintStatus"));
//                            }
//                        }
//                        prePopulateData();
//                    }
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
                prePopulateData();
            }
        });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*Prepopulate Data*/
    private void prePopulateData() {
        sv_main.setVisibility(View.VISIBLE);
        try {
            tv_complaint_id.setText(complaintModel.getComplaint_Id());
            tv_service_no.setText(complaintModel.getService_No());
            tv_consumer_name.setText(complaintModel.getConsumer_Name());
            tv_mobile_no.setText(complaintModel.getConsumer_Phone_No());
            tv_address.setText(complaintModel.getAddress());
            tv_category.setText(complaintModel.getCategory());
            tv_complaint_type.setText(complaintModel.getComplaint_Type());
            tv_sub_complaint_type.setText(complaintModel.getComplaint_Sub_Type());
            tv_add_info.setText(complaintModel.getAdditional_Information());
            tv_reg_date.setText(complaintModel.getCreated_Date_Time());
            tv_completion_date.setText(complaintModel.getTo_be_completion_Date_Time());
            tv_resolution.setText(complaintModel.getResolution_Flag());
            if (complaintModel.getComplaint_Type().equalsIgnoreCase("Supply Failure Related")) {
                spn_supply_failure.setVisibility(View.VISIBLE);
                et_remarks.setVisibility(View.GONE);
            } else {
                et_remarks.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean checkComplaintDate() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
            Date selectedDate = format.parse(complaint_date.getText().toString().trim() + " " + complaint_time.getText().toString().trim());

            long diffMillis = selectedDate.getTime() - dateVal.getTime();
            int diffMins = (int) (diffMillis / (1000 * 60));

            if(System.currentTimeMillis() - selectedDate.getTime() < 0)
            {
                Utility.showCustomOKOnlyDialog(this, "Complaint closing time should not be in future.");
                return  false;
            }

            if (diffMins < 15)
            {
                Utility.showCustomOKOnlyDialog(this, "Complaint closing time should not be less than 15 min from Complaint Registered time/ Not empty.");
                return false;
            }

            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /*Update Complaint status*/
    @OnClick(R.id.btn_submit)
    void submitDetails() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                pDialog = new ProgressDialog(this);
                pDialog.show();
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                postData();
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }

    }

    /*Post Data to API*/
    private void postData() {


//        RequestParams requestParams = new RequestParams();
//        requestParams.put("COMPLAINTID", complaintModel.getComplaint_Id());
//        requestParams.put("RESOLVED", status);
//        requestParams.put("RESOLVEDTIME", complaint_date.getText().toString().trim() + "_" + complaint_time.getText().toString().trim());
//        requestParams.put("REMARKS", et_remarks.getText().toString());
        DateFormat currentFormate = new SimpleDateFormat("dd-MMM-yyyy HH:mm",Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyyMMddHHmmss",Locale.ENGLISH);
        Date date1 = null;
        String closeDate = "";
        try {
            date1 = currentFormate.parse(complaint_date.getText().toString() +" " + complaint_time.getText().toString().trim());
            closeDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject requestObj = new JSONObject();
       try{
           requestObj.put("COMPLAINT_ID",complaintModel.getComplaint_Id());
           requestObj.put("RESOLVED",status);
           requestObj.put("COMPLAINT_CLOSE_DT",closeDate);
           requestObj.put("USER_ID",userID);
           if (!complaintModel.getComplaint_Type().equalsIgnoreCase("Supply Failure Related")) {
               requestObj.put("REMARKS", et_remarks.getText().toString());
           } else if (complaintModel.getComplaint_Type().equalsIgnoreCase("Supply Failure Related") && !supplyFailureStr.equalsIgnoreCase("Supply Failure - Other")) {
               requestObj.put("REMARKS", supplyFailureStr);
           } else if (complaintModel.getComplaint_Type().equalsIgnoreCase("Supply Failure Related") && supplyFailureStr.equalsIgnoreCase("Supply Failure - Other")) {
               requestObj.put("REMARKS", et_remarks.getText().toString() + " " + supplyFailureStr);
           }else {
               requestObj.put("REMARKS","YES");
           }

       }catch (Exception e){
           e.printStackTrace();
       }

        //requestParams.put("REMARKS", et_remarks.getText().toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/ComplaintUpdate/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                Utility.showLog("onSuccess", responseStr);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject responseObj = new JSONObject(responseStr);
                    responseObj = responseObj.getJSONObject("response");
                    String success = responseObj.getString("success");
                    if(responseObj.has("success")&&success.equalsIgnoreCase("True")){
                        if(cType.equalsIgnoreCase("No"))
                            ComplaintsListActivity.getInstance().updateList(complaintModel.getComplaint_Id());
                        showCustomOKOnlyDialog("Complaint Status updated successfully.");
                    }else {
                        Utility.showCustomOKOnlyDialog(ComplaintDetailActivity.this, responseObj.optString("message"));
                    }
//                    if (jsonObject.has("STATUS")) {
//                        if (!jsonObject.optString("STATUS").equalsIgnoreCase("False")) {
//                            ComplaintsListActivity.getInstance().updateList(complaintModel.getComplaint_Id());
//                            showCustomOKOnlyDialog("Complaint Status updated successfully.");
//                        } else {
//                            Utility.showCustomOKOnlyDialog(ComplaintDetailActivity.this, jsonObject.optString("FLAG1"));
//                        }
//                    }
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
                Utility.showCustomOKOnlyDialog(ComplaintDetailActivity.this, error.getMessage());
            }
        });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /*Validate Mandatory Fields*/
    private boolean validateFields() {
        if (rg_status.getCheckedRadioButtonId() == -1) {
            // no radio buttons are checked
            Utility.showCustomOKOnlyDialog(this, "Please Select Complaint Status");
            return false;
        } else if (complaintModel.getComplaint_Type().equalsIgnoreCase("Supply Failure Related")) {
            if (Utility.isValueNullOrEmpty(supplyFailureStr)) {
                Utility.showCustomOKOnlyDialog(this, "Please Enter Remarks");
                return false;
            } else if (supplyFailureStr.equalsIgnoreCase("Supply Failure - Other")) {
                if (Utility.isValueNullOrEmpty(et_remarks.getText().toString())) {
                    Utility.showCustomOKOnlyDialog(this, "Please Enter Remarks Text");
                    return false;
                }
            }
        } else if (!complaintModel.getComplaint_Type().equalsIgnoreCase("Supply Failure Related") && Utility.isValueNullOrEmpty(et_remarks.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Remarks Text");
            return false;
        }else if (Utility.isValueNullOrEmpty(complaint_time.getText().toString()) || Utility.isValueNullOrEmpty(complaint_date.getText().toString()) ) {
            Utility.showCustomOKOnlyDialog(this, "Please select the Date/Time");
            return false;
        }


        if(!checkComplaintDate()){
            return false;
        }
//        else if (Utility.isValueNullOrEmpty(supplyFailureStr)) {
//            Utility.showCustomOKOnlyDialog(this, "Please Enter Remarks");
//            return false;
//        }else if(supplyFailureStr.equalsIgnoreCase("Supply Failure - Other")){
//            if (Utility.isValueNullOrEmpty(et_remarks.getText().toString())) {
//                Utility.showCustomOKOnlyDialog(this, "Please Enter Remarks Text");
//                return false;
//            }
//        }
        return true;
    }

    /*Show Dialog*/
    public void showCustomOKOnlyDialog(String message) {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(this);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_ok);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);

            TextView txt_msz = (TextView) dialog_confirm.findViewById(R.id.txt_heading);
            txt_msz.setText(message);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_confirm.dismiss();
                    finish();
                }
            });
            dialog_confirm.show();
            dialog_confirm.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    Constants.isDialogOpen = false;
                }
            });
        }
    }
}
