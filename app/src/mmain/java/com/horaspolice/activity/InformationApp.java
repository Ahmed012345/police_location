package com.horaspolice.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.horaspolice.R;
import com.horaspolice.databinding.ActivityInformationsAppBinding;
import com.horaspolice.small_library.FontManger;
import com.jaeger.library.StatusBarUtil;
import com.skydoves.indicatorscrollview.IndicatorAnimation;
import com.skydoves.indicatorscrollview.IndicatorItem;


public class InformationApp extends AppCompatActivity {

    private MediaPlayer radio2;
    private InterstitialAd mInterstitialAd;
    private ActivityInformationsAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationsAppBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        FontManger.getInstance(getApplicationContext().getAssets());
        StatusBarUtil.setColor(this, getResources().getColor(R.color.funny222));
        init();
        listener();



        binding.btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        binding.indicatorScrollView.bindIndicatorView(binding.indicatorView);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9574398154137015/2027533744");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        binding.adbutton.setOnClickListener(new View.OnClickListener() {
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

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);

        AdRequest adRequest1 = new AdRequest.Builder().build();
        binding.adView1.loadAd(adRequest1);

        AdRequest adRequest2 = new AdRequest.Builder().build();
        binding.adView2.loadAd(adRequest2);

        AdRequest adRequest3 = new AdRequest.Builder().build();
        binding.adView3.loadAd(adRequest3);

        AdRequest adRequest4 = new AdRequest.Builder().build();
        binding.adView4.loadAd(adRequest4);

        AdRequest adRequest5 = new AdRequest.Builder().build();
        binding.adView5.loadAd(adRequest5);

        AdRequest adRequest6 = new AdRequest.Builder().build();
        binding.adView6.loadAd(adRequest6);

    }



    private void init() {

        radio2 = MediaPlayer.create(this, R.raw.radio2);

    }



    private void listener() {

        IndicatorItem myIndicatorItem1 =
                new IndicatorItem.Builder(binding.section1)
                .setItemColorResource(R.color.pinkDark)
                .setItemIconResource(R.drawable.ic_wifi)
                .setIndicatorAnimation(IndicatorAnimation.BOUNCE)
                .setItemCornerRadius(30f)
                .build();
        binding.indicatorView.addIndicatorItem(myIndicatorItem1);

        IndicatorItem  myIndicatorItem2  =
               new IndicatorItem.Builder(binding.section2)
                        .setItemColorResource(R.color.md_orange_100)
                        .setItemIconResource(R.drawable.placeholder)
                        .setIndicatorAnimation(IndicatorAnimation.BOUNCE)
                        //  .setItemDuration(400) // sets the expanding and collapsing duration.
                        .setItemCornerRadius(30f) // sets the corner radius of the indicator item.
                        // .setItemIconTopPadding(12) // sets the padding top value between the indicator items.
                        // .setExpandedSize(600) // customizes the fully expanded height size.
                        .build();
        binding.indicatorView.addIndicatorItem(myIndicatorItem2);

        IndicatorItem  myIndicatorItem3  =
              new  IndicatorItem.Builder(binding.section3)
                        .setItemColorResource(R.color.md_yellow_200)
                        .setItemIconResource(R.drawable.ic_gmail_lesson)
                        .setIndicatorAnimation(IndicatorAnimation.BOUNCE)
                        //  .setItemDuration(400) // sets the expanding and collapsing duration.
                        .setItemCornerRadius(30f) // sets the corner radius of the indicator item.
                        //  .setItemIconTopPadding(12) // sets the padding top value between the indicator items.
                        // .setExpandedSize(600) // customizes the fully expanded height size.
                        .build();
        binding.indicatorView.addIndicatorItem(myIndicatorItem3);

        IndicatorItem  myIndicatorItem4  =
              new IndicatorItem.Builder(binding.section4)
                        .setItemColorResource(R.color.md_green_200)
                        .setItemIconResource(R.drawable.ic_lock_lesson)
                        .setIndicatorAnimation(IndicatorAnimation.BOUNCE)
                        //  .setItemDuration(400) // sets the expanding and collapsing duration.
                        .setItemCornerRadius(30f) // sets the corner radius of the indicator item.
                        //  .setItemIconTopPadding(12) // sets the padding top value between the indicator items.
                        // .setExpandedSize(600) // customizes the fully expanded height size.
                        .build();
        binding.indicatorView.addIndicatorItem(myIndicatorItem4);

        IndicatorItem  myIndicatorItem5  =
               new IndicatorItem.Builder(binding.section5)
                        .setItemColorResource(R.color.md_blue_200)
                        .setItemIconResource(R.drawable.ic_setting_lesson)
                        .setIndicatorAnimation(IndicatorAnimation.BOUNCE)
                        //  .setItemDuration(400) // sets the expanding and collapsing duration.
                        .setItemCornerRadius(30f) // sets the corner radius of the indicator item.
                        // .setItemIconTopPadding(12) // sets the padding top value between the indicator items.
                        // .setExpandedSize(600) // customizes the fully expanded height size.
                        .build();
        binding.indicatorView.addIndicatorItem(myIndicatorItem5);

        IndicatorItem  myIndicatorItem6  =
               new IndicatorItem.Builder(binding.section6)
                        .setItemColorResource(R.color.md_purple_100)
                        .setItemIconResource(R.drawable.ic_description)
                        .setIndicatorAnimation(IndicatorAnimation.BOUNCE)
                        //  .setItemDuration(400) // sets the expanding and collapsing duration.
                        .setItemCornerRadius(30f) // sets the corner radius of the indicator item.
                        //  .setItemIconTopPadding(12) // sets the padding top value between the indicator items.
                        // .setExpandedSize(600) // customizes the fully expanded height size.
                        .build();
        binding.indicatorView.addIndicatorItem(myIndicatorItem6);
    }

    @Override
    public void onBackPressed() {
        radio2.start();
        super.onBackPressed();

    }

}
