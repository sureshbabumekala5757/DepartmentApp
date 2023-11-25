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

public class OMSDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_live_interruptions)
    Button btn_live_interruptions;
    @BindView(R.id.btn_saidi_saifi)
    Button btn_saidi_saifi;
    @BindView(R.id.btn_lc_operations)
    Button btn_lc_operations;
    @BindView(R.id.btn_scheduled_outages)
    Button btn_scheduled_outages;
    @BindView(R.id.btn_interruptions_form)
    Button btn_interruptions_form;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oms_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        btn_interruptions_form.setVisibility(View.GONE);
    }


    @OnClick(R.id.btn_live_interruptions)
    void navigateToLiveInterruptions() {
        Intent in = new Intent(getApplicationContext(), LiveInterruptionsActivity.class);
        startActivity(in);
    }


    @OnClick(R.id.btn_saidi_saifi)
    void navigateToSaidiSaifi() {
        Intent in = new Intent(getApplicationContext(), SaidiSaifiActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_interruptions_form)
    void navigateToInterruptionsForm() {
        Intent in = new Intent(getApplicationContext(), InterruptionsRequestFormActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_interruptions)
    void navigateToInterruptionsReport() {
        Intent in = new Intent(getApplicationContext(), InterruptionsListActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_scheduled_outages)
    void navigateToScheduledOutages() {
        Intent in = new Intent(getApplicationContext(), ScheduledOutagesDashBoardActivity.class);
        startActivity(in);
    }

}
