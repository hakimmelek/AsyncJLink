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
import com.ptc.pfc.pfcBase.Transform3D;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Assembly.AssembleComponent &mdash; pfcAssembly.
 *
 * <pre>
 * Feature AssembleComponent(Solid, Transform3D) throws jxthrowable
 * </pre>
 */
public final class AssemblyAssembleComponentCommand implements Command {

    @Override public String name() { return "Assembly.AssembleComponent"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "Feature AssembleComponent(Solid, Transform3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Assembly.AssembleComponent \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("Assembly"),
                        "The Assembly to act on.")
                .optional("solid", JsonSchema.handle("Solid"),
                        "Handle to a Solid, as returned by an earlier command.")
                .optional("transform3D", JsonSchema.dataObject("Transform3D"),
                        "Transform3D options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly target = Marshal.in(ctx, params.get("target"),
                "Assembly", Assembly.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Solid solid = Marshal.in(ctx, params.get("solid"),
                "Solid", Solid.class, "solid");
        Transform3D transform3D = Marshal.in(ctx, params.get("transform3D"),
                "Transform3D", Transform3D.class, "transform3D");
        return Marshal.result(ctx, target.AssembleComponent(solid, transform3D), "Feature");
    }
}
