package com.maq.ecom.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;

public class SuccessActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        MainActivity.mCartList.clear();//clr cart after order
    }


    public void actionContinueShopping(View view) {
        startActivity(new Intent(this, MainActivity.class));

    }

    public void actionViewOrders(View view) {
        startActivity(new Intent(this, MyOrdersActivity.class));
    }


    @Override
    public void onBackPressed() {
        Utils.navigateClearTo(this, MainActivity.class);
    }
}