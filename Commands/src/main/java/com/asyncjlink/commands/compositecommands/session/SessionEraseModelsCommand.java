package com.asyncjlink.commands.compositecommands.session;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Session.EraseModels — clear models out of session.
 *
 * <p>Erasing only removes a model from memory; it never touches the file on disk. It does, however,
 * discard unsaved changes, so modified models are skipped unless the caller says otherwise.
 */
public final class SessionEraseModelsCommand extends Composite {

    @Override public String name() { return "Session.EraseModels"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "EraseModels([names, undisplayedOnly]) — composite"; }

    @Override
    public String description() {
        return "Erase models from session memory (files on disk are untouched). Modified models are "
                + "skipped unless eraseModified is set, because erasing discards unsaved changes.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .optional("names", JsonSchema.array(JsonSchema.string()),
                        "Model names to erase. Omit to use undisplayedOnly.")
                .optional("undisplayedOnly", JsonSchema.bool(),
                        "Erase every model with no open window. Default false.")
                .optional("withDependencies", JsonSchema.bool(),
                        "Erase each model's dependencies too. Default false.")
                .optional("eraseModified", JsonSchema.bool(),
                        "Erase even models with unsaved changes, discarding them. Default false.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would be erased without erasing. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        boolean dryRun = dryRun(params);
        boolean withDeps = params.getBoolean("withDependencies", false);
        boolean eraseModified = params.getBoolean("eraseModified", false);

        JsonArray results = new JsonArray();

        if (params.getBoolean("undisplayedOnly", false)) {
            if (dryRun) {
                return JsonObject.of(
                        "dryRun", Boolean.TRUE,
                        "action", "wouldEraseUndisplayedModels");
            }
            ctx.session().EraseUndisplayedModels();
            return JsonObject.of(
                    "dryRun", Boolean.FALSE,
                    "action", "erasedUndisplayedModels");
        }

        List<String> names = new ArrayList<>();
        JsonArray asked = params.getArray("names");
        if (asked != null) {
            for (int i = 0; i < asked.size(); i++) {
                names.add(String.valueOf(asked.get(i)));
            }
        }
        if (names.isEmpty()) {
            return JsonObject.of(
                    "erased", Integer.valueOf(0),
                    "note", "Nothing to do: pass 'names', or set 'undisplayedOnly'.");
        }

        int erased = 0;
        int skipped = 0;
        for (String ref : names) {
            JsonObject entry = JsonObject.of("requested", ref);
            Model m = ctx.resolveModel(ref);
            if (m == null) {
                entry.put("action", "skipped");
                entry.put("reason", "not in session");
                skipped++;
                results.add(entry);
                continue;
            }
            entry.putIfPresent("model", modelRef(ctx, m));

            boolean modified = false;
            try {
                modified = m.GetIsModified();
            } catch (jxthrowable | RuntimeException ignored) {
                // Treat an unreadable state as unmodified; the erase itself will fail if it matters.
            }
            if (modified && !eraseModified) {
                entry.put("action", "skipped");
                entry.put("reason", "has unsaved changes; set eraseModified to discard them");
                skipped++;
                results.add(entry);
                continue;
            }

            if (dryRun) {
                entry.put("action", "wouldErase");
                erased++;
                results.add(entry);
                continue;
            }

            try {
                if (withDeps) {
                    m.EraseWithDependencies();
                } else {
                    m.Erase();
                }
                entry.put("action", "erased");
                erased++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
                skipped++;
            }
            results.add(entry);
        }

        return JsonObject.of(
                "dryRun", Boolean.valueOf(dryRun),
                "erased", Integer.valueOf(erased),
                "skipped", Integer.valueOf(skipped),
                "models", results);
    }
}
