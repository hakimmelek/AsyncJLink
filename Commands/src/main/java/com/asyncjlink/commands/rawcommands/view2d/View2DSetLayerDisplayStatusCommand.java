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
import com.ptc.pfc.pfcLayer.DisplayStatus;
import com.ptc.pfc.pfcLayer.Layer;
import com.ptc.pfc.pfcView2D.View2D;

/**
 * View2D.SetLayerDisplayStatus &mdash; pfcView2D.
 *
 * <pre>
 * void SetLayerDisplayStatus(Layer, DisplayStatus) throws jxthrowable
 * </pre>
 */
public final class View2DSetLayerDisplayStatusCommand implements Command {

    @Override public String name() { return "View2D.SetLayerDisplayStatus"; }
    @Override public String jlinkPackage() { return "pfcView2D"; }
    @Override public String receiverType() { return "View2D"; }
    @Override public String signature() { return "void SetLayerDisplayStatus(Layer, DisplayStatus) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("View2D.SetLayerDisplayStatus \u2014 pfcView2D")
                .required("target", JsonSchema.handle("View2D"),
                        "The View2D to act on.")
                .optional("layer", JsonSchema.handle("Layer"),
                        "Handle to a Layer, as returned by an earlier command.")
                .optional("displayStatus", JsonSchema.enumOf("DisplayStatus", "LAYER_NORMAL", "LAYER_DISPLAY", "LAYER_BLANK", "LAYER_HIDDEN"),
                        "One of the DisplayStatus constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        View2D target = Marshal.in(ctx, params.get("target"),
                "View2D", View2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Layer layer = Marshal.in(ctx, params.get("layer"),
                "Layer", Layer.class, "layer");
        DisplayStatus displayStatus = Marshal.in(ctx, params.get("displayStatus"),
                "DisplayStatus", DisplayStatus.class, "displayStatus");
        target.SetLayerDisplayStatus(layer, displayStatus);
        return Marshal.ok();
    }
}
