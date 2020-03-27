package com.mercadolibre.android.device.sdk.collectors;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.support.annotation.VisibleForTesting;

import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.domain.Fingerprint;
import com.mercadolibre.android.device.sdk.domain.VendorId;
import com.mercadolibre.android.device.sdk.domain.VendorSpecificAttributes;
import com.mercadolibre.android.device.sdk.utils.WidevineClientUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * DeviceDataCollector allows to complete the Device Fingerprint object with
 * all the attributes related to the device.
 */
@SuppressWarnings({
        "PMD.AvoidCatchingGenericException",
        "PMD.AvoidFileStream",
        "PMD.IdenticalCatchBranches",
        "PMD.UselessParentheses"
})
public class DeviceDataCollector {

    private static final String TAG = DeviceDataCollector.class.getSimpleName();

    private static final long DISK_STORAGE_UNIT = 1048576;

    private static final String PLATFORM_PROPERTY = "ro.product.cpu.abi";

    /**
     * collectData is the method responsible for fill
     * all the information of the Fingerprint object.
     *
     * @param context   context to take data from
     * @param deviceRef device we need to complete
     */
    public void collectData(final Context context, final Device deviceRef) {
        if (deviceRef == null) {
            return;
        }

        Fingerprint fingerprint = deviceRef.getFingerprint();
        if (fingerprint == null) {
            fingerprint = new Fingerprint();
            deviceRef.setFingerprint(fingerprint);
        }
        fingerprint.setModel(Build.MODEL);
        fingerprint.setOs("android");
        fingerprint.setSystemVersion(Build.VERSION.RELEASE);
        fingerprint.setResolution(getDeviceResolution(context));
        fingerprint.setRam(getDeviceMemory(context));
        fingerprint.setDiskSpace(getDeviceDiskSpace());
        fingerprint.setFreeDiskSpace(getDeviceFreeDiskSpace());

        completeVendorIds(context, fingerprint);
        completeVendorSpecificAttributes(context, fingerprint);
    }

    private void completeVendorIds(Context context, Fingerprint fingerprint) {
        String androidId = getAndroidId(context);
        String widevineId = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            WidevineClientUtils widevineClientUtils = getWidevineClientUtils();
            widevineId = widevineClientUtils.getWidevineClientId();
        }
        if (androidId != null && !androidId.isEmpty()) {
            fingerprint.getVendorIds().add(new VendorId("android_id", androidId));
        }
        if (widevineId != null && !widevineId.isEmpty()) {
            fingerprint.getVendorIds().add(new VendorId("widevine_id", widevineId));
        }
    }

    /**
     * Obtains androidId
     *
     * @return AndroidId
     */
    @VisibleForTesting
    /* default */ String getAndroidId(Context context){
        Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return null;
    }

    /**
     * Obtains getWidevineClientUtils from media drm api
     *
     * @return WidevineClientUtils that will return an unique identifier of media drm api
     */
    @VisibleForTesting
    /* default */ WidevineClientUtils getWidevineClientUtils(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return new WidevineClientUtils();
        }
        return null;
    }

    private static String getDeviceResolution(Context context) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            return "";
        }

        final DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels + "x" + metrics.heightPixels;
    }

    private static long getDeviceMemory(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    private static long getDeviceDiskSpace() {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return (((long) statFs.getBlockSize() * (long) statFs.getBlockCount()) / DISK_STORAGE_UNIT);
    }

    private static long getDeviceFreeDiskSpace() {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return (((long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks()) / DISK_STORAGE_UNIT);
    }

    private static void completeVendorSpecificAttributes(Context context, Fingerprint fingerprint) {
        VendorSpecificAttributes vendorSpecificAttributes = new VendorSpecificAttributes();
        vendorSpecificAttributes.setFeatureCamera(getSystemFeature(context, PackageManager.FEATURE_CAMERA));
        vendorSpecificAttributes.setFeatureFlash(getSystemFeature(context, PackageManager.FEATURE_CAMERA_FLASH));
        vendorSpecificAttributes.setFeatureFrontCamera(getSystemFeature(context, PackageManager.FEATURE_CAMERA_FRONT));
        vendorSpecificAttributes.setProduct(Build.PRODUCT);
        vendorSpecificAttributes.setDevice(Build.DEVICE);
        vendorSpecificAttributes.setPlatform(getSystemProperty(PLATFORM_PROPERTY));
        vendorSpecificAttributes.setBrand(Build.BRAND);
        vendorSpecificAttributes.setFeatureAccelerometer(getSystemFeature(context, PackageManager.FEATURE_SENSOR_ACCELEROMETER));
        vendorSpecificAttributes.setFeatureBluetooth(getSystemFeature(context, PackageManager.FEATURE_BLUETOOTH));
        vendorSpecificAttributes.setFeatureCompass(getSystemFeature(context, PackageManager.FEATURE_SENSOR_COMPASS));
        vendorSpecificAttributes.setFeatureGps(getSystemFeature(context, PackageManager.FEATURE_LOCATION_GPS));
        vendorSpecificAttributes.setFeatureGyroscope(getSystemFeature(context, PackageManager.FEATURE_SENSOR_GYROSCOPE));
        vendorSpecificAttributes.setFeatureMicrophone(getSystemFeature(context, PackageManager.FEATURE_MICROPHONE));
        vendorSpecificAttributes.setFeatureNfc(getSystemFeature(context, PackageManager.FEATURE_NFC));
        vendorSpecificAttributes.setFeatureTelephony(getSystemFeature(context, PackageManager.FEATURE_TELEPHONY));
        vendorSpecificAttributes.setFeatureTouchScreen(getSystemFeature(context, PackageManager.FEATURE_TOUCHSCREEN));
        vendorSpecificAttributes.setManufacturer(Build.MANUFACTURER);
        vendorSpecificAttributes.setScreenDensity(context.getResources().getDisplayMetrics().density);
        fingerprint.setVendorSpecificAttributes(vendorSpecificAttributes);
    }

    private static boolean getSystemFeature(Context context, String feature) {
        return context.getPackageManager().hasSystemFeature(feature);
    }

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }
}
