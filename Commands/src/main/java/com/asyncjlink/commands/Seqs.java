package com.asyncjlink.commands;

import com.asyncjlink.json.JsonArray;

import java.lang.reflect.Method;

/**
 * Converts the 105 {@code jxobject_i} sequence types to and from JSON arrays.
 *
 * <p>Most sequences in {@code pfcasync.jar} expose the same shape — {@code create()},
 * {@code getarraysize()}, {@code get(int)}, {@code append(E)} — so one reflective implementation
 * covers all of them, including the {@code com.ptc.cipjava} primitives ({@code stringseq},
 * {@code intseq}, {@code realseq}).
 *
 * <p>A second family has no {@code getarraysize()} or {@code append()} at all, because the
 * dictionary declares them as fixed-size: {@code Point2D}/{@code UVParams} (always 2),
 * {@code Point3D}/{@code Vector3D} (always 3), {@code Outline2D}/{@code Outline3D} (always 2
 * points), and so on. There is no accessor anywhere that reports which fixed size a given type is,
 * so rather than hardcode one constant that would be right for some of these and silently wrong for
 * others (as an earlier version of this file did for exactly that reason), {@link #toJson} reads
 * sequentially from index 0 until the index is rejected.
 *
 * <p>A third family — {@code Matrix3D}, {@code Inertia} — is two-dimensional:
 * {@code get(int, int)}/{@code set(int, int, E)}, addressed the same way but rendered as rows of
 * columns.
 */
public final class Seqs {

    /**
     * Upper bound when probing a fixed-size type's dimension by index, per axis. Every declared
     * fixed-size type is 4 or smaller (a homogeneous transform matrix is the largest, at 4x4); this
     * leaves headroom without letting a genuinely broken accessor spin.
     */
    private static final int MAX_FIXED_DIMENSION = 8;

    private Seqs() {
    }

    /** Builds a live sequence from a JSON array. */
    public static Object fromJson(CreoContext ctx, String seqTypeName, Object json, String field) {
        if (json == null) {
            return null;
        }
        if (!(json instanceof JsonArray)) {
            throw new CommandException(
                    "Field '" + field + "' must be an array of " + elementTypeName(seqTypeName),
                    "invalid_params");
        }
        JsonArray array = (JsonArray) json;
        Class<?> seqType = TypeRegistry.classFor(seqTypeName);
        Class<?> elem = elementClass(seqType);
        String elemName = elem.getSimpleName();
        try {
            Method create = seqType.getMethod("create");
            create.setAccessible(true);
            Object seq = create.invoke(null);
            Method append = appendMethod(seqType, elem);
            if (append != null) {
                append.setAccessible(true);
                for (int i = 0; i < array.size(); i++) {
                    Object v = Marshal.in(ctx, array.get(i), elemName, elem, field + "[" + i + "]");
                    append.invoke(seq, v);
                }
                return seq;
            }
            // No append(): a fixed-size type such as Outline2D/Outline3D, addressed by index instead.
            Method set = findMethod(seqType, "set", int.class, elem);
            set.setAccessible(true);
            for (int i = 0; i < array.size(); i++) {
                Object v = Marshal.in(ctx, array.get(i), elemName, elem, field + "[" + i + "]");
                set.invoke(seq, Integer.valueOf(i), v);
            }
            return seq;
        } catch (CommandException e) {
            throw e;
        } catch (ReflectiveOperationException e) {
            throw new CommandException(
                    "Cannot build " + seqTypeName + " for field '" + field + "': " + rootMessage(e),
                    "internal", e);
        }
    }

    /** Renders a live sequence as a JSON array. */
    public static JsonArray toJson(CreoContext ctx, Object seq, String seqTypeName, int depth) {
        if (seq == null) {
            return null;
        }
        try {
            Class<?> seqType = seq.getClass();
            Method size = findMethodOrNull(seqType, "getarraysize");
            if (size != null) {
                Method get = findMethod(seqType, "get", int.class);
                String elemName = get.getReturnType().getSimpleName();
                get.setAccessible(true);
                size.setAccessible(true);
                int n = ((Number) size.invoke(seq)).intValue();
                JsonArray out = new JsonArray();
                for (int i = 0; i < n; i++) {
                    out.add(Marshal.out(ctx, get.invoke(seq, i), elemName, depth + 1));
                }
                return out;
            }
            Method get1 = findMethodOrNull(seqType, "get", int.class);
            if (get1 != null) {
                // No getarraysize(): a fixed-size 1-D type (Point2D/3D, Vector3D, UVParams,
                // Outline2D/3D, ...). Nothing reports its size, so read sequentially until Creo
                // rejects the index rather than assuming a size that is right for some of these
                // types and wrong for others.
                return readFixed1D(ctx, seq, get1, depth);
            }
            Method get2 = findMethodOrNull(seqType, "get", int.class, int.class);
            if (get2 != null) {
                // Also no getarraysize(): a fixed-size 2-D type (Matrix3D, Inertia, ...), rendered
                // as rows of columns, each probed the same way.
                return readFixed2D(ctx, seq, get2, depth);
            }
            throw new CommandException(
                    "Cannot read " + seqTypeName + ": no get() accessor found", "internal");
        } catch (ReflectiveOperationException e) {
            throw new CommandException(
                    "Cannot read " + seqTypeName + ": " + rootMessage(e), "internal", e);
        }
    }

    private static JsonArray readFixed1D(CreoContext ctx, Object seq, Method get, int depth)
            throws ReflectiveOperationException {
        String elemName = get.getReturnType().getSimpleName();
        get.setAccessible(true);
        JsonArray out = new JsonArray();
        for (int i = 0; i < MAX_FIXED_DIMENSION; i++) {
            Object v;
            try {
                v = get.invoke(seq, Integer.valueOf(i));
            } catch (ReflectiveOperationException | RuntimeException e) {
                break;
            }
            out.add(Marshal.out(ctx, v, elemName, depth + 1));
        }
        return out;
    }

    private static JsonArray readFixed2D(CreoContext ctx, Object seq, Method get, int depth)
            throws ReflectiveOperationException {
        String elemName = get.getReturnType().getSimpleName();
        get.setAccessible(true);
        JsonArray out = new JsonArray();
        for (int r = 0; r < MAX_FIXED_DIMENSION; r++) {
            JsonArray row = new JsonArray();
            for (int c = 0; c < MAX_FIXED_DIMENSION; c++) {
                Object v;
                try {
                    v = get.invoke(seq, Integer.valueOf(r), Integer.valueOf(c));
                } catch (ReflectiveOperationException | RuntimeException e) {
                    break;
                }
                row.add(Marshal.out(ctx, v, elemName, depth + 1));
            }
            if (row.size() == 0) {
                break;
            }
            out.add(row);
        }
        return out;
    }

    /** The J-Link name of a sequence's element type, e.g. {@code Selections} to {@code Selection}. */
    public static String elementTypeName(String seqTypeName) {
        try {
            return elementClass(TypeRegistry.classFor(seqTypeName)).getSimpleName();
        } catch (RuntimeException e) {
            return "value";
        }
    }

    private static Class<?> elementClass(Class<?> seqType) {
        try {
            return seqType.getMethod("get", int.class).getReturnType();
        } catch (NoSuchMethodException e) {
            throw new CommandException(
                    seqType.getSimpleName() + " is not a J-Link sequence", "internal", e);
        }
    }

    /** {@code null} when the type has no {@code append(E)} at all, e.g. a fixed-size outline. */
    private static Method appendMethod(Class<?> seqType, Class<?> elem) {
        try {
            return seqType.getMethod("append", elem);
        } catch (NoSuchMethodException e) {
            // Some sequences declare append against a supertype of the element returned by get().
            for (Method m : seqType.getMethods()) {
                if (m.getName().equals("append") && m.getParameterCount() == 1) {
                    return m;
                }
            }
            return null;
        }
    }

    private static Method findMethod(Class<?> type, String name, Class<?>... args)
            throws NoSuchMethodException {
        Method m = findMethodOrNull(type, name, args);
        if (m == null) {
            throw new NoSuchMethodException(type.getName() + "." + name);
        }
        return m;
    }

    private static Method findMethodOrNull(Class<?> type, String name, Class<?>... args) {
        try {
            return type.getMethod(name, args);
        } catch (NoSuchMethodException e) {
            for (Method m : type.getMethods()) {
                if (m.getName().equals(name) && m.getParameterCount() == args.length) {
                    return m;
                }
            }
            return null;
        }
    }

    static String rootMessage(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null) {
            c = c.getCause();
        }
        String m = c.getMessage();
        return m == null ? c.getClass().getSimpleName() : m;
    }
}
