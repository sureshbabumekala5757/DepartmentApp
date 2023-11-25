package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 25-09-2018.
 */

public class SurveyDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_line_survey)
    Button btn_line_survey;
    @BindView(R.id.btn_dtr_survey)
    Button btn_dtr_survey;
    @BindView(R.id.btn_lt_line_survey)
    Button btn_lt_line_survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_line_survey)
    void navigateToLineSurvey() {
        //    Intent in = new Intent(getApplicationContext(), ElevenKVLineSurveyActivity.class);
        Intent in = new Intent(getApplicationContext(), LTLineSurveyActivity.class);
        in.putExtra(Constants.FROM, "11KV LINE");
        startActivity(in);
    }

    @OnClick(R.id.btn_dtr_survey)
    void navigateToDTRSurvey() {
        Intent in = new Intent(getApplicationContext(), DTRStructureSurveyActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.btn_lt_line_survey)
    void navigateToLTLineSurvey() {
        Intent in = new Intent(getApplicationContext(), LTLineSurveyActivity.class);
        in.putExtra(Constants.FROM, "LT LINE");
        startActivity(in);
    }

}
