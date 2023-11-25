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

public class AAOMeterChangeDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_exception)
    Button btn_exception;
    @BindView(R.id.btn_mtr_change)
    Button btn_mtr_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        btn_exception.setText("Delete Meter Changes");
        btn_mtr_change.setText("Pending Meter Changes");
    }


    @OnClick(R.id.btn_mtr_change)
    void navigateToMeterChange() {
        Intent in = new Intent(this, AAOMeterChangeListActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.btn_exception)
    void navigateToExceptions() {
        Intent in = new Intent(this, AAOMeterChangeDeleteListActivity.class);
        startActivity(in);
    }
}
