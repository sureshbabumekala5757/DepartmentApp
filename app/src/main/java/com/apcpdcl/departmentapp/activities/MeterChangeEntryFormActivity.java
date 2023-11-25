package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.customviews.DatePickerFragment;
import com.apcpdcl.departmentapp.customviews.ExpandableLayoutListener;
import com.apcpdcl.departmentapp.customviews.Utils;
import com.apcpdcl.departmentapp.models.MeterChangeEntryModel;
import com.apcpdcl.departmentapp.models.MeterDetailsModel;
import com.apcpdcl.departmentapp.services.NetworkChangeReceiver;
import com.apcpdcl.departmentapp.services.ServiceConstants;
import com.apcpdcl.departmentapp.shared.AppPrefs;
import com.apcpdcl.departmentapp.sqlite.MeterChangeDatabaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
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
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseena
 * on 23-02-2018.
 */

public class MeterChangeEntryFormActivity extends AppCompatActivity implements View.OnFocusChangeListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    @BindView(R.id.tv_service_no)
    TextView tv_service_no;
    @BindView(R.id.tv_consumer_name)
    TextView tv_consumer_name;
    @BindView(R.id.tv_service_type)
    TextView tv_service_type;
    @BindView(R.id.tv_cycle)
    TextView tv_cycle;
    @BindView(R.id.tv_category)
    TextView tv_category;
    @BindView(R.id.tv_sub_category)
    TextView tv_sub_category;
    @BindView(R.id.tv_dtr_code)
    TextView tv_dtr_code;
    @BindView(R.id.tv_meter_number)
    TextView tv_meter_number;
    @BindView(R.id.tv_meter_make)
    TextView tv_meter_make;
    @BindView(R.id.tv_meter_type)
    TextView tv_meter_type;
    @BindView(R.id.tv_meter_class)
    TextView tv_meter_class;
    @BindView(R.id.tv_meter_capacity)
    TextView tv_meter_capacity;
    @BindView(R.id.tv_meter_mf)
    TextView tv_meter_mf;
    @BindView(R.id.tv_meter_closing_reading_kwh)
    TextView tv_meter_closing_reading_kwh;
    @BindView(R.id.tv_meter_closing_reading)
    TextView tv_meter_closing_reading;
    @BindView(R.id.tv_meter_closing_date)
    TextView tv_meter_closing_date;
    @BindView(R.id.tv_meter_closing_reading_status)
    TextView tv_meter_closing_reading_status;
    @BindView(R.id.el_master_details)
    ExpandableRelativeLayout el_master_details;
    @BindView(R.id.iv_master_details)
    ImageView iv_master_details;
    @BindView(R.id.el_meter_change_particulars)
    ExpandableRelativeLayout el_meter_change_particulars;
    @BindView(R.id.iv_meter_change_particulars)
    ImageView iv_meter_change_particulars;
    @BindView(R.id.el_new_meter_details)
    ExpandableRelativeLayout el_new_meter_details;
    @BindView(R.id.iv_new_meter_details)
    ImageView iv_new_meter_details;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_get_new_meter_details)
    Button btn_get_new_meter_details;
    @BindView(R.id.spn_meter_change_reason)
    Spinner spn_meter_change_reason;
    @BindView(R.id.et_meter_change_reason)
    EditText et_meter_change_reason;
    @BindView(R.id.spn_irda)
    Spinner spn_irda;
    @BindView(R.id.spn_meter_phase)
    Spinner spn_meter_phase;
    @BindView(R.id.spn_meter_make)
    Spinner spn_meter_make;
    @BindView(R.id.spn_meter_type)
    Spinner spn_meter_type;
    @BindView(R.id.tv_tc_seal)
    TextView tv_tc_seal;
    @BindView(R.id.tv_seal_one)
    TextView tv_seal_one;
    @BindView(R.id.tv_seal_two)
    TextView tv_seal_two;
    @BindView(R.id.tv_meter_cover_seals)
    TextView tv_meter_cover_seals;
    @BindView(R.id.et_meter_type)
    EditText et_meter_type;
    @BindView(R.id.et_meter_make)
    EditText et_meter_make;
    @BindView(R.id.et_meter_change_date)
    EditText et_meter_change_date;
    @BindView(R.id.et_old_meter_number)
    EditText et_old_meter_number;
    @BindView(R.id.et_final_reading_kwh)
    EditText et_final_reading_kwh;
    @BindView(R.id.et_final_reading_kvah)
    EditText et_final_reading_kvah;

    @BindView(R.id.et_final_reading_exp_kwh)
    EditText et_final_reading_exp_kwh;

    @BindView(R.id.et_final_reading_exp_kvah)
    EditText et_final_reading_exp_kvah;

    @BindView(R.id.et_meter_change_slip_number)
    EditText et_meter_change_slip_number;
    @BindView(R.id.et_changed_by)
    EditText et_changed_by;
    @BindView(R.id.sv_main)
    ScrollView sv_main;
    @BindView(R.id.et_new_meter_number)
    EditText et_new_meter_number;
    @BindView(R.id.et_meter_capacity)
    EditText et_meter_capacity;
    @BindView(R.id.et_meter_class)
    EditText et_meter_class;
    @BindView(R.id.et_meter_digits)
    EditText et_meter_digits;
    @BindView(R.id.et_meter_mfg_date)
    EditText et_meter_mfg_date;
    @BindView(R.id.et_initial_reading_kwh)
    EditText et_initial_reading_kwh;
    @BindView(R.id.et_initial_reading_kvah)
    EditText et_initial_reading_kvah;
    @BindView(R.id.et_mf)
    EditText et_mf;

    @BindView(R.id.et_meter_phase)
    EditText et_meter_phase;
    @BindView(R.id.et_seal_one)
    EditText et_seal_one;
    @BindView(R.id.et_seal_two)
    EditText et_seal_two;
    @BindView(R.id.et_seal_three)
    EditText et_seal_three;
    @BindView(R.id.et_seal_four)
    EditText et_seal_four;
    @BindView(R.id.et_meter_tc_seals)
    EditText et_meter_tc_seals;

    @BindView(R.id.ll_form)
    LinearLayout ll_form;
    @BindView(R.id.ll_details)
    LinearLayout ll_details;
    @BindView(R.id.ll_rmd)
    LinearLayout ll_rmd;
    @BindView(R.id.et_service_number)
    EditText et_service_number;
    @BindView(R.id.et_rmd)
    EditText et_rmd;
    @BindView(R.id.btn_get_details)
    Button btn_get_details;
    @BindView(R.id.ll_map)
    LinearLayout ll_map;
    @BindView(R.id.ll_latitude)
    LinearLayout ll_latitude;
    @BindView(R.id.ll_longitude)
    LinearLayout ll_longitude;
    @BindView(R.id.ll_fetch_location)
    LinearLayout ll_fetch_location;

    @BindView(R.id.tv_latitude)
    TextView tv_latitude;
    @BindView(R.id.tv_longitude)
    TextView tv_longitude;
    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private String mMeterChangeReason = "";
    private String mIRDA = "";
    private String mMeterPhase = "";
    private String mMeterMake = "";
    private String mMeterType = "";
    private boolean kwhProceed = false;
    private boolean kvahProceed = false;
    private boolean ikwhProceed = false;
    private boolean ikvahProceed = false;
    private boolean meterNumberProceed = false;
    private MeterDetailsModel meterDetailsModel;
    private ProgressDialog prgDialog;
    private MeterChangeDatabaseHandler dbManager;
    private NetworkChangeReceiver networkChangeReceiver;
    private String sectionCode = "";
    private String user = "", oldMPhase;
    public static final String TAG = MeterChangeEntryFormActivity.class.getSimpleName();
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean isList = false;

    //SAP ISU
    private LinearLayout ll_new_meter_make,ll_new_meter_type,ll_new_meter_phase,ll_new_meter_kwh,ll_new_meter_kvah, ll_new_meter_ex_kwh,ll_new_meter_ex_kvah,ll_new_meter_rmd, ll_new_meter_kava_lag_lead, ll_new_meter_reading_v,
            ll_new_meter_reading_i, ll_new_meter_seals_details,ll_new_meter_wo_tc_body, ll_final_reading_exp_kvah,ll_final_reading_exp_kwh,ll_meter_exp_kwh_closing_reading,ll_meter_exp_kvah_closing_reading;
    private TextView tv_new_meter_kwh, tv_new_meter_kvah,tv_meter_exp_kwh_closing_reading,tv_meter_exp_kvah_closing_reading;
    private EditText et_new_meter_kwh, et_new_meter_kvah, et_new_meter_rmd, et_new_meter_kvah_lag, ed_new_meter_kvah_lead;
    private EditText et_vr,et_vy, et_vb,et_ir,et_iy,et_ib;
    private EditText et_new_meter_tc,et_new_meter_body,et_new_meter_board,et_new_meter_mri,et_new_meter_ttb,et_new_meter_ctbox,
            et_new_meter_box, et_new_meter_box_seal,et_new_meter_md_reset,et_new_meter_ex_kwh,et_new_meter_ex_kvah;
    private String sPhase,sMeterType, isSolar, serviceRequestNo, oldMisSolar;
    ImageView OldMeter_iv,newMeter_iv;
    Bitmap photo_OldMeter;
    Bitmap photo_newMeter;
    String base64_oldmeterimg;
    String base64_newmeterimg;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Uri imageUri;
    ContentValues values;
    String imageurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_change_entry_form_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        //LinearLayout
        ll_new_meter_kwh = findViewById(R.id.ll_new_meter_kwh);
        ll_new_meter_kvah = findViewById(R.id.ll_new_meter_kvah);
        ll_new_meter_rmd = findViewById(R.id.ll_new_meter_rmd);
        ll_new_meter_kava_lag_lead = findViewById(R.id.ll_new_meter_kava_lag_lead);
        ll_new_meter_reading_v = findViewById(R.id.ll_new_meter_reading_v);
        ll_new_meter_reading_i = findViewById(R.id.ll_new_meter_reading_i);
        ll_new_meter_seals_details = findViewById(R.id.ll_new_meter_seals_details);
        ll_new_meter_wo_tc_body = findViewById(R.id.ll_new_meter_wo_tc_body);
        ll_new_meter_make = findViewById(R.id.ll_new_meter_make);
        ll_new_meter_type = findViewById(R.id.ll_new_meter_type);
        ll_new_meter_phase = findViewById(R.id.ll_new_meter_phase);
        ll_new_meter_ex_kwh = findViewById(R.id.ll_new_meter_ex_kwh);
        ll_new_meter_ex_kvah = findViewById(R.id.ll_new_meter_ex_kvah);

        ll_final_reading_exp_kwh = findViewById(R.id.ll_final_reading_exp_kwh);
        ll_final_reading_exp_kvah = findViewById(R.id.ll_final_reading_exp_kvah);

        ll_meter_exp_kwh_closing_reading = findViewById(R.id.ll_meter_exp_kwh_closing_reading);
        ll_meter_exp_kvah_closing_reading = findViewById(R.id.ll_meter_exp_kvah_closing_reading);

        //TextView
        tv_new_meter_kwh = findViewById(R.id.tv_new_meter_kwh);
        tv_new_meter_kvah = findViewById(R.id.tv_new_meter_kvah);

        tv_meter_exp_kwh_closing_reading = findViewById(R.id.tv_meter_exp_kwh_closing_reading);
        tv_meter_exp_kvah_closing_reading = findViewById(R.id.tv_meter_exp_kvah_closing_reading);

        //EditText
        et_new_meter_kwh = findViewById(R.id.et_new_meter_kwh);
        et_new_meter_kvah = findViewById(R.id.et_new_meter_kvah);
        et_new_meter_ex_kwh = findViewById(R.id.et_new_meter_ex_kwh);
        et_new_meter_ex_kvah = findViewById(R.id.et_new_meter_ex_kvah);
        et_new_meter_rmd = findViewById(R.id.et_new_meter_rmd);
        et_new_meter_kvah_lag = findViewById(R.id.et_new_meter_kvah_lag);
        ed_new_meter_kvah_lead = findViewById(R.id.ed_new_meter_kvah_lead);

        et_vr = findViewById(R.id.et_vr);
        et_vy = findViewById(R.id.et_vy);
        et_vb = findViewById(R.id.et_vb);
        et_ir = findViewById(R.id.et_ir);
        et_iy = findViewById(R.id.et_iy);
        et_ib = findViewById(R.id.et_ib);

        et_new_meter_tc = findViewById(R.id.et_new_meter_tc);
        et_new_meter_body = findViewById(R.id.et_new_meter_body);
        et_new_meter_board = findViewById(R.id.et_new_meter_board);
        et_new_meter_mri = findViewById(R.id.et_new_meter_mri);
        et_new_meter_ttb = findViewById(R.id.et_new_meter_ttb);
        et_new_meter_ctbox = findViewById(R.id.et_new_meter_ctbox);
        et_new_meter_box = findViewById(R.id.et_new_meter_box);
        et_new_meter_box_seal = findViewById(R.id.et_new_meter_box_seal);
        et_new_meter_md_reset = findViewById(R.id.et_new_meter_md_reset);
        ll_new_meter_make.setVisibility(View.GONE);
        ll_new_meter_type.setVisibility(View.GONE);
        ll_new_meter_phase.setVisibility(View.GONE);
        ll_new_meter_kwh.setVisibility(View.GONE);
        ll_new_meter_kvah.setVisibility(View.GONE);
        ll_new_meter_ex_kwh.setVisibility(View.GONE);
        ll_new_meter_ex_kvah.setVisibility(View.GONE);
        ll_new_meter_rmd.setVisibility(View.GONE);
        ll_new_meter_kava_lag_lead.setVisibility(View.GONE);
        ll_new_meter_reading_v.setVisibility(View.GONE);
        ll_new_meter_reading_i.setVisibility(View.GONE);
        ll_new_meter_seals_details.setVisibility(View.GONE);

        OldMeter_iv = findViewById(R.id.OldMeter_iv);
        newMeter_iv = findViewById(R.id.newMeter_iv);

        init();
    }
    //Capture the CTMeter Image
    public void captureCTMImg(View view) {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, 100);
            values = new ContentValues();
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 101);
        }
    }
    public void captureCTMImg1(View view) {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
//            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(cameraIntent, 100);
            values = new ContentValues();
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, 100);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


//        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
//            photo_CTM = (Bitmap) data.getExtras().get("data");
//            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
//                    photo_CTM, 450,600, true);
//            ctMeter_iv.setImageBitmap(resizedBitmap);
//            base64_ctmimg = ImageUtil.convert(resizedBitmap);

//        }


        if (requestCode == 100 || requestCode == 101)
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    String mType = OldMeter_iv.getTag().toString();
                    if(requestCode== 100)
                        newMeter_iv.setImageBitmap(thumbnail);
                    else
                        OldMeter_iv.setImageBitmap(thumbnail);

                    imageurl = getRealPathFromURI(imageUri);

                    Bitmap bm = BitmapFactory.decodeFile(imageurl);

                    bm = Bitmap.createScaledBitmap(
                            bm, 1024, 768, false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    if(requestCode== 100)
                        base64_newmeterimg = Base64.encodeToString(b, Base64.DEFAULT);
                    else
                     base64_oldmeterimg = Base64.encodeToString(b, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    private void init() {
        if (getIntent().hasExtra(Constants.USCNO)) {
            ll_form.setVisibility(View.GONE);
            et_service_number.setText(getIntent().getStringExtra(Constants.USCNO));
            serviceRequestNo = getIntent().getStringExtra("SERVICEREQUESTNO");
            isList = true;
        }
//        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
//        sectionCode = prefs.getString("Section_Code", "");
//        user = prefs.getString("UserName", "");
        user = AppPrefs.getInstance(getApplicationContext()).getString("USERNAME", "");
        sectionCode = AppPrefs.getInstance(getApplicationContext()).getString("SECTIONCODE", "");

        et_changed_by.setText(user);
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.GET_METER_MAKE_LIST))) {
            Utility.getMeterMake(this);
        }
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        Utility.showLog("IMEI", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        prgDialog = new ProgressDialog(this);

        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        FragmentManager fmanager = getSupportFragmentManager();
        Fragment fragment = fmanager.findFragmentById(R.id.map);
        SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
        supportmapfragment.getMapAsync(this);
        setExpandableButtonAnimators();
        setMeterChangeReasonSpinnerData();
        buildGoogleApiClient();
        setIRDASpinnerData();
        setMeterPhaseSpinnerData();
        setMeterMakeSpinnerData();
        setMeterTypeSpinnerData();
        setAshtrikColor();
        inLineValidations();

//        et_service_number.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() == 5 && !charSequence.toString().equals(sectionCode)) {
//                    Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this,
//                            et_service_number.getText().toString() + " Service Number is not related to you");
//                    et_service_number.setText("");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        et_new_meter_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    String meterSrialNumber = et_new_meter_number.getText().toString();
//                    JSONObject requestObj = null;
//                    try {
//                        requestObj = new JSONObject();
//                        requestObj.put("MethodName", "ValidateMtrNo");
//                        requestObj.put("MeterSerialNo", meterSrialNumber);
//                        requestObj.put("SectionCode", sectionCode);
//                        getNewMeterInfo(requestObj);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
        if (getIntent().hasExtra(Constants.USCNO)) {
            btn_get_details.performClick();
        }
    }


    @OnClick({R.id.rl_master_details, R.id.iv_master_details})
    void toggleMasterDetails() {
        el_master_details.toggle();
    }

    @OnClick({R.id.rl_meter_change_particulars, R.id.iv_meter_change_particulars})
    void toggleMeterChangeParticulars() {
        el_meter_change_particulars.toggle();
    }

    @OnClick({R.id.rl_new_meter_details, R.id.iv_new_meter_details})
    void toggleNewMeterDetails() {
        el_new_meter_details.toggle();
    }

    @OnClick(R.id.btn_submit)
    void navigateToNextScreen() {
        prgDialog.show();
        prgDialog.setMessage("Please wait...");
        if (validateFields()) {
            getFinalUrlFromFields();
        }else {
            if (prgDialog != null && prgDialog.isShowing()) {
                prgDialog.dismiss();
            }
        }
    }

    @OnClick(R.id.btn_get_details)
    void getDetails() {
        if (!Utility.isValueNullOrEmpty(et_service_number.getText().toString())) {
//            if (et_service_number.getText().toString().length() < 13) {
//                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Enter a valid Service Number");
//            } else {
                if (Utility.isNetworkAvailable(this)) {
//                    getMeterDetails(et_service_number.getText().toString());
                    getOldMeterInfo(et_service_number.getText().toString());
                } else {
                    showCustomDialog(this, Utility.getResourcesString(
                            this, R.string.no_internet_msg), false);
                }
//            }
        } else {
            Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Enter Service Number");
        }
    }
    @OnClick(R.id.btn_get_new_meter_details)
    void getNewMeterDetails() {
        String meterSrialNumber = et_new_meter_number.getText().toString();
        JSONObject requestObj = null;
        try {
            if (meterSrialNumber.equalsIgnoreCase("")) {
                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Please enter meter SL number");
            } else{
                requestObj = new JSONObject();
                requestObj.put("MethodName", "ValidateMtrNo");
                requestObj.put("MeterSerialNo", meterSrialNumber);
                requestObj.put("SectionCode", sectionCode);
                requestObj.put("ServiceRequest", serviceRequestNo);
                getNewMeterInfo(requestObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.et_meter_change_date)
    void openDatePicker() {
        DatePickerFragment date = new DatePickerFragment(et_meter_change_date, meterDetailsModel.getBLCLRDT());
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt(Constants.YEAR, calender.get(Calendar.YEAR));
        args.putInt(Constants.MONTH, calender.get(Calendar.MONTH));
        args.putInt(Constants.DAY, calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(DatePickerFragment.ondate);
        date.show(getSupportFragmentManager(), Constants.DATE_PICKER);
    }

    @OnClick(R.id.et_meter_mfg_date)
    void openDatePickerManufacturingDate() {
        if (Utility.isValueNullOrEmpty(et_meter_change_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_select_meter_change_date_first));
            sv_main.requestChildFocus(et_meter_change_date, et_meter_change_date);
        } else {
            DatePickerFragment date = new DatePickerFragment(et_meter_mfg_date, "", et_meter_change_date.getText().toString());
            Calendar calender = Calendar.getInstance();
            Bundle args = new Bundle();
            args.putInt(Constants.YEAR, calender.get(Calendar.YEAR));
            args.putInt(Constants.MONTH, calender.get(Calendar.MONTH));
            args.putInt(Constants.DAY, calender.get(Calendar.DAY_OF_MONTH));
            date.setArguments(args);
            date.setCallBack(DatePickerFragment.ondate);
            date.show(getSupportFragmentManager(), Constants.DATE_PICKER);
        }
    }

    /* Set Expandable buttons animator*/
    private void setExpandableButtonAnimators() {
        el_master_details.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(iv_master_details, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(iv_master_details, 180f, 0f).start();
            }

            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });
        el_meter_change_particulars.collapse();
        el_meter_change_particulars.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(iv_meter_change_particulars, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(iv_meter_change_particulars, 180f, 0f).start();
            }

            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });
        el_new_meter_details.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(iv_new_meter_details, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(iv_new_meter_details, 180f, 0f).start();
            }

            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });

    }

    /*Rotate Animator for buttons*/
    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    /* *Set Meter change reason Spinner Data**/
    private void setMeterChangeReasonSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("MRT Testing");
        list.add("Meter Stuckup");
        list.add("Meter Burnt");
        list.add("Meter Burnt-Supply Available");
        list.add("Display Fault");
        list.add("Meter Running Slow/Sluggish");
        list.add("Meter Running Fast/Creeping");
        list.add("Meter Damage");
        list.add("Meter Challenge");
        list.add("High Accuracy");
        list.add("Phase Change");
        list.add("Dial Jump");
        list.add("No Display");
        list.add("Voltage Drop");
        list.add("Current Drop");
        list.add("IRDA not Scanned");
        list.add("IRDA");
        list.add("Others");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_change_reason.setAdapter(newlineAdapter);
        final boolean[] isFirstTime = {true};
        spn_meter_change_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position == list.indexOf("Others")) {
                    et_meter_change_reason.setVisibility(View.VISIBLE);
                    mMeterChangeReason = parent.getItemAtPosition(position).toString();
                } else if (position != 0) {
                    mMeterChangeReason = parent.getItemAtPosition(position).toString();
                    et_meter_change_reason.setVisibility(View.GONE);
                } else {
                    et_meter_change_reason.setVisibility(View.GONE);
                }
                if (!isFirstTime[0] && Utility.isValueNullOrEmpty(et_meter_change_slip_number.getText().toString())) {
                    Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                            MeterChangeEntryFormActivity.this, R.string.err_meter_change_slip_number_cant_be_empty));
                    sv_main.requestChildFocus(et_meter_change_slip_number, et_meter_change_slip_number);
                }
                isFirstTime[0] = false;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                if (!isFirstTime[0] && Utility.isValueNullOrEmpty(et_meter_change_slip_number.getText().toString())) {
                    Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                            MeterChangeEntryFormActivity.this, R.string.err_meter_change_slip_number_cant_be_empty));
                    sv_main.requestChildFocus(et_meter_change_slip_number, et_meter_change_slip_number);
                }
            }
        });
    }

    /* *Set IRDA Spinner Data**/
    private void setIRDASpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Yes");
        list.add("No");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_irda.setAdapter(newlineAdapter);
        spn_irda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mIRDA = parent.getItemAtPosition(position).toString();
                } else {
                    mIRDA = "";
                }

                if (mIRDA.equals("Yes")) {
                    et_meter_make.setVisibility(View.GONE);
                    spn_meter_type.setVisibility(View.GONE);
                    et_meter_type.setVisibility(View.VISIBLE);
                    spn_meter_make.setVisibility(View.VISIBLE);
                    spn_meter_make.setSelection(0);

                }
                if (mIRDA.equals("No")) {
                    et_meter_make.setVisibility(View.VISIBLE);
                    spn_meter_type.setVisibility(View.VISIBLE);
                    spn_meter_type.setSelection(0);
                    et_meter_type.setVisibility(View.GONE);
                    spn_meter_make.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Meter Make Spinner Data**/
    private void setMeterMakeSpinnerData() {
        final ArrayList<String> list = Utility.getMeterMakeList(this);
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_make.setAdapter(newlineAdapter);
        spn_meter_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMeterMake = parent.getItemAtPosition(position).toString();
                } else {
                    mMeterMake = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Meter Type Spinner Data**/
    private void setMeterTypeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> sendList = new ArrayList<>();
        list.add("--Select--");
        list.add("MECHANICAL");
        list.add("ELECTRONIC");
        list.add("HIGH ACCURACY");
        list.add("OLD MECHANICAL");
        list.add("LTCMETER");
        list.add("HTCMETER");

        sendList.add("--Select--");
        sendList.add("1-MECHANIC");
        sendList.add("2-ELECTRON");
        sendList.add("3-HAMECHAN");
        sendList.add("4-OLMECHAN");
        sendList.add("5-LTCTMTR");
        sendList.add("6-HTCTMTR");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_type.setAdapter(newlineAdapter);
        spn_meter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMeterType = sendList.get(position);
                } else {
                    mMeterType = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Meter Phase Spinner Data**/
    private void setMeterPhaseSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("1");
        list.add("3");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_phase.setAdapter(newlineAdapter);
        spn_meter_phase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMeterPhase = parent.getItemAtPosition(position).toString();
                } else {
                    mMeterPhase = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Red Ashtrik in Text**/
    private void setAshtrikColor() {
        //tv_seal_two.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 2"));
        tv_seal_one.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 1"));
        tv_tc_seal.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Meter TC Seals"));
        tv_meter_cover_seals.setText(Html.fromHtml("Meter Cover Seals (MRT)\n" +
                " ( " + "<font color=\"#E50E0E\">" + "*" + "</font>" + " Seals are mandatory)"));
    }

    /*Validate Fields*/
    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(et_meter_change_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_change_date));
          /*  if (!el_meter_change_particulars.isExpanded()) {
                el_meter_change_particulars.toggle();
            }*/
            sv_main.requestChildFocus(et_meter_change_date, et_meter_change_date);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_old_meter_number.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_old_meter_number));
            sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
            return false;
        }
//        else if (et_old_meter_number.getText().toString().length() < 5) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_old_meter_number_lees_than_five));
//            sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
//            return false;
//        }
        else if (!meterNumberProceed && !et_old_meter_number.getText().toString().equalsIgnoreCase(meterDetailsModel.getMTNO())) {
            verifyOldMeterNumber();
            return false;
        } else if (Utility.isValueNullOrEmpty(et_final_reading_kwh.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_old_meter_kwh_reading));
            sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
            return false;
        } else if (Double.parseDouble(et_final_reading_kwh.getText().toString()) < Double.parseDouble(meterDetailsModel.getBLCLKWH())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_final_reading_cant_be_less_kwh));
            sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
            return false;
        } else if (!kwhProceed &&
                Double.parseDouble(et_final_reading_kwh.getText().toString()) > Double.parseDouble(meterDetailsModel.getBLCLKWH()) + 3000) {
            abnormalKWH();
            return false;
        } else if (meterDetailsModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                Utility.isValueNullOrEmpty(et_final_reading_kvah.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_old_meter_kvah_reading));
            sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
            return false;
        } else if (meterDetailsModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(meterDetailsModel.getBLCLKVAH())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_final_reading_cant_be_less_kvah));
            sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
            return false;
        } else if (!kvahProceed && meterDetailsModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(meterDetailsModel.getBLCLKVAH()) + 3000) {
            abnormalKVAH();
            return false;
        }else if( ((oldMisSolar.equalsIgnoreCase("Y")) && (oldMPhase.contains("1") || (oldMPhase.contains("3")))) && (Utility.isValueNullOrEmpty(et_final_reading_exp_kwh.getText().toString()))){

                Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                        this, R.string.err_enter_old_meter_kwh_exp_reading));
                sv_main.requestChildFocus(et_final_reading_exp_kwh, et_final_reading_exp_kwh);
                return false;

        }else if( ((oldMisSolar.equalsIgnoreCase("Y")) && (oldMPhase.contains("1") || (oldMPhase.contains("3")))) && (Double.parseDouble(et_final_reading_exp_kwh.getText().toString()) < Double.parseDouble(meterDetailsModel.getEXPKWH()))){
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.err_final_reading_cant_be_less_exp_kwh));
            sv_main.requestChildFocus(et_final_reading_exp_kwh, et_final_reading_exp_kwh);
            return false;
        }else if( (oldMisSolar.equalsIgnoreCase("Y")) && ((oldMPhase.contains("3"))) && (Utility.isValueNullOrEmpty(et_final_reading_exp_kvah.getText().toString()))){

                Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.err_enter_old_meter_kvah_exp_reading));
                sv_main.requestChildFocus(et_final_reading_exp_kvah, et_final_reading_exp_kvah);
                return false;
        }else if( (oldMisSolar.equalsIgnoreCase("Y")) && ((oldMPhase.contains("3"))) && (Double.parseDouble(et_final_reading_exp_kvah.getText().toString()) < Double.parseDouble(meterDetailsModel.getEXPKVAH()))){
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.err_final_reading_cant_be_less_exp_kvah));
            return false;
        }else if (Utility.isValueNullOrEmpty(et_meter_change_slip_number.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_meter_change_slip_number_cant_be_empty));
            sv_main.requestChildFocus(et_meter_change_slip_number, et_meter_change_slip_number);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_meter_change_reason.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_select_meter_change_reason));
            //sv_main.requestChildFocus(spn_meter_change_reason, spn_meter_change_reason);
            return false;
        }
//        else if (mMeterChangeReason.equalsIgnoreCase("Others") &&
//                Utility.isValueNullOrEmpty(et_meter_change_reason.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_change_reason));
//            sv_main.requestChildFocus(et_meter_change_reason, et_meter_change_reason);
//            return false;
//        }
//        else if (Utility.isValueNullOrEmpty(mIRDA)) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_select_irda_flag));
//            sv_main.requestChildFocus(spn_irda, spn_irda);
//            return false;
//        }
        else if (mIRDA.equalsIgnoreCase("Yes") && Utility.isValueNullOrEmpty(mMeterMake)) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_select_new_meter_make));
            sv_main.requestChildFocus(spn_meter_make, spn_meter_make);
            return false;
        } else if (mIRDA.equalsIgnoreCase("No") && Utility.isValueNullOrEmpty(et_meter_make.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_new_meter_make_cant_be_empty));
            sv_main.requestChildFocus(et_meter_make, et_meter_make);
            return false;
        } else if (mIRDA.equalsIgnoreCase("No") && Utility.isValueNullOrEmpty(mMeterType)) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_select_new_meter_type));
            sv_main.requestChildFocus(spn_meter_type, spn_meter_type);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_new_meter_number.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_new_meter_number_cant_be_empty));
            sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
            return false;
        }
//        else if (et_new_meter_number.getText().toString().length() < 5) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_valid_new_meter_number));
//            sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
//            return false;
//        }
        else if (Utility.isValueNullOrEmpty(base64_oldmeterimg)) {
            Utility.showCustomOKOnlyDialog(this, "Please Capture Old Meter Image");
            return false;
        }
        else if (et_new_meter_number.getText().toString().equalsIgnoreCase(meterDetailsModel.getMTNO())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_valid_new_meter_number));
            sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
            return false;
        }
        else if (Utility.isValueNullOrEmpty(et_new_meter_kwh.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please enter the KWH");
            return false;
        }else if (isSolar.equalsIgnoreCase("Y") && Utility.isValueNullOrEmpty(et_new_meter_ex_kwh.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please enter the Export KWH");
            return false;
        }else if (Utility.isValueNullOrEmpty(et_new_meter_tc.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please enter the TC Seal");
            return false;
        }else if (Utility.isValueNullOrEmpty(base64_newmeterimg)) {
            Utility.showCustomOKOnlyDialog(this, "Please Capture New Meter Image");
            return false;
        }


//        else if (Utility.isValueNullOrEmpty(mMeterPhase)) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_select_new_meter_phase));
//            sv_main.requestChildFocus(spn_meter_phase, spn_meter_phase);
//            return false;
//        }
//        else if (Utility.isValueNullOrEmpty(et_meter_capacity.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_new_meter_capacity_cant_be_empty));
//            sv_main.requestChildFocus(et_meter_capacity, et_meter_capacity);
//            return false;
//        } else if (Utility.isValueNullOrEmpty(et_meter_class.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_new_meter_class_cant_be_empty));
//            sv_main.requestChildFocus(et_meter_class, et_meter_class);
//            return false;
//        }
//        else if (Utility.isValueNullOrEmpty(et_meter_digits.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_meter_digits_cant_be_empty));
//            sv_main.requestChildFocus(et_meter_digits, et_meter_digits);
//            return false;
//        } else if (Utility.isValueNullOrEmpty(et_meter_mfg_date.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_manufacture_date));
//            sv_main.requestChildFocus(et_meter_mfg_date, et_meter_mfg_date);
//            return false;
//        } else if (Utility.isValueNullOrEmpty(et_initial_reading_kwh.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_reading_kwh));
//            sv_main.requestChildFocus(et_initial_reading_kwh, et_initial_reading_kwh);
//            return false;
//        } else if (!ikwhProceed && Double.parseDouble(et_initial_reading_kwh.getText().toString()) > 10) {
//            abNormalInitialKWH();
//            return false;
//        } else if (Utility.isValueNullOrEmpty(et_initial_reading_kvah.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_reading_kvah));
//            sv_main.requestChildFocus(et_initial_reading_kvah, et_initial_reading_kvah);
//            return false;
//        } else if (!ikvahProceed && Double.parseDouble(et_initial_reading_kvah.getText().toString()) > 10) {
//            abNormalInitialKVAH();
//            return false;
//        } else if (Utility.isValueNullOrEmpty(et_mf.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_mf));
//            sv_main.requestChildFocus(et_mf, et_mf);
//            return false;
//        } else if (et_mf.getText().toString().equalsIgnoreCase("0")
//                || et_mf.getText().toString().equalsIgnoreCase("00")) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_valid_meter_mf));
//            sv_main.requestChildFocus(et_mf, et_mf);
//            return false;
//        } else if (Utility.isValueNullOrEmpty(et_seal_one.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_seal_one));
//            sv_main.requestChildFocus(et_seal_one, et_seal_one);
//            return false;
//    /*    } else if (Utility.isValueNullOrEmpty(et_seal_two.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_seal_two));
//            sv_main.requestChildFocus(et_seal_two, et_seal_two);
//            return false;*/
//        } else if (Utility.isValueNullOrEmpty(et_meter_tc_seals.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                    this, R.string.err_enter_meter_seal_tc));
//            sv_main.requestChildFocus(et_meter_tc_seals, et_meter_tc_seals);
//            return false;
//        } else if (Utility.isNetworkAvailable(this) && Utility.isValueNullOrEmpty(tv_latitude.getText().toString())) {
//            Utility.showCustomOKOnlyDialog(this, "Please Tag Location.");
//            sv_main.requestChildFocus(ll_fetch_location, ll_fetch_location);
//            return false;
//        }
        return true;
    }

    @OnClick(R.id.ll_fetch_location)
    void fetchLatLang() {
        checkPermissionsAndCallServiceToGetLatAndLng();
    }


    /*Set Master Details*/
    private void setData() {
        ll_form.setVisibility(View.GONE);
        ll_details.setVisibility(View.VISIBLE);
        tv_service_no.setText(et_service_number.getText().toString());
        tv_consumer_name.setText(meterDetailsModel.getCNAME());
        tv_service_type.setText(meterDetailsModel.getSCTYPE());
        tv_cycle.setText("");
        tv_category.setText(meterDetailsModel.getCAT());
        tv_sub_category.setText("");
        tv_dtr_code.setText(meterDetailsModel.getDTRCD());

        tv_meter_make.setText(meterDetailsModel.getMTRMAKE());
        tv_meter_number.setText(meterDetailsModel.getMTNO());
        tv_meter_type.setText(meterDetailsModel.getMTTYPE());
        tv_meter_class.setText(meterDetailsModel.getMTCLASS());
        tv_meter_class.setText(meterDetailsModel.getMTCLASS());
        tv_meter_capacity.setText(meterDetailsModel.getMTCAPACITY());
        tv_meter_mf.setText(meterDetailsModel.getMTMF());
        et_meter_change_reason.setText(meterDetailsModel.getEXPCLRDG());

        tv_meter_closing_reading_kwh.setText(meterDetailsModel.getBLCLKWH());
        tv_meter_closing_reading.setText(meterDetailsModel.getBLCLKVAH());
        oldMisSolar = meterDetailsModel.getSNMFLAG();
        oldMPhase = meterDetailsModel.getMTCLASS();
        if(oldMisSolar.equalsIgnoreCase("Y") && oldMPhase.contains("1")){
            ll_meter_exp_kwh_closing_reading.setVisibility(View.VISIBLE);
            ll_meter_exp_kvah_closing_reading.setVisibility(View.GONE);
            tv_meter_exp_kwh_closing_reading.setText(meterDetailsModel.getEXPKWH());
            ll_final_reading_exp_kwh.setVisibility(View.VISIBLE);
            ll_final_reading_exp_kvah.setVisibility(View.GONE);
        }else if(oldMisSolar.equalsIgnoreCase("Y") && oldMPhase.contains("3")){
            ll_meter_exp_kwh_closing_reading.setVisibility(View.VISIBLE);
            ll_meter_exp_kvah_closing_reading.setVisibility(View.VISIBLE);
            tv_meter_exp_kwh_closing_reading.setText(meterDetailsModel.getEXPKWH());
            tv_meter_exp_kvah_closing_reading.setText(meterDetailsModel.getEXPKVAH());
            ll_final_reading_exp_kwh.setVisibility(View.VISIBLE);
            ll_final_reading_exp_kvah.setVisibility(View.VISIBLE);
        }else{
            ll_meter_exp_kwh_closing_reading.setVisibility(View.GONE);
            ll_meter_exp_kvah_closing_reading.setVisibility(View.GONE);
            ll_final_reading_exp_kwh.setVisibility(View.GONE);
            ll_final_reading_exp_kvah.setVisibility(View.GONE);
        }


        if (Double.parseDouble(meterDetailsModel.getBLCLKVAH()) == 0) {
            et_final_reading_kvah.setText("0");
//            et_final_reading_kvah.setFocusable(false);
//            et_final_reading_kvah.setCursorVisible(false);
        }
        if (Double.parseDouble(meterDetailsModel.getBLRMD()) == 0) {
            ll_rmd.setVisibility(View.GONE);
            et_rmd.setText("0");
        } else {
            ll_rmd.setVisibility(View.VISIBLE);
        }

        tv_meter_closing_date.setText(meterDetailsModel.getBLCLRDT());

        if (meterDetailsModel.getRQSTATUS().equalsIgnoreCase("VALID")) {
            tv_meter_closing_reading_status.setText("01");
        } else {
            tv_meter_closing_reading_status.setText("00");
        }
        tv_meter_closing_reading_status.setText(meterDetailsModel.getRQSTATUS());
        if (!Utility.isValueNullOrEmpty(meterDetailsModel.getLAT()) && !meterDetailsModel.getLAT().equalsIgnoreCase("0")) {
            mLastLocation = new Location(LocationManager.GPS_PROVIDER);
            mLastLocation.setLatitude(Double.parseDouble(meterDetailsModel.getLAT()));
            mLastLocation.setLongitude(Double.parseDouble(meterDetailsModel.getLONG()));
            tv_latitude.setText(String.format("%s", mLastLocation.getLatitude()));
            tv_longitude.setText(String.format("%s", mLastLocation.getLongitude()));
            mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                    .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                    .title("(" + tv_latitude.getText().toString() + "," + tv_longitude.getText().toString() + ")"));
            //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Intent intent = new Intent(MeterChangeEntryFormActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                    return false;
                }
            });
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent intent = new Intent(MeterChangeEntryFormActivity.this, ExploreLocalityActivity.class);
                    intent.putExtra(Constants.LATITUDE, mLastLocation.getLatitude());
                    intent.putExtra(Constants.LONGITUDE, mLastLocation.getLongitude());
                    startActivity(intent);
                }
            });
            ll_map.setVisibility(View.VISIBLE);
            ll_latitude.setVisibility(View.VISIBLE);
            ll_longitude.setVisibility(View.VISIBLE);
            buildGoogleApiClient();
        }
    }

    private void abNormalInitialKWH() {
        String msg = "Initial Reading(KWH) of the New Meter is high.Do you want to proceed";
        new android.app.AlertDialog.Builder(this)
                // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                .setMessage(msg)
                .setTitle("Alert")
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                String str = "Initial Reading(KWH) of the New Meter is high.Are you Sure you want to proceed!";
                                new android.app.AlertDialog.Builder(MeterChangeEntryFormActivity.this)
                                        // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                                        .setMessage(str)
                                        .setPositiveButton(R.string.alert_dialog_ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        ikwhProceed = true;
                                                        //validateFields();
                                                    }
                                                })
                                        .setNegativeButton(R.string.alert_dialog_cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        dialog.dismiss();
                                                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                                        et_final_reading_kwh.setText("");
                                                    }
                                                }).show();
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                        MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                et_final_reading_kwh.setText("");
                            }
                        }).show();
    }

    private void abNormalInitialKVAH() {
        String msg = "Initial Reading(KVAH) of the New Meter is high.Do you want to proceed";
        new android.app.AlertDialog.Builder(this)
                // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                .setMessage(msg)
                .setTitle("Alert")
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                String str = "Initial Reading(KVAH) of the New Meter is high.Are you Sure you want to proceed!";
                                new android.app.AlertDialog.Builder(MeterChangeEntryFormActivity.this)
                                        // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                                        .setMessage(str)
                                        .setPositiveButton(R.string.alert_dialog_ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        ikvahProceed = true;
                                                        //  validateFields();
                                                    }
                                                })
                                        .setNegativeButton(R.string.alert_dialog_cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        dialog.dismiss();
                                                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                                        et_final_reading_kwh.setText("");
                                                    }
                                                }).show();
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                        MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                et_final_reading_kwh.setText("");
                            }
                        }).show();
    }

    private void abnormalKVAH() {
        double oldmtrkwhrdg1 = Double.parseDouble(tv_meter_closing_reading.getText().toString());
        double oldmtrkwh = Double.parseDouble(et_final_reading_kvah.getText().toString());
        final double oldmtrConsumption = oldmtrkwh - oldmtrkwhrdg1;
        String msg = "Abnormal old meter consumption( " + oldmtrConsumption + " units).Do you want to proceed";
        new android.app.AlertDialog.Builder(this)
                // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                .setMessage(msg)
                .setTitle("Alert")
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                String str = "Abnormal old meter consumption( " + oldmtrConsumption + " units).Are you Sure you want to proceed!";
                                new android.app.AlertDialog.Builder(MeterChangeEntryFormActivity.this)
                                        // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                                        .setMessage(str)
                                        .setPositiveButton(R.string.alert_dialog_ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        kvahProceed = true;
                                                        //validateFields();
                                                    }
                                                })
                                        .setNegativeButton(R.string.alert_dialog_cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        dialog.dismiss();
                                                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                                        et_final_reading_kvah.setText("");
                                                    }
                                                }).show();
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                        MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                et_final_reading_kwh.setText("");
                            }
                        }).show();
    }

    private void abnormalKWH() {
        double oldmtrkwhrdg1 = Double.parseDouble(tv_meter_closing_reading_kwh.getText().toString());
        double oldmtrkwh = Double.parseDouble(et_final_reading_kwh.getText().toString());
        final double oldmtrConsumption = oldmtrkwh - oldmtrkwhrdg1;
        final String msg = "Abnormal old meter consumption( " + oldmtrConsumption + " units).Do you want to proceed";
        new android.app.AlertDialog.Builder(this)
                // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                .setMessage(msg)
                .setTitle("Alert")
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                String str = "Abnormal old meter consumption( " + oldmtrConsumption + " units).Are you Sure you want to proceed!";
                                new android.app.AlertDialog.Builder(MeterChangeEntryFormActivity.this)
                                        // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                                        .setMessage(str)
                                        .setPositiveButton(R.string.alert_dialog_ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        kwhProceed = true;
                                                        //  validateFields();
                                                    }
                                                })
                                        .setNegativeButton(R.string.alert_dialog_cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,
                                                                        int whichButton) {
                                                        dialog.dismiss();
                                                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                                        et_final_reading_kwh.setText("");
                                                    }
                                                }).show();
                            }
                        })
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                        MeterChangeEntryFormActivity.this, R.string.you_pressed_cancel));
                                et_final_reading_kwh.setText("");
                            }
                        }).show();
    }
    //SAP get Meter Details
    private void getOldMeterInfo(String scno) {
//        RequestParams params = new RequestParams();
//        params.put("SCNO", scno);
        prgDialog.show();
        prgDialog.setMessage("Please wait...");
        JSONObject requestObjPayLoad = new JSONObject();
        try{
            requestObjPayLoad = new JSONObject();
            requestObjPayLoad.put("Method_Name","GetServConnDtls");
            requestObjPayLoad.put("USCNO",scno);
            requestObjPayLoad.put("ServiceRequest",serviceRequestNo);
        }catch (Exception e){

        }

        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("URL", Constants.URL + Constants.FETCH_DETAILS);
        Utility.showLog("SCNO", scno);
        HttpEntity entity;
        try {
            entity = new StringEntity(requestObjPayLoad.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
        client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/ISU/MeterReplacement/GetServiceConnectionDetails/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseStr) {
                Utility.showLog("response", responseStr);
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {


                    JSONObject jsonObject = new JSONObject(responseStr);
                    jsonObject = jsonObject.getJSONObject("response");
                    String success = jsonObject.getString("success");

                    if(success.equalsIgnoreCase("True")){
                        meterDetailsModel = new MeterDetailsModel(
                                jsonObject.getString("mtrno"),
                                jsonObject.getString("oldmtrclosingrmd"),
                                jsonObject.getString("consclassdesc"),
                                jsonObject.getString("category"),
                                jsonObject.optString("mtrmf"),
                                jsonObject.optString("oldmtrclosingkwh"),
                                jsonObject.optString("dtr"),
                                jsonObject.optString("cusname"),
                                jsonObject.optString("mtrmake"),
                                jsonObject.optString("mtrmake"),
                                jsonObject.optString("oldmtrclosingreaddt"),
                                "",
                                "",
                                jsonObject.optString("uscno"),
                                jsonObject.optString("mtrtype"),
                                jsonObject.optString("oldmtrclosingreadst"),
                                jsonObject.optString("oldmtrclosingkvah"),
                                jsonObject.optString("oldmtrclosingreadst"),
                                jsonObject.optString("servicetype"),
                                jsonObject.optString("servicetype"),
                                "",
                                "",
                                jsonObject.optString("issolar"),
                                jsonObject.optString("servicerequestreason"),
                                jsonObject.optString("oldmtrclosingexpkwh"),
                                jsonObject.optString("oldmtrclosingexpkvah")
                        );
                        setData();
                    }else{
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Not a valid Service Number");
                    }
//                    if (jsonObject.has("MSG")) {
//                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Not a valid Service Number");
//                    } else {
//                        meterDetailsModel = new Gson().fromJson(jsonObject.toString(), MeterDetailsModel.class);
//                        setData();
//                    }
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
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    //SAP get Meter Details
    private void getNewMeterInfo(JSONObject requestObj) {
//        RequestParams params = new RequestParams();
//        params.put("SCNO", scno);
        prgDialog.show();
        prgDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();

        HttpEntity entity;
        try {
            entity = new StringEntity(requestObj.toString());
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this, "https://apcpcdcl-test-k5qoqm5y.it-cpi012-rt.cfapps.ap21.hana.ondemand.com/http/DepartmentalApp/SAPISU/MeterReplacement/NewMeterValidation/DEV", headers, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("response", responseStr);
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                    try {


                        JSONObject jsonObject = new JSONObject(responseStr);
                        jsonObject = jsonObject.getJSONObject("response");
                        if(jsonObject != null && !jsonObject.equals("")){
                            if (jsonObject.optString("success").equalsIgnoreCase("True")) {
                                sPhase = jsonObject.optString("phase");
                                sMeterType = jsonObject.optString("metertype");
                                isSolar = jsonObject.optString("issolar");

                                ll_new_meter_make.setVisibility(View.VISIBLE);
                                ll_new_meter_type.setVisibility(View.VISIBLE);
                                ll_new_meter_phase.setVisibility(View.VISIBLE);

                                et_meter_make.setText(jsonObject.optString("make"));
                                et_meter_type.setText(jsonObject.optString("metertype"));
                                et_meter_phase.setText(jsonObject.optString("phase"));
                                //et_meter_class.setText(jsonObject.optString("consclassdesc"));
                                //If phase 1


                                if (sPhase.equalsIgnoreCase("1")) {
                                    ll_new_meter_kwh.setVisibility(View.VISIBLE);
                                    if(isSolar.equalsIgnoreCase("Y")) {
                                        ll_new_meter_ex_kwh.setVisibility(View.VISIBLE);
                                    }else{
                                        ll_new_meter_ex_kwh.setVisibility(View.GONE);
                                    }
                                    ll_new_meter_kvah.setVisibility(View.GONE);
                                    ll_new_meter_rmd.setVisibility(View.VISIBLE);
                                    ll_new_meter_kava_lag_lead.setVisibility(View.GONE);
                                    ll_new_meter_reading_v.setVisibility(View.GONE);
                                    ll_new_meter_reading_i.setVisibility(View.GONE);
                                    ll_new_meter_seals_details.setVisibility(View.VISIBLE);
                                    ll_new_meter_wo_tc_body.setVisibility(View.GONE);
                                } else if (sPhase.equalsIgnoreCase("3") || sMeterType.equalsIgnoreCase("CT")){
                                    ll_new_meter_kwh.setVisibility(View.VISIBLE);
                                    ll_new_meter_kvah.setVisibility(View.VISIBLE);
                                    if(isSolar.equalsIgnoreCase("Y")) {
                                        ll_new_meter_ex_kwh.setVisibility(View.VISIBLE);
                                        ll_new_meter_ex_kvah.setVisibility(View.VISIBLE);
                                    }else{
                                        ll_new_meter_ex_kwh.setVisibility(View.GONE);
                                        ll_new_meter_ex_kvah.setVisibility(View.GONE);
                                    }
                                    ll_new_meter_rmd.setVisibility(View.VISIBLE);
                                    if (sMeterType.equalsIgnoreCase("CT"))
                                        ll_new_meter_kava_lag_lead.setVisibility(View.VISIBLE);
                                    else
                                        ll_new_meter_kava_lag_lead.setVisibility(View.GONE);
                                    ll_new_meter_reading_v.setVisibility(View.VISIBLE);
                                    ll_new_meter_reading_i.setVisibility(View.VISIBLE);
                                    ll_new_meter_seals_details.setVisibility(View.VISIBLE);
                                    if (sMeterType.equalsIgnoreCase("CT"))
                                        ll_new_meter_wo_tc_body.setVisibility(View.VISIBLE);
                                    else
                                        ll_new_meter_wo_tc_body.setVisibility(View.GONE);

                                }
                                el_new_meter_details.toggle();
                            }else{
                                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, jsonObject.getString("message"));
                            }

                            el_new_meter_details.toggle();
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
                            Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Unable to Connect Server");
                            break;
                        case 500:
                            Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Something went wrong at server end");
                            break;
                        default:
                            Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Check Your Internet Connection and Try Again");

                            break;
                    }
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void getMeterDetails(String scno) {
        RequestParams params = new RequestParams();
        params.put("SCNO", scno);
        prgDialog.show();
        prgDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("URL", Constants.URL + Constants.FETCH_DETAILS);
        Utility.showLog("SCNO", scno);
        // client.addHeader("SCNO", scno);
        client.post(Constants.URL + Constants.FETCH_DETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("MSG")) {
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Not a valid Service Number");
                    } else {
                        meterDetailsModel = new Gson().fromJson(jsonObject.toString(), MeterDetailsModel.class);
                        setData();
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
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    private void inLineValidations() {
        et_meter_change_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_meter_mfg_date.setText("");
                if (!meterDetailsModel.getNMTCHDT().equalsIgnoreCase("NA")) {
                    if (et_meter_change_date.getText().toString().equalsIgnoreCase(meterDetailsModel.getNMTCHDT())) {
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, "Meter change was already done on the same date for the service number");
                        et_meter_change_date.setText("");
                    }
                }
            }
        });
        et_old_meter_number.setOnFocusChangeListener(this);
        et_final_reading_kwh.setOnFocusChangeListener(this);
        et_final_reading_kvah.setOnFocusChangeListener(this);
        et_meter_change_slip_number.setOnFocusChangeListener(this);
        spn_meter_change_reason.setOnFocusChangeListener(this);
        et_meter_change_reason.setOnFocusChangeListener(this);
        spn_irda.setOnFocusChangeListener(this);
        et_meter_make.setOnFocusChangeListener(this);
        spn_meter_make.setOnFocusChangeListener(this);
        spn_meter_type.setOnFocusChangeListener(this);
        et_new_meter_number.setOnFocusChangeListener(this);
        spn_meter_phase.setOnFocusChangeListener(this);
        et_meter_capacity.setOnFocusChangeListener(this);
        et_meter_class.setOnFocusChangeListener(this);
        et_meter_digits.setOnFocusChangeListener(this);
        et_meter_mfg_date.setOnFocusChangeListener(this);
        et_initial_reading_kwh.setOnFocusChangeListener(this);
        et_initial_reading_kvah.setOnFocusChangeListener(this);
        et_mf.setOnFocusChangeListener(this);
        et_seal_one.setOnFocusChangeListener(this);
        et_seal_two.setOnFocusChangeListener(this);
        et_meter_tc_seals.setOnFocusChangeListener(this);
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
       /* if (!hasFocus){
            validateFields();
        }*/
        if (!hasFocus) {
            switch (view.getId()) {
                case R.id.et_old_meter_number:
                    if (Utility.isValueNullOrEmpty(et_meter_change_date.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_change_date));
                        if (!el_meter_change_particulars.isExpanded()) {
                            el_meter_change_particulars.toggle();
                        }
                        sv_main.requestChildFocus(et_meter_change_date, et_meter_change_date);
                    } else if (Utility.isValueNullOrEmpty(et_old_meter_number.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                MeterChangeEntryFormActivity.this, R.string.err_enter_old_meter_number));
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    } else if (et_old_meter_number.getText().toString().length() < 5) {
                        Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                MeterChangeEntryFormActivity.this, R.string.err_old_meter_number_lees_than_five));
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    } else if (!meterNumberProceed && !et_old_meter_number.getText().toString().equalsIgnoreCase(meterDetailsModel.getMTNO())) {
                     /*   Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this, Utility.getResourcesString(
                                MeterChangeEntryFormActivity.this, R.string.err_enter_valid_old_meter_number));*/
                        verifyOldMeterNumber();
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    }
                    break;
                case R.id.et_final_reading_kwh:
                    if (Utility.isValueNullOrEmpty(et_final_reading_kwh.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_old_meter_kwh_reading));
                        sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
                    } else if (Double.parseDouble(et_final_reading_kwh.getText().toString()) < Double.parseDouble(meterDetailsModel.getBLCLKWH())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_final_reading_cant_be_less_kwh));
                        sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
                    } else if (!kwhProceed &&
                            Double.parseDouble(et_final_reading_kwh.getText().toString()) > Double.parseDouble(meterDetailsModel.getBLCLKWH()) + 3000) {
                        abnormalKWH();
                    }
                    break;
                case R.id.et_final_reading_kvah:
                    if (meterDetailsModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                            Utility.isValueNullOrEmpty(et_final_reading_kvah.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_old_meter_kvah_reading));
                        sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
                    } else if (meterDetailsModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                            Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(meterDetailsModel.getBLCLKVAH())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_final_reading_cant_be_less_kvah));
                        sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
                    } else if (!kvahProceed && meterDetailsModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                            Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(meterDetailsModel.getBLCLKVAH()) + 3000) {
                        abnormalKVAH();
                    }
                    break;
                case R.id.et_meter_change_slip_number:
                    if (Utility.isValueNullOrEmpty(et_meter_change_slip_number.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_meter_change_slip_number_cant_be_empty));
                        sv_main.requestChildFocus(et_meter_change_slip_number, et_meter_change_slip_number);
                    }
                    break;
                case R.id.spn_meter_change_reason:
                    if (Utility.isValueNullOrEmpty(mMeterChangeReason)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_meter_change_reason));
                        sv_main.requestChildFocus(spn_meter_change_reason, spn_meter_change_reason);
                    } else if (mMeterChangeReason.equalsIgnoreCase("Others") &&
                            Utility.isValueNullOrEmpty(et_meter_change_reason.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_change_reason));
                        sv_main.requestChildFocus(et_meter_change_reason, et_meter_change_reason);
                    }
                    break;
                case R.id.et_meter_change_reason:
                    if (Utility.isValueNullOrEmpty(mMeterChangeReason)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_meter_change_reason));
                        sv_main.requestChildFocus(spn_meter_change_reason, spn_meter_change_reason);
                    } else if (mMeterChangeReason.equalsIgnoreCase("Others") &&
                            Utility.isValueNullOrEmpty(et_meter_change_reason.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_change_reason));
                        sv_main.requestChildFocus(et_meter_change_reason, et_meter_change_reason);
                    }
                    break;
                case R.id.spn_irda:
                case R.id.et_meter_make:
//                    if (Utility.isValueNullOrEmpty(mIRDA)) {
//                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                                this, R.string.err_select_irda_flag));
//                        sv_main.requestChildFocus(spn_irda, spn_irda);
//                    } else
                    if (mIRDA.equalsIgnoreCase("Yes") && Utility.isValueNullOrEmpty(mMeterMake)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_new_meter_make));
                        sv_main.requestChildFocus(spn_meter_make, spn_meter_make);
                    } else if (mIRDA.equalsIgnoreCase("No") && Utility.isValueNullOrEmpty(et_meter_make.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_new_meter_make_cant_be_empty));
                        sv_main.requestChildFocus(et_meter_make, et_meter_make);
                    } else if (mIRDA.equalsIgnoreCase("No") && Utility.isValueNullOrEmpty(mMeterType)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_new_meter_type));
                        sv_main.requestChildFocus(spn_meter_type, spn_meter_type);
                    }
                    break;
                case R.id.et_new_meter_number:
//                    if (Utility.isValueNullOrEmpty(mIRDA)) {
//                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                                this, R.string.err_select_irda_flag));
//                        sv_main.requestChildFocus(spn_irda, spn_irda);
//                    } else
                    if (mIRDA.equalsIgnoreCase("Yes") && Utility.isValueNullOrEmpty(mMeterMake)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_new_meter_make));
                        sv_main.requestChildFocus(spn_meter_make, spn_meter_make);
                    } else if (mIRDA.equalsIgnoreCase("No") && Utility.isValueNullOrEmpty(et_meter_make.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_new_meter_make_cant_be_empty));
                        sv_main.requestChildFocus(et_meter_make, et_meter_make);
                    } else if (mIRDA.equalsIgnoreCase("No") && Utility.isValueNullOrEmpty(mMeterType)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_new_meter_type));
                        sv_main.requestChildFocus(spn_meter_type, spn_meter_type);
                    } else if (Utility.isValueNullOrEmpty(et_new_meter_number.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_new_meter_number_cant_be_empty));
                        sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
                    }
//                    else if (et_new_meter_number.getText().toString().length() < 5) {
//                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
//                                this, R.string.err_valid_new_meter_number));
//                        sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
//                    }
                    else if (et_new_meter_number.getText().toString().equalsIgnoreCase(meterDetailsModel.getMTNO())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_valid_new_meter_number));
                        sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
                    }
                    break;
                case R.id.et_meter_capacity:
                    if (Utility.isValueNullOrEmpty(et_meter_capacity.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_new_meter_capacity_cant_be_empty));
                        sv_main.requestChildFocus(et_meter_capacity, et_meter_capacity);
                    }
                    break;
                case R.id.et_meter_class:
                    if (Utility.isValueNullOrEmpty(et_meter_class.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_new_meter_class_cant_be_empty));
                        sv_main.requestChildFocus(et_meter_class, et_meter_class);
                    }
                    break;
                case R.id.et_meter_digits:
                    if (Utility.isValueNullOrEmpty(et_meter_digits.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_meter_digits_cant_be_empty));
                        sv_main.requestChildFocus(et_meter_digits, et_meter_digits);
                    }
                    break;
                case R.id.et_meter_mfg_date:
                    if (Utility.isValueNullOrEmpty(et_meter_mfg_date.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_manufacture_date));
                        sv_main.requestChildFocus(et_meter_mfg_date, et_meter_mfg_date);
                    }
                    break;
                case R.id.et_initial_reading_kwh:
                    if (Utility.isValueNullOrEmpty(et_meter_mfg_date.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_manufacture_date));
                        sv_main.requestChildFocus(et_meter_mfg_date, et_meter_mfg_date);
                    } else if (Utility.isValueNullOrEmpty(et_initial_reading_kwh.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_reading_kwh));
                        sv_main.requestChildFocus(et_initial_reading_kwh, et_initial_reading_kwh);
                    } else if (!ikwhProceed && Double.parseDouble(et_initial_reading_kwh.getText().toString()) > 10) {
                        abNormalInitialKWH();
                    }
                    break;
                case R.id.et_initial_reading_kvah:
                    if (Utility.isValueNullOrEmpty(et_initial_reading_kvah.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_reading_kvah));
                        sv_main.requestChildFocus(et_initial_reading_kvah, et_initial_reading_kvah);
                    } else if (!ikvahProceed && Double.parseDouble(et_initial_reading_kvah.getText().toString()) > 10) {
                        abNormalInitialKVAH();
                    }
                    break;
                case R.id.et_mf:
                    if (Utility.isValueNullOrEmpty(et_mf.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_mf));
                        sv_main.requestChildFocus(et_mf, et_mf);
                    } else if (et_mf.getText().toString().equalsIgnoreCase("0")
                            || et_mf.getText().toString().equalsIgnoreCase("00")) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_valid_meter_mf));
                        sv_main.requestChildFocus(et_mf, et_mf);
                    }
                    break;
                case R.id.et_seal_one:
                    if (Utility.isValueNullOrEmpty(et_seal_one.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_seal_one));
                        sv_main.requestChildFocus(et_seal_one, et_seal_one);
                    }
                    break;
                case R.id.et_seal_two:
                    if (Utility.isValueNullOrEmpty(et_seal_two.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_seal_two));
                        sv_main.requestChildFocus(et_seal_two, et_seal_two);
                    }
                    break;
                case R.id.et_meter_tc_seals:
                    if (Utility.isValueNullOrEmpty(et_meter_tc_seals.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_seal_tc));
                        sv_main.requestChildFocus(et_meter_tc_seals, et_meter_tc_seals);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void verifyOldMeterNumber() {
        new android.app.AlertDialog.Builder(this)
                // .setMobile_icon_code(android.R.attr.alertDialogIcon)
                .setMessage(Utility.getResourcesString(MeterChangeEntryFormActivity.this,
                        R.string.err_enter_valid_old_meter_number))
                .setTitle("Alert")
                .setNeutralButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                                meterNumberProceed = true;
                                // validateFields();
                            }

                }).show();
    }

    private void getFinalUrlFromFields() {
        MeterChangeEntryModel meterChangeModel = new MeterChangeEntryModel();
        meterChangeModel.setServiceNo(et_service_number.getText().toString());
        meterChangeModel.setMtrchgDt(et_meter_change_date.getText().toString());
        meterChangeModel.setOldmtrno(et_old_meter_number.getText().toString());
        meterChangeModel.setOldmtrkwh(et_final_reading_kwh.getText().toString());
        meterChangeModel.setOldmtrkvah(et_final_reading_kvah.getText().toString());
        meterChangeModel.setMtrchfslip(et_meter_change_slip_number.getText().toString());
        if (mMeterChangeReason.equalsIgnoreCase("Others")) {
            meterChangeModel.setNewremarks(et_meter_change_reason.getText().toString());
        } else {
            meterChangeModel.setNewremarks(mMeterChangeReason);
        }
        meterChangeModel.setOldmtrrmd(et_rmd.getText().toString());
        meterChangeModel.setNewmtrIRDA(mIRDA);
        if (mIRDA.equalsIgnoreCase("Yes")) {
            meterChangeModel.setNewmtrmake(mMeterMake.replace("&", "N"));
            meterChangeModel.setNewmtrType(et_meter_type.getText().toString());
        } else {
            meterChangeModel.setNewmtrType(mMeterType);
            meterChangeModel.setNewmtrmake(et_meter_make.getText().toString().replace("&", "N"));
        }
        meterChangeModel.setNewmtrno(et_new_meter_number.getText().toString());
        meterChangeModel.setNewmtrphase(mMeterPhase);
        meterChangeModel.setNewmtrcurrent(et_meter_capacity.getText().toString());
        meterChangeModel.setNewmtrclass(et_meter_class.getText().toString());
        meterChangeModel.setNewmtrDigits(et_meter_digits.getText().toString());
        meterChangeModel.setNewmtrmfgdt(et_meter_mfg_date.getText().toString());
        meterChangeModel.setNewmtrkwh(et_initial_reading_kwh.getText().toString());
        meterChangeModel.setNewmtrkvah(et_initial_reading_kvah.getText().toString());
        meterChangeModel.setNewmtrmf(et_mf.getText().toString());
        meterChangeModel.setNewmtrMRTSeal1(et_seal_one.getText().toString());
        if (Utility.isValueNullOrEmpty(et_seal_two.getText().toString())) {
            meterChangeModel.setNewmtrMRTSeal2("NA");
        } else {
            meterChangeModel.setNewmtrMRTSeal2(et_seal_two.getText().toString());
        }
        if (Utility.isValueNullOrEmpty(et_seal_three.getText().toString())) {
            meterChangeModel.setNewmtrMRTSeal3("NA");
        } else {
            meterChangeModel.setNewmtrMRTSeal3(et_seal_three.getText().toString());
        }
        if (Utility.isValueNullOrEmpty(et_seal_four.getText().toString())) {
            meterChangeModel.setNewmtrMRTSeal4("NA");
        } else {
            meterChangeModel.setNewmtrMRTSeal4(et_seal_four.getText().toString());
        }
        meterChangeModel.setNewmtrSeal(et_meter_tc_seals.getText().toString());
        meterChangeModel.setLoginUserName(user);
        meterChangeModel.setImei(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        meterChangeModel.setUnitCode(sectionCode);
        meterChangeModel.setLatitude(tv_latitude.getText().toString());
        meterChangeModel.setLongitude(tv_longitude.getText().toString());
        String finalString = meterChangeModel.getServiceNo() + "|" + meterChangeModel.getMtrchgDt() + "|" +
                meterChangeModel.getOldmtrno() + "|" + meterChangeModel.getOldmtrkwh() + "|" +
                meterChangeModel.getOldmtrkvah() + "|" + meterChangeModel.getMtrchfslip() + "|" +
                meterChangeModel.getNewremarks() + "|" + meterChangeModel.getOldmtrrmd() + "|" +
                meterChangeModel.getNewmtrIRDA() + "|" + meterChangeModel.getNewmtrmake() + "|" +
                meterChangeModel.getNewmtrType() + "|" + meterChangeModel.getNewmtrno() + "|" +
                meterChangeModel.getNewmtrphase() + "|" + meterChangeModel.getNewmtrcurrent() + "|" +
                meterChangeModel.getNewmtrclass() + "|" + meterChangeModel.getNewmtrDigits() + "|" +
                meterChangeModel.getNewmtrmfgdt() + "|" + meterChangeModel.getNewmtrkwh() + "|" +
                meterChangeModel.getNewmtrkvah() + "|" + meterChangeModel.getNewmtrmf() + "|" +
                meterChangeModel.getNewmtrMRTSeal1() + "|" + meterChangeModel.getNewmtrMRTSeal2() + "|" +
                meterChangeModel.getNewmtrMRTSeal3() + "|" + meterChangeModel.getNewmtrMRTSeal4() + "|" +
                meterChangeModel.getNewmtrSeal() + "|" + meterChangeModel.getLoginUserName() + "|" +
                meterChangeModel.getImei() + "|" + meterChangeModel.getUnitCode() + "|" + meterChangeModel.getLatitude() + "|" + meterChangeModel.getLongitude();
        meterChangeModel.setFinalDataString(finalString);
        JSONObject requestObj = new JSONObject();
        HttpEntity entity = null;
        try{
            requestObj.put("USCNO", meterChangeModel.getServiceNo());
            requestObj.put("MeterChangeDt", meterChangeModel.getMtrchgDt());
            requestObj.put("ServiceRequest",serviceRequestNo);
            requestObj.put("OldMtrMake", tv_meter_make.getText().toString());
            requestObj.put("OldMtrNo",  tv_meter_number.getText().toString());
            requestObj.put("IncorrectMeter", "");
            requestObj.put("OldMtrSolar", "");
            requestObj.put("OldMtrType", tv_meter_type.getText().toString());
            requestObj.put("OldMtrKwh", meterChangeModel.getOldmtrkwh());
            requestObj.put("OldMtrKvah", meterChangeModel.getOldmtrkvah());
            requestObj.put("OldMtrRMD", (meterChangeModel.getOldmtrrmd().equalsIgnoreCase("")) ? ("0"):(meterChangeModel.getOldmtrrmd()));
            if(isSolar.equalsIgnoreCase("Y")) {
                requestObj.put("OldMtrExpKwh", et_final_reading_exp_kwh.getText().toString());
                requestObj.put("OldMtrExpKvah", (et_final_reading_exp_kvah.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_final_reading_exp_kvah.getText().toString()));
            }else{
                requestObj.put("OldMtrExpKwh","0");
                requestObj.put("OldMtrExpKvah", "0");
            }
            requestObj.put("NewMtrNo", meterChangeModel.getNewmtrno());
//            requestObj.put("NewMtrKwh", meterChangeModel.getNewmtrkwh());
//            requestObj.put("NewMtrKvah", meterChangeModel.getNewmtrkvah());
//            requestObj.put("NewMtrRMD", "3.00");
//            requestObj.put("NewMtrExpKwh", "10");
//            requestObj.put("NewMtrExpKvah", "");
            if(sPhase.equalsIgnoreCase("3") || sMeterType.equalsIgnoreCase("CT")) {
                requestObj.put("NewMtrKwh", et_new_meter_kwh.getText().toString());
                requestObj.put("NewMtrKvah", (et_new_meter_kvah.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_new_meter_kvah.getText().toString()));
            }else{
                if(sPhase.equalsIgnoreCase("1")) {
                    requestObj.put("NewMtrKwh", et_new_meter_kwh.getText().toString());
                    requestObj.put("NewMtrKvah", "0");
                }
            }
            requestObj.put("NewMtrRMD" , (et_new_meter_rmd.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_new_meter_rmd.getText().toString()));
            if(isSolar.equalsIgnoreCase("Y")) {
                requestObj.put("NewMtrExpKwh", et_new_meter_ex_kwh.getText().toString());
                requestObj.put("NewMtrExpKvah", (et_new_meter_ex_kvah.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_new_meter_ex_kvah.getText().toString()));
            }else{
                requestObj.put("NewMtrExpKwh", "0");
                requestObj.put("NewMtrExpKvah", "0");
            }
            requestObj.put("Mtrseals", "");
            requestObj.put("Terminalseals", "");
            requestObj.put("ChangeSlipNo", meterChangeModel.getMtrchfslip());
            requestObj.put("Status", tv_meter_closing_reading_status.getText().toString());
            requestObj.put("Message", et_meter_change_reason.getText().toString());
            requestObj.put("OldMtrExpRMD", "0");
            requestObj.put("OldMtrKvarhLag", "0");
            requestObj.put("OldMtrKvarhLead", "0");
            requestObj.put("OldMtrVR", "0");
            requestObj.put("OldMtrVY", "0");
            requestObj.put("OldMtrVB", "0");
            requestObj.put("OldMtrIR", "0");
            requestObj.put("OldMtrIY", "0");
            requestObj.put("OldMtrIB", "0");

            requestObj.put("NewMtrExpRMD", "0");
//            requestObj.put("NewMtrKvarhLag", "");
//            requestObj.put("NewMtrKvarhLead", "");
//            requestObj.put("NewMtrVR", "1");
//            requestObj.put("NewMtrVY", "1");
//            requestObj.put("NewMtrVB", "1");
//            requestObj.put("NewMtrIR", "1");
//            requestObj.put("NewMtrIY", "1");
//            requestObj.put("NewMtrIB", "1");
//
//            requestObj.put("MTR_SEAL_TC", "AZ09345");
//            requestObj.put("MTR_SEAL_METER_BODY", "I34566");
//            requestObj.put("MTR_SEAL_METERBOX", "");
//            requestObj.put("MTR_SEAL_METER_BOARD", "");
//            requestObj.put("MTR_SEAL_MRI", "");
//            requestObj.put("MTR_SEAL_MRI", "");
//            requestObj.put("MTR_SEAL_CT_BOX", "");
//            requestObj.put("MTR_SEAL_MD_RESET", "");


            if(sMeterType.equalsIgnoreCase("CT")) {
                requestObj.put("NewMtrKvarhLag", et_new_meter_kvah_lag.getText().toString());
                requestObj.put("NewMtrKvarhLead", ed_new_meter_kvah_lead.getText().toString());
            }else{
                requestObj.put("NewMtrKvarhLag", "0");
                requestObj.put("NewMtrKvarhLead", "0");
            }
            if(sPhase.equalsIgnoreCase("3") || sMeterType.equalsIgnoreCase("CT")) {
                requestObj.put("NewMtrVR", (et_vr.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_vr.getText().toString()));
                requestObj.put("NewMtrVY", (et_vy.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_vy.getText().toString()));
                requestObj.put("NewMtrVB", (et_vb.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_vb.getText().toString()));
                requestObj.put("NewMtrIR", (et_ir.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_ir.getText().toString()));
                requestObj.put("NewMtrIY", (et_iy.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_iy.getText().toString()));
                requestObj.put("NewMtrIB", (et_ib.getText().toString().equalsIgnoreCase("")) ? ("0"):(et_ib.getText().toString()));
            }else{
                requestObj.put("NewMtrVR", "0");
                requestObj.put("NewMtrVY", "0");
                requestObj.put("NewMtrVB", "0");
                requestObj.put("NewMtrIR", "0");
                requestObj.put("NewMtrIY", "0");
                requestObj.put("NewMtrIB", "0");
            }
            if(sMeterType.equalsIgnoreCase("CT")) {
                requestObj.put("MTR_SEAL_TC" , et_new_meter_tc.getText().toString());
                requestObj.put("MTR_SEAL_METER_BODY" , et_new_meter_board.getText().toString());
                requestObj.put("MTR_SEAL_METERBOX", et_new_meter_ctbox.getText().toString());
                requestObj.put("MTR_SEAL_METER_BOARD", et_new_meter_board.getText().toString());
                requestObj.put("MTR_SEAL_MRI", et_new_meter_mri.getText().toString());
                requestObj.put("MTR_SEAL_MRI", et_new_meter_ttb.getText().toString());
                requestObj.put("MTR_SEAL_CT_BOX", et_new_meter_ctbox.getText().toString());
                requestObj.put("MTR_SEAL_MD_RESET", et_new_meter_md_reset.getText().toString());
            }else{
                requestObj.put("MTR_SEAL_TC" , et_new_meter_tc.getText().toString());
                requestObj.put("MTR_SEAL_METER_BODY" , et_new_meter_body.getText().toString());
                requestObj.put("MTR_SEAL_METERBOX", "");
                requestObj.put("MTR_SEAL_METER_BOARD", "");
                requestObj.put("MTR_SEAL_MRI", "");
                requestObj.put("MTR_SEAL_MRI", "");
                requestObj.put("MTR_SEAL_CT_BOX", "");
                requestObj.put("MTR_SEAL_MD_RESET", "");
            }
            requestObj.put("OldMeterImage", base64_oldmeterimg);
            requestObj.put("NewMeterImage", base64_newmeterimg);
            entity = new StringEntity(requestObj.toString());

        }catch(Exception e){
            e.printStackTrace();
        }
        if (Utility.isNetworkAvailable(this)) {
            Utility.showLog("finalString", finalString);
            RequestParams params = new RequestParams();
            params.put("INPUT", finalString);
            AsyncHttpClient client = new AsyncHttpClient();
            Utility.showLog("URL", ServiceConstants.USER_METER_CHANGE_DATA_SAVE);
            Utility.showLog("SCNO", finalString);
            BasicHeader[] headers = new BasicHeader[]{new BasicHeader("Authorization", "Basic "+AppPrefs.getInstance(getApplicationContext()).getString("USER_AUTH", ""))};
            client.post(this,ServiceConstants.USER_METER_CHANGE_DATA_SAVE,headers, entity,"application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String responseStr) {
                    Utility.showLog("onSuccess", responseStr);
                    Utility.showLog("response", responseStr);
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                    try {
                        JSONObject responseObj = new JSONObject(responseStr);
                        responseObj = responseObj.getJSONObject("response");
                        String status = responseObj.getString("success");
                        if (responseObj.has("success")) {
                            if (status.equalsIgnoreCase("True")) {
                                showCustomDialog(MeterChangeEntryFormActivity.this, responseObj.optString("message"), true);

                            } else {
                                Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this,responseObj.optString("message"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Throwable error, String content) {
                    Utility.showLog("error", error.toString());
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                }
            });
        } else {
            Utility.showCustomOKOnlyDialog(MeterChangeEntryFormActivity.this,"Please check internate connection");
//            meterChangeModel.setStatus("Pending");
//            meterChangeModel.setMsg("Pending");
//            MeterChangeDatabaseHandler db = new MeterChangeDatabaseHandler(this);
//            db.saveModel(meterChangeModel);
//            if (prgDialog != null && prgDialog.isShowing()) {
//                prgDialog.dismiss();
//            }
//            Utility.showToastMessage(MeterChangeEntryFormActivity.this,
//                    "As You are Offline Data saved in Database.");
//            finish();
//            if (!isList) {
//                Intent intent = new Intent(MeterChangeEntryFormActivity.this, MeterChangeEntryFormActivity.class);
//                startActivity(intent);
//            }
        }
    }

    private void showCustomDialog(final Context context, String message, final boolean isSuccess) {
        if (!Constants.isDialogOpen) {
            Constants.isDialogOpen = true;
            final Dialog dialog_confirm = new Dialog(context);
            dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_confirm.setContentView(R.layout.dialog_layout);
            WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
            Window window1 = dialog_confirm.getWindow();
            if (window1 != null)
                lp1.copyFrom(window1.getAttributes());
            lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
            assert window1 != null;
            window1.setAttributes(lp1);
            Button btn_ok = (Button) dialog_confirm.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) dialog_confirm.findViewById(R.id.btn_cancel);
            View view_divider = (View) dialog_confirm.findViewById(R.id.view_divider);
            if (isSuccess) {
                btn_cancel.setVisibility(View.GONE);
                view_divider.setVisibility(View.GONE);
            }
            TextView txt_msz = (TextView) dialog_confirm.findViewById(R.id.txt_heading);
            txt_msz.setText(message);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_service_number.setText("");
                    if (isSuccess) {
                        if (!isList) {
                            Intent intent = new Intent(MeterChangeEntryFormActivity.this, MeterChangeEntryFormActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    } else if (dbManager.getMeterChangeRequestsCount() > 0) {
                        Intent intent = new Intent(context, OfflineMeterChangeListActivity.class);
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, OfflineMeterChangeRequestFormActivity.class);
                        intent.putExtra(Constants.FROM, TAG);
                        context.startActivity(intent);
                    }
                    dialog_confirm.dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_confirm.dismiss();
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

    @Override
    protected void onResume() {
        super.onResume();
        dbManager = new MeterChangeDatabaseHandler(this);
        Utility.showLog("Excluding failed requests Count", "" + dbManager.getMeterChangeRequestsCountExcludingFail());
        Utility.showLog("Count", "" + dbManager.getMeterChangeRequestsCount());
        try {
            if (dbManager.getMeterChangeRequestsCountExcludingFail() > 0) {
                Utility.showLog("Excluding failed requests Count", "" + dbManager.getMeterChangeRequestsCountExcludingFail());
                Utility.showLog("Count", "" + dbManager.getMeterChangeRequestsCount());
                IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
                networkChangeReceiver = new NetworkChangeReceiver();
                this.registerReceiver(networkChangeReceiver, intentFilter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }

    }

    private void getDeviceId() {
       /* TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        if (Utility.isMarshmallowOS()) {
            PackageManager pm = this.getPackageManager();
            int hasWritePerm = pm.checkPermission(
                    Manifest.permission.READ_PHONE_STATE,
                    this.getPackageName());

            if (hasWritePerm != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_IMEI) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
                Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER, manager.getDeviceId());
                Utility.showLog("IMEI", manager.getDeviceId());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkChangeReceiver != null) {
            this.unregisterReceiver(networkChangeReceiver);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
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
                Utility.showToastMessage(this, Utility.getResourcesString(MeterChangeEntryFormActivity.this, R.string.no_geocoder_available));
            } else {
                mMap.clear();
                tv_latitude.setText(String.format("%s", mLastLocation.getLatitude()));
                tv_longitude.setText(String.format("%s", mLastLocation.getLongitude()));
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin))
                        .position(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()))
                        .title("(" + tv_latitude.getText().toString() + "," + tv_longitude.getText().toString() + ")"));
                //   mMap.animateCamera(CameraUpdateFactory.zoomTo(13.5f));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 15));
                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                ll_map.setVisibility(View.VISIBLE);
                ll_latitude.setVisibility(View.VISIBLE);
                ll_longitude.setVisibility(View.VISIBLE);
                btn_submit.setVisibility(View.VISIBLE);
                buildGoogleApiClient();
            }

        } else {
            checkPermissionsAndCallServiceToGetLatAndLng();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
                    prgDialog.show();
                    if (Utility.isLocationEnabled(this)) {
                        if (Utility.isNetworkAvailable(this)) {
                            createLocationRequest();
                        } else {
                            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
                        }
                    } else {
                        if (prgDialog != null && prgDialog.isShowing()) {
                            prgDialog.dismiss();
                        }
                        createLocationRequest();
                    }
                }
            } else {
                prgDialog.show();
                if (Utility.isLocationEnabled(this)) {
                    if (Utility.isNetworkAvailable(this)) {
                        createLocationRequest();
                    } else {
                        Utility.showCustomOKOnlyDialog(this,
                                Utility.getResourcesString(this,
                                        R.string.no_internet));
                    }
                } else {
                    if (prgDialog != null && prgDialog.isShowing()) {
                        prgDialog.dismiss();
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

    protected void createLocationRequest() {
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
                            status.startResolutionForResult(MeterChangeEntryFormActivity.this, REQUEST_CHECK_SETTINGS);
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
}
