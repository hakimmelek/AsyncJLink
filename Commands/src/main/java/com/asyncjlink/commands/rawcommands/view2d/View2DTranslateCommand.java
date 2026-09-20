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
import com.ptc.pfc.pfcBase.Vector3D;
import com.ptc.pfc.pfcView2D.View2D;

/**
 * View2D.Translate &mdash; pfcView2D.
 *
 * <pre>
 * void Translate(Vector3D) throws jxthrowable
 * </pre>
 */
public final class View2DTranslateCommand implements Command {

    @Override public String name() { return "View2D.Translate"; }
    @Override public String jlinkPackage() { return "pfcView2D"; }
    @Override public String receiverType() { return "View2D"; }
    @Override public String signature() { return "void Translate(Vector3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("View2D.Translate \u2014 pfcView2D")
                .required("target", JsonSchema.handle("View2D"),
                        "The View2D to act on.")
                .optional("vector3D", JsonSchema.sequence("Vector3D", JsonSchema.number()),
                        "Array of double.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        View2D target = Marshal.in(ctx, params.get("target"),
                "View2D", View2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Vector3D vector3D = Marshal.in(ctx, params.get("vector3D"),
                "Vector3D", Vector3D.class, "vector3D");
        target.Translate(vector3D);
        return Marshal.ok();
    }
}
