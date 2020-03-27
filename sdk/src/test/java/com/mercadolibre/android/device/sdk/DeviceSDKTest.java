package com.mercadolibre.android.device.sdk;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.domain.LocationData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLocationManager;

import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.mockito.Mockito.spy;

@SuppressFBWarnings
@RunWith(RobolectricTestRunner.class)
public class DeviceSDKTest extends AbstractRobolectricTest{
    private Context context;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        Application a = getApplication();
        ShadowApplication sa = Shadow.extract(a.getApplicationContext());
        sa.grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

        context = a.getApplicationContext();

    }

    @After
    @Override
    public void tearDown() {
        super.tearDown();
        stopLocationMocks(context);
    }

    @Test
    public void startDeviceSDK_WithGPSLocation() {
        mockLocation(LocationManager.GPS_PROVIDER);
        final DeviceSDK sdk = spy(DeviceSDK.class).getInstance();
        sdk.execute(context);

        Device d = sdk.getInfo();
        Assert.assertNotNull(d);
        Assert.assertEquals(Device.class, d.getClass());
        Assert.assertNotNull(d.getFingerprint());
        LocationData ld = d.getFingerprint().locationData;
        Assert.assertNotNull(ld);
        Assert.assertNotNull(ld.getAccuracy());
        Assert.assertNotNull(ld.getLatitude());
        Assert.assertNotNull(ld.getLongitude());
        Assert.assertNotNull(ld.getTimestamp());
        Assert.assertEquals(LocationManager.GPS_PROVIDER, ld.getProvider());
    }

    @Test
    public void startDeviceSDK_WithNetworkLocation() {
        mockLocation(LocationManager.NETWORK_PROVIDER);
        final DeviceSDK sdk = spy(DeviceSDK.class).getInstance();
        sdk.execute(context);

        Device d = sdk.getInfo();
        Assert.assertNotNull(d);
        Assert.assertEquals(Device.class, d.getClass());
        Assert.assertNotNull(d.getFingerprint());
        Assert.assertEquals("robolectric", d.getFingerprint().model);
        LocationData ld = d.getFingerprint().locationData;
        Assert.assertNotNull(ld);
        Assert.assertNotNull(ld.getAccuracy());
        Assert.assertNotNull(ld.getLatitude());
        Assert.assertNotNull(ld.getLongitude());
        Assert.assertNotNull(ld.getTimestamp());
        Assert.assertEquals(LocationManager.NETWORK_PROVIDER, ld.getProvider());
    }

    @Test
    public void startDeviceSDK_AsMap() {
        mockLocation(LocationManager.NETWORK_PROVIDER);
        final DeviceSDK sdk = spy(DeviceSDK.class).getInstance();
        sdk.execute(context);

        Map<String, Object> d = sdk.getInfoAsMap();
        Assert.assertNotNull(d);
        Assert.assertNotNull(d.get("fingerprint"));
        Map<String, Object> fingerprint = (Map) d.get("fingerprint");
        Assert.assertEquals("robolectric", fingerprint.get("model"));
        Map<String, Object> location = (Map) fingerprint.get("location");
        Assert.assertNotNull(location.get("accuracy"));
        Assert.assertEquals("network", location.get("provider"));
    }

    @Test
    public void startDeviceSDK_AsJsonString() {
        mockLocation(LocationManager.NETWORK_PROVIDER);
        final DeviceSDK sdk = spy(DeviceSDK.class).getInstance();
        sdk.execute(context);

        String d = sdk.getInfoAsJsonString();
        Assert.assertNotNull(d);
    }

    private void mockLocation(String provider) {
        ShadowLocationManager lm = Shadow.extract(getContext().getSystemService(Context.LOCATION_SERVICE));
        ShadowApplication sa = Shadow.extract(getContext());
        sa.grantPermissions();
        lm.setProviderEnabled(provider, true);

        Location locationFromSdk = new Location(provider);
        locationFromSdk.setLatitude(37);
        locationFromSdk.setLongitude(-122);
        locationFromSdk.setAccuracy(5.0f);
        locationFromSdk.setTime(2000);
        lm.setLastKnownLocation(provider, locationFromSdk);
    }

    private void stopLocationMocks(Context ctx) {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        lm.removeTestProvider(LocationManager.GPS_PROVIDER);
        lm.removeTestProvider(LocationManager.NETWORK_PROVIDER);

    }

}
