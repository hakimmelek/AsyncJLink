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

import com.ptc.cipjava.intseq;
import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.ComponentPath;

/**
 * ComponentPath.SetComponentIds &mdash; pfcAssembly.
 *
 * <pre>
 * void SetComponentIds(intseq) throws jxthrowable
 * </pre>
 */
public final class ComponentPathSetComponentIdsCommand implements Command {

    @Override public String name() { return "ComponentPath.SetComponentIds"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "ComponentPath"; }
    @Override public String signature() { return "void SetComponentIds(intseq) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentPath.SetComponentIds \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("ComponentPath"),
                        "The ComponentPath to act on.")
                .optional("values", JsonSchema.sequence("intseq", JsonSchema.integer()),
                        "Array of int.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentPath target = Marshal.in(ctx, params.get("target"),
                "ComponentPath", ComponentPath.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        intseq values = Marshal.in(ctx, params.get("values"),
                "intseq", intseq.class, "values");
        target.SetComponentIds(values);
        return Marshal.ok();
    }
}
