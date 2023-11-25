package com.apcpdcl.departmentapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.services.NetworkReceiver;

public class DataMovementActivity extends AppCompatActivity implements View.OnClickListener {
    NetworkReceiver objNetworkReceiver = new NetworkReceiver();
    ProgressDialog pDialog;
    public static TextView tvresult,tvlatitude,tvlongitude;
    Button scanbtn;
    LocationManager locationManager;
    String str_Latitude, str_Longitude;
    private static final int REQUEST_LOCATION = 1;
    double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamovement);
        init();
    }
    private void init() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        tvresult = (TextView) findViewById(R.id.tvresult);
        tvlatitude = (TextView) findViewById(R.id.tvlat);
        tvlongitude = (TextView) findViewById(R.id.tvlong);


        pDialog = new ProgressDialog(this);

        scanbtn = (Button) findViewById(R.id.scanbtn);
        scanbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == scanbtn) {

            try {
                GPSTracker gps = new GPSTracker(DataMovementActivity.this);

                // Check if GPS enabled
                if(gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    str_Latitude=String.valueOf(latitude);
                    str_Longitude=String.valueOf(longitude);

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // Can't get location.
                    // GPS or network is not enabled.
                    // Ask user to enable GPS/network in settings.
                    gps.showSettingsAlert();

                }
                    tvlatitude.setText(str_Latitude);
                    tvlongitude.setText(str_Longitude);
                    Intent intent = new Intent(DataMovementActivity.this, ScanActivity.class);
                    startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        GPSTracker gps = new GPSTracker(DataMovementActivity.this);

        // Check if GPS enabled
        if(gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            str_Latitude=String.valueOf(latitude);
            str_Longitude=String.valueOf(longitude);

            // \n is for new line
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();

        }
    }
}



