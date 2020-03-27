package com.mercadolibre.android.device.sdk.utils;

import android.support.annotation.Nullable;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public final class JsonUtils {

    private static final Gson GSON;

    static {
        GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .serializeNulls()
                .registerTypeAdapterFactory(ObjectMapTypeAdapter.FACTORY)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
    }

    private JsonUtils() {
    }

    private static Map<String, Object> getMapFromJson(@Nullable final String json) {
        return GSON.fromJson(
                json, new TypeToken<ObjectMapTypeAdapter.ObjectMapType>() {
                }.getType()
        );
    }

    public static Map<String, Object> getMapFromObject(@Nullable final Object src) {
        return getMapFromJson(GSON.toJson(src));
    }

}
