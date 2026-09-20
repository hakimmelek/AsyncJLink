package com.asyncjlink.commands;

/**
 * A failure AsyncJLink itself detected — a bad parameter, an unresolvable handle, an operation that
 * cannot be driven from JSON.
 *
 * <p>Distinct from {@code jxthrowable}, which is Creo rejecting the call. Keeping the two apart lets
 * the front doors report "you asked for the wrong thing" separately from "Creo said no".
 */
public class CommandException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    public CommandException(String message) {
        this(message, "invalid_params", null);
    }

    public CommandException(String message, String code) {
        this(message, code, null);
    }

    public CommandException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /** Stable machine-readable code: {@code invalid_params}, {@code unknown_handle}, … */
    public String code() {
        return code;
    }

    public static CommandException unknownHandle(String field, String value) {
        return new CommandException(
                "Field '" + field + "' is not a known object handle: '" + value + "'. "
                        + "Handles are returned by earlier commands and are only valid for the "
                        + "lifetime of this connection.",
                "unknown_handle");
    }

    public static CommandException unsupported(String what, String why) {
        return new CommandException(what + " cannot be driven from JSON: " + why, "unsupported");
    }
}
