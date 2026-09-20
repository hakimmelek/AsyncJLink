/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.family;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFamily.FamColParam;

/**
 * FamColParam.GetRefParam &mdash; pfcFamily.
 *
 * <pre>
 * Parameter GetRefParam() throws jxthrowable
 * </pre>
 */
public final class FamColParamGetRefParamCommand implements Command {

    @Override public String name() { return "FamColParam.GetRefParam"; }
    @Override public String jlinkPackage() { return "pfcFamily"; }
    @Override public String receiverType() { return "FamColParam"; }
    @Override public String signature() { return "Parameter GetRefParam() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("FamColParam.GetRefParam \u2014 pfcFamily")
                .required("target", JsonSchema.handle("FamColParam"),
                        "The FamColParam to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        FamColParam target = Marshal.in(ctx, params.get("target"),
                "FamColParam", FamColParam.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetRefParam(), "Parameter");
    }
}
