/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.view2d;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcView2D.View2D;

/**
 * View2D.SetIsViewdisplayLayerDependent &mdash; pfcView2D.
 *
 * <pre>
 * void SetIsViewdisplayLayerDependent(boolean) throws jxthrowable
 * </pre>
 */
public final class View2DSetIsViewdisplayLayerDependentCommand implements Command {

    @Override public String name() { return "View2D.SetIsViewdisplayLayerDependent"; }
    @Override public String jlinkPackage() { return "pfcView2D"; }
    @Override public String receiverType() { return "View2D"; }
    @Override public String signature() { return "void SetIsViewdisplayLayerDependent(boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("View2D.SetIsViewdisplayLayerDependent \u2014 pfcView2D")
                .required("target", JsonSchema.handle("View2D"),
                        "The View2D to act on.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        View2D target = Marshal.in(ctx, params.get("target"),
                "View2D", View2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        target.SetIsViewdisplayLayerDependent(flag);
        return Marshal.ok();
    }
}
