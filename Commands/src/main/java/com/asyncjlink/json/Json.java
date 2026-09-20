package com.asyncjlink.json;

import java.util.Collection;
import java.util.Map;

/**
 * Minimal, dependency-free JSON reader/writer.
 *
 * <p>AsyncJLink deliberately ships no third-party jars: Creo sites are frequently air-gapped and the
 * only external artifact on the classpath should be PTC's own {@code pfcasync.jar}. This class covers
 * the whole of RFC 8259 apart from features the MCP wire format never uses.
 */
public final class Json {

    private Json() {
    }

    // ---- normalisation ---------------------------------------------------

    /** Coerces an arbitrary Java value into the canonical JSON model. */
    static Object normalise(Object v) {
        if (v == null || v instanceof String || v instanceof Boolean
                || v instanceof JsonObject || v instanceof JsonArray) {
            return v;
        }
        if (v instanceof Double || v instanceof Float) {
            double d = ((Number) v).doubleValue();
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                // JSON has no encoding for these; surface them as strings rather than emitting
                // invalid output that the client would reject.
                return String.valueOf(d);
            }
            return d;
        }
        if (v instanceof Number) {
            return ((Number) v).longValue();
        }
        if (v instanceof Character) {
            return String.valueOf(v);
        }
        if (v instanceof Map) {
            JsonObject o = new JsonObject();
            for (Map.Entry<?, ?> e : ((Map<?, ?>) v).entrySet()) {
                o.put(String.valueOf(e.getKey()), e.getValue());
            }
            return o;
        }
        if (v instanceof Collection) {
            JsonArray a = new JsonArray();
            for (Object e : (Collection<?>) v) {
                a.add(e);
            }
            return a;
        }
        if (v.getClass().isArray()) {
            JsonArray a = new JsonArray();
            int n = java.lang.reflect.Array.getLength(v);
            for (int i = 0; i < n; i++) {
                a.add(java.lang.reflect.Array.get(v, i));
            }
            return a;
        }
        return String.valueOf(v);
    }

    // ---- writing ---------------------------------------------------------

    public static String write(Object value) {
        StringBuilder sb = new StringBuilder();
        writeValue(sb, normalise(value));
        return sb.toString();
    }

    public static String writePretty(Object value) {
        StringBuilder sb = new StringBuilder();
        writePretty(sb, normalise(value), 0);
        return sb.toString();
    }

    private static void writeValue(StringBuilder sb, Object v) {
        if (v == null) {
            sb.append("null");
        } else if (v instanceof String) {
            writeString(sb, (String) v);
        } else if (v instanceof Boolean || v instanceof Long) {
            sb.append(v);
        } else if (v instanceof Double) {
            writeDouble(sb, (Double) v);
        } else if (v instanceof JsonObject) {
            sb.append('{');
            boolean first = true;
            for (Map.Entry<String, Object> e : ((JsonObject) v).asMap().entrySet()) {
                if (!first) {
                    sb.append(',');
                }
                first = false;
                writeString(sb, e.getKey());
                sb.append(':');
                writeValue(sb, e.getValue());
            }
            sb.append('}');
        } else if (v instanceof JsonArray) {
            sb.append('[');
            boolean first = true;
            for (Object e : (JsonArray) v) {
                if (!first) {
                    sb.append(',');
                }
                first = false;
                writeValue(sb, e);
            }
            sb.append(']');
        } else {
            writeString(sb, String.valueOf(v));
        }
    }

    private static void writePretty(StringBuilder sb, Object v, int depth) {
        String pad = repeat(depth + 1);
        String padEnd = repeat(depth);
        if (v instanceof JsonObject) {
            JsonObject o = (JsonObject) v;
            if (o.isEmpty()) {
                sb.append("{}");
                return;
            }
            sb.append("{\n");
            boolean first = true;
            for (Map.Entry<String, Object> e : o.asMap().entrySet()) {
                if (!first) {
                    sb.append(",\n");
                }
                first = false;
                sb.append(pad);
                writeString(sb, e.getKey());
                sb.append(": ");
                writePretty(sb, e.getValue(), depth + 1);
            }
            sb.append('\n').append(padEnd).append('}');
        } else if (v instanceof JsonArray) {
            JsonArray a = (JsonArray) v;
            if (a.isEmpty()) {
                sb.append("[]");
                return;
            }
            sb.append("[\n");
            boolean first = true;
            for (Object e : a) {
                if (!first) {
                    sb.append(",\n");
                }
                first = false;
                sb.append(pad);
                writePretty(sb, e, depth + 1);
            }
            sb.append('\n').append(padEnd).append(']');
        } else {
            writeValue(sb, v);
        }
    }

    private static String repeat(int depth) {
        StringBuilder sb = new StringBuilder(depth * 2);
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    private static void writeDouble(StringBuilder sb, double d) {
        if (d == Math.rint(d) && !Double.isInfinite(d) && Math.abs(d) < 1e15) {
            sb.append((long) d);
        } else {
            sb.append(d);
        }
    }

    private static void writeString(StringBuilder sb, String s) {
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                case '\b': sb.append("\\b");  break;
                case '\f': sb.append("\\f");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append('"');
    }

    // ---- parsing ---------------------------------------------------------

    public static Object parse(String text) {
        Parser p = new Parser(text);
        p.skipWhitespace();
        Object v = p.value();
        p.skipWhitespace();
        if (!p.atEnd()) {
            throw new JsonException("Trailing content at offset " + p.pos);
        }
        return v;
    }

    public static JsonObject parseObject(String text) {
        Object v = parse(text);
        if (!(v instanceof JsonObject)) {
            throw new JsonException("Expected a JSON object");
        }
        return (JsonObject) v;
    }

    /** Thrown for malformed JSON input. */
    public static final class JsonException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public JsonException(String message) {
            super(message);
        }
    }

    private static final class Parser {
        private final String s;
        private int pos;

        Parser(String s) {
            this.s = s;
        }

        boolean atEnd() {
            return pos >= s.length();
        }

        void skipWhitespace() {
            while (pos < s.length()) {
                char c = s.charAt(pos);
                if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                    pos++;
                } else {
                    break;
                }
            }
        }

        Object value() {
            if (atEnd()) {
                throw new JsonException("Unexpected end of input");
            }
            char c = s.charAt(pos);
            switch (c) {
                case '{': return object();
                case '[': return array();
                case '"': return string();
                case 't': expect("true");  return Boolean.TRUE;
                case 'f': expect("false"); return Boolean.FALSE;
                case 'n': expect("null");  return null;
                default:  return number();
            }
        }

        private void expect(String literal) {
            if (!s.startsWith(literal, pos)) {
                throw new JsonException("Expected '" + literal + "' at offset " + pos);
            }
            pos += literal.length();
        }

        private JsonObject object() {
            JsonObject o = new JsonObject();
            pos++; // '{'
            skipWhitespace();
            if (!atEnd() && s.charAt(pos) == '}') {
                pos++;
                return o;
            }
            while (true) {
                skipWhitespace();
                if (atEnd() || s.charAt(pos) != '"') {
                    throw new JsonException("Expected object key at offset " + pos);
                }
                String key = string();
                skipWhitespace();
                if (atEnd() || s.charAt(pos) != ':') {
                    throw new JsonException("Expected ':' at offset " + pos);
                }
                pos++;
                skipWhitespace();
                o.put(key, value());
                skipWhitespace();
                if (atEnd()) {
                    throw new JsonException("Unterminated object");
                }
                char c = s.charAt(pos++);
                if (c == '}') {
                    return o;
                }
                if (c != ',') {
                    throw new JsonException("Expected ',' or '}' at offset " + (pos - 1));
                }
            }
        }

        private JsonArray array() {
            JsonArray a = new JsonArray();
            pos++; // '['
            skipWhitespace();
            if (!atEnd() && s.charAt(pos) == ']') {
                pos++;
                return a;
            }
            while (true) {
                skipWhitespace();
                a.add(value());
                skipWhitespace();
                if (atEnd()) {
                    throw new JsonException("Unterminated array");
                }
                char c = s.charAt(pos++);
                if (c == ']') {
                    return a;
                }
                if (c != ',') {
                    throw new JsonException("Expected ',' or ']' at offset " + (pos - 1));
                }
            }
        }

        private String string() {
            pos++; // opening quote
            StringBuilder sb = new StringBuilder();
            while (true) {
                if (atEnd()) {
                    throw new JsonException("Unterminated string");
                }
                char c = s.charAt(pos++);
                if (c == '"') {
                    return sb.toString();
                }
                if (c != '\\') {
                    sb.append(c);
                    continue;
                }
                if (atEnd()) {
                    throw new JsonException("Unterminated escape");
                }
                char e = s.charAt(pos++);
                switch (e) {
                    case '"':  sb.append('"');  break;
                    case '\\': sb.append('\\'); break;
                    case '/':  sb.append('/');  break;
                    case 'b':  sb.append('\b'); break;
                    case 'f':  sb.append('\f'); break;
                    case 'n':  sb.append('\n'); break;
                    case 'r':  sb.append('\r'); break;
                    case 't':  sb.append('\t'); break;
                    case 'u':
                        if (pos + 4 > s.length()) {
                            throw new JsonException("Truncated \\u escape");
                        }
                        sb.append((char) Integer.parseInt(s.substring(pos, pos + 4), 16));
                        pos += 4;
                        break;
                    default:
                        throw new JsonException("Invalid escape '\\" + e + "' at offset " + (pos - 1));
                }
            }
        }

        private Object number() {
            int start = pos;
            if (!atEnd() && (s.charAt(pos) == '-' || s.charAt(pos) == '+')) {
                pos++;
            }
            boolean integral = true;
            while (!atEnd()) {
                char c = s.charAt(pos);
                if (c >= '0' && c <= '9') {
                    pos++;
                } else if (c == '.' || c == 'e' || c == 'E' || c == '+' || c == '-') {
                    integral = false;
                    pos++;
                } else {
                    break;
                }
            }
            String raw = s.substring(start, pos);
            if (raw.isEmpty()) {
                throw new JsonException("Expected a value at offset " + start);
            }
            try {
                if (integral) {
                    return Long.valueOf(raw);
                }
                return Double.valueOf(raw);
            } catch (NumberFormatException ex) {
                throw new JsonException("Malformed number '" + raw + "' at offset " + start);
            }
        }
    }
}
