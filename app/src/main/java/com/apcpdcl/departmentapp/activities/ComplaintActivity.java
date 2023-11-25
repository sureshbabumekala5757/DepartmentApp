package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;

public class ComplaintActivity extends AppCompatActivity implements View.OnClickListener {
    Button cmpreqgentbtn, comprequpdatebtn;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        init();
    }
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pDialog = new ProgressDialog(this);

        cmpreqgentbtn = (Button) findViewById(R.id.cmpreqgentbtn);
        comprequpdatebtn = (Button) findViewById(R.id.comprequpdatebtn);

        cmpreqgentbtn.setOnClickListener(this);
        comprequpdatebtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == cmpreqgentbtn) {
            Intent intent = new Intent(ComplaintActivity.this, ComplaintReqGenActivity.class);
            startActivity(intent);
        }
        if (view == comprequpdatebtn) {
            Intent intent = new Intent(ComplaintActivity.this, ComplaintReqUpdateActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

}
