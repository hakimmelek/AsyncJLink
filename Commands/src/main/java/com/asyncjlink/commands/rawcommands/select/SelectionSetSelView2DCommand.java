/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.select;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcView2D.View2D;

/**
 * Selection.SetSelView2D &mdash; pfcSelect.
 *
 * <pre>
 * void SetSelView2D(View2D) throws jxthrowable
 * </pre>
 */
public final class SelectionSetSelView2DCommand implements Command {

    @Override public String name() { return "Selection.SetSelView2D"; }
    @Override public String jlinkPackage() { return "pfcSelect"; }
    @Override public String receiverType() { return "Selection"; }
    @Override public String signature() { return "void SetSelView2D(View2D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Selection.SetSelView2D \u2014 pfcSelect")
                .required("target", JsonSchema.handle("Selection"),
                        "The Selection to act on.")
                .optional("view2D", JsonSchema.handle("View2D"),
                        "Handle to a View2D, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Selection target = Marshal.in(ctx, params.get("target"),
                "Selection", Selection.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        View2D view2D = Marshal.in(ctx, params.get("view2D"),
                "View2D", View2D.class, "view2D");
        target.SetSelView2D(view2D);
        return Marshal.ok();
    }
}
