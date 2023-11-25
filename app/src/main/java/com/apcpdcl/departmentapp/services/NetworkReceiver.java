package com.apcpdcl.departmentapp.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Admin on 15-12-2017.
 */

public class NetworkReceiver  {

    private boolean NetworkState = false;

    public boolean hasInternetConnection(final Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                int netType = networkInfo.getType();
                NetworkState = (netType == ConnectivityManager.TYPE_WIFI || netType == ConnectivityManager.TYPE_MOBILE) && networkInfo.isConnected();
            } else {
                NetworkState = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return NetworkState;
    }

}
