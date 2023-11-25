package com.apcpdcl.departmentapp.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.RequiresApi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DtrTrackingDetails extends Fragment {

    View view = null;
    private ProgressDialog pDialog;
    private String complaintId;
    TextView tv_complaint_no, tv_USCNO, tv_dtr_location, tv_caller_name, tv_caller_phone;
    EditText edt_staffName, edt_staffPhone, edt_fDtr, edt_tNoteNo, edt_tNoteDate;
    Spinner sp_designation, sp_vehicalType, sp_priority, sp_dtrPurpose, spn_staff_names;
    private String designation, vehicalType, priority, dtrPurpose, staffName, staffId, staffPhone, fDtr, tNoteNo, tNoteDate;
    private String complaintNo = "";
    private String USCNO = "";
    private String dtrLocation = "";
    private String callerName = "";
    private String callerPhone = "";
    private String sectionCode, costCenterCode;
    LinearLayout ll_fdtr_details, ll_fdtr_fail_details;

    TextView tv_fail, tv_seril_no, tv_Make, tv_capacity, tv_phase;
    Button btn_submit, btn_verify;
    private String sFDate, sFTime;

    private List<String> listDesignation = new ArrayList<>();
    HashMap<String, String> hashMapStaffData = new HashMap<>();
    HashMap<String, String> hashMapStaffDeisg = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.dtr_tracking_details_fragment_layout, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.show();
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        if (getArguments() != null) {
            complaintId = getArguments().getString("COMPLAINTID");
            sectionCode = getArguments().getString("SECTION_CODE");
            costCenterCode = getArguments().getString("COSTCENTER");
        }
        Log.d("COMPLAINTID", complaintId);
        tv_complaint_no = view.findViewById(R.id.tv_complaint_no);
        tv_USCNO = view.findViewById(R.id.tv_USCNO);
        tv_dtr_location = view.findViewById(R.id.tv_dtr_location);
        tv_caller_name = view.findViewById(R.id.tv_caller_name);
        tv_caller_phone = view.findViewById(R.id.tv_caller_phone);
        edt_staffName = view.findViewById(R.id.staffName);
        edt_staffPhone = view.findViewById(R.id.staffPhone);
        edt_fDtr = view.findViewById(R.id.fDtr);
        edt_tNoteNo = view.findViewById(R.id.tNoteNo);
        edt_tNoteDate = view.findViewById(R.id.tNoteDate);

        //Spiners
        sp_designation = view.findViewById(R.id.designation);
        sp_vehicalType = view.findViewById(R.id.vehicalType);
        sp_priority = view.findViewById(R.id.priority);
        sp_dtrPurpose = view.findViewById(R.id.dtrPurpose);
        spn_staff_names = view.findViewById(R.id.spn_staff_names);

        //Fail DTR LL
        tv_fail = view.findViewById(R.id.tv_fail);
        tv_seril_no = view.findViewById(R.id.tv_seril_no);
        tv_Make = view.findViewById(R.id.tv_Make);
        tv_capacity = view.findViewById(R.id.tv_capacity);
        tv_phase = view.findViewById(R.id.tv_phase);
        ll_fdtr_details = view.findViewById(R.id.ll_fdtr_suc_details);
        ll_fdtr_details.setVisibility(View.GONE);
        ll_fdtr_fail_details = view.findViewById(R.id.ll_fdtr_fail_details);
        ll_fdtr_fail_details.setVisibility(View.GONE);

        //Submit button
        btn_submit = view.findViewById(R.id.btn_submit);
        btn_verify = view.findViewById(R.id.btn_verify);

        btn_submit.setEnabled(false);
        btn_submit.setBackgroundColor(Color.GRAY);

        //Fail dtr edit text focus change event
//        edt_fDtr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
////                    Log.d("CHANGE","CHANGE");
////                    ll_fdtr_details.setVisibility(View.VISIBLE);
////                    btn_submit.setVisibility(View.VISIBLE);
//                    fDtr = edt_fDtr.getText().toString();
//                    if(fDtr.length() != 10){
//                        Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the valid FDTR Number");
//                    }else {
//                        //getFailedDtrDetails();
//                    }
//                }
//            }
//        });

        //Data Binding
        getDTRComplaintDetails();
        //dataBindDesignation();
        dataBindDtrPurpose();
        dataBindPriority();
        dataBindvehicalType();


        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                // edittext.setText(sdf.format(myCalendar.getTime()));
                edt_tNoteDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        edt_tNoteDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(getActivity(), date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return true;
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CHANGE","CHANGE");
                //ll_fdtr_details.setVisibility(View.VISIBLE);
                //btn_submit.setVisibility(View.VISIBLE);
                fDtr = edt_fDtr.getText().toString();
                if(fDtr.length() < 5){
                    Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the valid FDTR Number");
                }else {
                    getFailedDtrDetails();
                }
            }
        });
        //Submit Button Action Start
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //staffName = edt_staffName.getText().toString();
                staffPhone = edt_staffPhone.getText().toString();
                fDtr = edt_fDtr.getText().toString();
                tNoteNo = edt_tNoteNo.getText().toString();
                tNoteDate = edt_tNoteDate.getText().toString();
                if (staffName.equalsIgnoreCase("")) {
                    Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the Staff Name");
                } else if (staffPhone.length() != 10) {
                    Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the valid Staff Phone Number");
                }
//                else if (fDtr.length() != 10) {
//                    Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the valid FDTR Number");
//                }
                else if (tNoteNo.length() >= 3 && tNoteNo.length() <= 10) {
                    Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the valid T Note Number");
                } else if (tNoteDate.equalsIgnoreCase("")) {
                    Utility.showCustomOKOnlyDialog(getActivity(), "Please enter the T Note Date");
                } else {
                    StringEntity entity = null;
                    try {
                        JSONObject requestObj = new JSONObject();
//                        JSONObject jsonParams = new JSONObject();
//                        JSONObject requestObj = new JSONObject();
//                        requestObj.put("action", "UPDATE_FAILED_DTR_ACKNOWLEDGEMENT");
//                        jsonParams.put("COMPLAINT_NO", complaintId);
//                        jsonParams.put("FEQNO",fDtr);
//                        jsonParams.put("STAFF_NAME",staffName);
//                        jsonParams.put("STAFF_PHONE",staffPhone);
//                        jsonParams.put("STAFF_DESIGNATION",designation);
//                        jsonParams.put("PHASE",tv_phase.getText().toString());
//                        jsonParams.put("TNOTE_NO",tNoteNo);
//                        jsonParams.put("VEHICLE_TYPE",vehicalType);
//                        jsonParams.put("DTR_PURPOSE",dtrPurpose);
//                        jsonParams.put("PRIORITY",priority);
//                        jsonParams.put("DTR_CAPACITY",tv_capacity.getText().toString());
//                        jsonParams.put("TNOTE_DATE",tNoteDate);
//                        jsonParams.put("NOTIFICATION_NO","");

//                        requestObj.put("CSCNO", complaintId);
//                        requestObj.put("SECNO", sectionCode);
//                        requestObj.put("FDATE", sFDate);
//                        requestObj.put("FTIME", sFTime);
//                        requestObj.put("CNAME", tv_caller_name.getText().toString());
//                        requestObj.put("CPHNE", tv_caller_phone.getText().toString());
//                        requestObj.put("DSTAF", staffName);
//                        requestObj.put("DDESG", designation);
//                        requestObj.put("DPHNE", staffPhone);
//                        requestObj.put("FEQNO", fDtr);
//                        requestObj.put("VTYPE", vehicalType);
//                        requestObj.put("CTYPE", dtrPurpose);
//                        requestObj.put("TNOTE", tNoteNo);
//                        requestObj.put("TDATE", tNoteDate);
//                        requestObj.put("REQNO", "0");
//                        requestObj.put("RDATE", "2021-05-24");
//                        requestObj.put("ZZFAILURE", "NON-THEFT");
//                        requestObj.put("PRIOK", priority);

                        createDTRNotification();
                        //updateFailedDtrDetails(entity);
                    } catch (Exception e) {

                    }
                }
            }
        });
        //Submit Button action end
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
        AsyncHttpClient client = new AsyncHttpClient();
//        client.setTimeout(50000);
//        client.addHeader("Content-Type", "application/json");
//        client.addHeader("access_token", Constants.ACCESS_TOKEN_VALUE);

        StringEntity entity = null;
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(view.getContext()).getString("USER_AUTH", ""))};
        try {
            JSONObject jsonParams = new JSONObject();
            //jsonParams.put("action", "GET_DTR_COMPLAINT_DETAILS");
            jsonParams.put("ComplaintID", complaintId);
            entity = new StringEntity(jsonParams.toString());

        } catch (Exception e) {
            dismissDialog();
        }
        client.post(getActivity(), ServiceConstants.USER_DTR_COMPLAINT_STATUS, headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                Utility.showLog("onSuccess", responseStr);
                try {
                    dismissDialog();
                    JSONObject responseObj = new JSONObject(responseStr);
                    responseObj = responseObj.getJSONObject("response");
                    String status = responseObj.getString("success");
                    if (status.equalsIgnoreCase("True")) {
                        getStaffDetails();

                        if (responseObj != null && responseObj.length() > 0) {
                            //Complaint ID
                            if ((responseObj.getString("complaintid") != null) && (!responseObj.getString("complaintid").equalsIgnoreCase("null"))) {
                                tv_complaint_no.setText(responseObj.getString("complaintid"));
                                complaintNo = responseObj.getString("complaintid");
                            } else {
                                tv_complaint_no.setText("");
                                complaintNo = "";
                            }
                            //USCNO
                            if (responseObj.getString("bpid") != null && !responseObj.getString("bpid").equalsIgnoreCase("null")) {
                                tv_USCNO.setText(responseObj.getString("bpid"));
                                USCNO = responseObj.getString("bpid");
                            } else {
                                tv_USCNO.setText("");
                                USCNO = "";
                            }

                            //DTR_LOCATION
                            if (responseObj.getString("consumerdetails") != null && !responseObj.getString("consumerdetails").equalsIgnoreCase("null")) {
                                tv_dtr_location.setText(responseObj.getString("consumerdetails"));
                                dtrLocation = responseObj.getString("consumerdetails");
                            } else {
                                tv_dtr_location.setText("");
                                dtrLocation = "";
                            }

                            //CALLER_NAME
                            if (responseObj.getString("consumername") != null && !responseObj.getString("consumername").equalsIgnoreCase("null")) {
                                tv_caller_name.setText(responseObj.getString("consumername"));
                                callerName = responseObj.getString("consumername");
                            } else {
                                tv_caller_name.setText("");
                                callerName = "";
                            }
                            //getStaffDetails();

                            //CALLER_PHONE
                            if (responseObj.getString("phonenumber") != null && !responseObj.getString("phonenumber").equalsIgnoreCase("null")) {
                                tv_caller_phone.setText(responseObj.getString("phonenumber"));
                                callerPhone = responseObj.getString("phonenumber");
                            } else {
                                tv_caller_phone.setText("");
                                callerPhone = "";
                            }

                            if (responseObj.getString("registereddate").length() > 0) {
                                String[] splited = responseObj.getString("registereddate").toString().split("\\s+");
                                sFDate = splited[0];
                                sFTime = splited[1];
                            }
                            dismissDialog();
                        } else {
                            dismissDialog();
                        }

                    } else {
                        dismissDialog();
                        Utility.showCustomOKOnlyDialog(getActivity(), responseObj.getString("message"));
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
                Utility.showCustomOKOnlyDialog(getActivity(), error.getMessage());
            }
        });
    }

    private void getStaffDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        StringEntity entity = null;
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(view.getContext()).getString("USER_AUTH", ""))};
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("ansvh", "07");
            jsonParams.put("section_code", sectionCode);
            entity = new StringEntity(jsonParams.toString());
        } catch (Exception e) {
            dismissDialog();
        }
        client.post(getActivity(), ServiceConstants.DTRF4HELP, headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                Utility.showLog("onSuccess", responseStr);
                try {
                    dismissDialog();
                    JSONObject responseObj = new JSONObject(responseStr);
                    responseObj = responseObj.getJSONObject("response");
                    //responseObj = responseObj.getJSONObject("ETY_SECTION");
                    String status = responseObj.getString("success");
                    if (status.equalsIgnoreCase("True")) {
                        JSONObject f4helpDataObj = responseObj.getJSONObject("ATN_SECTION_TO_USER_NAV");
                        AppPrefs.getInstance(view.getContext()).putString("DTR_F4HELP", f4helpDataObj.toString());

                        //Get ORGDETAILS  Arr obj in RESPONSE
                        JSONArray resultDataArrObj = f4helpDataObj.getJSONArray("ETY_USERS");
                        if (resultDataArrObj != null && resultDataArrObj.length() > 0) {
                            hashMapStaffData = new HashMap<>();
                            JSONObject sigleJsonObj = new JSONObject();
                            for (int i = 0; i < resultDataArrObj.length(); i++) {
                                sigleJsonObj = resultDataArrObj.getJSONObject(i);

                                hashMapStaffData.put(sigleJsonObj.optString("ename"), sigleJsonObj.optString("user_name"));
                            }
                            dataBindStaffNames(hashMapStaffData);

                        }
                    } else {

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
                Utility.showCustomOKOnlyDialog(getActivity(), error.getMessage());
            }
        });
    }

    //Data bind Staff Names dropdown
    private void dataBindStaffNames(HashMap<String, String> hashMapStaffData) {
        final ArrayList<String> list = new ArrayList<>();
        list.add(0, "--Select--");

        for (String name : hashMapStaffData.keySet()) {
            list.add(name);
            System.out.println("Key = " + name);
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        spn_staff_names.setAdapter(newlineAdapter);
        spn_staff_names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position == 0) {
                    staffId = "";
                    staffName = "";
                } else {
                    staffName = list.get(position);
                    staffId = hashMapStaffData.get(staffName);
                    dataBindDesignation(staffId);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Data bind Designation dropdown
    private void dataBindDesignation(String staffUserId) {
        final ArrayList<String> list = new ArrayList<>();
        hashMapStaffDeisg = new HashMap<>();
        list.add(0, "--Select--");
        String f4helpData = AppPrefs.getInstance(view.getContext()).getString("DTR_F4HELP", "");
        try {
            //SP string to Object
            JSONObject f4helpDataObj = new JSONObject(f4helpData);
            //Get ORGDETAILS  Arr obj in RESPONSE
            JSONArray resultDataArrObj = f4helpDataObj.getJSONArray("ETY_USERS");
            if (resultDataArrObj != null && resultDataArrObj.length() > 0) {
                JSONObject sigleJsonObj = new JSONObject();
                for (int i = 0; i < resultDataArrObj.length(); i++) {
                    sigleJsonObj = resultDataArrObj.getJSONObject(i);
                    if (sigleJsonObj.optString("user_name").equals(staffUserId)) {
                        hashMapStaffDeisg.put(sigleJsonObj.optString("designation"), sigleJsonObj.optString("user_name"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        list.add("FM");
//        list.add("SLI");
//        list.add("LI");
//        list.add("LM");
//        list.add("ALM");
//        list.add("JLM");
        for (String name : hashMapStaffDeisg.keySet()) {
            list.add(name);
            System.out.println("Key = " + name);
        }

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        sp_designation.setAdapter(newlineAdapter);
        sp_designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position == 0) {
                    designation = "";
                } else {
                    designation = list.get(position);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Data bind Vehical Type dropdown
    private void dataBindvehicalType() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("Department");
        list.add("Private");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        sp_vehicalType.setAdapter(newlineAdapter);
        sp_vehicalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
//                if (position != 0) {
//                    vehicalType = parent.getItemAtPosition(position).toString();
//                } else {
//                    vehicalType = "";
//                }
                vehicalType = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Data bind Priority dropdown
    private void dataBindPriority() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("RGP");
        list.add("WGP");
        list.add("BGP");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        sp_priority.setAdapter(newlineAdapter);
        sp_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
//                if (position != 0) {
//                    priority = parent.getItemAtPosition(position).toString();
//                } else {
//                    priority = "";
//                }
                priority = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //Data bind DtrPurpose dropdown
    private void dataBindDtrPurpose() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("Rural_Lightning");
        list.add("Rural_Agl");
        list.add("Rural_MIxted");
        list.add("Rural_Industrial");
        list.add("Urban_Industrial");
        list.add("Urban_Lightning");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        sp_dtrPurpose.setAdapter(newlineAdapter);
        sp_dtrPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
//                if (position != 0) {
//                    dtrPurpose = parent.getItemAtPosition(position).toString();
//                } else {
//                    dtrPurpose = "";
//                }
                dtrPurpose = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *
     *GET failed dtr details form Sap
     * */


    private void getFailedDtrDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        StringEntity entity = null;
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(view.getContext()).getString("USER_AUTH", ""))};
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("I_KOSTL", AppPrefs.getInstance(view.getContext()).getString("COSTCENTER",""));
            jsonParams.put("I_EQUNR", edt_fDtr.getText().toString());//edt_fDtr.getText().toString()
            jsonParams.put("I_EQTYP", "F");
            jsonParams.put("I_CSCNO", complaintId);
            entity = new StringEntity(jsonParams.toString());
        } catch (Exception e) {

        }
        client.post(getActivity(), ServiceConstants.USER_DTR_CHECK_EQUER, headers,entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                try {
                    JSONObject responseObj = new JSONObject(response);
                    responseObj = responseObj.getJSONObject("MT_ZPM_CHECK_EQUNR_RES");

                    if (responseObj != null && responseObj.length() > 0) {
                        if (responseObj.getString("E_ERROR").equalsIgnoreCase("OK")) {
                            Utility.showLog("Ok", responseObj.getString("E_ERROR"));
                            ll_fdtr_fail_details.setVisibility(View.GONE);
                            ll_fdtr_details.setVisibility(View.VISIBLE);
                            tv_phase.setText(responseObj.getString("E_PHASE"));
                            tv_capacity.setText(responseObj.getString("E_CAPACITY"));
                            tv_Make.setText(responseObj.getString("E_MAKE"));
                            tv_seril_no.setText(responseObj.getString("E_SERIALNO"));
                            btn_submit.setEnabled(true);
                            btn_submit.setBackgroundColor(Color.BLUE);
                        } else {
                            ll_fdtr_details.setVisibility(View.GONE);
                            btn_submit.setEnabled(false);
                            btn_submit.setBackgroundColor(Color.GRAY);
                            btn_submit.setEnabled(true);
                            btn_submit.setBackgroundColor(Color.BLUE);
                            ll_fdtr_fail_details.setVisibility(View.VISIBLE);
                            tv_fail.setText(responseObj.getString("E_ERROR"));
                        }
                    }

                } catch (JSONException e) {
                    // dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                //dismissDialog();
                Utility.showCustomOKOnlyDialog(getActivity(), error.getMessage());
            }
        });
    }

    //
    /* *
     *UPDATE_FAILED_DTR_ACKNOWLEDGEMENT
     * */
    private void updateFailedDtrDetails(HttpEntity entity) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        client.addHeader("Content-Type", "application/json");
//        client.addHeader("access_token", Constants.ACCESS_TOKEN_VALUE);

        client.post(getActivity(), "Constants.DTR_URL", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("MESSAGE").equalsIgnoreCase("Successfully updated Failed DTR Acknowledgement")) {
                        if (jsonObject != null && jsonObject.has("DATA")) {
                            JSONObject dataObj = jsonObject.getJSONObject("DATA");
                            if (dataObj != null && dataObj.length() > 0) {
                                //Utility.showCustomOKOnlyDialog(getActivity(), jsonObject.getString("MESSAGE"));
//                                Intent intent = new Intent(getActivity(), DTRTrackingActivity.class);
//                                startActivity(intent);
                                createDTRNotification();
                            } else {

                            }
                        }
                    } else {
                        Utility.showCustomOKOnlyDialog(getActivity(), jsonObject.getString("MESSAGE"));
                    }

                } catch (JSONException e) {
                    // dismissDialog();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                //dismissDialog();
                Utility.showCustomOKOnlyDialog(getActivity(), error.getMessage());
            }
        });
    }

    /* *
     *CREATE_NOTIFICATION
     * */


    private void createDTRNotification() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(view.getContext()).getString("USER_AUTH", ""))};
        StringEntity entity = null;
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("CSCNO", complaintId);
            jsonParams.put("SECNO",  AppPrefs.getInstance(view.getContext()).getString("COSTCENTER",""));
            jsonParams.put("FDATE", sFDate);
            jsonParams.put("FTIME", sFTime);
            jsonParams.put("CNAME", callerName);
            jsonParams.put("CPHNE", callerPhone);
            jsonParams.put("DSTAF", staffName);
            jsonParams.put("DDESG", designation);
            jsonParams.put("DPHNE", staffPhone);
            jsonParams.put("FEQNO", fDtr);
            jsonParams.put("VTYPE", vehicalType);
            jsonParams.put("CTYPE", dtrPurpose);
            jsonParams.put("TNOTE", tNoteNo);
            jsonParams.put("TDATE", tNoteDate);
            jsonParams.put("REQNO", "");
            jsonParams.put("RDATE", "");
            jsonParams.put("ZZFAILURE", "");
            jsonParams.put("PRIOK", priority);
            entity = new StringEntity(jsonParams.toString());
            entity = new StringEntity(jsonParams.toString());
        } catch (Exception e) {

        }
        client.post(getActivity(), ServiceConstants.USER_DTR_CREATE_NOTIFICATION, headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
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
