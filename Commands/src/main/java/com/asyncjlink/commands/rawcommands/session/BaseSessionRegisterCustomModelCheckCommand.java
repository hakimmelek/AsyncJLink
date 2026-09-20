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
import com.ptc.pfc.pfcModelCheck.CustomCheckInstructions;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.RegisterCustomModelCheck &mdash; pfcSession.
 *
 * <pre>
 * void RegisterCustomModelCheck(CustomCheckInstructions) throws jxthrowable
 * </pre>
 */
public final class BaseSessionRegisterCustomModelCheckCommand implements Command {

    @Override public String name() { return "BaseSession.RegisterCustomModelCheck"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void RegisterCustomModelCheck(CustomCheckInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.RegisterCustomModelCheck \u2014 pfcSession")
                .optional("customCheckInstructions", JsonSchema.dataObject("CustomCheckInstructions"),
                        "CustomCheckInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        CustomCheckInstructions customCheckInstructions = Marshal.in(ctx, params.get("customCheckInstructions"),
                "CustomCheckInstructions", CustomCheckInstructions.class, "customCheckInstructions");
        target.RegisterCustomModelCheck(customCheckInstructions);
        return Marshal.ok();
    }
}
