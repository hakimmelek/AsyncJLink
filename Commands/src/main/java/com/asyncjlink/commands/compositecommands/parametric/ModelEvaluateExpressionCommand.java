package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ParamValue;

/**
 * Model.EvaluateExpression — evaluate an expression in Creo's own semantics, changing nothing.
 *
 * <p>Cheap, safe, and lets a caller check its arithmetic against the model before committing to a
 * relation or a dimension change.
 */
public final class ModelEvaluateExpressionCommand extends Composite {

    @Override public String name() { return "Model.EvaluateExpression"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "EvaluateExpression(target, expression) — composite"; }

    @Override
    public String description() {
        return "Evaluate a Creo relation expression against a model without modifying anything — "
                + "check arithmetic before writing a relation or setting a dimension.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model to evaluate against.")
                .required("expression", JsonSchema.string(),
                        "Expression in Creo relation syntax, e.g. \"d12 * 2 + 1\".");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        String expression = params.getString("expression", null);
        if (expression == null || expression.isEmpty()) {
            throw new CommandException("Field 'expression' is required", "invalid_params");
        }

        JsonObject out = JsonObject.of(
                "model", modelRef(ctx, model),
                "expression", expression);
        try {
            ParamValue value = model.EvaluateExpression(expression);
            out.put("ok", Boolean.TRUE);
            out.putIfPresent("value", paramValue(value));
            out.putIfPresent("type", paramValueType(value));
        } catch (jxthrowable | RuntimeException e) {
            // A rejected expression is an ordinary answer to "is this valid?", not a failure of the
            // command, so it is reported in the envelope rather than thrown.
            out.put("ok", Boolean.FALSE);
            out.put("error", rootMessage(e));
        }
        return out;
    }
}
