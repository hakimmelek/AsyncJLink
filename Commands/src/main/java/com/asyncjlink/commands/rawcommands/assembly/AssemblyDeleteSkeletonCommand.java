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

/**
 * Assembly.DeleteSkeleton &mdash; pfcAssembly.
 *
 * <pre>
 * void DeleteSkeleton() throws jxthrowable
 * </pre>
 */
public final class AssemblyDeleteSkeletonCommand implements Command {

    @Override public String name() { return "Assembly.DeleteSkeleton"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "void DeleteSkeleton() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Assembly.DeleteSkeleton \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("Assembly"),
                        "The Assembly to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly target = Marshal.in(ctx, params.get("target"),
                "Assembly", Assembly.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        target.DeleteSkeleton();
        return Marshal.ok();
    }
}
