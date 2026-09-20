package com.asyncjlink.config;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A small YAML reader covering the subset {@code paths.yaml} uses.
 *
 * <p>Deliberately not a full YAML implementation. AsyncJLink ships no third-party jars — Creo sites
 * are often air-gapped, and the only external artifact on the classpath should be PTC's own
 * {@code pfcasync.jar} — so this handles what a user-written paths file actually contains: nested
 * mappings by indentation, block sequences, scalars, quoting, comments and {@code null}.
 *
 * <p>Not supported, and reported rather than silently mis-read: flow collections ({@code {a: 1}},
 * {@code [1, 2]}), anchors, multi-document streams and block scalars ({@code |}, {@code >}).
 */
public final class Yaml {

    private Yaml() {
    }

    public static Map<String, Object> load(Path file) {
        try {
            return parse(new String(Files.readAllBytes(file), StandardCharsets.UTF_8), file.toString());
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + file, e);
        }
    }

    public static Map<String, Object> parse(String text, String origin) {
        Map<String, Object> root = new LinkedHashMap<>();
        Deque<Frame> stack = new ArrayDeque<>();
        stack.push(new Frame(-1, root));

        String[] lines = text.split("\r\n|\r|\n");
        for (int lineNo = 0; lineNo < lines.length; lineNo++) {
            String raw = lines[lineNo];
            String stripped = stripComment(raw);
            if (stripped.trim().isEmpty()) {
                continue;
            }
            if (stripped.trim().equals("---")) {
                continue;
            }
            int indent = indentOf(stripped);
            String body = stripped.trim();

            if (body.startsWith("- ") || body.equals("-")) {
                handleSequenceItem(stack, indent, body, origin, lineNo);
                continue;
            }

            int colon = splitKey(body);
            if (colon < 0) {
                throw new ConfigException(where(origin, lineNo)
                        + ": expected 'key: value' but found '" + body + "'");
            }
            String key = unquote(body.substring(0, colon).trim());
            String value = body.substring(colon + 1).trim();

            while (stack.size() > 1 && indent <= stack.peek().indent) {
                stack.pop();
            }
            Object container = stack.peek().node;
            if (!(container instanceof Map)) {
                throw new ConfigException(where(origin, lineNo)
                        + ": '" + key + "' appears inside a list item that is not a mapping");
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) container;

            if (value.isEmpty()) {
                // A nested block follows; whether it is a mapping or a sequence is decided by the
                // first child line, so start with a mapping and let a leading '-' replace it.
                Map<String, Object> child = new LinkedHashMap<>();
                map.put(key, child);
                stack.push(new Frame(indent, child, map, key));
            } else {
                map.put(key, scalar(value, origin, lineNo));
            }
        }
        return root;
    }

    private static void handleSequenceItem(Deque<Frame> stack, int indent, String body,
            String origin, int lineNo) {
        while (stack.size() > 1 && indent < stack.peek().indent) {
            stack.pop();
        }
        Frame frame = stack.peek();
        // The parent guessed "mapping" when it saw an empty value; a '-' proves it is a sequence.
        if (frame.node instanceof Map && ((Map<?, ?>) frame.node).isEmpty() && frame.parent != null) {
            List<Object> list = new ArrayList<>();
            frame.parent.put(frame.key, list);
            frame.node = list;
        }
        if (!(frame.node instanceof List)) {
            throw new ConfigException(where(origin, lineNo)
                    + ": list item has no parent key to attach to");
        }
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) frame.node;
        String item = body.equals("-") ? "" : body.substring(2).trim();
        if (item.isEmpty()) {
            throw new ConfigException(where(origin, lineNo)
                    + ": empty list items are not supported");
        }
        list.add(scalar(item, origin, lineNo));
    }

    /** Finds the ':' that separates key from value, ignoring any inside quotes. */
    private static int splitKey(String body) {
        boolean single = false;
        boolean dbl = false;
        for (int i = 0; i < body.length(); i++) {
            char c = body.charAt(i);
            if (c == '\'' && !dbl) {
                single = !single;
            } else if (c == '"' && !single) {
                dbl = !dbl;
            } else if (c == ':' && !single && !dbl) {
                if (i + 1 >= body.length() || body.charAt(i + 1) == ' ') {
                    return i;
                }
            }
        }
        return -1;
    }

    private static String stripComment(String line) {
        boolean single = false;
        boolean dbl = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\'' && !dbl) {
                single = !single;
            } else if (c == '"' && !single) {
                dbl = !dbl;
            } else if (c == '#' && !single && !dbl && (i == 0 || line.charAt(i - 1) == ' ')) {
                return line.substring(0, i);
            }
        }
        return line;
    }

    private static int indentOf(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }
        if (i < line.length() && line.charAt(i) == '\t') {
            throw new ConfigException("Tabs cannot be used for indentation in YAML");
        }
        return i;
    }

    private static Object scalar(String v, String origin, int lineNo) {
        if (v.startsWith("{") || v.startsWith("[")) {
            throw new ConfigException(where(origin, lineNo)
                    + ": flow collections are not supported; use an indented block instead");
        }
        if (v.equals("|") || v.equals(">")) {
            throw new ConfigException(where(origin, lineNo)
                    + ": block scalars are not supported");
        }
        if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
            return unescape(v.substring(1, v.length() - 1));
        }
        if (v.startsWith("'") && v.endsWith("'") && v.length() >= 2) {
            return v.substring(1, v.length() - 1).replace("''", "'");
        }
        if (v.equals("null") || v.equals("~") || v.equals("Null") || v.equals("NULL")) {
            return null;
        }
        if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes")) {
            return Boolean.TRUE;
        }
        if (v.equalsIgnoreCase("false") || v.equalsIgnoreCase("no")) {
            return Boolean.FALSE;
        }
        try {
            if (v.matches("[-+]?\\d+")) {
                return Long.valueOf(v);
            }
            if (v.matches("[-+]?(\\d+\\.\\d*|\\.\\d+)([eE][-+]?\\d+)?")) {
                return Double.valueOf(v);
            }
        } catch (NumberFormatException ignored) {
            // Out of range for the Java type; keep it as text rather than losing the value.
        }
        return v;
    }

    private static String unquote(String s) {
        if (s.length() >= 2
                && ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    /**
     * Applies double-quoted escapes.
     *
     * <p>This matters more than usual here: Windows paths are the main content of {@code paths.yaml},
     * and inside double quotes {@code "D:\\appli"} means {@code D:\appli}.
     */
    private static String unescape(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '\\' || i + 1 >= s.length()) {
                sb.append(c);
                continue;
            }
            char e = s.charAt(++i);
            switch (e) {
                case 'n':  sb.append('\n'); break;
                case 't':  sb.append('\t'); break;
                case 'r':  sb.append('\r'); break;
                case '0':  sb.append('\0'); break;
                case '\\': sb.append('\\'); break;
                case '"':  sb.append('"');  break;
                case '/':  sb.append('/');  break;
                case 'u':
                    if (i + 4 < s.length()) {
                        sb.append((char) Integer.parseInt(s.substring(i + 1, i + 5), 16));
                        i += 4;
                    }
                    break;
                default:
                    // Preserve an unrecognised escape verbatim; a lone backslash in a path is
                    // far more likely to be a typo than an intended control character.
                    sb.append('\\').append(e);
            }
        }
        return sb.toString();
    }

    private static String where(String origin, int lineNo) {
        return origin + ":" + (lineNo + 1);
    }

    private static final class Frame {
        final int indent;
        Object node;
        final Map<String, Object> parent;
        final String key;

        Frame(int indent, Object node) {
            this(indent, node, null, null);
        }

        Frame(int indent, Object node, Map<String, Object> parent, String key) {
            this.indent = indent;
            this.node = node;
            this.parent = parent;
            this.key = key;
        }
    }
}
