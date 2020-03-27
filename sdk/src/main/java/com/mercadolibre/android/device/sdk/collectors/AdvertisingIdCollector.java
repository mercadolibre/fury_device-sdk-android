package com.mercadolibre.android.device.sdk.collectors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.mercadolibre.android.device.sdk.domain.Device;
import com.mercadolibre.android.device.sdk.domain.VendorId;

/**
 * AdvertisingIdCollector collect the advertising identifier of the device, this information
 * requires that this module not run on the main thread.
 */
public class AdvertisingIdCollector {

    // The singleton instance.
    private static final AdvertisingIdCollector INSTANCE = new AdvertisingIdCollector();

    /**
     * Retry 10 times to get advertising_id to a max of 2.5 seconds sleep on thread
     */
    public static int MAX_RETRY = 10;
    /* default */ static final String ADVERTISING_ID_TAG = "ADVERTISING_ID";
    /* default */ static final int THREAD_SLEEP_TIME = 250;

    @Nullable
    private static String advertisingId;
    private Device deviceRef;

    /* default */ final ExecutorService executorService = Executors.newSingleThreadExecutor();
    /* default */ boolean shouldRetryAdvertiserId = true;
    /* default */ Context context;
    private Collection<Callable<String>> requestBatch;

    private AdvertisingIdCollector() {
        // Nothing to do here.
    }

    /**
     * Gets the singleton instance of the {@link AdvertisingIdCollector}.
     *
     * @return The singleton instance.
     */
    public static AdvertisingIdCollector getInstance() {
        return INSTANCE;
    }

    /**
     * start a request (thread safe) in order to load the advertising_id.
     *
     * @param context application context
     */
    public void start(@NonNull final Context context, final Device device) {
        synchronized (this) {
            this.context = context;
            this.deviceRef = device;
            requestBatch = generateAdvertiserIdRequest();
            new Thread(() -> retryAdvertisingIdRequest()).start();
        }
    }

    /* default */ void retryAdvertisingIdRequest() {
        try {
            advertisingId = executorService.invokeAny(requestBatch);
            if (deviceRef.getFingerprint().getVendorIds().isEmpty()) {
                deviceRef.getFingerprint().setVendorIds(new ArrayList<>());
            }
            if (advertisingId != null) {
                deviceRef.getFingerprint().vendorIds.add(new VendorId("advertising_id", advertisingId));
            }
        } catch (InterruptedException e) {
            Log.e(ADVERTISING_ID_TAG, "Executor call was interrupted by shutting down", e);
        } catch (ExecutionException e) {
            Log.e(ADVERTISING_ID_TAG, String.format(Locale.US, "Couldn't get advertising_id after %d retries", MAX_RETRY), e);
        }
    }

    /* default */ boolean shouldRetryAdvertiserIdRequest() {
        return shouldRetryAdvertiserId && TextUtils.isEmpty(advertisingId);
    }

    private Collection<Callable<String>> generateAdvertiserIdRequest() {
        List<Callable<String>> advertiserIdsRetries = new ArrayList<>();
        for (int i = 0; i < MAX_RETRY; i++) {
            advertiserIdsRetries.add(new AdvertiserIdTask());
        }
        return advertiserIdsRetries;
    }

    /* default */ void sleepThread() {
        try {
            Thread.sleep(THREAD_SLEEP_TIME);
        } catch (InterruptedException e) {
            Log.w(ADVERTISING_ID_TAG, "Sleep for retries interrupted: " + e.getMessage());
        }
    }

    private class AdvertiserIdTask implements Callable<String> {

        @Override
        public String call() {
            String possibleAdId = null;

            if (shouldRetryAdvertiserIdRequest()) {
                try {
                    AdvertisingIdClient.Info adInfo = getAdvertisingIdInfo(context);
                    possibleAdId = adInfo.getId();
                } catch (IOException | GooglePlayServicesRepairableException ioe) {
                    Log.w(ADVERTISING_ID_TAG, "Connection to Google Play Services failed: " + ioe.getMessage());
                } catch (GooglePlayServicesNotAvailableException gpsnae) {
                    shouldRetryAdvertiserId = false;
                    Log.w(ADVERTISING_ID_TAG, "Google Play is not installed on this device: " + gpsnae.getMessage());
                } catch (IllegalStateException e) {
                    shouldRetryAdvertiserId = false;
                    Log.e(ADVERTISING_ID_TAG, "Error getting Advertising ID: ", e);
                }

                sleepThread();
            }

            return possibleAdId;
        }
    }

    /**
     * Obtains AdvertisingIdInfo from Advertising API
     *
     * @return AdvertisingIdInfo with the advertisingId
     */
    @VisibleForTesting
    @NonNull
    public AdvertisingIdClient.Info getAdvertisingIdInfo(@NonNull final Context context) throws IOException, GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        return AdvertisingIdClient.getAdvertisingIdInfo(context);

    }
}
