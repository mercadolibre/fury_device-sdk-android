package com.mercadolibre.android.device.sdk.utils;

import android.support.annotation.Nullable;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* default */ final class ObjectMapTypeAdapter extends TypeAdapter<Object> {

    /* default */ static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @Nullable
        @SuppressWarnings("unchecked")
        @Override
        public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
            if (type.getRawType().equals(ObjectMapType.class)) {
                return (TypeAdapter<T>) new ObjectMapTypeAdapter(gson);
            }
            return null;
        }
    };

    private final Gson gson;

    /* default */ ObjectMapTypeAdapter(final Gson gson) {
        this.gson = gson;
    }

    @Nullable
    @Override
    public Object read(final JsonReader in) throws IOException {
        final JsonToken token = in.peek();
        switch (token) {
            case BEGIN_ARRAY:
                final List<Object> list = new ArrayList<>();
                in.beginArray();
                while (in.hasNext()) {
                    list.add(read(in));
                }
                in.endArray();
                return list;

            case BEGIN_OBJECT:
                final Map<String, Object> map = new LinkedTreeMap<>();
                in.beginObject();
                while (in.hasNext()) {
                    map.put(in.nextName(), read(in));
                }
                in.endObject();
                return map;

            case STRING:
                return in.nextString();

            case NUMBER: {
                final String s = in.nextString();
                try {
                    return Integer.parseInt(s);
                } catch (final NumberFormatException e) {
                    Log.d(ObjectMapTypeAdapter.class.getCanonicalName(), e.getLocalizedMessage());
                }
                try {
                    return Long.parseLong(s);
                } catch (final NumberFormatException e) {
                    Log.d(ObjectMapTypeAdapter.class.getCanonicalName(), e.getLocalizedMessage());
                }
                try {
                    return new BigDecimal(s);
                } catch (final NumberFormatException e) {
                    Log.d(ObjectMapTypeAdapter.class.getCanonicalName(), e.getLocalizedMessage());
                }
                return s;
            }

            case BOOLEAN:
                return in.nextBoolean();

            case NULL:
                in.nextNull();
                return null;

            default:
                throw new IllegalStateException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(final JsonWriter out, final Object value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        final TypeAdapter<Object> typeAdapter = gson.getAdapter((Class<Object>) value.getClass());
        if (typeAdapter instanceof ObjectMapTypeAdapter) {
            return;
        }

        typeAdapter.write(out, value);
    }

    /* default */ final class ObjectMapType {
    }
}