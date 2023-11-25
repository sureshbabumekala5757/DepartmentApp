package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;

import com.apcpdcl.departmentapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class PMIDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_dtr_survey)
    Button btn_dtr_survey;
    @BindView(R.id.btn_line_survey)
    Button btn_line_survey;
    @BindView(R.id.btn_11kv)
    Button btn_11kv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmi_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_dtr_survey)
    void navigateToDTRSurveyScreen() {
        Intent in = new Intent(getApplicationContext(), DTRStructureSurveyActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_line_survey)
    void navigateTo11kvLineSurveyScreen() {
        Intent in = new Intent(getApplicationContext(), AELCRequestListActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_11kv)
    void navigateToLineSurveyScreen() {
        Intent in = new Intent(getApplicationContext(), AELCRequestListActivity.class);
        startActivity(in);
    }
}
