package com.asyncjlink.commands.compositecommands.selection;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcSelect.SelectionBuffer;
import com.ptc.pfc.pfcSelect.Selections;

/**
 * Session.GetSelection — what the user currently has selected in Creo.
 *
 * <p>{@code BaseSession.Select} is interactive: it blocks waiting for a human to pick geometry, so
 * it cannot be driven from the CLI or from MCP. This is the way round that. It makes the human the
 * selector and the tool the executor — "I have this face selected, chamfer it 2 mm" — which
 * sidesteps spatial reasoning from text instead of pretending to solve it.
 */
public final class SessionGetSelectionCommand extends Composite {

    @Override public String name() { return "Session.GetSelection"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "GetSelection() — composite"; }

    @Override
    public String description() {
        return "Read what the user currently has selected in the Creo window: items, types, names "
                + "and owning models. Use this to act on the user's own selection.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name());
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        JsonArray out = new JsonArray();

        SelectionBuffer buffer = ctx.session().GetCurrentSelectionBuffer();
        if (buffer == null) {
            return listResult("selections", out);
        }
        Selections contents = buffer.GetContents();
        if (contents == null) {
            return listResult("selections", out);
        }

        for (int i = 0; i < contents.getarraysize(); i++) {
            Selection sel = contents.get(i);
            if (sel == null) {
                continue;
            }
            JsonObject entry = JsonObject.of(
                    "$handle", ctx.handles().handleFor(sel, "Selection"),
                    "$type", "Selection");

            ModelItem item = safeItem(sel);
            if (item != null) {
                entry.put("item", itemRef(ctx, item));
            }
            entry.putIfPresent("model", modelRef(ctx, safeModel(sel)));
            entry.putIfPresent("selectionString", safeSelectionString(sel));
            entry.putIfPresent("point", point(safePoint(sel)));
            out.add(entry);
        }
        return listResult("selections", out);
    }

    private static ModelItem safeItem(Selection sel) {
        try {
            return sel.GetSelItem();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static com.ptc.pfc.pfcModel.Model safeModel(Selection sel) {
        try {
            return sel.GetSelModel();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeSelectionString(Selection sel) {
        try {
            return sel.GetSelectionString();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static Point3D safePoint(Selection sel) {
        try {
            return sel.GetPoint();
        } catch (jxthrowable | RuntimeException e) {
            // Only surface and edge picks carry a point; a feature pick does not.
            return null;
        }
    }

    private static JsonArray point(Point3D p) {
        if (p == null) {
            return null;
        }
        try {
            JsonArray a = new JsonArray();
            for (int i = 0; i < 3; i++) {
                a.add(Double.valueOf(p.get(i)));
            }
            return a;
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
