package com.horaspolice.nointernetstart

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.MediaPlayer
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.android.gms.location.LocationRequest
import com.horaspolice.R
import com.horaspolice.activity.InformationApp
import com.horaspolice.activity.StartActivity.locationUtility
import com.horaspolice.easylocationutility.EasyLocationUtility
import com.horaspolice.easylocationutility.LocationRequestCallback
import com.marcoscg.materialtoast.MaterialToast
import com.thanosfisherman.wifiutils.WifiUtils
import kotlinx.android.synthetic.main.no_internet_start.*


@SuppressLint("Registered")
class NoInternetLayout(private val activity: Activity, private val layoutResID: Int) {

    internal lateinit var sarenin: MediaPlayer
    internal lateinit var radioinfo: MediaPlayer
    internal lateinit var azrar: MediaPlayer


    private fun onRetryButtonClick() {
        activity.button_refresh.setOnClickListener {
            if (NetworkChecker.isNetworkConnected(activity.applicationContext)) {
                sarenin = MediaPlayer.create(activity, R.raw.sound1)
                sarenin.start()
                activity.setContentView(layoutResID)
            }else {
                sarenin = MediaPlayer.create(activity, R.raw.sound1)
                sarenin.start()
                MaterialToast(activity)
                    .setMessage(R.string.no_internet_title)
                    .setIcon(R.mipmap.icicpolice)
                    .setDuration(Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    private fun onInfoButtonClick() {
        activity.animation_view_info.setOnClickListener {
            radioinfo = MediaPlayer.create(activity, R.raw.radio1)
            radioinfo.start()
            MaterialToast(activity)
                .setMessage(R.string.infowork)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show()
            activity.startActivity(Intent(activity, InformationApp::class.java))
              }

        }

        //activity.startActivity(Intent(activity, InformationApp::class.java))





    private fun onWifiButtonClick() {
        activity.fab_wifi.setOnClickListener {
            azrar = MediaPlayer.create(activity, R.raw.azrard)
            azrar.start()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
                activity.startActivityForResult(panelIntent, 545)
            } else {
                // use previous solution, add appropriate permissions to AndroidManifest file (see answers above)
                WifiUtils.withContext(activity).enableWifi()
            }

            }

            MaterialToast(activity)
                .setMessage(R.string.turn_on_wifi)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show()


        }




    private fun onLocationButtonClick() {
        activity.fab_lock.setOnClickListener {
            // OPTIONAL: Use the setLocationRequestParams() method to change the default location
            //           request values. Here we're setting up the location request with a target update
            //           interval of 10 seconds and a fastest update interval cap of 5 seconds using the
            //           high accuracy power priority.
            azrar = MediaPlayer.create(activity, R.raw.azrard)
            azrar.start()
           locationUtility.setLocationRequestParams(
               10000,
               5000,
               LocationRequest.PRIORITY_HIGH_ACCURACY
           )
            // Check the permissions
            if (locationUtility.permissionIsGranted()) { // Check device settings
                locationUtility.checkDeviceSettings(EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES)
                // Request location updates
                locationUtility.getCurrentLocationUpdates(object : LocationRequestCallback {
                    override fun onLocationResult(location: Location) { // Location result successfully returned
                        // Increment the counter every time a location update is received
                    }

                    override fun onFailedRequest(result: String) { // Location request failed, output the error.


                    }
                })
            } else { // Permission not granted, ask for it
                locationUtility.requestPermission(EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES)
            }
            MaterialToast(activity)
                .setMessage(R.string.please_turn_on_location)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show()
         //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           //     val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
           //     activity.startActivity(intent)
          //  }
        }
    }

    /**
     * @param activity .
     * @param layoutResID activity layout resource id
     */
    class Builder(private val activity: Activity, layoutResID: Int) {

        private var noInternetLayout: NoInternetLayout = NoInternetLayout(activity, layoutResID)

        init {
            if (NetworkChecker.isNetworkConnected(activity.applicationContext)) {
                activity.setContentView(layoutResID)
            } else {
                activity.setContentView(R.layout.no_internet_start)

                noInternetLayout.onRetryButtonClick()
                noInternetLayout.onWifiButtonClick()
                noInternetLayout.onLocationButtonClick()
                noInternetLayout.onInfoButtonClick()

            }
        }

    }

}


