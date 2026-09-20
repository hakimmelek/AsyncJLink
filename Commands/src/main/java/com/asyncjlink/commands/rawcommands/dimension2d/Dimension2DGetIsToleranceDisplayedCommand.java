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
 * Dimension2D.GetIsToleranceDisplayed &mdash; pfcDimension2D.
 *
 * <pre>
 * boolean GetIsToleranceDisplayed() throws jxthrowable
 * </pre>
 */
public final class Dimension2DGetIsToleranceDisplayedCommand implements Command {

    @Override public String name() { return "Dimension2D.GetIsToleranceDisplayed"; }
    @Override public String jlinkPackage() { return "pfcDimension2D"; }
    @Override public String receiverType() { return "Dimension2D"; }
    @Override public String signature() { return "boolean GetIsToleranceDisplayed() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Dimension2D.GetIsToleranceDisplayed \u2014 pfcDimension2D")
                .required("target", JsonSchema.handle("Dimension2D"),
                        "The Dimension2D to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Dimension2D target = Marshal.in(ctx, params.get("target"),
                "Dimension2D", Dimension2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetIsToleranceDisplayed(), "boolean");
    }
}
