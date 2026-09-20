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
import com.ptc.pfc.pfcFeature.DeleteOperation;

/**
 * DeleteOperation.GetClip &mdash; pfcFeature.
 *
 * <pre>
 * boolean GetClip() throws jxthrowable
 * </pre>
 */
public final class DeleteOperationGetClipCommand implements Command {

    @Override public String name() { return "DeleteOperation.GetClip"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "DeleteOperation"; }
    @Override public String signature() { return "boolean GetClip() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DeleteOperation.GetClip \u2014 pfcFeature")
                .required("target", JsonSchema.handle("DeleteOperation"),
                        "The DeleteOperation to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DeleteOperation target = Marshal.in(ctx, params.get("target"),
                "DeleteOperation", DeleteOperation.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetClip(), "boolean");
    }
}
