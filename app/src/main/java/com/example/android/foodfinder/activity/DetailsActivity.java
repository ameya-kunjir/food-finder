package com.example.android.foodfinder.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.foodfinder.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
