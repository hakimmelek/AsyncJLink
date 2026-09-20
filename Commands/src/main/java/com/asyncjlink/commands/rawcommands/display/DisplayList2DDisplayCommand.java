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
import com.ptc.pfc.pfcBase.ScreenTransform;
import com.ptc.pfc.pfcDisplay.DisplayList2D;

/**
 * DisplayList2D.Display &mdash; pfcDisplay.
 *
 * <pre>
 * void Display(ScreenTransform) throws jxthrowable
 * </pre>
 */
public final class DisplayList2DDisplayCommand implements Command {

    @Override public String name() { return "DisplayList2D.Display"; }
    @Override public String jlinkPackage() { return "pfcDisplay"; }
    @Override public String receiverType() { return "DisplayList2D"; }
    @Override public String signature() { return "void Display(ScreenTransform) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DisplayList2D.Display \u2014 pfcDisplay")
                .required("target", JsonSchema.handle("DisplayList2D"),
                        "The DisplayList2D to act on.")
                .optional("screenTransform", JsonSchema.dataObject("ScreenTransform"),
                        "ScreenTransform options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DisplayList2D target = Marshal.in(ctx, params.get("target"),
                "DisplayList2D", DisplayList2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ScreenTransform screenTransform = Marshal.in(ctx, params.get("screenTransform"),
                "ScreenTransform", ScreenTransform.class, "screenTransform");
        target.Display(screenTransform);
        return Marshal.ok();
    }
}
