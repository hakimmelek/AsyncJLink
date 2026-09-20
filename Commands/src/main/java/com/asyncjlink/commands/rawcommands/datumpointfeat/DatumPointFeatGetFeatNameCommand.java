/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.datumpointfeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDatumPointFeat.DatumPointFeat;

/**
 * DatumPointFeat.GetFeatName &mdash; pfcDatumPointFeat.
 *
 * <pre>
 * String GetFeatName() throws jxthrowable
 * </pre>
 */
public final class DatumPointFeatGetFeatNameCommand implements Command {

    @Override public String name() { return "DatumPointFeat.GetFeatName"; }
    @Override public String jlinkPackage() { return "pfcDatumPointFeat"; }
    @Override public String receiverType() { return "DatumPointFeat"; }
    @Override public String signature() { return "String GetFeatName() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DatumPointFeat.GetFeatName \u2014 pfcDatumPointFeat")
                .required("target", JsonSchema.handle("DatumPointFeat"),
                        "The DatumPointFeat to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DatumPointFeat target = Marshal.in(ctx, params.get("target"),
                "DatumPointFeat", DatumPointFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetFeatName(), "String");
    }
}
