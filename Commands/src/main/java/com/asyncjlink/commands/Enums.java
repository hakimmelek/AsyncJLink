package com.asyncjlink.commands;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Converts the 132 {@code jxenum} classes to and from JSON.
 *
 * <p>J-Link enums are not Java enums — each is a final class exposing {@code static final Foo BAR}
 * singletons alongside {@code static final int _BAR} codes. They are handled reflectively here rather
 * than tabulated, so the 2,562 documented constants cost nothing at runtime.
 */
public final class Enums {

    private Enums() {
    }

    private static final Map<Class<?>, List<Field>> CONSTANTS = new ConcurrentHashMap<>();

    /** Accepts the constant name ({@code "MDL_PART"}) or its integer code. */
    public static Object fromJson(String typeName, Object json, String field) {
        if (json == null) {
            return null;
        }
        Class<?> type = TypeRegistry.classFor(typeName);
        if (json instanceof Number) {
            return fromInt(type, ((Number) json).intValue(), field);
        }
        String name = String.valueOf(json).trim();
        if (name.isEmpty()) {
            return null;
        }
        for (Field f : constants(type)) {
            if (f.getName().equals(name)) {
                try {
                    return f.get(null);
                } catch (IllegalAccessException e) {
                    throw new CommandException(
                            "Cannot read enum constant " + typeName + "." + name, "internal", e);
                }
            }
        }
        // Tolerate the integer code arriving as a string, which is what a shell pipeline produces.
        try {
            return fromInt(type, Integer.parseInt(name), field);
        } catch (NumberFormatException ignored) {
            // fall through to the descriptive error below
        }
        throw new CommandException(
                "Field '" + field + "' must be one of " + names(typeName) + " but was '" + name + "'",
                "invalid_params");
    }

    private static Object fromInt(Class<?> type, int value, String field) {
        try {
            Method m = type.getMethod("FromInt", int.class);
            Object v = m.invoke(null, value);
            if (v == null) {
                throw new CommandException(
                        "Field '" + field + "': " + value + " is not a valid "
                                + type.getSimpleName() + " code", "invalid_params");
            }
            return v;
        } catch (CommandException e) {
            throw e;
        } catch (ReflectiveOperationException e) {
            throw new CommandException(
                    "Cannot convert " + value + " to " + type.getSimpleName(), "internal", e);
        }
    }

    /** Renders an enum instance as its constant name. */
    public static String toJson(Object value) {
        if (value == null) {
            return null;
        }
        Class<?> type = value.getClass();
        for (Field f : constants(type)) {
            try {
                if (f.get(null) == value) {
                    return f.getName();
                }
            } catch (IllegalAccessException ignored) {
                // Unreadable constant; keep scanning.
            }
        }
        // Identity failed — compare by code, which survives any re-boxing inside CIP.
        Integer code = codeOf(value);
        if (code != null) {
            for (Field f : constants(type)) {
                try {
                    Integer other = codeOf(f.get(null));
                    if (code.equals(other)) {
                        return f.getName();
                    }
                } catch (IllegalAccessException ignored) {
                    // Unreadable constant; keep scanning.
                }
            }
            return String.valueOf(code);
        }
        return String.valueOf(value);
    }

    private static Integer codeOf(Object enumValue) {
        if (enumValue == null) {
            return null;
        }
        try {
            Method m = enumValue.getClass().getMethod("getValue");
            Object v = m.invoke(enumValue);
            return v instanceof Number ? ((Number) v).intValue() : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /** Every constant name of an enum type, in declaration order — used to build schemas. */
    public static List<String> names(String typeName) {
        List<String> out = new ArrayList<>();
        for (Field f : constants(TypeRegistry.classFor(typeName))) {
            out.add(f.getName());
        }
        return out;
    }

    private static List<Field> constants(Class<?> type) {
        return CONSTANTS.computeIfAbsent(type, t -> {
            List<Field> out = new ArrayList<>();
            for (Field f : t.getFields()) {
                int mods = f.getModifiers();
                // The singletons are typed as the enum class itself; the parallel `_NAME` ints are
                // declared `int` and are skipped by this test.
                if (Modifier.isStatic(mods) && Modifier.isFinal(mods) && f.getType() == t) {
                    out.add(f);
                }
            }
            return out;
        });
    }
}
