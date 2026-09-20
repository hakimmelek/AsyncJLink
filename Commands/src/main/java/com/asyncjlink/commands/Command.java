package com.asyncjlink.commands;

import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;
import com.ptc.cipjava.jxthrowable;

/**
 * One Creo operation.
 *
 * <p>A {@code Command} knows nothing about sockets, argv, stdio or MCP. Given a {@link CreoContext}
 * and a parameter object it performs exactly one J-Link call and returns its result as JSON. This is
 * the single vocabulary shared by {@code Cli} and {@code Mcp}: every operation is added once, under
 * {@code Commands/rawcommands/}, and both front doors pick it up automatically.
 *
 * <p>Implementations under {@code rawcommands/} are generated from
 * {@code docs/jlink-api-asynchronous.md} by {@code tools/generate_commands.py} and must not be
 * edited by hand.
 */
public interface Command {

    /**
     * Globally unique identifier, {@code <Receiver>.<Method>} — for example
     * {@code Solid.GetMassProperty}. The receiver is part of the name because 50 method names recur
     * across classes within a single pfc package.
     */
    String name();

    /** The J-Link package this operation comes from, e.g. {@code pfcSolid}. */
    String jlinkPackage();

    /** The J-Link interface the method is declared on, e.g. {@code Solid}. */
    String receiverType();

    /** Verbatim J-Link signature, carried through for help text and tool descriptions. */
    String signature();

    /** Schema for {@code params}; drives both {@code creoctl --help} and the MCP {@code inputSchema}. */
    JsonSchema paramSchema();

    /**
     * Runs the operation.
     *
     * @return a JSON object; by convention the J-Link return value is under {@code "result"}, and
     *     {@code void} methods return an empty object.
     */
    JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable;

    /**
     * Whether every parameter can be expressed as JSON.
     *
     * <p>A handful of J-Link calls take live callback listeners, which no CLI argument or MCP client
     * can supply. Those commands are still catalogued — they appear in {@code --help} and in the tool
     * list — but {@link #execute} rejects them with an explanation rather than pretending to work.
     */
    default boolean isInvocable() {
        return true;
    }

    /** Why {@link #isInvocable()} is false, or {@code null} when it is true. */
    default String unsupportedReason() {
        return null;
    }

    /** One-line human description, used as the MCP tool description. */
    default String description() {
        return receiverType() + "." + methodName() + " — " + signature();
    }

    /** The bare J-Link method name, without the receiver prefix. */
    default String methodName() {
        String n = name();
        int dot = n.indexOf('.');
        return dot < 0 ? n : n.substring(dot + 1);
    }

    /** MCP tool name: {@code Receiver_Method}, matching {@code ^[A-Za-z0-9_-]{1,64}$}. */
    default String toolName() {
        return name().replace('.', '_');
    }
}
