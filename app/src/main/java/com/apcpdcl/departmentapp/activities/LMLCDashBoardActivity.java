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

public class LMLCDashBoardActivity extends AppCompatActivity {

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
        btn_lc.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.btn_lc_request)
    void navigateToLiveInterruptions() {
        Intent in = new Intent(getApplicationContext(), LCRequestFormActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_lc_return)
    void navigateToLCOperations() {
        Intent in = new Intent(getApplicationContext(), LMLCReturnListActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_lc)
    void navigateToLC() {
        Intent in = new Intent(getApplicationContext(), RequestedLCListActivity.class);
        startActivity(in);
    }


}
