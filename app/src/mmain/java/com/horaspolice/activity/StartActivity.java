package com.horaspolice.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.androidhiddencamera.HiddenCameraUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.horaspolice.BuildConfig;
import com.horaspolice.R;
import com.horaspolice.easylocationutility.EasyLocationUtility;
import com.horaspolice.easylocationutility.LocationRequestCallback;
import com.horaspolice.locker_camera.SelfieCamera;
import com.horaspolice.nointernetstart.NetworkChecker;
import com.horaspolice.nointernetstart.NoInternetLayout;
import com.horaspolice.small_library.FontManger;
import com.horaspolice.widget.AppUpdateDialog;
import com.jaeger.library.StatusBarUtil;
import com.marcoscg.materialtoast.MaterialToast;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String FB_RC_KEY_TITLE="update_title";
    private static final String FB_RC_KEY_DESCRIPTION="update_description";
    private static final String FB_RC_KEY_FORCE_UPDATE_VERSION="force_update_version";
    private static final String FB_RC_KEY_LATEST_VERSION="latest_version";
    AppUpdateDialog appUpdateDialog;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseCrashlytics crashlytics;
    private static final int REQ_CODE_CAMERA_PERMISSION = 1253;
    private static final String TAG = StartActivity.class.getSimpleName();
    public static EasyLocationUtility locationUtility;
    Context context;
    MediaPlayer backb, pol, redioselfy, redioabout;
    boolean doubleBackToExitPressedOnce = false;
    private  AdView ad1, ad2, ad3, ad4,ad5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);
        new NoInternetLayout.Builder(this, R.layout.start_main);
        StatusBarUtil.setTransparent(this);
        FontManger.getInstance(getApplicationContext().getAssets());


         locationUtility = new EasyLocationUtility(this);
         logUser();
         checkAppUpdate();
         init();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                if (HiddenCameraUtils.canOverDrawOtherApps(context)) {
                    final int[] Hello_Boss = { R.string.Hello1, R.string.Hello2, R.string.Hello3, R.string.Hello4,
                            R.string.Hello5, R.string.Hello6, R.string.Hello7, R.string.Hello8, R.string.Hello9,
                            R.string.Hello10, R.string.Hello11, R.string.Hello12, R.string.Hello13, R.string.Hello14,
                            R.string.Hello15, R.string.Hello16, R.string.Hello17, R.string.Hello18, R.string.Hello19};
                    Random random = new Random();
                    int randomText = Hello_Boss[random.nextInt(Hello_Boss.length)];
                    new MaterialToast(context)
                            .setMessage(randomText)
                            .setIcon(R.mipmap.icicpolice)
                            .setDuration(Toast.LENGTH_LONG)
                            .show();
                }else {
                    //Open settings to grant permission for "Draw other apps".
                    HiddenCameraUtils.openDrawOverPermissionSetting(context);
                }
                new MaterialToast(context)
                        .setMessage(getResources().getString(R.string.Permission_Granted))
                        .setIcon(R.mipmap.icicpolice)
                        .setDuration(Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                new MaterialToast(context)
                        .setMessage(getResources().getString(R.string.Permission_Denied) + deniedPermissions.toString())
                        .setIcon(R.mipmap.icicpolice)
                        .setDuration(Toast.LENGTH_LONG)
                        .show();
            }
        };

     //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
     //       TedPermission.with(this)
     //               .setPermissionListener(permissionlistener)
     //               .setRationaleTitle(R.string.rationale_title)
     //               .setRationaleMessage(R.string.rationale_message)
     //               .setDeniedTitle(R.string.Permission_Denied)
     //               .setDeniedMessage(R.string.Denied_Message)
     //               .setGotoSettingButtonText(R.string.cam)
     //               .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
     //                       Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
     //                       Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.CAMERA)
     //               .check();
     //   }
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_message)
                .setDeniedTitle(R.string.Permission_Denied)
                .setDeniedMessage(R.string.Denied_Message)
                .setGotoSettingButtonText(R.string.cam)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA)
                .check();


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                REQ_CODE_CAMERA_PERMISSION);


        if (NetworkChecker.isNetworkConnected(StartActivity.this)) {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            ad1 = findViewById(R.id.adView1);
            AdRequest adRequest1 = new AdRequest.Builder().build();
            ad1.loadAd(adRequest1);

            ad2 = findViewById(R.id.adView2);
            AdRequest adRequest2 = new AdRequest.Builder().build();
            ad2.loadAd(adRequest2);

            ad3 = findViewById(R.id.adView3);
            AdRequest adRequest3 = new AdRequest.Builder().build();
            ad3.loadAd(adRequest3);

            ad4 = findViewById(R.id.adView4);
            AdRequest adRequest4 = new AdRequest.Builder().build();
            ad4.loadAd(adRequest4);

            ad5 = findViewById(R.id.adView5);
            AdRequest adRequest5 = new AdRequest.Builder().build();
            ad5.loadAd(adRequest5);
        }


    }


    private void checkAppUpdate() {
        final int versionCode = BuildConfig.VERSION_CODE;

        final HashMap<String, Object> defaultMap = new HashMap<>();
        defaultMap.put(FB_RC_KEY_TITLE, getResources().getString(R.string.dialog_title));
        defaultMap.put(FB_RC_KEY_DESCRIPTION, getResources().getString(R.string.dialog_message));
        defaultMap.put(FB_RC_KEY_FORCE_UPDATE_VERSION, ""+versionCode);
        defaultMap.put(FB_RC_KEY_LATEST_VERSION, ""+versionCode);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().build());

        mFirebaseRemoteConfig.setDefaultsAsync(defaultMap);

        Task<Void> fetchTask=mFirebaseRemoteConfig.fetch(BuildConfig.DEBUG?0: TimeUnit.HOURS.toSeconds(4));

        fetchTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    new MaterialToast(context)
                            .setMessage(getResources().getString(R.string.fetchs))
                            .setIcon(R.drawable.wifi)
                            .setDuration(Toast.LENGTH_LONG)
                            .setBackgroundColor(Color.parseColor("#55b859"))
                            .show();
                    mFirebaseRemoteConfig.fetchAndActivate();

                    String title=getValue(FB_RC_KEY_TITLE,defaultMap);
                    String description=getValue(FB_RC_KEY_DESCRIPTION,defaultMap);
                    int forceUpdateVersion= Integer.parseInt(getValue(FB_RC_KEY_FORCE_UPDATE_VERSION,defaultMap));
                    int latestAppVersion= Integer.parseInt(getValue(FB_RC_KEY_LATEST_VERSION,defaultMap));

                    boolean isCancelable=true;

                    if(latestAppVersion>versionCode)
                    {
                        if(forceUpdateVersion>versionCode)
                            isCancelable=false;

                        appUpdateDialog = new AppUpdateDialog(StartActivity.this, title, description, isCancelable);
                        appUpdateDialog.setCancelable(false);
                        appUpdateDialog.show();

                        Window window = appUpdateDialog.getWindow();
                        assert window != null;
                        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    }

                } else {

                    new MaterialToast(context)
                            .setMessage(getResources().getString(R.string.fetchs))
                            .setIcon(R.drawable.nowifi)
                            .setDuration(Toast.LENGTH_LONG)
                            .setBackgroundColor(Color.parseColor("#EE4035"))
                            .show();
                }
            }
        });
    }
    public String getValue(String parameterKey,HashMap<String, Object> defaultMap)
    {
        String value=mFirebaseRemoteConfig.getString(parameterKey);
        if(TextUtils.isEmpty(value))
            value= (String) defaultMap.get(parameterKey);

        return value;
    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCrashlyticsCollectionEnabled(true);
        crashlytics.checkForUnsentReports();
        crashlytics.sendUnsentReports();
        crashlytics.setUserId("id-99999");
        crashlytics.setCustomKey("Name", "Sapeh");
        crashlytics.setCustomKey("Email", "asmmya7@gmail.com");

    }

    public void getLastLocation(){

        // First, check the user has granted the required permission.
        if (locationUtility.permissionIsGranted()){

            // Permission is already granted. First, we'll check the required device location settings
            // are satisfied. If they are not the user will automatically be prompted to enable them.
            // The result of this can be checked and handled by implementing and overriding the
            // onActivityResult callback in your calling Activity. The request code we're passing in
            // can be tested for in onActivityResult to determine where the request originated.
            locationUtility.checkDeviceSettings(EasyLocationUtility.RequestCodes.LAST_KNOWN_LOCATION);

            // Now we can request the last known location from the device's cache.
            locationUtility.getLastKnownLocation(new LocationRequestCallback() {
                @Override
                public void onLocationResult(Location location) {

                    // Location result successfully received. This should never be null as a null
                    // result will be returned via the onFailedRequest callback method instead.


                }
                @Override
                public void onFailedRequest(String result) {

                    // Location request failed, output the error.
                    if (NetworkChecker.isNetworkConnected(StartActivity.this)){

                    }else {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }

                    }

            });

        } else {

            // Permission not granted, ask for it. You must implement and override the
            // onRequestPermissionsResult callback method in your calling Activity in order to handle
            // the result of the request.
            // Here we're passing in a request code that corresponds to the type of location request
            // we're attempting to make. We can test for the result of this specific request in the
            // onRequestPermissionResult implementation.
            locationUtility.requestPermission(EasyLocationUtility.RequestCodes.LAST_KNOWN_LOCATION);

        }

    }

    public void getLocationUpdates(){
        // OPTIONAL: Use the setLocationRequestParams() method to change the default location
        //           request values. Here we're setting up the location request with a target update
        //           interval of 10 seconds and a fastest update interval cap of 5 seconds using the
        //           high accuracy power priority.
        locationUtility.setLocationRequestParams(10000, 5000,
                LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Check the permissions
        if (locationUtility.permissionIsGranted()){

            // Check device settings
            locationUtility.checkDeviceSettings(EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES);

            // Request location updates
            locationUtility.getCurrentLocationUpdates(new LocationRequestCallback() {
                @Override
                public void onLocationResult(Location location) {

                    // Location result successfully returned


                }
                @Override
                public void onFailedRequest(String result) {

                    // Location request failed, output the error.
                    if (NetworkChecker.isNetworkConnected(StartActivity.this)){
                    }else {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                }
            });

        } else {

            // Permission not granted, ask for it
            locationUtility.requestPermission(EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES);

        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        // Check if the permission request has been granted and store the result in a boolean
        boolean requestGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        if (requestGranted) {
            // We've got permission, yay! Now we can carry on where we left off...

            // Query the incoming request code to determine which request we're responding to
            switch (requestCode) {

                case EasyLocationUtility.RequestCodes.LAST_KNOWN_LOCATION:
                    // Carry on...
                    getLastLocation();
                    break;

                case EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES:
                    // Carry on...
                    getLocationUpdates();
                    break;

                default:
                    break;

            }

        } else {

            // Permission denied, boo! Log our displeasure...
            Log.e(TAG, "You denied the permission request. You must hate us :(");

        }
        if (requestCode == REQ_CODE_CAMERA_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, R.string.error_camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {

            // The user has either denied or cancelled out of the request to change device
            // settings so you will need to handle for that here.
            Log.e(TAG, "Unable to proceed: required device settings not satisfied");

        } else {

            // Location settings have been enabled, query the incoming request code to determine
            // which request we're responding to and take the appropriate action.
            switch (requestCode){

                case EasyLocationUtility.RequestCodes.LAST_KNOWN_LOCATION:
                    // Carry on where we left off...
                    getLastLocation();
                    break;

                case EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES:
                    // Carry on where we left off...
                    getLocationUpdates();
                    break;

                default:
                    break;

            }

        }

    }

    private void init() {
        context = this;
        backb = MediaPlayer.create(this, R.raw.backb);
        redioabout = MediaPlayer.create(this, R.raw.radio3);
        redioselfy = MediaPlayer.create(this, R.raw.radio4);

    }

    private void stopPlaying() {
        if (pol != null) {
            pol.stop();
            pol.release();
            pol = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlaying();
        locationUtility.stopLocationUpdates();
    }
    @Override
    public void onResume() {
        super.onResume();
        stopPlaying();
        pol = MediaPlayer.create(this, R.raw.startpolice);
        pol.setAudioStreamType(AudioManager.STREAM_MUSIC);
        pol.setLooping(true);
        pol.start();
        locationUtility.resumeLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlaying();
    }
    @Override
    public void onBackPressed(){
        backb.start();
        stopPlaying();

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            backb.start();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        new MaterialToast(context)
                .setMessage(getResources().getString(R.string.out))
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

    public void seilfy(View view) {
        getLocationUpdates();
        new MaterialToast(context)
                .setMessage(R.string.menuselfy)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();
        stopPlaying();
        redioselfy.start();
        startActivity(new Intent(this, SelfieCamera.class));


    }

    public void about(View view) {
        getLastLocation();
        new MaterialToast(context)
                .setMessage(R.string.menuabout)
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();
        stopPlaying();
        redioabout.start();
        startActivity(new Intent(this, AboutActivity.class));

    }


}
