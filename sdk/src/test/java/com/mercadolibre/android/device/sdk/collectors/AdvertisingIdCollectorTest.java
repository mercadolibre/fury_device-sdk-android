package com.mercadolibre.android.device.sdk.collectors;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.mercadolibre.android.device.sdk.domain.Device;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@SuppressFBWarnings
@RunWith(RobolectricTestRunner.class)
public class AdvertisingIdCollectorTest {

    private AdvertisingIdCollector advertisingIdCollector;
    @Mock
    private Context mContext;
    @Mock
    private AdvertisingIdClient.Info advertisingIdInfo;

    @Before
    public void setUp() {
        initMocks();
    }

    private void initMocks() {
        mContext = mock(Context.class);
        setUpAdvertisingIdCollector(mContext);
    }

    private void setUpAdvertisingIdCollector(final Context context) {

        advertisingIdCollector = spy(AdvertisingIdCollector.getInstance());
        advertisingIdInfo = mock(AdvertisingIdClient.Info.class);
        try {
            doReturn(advertisingIdInfo).when(advertisingIdCollector).getAdvertisingIdInfo(context);
            setMaxRetriesToOneForTesting();
        } catch (IOException | IllegalStateException | GooglePlayServicesNotAvailableException | GooglePlayServicesRepairableException e) {
            assertNull(e);
        }

    }

    private void setMaxRetriesToOneForTesting() {
        try {
            advertisingIdCollector.MAX_RETRY = 1;
        } catch (@SuppressWarnings("PMD.AvoidCatchingGenericException") final Exception exception) {
            assertNull(exception);
        }
    }

    @Test
    public void testGetAdvertisingIdOK() throws InterruptedException {
        Device device = new Device();
        doReturn("123").when(advertisingIdInfo).getId();
        advertisingIdCollector.start(mContext, device);
        Thread.sleep(300);

        assertNotNull(device.getFingerprint().getVendorIds());
        assertEquals(1, device.getFingerprint().getVendorIds().size());
        assertEquals("advertising_id", device.getFingerprint().getVendorIds().get(0).getName());
        assertEquals("123", device.getFingerprint().getVendorIds().get(0).getValue());
    }

    @Test
    public void testNullResponseWhenGetAdvertisingIdInfo() throws InterruptedException {
        Device device = new Device();
        doReturn(null).when(advertisingIdInfo).getId();
        advertisingIdCollector.start(mContext, device);
        Thread.sleep(300);

        assertEquals(0, device.getFingerprint().getVendorIds().size());
    }


    @Test
    public void testGPSExceptionWhenGetAdvertisingIdInfo() throws InterruptedException, IOException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        Device device = new Device();
        doThrow(new GooglePlayServicesRepairableException(1, "Connection to Google Play Services failed", null)).when(advertisingIdCollector).getAdvertisingIdInfo(any(Context.class));
        advertisingIdCollector.start(mContext, device);
        Thread.sleep(300);

        assertEquals(0, device.getFingerprint().getVendorIds().size());

    }

    @Test
    public void testGPSNotAvailableExceptionWhenGetAdvertisingIdInfo() throws InterruptedException, IOException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        Device device = new Device();
        doThrow(new GooglePlayServicesNotAvailableException(1)).when(advertisingIdCollector).getAdvertisingIdInfo(any(Context.class));
        advertisingIdCollector.start(mContext, device);
        Thread.sleep(300);

        assertEquals(0, device.getFingerprint().getVendorIds().size());
    }

    @Test
    public void testExceptionWhenGetAdvertisingIdInfo() throws InterruptedException, IOException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        Device device = new Device();
        doThrow(new IllegalStateException("Exception")).when(advertisingIdCollector).getAdvertisingIdInfo(any(Context.class));
        advertisingIdCollector.start(mContext, device);
        Thread.sleep(300);

        assertEquals(0, device.getFingerprint().getVendorIds().size());
    }

}
