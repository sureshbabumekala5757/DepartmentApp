package com.apcpdcl.departmentapp.activities;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.view.MotionEvent;
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
import com.apcpdcl.departmentapp.customviews.ExpandableLayoutListener;
import com.apcpdcl.departmentapp.customviews.ExpandableRelativeLayout;
import com.apcpdcl.departmentapp.customviews.Utils;
import com.apcpdcl.departmentapp.models.MeterChangeModel;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AAODeleteMeterChangeActivity extends AppCompatActivity {


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
    @BindView(R.id.btn_delete)
    Button btn_delete;
    @BindView(R.id.ll_meter_change_particulars)
    LinearLayout ll_meter_change_particulars;
    @BindView(R.id.ll_new_meter_details)
    LinearLayout ll_new_meter_details;

    private String mIRDA = "";
    private MeterChangeModel mMeterChangeModel;
    private ProgressDialog prgDialog;
    private String mServiceNum = "";
    private String mMeterChgDate = "";
    private String mNewMtrNum = "";
    private String mUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_meter_change_activity);
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
        setExpandableButtonAnimators(el_meter_change_particulars, iv_meter_change_particulars);
        setExpandableButtonAnimators(el_new_meter_details, iv_new_meter_details);
        disableAllViewsInParentLayout(ll_meter_change_particulars);
        disableAllViewsInParentLayout(ll_new_meter_details);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
       // prgDialog.show();
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.GET_METER_MAKE_LIST))) {
            Utility.getMeterMake(this);
        }
        if (Utility.isNetworkAvailable(this)) {
            getMeterDetails();
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
        }
        setAshtrikColor();
    }

    @OnClick(R.id.btn_delete)
    void delete() {
        if (Utility.isNetworkAvailable(this)) {
            deleteMeterDetails();
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this, R.string.no_internet));
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

    /* *Set Red Ashtrik in Text**/
    private void setAshtrikColor() {
        // tv_seal_two.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 2"));
        tv_seal_one.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Seal 1"));
        tv_meter_cover_seals.setText(Html.fromHtml("Meter Cover Seals (MRT)\n" +
                " ( " + "<font color=\"#E50E0E\">" + "*" + "</font>" + " Seals are mandatory)"));
        tv_tc_seal.setText(Html.fromHtml("<font color=\"#E50E0E\">" + "*" + "</font>" + "Meter TC Seals"));
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

    /*GET METER CHANGE DETAILS*/
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
                        Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this, "Not a valid Service Number");
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
                        Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
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
        et_meter_change_date.setFocusable(false);
        et_old_meter_number.setText(mMeterChangeModel.getOLDMTRNO());
        et_old_meter_number.setFocusable(false);
        et_final_reading_kwh.setText(mMeterChangeModel.getOLDMTRKWH());
        et_final_reading_kwh.setFocusable(false);
        et_final_reading_kvah.setText(mMeterChangeModel.getOLDMTRKVAH());
        et_final_reading_kvah.setFocusable(false);
        et_meter_change_slip_number.setText(mMeterChangeModel.getMTRCHFSLIP());
        et_meter_change_slip_number.setFocusable(false);
        et_changed_by.setText(mUser);
        et_rmd.setText(mMeterChangeModel.getOLDMTRRMD());
        et_rmd.setFocusable(false);
        et_new_meter_number.setText(mMeterChangeModel.getNEWMTRNO());
        et_new_meter_number.setFocusable(false);
        et_meter_capacity.setText(mMeterChangeModel.getMTCAPACITY());
        et_meter_capacity.setFocusable(false);
        et_meter_class.setText(mMeterChangeModel.getMTCLASS());
        et_meter_class.setFocusable(false);
        et_meter_digits.setText(mMeterChangeModel.getMTRDIGITS());
        et_meter_digits.setFocusable(false);
        et_meter_mfg_date.setText(mMeterChangeModel.getMTRMFGDT());
        et_meter_mfg_date.setFocusable(false);
        et_initial_reading_kwh.setText(mMeterChangeModel.getNEWMTRKWH());
        et_initial_reading_kwh.setFocusable(false);
        et_initial_reading_kvah.setText(mMeterChangeModel.getNEWMTRKVAH());
        et_initial_reading_kvah.setFocusable(false);
        et_mf.setText(mMeterChangeModel.getMTMF());
        et_mf.setFocusable(false);
        et_seal_one.setText(mMeterChangeModel.getMRTSEAL1());
        et_seal_one.setFocusable(false);
        et_seal_two.setText(mMeterChangeModel.getMRTSEAL2());
        et_seal_two.setFocusable(false);
        if (!Utility.isValueNullOrEmpty(mMeterChangeModel.getMRTSEAL3()) &&
                mMeterChangeModel.getMRTSEAL3().equalsIgnoreCase("NA")) {
            et_seal_three.setText("");
        } else {
            et_seal_three.setText(mMeterChangeModel.getMRTSEAL3());
        }
        et_seal_three.setFocusable(false);
        if (!Utility.isValueNullOrEmpty(mMeterChangeModel.getMRTSEAL4()) &&
                mMeterChangeModel.getMRTSEAL4().equalsIgnoreCase("NA")) {
            et_seal_four.setText("");
        } else {
            et_seal_four.setText(mMeterChangeModel.getMRTSEAL4());
        }
        et_seal_four.setFocusable(false);
        et_meter_tc_seals.setText(mMeterChangeModel.getMTRSEAL());
        et_meter_tc_seals.setFocusable(false);
        if (!Utility.isValueNullOrEmpty(mMeterChangeModel.getMTRIRDA()) && mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("No")) {
            et_meter_make.setText(mMeterChangeModel.getMTRMAKE());
            et_meter_make.setFocusable(false);
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
        spn_meter_change_reason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
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
        if (!Utility.isValueNullOrEmpty(mMeterChangeModel.getMTRIRDA()) && mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("Yes")) {
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
        spn_irda.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    /* *Set Meter Make Spinner Data**/
    private void setMeterMakeSpinnerData() {
        final ArrayList<String> list = Utility.getMeterMakeList(this);
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_make.setAdapter(newlineAdapter);
        if (!Utility.isValueNullOrEmpty(mMeterChangeModel.getMTRIRDA()) && mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("Yes")) {
            int i = list.indexOf(mMeterChangeModel.getMTRMAKE());
            if (i != -1)
                spn_meter_make.setSelection(i);
        }
        spn_meter_make.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
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
        if (!Utility.isValueNullOrEmpty(mMeterChangeModel.getMTRIRDA()) && mMeterChangeModel.getMTRIRDA().equalsIgnoreCase("No")) {
            spn_meter_type.setSelection(arrayList.indexOf(mMeterChangeModel.getMTTYPE()));
        }
        spn_meter_type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
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
        spn_meter_phase.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
    }

    /*DELETE METER CHANGE DETAILS*/
    private void deleteMeterDetails() {
        RequestParams params = new RequestParams();
        String param = mServiceNum + "|" + mMeterChgDate + "|" + mNewMtrNum;

        params.put("SCNO", mServiceNum);
        params.put("NEWMTRNO", mNewMtrNum);
        params.put("MTRCHGDT", mMeterChgDate);
        params.put("IPADDRESS", Utility.getSharedPrefStringData(this,Constants.IMEI_NUMBER));
        prgDialog.show();
        prgDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("URL", Constants.URL_ERO + "EROservices/delete");
        Utility.showLog(Constants.USCNO, mServiceNum);
        Utility.showLog(Constants.MTRCHGDT, mMeterChgDate);
        Utility.showLog(Constants.NEWMTRNO, mNewMtrNum);
        // client.addHeader("SCNO", scno);
        client.post(Constants.URL_ERO + "EROservices/delete", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("onSuccess", response);
                Utility.showLog("response", response);
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("Status")) {
                        if (jsonObject.optString("Status").equalsIgnoreCase("TRUE")) {
                            AAOMeterChangeDeleteListActivity.getInstance().updateList(mServiceNum);
                            Utility.showToastMessage(AAODeleteMeterChangeActivity.this, jsonObject.optString("Message"));
                            finish();
                        } else if (jsonObject.has("Message")) {
                            Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this,
                                    jsonObject.optString("Message"));
                        }
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
                        Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(AAODeleteMeterChangeActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    /*DISABLE ALL VIEWS IN PARENT LAYOUT*/
    private void disableAllViewsInParentLayout(LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setEnabled(false);
            child.setFocusable(false);
            child.setFocusableInTouchMode(false);
            child.setClickable(false);
        }
    }

}
