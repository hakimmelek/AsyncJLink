package com.asyncjlink.commands.compositecommands.presentation;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcAssembly.ExplodedState;

/**
 * Assembly.SetExplodedView — explode an assembly for documentation, or return it to assembled.
 *
 * <p>One call instead of the explode/get-active-state/verify sequence, and the natural companion to
 * {@code Model.CaptureImage} for producing assembly-instruction images.
 */
public final class AssemblySetExplodedViewCommand extends Composite {

    @Override public String name() { return "Assembly.SetExplodedView"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "SetExplodedView(target[, explode]) — composite"; }

    @Override
    public String description() {
        return "Explode an assembly, or return it to its assembled state, and report which "
                + "exploded state is active.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Assembly", "The assembly to explode or un-explode.")
                .optional("explode", JsonSchema.bool(), "Explode (true) or assemble (false). Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly assembly = requireAssembly(ctx, params);
        boolean explode = params.getBoolean("explode", true);

        if (explode) {
            assembly.Explode();
        } else {
            assembly.UnExplode();
        }

        JsonObject out = JsonObject.of("model", modelRef(ctx, assembly));
        boolean isExploded = false;
        try {
            isExploded = assembly.GetIsExploded();
        } catch (jxthrowable | RuntimeException ignored) {
            // Fall through with the requested state below.
        }
        out.put("exploded", Boolean.valueOf(isExploded));

        if (isExploded) {
            out.putIfPresent("activeState", stateName(assembly));
        }
        return out;
    }

    private static String stateName(Assembly assembly) {
        try {
            ExplodedState state = assembly.GetActiveExplodedState();
            if (state == null) {
                state = assembly.GetDefaultExplodedState();
            }
            return state == null ? null : state.GetName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
