package com.asyncjlink.commands.compositecommands.drawing;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailNoteInstructions;
import com.ptc.pfc.pfcDetail.DetailNoteItem;
import com.ptc.pfc.pfcDetail.DetailText;
import com.ptc.pfc.pfcDetail.DetailTextLine;
import com.ptc.pfc.pfcDetail.DetailTextLines;
import com.ptc.pfc.pfcDetail.DetailTexts;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;

import java.util.List;

/**
 * Drawing.SetNoteText — edit the text of existing notes by id.
 *
 * <p>{@code DetailNoteItem.Modify} takes a full {@code DetailNoteInstructions}, not a bare string, so
 * this reads a note's existing instructions and writes new text into its existing structure rather
 * than building one from nothing — there is no factory for a bare {@code DetailText}/{@code
 * DetailTextLine} in the dictionary, only for the sequences that hold them
 * ({@code DetailTextLines.create()}/{@code DetailTexts.create()}). Concretely: a caller's text lines
 * are written one-to-one into the note's existing {@code DetailTextLine}s, each line's first
 * {@code DetailText} run getting the new text (later runs on the same line, if any, are left as
 * they were, so per-run formatting on a multi-run line survives). <strong>This can only replace text
 * in lines that already exist</strong> — supplying more lines than the note has is reported as
 * rejected rather than guessed at, since there is no way to create a new line from this API.
 */
public final class DrawingSetNoteTextCommand extends Composite {

    @Override public String name() { return "Drawing.SetNoteText"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "SetNoteText(target, notes[, dryRun]) — composite"; }

    @Override
    public String description() {
        return "Edit the text of one or more existing notes by id, e.g. {\"215\": \"REV C\"} or "
                + "{\"215\": [\"line one\", \"line two\"]}. Can only replace text in lines the note "
                + "already has. Supports dryRun.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing whose notes to edit.")
                .required("notes", JsonSchema.dataObject("NoteText"),
                        "Note id/text pairs. A value may be a single string (one line) or an "
                                + "array of strings (one per existing line).")
                .optional("dryRun", JsonSchema.bool(),
                        "Report what would change without writing. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        JsonObject wanted = params.getObject("notes");
        if (wanted == null || wanted.isEmpty()) {
            throw new CommandException(
                    "Field 'notes' must be an object of note-id/text pairs", "invalid_params");
        }
        boolean dryRun = dryRun(params);

        java.util.Map<Integer, DetailNoteItem> byId = index(drawing);

        JsonArray results = new JsonArray();
        int changed = 0;
        int rejected = 0;

        for (String key : wanted.keys()) {
            JsonObject entry = JsonObject.of("id", key);
            Integer id;
            try {
                id = Integer.valueOf(key.trim());
            } catch (NumberFormatException e) {
                entry.put("action", "rejected");
                entry.put("reason", "note id must be numeric; Drawing.GetNotes lists them");
                rejected++;
                results.add(entry);
                continue;
            }

            DetailNoteItem note = byId.get(id);
            if (note == null) {
                entry.put("action", "rejected");
                entry.put("reason", "no note with this id on this drawing");
                rejected++;
                results.add(entry);
                continue;
            }

            List<String> newLines = asLines(wanted.get(key));

            DetailNoteInstructions instructions;
            try {
                instructions = note.GetInstructions(false);
            } catch (jxthrowable e) {
                entry.put("action", "rejected");
                entry.put("reason", "could not read this note's instructions: " + rootMessage(e));
                rejected++;
                results.add(entry);
                continue;
            }

            entry.putIfPresent("before", DrawingGetNotesCommand.text(instructions));

            DetailTextLines lines = instructions.GetTextLines();
            int existing = lines == null ? 0 : lines.getarraysize();
            if (newLines.size() > existing) {
                entry.put("action", "rejected");
                entry.put("reason", "this note has " + existing + " line(s); cannot add lines "
                        + "through this API, only replace text in ones that already exist");
                rejected++;
                results.add(entry);
                continue;
            }

            if (dryRun) {
                entry.put("action", "wouldSet");
                changed++;
                results.add(entry);
                continue;
            }

            try {
                for (int i = 0; i < newLines.size(); i++) {
                    setFirstRunText(lines.get(i), newLines.get(i));
                }
                note.Modify(instructions);
                entry.put("action", "set");
                entry.putIfPresent("after", DrawingGetNotesCommand.text(note.GetInstructions(false)));
                changed++;
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
                "changed", Integer.valueOf(changed),
                "rejected", Integer.valueOf(rejected),
                "notes", results);
    }

    private static void setFirstRunText(DetailTextLine line, String text) throws jxthrowable {
        if (line == null) {
            throw new CommandException("This note's text line could not be read", "creo_error");
        }
        DetailTexts texts = line.GetTexts();
        if (texts == null || texts.getarraysize() == 0) {
            throw new CommandException("This note's line has no text run to write into", "creo_error");
        }
        DetailText first = texts.get(0);
        first.SetText(text);
    }

    private static List<String> asLines(Object value) {
        List<String> out = new java.util.ArrayList<>();
        if (value instanceof com.asyncjlink.json.JsonArray) {
            com.asyncjlink.json.JsonArray arr = (com.asyncjlink.json.JsonArray) value;
            for (int i = 0; i < arr.size(); i++) {
                out.add(String.valueOf(arr.get(i)));
            }
        } else {
            out.add(String.valueOf(value));
        }
        return out;
    }

    private static java.util.Map<Integer, DetailNoteItem> index(Model drawing) throws jxthrowable {
        java.util.Map<Integer, DetailNoteItem> out = new java.util.LinkedHashMap<>();
        List<ModelItem> found = items(drawing, ModelItemType.ITEM_DTL_NOTE);
        for (ModelItem item : found) {
            if (item instanceof DetailNoteItem) {
                out.put(Integer.valueOf(item.GetId()), (DetailNoteItem) item);
            }
        }
        return out;
    }
}
