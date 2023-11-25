package com.apcpdcl.departmentapp.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.ImageUtil;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PendingDtrComplaintsDetailsActivity extends AppCompatActivity {

    String strCompID, base64_f, base64_h, base64_v, str_Status, str_uploadimage, strCompCreateDt;
    EditText et_complaintID;
    Button btn_submit;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    String longitude_F, latitude_F, longitude_H, latitude_H, longitude_V, latitude_V;


    public SharedPreferences prefs;
    private GPSTrackerActivity gpsTracker;
    String strSection_Code;
    Bitmap photo_F, photo_H, photo_V;
    ProgressDialog pDialog;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @BindView(R.id.iv_F)
    ImageView iv_F;
    @BindView(R.id.iv_H)
    ImageView iv_H;
    @BindView(R.id.iv_v)
    ImageView iv_V;

    @BindView(R.id.complaint_date)
    EditText complaint_date;
    @BindView(R.id.complaint_time)
    EditText complaint_time;

    LocationManager locationManager;
    String mprovider;
    String str_Latitude, str_Longitude;
    private static final int REQUEST_LOCATION = 1;

    /*  @BindView(R.id.ll_f)
      LinearLayout ll_f;
      @BindView(R.id.ll_h)
      LinearLayout ll_h;
      @BindView(R.id.ll_v)
      LinearLayout ll_v;
  */
    private int minutes = -1;
    private int mYear = -1;
    private int mMonth = -1;
    private int mDay = -1;
    private int mHour, mMinute;
    private int mFromHour, mFromMinute;
    private boolean isRestore = false;
    private Date dateVal = null;
    private TextView fdtr_tv,hdtr_tv,vehical_tv;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtrcomp_details);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        fdtr_tv = findViewById(R.id.fdtr_tv);
        hdtr_tv = findViewById(R.id.hdtr_tv);
        vehical_tv = findViewById(R.id.vehical_tv);
        et_complaintID = findViewById(R.id.et_complaintID);
        btn_submit = findViewById(R.id.btn_submit);

        pDialog = new ProgressDialog(this);
        userID = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
    }

    /***************************************************************************************************** Start*/
    @OnClick(R.id.complaint_time)
    void openFromTimePicker() {
        if (Utility.isValueNullOrEmpty(complaint_date.getText().toString())) {
            Utility.showToastMessage(this,
                    "Please Select From Date first");
        } else {
            Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            mFromHour = hourOfDay;
                            mFromMinute = minute;
                            complaint_time.setText((hourOfDay < 10 && minute < 10) ? "0" + hourOfDay + ":0" + minute
                                    : (hourOfDay < 10 && minute > 9) ? "0" + hourOfDay + ":" + minute
                                    : (hourOfDay > 9 && minute < 10) ? hourOfDay + ":0" + minute
                                    : hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub

            if (isRestore) {

                isRestore = false;
            } else {
                if (dayOfMonth < 10) {
                    complaint_date.setText("0" + dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                } else {
                    complaint_date.setText(dayOfMonth + "-" + Utility.getMonthFormat(monthOfYear + 1) + "-" + year);
                }
                mYear = year;
                mMonth = monthOfYear + 1;
                mDay = dayOfMonth;

            }

        }

    };

    @OnClick(R.id.complaint_date)
    void openOutageDatePicker() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH), c.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(dateVal.getTime());
        datePickerDialog.show();
    }
    private boolean checkComplaintDate() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
            Date selectedDate = format.parse(complaint_date.getText().toString().trim() + " " + complaint_time.getText().toString().trim());

            long diffMillis = selectedDate.getTime() - dateVal.getTime();
            int diffMins = (int) (diffMillis / (1000 * 60));

            if(System.currentTimeMillis() - selectedDate.getTime() < 0)
            {
                Utility.showCustomOKOnlyDialog(this, "Complaint closing time should not be in future.");
                return  false;
            }

            if (diffMins < 15)
            {
                Utility.showCustomOKOnlyDialog(this, "Complaint closing time should not be less than 15 min from Complaint Registered time/ Not empty.");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**************************************************************************************************** End*/
    @SuppressLint("NewApi")
    @OnClick(R.id.iv_F)
    void openFaulty() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                if (ActivityCompat.checkSelfPermission(
                        PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                } else {
                    Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (locationGPS != null) {
                        latitude_F = String.valueOf(locationGPS.getLatitude());
                        longitude_F = String.valueOf(locationGPS.getLongitude());
                    } else {
                        latitude_F = "0.0";
                        longitude_F = "0.0";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 100);
        }
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.iv_H)
    void openHealthy() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(
                    PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) {
                    latitude_H = String.valueOf(locationGPS.getLatitude());
                    longitude_H = String.valueOf(locationGPS.getLongitude());
                } else {
                    latitude_H = "0.0";
                    longitude_H = "0.0";
                }
            }
        }
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 101);
        }

    }

    @SuppressLint("NewApi")
    @OnClick(R.id.iv_v)
    void openVehicle() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            if (ActivityCompat.checkSelfPermission(
                    PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) {
                    latitude_V = String.valueOf(locationGPS.getLatitude());
                    longitude_V = String.valueOf(locationGPS.getLongitude());
                } else {
                    latitude_V = "0.0";
                    longitude_V = "0.0";
                }
            }
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 102);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strCompID = (String) bd.get("COMPID");
            strCompCreateDt = (String) bd.get("TRANDT");
        }

        try {


            et_complaintID.setText(strCompID);
            prefs = getSharedPreferences("loginPrefs", 0);
            strSection_Code = prefs.getString("Section_Code", "");

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            dateVal = format.parse(strCompCreateDt);
            System.out.println(dateVal);

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utility.isValueNullOrEmpty(base64_f)) {
                        Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, "Please Capture Failed DTR Image");
                    } else if (Utility.isValueNullOrEmpty(base64_h)) {
                        Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, "Please Capture Healthy DTR Image");
                    } else if (Utility.isValueNullOrEmpty(base64_v)) {
                        Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, "Please Capture Vehicle Image");
                    } else if (Utility.isValueNullOrEmpty(complaint_date.getText().toString())) {
                        Utility.showToastMessage(PendingDtrComplaintsDetailsActivity.this,
                                "Please Select Closed Date");
                    } else if (Utility.isValueNullOrEmpty(complaint_time.getText().toString())) {
                        Utility.showToastMessage(PendingDtrComplaintsDetailsActivity.this,
                                "Please Select Closed Time");
                    } else if(checkComplaintDate() == false){
                        //Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, "Complaint closing time should not be less than 15 min from Complaint Registered time/ Not empty");
                    }else {
                        if (objNetworkReceiver.hasInternetConnection(getApplicationContext())) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("COMID", strCompID);
                                jsonObject.put("GEOPARTCD", strSection_Code);

                                jsonObject.put("FLONGITUDE", longitude_F);
                                jsonObject.put("FLATITUDE", latitude_F);
                                jsonObject.put("HLONGITUDE", longitude_H);
                                jsonObject.put("HLATITUDE", latitude_H);
                                jsonObject.put("VLONGITUDE", longitude_V);
                                jsonObject.put("VLATITUDE", latitude_V);

                                jsonObject.put("FIMAGE", base64_f.replace("\n", ""));
                                jsonObject.put("HIMAGE", base64_h.replace("\n", ""));
                                jsonObject.put("VIMAGE", base64_v.replace("\n", ""));
                                jsonObject.put("DTRCLOSEDTIME", complaint_date.getText().toString().trim() + " " + complaint_time.getText().toString().trim());
                                //Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
                                //invokeWebService(jsonObject);
                                fileUploadApi();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invokeWebService(JSONObject jsonObject) {

        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            client.post(this, "http://112.133.252.110:8080/CscSapService/CCC2SAP/geolonglatInput", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        JSONObject jsonObject = new JSONObject(response);
                        str_Status = jsonObject.getString("status");
                        if (str_Status.equalsIgnoreCase("S")) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), AeDashBoardActivity.class));
                        } else if (str_Status.equalsIgnoreCase("F")) {
                            String str_Error = jsonObject.getString("Error");
                            Toast.makeText(getApplicationContext(), str_Error, Toast.LENGTH_LONG).show();

                            iv_F.setImageResource(android.R.color.transparent);
                            iv_H.setImageResource(android.R.color.transparent);
                            iv_V.setImageResource(android.R.color.transparent);
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
                    Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            photo_F = (Bitmap) data.getExtras().get("data");
            iv_F.setImageBitmap(photo_F);
            base64_f = ImageUtil.convert(photo_F);
        }

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            photo_H = (Bitmap) data.getExtras().get("data");
            iv_H.setImageBitmap(photo_H);
            base64_h = ImageUtil.convert(photo_H);
        }

        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            photo_V = (Bitmap) data.getExtras().get("data");
            iv_V.setImageBitmap(photo_V);
            base64_v = ImageUtil.convert(photo_V);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String stringfromImage(Intent data, Bitmap bitmap) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        assert thumbnail != null;
        Uri filepath = getImageUri(getApplicationContext(), thumbnail);
        File finalFile = new File(getRealPathFromURI(filepath));//here we get Path
        Uri picUri = Uri.fromFile(finalFile);
        InputStream iStream = null;
        try {
            iStream = PendingDtrComplaintsDetailsActivity.this.getContentResolver().openInputStream(picUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] inputData = new byte[0];
        try {
            inputData = getBytes(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeByteArray(inputData, 0, inputData.length);
        str_uploadimage = ImageUtil.updateConvert(bitmap);
        str_uploadimage = str_uploadimage.replace("\r\n", "");
        Utility.showLog("base64", str_uploadimage);
        return str_uploadimage;
    }


    private String getRealPathFromURI(Uri uri) {
       /* Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);*/
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        int column_index_data = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToLast();

        return cursor.getString(column_index_data);
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                PendingDtrComplaintsDetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                str_Latitude = String.valueOf(locationGPS.getLatitude());
                str_Longitude = String.valueOf(locationGPS.getLongitude());
            } else {
                str_Latitude = "0.0";
                str_Longitude = "0.0";
            }
        }
    }

    /***********Photo Upload *****************/
    private void fileUploadApi(){
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+ AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
        HttpEntity entity;

        JSONObject requestObj = new JSONObject();
        JSONObject failDataObj = new JSONObject();
        JSONObject healthyDataObj = new JSONObject();
        JSONObject vehicleDataObj = new JSONObject();
        JSONArray imgsArr = new JSONArray();
        try{

            requestObj.put("sr_no",strCompID);
            requestObj.put("bp_no","");
            //fail
            failDataObj.put("document_type","jpeg");
            failDataObj.put("file_name","FailedDTR");
            failDataObj.put("document",base64_f);
            imgsArr.put(failDataObj);
            //Helthy
            healthyDataObj.put("document_type","jpeg");
            healthyDataObj.put("file_name","HealthyDtr");
            healthyDataObj.put("document",base64_h);
            imgsArr.put(healthyDataObj);
            //Vehical
            vehicleDataObj.put("document_type","jpeg");
            vehicleDataObj.put("file_name","Vehicle");
            vehicleDataObj.put("document",base64_v);
            imgsArr.put(vehicleDataObj);

            requestObj.put("documents",imgsArr);
//        JSONObject requestObj = new JSONObject();
//        try{
//            requestObj.put("sr_no",strCompID);
//            requestObj.put("bp_no","");
//            requestObj.put("document_type","");
//            requestObj.put("file_name","");
//            if(fdtr_tv.getText().toString().equalsIgnoreCase("Failed DTR")&&hdtr_tv.getText().toString().equalsIgnoreCase("Healthy DTR")&&vehical_tv.getText().toString().equalsIgnoreCase("Vehicle"))
//                requestObj.put("document",iv_F);
//            else if(fdtr_tv.getText().toString().equalsIgnoreCase("Failed DTR Uploaded")&&hdtr_tv.getText().toString().equalsIgnoreCase("Healthy DTR")&&vehical_tv.getText().toString().equalsIgnoreCase("Vehicle"))
//                requestObj.put("document",iv_H);
//            else if(fdtr_tv.getText().toString().equalsIgnoreCase("Failed DTR Uploaded")&&hdtr_tv.getText().toString().equalsIgnoreCase("Healthy DTR Uploaded")&&vehical_tv.getText().toString().equalsIgnoreCase("Vehicle"))
//                requestObj.put("document",iv_V);
//            else {
//                complaintCloseApi();
//                return;
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {


            entity = new StringEntity(requestObj.toString());
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/UtilityDocumentUpload/DEV",headers,entity,"application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        JSONObject responseObject = new JSONObject(responseStr);
                        responseObject = responseObject.getJSONObject("response");
                        String status = responseObject.getString("success");
                        if(status.equalsIgnoreCase("True")){
                            complaintCloseApi();
                        }else{
                            Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, responseObject.getString("message"));
                        }
//                        if(responseObject.has("document")&&!responseObject.getString("document").equalsIgnoreCase("")){
//                            if(fdtr_tv.getText().toString().equalsIgnoreCase("Failed DTR Uploaded")&&hdtr_tv.getText().toString().equalsIgnoreCase("Healthy DTR Uploaded")&&vehical_tv.getText().toString().equalsIgnoreCase("Vehicle Uploaded")){
//                                complaintCloseApi();
//                            }else{
//                                if(fdtr_tv.getText().toString().equalsIgnoreCase("Failed DTR")&&hdtr_tv.getText().toString().equalsIgnoreCase("Healthy DTR")&&vehical_tv.getText().toString().equalsIgnoreCase("Vehicle"))
//                                    fdtr_tv.setText("Failed DTR Uploaded");
//                                else if(fdtr_tv.getText().toString().equalsIgnoreCase("Failed DTR Uploaded")&&hdtr_tv.getText().toString().equalsIgnoreCase("Healthy DTR")&&vehical_tv.getText().toString().equalsIgnoreCase("Vehicle"))
//                                    hdtr_tv.setText("Healthy DTR Uploaded");
//                                else if(fdtr_tv.getText().toString().equalsIgnoreCase("Failed DTR Uploaded")&&hdtr_tv.getText().toString().equalsIgnoreCase("Healthy DTR Uploaded")&&vehical_tv.getText().toString().equalsIgnoreCase("Vehicle"))
//                                    vehical_tv.setText("Vehicle Uploaded");
//                                fileUploadApi();
//                            }
//                        }else{
//                            fdtr_tv.setText("Failed DTR");
//                            hdtr_tv.setText("Healthy DTR");
//                            vehical_tv.setText("Vehicle");
//                        }

                    } catch (JSONException e) {
//                        fdtr_tv.setText("Failed DTR");
//                        hdtr_tv.setText("Healthy DTR");
//                        vehical_tv.setText("Vehicle");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
//                    fdtr_tv.setText("Failed DTR");
//                    hdtr_tv.setText("Healthy DTR");
//                    vehical_tv.setText("Vehicle");
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
    }

    /*Post Data to API*/
    private void complaintCloseApi() {

        DateFormat currentFormate = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        DateFormat targetFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date1 = null;
        String closeDate = "";
        try {
            date1 = currentFormate.parse(complaint_date.getText().toString() +" " + complaint_time.getText().toString().trim());
            closeDate = targetFormat.format(date1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        JSONObject requestObj = new JSONObject();
        try{
            requestObj.put("COMPLAINT_ID",strCompID);
            requestObj.put("RESOLVED","YES");
            requestObj.put("COMPLAINT_CLOSE_DT",closeDate);
            requestObj.put("USER_ID",userID);
        }catch (Exception e){
            e.printStackTrace();
        }

        //requestParams.put("REMARKS", et_remarks.getText().toString());
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic " + AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/ComplaintUpdate/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject responseObj = new JSONObject(responseStr);
                        responseObj = responseObj.getJSONObject("response");
                        String success = responseObj.getString("success");

                        if (success.equalsIgnoreCase("True")) {
                            Toast.makeText(getApplicationContext(), "Healthy DTR photos uploaded successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), responseObj.getString("message"), Toast.LENGTH_LONG).show();
                            iv_F.setImageResource(android.R.color.transparent);
                            iv_H.setImageResource(android.R.color.transparent);
                            iv_V.setImageResource(android.R.color.transparent);
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
                    Utility.showCustomOKOnlyDialog(PendingDtrComplaintsDetailsActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

