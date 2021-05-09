package com.horaspolice.activity;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.horaspolice.R;
import com.horaspolice.materialabout.builder.AboutBuilder;
import com.horaspolice.materialabout.views.AboutView;
import com.horaspolice.small_library.FontManger;
import com.jaeger.library.StatusBarUtil;
import com.marcoscg.materialtoast.MaterialToast;

public class AboutActivity extends AppCompatActivity {

    private MediaPlayer backb;
    private InterstitialAd mInterstitialAd;
    ImageButton btnRec, ads;
    FrameLayout flHolder;
    Context context = this;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.e26));
        FontManger.getInstance(getApplicationContext().getAssets());

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9574398154137015/8645214304");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        backb = MediaPlayer.create(this, R.raw.backb);
        btnRec = findViewById(R.id.btn_rec);
        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backb.start();
                onBackPressed();

            }
        });

        ads = findViewById(R.id.ads);
        ads.setOnClickListener(new View.OnClickListener() {
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


        flHolder = findViewById(R.id.about);
        AboutBuilder builder = AboutBuilder.with(this)
                .setAppIcon(R.mipmap.icicpolice)
                .setAppName(R.string.app_name)
                .setPhoto(R.mipmap.profile_picture)
                .setCover(R.mipmap.profile_cover)
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setBackgroundColor(R.color.common_text_color)
                .setName(R.string.myname)
                .setSubTitle(R.string.Developer)
                .setLinksColumnsCount(4)
                .setBrief(R.string.makolaty)
                .addGitHubLink("Ahmed012345")
                .addFacebookLink("kraro0o0o")
                .addTwitterLink("A7md_c3d")
                //         .addInstagramLink("jnrvans")
                .addYoutubeChannelLink("UCYg6-xM8lEL6RAGwJ5BFG7A")
                //         .addLinkedInLink("arleu-cezar-vansuita-júnior-83769271")
                .addEmailLink("asmmya7@gmail.com")
                .addWhatsappLink("Ahmed", "+201002264423")
                //         .addSkypeLink("user")
                .addWebsiteLink("mybluesky.site")
                .addFiveStarsAction()
                .addMoreFromMeAction("A7md+S3d&hl=ar&gl=US")
                .addPrivacyPolicyAction("https://sites.google.com/view/poan")
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setActionsColumnsCount(2)
                .addFeedbackAction("asmmya7@gmail.com")
                .addIntroduceAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DemoUtils.showLicensesDialog(context);
                    }
                })
                .setWrapScrollView(true)
                .setShowAsCard(true);

        AboutView view = builder.build();

        flHolder.addView(view);
    }

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        // Toast for back pressed
        new MaterialToast(context)
                .setMessage(getResources().getString(R.string.finalback))
                .setIcon(R.drawable.ic_check_circle)
                .setDuration(Toast.LENGTH_LONG)
                .setBackgroundColor(Color.parseColor("#9E9E9E"))
                .show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
    }


}
