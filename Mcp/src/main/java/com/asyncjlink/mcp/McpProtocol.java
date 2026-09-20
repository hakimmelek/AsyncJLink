package com.asyncjlink.mcp;

import com.asyncjlink.json.Json;
import com.asyncjlink.json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * MCP stdio framing and JSON-RPC 2.0 message handling.
 *
 * <p>The stdio transport is newline-delimited JSON — one complete message per line, UTF-8, with no
 * embedded newlines. That is why {@link Json#write} is used for output rather than
 * {@code writePretty}, and why every diagnostic in this module goes to stderr: stdout carries the
 * protocol and nothing else.
 */
public final class McpProtocol implements AutoCloseable {

    /** Protocol revisions this server implements. The newest is offered when a client asks for one we do not know. */
    static final String[] SUPPORTED_VERSIONS = {"2025-06-18", "2025-03-26", "2024-11-05"};
    static final String PREFERRED_VERSION = SUPPORTED_VERSIONS[0];

    // JSON-RPC 2.0 error codes.
    public static final int PARSE_ERROR = -32700;
    public static final int INVALID_REQUEST = -32600;
    public static final int METHOD_NOT_FOUND = -32601;
    public static final int INVALID_PARAMS = -32602;
    public static final int INTERNAL_ERROR = -32603;

    private final BufferedReader in;
    private final PrintStream out;
    private final PrintStream log;

    public McpProtocol(InputStream in, OutputStream out, PrintStream log) {
        this.in = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            this.out = new PrintStream(out, true, StandardCharsets.UTF_8.name());
        } catch (java.io.UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 is required", e);
        }
        this.log = log;
    }

    /** Reads the next message, or {@code null} at end of stream. Blank lines are skipped. */
    public JsonObject read() throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                Object parsed = Json.parse(line);
                if (parsed instanceof JsonObject) {
                    return (JsonObject) parsed;
                }
                // A JSON-RPC batch is an array; MCP does not use them, and answering per-element
                // would be guesswork, so it is rejected explicitly rather than ignored.
                writeError(null, INVALID_REQUEST, "Batch requests are not supported");
            } catch (Json.JsonException e) {
                writeError(null, PARSE_ERROR, "Invalid JSON: " + e.getMessage());
            }
        }
        return null;
    }

    public void writeResult(Object id, JsonObject result) {
        JsonObject msg = JsonObject.of("jsonrpc", "2.0");
        msg.put("id", id);
        msg.put("result", result == null ? new JsonObject() : result);
        send(msg);
    }

    public void writeError(Object id, int code, String message) {
        JsonObject msg = JsonObject.of("jsonrpc", "2.0");
        msg.put("id", id);
        msg.put("error", JsonObject.of("code", (long) code, "message", message));
        send(msg);
    }

    private void send(JsonObject msg) {
        String line = Json.write(msg);
        synchronized (out) {
            out.print(line);
            out.print('\n');
            out.flush();
        }
    }

    /** Diagnostics. Always stderr — stdout belongs to the protocol. */
    public void log(String message) {
        if (log != null) {
            log.println("[asyncjlink] " + message);
            log.flush();
        }
    }

    static String negotiateVersion(String requested) {
        if (requested != null) {
            for (String v : SUPPORTED_VERSIONS) {
                if (v.equals(requested)) {
                    return v;
                }
            }
        }
        return PREFERRED_VERSION;
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException ignored) {
            // Closing on the way out; nothing left to report to.
        }
        out.flush();
    }
}
