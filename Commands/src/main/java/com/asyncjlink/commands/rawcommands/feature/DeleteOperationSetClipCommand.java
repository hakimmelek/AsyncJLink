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
 * DeleteOperation.SetClip &mdash; pfcFeature.
 *
 * <pre>
 * void SetClip(boolean) throws jxthrowable
 * </pre>
 */
public final class DeleteOperationSetClipCommand implements Command {

    @Override public String name() { return "DeleteOperation.SetClip"; }
    @Override public String jlinkPackage() { return "pfcFeature"; }
    @Override public String receiverType() { return "DeleteOperation"; }
    @Override public String signature() { return "void SetClip(boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DeleteOperation.SetClip \u2014 pfcFeature")
                .required("target", JsonSchema.handle("DeleteOperation"),
                        "The DeleteOperation to act on.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DeleteOperation target = Marshal.in(ctx, params.get("target"),
                "DeleteOperation", DeleteOperation.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        target.SetClip(flag);
        return Marshal.ok();
    }
}
