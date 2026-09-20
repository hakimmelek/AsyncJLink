package com.asyncjlink.json;

import java.util.ArrayList;
import java.util.List;

/**
 * A small JSON Schema (draft 2020-12 subset) builder.
 *
 * <p>One schema per {@code Command} serves two consumers without being written twice: {@code Cli}
 * renders it as {@code --help} text, and {@code Mcp} publishes it verbatim as the tool's
 * {@code inputSchema}.
 */
public final class JsonSchema {

    private final JsonObject properties = new JsonObject();
    private final List<String> required = new ArrayList<>();
    private String description;

    public static JsonSchema object() {
        return new JsonSchema();
    }

    public JsonSchema describedAs(String text) {
        this.description = text;
        return this;
    }

    public JsonSchema required(String name, JsonObject type, String doc) {
        properties.put(name, annotate(type, doc));
        required.add(name);
        return this;
    }

    public JsonSchema optional(String name, JsonObject type, String doc) {
        properties.put(name, annotate(type, doc));
        return this;
    }

    private static JsonObject annotate(JsonObject type, String doc) {
        if (doc != null && !doc.isEmpty()) {
            type.put("description", doc);
        }
        return type;
    }

    public JsonObject toJson() {
        JsonObject schema = JsonObject.of("type", "object");
        if (description != null) {
            schema.put("description", description);
        }
        schema.put("properties", properties);
        if (!required.isEmpty()) {
            JsonArray req = new JsonArray();
            for (String r : required) {
                req.add(r);
            }
            schema.put("required", req);
        }
        // Creo rejects unknown instruction fields late and obscurely; failing fast at the schema
        // boundary gives the caller a far better error.
        schema.put("additionalProperties", Boolean.FALSE);
        return schema;
    }

    public List<String> requiredNames() {
        return required;
    }

    public JsonObject propertiesJson() {
        return properties;
    }

    // ---- leaf types ------------------------------------------------------

    public static JsonObject string() {
        return JsonObject.of("type", "string");
    }

    public static JsonObject integer() {
        return JsonObject.of("type", "integer");
    }

    public static JsonObject number() {
        return JsonObject.of("type", "number");
    }

    public static JsonObject bool() {
        return JsonObject.of("type", "boolean");
    }

    public static JsonObject any() {
        return new JsonObject();
    }

    public static JsonObject array(JsonObject items) {
        return JsonObject.of("type", "array", "items", items);
    }

    public static JsonObject enumOf(String jlinkType, String... values) {
        JsonArray vals = new JsonArray();
        for (String v : values) {
            vals.add(v);
        }
        return JsonObject.of("type", "string", "enum", vals, "x-jlink-type", jlinkType);
    }

    /**
     * A reference to a live Creo object held in the session's {@code HandleStore}.
     *
     * <p>Accepts the opaque handle string returned by an earlier command. Types that can also be
     * addressed by name (models, above all) additionally accept that name; see
     * {@code Marshal} for the per-type resolution rules.
     */
    public static JsonObject handle(String jlinkType) {
        return JsonObject.of(
                "type", "string",
                "x-jlink-type", jlinkType,
                "x-jlink-kind", "handle");
    }

    /** An instruction/options object that AsyncJLink builds from these fields before the call. */
    public static JsonObject dataObject(String jlinkType) {
        return JsonObject.of(
                "type", "object",
                "x-jlink-type", jlinkType,
                "x-jlink-kind", "data",
                "additionalProperties", Boolean.TRUE);
    }

    /** A J-Link sequence type, rendered on the wire as a JSON array. */
    public static JsonObject sequence(String jlinkType, JsonObject items) {
        JsonObject o = JsonObject.of("type", "array", "items", items);
        o.put("x-jlink-type", jlinkType);
        o.put("x-jlink-kind", "sequence");
        return o;
    }
}
