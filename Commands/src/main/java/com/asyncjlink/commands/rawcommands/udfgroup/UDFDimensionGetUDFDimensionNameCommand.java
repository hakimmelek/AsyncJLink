/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.udfgroup;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUDFGroup.UDFDimension;

/**
 * UDFDimension.GetUDFDimensionName &mdash; pfcUDFGroup.
 *
 * <pre>
 * String GetUDFDimensionName() throws jxthrowable
 * </pre>
 */
public final class UDFDimensionGetUDFDimensionNameCommand implements Command {

    @Override public String name() { return "UDFDimension.GetUDFDimensionName"; }
    @Override public String jlinkPackage() { return "pfcUDFGroup"; }
    @Override public String receiverType() { return "UDFDimension"; }
    @Override public String signature() { return "String GetUDFDimensionName() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("UDFDimension.GetUDFDimensionName \u2014 pfcUDFGroup")
                .required("target", JsonSchema.handle("UDFDimension"),
                        "The UDFDimension to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        UDFDimension target = Marshal.in(ctx, params.get("target"),
                "UDFDimension", UDFDimension.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetUDFDimensionName(), "String");
    }
}
