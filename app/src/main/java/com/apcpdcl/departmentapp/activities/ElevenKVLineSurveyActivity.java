package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ElevenKVLineSurveyActivity extends AppCompatActivity {

    @BindView(R.id.spn_feeder)
    Spinner spn_feeder;
    @BindView(R.id.spn_ss)
    Spinner spn_ss;
    @BindView(R.id.et_feeder_type)
    EditText et_feeder_type;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private String strSubstation = "";
    private String strFeeder = "";
    private String strSelectCode = "";
    private String strFeederType = "";
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    List<String> subCodeList = new ArrayList<String>();
    List<String> feederList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kv_survey_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        pDialog = new ProgressDialog(this);
        setSubstationSpinnerData();

        spn_ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strSubstation = parent.getItemAtPosition(position).toString();
                Pattern pattern = Pattern.compile("- *");
                Matcher matcher = pattern.matcher(strSubstation);
                if (matcher.find()) {
                    strSelectCode = strSubstation.substring(0, matcher.start());
                }

                if (objNetworkReceiver.hasInternetConnection(ElevenKVLineSurveyActivity.this)) {
                    invokeFeederDataService(strSelectCode);
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spn_feeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                strFeeder = parent.getItemAtPosition(position).toString();
                Pattern pattern = Pattern.compile(", *");
                Matcher matcher = pattern.matcher(strFeeder);
                if (matcher.find()) {
                    strFeederType = strFeeder.substring(matcher.end());
                    et_feeder_type.setText(strFeederType);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }


    private void setSubstationSpinnerData() {
        if (objNetworkReceiver.hasInternetConnection(ElevenKVLineSurveyActivity.this)) {
            invokeSubStationDataService();
        } else {
            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.btn_submit)
    void navigateToNextScreen() {
        Intent intent = new Intent(this, ElevenKVLineSurveyDetailActivity.class);
        startActivity(intent);
    }

    private void invokeSubStationDataService() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(Constants.URL + "pmi11kvservice/subinfo?seccd=84323", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                subCodeList.clear();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) obj.get("substations");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strSubCode = jsonObject.getString("code");
                        String strSubName = jsonObject.getString("name");
                        String strSubStn = strSubCode + "" + "-" + "" + strSubName;
                        subCodeList.add(strSubStn);
                    }
                    ArrayAdapter<String> subStationAdapter = new ArrayAdapter<String>(ElevenKVLineSurveyActivity.this, android.R.layout.simple_spinner_item, subCodeList);
                    subStationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_ss.setAdapter(subStationAdapter);

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
                Utility.showCustomOKOnlyDialog(ElevenKVLineSurveyActivity.this, Utility.getResourcesString(ElevenKVLineSurveyActivity.this, R.string.err_session));
                Utility.showLog("error", error.toString());
            }
        });

    }

    private void invokeFeederDataService(String strSelectCode) {

        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(Constants.URL + "pmi11kvservice/feedersinsub?subcode=" + strSelectCode, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                feederList.clear();
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) obj.get("feeders");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String strFeederCode = jsonObject.getString("code");
                        String strFeederName = jsonObject.getString("name");
                        String strFeederType = jsonObject.getString("type");
                        String strFeed = strFeederCode + "" + "-" + "" + strFeederName + "," + strFeederType;
                        feederList.add(strFeed);
                    }

                    ArrayAdapter<String> FeederAdapter = new ArrayAdapter<String>(ElevenKVLineSurveyActivity.this, android.R.layout.simple_spinner_item, feederList);
                    FeederAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spn_feeder.setAdapter(FeederAdapter);


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
                Utility.showCustomOKOnlyDialog(ElevenKVLineSurveyActivity.this, Utility.getResourcesString(ElevenKVLineSurveyActivity.this, R.string.err_session));
                Utility.showLog("error", error.toString());
            }
        });
    }
}
