/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.view;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcView.View;

/**
 * View.GetTransform &mdash; pfcView.
 *
 * <pre>
 * Transform3D GetTransform() throws jxthrowable
 * </pre>
 */
public final class ViewGetTransformCommand implements Command {

    @Override public String name() { return "View.GetTransform"; }
    @Override public String jlinkPackage() { return "pfcView"; }
    @Override public String receiverType() { return "View"; }
    @Override public String signature() { return "Transform3D GetTransform() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("View.GetTransform \u2014 pfcView")
                .required("target", JsonSchema.handle("View"),
                        "The View to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        View target = Marshal.in(ctx, params.get("target"),
                "View", View.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetTransform(), "Transform3D");
    }
}
