/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.dimension2d;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension2D.Dimension2D;

/**
 * Dimension2D.ConvertToOrdinate &mdash; pfcDimension2D.
 *
 * <pre>
 * void ConvertToOrdinate(Dimension2D) throws jxthrowable
 * </pre>
 */
public final class Dimension2DConvertToOrdinateCommand implements Command {

    @Override public String name() { return "Dimension2D.ConvertToOrdinate"; }
    @Override public String jlinkPackage() { return "pfcDimension2D"; }
    @Override public String receiverType() { return "Dimension2D"; }
    @Override public String signature() { return "void ConvertToOrdinate(Dimension2D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Dimension2D.ConvertToOrdinate \u2014 pfcDimension2D")
                .required("target", JsonSchema.handle("Dimension2D"),
                        "The Dimension2D to act on.")
                .optional("dimension2D", JsonSchema.handle("Dimension2D"),
                        "Handle to a Dimension2D, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Dimension2D target = Marshal.in(ctx, params.get("target"),
                "Dimension2D", Dimension2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Dimension2D dimension2D = Marshal.in(ctx, params.get("dimension2D"),
                "Dimension2D", Dimension2D.class, "dimension2D");
        target.ConvertToOrdinate(dimension2D);
        return Marshal.ok();
    }
}
