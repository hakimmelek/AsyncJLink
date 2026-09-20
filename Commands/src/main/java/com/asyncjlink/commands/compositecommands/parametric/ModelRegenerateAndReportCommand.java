package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Model.RegenerateAndReport — regenerate, then say which features failed, by name.
 *
 * <p>The raw call either throws or leaves the model carrying failures the caller never learns
 * about.
 */
public final class ModelRegenerateAndReportCommand extends Composite {

    @Override public String name() { return "Model.RegenerateAndReport"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "RegenerateAndReport(target) — composite"; }

    @Override
    public String description() {
        return "Regenerate a part, assembly or drawing and report which features failed, by name "
                + "and id, rather than leaving the caller to discover it later.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to regenerate.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        JsonObject out = JsonObject.of("model", modelRef(ctx, model));

        if (model instanceof Solid) {
            Solid solid = (Solid) model;
            JsonObject regen = regenerate(ctx, solid);
            out.put("regeneration", regen);
            boolean ok = regen.getBoolean("regenerated", false)
                    && regen.getInt("failedFeatureCount", 0) == 0;
            out.put("ok", Boolean.valueOf(ok));
            return out;
        }

        if (model instanceof Model2D) {
            try {
                ((Model2D) model).Regenerate();
                out.put("ok", Boolean.TRUE);
                out.put("regeneration", JsonObject.of("regenerated", Boolean.TRUE));
            } catch (jxthrowable | RuntimeException e) {
                out.put("ok", Boolean.FALSE);
                out.put("regeneration", JsonObject.of(
                        "regenerated", Boolean.FALSE,
                        "regenerationError", rootMessage(e)));
            }
            return out;
        }

        throw new CommandException(
                "Field 'target' must be a part, assembly or drawing, but was a " + typeOf(model),
                "invalid_params");
    }
}
