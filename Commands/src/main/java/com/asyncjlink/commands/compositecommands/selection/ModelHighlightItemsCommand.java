package com.asyncjlink.commands.compositecommands.selection;

import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.StdColor;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcSelect.pfcSelect;
import com.ptc.pfc.pfcWindow.Window;

/**
 * Model.HighlightItems — show the user what is about to change.
 *
 * <p>Touches no geometry and is entirely reversible, which is precisely what makes automated
 * mutation acceptable to work alongside: the caller can highlight, ask, then act.
 */
public final class ModelHighlightItemsCommand extends Composite {

    @Override public String name() { return "Model.HighlightItems"; }
    @Override public String receiverType() { return "Model"; }
    @Override public String signature() { return "HighlightItems(target, items[, color, clear]) — composite"; }

    @Override
    public String description() {
        return "Highlight model items in the Creo window so a human can confirm them before "
                + "anything is changed. Display only — no geometry is modified.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Model", "The model whose items are being highlighted.")
                .required("items", JsonSchema.array(JsonSchema.handle("ModelItem")),
                        "Item handles, as returned by Model.FindItems.")
                .optional("color", JsonSchema.string(),
                        "StdColor constant, e.g. COLOR_HIGHLIGHT (default) or COLOR_ERROR.")
                .optional("clear", JsonSchema.bool(),
                        "Un-highlight these items instead of highlighting them. Default false.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model model = requireModel(ctx, params);
        JsonArray handles = params.getArray("items");
        if (handles == null || handles.size() == 0) {
            throw new CommandException("Field 'items' must list at least one handle", "invalid_params");
        }

        boolean clear = params.getBoolean("clear", false);
        StdColor color = StdColor.COLOR_HIGHLIGHT;
        String colorName = params.getString("color", null);
        if (colorName != null && !colorName.isEmpty()) {
            color = (StdColor) Enums.fromJson("StdColor", colorName, "color");
        }

        JsonArray results = new JsonArray();
        int affected = 0;
        for (int i = 0; i < handles.size(); i++) {
            String ref = String.valueOf(handles.get(i));
            JsonObject entry = JsonObject.of("handle", ref);
            Object found = ctx.handles().lookup(ref);
            if (!(found instanceof ModelItem)) {
                entry.put("action", "skipped");
                entry.put("reason", found == null
                        ? "not a known handle" : "not a model item");
                results.add(entry);
                continue;
            }
            try {
                Selection sel = pfcSelect.CreateModelItemSelection((ModelItem) found, null);
                if (clear) {
                    sel.UnHighlight();
                } else {
                    sel.Highlight(color);
                }
                entry.put("action", clear ? "cleared" : "highlighted");
                affected++;
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
            }
            results.add(entry);
        }

        try {
            Window window = ctx.session().GetModelWindow(model);
            if (window != null) {
                window.Repaint();
            }
        } catch (jxthrowable | RuntimeException ignored) {
            // A repaint failure does not undo the highlight; there is nothing useful to report.
        }

        return JsonObject.of(
                "affected", Integer.valueOf(affected),
                "items", results);
    }
}
