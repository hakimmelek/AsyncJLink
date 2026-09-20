/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.display;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcBase.Vector3D;
import com.ptc.pfc.pfcDisplay.Display;

/**
 * Display.DrawArc2D &mdash; pfcDisplay.
 *
 * <pre>
 * void DrawArc2D(Point3D, double, Vector3D, Vector3D) throws jxthrowable
 * </pre>
 */
public final class DisplayDrawArc2DCommand implements Command {

    @Override public String name() { return "Display.DrawArc2D"; }
    @Override public String jlinkPackage() { return "pfcDisplay"; }
    @Override public String receiverType() { return "Display"; }
    @Override public String signature() { return "void DrawArc2D(Point3D, double, Vector3D, Vector3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Display.DrawArc2D \u2014 pfcDisplay")
                .required("target", JsonSchema.handle("Display"),
                        "The Display to act on.")
                .optional("point3D", JsonSchema.sequence("Point3D", JsonSchema.number()),
                        "Array of double.")
                .required("value", JsonSchema.number(),
                        "double value.")
                .optional("vector3D1", JsonSchema.sequence("Vector3D", JsonSchema.number()),
                        "Array of double.")
                .optional("vector3D2", JsonSchema.sequence("Vector3D", JsonSchema.number()),
                        "Array of double.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Display target = Marshal.in(ctx, params.get("target"),
                "Display", Display.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Point3D point3D = Marshal.in(ctx, params.get("point3D"),
                "Point3D", Point3D.class, "point3D");
        double value = Marshal.in(ctx, params.get("value"),
                "double", double.class, "value");
        Vector3D vector3D1 = Marshal.in(ctx, params.get("vector3D1"),
                "Vector3D", Vector3D.class, "vector3D1");
        Vector3D vector3D2 = Marshal.in(ctx, params.get("vector3D2"),
                "Vector3D", Vector3D.class, "vector3D2");
        target.DrawArc2D(point3D, value, vector3D1, vector3D2);
        return Marshal.ok();
    }
}
