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
import com.ptc.pfc.pfcPart.Part;

/**
 * Part.GetCurrentMaterial &mdash; pfcPart.
 *
 * <pre>
 * Material GetCurrentMaterial() throws jxthrowable
 * </pre>
 */
public final class PartGetCurrentMaterialCommand implements Command {

    @Override public String name() { return "Part.GetCurrentMaterial"; }
    @Override public String jlinkPackage() { return "pfcPart"; }
    @Override public String receiverType() { return "Part"; }
    @Override public String signature() { return "Material GetCurrentMaterial() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Part.GetCurrentMaterial \u2014 pfcPart")
                .required("target", JsonSchema.handle("Part"),
                        "The Part to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Part target = Marshal.in(ctx, params.get("target"),
                "Part", Part.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetCurrentMaterial(), "Material");
    }
}
