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
import com.ptc.pfc.pfcView2D.View2D;
import com.ptc.pfc.pfcView2D.ViewDisplay;

/**
 * View2D.SetDisplay &mdash; pfcView2D.
 *
 * <pre>
 * void SetDisplay(ViewDisplay) throws jxthrowable
 * </pre>
 */
public final class View2DSetDisplayCommand implements Command {

    @Override public String name() { return "View2D.SetDisplay"; }
    @Override public String jlinkPackage() { return "pfcView2D"; }
    @Override public String receiverType() { return "View2D"; }
    @Override public String signature() { return "void SetDisplay(ViewDisplay) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("View2D.SetDisplay \u2014 pfcView2D")
                .required("target", JsonSchema.handle("View2D"),
                        "The View2D to act on.")
                .optional("viewDisplay", JsonSchema.dataObject("ViewDisplay"),
                        "ViewDisplay options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        View2D target = Marshal.in(ctx, params.get("target"),
                "View2D", View2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ViewDisplay viewDisplay = Marshal.in(ctx, params.get("viewDisplay"),
                "ViewDisplay", ViewDisplay.class, "viewDisplay");
        target.SetDisplay(viewDisplay);
        return Marshal.ok();
    }
}
