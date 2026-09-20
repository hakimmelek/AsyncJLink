package com.asyncjlink.commands;

/**
 * How a J-Link type crosses the JSON boundary.
 *
 * <p>Derived from {@code docs/jlink-api-asynchronous.md}: of the 927 documented types, this is the
 * classification that decides whether a value is inlined, enumerated, arrayed, handed out as a
 * handle, or constructed from a nested object.
 */
public enum TypeKind {

    /** {@code String}, {@code int}, {@code double}, {@code boolean} and their boxes. Inlined as-is. */
    PRIMITIVE,

    /** A {@code jxenum}. Travels as its constant name, e.g. {@code "MDL_PART"}. */
    ENUM,

    /** A {@code jxobject_i} sequence. Travels as a JSON array. */
    SEQUENCE,

    /**
     * A live Creo object — {@code Model}, {@code Feature}, {@code Surface}, {@code Selection}.
     * Travels as an opaque handle from {@link HandleStore}; results also carry a short summary.
     */
    LIVE,

    /**
     * A caller-constructed instruction or options object — {@code ExportInstructions},
     * {@code CheckoutOptions}. Travels as a nested JSON object, built via its {@code pfc*} factory.
     */
    DATA,

    /**
     * A callback listener. No CLI argument or MCP client can supply one, so commands that require
     * a listener are catalogued but not invocable.
     */
    LISTENER,

    /** {@code void}. */
    VOID,

    /** Not described by the dictionary; handled reflectively and reported as a string. */
    UNKNOWN
}
