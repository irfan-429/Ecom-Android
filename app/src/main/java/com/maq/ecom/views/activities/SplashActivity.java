package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.helper.Utils;

public class SplashActivity extends AppCompatActivity {

    Context context = this;
    SessionManager sessionManager = new SessionManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setToFullScreen(this);
        setContentView(R.layout.activity_splash);

//        sessionManager.setFirmId("1");
//        Utils.navigateTo(this, CategoryItemsAdminActivity.class); finish();
        delayScreen();
    }

    @SuppressLint("NewApi")
    private void checkPermissions() {
        if (Utils.checkPermissionsGranted(this, Utils.APP_PERMISSIONS))  //If granted
            delayScreen();
        else Utils.askForPermissions(this, Utils.APP_PERMISSIONS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Utils.checkPermissionsGranted(grantResults)) delayScreen();
        else checkPermissions();
    }

    private void delayScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                checkIsLoggedIn();
                Utils.navigateClearTo(context, MainActivity.class);
            }
        }, Utils.SPLASH_TIME_OUT);
    }

    private void checkIsLoggedIn() {
        if (sessionManager.getIsLoggedIn()) Utils.navigateClearTo(context, MainActivity.class);
        else Utils.navigateClearTo(context, LoginActivity.class);
    }

}