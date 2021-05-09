package com.horaspolice.locker_camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.horaspolice.R;
import com.horaspolice.easylocationutility.EasyLocationUtility;
import com.horaspolice.locker_camera.utils.DateUtils;
import com.horaspolice.locker_camera.utils.ViewUtils;
import com.horaspolice.nointernetlocker.NoInternetDialog;
import com.horaspolice.nointernetstart.NetworkChecker;
import com.horaspolice.small_library.LockApplication;
import com.horaspolice.widget.TouchToUnLockView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class LockScreenActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static File mapFile;
    private Calendar calendar = GregorianCalendar.getInstance();
    private SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private SimpleDateFormat monthFormat = new SimpleDateFormat("MMM d", Locale.getDefault());

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 200;
    private GoogleMap mMap;
    public static EasyLocationUtility locationUtility;
    private NoInternetDialog noInternetDialog;
    private TextView mlat, mlang, time, data;
    private TouchToUnLockView mUnlockView;
    private View mContainerView;
    private ImageView e3lanat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noInternetDialog = new NoInternetDialog.Builder(this)
                .setButtonColor(getResources().getColor(R.color.green))
                .build();
        locationUtility = new EasyLocationUtility(this);

        if (NetworkChecker.isNetworkConnected(LockScreenActivity.this)) {
            locationUtility.requestPermission(EasyLocationUtility.RequestCodes.CURRENT_LOCATION_UPDATES);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            fetchLastlocation();
        }


        initView();
        updateTimeUI();
    }




    private void fetchLastlocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
            return;
        }


        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            SupportMapFragment supportMapFragment;

            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    //  Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "," + currentLocation.getLongitude(), Toast.LENGTH_LONG).show();

                    mlat.setText(String.valueOf(currentLocation.getLatitude()));
                    mlang.setText(String.valueOf(currentLocation.getLongitude()));
                }
                supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(LockScreenActivity.this);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateTimeUI() {
        time.setText(DateUtils.getHourString(this, System.currentTimeMillis()));
        data.setText(weekFormat.format(calendar.getTime()) + "    " + monthFormat.format(calendar.getTime()));
    }

    private void initView() {
        // Set everything up...
        mlat = ViewUtils.get(this, R.id.lating);
        mlang = ViewUtils.get(this, R.id.longing);
        time = ViewUtils.get(this, R.id.txtv_LockTime);
        data = ViewUtils.get(this, R.id.txtv_LockDate);
        mContainerView = ViewUtils.get(this, R.id.relel_ContentContainer);
        mUnlockView = ViewUtils.get(this, R.id.tulv_UnlockView);
        e3lanat = ViewUtils.get(this, R.id.e3lan);

        mUnlockView.setOnTouchToUnlockListener(new TouchToUnLockView.OnTouchToUnlockListener() {
            @Override
            public void onTouchLockArea() {
                //Take picture using the camera without preview.
                // Permission not granted, ask for it
                if (NetworkChecker.isNetworkConnected(LockScreenActivity.this)) {
                    locationUtility.checkDeviceSettings(EasyLocationUtility.RequestCodes.LAST_KNOWN_LOCATION);
                }
                if (mContainerView != null) {
                    mContainerView.setBackgroundColor(Color.parseColor("#66000000"));
                }
            }

            @Override
            public void onSlidePercent(float percent) {
                if (mContainerView != null) {
                    mContainerView.setAlpha(Math.max(1 - percent, 0.05f));
                    mContainerView.setScaleX(1 + (Math.min(percent, 1f)) * 0.08f);
                    mContainerView.setScaleY(1 + (Math.min(percent, 1f)) * 0.08f);
                }
            }

            @Override
            public void onSlideToUnlock() {
                finish();
            }

            @Override
            public void onSlideAbort() {
                if (mContainerView != null) {
                    mContainerView.setAlpha(1.0f);
                    mContainerView.setBackgroundColor(0);
                    mContainerView.setScaleX(1f);
                    mContainerView.setScaleY(1f);
                }
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onAttachedToWindow();
    }

    @Override
    public void onResume() {
        super.onResume();
            locationUtility.resumeLocationUpdates();
        mUnlockView.startAnim();
        ((LockApplication) getApplication()).lockScreenShow = true;
    }





    @Override
    public void onPause() {
        super.onPause();
        if (currentLocation != null){
            locationUtility.stopLocationUpdates();
        }
        mUnlockView.stopAnim();
        ((LockApplication) getApplication()).lockScreenShow = false;
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            googleMap.addMarker(markerOptions);
            googleMap.addCircle(new CircleOptions()
                    .strokeWidth(3)
                    .strokeColor(Color.RED)
                    .radius(70)
                    .fillColor(0x11D81B60)
                    .center(latLng) );
        }


        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            final String mPath = getApplicationContext().getExternalCacheDir().getAbsolutePath()
                    + File.separator
                    + "MAP_" + System.currentTimeMillis()
                    + ".jpeg";

            @Override
            public void onMapLoaded() {
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                e3lanat.setVisibility(View.GONE);
                try {
                    // image naming and path  to include sd card  appending name you choose for file
                    // create bitmap screen capture
                    View v1 = getWindow().getDecorView().getRootView();
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                    v1.setDrawingCacheEnabled(false);

                    mapFile = new File(mPath);

                    FileOutputStream outputStream = new FileOutputStream(mapFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();

                    // Toast.makeText(getApplicationContext(), mPath, Toast.LENGTH_SHORT).show();
                    e3lanat.setVisibility(View.VISIBLE);
                } catch (Throwable e) {
                    // Several error may come out with file handling or DOM
                    e.printStackTrace();
                    //  Toast.makeText(getApplicationContext(), "Not Save", Toast.LENGTH_SHORT).show();
                }
                if (mMap != null) {
                        startService(new Intent(LockScreenActivity.this, DemoCamService.class));
                }
            }
        });

        mMap.setMyLocationEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastlocation();
                }
                break;
        }
    }

}
