package com.apcpdcl.departmentapp.activities;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
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
import com.apcpdcl.departmentapp.models.MeterChangeModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseena
 * on 23-02-2018.
 */

public class   MeterChangeModifyFormActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    @BindView(R.id.tv_consumer_name)
    TextView tv_consumer_name;
    @BindView(R.id.tv_service_no)
    TextView tv_service_no;
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
    @BindView(R.id.ll_details)
    LinearLayout ll_details;
    @BindView(R.id.ll_rmd)
    LinearLayout ll_rmd;
    @BindView(R.id.et_rmd)
    EditText et_rmd;

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
    private MeterChangeModel mMeterChangeModel;
    private ProgressDialog prgDialog;
    private String mServiceNum = "";
    private String mMeterChgDate = "";
    private String mNewMtrNum = "";
    private String mUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_change_modify_form_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        mUser = prefs.getString("USER", "");
        mServiceNum = getIntent().getStringExtra(Constants.USCNO);
        mMeterChgDate = getIntent().getStringExtra(Constants.MTRCHGDT);
        mNewMtrNum = getIntent().getStringExtra(Constants.NEWMTRNO);
        setExpandableButtonAnimators(el_master_details, iv_master_details);
        el_meter_change_particulars.collapse();
        setExpandableButtonAnimators(el_meter_change_particulars, iv_meter_change_particulars);
        setExpandableButtonAnimators(el_new_meter_details, iv_new_meter_details);
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.GET_METER_MAKE_LIST))) {
            Utility.getMeterMake(this);
        }
        Utility.showLog("IMEI", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        prgDialog = new ProgressDialog(this);

        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        prgDialog.show();
        if (Utility.isNetworkAvailable(this)) {
            getMeterDetails();
        }
        setAshtrikColor();
        inLineValidations();
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
            if (Utility.isNetworkAvailable(this)) {
                getFinalUrlFromFields();
            } else {
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(this,
                        Utility.getResourcesString(this,
                                R.string.no_internet));
            }
        } else {
            if (prgDialog != null && prgDialog.isShowing()) {
                prgDialog.dismiss();
            }
        }
    }


    @OnClick(R.id.et_meter_change_date)
    void openDatePicker() {
        DatePickerFragment date = new DatePickerFragment(et_meter_change_date, mMeterChangeModel.getBLCLRDT());
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
    private void setExpandableButtonAnimators(ExpandableRelativeLayout expandableRelativeLayout, final ImageView imageView) {
        expandableRelativeLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(imageView, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(imageView, 180f, 0f).start();
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
        try {
            int i = list.indexOf(mMeterChangeModel.getPROPOSEDREM());
            if (i == -1) {
                spn_meter_change_reason.setSelection(list.indexOf("Others"));
                et_meter_change_reason.setText(mMeterChangeModel.getPROPOSEDREM());
            } else {
                spn_meter_change_reason.setSelection(list.indexOf(mMeterChangeModel.getPROPOSEDREM()));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

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
                    Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                            MeterChangeModifyFormActivity.this, R.string.err_meter_change_slip_number_cant_be_empty));
                    sv_main.requestChildFocus(et_meter_change_slip_number, et_meter_change_slip_number);
                }
                isFirstTime[0] = false;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                if (!isFirstTime[0] && Utility.isValueNullOrEmpty(et_meter_change_slip_number.getText().toString())) {
                    Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                            MeterChangeModifyFormActivity.this, R.string.err_meter_change_slip_number_cant_be_empty));
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
        if (mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("Yes")) {
            spn_irda.setSelection(1);
        } else {
            spn_irda.setSelection(2);
        }

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

                }
                if (mIRDA.equals("No")) {
                    et_meter_make.setVisibility(View.VISIBLE);
                    spn_meter_type.setVisibility(View.VISIBLE);
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
        if (mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("Yes")) {
            int i = list.indexOf(mMeterChangeModel.getMTRMAKE());
            if (i != -1)
                spn_meter_make.setSelection(i);
        }
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

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("--Select--");
        arrayList.add("1-MECHANIC");
        arrayList.add("2-ELECTRON");
        arrayList.add("3-HAMECHAN");
        arrayList.add("4-OLMECHAN");
        arrayList.add("5-LTCTMTR");
        arrayList.add("6-HTCTMTR");

        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("MECHANICAL");
        list.add("ELECTRONIC");
        list.add("HIGH ACCURACY");
        list.add("OLD MECHANICAL");
        list.add("LTCMETER");
        list.add("HTCMETER");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_type.setAdapter(newlineAdapter);
        if (mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("No")) {
            spn_meter_type.setSelection(arrayList.indexOf(mMeterChangeModel.getMTTYPE()));
        }
        spn_meter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMeterType = arrayList.get(position);
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
        spn_meter_phase.setSelection(list.indexOf(mMeterChangeModel.getMTRPHASE()));
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
        } else if (et_old_meter_number.getText().toString().length() < 5) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_old_meter_number_lees_than_five));
            sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
            return false;
        } else if (!meterNumberProceed && !et_old_meter_number.getText().toString().equalsIgnoreCase(mMeterChangeModel.getMTNO())) {
            verifyOldMeterNumber();
            return false;
        } else if (Utility.isValueNullOrEmpty(et_final_reading_kwh.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_old_meter_kwh_reading));
            sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
            return false;
        } else if (Double.parseDouble(et_final_reading_kwh.getText().toString()) < Double.parseDouble(mMeterChangeModel.getBLCLKWH())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_final_reading_cant_be_less_kwh));
            sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
            return false;
        } else if (!kwhProceed &&
                Double.parseDouble(et_final_reading_kwh.getText().toString()) > Double.parseDouble(mMeterChangeModel.getBLCLKWH()) + 3000) {
            abnormalKWH();
            return false;
        } else if (mMeterChangeModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                Utility.isValueNullOrEmpty(et_final_reading_kvah.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_old_meter_kvah_reading));
            sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
            return false;
        } else if (mMeterChangeModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(mMeterChangeModel.getBLCLKVAH())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_final_reading_cant_be_less_kvah));
            sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
            return false;
        } else if (!kvahProceed && mMeterChangeModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(mMeterChangeModel.getBLCLKVAH()) + 3000) {
            abnormalKVAH();
            return false;
        } else if (Utility.isValueNullOrEmpty(et_meter_change_slip_number.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_meter_change_slip_number_cant_be_empty));
            sv_main.requestChildFocus(et_meter_change_slip_number, et_meter_change_slip_number);
            return false;
        } else if (Utility.isValueNullOrEmpty(mMeterChangeReason)) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_select_meter_change_reason));
            sv_main.requestChildFocus(spn_meter_change_reason, spn_meter_change_reason);
            return false;
        } else if (mMeterChangeReason.equalsIgnoreCase("Others") &&
                Utility.isValueNullOrEmpty(et_meter_change_reason.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_change_reason));
            sv_main.requestChildFocus(et_meter_change_reason, et_meter_change_reason);
            return false;
        } else if (Utility.isValueNullOrEmpty(mIRDA)) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_select_irda_flag));
            sv_main.requestChildFocus(spn_irda, spn_irda);
            return false;
        } else if (mIRDA.equalsIgnoreCase("Yes") && Utility.isValueNullOrEmpty(mMeterMake)) {
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
        } else if (et_new_meter_number.getText().toString().length() < 5) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_valid_new_meter_number));
            sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
            return false;
        } else if (et_new_meter_number.getText().toString().equalsIgnoreCase(mMeterChangeModel.getOLDMTRNO())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_valid_new_meter_number));
            sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
            return false;
        } else if (Utility.isValueNullOrEmpty(mMeterPhase)) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_select_new_meter_phase));
            sv_main.requestChildFocus(spn_meter_phase, spn_meter_phase);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_meter_capacity.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_new_meter_capacity_cant_be_empty));
            sv_main.requestChildFocus(et_meter_capacity, et_meter_capacity);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_meter_class.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_new_meter_class_cant_be_empty));
            sv_main.requestChildFocus(et_meter_class, et_meter_class);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_meter_digits.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_meter_digits_cant_be_empty));
            sv_main.requestChildFocus(et_meter_digits, et_meter_digits);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_meter_mfg_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_manufacture_date));
            sv_main.requestChildFocus(et_meter_mfg_date, et_meter_mfg_date);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_initial_reading_kwh.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_reading_kwh));
            sv_main.requestChildFocus(et_initial_reading_kwh, et_initial_reading_kwh);
            return false;
        } else if (!ikwhProceed && Double.parseDouble(et_initial_reading_kwh.getText().toString()) > 10) {
            abNormalInitialKWH();
            return false;
        } else if (Utility.isValueNullOrEmpty(et_initial_reading_kvah.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_reading_kvah));
            sv_main.requestChildFocus(et_initial_reading_kvah, et_initial_reading_kvah);
            return false;
        } else if (!ikvahProceed && Double.parseDouble(et_initial_reading_kvah.getText().toString()) > 10) {
            abNormalInitialKVAH();
            return false;
        } else if (Utility.isValueNullOrEmpty(et_mf.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_mf));
            sv_main.requestChildFocus(et_mf, et_mf);
            return false;
        } else if (et_mf.getText().toString().equalsIgnoreCase("0")
                || et_mf.getText().toString().equalsIgnoreCase("00")) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_valid_meter_mf));
            sv_main.requestChildFocus(et_mf, et_mf);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_seal_one.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_seal_one));
            sv_main.requestChildFocus(et_seal_one, et_seal_one);
            return false;
       /* } else if (Utility.isValueNullOrEmpty(et_seal_two.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_seal_two));
            sv_main.requestChildFocus(et_seal_two, et_seal_two);
            return false;*/
        } else if (Utility.isValueNullOrEmpty(et_meter_tc_seals.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_seal_tc));
            sv_main.requestChildFocus(et_meter_tc_seals, et_meter_tc_seals);
            return false;
        }
        return true;
    }

    /*Set Master Details*/
    private void setData() {
        ll_details.setVisibility(View.VISIBLE);
        tv_service_no.setText(mServiceNum);
        tv_consumer_name.setText(mMeterChangeModel.getCNAME());
        tv_service_type.setText(mMeterChangeModel.getSCTYPE());
        tv_cycle.setText(mMeterChangeModel.getGRP());
        tv_category.setText(mMeterChangeModel.getCAT());
        tv_sub_category.setText(mMeterChangeModel.getSUBCAT());
        tv_dtr_code.setText(mMeterChangeModel.getDTRCD());

        tv_meter_make.setText(mMeterChangeModel.getMTRMAKE());
        tv_meter_number.setText(mMeterChangeModel.getOLDMTRNO());
        tv_meter_type.setText(mMeterChangeModel.getMTTYPE());
        tv_meter_class.setText(mMeterChangeModel.getMTCLASS());
        tv_meter_class.setText(mMeterChangeModel.getMTCLASS());
        tv_meter_capacity.setText(mMeterChangeModel.getMTCAPACITY());
        tv_meter_mf.setText(mMeterChangeModel.getMTMF());

        tv_meter_closing_reading_kwh.setText(mMeterChangeModel.getBLCLKWH());
        tv_meter_closing_reading.setText(mMeterChangeModel.getBLCLKVAH());
        et_meter_change_date.setText(mMeterChangeModel.getMTRCHGDT());
        et_old_meter_number.setText(mMeterChangeModel.getOLDMTRNO());
        et_final_reading_kwh.setText(mMeterChangeModel.getOLDMTRKWH());
        et_final_reading_kvah.setText(mMeterChangeModel.getOLDMTRKVAH());
        et_meter_change_slip_number.setText(mMeterChangeModel.getMTRCHFSLIP());
        et_changed_by.setText(mUser);
        et_rmd.setText(mMeterChangeModel.getOLDMTRRMD());
        et_new_meter_number.setText(mMeterChangeModel.getNEWMTRNO());
        et_meter_capacity.setText(mMeterChangeModel.getMTCAPACITY());
        et_meter_class.setText(mMeterChangeModel.getMTCLASS());
        et_meter_digits.setText(mMeterChangeModel.getMTRDIGITS());
        et_meter_mfg_date.setText(mMeterChangeModel.getMTRMFGDT());
        et_initial_reading_kwh.setText(mMeterChangeModel.getNEWMTRKWH());
        et_initial_reading_kvah.setText(mMeterChangeModel.getNEWMTRKVAH());
        et_mf.setText(mMeterChangeModel.getMTMF());
        et_seal_one.setText(mMeterChangeModel.getMRTSEAL1());
        et_seal_two.setText(mMeterChangeModel.getMRTSEAL2());
        if (Utility.isValueNullOrEmpty(mMeterChangeModel.getMRTSEAL3()) || mMeterChangeModel.getMRTSEAL3().equalsIgnoreCase("NA")) {
            et_seal_three.setText("");
        } else {
            et_seal_three.setText(mMeterChangeModel.getMRTSEAL3());
        }
        if (Utility.isValueNullOrEmpty(mMeterChangeModel.getMRTSEAL4()) || mMeterChangeModel.getMRTSEAL4().equalsIgnoreCase("NA")) {
            et_seal_four.setText("");
        } else {
            et_seal_four.setText(mMeterChangeModel.getMRTSEAL4());
        }
        et_meter_tc_seals.setText(mMeterChangeModel.getMTRSEAL());
        if (mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("No")) {
            et_meter_make.setText(mMeterChangeModel.getMTRMAKE());
        }
        if (Double.parseDouble(mMeterChangeModel.getBLCLKVAH()) == 0) {
            et_final_reading_kvah.setText("0");
            et_final_reading_kvah.setFocusable(false);
            et_final_reading_kvah.setCursorVisible(false);
        }
        if (Double.parseDouble(mMeterChangeModel.getBLRMD()) == 0) {
            ll_rmd.setVisibility(View.GONE);
            et_rmd.setText("0");
        } else {
            ll_rmd.setVisibility(View.VISIBLE);
        }

        tv_meter_closing_date.setText(mMeterChangeModel.getBLCLRDT());
        if (mMeterChangeModel.getRQSTATUS().equalsIgnoreCase("VALID")) {
            tv_meter_closing_reading_status.setText("01");
        } else {
            tv_meter_closing_reading_status.setText("00");
        }
        setMeterChangeReasonSpinnerData();
        setIRDASpinnerData();
        setMeterPhaseSpinnerData();
        setMeterTypeSpinnerData();
        setMeterMakeSpinnerData();

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
                                new android.app.AlertDialog.Builder(MeterChangeModifyFormActivity.this)
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
                                                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
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
                                Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                        MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
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
                                new android.app.AlertDialog.Builder(MeterChangeModifyFormActivity.this)
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
                                                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
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
                                Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                        MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
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
                                new android.app.AlertDialog.Builder(MeterChangeModifyFormActivity.this)
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
                                                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
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
                                Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                        MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
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
                                new android.app.AlertDialog.Builder(MeterChangeModifyFormActivity.this)
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
                                                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                                                MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
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
                                Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                        MeterChangeModifyFormActivity.this, R.string.you_pressed_cancel));
                                et_final_reading_kwh.setText("");
                            }
                        }).show();
    }

    private void getMeterDetails() {
        RequestParams params = new RequestParams();
        params.put(Constants.USCNO, mServiceNum);
        params.put(Constants.MTRCHGDT, mMeterChgDate);
        params.put(Constants.NEWMTRNO, mNewMtrNum);
        prgDialog.show();
        prgDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("URL", Constants.METER_CHANGE_URL + Constants.MODIFY_MTR_DETAILS);
        Utility.showLog(Constants.USCNO, mServiceNum);
        Utility.showLog(Constants.MTRCHGDT, mMeterChgDate);
        Utility.showLog(Constants.NEWMTRNO, mNewMtrNum);
        // client.addHeader("SCNO", scno);
        client.post(Constants.METER_CHANGE_URL + Constants.MODIFY_MTR_DETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("MSG")) {
                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, "Not a valid Service Number");
                    } else {
                        mMeterChangeModel = new Gson().fromJson(jsonObject.toString(), MeterChangeModel.class);
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
                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    private void inLineValidations() {
      /*  et_meter_change_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                et_meter_mfg_date.setText("");
                if (!mMeterChangeModel.getMTRCHGDT().equalsIgnoreCase("NA")) {
                    if (et_meter_change_date.getText().toString().equalsIgnoreCase(mMeterChangeModel.getMTRCHGDT())) {
                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, "Meter change was already done on the same date for the service number");
                        et_meter_change_date.setText("");
                    }
                }
            }
        });*/
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
                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                MeterChangeModifyFormActivity.this, R.string.err_enter_old_meter_number));
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    } else if (et_old_meter_number.getText().toString().length() < 5) {
                        Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                MeterChangeModifyFormActivity.this, R.string.err_old_meter_number_lees_than_five));
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    } else if (!meterNumberProceed && !et_old_meter_number.getText().toString().equalsIgnoreCase(mMeterChangeModel.getMTNO())) {
                     /*   Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this, Utility.getResourcesString(
                                MeterChangeModifyFormActivity.this, R.string.err_enter_valid_old_meter_number));*/
                        verifyOldMeterNumber();
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    }
                    break;
                case R.id.et_final_reading_kwh:
                    if (Utility.isValueNullOrEmpty(et_final_reading_kwh.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_old_meter_kwh_reading));
                        sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
                    } else if (Double.parseDouble(et_final_reading_kwh.getText().toString()) < Double.parseDouble(mMeterChangeModel.getBLCLKWH())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_final_reading_cant_be_less_kwh));
                        sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
                    } else if (!kwhProceed &&
                            Double.parseDouble(et_final_reading_kwh.getText().toString()) > Double.parseDouble(mMeterChangeModel.getBLCLKWH()) + 3000) {
                        abnormalKWH();
                    }
                    break;
                case R.id.et_final_reading_kvah:
                    if (mMeterChangeModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                            Utility.isValueNullOrEmpty(et_final_reading_kvah.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_old_meter_kvah_reading));
                        sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
                    } else if (mMeterChangeModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                            Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(mMeterChangeModel.getBLCLKVAH())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_final_reading_cant_be_less_kvah));
                        sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
                    } else if (!kvahProceed && mMeterChangeModel.getBLCLKVAH().equalsIgnoreCase("1") &&
                            Double.parseDouble(et_final_reading_kvah.getText().toString()) < Double.parseDouble(mMeterChangeModel.getBLCLKVAH()) + 3000) {
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
                    if (Utility.isValueNullOrEmpty(mIRDA)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_irda_flag));
                        sv_main.requestChildFocus(spn_irda, spn_irda);
                    } else if (mIRDA.equalsIgnoreCase("Yes") && Utility.isValueNullOrEmpty(mMeterMake)) {
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
                    if (Utility.isValueNullOrEmpty(mIRDA)) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_select_irda_flag));
                        sv_main.requestChildFocus(spn_irda, spn_irda);
                    } else if (mIRDA.equalsIgnoreCase("Yes") && Utility.isValueNullOrEmpty(mMeterMake)) {
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
                    } else if (et_new_meter_number.getText().toString().length() < 5) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_valid_new_meter_number));
                        sv_main.requestChildFocus(et_new_meter_number, et_new_meter_number);
                    } else if (et_new_meter_number.getText().toString().equalsIgnoreCase(mMeterChangeModel.getMTNO())) {
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
                .setMessage(Utility.getResourcesString(MeterChangeModifyFormActivity.this,
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
        MeterChangeModel meterChangeModel = new MeterChangeModel();
        meterChangeModel.setUSCNO(mServiceNum);
        meterChangeModel.setMTRCHGDT(et_meter_change_date.getText().toString());
        meterChangeModel.setOLDMTRNO(et_old_meter_number.getText().toString());
        meterChangeModel.setOLDMTRKWH(et_final_reading_kwh.getText().toString());
        meterChangeModel.setOLDMTRKVAH(et_final_reading_kvah.getText().toString());
        meterChangeModel.setMTRCHFSLIP(et_meter_change_slip_number.getText().toString());
        if (mMeterChangeReason.equalsIgnoreCase("Others")) {
            meterChangeModel.setREMARKS(et_meter_change_reason.getText().toString());
        } else {
            meterChangeModel.setREMARKS(mMeterChangeReason);
        }
        meterChangeModel.setOLDMTRRMD(et_rmd.getText().toString());
        meterChangeModel.setMTRIRDA(mIRDA);
        if (mIRDA.equalsIgnoreCase("Yes")) {
            meterChangeModel.setMTRMAKE(mMeterMake.replace("&", "N"));
            meterChangeModel.setMTTYPE(et_meter_type.getText().toString());
        } else {
            meterChangeModel.setMTTYPE(mMeterType);
            meterChangeModel.setMTRMAKE(et_meter_make.getText().toString().replace("&", "N"));
        }
        meterChangeModel.setNEWMTRNO(et_new_meter_number.getText().toString());
        meterChangeModel.setMTRPHASE(mMeterPhase);
        meterChangeModel.setMTCAPACITY(et_meter_capacity.getText().toString());
        meterChangeModel.setMTCLASS(et_meter_class.getText().toString());
        meterChangeModel.setMTRDIGITS(et_meter_digits.getText().toString());
        meterChangeModel.setMTRMFGDT(et_meter_mfg_date.getText().toString());
        meterChangeModel.setNEWMTRKWH(et_initial_reading_kwh.getText().toString());
        meterChangeModel.setNEWMTRKVAH(et_initial_reading_kvah.getText().toString());
        meterChangeModel.setMTMF(et_mf.getText().toString());
        meterChangeModel.setMRTSEAL1(et_seal_one.getText().toString());
        meterChangeModel.setMRTSEAL2(et_seal_two.getText().toString());
        if (Utility.isValueNullOrEmpty(et_seal_three.getText().toString())) {
            meterChangeModel.setMRTSEAL3("NA");
        } else {
            meterChangeModel.setMRTSEAL3(et_seal_three.getText().toString());
        }
        if (Utility.isValueNullOrEmpty(et_seal_four.getText().toString())) {
            meterChangeModel.setMRTSEAL4("NA");
        } else {
            meterChangeModel.setMRTSEAL4(et_seal_four.getText().toString());
        }
        meterChangeModel.setMTRSEAL(et_meter_tc_seals.getText().toString());
        meterChangeModel.setCNAME(mUser);
        meterChangeModel.setImei(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        // mMeterChangeModel.setUnitCode(et_service_number.getText().toString().substring(0, 5));
        String finalString = meterChangeModel.getUSCNO() + "|" + meterChangeModel.getMTRCHGDT() + "|" +
                meterChangeModel.getOLDMTRNO() + "|" + meterChangeModel.getOLDMTRKWH() + "|" +
                meterChangeModel.getOLDMTRKVAH() + "|" + meterChangeModel.getMTRCHFSLIP() + "|" +
                meterChangeModel.getREMARKS() + "|" + meterChangeModel.getOLDMTRRMD() + "|" +
                meterChangeModel.getMTRIRDA() + "|" + meterChangeModel.getMTRMAKE() + "|" +
                meterChangeModel.getMTTYPE() + "|" + meterChangeModel.getNEWMTRNO() + "|" +
                meterChangeModel.getMTRPHASE() + "|" + meterChangeModel.getMTCAPACITY() + "|" +
                meterChangeModel.getMTCLASS() + "|" + meterChangeModel.getMTRDIGITS() + "|" +
                meterChangeModel.getMTRMFGDT() + "|" + meterChangeModel.getNEWMTRKWH() + "|" +
                meterChangeModel.getOLDMTRKVAH() + "|" + meterChangeModel.getMTMF() + "|" +
                meterChangeModel.getMRTSEAL1() + "|" + meterChangeModel.getMRTSEAL2() + "|" +
                meterChangeModel.getMRTSEAL3() + "|" + meterChangeModel.getMRTSEAL4() + "|" +
                meterChangeModel.getMTRSEAL() + "|" + meterChangeModel.getCNAME() + "|" +
                meterChangeModel.getImei() + "|" + mNewMtrNum + "|" + mMeterChgDate + "|" +
                mMeterChangeModel.getDBMTRNO() + "|" + mMeterChangeModel.getGRP() + "|" +
                mMeterChangeModel.getOLDMTRMF();
        Utility.showLog("finalString", finalString);
        RequestParams params = new RequestParams();
        params.put("INPUT", finalString);
        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("URL", Constants.METER_CHANGE_URL + Constants.UPDATE_MTR_DETAILS);
        Utility.showLog("INPUT", finalString);
        client.post(Constants.METER_CHANGE_URL + Constants.UPDATE_MTR_DETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("RQSTATUS")) {
                        if (jsonObject.optString("RQSTATUS").equalsIgnoreCase("VALID")) {
                            Utility.showToastMessage(MeterChangeModifyFormActivity.this, jsonObject.optString("MSG"));
                            MeterChangeListActivity.getInstance().updateList(mServiceNum);
                            finish();
                        } else if (jsonObject.has("MSG")) {
                            Utility.showCustomOKOnlyDialog(MeterChangeModifyFormActivity.this,
                                    jsonObject.optString("MSG"));
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
    }


    private void getDeviceId() {
        /*TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
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
        }*/
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
}
