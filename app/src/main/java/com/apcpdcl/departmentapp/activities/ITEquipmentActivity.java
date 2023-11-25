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

public class ITEquipmentActivity extends AppCompatActivity implements View.OnClickListener {
    Button dataMovementbtn, complaintbtn;
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itequip);
        init();
    }
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pDialog = new ProgressDialog(this);

        dataMovementbtn = (Button) findViewById(R.id.dataMovementbtn);
        complaintbtn = (Button) findViewById(R.id.complaintbtn);

        dataMovementbtn.setOnClickListener(this);
        complaintbtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view == dataMovementbtn) {
            Intent intent = new Intent(ITEquipmentActivity.this, DataMovementActivity.class);
            startActivity(intent);
        }
        if (view == complaintbtn) {
            Intent intent = new Intent(ITEquipmentActivity.this, ComplaintActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

}
