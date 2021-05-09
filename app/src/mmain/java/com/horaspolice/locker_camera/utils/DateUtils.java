package com.horaspolice.locker_camera.utils;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {
    private static SimpleDateFormat sHourFormat24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static SimpleDateFormat sHourFormat12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    public static String getHourString(Context context, long time) {
        String strTimeFormat = android.provider.Settings.System.getString(context.getContentResolver(),
                android.provider.Settings.System.TIME_12_24);
       if (("12").equals(strTimeFormat)) {
     //       try {
                return sHourFormat12.format(time);
     //       } catch (Exception ignored) {
           }
   //     }
  //      return sHourFormat24.format(time);
        return sHourFormat12.format(time);

    }

}
