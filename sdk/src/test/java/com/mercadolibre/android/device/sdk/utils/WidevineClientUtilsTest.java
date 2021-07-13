package com.mercadolibre.android.device.sdk.utils;

import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;

import com.mercadolibre.android.device.sdk.AbstractRobolectricTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WidevineClientUtilsTest extends AbstractRobolectricTest {
    @Spy
    private WidevineClientUtils widevineClient;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SuppressWarnings("PMD.CloseResource")
    public void shouldGetWidevineId() throws UnsupportedSchemeException {
        MediaDrm mediaDrm = mock(MediaDrm.class);
        byte[] widevineResponse = "23131321-31213-123131-321313".getBytes();
        when(mediaDrm.getPropertyByteArray(anyString())).thenReturn(widevineResponse);
        doNothing().when(mediaDrm).release();
        doReturn(mediaDrm).when(widevineClient).getMediaDrm();

        final String widevineId = widevineClient.getWidevineClientId();
        verify(mediaDrm).release();
        assertNotNull(widevineId);
    }

    @Test
    public void shouldNotGetWidevineIdWhenApiFails() throws UnsupportedSchemeException {
        doThrow(UnsupportedSchemeException.class).when(widevineClient).getMediaDrm();

        final String widevineId = widevineClient.getWidevineClientId();
        assertNull(widevineId);
    }
}