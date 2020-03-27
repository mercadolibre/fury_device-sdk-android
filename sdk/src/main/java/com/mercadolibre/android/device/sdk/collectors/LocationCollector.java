package com.mercadolibre.android.device.sdk.collectors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;

import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.domain.LocationData;

/**
 * LocationCollector is responsible to retrieve the location of the device, this collector checks if user
 * has enabled the location and allowed the running application to access location.
 */
@SuppressWarnings("MissingPermission")
public class LocationCollector {

    private static final long TIME_MILLIS = 1000L;

    private Context contextRef;
    /* default */ Device deviceRef;
    private FingerprintLocationListener mLocationListener;
    private LocationManager mLocationManager;

    /**
     * CollectData init the location listeners and look up if the app has location permissions enabled,
     * if permissions were not granted to the app, this will set empty location.
     * @param context application context
     * @param device device reference
     */
    public void collectData(final Context context, final Device device) {
        this.contextRef = context;
        this.deviceRef = device;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new FingerprintLocationListener();
        registerLocationUpdate();
        updateLocationWithCached();
    }


    private void updateLocationWithCached() {
        if (!isLocationPermissionGranted(contextRef)) {
            return;
        }
        android.location.Location networkCached = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (networkCached != null) {
            LocationData locationData = getLocationData(networkCached);
            deviceRef.getFingerprint().updateLocationData(locationData);
            return;
        }

        android.location.Location gpsCached = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (gpsCached != null) {
            LocationData locationData = getLocationData(gpsCached);
            deviceRef.getFingerprint().updateLocationData(locationData);
        }
    }

    /* default */ LocationData getLocationData(android.location.Location location) {
        LocationData ld = new LocationData();
        ld.setLongitude(location.getLongitude());
        ld.setLatitude(location.getLatitude());
        ld.setAccuracy(location.getAccuracy());
        ld.setProvider(location.getProvider());
        ld.setTimestamp(System.currentTimeMillis() / TIME_MILLIS);
        return ld;
    }

    @SuppressLint("MissingPermission")
    private void registerLocationUpdate() {
        if (isLocationPermissionGranted(contextRef)) {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    0, 0, mLocationListener, Looper.getMainLooper());
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, mLocationListener, Looper.getMainLooper());
        }
    }

    private boolean isLocationPermissionGranted(Context context) {
        return (mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) ||
                mLocationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /* default */ int checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission);
    }

    /**
     * stopLocationUpdates stops sending location updates.
     */
    @SuppressLint("MissingPermission")
    public void stopLocationUpdates() {
        if (mLocationManager == null) {
            return;
        }
        mLocationManager.removeUpdates(mLocationListener);
        mLocationManager = null;
    }

    /**
     * FingerprintLocationListener listens to device location updates and refresh it on device object.
     */
    private class FingerprintLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(android.location.Location location) {
            LocationData newLocation = getLocationData(location);
            deviceRef.getFingerprint().updateLocationData(newLocation);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            // do nothing
        }

        @Override
        public void onProviderEnabled(String s) {
            // do nothing
        }

        @Override
        public void onProviderDisabled(String s) {
            // do nothing
        }
    }
}
