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
import com.ptc.pfc.pfcBase.Transform3D;

/**
 * ComponentPath.SetTransform &mdash; pfcAssembly.
 *
 * <pre>
 * void SetTransform(boolean, Transform3D) throws jxthrowable
 * </pre>
 */
public final class ComponentPathSetTransformCommand implements Command {

    @Override public String name() { return "ComponentPath.SetTransform"; }
    @Override public String jlinkPackage() { return "pfcAssembly"; }
    @Override public String receiverType() { return "ComponentPath"; }
    @Override public String signature() { return "void SetTransform(boolean, Transform3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentPath.SetTransform \u2014 pfcAssembly")
                .required("target", JsonSchema.handle("ComponentPath"),
                        "The ComponentPath to act on.")
                .required("flag", JsonSchema.bool(),
                        "boolean value.")
                .optional("transform3D", JsonSchema.dataObject("Transform3D"),
                        "Transform3D options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentPath target = Marshal.in(ctx, params.get("target"),
                "ComponentPath", ComponentPath.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        boolean flag = Marshal.in(ctx, params.get("flag"),
                "boolean", boolean.class, "flag");
        Transform3D transform3D = Marshal.in(ctx, params.get("transform3D"),
                "Transform3D", Transform3D.class, "transform3D");
        target.SetTransform(flag, transform3D);
        return Marshal.ok();
    }
}
