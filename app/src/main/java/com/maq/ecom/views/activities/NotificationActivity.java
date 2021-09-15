package com.maq.ecom.views.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonObject;
import com.maq.ecom.R;
import com.maq.ecom.database.SessionManager;
import com.maq.ecom.interfaces.ApiConfig;
import com.maq.ecom.networking.RetrofitClient;

import retrofit2.Call;

public class NotificationActivity extends AppCompatActivity {

    Context context = this;
    SessionManager sessionManager = new SessionManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (!sessionManager.getIsLoggedIn())
            startActivityForResult(new Intent(context, LoginActivity.class), 200);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode != Activity.RESULT_OK) {
                finish();
            }
        }
    }

}