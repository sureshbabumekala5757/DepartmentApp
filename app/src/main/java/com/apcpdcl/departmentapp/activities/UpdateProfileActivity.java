package com.apcpdcl.departmentapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.ProfileModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseena
 * on 06-03-2018.
 */

public class UpdateProfileActivity extends AppCompatActivity {

    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.tv_designation)
    TextView tv_designation;
    @BindView(R.id.tv_section_name)
    TextView tv_section_name;
    @BindView(R.id.btn_update)
    Button btn_update;
    @BindView(R.id.ll_form)
    LinearLayout ll_form;

    private ProgressDialog prgDialog;
    private ProfileModel mProfileModel;
    private String lmCode = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        prgDialog = new ProgressDialog(this);

        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        lmCode = prefs.getString("LMCode", "");
        if (Utility.isNetworkAvailable(this)) {
            getData();
        } else {
            Utility.showCustomOKOnlyDialog(this, "Please Check Your Internet Connection and Try Again");
        }
    }

    @OnClick(R.id.btn_update)
    void updateData() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                postData();
            } else {
                Utility.showCustomOKOnlyDialog(this, "Please Check Your Internet Connection and Try Again");
            }
        }
    }

    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(et_name.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Name cannot be empty");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_mobile.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Mobile Number cannot be empty");
            return false;
        } else if (!et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("9") &&
                !et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("8") &&
                !et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("7") &&
                !et_mobile.getText().toString().substring(0, 1).equalsIgnoreCase("6")) {
            Utility.showCustomOKOnlyDialog(this, "Enter valid mobile number");
            return false;
        } else if (et_mobile.getText().toString().length() < 10) {
            Utility.showCustomOKOnlyDialog(this, "Enter valid mobile number");
            return false;
        }
        return true;
    }

    private void getData() {
        RequestParams params = new RequestParams();
        Log.e("LMCODE", lmCode);
        Utility.showLog("Url", Constants.URL + Constants.PROFILE);
        params.put("LMCODE", lmCode);
        prgDialog.show();
        prgDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.URL + Constants.PROFILE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);

                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("MSG")) {
                        Utility.showCustomOKOnlyDialog(UpdateProfileActivity.this, jsonObject.optString("MSG"));
                    } else {
                        mProfileModel = new Gson().fromJson(jsonObject.toString(), ProfileModel.class);
                        prePopulateData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Utility.showCustomOKOnlyDialog(UpdateProfileActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(UpdateProfileActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(UpdateProfileActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    private void prePopulateData() {
        ll_form.setVisibility(View.VISIBLE);
        tv_code.setText(lmCode);
        et_name.setText(mProfileModel.getNAME());
        et_mobile.setText(mProfileModel.getMOBILE());
        if (Utility.isValueNullOrEmpty(mProfileModel.getDESIGNATION())){
            tv_designation.setText("NA");
        }else {
            tv_designation.setText(mProfileModel.getDESIGNATION());
        }
        tv_section_name.setText(mProfileModel.getSECTION());
    }

    private void postData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("LMCODE", lmCode);
            jsonObject.put("NAME", et_name.getText().toString());
            jsonObject.put("MOBILE", et_mobile.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestParams params = new RequestParams();
        Log.e("PROFILE", jsonObject.toString());
        Utility.showLog("Url", Constants.URL + Constants.UPDATE_PROFILE);
        params.put("PROFILE", jsonObject.toString());
        prgDialog.show();
        prgDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Constants.URL + Constants.UPDATE_PROFILE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);

                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = "";
                    if (jsonObject.has("STATUS")) {
                        status = jsonObject.optString("STATUS");
                    }
                    if (jsonObject.has("MSG")) {
                        showCustomOKOnlyDialog(jsonObject.optString("MSG"), status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                switch (statusCode) {
                    case 404:
                        Utility.showCustomOKOnlyDialog(UpdateProfileActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(UpdateProfileActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(UpdateProfileActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    private void showCustomOKOnlyDialog(String message, final String status) {
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
            Button btn_ok =(Button) dialog_confirm.findViewById(R.id.btn_ok);

            TextView txt_msz = (TextView)dialog_confirm.findViewById(R.id.txt_heading);
            txt_msz.setText(message);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_confirm.dismiss();
                    if (!status.equalsIgnoreCase("Fail")) {
                        finish();
                    }

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

}
