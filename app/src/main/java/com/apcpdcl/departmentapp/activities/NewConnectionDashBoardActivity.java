package com.apcpdcl.departmentapp.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.apcpdcl.departmentapp.R;
import com.apcpdcl.departmentapp.utils.Constants;
import com.apcpdcl.departmentapp.utils.Utility;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Haseen
 * on 28-03-2018.
 */

public class NewConnectionDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.btn_search)
    Button btn_search;
    @BindView(R.id.btn_edit)
    Button btn_edit;
    @BindView(R.id.btn_pending)
    Button btn_pending;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_connection_dashboard_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);
        toolbar_title.setText(Utility.getResourcesString(this, R.string.new_services));
        btn_pending.setText(Utility.getResourcesString(this,R.string.pending_registrations));
    }

    @OnClick(R.id.btn_search)
    void navigateToSearch() {
        if (Utility.isLocationEnabled(this)) {
            Intent in = new Intent(getApplicationContext(), NewConnectionReleaseActivity.class);
            in.putExtra(Constants.FROM, "Search To Tag Location");
            startActivity(in);
        }else {
            displayLocationSettingsRequest();
        }
    }

    @OnClick(R.id.btn_edit)
    void navigateToEdit() {
        if (Utility.isLocationEnabled(this)) {
            Intent in = new Intent(getApplicationContext(), NewConnectionReleaseActivity.class);
            in.putExtra(Constants.FROM, "Edit To Tag Location");
            startActivity(in);
        }else {
            displayLocationSettingsRequest();
        }
    }

    @OnClick(R.id.btn_pending)
    void navigateToPending() {
        Intent in = new Intent(getApplicationContext(), LMRegistrationListActivity.class);
        startActivity(in);
    }
    public void displayLocationSettingsRequest() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(NewConnectionDashBoardActivity.this, GPSTrackerActivity.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Utility.showLog("PendingIntent", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Utility.showLog("Location", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}
