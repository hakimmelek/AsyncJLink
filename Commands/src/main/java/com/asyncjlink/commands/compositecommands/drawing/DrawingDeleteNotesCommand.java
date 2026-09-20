package com.asyncjlink.commands.compositecommands.drawing;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailNoteItem;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;

/**
 * Drawing.DeleteNotes — remove notes by id.
 *
 * <p>{@code DetailNoteItem.Remove} is real and already generated; this is the bulk, reported
 * version — same shape as {@code Solid.DeleteFeatures}, including {@code dryRun} defaulting to
 * {@code true} for the same reason (rule 3: no undo).
 */
public final class DrawingDeleteNotesCommand extends Composite {

    @Override public String name() { return "Drawing.DeleteNotes"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "DeleteNotes(target, ids[, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Remove notes by id. dryRun defaults to TRUE -- pass dryRun=false to actually "
                + "delete. There is no undo.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing to remove notes from.")
                .required("ids", JsonSchema.array(JsonSchema.integer()), "Note ids to remove.")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would happen without doing it. Defaults to TRUE, because the "
                                + "change cannot be undone.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        JsonArray ids = params.getArray("ids");
        if (ids == null || ids.size() == 0) {
            throw new CommandException("Field 'ids' must list at least one note id", "invalid_params");
        }
        boolean dryRun = params.getBoolean("dryRun", true);

        java.util.Map<Integer, DetailNoteItem> byId = new java.util.LinkedHashMap<>();
        for (ModelItem item : items(drawing, ModelItemType.ITEM_DTL_NOTE)) {
            if (item instanceof DetailNoteItem) {
                byId.put(Integer.valueOf(item.GetId()), (DetailNoteItem) item);
            }
        }

        JsonArray results = new JsonArray();
        int removed = 0;
        int rejected = 0;

        for (int i = 0; i < ids.size(); i++) {
            int id = ((Number) ids.get(i)).intValue();
            JsonObject entry = JsonObject.of("id", Integer.valueOf(id));
            DetailNoteItem note = byId.get(Integer.valueOf(id));
            if (note == null) {
                entry.put("action", "rejected");
                entry.put("reason", "no note with this id on this drawing");
                rejected++;
                results.add(entry);
                continue;
            }
            entry.putIfPresent("text", DrawingGetNotesCommand.safeText(note));

            if (dryRun) {
                entry.put("action", "wouldRemove");
                removed++;
                results.add(entry);
                continue;
            }
            try {
                note.Remove();
                entry.put("action", "removed");
                removed++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
                rejected++;
            }
            results.add(entry);
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "dryRun", Boolean.valueOf(dryRun),
                "removed", Integer.valueOf(removed),
                "rejected", Integer.valueOf(rejected),
                "notes", results);
    }
}
