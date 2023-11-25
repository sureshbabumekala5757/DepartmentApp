package com.apcpdcl.departmentapp.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.apcpdcl.departmentapp.utils.Constants;

import java.io.PrintWriter;
import java.io.StringWriter;

//import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * location update service continues to running and getting location information
 */
public class LocationService extends JobService implements LocationComponent.ILocationProvider {

    private static final String TAG = LocationService.class.getSimpleName();
    public static final int LOCATION_MESSAGE = 9999;

    private LocationComponent locationUpdatesComponent;

    //private Messenger messageHandler;

    public LocationService() {
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i(TAG, "onStartJob....");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(TAG, "onStopJob....");

        return false;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "created...............");

        try {
            /*if (Build.VERSION.SDK_INT >= O) {
                String CHANNEL_ID = "my_app";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.location_updates)).build();
                startForeground(1, notification);
            }*/

            locationUpdatesComponent = new LocationComponent(this);
            locationUpdatesComponent.onCreate(this);
        } catch (Exception e) {
            caughtException(e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent != null) {
                if (intent.getAction() != null) {
                    if (intent.getAction().equals(Constants.START_FOREGROUND_ACTION)) {
                        Log.i(TAG, "onStartCommand Service started");

                        // request for location updates
                        if (locationUpdatesComponent != null)
                            locationUpdatesComponent.onStart();

                        /*Bundle extras = intent.getExtras();
                        messageHandler = (Messenger) extras.get("MESSENGER");*/

                    } else if (intent.getAction().equals(Constants.STOP_FOREGROUND_ACTION)) {
                        Log.i(TAG, "onStartCommand Service stopped");
                        //your end service code
                        stopForeground(true);
                        stopSelf();
                    }
                }
            }
        } catch (Exception e) {}

        return START_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy....");

        if (locationUpdatesComponent != null)
            locationUpdatesComponent.onStop();
    }

    /**
     * send message by using messenger
     *
     */
    private void sendMessage(Location location) {
        // If this service is launched by the JobScheduler, there's no callback Messenger. It
        // only exists when the MainActivity calls startService() with the callback in the Intent.

        try {
            if (location != null) {

                //Logger.getInstance(this).log(TAG, "sendMessage called");
                if (location != null) {
                    //Toast.makeText(getApplicationContext(), location.getLatitude() + ", " + location.getLongitude()+","+location.getAccuracy(), Toast.LENGTH_SHORT).show();
                    //Logger.getInstance(this).log(TAG, "sendMessage " + location.getLatitude() + ", " + location.getLongitude());

                    // The string "my-location" will be used to filer the intent
                    Intent intent = new Intent("my-location");
                    // Adding some data
                    intent.putExtra("latitude", "" + location.getLatitude());
                    intent.putExtra("longitude", "" + location.getLongitude());
                    intent.putExtra("accuracy", "" + location.getAccuracy());
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    /*Message message = Message.obtain();
                    try {
                        Intent intent = new Intent("my-location");
                        intent.putExtra("latitude", "" + location.getLatitude());
                        intent.putExtra("longitude", "" + location.getLongitude());
                        message.setData(intent.getExtras());
                        messageHandler.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        } catch (Exception e) {
            caughtException(e);
        }
    }

    @Override
    public void onLocationUpdate(Location location) {
        sendMessage(location);
    }

    public void caughtException(Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        //Logger.getInstance(this).log(TAG, "Exception" + stackTrace);
    }
}