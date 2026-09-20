/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.model2d;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel2D.Model2D;

/**
 * Model2D.Regenerate &mdash; pfcModel2D.
 *
 * <pre>
 * void Regenerate() throws jxthrowable
 * </pre>
 */
public final class Model2DRegenerateCommand implements Command {

    @Override public String name() { return "Model2D.Regenerate"; }
    @Override public String jlinkPackage() { return "pfcModel2D"; }
    @Override public String receiverType() { return "Model2D"; }
    @Override public String signature() { return "void Regenerate() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model2D.Regenerate \u2014 pfcModel2D")
                .required("target", JsonSchema.handle("Model2D"),
                        "The Model2D to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model2D target = Marshal.in(ctx, params.get("target"),
                "Model2D", Model2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.Regenerate();
        return Marshal.ok();
    }
}
