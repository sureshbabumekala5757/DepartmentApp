package com.apcpdcl.departmentapp.service;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpCall {

    private Context context;
    public static final String TAG;
    private static final OkHttpClient okHttpClient;

    static {
        TAG = OkHttpCall.class.getSimpleName();
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(4, TimeUnit.MINUTES)
                .writeTimeout(4, TimeUnit.MINUTES)
                .build();
    }

    public OkHttpCall(Context context) {
        this.context = context;
    }

    public synchronized String getResponse(String strUrl, String strRequest) {
        try {
            strUrl = strUrl + strRequest;
            Log.d("URL", strRequest);
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, strRequest);

            Request request = new Request.Builder()
                    .url(strUrl)
                    .method("GET", body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                assert response.body() != null;
                return response.body().string();
            }

        } catch (Exception e) {
            return "Error in getting response";
        }
        return "";
    }

    public synchronized String getResp(String strUrl, String strRequest) {
        try {
            strUrl = strUrl + strRequest;
            Log.d("URL", strRequest);
            Request request = new Request.Builder()
                    .url(strUrl)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                assert response.body() != null;
                return response.body().string();
            }

        } catch (Exception e) {
            return "Error in getting response";
        }
        return "";
    }

    public synchronized String postResponse(String strUrl, String strRequest) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, strRequest);
        Request request = new Request.Builder()
                .url(strUrl)
                .method("POST", body)
                .addHeader("Authorization", "Basic U1MzMDI3MjEyNDAxOkFwQ3BEY0xAOTg3")
                .addHeader("Content-Type", "application/json")
                .addHeader("Cookie", "JSESSIONID=647DCB19F3855F4AE418AB655A46AAF6; __VCAP_ID__=2a760195-298b-42a4-47a8-dabe")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public synchronized String postResp(String strUrl, String strRequest) {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, strRequest);
        Request request = new Request.Builder()
                .url(strUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public synchronized String uploadFile(String strUrl, String imagePath, String recordPath, String authorization) {
        MediaType mediaType = MediaType.parse("application/octet-stream");

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (!imagePath.isEmpty()) {
            multipartBuilder.addFormDataPart("files", imagePath,
                    RequestBody.create(mediaType, new File(imagePath)));
        }
        if (!recordPath.isEmpty()) {
            multipartBuilder.addFormDataPart("files", recordPath,
                    RequestBody.create(mediaType, new File(recordPath)));
        }
        multipartBuilder.addFormDataPart("path", "mp-survey");
        RequestBody body = multipartBuilder.build();
        Request request = new Request.Builder()
                .url(strUrl)
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + authorization)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}