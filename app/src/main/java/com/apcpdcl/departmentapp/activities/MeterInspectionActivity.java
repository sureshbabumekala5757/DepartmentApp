package com.apcpdcl.departmentapp.activities;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.customviews.ExpandableLayoutListener;
import com.apcpdcl.departmentapp.customviews.Utils;
import com.apcpdcl.departmentapp.models.MeterInspectionAddressModel;
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

public class MeterInspectionActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_service_no)
    EditText et_service_no;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_designation)
    EditText et_designation;
    @BindView(R.id.spn_meter_type)
    Spinner spn_meter_type;
    @BindView(R.id.btn_fetch)
    Button btn_fetch;
    @BindView(R.id.ll_total)
    LinearLayout ll_total;

    @BindView(R.id.rl_details_layout)
    RelativeLayout rl_details_layout;
    @BindView(R.id.tv_details)
    TextView tv_details;
    @BindView(R.id.iv_details)
    ImageView iv_details;
    @BindView(R.id.details_expandable)
    ExpandableRelativeLayout details_expandable;

    @BindView(R.id.rl_meterdisplay_layout)
    RelativeLayout rl_meterdisplay_layout;
    @BindView(R.id.tv_meterdisplay)
    TextView tv_meterdisplay;
    @BindView(R.id.iv_meterdisplay)
    ImageView iv_meterdisplay;
    @BindView(R.id.meterdisplay_expandable)
    ExpandableRelativeLayout meterdisplay_expandable;

    @BindView(R.id.rl_meter_particulars)
    RelativeLayout rl_meter_particulars;
    @BindView(R.id.tv_meter_particulars)
    TextView tv_meter_particulars;
    @BindView(R.id.iv_meter_particulars)
    ImageView iv_meter_particulars;
    @BindView(R.id.meter_particulars_expandable)
    ExpandableRelativeLayout meter_particulars_expandable;

    @BindView(R.id.rl_incriminating_layout)
    RelativeLayout rl_incriminating_layout;
    @BindView(R.id.tv_incriminating)
    TextView tv_incriminating;
    @BindView(R.id.iv_incriminating)
    ImageView iv_incriminating;
    @BindView(R.id.incriminating_expandable)
    ExpandableRelativeLayout incriminating_expandable;

    @BindView(R.id.rl_meter_readings)
    RelativeLayout rl_meter_readings;
    @BindView(R.id.tv_meter_readings)
    TextView tv_meter_readings;
    @BindView(R.id.iv_meter_readings)
    ImageView iv_meter_readings;
    @BindView(R.id.meter_readings_expandable)
    ExpandableRelativeLayout meter_readings_expandable;

    @BindView(R.id.rl_terminal_blocks)
    RelativeLayout rl_terminal_blocks;
    @BindView(R.id.tv_terminal_blocks)
    TextView tv_terminal_blocks;
    @BindView(R.id.iv_terminal_blocks)
    ImageView iv_terminal_blocks;
    @BindView(R.id.terminal_blocks_expandable)
    ExpandableRelativeLayout terminal_blocks_expandable;


    @BindView(R.id.et_category)
    EditText et_category;
    @BindView(R.id.et_section)
    EditText et_section;
    @BindView(R.id.et_distribution)
    EditText et_distribution;
    @BindView(R.id.et_name_of_consumer)
    EditText et_name_of_consumer;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_date)
    EditText et_date;
    @BindView(R.id.et_load)
    EditText et_load;

    @BindView(R.id.et_metermake)
    EditText et_metermake;
    @BindView(R.id.et_meterslno)
    EditText et_meterslno;
    @BindView(R.id.et_capacity)
    EditText et_capacity;
    @BindView(R.id.et_class)
    EditText et_class;
    @BindView(R.id.et_metertype)
    EditText et_metertype;
    @BindView(R.id.et_ctratio)
    EditText et_ctratio;
    @BindView(R.id.et_ptratio)
    EditText et_ptratio;
    @BindView(R.id.et_mf)
    EditText et_mf;
    @BindView(R.id.et_dailmf)
    EditText et_dailmf;
    @BindView(R.id.spn_year_manufacture)
    Spinner spn_year_manufacture;

    @BindView(R.id.spn_meter_changed)
    Spinner spn_meter_changed;
    @BindView(R.id.ll_meter_change_details)
    LinearLayout ll_meter_change_details;

    private ProgressDialog prgDialog;
    private String mMeterType = "";
    private String mMeterChanged = "";
    private String mYearOfManufacturing = "";
    private MeterInspectionAddressModel meterDetailsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meterinspection);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        prgDialog = new ProgressDialog(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setExpandableButtonAnimators(details_expandable, iv_details);
        setExpandableButtonAnimators(meterdisplay_expandable, iv_meterdisplay);
        setExpandableButtonAnimators(meter_particulars_expandable, iv_meter_particulars);
        setExpandableButtonAnimators(incriminating_expandable, iv_incriminating);
        setExpandableButtonAnimators(meter_readings_expandable, iv_meter_readings);
        setExpandableButtonAnimators(terminal_blocks_expandable, iv_terminal_blocks);
        setMeterTypeSpinnerData();
        setYearOfManufactureSpinnerData();
        setMeterChangedSpinnerData();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meterinspection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_meterinspection_changepassword:
                // changePasswordpopup();
                return true;

            case R.id.action_meterinspection_logout:
                // calllogout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @OnClick({R.id.rl_details_layout, R.id.iv_details})
    void toggleMeterDetails() {
        details_expandable.toggle();
    }

    @OnClick({R.id.rl_meterdisplay_layout, R.id.iv_meterdisplay})
    void toggleMeterDisplay() {
        meterdisplay_expandable.toggle();
    }

    @OnClick({R.id.rl_meter_particulars, R.id.iv_meter_particulars})
    void toggleMeterParticulars() {
        meter_particulars_expandable.toggle();
    }

    @OnClick({R.id.rl_incriminating_layout, R.id.iv_incriminating})
    void toggleincriminatingLayout() {
        incriminating_expandable.toggle();
    }
    @OnClick({R.id.rl_meter_readings, R.id.iv_meter_readings})
    void toggleMeterReadingsLayout() {
        meter_readings_expandable.toggle();
    }@OnClick({R.id.rl_terminal_blocks, R.id.iv_terminal_blocks})
    void toggleTerminalBlocksLayout() {
        terminal_blocks_expandable.toggle();
    }

    @OnClick(R.id.btn_fetch)
    void getDetails() {
        if (Utility.isValueNullOrEmpty(et_service_no.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, getString(R.string.enetr_valid_scno));
        } else if (Utility.isValueNullOrEmpty(mMeterType)) {
            Utility.showCustomOKOnlyDialog(this, getString(R.string.select_mtr_type));
        } else if (Utility.isNetworkAvailable(this)) {
            getMeterDetails(et_service_no.getText().toString());
        } else {
            Utility.showCustomOKOnlyDialog(this, Utility.getResourcesString(this,
                    R.string.no_internet));
        }
    }

    private void getMeterDetails(String scno) {
        RequestParams params = new RequestParams();
        params.put("SCNO", "TPT254");
        params.put("MTRTYPE", "HT");
        prgDialog.show();
        prgDialog.setMessage("Please wait...");

        AsyncHttpClient client = new AsyncHttpClient();
        Utility.showLog("URL", Constants.GET_DETAILS);
        Utility.showLog("SCNO", scno);
        // client.addHeader("SCNO", scno);
        client.post(Constants.GET_DETAILS, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                if (prgDialog != null && prgDialog.isShowing()) {
                    prgDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("MSG")) {
                        Utility.showCustomOKOnlyDialog(MeterInspectionActivity.this, "Not a valid Service Number");
                    } else {
                        meterDetailsModel = new Gson().fromJson(jsonObject.toString(), MeterInspectionAddressModel.class);
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
                        Utility.showCustomOKOnlyDialog(MeterInspectionActivity.this, "Unable to Connect Server");
                        break;
                    case 500:
                        Utility.showCustomOKOnlyDialog(MeterInspectionActivity.this, "Something went wrong at server end");
                        break;
                    default:
                        Utility.showCustomOKOnlyDialog(MeterInspectionActivity.this, "Check Your Internet Connection and Try Again");

                        break;
                }
            }
        });
    }

    /* *Set Address  Data**/
    private void setData() {
        btn_fetch.setVisibility(View.GONE);
        ll_total.setVisibility(View.VISIBLE);
        et_category.setText(meterDetailsModel.getCATEGORY());
        et_section.setText(meterDetailsModel.getSECTION());
        et_distribution.setText(meterDetailsModel.getDISTRIBUTION());
        et_name_of_consumer.setText(meterDetailsModel.getNAME());
        et_address.setText(meterDetailsModel.getADDRESS());
        et_load.setText(meterDetailsModel.getLOAD());
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        int thisMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int thisDate = Calendar.getInstance().get(Calendar.DATE);
        String date = "";
        if (thisMonth < 10 && thisDate < 10) {
            date = "0" + thisDate + "-0" + thisMonth + "-" + thisYear;
        } else if (thisMonth < 10) {
            date = thisDate + "-0" + thisMonth + "-" + thisYear;
        } else if (thisDate < 10) {
            date = "0" + thisDate + "-" + thisMonth + "-" + thisYear;
        } else {
            date = thisDate + "-" + thisMonth + "-" + thisYear;
        }
        et_date.setText(date);

    }

    /* *Set MeterType Spinner Data**/
    private void setMeterTypeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("LTCT Metering");
        list.add("HT Metering");
        final ArrayList<String> strings = new ArrayList<>();
        strings.add("--Select--");
        strings.add("LTM");
        strings.add("HT");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_type.setAdapter(newlineAdapter);
        spn_meter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMeterType = strings.get(position);
                } else {
                    mMeterType = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set YearOfManufacture Spinner Data**/
    private void setYearOfManufactureSpinnerData() {
        final ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2009; i <= thisYear; i++) {
            years.add("" + i);
        }
        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, years);
        spn_year_manufacture.setAdapter(newlineAdapter);
        spn_year_manufacture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                mYearOfManufacturing = years.get(position);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    /* *Set Meter Changed Spinner Data**/
    private void setMeterChangedSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Yes");
        list.add("No");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_meter_changed.setAdapter(newlineAdapter);
        spn_meter_changed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMeterChanged = parent.getItemAtPosition(position).toString();
                } else {
                    mMeterChanged = "";
                }

                if (mMeterChanged.equals("Yes")) {
                    ll_meter_change_details.setVisibility(View.VISIBLE);
                }
                if (mMeterChanged.equals("No")) {
                    ll_meter_change_details.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private String getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
