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
import com.ptc.pfc.pfcDisplay.Display;

/**
 * Display.GetRotationAngle &mdash; pfcDisplay.
 *
 * <pre>
 * double GetRotationAngle() throws jxthrowable
 * </pre>
 */
public final class DisplayGetRotationAngleCommand implements Command {

    @Override public String name() { return "Display.GetRotationAngle"; }
    @Override public String jlinkPackage() { return "pfcDisplay"; }
    @Override public String receiverType() { return "Display"; }
    @Override public String signature() { return "double GetRotationAngle() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Display.GetRotationAngle \u2014 pfcDisplay")
                .required("target", JsonSchema.handle("Display"),
                        "The Display to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Display target = Marshal.in(ctx, params.get("target"),
                "Display", Display.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetRotationAngle(), "double");
    }
}
