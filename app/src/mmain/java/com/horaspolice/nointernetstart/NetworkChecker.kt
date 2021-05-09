package com.horaspolice.nointernetstart

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object NetworkChecker {


    @JvmStatic
    @Suppress("DEPRECATION")
    fun isNetworkConnected(context: Context): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
                                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
                                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)  -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) &&
                                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)  -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI  &&
                      locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE &&
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                        locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        result = true
                    }
                }
            }
        }
        return result
    }

}