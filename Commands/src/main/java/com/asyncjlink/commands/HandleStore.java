package com.asyncjlink.commands;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Names the live Creo objects that cross the JSON boundary.
 *
 * <p>Most J-Link calls return or accept a live object — a {@code Feature}, a {@code Surface}, a
 * {@code Selection} — that has no serialisable identity. Rather than flatten those away, every such
 * object is registered here and represented on the wire as an opaque string like {@code Surface@41}.
 * A later command passes that string back and gets the same object.
 *
 * <p>Handles live and die with the connection: a CLI invocation discards them on exit, an MCP
 * session keeps them for as long as the agent is connected. They are deliberately not portable
 * across processes.
 */
public final class HandleStore {

    private final Map<String, Object> byId = new HashMap<>();
    private final IdentityHashMap<Object, String> byObject = new IdentityHashMap<>();
    private final Map<String, Integer> counters = new HashMap<>();

    /**
     * Returns the handle for {@code object}, registering it on first sight.
     *
     * <p>Stable per object: handing the same live object back twice yields the same handle, so an
     * agent can recognise that two results refer to one thing.
     */
    public String handleFor(Object object, String jlinkType) {
        if (object == null) {
            return null;
        }
        String existing = byObject.get(object);
        if (existing != null) {
            return existing;
        }
        String type = jlinkType == null ? simpleName(object) : jlinkType;
        int n = counters.merge(type, 1, Integer::sum);
        String id = type + "@" + n;
        byId.put(id, object);
        byObject.put(object, id);
        return id;
    }

    public Object lookup(String handle) {
        return handle == null ? null : byId.get(handle);
    }

    public boolean isHandle(String candidate) {
        return candidate != null && byId.containsKey(candidate);
    }

    public int size() {
        return byId.size();
    }

    /** Drops every handle. The objects themselves belong to Creo and are untouched. */
    public void clear() {
        byId.clear();
        byObject.clear();
        counters.clear();
    }

    private static String simpleName(Object o) {
        // J-Link hands back implementation classes such as Surface_i; the interface name is what
        // callers recognise, so trim the CIP suffix when one is present.
        String n = o.getClass().getSimpleName();
        if (n.endsWith("_i") || n.endsWith("_u")) {
            n = n.substring(0, n.length() - 2);
        }
        return n.isEmpty() ? "Object" : n;
    }
}
