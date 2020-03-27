package com.mercadolibre.android.device.sdk.managers;

import android.content.Context;

import com.mercadolibre.android.device.sdk.collectors.AdvertisingIdCollector;
import com.mercadolibre.android.device.sdk.collectors.DeviceDataCollector;
import com.mercadolibre.android.device.sdk.collectors.LocationCollector;
import com.mercadolibre.android.device.sdk.domain.Device;

import java.io.Serializable;

/**
 * DataCollectorManager is the class in charge of managing every listener and collector
 * associated with the data we want to retrieve.
 *
 */
@SuppressWarnings({
        "PMD.CallSuperLast",
        "PMD.NonThreadSafeSingleton",
        "PMD.AvoidCatchingGenericException"
})
public final class DataCollectorManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private LocationCollector locationCollector;
    /**
     * startCollectors registers every listener needed for
     * capture or generate the data we want to track.
     *
     * @param context the application context
     * @param device the device we're going to populate, generated in DeviceSDK instance
     */
    public void startCollectors(final Context context, final Device device) {
        if (context == null || device == null) {
            return;
        }
        DeviceDataCollector deviceDataCollector = new DeviceDataCollector();
        deviceDataCollector.collectData(context, device);

        AdvertisingIdCollector.getInstance().start(context, device);
        locationCollector = new LocationCollector();
        locationCollector.collectData(context, device);

    }

    /**
     * stopListeners is called when caller needs to cancel current listeners.
     */
    public void stopListeners() {
        if (locationCollector == null) {
            return;
        }
        locationCollector.stopLocationUpdates();
    }
}