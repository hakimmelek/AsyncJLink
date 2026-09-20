package com.asyncjlink.commands;

import com.asyncjlink.commands.compositecommands.CompositeCommandIndex;
import com.asyncjlink.json.Json;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Verifies the command layer without touching Creo.
 *
 * <p>Answers the question a new installation actually raises — "is this thing wired up correctly?" —
 * before anyone tries to connect. It instantiates all generated commands, builds every schema, and
 * re-checks the parameter-name algorithm against the shared fixture. None of that needs a running
 * Creo, though it does need {@code pfcasync.jar} on the classpath, since the generated classes
 * reference PTC types.
 *
 * <p>Run it with {@code creoctl --selfcheck}.
 */
public final class SelfCheck {

    private SelfCheck() {
    }

    private static final String CASES_RESOURCE = "/com/asyncjlink/commands/paramname-cases.json";

    /** The outcome of a run. */
    public static final class Result {
        public final List<String> failures = new ArrayList<>();
        public int checks;
        public int commands;

        public boolean ok() {
            return failures.isEmpty();
        }
    }

    public static Result run() {
        Result r = new Result();
        checkParamNames(r);
        checkRegistry(r);
        return r;
    }

    // ---- parameter naming ------------------------------------------------

    private static void checkParamNames(Result r) {
        JsonObject cases;
        try {
            cases = Json.parseObject(readResource(CASES_RESOURCE));
        } catch (IOException | RuntimeException e) {
            r.failures.add("cannot read " + CASES_RESOURCE + ": " + e);
            return;
        }

        JsonObject single = cases.getObject("single");
        if (single != null) {
            for (String type : single.keys()) {
                String expected = single.getString(type, null);
                String actual = ParamNames.single(type);
                r.checks++;
                if (!actual.equals(expected)) {
                    r.failures.add("ParamNames.single(\"" + type + "\") was '" + actual
                            + "', fixture says '" + expected + "'");
                }
            }
        }

        JsonArray lists = cases.getArray("lists");
        if (lists != null) {
            for (Object entry : lists) {
                JsonObject c = (JsonObject) entry;
                String[] types = toStringArray(c.getArray("types"));
                String[] expected = toStringArray(c.getArray("names"));
                String[] actual = ParamNames.forTypes(types);
                r.checks++;
                if (!java.util.Arrays.equals(actual, expected)) {
                    r.failures.add("ParamNames.forTypes(" + java.util.Arrays.toString(types)
                            + ") was " + java.util.Arrays.toString(actual)
                            + ", fixture says " + java.util.Arrays.toString(expected));
                }
            }
        }
    }

    private static String[] toStringArray(JsonArray a) {
        if (a == null) {
            return new String[0];
        }
        String[] out = new String[a.size()];
        for (int i = 0; i < a.size(); i++) {
            out[i] = String.valueOf(a.get(i));
        }
        return out;
    }

    private static String readResource(String path) throws IOException {
        try (InputStream in = SelfCheck.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IOException("resource not on the classpath");
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) > 0) {
                out.write(buf, 0, n);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    // ---- the catalogue ---------------------------------------------------

    private static void checkRegistry(Result r) {
        CommandRegistry registry;
        try {
            registry = CommandRegistry.load();
        } catch (Throwable t) {
            r.failures.add("the command registry did not load: " + t);
            return;
        }
        r.commands = registry.size();
        r.checks++;
        // The registry holds the generated commands plus the hand-written composites. A shortfall
        // means two commands claimed the same name — most likely a composite shadowing a raw one,
        // which matters because the raw layer is regenerated from PTC's dictionary and can grow.
        int expected = RawCommandIndex.COUNT + CompositeCommandIndex.count();
        if (registry.size() != expected) {
            r.failures.add("registry holds " + registry.size() + " commands but expected " + expected
                    + " (" + RawCommandIndex.COUNT + " raw + " + CompositeCommandIndex.count()
                    + " composite); a duplicate name would cause this");
        }

        Set<String> toolNames = new HashSet<>();
        for (Command c : registry.all()) {
            r.checks++;
            try {
                // Building the schema is where a bad enum reference or a missing type would surface.
                JsonObject schema = c.paramSchema().toJson();
                if (schema.getObject("properties") == null) {
                    r.failures.add(c.name() + ": schema has no properties object");
                }
                Json.write(schema);
            } catch (Throwable t) {
                r.failures.add(c.name() + ": schema failed to build: " + t);
                continue;
            }
            String tool = c.toolName();
            if (!tool.matches("[A-Za-z0-9_-]{1,64}")) {
                r.failures.add(c.name() + ": tool name '" + tool + "' is not a legal MCP tool name");
            }
            if (!toolNames.add(tool)) {
                r.failures.add("duplicate MCP tool name: " + tool);
            }
            if (registry.get(c.name()) != c) {
                r.failures.add(c.name() + ": not resolvable by its own name");
            }
        }
    }

    /** Renders the result for the console. */
    public static String format(Result r) {
        StringBuilder sb = new StringBuilder();
        sb.append("commands loaded : ").append(r.commands).append('\n');
        sb.append("checks run      : ").append(r.checks).append('\n');
        if (r.ok()) {
            sb.append("result          : OK");
            return sb.toString();
        }
        sb.append("result          : ").append(r.failures.size()).append(" failure(s)\n");
        int shown = Math.min(r.failures.size(), 25);
        for (int i = 0; i < shown; i++) {
            sb.append("  - ").append(r.failures.get(i)).append('\n');
        }
        if (r.failures.size() > shown) {
            sb.append("  ... and ").append(r.failures.size() - shown).append(" more");
        }
        return sb.toString().trim();
    }
}
