package com.asyncjlink.commands.compositecommands.session;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.Models;

/**
 * Session.SaveAll — save every modified model in session.
 *
 * <p>An assembly edit dirties its children, and saving only the top level is a routine way to lose
 * work. Saving a single model needs no composite: {@code Model.Save} is already one raw call, and
 * {@code Model.SaveChecked} covers the guarded case. This exists only for the multi-model case.
 */
public final class SessionSaveAllCommand extends Composite {

    @Override public String name() { return "Session.SaveAll"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "SaveAll([dryRun]) — composite"; }

    @Override
    public String description() {
        return "Save every modified model in session, reporting per model what was saved and what "
                + "was skipped. Use dryRun to see what would be saved without writing anything.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would be saved without saving. Default false.")
                .optional("stopOnError", JsonSchema.bool(),
                        "Stop at the first failure instead of continuing. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        boolean dryRun = dryRun(params);
        boolean stopOnError = params.getBoolean("stopOnError", false);

        JsonArray results = new JsonArray();
        int saved = 0;
        int skipped = 0;
        int failed = 0;

        Models all = ctx.session().ListModels();
        if (all != null) {
            for (int i = 0; i < all.getarraysize(); i++) {
                Model m = all.get(i);
                if (m == null) {
                    continue;
                }
                JsonObject entry = modelRef(ctx, m);

                boolean modified;
                try {
                    modified = m.GetIsModified();
                } catch (jxthrowable | RuntimeException e) {
                    modified = false;
                }
                entry.put("modified", Boolean.valueOf(modified));

                if (!modified) {
                    entry.put("action", "skipped");
                    entry.put("reason", "not modified");
                    skipped++;
                    results.add(entry);
                    continue;
                }

                String blocker = saveBlocker(m);
                if (blocker != null) {
                    entry.put("action", "skipped");
                    entry.put("reason", blocker);
                    skipped++;
                    results.add(entry);
                    continue;
                }

                if (dryRun) {
                    entry.put("action", "wouldSave");
                    saved++;
                    results.add(entry);
                    continue;
                }

                try {
                    m.Save();
                    entry.put("action", "saved");
                    saved++;
                } catch (jxthrowable | RuntimeException e) {
                    entry.put("action", "failed");
                    entry.put("error", rootMessage(e));
                    failed++;
                }
                results.add(entry);
                if (failed > 0 && stopOnError) {
                    break;
                }
            }
        }

        return JsonObject.of(
                "dryRun", Boolean.valueOf(dryRun),
                "saved", Integer.valueOf(saved),
                "skipped", Integer.valueOf(skipped),
                "failed", Integer.valueOf(failed),
                "models", results);
    }

    /** Why this model cannot be saved, or null when it can. */
    private static String saveBlocker(Model m) {
        try {
            if (!m.CheckIsSaveAllowed(false)) {
                return "Creo does not allow saving this model";
            }
        } catch (jxthrowable | RuntimeException e) {
            return "save check failed: " + rootMessage(e);
        }
        return null;
    }
}
