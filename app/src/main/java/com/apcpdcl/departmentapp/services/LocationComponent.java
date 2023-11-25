package com.apcpdcl.departmentapp.services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * stand alone component for location updates
 */
public class LocationComponent {

    private static final String TAG = LocationComponent.class.getSimpleName();

    private int ACCURACY_DISTANCE = 200;
    public static final int LOCATION_UPDATE_INTERVAL = 2000;
    public static final int LOCATION_UPDATE_FASTEST_INTERVAL = 2000;
    /**
     * If accuracy is lesser than 20m , discard it
     */
    private int ACCURACY_THRESHOLD = 20;

    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    /**
     * The current location.
     */
    private Location mLocation;
    private Location oldLocation;
    private Location newLocation;

    /**
     * Total distance covered
     */
    private float totalDistance;
    private float currentDistance;

    public ILocationProvider iLocationProvider;

    private Context context = null;

    public LocationComponent(ILocationProvider iLocationProvider) {
        this.iLocationProvider = iLocationProvider;
    }

    /**
     * create first time to initialize the location components
     *
     * @param context
     */
    public void onCreate(Context context) {
        this.context = context;
        //Logger.getInstance(context).log(TAG, "START called");
        //Logger.getInstance(context).log(TAG, "onCreate called");

        oldLocation = new Location("Point A");
        newLocation = new Location("Point B");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mLocation = locationResult.getLastLocation();
                onNewLocation(mLocation);
            }
        };
        // create location request
        createLocationRequest();
        // get last known location
        getLastLocation();
    }

    /**
     * start location updates
     */
    public void onStart() {
        //Logger.getInstance(context).log(TAG, "onStart called");
        //hey request for location updates
        requestLocationUpdates();
    }

    /**
     * remove location updates
     */
    public void onStop() {
       // Logger.getInstance(context).log(TAG, "onStop called");
        removeLocationUpdates();
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        //Logger.getInstance(context).log(TAG, "requestLocationUpdates called");

        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.getMainLooper());
        } catch (SecurityException unlikely) {
            caughtException(unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        //Logger.getInstance(context).log(TAG, "removeLocationUpdates called");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            // LocationUtils.setRequestingLocationUpdates(this, false);
            // stopSelf();
        } catch (SecurityException unlikely) {
            // LocationUtils.setRequestingLocationUpdates(this, true);
            caughtException(unlikely);
        }
    }

    /**
     * get last location
     */
    private void getLastLocation() {
        //Logger.getInstance(context).log(TAG, "getLastLocation called");
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                                //Logger.getInstance(context).log(TAG, "getLastLocation" + mLocation.toString());
                                // Toast.makeText(context, "" + mLocation, Toast.LENGTH_SHORT).show();

                                onNewLocation(mLocation);
                            } else {
                                //Logger.getInstance(context).log(TAG, "getLastLocation Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            caughtException(unlikely);
        }
    }

    private void onNewLocation(Location location) {
        //Logger.getInstance(context).log(TAG, "onNewLocation called");

        float fDistance = getUpdatedDistance(location);
        //Logger.getInstance(context).log(TAG, "getUpdatedDistance " + fDistance);
        if (fDistance < ACCURACY_DISTANCE) {
            location = oldLocation;
        } else {
            requestLocationUpdates();
            return;
        }

        try {
            if (location != null) {
               // Logger.getInstance(context).log(TAG, "locationUpdated called");

                //Toast.makeText(context, location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            caughtException(e);
        }

        //mLocation = location;
        if (mLocation != null && this.iLocationProvider != null) {
            this.iLocationProvider.onLocationUpdate(mLocation);
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private float getUpdatedDistance(Location location) {
        //Logger.getInstance(context).log(TAG, "getUpdatedDistance called");

        /**
         * There is 68% chance that user is with in 100m from this location.
         * So neglect location updates with poor accuracy
         */

        try {
            //Logger.getInstance(context).log(TAG, "location accuracy " + location.getAccuracy());
            if (location.getAccuracy() > ACCURACY_THRESHOLD) {
                if (oldLocation.getLatitude() == 0 && oldLocation.getLongitude() == 0)
                    oldLocation = location;
                return currentDistance;
            }

            if (oldLocation.getLatitude() == 0 && oldLocation.getLongitude() == 0) {

                oldLocation.setLatitude(location.getLatitude());
                oldLocation.setLongitude(location.getLongitude());

                newLocation.setLatitude(location.getLatitude());
                newLocation.setLongitude(location.getLongitude());

                return currentDistance;
            } else {

                oldLocation.setLatitude(newLocation.getLatitude());
                oldLocation.setLongitude(newLocation.getLongitude());

                newLocation.setLatitude(location.getLatitude());
                newLocation.setLongitude(location.getLongitude());
            }

            /**
             * Calculate distance between last two geo locations
             */
            currentDistance = newLocation.distanceTo(oldLocation);
            totalDistance += newLocation.distanceTo(oldLocation);
            //Logger.getInstance(context).log(TAG, "distance " + currentDistance);
        } catch (Exception e) {
            caughtException(e);
        }
        return currentDistance;
    }

    private String getUpdatedAddress(Location location) {
        //Logger.getInstance(context).log(TAG, "getUpdatedAddress called");

        String errorMessage = "";
        String strAddress = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());

            // Address found using the Geocoder.
            List<Address> addresses = null;

            try {
                // Using getFromLocation() returns an array of Addresses for the area immediately
                // surrounding the given latitude and longitude. The results are a best guess and are
                // not guaranteed to be accurate.
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), // In this sample, we get just a single address.
                        1);
            } catch (IOException ioException) {
                // Catch network or other I/O problems.
                caughtException(ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Catch invalid latitude or longitude values.
                caughtException(illegalArgumentException);
            }

            // Handle case where no address was found.
            if (addresses == null) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "no_address_found";
                }
            } else {
                if (addresses.size() == 0) {
                    errorMessage = "no_address_found";
                    return "";
                }

                Address address = addresses.get(0);
                ArrayList<String> addressFragments = new ArrayList<String>();

                // Fetch the address lines using {@code getAddressLine},
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
                strAddress = TextUtils.join(System.getProperty("line.separator"), addressFragments);
                return strAddress;
            }
        } catch (Exception e) {
            caughtException(e);
        }
        return "";
    }

    /**
     * implements this interface to get call back of location changes
     */
    public interface ILocationProvider {
        void onLocationUpdate(Location location);
    }

    public void caughtException(Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        //Logger.getInstance(context).log(TAG, "Exception" + stackTrace);
    }
}