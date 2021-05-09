package com.horaspolice.locker_camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraFocus;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.horaspolice.R;
import com.horaspolice.nointernetstart.NetworkChecker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import co.nedim.maildroidx.MaildroidX;
import co.nedim.maildroidx.MaildroidXType;

import static com.horaspolice.locker_camera.LockScreenActivity.mapFile;
import static com.horaspolice.locker_camera.SelfieCamera.editText;


public class DemoCamService extends HiddenCameraService {




    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        DemoCamService getService() {
            return DemoCamService.this;
        }
    }


    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                CameraConfig cameraConfig = new CameraConfig()
                        .getBuilder(this)
                        .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .setImageRotation(CameraRotation.ROTATION_270)
                        .setCameraFocus(CameraFocus.AUTO)
                        .build();
                startCamera(cameraConfig);
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        takePicture();
                    }
                }, 2000L);
            } else {

                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {

            //TODO Ask your parent activity for providing runtime permission
            Toast.makeText(this, "Camera permission not available", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }


    @Override
    public void onImageCapture(@Nullable File imageFile) {
       // Toast.makeText(this, "Captured image size is : " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            // Do something with the image...

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            // Instead of display on ImageView

        if (NetworkChecker.isNetworkConnected(DemoCamService.this)){
            // send here...
                     new MaildroidX.Builder()
                             .smtp("smtp.gmail.com")
                             .smtpUsername("policeroid@gmail.com")
                             .smtpPassword("czuymgruowgmoxdg")
                             .smtpAuthentication(true)
                             .port("465")
                             .type(MaildroidXType.PLAIN)
                             .to(editText.getText().toString())
                             .from("policeroid@gmail.com")
                             .subject("Your Device Location And How Try To Open It...")
                             .body("Police Android")
                             .attachments(Arrays.asList(imageFile.getAbsolutePath(), mapFile.getAbsolutePath()))
                             .onCompleteCallback(new MaildroidX.onCompleteCallback() {
                                 @Override
                                 public void onSuccess() {
                                  //  Toast.makeText(getApplicationContext(), "sent_successfully", Toast.LENGTH_SHORT).show();
                                 }

                                 @Override
                                 public void onFail(@NotNull String s) {
                                 //   Toast.makeText(getApplicationContext(), "sent_Fail", Toast.LENGTH_SHORT).show();
                                }

                                 @Override
                                 public long getTimeout() {
                                     return 3000;
                                 }
                             })
                             .mail();
                 } else {
                 Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
             }

        stopSelf();
    }


    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, R.string.error_cannot_open, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, R.string.error_cannot_write, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, R.string.error_cannot_get_permission, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, R.string.error_not_having_camera, Toast.LENGTH_LONG).show();
                break;
        }

        stopSelf();
    }

}
