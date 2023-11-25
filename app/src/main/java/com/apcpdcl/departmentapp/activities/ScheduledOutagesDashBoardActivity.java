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

public class ScheduledOutagesDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_schedule)
    Button btn_schedule;
    @BindView(R.id.btn_scheduled_outages)
    Button btn_scheduled_outages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outages_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.btn_schedule)
    void navigateToLiveInterruptions() {
        Intent in = new Intent(getApplicationContext(), ScheduleOutageFormActivity.class);
        startActivity(in);
    }
    @OnClick(R.id.btn_scheduled_outages)
    void navigateToLCOperations() {
        Intent in = new Intent(getApplicationContext(), ScheduledOutagesListActivity.class);
        startActivity(in);
        //Utility.showToastMessage(this, "In Progress...");
    }
}
