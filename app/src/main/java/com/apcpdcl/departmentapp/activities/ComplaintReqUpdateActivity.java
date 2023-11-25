package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;

public class ComplaintReqUpdateActivity extends AppCompatActivity{
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintreqlist);
        init();
    }
    private void init() {

    }
}
