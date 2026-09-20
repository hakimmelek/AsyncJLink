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
 * Model2D.GetViewByName &mdash; pfcModel2D.
 *
 * <pre>
 * View2D GetViewByName(String) throws jxthrowable
 * </pre>
 */
public final class Model2DGetViewByNameCommand implements Command {

    @Override public String name() { return "Model2D.GetViewByName"; }
    @Override public String jlinkPackage() { return "pfcModel2D"; }
    @Override public String receiverType() { return "Model2D"; }
    @Override public String signature() { return "View2D GetViewByName(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model2D.GetViewByName \u2014 pfcModel2D")
                .required("target", JsonSchema.handle("Model2D"),
                        "The Model2D to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model2D target = Marshal.in(ctx, params.get("target"),
                "Model2D", Model2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        return Marshal.result(ctx, target.GetViewByName(value), "View2D");
    }
}
