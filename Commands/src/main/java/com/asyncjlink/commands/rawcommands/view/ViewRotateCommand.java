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
import com.ptc.pfc.pfcBase.CoordAxis;
import com.ptc.pfc.pfcView.View;

/**
 * View.Rotate &mdash; pfcView.
 *
 * <pre>
 * void Rotate(CoordAxis, double) throws jxthrowable
 * </pre>
 */
public final class ViewRotateCommand implements Command {

    @Override public String name() { return "View.Rotate"; }
    @Override public String jlinkPackage() { return "pfcView"; }
    @Override public String receiverType() { return "View"; }
    @Override public String signature() { return "void Rotate(CoordAxis, double) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("View.Rotate \u2014 pfcView")
                .required("target", JsonSchema.handle("View"),
                        "The View to act on.")
                .optional("coordAxis", JsonSchema.enumOf("CoordAxis", "COORD_AXIS_X", "COORD_AXIS_Y", "COORD_AXIS_Z"),
                        "One of the CoordAxis constants.")
                .required("value", JsonSchema.number(),
                        "double value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        View target = Marshal.in(ctx, params.get("target"),
                "View", View.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        CoordAxis coordAxis = Marshal.in(ctx, params.get("coordAxis"),
                "CoordAxis", CoordAxis.class, "coordAxis");
        double value = Marshal.in(ctx, params.get("value"),
                "double", double.class, "value");
        target.Rotate(coordAxis, value);
        return Marshal.ok();
    }
}
