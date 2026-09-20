package com.asyncjlink.mcp;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandRegistry;
import com.asyncjlink.commands.CreoConnection;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.config.Bootstrap;
import com.asyncjlink.config.PathConfig;
import com.asyncjlink.json.Json;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;

import java.io.IOException;

/**
 * The AI-agent-facing front door: an MCP server over stdin/stdout.
 *
 * <p>Reads {@code paths.yaml}, opens one direct Creo connection, and serves MCP JSON-RPC for the life
 * of the AI session. Like {@code Cli} it never imports {@code pfcasync.jar} — every call goes through
 * {@link CommandRegistry#invoke}.
 *
 * <p>The Creo connection is opened lazily, on the first {@code tools/call}. That matters because an AI
 * client typically launches its MCP servers when it starts, long before the user asks for anything:
 * connecting eagerly would fail on every launch where Creo is not yet running, and the server would
 * be dead for the rest of the session.
 */
public final class McpServer {

    private static final String SERVER_NAME = "creo-jlink";
    private static final String SERVER_VERSION = "1.0.0";

    private final McpProtocol protocol;
    private final CommandRegistry registry;
    private final ToolCatalog catalog;
    private final PathConfig config;

    private CreoConnection connection;
    private boolean initialised;

    McpServer(McpProtocol protocol, CommandRegistry registry, PathConfig config) {
        this.protocol = protocol;
        this.registry = registry;
        this.config = config;
        this.catalog = new ToolCatalog(registry);
    }

    public static void main(String[] args) {
        PathConfig config;
        try {
            config = PathConfig.load();
            Bootstrap.ensureEnvironment(config, McpServer.class, args);
        } catch (RuntimeException e) {
            // stderr only: stdout is the protocol channel and must stay clean even on failure.
            System.err.println("[asyncjlink] configuration error: " + e.getMessage());
            System.exit(3);
            return;
        }

        McpProtocol protocol = new McpProtocol(System.in, System.out, System.err);
        CommandRegistry registry = CommandRegistry.load();
        McpServer server = new McpServer(protocol, registry, config);
        protocol.log("serving " + registry.size() + " Creo commands as MCP tools from "
                + config.origin());
        try {
            server.serve();
        } catch (IOException e) {
            protocol.log("transport closed: " + e.getMessage());
        } finally {
            server.shutdown();
        }
    }

    void serve() throws IOException {
        JsonObject message;
        while ((message = protocol.read()) != null) {
            dispatch(message);
        }
    }

    private void dispatch(JsonObject message) {
        Object id = message.get("id");
        String method = message.getString("method", null);
        if (method == null) {
            // A response or a malformed frame. Responses to a server that sends no requests are
            // simply ignored, as the spec requires.
            return;
        }
        boolean isNotification = !message.has("id");

        try {
            switch (method) {
                case "initialize":
                    protocol.writeResult(id, initialize(message.getObject("params")));
                    return;
                case "notifications/initialized":
                    initialised = true;
                    return;
                case "ping":
                    protocol.writeResult(id, new JsonObject());
                    return;
                case "tools/list":
                    warnIfNotInitialised(method);
                    protocol.writeResult(id, toolsList(message.getObject("params")));
                    return;
                case "tools/call":
                    warnIfNotInitialised(method);
                    protocol.writeResult(id, toolsCall(message.getObject("params")));
                    return;
                default:
                    if (isNotification) {
                        return; // unknown notifications are ignored, not answered
                    }
                    protocol.writeError(id, McpProtocol.METHOD_NOT_FOUND,
                            "Method not found: " + method);
            }
        } catch (McpError e) {
            protocol.writeError(id, e.code, e.getMessage());
        } catch (RuntimeException e) {
            protocol.log("internal error handling " + method + ": " + e);
            if (!isNotification) {
                protocol.writeError(id, McpProtocol.INTERNAL_ERROR,
                        e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    private JsonObject initialize(JsonObject params) {
        String requested = params == null ? null : params.getString("protocolVersion", null);
        return JsonObject.of(
                "protocolVersion", McpProtocol.negotiateVersion(requested),
                "capabilities", JsonObject.of("tools", JsonObject.of("listChanged", Boolean.FALSE)),
                "serverInfo", JsonObject.of("name", SERVER_NAME, "version", SERVER_VERSION),
                "instructions",
                "Drives Creo Parametric through asynchronous J-Link. Each tool is one J-Link "
                        + "operation named Receiver_Method. Pass the object to act on as `target`: "
                        + "either a handle string returned by an earlier tool (for example "
                        + "\"Part@3\"), or, for models, a file name such as \"bracket_01.prt\". "
                        + "Results wrap live objects as {\"$handle\":...}; feed that handle back in "
                        + "to continue working with the same object. Handles last only for this "
                        + "session. Start from Session_GetCurrentModel or Session_ListModels.");
    }

    private JsonObject toolsList(JsonObject params) {
        String cursor = params == null ? null : params.getString("cursor", null);
        return catalog.list(cursor);
    }

    private JsonObject toolsCall(JsonObject params) {
        if (params == null) {
            throw new McpError(McpProtocol.INVALID_PARAMS, "tools/call requires params");
        }
        String name = params.getString("name", null);
        if (name == null) {
            throw new McpError(McpProtocol.INVALID_PARAMS, "tools/call requires a tool name");
        }
        Command command = registry.get(name);
        if (command == null) {
            throw new McpError(McpProtocol.INVALID_PARAMS, "Unknown tool: " + name);
        }
        JsonObject arguments = params.getObject("arguments");
        if (arguments == null) {
            arguments = new JsonObject();
        }

        CreoContext ctx;
        try {
            ctx = context();
        } catch (Exception e) {
            // Creo not running is an expected, recoverable situation, not a protocol failure: report
            // it as a tool error so the agent can tell the user to start Creo and try again.
            return toolError("Cannot reach Creo: "
                    + (e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage())
                    + ". Check that Creo Parametric is running and that PTCNMSPORT matches "
                    + config.nmsPort() + ".");
        }

        JsonObject result = registry.invoke(ctx, command.name(), arguments);
        boolean ok = result.getBoolean("ok", false);
        return content(Json.writePretty(result), !ok);
    }

    private JsonObject content(String text, boolean isError) {
        JsonArray content = JsonArray.of(JsonObject.of("type", "text", "text", text));
        JsonObject out = JsonObject.of("content", content);
        if (isError) {
            out.put("isError", Boolean.TRUE);
        }
        return out;
    }

    private JsonObject toolError(String message) {
        return content(Json.writePretty(JsonObject.of(
                "ok", Boolean.FALSE,
                "error", JsonObject.of("code", "no_connection", "message", message))), true);
    }

    /** Opens the Creo connection on first use and keeps it for the session. */
    private synchronized CreoContext context() throws Exception {
        if (connection == null) {
            connection = CreoConnection.open(config);
            protocol.log("connected to Creo");
        }
        return connection.context();
    }

    synchronized void shutdown() {
        if (connection != null) {
            connection.close();
            connection = null;
            protocol.log("disconnected from Creo");
        }
        protocol.close();
    }

    /**
     * Notes a request that arrived before the handshake finished.
     *
     * <p>The spec says a server should not serve requests until it has had
     * {@code notifications/initialized}, but real clients do sometimes send {@code tools/list}
     * straight after {@code initialize}. Refusing them would break those clients for no benefit, so
     * the request is served and the irregularity is recorded on stderr instead.
     */
    private void warnIfNotInitialised(String method) {
        if (!initialised) {
            protocol.log("note: " + method + " arrived before notifications/initialized; serving anyway");
        }
    }

    /** A JSON-RPC level failure. */
    static final class McpError extends RuntimeException {
        private static final long serialVersionUID = 1L;
        final int code;

        McpError(int code, String message) {
            super(message);
            this.code = code;
        }
    }
}
