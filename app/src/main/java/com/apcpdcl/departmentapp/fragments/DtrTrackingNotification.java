package com.apcpdcl.departmentapp.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

//import java.util.Base64;


public class DtrTrackingNotification extends Fragment {
    View view = null;
    TextView tv_complaintNo, tv_faultDtrNo, tv_phase, tv_capacity, tv_notificationNo;
    EditText edt_healthyDtr;
    private String complaintNo, faultDtrNo, phase, capacity, notificationNo, healthyDtr;
    private String complaintId;
    private String sectionCode;
    ProgressDialog pDialog;
    LinearLayout ll_hdtr_details, ll_hdtr_fail_details;

    TextView tv_hdtr_fail, tv_hdtr_seril_no, tv_hdtr_Make, tv_hdtr_capacity, tv_hdtr_phase;
    Button btn_submit, btn_verify;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dtr_tracking_notification_fragment_layout, container, false);
        pDialog = new ProgressDialog(view.getContext());
        if (getArguments() != null) {
            complaintId = getArguments().getString("COMPLAINTID");
            sectionCode = getArguments().getString("SECTION_CODE");
        }
        Log.d("COMPLAINTID",complaintId);
        tv_complaintNo = view.findViewById(R.id.complaintId);
        tv_faultDtrNo = view.findViewById(R.id.faultDtrNo);
        tv_phase = view.findViewById(R.id.phase);
        tv_capacity = view.findViewById(R.id.capacity);   
        tv_notificationNo = view.findViewById(R.id.notificationNo);
        edt_healthyDtr = view.findViewById(R.id.healthyDtr);

        //Fail DTR LL
        tv_hdtr_fail = view.findViewById(R.id.tv_hdtr_fail);
        tv_hdtr_seril_no = view.findViewById(R.id.tv_hdtr_seril_no);
        tv_hdtr_Make = view.findViewById(R.id.tv_hdtr_Make);
        tv_hdtr_capacity = view.findViewById(R.id.tv_hdtr_capacity);
        tv_hdtr_phase = view.findViewById(R.id.tv_hdtr_phase);
        ll_hdtr_details = view.findViewById(R.id.ll_hdtr_suc_details);
        ll_hdtr_details.setVisibility(View.GONE);
        ll_hdtr_fail_details = view.findViewById(R.id.ll_hdtr_fail_details);
        ll_hdtr_fail_details.setVisibility(View.GONE);

        //Submit button
        btn_submit = view.findViewById(R.id.btn_submit);
        btn_verify = view.findViewById(R.id.btn_verify);

        btn_submit.setEnabled(false);
        btn_submit.setBackgroundColor(Color.GRAY);

        getDTRComplaintDetails();
        //Fail dtr edit text focus change event
//        edt_healthyDtr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//                if (!hasFocus) {
//                    healthyDtr = edt_healthyDtr.getText().toString();
//                    if(healthyDtr.length() < 5){
//                        Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the valid HDTR Number");
//                    }else {
//                        getHealthyDtrDetails();
//                    }
//                }
//
//            }
//        });
        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthyDtr = edt_healthyDtr.getText().toString();
                if(healthyDtr.length() < 5){
                    Utility.showCustomOKOnlyDialog(view.getContext(), "Please enter the valid HDTR Number");
                }else {
                    getHealthyDtrDetails();
                }
            }
        });
        //Submit Button Action Start
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                healthyDtr = edt_healthyDtr.getText().toString();
                if(healthyDtr.length() < 5){
                    Utility.showCustomOKOnlyDialog(view.getContext(), "Please enter the Healthy DTR");
                }else{
                    StringEntity entity = null;
                    try{
                        JSONObject jsonParams = new JSONObject();
//                        jsonParams.put("action", "UPDATE_HEALTHY_EQUIPMENT");
//                        jsonParams.put("COMPLAINT_NO", complaintId);
//                        jsonParams.put("HEQ_NO",healthyDtr);
//                        jsonParams.put("PHASE",phase);
//                        jsonParams.put("DTR_CAPACITY",capacity);//AppPrefs.getInstance(view.getContext()).getString("COSTCENTER","")
                        jsonParams.put("CSCNO", complaintId);
                        jsonParams.put("SECNO",  "");
                        jsonParams.put("FDATE", "");
                        jsonParams.put("FTIME", "");
                        jsonParams.put("CNAME", "");
                        jsonParams.put("CPHNE", "");
                        jsonParams.put("DSTAF", "");
                        jsonParams.put("DDESG", "");
                        jsonParams.put("DPHNE", "");
                        jsonParams.put("FEQNO", "");
                        jsonParams.put("VTYPE", "");
                        jsonParams.put("CTYPE", "");
                        jsonParams.put("TNOTE", "");
                        jsonParams.put("TDATE", "");
                        jsonParams.put("REQNO", healthyDtr);
                        jsonParams.put("RDATE", "");
                        jsonParams.put("ZZFAILURE", "");
                        jsonParams.put("PRIOK", "");
                        entity = new StringEntity(jsonParams.toString());
                        updateHealthyDtrDetails(entity);
                    }catch (Exception e){

                    }
                }
            }
        });
        //Submit Button action end
        //dtrCancleEquipments();
        return view;
    }
    private void dismissDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
    /* *
     *Get DTR Complaint Details
     * */
    private void getDTRComplaintDetails() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        client.addHeader("Content-Type", "application/json");
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(view.getContext()).getString("USER_AUTH", ""))};
        StringEntity entity = null;
        try{
            JSONObject jsonParams = new JSONObject();
            //jsonParams.put("action", "GET_DTR_COMPLAINT_DETAILS");
            jsonParams.put("ComplaintID", complaintId);
            entity = new StringEntity(jsonParams.toString());
        }catch (Exception e){

        }
        client.post(view.getContext(), ServiceConstants.USER_DTR_COMPLAINT_STATUS, headers, entity, "application/json", new AsyncHttpResponseHandler() {
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
                    if (status.equalsIgnoreCase("True")) {
                        if (responseObj != null && responseObj.length() > 0) {
                            //COMPLAINT_NO
                            if((responseObj.getString("complaintid") != null) && (!responseObj.getString("complaintid").equalsIgnoreCase(""))) {
                                tv_complaintNo.setText(responseObj.getString("complaintid"));
                                complaintNo = responseObj.getString("complaintid");
                            }else {
                                tv_complaintNo.setText("");
                                complaintNo = "";
                            }
                            //FEQNO
                            if(responseObj.getString("sickdtrno") != null && !responseObj.getString("sickdtrno").equalsIgnoreCase("")) {
                                tv_faultDtrNo.setText(responseObj.getString("sickdtrno"));
                                faultDtrNo = responseObj.getString("sickdtrno");
                            }else {
                                tv_faultDtrNo.setText("");
                                faultDtrNo = "";
                            }

                            //PHASE
                            if(responseObj.getString("sickdtrphase") != null && !responseObj.getString("sickdtrphase").equalsIgnoreCase("")) {
                                tv_phase.setText(responseObj.getString("sickdtrphase"));
                                phase = responseObj.getString("sickdtrphase");
                            }else {
                                tv_phase.setText("");
                                phase = "";
                            }

                            //DTR_CAPACITY
                            if(responseObj.getString("sickdtrcapacity") != null && !responseObj.getString("sickdtrcapacity").equalsIgnoreCase("")) {
                                tv_capacity.setText(responseObj.getString("sickdtrcapacity"));
                                capacity = responseObj.getString("sickdtrcapacity");
                            }else {
                                tv_capacity.setText("");
                                capacity = "";
                            }

                            //NOTIFICATION_NO
                            if(responseObj.getString("notificationno") != null && !responseObj.getString("notificationno").equalsIgnoreCase("")) {
                                tv_notificationNo.setText(responseObj.getString("notificationno"));
                                notificationNo = responseObj.getString("notificationno");
                            }else {
                                tv_notificationNo.setText("");
                                notificationNo = "";
                            }
                        }
                    }else {
                        Utility.showCustomOKOnlyDialog(view.getContext(), responseObj.getString("message"));
                    }
                } catch (JSONException e) {
                     dismissDialog();

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                dismissDialog();
                Utility.showCustomOKOnlyDialog(view.getContext(), error.getMessage());
            }
        });
    }
    /* *
     *GET faild dtr details
     * */


    private void getHealthyDtrDetails() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(view.getContext()).getString("USER_AUTH", ""))};
        StringEntity entity = null;
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("I_KOSTL", AppPrefs.getInstance(view.getContext()).getString("COSTCENTER",""));
            jsonParams.put("I_EQUNR", edt_healthyDtr.getText().toString());
            jsonParams.put("I_EQTYP", "H");
            jsonParams.put("I_CSCNO", complaintId);
            entity = new StringEntity(jsonParams.toString());
        }catch (Exception e){

        }
        client.post(view.getContext(), ServiceConstants.USER_DTR_CHECK_EQUER, headers,entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("MT_ZPM_CHECK_EQUNR_RES");

                    if(jsonObject != null && jsonObject.length()>0 ){
                        if(jsonObject.getString("E_ERROR").equalsIgnoreCase("OK")){
                            Utility.showLog("Ok",jsonObject.getString("E_ERROR"));
                            ll_hdtr_fail_details.setVisibility(View.GONE);
                            ll_hdtr_details.setVisibility(View.VISIBLE);
                            tv_hdtr_phase.setText(jsonObject.getString("E_PHASE"));
                            tv_hdtr_capacity.setText(jsonObject.getString("E_CAPACITY"));
                            tv_hdtr_Make.setText(jsonObject.getString("E_MAKE"));
                            tv_hdtr_seril_no.setText(jsonObject.getString("E_SERIALNO"));
                            btn_submit.setEnabled(true);
                            btn_submit.setBackgroundColor(Color.BLUE);
                        }else{
                            ll_hdtr_details.setVisibility(View.GONE);
                            btn_submit.setEnabled(false);
                            btn_submit.setBackgroundColor(Color.GRAY);
                            ll_hdtr_fail_details.setVisibility(View.VISIBLE);
                            tv_hdtr_fail.setText(jsonObject.getString("E_ERROR"));
                        }
                    }

                } catch (JSONException e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                dismissDialog();
                Utility.showCustomOKOnlyDialog(view.getContext(), error.getMessage());
            }
        });
    }
    //
    /* *
     *UPDATE_FAILED_DTR_ACKNOWLEDGEMENT
     * */
    private void updateHealthyDtrDetails(HttpEntity entity) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(view.getContext()).getString("USER_AUTH", ""))};
        client.post(view.getContext(), ServiceConstants.USER_DTR_CREATE_NOTIFICATION, headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                dismissDialog();
                try {
                    dismissDialog();
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject = jsonObject.getJSONObject("MT_ZPM_CREATE_NOTIFICATION_RES");
                    if (jsonObject != null && jsonObject.length() > 0) {
                        if(jsonObject.has("REMARKS")){
                            if (jsonObject.getString("REMARKS").equalsIgnoreCase("Success")) {
                                Utility.showLog("Ok", jsonObject.getString("REMARKS"));
                                Utility.showCustomOKOnlyDialog(view.getContext(), jsonObject.getString("REMARKS"));
                            } else {
                                Utility.showCustomOKOnlyDialog(view.getContext(), jsonObject.getString("REMARKS"));
                            }
                        }
                        dismissDialog();
                    }
                    dismissDialog();

                }  catch (JSONException e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                dismissDialog();
                Utility.showCustomOKOnlyDialog(view.getContext(), error.getMessage());
            }
        });
    }
    /* *
     *DTR CANCEL EQUIPMENTS
     * */


    private void dtrCancleEquipments() {

        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Authorization", "Basic + Constants.AUTHORIZATION_VALUE");
        StringEntity entity = null;
        try{
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("I_CSCNO", complaintId);
            jsonParams.put("I_DETAILS", "X");
            entity = new StringEntity(jsonParams.toString());
        }catch (Exception e){

        }
        client.post(view.getContext(), "http://10.64.5.24:50000/RESTAdapter/DTR_CANCEL_EQUIPMENTS",entity,"application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                dismissDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject != null && jsonObject.length()>0 ){
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("MT_ZPM_DTR_CANCEL_EQUIPMENTS_RES"));
                        if(jsonObject1 != null && jsonObject1.length()>0 ){
                            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("E_CSCDET"));
                        }
                    }

                } catch (JSONException e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                dismissDialog();
                Utility.showCustomOKOnlyDialog(view.getContext(), error.getMessage());
            }
        });
    }

}
