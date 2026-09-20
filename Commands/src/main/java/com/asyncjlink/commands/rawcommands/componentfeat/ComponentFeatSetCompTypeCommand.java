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
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcComponentFeat.ComponentType;

/**
 * ComponentFeat.SetCompType &mdash; pfcComponentFeat.
 *
 * <pre>
 * void SetCompType(ComponentType) throws jxthrowable
 * </pre>
 */
public final class ComponentFeatSetCompTypeCommand implements Command {

    @Override public String name() { return "ComponentFeat.SetCompType"; }
    @Override public String jlinkPackage() { return "pfcComponentFeat"; }
    @Override public String receiverType() { return "ComponentFeat"; }
    @Override public String signature() { return "void SetCompType(ComponentType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("ComponentFeat.SetCompType \u2014 pfcComponentFeat")
                .required("target", JsonSchema.handle("ComponentFeat"),
                        "The ComponentFeat to act on.")
                .optional("componentType", JsonSchema.enumOf("ComponentType", "COMPONENT_WORKPIECE", "COMPONENT_REF_MODEL", "COMPONENT_FIXTURE", "COMPONENT_MOLD_BASE", "COMPONENT_MOLD_COMP", "COMPONENT_MOLD_ASSEM", "COMPONENT_GEN_ASSEM", "COMPONENT_CAST_ASSEM", "COMPONENT_DIE_BLOCK", "COMPONENT_DIE_COMP", "COMPONENT_SAND_CORE", "COMPONENT_CAST_RESULT", "COMPONENT_FROM_MOTION", "COMPONENT_NO_DEF_ASSUM", "COMPONENT_NONE"),
                        "One of the ComponentType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        ComponentFeat target = Marshal.in(ctx, params.get("target"),
                "ComponentFeat", ComponentFeat.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        ComponentType componentType = Marshal.in(ctx, params.get("componentType"),
                "ComponentType", ComponentType.class, "componentType");
        target.SetCompType(componentType);
        return Marshal.ok();
    }
}
