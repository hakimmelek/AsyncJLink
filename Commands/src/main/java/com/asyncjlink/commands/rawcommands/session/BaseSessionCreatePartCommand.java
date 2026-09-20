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

/**
 * BaseSession.CreatePart &mdash; pfcSession.
 *
 * <pre>
 * Part CreatePart(String) throws jxthrowable
 * </pre>
 */
public final class BaseSessionCreatePartCommand implements Command {

    @Override public String name() { return "BaseSession.CreatePart"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "Part CreatePart(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.CreatePart \u2014 pfcSession")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        return Marshal.result(ctx, target.CreatePart(value), "Part");
    }
}
