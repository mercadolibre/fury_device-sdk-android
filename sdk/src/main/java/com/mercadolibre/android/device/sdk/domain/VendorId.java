package com.mercadolibre.android.device.sdk.domain;

import java.io.Serializable;

/**
 * VendorId represents an unique identifier of the device.
 */
@SuppressWarnings({
        "unused",
        "PMD.SingularField"
})
public class VendorId implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final String value;

    public VendorId(String mname, String mvalue) {
        name = mname;
        value = mvalue;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
