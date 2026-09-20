package com.asyncjlink.commands.compositecommands.drawing;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailSymbolGroups;
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;
import com.ptc.pfc.pfcDetail.SymbolGroupFilter;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;

import java.util.List;

/**
 * Drawing.GetSymbols — every drawing symbol instance placed on a drawing.
 *
 * <p>Surface finish marks, welding symbols, custom company symbols — the counterpart to
 * {@code Drawing.GetNotes} for symbol annotations rather than text. Same {@code
 * ModelItemOwner.ListItems} mechanism, with {@code ITEM_DTL_SYM_INSTANCE} in place of
 * {@code ITEM_DTL_NOTE}.
 */
public final class DrawingGetSymbolsCommand extends Composite {

    @Override public String name() { return "Drawing.GetSymbols"; }
    @Override public String receiverType() { return "Drawing"; }
    @Override public String signature() { return "GetSymbols(target) — composite"; }

    @Override
    public String description() {
        return "Every drawing symbol instance placed on a drawing, with the group(s) it belongs to.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Drawing", "The drawing whose symbols to read.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model drawing = requireModel(ctx, params);
        List<ModelItem> found = items(drawing, ModelItemType.ITEM_DTL_SYM_INSTANCE);

        JsonArray out = new JsonArray();
        for (ModelItem item : found) {
            if (!(item instanceof DetailSymbolInstItem)) {
                continue;
            }
            DetailSymbolInstItem symbol = (DetailSymbolInstItem) item;
            JsonObject entry = itemRef(ctx, item, null);
            entry.putIfPresent("groupCount", groupCount(symbol));
            out.add(entry);
        }

        return JsonObject.of(
                "drawing", modelRef(ctx, drawing),
                "matched", Integer.valueOf(out.size()),
                "symbols", out);
    }

    private static Integer groupCount(DetailSymbolInstItem symbol) {
        try {
            DetailSymbolGroups groups = symbol.ListGroups(SymbolGroupFilter.DTLSYMINST_ALL_GROUPS);
            return groups == null ? null : Integer.valueOf(groups.getarraysize());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
