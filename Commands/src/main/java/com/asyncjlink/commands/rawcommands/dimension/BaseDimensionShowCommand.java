/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.dimension;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcDimension.DimensionShowInstructions;

/**
 * BaseDimension.Show &mdash; pfcDimension.
 *
 * <pre>
 * void Show(DimensionShowInstructions) throws jxthrowable
 * </pre>
 */
public final class BaseDimensionShowCommand implements Command {

    @Override public String name() { return "BaseDimension.Show"; }
    @Override public String jlinkPackage() { return "pfcDimension"; }
    @Override public String receiverType() { return "BaseDimension"; }
    @Override public String signature() { return "void Show(DimensionShowInstructions) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseDimension.Show \u2014 pfcDimension")
                .required("target", JsonSchema.handle("BaseDimension"),
                        "The BaseDimension to act on.")
                .optional("dimensionShowInstructions", JsonSchema.dataObject("DimensionShowInstructions"),
                        "DimensionShowInstructions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        BaseDimension target = Marshal.in(ctx, params.get("target"),
                "BaseDimension", BaseDimension.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DimensionShowInstructions dimensionShowInstructions = Marshal.in(ctx, params.get("dimensionShowInstructions"),
                "DimensionShowInstructions", DimensionShowInstructions.class, "dimensionShowInstructions");
        target.Show(dimensionShowInstructions);
        return Marshal.ok();
    }
}
