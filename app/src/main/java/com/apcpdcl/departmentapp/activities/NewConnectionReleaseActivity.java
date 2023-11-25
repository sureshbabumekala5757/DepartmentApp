package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.MtrDC;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.customviews.ZoomableImageView;
import com.apcpdcl.departmentapp.models.DistributionModel;
import com.apcpdcl.departmentapp.models.ServiceDetailsModel;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.ImageUtil;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewConnectionReleaseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    @BindView(R.id.btn_get_details)
    Button btn_get_details;
    @BindView(R.id.ll_details)
    LinearLayout ll_details;
    @BindView(R.id.ll_form)
    LinearLayout ll_form;
    @BindView(R.id.et_service_number)
    EditText et_service_number;
    @BindView(R.id.ll_fetch_location)
    LinearLayout ll_fetch_location;
    @BindView(R.id.ll_lat)
    LinearLayout ll_lat;
    @BindView(R.id.ll_long)
    LinearLayout ll_long;
    @BindView(R.id.tv_lat)
    TextView tv_lat;
    @BindView(R.id.tv_long)
    TextView tv_long;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.tv_service_no)
    TextView tv_service_no;
    @BindView(R.id.tv_consumer_name)
    TextView tv_consumer_name;
    @BindView(R.id.tv_category_type)
    TextView tv_category_type;
    @BindView(R.id.tv_load)
    TextView tv_load;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_get_meter_details)
    Button btn_get_meter_details;

    @BindView(R.id.btn_get_meter_dc)
    Button btn_get_meter_dc;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.ll_image)
    LinearLayout ll_image;
    @BindView(R.id.ll_sign)
    LinearLayout ll_sign;
    @BindView(R.id.tv_seal_one)
    TextView tv_seal_one;
    @BindView(R.id.tv_seal_two)
    TextView tv_seal_two;
    @BindView(R.id.tv_meter_cover_seals)
    TextView tv_meter_cover_seals;
    @BindView(R.id.et_seal_one)
    EditText et_seal_one;
    @BindView(R.id.et_seal_two)
    EditText et_seal_two;
    @BindView(R.id.et_seal_three)
    EditText et_seal_three;
    @BindView(R.id.et_seal_four)
    EditText et_seal_four;

    @BindView(R.id.iv_meter)
    ZoomableImageView iv_meter;
    @BindView(R.id.iv_sign)
    ImageView iv_sign;

    @BindView(R.id.ll_map)
    LinearLayout ll_map;

    private GoogleApiClient mGoogleApiClient;
    protected boolean mAddressRequested = false;
    protected Location mLastLocation;
    private ServiceDetailsModel serviceDetailsModel;
    private String from;
    ProgressDialog pDialog;
    private String sectionCode = "";
    private String userId = "";
    private String base64;
    private String base64Sign;
    private Bitmap bitmap;
    private Bitmap bitmap_sign;
    Button mClear, mGetSign, mCancel;
    Dialog dialog;
    LinearLayout mContent;
    View view;
    signature mSignature;
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";
    private GoogleMap mMap;

    //SAP ISU variables
    private LinearLayout ll_mslno_details, ll_meter_kwh, ll_meter_kvh, ll_meter_expor_kwh;
    private TextView tv_meter_make, tv_meter_phase, tv_meter_type;
    private EditText et_mslno, et_kwh, et_kvh, et_expor_kwh;
    private String sPhase = "";
    private String sMeterType = "";

    private LinearLayout ll_ct_meter,ll_single_three_phase_meter, ll_meter_rmd,ll_meter_kava_lag_lead, ll_meter_reading_v,ll_meter_reading_i;
    private EditText et_meter_body1_3,et_meter_tc1_3,et_ct_meter_board,et_ct_meter_body,et_ct_meter_mri,et_ct_meter_tc;
    private EditText et_ct_meter_ttb,et_ct_meter_ctbox,et_ct_meter_box,et_ct_meter_box_seal,et_ct_meter_md_reset;
    private EditText readings_edt_rmd,readings_edt_kvahlag,readings_edt_kvahlead,readings_edt_vr,readings_edt_vy,readings_edt_vb,readings_edt_ir,readings_edt_iy,readings_edt_ib;
    private ArrayList<MtrDC> mtrDCDArrList;
    private Spinner spn_mtrdc;
    private String sSelectedDcd,sSelectedDc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_connection_release);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        ll_mslno_details = findViewById(R.id.ll_mslno_details);
        ll_meter_kwh = findViewById(R.id.ll_meter_kwh);
        ll_meter_kvh = findViewById(R.id.ll_meter_kvh);
        ll_meter_expor_kwh = findViewById(R.id.ll_meter_expor_kwh);
        ll_meter_rmd = findViewById(R.id.ll_meter_rmd);
        ll_meter_kava_lag_lead = findViewById(R.id.ll_meter_kava_lag_lead);
        ll_meter_reading_v = findViewById(R.id.ll_meter_reading_v);
        ll_meter_reading_i = findViewById(R.id.ll_meter_reading_i);

        tv_meter_make = findViewById(R.id.tv_meter_make);
        tv_meter_phase = findViewById(R.id.tv_meter_phase);
        tv_meter_type = findViewById(R.id.tv_meter_type);

        et_mslno = findViewById(R.id.et_mslno);
        et_kwh = findViewById(R.id.et_kwh);
        et_kvh = findViewById(R.id.et_kvh);
        et_expor_kwh = findViewById(R.id.et_expor_kwh);

        readings_edt_rmd = findViewById(R.id.readings_edt_rmd);
        readings_edt_kvahlag = findViewById(R.id.readings_edt_kvahlag);
        readings_edt_kvahlead = findViewById(R.id.readings_edt_kvahlead);

        readings_edt_vr = findViewById(R.id.readings_edt_vr);
        readings_edt_vy = findViewById(R.id.readings_edt_vy);
        readings_edt_vb = findViewById(R.id.readings_edt_vb);
        readings_edt_ir = findViewById(R.id.readings_edt_ir);
        readings_edt_iy = findViewById(R.id.readings_edt_iy);
        readings_edt_ib = findViewById(R.id.readings_edt_ib);


        //CT/1/3 Meter LL
        ll_ct_meter = findViewById(R.id.ll_ct_meter);
        ll_single_three_phase_meter = findViewById(R.id.ll_single_three_phase_meter);
        ll_ct_meter.setVisibility(View.GONE);
        ll_single_three_phase_meter.setVisibility(View.GONE);

        //Ct/1/3 Meter details EditText
        et_meter_body1_3 = findViewById(R.id.et_meter_body1_3);
        et_meter_tc1_3 = findViewById(R.id.et_meter_tc1_3);
        et_ct_meter_board = findViewById(R.id.et_ct_meter_board);
        et_ct_meter_body = findViewById(R.id.et_ct_meter_body);
        et_ct_meter_mri = findViewById(R.id.et_ct_meter_mri);
        et_ct_meter_tc = findViewById(R.id.et_ct_meter_tc);
        et_ct_meter_ttb = findViewById(R.id.et_ct_meter_ttb);
        et_ct_meter_ctbox = findViewById(R.id.et_ct_meter_ctbox);
        et_ct_meter_box = findViewById(R.id.et_ct_meter_box);
        et_ct_meter_box_seal = findViewById(R.id.et_ct_meter_box_seal);
        et_ct_meter_md_reset = findViewById(R.id.et_ct_meter_md_reset);

        ll_mslno_details.setVisibility(View.GONE);
        ll_meter_kwh.setVisibility(View.GONE);
        ll_meter_kvh.setVisibility(View.GONE);
        ll_meter_expor_kwh.setVisibility(view.GONE);
        ll_meter_rmd.setVisibility(view.GONE);
        ll_meter_kava_lag_lead.setVisibility(view.GONE);
        ll_meter_reading_v.setVisibility(view.GONE);
        ll_meter_reading_i.setVisibility(view.GONE);

        //New
        spn_mtrdc = findViewById(R.id.spn_mtrdc);

        init();
    }

    private void init() {
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        sectionCode = prefs.getString("Section_Code", "");
//        userId = prefs.getString("UserName", "");

        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");
        userId = AppPrefs.getInstance(getApplicationContext()).getString("USERID", "");
        pDialog = new ProgressDialog(NewConnectionReleaseActivity.this);
        buildGoogleApiClient();
        setAshtrikColor();
        Intent intent = getIntent();
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);
        if (intent.hasExtra(Constants.SERVICE_DETAILS)) {
            serviceDetailsModel = (ServiceDetailsModel) intent.getSerializableExtra(Constants.SERVICE_DETAILS);
            prePopulateData();
        }
        if (intent.hasExtra(Constants.FROM)) {
            from = intent.getStringExtra(Constants.FROM);
            toolbar_title.setText(from);
            ll_main.setVisibility(View.VISIBLE);
            ll_form.setVisibility(View.VISIBLE);
            ll_details.setVisibility(View.GONE);
        }
//        et_service_number.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 5 && !charSequence.toString().equals(sectionCode)) {
//                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
//                            et_service_number.getText().toString() + " Service Number is not related to you");
//                    et_service_number.setText("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        et_mslno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    String meterSrialNumber = et_mslno.getText().toString();
//                    JSONObject requestObj = null;
//                    try {
//                        requestObj = new JSONObject();
//                        requestObj.put("MethodName", "ValidateMtrNo");
//                        requestObj.put("MeterSerialNo", meterSrialNumber);
//                        requestObj.put("SectionCode", sectionCode);
//                        getMeterDetails(requestObj);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
        et_mslno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_mslno.setText("");
                sPhase = "";
                return false;
            }
        });
    }

    //Get Meter Details
    private void getMeterDetails(JSONObject requestObjPayLoad) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(false);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            Utility.showLog("URL", Constants.URL + Constants.GEO_LATLONGINPUT);
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/MeterReplacement/NewMeterValidation/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");
                        if (jsonObject.has("success")) {
                            if (jsonObject.optString("success").equalsIgnoreCase("True")) {
                                ll_mslno_details.setVisibility(View.VISIBLE);
                                sPhase = jsonObject.optString("phase");
                                sMeterType = jsonObject.optString("metertype");
                                tv_meter_make.setText(jsonObject.optString("make"));
                                tv_meter_phase.setText(jsonObject.optString("phase"));
                                tv_meter_type.setText(jsonObject.optString("metertype"));
                                if (sPhase.equalsIgnoreCase("1")) {
                                    ll_meter_kwh.setVisibility(View.VISIBLE);
                                    ll_meter_kvh.setVisibility(View.GONE);
                                    ll_meter_expor_kwh.setVisibility(view.GONE);
                                    ll_meter_rmd.setVisibility(view.VISIBLE);
                                    ll_meter_kava_lag_lead.setVisibility(view.GONE);
                                    ll_meter_reading_v.setVisibility(view.GONE);
                                    ll_meter_reading_i.setVisibility(view.GONE);
                                } else if (sPhase.equalsIgnoreCase("3") || sMeterType.equalsIgnoreCase("CT")) {
                                    ll_meter_kwh.setVisibility(View.VISIBLE);
                                    ll_meter_kvh.setVisibility(View.VISIBLE);
                                    ll_meter_expor_kwh.setVisibility(view.GONE);
                                    ll_meter_expor_kwh.setVisibility(view.GONE);
                                    ll_meter_rmd.setVisibility(view.VISIBLE);
                                    if (sMeterType.equalsIgnoreCase("CT"))
                                        ll_meter_kava_lag_lead.setVisibility(view.VISIBLE);
                                    else
                                        ll_meter_kava_lag_lead.setVisibility(view.GONE);
                                    ll_meter_reading_v.setVisibility(view.VISIBLE);
                                    ll_meter_reading_i.setVisibility(view.VISIBLE);
                                } else if (sPhase.equalsIgnoreCase("solar")) {
                                    ll_meter_kwh.setVisibility(View.GONE);
                                    ll_meter_kvh.setVisibility(View.GONE);
                                    ll_meter_expor_kwh.setVisibility(view.VISIBLE);
                                    ll_meter_rmd.setVisibility(view.GONE);
                                    ll_meter_kava_lag_lead.setVisibility(view.GONE);
                                    ll_meter_reading_v.setVisibility(view.GONE);
                                    ll_meter_reading_i.setVisibility(view.GONE);
                                }
                                if((sPhase.equalsIgnoreCase("1") || sPhase.equalsIgnoreCase("3"))&& (!sMeterType.equalsIgnoreCase("CT"))){
                                    ll_ct_meter.setVisibility(View.GONE);
                                    ll_single_three_phase_meter.setVisibility(View.VISIBLE);
                                }else{
                                    ll_single_three_phase_meter.setVisibility(View.GONE);
                                    ll_ct_meter.setVisibility(View.VISIBLE);
                                }


                            } else {
                                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, jsonObject.getString("message"));
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
                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "Something Went Wrong, Please Try Again.");
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    //Get Device Category Description
    private void getMeterDCD(JSONObject requestObjPayLoad) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(false);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            Utility.showLog("URL", Constants.URL + Constants.GEO_LATLONGINPUT);
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/MeterReplacement/NewMeterValidation/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");
                        if (jsonObject.has("success")) {
                            if (jsonObject.optString("success").equalsIgnoreCase("True")) {


                            } else {
                                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, jsonObject.getString("message"));
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
                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "Something Went Wrong, Please Try Again.");
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.ll_image)
    void openCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    /* *Set Red Ashtrik in Text**/
    private void setAshtrikColor() {
        /* tv_seal_two.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 2"));*/
        tv_seal_one.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 1"));
        tv_meter_cover_seals.setText(Html.fromHtml("Meter Cover Seals (MRT)\n" +
                " ( " + "<font color=\"#E50E0E\">" + "*" + "</font>" + " Seals are mandatory)"));
    }

    private void getDeviceId() {
     /*   TelephonyManager manager = (TelephonyManager) getSystemService(NewConnectionReleaseActivity.TELEPHONY_SERVICE);
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
        }*/
        String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, IMEI_NUMBER);
        Utility.showLog("IMEI", IMEI_NUMBER);
    }

    //    Bind Meter description data
    private void bindMtrDCDData() {
        //Add String meterMakeStrArray to meterMakeArrayList
        ArrayList<String> dcdList = new ArrayList<>();
        dcdList.add("--Select--");
        ArrayList<MtrDC> mtrDCDArrList = new ArrayList<>();
        if(mtrDCDArrList.size()>0) {
            for (MtrDC dcd : mtrDCDArrList) {
                dcdList.add(dcd.getMtrDCD());
            }
        }
        //String[] meterMakeStrArray = getResources().getStringArray(R.array.distributions);
        //distributionList = new ArrayList<>(Arrays.asList(meterMakeStrArray));
        SpinnerAdapter adapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, dcdList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_mtrdc.setAdapter(adapter);
        spn_mtrdc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sSelectedDcd = (String) dcdList.get(position);
                if (!sSelectedDc.equalsIgnoreCase("--Select--")) {
                    MtrDC obj = mtrDCDArrList.get(position - 1);
                    sSelectedDc = obj.getMtrDC();
                    sSelectedDcd = obj.getMtrDCD();
                } else {
                    sSelectedDc = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @OnClick(R.id.btn_get_meter_dc)
    void getDCDData() {
        String meterSrialNumber = et_mslno.getText().toString();
        JSONObject requestObj = null;
        try {
            if(meterSrialNumber.equalsIgnoreCase("")){
                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "First get Meter Device Category.");
            }else if (sSelectedDcd.equalsIgnoreCase("")) {
                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "Please select Meter Device Category Description.");
            } else{
                requestObj = new JSONObject();
                requestObj.put("MethodName", "ValidateMtrNo");
                requestObj.put("MeterSerialNo", meterSrialNumber);
                requestObj.put("SectionCode", sectionCode);
                requestObj.put("ServiceRequest",serviceDetailsModel.getREGNO());
                getMeterDCD(requestObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.btn_get_meter_details)
    void getMeterDetailsData() {
        String meterSrialNumber = et_mslno.getText().toString();
        JSONObject requestObj = null;
        try {
            if (meterSrialNumber.equalsIgnoreCase("")) {
                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "Please enter meter SL number");
            } else{
                requestObj = new JSONObject();
                requestObj.put("MethodName", "ValidateMtrNo");
                requestObj.put("MeterSerialNo", meterSrialNumber);
                requestObj.put("SectionCode", sectionCode);
                requestObj.put("ServiceRequest",serviceDetailsModel.getREGNO());
                getMeterDetails(requestObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick(R.id.btn_submit)
    void postData() {
        if (validateFields()) {
            if (Utility.isNetworkAvailable(this)) {
                if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
                    getDeviceId();
                } else {
                    if(sPhase.equalsIgnoreCase("")){
                        Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "First Please Get the meter details");
                    }else {
                        postServiceDetails();
                    }
                }
            } else {
                Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this,
                        R.string.no_internet));
            }
        }
    }

    @OnClick(R.id.iv_meter)
    void imageFullView() {
        showImageDialog(bitmap);
    }
    //SAPISU_POST SERVICE
    private void postServiceDetails() {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setURLEncodingEnabled(false);
        BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
        JSONObject requestObj = new JSONObject();
        try {

                requestObj.put("RegistrationId" , serviceDetailsModel.getREGNO());
                requestObj.put("MeterInstallationDate" , new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));//
                requestObj.put("MeterCategory" , sMeterType);
                requestObj.put("MeterMake" , tv_meter_make.getText().toString());
                requestObj.put("MeterSerialNo" , et_mslno.getText().toString());
                requestObj.put("MeterPhase" , tv_meter_phase.getText().toString());
                requestObj.put("MeterIrda" , "");
                requestObj.put("MeterSolar" , "");

                if(sPhase.equalsIgnoreCase("3") || sMeterType.equalsIgnoreCase("CT")) {
                    requestObj.put("MrKwh", et_kwh.getText().toString());
                    requestObj.put("MrKvah", et_kvh.getText().toString());
                }else{
                    if(sPhase.equalsIgnoreCase("1")) {
                        requestObj.put("MrKwh", et_kwh.getText().toString());
                        requestObj.put("MrKvah", "0");
                    }
                }
                requestObj.put("MrMd" , (readings_edt_rmd.getText().toString().equalsIgnoreCase("")) ? ("0"):(readings_edt_rmd.getText().toString()));
                if(sMeterType.equalsIgnoreCase("solar")) {
                    requestObj.put("MrExpkwh", et_expor_kwh.getText().toString());
                    requestObj.put("MrExpkvah", "0");
                }else{
                    requestObj.put("MrExpkwh", "0");
                    requestObj.put("MrExpkvah", "0");
                }
                if(sMeterType.equalsIgnoreCase("CT")) {
                    requestObj.put("MrKvarhlag", readings_edt_kvahlag.getText().toString());
                    requestObj.put("MrKvarhlead", readings_edt_kvahlead.getText().toString());
                }else{
                    requestObj.put("MrKvarhlag", "0");
                    requestObj.put("MrKvarhlead", "0");
                }
                if(sPhase.equalsIgnoreCase("3") || sMeterType.equalsIgnoreCase("CT")) {
                    requestObj.put("MrVr", (readings_edt_vr.getText().toString().equalsIgnoreCase("")) ? ("0"):(readings_edt_vr.getText().toString()));
                    requestObj.put("MrVy", (readings_edt_vy.getText().toString().equalsIgnoreCase("")) ? ("0"):(readings_edt_vy.getText().toString()));
                    requestObj.put("MrVb", (readings_edt_vb.getText().toString().equalsIgnoreCase("")) ? ("0"):(readings_edt_vb.getText().toString()));
                    requestObj.put("MrIr", (readings_edt_ir.getText().toString().equalsIgnoreCase("")) ? ("0"):(readings_edt_ir.getText().toString()));
                    requestObj.put("MrIy", (readings_edt_iy.getText().toString().equalsIgnoreCase("")) ? ("0"):(readings_edt_iy.getText().toString()));
                    requestObj.put("MrIb", (readings_edt_ib.getText().toString().equalsIgnoreCase("")) ? ("0"):(readings_edt_ib.getText().toString()));
                }else{
                    requestObj.put("MrVr", "0");
                    requestObj.put("MrVy", "0");
                    requestObj.put("MrVb", "0");
                    requestObj.put("MrIr", "0");
                    requestObj.put("MrIy", "0");
                    requestObj.put("MrIb", "0");
                }
                if(sMeterType.equalsIgnoreCase("CT")) {
                    requestObj.put("MsTc" , et_ct_meter_tc.getText().toString());
                    requestObj.put("MsMeterBody" , et_ct_meter_body.getText().toString());
                    requestObj.put("MsMeterBox", et_ct_meter_box.getText().toString());
                    requestObj.put("MsMeterBoard", et_ct_meter_board.getText().toString());
                    requestObj.put("MsMri", et_ct_meter_mri.getText().toString());
                    requestObj.put("MsTtb", et_ct_meter_ttb.getText().toString());
                    requestObj.put("MsCtbox", et_ct_meter_ctbox.getText().toString());
                    requestObj.put("MsBoxseal", et_ct_meter_box_seal.getText().toString());
                    requestObj.put("MsMdreset", et_ct_meter_md_reset.getText().toString());
                }else{
                    requestObj.put("MsTc" , et_meter_tc1_3.getText().toString());
                    requestObj.put("MsMeterBody" , et_meter_body1_3.getText().toString());
                    requestObj.put("MsMeterBox", "");
                    requestObj.put("MsMeterBoard", "");
                    requestObj.put("MsMri", "");
                    requestObj.put("MsTtb", "");
                    requestObj.put("MsCtbox", "");
                    requestObj.put("MsBoxseal", "");
                    requestObj.put("MsMdreset", "");
                }
                requestObj.put("Longitude" , tv_long.getText().toString());
                requestObj.put("Latitude" , tv_lat.getText().toString());
                requestObj.put("MeterImage" , base64.replace("\n", ""));

            HttpEntity entity;
            try {
                entity = new StringEntity(requestObj.toString());
                client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/NewConnectionRelease/Save/DEV", headers,entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String responseStr) {
                        Utility.showLog("onSuccess", responseStr);
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(responseStr);
                            jsonObject = jsonObject.getJSONObject("response");
                            String  sStatus= jsonObject.getString("success");

                                if (sStatus.equalsIgnoreCase("true")) {
                                    if (Utility.isValueNullOrEmpty(from)) {
                                        LMRegistrationListActivity.getInstance().updateList(serviceDetailsModel.getREGNO());
                                    }
                                    showCustomOKOnlyDialog("Updated Successfully");
                                } else if (sStatus.equalsIgnoreCase("false")){
                                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, jsonObject.optString("message"));
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
                        Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "Something Went Wrong, Please Try Again.");
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
//    private void postServiceDetails() {
//        pDialog.show();
//        pDialog.setMessage("Please wait...");
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.setURLEncodingEnabled(false);
//        JSONObject requestParams = new JSONObject();
//        try {
//            requestParams.put("REGNO", serviceDetailsModel.getREGNO());
//            requestParams.put("ADDRESS", serviceDetailsModel.getAddress());
//            requestParams.put("HOST", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
//            requestParams.put("GEOPARTCD", serviceDetailsModel.getREGNO().substring(0, 5));
//            requestParams.put("LONGITUDE", "" + tv_long.getText().toString());
//            requestParams.put("LATITUDE", "" + tv_lat.getText().toString());
//            requestParams.put("IMAGE", "" + base64.replace("\n", ""));
//            requestParams.put("SIGNATURE", "" + base64Sign.replace("\n", ""));
//            String seal = et_seal_one.getText().toString() + "-" + et_seal_two.getText().toString() + "-" +
//                    et_seal_three.getText().toString() + "-" + et_seal_four.getText().toString();
//            requestParams.put("SEAL", seal);
//            requestParams.put("USERID", "" + userId);
//            HttpEntity entity;
//            try {
//                entity = new StringEntity(requestParams.toString());
//                Utility.showLog("Params", requestParams.toString());
//                Utility.showLog("URL", Constants.URL + Constants.GEO_LATLONGINPUT);
//                client.post(this, Constants.URL + Constants.GEO_LATLONGINPUT, entity, "application/json", new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(String response) {
//                        Utility.showLog("onSuccess", response);
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            if (jsonObject.has("status")) {
//                                if (jsonObject.optString("status").equalsIgnoreCase("S")) {
//                                    if (Utility.isValueNullOrEmpty(from)) {
//                                        LMRegistrationListActivity.getInstance().updateList(serviceDetailsModel.getREGNO());
//                                    }
//                                    showCustomOKOnlyDialog("Updated Successfully");
//                                } else {
//                                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, jsonObject.optString("Error"));
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Throwable error, String content) {
//                        Utility.showLog("error", error.toString());
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.dismiss();
//                        }
//                        Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this, "Something Went Wrong, Please Try Again.");
//                    }
//                });
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

    private void prePopulateData() {
        toolbar_title.setText("NEW SERVICES-TAG LOCATION");
        ll_form.setVisibility(View.GONE);
        ll_details.setVisibility(View.VISIBLE);
        ll_main.setVisibility(View.VISIBLE);
        tv_service_no.setText(serviceDetailsModel.getREGNO());
        tv_consumer_name.setText(serviceDetailsModel.getConsumerName());
        tv_category_type.setText(serviceDetailsModel.getCategory());
        tv_load.setText(serviceDetailsModel.getLoad());
        tv_address.setText(serviceDetailsModel.getAddress());
        if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getLatitude()) && !serviceDetailsModel.getLatitude().equalsIgnoreCase("0")) {
            tv_lat.setText(serviceDetailsModel.getLatitude());
            ll_lat.setVisibility(View.VISIBLE);
        }
        if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getLongitude()) && !serviceDetailsModel.getLongitude().equalsIgnoreCase("0")) {
            tv_long.setText(serviceDetailsModel.getLongitude());
            ll_long.setVisibility(View.VISIBLE);
        }
        if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getLatitude()) && !serviceDetailsModel.getLatitude().equalsIgnoreCase("0") && mMap != null) {
            mLastLocation = new Location(LocationManager.GPS_PROVIDER);
            mLastLocation.setLatitude(Double.parseDouble(serviceDetailsModel.getLatitude()));
            mLastLocation.setLongitude(Double.parseDouble(serviceDetailsModel.getLongitude()));
            tv_lat.setText(String.format("%s", mLastLocation.getLatitude()));
            tv_long.setText(String.format("%s", mLastLocation.getLongitude()));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .title("(" + tv_lat.getText().toString() + "," + tv_long.getText().toString() + ")"));
            //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(NewConnectionReleaseActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(NewConnectionReleaseActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                }
            });
            ll_map.setVisibility(View.VISIBLE);
            ll_lat.setVisibility(View.VISIBLE);
            ll_long.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
        }
        if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getSeal())) {
            String[] strings = serviceDetailsModel.getSeal().split("-");
            switch (strings.length) {
                case 4:
                    et_seal_one.setText(strings[0]);
                    et_seal_two.setText(strings[1]);
                    et_seal_three.setText(strings[2]);
                    et_seal_four.setText(strings[3]);
                    break;
                case 3:
                    et_seal_one.setText(strings[0]);
                    et_seal_two.setText(strings[1]);
                    et_seal_three.setText(strings[2]);
                    break;
                case 2:
                    et_seal_one.setText(strings[0]);
                    et_seal_two.setText(strings[1]);
                    break;
                case 1:
                    et_seal_one.setText(strings[0]);
                    break;
            }
        }
        if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getImage())) {
            base64 = serviceDetailsModel.getImage().replace("\r\n", "");
            bitmap = ImageUtil.convert(base64);
            iv_meter.setImageBitmap(bitmap);
            iv_meter.setVisibility(View.VISIBLE);
        }
        if (!Utility.isValueNullOrEmpty(serviceDetailsModel.getSignature())) {
            base64Sign = serviceDetailsModel.getSignature().replace("\r\n", "");
            bitmap_sign = ImageUtil.convert(base64Sign);
            iv_sign.setImageBitmap(bitmap_sign);
            iv_sign.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ll_fetch_location)
    void fetchLatLang() {
        checkPermissionsAndCallServiceToGetLatAndLng();
    }

    @OnClick(R.id.ll_sign)
    void openCanvas() {
        dialog_action();
    }

    protected void createLocationRequest() {
        // pDialog.show();
        mAddressRequested = true;
        mGoogleApiClient.connect();
        @SuppressLint("RestrictedApi") LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                // final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Utility.showLog("LocationSettingsStatusCodes.SUCCESS",
                                "LocationSettingsStatusCodes.SUCCESS" + LocationSettingsStatusCodes.SUCCESS);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //checkPermissionsAndCallServiceToGetLatAndLng();
                        mGoogleApiClient.connect();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        Utility.showLog("LocationSettingsStatusCodes.RESOLUTION_REQUIRED",
                                "LocationSettingsStatusCodes.RESOLUTION_REQUIRED" + LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(NewConnectionReleaseActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Utility.showLog("1 LocationServices mLastLocation", "LocationServices mLastLocation");
            checkPermissionsAndCallServiceToGetLatAndLng();
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Utility.showLog("Geocoder mLastLocation", "Geocoder mLastLocation");
            // Determine whether a Geocoder is available.
            if (!Geocoder.isPresent()) {
                Utility.showToastMessage(this, Utility.getResourcesString(NewConnectionReleaseActivity.this, R.string.no_geocoder_available));
            } else {
                tv_lat.setText(String.format("%s", mLastLocation.getLatitude()));
                tv_long.setText(String.format("%s", mLastLocation.getLongitude()));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .title("(" + tv_lat.getText().toString() + "," + tv_long.getText().toString() + ")"));
                //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intent = new Intent(NewConnectionReleaseActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                        return false;
                    }
                });
                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        Intent intent = new Intent(NewConnectionReleaseActivity.this, ExploreLocalityActivity.class);
                        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                        startActivity(intent);
                    }
                });
                ll_map.setVisibility(View.VISIBLE);
                ll_lat.setVisibility(View.VISIBLE);
                ll_long.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                //  ll_fetch_location.setVisibility(View.GONE);
            }
            buildGoogleApiClient();

        } else {
            checkPermissionsAndCallServiceToGetLatAndLng();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @OnClick(R.id.ll_map)
    void navigateToMapView() {
        Intent intent = new Intent(this, ExploreLocalityActivity.class);
        intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
        intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
        startActivity(intent);
    }

    /*
     *CHECK PERMISSIONS AND CALL SERVICE TO GET CITY BASED ON LAT AND LNG
     */
    private void checkPermissionsAndCallServiceToGetLatAndLng() {
        if (Utility.isNetworkAvailable(this)) {
            if (Utility.isMarshmallowOS()) {
                PackageManager pm = getPackageManager();
                int hasFineLocationPerm = pm.checkPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        getPackageName());
                int hasCoarseLocationPerm = pm.checkPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        getPackageName());
                if (hasFineLocationPerm != PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            Constants.MY_PERMISSIONS_REQUEST_LOCATION);
                } else if (hasCoarseLocationPerm == PackageManager.PERMISSION_GRANTED &&
                        hasFineLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.MY_PERMISSIONS_REQUEST_LOCATION_SINGLE);
                } else if (hasCoarseLocationPerm != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            Constants.MY_PERMISSIONS_REQUEST_LOCATION_SINGLE);
                } else {
                    pDialog.show();
                    if (Utility.isLocationEnabled(this)) {
                        if (Utility.isNetworkAvailable(this)) {
                            createLocationRequest();
                        } else {
                            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
                        }
                    } else {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        createLocationRequest();
                    }
                }
            } else {
                pDialog.show();
                if (Utility.isLocationEnabled(this)) {
                    if (Utility.isNetworkAvailable(this)) {
                        createLocationRequest();
                    } else {
                        Utility.showCustomOKOnlyDialog(this,
                                Utility.getResourcesString(this,
                                        R.string.no_internet));
                    }
                } else {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    createLocationRequest();
                }
            }
        } else {
            Utility.showCustomOKOnlyDialog(this,
                    Utility.getResourcesString(this,
                            R.string.no_internet));
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_IMEI:
               /* if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager manager = (TelephonyManager) getSystemService(NewConnectionReleaseActivity.TELEPHONY_SERVICE);
                    Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                    Utility.showLog("IMEI", manager.getDeviceId());
                }*/
                String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, IMEI_NUMBER);
                Utility.showLog("IMEI", IMEI_NUMBER);
                break;
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndCallServiceToGetLatAndLng();
                }
                break;
            case Constants.MY_PERMISSIONS_REQUEST_LOCATION_SINGLE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionsAndCallServiceToGetLatAndLng();
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ll_fetch_location.performClick();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
            case 100:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    iv_meter.setImageBitmap(bitmap);
                    iv_meter.setVisibility(View.VISIBLE);
                    base64 = ImageUtil.convert(bitmap);


                   /* Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    assert thumbnail != null;
                    Uri filepath = getImageUri(getApplicationContext(), thumbnail);
                    File finalFile = new File(getRealPathFromURI(filepath));//here we get Path
                    Uri picUri = Uri.fromFile(finalFile);
                    // getContentResolver().notifyChange(picUri, null);
                    InputStream iStream = null;
                    try {
                        iStream = NewConnectionReleaseActivity.this.getContentResolver().openInputStream(picUri);
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
                   // bitmap = thumbnail;
                    iv_meter.setImageBitmap(bitmap);
                    iv_meter.setVisibility(View.VISIBLE);
                    // ll_image.setVisibility(View.GONE);
                    base64 = ImageUtil.convert(bitmap);
                    Utility.showLog("base64", base64);*/
                }
        }
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
                    tv_lat.setText("");
                    tv_long.setText("");
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

    public void showImageDialog(Bitmap bitmap) {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(this);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_image_fullview);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);

            ZoomableImageView iv_meter = (ZoomableImageView) dialog_confirm.findViewById(R.id.iv_meter);
            iv_meter.setImageBitmap(bitmap);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_confirm.dismiss();
                    //  finish();
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

    @OnClick(R.id.btn_get_details)
    void implementSearch() {
//        if (Utility.isValueNullOrEmpty(et_service_number.getText().toString()) || et_service_number.getText().toString().length() >= 10) {
        if (Utility.isValueNullOrEmpty(et_service_number.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please enter valid 20 digits Registration Number.");
        } else {
//            if (et_service_number.getText().toString().substring(0, 5).equalsIgnoreCase(sectionCode)) {
                if (Utility.isNetworkAvailable(this)) {
                    getServiceDetails(et_service_number.getText().toString());
                } else {
                    Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this,
                            R.string.no_internet));
                }
//            } else {
//                Utility.showCustomOKOnlyDialog(this, "This Registration Number is not related to you.");
//            }
        }
    }

    //SAP ISU code
    private void getServiceDetails(String serviceNumber) {
        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject requestObjPayLoad = null;
        HttpEntity entity;
        try {
            try {
                requestObjPayLoad = new JSONObject();
                requestObjPayLoad.put("REGNO", serviceNumber);
            } catch (Exception e) {

            }
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/SearchRegistration/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");

                        if (!jsonObject.getString("success").equalsIgnoreCase("true")) {
                            Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
                                    jsonObject.getString("message"));
                            et_service_number.setText("");
                        } else {
                            serviceDetailsModel = new ServiceDetailsModel(
                                    jsonObject.getString("category"),
                                    jsonObject.getString("load"),
                                    jsonObject.getString("consumeraddress"),
                                    jsonObject.getString("consumername"),
                                    jsonObject.getString("registrationdate"),
                                    jsonObject.getString("registrationid"),
                                    jsonObject.getString("longitude"),
                                    jsonObject.getString("latitude"),
                                    jsonObject.getString("seal"),
                                    jsonObject.getString("meterimage"),
                                    ""
                            );

                            if (from.equalsIgnoreCase("Edit") &&
                                    (serviceDetailsModel.getLongitude().equalsIgnoreCase("0") &&
                                            serviceDetailsModel.getLatitude().equalsIgnoreCase("0"))) {
                                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
                                        "Location Not tagged yet for this service.");
                            } else if (from.equalsIgnoreCase("Search") &&
                                    (!serviceDetailsModel.getLongitude().equalsIgnoreCase("0") &&
                                            !serviceDetailsModel.getLatitude().equalsIgnoreCase("0"))) {
                                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
                                        "Location Already tagged for this service.");
                            } else {
                                prePopulateData();
                            }

                        }
                    } catch (JSONException e) {
                        Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
                                "Something went wrong please try again later...");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
                            "Something went wrong please try again later...");
                }
            });
        }catch (Exception e){

        }
    }

//    private void getServiceDetails(String serviceNumber) {
//        pDialog.show();
//        pDialog.setMessage("Please wait...");
//        AsyncHttpClient client = new AsyncHttpClient();
//        Utility.showLog("Url", Constants.URL + Constants.GEO_REG_DETAILS + "/" + serviceNumber);
//        client.get(Constants.URL + Constants.GEO_REG_DETAILS + "/" + serviceNumber, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(String response) {
//                Utility.showLog("onSuccess", response);
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.has("RESPONSE")) {
//                        JSONArray jsonArray = jsonObject.getJSONArray("RESPONSE");
//                        if (jsonArray.length() > 0) {
//                            JSONObject json = jsonArray.optJSONObject(0);
//                            if (json.has("STATUS")) {
//                                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
//                                        json.optString("STATUSMSG"));
//                                et_service_number.setText("");
//                            } else {
//                                serviceDetailsModel = new Gson().fromJson(json.toString(),
//                                        ServiceDetailsModel.class);
//                                if (from.equalsIgnoreCase("Edit") &&
//                                        (serviceDetailsModel.getLongitude().equalsIgnoreCase("0") &&
//                                                serviceDetailsModel.getLatitude().equalsIgnoreCase("0"))) {
//                                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
//                                            "Location Not tagged yet for this service.");
//                                } else if (from.equalsIgnoreCase("Search") &&
//                                        (!serviceDetailsModel.getLongitude().equalsIgnoreCase("0") &&
//                                                !serviceDetailsModel.getLatitude().equalsIgnoreCase("0"))) {
//                                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
//                                            "Location Already tagged for this service.");
//                                } else {
//                                    prePopulateData();
//                                }
//                            }
//                        }
//                    }
//                } catch (JSONException e) {
//                    Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
//                            "Something went wrong please try again later...");
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Throwable error, String content) {
//                Utility.showLog("error", error.toString());
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.dismiss();
//                }
//                Utility.showCustomOKOnlyDialog(NewConnectionReleaseActivity.this,
//                        "Something went wrong please try again later...");
//            }
//        });
//    }

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

    // Function for Digital Signature
    public void dialog_action() {
        File file = new File(DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }
        // Dialog Function
        dialog = new Dialog(NewConnectionReleaseActivity.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);
        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Utility.showLog("log_tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath);
                dialog.dismiss();
                iv_sign.setImageBitmap(bitmap_sign);
                iv_sign.setVisibility(View.VISIBLE);
                //ll_sign.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                // Calling the same class
                recreate();
            }
        });
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (serviceDetailsModel != null && !Utility.isValueNullOrEmpty(serviceDetailsModel.getLatitude()) && !serviceDetailsModel.getLatitude().equalsIgnoreCase("0")) {
            mLastLocation = new Location(LocationManager.NETWORK_PROVIDER);
            mLastLocation.setLatitude(Double.parseDouble(serviceDetailsModel.getLatitude()));
            mLastLocation.setLongitude(Double.parseDouble(serviceDetailsModel.getLongitude()));
            tv_lat.setText(String.format("%s", mLastLocation.getLatitude()));
            tv_long.setText(String.format("%s", mLastLocation.getLongitude()));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .title("(" + tv_lat.getText().toString() + "," + tv_long.getText().toString() + ")"));
            //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(NewConnectionReleaseActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(NewConnectionReleaseActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                }
            });
            ll_map.setVisibility(View.VISIBLE);
            ll_lat.setVisibility(View.VISIBLE);
            ll_long.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);
        }

    }


    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        @SuppressLint("WrongThread")
        public void save(View v, String StoredPath) {
            Utility.showLog("log_tag", "Width: " + v.getWidth());
            Utility.showLog("log_tag", "Height: " + v.getHeight());
            if (bitmap_sign == null) {
                bitmap_sign = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap_sign);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                bitmap_sign.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                base64Sign = ImageUtil.convert(bitmap_sign);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Utility.showLog("log_tag", e.toString());
            }

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
            Utility.showLog("log_tag", string);
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(tv_long.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Tag Location");
            return false;
        } else if (Utility.isValueNullOrEmpty(base64)) {
            Utility.showCustomOKOnlyDialog(this, "Please Capture Meter Image");
            return false;
        }else if(Utility.isValueNullOrEmpty(et_mslno.getText().toString())){
            Utility.showCustomOKOnlyDialog(this, "Please enter the meter sl number");
            return false;
        }else if(Utility.isValueNullOrEmpty(et_kwh.getText().toString())){
            Utility.showCustomOKOnlyDialog(this, "Please enter kwh");
            return false;
        }else if((sMeterType.equalsIgnoreCase("CT") || sPhase.equalsIgnoreCase("3"))&& Utility.isValueNullOrEmpty(et_kvh.getText().toString())){
            Utility.showCustomOKOnlyDialog(this, "Please enter kvah");
            return false;
        }else if((sPhase.equalsIgnoreCase("1")||sPhase.equalsIgnoreCase("3"))&& (Utility.isValueNullOrEmpty(et_meter_tc1_3.getText().toString())) && (!sMeterType.equalsIgnoreCase("CT"))){
            Utility.showCustomOKOnlyDialog(this, "Please enter TC Seal");
            return false;
        }else if(sMeterType.equalsIgnoreCase("CT")&& Utility.isValueNullOrEmpty(et_ct_meter_tc.getText().toString())){
            Utility.showCustomOKOnlyDialog(this, "Please enter TC Seal");
            return false;
        }
//        else if (Utility.isValueNullOrEmpty(base64Sign)) {
//            Utility.showCustomOKOnlyDialog(this, "Please Add Consumer Digital Signature");
//            return false;
//        }
//        else if (Utility.isValueNullOrEmpty(et_seal_one.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, "Please Enter Seal 1");
//            return false;
//        }
       /* else if (Utility.isValueNullOrEmpty(et_seal_two.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Seal 2");
            return false;
        }*/
        return true;
    }


}