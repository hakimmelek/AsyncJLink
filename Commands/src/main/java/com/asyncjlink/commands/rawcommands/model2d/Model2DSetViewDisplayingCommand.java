/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.model2d;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcView2D.View2D;

/**
 * Model2D.SetViewDisplaying &mdash; pfcModel2D.
 *
 * <pre>
 * void SetViewDisplaying(BaseDimension, View2D) throws jxthrowable
 * </pre>
 */
public final class Model2DSetViewDisplayingCommand implements Command {

    @Override public String name() { return "Model2D.SetViewDisplaying"; }
    @Override public String jlinkPackage() { return "pfcModel2D"; }
    @Override public String receiverType() { return "Model2D"; }
    @Override public String signature() { return "void SetViewDisplaying(BaseDimension, View2D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model2D.SetViewDisplaying \u2014 pfcModel2D")
                .required("target", JsonSchema.handle("Model2D"),
                        "The Model2D to act on.")
                .optional("baseDimension", JsonSchema.handle("BaseDimension"),
                        "Handle to a BaseDimension, as returned by an earlier command.")
                .optional("view2D", JsonSchema.handle("View2D"),
                        "Handle to a View2D, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model2D target = Marshal.in(ctx, params.get("target"),
                "Model2D", Model2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        BaseDimension baseDimension = Marshal.in(ctx, params.get("baseDimension"),
                "BaseDimension", BaseDimension.class, "baseDimension");
        View2D view2D = Marshal.in(ctx, params.get("view2D"),
                "View2D", View2D.class, "view2D");
        target.SetViewDisplaying(baseDimension, view2D);
        return Marshal.ok();
    }
}
