package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.BillDetailsModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceDetailsActivity extends AppCompatActivity {
    @BindView(R.id.et_service_number)
    EditText et_service_number;
    @BindView(R.id.tv_service_no)
    TextView tv_service_no;
    @BindView(R.id.tv_section)
    TextView tv_section;
    @BindView(R.id.tv_distribution)
    TextView tv_distribution;
    @BindView(R.id.tv_ero_name)
    TextView tv_ero_name;
    @BindView(R.id.tv_category)
    TextView tv_category;
    @BindView(R.id.tv_consumer_name)
    TextView tv_consumer_name;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_last_paid_amount)
    TextView tv_last_paid_amount;
    @BindView(R.id.tv_last_paid_date)
    TextView tv_last_paid_date;
    @BindView(R.id.tv_opening_reading)
    TextView tv_opening_reading;
    @BindView(R.id.tv_closing_reading)
    TextView tv_closing_reading;
    @BindView(R.id.tv_due_date)
    TextView tv_due_date;
    @BindView(R.id.tv_disconnection_date)
    TextView tv_disconnection_date;
    @BindView(R.id.tv_billed_units)
    TextView tv_billed_units;
    @BindView(R.id.tv_bill_amount)
    TextView tv_bill_amount;
    @BindView(R.id.tv_arrears_amount)
    TextView tv_arrears_amount;
    @BindView(R.id.tv_acd_amount)
    TextView tv_acd_amount;
    @BindView(R.id.tv_bill_date)
    TextView tv_bill_date;
    @BindView(R.id.tv_cycle)
    TextView tv_cycle;
    @BindView(R.id.tv_contract_load)
    TextView tv_contract_load;
    @BindView(R.id.tv_dtr_code)
    TextView tv_dtr_code;
    @BindView(R.id.tv_feeder_code)
    TextView tv_feeder_code;
    @BindView(R.id.tv_mf)
    TextView tv_mf;
    @BindView(R.id.tv_closing_status)
    TextView tv_closing_status;
    @BindView(R.id.tv_service_type)
    TextView tv_service_type;
    @BindView(R.id.tv_mobile_number)
    TextView tv_mobile_number;
    @BindView(R.id.tv_aadhar_number)
    TextView tv_aadhar_number;
    @BindView(R.id.tv_irda_flag)
    TextView tv_irda_flag;
    @BindView(R.id.tv_date_of_supply)
    TextView tv_date_of_supply;
    @BindView(R.id.tv_bill_type)
    TextView tv_bill_type;
    @BindView(R.id.tv_meter_number)
    TextView tv_meter_number;
    @BindView(R.id.tv_dtr_location)
    TextView tv_dtr_location;
    @BindView(R.id.tv_linked_service_no)
    TextView tv_linked_service_no;
    @BindView(R.id.tv_acd)
    TextView tv_acd;
    @BindView(R.id.tv_nap)
    TextView tv_nap;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_get_details)
    Button btn_get_details;
    @BindView(R.id.ll_form)
    LinearLayout ll_form;
    private ProgressDialog pDialog;
    private BillDetailsModel billDetailsModel;
    private String sectionCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        pDialog = new ProgressDialog(this);
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        sectionCode = prefs.getString("Section_Code", "");
        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
//        et_service_number.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                if (charSequence.length() == 5 && !charSequence.toString().equals(sectionCode)) {
//                    Utility.showCustomOKOnlyDialog(ServiceDetailsActivity.this,
//                            et_service_number.getText().toString() + " Service Number is not related to you");
//                    et_service_number.setText("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
    }

    @OnClick(R.id.btn_get_details)
    void getDetails() {
        if (Utility.isValueNullOrEmpty(et_service_number.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please enter valid Service Number.");
        } else {
            if (Utility.isNetworkAvailable(this)) {
                pDialog.show();
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                Utility.hideSoftKeyboard(this, et_service_number);
                String sNumber = et_service_number.getText().toString();
                JSONObject requestObjPayLoad = new JSONObject();
                try{
                    if(sNumber.length() == 10){
                        requestObjPayLoad.put("SCN_No","");
                        requestObjPayLoad.put("bp_number",sNumber);
                    }else{
                        requestObjPayLoad.put("SCN_No",sNumber);
                        requestObjPayLoad.put("bp_number","");
                    }
                    getBillDetails(requestObjPayLoad);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
            }
        }
    }

    @OnClick(R.id.btn_submit)
    void navigateToBack() {
        et_service_number.setText("");
        ll_form.setVisibility(View.VISIBLE);
        billDetailsModel = null;
    }
    private void getBillDetails(JSONObject requestObj) {
        RequestParams params = new RequestParams();
        params.put("SCNO", et_service_number.getText().toString());
        AsyncHttpClient client = new AsyncHttpClient();
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            Utility.showLog("URL", Constants.URL + Constants.GEO_LATLONGINPUT);
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DeptApp/SAPISU/servicedetails", headers, entity, "application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(responseStr);
                    jsonObject = jsonObject.getJSONObject("response");
                    String successStr = jsonObject.getString("success");
                    if(successStr.equalsIgnoreCase("True")){
                        billDetailsModel = new Gson().fromJson(jsonObject.toString(), BillDetailsModel.class);
                        billDetailsModel = new BillDetailsModel(
                                jsonObject.getString("mobile"),
                                jsonObject.getString("section"),
                                jsonObject.getString("lpa"),
                                jsonObject.getString("nap"),
                                jsonObject.getString("subcycle"),
                                "",
                                jsonObject.getString("csname"),
                                jsonObject.getString("billdt"),
                                jsonObject.getString("lnksrvno"),
                                jsonObject.getString("dtrl"),
                                jsonObject.getString("dos"),
                                jsonObject.getString("mtrno"),
                                jsonObject.getString("arrearsamount"),
                                jsonObject.getString("contload"),
                                jsonObject.getString("discdt"),
                                jsonObject.getString("distribution"),
                                jsonObject.getString("feedercode"),
                                jsonObject.getString("billamount"),
                                jsonObject.getString("duedate"),
                                jsonObject.getString("mf"),
                                jsonObject.getString("clstat"),
                                jsonObject.getString("sctype"),
                                jsonObject.getString("acdAmount"),
                                jsonObject.getString("aadhar"),
                                jsonObject.getString("openrdg"),
                                jsonObject.getString("billunits"),
                                jsonObject.getString("address"),
                                jsonObject.getString("category"),
                                jsonObject.getString("ero"),
                                jsonObject.getString("lpd"),
                                jsonObject.getString("closerdg"),
                                jsonObject.getString("billtype"),
                                jsonObject.getString("irdaflag")

                        );
                        if (billDetailsModel.getERO().equalsIgnoreCase("No Bill parameters")) {
                            Utility.showCustomOKOnlyDialog(ServiceDetailsActivity.this, "Enter Valid Service Number");
                        } else {
                            setData();
                        }
                    }else{
                        Utility.showCustomOKOnlyDialog(ServiceDetailsActivity.this, jsonObject.getString("message"));
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
    } catch(Exception e) {
        e.printStackTrace();
    }

    }

//    private void getBillDetails() {
//        RequestParams params = new RequestParams();
//        params.put("SCNO", et_service_number.getText().toString());
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(Constants.GET_BILL_DETAILS, params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String response) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    billDetailsModel = new Gson().fromJson(jsonObject.toString(), BillDetailsModel.class);
//                    if (billDetailsModel.getERO().equalsIgnoreCase("No Bill parameters")) {
//                        Utility.showCustomOKOnlyDialog(ServiceDetailsActivity.this, "Enter Valid Service Number");
//                    } else {
//                        setData();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Throwable error, String content) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    private void setData() {
        ll_form.setVisibility(View.GONE);
        tv_service_no.setText(et_service_number.getText().toString());
        if (Utility.isValueNullOrEmpty(billDetailsModel.getSECTION())) {
            tv_section.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_section.setText(billDetailsModel.getSECTION());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getCategory())) {
            tv_category.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_category.setText(billDetailsModel.getCategory());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getCSNAME())) {
            tv_consumer_name.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_consumer_name.setText(billDetailsModel.getCSNAME());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getAddress())) {
            tv_address.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_address.setText(billDetailsModel.getAddress());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getDISTRIBUTION())) {
            tv_distribution.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_distribution.setText(billDetailsModel.getDISTRIBUTION());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getERO())) {
            tv_ero_name.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_ero_name.setText(billDetailsModel.getERO());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getLPA())) {
            tv_last_paid_amount.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_last_paid_amount.setText(billDetailsModel.getLPA());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getLPD())) {
            tv_last_paid_date.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_last_paid_date.setText(billDetailsModel.getLPD());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getOpenRdg())) {
            tv_opening_reading.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_opening_reading.setText(billDetailsModel.getOpenRdg());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getCloseRdg())) {
            tv_closing_reading.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_closing_reading.setText(billDetailsModel.getCloseRdg());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getDueDate())) {
            tv_due_date.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_due_date.setText(billDetailsModel.getDueDate());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getDiscDt())) {
            tv_disconnection_date.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_disconnection_date.setText(billDetailsModel.getDiscDt());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getBillUnits())) {
            tv_billed_units.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_billed_units.setText(billDetailsModel.getBillUnits());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getBillAmount())) {
            tv_bill_amount.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_bill_amount.setText(billDetailsModel.getBillAmount());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getArrearsAmount())) {
            tv_arrears_amount.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_arrears_amount.setText(billDetailsModel.getArrearsAmount());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getACDAmount())) {
            tv_acd_amount.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_acd_amount.setText(billDetailsModel.getACDAmount());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getBillDT())) {
            tv_bill_date.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_bill_date.setText(billDetailsModel.getBillDT());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getSubCycle())) {
            tv_cycle.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_cycle.setText(billDetailsModel.getSubCycle());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getContLoad())) {
            tv_contract_load.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_contract_load.setText(billDetailsModel.getContLoad());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getFeederCode())) {
            tv_feeder_code.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_feeder_code.setText(billDetailsModel.getFeederCode());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getMF())) {
            tv_mf.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_mf.setText(billDetailsModel.getMF());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getCLSTAT())) {
            tv_closing_status.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_closing_status.setText(billDetailsModel.getCLSTAT());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getSCType())) {
            tv_service_type.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_service_type.setText(billDetailsModel.getSCType());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getMOBILE())) {
            tv_mobile_number.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_mobile_number.setText(billDetailsModel.getMOBILE().replaceAll("\\w(?=\\w{3})", "*"));
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getAADHAR())) {
            tv_aadhar_number.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_aadhar_number.setText(billDetailsModel.getAADHAR().replaceAll("\\w(?=\\w{3})", "*"));
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getDOS())) {
            tv_date_of_supply.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_date_of_supply.setText(billDetailsModel.getDOS());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getMTRNO())) {
            tv_meter_number.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_meter_number.setText(billDetailsModel.getMTRNO());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getDTRL())) {
            tv_dtr_location.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_dtr_location.setText(billDetailsModel.getDTRL());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getNAP())) {
            tv_nap.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_nap.setText(billDetailsModel.getNAP());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getLnkSrvNo())) {
            tv_linked_service_no.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_linked_service_no.setText(billDetailsModel.getLnkSrvNo());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getACDAmount())) {
            tv_acd.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_acd.setText(billDetailsModel.getACDAmount());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getTransCode())) {
            tv_dtr_code.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_dtr_code.setText(billDetailsModel.getTransCode());
        }
        if (Utility.isValueNullOrEmpty(billDetailsModel.getBillType())) {
            tv_bill_type.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_bill_type.setText(billDetailsModel.getBillType());

        } if (Utility.isValueNullOrEmpty(billDetailsModel.getIrdaFlag())) {
            tv_irda_flag.setText(Utility.getResourcesString(this,R.string.not_available));
        } else {
            tv_irda_flag.setText(billDetailsModel.getIrdaFlag());
        }

    }

    @Override
    public void onBackPressed() {
        if (ll_form.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else {
            et_service_number.setText("");
            ll_form.setVisibility(View.VISIBLE);
            billDetailsModel = null;
        }
    }
}
