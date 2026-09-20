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
import com.ptc.pfc.pfcBase.Transform3D;
import com.ptc.pfc.pfcDisplay.DisplayList3D;

/**
 * DisplayList3D.Display &mdash; pfcDisplay.
 *
 * <pre>
 * void Display(Transform3D) throws jxthrowable
 * </pre>
 */
public final class DisplayList3DDisplayCommand implements Command {

    @Override public String name() { return "DisplayList3D.Display"; }
    @Override public String jlinkPackage() { return "pfcDisplay"; }
    @Override public String receiverType() { return "DisplayList3D"; }
    @Override public String signature() { return "void Display(Transform3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DisplayList3D.Display \u2014 pfcDisplay")
                .required("target", JsonSchema.handle("DisplayList3D"),
                        "The DisplayList3D to act on.")
                .optional("transform3D", JsonSchema.dataObject("Transform3D"),
                        "Transform3D options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DisplayList3D target = Marshal.in(ctx, params.get("target"),
                "DisplayList3D", DisplayList3D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Transform3D transform3D = Marshal.in(ctx, params.get("transform3D"),
                "Transform3D", Transform3D.class, "transform3D");
        target.Display(transform3D);
        return Marshal.ok();
    }
}
