/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.window;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.ScreenTransform;
import com.ptc.pfc.pfcWindow.Window;

/**
 * Window.SetScreenTransform &mdash; pfcWindow.
 *
 * <pre>
 * void SetScreenTransform(ScreenTransform) throws jxthrowable
 * </pre>
 */
public final class WindowSetScreenTransformCommand implements Command {

    @Override public String name() { return "Window.SetScreenTransform"; }
    @Override public String jlinkPackage() { return "pfcWindow"; }
    @Override public String receiverType() { return "Window"; }
    @Override public String signature() { return "void SetScreenTransform(ScreenTransform) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Window.SetScreenTransform \u2014 pfcWindow")
                .required("target", JsonSchema.handle("Window"),
                        "The Window to act on.")
                .optional("screenTransform", JsonSchema.dataObject("ScreenTransform"),
                        "ScreenTransform options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Window target = Marshal.in(ctx, params.get("target"),
                "Window", Window.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ScreenTransform screenTransform = Marshal.in(ctx, params.get("screenTransform"),
                "ScreenTransform", ScreenTransform.class, "screenTransform");
        target.SetScreenTransform(screenTransform);
        return Marshal.ok();
    }
}
