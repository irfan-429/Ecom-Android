package com.maq.ecom.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.maq.ecom.R;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        super.toolbar.setTitle("Search");
    }
}