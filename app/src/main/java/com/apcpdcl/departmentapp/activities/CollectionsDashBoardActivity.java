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

public class CollectionsDashBoardActivity extends AppCompatActivity{

    @BindView(R.id.btn_digital_trans)
    Button btn_digital_trans;
    @BindView(R.id.btn_counterwise)
    Button btn_counterwise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collections_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_digital_trans)
    void navigateToDigitalTransactions() {
        Intent in = new Intent(this, DigitalTranscationActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.btn_counterwise)
    void counterwiseTransactions() {
        Intent in = new Intent(this, CounterWise_Report.class);
        startActivity(in);
    }
}

