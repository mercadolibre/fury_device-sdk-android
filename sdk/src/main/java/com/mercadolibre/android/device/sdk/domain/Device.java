package com.mercadolibre.android.device.sdk.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.mercadolibre.android.device.sdk.utils.JsonUtils;

import java.io.Serializable;
import java.util.Map;


/**
 * Device represents complete device information.
 */
@SuppressWarnings({
        "unused",
        "PMD.SingularField"
})
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("fingerprint")
    private Fingerprint fingerprint = new Fingerprint();

    /**
     * getFingerPrint returns device fingerprint data collected.
     *
     * @return collected device data
     */
    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    /**
     * setFingerprint sets current device data.
     *
     * @param fingerprint the input value
     */
    public void setFingerprint(final Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * getJson returns collected track in JSON format.
     *
     * @return JSON track
     */
    public String getJsonString() {
        return new Gson().toJson(this);
    }

    /**
     * getJson returns collected track in Map format.
     *
     * @return Map track
     */
    public Map<String, Object> getAsMap() {
        return JsonUtils.getMapFromObject(this);
    }

}
