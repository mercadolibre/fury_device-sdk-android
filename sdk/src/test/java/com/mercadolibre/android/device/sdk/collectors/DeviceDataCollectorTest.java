package com.mercadolibre.android.device.sdk.collectors;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.domain.Fingerprint;
import com.mercadolibre.android.device.sdk.utils.WidevineClientUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import android.view.Display;

import org.robolectric.RobolectricTestRunner;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SuppressFBWarnings
@RunWith(RobolectricTestRunner.class)
public class DeviceDataCollectorTest {

    @Mock
    private Context mContext;
    private DeviceDataCollector deviceDataCollector;

    @Before
    public void setUp() {
        initMocks();
    }

    private void initMocks() {
        mContext = mock(Context.class);
        setUpDeviceDataCollector();
    }

    private void setUpDeviceDataCollector() {
        deviceDataCollector = spy(DeviceDataCollector.class);

        //Mock system features like (hasBluetooth, camera, gps, etc.)
        PackageManager pm = mock(PackageManager.class);
        doReturn(pm).when(mContext).getPackageManager();
        doReturn(true).when(pm).hasSystemFeature(any(String.class));

        //Mock system screen density
        Resources r = spy(Resources.class);
        DisplayMetrics dm = spy(DisplayMetrics.class);
        dm.density = 100;
        doReturn(dm).when(r).getDisplayMetrics();
        doReturn(r).when(mContext).getResources();

        //Mock system memory information
        ActivityManager am = spy(ActivityManager.class);
        doNothing().when(am).getMemoryInfo(any());
        doReturn(am).when(mContext).getSystemService(Context.ACTIVITY_SERVICE);

        //Mock system window manager
        WindowManager wm = spy(WindowManager.class);
        dm.widthPixels = 1024;
        dm.heightPixels = 768;
        Display d = spy(Display.class);
        doNothing().when(d).getMetrics(any());
        doReturn(d).when(wm).getDefaultDisplay();
        doReturn(wm).when(mContext).getSystemService(Context.WINDOW_SERVICE);

        //Mock widevine client utils call
        WidevineClientUtils w = mock(WidevineClientUtils.class);
        when(w.getWidevineClientId()).thenReturn("123");
        doReturn(w).when(deviceDataCollector).getWidevineClientUtils();

        //Mock androidId
        doReturn("123").when(deviceDataCollector).getAndroidId(mContext);

    }

    @Test
    public void testGetDeviceDataOK() {
        Device device = new Device();
        device.setFingerprint(null);

        deviceDataCollector.collectData(mContext, device);
        Fingerprint deviceFingerprint = device.getFingerprint();
        assertNotNull(deviceFingerprint);
        assertNotNull(deviceFingerprint.getVendorIds());
        assertEquals(2, deviceFingerprint.getVendorIds().size());
        assertEquals("123", deviceFingerprint.getVendorIds().get(0).getValue());
        assertEquals("android_id", deviceFingerprint.getVendorIds().get(0).getName());
        assertEquals("123", deviceFingerprint.getVendorIds().get(1).getValue());
        assertEquals("widevine_id", deviceFingerprint.getVendorIds().get(1).getName());
        assertNotNull(deviceFingerprint.vendorSpecificAttributes);
        assertEquals("Android", deviceFingerprint.vendorSpecificAttributes.getBrand());
        assertTrue(deviceFingerprint.vendorSpecificAttributes.isFeatureGps());
    }

}
