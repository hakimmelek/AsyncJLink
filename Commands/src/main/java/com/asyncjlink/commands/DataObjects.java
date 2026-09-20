package com.asyncjlink.commands;

import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Builds and renders the instruction/options objects that J-Link calls take as parameters —
 * {@code ExportInstructions}, {@code CheckoutOptions}, {@code RegenInstructions} and some 250 others.
 *
 * <p>Each is created by a static factory on its package class ({@code pfcExport.ExportInstructions_Create})
 * and then refined through {@code Set*} methods. This class drives that pattern from a JSON object:
 * keys matching factory arguments are passed to the factory, and every remaining key is applied as a
 * setter. Factory arguments are matched by the synthetic names from {@link ParamNames}, since
 * {@code pfcasync.jar} carries no parameter names.
 *
 * <p>Two escape hatches are accepted for the cases where that inference is not enough:
 * {@code "$factory"} names the factory method explicitly, and {@code "$args"} supplies its arguments
 * positionally.
 */
public final class DataObjects {

    private DataObjects() {
    }

    private static final Map<Class<?>, List<Method>> FACTORIES = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, Method>> SETTERS = new ConcurrentHashMap<>();

    /** Builds a live instruction object from its JSON description. */
    public static Object fromJson(CreoContext ctx, String typeName, Object json, String field) {
        if (json == null) {
            return null;
        }
        Class<?> type = TypeRegistry.classFor(typeName);

        // A bare string is an already-built object held in the handle store.
        if (json instanceof String) {
            Object existing = ctx.handles().lookup((String) json);
            if (existing != null && type.isInstance(existing)) {
                return existing;
            }
            throw new CommandException(
                    "Field '" + field + "' expects a " + typeName
                            + " object (or a handle to one), but got the string '" + json + "'",
                    "invalid_params");
        }
        if (!(json instanceof JsonObject)) {
            throw new CommandException(
                    "Field '" + field + "' must be a " + typeName + " object", "invalid_params");
        }
        JsonObject spec = (JsonObject) json;

        Method factory = chooseFactory(type, typeName, spec, field);
        Object[] args = factoryArgs(ctx, factory, spec, field);
        Object instance;
        try {
            instance = factory.invoke(null, args);
        } catch (ReflectiveOperationException e) {
            throw new CommandException(
                    "Creo rejected the " + typeName + " for field '" + field + "': "
                            + Seqs.rootMessage(e), "invalid_params", e);
        }
        if (instance == null) {
            throw new CommandException(
                    "Creo returned no " + typeName + " for field '" + field + "'", "invalid_params");
        }
        applySetters(ctx, instance, type, typeName, spec, consumedKeys(factory), field);
        return instance;
    }

    private static Set<String> consumedKeys(Method factory) {
        Set<String> used = new LinkedHashSet<>();
        used.add("$factory");
        used.add("$args");
        for (String n : ParamNames.forTypes(simpleNames(factory.getParameterTypes()))) {
            used.add(n);
        }
        return used;
    }

    private static Method chooseFactory(Class<?> type, String typeName, JsonObject spec, String field) {
        List<Method> candidates = factories(type, typeName);
        if (candidates.isEmpty()) {
            throw CommandException.unsupported(
                    typeName,
                    "no public factory is exposed by " + TypeRegistry.packageOf(typeName)
                            + ", so it cannot be constructed from JSON");
        }
        String explicit = spec.getString("$factory", null);
        if (explicit != null) {
            for (Method m : candidates) {
                if (m.getName().equals(explicit)) {
                    return m;
                }
            }
            throw new CommandException(
                    "Field '" + field + "': no factory named '" + explicit + "' for " + typeName
                            + ". Available: " + factoryNames(candidates), "invalid_params");
        }
        JsonArray positional = spec.getArray("$args");
        if (positional != null) {
            for (Method m : candidates) {
                if (m.getParameterCount() == positional.size()) {
                    return m;
                }
            }
            throw new CommandException(
                    "Field '" + field + "': no " + typeName + " factory takes "
                            + positional.size() + " arguments", "invalid_params");
        }
        // Prefer the factory whose synthetic argument names the caller actually supplied; among
        // equally good matches prefer the simplest, so the common zero/one-argument form wins.
        Method best = null;
        int bestScore = -1;
        for (Method m : candidates) {
            String[] names = ParamNames.forTypes(simpleNames(m.getParameterTypes()));
            int matched = 0;
            for (String n : names) {
                if (spec.has(n)) {
                    matched++;
                }
            }
            if (matched < names.length) {
                continue; // cannot satisfy every argument
            }
            int score = matched * 100 - m.getParameterCount();
            if (score > bestScore) {
                bestScore = score;
                best = m;
            }
        }
        if (best != null) {
            return best;
        }
        Method simplest = candidates.get(0);
        throw new CommandException(
                "Field '" + field + "': cannot build " + typeName + ". Expected "
                        + describe(simplest) + ". Available factories: " + factoryNames(candidates)
                        + ". Supply the arguments by name, or use \"$factory\"/\"$args\".",
                "invalid_params");
    }

    private static Object[] factoryArgs(CreoContext ctx, Method factory, JsonObject spec, String field) {
        Class<?>[] types = factory.getParameterTypes();
        Object[] args = new Object[types.length];
        JsonArray positional = spec.getArray("$args");
        String[] names = ParamNames.forTypes(simpleNames(types));
        for (int i = 0; i < types.length; i++) {
            Object raw = positional != null ? positional.get(i) : spec.get(names[i]);
            args[i] = Marshal.in(ctx, raw, types[i].getSimpleName(), types[i],
                    field + "." + names[i]);
        }
        return args;
    }

    private static void applySetters(CreoContext ctx, Object instance, Class<?> type, String typeName,
            JsonObject spec, Set<String> alreadyUsed, String field) {
        Map<String, Method> setters = setters(type);
        for (String key : new ArrayList<>(spec.keys())) {
            if (alreadyUsed.contains(key)) {
                continue;
            }
            Method setter = setters.get(key.toLowerCase(java.util.Locale.ROOT));
            if (setter == null) {
                throw new CommandException(
                        "Field '" + field + "." + key + "' is not settable on " + typeName
                                + ". Settable: " + new ArrayList<>(settableNames(type)),
                        "invalid_params");
            }
            Class<?> pt = setter.getParameterTypes()[0];
            Object v = Marshal.in(ctx, spec.get(key), pt.getSimpleName(), pt, field + "." + key);
            try {
                setter.invoke(instance, v);
            } catch (ReflectiveOperationException e) {
                throw new CommandException(
                        "Creo rejected " + typeName + "." + setter.getName() + ": "
                                + Seqs.rootMessage(e), "invalid_params", e);
            }
        }
    }

    /** Renders an instruction object, or any other non-live J-Link struct, as a JSON object. */
    public static JsonObject toJson(CreoContext ctx, Object value, String typeName, int depth) {
        if (value == null) {
            return null;
        }
        JsonObject out = new JsonObject();
        for (Method m : value.getClass().getMethods()) {
            if (m.getParameterCount() != 0 || Modifier.isStatic(m.getModifiers())) {
                continue;
            }
            String n = m.getName();
            boolean getter = (n.startsWith("Get") && n.length() > 3)
                    || (n.startsWith("Is") && n.length() > 2)
                    || (n.startsWith("Has") && n.length() > 3);
            if (!getter || m.getReturnType() == void.class) {
                continue;
            }
            String key = n.startsWith("Get") ? n.substring(3) : n;
            try {
                // getMethods() resolves against value's *runtime* class, which for a pfcasync CIP
                // object is usually a non-public com.ptc.pfc.Implementation.* class implementing a
                // public interface. Invoking the Method taken from that non-public class fails with
                // IllegalAccessException even though the method itself is public; setAccessible(true)
                // is the standard way to invoke a public method reached through a non-public class.
                m.setAccessible(true);
                Object v = m.invoke(value);
                out.put(key, Marshal.out(ctx, v, m.getReturnType().getSimpleName(), depth + 1));
            } catch (ReflectiveOperationException | RuntimeException e) {
                // A getter that is invalid for this object's current state is normal in J-Link (an
                // unset optional field throws), so the result still gets a value for every declared
                // field rather than failing outright. But a null here is otherwise indistinguishable
                // from a field that is genuinely unset, which is exactly what hid the real cause the
                // first time this swallowed a marshalling bug -- so the reason travels alongside it.
                out.put(key, null);
                out.put(key + "Error", Seqs.rootMessage(e));
            }
        }
        return out;
    }

    private static List<Method> factories(Class<?> type, String typeName) {
        return FACTORIES.computeIfAbsent(type, t -> {
            List<Method> out = new ArrayList<>();
            Class<?> pkgClass = TypeRegistry.packageClassFor(typeName);
            if (pkgClass != null) {
                for (Method m : pkgClass.getMethods()) {
                    if (Modifier.isStatic(m.getModifiers()) && t.isAssignableFrom(m.getReturnType())) {
                        out.add(m);
                    }
                }
            }
            // Some sequence-like data types expose their own create().
            for (Method m : t.getMethods()) {
                if (Modifier.isStatic(m.getModifiers()) && t.isAssignableFrom(m.getReturnType())) {
                    out.add(m);
                }
            }
            String exact = t.getSimpleName() + "_Create";
            String alt = "Create" + t.getSimpleName();
            out.sort(Comparator
                    .comparingInt((Method m) -> m.getName().equals(exact) ? 0
                            : m.getName().equals(alt) ? 1 : 2)
                    .thenComparingInt(Method::getParameterCount)
                    .thenComparing(Method::getName));
            return out;
        });
    }

    private static Map<String, Method> setters(Class<?> type) {
        return SETTERS.computeIfAbsent(type, t -> {
            Map<String, Method> out = new ConcurrentHashMap<>();
            for (Method m : t.getMethods()) {
                if (m.getParameterCount() == 1 && m.getName().startsWith("Set")
                        && !Modifier.isStatic(m.getModifiers())) {
                    out.put(m.getName().substring(3).toLowerCase(java.util.Locale.ROOT), m);
                }
            }
            return out;
        });
    }

    private static Set<String> settableNames(Class<?> type) {
        Set<String> out = new LinkedHashSet<>();
        for (Method m : type.getMethods()) {
            if (m.getParameterCount() == 1 && m.getName().startsWith("Set")) {
                out.add(m.getName().substring(3));
            }
        }
        return out;
    }

    private static List<String> factoryNames(List<Method> candidates) {
        List<String> out = new ArrayList<>();
        for (Method m : candidates) {
            out.add(describe(m));
        }
        return out;
    }

    private static String describe(Method m) {
        StringBuilder sb = new StringBuilder(m.getName()).append('(');
        String[] names = ParamNames.forTypes(simpleNames(m.getParameterTypes()));
        Class<?>[] types = m.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(types[i].getSimpleName()).append(' ').append(names[i]);
        }
        return sb.append(')').toString();
    }

    static String[] simpleNames(Class<?>[] types) {
        String[] out = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            out[i] = types[i].getSimpleName();
        }
        return out;
    }
}
