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
import com.ptc.pfc.pfcDisplay.Display;
import com.ptc.pfc.pfcDisplay.DisplayListener;

/**
 * Display.CreateDisplayList2D &mdash; pfcDisplay.
 *
 * <pre>
 * DisplayList2D CreateDisplayList2D(int, ScreenTransform, DisplayListener) throws jxthrowable
 * </pre>
 *
 * <p>Catalogued but not invocable: it requires a live DisplayListener callback.
 */
public final class DisplayCreateDisplayList2DCommand implements Command {

    @Override public String name() { return "Display.CreateDisplayList2D"; }
    @Override public String jlinkPackage() { return "pfcDisplay"; }
    @Override public String receiverType() { return "Display"; }
    @Override public String signature() { return "DisplayList2D CreateDisplayList2D(int, ScreenTransform, DisplayListener) throws jxthrowable"; }

    @Override public boolean isInvocable() { return false; }
    @Override public String unsupportedReason() { return "it takes a DisplayListener callback, which has no JSON representation; drive it from an in-process J-Link application instead"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Display.CreateDisplayList2D \u2014 pfcDisplay")
                .required("target", JsonSchema.handle("Display"),
                        "The Display to act on.")
                .required("value", JsonSchema.integer(),
                        "int value.")
                .optional("screenTransform", JsonSchema.dataObject("ScreenTransform"),
                        "ScreenTransform options object; its fields are passed to the pfc factory and setters.")
                .optional("displayListener", JsonSchema.dataObject("DisplayListener"),
                        "DisplayListener callback (not supplyable over JSON).")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        throw CommandException.unsupported(
                "Display.CreateDisplayList2D", unsupportedReason());
    }
}
