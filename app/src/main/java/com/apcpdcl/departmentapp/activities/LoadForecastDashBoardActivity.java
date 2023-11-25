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
 * on 28-03-2018.
 */

public class LoadForecastDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_form)
    Button btn_form;
    @BindView(R.id.btn_list)
    Button btn_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_forecast_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
    }                                   


    @OnClick(R.id.btn_form)
    void navigateToEdit() {
        Intent in = new Intent(this, LoadForecastFormActivity.class);
        startActivity(in);

    }

    @OnClick(R.id.btn_list)
    void navigateToPending() {
        Intent in = new Intent(this, LoadForecastListActivity.class);
        startActivity(in);
    }

}
