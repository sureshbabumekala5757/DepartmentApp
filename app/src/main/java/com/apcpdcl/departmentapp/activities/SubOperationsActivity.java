package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.apcpdcl.departmentapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubOperationsActivity extends AppCompatActivity {
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.btn_attendance)
    Button btn_attendance;
    @BindView(R.id.btn_substation_co)
    Button btn_substation_co;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_operations_activity);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_substation_co)
    void navigateToSubStationList() {
        Intent intent = new Intent(this, SubStationListActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btn_register)
    void navigateToRegisterScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
