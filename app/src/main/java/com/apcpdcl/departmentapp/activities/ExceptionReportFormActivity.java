package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.models.ExceptionReportModel;
import com.apcpdcl.departmentapp.models.ExceptionalModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExceptionReportFormActivity extends AppCompatActivity {

    @BindView(R.id.spn_year)
    Spinner spn_year;
    @BindView(R.id.spn_month)
    Spinner spn_month;
    @BindView(R.id.spn_status)
    Spinner spn_status;
    @BindView(R.id.spn_type)
    Spinner spn_type;

    @BindView(R.id.tv_year)
    TextView tv_year;
    @BindView(R.id.tv_month)
    TextView tv_month;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_type)
    TextView tv_type;

    private String mType = "";
    private String mStatus = "";
    private String mMonth = "";
    private String mYear = "";
    private String user = "";
    private int year;
    private ProgressDialog prgDialog;
    private ExceptionalModel exceptionalModel;
    private boolean mShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exception_report_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        user = prefs.getString("UserName", "");
        setAshtrikColor();
        setTypeSpinnerData();
        setStatusSpinnerData();
        setYearSpinnerData();
        setMonthSpinnerData();
        prgDialog = new ProgressDialog(this);

        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }

    /* *Set Type Spinner Data**/
    private void setTypeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Less than 2 Months");
        list.add("Less than 6 Months");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_type.setAdapter(newlineAdapter);

        spn_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position == 0) {
                    mType = "";
                } else if (position == 1) {
                    mType = "2";
                } else {
                    mType = "6";
                }
            }


            {
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Status Spinner Data**/
    private void setStatusSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("STRUCK UP");
        list.add("UNDER DISCONNECTION");
        list.add("DOOR LOCK");
        list.add("METER NOT EXISTING");
        list.add("READING NOT FURNISHED");
        list.add("NIL CONSUMPTION");
        list.add("BURNT");
        final ArrayList<String> sendlist = new ArrayList<>();
        sendlist.add("--Select--");
        sendlist.add("02");
        sendlist.add("03");
        sendlist.add("05");
        sendlist.add("06");
        sendlist.add("08");
        sendlist.add("09");
        sendlist.add("11");
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_status.setAdapter(newlineAdapter);

        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mStatus = sendlist.get(position);
                    mShow = mStatus.equalsIgnoreCase("02") || mStatus.equalsIgnoreCase("11");
                } else {
                    mStatus = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Year Spinner Data**/
    private void setYearSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("" + (year - 1));
        list.add("" + year);


        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_year.setAdapter(newlineAdapter);

        spn_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mYear = parent.getItemAtPosition(position).toString();
                } else {
                    mYear = "";
                }
                setMonthSpinnerData();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Month Spinner Data**/
    private void setMonthSpinnerData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final ArrayList<String> list = new ArrayList<>();
        String[] months = new DateFormatSymbols().getMonths();
        list.add("--Select--");
        if (mYear.equalsIgnoreCase("" + year)) {
            for (int i = 0; i < month; i++) {
                list.add(months[i].substring(0, 3));
            }
        } else {
            for (String mnt : months) {
                System.out.println("month = " + mnt);
                list.add(mnt.substring(0, 3));
            }
        }


        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_month.setAdapter(newlineAdapter);

        spn_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMonth = "" + (position);
                } else {
                    mMonth = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Red Ashtrik in Text**/
    private void setAshtrikColor() {
        tv_year.setText(Html.fromHtml("Year " + "<font color=\"#E50E0E\">" + "*" + "</font>"));
        tv_month.setText(Html.fromHtml("Month " + "<font color=\"#E50E0E\">" + "*" + "</font>"));
        tv_status.setText(Html.fromHtml("Status " + "<font color=\"#E50E0E\">" + "*" + "</font>"));
        tv_type.setText(Html.fromHtml("Type " + "<font color=\"#E50E0E\">" + "*" + "</font>"));
    }

    @OnClick(R.id.btn_submit)
    void navigateToNextScreen() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                getExceptionalReport();
            } else {
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        }
    }


    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(mYear)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Year.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mMonth)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Month.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mStatus)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Status.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mType)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Type.");
            return false;
        }
        return true;
    }


    private void getExceptionalReport() {
        prgDialog.show();
        RequestParams params = new RequestParams();
        params.put("Month", mMonth);
        params.put("Year", mYear);
        params.put("Status", mStatus);
        params.put("Type", mType);
        params.put("EROCD", user);
        Utility.showLog("params", params.toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.URL_ERO + "Exception_Report", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject sectionJSON;
                    if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("TRUE")) {

                        exceptionalModel = new ExceptionalModel();
                        if (jsonObject.has("ERO")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("ERO");
                            ArrayList<ExceptionReportModel> exceptionReportModels = new ArrayList<ExceptionReportModel>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.optJSONObject(i);
                                ExceptionReportModel registrationModel = new Gson().fromJson(json.toString(),
                                        ExceptionReportModel.class);
                                exceptionReportModels.add(registrationModel);
                            }
                            exceptionalModel.setERO(exceptionReportModels);
                        }
                        if (jsonObject.has("DIVISION")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("DIVISION");
                            ArrayList<ExceptionReportModel> exceptionReportModels = new ArrayList<ExceptionReportModel>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.optJSONObject(i);
                                ExceptionReportModel registrationModel = new Gson().fromJson(json.toString(),
                                        ExceptionReportModel.class);
                                if (jsonObject.has("SECTION")) {
                                    sectionJSON = jsonObject.getJSONObject("SECTION");
                                    if (sectionJSON.has(registrationModel.getDIV_Name())) {
                                        JSONArray jsonArray1 = sectionJSON.getJSONArray(registrationModel.getDIV_Name());
                                        ArrayList<ExceptionReportModel> exceptionReportModelArrayList = new ArrayList<ExceptionReportModel>();
                                        for (int j = 0; j < jsonArray1.length(); j++) {
                                            JSONObject object = jsonArray1.optJSONObject(j);
                                            ExceptionReportModel reportModel = new Gson().fromJson(object.toString(),
                                                    ExceptionReportModel.class);
                                            exceptionReportModelArrayList.add(reportModel);
                                        }
                                        registrationModel.setSECTION(exceptionReportModelArrayList);
                                    }

                                }
                                exceptionReportModels.add(registrationModel);
                            }
                            exceptionalModel.setDIVISION(exceptionReportModels);
                        }


                        Intent in = new Intent(ExceptionReportFormActivity.this, ExceptionReportListActivity.class);
                        in.putExtra(ExceptionReportFormActivity.class.getSimpleName(), exceptionalModel);
                        in.putExtra("Type", mType);
                        in.putExtra("Show", mShow);
                        startActivity(in);
                    } else if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("FALSE")) {
                        Utility.showCustomOKOnlyDialog(ExceptionReportFormActivity.this, "No Exceptions Found");
                    } else {
                        Utility.showCustomOKOnlyDialog(ExceptionReportFormActivity.this, "Something Went Wrong!! Please Try Again Later...");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                Utility.showLog("error", error.toString());
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
            }
        });
    }


}
