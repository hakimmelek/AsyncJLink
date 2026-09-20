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
import com.ptc.pfc.pfcAssembly.ComponentPath;
import com.ptc.pfc.pfcSimpRep.SimpRep;

/**
 * ComponentPath.GetIsVisible &mdash; pfcAssembly.
 *
 * <pre>
 * boolean GetIsVisible(SimpRep) throws jxthrowable
 * </pre>
 */
public final class ComponentPathGetIsVisibleCommand implements Command {

    @Override public String name() { return "ComponentPath.GetIsVisible"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "ComponentPath"; }
    @Override public String signature() { return "boolean GetIsVisible(SimpRep) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentPath.GetIsVisible \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("ComponentPath"),
                        "The ComponentPath to act on.")
                .optional("simpRep", JsonSchema.handle("SimpRep"),
                        "Handle to a SimpRep, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentPath target = Marshal.in(ctx, params.get("target"),
                "ComponentPath", ComponentPath.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        SimpRep simpRep = Marshal.in(ctx, params.get("simpRep"),
                "SimpRep", SimpRep.class, "simpRep");
        return Marshal.result(ctx, target.GetIsVisible(simpRep), "boolean");
    }
}
