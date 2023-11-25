package com.apcpdcl.departmentapp.activities;

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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.customviews.DatePickerFragment;
import com.apcpdcl.departmentapp.models.MeterChangeEntryModel;
import com.apcpdcl.departmentapp.sqlite.MeterChangeDatabaseHandler;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseena
 * on 23-02-2018.
 */

public class OfflineMeterChangeRequestFormActivity extends AppCompatActivity implements View.OnFocusChangeListener {

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
    @BindView(R.id.et_service_number)
    EditText et_service_number;
    @BindView(R.id.ll_rmd)
    LinearLayout ll_rmd;
    @BindView(R.id.et_rmd)
    EditText et_rmd;

    private String mMeterChangeReason = "";
    private String mIRDA = "";
    private String sectionCode = "";
    private String mMeterPhase = "";
    private String mMeterMake = "";
    private String mMeterType = "";
    private String user = "";
    private boolean ikwhProceed = false;
    private boolean ikvahProceed = false;
    private ProgressDialog prgDialog;
    private boolean prePopulateData = false;
    private MeterChangeEntryModel mMeterChangeModel;
    private MeterChangeDatabaseHandler dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_meter_change_form_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    /*Initialize Views*/
    private void init() {
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        user = prefs.getString("UserName", "");
        et_changed_by.setText(user);
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.FROM)) {
                String from = mBundle.getString(Constants.FROM);
                if (from != null && from.equalsIgnoreCase(OfflineMeterChangeListActivity.TAG)) {
                    prePopulateData = true;
                    mMeterChangeModel = (MeterChangeEntryModel) mBundle.getSerializable(Constants.METER_CHANGE_MODEL);
                    prePopulateData();
                }
            }
        }
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        prgDialog = new ProgressDialog(this);

        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        setMeterChangeReasonSpinnerData();
        setIRDASpinnerData();
        setMeterPhaseSpinnerData();
        setAshtrikColor();
        inLineValidations();
        checkServiceNumberAlreadyExists();
        setMeterMakeSpinnerData();
        setMeterTypeSpinnerData();
    }

    @OnClick(R.id.et_meter_change_date)
    void openDatePicker() {
        DatePickerFragment date = new DatePickerFragment(et_meter_change_date, true);
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

    @OnClick(R.id.btn_submit)
    void navigateToNextScreen() {
        if (validateFields()) {
            saveFormDataInDB();
        }
    }

    private void saveFormDataInDB() {
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
        meterChangeModel.setNewmtrIRDA(mIRDA);
        meterChangeModel.setOldmtrrmd(et_rmd.getText().toString());
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
        meterChangeModel.setUnitCode(et_service_number.getText().toString().substring(0, 5));
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
                meterChangeModel.getImei() + "|" + meterChangeModel.getUnitCode()+"|0|0";
        meterChangeModel.setFinalDataString(finalString);
        meterChangeModel.setStatus("Pending");
        meterChangeModel.setMsg("Pending");
        MeterChangeDatabaseHandler db = new MeterChangeDatabaseHandler(this);
        if (prePopulateData) {
            db.updateTotalModel(meterChangeModel);
        } else {
            db.saveModel(meterChangeModel);
        }
        Utility.showToastMessage(this, "Saved in Database.");
        finish();
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
        if (prePopulateData) {
            try {
                int i = list.indexOf(mMeterChangeModel.getNewremarks());
                if (i == -1) {
                    spn_meter_change_reason.setSelection(list.indexOf("Others"));
                    et_meter_change_reason.setText(mMeterChangeModel.getNewremarks());
                } else {
                    spn_meter_change_reason.setSelection(list.indexOf(mMeterChangeModel.getNewremarks()));
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

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
                    Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                            OfflineMeterChangeRequestFormActivity.this, R.string.err_meter_change_slip_number_cant_be_empty));
                    sv_main.requestChildFocus(et_meter_change_slip_number, et_meter_change_slip_number);
                }
                isFirstTime[0] = false;
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                if (!isFirstTime[0] && Utility.isValueNullOrEmpty(et_meter_change_slip_number.getText().toString())) {
                    Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                            OfflineMeterChangeRequestFormActivity.this, R.string.err_meter_change_slip_number_cant_be_empty));
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
        if (prePopulateData) {
            spn_irda.setSelection(list.indexOf(mMeterChangeModel.getNewmtrIRDA()));
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
                    if (!prePopulateData)
                        spn_meter_make.setSelection(0);
                }
                if (mIRDA.equals("No")) {
                    et_meter_make.setVisibility(View.VISIBLE);
                    spn_meter_type.setVisibility(View.VISIBLE);
                    if (!prePopulateData)
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
        if (prePopulateData) {
            if (mMeterChangeModel.getNewmtrIRDA().equalsIgnoreCase("Yes")) {
                int i = list.indexOf(mMeterChangeModel.getNewmtrmake());
                spn_meter_make.setSelection(i);
            }
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
        if (prePopulateData && mMeterChangeModel.getNewmtrIRDA().equalsIgnoreCase("No")) {
            spn_meter_type.setSelection(arrayList.indexOf(mMeterChangeModel.getNewmtrType()));
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
        if (prePopulateData) {
            spn_meter_phase.setSelection(list.indexOf(mMeterChangeModel.getNewmtrphase()));
        }
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
        // tv_seal_two.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 2"));
        tv_seal_one.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 1"));
        tv_tc_seal.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Meter TC Seals"));
        tv_meter_cover_seals.setText(Html.fromHtml("Meter Cover Seals (MRT)\n" +
                " ( " + "<font color=\"#E50E0E\">" + "*" + "</font>" + " Seals are mandatory)"));
    }

    /*Validate Fields*/
    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(et_service_number.getText().toString())) {
            Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                    OfflineMeterChangeRequestFormActivity.this, R.string.err_enter_service_number));
            sv_main.requestChildFocus(et_service_number, et_service_number);
            return false;
        } else if (et_service_number.getText().toString().length() < 13) {
            Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                    OfflineMeterChangeRequestFormActivity.this, R.string.err_enter_valid_service_number));
            sv_main.requestChildFocus(et_service_number, et_service_number);
            return false;
        } else if (Utility.isValueNullOrEmpty(et_meter_change_date.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_change_date));
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
        } else if (Utility.isValueNullOrEmpty(et_final_reading_kwh.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_old_meter_kwh_reading));
            sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
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
        } else if (!ikwhProceed && Integer.parseInt(et_initial_reading_kwh.getText().toString()) > 10) {
            abNormalInitialKWH();
            return false;
        } else if (Utility.isValueNullOrEmpty(et_initial_reading_kvah.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                    this, R.string.err_enter_meter_reading_kvah));
            sv_main.requestChildFocus(et_initial_reading_kvah, et_initial_reading_kvah);
            return false;
        } else if (!ikvahProceed && Integer.parseInt(et_initial_reading_kvah.getText().toString()) > 10) {
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
                                new android.app.AlertDialog.Builder(OfflineMeterChangeRequestFormActivity.this)
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
                                                        Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                                                OfflineMeterChangeRequestFormActivity.this, R.string.you_pressed_cancel));
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
                                Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                        OfflineMeterChangeRequestFormActivity.this, R.string.you_pressed_cancel));
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
                                new android.app.AlertDialog.Builder(OfflineMeterChangeRequestFormActivity.this)
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
                                                        Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                                                OfflineMeterChangeRequestFormActivity.this, R.string.you_pressed_cancel));
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
                                Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                        OfflineMeterChangeRequestFormActivity.this, R.string.you_pressed_cancel));
                                et_final_reading_kwh.setText("");
                            }
                        }).show();
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
            }
        });
        et_service_number.setOnFocusChangeListener(this);
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

    private void prePopulateData() {
        et_service_number.setText(mMeterChangeModel.getServiceNo());
        et_meter_change_date.setText(mMeterChangeModel.getMtrchgDt());
        et_old_meter_number.setText(mMeterChangeModel.getOldmtrno());
        et_final_reading_kwh.setText(mMeterChangeModel.getOldmtrkwh());
        et_final_reading_kvah.setText(mMeterChangeModel.getOldmtrkvah());
        et_meter_change_slip_number.setText(mMeterChangeModel.getMtrchfslip());
        et_changed_by.setText(mMeterChangeModel.getLoginUserName());
        et_rmd.setText(mMeterChangeModel.getOldmtrrmd());
        et_new_meter_number.setText(mMeterChangeModel.getNewmtrno());
        et_meter_capacity.setText(mMeterChangeModel.getNewmtrcurrent());
        et_meter_class.setText(mMeterChangeModel.getNewmtrclass());
        et_meter_digits.setText(mMeterChangeModel.getNewmtrDigits());
        et_meter_mfg_date.setText(mMeterChangeModel.getNewmtrmfgdt());
        et_initial_reading_kwh.setText(mMeterChangeModel.getNewmtrkwh());
        et_initial_reading_kvah.setText(mMeterChangeModel.getNewmtrkvah());
        et_mf.setText(mMeterChangeModel.getNewmtrmf());
        et_seal_one.setText(mMeterChangeModel.getNewmtrMRTSeal1());
        if (mMeterChangeModel.getNewmtrMRTSeal2().equalsIgnoreCase("NA")) {
            et_seal_three.setText("");
        } else {
            et_seal_two.setText(mMeterChangeModel.getNewmtrMRTSeal2());
        }
        if (mMeterChangeModel.getNewmtrMRTSeal3().equalsIgnoreCase("NA")) {
            et_seal_three.setText("");
        } else {
            et_seal_three.setText(mMeterChangeModel.getNewmtrMRTSeal3());
        }
        if (mMeterChangeModel.getNewmtrMRTSeal4().equalsIgnoreCase("NA")) {
            et_seal_four.setText("");
        } else {
            et_seal_four.setText(mMeterChangeModel.getNewmtrMRTSeal4());
        }
        et_meter_tc_seals.setText(mMeterChangeModel.getNewmtrSeal());
        if (mMeterChangeModel.getNewmtrIRDA().equalsIgnoreCase("No")) {
            et_meter_make.setText(mMeterChangeModel.getNewmtrmake());
        }
    }

    private void checkServiceNumberAlreadyExists() {
        dbManager = new MeterChangeDatabaseHandler(this);
        et_service_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 5 && !charSequence.toString().equals(sectionCode)) {
                    Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this,
                            et_service_number.getText().toString() + " Service Number is not related to you.");
                    et_service_number.setText("");
                }
                if (!prePopulateData && dbManager.getMeterChangeRequestsCount() > 0) {
                    if (charSequence.toString().length() == 13) {
                        ArrayList<String> serviceNos = dbManager.getAllServiceNumbers();
                        if (serviceNos.contains(charSequence.toString())) {
                            Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this,
                                    "Meter Change Request already added for " + et_service_number.getText().toString() + " Service Number");
                            et_service_number.setText("");

                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
       /* if (!hasFocus){
            validateFields();
        }*/
        if (!hasFocus) {
            switch (view.getId()) {
                case R.id.et_service_number:
                    if (Utility.isValueNullOrEmpty(et_service_number.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                OfflineMeterChangeRequestFormActivity.this, R.string.err_enter_service_number));
                        sv_main.requestChildFocus(et_service_number, et_service_number);
                    } else if (et_service_number.getText().toString().length() < 13) {
                        Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                OfflineMeterChangeRequestFormActivity.this, R.string.err_enter_valid_service_number));
                        sv_main.requestChildFocus(et_service_number, et_service_number);
                    }
                    break;
                case R.id.et_old_meter_number:
                    if (Utility.isValueNullOrEmpty(et_meter_change_date.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_change_date));
                        sv_main.requestChildFocus(et_meter_change_date, et_meter_change_date);
                    } else if (Utility.isValueNullOrEmpty(et_old_meter_number.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                OfflineMeterChangeRequestFormActivity.this, R.string.err_enter_old_meter_number));
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    } else if (et_old_meter_number.getText().toString().length() < 5) {
                        Utility.showCustomOKOnlyDialog(OfflineMeterChangeRequestFormActivity.this, Utility.getResourcesString(
                                OfflineMeterChangeRequestFormActivity.this, R.string.err_old_meter_number_lees_than_five));
                        sv_main.requestChildFocus(et_old_meter_number, et_old_meter_number);
                    }
                    break;
                case R.id.et_final_reading_kwh:
                    if (Utility.isValueNullOrEmpty(et_final_reading_kwh.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_old_meter_kwh_reading));
                        sv_main.requestChildFocus(et_final_reading_kwh, et_final_reading_kwh);
                    }
                    break;
                case R.id.et_final_reading_kvah:
                    if (Utility.isValueNullOrEmpty(et_final_reading_kvah.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_old_meter_kvah_reading));
                        sv_main.requestChildFocus(et_final_reading_kvah, et_final_reading_kvah);
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
                    } else if (!ikwhProceed && Integer.parseInt(et_initial_reading_kwh.getText().toString()) > 10) {
                        abNormalInitialKWH();
                    }
                    break;
                case R.id.et_initial_reading_kvah:
                    if (Utility.isValueNullOrEmpty(et_initial_reading_kvah.getText().toString())) {
                        Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(
                                this, R.string.err_enter_meter_reading_kvah));
                        sv_main.requestChildFocus(et_initial_reading_kvah, et_initial_reading_kvah);
                    } else if (!ikvahProceed && Integer.parseInt(et_initial_reading_kvah.getText().toString()) > 10) {
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

    private void getDeviceId() {
       /* TelephonyManager manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
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

