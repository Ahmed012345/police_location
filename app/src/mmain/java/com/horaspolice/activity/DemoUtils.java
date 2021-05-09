package com.horaspolice.activity;

import android.content.Context;

import com.horaspolice.R;
import com.marcoscg.licenser.Library;
import com.marcoscg.licenser.License;
import com.marcoscg.licenser.LicenserDialog;

public class DemoUtils {

    public static void showLicensesDialog(Context context) {
        new LicenserDialog(context, R.style.DialogStyle)
                .setTitle("Licenses")
                .setLibrary(new Library("Android Support Libraries",
                        "https://developer.android.com/topic/libraries/support-library/index.html",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Status Bar Util",
                        "https://github.com/laobie/StatusBarUtil",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Dynamic Sizes",
                        "https://github.com/MrNouri/DynamicSizes",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Lottie for Android",
                        "https://github.com/airbnb/lottie-android",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Material Toast",
                        "https://github.com/marcoscgdev/MaterialToast",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Boom Menu",
                        "https://github.com/Nightonke/BoomMenu",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Wifi Utils",
                        "https://github.com/ThanosFisherman/WifiUtils",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Ted Permission",
                        "https://github.com/ParkSangGwon/TedPermission",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Maildroid",
                        "https://github.com/nedimf/maildroid",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Android Hidden Camera",
                        "https://github.com/kevalpatel2106/android-hidden-camera",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Easy-SharedPreferences",
                        "https://github.com/AmanpreetYatin/Easy-SharedPreferences",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Material Text Field",
                        "https://github.com/florent37/MaterialTextField",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("IndicatorScrollView",
                        "https://github.com/skydoves/IndicatorScrollView",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("ActivityCircularReveal",
                        "https://github.com/tombayley/ActivityCircularReveal",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("TextInputView",
                        "https://github.com/Faltenreich/TextInputView",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("ExpansionPanel",
                        "https://github.com/florent37/ExpansionPanel",
                        License.Companion.getAPACHE2()))
                .setLibrary(new Library("Shareable",
                        "https://github.com/robertsimoes/Shareable",
                        License.Companion.getMIT()))
                .setLibrary(new Library("Easy About",
                        "https://github.com/marcoscgdev/EasyAbout",
                        License.Companion.getMIT()))
                .setLibrary(new Library("Licenser",
                        "https://github.com/marcoscgdev/Licenser",
                        License.Companion.getMIT()))
                .setLibrary(new Library("Click Shrink Effect",
                        "https://github.com/realpacific/click-shrink-effect",
                        License.Companion.getMIT()))
                .setLibrary(new Library("FloatingActionButton",
                        "https://github.com/makovkastar/FloatingActionButton",
                        License.Companion.getMIT()))
                .setLibrary(new Library("SwitchButton",
                        "https://github.com/zcweng/SwitchButton",
                        License.Companion.getMIT()))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

}