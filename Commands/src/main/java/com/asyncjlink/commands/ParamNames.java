package com.asyncjlink.commands;

/**
 * Derives parameter names from parameter types.
 *
 * <p>{@code pfcasync.jar} is compiled without {@code MethodParameters}, so {@code javap} — and
 * therefore the dictionary this project generates from — records parameter <em>types</em> but no
 * names. Names are synthesised from the type instead: {@code ExportInstructions} becomes
 * {@code exportInstructions}, and repeats within one signature get a 1-based suffix
 * ({@code model1}, {@code model2}).
 *
 * <p>The identical algorithm is implemented in {@code tools/generate_commands.py}. The generator
 * uses it to name schema fields; this class uses it at runtime to match JSON keys onto the arguments
 * of a {@code pfc*} factory. The two must agree, which is why the rule is kept deliberately trivial.
 *
 * <p>Both are pinned to one fixture, {@code paramname-cases.json}: {@code tools/test_generator.py}
 * checks the Python side against it and {@link SelfCheck} ({@code creoctl --selfcheck}) checks this
 * one, so neither can drift unnoticed.
 */
public final class ParamNames {

    private ParamNames() {
    }

    /** Names one argument list. Returns an array the same length as {@code typeNames}. */
    public static String[] forTypes(String[] typeNames) {
        String[] base = new String[typeNames.length];
        java.util.Map<String, Integer> total = new java.util.HashMap<>();
        for (int i = 0; i < typeNames.length; i++) {
            base[i] = single(typeNames[i]);
            total.merge(base[i], 1, Integer::sum);
        }
        // Only disambiguate the names that actually repeat, so single-argument calls keep the clean
        // form the schema advertises: `model`, not `model1`.
        String[] out = new String[typeNames.length];
        java.util.Map<String, Integer> seen = new java.util.HashMap<>();
        for (int i = 0; i < typeNames.length; i++) {
            if (total.get(base[i]) > 1) {
                int n = seen.merge(base[i], 1, Integer::sum);
                out[i] = base[i] + n;
            } else {
                out[i] = base[i];
            }
        }
        return out;
    }

    /** Names a single type, ignoring collisions. */
    public static String single(String typeName) {
        String t = typeName == null ? "" : typeName.trim();
        int dot = t.lastIndexOf('.');
        if (dot >= 0) {
            t = t.substring(dot + 1);
        }
        t = t.replace("[]", "");
        if (t.isEmpty()) {
            return "arg";
        }
        switch (t) {
            case "String":   return "value";
            case "int":
            case "Integer":  return "value";
            case "double":
            case "Double":   return "value";
            case "boolean":
            case "Boolean":  return "flag";
            case "stringseq": return "values";
            case "intseq":    return "values";
            case "realseq":   return "values";
            default:
                return decapitalise(t);
        }
    }

    /** {@code ExportInstructions} to {@code exportInstructions}; {@code UVParams} to {@code uvParams}. */
    static String decapitalise(String s) {
        if (s.isEmpty()) {
            return s;
        }
        int upper = 0;
        while (upper < s.length() && Character.isUpperCase(s.charAt(upper))) {
            upper++;
        }
        if (upper == 0) {
            return s;
        }
        if (upper == s.length()) {
            // All caps, e.g. "OId" is not this case but "UDF" would be.
            return s.toLowerCase(java.util.Locale.ROOT);
        }
        if (upper == 1) {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
        // Leading acronym: lowercase all but the last capital, which starts the next word.
        return s.substring(0, upper - 1).toLowerCase(java.util.Locale.ROOT) + s.substring(upper - 1);
    }
}
