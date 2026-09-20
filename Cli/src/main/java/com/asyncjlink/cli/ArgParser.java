package com.asyncjlink.cli;

import com.asyncjlink.json.Json;
import com.asyncjlink.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns {@code creoctl Solid.GetMassProperty --target BRACKET_01.prt} into a command name and a
 * parameter object.
 *
 * <p>Values are typed the way a shell user expects: {@code true}/{@code false} become booleans, bare
 * numbers become numbers, anything starting with {@code [} or <code>{</code> is parsed as JSON, and
 * everything else stays a string. {@code --raw '{...}'} bypasses all of that and supplies the whole
 * parameter object verbatim, which is what scripts should use.
 */
public final class ArgParser {

    private ArgParser() {
    }

    /** A parsed command line. */
    public static final class Parsed {
        public String command;
        public JsonObject params = new JsonObject();
        public boolean help;
        public boolean list;
        public boolean stats;
        public boolean pretty = true;
        public boolean describe;
        public boolean selfcheck;
        public String search;
        public String configPath;
        public final List<String> errors = new ArrayList<>();
    }

    public static Parsed parse(String[] args) {
        Parsed p = new Parsed();
        List<String> positional = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            if (!a.startsWith("--")) {
                positional.add(a);
                continue;
            }
            switch (a) {
                case "--help":
                case "-h":
                    p.help = true;
                    continue;
                case "--list":
                    p.list = true;
                    continue;
                case "--stats":
                    p.stats = true;
                    continue;
                case "--describe":
                    p.describe = true;
                    continue;
                case "--selfcheck":
                    p.selfcheck = true;
                    continue;
                case "--compact":
                    p.pretty = false;
                    continue;
                default:
                    break;
            }

            // --key=value or --key value
            String key;
            String value;
            int eq = a.indexOf('=');
            if (eq > 0) {
                key = a.substring(2, eq);
                value = a.substring(eq + 1);
            } else {
                key = a.substring(2);
                if (i + 1 >= args.length || args[i + 1].startsWith("--")) {
                    // A bare --flag is a boolean true; that is how J-Link's boolean params read.
                    value = "true";
                } else {
                    value = args[++i];
                }
            }

            switch (key) {
                case "search":
                    p.search = value;
                    break;
                case "config":
                    p.configPath = value;
                    break;
                case "raw":
                    applyRaw(p, value);
                    break;
                default:
                    p.params.put(key, coerce(value));
                    break;
            }
        }

        if (!positional.isEmpty()) {
            p.command = positional.get(0);
            for (int i = 1; i < positional.size(); i++) {
                p.errors.add("Unexpected argument '" + positional.get(i)
                        + "'. Parameters are passed as --name value.");
            }
        }
        return p;
    }

    private static void applyRaw(Parsed p, String value) {
        try {
            Object parsed = Json.parse(value);
            if (!(parsed instanceof JsonObject)) {
                p.errors.add("--raw must be a JSON object");
                return;
            }
            JsonObject o = (JsonObject) parsed;
            for (String k : o.keys()) {
                p.params.put(k, o.get(k));
            }
        } catch (RuntimeException e) {
            p.errors.add("--raw is not valid JSON: " + e.getMessage());
        }
    }

    /** Gives a shell token the JSON type it obviously has. */
    static Object coerce(String v) {
        if (v == null) {
            return null;
        }
        if (v.equals("true")) {
            return Boolean.TRUE;
        }
        if (v.equals("false")) {
            return Boolean.FALSE;
        }
        if (v.equals("null")) {
            return null;
        }
        String t = v.trim();
        if (t.startsWith("{") || t.startsWith("[")) {
            try {
                return Json.parse(t);
            } catch (RuntimeException e) {
                return v; // not JSON after all; a Windows path can start with neither, so this is rare
            }
        }
        if (t.matches("[-+]?\\d+")) {
            try {
                return Long.valueOf(t);
            } catch (NumberFormatException e) {
                return v;
            }
        }
        if (t.matches("[-+]?\\d*\\.\\d+([eE][-+]?\\d+)?")) {
            return Double.valueOf(t);
        }
        return v;
    }
}
