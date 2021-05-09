package com.horaspolice.locker_camera.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootDeviceReceiver extends BroadcastReceiver {

    private static final String TAG_BOOT_BROADCAST_RECEIVER = "BOOT_BROADCAST_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (null != context) {
            if (LockScreen.getInstance().isActive()) {
                if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                    //startServiceDirectly(context);
                    startServiceByAlarm(context);
                }
            }
            if (LockScreen.getInstance().isActive()) {
                if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
                    //startServiceDirectly(context);
                    startServiceByAlarm(context);
                }
            }

            // startServiceByAlarm(context);
        }



    }

    /* Start RunAfterBootService service directly and invoke the service every 10 seconds. */
    private void startServiceDirectly(Context context)
    {
        try {
            while (true) {


                // This intent is used to start background service. The same service will be invoked for each invoke in the loop.
                Intent startServiceIntent = new Intent(context, LockscreenService.class);
                context.startService(startServiceIntent);

                // Current thread will sleep one second.
                Thread.sleep(10000);
            }
        }catch(InterruptedException ex)
        {
            Log.e(TAG_BOOT_BROADCAST_RECEIVER, ex.getMessage(), ex);
        }
    }

    /* Create an repeat Alarm that will invoke the background service for each execution time.
     * The interval time can be specified by your self.  */
    private void startServiceByAlarm(Context context)
    {
        // Get alarm manager.
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        // Create intent to invoke the background service.
        Intent intent = new Intent(context, LockscreenService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long startTime = System.currentTimeMillis();
        long intervalTime = 60*1000;

        // Create repeat alarm.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, intervalTime, pendingIntent);
    }
}