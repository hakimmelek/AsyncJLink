/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.componentfeat;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.ComponentPath;
import com.ptc.pfc.pfcComponentFeat.ComponentConstraints;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;

/**
 * ComponentFeat.SetConstraints &mdash; pfcComponentFeat.
 *
 * <pre>
 * void SetConstraints(ComponentConstraints, ComponentPath) throws jxthrowable
 * </pre>
 */
public final class ComponentFeatSetConstraintsCommand implements Command {

    @Override public String name() { return "ComponentFeat.SetConstraints"; }
    @Override public String jlinkPackage() { return "pfcComponentFeat"; }
    @Override public String receiverType() { return "ComponentFeat"; }
    @Override public String signature() { return "void SetConstraints(ComponentConstraints, ComponentPath) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentFeat.SetConstraints \u2014 pfcComponentFeat")
                .required("target", JsonSchema.handle("ComponentFeat"),
                        "The ComponentFeat to act on.")
                .optional("componentConstraints", JsonSchema.sequence("ComponentConstraints", JsonSchema.dataObject("ComponentConstraint")),
                        "Array of ComponentConstraint.")
                .optional("componentPath", JsonSchema.handle("ComponentPath"),
                        "Handle to a ComponentPath, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentFeat target = Marshal.in(ctx, params.get("target"),
                "ComponentFeat", ComponentFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ComponentConstraints componentConstraints = Marshal.in(ctx, params.get("componentConstraints"),
                "ComponentConstraints", ComponentConstraints.class, "componentConstraints");
        ComponentPath componentPath = Marshal.in(ctx, params.get("componentPath"),
                "ComponentPath", ComponentPath.class, "componentPath");
        target.SetConstraints(componentConstraints, componentPath);
        return Marshal.ok();
    }
}
