package com.androidthings.thingsleddemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidthings.thingsleddemo.R;
import com.androidthings.thingsleddemo.helper.GPIOHelper;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "IOTThings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            GPIOHelper.getGPIOHelper().startSensingObstacles();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            GPIOHelper.getGPIOHelper().stopSensingObstacles();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
