package com.asyncjlink.commands.compositecommands.session;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.Models;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Session.GetContext — what is open, where, and in what units.
 *
 * <p>The natural first call for an agent, and the answer to "what am I even looking at" for a
 * script.
 */
public final class SessionGetContextCommand extends Composite {

    @Override public String name() { return "Session.GetContext"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "GetContext() — composite"; }

    @Override
    public String description() {
        return "Everything about the current session in one call: the current model, every model in "
                + "session, the working directory and the unit system. Start here.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name());
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        JsonObject out = new JsonObject();

        Model current = ctx.session().GetCurrentModel();
        out.putIfPresent("currentModel", modelRef(ctx, current));
        if (current instanceof Solid) {
            out.putIfPresent("unitSystem", unitSystem((Solid) current));
        }
        if (current != null) {
            out.put("modified", Boolean.valueOf(current.GetIsModified()));
        }

        out.putIfPresent("workingDirectory", ctx.session().GetCurrentDirectory());

        JsonArray models = new JsonArray();
        Models all = ctx.session().ListModels();
        if (all != null) {
            for (int i = 0; i < all.getarraysize(); i++) {
                Model m = all.get(i);
                if (m == null) {
                    continue;
                }
                JsonObject ref = modelRef(ctx, m);
                ref.put("modified", Boolean.valueOf(m.GetIsModified()));
                models.add(ref);
            }
        }
        out.put("modelCount", Integer.valueOf(models.size()));
        out.put("models", models);
        return out;
    }
}
