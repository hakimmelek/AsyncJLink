package com.asyncjlink.json;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Insertion-ordered JSON object.
 *
 * <p>Values are restricted to the canonical JSON model used throughout AsyncJLink:
 * {@link String}, {@link Double}, {@link Long}, {@link Boolean}, {@code null},
 * {@link JsonObject} and {@link JsonArray}. Numbers are normalised to {@code Long}
 * when integral and {@code Double} otherwise.
 */
public final class JsonObject {

    private final LinkedHashMap<String, Object> map = new LinkedHashMap<>();

    public static JsonObject of(Object... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new IllegalArgumentException("of() requires an even number of arguments");
        }
        JsonObject o = new JsonObject();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            o.put(String.valueOf(keyValuePairs[i]), keyValuePairs[i + 1]);
        }
        return o;
    }

    public JsonObject put(String key, Object value) {
        map.put(key, Json.normalise(value));
        return this;
    }

    /** Stores {@code value} only when it is non-null; keeps generated output free of explicit nulls. */
    public JsonObject putIfPresent(String key, Object value) {
        if (value != null) {
            put(key, value);
        }
        return this;
    }

    public Object get(String key) {
        return map.get(key);
    }

    public boolean has(String key) {
        return map.containsKey(key) && map.get(key) != null;
    }

    public Set<String> keys() {
        return map.keySet();
    }

    public Collection<Object> values() {
        return map.values();
    }

    public Map<String, Object> asMap() {
        return map;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public JsonObject remove(String key) {
        map.remove(key);
        return this;
    }

    // ---- typed accessors -------------------------------------------------

    public String getString(String key, String fallback) {
        Object v = map.get(key);
        return v == null ? fallback : String.valueOf(v);
    }

    public String requireString(String key) {
        Object v = require(key);
        return String.valueOf(v);
    }

    public boolean getBoolean(String key, boolean fallback) {
        Object v = map.get(key);
        if (v == null) {
            return fallback;
        }
        if (v instanceof Boolean) {
            return (Boolean) v;
        }
        return Boolean.parseBoolean(String.valueOf(v));
    }

    public int getInt(String key, int fallback) {
        Number n = number(key);
        return n == null ? fallback : n.intValue();
    }

    public double getDouble(String key, double fallback) {
        Number n = number(key);
        return n == null ? fallback : n.doubleValue();
    }

    public JsonObject getObject(String key) {
        Object v = map.get(key);
        if (v == null) {
            return null;
        }
        if (!(v instanceof JsonObject)) {
            throw new IllegalArgumentException("Field '" + key + "' is not an object");
        }
        return (JsonObject) v;
    }

    public JsonArray getArray(String key) {
        Object v = map.get(key);
        if (v == null) {
            return null;
        }
        if (!(v instanceof JsonArray)) {
            throw new IllegalArgumentException("Field '" + key + "' is not an array");
        }
        return (JsonArray) v;
    }

    private Number number(String key) {
        Object v = map.get(key);
        if (v == null) {
            return null;
        }
        if (v instanceof Number) {
            return (Number) v;
        }
        try {
            return Double.valueOf(String.valueOf(v));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Field '" + key + "' is not a number: " + v);
        }
    }

    private Object require(String key) {
        Object v = map.get(key);
        if (v == null) {
            throw new IllegalArgumentException("Missing required field '" + key + "'");
        }
        return v;
    }

    @Override
    public String toString() {
        return Json.write(this);
    }
}
