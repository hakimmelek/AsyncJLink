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
 * BaseSession.GetPrintPlacementOptions &mdash; pfcSession.
 *
 * <pre>
 * PrintPlacementOption GetPrintPlacementOptions() throws jxthrowable
 * </pre>
 */
public final class BaseSessionGetPrintPlacementOptionsCommand implements Command {

    @Override public String name() { return "BaseSession.GetPrintPlacementOptions"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "PrintPlacementOption GetPrintPlacementOptions() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.GetPrintPlacementOptions \u2014 pfcSession")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        return Marshal.result(ctx, target.GetPrintPlacementOptions(), "PrintPlacementOption");
    }
}
