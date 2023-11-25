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
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DTRStructureSurveyActivity extends AppCompatActivity {

    @BindView(R.id.btn_next)
    Button btn_next;
    @BindView(R.id.spn_feeder)
    Spinner spn_feeder;
    @BindView(R.id.spn_phase)
    Spinner spn_phase;
    @BindView(R.id.spn_capacity)
    Spinner spn_capacity;
    @BindView(R.id.spn_load)
    Spinner spn_load;
    @BindView(R.id.spn_fencing)
    Spinner spn_fencing;
    @BindView(R.id.spn_mfd_year)
    Spinner spn_mfd_year;
    @BindView(R.id.spn_dtr_structure)
    Spinner spn_dtr_structure;
    @BindView(R.id.et_make)
    EditText et_make;
    @BindView(R.id.et_ss_no)
    EditText et_ss_no;
    @BindView(R.id.et_landmark)
    EditText et_landmark;
    @BindView(R.id.et_village)
    EditText et_village;
    private String mPhase = "";
    private String mLoad = "";
    private String mFencing= "";
    private String mYear= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtr_structure_survey_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setPhaseSpinnerData();
        setLoadSpinnerData();
        setFencingSpinnerData();
        setYearSpinnerData();
    }

    /* *Set Phase Spinner Data**/
    private void setPhaseSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("1");
        list.add("3");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_phase.setAdapter(newlineAdapter);
        spn_phase.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mPhase = parent.getItemAtPosition(position).toString();
                } else {
                    mPhase = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    /* *Set Load Spinner Data**/
    private void setLoadSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Industrial");
        list.add("Agricultural");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_load.setAdapter(newlineAdapter);
        spn_load.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mLoad = parent.getItemAtPosition(position).toString();
                } else {
                    mLoad = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    /* *Set Fencing Spinner Data**/
    private void setFencingSpinnerData() {
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        list.add("Yes");
        list.add("No");

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_fencing.setAdapter(newlineAdapter);
        spn_fencing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mFencing = parent.getItemAtPosition(position).toString();
                } else {
                    mFencing = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    /* *Set Year Spinner Data**/
    private void setYearSpinnerData() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        final ArrayList<String> list = new ArrayList<>();
        list.add("--Select--");
        for (int i = year-25; i <= year; i++) {
            list.add(""+i);
        }

        SpinnerAdapter newlineAdapter = new SpinnerAdapter(this,
                android.R.layout.simple_spinner_item, list);
        spn_mfd_year.setAdapter(newlineAdapter);
        spn_mfd_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
                if (position != 0) {
                    mYear = parent.getItemAtPosition(position).toString();
                } else {
                    mYear = "";
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
}
