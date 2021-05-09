package com.horaspolice.nointernetlocker;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.horaspolice.R;
import com.horaspolice.easylocationutility.EasyLocationUtility;
import com.horaspolice.easylocationutility.LocationRequestCallback;
import com.marcoscg.materialtoast.MaterialToast;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.horaspolice.locker_camera.LockScreenActivity.locationUtility;

public class NoInternetUtils {
    public static boolean isConnectedToInternet(Context context) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI ) &&
                    lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)&&
                            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        result = true;
                    }else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)&&
                            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))  {
                        result = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI &&
                            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))  {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE&&
                            lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                            lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))  {
                        result = true;
                    }
                }
            }
        }
        return result;
    }


    static boolean hasActiveInternetConnection(Context context) {
        try {
            HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(1500);
            urlc.connect();
            return urlc.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiInfo.isConnected();
    }

    public static boolean isConnectedToMobileNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileInfo.isConnected();
    }


    static void turnOn3g(Context context) {
        new MaterialToast(context)
                .setMessage(R.string.no_internet_body)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();
    }



    static void turnOnWifi(Context context) {
        WifiUtils.withContext(context).enableWifi();
        new MaterialToast(context)
                .setMessage(R.string.turn_on_wifi)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();
    }

    static void turnOnLocation(Context context){
        // OPTIONAL: Use the setLocationRequestParams() method to change the default location
        //           request values. Here we're setting up the location request with a target update
        //           interval of 10 seconds and a fastest update interval cap of 5 seconds using the
        //           high accuracy power priority.
        locationUtility.setLocationRequestParams(10000, 5000, LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Check the permissions
        if (locationUtility.permissionIsGranted()){

            // Check device settings
            locationUtility.checkDeviceSettings(EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES);

            // Request location updates
            locationUtility.getCurrentLocationUpdates(new LocationRequestCallback() {
                @Override
                public void onLocationResult(Location location) {




                }
                @Override
                public void onFailedRequest(String result) {



                }
            });

        } else {

            // Permission not granted, ask for it
            locationUtility.requestPermission(EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES);

        }

        new MaterialToast(context)
                .setMessage(R.string.please_turn_on_location)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();
    }

    static boolean isAirplaneModeOn(Context context) {

            return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;


    }

    static void turnOffAirplaneMode(Context context) {
        new MaterialToast(context)
                .setMessage(R.string.please_turn_off_Airplane)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();
    }


}
