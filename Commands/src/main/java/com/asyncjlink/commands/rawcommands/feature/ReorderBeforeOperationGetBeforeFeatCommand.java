/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.feature;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.ReorderBeforeOperation;

/**
 * ReorderBeforeOperation.GetBeforeFeat &mdash; pfcFeature.
 *
 * <pre>
 * Feature GetBeforeFeat() throws jxthrowable
 * </pre>
 */
public final class ReorderBeforeOperationGetBeforeFeatCommand implements Command {

    @Override public String name() { return "ReorderBeforeOperation.GetBeforeFeat"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "ReorderBeforeOperation"; }
    @Override public String signature() { return "Feature GetBeforeFeat() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ReorderBeforeOperation.GetBeforeFeat \u2014 pfcFeature")
                .required("target", JsonSchema.handle("ReorderBeforeOperation"),
                        "The ReorderBeforeOperation to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ReorderBeforeOperation target = Marshal.in(ctx, params.get("target"),
                "ReorderBeforeOperation", ReorderBeforeOperation.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetBeforeFeat(), "Feature");
    }
}
