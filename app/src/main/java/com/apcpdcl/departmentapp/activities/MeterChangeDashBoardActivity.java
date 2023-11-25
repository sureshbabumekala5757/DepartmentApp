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
 * on 28-03-2018.
 */

public class MeterChangeDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_rejected)
    Button btn_rejected;
    @BindView(R.id.btn_pending)
    Button btn_pending;
    @BindView(R.id.btn_delete)
    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meter_change_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
    }                                   


    @OnClick(R.id.btn_rejected)
    void navigateToEdit() {
        Intent in = new Intent(this, RejectedListActivity.class);
        startActivity(in);

    }

    @OnClick(R.id.btn_pending)
    void navigateToPending() {
        Intent in = new Intent(this, MeterChangeListActivity.class);
        in.putExtra(Constants.FROM, "Pending");
        startActivity(in);
    }

    @OnClick(R.id.btn_delete)
    void navigateToDelete() {
        Intent in = new Intent(this, MeterChangeListActivity.class);
        in.putExtra(Constants.FROM, "Delete");
        startActivity(in);
    }
}
