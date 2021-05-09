package com.horaspolice.nointernetlocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class WifiReceiver extends BroadcastReceiver {
    private ConnectionListener connectionListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();

        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI  &&
                lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (connectionListener != null) {
                connectionListener.onWifiTurnedOn();
            }
        } else {
            if (connectionListener != null) {
                connectionListener.onWifiTurnedOff();
            }
        }
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }
}
