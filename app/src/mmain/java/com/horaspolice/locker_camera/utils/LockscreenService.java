package com.horaspolice.locker_camera.utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import com.horaspolice.locker_camera.LockScreenActivity;

import java.util.Objects;

public class LockscreenService extends Service {

    private final String TAG = "LockscreenService";
    private int mServiceStartId = 0;
    private Context mContext = null;
    private boolean mReceiverTag = false;

  BootDeviceReceiver  mLockscreenReceiver = new BootDeviceReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != context) {
                if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_SCREEN_OFF)) {
                    startLockscreenActivity();
                }

                if (Objects.requireNonNull(intent.getAction()).equals(Intent.ACTION_BOOT_COMPLETED)) {
                    startLockscreenActivity();
                }

                // startServiceByAlarm(context);
            }
        }
    };




        private void stateRecever ( boolean isStartRecever){
            if (isStartRecever) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_SCREEN_OFF);
                registerReceiver(mLockscreenReceiver, filter);
            }
            if (isStartRecever) {
                IntentFilter filter = new IntentFilter();
                filter.addAction(Intent.ACTION_BOOT_COMPLETED);
                registerReceiver(mLockscreenReceiver, filter);
            } else {
               if (null != mLockscreenReceiver) {
               //   unregisterReceiver(mLockscreenReceiver);
                  if (mReceiverTag) {   //判断广播是否注册
                      mReceiverTag = false;   //Tag值 赋值为false 表示该广播已被注销
                      unregisterReceiver(mLockscreenReceiver);  //注销广播
                   }
               }
            }
        }


        @Override
        public void onCreate () {
            super.onCreate();
            mContext = this;

        }


        @Override
        public int onStartCommand (Intent intent,int flags, int startId){
            mServiceStartId = startId;
            stateRecever(true);
            Intent bundleIntet = intent;
            if (null != bundleIntet) {
               // startLockscreenActivity();
                Log.d(TAG, TAG + " onStartCommand intent  existed");
            } else {
                Log.d(TAG, TAG + " onStartCommand intent NOT existed");
            }
            return super.onStartCommand(intent, flags, startId);
         //   return LockscreenService.START_STICKY;
        }

        @Override
        public IBinder onBind (Intent intent){
        //    return null;
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }


        @Override
        public void onDestroy () {
           stateRecever(false);
        }

        public void startLockscreenActivity () {
            Intent startLockscreenActIntent = new Intent(mContext, LockScreenActivity.class);
            startLockscreenActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startLockscreenActIntent);
        }




}
