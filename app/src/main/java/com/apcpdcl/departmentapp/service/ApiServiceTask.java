package com.apcpdcl.departmentapp.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class ApiServiceTask extends AsyncTask<String, Void, String> {

    private static String TAG = ApiServiceTask.class.getSimpleName();

    private Activity activity = null;
    private String strMethodType = "", strUrl = "";

    private ProgressDialog progressDialog = null;

    private ApiTaskCallBack asyncTaskCallBack;

    public ApiServiceTask(Activity activity, ApiTaskCallBack asyncTaskCallBack, String methodType, String strUrl) {
        this.activity = activity;
        this.asyncTaskCallBack = asyncTaskCallBack;
        this.strMethodType = methodType;
        this.strUrl = strUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        setMessage();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void setMessage() {
        progressDialog.setMessage("Please wait...");
    }

    @Override
    protected String doInBackground(String... strings) {

        if (strMethodType.equalsIgnoreCase("1")) {
            return new OkHttpCall(activity).postResponse(strUrl, strings[0]);
        } else {
            return new OkHttpCall(activity).postResponse(strUrl, strings[0]);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        asyncTaskCallBack.onTaskCompleted(s);
    }
}