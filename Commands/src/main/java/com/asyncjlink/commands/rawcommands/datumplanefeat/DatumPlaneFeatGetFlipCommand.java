/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.datumplanefeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDatumPlaneFeat.DatumPlaneFeat;

/**
 * DatumPlaneFeat.GetFlip &mdash; pfcDatumPlaneFeat.
 *
 * <pre>
 * Boolean GetFlip() throws jxthrowable
 * </pre>
 */
public final class DatumPlaneFeatGetFlipCommand implements Command {

    @Override public String name() { return "DatumPlaneFeat.GetFlip"; }
    @Override public String jlinkPackage() { return "pfcDatumPlaneFeat"; }
    @Override public String receiverType() { return "DatumPlaneFeat"; }
    @Override public String signature() { return "Boolean GetFlip() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DatumPlaneFeat.GetFlip \u2014 pfcDatumPlaneFeat")
                .required("target", JsonSchema.handle("DatumPlaneFeat"),
                        "The DatumPlaneFeat to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DatumPlaneFeat target = Marshal.in(ctx, params.get("target"),
                "DatumPlaneFeat", DatumPlaneFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetFlip(), "Boolean");
    }
}
