package com.apcpdcl.departmentapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;
import com.apcpdcl.departmentapp.services.NetworkReceiver;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LTLineSurveyDetailActivity extends AppCompatActivity implements LocationListener {

    @BindView(R.id.spn_feedings)
    Spinner spn_feedings;
    @BindView(R.id.spn_pole_config)
    Spinner spn_pole_config;
    @BindView(R.id.spn_pole_type)
    Spinner spn_pole_type;
    @BindView(R.id.spn_pole_height)
    Spinner spn_pole_height;
    @BindView(R.id.spn_cond_config)
    Spinner spn_cond_config;
    @BindView(R.id.spn_addl_type)
    Spinner spn_addl_type;
    @BindView(R.id.spn_feeder_type)
    Spinner spn_feeder_type;/*
    @BindView(R.id.spn_wires_no)
    Spinner spn_wires_no;*/
    @BindView(R.id.et_no_wires)
    EditText et_no_wires;
    @BindView(R.id.spn_tree)
    Spinner spn_tree;
    @BindView(R.id.spn_maintenance)
    Spinner spn_maintenance;
    @BindView(R.id.spn_sizeofLTABCable)
    Spinner spn_sizeofLTABCable;
    @BindView(R.id.spn_spt_one)
    Spinner spn_spt_one;
    @BindView(R.id.spn_spt_two)
    Spinner spn_spt_two;
    @BindView(R.id.spn_strut)
    Spinner spn_strut;
    @BindView(R.id.spn_stay_one)
    Spinner spn_stay_one;
    @BindView(R.id.spn_stay_two)
    Spinner spn_stay_two;
    @BindView(R.id.spn_xarms)
    Spinner spn_xarms;
    @BindView(R.id.spn_insulator)
    Spinner spn_insulator;
    @BindView(R.id.spn_int_pole_one)
    Spinner spn_int_pole_one;
    @BindView(R.id.spn_int_pole_two)
    Spinner spn_int_pole_two;
    @BindView(R.id.spn_cdtr_one)
    Spinner spn_cdtr_one;
    @BindView(R.id.spn_cdtr_two)
    Spinner spn_cdtr_two;
    @BindView(R.id.et_int_pole)
    EditText et_int_pole;
    @BindView(R.id.et_cdtr)
    EditText et_cdtr;
    @BindView(R.id.et_remarks)
    EditText et_remarks;
    @BindView(R.id.et_dtr_no)
    EditText et_dtr_no;
    @BindView(R.id.et_location_no)
    EditText et_location_no;
    @BindView(R.id.et_pole_no)
    EditText et_pole_no;
    @BindView(R.id.et_one_ph)
    EditText et_one_ph;
    @BindView(R.id.et_three_ph)
    EditText et_three_ph;
    @BindView(R.id.ll_maintenance)
    LinearLayout ll_maintenance;
    @BindView(R.id.poledata_layout)
    LinearLayout poledata_layout;
    @BindView(R.id.clearandmaintain_layout)
    LinearLayout clearandmaintain_layout;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.btn_polesubmit)
    Button btn_polesubmit;
    @BindView(R.id.btn_poletruesubmit)
    Button btn_poletruesubmit;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    private String mInsulator = "";
    private String mStay = "";
    private String mXArms = "";
    private String mStrut = "";
    private String mFeederType = "";
    private String mPoleConfig = "";
    private String mltCableSize = "";
    private String mPoleType = "";
    private String mPoleHeight = "";
    private String mCondConfig = "";
    private String mAddlType = "";
    private String mMaintenance = "";
    private String mTree = "";
    private String mCondType = "";
    private String mSupport = "";
    private String mCondReq = "";
    private String mIntPoleType = "";
    private String strFeedings = "";
    private String strDtrCode = "";
    private String dtrCode = "";
    private String strPolenum = "";
    private String strStatus = "";
    private String sectionCode = "";
    private String userID = "";
    private String Loc_Name = "";
    private String mPoleReq = "";
    private String str_Latitude = "";
    private String str_Longitude = "";
    private String subCode = "";
    private String ckts = "";
    LocationManager locationManager;
    String mprovider;
    TelephonyManager manager;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    private String from = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lt_line_survey_detail_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }


    @OnClick(R.id.btn_submit)
    void navigateToNextScreen() {
        if (validateFields()) {
            if (objNetworkReceiver.hasInternetConnection(LTLineSurveyDetailActivity.this)) {
                strPolenum = et_pole_no.getText().toString();
                invokePoleDataService(dtrCode, strPolenum);
            } else {
                Toast.makeText(getApplicationContext(), "Please check your Internet connection", Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean validateFields() {
        if (Utility.isValueNullOrEmpty(strFeedings)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Feedings.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_pole_no.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Pole Number.");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_polesubmit)
    void navigateToPoleData() {
        if (validatePoleFields()) {
            addPoleData();
        }
    }

    private boolean validatePoleFields() {
        if (Utility.isValueNullOrEmpty(mPoleConfig)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Pole Configuration.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mPoleType)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Pole Type.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mPoleHeight)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Pole Height.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mCondConfig)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Conductor Configuration.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mAddlType)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Type of Addl. Support.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mFeederType)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Type of Lt Feeder.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_no_wires.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter No. of Wires.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mltCableSize)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Size of LT AB Cable.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_one_ph.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter No. of 1Ph Services.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_three_ph.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter No. of 3Ph Services.");
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_poletruesubmit)
    void navigateToPoleTrueData() {
        if (Utility.isValueNullOrEmpty(mTree)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Tree Clearance Required or not.");
        } else if (Utility.isValueNullOrEmpty(mMaintenance)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Maintenance Required or not.");
        } else {
            if (mMaintenance.equalsIgnoreCase("Yes")) {
                if (validateMaintenance()) {
                    addClearance_MaintainData();
                }
            } else {
                addNoClearance_MaintainData();
            }
        }
    }


    private void init() {
        pDialog = new ProgressDialog(this);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            strDtrCode = (String) bd.get("structurecode");
            subCode = (String) bd.get("SubStationCode");
            ckts = (String) bd.get("Ckts");
            from = (String) bd.get(Constants.FROM);
            if (from.contains("11")){
                toolbar_title.setText("11KV LINE SURVEY");
            }
            et_dtr_no.setText(strDtrCode);
        } else {
            strDtrCode = "";
            et_dtr_no.setText(strDtrCode);
        }
        Pattern pattern = Pattern.compile("- *");
        Matcher matcher = pattern.matcher(strDtrCode);
        if (matcher.find()) {
            dtrCode = strDtrCode.substring(0, matcher.start());
            Loc_Name = strDtrCode.substring(matcher.end());
            et_location_no.setText(Loc_Name);
        }
        SharedPreferences prefs = getSharedPreferences("loginPrefs", 0);
        sectionCode = prefs.getString("Section_Code", "");
        userID = prefs.getString("UserName", "");
        if (Utility.isValueNullOrEmpty(Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER))) {
            getDeviceId();
        }
        Utility.showLog("IMEI", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
        setFeedingsSpinnerData();
        setPoleConfigurationSpinnerData();
        setPoleTYpeSpinnerData();
        setPoleHeightSpinnerData();
        setConductorConfigurationSpinnerData();
        setAddlTypeSpinnerData();
        setTreeClearanceSpinnerData();
        setMaintenanceSpinnerData();
        setInterPoleRequiredSpinnerData();
        setLTFilterTypeSpinnerData();
        setConductorTypeSpinnerData();
        setInterPoleTypeSpinnerData();
        setConductorRequiredSpinnerData();
        setConditionOfSupportSpinnerData();
        setConditionOfStrutSpinnerData();
        setConditionOfStaySpinnerData();
        setXArmsSpinnerData();
        setInsulatorSpinnerData();
        setSizeOfLTABCableSpinnerData();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        if (mprovider != null && !mprovider.equals("")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);
            locationManager.requestLocationUpdates(mprovider, 15000, 1, (LocationListener) this);
            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }
    }


    private void setFeedingsSpinnerData() {
        final ArrayList<String> feedingsList = new ArrayList<>();
        feedingsList.add("--Select--");
        feedingsList.add("A");
        feedingsList.add("B");

        SpinnerAdapter feedingAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, feedingsList);
        spn_feedings.setAdapter(feedingAdapter);
        spn_feedings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    strFeedings = parent.getItemAtPosition(position).toString();
                } else {
                    strFeedings = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    /* *Set Size of LT AB Cable Spinner Data**/
    private void setSizeOfLTABCableSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("LT AB CABLE 1 X 16 + 25 SQ. MM");
        list.add("LT AB CABLE 2 X 16 + 25 SQ. MM");
        list.add("LT AB CABLE 3 X 16 + 25 SQ. MM");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_sizeofLTABCable.setAdapter(newlineAdapter);
        spn_sizeofLTABCable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mltCableSize = parent.getItemAtPosition(position).toString();
                } else {
                    mltCableSize = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Pole Configuration Spinner Data**/
    private void setPoleConfigurationSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Lt");
        list.add("Ht");
        list.add("Lt/Ht");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_pole_config.setAdapter(newlineAdapter);
        spn_pole_config.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mPoleConfig = parent.getItemAtPosition(position).toString();
                } else {
                    mPoleConfig = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Pole Type Spinner Data**/
    private void setPoleTYpeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Spun");
        list.add("RCC");
        list.add("PSCC");
        list.add("TOWER");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_pole_type.setAdapter(newlineAdapter);
        spn_pole_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mPoleType = parent.getItemAtPosition(position).toString();
                } else {
                    mPoleType = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Pole Height Spinner Data**/
    private void setPoleHeightSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("12.5");
        list.add("9.1");
        list.add("8");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_pole_height.setAdapter(newlineAdapter);
        spn_pole_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mPoleHeight = parent.getItemAtPosition(position).toString();
                } else {
                    mPoleHeight = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Conductor Configuration Spinner Data**/
    private void setConductorConfigurationSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("AAAc");
        list.add("110sqmm");
        list.add("100");
        list.add("55");
        list.add("34");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_cond_config.setAdapter(newlineAdapter);
        spn_cond_config.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mCondConfig = parent.getItemAtPosition(position).toString();
                } else {
                    mCondConfig = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Addl Type Spinner Data**/
    private void setAddlTypeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Strut");
        list.add("Stay");
        list.add("Inline");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_addl_type.setAdapter(newlineAdapter);
        spn_addl_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mAddlType = parent.getItemAtPosition(position).toString();
                } else {
                    mAddlType = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Maintenance Spinner Data**/
    private void setMaintenanceSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Yes");
        list.add("No");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_maintenance.setAdapter(newlineAdapter);
        spn_maintenance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mMaintenance = parent.getItemAtPosition(position).toString();
                    if (mMaintenance.equalsIgnoreCase("Yes")) {
                        ll_maintenance.setVisibility(View.VISIBLE);
                    } else {
                        ll_maintenance.setVisibility(View.GONE);
                    }
                } else {
                    mMaintenance = "";
                    ll_maintenance.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Intermediate Pole Required Spinner Data**/
    private void setInterPoleRequiredSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Yes");
        list.add("No");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_int_pole_one.setAdapter(newlineAdapter);
        spn_int_pole_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mPoleReq = parent.getItemAtPosition(position).toString();
                    if (mPoleReq.equalsIgnoreCase("Yes")) {
                        spn_int_pole_two.setVisibility(View.VISIBLE);
                        et_int_pole.setVisibility(View.VISIBLE);
                    } else {
                        spn_int_pole_two.setVisibility(View.GONE);
                        et_int_pole.setVisibility(View.GONE);
                    }
                } else {
                    mPoleReq = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Intermediate  Required Pole Type Spinner Data**/
    private void setInterPoleTypeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("8 MTS 140KGS PSCC POLES");
        list.add("8 MTS 200KGS PSCC POLES");
        list.add("9.1 MTS 280KGS PSCC POLES");
        list.add("11 MTS SPUN POLES");
        list.add("12 MTS Spun Pole");
        list.add("11 Mts 365 Kgs PSCC Poles ");
        list.add("12.5 MTS SPUN POLES");
        list.add("11 MTS 365 Kg PSCC POLES");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_int_pole_two.setAdapter(newlineAdapter);
        spn_int_pole_two.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mIntPoleType = parent.getItemAtPosition(position).toString();
                } else {
                    mIntPoleType = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Conductor Required Spinner Data**/
    private void setConductorRequiredSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Yes");
        list.add("No");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_cdtr_one.setAdapter(newlineAdapter);
        spn_cdtr_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mCondReq = parent.getItemAtPosition(position).toString();
                    if (mCondReq.equalsIgnoreCase("Yes")) {
                        spn_cdtr_two.setVisibility(View.VISIBLE);
                        et_cdtr.setVisibility(View.VISIBLE);
                    } else {
                        spn_cdtr_two.setVisibility(View.GONE);
                        et_cdtr.setVisibility(View.GONE);
                    }
                } else {
                    mCondReq = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Required Conductor Type Spinner Data**/
    private void setConductorTypeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("AAA CONDUCTOR 34 SQ. MM");
        list.add("AAA CONDUCTOR 55 SQ. MM");
        list.add("AAA CONDUCTOR 100 SQ. MM");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_cdtr_two.setAdapter(newlineAdapter);
        spn_cdtr_two.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mCondType = parent.getItemAtPosition(position).toString();
                } else {
                    mCondType = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Tree Clearance Spinner Data**/
    private void setTreeClearanceSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Yes");
        list.add("No");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_tree.setAdapter(newlineAdapter);
        spn_tree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mTree = parent.getItemAtPosition(position).toString();
                } else {
                    mTree = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set LT Filter Type Spinner Data**/
    private void setLTFilterTypeSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Ab cable single circuit");
        list.add("Ab cable double circuit");
        list.add("Conductor single circuit");
        list.add("Conductor double circuit");

        SpinnerAdapter FeederTypeAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_feeder_type.setAdapter(FeederTypeAdapter);
        spn_feeder_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mFeederType = parent.getItemAtPosition(position).toString();
                } else {
                    mFeederType = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Condition of Support Spinner Data**/
    private void setConditionOfSupportSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Damaged");
        list.add("Ok");

        SpinnerAdapter FeederTypeAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_spt_one.setAdapter(FeederTypeAdapter);
        spn_spt_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mSupport = parent.getItemAtPosition(position).toString();
                } else {
                    mSupport = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Condition of Strut Spinner Data**/
    private void setConditionOfStrutSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Required");
        list.add("Not Required");

        SpinnerAdapter FeederTypeAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_strut.setAdapter(FeederTypeAdapter);
        spn_strut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mStrut = parent.getItemAtPosition(position).toString();
                } else {
                    mStrut = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Condition of Stay Spinner Data**/
    private void setConditionOfStaySpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Damaged");
        list.add("Ok");

        SpinnerAdapter FeederTypeAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_stay_one.setAdapter(FeederTypeAdapter);
        spn_stay_one.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mStay = parent.getItemAtPosition(position).toString();
                } else {
                    mStay = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set X-Arms Spinner Data**/
    private void setXArmsSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Damaged");
        list.add("Ok");

        SpinnerAdapter FeederTypeAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_xarms.setAdapter(FeederTypeAdapter);
        spn_xarms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mXArms = parent.getItemAtPosition(position).toString();
                } else {
                    mXArms = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    /* *Set Insulator Spinner Data**/
    private void setInsulatorSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Damaged");
        list.add("Ok");

        SpinnerAdapter FeederTypeAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_insulator.setAdapter(FeederTypeAdapter);
        spn_insulator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mInsulator = parent.getItemAtPosition(position).toString();
                } else {
                    mInsulator = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private void invokePoleDataService(String dtrCode, String strPoleNum) {

        pDialog.show();
        pDialog.setMessage("Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();


        client.get(Constants.URL+"pmi11kvservice/attrcheck?poleno=" + strPoleNum + "&seccd=" + sectionCode +  "&dtrcode=" + dtrCode + "&SURVEY_TYPE=LT%20LINE", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Utility.showLog("response", response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    strStatus = obj.getString("status");

                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.dismiss();
                    }
                    if (strStatus.equalsIgnoreCase("false")) {
                        poledata_layout.setVisibility(View.VISIBLE);
                        clearandmaintain_layout.setVisibility(View.GONE);
                        btn_submit.setVisibility(View.GONE);
                        btn_poletruesubmit.setVisibility(View.GONE);
                        btn_polesubmit.setVisibility(View.VISIBLE);
                        et_pole_no.setEnabled(false);
                        et_pole_no.setFocusable(false);
                        et_pole_no.setCursorVisible(false);
                        spn_feedings.setEnabled(false);
                        spn_feedings.setFocusable(false);
                        spn_feedings.setFocusableInTouchMode(false);
                        spn_feedings.setClickable(false);

                    } else if (strStatus.equalsIgnoreCase("true")) {
                        poledata_layout.setVisibility(View.GONE);
                        clearandmaintain_layout.setVisibility(View.VISIBLE);
                        btn_submit.setVisibility(View.GONE);
                        btn_poletruesubmit.setVisibility(View.VISIBLE);
                        btn_polesubmit.setVisibility(View.GONE);
                        et_pole_no.setEnabled(false);
                        et_pole_no.setFocusable(false);
                        et_pole_no.setCursorVisible(false);
                        spn_feedings.setEnabled(false);
                        spn_feedings.setFocusable(false);
                        spn_feedings.setFocusableInTouchMode(false);
                        spn_feedings.setClickable(false);
                    } else {
                        Utility.showCustomOKOnlyDialog(LTLineSurveyDetailActivity.this,
                                Utility.getResourcesString(LTLineSurveyDetailActivity.this, R.string.err_session));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                Utility.showCustomOKOnlyDialog(LTLineSurveyDetailActivity.this,
                        Utility.getResourcesString(LTLineSurveyDetailActivity.this, R.string.err_session));
                Utility.showLog("error", error.toString());
            }
        });
    }

    private void addPoleData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("SUB_STATION", subCode);
            jsonObject.put("POLE_NUM", et_pole_no.getText().toString());
            jsonObject.put("LOC_NAME", et_location_no.getText().toString());
            jsonObject.put("POLE_CONFIG", mPoleConfig);
            jsonObject.put("POLE_TYPE", mPoleType);
            jsonObject.put("POLE_HEIGHT", mPoleHeight);
            jsonObject.put("CONDUCTOR_CONFIG", mCondConfig);
            jsonObject.put("SUPPORT_TYPE", mAddlType);
            jsonObject.put("NO_OF_CKTS", ckts);
            jsonObject.put("DTR", dtrCode);
            jsonObject.put("LATITUDE", str_Latitude);
            jsonObject.put("LONGITUDE", str_Longitude);
            jsonObject.put("SEC_CODE", sectionCode);
            jsonObject.put("IMEI", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
            jsonObject.put("USER_ID", userID);
            // jsonObject.put("SURVEY_TYPE", "LT LINE/11KV SURVEY");
            jsonObject.put("SURVEY_TYPE", "LT LINE");
            savePoleData(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addClearance_MaintainData() {

        JSONObject maintainJsonObject = new JSONObject();
        JSONObject maintainInnerJsonObject = new JSONObject();
        try {
            if (mMaintenance.equalsIgnoreCase("Yes")) {
                maintainJsonObject.put("SUB_STATION", subCode);
                maintainJsonObject.put("LOC_NAME", et_location_no.getText().toString());
                maintainJsonObject.put("POLE_NUM", et_pole_no.getText().toString());
                maintainJsonObject.put("TREE_CLEARANCE", mTree);
                maintainJsonObject.put("MAINT_REQ", mMaintenance);
                maintainJsonObject.put("SUPPORT", mSupport);//Condition of Support
                maintainJsonObject.put("STRUT", mStrut); //Condition of Strut
                maintainJsonObject.put("STAY", mStay);//Condition of Stay
                maintainJsonObject.put("CROSSARMS", mXArms);//XArms
                maintainJsonObject.put("INSULATOR", mInsulator);//Insulators
                maintainJsonObject.put("SURVEY_TYPE", "LT LINE");
                //Intermediate pole required
                if (mPoleReq.equalsIgnoreCase("Yes")) {//Intermediate pole required
                    maintainJsonObject.put("INTERPOLE_NOS", et_int_pole.getText().toString());//Intermediate pole required
                    maintainInnerJsonObject.put(mIntPoleType, et_int_pole.getText().toString());// Intermediate pole required(Here key and value send by us)
                } else {
                    maintainJsonObject.put("INTERPOLE_NOS", "0");//Intermediate pole required
                }

                maintainJsonObject.put("INTERPOLE_REQ", mPoleReq);

                //Conductor
                if (mCondReq.equalsIgnoreCase("Yes")) {
                    maintainJsonObject.put("CONDUCTOR", "Damaged");//Conductor
                    maintainInnerJsonObject.put(mCondType, et_cdtr.getText().toString());// Conductor(Here key and value send by us)
                } else {
                    maintainJsonObject.put("CONDUCTOR", "NotDamaged");//Conductor
                }

                maintainJsonObject.put("REMARKS", et_remarks.getText().toString());
                maintainJsonObject.put("SEC_CODE", sectionCode);
                maintainJsonObject.put("IMEI", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
                maintainJsonObject.put("USER_ID", userID);
                maintainJsonObject.put("MATERIAL", maintainInnerJsonObject);
                maintainJsonObject.put("DTR", dtrCode);
                saveMaintainPoleData(maintainJsonObject);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validateMaintenance() {
        if (Utility.isValueNullOrEmpty(mSupport)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Condition of Support.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mStrut)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Condition of Strut.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mStay)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Condition of Stay.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mXArms)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select X-arms.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mInsulator)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Insulators.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mPoleReq)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Intermediate Pole Required.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mIntPoleType)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Intermediate Pole Type.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_int_pole.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Number of Poles Required.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mCondReq)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Conductor Required.");
            return false;
        } else if (Utility.isValueNullOrEmpty(mCondType)) {
            Utility.showCustomOKOnlyDialog(this, "Please Select Type Of Conductor.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_cdtr.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter No of Conductors Required.");
            return false;
        } else if (Utility.isValueNullOrEmpty(et_remarks.getText().toString())) {
            Utility.showCustomOKOnlyDialog(this, "Please Enter Remarks.");
            return false;
        }
        return true;
    }

    private void addNoClearance_MaintainData() {
        JSONObject nMaintainJsonObject = new JSONObject();
        try {
            nMaintainJsonObject.put("SUB_STATION", subCode);
            nMaintainJsonObject.put("LOC_NAME", et_location_no.getText().toString());
            nMaintainJsonObject.put("POLE_NUM", et_pole_no.getText().toString());
            nMaintainJsonObject.put("TREE_CLEARANCE", mTree);
            nMaintainJsonObject.put("MAINT_REQ", mMaintenance);
            nMaintainJsonObject.put("SEC_CODE", sectionCode);
            nMaintainJsonObject.put("IMEI", Utility.getSharedPrefStringData(this, Constants.IMEI_NUMBER));
            nMaintainJsonObject.put("USER_ID", userID);
            nMaintainJsonObject.put("USER_ID", userID);
            nMaintainJsonObject.put("SURVEY_TYPE", "LT LINE");
            saveMaintainPoleData(nMaintainJsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void savePoleData(JSONObject jsonObject) {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            client.post(this, Constants.URL+"pmi11kvservice/saveattr", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        JSONObject jsonObject = new JSONObject(response);
                        String strStatus = jsonObject.getString("status");
                        if (strStatus.equalsIgnoreCase("true")) {
                            Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                            poledata_layout.setVisibility(View.GONE);
                            clearandmaintain_layout.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);
                            btn_poletruesubmit.setVisibility(View.GONE);
                            btn_polesubmit.setVisibility(View.GONE);

                        } else if (strStatus.equalsIgnoreCase("false")) {
                            Toast.makeText(getApplicationContext(), "Insertion Failure", Toast.LENGTH_LONG).show();
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
                    Utility.showCustomOKOnlyDialog(LTLineSurveyDetailActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void saveMaintainPoleData(JSONObject jsonObject) {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(50000);
        HttpEntity entity;
        try {
            entity = new StringEntity(jsonObject.toString());
            client.post(this, Constants.URL+"pmi11kvservice/savemaintenance", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Utility.showLog("onSuccess", response);
                    try {
                        if (pDialog != null && pDialog.isShowing()) {
                            pDialog.dismiss();
                        }
                        JSONObject jsonObject = new JSONObject(response);
                        boolean strStatus = jsonObject.getBoolean("status");
                        if (strStatus) {
                            Toast.makeText(getApplicationContext(), "Inserted Successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), LTLineSurveyActivity.class);
                            i.putExtra(Constants.FROM, from);
                            startActivity(i);
                            finish();

                        } else{
                            Toast.makeText(getApplicationContext(), "Insertion Failure", Toast.LENGTH_LONG).show();
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
                    Utility.showCustomOKOnlyDialog(LTLineSurveyDetailActivity.this, error.getMessage());
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void getDeviceId() {
       /* manager = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
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
        String IMEI_NUMBER = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Utility.setSharedPrefStringData(this, Constants.IMEI_NUMBER,IMEI_NUMBER);
        Utility.showLog("IMEI", IMEI_NUMBER);

    }


    @Override
    public void onLocationChanged(Location location) {
        str_Latitude = String.valueOf(location.getLatitude());
        str_Longitude = String.valueOf(location.getLongitude());
        Log.e("ee", str_Longitude + str_Latitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
