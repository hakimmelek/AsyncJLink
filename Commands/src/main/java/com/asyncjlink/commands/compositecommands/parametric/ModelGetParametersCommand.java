package com.asyncjlink.commands.compositecommands.parametric;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcModelItem.ParamValue;
import com.ptc.pfc.pfcModelItem.Parameters;
import com.ptc.pfc.pfcUnits.Unit;

import java.util.regex.Pattern;

/**
 * Model.GetParameters — every parameter of a model, in one typed table.
 *
 * <p>Raw, this is a walk: list, then a {@code GetValue} and a type switch per parameter — roughly
 * four calls times the number of parameters.
 */
public final class ModelGetParametersCommand extends Composite {

    @Override public String name() { return "Model.GetParameters"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "GetParameters(target[, namePattern]) — composite"; }

    @Override
    public String description() {
        return "Every parameter of a model with its typed value, units, description and whether it "
                + "is designated or relation-driven. Replaces a per-parameter walk.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose parameters to read.")
                .optional("namePattern", JsonSchema.string(),
                        "Name filter; * and ? are wildcards.")
                .optional("designatedOnly", JsonSchema.bool(),
                        "Only parameters designated for PDM. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        Pattern pattern = glob(params.getString("namePattern", null));
        boolean designatedOnly = params.getBoolean("designatedOnly", false);

        JsonArray out = new JsonArray();
        Parameters list = model.ListParams();
        int scanned = 0;
        if (list != null) {
            for (int i = 0; i < list.getarraysize(); i++) {
                Parameter p = list.get(i);
                if (p == null) {
                    continue;
                }
                scanned++;
                String pname = namedItemName(p);
                if (!matches(pattern, pname)) {
                    continue;
                }
                JsonObject entry = describe(ctx, p, pname);
                if (designatedOnly && !entry.getBoolean("designated", false)) {
                    continue;
                }
                out.add(entry);
            }
        }

        return JsonObject.of(
                "model", modelRef(ctx, model),
                "matched", Integer.valueOf(out.size()),
                "scanned", Integer.valueOf(scanned),
                "parameters", out);
    }

    private static JsonObject describe(CreoContext ctx, Parameter p, String pname) throws jxthrowable {
        JsonObject entry = JsonObject.of(
                "$handle", ctx.handles().handleFor(p, "Parameter"),
                "name", pname);
        // One GetValue, used twice: each call is a round trip to Creo.
        ParamValue value = p.GetValue();
        entry.putIfPresent("value", paramValue(value));
        entry.putIfPresent("type", paramValueType(value));
        entry.putIfPresent("units", unitName(p));
        entry.putIfPresent("description", safeDescription(p));
        entry.putIfPresent("driverType", safeDriverType(p));
        entry.put("designated", Boolean.valueOf(safeFlag(p, Flag.DESIGNATED)));
        entry.put("relationDriven", Boolean.valueOf(safeFlag(p, Flag.RELATION_DRIVEN)));
        entry.put("modified", Boolean.valueOf(safeFlag(p, Flag.MODIFIED)));
        return entry;
    }

    private enum Flag { DESIGNATED, RELATION_DRIVEN, MODIFIED }

    private static boolean safeFlag(Parameter p, Flag flag) {
        try {
            switch (flag) {
                case DESIGNATED:
                    return p.GetIsDesignated();
                case RELATION_DRIVEN:
                    return p.GetIsRelationDriven();
                default:
                    return p.GetIsModified();
            }
        } catch (jxthrowable | RuntimeException e) {
            return false;
        }
    }

    private static String unitName(Parameter p) {
        try {
            Unit u = p.GetUnits();
            return u == null ? null : u.GetName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeDescription(Parameter p) {
        try {
            String d = p.GetDescription();
            return d == null || d.isEmpty() ? null : d;
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeDriverType(Parameter p) {
        try {
            return Enums.toJson(p.GetDriverType());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
