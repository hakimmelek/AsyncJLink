/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.geometry;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcGeometry.Text;

/**
 * Text.GetPoint &mdash; pfcGeometry.
 *
 * <pre>
 * Point3D GetPoint() throws jxthrowable
 * </pre>
 */
public final class TextGetPointCommand implements Command {

    @Override public String name() { return "Text.GetPoint"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "Text"; }
    @Override public String signature() { return "Point3D GetPoint() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Text.GetPoint \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("Text"),
                        "The Text to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Text target = Marshal.in(ctx, params.get("target"),
                "Text", Text.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetPoint(), "Point3D");
    }
}
