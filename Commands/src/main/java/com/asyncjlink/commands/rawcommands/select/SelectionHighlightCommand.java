/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.select;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.StdColor;
import com.ptc.pfc.pfcSelect.Selection;

/**
 * Selection.Highlight &mdash; pfcSelect.
 *
 * <pre>
 * void Highlight(StdColor) throws jxthrowable
 * </pre>
 */
public final class SelectionHighlightCommand implements Command {

    @Override public String name() { return "Selection.Highlight"; }
    @Override public String jlinkPackage() { return "pfcSelect"; }
    @Override public String receiverType() { return "Selection"; }
    @Override public String signature() { return "void Highlight(StdColor) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Selection.Highlight \u2014 pfcSelect")
                .required("target", JsonSchema.handle("Selection"),
                        "The Selection to act on.")
                .optional("stdColor", JsonSchema.enumOf("StdColor", "COLOR_LETTER", "COLOR_HIGHLIGHT", "COLOR_DRAWING", "COLOR_BACKGROUND", "COLOR_HALF_TONE", "COLOR_EDGE_HIGHLIGHT", "COLOR_DIMMED", "COLOR_ERROR", "COLOR_WARNING", "COLOR_SHEETMETAL", "COLOR_CURVE", "COLOR_PRESEL_HIGHLIGHT", "COLOR_SELECTED", "COLOR_SECONDARY_SELECTED", "COLOR_PREVIEW", "COLOR_SECONDARY_PREVIEW", "COLOR_DATUM", "COLOR_QUILT", "COLOR_LWW", "COLOR_MAX", "COLOR_SHADED_EDGE", "COLOR_SS_HAS_FROZEN_FEAT", "COLOR_SS_FROZEN_COMPONENT", "COLOR_SS_FAILED_ITEMS", "COLOR_SS_PACKAGED", "COLOR_SS_RECENT_SEARCH", "COLOR_SS_INTCH_GROUP_MEMBERS", "COLOR_SS_REPLACED_COMPS", "COLOR_SS_FT_INSTANCES", "COLOR_SS_FT_GENERICS", "COLOR_SS_HAS_PROPROGRAM", "COLOR_SS_HAS_PROPROGRAM_INPUT", "COLOR_SS_DRIVEN_BY_PROPROGRAM", "COLOR_SS_MODULE_NODES", "COLOR_SS_CURRENT_DESIGN_SOL", "COLOR_SS_NONCURRENT_DESIGN_SOL", "COLOR_SS_REPRESENTATIVE_DESIGN_SOL", "COLOR_SS_CONF_ASM_NODES", "COLOR_SS_CHILD_OF_MODIFIED_IN_DMA"),
                        "One of the StdColor constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Selection target = Marshal.in(ctx, params.get("target"),
                "Selection", Selection.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        StdColor stdColor = Marshal.in(ctx, params.get("stdColor"),
                "StdColor", StdColor.class, "stdColor");
        target.Highlight(stdColor);
        return Marshal.ok();
    }
}
