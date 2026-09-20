package com.asyncjlink.commands.compositecommands.drawing;

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
 * Drawing.GetNotes — every note on a drawing, with its text.
 *
 * <p>Relies on {@code ModelItemOwner.ListItems(ITEM_DTL_NOTE)} rather than the more obvious {@code
 * DetailItemOwner.ListDetailItems}: the latter has no generated raw command in this catalogue (see
 * {@code Drawing.GetSheets} in the design doc for why), while item-type listing is the same real,
 * generated, already-exercised mechanism {@code Model.FindItems} uses for every other item type.
 */
public final class DrawingGetNotesCommand extends Composite {

    @Override public String name() { return "Drawing.GetNotes"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "GetNotes(target) — composite"; }

    @Override
    public String description() {
        return "Every note on a drawing, with its text lines and the view it appears on.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing whose notes to read.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        List<ModelItem> found = items(drawing, ModelItemType.ITEM_DTL_NOTE);

        JsonArray out = new JsonArray();
        for (ModelItem item : found) {
            if (!(item instanceof DetailNoteItem)) {
                continue;
            }
            DetailNoteItem note = (DetailNoteItem) item;
            JsonObject entry = itemRef(ctx, item, null);
            entry.putIfPresent("text", safeText(note));
            entry.putIfPresent("modelReference", safeModelReference(ctx, note));
            out.add(entry);
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "matched", Integer.valueOf(out.size()),
                "notes", out);
    }

    /** Package-visible: {@link DrawingSetNoteTextCommand} reads the same instructions to edit them. */
    static String safeText(DetailNoteItem note) {
        try {
            return text(note.GetInstructions(false));
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    static String text(DetailNoteInstructions instructions) throws jxthrowable {
        if (instructions == null) {
            return null;
        }
        DetailTextLines lines = instructions.GetTextLines();
        if (lines == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.getarraysize(); i++) {
            DetailTextLine line = lines.get(i);
            if (line == null) {
                continue;
            }
            if (i > 0) {
                sb.append('\n');
            }
            DetailTexts texts = line.GetTexts();
            if (texts == null) {
                continue;
            }
            for (int j = 0; j < texts.getarraysize(); j++) {
                DetailText t = texts.get(j);
                if (t != null) {
                    sb.append(t.GetText());
                }
            }
        }
        return sb.toString();
    }

    private static JsonObject safeModelReference(CreoContext ctx, DetailNoteItem note) {
        try {
            // GetModelReference(int, int) is undocumented beyond its signature (pfcasync.jar
            // carries no parameter names); (0, 0) is a best-effort guess at "the first reference on
            // the first line", wrapped so a wrong guess just omits this optional field.
            Model m = note.GetModelReference(0, 0);
            return m == null ? null : modelRef(ctx, m);
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
