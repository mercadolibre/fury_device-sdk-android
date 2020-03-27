package com.mercadolibre.android.device.sdk.collectors;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.domain.Fingerprint;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;

@SuppressFBWarnings
@RunWith(RobolectricTestRunner.class)
public class LocationCollectorTest {

    private LocationCollector locationCollector;
    @Mock
    private Context mContext;

    @Mock
    private LocationManager locationManager;

    @Before
    public void setUp() {
        initMocks();
    }

    private void initMocks() {
        mContext = mock(Context.class);
        setUpLocationCollector(mContext);
    }

    private void setUpLocationCollector(final Context context) {
        locationCollector = spy(LocationCollector.class);
        locationManager = mock(LocationManager.class);
        doReturn(locationManager).when(context).getSystemService(Context.LOCATION_SERVICE);
    }

    @Test
    public void shouldNotLoadLocation_emptyLocationProviders() {
        Device device = new Device();

        //Mock empty location providers
        List<String> providers = new ArrayList<>();
        doReturn(providers).when(locationManager).getAllProviders();

        locationCollector.collectData(mContext, device);

        Fingerprint deviceFingerprint = device.getFingerprint();
        assertNotNull(deviceFingerprint);
        assertNull(deviceFingerprint.locationData);
    }

    @Test
    public void shouldNotLoadLocation_permissionsDisabled() {
        Device device = new Device();

        //Mock location providers with network available
        List<String> providers = new ArrayList<>();
        providers.add(LocationManager.NETWORK_PROVIDER);
        doReturn(providers).when(locationManager).getAllProviders();

        //Mock permission disabled
        doReturn(PackageManager.PERMISSION_DENIED).when(locationCollector).checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);

        locationCollector.collectData(mContext, device);

        Fingerprint deviceFingerprint = device.getFingerprint();
        assertNotNull(deviceFingerprint);
        assertNull(deviceFingerprint.locationData);
    }

    @Test
    public void shouldLoadLocation_fromNetworkProvider() {
        Device device = new Device();

        //Mock location providers with network available
        List<String> providers = new ArrayList<>();
        providers.add(LocationManager.NETWORK_PROVIDER);
        doReturn(providers).when(locationManager).getAllProviders();

        //Mock permission enabled
        doReturn(PackageManager.PERMISSION_GRANTED).when(locationCollector).checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        doReturn(PackageManager.PERMISSION_DENIED).when(locationCollector).checkPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);

        //Mock location
        Location loc = mockLocation(LocationManager.NETWORK_PROVIDER);
        doReturn(loc).when(locationManager).getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationCollector.collectData(mContext, device);

        Fingerprint deviceFingerprint = device.getFingerprint();
        assertNotNull(deviceFingerprint);
        assertNotNull(deviceFingerprint.locationData);
        assertEquals(15, deviceFingerprint.locationData.getAccuracy(), 0);
        assertEquals(20, deviceFingerprint.locationData.getLongitude(), 0);
        assertEquals(10, deviceFingerprint.locationData.getLatitude(), 0);

    }

    @Test
    public void shouldLoadLocation_fromGPSProvider() {
        Device device = new Device();

        //Mock location providers with network available
        List<String> providers = new ArrayList<>();
        providers.add(LocationManager.GPS_PROVIDER);
        doReturn(providers).when(locationManager).getAllProviders();

        //Mock permission enabled
        doReturn(PackageManager.PERMISSION_DENIED).when(locationCollector).checkPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
        doReturn(PackageManager.PERMISSION_GRANTED).when(locationCollector).checkPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);

        //Mock location
        Location loc = mockLocation(LocationManager.GPS_PROVIDER);
        doReturn(loc).when(locationManager).getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationCollector.collectData(mContext, device);

        Fingerprint deviceFingerprint = device.getFingerprint();
        assertNotNull(deviceFingerprint);
        assertNotNull(deviceFingerprint.locationData);
        assertEquals(15, deviceFingerprint.locationData.getAccuracy(), 0);
        assertEquals(20, deviceFingerprint.locationData.getLongitude(), 0);
        assertEquals(10, deviceFingerprint.locationData.getLatitude(), 0);

    }

    private Location mockLocation(String provider) {
        Location loc = new Location(provider);

        loc.setLatitude(10);
        loc.setLongitude(20);
        loc.setAccuracy(15);
        loc.setAltitude(0);
        loc.setTime(System.currentTimeMillis());
        return loc;
    }
}
