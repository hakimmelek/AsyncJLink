package com.asyncjlink.commands;

import com.asyncjlink.commands.compositecommands.CompositeCommandIndex;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.ptc.cipjava.jxthrowable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The name-to-{@link Command} map — the single vocabulary shared by {@code Cli} and {@code Mcp}.
 *
 * <p>The index is generated rather than discovered by scanning the classpath: the front doors ship as
 * shaded jars, where directory scanning is unreliable, and a generated index fails at build time
 * instead of silently yielding an empty catalogue. {@code RawCommandIndex} is written by
 * {@code tools/generate_commands.py} alongside the commands themselves, so the two cannot drift.
 */
public final class CommandRegistry {

    private final Map<String, Command> byName = new TreeMap<>();
    private final Map<String, Command> byToolName = new TreeMap<>();

    private static CommandRegistry instance;

    private CommandRegistry() {
    }

    /**
     * Builds (once) the registry of every command, generated and hand-written.
     *
     * <p>Raw commands go in first, so a composite that accidentally reuses a J-Link name is rejected
     * by {@link #add} as a duplicate rather than silently shadowing the real operation. Since the raw
     * layer is regenerated from PTC's dictionary, that turns a future Creo release introducing, say,
     * a real {@code Assembly.GetBOM} into a loud failure at startup.
     */
    public static synchronized CommandRegistry load() {
        if (instance == null) {
            CommandRegistry r = new CommandRegistry();
            for (Command c : RawCommandIndex.all()) {
                r.add(c);
            }
            for (Command c : CompositeCommandIndex.all()) {
                r.add(c);
            }
            instance = r;
        }
        return instance;
    }

    void add(Command command) {
        Command clash = byName.put(command.name(), command);
        if (clash != null) {
            throw new IllegalStateException("Duplicate command name: " + command.name());
        }
        byToolName.put(command.toolName(), command);
    }

    /** Looks a command up by {@code Receiver.Method} or by its {@code Receiver_Method} tool name. */
    public Command get(String name) {
        if (name == null) {
            return null;
        }
        Command c = byName.get(name);
        return c != null ? c : byToolName.get(name);
    }

    public Command require(String name) {
        Command c = get(name);
        if (c == null) {
            throw new CommandException(
                    "Unknown command '" + name + "'. " + suggest(name), "unknown_command");
        }
        return c;
    }

    public Collection<Command> all() {
        return byName.values();
    }

    public int size() {
        return byName.size();
    }

    public List<String> names() {
        return new ArrayList<>(byName.keySet());
    }

    /** Commands grouped by their J-Link package, for {@code --help} and tool listings. */
    public Map<String, List<Command>> byPackage() {
        Map<String, List<Command>> out = new TreeMap<>();
        for (Command c : byName.values()) {
            out.computeIfAbsent(c.jlinkPackage(), k -> new ArrayList<>()).add(c);
        }
        return out;
    }

    public List<Command> search(String term) {
        List<Command> out = new ArrayList<>();
        if (term == null || term.isEmpty()) {
            return out;
        }
        String t = term.toLowerCase(java.util.Locale.ROOT);
        for (Command c : byName.values()) {
            if (c.name().toLowerCase(java.util.Locale.ROOT).contains(t)
                    || c.jlinkPackage().toLowerCase(java.util.Locale.ROOT).contains(t)) {
                out.add(c);
            }
        }
        return out;
    }

    /**
     * Runs a command and returns a result envelope, never throwing.
     *
     * <p>This is the only entry point the front doors use, which is what keeps {@code jxthrowable}
     * — and therefore {@code pfcasync.jar} — out of {@code Cli} and {@code Mcp} entirely.
     *
     * @return {@code {"ok":true, ...}} on success, or {@code {"ok":false,"error":{...}}} on failure
     */
    public JsonObject invoke(CreoContext ctx, String name, JsonObject params) {
        JsonObject args = params == null ? new JsonObject() : params;
        Command command;
        try {
            command = require(name);
        } catch (CommandException e) {
            return error(e.code(), e.getMessage(), null);
        }
        if (!command.isInvocable()) {
            return error("unsupported", command.name() + " cannot be invoked: "
                    + command.unsupportedReason(), null);
        }
        try {
            JsonObject result = command.execute(ctx, args);
            JsonObject out = JsonObject.of("ok", Boolean.TRUE, "command", command.name());
            for (String k : result.keys()) {
                out.put(k, result.get(k));
            }
            return out;
        } catch (CommandException e) {
            return error(e.code(), e.getMessage(), command.name());
        } catch (jxthrowable e) {
            // Creo refused the call. Its own message is the useful part.
            return error("creo_error", creoMessage(e), command.name());
        } catch (RuntimeException e) {
            return error("internal", e.getClass().getSimpleName() + ": " + e.getMessage(),
                    command.name());
        }
    }

    private static String creoMessage(jxthrowable e) {
        String m = e.getMessage();
        String type = e.getClass().getSimpleName();
        return m == null || m.isEmpty() ? type : type + ": " + m;
    }

    private static JsonObject error(String code, String message, String command) {
        JsonObject err = JsonObject.of("code", code, "message", message);
        JsonObject out = JsonObject.of("ok", Boolean.FALSE);
        out.putIfPresent("command", command);
        out.put("error", err);
        return out;
    }

    private String suggest(String name) {
        List<Command> near = new ArrayList<>();
        String t = name.toLowerCase(java.util.Locale.ROOT);
        int dot = t.lastIndexOf('.');
        String tail = dot >= 0 ? t.substring(dot + 1) : t;
        for (Command c : byName.values()) {
            if (c.methodName().toLowerCase(java.util.Locale.ROOT).equals(tail)) {
                near.add(c);
            }
            if (near.size() >= 5) {
                break;
            }
        }
        if (near.isEmpty()) {
            return "Run with --list to see all " + byName.size() + " commands.";
        }
        StringBuilder sb = new StringBuilder("Did you mean: ");
        for (int i = 0; i < near.size(); i++) {
            sb.append(i > 0 ? ", " : "").append(near.get(i).name());
        }
        return sb.append('?').toString();
    }

    /** The whole catalogue as JSON; {@code Mcp.ToolCatalog} and {@code creoctl --list} both use it. */
    public JsonObject describeAll() {
        JsonArray arr = new JsonArray();
        for (Command c : byName.values()) {
            arr.add(describe(c));
        }
        return JsonObject.of("count", byName.size(), "commands", arr);
    }

    public JsonObject describe(Command c) {
        JsonObject o = new JsonObject();
        o.put("name", c.name());
        o.put("tool", c.toolName());
        o.put("package", c.jlinkPackage());
        o.put("receiver", c.receiverType());
        o.put("signature", c.signature());
        o.put("inputSchema", c.paramSchema().toJson());
        if (!c.isInvocable()) {
            o.put("invocable", Boolean.FALSE);
            o.put("unsupportedReason", c.unsupportedReason());
        }
        return o;
    }

    /** Package-visible hook for tests that need an isolated registry. */
    static CommandRegistry of(Collection<Command> commands) {
        CommandRegistry r = new CommandRegistry();
        for (Command c : commands) {
            r.add(c);
        }
        return r;
    }

    /** Diagnostic counts used by {@code creoctl --stats}. */
    public JsonObject stats() {
        Map<String, Integer> perPackage = new LinkedHashMap<>();
        int invocable = 0;
        for (Command c : byName.values()) {
            perPackage.merge(c.jlinkPackage(), 1, Integer::sum);
            if (c.isInvocable()) {
                invocable++;
            }
        }
        return JsonObject.of(
                "total", byName.size(),
                "invocable", invocable,
                "requiresListener", byName.size() - invocable,
                "packages", perPackage);
    }
}
