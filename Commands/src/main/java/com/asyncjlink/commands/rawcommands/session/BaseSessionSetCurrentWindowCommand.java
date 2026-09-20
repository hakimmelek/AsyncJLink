/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.session;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;
import com.ptc.pfc.pfcWindow.Window;

/**
 * BaseSession.SetCurrentWindow &mdash; pfcSession.
 *
 * <pre>
 * void SetCurrentWindow(Window) throws jxthrowable
 * </pre>
 */
public final class BaseSessionSetCurrentWindowCommand implements Command {

    @Override public String name() { return "BaseSession.SetCurrentWindow"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void SetCurrentWindow(Window) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.SetCurrentWindow \u2014 pfcSession")
                .optional("window", JsonSchema.handle("Window"),
                        "Handle to a Window, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        Window window = Marshal.in(ctx, params.get("window"),
                "Window", Window.class, "window");
        target.SetCurrentWindow(window);
        return Marshal.ok();
    }
}
