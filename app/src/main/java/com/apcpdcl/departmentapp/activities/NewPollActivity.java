package com.apcpdcl.departmentapp.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 15-12-2018.
 */

public class NewPollActivity extends AppCompatActivity {


    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.et_to_date)
    EditText et_to_date;

    @BindView(R.id.et_polling)
    EditText et_polling;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    private ProgressDialog pDialog;


    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private boolean isRestore = false;
    private String userId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poll);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();

    }

    private void init() {
        pDialog = new ProgressDialog(this);
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        userId = prefs.getString("UserName", "");
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
    }

    @OnClick(R.id.et_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();

    }

    @OnClick(R.id.btn_submit)
    void postDAta() {
        if (Utility.isNetworkAvailable(this)) {
            if (validateFields()) {
                postQuestion(getJSON());
            }

        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }

    }

    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select From Date.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_to_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Select To Date.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_polling.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Question.");
            return false;
        }
        return true;
    }

    private JSONObject getJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("APPUSER", userId);
            jsonObject.put("QUESTION", et_polling.getText().toString());
            jsonObject.put("FROMDATE", et_date.getText().toString());
            jsonObject.put("TODATE", et_to_date.getText().toString());
            jsonObject.put("IMEI", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @OnClick(R.id.et_to_date)
    void openRestoreDatePicker() {
        if (Utility.isValueNullOrEmpty(et_date.getText().toString())) {
            Utility.showToastMessage(this,
                    "Please Select From Date first");
        } else {
            isRestore = true;
            final Calendar c = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH), c.get(Calendar.DATE));
            c.set(mYear, (mMonth - 1), mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();

        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            if (isRestore) {
                et_to_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1).toUpperCase() + "-" + year);
                isRestore = false;
            } else {
                et_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1).toUpperCase() + "-" + year);
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;
                et_to_date.setText("");
            }
        }

    };

    private void getDeviceId() {
       /* TelephonyManager manager = (TelephonyManager) getSystemService(GPSTrackerActivity.TELEPHONY_SERVICE);
        if (Utility.isMarshmallowOS()) {
            PackageManager pm = this.getPackageManager();
            int hasWritePerm = pm.checkPermission(
                    Manifest.permission.READ_PHONE_STATE,
                    this.getPackageName());

            if (hasWritePerm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_PHONE_STATE},
                        Constants.MY_PERMISSIONS_REQUEST_IMEI);
            } else {
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                Utility.showLog("IMEI", manager.getDeviceId());
            }
        } else {
            Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
            Utility.showLog("IMEI", manager.getDeviceId());
        }
*/
        String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER,IMEI_NUMBER);
        Utility.showLog("IMEI", IMEI_NUMBER);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_IMEI) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager manager = (TelephonyManager) getSystemService(GPSTrackerActivity.TELEPHONY_SERVICE);
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                Utility.showLog("IMEI", manager.getDeviceId());
            }
        }
    }

    /* *
     *PostQuestion
     * */
    private void postQuestion(JSONObject jsonObject) {
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("POLLINFO", jsonObject.toString());
        client.post(Constants.URL_GL+"poll/create", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("STATUS") && jsonObject.optString("STATUS").equalsIgnoreCase("SUCCESS")) {
                        Utility.showToastMessage(NewPollActivity.this, "Question Posted Successfully.");
                        Intent i = new Intent(NewPollActivity.this, NewPollActivity.class);
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(i);
                        overridePendingTransition(0, 0);
                    } else {
                        Utility.showToastMessage(NewPollActivity.this, jsonObject.optString("MSG"));
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
                Utility.showCustomOKOnlyDialog(NewPollActivity.this, error.getMessage());
            }
        });
    }
}
