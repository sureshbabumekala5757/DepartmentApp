package com.apcpdcl.departmentapp.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.utils.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PolesDtrsActivity extends Activity {

    Button submitbtn;
    EditText et_wbsno, et_pmorder;
    String str_wbsno, str_pmorder, sec_code;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    StringEntity entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poleslines_layout);
        init();
    }

    /*Initialize Views*/
    private void init() {

        SharedPreferences lprefs = getSharedPreferences("loginPrefs", 0);
        sec_code = lprefs.getString("Section_Code", "");

        et_wbsno = (EditText) findViewById(R.id.et_wbsno);
        et_pmorder = (EditText) findViewById(R.id.et_pmorder);
        submitbtn = (Button) findViewById(R.id.submitbtn);
        pDialog = new ProgressDialog(this);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start,
                                       int end, Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (!Character.toString(source.charAt(i)).matches("[a-zA-Z0-9-_]+")) {
                        return "";
                    }
                }
                return null;
            }
        };
        et_wbsno.setFilters(new InputFilter[]{filter});

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    str_wbsno = et_wbsno.getText().toString();
                    str_pmorder = et_pmorder.getText().toString();
                    if (Utils.IsNullOrBlank(str_wbsno) && Utils.IsNullOrBlank(str_pmorder)) {
                        Toast.makeText(getApplicationContext(), "Please enter WBS No or PM Order Value", Toast.LENGTH_LONG).show();
                    } else {

                        if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                            getPolesCount();

                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getPolesCount() {
        JSONObject json = new JSONObject();
        try {
            json.put("I_POSID", str_wbsno);
            json.put("I_AUFNR", str_pmorder);
            json.put("LV_KOSTL", sec_code);
            entity = new StringEntity(json.toString());
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        pDialog.show();
        pDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(getApplicationContext(),/*"http://122.252.251.175:8080/SAPIntgrations/JavaCodeGeeks/SapCoordnateService/Dtrcounttocsc"*/
                "http://122.252.251.175:2020/SapReport/JavaCodeGeeks/SapCoordnateService/Dtrcounttocsc", entity,
                "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Utility.showLog("response", response);
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            /*   Utility.showCustomOKOnlyDialog(PolesDtrsActivity.this, jsonObject.toString());*/
                            String str_e_DTRS = jsonObject.getString("e_DTRS");
                            String str_e_GROUP = jsonObject.getString("e_GROUP");
                            String str_e_POLES = jsonObject.getString("e_POLES");
                            String str_e_REGID = jsonObject.getString("e_REGID");
                            String str_e_REMARKS = jsonObject.getString("e_REMARKS");

                            /*Intent in = new Intent(PolesDtrsActivity.this, PolesDtrsMapActivity.class);
                            in.putExtra("poles", str_e_POLES);
                            in.putExtra("dtrs", str_e_DTRS);
                            in.putExtra("I_POSID", str_wbsno);
                            in.putExtra("I_AUFNR", str_pmorder);
                            in.putExtra("LV_KOSTL", sec_code);
                            startActivity(in);*/

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
                                Utility.showCustomOKOnlyDialog(PolesDtrsActivity.this, "Unable to Connect Server");
                                break;
                            case 500:
                                Utility.showCustomOKOnlyDialog(PolesDtrsActivity.this, "Something went wrong at server end");
                                break;
                            default:
                                Utility.showCustomOKOnlyDialog(PolesDtrsActivity.this, "Check Your Internet Connection and Try Again");
                                break;
                        }
                    }
                });
    }
}
