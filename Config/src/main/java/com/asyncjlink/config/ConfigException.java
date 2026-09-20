package com.asyncjlink.config;

/** A problem with {@code paths.yaml} — missing, malformed, or missing a required entry. */
public class ConfigException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
