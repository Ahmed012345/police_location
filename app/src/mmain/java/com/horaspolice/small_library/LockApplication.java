package com.horaspolice.small_library;

import android.app.Application;
import android.content.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.horaspolice.easysharedpreferences.EasySharedPreferenceConfig;



public class LockApplication extends Application {


   public boolean lockScreenShow=false;
    public int notificationId=1989;


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        EasySharedPreferenceConfig.Companion.initDefault(new EasySharedPreferenceConfig.Builder()
                .inputFileName("easy_preference")
                .inputMode(Context.MODE_PRIVATE)
                .build());
    }


}
