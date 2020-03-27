package com.mercadolibre.android.device.sdk.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * LocationData represents location information to be tracked.
 *
 */
@SuppressWarnings({
        "unused",
        "PMD.SingularField"
})
public class LocationData implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("accuracy")
    private Float accuracy;

    @SerializedName("provider")
    private String provider;

    @SerializedName("timestamp")
    private Long timestamp;

    /**
     * setLatitude sets device's latitude
     *
     * @param latitude device's latitude
     */
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    /**
     * setLongitude sets device longitude
     *
     * @param longitude device's longitude
     */
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    /**
     * setProvider sets location provider
     *
     * @param provider location provider used to get data from
     */
    public void setProvider(final String provider) {
        this.provider = provider;
    }

    /**
     * setProvider sets location provider
     *
     * @param accuracy location accuracy
     */
    public void setAccuracy(final float accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * getLatitude returns current latitude
     *
     * @return current accuracy
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * getLongitude returns current longitude
     *
     * @return current accuracy
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * getProvider returns current provider
     *
     * @return current accuracy
     */
    public String getProvider() {
        return provider;
    }

    /**
     * getAccuracy returns current accuracy
     *
     * @return current accuracy
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * getTimestamp returns current location timestamp
     *
     * @return current accuracy
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * setTimestamp sets a timestamp
     *
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * hasLocation returns if current instance has a defined location
     *
     * @return true if current instance has a valid location event. false otherwise
     */
    public boolean hasLocation() {
        return provider != null && !("".equals(provider));
    }
}
