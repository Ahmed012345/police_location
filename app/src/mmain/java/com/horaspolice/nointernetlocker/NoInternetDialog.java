package com.horaspolice.nointernetlocker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.RenderMode;
import com.horaspolice.R;
import com.thanosfisherman.wifiutils.WifiUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by robertlevonyan on 11/20/17.
 */

public class NoInternetDialog extends Dialog implements View.OnClickListener, ConnectionListener, ConnectionCallback {

    public static final int GRADIENT_LINEAR = 0;
    public static final int GRADIENT_RADIAL = 1;
    public static final int GRADIENT_SWEEP = 2;
    public static final float NO_RADIUS = -1f;
    private static final float RADIUS = 12f;


    @Retention(RetentionPolicy.RUNTIME)
    @IntDef({ORIENTATION_TOP_BOTTOM,
            ORIENTATION_BOTTOM_TOP,
            ORIENTATION_RIGHT_LEFT,
            ORIENTATION_LEFT_RIGHT,
            ORIENTATION_BL_TR,
            ORIENTATION_TR_BL,
            ORIENTATION_BR_TL,
            ORIENTATION_TL_BR})
    @interface Orientation {
    }

    public static final int ORIENTATION_TOP_BOTTOM = 10;
    public static final int ORIENTATION_BOTTOM_TOP = 11;
    public static final int ORIENTATION_RIGHT_LEFT = 12;
    public static final int ORIENTATION_LEFT_RIGHT = 13;
    public static final int ORIENTATION_BL_TR = 14;
    public static final int ORIENTATION_TR_BL = 15;
    public static final int ORIENTATION_BR_TL = 16;
    public static final int ORIENTATION_TL_BR = 17;

    private RelativeLayout root;
    private ImageView close;
    private LottieAnimationView wifiLottie;
    private LottieAnimationView locationLottie;
    private LottieAnimationView airplanLottie;
    private TextView noInternet;
    private TextView noInternetBody;
    private TextView turnOn;
    private Button wifiOn;
    private Button mobileOn;
    private Button locationOn;
    private Button airplaneOff;

    private int bgGradientStart;
    private int bgGradientCenter;
    private int bgGradientEnd;
    private int bgGradientOrientation;
    private int bgGradientType;
    private float dialogRadius;
    private int buttonColor;
    private int buttonTextColor;
    private int buttonIconsColor;
    private boolean cancelable;

    private boolean isHalloween;
    private WifiReceiver wifiReceiver;
    private NetworkStatusReceiver networkStatusReceiver;
    private ConnectionCallback connectionCallback;
    MediaPlayer azrar, sarenin;

    private NoInternetDialog(@NonNull Context context, int bgGradientStart, int bgGradientCenter, int bgGradientEnd,
                             int bgGradientOrientation, int bgGradientType, float dialogRadius,
                             int buttonColor, int buttonTextColor, int buttonIconsColor, int wifiLoaderColor,
                             boolean cancelable) {
        super(context);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        this.bgGradientStart = bgGradientStart == 0
                ? (isHalloween
                ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradStartH)
                : ContextCompat.getColor(getContext(), R.color.colorNoInternetGradStart))
                : bgGradientStart;
        this.bgGradientCenter = bgGradientCenter == 0
                ? (isHalloween
                ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradCenterH)
                : ContextCompat.getColor(getContext(), R.color.colorNoInternetGradCenter))
                : bgGradientCenter;
        this.bgGradientEnd = bgGradientEnd == 0
                ? (isHalloween
                ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradEndH)
                : ContextCompat.getColor(getContext(), R.color.colorNoInternetGradEnd))
                : bgGradientEnd;
        this.bgGradientOrientation = bgGradientOrientation < 10 || bgGradientOrientation > 17 ? ORIENTATION_TOP_BOTTOM : bgGradientOrientation;
        this.bgGradientType = bgGradientType <= 0 || bgGradientType > 2 ? GRADIENT_LINEAR : bgGradientType;
        this.dialogRadius = dialogRadius == 0 ? RADIUS : dialogRadius;
        if (dialogRadius == NO_RADIUS) {
            this.dialogRadius = 0f;
        }


        this.buttonColor = buttonColor == 0
                ? (isHalloween
                ? ContextCompat.getColor(getContext(), R.color.colorNoInternetGradCenterH)
                : ContextCompat.getColor(getContext(), R.color.colorAccent))
                : buttonColor;
        this.buttonTextColor = buttonTextColor == 0
                ? ContextCompat.getColor(getContext(), R.color.colorWhite)
                : buttonTextColor;
        this.buttonIconsColor = buttonIconsColor == 0
                ? ContextCompat.getColor(getContext(), R.color.colorWhite)
                : buttonIconsColor;


        this.cancelable = cancelable;

        initReceivers(context);
    }

    private void initReceivers(Context context) {
        wifiReceiver = new WifiReceiver();
        context.registerReceiver(wifiReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        networkStatusReceiver = new NetworkStatusReceiver();
        context.registerReceiver(networkStatusReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        wifiReceiver.setConnectionListener(this);
        networkStatusReceiver.setConnectionCallback(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_no_internet);
        initMainWindow();
        initView();
        initBackground();
        initButtonStyle();
        initListeners();
        initAnimations();
        initClose();
    }

    private void initMainWindow() {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void initView() {
        root = findViewById(R.id.root);
        close = findViewById(R.id.close);
        wifiLottie = findViewById(R.id.wifi_lottie);
        locationLottie = findViewById(R.id.location_lottie);
        airplanLottie = findViewById(R.id.airplan_lottie);
        noInternet = findViewById(R.id.no_internet);
        noInternetBody = findViewById(R.id.no_internet_body);
        turnOn = findViewById(R.id.turn_on);
        wifiOn = findViewById(R.id.wifi_on);
        mobileOn = findViewById(R.id.mobile_on);
        locationOn = findViewById(R.id.location_on);
        airplaneOff = findViewById(R.id.airplane_off);
        azrar = MediaPlayer.create(getContext(), R.raw.azrard);
    }


    private void initBackground() {
        GradientDrawable.Orientation orientation = getOrientation();

        GradientDrawable drawable = new GradientDrawable(orientation, new int[]{bgGradientStart, bgGradientCenter, bgGradientEnd});
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(dialogRadius);

        switch (bgGradientType) {
            case GRADIENT_RADIAL:
                drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                break;
            case GRADIENT_SWEEP:
                drawable.setGradientType(GradientDrawable.SWEEP_GRADIENT);
                break;
            default:
                drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                break;
        }
        if (isHalloween) {
            drawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            drawable.setGradientRadius(getContext().getResources().getDimensionPixelSize(R.dimen.dialog_height) / 2);
        } else {
            drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        }
       root.setBackground(drawable);
    }

    private void initButtonStyle() {
        wifiOn.getBackground().mutate().setColorFilter(buttonColor, PorterDuff.Mode.SRC_IN);
        mobileOn.getBackground().mutate().setColorFilter(buttonColor, PorterDuff.Mode.SRC_IN);
        locationOn.getBackground().mutate().setColorFilter(buttonColor, PorterDuff.Mode.SRC_IN);
        airplaneOff.getBackground().mutate().setColorFilter(buttonColor, PorterDuff.Mode.SRC_IN);

        wifiOn.setTextColor(buttonTextColor);
        mobileOn.setTextColor(buttonTextColor);
        locationOn.setTextColor(buttonTextColor);
        airplaneOff.setTextColor(buttonTextColor);

        Drawable wifi = ContextCompat.getDrawable(getContext(), R.drawable.ic_wifi_white);
        Drawable location = ContextCompat.getDrawable(getContext(), R.drawable.ic_location_on_black_24dp);
        Drawable airplane = ContextCompat.getDrawable(getContext(), R.drawable.ic_airplane_off);

        wifi.mutate().setColorFilter(buttonIconsColor, PorterDuff.Mode.SRC_ATOP);
        location.mutate().setColorFilter(buttonIconsColor, PorterDuff.Mode.SRC_ATOP);
        airplane.mutate().setColorFilter(buttonIconsColor, PorterDuff.Mode.SRC_ATOP);

        wifiOn.setCompoundDrawablesRelativeWithIntrinsicBounds(wifi, null, null, null);
        locationOn.setCompoundDrawablesRelativeWithIntrinsicBounds(location, null, null, null);
        airplaneOff.setCompoundDrawablesRelativeWithIntrinsicBounds(airplane, null, null, null);
    }

    private GradientDrawable.Orientation getOrientation() {
        GradientDrawable.Orientation orientation;
        switch (bgGradientOrientation) {
            case ORIENTATION_BOTTOM_TOP:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case ORIENTATION_RIGHT_LEFT:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case ORIENTATION_LEFT_RIGHT:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case ORIENTATION_BL_TR:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case ORIENTATION_TR_BL:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            case ORIENTATION_BR_TL:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
            case ORIENTATION_TL_BR:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
            default:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
        }

        return orientation;
    }

    private void initListeners() {
        close.setOnClickListener(this);
        wifiOn.setOnClickListener(this);
        mobileOn.setOnClickListener(this);
        locationOn.setOnClickListener(this);
        airplaneOff.setOnClickListener(this);
    }


    private void initAnimations() {

        // Lottie Animation For WIFI And Location .....

        wifiLottie.setAnimation("wifi.json");
        wifiLottie.setRenderMode(RenderMode.SOFTWARE);
        wifiLottie.loop(true);
        wifiLottie.playAnimation();

        locationLottie.setAnimation("location.json");
        locationLottie.setRenderMode(RenderMode.SOFTWARE);
        locationLottie.loop(true);
        locationLottie.playAnimation();
        startFlight();
    }

    private void startFlight() {
        // Lottie Animation For plan .....

        if (!NoInternetUtils.isAirplaneModeOn(getContext())) {
            airplanLottie.setVisibility(View.GONE);
            return;
        }

        wifiLottie.setVisibility(View.INVISIBLE);
        locationLottie.setVisibility(View.INVISIBLE);
        airplanLottie.setVisibility(View.VISIBLE);
        noInternetBody.setText(R.string.airplane_on);
        turnOn.setText(R.string.turn_off);
        airplaneOff.setVisibility(View.VISIBLE);
        wifiOn.setVisibility(View.INVISIBLE);
        mobileOn.setVisibility(View.INVISIBLE);
        locationOn.setVisibility(View.INVISIBLE);

        // plan Animation From Lottie.....
        airplanLottie.setAnimation("plan.json");
        airplanLottie.setRenderMode(RenderMode.SOFTWARE);
        airplanLottie.loop(true);
        airplanLottie.playAnimation();

    }


    private void initClose() {
        setCancelable(cancelable);
        close.setVisibility(cancelable ? View.VISIBLE : View.GONE);
    }




    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.close) {
            dismiss();
        } else if (id == R.id.wifi_on) {
            azrar.start();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                dismiss();
            }
            NoInternetUtils.turnOnWifi(getContext());
        } else if (id == R.id.location_on) {
            azrar.start();
            NoInternetUtils.turnOnLocation(getContext());
        } else if (id == R.id.mobile_on) {
            azrar.start();
            dismiss();
            NoInternetUtils.turnOn3g(getContext());
             dismiss();
        } else if (id == R.id.airplane_off) {
            azrar.start();
                dismiss();
            NoInternetUtils.turnOffAirplaneMode(getContext());
        }
    }



    @Override
    public void onWifiTurnedOn() {
    }

   @Override
    public void onWifiTurnedOff() {
        show();
    }

    @Override
    public void hasActiveConnection(boolean hasActiveConnection) {
        if(this.connectionCallback != null)
            this.connectionCallback.hasActiveConnection(hasActiveConnection);
        if (!hasActiveConnection) {
            showDialog();
        } else {
            dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
        startFlight();
    }

    public void showDialog() {
        Ping ping = new Ping();
        ping.setConnectionCallback(new ConnectionCallback() {
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                if (!hasActiveConnection) {
                    show();
                }
            }
        });
        ping.execute(getContext());
    }

    @Override
    public void dismiss() {
        reset();
        super.dismiss();
    }

    private void reset() {
        if (airplaneOff != null) {
            airplaneOff.setVisibility(View.GONE);
        }
        if (wifiOn != null) {
            wifiOn.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams wifiParams = (RelativeLayout.LayoutParams) wifiOn.getLayoutParams();
            wifiParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.button_width);
            wifiOn.setLayoutParams(wifiParams);
            wifiOn.setTextSize(13f);
            wifiOn.setTranslationX(1f);
            wifiOn.setTranslationY(1f);
        }
        if (mobileOn != null) {
            mobileOn.setVisibility(View.VISIBLE);
        }
        if (airplaneOff != null) {
            airplaneOff.setVisibility(View.VISIBLE);
        }
        if (wifiOn != null) {
            wifiOn.setText(R.string.turn_on);
        }


    }

    public void onDestroy() {
        try {
            getContext().unregisterReceiver(networkStatusReceiver);
        }catch(Exception e){ }
        try {
            getContext().unregisterReceiver(wifiReceiver);
        }catch (Exception e){}
    }

    public void setConnectionCallback(ConnectionCallback connectionCallback) {
        this.connectionCallback = connectionCallback;
    }

    public static class Builder {
        private Context context;
        private int bgGradientStart;
        private int bgGradientCenter;
        private int bgGradientEnd;
        private int bgGradientOrientation;
        private int bgGradientType;
        private float dialogRadius;
        private int buttonColor;
        private int buttonTextColor;
        private int buttonIconsColor;
        private int wifiLoaderColor;
        private ConnectionCallback connectionCallback;
        private boolean cancelable;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Fragment fragment) {
            this.context = fragment.getContext();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public Builder(android.app.Fragment fragment) {
            this.context = fragment.getContext();
        }

        public Builder setBgGradientStart(@ColorInt int bgGradientStart) {
            this.bgGradientStart = bgGradientStart;
            return this;
        }

        public Builder setBgGradientCenter(@ColorInt int bgGradientCenter) {
            this.bgGradientCenter = bgGradientCenter;
            return this;
        }

        public Builder setBgGradientEnd(@ColorInt int bgGradientEnd) {
            this.bgGradientEnd = bgGradientEnd;
            return this;
        }

        public Builder setBgGradientOrientation(@Orientation int bgGradientOrientation) {
            this.bgGradientOrientation = bgGradientOrientation;
            return this;
        }

        public Builder setBgGradientType(int bgGradientType) {
            this.bgGradientType = bgGradientType;
            return this;
        }

        public Builder setDialogRadius(float dialogRadius) {
            this.dialogRadius = dialogRadius;
            return this;
        }

        public Builder setDialogRadius(@DimenRes int dialogRadiusDimen) {
            this.dialogRadius = context.getResources().getDimensionPixelSize(dialogRadiusDimen);
            return this;
        }

        public Builder setButtonColor(int buttonColor) {
            this.buttonColor = buttonColor;
            return this;
        }

        public Builder setButtonTextColor(int buttonTextColor) {
            this.buttonTextColor = buttonTextColor;
            return this;
        }

        public Builder setButtonIconsColor(int buttonIconsColor) {
            this.buttonIconsColor = buttonIconsColor;
            return this;
        }

        public Builder setWifiLoaderColor(int wifiLoaderColor) {
            this.wifiLoaderColor = wifiLoaderColor;
            return this;
        }

        public Builder setConnectionCallback(ConnectionCallback callback) {
            this.connectionCallback = callback;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public NoInternetDialog build() {
            NoInternetDialog dialog = new NoInternetDialog(context, bgGradientStart, bgGradientCenter, bgGradientEnd,
                    bgGradientOrientation, bgGradientType, dialogRadius, buttonColor, buttonTextColor, buttonIconsColor,
                    wifiLoaderColor, cancelable);
            dialog.setConnectionCallback(connectionCallback);

            return dialog;
        }
    }
}
