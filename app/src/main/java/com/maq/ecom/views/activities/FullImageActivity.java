package com.maq.ecom.views.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.maq.ecom.R;
import com.maq.ecom.helper.Utils;

public class FullImageActivity extends AppCompatActivity {


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setToFullScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);


        AppCompatImageView imageView= findViewById(R.id.expanded_imageView);
        Utils.loadImage(this, "uploads/receipt/"+ getIntent().getStringExtra("img"), imageView);
        imageView.setOnTouchListener(new ImageMatrixTouchHandler(this));
    }
}