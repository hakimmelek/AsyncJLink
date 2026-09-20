/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.jlink;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcJLink.JLinkApplication;

/**
 * JLinkApplication.Stop &mdash; pfcJLink.
 *
 * <pre>
 * void Stop() throws jxthrowable
 * </pre>
 */
public final class JLinkApplicationStopCommand implements Command {

    @Override public String name() { return "JLinkApplication.Stop"; }
    @Override public String jlinkPackage() { return "pfcJLink"; }
    @Override public String receiverType() { return "JLinkApplication"; }
    @Override public String signature() { return "void Stop() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("JLinkApplication.Stop \u2014 pfcJLink")
                .required("target", JsonSchema.handle("JLinkApplication"),
                        "The JLinkApplication to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        JLinkApplication target = Marshal.in(ctx, params.get("target"),
                "JLinkApplication", JLinkApplication.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.Stop();
        return Marshal.ok();
    }
}
