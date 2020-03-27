package com.mercadolibre.android.device.sdk.utils;

import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
    public String getWidevineClientId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                final MediaDrm mediaDrm = new MediaDrm(WIDEVINE_UUID);
                final byte[] deviceUniqueId = mediaDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
                if (deviceUniqueId != null) {
                    return Base64.encodeToString(deviceUniqueId, Base64.NO_WRAP);
                }
            } catch (final UnsupportedSchemeException e) {
                Log.i(TAG, String.format("error when trying to get widevine id: %s", e.getMessage()));
            }
        }
        return null;
    }
}
