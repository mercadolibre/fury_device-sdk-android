package com.mercadolibre.android.device.sdk;

import android.content.Context;

import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.tasks.DeviceSDKTask;
import java.util.Map;

/**
 * DeviceSDK is the starting point for the integration of Device Profiling.
 * This class allows instantiating a new SDK and initiating all modules
 * responsible for collecting information related to the device. None
 * of the modules initiated through DeviceSDK generates any type of
 * friction with the user or blocking operations.
 * <p>
 * Device Profiling tracking is executed asynchronously without
 * locking the corresponding context execution flow.
 *
 */
@SuppressWarnings({
        "unused",
        "PMD.SingularField",
        "PMD.CommentDefaultAccessModifier"
})
public final class DeviceSDK {

    private static final DeviceSDK instance = new DeviceSDK();

    private final Device device;

    /**
     * DeviceSDK constructor is responsible for initializing the track.
     */
    private DeviceSDK() {
        device = new Device();
    }

    /**
     * getInstance returns a singleton of this DeviceSDK to be used in any module.
     *
     * @return the singleton of DeviceSDK
     */
    public static DeviceSDK getInstance() {
        return instance;
    }

    /**
     * Execute instantiates a new object of the DeviceSDKTask
     * class and, in this way, the listeners are initialized
     * and information tracking is executed asynchronously.
     *
     * @param context the application context to be used
     */
    public void execute(final Context context) {
        new DeviceSDKTask(context, device).execute();
    }

    /**
     * getInfo returns a Device object with all the necessary information needed to send to the
     * MercadoPago API when a CardToken is created
     *
     * @return Device object used on body of CardToken creation
     */
    public Device getInfo() {
        return device;
    }

    /**
     * getInfo returns a Device object with all the necessary information needed to send to the
     * MercadoPago API when a CardToken is created
     *
     * @return JSON object of Device object used on body of CardToken creation
     */
    public String getInfoAsJsonString() {
        return device.getJsonString();
    }

    /**
     * getInfo returns a Device object with all the necessary information needed to send to the
     * MercadoPago API when a CardToken is created
     *
     * @return Map of string,object of Device object to be used on body of CardToken creation
     */
    public Map<String, Object> getInfoAsMap() {
        return device.getAsMap();
    }
}
