package com.asyncjlink.mcp;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandRegistry;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns the {@link CommandRegistry} into MCP tool definitions.
 *
 * <p>Walked once at startup, so no tool list is ever hand-maintained separately from the commands:
 * every operation added under {@code rawcommands/} becomes a tool automatically, with the same schema
 * that {@code creoctl --describe} prints.
 *
 * <p>This server publishes the whole catalogue — one tool per J-Link operation. That is a large tool
 * list by MCP standards, so {@code tools/list} supports cursor pagination and the page size can be
 * capped with {@code -Dasyncjlink.mcp.pageSize=N} for clients that need smaller responses.
 */
public final class ToolCatalog {

    /** Set {@code -Dasyncjlink.mcp.pageSize=N} to page {@code tools/list}; 0 means one page. */
    public static final String PAGE_SIZE_PROPERTY = "asyncjlink.mcp.pageSize";

    private final List<Command> commands;
    private final int pageSize;

    public ToolCatalog(CommandRegistry registry) {
        this.commands = new ArrayList<>(registry.all());
        this.pageSize = readPageSize();
    }

    private static int readPageSize() {
        String v = System.getProperty(PAGE_SIZE_PROPERTY);
        if (v == null || v.trim().isEmpty()) {
            return 0;
        }
        try {
            return Math.max(0, Integer.parseInt(v.trim()));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int size() {
        return commands.size();
    }

    /**
     * Builds one page of the {@code tools/list} result.
     *
     * @param cursor the {@code nextCursor} from the previous page, or {@code null} for the first
     */
    public JsonObject list(String cursor) {
        int from = 0;
        if (cursor != null && !cursor.isEmpty()) {
            try {
                from = Integer.parseInt(cursor);
            } catch (NumberFormatException e) {
                from = 0;
            }
            from = Math.max(0, Math.min(from, commands.size()));
        }
        int to = pageSize > 0 ? Math.min(from + pageSize, commands.size()) : commands.size();

        JsonArray tools = new JsonArray();
        for (int i = from; i < to; i++) {
            tools.add(toTool(commands.get(i)));
        }
        JsonObject result = JsonObject.of("tools", tools);
        if (to < commands.size()) {
            result.put("nextCursor", String.valueOf(to));
        }
        return result;
    }

    /** One MCP tool definition. */
    public JsonObject toTool(Command c) {
        JsonObject tool = JsonObject.of(
                "name", c.toolName(),
                "description", describe(c),
                "inputSchema", c.paramSchema().toJson());
        // Read-only operations are safe to retry and safe to call speculatively; saying so lets a
        // client present them differently from the ones that modify the user's model.
        JsonObject hints = JsonObject.of("title", c.name());
        if (isReadOnly(c)) {
            hints.put("readOnlyHint", Boolean.TRUE);
        }
        if (isDestructive(c)) {
            hints.put("destructiveHint", Boolean.TRUE);
        }
        tool.put("annotations", hints);
        return tool;
    }

    private static String describe(Command c) {
        StringBuilder sb = new StringBuilder();
        sb.append(c.receiverType()).append('.').append(c.methodName());
        sb.append(" (").append(c.jlinkPackage()).append("). ");
        if (c.jlinkPackage().startsWith("pfc")) {
            sb.append("J-Link signature: ").append(c.signature()).append('.');
        } else {
            // A composite has no J-Link signature to quote; its own description says far more about
            // what it does than a synthesised one would.
            sb.append(c.description());
        }
        if (!c.isInvocable()) {
            sb.append(" NOT CALLABLE: ").append(c.unsupportedReason()).append('.');
        }
        return sb.toString();
    }

    /**
     * Whether the operation only reads.
     *
     * <p>Judged from the J-Link naming convention, which is consistent across the API: {@code Get*},
     * {@code Is*}, {@code Has*}, {@code List*} and {@code Check*} read, everything else may write.
     * Anything ambiguous is treated as a writer, since over-reporting a mutation is the safe error.
     * Composites follow the same convention deliberately, and add {@code Find*} and {@code Audit*}.
     */
    static boolean isReadOnly(Command c) {
        String m = c.methodName();
        return m.startsWith("Get") || m.startsWith("Is") || m.startsWith("Has")
                || m.startsWith("List") || m.startsWith("Check") || m.startsWith("Eval")
                || m.startsWith("Find") || m.startsWith("Audit");
    }

    static boolean isDestructive(Command c) {
        String m = c.methodName();
        return m.startsWith("Delete") || m.startsWith("Erase") || m.startsWith("Remove")
                || m.equals("End") || m.startsWith("Suppress");
    }
}
