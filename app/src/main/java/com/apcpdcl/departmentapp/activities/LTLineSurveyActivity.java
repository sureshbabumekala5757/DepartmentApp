package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.models.FeedersModel;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LTLineSurveyActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.spn_feeder)
    Spinner spn_feeder;
    @BindView(R.id.spn_no_ckt)
    Spinner spn_no_ckt;
    @BindView(R.id.spn_code)
    Spinner spn_code;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private String mFeeder = "";
    private String mCkt = "";
    private String strFeederCode = "";
    private String strSelectFeederCode = "";
    private String strStructureCode = "";
    private String subCode = "";
    private String sectionCode = "";
    private String userID = "";
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    ArrayList<FeedersModel> feederCodeList = new ArrayList<FeedersModel>();
    ArrayList<String> structureList = new ArrayList<String>();
    private String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lt_line_survey_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra(Constants.FROM);
        if (from.contains("11")) {
            toolbar_title.setText("11KV LINE SURVEY");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        pDialog = new ProgressDialog(this);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        userID = prefs.getString("UserName", "");
        if (objNetworkReceiver.hasInternetConnection(LTLineSurveyActivity.this)) {
            invoke11kvFeederService();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }
        setCircuitSpinnerData();
    }

    @OnClick(R.id.btn_submit)
    void navigateToNextScreen() {
        if (validateFields()) {
            Intent intent = new Intent(this, LTLineSurveyDetailActivity.class);
            intent.putExtra("structurecode", strStructureCode);
            intent.putExtra("SubStationCode", strFeederCode);
            intent.putExtra("Ckts", mCkt);
            intent.putExtra(Constants.FROM, from);
            startActivity(intent);
            finish();
        }
    }

    /* *Set No of Circuit Spinner Data**/
    private void setCircuitSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_no_ckt.setAdapter(newlineAdapter);
        spn_no_ckt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mCkt = parent.getItemAtPosition(position).toString();
                } else {
                    mCkt = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set 11kv Feeder Spinner Data**/
    private void set11kvFeederSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        // list.add("--Select--");
        for (int i = 0; i < feederCodeList.size(); i++) {
            list.add(feederCodeList.get(i).getCode() + " - " + feederCodeList.get(i).getName());
        }

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_feeder.setAdapter(newlineAdapter);
        spn_feeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strFeederCode = feederCodeList.get(position).getSubcode();
                if (objNetworkReceiver.hasInternetConnection(LTLineSurveyActivity.this)) {
                    invokestructureDataService(feederCodeList.get(position).getCode());
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Structure Code Spinner Data**/
    private void setStructureCodeSpinnerData() {

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(LTLineSurveyActivity.this,
                R.layout.spinner_item_layout, structureList);
        spn_code.setAdapter(newlineAdapter);
        spn_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strStructureCode = parent.getItemAtPosition(position).toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void invoke11kvFeederService() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(Constants.URL+"pmi11kvservice/feedersinfo?seccd=" + sectionCode, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                feederCodeList.clear();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) obj.get("feeders");
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            FeedersModel feedersModel = new Gson().fromJson(jsonObject.toString(),
                                    FeedersModel.class);
                            feederCodeList.add(feedersModel);
                        }
                        set11kvFeederSpinnerData();
                    } else {
                        String str_msg = obj.getString("error");
                        Utility.showCustomOKOnlyDialog(LTLineSurveyActivity.this, str_msg);
                    }

                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
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
                Utility.showCustomOKOnlyDialog(LTLineSurveyActivity.this, "No Records Found");
                Utility.showLog("error", error.toString());
            }
        });

    }


    private void invokestructureDataService(String strSelectFeederCode) {

        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(Constants.URL+"pmi11kvservice/dtrsinfo?feedercode=" + strSelectFeederCode + "&seccd=" + sectionCode, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                structureList.clear();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }

                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) obj.get("dtrs");
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String strFeederName = jsonObject.getString("name");
                            String strFeederLoc = jsonObject.getString("location");
                            String strFeed = strFeederName + "" + "-" + "" + strFeederLoc;
                            structureList.add(strFeed);
                        }
                        // structureList.set(0, "Select");
                        setStructureCodeSpinnerData();

                    } else {
                        String str_msg = obj.getString("error");
                        Utility.showCustomOKOnlyDialog(LTLineSurveyActivity.this, str_msg);
                    }


                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
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
                Utility.showCustomOKOnlyDialog(LTLineSurveyActivity.this, "No Records Found");
                Utility.showLog("error", error.toString());
            }
        });
    }

    /*    Validate Mandatory Fields*/
    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(strFeederCode)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select 11KV Feeder");
            return false;
        } else if (Utility.isValueNullOrEmpty(strStructureCode)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Structure Code");
            return false;
        } else if (Utility.isValueNullOrEmpty(mCkt)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Number of Circuit");
            return false;
        }
        return true;
    }


}
