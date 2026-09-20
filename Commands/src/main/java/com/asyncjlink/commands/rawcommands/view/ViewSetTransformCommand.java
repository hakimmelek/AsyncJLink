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
import com.ptc.pfc.pfcBase.Transform3D;
import com.ptc.pfc.pfcView.View;

/**
 * View.SetTransform &mdash; pfcView.
 *
 * <pre>
 * void SetTransform(Transform3D) throws jxthrowable
 * </pre>
 */
public final class ViewSetTransformCommand implements Command {

    @Override public String name() { return "View.SetTransform"; }
    @Override public String jlinkPackage() { return "pfcView"; }
    @Override public String receiverType() { return "View"; }
    @Override public String signature() { return "void SetTransform(Transform3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("View.SetTransform \u2014 pfcView")
                .required("target", JsonSchema.handle("View"),
                        "The View to act on.")
                .optional("transform3D", JsonSchema.dataObject("Transform3D"),
                        "Transform3D options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        View target = Marshal.in(ctx, params.get("target"),
                "View", View.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Transform3D transform3D = Marshal.in(ctx, params.get("transform3D"),
                "Transform3D", Transform3D.class, "transform3D");
        target.SetTransform(transform3D);
        return Marshal.ok();
    }
}
