package com.mercadolibre.android.device.sdk.utils;

import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.util.Base64;
import android.util.Log;

import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class WidevineClientUtils {

    private static final String TAG = WidevineClientUtils.class.getSimpleName();

    private static final UUID WIDEVINE_UUID = UUID.fromString("edef8ba9-79d6-4ace-a3c8-27dcd51d21ed");

    /**
     * getWidevineClientId gets widevine client ID
     *
     * @return widevine ID
     */
    @VisibleForTesting
    public String getWidevineClientId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            MediaDrm mediaDrm = null;
            try {
                mediaDrm = getMediaDrm();
                final byte[] deviceUniqueId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
                if (deviceUniqueId != null) {
                    return Base64.encodeToString(deviceUniqueId, Base64.NO_WRAP);
                }
            } catch (final UnsupportedSchemeException e) {
                Log.w(TAG, "Error when trying to get widevine id: " + e.getMessage());
            } catch (@SuppressWarnings("PMD.AvoidCatchingGenericException") final Exception e) {
                Log.w(TAG, "Error getting widevine id: " + e.getMessage());
            } finally {
                if (mediaDrm != null) {
                    closeDrmApi(mediaDrm);
                }
            }
        }
        return null;
    }

    @VisibleForTesting
        /* default */ MediaDrm getMediaDrm() throws UnsupportedSchemeException {
        return new MediaDrm(WIDEVINE_UUID);
    }

    @SuppressWarnings("PMD.CloseResource")
    private void closeDrmApi(MediaDrm mediaDrm) {
        final int buildVersion = Build.VERSION.SDK_INT;
        if (buildVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2 && buildVersion <= Build.VERSION_CODES.O_MR1) {
            mediaDrm.release();
        }
        if (buildVersion >= Build.VERSION_CODES.P) {
            mediaDrm.close();
        }
        // There is no need to do anything for API versions prior to 18.
    }
}
