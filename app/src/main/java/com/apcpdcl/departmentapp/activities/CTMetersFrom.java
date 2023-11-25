package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.models.CTMFeedersModel;
import com.apcpdcl.departmentapp.models.CTMPurposeModel;
import com.apcpdcl.departmentapp.models.CTMSSModel;
import com.apcpdcl.departmentapp.models.DTRModel;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.ImageUtil;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CTMetersFrom extends AppCompatActivity {
    private ProgressDialog pDialog;
    private Spinner spn_month,spn_year,spn_meter_make,spn_ss_name,spn_feder_name, spn_purpose,spn_service_type;
    private Spinner spn_meter_model,spn_meter_ct_ratio,spn_meter_type,spn_meter_accuracy_class,spn_dtr_name;
    private String strDtrCode,selectedMonth,selectedYear,selectedMeterMakeStr, selectedModel,selectedMeterCtRatio,selectedServiceType, strSSCode, strFeederCode, strPurposeCode;
    private String selectedMeterType,selectedMeterAccuracyClass;
    private ArrayList<String> meterMakeList;
    private ArrayList<String> meterModelList;
    private ArrayList<String> meterCtRatioList;
    private ArrayList<String> meterTypeList;
    private ArrayList<String> meterAccuracyClassList;
    private ArrayList<String> meterServiceTypeList;
    private static String serviceNumber = null;
    ArrayList<CTMPurposeModel> purposeList = new ArrayList<CTMPurposeModel>();
    ArrayList<CTMSSModel> ssList = new ArrayList<CTMSSModel>();
    ArrayList<CTMFeedersModel> feederList = new ArrayList<CTMFeedersModel>();
    ArrayList<DTRModel> dtrList = new ArrayList<DTRModel>();
    LocationManager locationManager;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String str_Latitude, str_Longitude;
    private static final int REQUEST_LOCATION = 1;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();

    ImageView ctMeter_iv;
    Bitmap photo_CTM;
    String base64_ctmimg;
    SpinnerAdapter ssSpinnerAdapter, feederSpinnerAdapter, dtrSpinnerAdapter;

//    @BindView(R.id.btn_submit_ctm_details)
//    Button btn_submit_ctm_details;

    private EditText et_service_number, et_name, et_dtr_code, et_pole_no, et_meter_sl_no,et_cat, et_nodigits, et_pono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ctmeters_from);
        pDialog = new ProgressDialog(this);

        spn_meter_make = findViewById(R.id.spn_meter_make);
        spn_meter_model = findViewById(R.id.spn_meter_model);
        spn_meter_type = findViewById(R.id.spn_meter_type);
        spn_year = findViewById(R.id.spn_year);
        spn_month = findViewById(R.id.spn_month);
        spn_meter_accuracy_class = findViewById(R.id.spn_meter_accuracy_class);
        spn_meter_ct_ratio = findViewById(R.id.spn_meter_ct_ratio);
        spn_purpose = findViewById(R.id.spn_purpose);
        spn_service_type = findViewById(R.id.spn_service_type);

        spn_ss_name = findViewById(R.id.spn_ss_name);
        spn_feder_name = findViewById(R.id.spn_feder_name);
        spn_dtr_name = findViewById(R.id.spn_dtr_name);

        et_service_number = findViewById(R.id.et_service_number);
        et_name = findViewById(R.id.et_name);
        et_dtr_code = findViewById(R.id.et_dtr_code);
        et_pole_no = findViewById(R.id.et_pole_no);
        et_meter_sl_no = findViewById(R.id.et_meter_sl_no);
        et_cat = findViewById(R.id.et_cat);
        et_nodigits = findViewById(R.id.et_nodigits);
        et_pono = findViewById(R.id.et_pono);

        ctMeter_iv = findViewById(R.id.ctMeter_iv);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            serviceNumber = (String) bd.get("CTMSERVICENO");
        }
        JSONObject paramJsonObject = new JSONObject();

        try {
            paramJsonObject.put("serviceno", serviceNumber);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        getServiceNumberDetails(paramJsonObject);

        init();


    }

    private void init() {
        //Add String meterMakeStrArray to meterMakeArrayList
        meterMakeList = new ArrayList<>();
        String[] meterMakeStrArray = getResources().getStringArray(R.array.ctmeter_make);
        meterMakeList = new ArrayList<>(Arrays.asList(meterMakeStrArray));
        bindMeterMakeData(meterMakeList);//Meter Make data load

        //Add String meterModelStrArray to meterModelArrayList
        meterModelList = new ArrayList<>();
        String[] meterModelStrArray = getResources().getStringArray(R.array.model);
        meterModelList = new ArrayList<>(Arrays.asList(meterModelStrArray));
        bindMeterModelData(meterModelList);//Meter Make data load

        //Add String meterTypeStrArray to meterTypeArrayList
        meterTypeList = new ArrayList<>();
        String[] meterTypeStrArray = getResources().getStringArray(R.array.type);
        meterTypeList = new ArrayList<>(Arrays.asList(meterTypeStrArray));
        bindMeterTypeData(meterTypeList);//Meter Type data load

        //Add String meterAccuracyClassStrArray to meterAccuracyClassArrayList
        meterAccuracyClassList = new ArrayList<>();
        String[] meterAccuracyClassStrArray = getResources().getStringArray(R.array.accuracy_class);
        meterAccuracyClassList = new ArrayList<>(Arrays.asList(meterAccuracyClassStrArray));
        bindMeterAccuracyClassData(meterAccuracyClassList);//Meter Accuracy Class data load

        //Add String meterAccuracyClassStrArray to meterAccuracyClassArrayList
        meterCtRatioList = new ArrayList<>();
        String[] meterRatioStrArray = getResources().getStringArray(R.array.ratio);
        meterCtRatioList = new ArrayList<>(Arrays.asList(meterRatioStrArray));
        bindMeterRatioData(meterCtRatioList);//Meter Ratio data load

        //Add String meterAccuracyClassStrArray to meterAccuracyClassArrayList
        meterServiceTypeList = new ArrayList<>();
        String[] meterServiceTypeStrArray = getResources().getStringArray(R.array.ctm_service_type);
        meterServiceTypeList = new ArrayList<>(Arrays.asList(meterServiceTypeStrArray));
        bindServiceTypeData(meterServiceTypeList);//Meter Ratio data load


        loadMonthData();//Month data load
        loadYearData(); //Year data load
        //getSSDetails();

    }

    //Submit CTM Details
    public void onSubmitClicked(View view) {
        String poNumber= et_pono.getText().toString();
        String noDigits= et_nodigits.getText().toString();

        //Toast.makeText(getApplicationContext(), "Service numer is should be need", Toast.LENGTH_LONG).show();
        if(validateFields()){
            //Toast.makeText(getApplicationContext(), et_service_number.getText().toString()+"::et_service_number", Toast.LENGTH_LONG).show();
            JSONObject ctmDataJsonObg = new JSONObject();
//            try {
//                ctmDataJsonObg.put("SCNO", et_service_number.getText().toString());
//                ctmDataJsonObg.put("CTNAME", et_name.getText().toString());
//                ctmDataJsonObg.put("TYPE", selectedServiceType);
//                ctmDataJsonObg.put("CMPURPOSECD", strPurposeCode);
//                ctmDataJsonObg.put("CTDTR_CODE", et_dtr_code.getText().toString());
//                ctmDataJsonObg.put("CTPOLE_NO", et_pole_no.getText().toString());
//                ctmDataJsonObg.put("CTMETER_NO", et_meter_sl_no.getText().toString());
//                ctmDataJsonObg.put("CTSS_NAME", strSSCode);
//                ctmDataJsonObg.put("CTFDR_NAME", strFeederCode);
//                ctmDataJsonObg.put("CTMETER_MAKE", selectedMeterMakeStr);
//                ctmDataJsonObg.put("CTMETER_MODEL", selectedModel);
//                ctmDataJsonObg.put("CTMETER_CT_RATION", selectedMeterCtRatio);
//                ctmDataJsonObg.put("CTMETER_TYPE", selectedMeterType);
//                ctmDataJsonObg.put("CTCLASS_OF_ACCURACY", selectedMeterAccuracyClass);
//                ctmDataJsonObg.put("CTMFDATE", selectedMonth + " " + selectedYear);
//                ctmDataJsonObg.put("IMAGE", base64_ctmimg);
//                ctmDataJsonObg.put("LONGITUDE", str_Longitude);
//                ctmDataJsonObg.put("LATITUDE", str_Latitude);
//                ctmDataJsonObg.put("CTMCATEGORY", et_cat.getText().toString());
//                ctmDataJsonObg.put("CTMDIGITS", noDigits);
//                ctmDataJsonObg.put("CTMPONO", poNumber);
//
//                ctmDataSave(ctmDataJsonObg);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            try {
                ctmDataJsonObg.put("SCNO",et_service_number.getText().toString());
                ctmDataJsonObg.put("CTNAME",et_name.getText().toString());
                ctmDataJsonObg.put("TYPE",selectedServiceType);
                ctmDataJsonObg.put("CMPURPOSECD", "");
                ctmDataJsonObg.put("CTDTR_CODE",et_dtr_code.getText().toString());
                ctmDataJsonObg.put("CTPOLE_NO",et_pole_no.getText().toString());
                ctmDataJsonObg.put("CTMETER_NO",et_meter_sl_no.getText().toString());
                ctmDataJsonObg.put("CTSS_NAME", "");
                ctmDataJsonObg.put("CTFDR_NAME", "");
                ctmDataJsonObg.put("CTMETER_MAKE",selectedMeterMakeStr);
                ctmDataJsonObg.put("CTMETER_MODEL", "");
                ctmDataJsonObg.put("CTMETER_CT_RATION",selectedMeterCtRatio);
                ctmDataJsonObg.put("CTMETER_TYPE",selectedMeterType);
                ctmDataJsonObg.put("CTCLASS_OF_ACCURACY",selectedMeterAccuracyClass);
                ctmDataJsonObg.put("CTMFDATE",selectedMonth+ " "+ selectedYear);
                ctmDataJsonObg.put("IMAGE", "");
                ctmDataJsonObg.put("LONGITUDE", "");
                ctmDataJsonObg.put("LATITUDE", "");
                ctmDataJsonObg.put("CTMCATEGORY",et_cat.getText().toString());
                ctmDataJsonObg.put("CTMDIGITS", "");
                ctmDataJsonObg.put("CTMPONO",poNumber);

                ctmDataSave(ctmDataJsonObg);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }else {

        }
    }

    private void ctmDataSave(JSONObject ctmDataJsonObg) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        HttpEntity entity;
        try {
            entity = new StringEntity(ctmDataJsonObg.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Bearer ybgasdf54bYHFr2347yh786%$#NHS&ghgapcpdcl*")};
            client.post(this,Constants.CTMETERS_URL+ "ctmtinsert/",headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {

                        JSONObject obj = new JSONObject(response);
                        if(obj.length()>0 && obj!= null){
                            String status = obj.getString("Status");
                            Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                            Intent inCTMSList = new Intent(getApplicationContext(), CTMeterServiceListActivity.class);
                            startActivity(inCTMSList);
                        }else{
                            Toast.makeText(getApplicationContext(), "Data Not Saved", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (UnsupportedEncodingException e){
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            e.printStackTrace();
        }
    }

    //    @OnClick(R.id.btn_submit_ctm_details)
//    void submitCTMDetails() {
//        Toast.makeText(getApplicationContext(), "Service numer is should be need", Toast.LENGTH_LONG).show();
//        if(validateFields()){
//            Toast.makeText(getApplicationContext(), et_service_number.getText().toString()+"::et_service_number", Toast.LENGTH_LONG).show();
//        }
//    }
    //Bind Meter Make Data
    private void bindMeterMakeData(ArrayList meterMakeList){
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, meterMakeList);
        spn_meter_make.setAdapter(adapter);
        spn_meter_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedMeterMakeStr = (String) meterMakeList.get(position);
                if (selectedMeterMakeStr.equalsIgnoreCase("--Select--")) {
                        selectedMeterMakeStr = "";
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Bind Service Type Data
    private void bindServiceTypeData(ArrayList serviceTypeList){
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, serviceTypeList);
        spn_service_type.setAdapter(adapter);
        spn_service_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedServiceType = (String) serviceTypeList.get(position);
                if (selectedServiceType.equalsIgnoreCase("--Select--")) {
                        selectedServiceType = "";
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Bind Meter Model Data
    private void bindMeterModelData(ArrayList meterModelList){
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, meterModelList);
        spn_meter_model.setAdapter(adapter);
        spn_meter_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedModel = (String) meterModelList.get(position);
                if (selectedModel.equalsIgnoreCase("--Select--")) {
                        selectedModel = "";
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Bind Meter Accuracy Class Data
    private void bindMeterAccuracyClassData(ArrayList meterAccuracyClassList){
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, meterAccuracyClassList);
        spn_meter_accuracy_class.setAdapter(adapter);
        spn_meter_accuracy_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedMeterAccuracyClass = (String) meterAccuracyClassList.get(position);
                if (selectedMeterAccuracyClass.equalsIgnoreCase("--Select--")) {
                        selectedMeterAccuracyClass = "";
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Bind Meter Type Data
    private void bindMeterTypeData(ArrayList meterTypeList){
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, meterTypeList);
        spn_meter_type.setAdapter(adapter);
        spn_meter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedMeterType = (String) meterTypeList.get(position);
                if (selectedMeterType.equalsIgnoreCase("--Select--")) {
                        selectedMeterType = "";
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Bind Meter Ratio Data
    private void bindMeterRatioData(ArrayList meterRatioList){
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, meterRatioList);
        spn_meter_ct_ratio.setAdapter(adapter);
        spn_meter_ct_ratio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    selectedMeterCtRatio = (String) meterRatioList.get(position);
                if (selectedMeterCtRatio.equalsIgnoreCase("--Select--")) {
                        selectedMeterCtRatio = "";
                    }
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Load Month Data
    private void loadMonthData(){
        final ArrayList<String> arrMonth = new ArrayList<>();
        arrMonth.add(0, "--Select Month--");
        arrMonth.add("01");
        arrMonth.add("02");
        arrMonth.add("03");
        arrMonth.add("04");
        arrMonth.add("05");
        arrMonth.add("06");
        arrMonth.add("07");
        arrMonth.add("08");
        arrMonth.add("09");
        arrMonth.add("10");
        arrMonth.add("11");
        arrMonth.add("12");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrMonth);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_month.setAdapter(arrayAdapter);
        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedMonth = arrMonth.get(position);
                if (selectedMonth.equalsIgnoreCase("--Select Month--")) {
                    selectedMonth = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Load Year Data
    private void loadYearData(){

        ArrayList<String> arrYear = new ArrayList<>();
        int cyear = Calendar.getInstance().get(Calendar.YEAR);
        arrYear.add(0, "--Select Year--");
        for(int py=2000,i=1;py<=cyear;py++,i++){
            arrYear.add(i, String.valueOf(py));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrYear);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_year.setAdapter(arrayAdapter);
        spn_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedYear = arrYear.get(position);
                if (selectedYear.equalsIgnoreCase("--Select Year--")) {
                    selectedYear = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //if (progressDialog != null && progressDialog.isShowing()) {
                //  progressDialog.dismiss();
                //  }
            }
        });
    }

    //Validate Fields
    /*    Validate Mandatory Fields*/
    private boolean validateFields() {
        if(Utility.isValueNullOrEmpty(et_service_number.getText().toString())){
            Toast.makeText(getApplicationContext(), "Service number is should be need", Toast.LENGTH_LONG).show();
            return false;
        }else if(Utility.isValueNullOrEmpty(et_name.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please enter the name", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if(Utility.isValueNullOrEmpty(et_dtr_code.getText().toString())){
//            Toast.makeText(getApplicationContext(), "Please enter the DTR Code", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if(Utility.isValueNullOrEmpty(et_pole_no.getText().toString())){
//            Toast.makeText(getApplicationContext(), "Please enter the pole number", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if (Utility.isValueNullOrEmpty(et_cat.getText().toString())) {
//            Toast.makeText(getApplicationContext(), "Please enter the Category", Toast.LENGTH_LONG).show();
//            return false;
//        }
        else if (Utility.isValueNullOrEmpty(et_meter_sl_no.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Please enter the Meter Serial Number", Toast.LENGTH_LONG).show();
            return false;
        }else if(Utility.isValueNullOrEmpty(selectedServiceType)){
            Toast.makeText(getApplicationContext(), "Please enter the Service Type", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if (Utility.isValueNullOrEmpty(strPurposeCode)) {
//            Toast.makeText(getApplicationContext(), "Please select the Purpose", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        else if (Utility.isValueNullOrEmpty(strSSCode)) {
//            Toast.makeText(getApplicationContext(), "Please select the SS name", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (Utility.isValueNullOrEmpty(strFeederCode)) {
//            Toast.makeText(getApplicationContext(), "Please select the feeeder name", Toast.LENGTH_LONG).show();
//            return false;
//        } else if (Utility.isValueNullOrEmpty(strDtrCode)) {
//            Toast.makeText(getApplicationContext(), "Please select the DTR name", Toast.LENGTH_LONG).show();
//            return false;
//        }
        else if (Utility.isValueNullOrEmpty(selectedMeterMakeStr)) {
            Toast.makeText(getApplicationContext(), "Please select the Meter Make", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if (Utility.isValueNullOrEmpty(selectedModel)) {
//            Toast.makeText(getApplicationContext(), "Please select the Meter Model", Toast.LENGTH_LONG).show();
//            return false;
//        }
        else if (Utility.isValueNullOrEmpty(selectedMeterCtRatio)) {
            Toast.makeText(getApplicationContext(), "Please select the Meter CT Ratio", Toast.LENGTH_LONG).show();
            return false;
        }else if(Utility.isValueNullOrEmpty(selectedMeterType)){
            Toast.makeText(getApplicationContext(), "Please select the Meter Type", Toast.LENGTH_LONG).show();
            return false;
        }else if(Utility.isValueNullOrEmpty(selectedMeterAccuracyClass)){
            Toast.makeText(getApplicationContext(), "Please select the Meter Accuracy Class", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if (Utility.isValueNullOrEmpty(et_pono.getText().toString())) {
//            Toast.makeText(getApplicationContext(), "Please enter the PO Number", Toast.LENGTH_LONG).show();
//            return false;
//        }
        else if (Utility.isValueNullOrEmpty(selectedMonth)) {
            Toast.makeText(getApplicationContext(), "Please select the Meter Manufacturing Month", Toast.LENGTH_LONG).show();
            return false;
        }else if(Utility.isValueNullOrEmpty(selectedYear)){
            Toast.makeText(getApplicationContext(), "Please select the Meter Manufacturing Year", Toast.LENGTH_LONG).show();
            return false;
        }
//        else if (Utility.isValueNullOrEmpty(base64_ctmimg)) {
//            Toast.makeText(getApplicationContext(), "Please capture the CTMeter image", Toast.LENGTH_LONG).show();
//            return false;
//        }
        return true;
    }

    //Featch Service Number Details Web Service call
    private void getServiceNumberDetails(JSONObject paramsJson) {
        HttpEntity entity;
        try {
            entity = new StringEntity(paramsJson.toString());
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            AsyncHttpClient client = new AsyncHttpClient();
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Bearer ybgasdf54bYHFr2347yh786%$#NHS&ghgapcpdcl*")};
            client.post(this,Constants.CTMETERS_URL+ "ConsumerDetails/",headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        //JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = new JSONArray(response);
                        //Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();

                        if(jsonArray != null && jsonArray.length() > 0 ){
                            if(jsonArray.getJSONObject(0).getString("CONSDETAILS") != null && jsonArray.getJSONObject(0).getString("CONSDETAILS").length()>0){
                                JSONArray jsonConsObj = new JSONArray(jsonArray.getJSONObject(0).getString("CONSDETAILS"));
                                et_service_number.setText(jsonConsObj.getJSONObject(0).getString("CUSCNO"));
                                et_name.setText(jsonConsObj.getJSONObject(0).getString("CFNAME"));
                                et_dtr_code.setText(jsonConsObj.getJSONObject(0).getString("CDTRCD"));
                                if(jsonConsObj.getJSONObject(0).getString("CPOLENUM").trim().equalsIgnoreCase("--") || jsonConsObj.getJSONObject(0).getString("CPOLENUM").trim().equalsIgnoreCase("0")){
                                    et_pole_no.setEnabled(true);
                                    et_pole_no.setText(jsonConsObj.getJSONObject(0).getString("CPOLENUM"));
                                }else {
                                    et_pole_no.setEnabled(false);
                                    et_pole_no.setText(jsonConsObj.getJSONObject(0).getString("CPOLENUM"));
                                }
                                //et_meter_sl_no.setText(jsonConsObj.getJSONObject(0).getString("MNUMBER"));
                                et_cat.setText(jsonConsObj.getJSONObject(0).getString("CMCAT"));
                            }
                            if(jsonArray.getJSONObject(0).getString("PURPOSE_OF_SUPPLY") != null && jsonArray.getJSONObject(0).getString("PURPOSE_OF_SUPPLY").length()>0){
                                JSONArray jsonPurposeObj = new JSONArray(jsonArray.getJSONObject(0).getString("PURPOSE_OF_SUPPLY"));
                                for (int i = 0; i < jsonPurposeObj.length(); i++) {
                                    JSONObject jsonObject = jsonPurposeObj.getJSONObject(i);
                                    CTMPurposeModel ctmPurposeModel = new Gson().fromJson(jsonObject.toString(),
                                            CTMPurposeModel.class);
                                    purposeList.add(ctmPurposeModel);
                                }
                                //bindPurposeSpinnerData();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
    //                if (pDialog != null && pDialog.isShowing()) {
    //                    pDialog.dismiss();
    //                }
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    /* Bind Purpose Spinner Data**/
    private void bindPurposeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < purposeList.size(); i++) {
            list.add(purposeList.get(i).getCMPURPOSENAME());
        }

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_purpose.setAdapter(newlineAdapter);
        spn_purpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strPurposeCode = list.get(position);
                if (!strPurposeCode.equalsIgnoreCase("--Select--")) {
                    strPurposeCode = purposeList.get(position-1).getCMPURPOSECD();
                }else{
                    strPurposeCode = "";
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Featch Division wise Substation list Web Service call
    private void getSSDetails() {
        JSONObject paramsSSGetObj = new JSONObject();
        try {
            paramsSSGetObj.put("divcd", serviceNumber.substring(0,2));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpEntity entity;
        try {
            entity = new StringEntity(paramsSSGetObj.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Bearer ybgasdf54bYHFr2347yh786%$#NHS&ghgapcpdcl*")};
            client.post(this,Constants.CTMETERS_URL+ "CTMetersubcdlist/",headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //                if (pDialog != null && pDialog.isShowing()) {
                    //                    pDialog.dismiss();
                    //                }
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        //Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                        if(ssList.size()>0)
                            ssList.clear();
                        if(jsonArray != null && jsonArray.length() > 0 ){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CTMSSModel ctmSsModel = new Gson().fromJson(jsonObject.toString(),
                                        CTMSSModel.class);
                                ssList.add(ctmSsModel);
                            }
                            bindSsSpinnerData(ssList);
                        }else{
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    //                if (pDialog != null && pDialog.isShowing()) {
                    //                    pDialog.dismiss();
                    //                }
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    /* Bind SS Spinner Data**/
    private void bindSsSpinnerData(ArrayList<CTMSSModel> ssListData) {
        ssList = ssListData;
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < ssList.size(); i++) {
            list.add(ssList.get(i).getsubname());
        }

        ssSpinnerAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_ss_name.setAdapter(ssSpinnerAdapter);
        spn_ss_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strSSCode = list.get(position);
//                Toast.makeText(getApplicationContext(), "strSSCode Length"+list.size(), Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), "ssList Length"+ssList.size(), Toast.LENGTH_LONG).show();
                feederList.clear();
                bindFeederSpinnerData();
                dtrList.clear();
                bindDTRSpinnerData();
                if (!strSSCode.equalsIgnoreCase("--Select--")) {
                    if (objNetworkReceiver.hasInternetConnection(CTMetersFrom.this)) {
                        strSSCode = ssList.get(position-1).getsubcode();
                        //getFeedersDetails(strSSCode);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }else{
                    strSSCode = "";
                    feederList.clear();
                    dtrList.clear();
                }

            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Featch Sub station wise feeder list Web Service call
    private void getFeedersDetails(String strSSCode) {
        JSONObject paramsSSGetObj = new JSONObject();
        try {
            paramsSSGetObj.put("subcd", strSSCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpEntity entity;
        try {
            entity = new StringEntity(paramsSSGetObj.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Bearer ybgasdf54bYHFr2347yh786%$#NHS&ghgapcpdcl*")};
            client.post(this,Constants.CTMETERS_URL+ "CTMeterFeederlist/",headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //                if (pDialog != null && pDialog.isShowing()) {
                    //                    pDialog.dismiss();
                    //                }
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        //Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                        feederList.clear();
                        if(jsonArray != null && jsonArray.length() > 0 ){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                CTMFeedersModel feederModel = new Gson().fromJson(jsonObject.toString(),
                                        CTMFeedersModel.class);
                                feederList.add(feederModel);
                            }
                            bindFeederSpinnerData();
                            if (objNetworkReceiver.hasInternetConnection(CTMetersFrom.this)) {
                                //getDtrDetails(strFeederCode);

                            } else {
                                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    //                if (pDialog != null && pDialog.isShowing()) {
                    //                    pDialog.dismiss();
                    //                }
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    //Featch Feeder wise DTR list Web Service call
    private void getDtrDetails(String strFdCode) {
        JSONObject paramsSSGetObj = new JSONObject();
        try {
            paramsSSGetObj.put("FDRCD", strFdCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpEntity entity;
        try {
            entity = new StringEntity(paramsSSGetObj.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Bearer ybgasdf54bYHFr2347yh786%$#NHS&ghgapcpdcl*")};
            client.post(this,Constants.CTMETERS_URL+ "CTMeterFdrDtrlist/",headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    //                if (pDialog != null && pDialog.isShowing()) {
                    //                    pDialog.dismiss();
                    //                }
                    try {

                        JSONArray jsonArray = new JSONArray(response);
                        //Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                        dtrList.clear();
                        if(jsonArray != null && jsonArray.length() > 0 ){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                DTRModel dtrModel = new Gson().fromJson(jsonObject.toString(),
//                                        DTRModel.class);

                                dtrList.add(new DTRModel(jsonObject.getString("DTRLOC"),jsonObject.getString("DTRCD")));
                            }

                            bindDTRSpinnerData();
//                            if (objNetworkReceiver.hasInternetConnection(CTMetersFrom.this)) {
//
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
//                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    //                if (pDialog != null && pDialog.isShowing()) {
                    //                    pDialog.dismiss();
                    //                }
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    /* Bind Feeder Spinner Data**/
    private void bindFeederSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < feederList.size(); i++) {
            list.add(feederList.get(i).getfdrname());
        }

        feederSpinnerAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_feder_name.setAdapter(feederSpinnerAdapter);
        spn_feder_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strFeederCode = list.get(position);
                dtrList.clear();
                bindDTRSpinnerData();
                if (!strFeederCode.equalsIgnoreCase("--Select--")) {
                    if (objNetworkReceiver.hasInternetConnection(CTMetersFrom.this)) {
                        strFeederCode = feederList.get(position-1).getfdrcode();
                        //getDtrDetails(strFeederCode);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }else{
                    strFeederCode = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* Bind DTR Spinner Data**/
    private void bindDTRSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = 0; i < dtrList.size(); i++) {
            list.add(dtrList.get(i).getDTRName());
        }

        dtrSpinnerAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_dtr_name.setAdapter(dtrSpinnerAdapter);
        spn_dtr_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strDtrCode = list.get(position);

                if (!strDtrCode.equalsIgnoreCase("--Select--")) {
                    if (objNetworkReceiver.hasInternetConnection(CTMetersFrom.this)) {
                        strDtrCode = dtrList.get(position-1).getDTRCode();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                    }
                }else{
                    strDtrCode = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Capture the CTMeter Image
    public void captureCTMImg(View view) {
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//                OnGPS();
//            } else {
//                if (ActivityCompat.checkSelfPermission(
//                        CTMetersFrom.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        CTMetersFrom.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//                } else {
//                    Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                    if (locationGPS != null) {
//                        str_Latitude = String.valueOf(locationGPS.getLatitude());
//                        str_Longitude = String.valueOf(locationGPS.getLongitude());
//                    } else {
//                        str_Latitude = "0.0";
//                        str_Longitude = "0.0";
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 100);
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            photo_CTM = (Bitmap) data.getExtras().get("data");
            ctMeter_iv.setImageBitmap(photo_CTM);
            base64_ctmimg = ImageUtil.convert(photo_CTM);
        }

    }
}