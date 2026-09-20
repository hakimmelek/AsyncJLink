/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.layer;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcLayer.Layer;

/**
 * Layer.Delete &mdash; pfcLayer.
 *
 * <pre>
 * void Delete() throws jxthrowable
 * </pre>
 */
public final class LayerDeleteCommand implements Command {

    @Override public String name() { return "Layer.Delete"; }
    @Override public String jlinkPackage() { return "pfcLayer"; }
    @Override public String receiverType() { return "Layer"; }
    @Override public String signature() { return "void Delete() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Layer.Delete \u2014 pfcLayer")
                .required("target", JsonSchema.handle("Layer"),
                        "The Layer to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Layer target = Marshal.in(ctx, params.get("target"),
                "Layer", Layer.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.Delete();
        return Marshal.ok();
    }
}
