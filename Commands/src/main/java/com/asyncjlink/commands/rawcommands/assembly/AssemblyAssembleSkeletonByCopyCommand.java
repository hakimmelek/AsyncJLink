/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.assembly;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Assembly.AssembleSkeletonByCopy &mdash; pfcAssembly.
 *
 * <pre>
 * void AssembleSkeletonByCopy(String, Solid) throws jxthrowable
 * </pre>
 */
public final class AssemblyAssembleSkeletonByCopyCommand implements Command {

    @Override public String name() { return "Assembly.AssembleSkeletonByCopy"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "void AssembleSkeletonByCopy(String, Solid) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Assembly.AssembleSkeletonByCopy \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("Assembly"),
                        "The Assembly to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("solid", JsonSchema.handle("Solid"),
                        "Handle to a Solid, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly target = Marshal.in(ctx, params.get("target"),
                "Assembly", Assembly.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        Solid solid = Marshal.in(ctx, params.get("solid"),
                "Solid", Solid.class, "solid");
        target.AssembleSkeletonByCopy(value, solid);
        return Marshal.ok();
    }
}
