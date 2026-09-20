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
import com.ptc.pfc.pfcLayer.DisplayStatus;
import com.ptc.pfc.pfcLayer.Layer;

/**
 * Layer.SetStatus &mdash; pfcLayer.
 *
 * <pre>
 * void SetStatus(DisplayStatus) throws jxthrowable
 * </pre>
 */
public final class LayerSetStatusCommand implements Command {

    @Override public String name() { return "Layer.SetStatus"; }
    @Override public String jlinkPackage() { return "pfcLayer"; }
    @Override public String receiverType() { return "Layer"; }
    @Override public String signature() { return "void SetStatus(DisplayStatus) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Layer.SetStatus \u2014 pfcLayer")
                .required("target", JsonSchema.handle("Layer"),
                        "The Layer to act on.")
                .optional("displayStatus", JsonSchema.enumOf("DisplayStatus", "LAYER_NORMAL", "LAYER_DISPLAY", "LAYER_BLANK", "LAYER_HIDDEN"),
                        "One of the DisplayStatus constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Layer target = Marshal.in(ctx, params.get("target"),
                "Layer", Layer.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        DisplayStatus displayStatus = Marshal.in(ctx, params.get("displayStatus"),
                "DisplayStatus", DisplayStatus.class, "displayStatus");
        target.SetStatus(displayStatus);
        return Marshal.ok();
    }
}
