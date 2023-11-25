package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
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

public class LCDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_lc_request)
    Button btn_lc_request;
    @BindView(R.id.btn_lc_return)
    Button btn_lc_return;
    @BindView(R.id.btn_lc)
    Button btn_lc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_lc_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        btn_lc_request.setText("LC Issue Form");
        btn_lc_return.setText("Returned LCs");
        btn_lc.setVisibility(View.GONE);
    }


    @OnClick(R.id.btn_lc_request)
    void navigateToLiveInterruptions() {
        Intent in = new Intent(getApplicationContext(), LCRequestListActivity .class);
        startActivity(in);
    }
    @OnClick(R.id.btn_lc_return)
    void navigateToLCOperations() {
        Intent in = new Intent(getApplicationContext(), LCReturnedListActivity.class);
        startActivity(in);
    }


}
