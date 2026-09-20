package com.asyncjlink.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Ordered JSON array holding values from the canonical model described on {@link JsonObject}. */
public final class JsonArray implements Iterable<Object> {

    private final ArrayList<Object> items = new ArrayList<>();

    public static JsonArray of(Object... values) {
        JsonArray a = new JsonArray();
        for (Object v : values) {
            a.add(v);
        }
        return a;
    }

    public JsonArray add(Object value) {
        items.add(Json.normalise(value));
        return this;
    }

    public Object get(int index) {
        return items.get(index);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<Object> asList() {
        return items;
    }

    @Override
    public Iterator<Object> iterator() {
        return items.iterator();
    }

    @Override
    public String toString() {
        return Json.write(this);
    }
}
