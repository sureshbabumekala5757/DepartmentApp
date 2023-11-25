package com.apcpdcl.departmentapp.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.apcpdcl.departmentapp.models.PoleUploadDataModel;
import com.apcpdcl.departmentapp.sqlite.PoleDataDataBaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadService extends IntentService {

    private static String TAG = UploadService.class.getSimpleName();

    public static final String ACTION_UPLOAD = "com.myapp.action.UPLOAD";
    public static final String BROADCAST_ACTION = "com.apcpdcl.departmentapp.services.ACTION";

    public static boolean isIntentServiceRunning = false;

    private Context context;
    private PoleDataDataBaseHandler poleDBHandler;
    private List<PoleUploadDataModel> poleUploadDataList = null;
    //
    private String strResult = "";
    private PoleUploadDataModel poleUploadData = null;
    private JSONObject requestJson = null;
    private JSONObject responseJson = null;
    private String finalPoleData;
    private String disCode, dtrCode, poleCode;
    private Iterator<PoleUploadDataModel> iterator = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadService(String name) {
        super(name);
    }

    public UploadService() {
        super("my_intent_thread");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        poleDBHandler = new PoleDataDataBaseHandler(context);
        Log.d(TAG, "Service started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isIntentServiceRunning = false;
        log("Service destroyed");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        isIntentServiceRunning = true;
        log("Service running");
        sendDataToService();
    }

    private void sendDataToService() {
        isIntentServiceRunning = true;
        log("sendDataToService");
        try {
            poleUploadDataList = poleDBHandler.getAllPoleDAta();
            if (poleUploadDataList != null) {
                log("services to upload " + poleUploadDataList.size());
                iterator = poleUploadDataList.iterator();
                while (iterator.hasNext()) {
                    poleUploadData = iterator.next();
                    if (poleUploadData != null) {
                        log(poleUploadData.getsPoleCode() + " is uploading");

                        //requestJson = new JSONObject(poleUploadData.getsPoleUscnodata());
                        finalPoleData = poleUploadData.getsPoleUscnodata();
                        poleCode = poleUploadData.getsPoleCode();
                        disCode = poleUploadData.getsDistributionCode();
                        dtrCode = poleUploadData.getsDtrCode();
                        RequestParams params = new RequestParams();
                        params.put("DATA", finalPoleData);
                        String resVal = uploadPoleData("DATA=" + finalPoleData);
                        try {
                            JSONObject obj = new JSONObject(resVal);
                            String status = obj.getString("STATUS");

                            if (status.equals("TURE")) {
                                //Toast.makeText(getApplicationContext(), "The Pole Data Saved Successfully.", Toast.LENGTH_LONG).show();
                                poleDBHandler.updateStatus(dtrCode, poleCode, "1");
                            } else {

                                //Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(TAG, "exception " + e.getLocalizedMessage());
                        }

                    }
                }
            } else {
                isIntentServiceRunning = false;
                Log.d(TAG, "service list null");
                stopSelf();
            }
        } catch (Exception e) {
            log("exception " + e.getLocalizedMessage());
            stopSelf();
        }
    }

    //Upload Data
    private String uploadPoleData(String params) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, params);
        Request request = new Request.Builder()
                .url(Constants.URL+"PoleDetails/savePole")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void uploadPoleData1(RequestParams params) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.post(Constants.URL+"PoleDetails/savePole", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("STATUS");

                    if (status.equals("TRUE")) {
                        Toast.makeText(getApplicationContext(), "Seal Bits Details Saved Successfully.", Toast.LENGTH_LONG).show();
                        poleDBHandler.updateStatus(dtrCode, poleCode, "1");
                    } else if (status.equals("ERROR")) {

                        Toast.makeText(getApplicationContext(), "Seal Bits Details Already Submitted / Failed to Save Seal Bits Details", Toast.LENGTH_LONG).show();

                    } else if (status.equalsIgnoreCase("FALSE")) {

                        Toast.makeText(getApplicationContext(), "Seal Bits Details Already Submitted / Failed to Save Seal Bits Details", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(getApplicationContext(), "Please check your Details", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    Utility.showCustomOKOnlyDialog(context, e.getLocalizedMessage());
                    Log.d(TAG, "exception " + e.getLocalizedMessage());
                }
            }


            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                //Utility.showCustomOKOnlyDialog(context, error.getLocalizedMessage());
                Log.d(TAG, "exception " + error.getLocalizedMessage());
            }
        });
    }

    // called to send data to Activity
    private void broadcastAction() {
        try {
            Intent intent = new Intent(BROADCAST_ACTION);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.sendBroadcast(intent);
        } catch (Exception e) {
//            log("exception " + e.getLocalizedMessage());
            Log.d(TAG, "exception " + e.getLocalizedMessage());
        }
    }

    private void log(String description) {
        Log.d(TAG, description);
    }
}