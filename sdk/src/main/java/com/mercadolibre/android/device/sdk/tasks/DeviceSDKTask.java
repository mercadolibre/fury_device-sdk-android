package com.mercadolibre.android.device.sdk.tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.managers.DataCollectorManager;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * DeviceSDKTask allows to gather all the needed information of the device. It should always be invoked from
 * DeviceSDK to execute asynchronously deferred with the main thread.
 *
 */
@SuppressLint("MissingPermission")
public class DeviceSDKTask extends AsyncTask<String, Integer, String> {

    private static final int LOCATION_WAIT_TIME = 3;
    private static final int COUNTDOWN_LIMIT = 1;
    private static final String TAG = DeviceSDKTask.class.getSimpleName();

    private final Context contextRef;
    private final Device deviceRef;

    private final DataCollectorManager dataCollectorManager;

    /**
     * DeviceSDKTask constructors references context and device.
     *
     * @param context the context where we take data from
     * @param device   device to be completed
     */
    public DeviceSDKTask(Context context, Device device) {
        contextRef = context;
        deviceRef = device;
        dataCollectorManager = new DataCollectorManager();
    }

    /**
     * doInBackground redefines the execution method for this task.
     *
     * @param strings inputs
     * @return processing result
     */
    @Override
    protected String doInBackground(String... strings) {
        dataCollectorManager.stopListeners();
        dataCollectorManager.startCollectors(contextRef, deviceRef);
        waitForListeners();
        dataCollectorManager.stopListeners();
        return "";
    }

    /**
     * waitForListeners waits for listeners allowing them to refresh the location,
     * this wait doesn't block main thread.
     */
    private void waitForListeners() {
        CountDownLatch countdown = new CountDownLatch(COUNTDOWN_LIMIT);
        try {
            countdown.await(LOCATION_WAIT_TIME, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            Log.i(TAG, String.format("error when trying to wait for location updates: %s", e.getMessage()));
        }
    }

}