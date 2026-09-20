/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.part;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcPart.Material;

/**
 * Material.SetModelDefByTests &mdash; pfcPart.
 *
 * <pre>
 * void SetModelDefByTests(Boolean) throws jxthrowable
 * </pre>
 */
public final class MaterialSetModelDefByTestsCommand implements Command {

    @Override public String name() { return "Material.SetModelDefByTests"; }
    @Override public String jlinkPackage() { return "pfcPart"; }
    @Override public String receiverType() { return "Material"; }
    @Override public String signature() { return "void SetModelDefByTests(Boolean) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Material.SetModelDefByTests \u2014 pfcPart")
                .required("target", JsonSchema.handle("Material"),
                        "The Material to act on.")
                .optional("flag", JsonSchema.bool(),
                        "Boolean value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Material target = Marshal.in(ctx, params.get("target"),
                "Material", Material.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Boolean flag = Marshal.in(ctx, params.get("flag"),
                "Boolean", Boolean.class, "flag");
        target.SetModelDefByTests(flag);
        return Marshal.ok();
    }
}
