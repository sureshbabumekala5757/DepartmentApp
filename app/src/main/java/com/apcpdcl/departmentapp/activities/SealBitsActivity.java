package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class SealBitsActivity extends AppCompatActivity implements View.OnClickListener {
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    //Service details
    LinearLayout whole_Layout;
    TextView tv_service_number, tv_name, tv_category, tv_phase;
    private String service_number_str, name_str, category_str, phase_str, msg;
    EditText et_distribution_code, et_scno, et_tc_seal_one, et_tc_seal_two, et_box_seal1, et_box_seal2;
    private String distribution_code_str, scno_str, tc_seal_one_str, tc_seal_two_str, box_seal1_str, box_seal2_str;
    Button submit_btn, save_btn;
    private String userName, sectionCode;

    private String str_servicenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seal_bits);
        init();

    }
    /*Initialize Views*/
    private void init() {
        pDialog = new ProgressDialog(this);
        //Text View
        tv_service_number = findViewById(R.id.tv_service_number);
        tv_name = findViewById(R.id.tv_name);
        tv_category = findViewById(R.id.tv_category);
        tv_phase = findViewById(R.id.tv_phase);
        //Edit Text
        et_distribution_code = findViewById(R.id.et_distribution_code);
        et_scno = findViewById(R.id.et_scno);
        et_tc_seal_one = findViewById(R.id.et_tc_seal_one);
        et_tc_seal_two = findViewById(R.id.et_tc_seal_two);
        et_box_seal1 = findViewById(R.id.et_box_seal1);
        et_box_seal2 = findViewById(R.id.et_box_seal2);
        //Button
        submit_btn = findViewById(R.id.submit_btn);
        save_btn = findViewById(R.id.save_btn);
        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
        userName = lprefs.getString("UserName", "");
        sectionCode = lprefs.getString("Section_Code", "");

        submit_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);

        whole_Layout = (LinearLayout) findViewById(R.id.whole_Layout);

        //Distribution code onchange event
        et_distribution_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    if (charSequence.toString().equals("1") || charSequence.toString().equals("4") || charSequence.toString().equals("9") || charSequence.toString().equals("6")) {

                    } else {
                        Utility.showCustomOKOnlyDialog(SealBitsActivity.this,
                                " Invalid Service Number");
                        et_distribution_code.setText("");
                    }
                }
                if (charSequence.length() == 5) {
                    if (charSequence.toString().equals(sectionCode)) {

                    } else {
                        Utility.showCustomOKOnlyDialog(SealBitsActivity.this,
                                " Invalid Service Number");
                        et_distribution_code.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    @Override
    public void onClick(View view) {
        //If Button click is Submit
        if (view == submit_btn) {
            String distributionCode = et_distribution_code.getText().toString().trim();
            String scNo = et_scno.getText().toString().trim();
            if (!scNo.equalsIgnoreCase("")) {
                int number = Integer.valueOf(scNo);
                scNo = String.format("%06d", number); //000001
            }
            str_servicenumber = distributionCode + scNo;
            if (Utils.IsNullOrBlank(str_servicenumber)) {
                Toast.makeText(getApplicationContext(), "Please enter Service Number", Toast.LENGTH_LONG).show();
            } else if (str_servicenumber.length() != 13) {
                Toast.makeText(getBaseContext(), "Enter 13 digit Service Number", Toast.LENGTH_LONG).show();
            } else if (objNetworkReceiver.hasInternetConnection(this)) {
                RequestParams params = new RequestParams();
                params.put("USCNO", str_servicenumber);
                invokeMeterSealFetchWS(params);
            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        }
        //If Button click is Save
        if (view == save_btn) {
            service_number_str = tv_service_number.getText().toString().trim();
            tc_seal_one_str = et_tc_seal_one.getText().toString().trim();
            tc_seal_two_str = et_tc_seal_two.getText().toString().trim();
            box_seal1_str = et_box_seal1.getText().toString().trim();
            box_seal2_str = et_box_seal2.getText().toString().trim();
            if(tc_seal_one_str.equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(), "Please Enter TC Seal One", Toast.LENGTH_LONG).show();
            }else if(tc_seal_one_str.trim().length() < 6){
                Toast.makeText(getApplicationContext(), "Please Enter TC Seal One (Minimum 6 characters)", Toast.LENGTH_LONG).show();
            }else{
                String strTCodeValue = service_number_str +"|"+tc_seal_one_str +"|"+tc_seal_two_str +"|"+ box_seal1_str +"|"+ box_seal2_str + "|" + userName;
                if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                    RequestParams params = new RequestParams();
                    params.put("DATA", strTCodeValue);
                    invokeSealBitsEntryWS(params);

                } else {
                    Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    //Get details of given service number
    private void invokeMeterSealFetchWS(RequestParams params) {

        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.GET_METERSEAL_DETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e("Errorfetch", obj.toString());
                    String status = obj.getString("STATUS");

                    if (status.equals("TRUE")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        whole_Layout.setVisibility(View.VISIBLE);

                        tv_service_number.setText(obj.getString("CMUSCNO"));
                        tv_name.setText(obj.getString("CMCNAME").trim());
                        tv_category.setText(obj.getString("CMCAT"));
                        tv_phase.setText(obj.getString("CMPHVLT"));

                        if( !obj.optString("TCseal1").equalsIgnoreCase("null") ){
                            et_tc_seal_one.setText(obj.getString("TCseal1"));
                        }else {
                            et_tc_seal_one.setText("");
                        }
                        if( !obj.optString("TCSEAL2").equalsIgnoreCase("null")){
                            et_tc_seal_two.setText(obj.getString("TCSEAL2"));
                        }else {
                            et_tc_seal_two.setText("");
                        }
                        if( !obj.optString("BOXSEAL1").equalsIgnoreCase("null")){
                            et_box_seal1.setText(obj.getString("BOXSEAL1"));
                        }else {
                            et_box_seal1.setText("");
                        }
                        if( !obj.optString("BOXSEAL2").equalsIgnoreCase("null")){
                            et_box_seal2.setText(obj.getString("BOXSEAL2"));
                        }else {
                            et_box_seal2.setText("");
                        }

                    } else if (status.equals("ERROR")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
//                        msg = obj.getString("FLAG1");
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();

                    } else if (status.equalsIgnoreCase("FALSE")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
//                        msg = obj.getString("FLAG1");
//                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();

                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    e.printStackTrace();
                    Utility.showCustomOKOnlyDialog(SealBitsActivity.this, e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(SealBitsActivity.this, error.getLocalizedMessage());
            }
        });
    }
    //End Get details of given service number

    //Entry seal bits data into DB
    private void invokeSealBitsEntryWS(RequestParams params){
        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post( Constants.SAVE_METERSEAL_DATA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");

                    if (status.equals("TRUE")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        hide_WholeLayout();
                        Toast.makeText(getApplicationContext(), "Seal Bits Details Saved Successfully.", Toast.LENGTH_LONG).show();
                    } else if (status.equals("ERROR")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        //msg = obj.getString("FLAG1");
                        Toast.makeText(getApplicationContext(), "Seal Bits Details Already Submitted / Failed to Save Seal Bits Details", Toast.LENGTH_LONG).show();

                    } else if (status.equalsIgnoreCase("FALSE")) {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        // msg = obj.getString("FLAG1");
                        Toast.makeText(getApplicationContext(), "Seal Bits Details Already Submitted / Failed to Save Seal Bits Details", Toast.LENGTH_LONG).show();

                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    e.printStackTrace();
                    Utility.showCustomOKOnlyDialog(SealBitsActivity.this, e.getLocalizedMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(SealBitsActivity.this, error.getLocalizedMessage());
            }
        });
    }
    //After SUCCESS save the data
    public void hide_WholeLayout() {
        et_scno.setText("");
        et_tc_seal_one.setText("");
        et_tc_seal_two.setText("");
        et_box_seal1.setText("");
        et_box_seal2.setText("");
        whole_Layout.setVisibility(View.GONE);
        recreate();
    }
}