package com.asyncjlink.commands;

import com.asyncjlink.json.JsonObject;
import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;

import java.lang.reflect.Method;

/**
 * The JSON boundary.
 *
 * <p>Every generated command funnels its arguments through {@link #in} and its return value through
 * {@link #out}. Which conversion applies is decided by the J-Link type name, taken from the
 * dictionary at generation time rather than sniffed from the runtime class — CIP hands back
 * implementation types like {@code Surface_i} whose identity would otherwise have to be rediscovered
 * on every call.
 */
public final class Marshal {

    private Marshal() {
    }

    /**
     * How deep to expand nested instruction structs in a result.
     *
     * <p>Some J-Link graphs are cyclic ({@code ComponentPath.GetRoot} leads back to the assembly),
     * so expansion is bounded; anything deeper is reported as a handle or a summary string.
     */
    static final int MAX_DEPTH = 4;

    // ---- JSON to Java ----------------------------------------------------

    /**
     * Converts one JSON value into the Java type a J-Link method expects.
     *
     * @param jlinkType the declared J-Link type name, e.g. {@code ExportInstructions}
     * @param as        the Java class to hand back, used for the cast and for primitive handling
     * @param field     the parameter path, used only in error messages
     */
    @SuppressWarnings("unchecked")
    public static <T> T in(CreoContext ctx, Object json, String jlinkType, Class<T> as, String field) {
        if (as == String.class) {
            return json == null ? null : (T) String.valueOf(json);
        }
        if (as == int.class || as == Integer.class) {
            if (json == null) {
                requirePresent(as == int.class, field, "an integer");
                return null;
            }
            return (T) Integer.valueOf(toNumber(json, field).intValue());
        }
        if (as == double.class || as == Double.class) {
            if (json == null) {
                requirePresent(as == double.class, field, "a number");
                return null;
            }
            return (T) Double.valueOf(toNumber(json, field).doubleValue());
        }
        if (as == boolean.class || as == Boolean.class) {
            if (json == null) {
                requirePresent(as == boolean.class, field, "a boolean");
                return null;
            }
            return (T) Boolean.valueOf(toBoolean(json, field));
        }
        if (json == null) {
            return null;
        }

        switch (TypeRegistry.kind(jlinkType)) {
            case ENUM:
                return (T) Enums.fromJson(jlinkType, json, field);
            case SEQUENCE:
                return (T) Seqs.fromJson(ctx, jlinkType, json, field);
            case LIVE:
                return (T) live(ctx, json, jlinkType, as, field);
            case DATA:
                return (T) DataObjects.fromJson(ctx, jlinkType, json, field);
            case LISTENER:
                throw CommandException.unsupported(
                        "Parameter '" + field + "' of type " + jlinkType,
                        "it is a callback listener, which has no JSON representation");
            default:
                // An unclassified type is still worth attempting as a handle: it is the only
                // representation that could have produced a reference to it.
                return (T) live(ctx, json, jlinkType, as, field);
        }
    }

    private static void requirePresent(boolean primitive, String field, String what) {
        if (primitive) {
            throw new CommandException(
                    "Field '" + field + "' is required and must be " + what, "invalid_params");
        }
    }

    private static Object live(CreoContext ctx, Object json, String jlinkType, Class<?> as, String field) {
        if (json instanceof JsonObject) {
            // Accept the same envelope shape results use, so a value can be round-tripped verbatim.
            Object h = ((JsonObject) json).get("$handle");
            if (h != null) {
                json = h;
            }
        }
        if (!(json instanceof String)) {
            throw new CommandException(
                    "Field '" + field + "' must be a " + jlinkType + " handle (a string returned by "
                            + "an earlier command), but was " + describeJson(json),
                    "invalid_params");
        }
        String ref = (String) json;
        Object found = ctx.handles().lookup(ref);
        if (found != null) {
            if (as.isInstance(found)) {
                return found;
            }
            throw new CommandException(
                    "Field '" + field + "' expects a " + jlinkType + " but handle '" + ref
                            + "' refers to a " + simpleName(found), "invalid_params");
        }
        // Models are the one type with a stable external name, so they may be addressed directly.
        if (Model.class.isAssignableFrom(as)) {
            try {
                Model m = ctx.resolveModel(ref);
                if (m != null && as.isInstance(m)) {
                    return m;
                }
                if (m != null) {
                    throw new CommandException(
                            "Field '" + field + "': '" + ref + "' is a " + simpleName(m)
                                    + ", not a " + jlinkType, "invalid_params");
                }
                throw new CommandException(
                        "Field '" + field + "': no model named '" + ref + "' is in session. "
                                + "Retrieve it first, or pass a handle.", "unknown_handle");
            } catch (jxthrowable e) {
                throw new CommandException(
                        "Creo could not resolve model '" + ref + "': " + Seqs.rootMessage(e),
                        "creo_error", e);
            }
        }
        throw CommandException.unknownHandle(field, ref);
    }

    private static Number toNumber(Object json, String field) {
        if (json instanceof Number) {
            return (Number) json;
        }
        try {
            return Double.valueOf(String.valueOf(json));
        } catch (NumberFormatException e) {
            throw new CommandException(
                    "Field '" + field + "' must be a number but was " + describeJson(json),
                    "invalid_params");
        }
    }

    private static boolean toBoolean(Object json, String field) {
        if (json instanceof Boolean) {
            return (Boolean) json;
        }
        String s = String.valueOf(json).trim();
        if (s.equalsIgnoreCase("true") || s.equals("1")) {
            return true;
        }
        if (s.equalsIgnoreCase("false") || s.equals("0")) {
            return false;
        }
        throw new CommandException(
                "Field '" + field + "' must be true or false but was " + describeJson(json),
                "invalid_params");
    }

    // ---- Java to JSON ----------------------------------------------------

    public static Object out(CreoContext ctx, Object value, String jlinkType) {
        return out(ctx, value, jlinkType, 0);
    }

    /** Wraps a return value in the standard envelope; {@code void} methods use {@link #ok()}. */
    public static JsonObject result(CreoContext ctx, Object value, String jlinkType) {
        return JsonObject.of("result", out(ctx, value, jlinkType, 0));
    }

    /** The empty result of a {@code void} command. */
    public static JsonObject ok() {
        return new JsonObject();
    }

    static Object out(CreoContext ctx, Object value, String jlinkType, int depth) {
        if (value == null) {
            return null;
        }
        if (value instanceof String || value instanceof Boolean) {
            return value;
        }
        if (value instanceof Number) {
            return value;
        }

        TypeKind kind = TypeRegistry.kind(jlinkType);
        if (kind == TypeKind.UNKNOWN) {
            kind = TypeRegistry.kind(simpleName(value));
        }
        switch (kind) {
            case ENUM:
                return Enums.toJson(value);
            case SEQUENCE:
                return Seqs.toJson(ctx, value, jlinkType, depth);
            case LIVE:
                return handle(ctx, value, jlinkType);
            case DATA:
                if (depth >= MAX_DEPTH) {
                    return summary(value);
                }
                return DataObjects.toJson(ctx, value, jlinkType, depth);
            default:
                if (depth >= MAX_DEPTH) {
                    return summary(value);
                }
                // Unclassified but structured: describing it beats printing an object address.
                return DataObjects.toJson(ctx, value, jlinkType, depth);
        }
    }

    /**
     * Represents a live object as its handle plus whatever short identification it can cheaply give.
     *
     * <p>The summary matters in practice: an agent that gets back {@code Feature@7} alone has to make
     * another call to learn what it is, whereas {@code {"$handle":"Feature@7","Name":"ROUND_1"}} is
     * usually enough to decide the next step.
     */
    private static JsonObject handle(CreoContext ctx, Object value, String jlinkType) {
        String type = TypeRegistry.kind(jlinkType) == TypeKind.LIVE ? jlinkType : simpleName(value);
        JsonObject out = JsonObject.of(
                "$handle", ctx.handles().handleFor(value, type),
                "$type", type);
        probe(out, value, "GetFullName", "FullName");
        probe(out, value, "GetName", "Name");
        probe(out, value, "GetId", "Id");
        probe(out, value, "GetFileName", "FileName");
        return out;
    }

    private static void probe(JsonObject out, Object value, String getter, String key) {
        if (out.has(key)) {
            return;
        }
        Method m;
        try {
            m = value.getClass().getMethod(getter);
        } catch (NoSuchMethodException e) {
            // This type simply does not offer this accessor -- every probe() call tries several
            // candidate getters on the assumption that most will miss, so this is the normal case.
            return;
        }
        if (m.getParameterCount() != 0) {
            return;
        }
        try {
            // See DataObjects.toJson: the Method comes from value's runtime class, which for a
            // pfcasync CIP object is usually a non-public Implementation class -- setAccessible(true)
            // is needed to invoke it even though the method itself is public.
            m.setAccessible(true);
            Object v = m.invoke(value);
            if (v instanceof String || v instanceof Number) {
                out.put(key, v);
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            // Unlike a missing accessor, this one exists but refused to run -- that is a real
            // failure (an async proxy call breaking, say), not the object "simply not offering" the
            // value, so it should not look identical to the field being genuinely absent.
            out.put(key + "Error", Seqs.rootMessage(e));
        }
    }

    private static String summary(Object value) {
        return simpleName(value);
    }

    private static String describeJson(Object json) {
        if (json == null) {
            return "null";
        }
        if (json instanceof String) {
            return "the string '" + json + "'";
        }
        return "a " + json.getClass().getSimpleName().toLowerCase(java.util.Locale.ROOT);
    }

    static String simpleName(Object o) {
        String n = o.getClass().getSimpleName();
        if (n.endsWith("_i") || n.endsWith("_u")) {
            n = n.substring(0, n.length() - 2);
        }
        return n;
    }
}
