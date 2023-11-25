package com.apcpdcl.departmentapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.adapters.SpinnerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ElevenKVLineSurveyDetailActivity extends AppCompatActivity {

    @BindView(R.id.et_location_no)
    EditText et_location_no;
    @BindView(R.id.et_pole)
    EditText et_pole;
    @BindView(R.id.et_loc_name)
    EditText et_loc_name;
    @BindView(R.id.spn_ss_name)
    Spinner spn_ss_name;
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
    @BindView(R.id.spn_addl_no)
    Spinner spn_addl_no;
    @BindView(R.id.spn_line_type)
    Spinner spn_line_type;
    @BindView(R.id.spn_line_size)
    Spinner spn_line_size;
    @BindView(R.id.spn_add_equipment)
    Spinner spn_add_equipment;
    @BindView(R.id.spn_rmu)
    Spinner spn_rmu;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    private String mPoleConfig = "";
    private String mPoleType = "";
    private String mPoleHeight = "";
    private String mCondConfig = "";
    private String mAddlType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kv_survey_detail_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setPoleConfigurationSpinnerData();
        setPoleTYpeSpinnerData();
        setPoleHeightSpinnerData();
        setConductorConfigurationSpinnerData();
        setAddlTypeSpinnerData();
    }

    /* *Set Pole Configuration Spinner Data**/
    private void setPoleConfigurationSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Lt");
        list.add("Ht");

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
}
