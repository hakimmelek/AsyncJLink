package com.asyncjlink.commands;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAsyncConnection.AsyncConnection;
import com.ptc.pfc.pfcAsyncConnection.pfcAsyncConnection;
import com.ptc.pfc.pfcSession.Session;

/**
 * Opens and closes the asynchronous connection to a running Creo Parametric process.
 *
 * <p>There is no shared daemon: {@code Cli} opens one of these per invocation and {@code Mcp} opens
 * one for the life of the AI session. Each owns its connection independently.
 *
 * <p>Connection parameters come from {@code paths.yaml} by way of {@link CreoPaths}. Note what is
 * <em>not</em> here: there is no host or port argument. {@code AsyncConnection_Connect} takes only
 * {@code (userName, displayName, messageMenuPath, timeoutSeconds)}; the network endpoint is
 * negotiated by PTC's Name Service Message Daemon, and the only port that matters is
 * {@code PTCNMSPORT}, which must be identical for Creo and for this process. See
 * {@code com.asyncjlink.config.Bootstrap} for how that is arranged.
 */
public final class CreoConnection implements AutoCloseable {

    static {
        // PTC's asynchronous J-Link setup guide requires the application itself to load this before
        // any AsyncConnection method is called: it is not pulled in automatically by pfcasync.jar's
        // own static initializers, which only cover the generic CIP/jxthrowable native bridge. A
        // static initializer runs this exactly once, before the class can be used for anything.
        System.loadLibrary("pfcasyncmt");
    }

    private final AsyncConnection connection;
    private final CreoContext context;
    private final boolean ownsConnection;

    private CreoConnection(AsyncConnection connection, CreoContext context, boolean ownsConnection) {
        this.connection = connection;
        this.context = context;
        this.ownsConnection = ownsConnection;
    }

    /**
     * Connects to a Creo process already running on this machine.
     *
     * <p>An existing active connection in this JVM is reused rather than duplicated.
     */
    public static CreoConnection open(CreoPaths paths) throws jxthrowable {
        AsyncConnection existing = activeConnection();
        if (existing != null && existing.IsRunning()) {
            return new CreoConnection(existing, new CreoContext(existing.GetSession(), paths), false);
        }
        // "" and null are both meaningful and mean different things to PTC: "" is "the current user"
        // / "the local display", null is "match any". paths.yaml preserves the difference by
        // distinguishing an empty value from an omitted key, so both are passed through untouched.
        AsyncConnection conn = pfcAsyncConnection.AsyncConnection_Connect(
                paths.connectUserName(),
                paths.connectDisplayName(),
                paths.connectMessageMenuPath(),
                Integer.valueOf(paths.connectTimeoutSeconds()));
        if (conn == null) {
            throw new CommandException(
                    "Could not connect to Creo. Check that Creo Parametric is running, that it was "
                            + "started with PTCNMSPORT=" + paths.nmsPort() + ", and that "
                            + "PRO_COMM_MSG_EXE points at this installation.",
                    "no_connection");
        }
        return new CreoConnection(conn, new CreoContext(conn.GetSession(), paths), true);
    }

    private static AsyncConnection activeConnection() {
        try {
            return pfcAsyncConnection.AsyncConnection_GetActiveConnection();
        } catch (Throwable e) {
            // Nothing active, which is the normal first-run case. This probe has also been observed
            // to fail with UnsatisfiedLinkError (an Error, not a RuntimeException) on a connection's
            // very first use in a fresh JVM, so any failure here is treated the same way: fall through
            // to AsyncConnection_Connect below, which reports real connection failures properly.
            return null;
        }
    }

    public CreoContext context() {
        return context;
    }

    public Session session() {
        return context.session();
    }

    public AsyncConnection connection() {
        return connection;
    }

    /**
     * Disconnects, leaving Creo running.
     *
     * <p>Deliberately never calls {@code AsyncConnection.End()}, which would terminate the user's
     * Creo session. A tool that reads a model should not be able to close the application.
     */
    @Override
    public void close() {
        context.handles().clear();
        if (!ownsConnection) {
            return;
        }
        try {
            connection.Disconnect(Integer.valueOf(0));
        } catch (jxthrowable | RuntimeException e) {
            // Creo may already be gone; there is nothing useful left to do about it here.
        }
    }
}
