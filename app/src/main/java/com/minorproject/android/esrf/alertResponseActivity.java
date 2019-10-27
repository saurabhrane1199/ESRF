package com.minorproject.android.esrf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class alertResponseActivity extends AppCompatActivity {
    Intent intent;
    final String TAG = "Inside ALertResponse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_response);
        intent = getIntent();
        String username = intent.getStringExtra("user");
        //String lat = intent.getStringExtra("lat");
        //String lon = intent.getStringExtra("long");
        //String location = intent.getStringExtra("location");
        Log.d(TAG,username);
        //Log.d(TAG,lat);
        //Log.d(TAG,lon);
        //Log.d(TAG,location);*/
    }
}
