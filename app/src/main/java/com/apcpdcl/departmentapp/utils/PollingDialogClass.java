package com.apcpdcl.departmentapp.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.models.PollModel;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class PollingDialogClass {

    private ProgressDialog prgDialog;
    private String status = "";
    private String appUser = "";
    private Dialog dialog_confirm;
    private Button btn_send;
    private RadioGroup rg_status;
    private LinearLayout ll_result;
    private ProgressBar pb_yes;
    private ProgressBar pb_no;
    private TextView tv_yes_result;
    private TextView tv_no_result;
    private TextView txt_msz;
    private Context context;


    private void showPollingDialog(final PollModel pollModel) {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
        dialog_confirm = new Dialog(context);
        dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_confirm.setContentView(R.layout.dialog_polling);
        dialog_confirm.setCancelable(false);
        dialog_confirm.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
        Window window1 = dialog_confirm.getWindow();
        if (window1 != null)
            lp1.copyFrom(window1.getAttributes());
        lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
        assert window1 != null;
        window1.setAttributes(lp1);
        btn_send = (Button) dialog_confirm.findViewById(R.id.btn_send);
        rg_status = (RadioGroup) dialog_confirm.findViewById(R.id.rg_status);
        ll_result = (LinearLayout) dialog_confirm.findViewById(R.id.ll_result);
        pb_yes = (ProgressBar) dialog_confirm.findViewById(R.id.pb_yes);
        pb_no = (ProgressBar) dialog_confirm.findViewById(R.id.pb_no);
        tv_yes_result = (TextView) dialog_confirm.findViewById(R.id.tv_yes_result);
        tv_no_result = (TextView) dialog_confirm.findViewById(R.id.tv_no_result);
        rg_status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_yes) {
                    status = "YES";
                } else {
                    status = "NO";
                }
                // status = (String) ((RadioButton) findViewById(checkedId)).getText();
                Utility.showLog("status", status);
            }

        });
        txt_msz = (TextView) dialog_confirm.findViewById(R.id.txt_heading);
        txt_msz.setText(pollModel.getQUESTION());
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_result.getVisibility() == View.VISIBLE) {
                    dialog_confirm.dismiss();
                } else if (!Utility.isValueNullOrEmpty(status)) {
                    if (Utility.isNetworkAvailable(context)) {
                        sendPollingData(getJSON(pollModel, status));
                    } else {
                        Utility.showToastMessage(context, context.getResources().getString(R.string.no_internet));
                    }
                } else {
                    Utility.showToastMessage(context, "Please Select your Answer.");
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

    private JSONObject getJSON(PollModel pollModel, String status) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("IMEI", Utility.getSharedPrefStringData(context, Constants.IMEI_NUMBER));
            jsonObject.put("ANSWER", status);
            jsonObject.put("QID", pollModel.getQID());
            jsonObject.put("APPUSER", appUser);
            jsonObject.put("FROMDATE", pollModel.getFROMDATE());
            jsonObject.put("TODATE", pollModel.getTODATE());
            jsonObject.put("QUESTION", pollModel.getQUESTION());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * Polling answer
     */
    private void sendPollingData(JSONObject jsonObject) {
        prgDialog = new ProgressDialog(context);
        prgDialog.show();
        prgDialog.setMessage("Please wait ...");
        prgDialog.setCancelable(false);
        prgDialog.setCanceledOnTouchOutside(false);

        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(500000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("POLLINFO", jsonObject.toString());
        Utility.showLog("POLLINFO", jsonObject.toString());
        client.post(Constants.URL_GL+"poll/save", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        Utility.showToastMessage(context, "Posted Your Answer.");
                        dialog_confirm.dismiss();
                    } else {
                        Utility.showToastMessage(context, obj.getString("MSG"));
                    }
                   /* rg_status.setVisibility(View.GONE);
                    ll_result.setVisibility(View.VISIBLE);
                    btn_send.setText("Done");*/

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error", error.toString());
            }
        });
    }

    /**
     * GET Poll
     */
    public void getPollingData(final Context context, String appUser) {
        if (Utility.isNetworkAvailable(context)) {

            this.appUser = appUser;
            this.context = context;
            prgDialog = new ProgressDialog(context);
            prgDialog.show();
            prgDialog.setMessage("Please wait ...");
            prgDialog.setCancelable(false);
            prgDialog.setCanceledOnTouchOutside(false);

            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(500000);
            RequestParams requestParams = new RequestParams();
            requestParams.put("APPUSER", appUser);
            client.post(Constants.URL_GL+"poll/status", requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("response",response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.getString("STATUS");
                        if (status.equalsIgnoreCase("NOTEXISTS")) {
                            PollModel pollModel = new Gson().fromJson(obj.toString(), PollModel.class);
                            showPollingDialog(pollModel);
                        }else {
                            if (prgDialog != null && prgDialog.isShowing()) {
                                prgDialog.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        if (prgDialog != null && prgDialog.isShowing()) {
                            prgDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                    Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("error", error.toString());
                }
            });

        }
    }
}
