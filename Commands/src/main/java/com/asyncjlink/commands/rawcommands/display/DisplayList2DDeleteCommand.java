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
import com.ptc.pfc.pfcDisplay.DisplayList2D;

/**
 * DisplayList2D.Delete &mdash; pfcDisplay.
 *
 * <pre>
 * void Delete() throws jxthrowable
 * </pre>
 */
public final class DisplayList2DDeleteCommand implements Command {

    @Override public String name() { return "DisplayList2D.Delete"; }
    @Override public String jlinkPackage() { return "pfcDisplay"; }
    @Override public String receiverType() { return "DisplayList2D"; }
    @Override public String signature() { return "void Delete() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("DisplayList2D.Delete \u2014 pfcDisplay")
                .required("target", JsonSchema.handle("DisplayList2D"),
                        "The DisplayList2D to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        DisplayList2D target = Marshal.in(ctx, params.get("target"),
                "DisplayList2D", DisplayList2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.Delete();
        return Marshal.ok();
    }
}
