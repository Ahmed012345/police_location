package com.horaspolice.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroCustomLayoutFragment.Companion.newInstance
import com.github.appintro.AppIntroPageTransformerType
import com.horaspolice.R
import com.horaspolice.small_library.FontManger
import com.marcoscg.materialtoast.MaterialToast

class PermissionsIntro2 : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FontManger.getInstance(applicationContext.assets)

        setTransformer(AppIntroPageTransformerType.Fade)
        isVibrate = true
        vibrateDuration = 50L
        addSlide(newInstance(R.layout.intro))
        addSlide(newInstance(R.layout.permissions1))
        addSlide(newInstance(R.layout.permissions2))
        addSlide(newInstance(R.layout.permissions3))
        askForPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            2, true
        )

        // Request optional storage permission
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    //        askForPermissions(
    //            arrayOf(
    //                Manifest.permission.ACCESS_COARSE_LOCATION,
    //                Manifest.permission.ACCESS_FINE_LOCATION,
    //                Manifest.permission.ACCESS_BACKGROUND_LOCATION
    //            ), 3, true
    //        )
    //    }
        askForPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            3, true
        )
        askForPermissions(arrayOf(Manifest.permission.CAMERA), 4, true)

    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        MaterialToast(this)
            .setMessage(R.string.no_step_out)
            .setIcon(R.mipmap.icicpolice)
            .setDuration(Toast.LENGTH_LONG)
            .show()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
                startActivity(Intent(this, StartActivity::class.java))
        }

}


