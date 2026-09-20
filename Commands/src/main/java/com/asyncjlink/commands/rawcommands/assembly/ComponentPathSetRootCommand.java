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
import com.ptc.pfc.pfcAssembly.ComponentPath;

/**
 * ComponentPath.SetRoot &mdash; pfcAssembly.
 *
 * <pre>
 * void SetRoot(Assembly) throws jxthrowable
 * </pre>
 */
public final class ComponentPathSetRootCommand implements Command {

    @Override public String name() { return "ComponentPath.SetRoot"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "ComponentPath"; }
    @Override public String signature() { return "void SetRoot(Assembly) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentPath.SetRoot \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("ComponentPath"),
                        "The ComponentPath to act on.")
                .optional("assembly", JsonSchema.handle("Assembly"),
                        "Handle to a Assembly, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentPath target = Marshal.in(ctx, params.get("target"),
                "ComponentPath", ComponentPath.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Assembly assembly = Marshal.in(ctx, params.get("assembly"),
                "Assembly", Assembly.class, "assembly");
        target.SetRoot(assembly);
        return Marshal.ok();
    }
}
