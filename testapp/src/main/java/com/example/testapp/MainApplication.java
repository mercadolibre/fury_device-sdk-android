package com.example.testapp;

import android.app.Application;

import com.mercadolibre.android.device.sdk.DeviceSDK;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DeviceSDK.getInstance().execute(this);
    }

}
