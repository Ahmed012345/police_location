package com.horaspolice.locker_camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.horaspolice.R;
import com.horaspolice.activity.InformationApp;
import com.horaspolice.easysharedpreferences.EasySharedPreference;
import com.horaspolice.locker_camera.utils.LockScreen;
import com.horaspolice.small_library.FontManger;
import com.horaspolice.small_library.Shareable;
import com.jaeger.library.StatusBarUtil;
import com.marcoscg.materialtoast.MaterialToast;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.OnBoomListenerAdapter;
import com.nightonke.boommenu.Util;
import com.suke.widget.SwitchButton;
import com.thanosfisherman.wifiutils.WifiUtils;

public class SelfieCamera extends AppCompatActivity {

    String message = "App Link";
    String url = "https://play.google.com/store/apps/details?id=com.horaspolice";
    final static String KEY_EMAIL = "KEY_EMAIL";
    final static String KEY_BUTTON_CAMERA = "KEY_BUTTON_CAMERA";
    final static String KEY_BUTTON_S_D = "KEY_BUTTON_S_D";
    public static EditText editText;
    Context context;
    SwitchButton frontcamera, save_3;
    LottieAnimationView view_info, edit_animation, selfy_animation, save_animation;
    ExpansionLayout expansionLayout1, expansionLayout2, expansionLayout3;
    BoomMenuButton bmb;
    private ImageButton back, adbutton;
    MediaPlayer b1, backb, radioinfo, switch_on, switch_off;
    private AdView mAdView, mAdView1;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);
        LockScreen.getInstance().init(this, true);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.default_action_color));
        FontManger.getInstance(getApplicationContext().getAssets());

        init();
        listener();
        loadopjects();
        editText.setTypeface(FontManger.english);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[] { filter });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView1 = findViewById(R.id.adView1);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView1.loadAd(adRequest1);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9574398154137015/5423837360");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        adbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }

    private void loadopjects() {
        String text = EasySharedPreference.Companion.getString(KEY_EMAIL, "");
        editText.setText(text);
        frontcamera.setChecked(EasySharedPreference.Companion.getBoolean(KEY_BUTTON_CAMERA, false));
        save_3.setChecked(EasySharedPreference.Companion.getBoolean(KEY_BUTTON_S_D,false));
    }

    private void listener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backb.start();
                new MaterialToast(context)
                        .setMessage(getResources().getString(R.string.startmenu))
                        .setIcon(R.mipmap.icicpolice)
                        .setDuration(Toast.LENGTH_LONG)
                        .show();
                onBackPressed();
            }
        });

        frontcamera.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (frontcamera.isChecked()) {
                    switch_on.start();
                    EasySharedPreference.Companion.putBoolean(KEY_BUTTON_CAMERA, true);
                    LockScreen.getInstance().active();
                } else {
                    switch_off.start();
                    EasySharedPreference.Companion.putBoolean(KEY_BUTTON_CAMERA, false);
                    LockScreen.getInstance().deactivate();
                }
            }
        });

        save_3.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (save_3.isChecked()) {
                    switch_on.start();
                    String text = editText.getText().toString();
                    EasySharedPreference.Companion.putString(KEY_EMAIL, text);
                    new MaterialToast(context)
                            .setMessage(R.string.save_words)
                            .setIcon(R.mipmap.icicpolice)
                            .setDuration(Toast.LENGTH_LONG)
                            .show();
                    if(TextUtils.isEmpty(text)) {
                        new MaterialToast(context)
                                .setMessage(R.string.mail_empty)
                                .setIcon(R.mipmap.icicpolice)
                                .setDuration(Toast.LENGTH_LONG)
                                .show();
                    }else {
                        new MaterialToast(context)
                                .setMessage(R.string.mail_notempty)
                                .setIcon(R.mipmap.icicpolice)
                                .setDuration(Toast.LENGTH_LONG)
                                .show();
                    }
                    EasySharedPreference.Companion.putBoolean(KEY_BUTTON_S_D, true);
                } else {
                    switch_off.start();

                    EasySharedPreference.Companion.putBoolean(KEY_BUTTON_S_D, false);
                }
            }
        });

        assert bmb != null;
        bmb.setShareLineLength(30);
        bmb.setShareLineWidth(4);
        bmb.setDotRadius(9);

        float w = Util.dp2px(80);
        float h = Util.dp2px(96);
        float h_0_5 = h / 2;
        float h_1_5 = h * 1.5f;

        float hm = bmb.getButtonHorizontalMargin();
        float vm = bmb.getButtonVerticalMargin();
        float vm_0_5 = vm / 2;
        float vm_1_5 = vm * 1.5f;

        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_1_5 - vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(      0, -h_1_5 - vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_1_5 - vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, -h_0_5 - vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(      0, -h_0_5 - vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, -h_0_5 - vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_0_5 + vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(      0, +h_0_5 + vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_0_5 + vm_0_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(-w - hm, +h_1_5 + vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(      0, +h_1_5 + vm_1_5));
        bmb.getCustomButtonPlacePositions().add(new PointF(+w + hm, +h_1_5 + vm_1_5));

        TextOutsideCircleButton.Builder facebook = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.facebook)
                .normalTextRes(R.string.sharefacebook)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.FACEBOOK)
                                .url(url)
                                .build();
                        shareInstance.share();

                    }
                });
        bmb.addBuilder(facebook);

        TextOutsideCircleButton.Builder twitter = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.twitter)
                .normalTextRes(R.string.sharetwitter)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.TWITTER)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(twitter);

        TextOutsideCircleButton.Builder google = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.gmail)
                .normalTextRes(R.string.sharegmail)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.GOOGLE)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(google);

        TextOutsideCircleButton.Builder whatsapp = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.whatsapp)
                .normalTextRes(R.string.sharewhatapp)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.WHATSAPP)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(whatsapp);

        TextOutsideCircleButton.Builder instagram = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.instagram)
                .normalTextRes(R.string.shareinstagram)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.INSTAGRAM)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(instagram);

        TextOutsideCircleButton.Builder tumblr = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.tumblr)
                .normalTextRes(R.string.sharetumblr)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.TUMBLR)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(tumblr);

        TextOutsideCircleButton.Builder linkedin = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.linkedin)
                .normalTextRes(R.string.sharelinkedin)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.LINKED_IN)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(linkedin);

        TextOutsideCircleButton.Builder mail = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.viber)
                .normalTextRes(R.string.shareviber)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.VIBER)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(mail);

        TextOutsideCircleButton.Builder snapchat = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.snapchat)
                .normalTextRes(R.string.sharesnapchat)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.SNAPCHAT)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(snapchat);

        TextOutsideCircleButton.Builder skype = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.skype)
                .normalTextRes(R.string.shareskype)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.SKYPE)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(skype);

        TextOutsideCircleButton.Builder telegram = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.telegram)
                .normalTextRes(R.string.sharetelegram)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.TELEGRAM)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(telegram);

        TextOutsideCircleButton.Builder reddit = new TextOutsideCircleButton.Builder()
                .normalImageRes(R.drawable.reddit)
                .normalTextRes(R.string.sharereddit)
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {

                        Shareable shareInstance = new Shareable.Builder(context)
                                .message(message)
                                .socialChannel(Shareable.Builder.REDDIT)
                                .url(url)
                                .build();
                        shareInstance.share();
                    }
                });
        bmb.addBuilder(reddit);



// Use OnBoomListenerAdapter to listen part of methods
        bmb.setOnBoomListener(new OnBoomListenerAdapter() {
            @Override
            public void onBoomWillShow() {
                super.onBoomWillShow();
                // logic here

            }
        });

        // Use OnBoomListener to listen all methods
        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                // If you have implement listeners for boom-buttons in builders,
                // then you shouldn't add any listener here for duplicate callbacks.
            }

            /**
             * When the background of boom-buttons is clicked.
             */
            @Override
            public void onBackgroundClick() {
                b1.start();

            }

            /**
             * When the BMB is going to hide its boom-buttons.
             */
            @Override
            public void onBoomWillHide() {
                b1.start();
            }

            /**
             * When the BMB finishes hide animations.
             */
            @Override
            public void onBoomDidHide() {

            }

            /**
             * When the BMB is going to show its boom-buttons.
             */
            @Override
            public void onBoomWillShow() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    ((Activity)context).startActivityForResult(panelIntent, 545);
                } else {
                    WifiUtils.withContext(getApplicationContext()).enableWifi(this::checkResult);
                }
                b1.start();
            }

            private void checkResult(boolean isSuccess) {
                if (isSuccess)
                    new MaterialToast(context)
                            .setMessage(getResources().getString(R.string.wifion))
                            .setIcon(R.drawable.ic_wifi)
                            .setDuration(Toast.LENGTH_LONG)
                            .setBackgroundColor(Color.parseColor("#55b859"))
                            .show();
                else
                    new MaterialToast(context)
                            .setMessage(getResources().getString(R.string.wifion))
                            .setIcon(R.drawable.nowifi)
                            .setDuration(Toast.LENGTH_LONG)
                            .setBackgroundColor(Color.parseColor("#EE4035"))
                            .show();
            }

            /**
             * When the BMB finished boom animations.
             */
            @Override
            public void onBoomDidShow() {

            }
        });

        expansionLayout1.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {
                if (expansionLayout1.isExpanded()) {
                    edit_animation.playAnimation();
                    selfy_animation.pauseAnimation();
                    save_animation.pauseAnimation();
                    view_info.pauseAnimation();
                } else {
                    view_info.playAnimation();
                }
            }
        });
        expansionLayout2.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {
                if (expansionLayout2.isExpanded()) {
                    edit_animation.pauseAnimation();
                    selfy_animation.playAnimation();
                    save_animation.pauseAnimation();
                    view_info.pauseAnimation();
                } else {
                    view_info.playAnimation();
                }
            }
        });
        expansionLayout3.addListener(new ExpansionLayout.Listener() {
            @Override
            public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {
                if (expansionLayout3.isExpanded()) {
                    edit_animation.pauseAnimation();
                    selfy_animation.pauseAnimation();
                    save_animation.playAnimation();
                    view_info.pauseAnimation();
                } else {
                    view_info.playAnimation();
                }
            }
        });
        view_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioinfo.start();
                new MaterialToast(context)
                        .setMessage(getResources().getString(R.string.infowork))
                        .setIcon(R.mipmap.icicpolice)
                        .setDuration(Toast.LENGTH_LONG)
                        .show();
                startActivity(new Intent(context, InformationApp.class));
            }
        });

    }

    private void init() {
        context = this;
        editText = findViewById(R.id.textView_edit);
        back = findViewById(R.id.btn_rec);
        frontcamera = findViewById(R.id.front_camera);
        save_3 = findViewById(R.id.save_3);
        bmb = findViewById(R.id.bmb);
        b1 = MediaPlayer.create(this, R.raw.aaaa);
        backb = MediaPlayer.create(this, R.raw.backb);
        radioinfo = MediaPlayer.create(this, R.raw.radio1);
        switch_on = MediaPlayer.create(this, R.raw.on);
        switch_off = MediaPlayer.create(this, R.raw.off);
        view_info = findViewById(R.id.view_info);
        edit_animation = findViewById(R.id.edit_animation);
        selfy_animation = findViewById(R.id.selfy_animation);
        save_animation = findViewById(R.id.save_animation);
        expansionLayout1 = findViewById(R.id.expansionLayout1);
        expansionLayout2 = findViewById(R.id.expansionLayout2);
        expansionLayout3 = findViewById(R.id.expansionLayout3);
        adbutton = findViewById(R.id.adbutton);
    }

    @Override
    public void onBackPressed(){
        backb.start();
        super.onBackPressed();
        new MaterialToast(context)
                .setMessage(getResources().getString(R.string.startmenu))
                .setIcon(R.mipmap.icicpolice)
                .setDuration(Toast.LENGTH_LONG)
                .show();

    }


}
