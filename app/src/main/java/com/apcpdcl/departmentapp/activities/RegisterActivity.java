package com.apcpdcl.departmentapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.model.Opts;
import com.apcpdcl.departmentapp.model.PidOptions;
import com.apcpdcl.departmentapp.models.SubStationModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.tv_aadhar_num)
    TextView tv_aadhar_num;
    @BindView(R.id.tv_aadhar_name)
    TextView tv_aadhar_name;
    @BindView(R.id.tv_instruction)
    TextView tv_instruction;

    @BindView(R.id.et_aadhar_num)
    EditText et_aadhar_num;
    @BindView(R.id.et_aadhar_name)
    EditText et_aadhar_name;
    @BindView(R.id.ll_image_scroll)
    LinearLayout ll_image_scroll;

    @BindView(R.id.btn_capture)
    Button btn_capture;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.spn_sub_station)
    Spinner spn_sub_station;

    private ArrayList<String> substations = new ArrayList<>();
    private ArrayList<SubStationModel> subStationModels;
    private String ssCode = "";
    private String ssName = "";
    private ProgressDialog pDialog;
    private String sectionCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        pDialog = new ProgressDialog(RegisterActivity.this);
        if (Utility.isNetworkAvailable(this)) {
            pDialog.show();
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            getSubStations();
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this, R.string.no_internet));
        }
    }

    @OnClick(R.id.btn_capture)
    void capture() {
        try {
            String pidOption = getPIDOptions();
            if (pidOption != null) {
                Log.e("PidOptions", pidOption);
                Intent intent2 = new Intent();
                intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                intent2.putExtra("PID_OPTIONS", pidOption);
                startActivityForResult(intent2, 2);
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private String getPIDOptions() {
        try {
            int fingerCount = 1;
            int fingerType = 0;
            int fingerFormat = 0;
            String pidVer = "2.0";
            String timeOut = "50000";
            String posh = "UNKNOWN";


            Opts opts = new Opts();
            opts.fCount = String.valueOf(fingerCount);
            opts.fType = String.valueOf(fingerType);
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = String.valueOf(fingerFormat);
            opts.pidVer = pidVer;
            opts.timeout = timeOut;
//            opts.otp = "123456";
//            opts.wadh = "Hello";
            opts.posh = posh;
            opts.env = "P";
            opts.wadh = "rhVuL7SnJi2W2UmsyukVqY7c93JWyL9O/kVKgdNMfv8=";

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }

    /*
     *Get SUB-STATIONS
     * */
    private void getSubStations() {
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("Url", Constants.URL + Constants.SUBSTATION_LIST);
        Utility.showLog("sectionCode", sectionCode);
        RequestParams params = new RequestParams();
        params.put("SECTION", sectionCode);
        client.setTimeout(50000);
        client.post(Constants.URL + Constants.EMP_SUBSTATION_LIST, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("SSLIST")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("SSLIST");
                        subStationModels = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.optJSONObject(i);
                            SubStationModel registrationModel = new Gson().fromJson(json.toString(),
                                    SubStationModel.class);
                            subStationModels.add(registrationModel);
                        }

                        if (subStationModels.size() > 0) {
                            setSubStationSpinnerData();
                        }
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
            }
        });
    }

    @OnClick(R.id.btn_submit)
    void submitDetails() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                saveEmpDetails();
            } else {
                Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
            }
        }
    }

    /* *Set Circle Spinner data**/
    private void setSubStationSpinnerData() {
        substations.add(0, "--Select--");
        for (int i = 0; i < subStationModels.size(); i++) {
            substations.add(subStationModels.get(i).getSSNAME());
        }
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, substations);
        spn_sub_station.setAdapter(adapter);
        spn_sub_station.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ssName = "";
                    ssCode = "";
                } else {
                    ssCode = subStationModels.get(position - 1).getSSCODE();
                    ssName = subStationModels.get(position - 1).getSSNAME();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validateFields() {

        if (Utility.isValueNullOrEmpty(ssName)) {
            Utility.showCustomOKOnlyDialog(this, "Please select Sub-Station");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_aadhar_num.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Enter Aadhar Number");
            return false;
        } else if (et_aadhar_num.getText().toString().length() < 12) {
            Utility.showCustomOKOnlyDialog(this, "Enter 12 digits valid Aadhar Number");
            return false;
  /*      } else if (Utility.isValueNullOrEmpty(et_aadhar_name.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Enter Aadhar Name");
            return false;
        } else if (fingersData.size() < 3) {
            Utility.showCustomOKOnlyDialog(this, "Capture Finger Three times.");
            return false;*/
        }
        return true;
    }

    private void saveEmpDetails() {
        pDialog.show();
        String data = sectionCode + "|" + ssCode + "|" + ssName + "|" +
                et_aadhar_name.getText().toString() + "|" + et_aadhar_num.getText().toString();
        RequestParams params = new RequestParams();
        params.put("DATA", data);
        Utility.showLog("DATA", data);
        Utility.showLog("Url", Constants.URL + Constants.SAVEEMP);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.URL + Constants.SAVEEMP, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);

                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("STATUS")) {
                        if (jsonObject.optString("STATUS").equalsIgnoreCase("ERROR")) {
                            if (jsonObject.has("MSG")) {
                                Utility.showCustomOKOnlyDialog(RegisterActivity.this, jsonObject.optString("MSG"));
                            }
                        } else {
                            if (jsonObject.has("MSG")) {
                                showCustomOKOnlyDialog(jsonObject.optString("MSG"));
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Utility.showCustomOKOnlyDialog(RegisterActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(RegisterActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(RegisterActivity.this, "Check Your Internet Connection and Try Again");
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {
                            String result = data.getStringExtra("PID_DATA");
                            if (result != null) {
                                if (result.contains("errCode") && result.contains("720")) {
                                    Utility.showCustomOKOnlyDialog(RegisterActivity.this, "Device Not Connected.");
                                } else if (validateFields()) {
                                    if (Utility.isNetworkAvailable(this)) {
                                        sendData(result);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;
        }
    }

    private void sendData(String data) {
        pDialog.show();
        String URL = "http://59.144.184.168:9999/ApcpdclDepartmentApp/mobileAction/aadhaar/info";
        RequestParams params = new RequestParams();
        params.put("AADHAARINFO", data);
        params.put("UDC", "MANT0");
        params.put("UID", et_aadhar_num.getText().toString());
        Log.d("data", data);
        Log.d("URL", URL);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.d("response", response);
                //Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                if (response.contains("ERROR:")) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(RegisterActivity.this, "Aadhaar Validation Failed.");
                } else {
                    et_aadhar_name.setText(response);
                    Toast.makeText(RegisterActivity.this, "Aadhar Validation Successful.", Toast.LENGTH_SHORT).show();
                    btn_submit.performClick();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(RegisterActivity.this, "Something went wrong.Please try again later...");
            }
        });
    }
}
