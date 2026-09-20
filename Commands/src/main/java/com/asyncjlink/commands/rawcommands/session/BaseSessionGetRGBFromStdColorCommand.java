/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.session;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.StdColor;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.GetRGBFromStdColor &mdash; pfcSession.
 *
 * <pre>
 * ColorRGB GetRGBFromStdColor(StdColor) throws jxthrowable
 * </pre>
 */
public final class BaseSessionGetRGBFromStdColorCommand implements Command {

    @Override public String name() { return "BaseSession.GetRGBFromStdColor"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "ColorRGB GetRGBFromStdColor(StdColor) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.GetRGBFromStdColor \u2014 pfcSession")
                .optional("stdColor", JsonSchema.enumOf("StdColor", "COLOR_LETTER", "COLOR_HIGHLIGHT", "COLOR_DRAWING", "COLOR_BACKGROUND", "COLOR_HALF_TONE", "COLOR_EDGE_HIGHLIGHT", "COLOR_DIMMED", "COLOR_ERROR", "COLOR_WARNING", "COLOR_SHEETMETAL", "COLOR_CURVE", "COLOR_PRESEL_HIGHLIGHT", "COLOR_SELECTED", "COLOR_SECONDARY_SELECTED", "COLOR_PREVIEW", "COLOR_SECONDARY_PREVIEW", "COLOR_DATUM", "COLOR_QUILT", "COLOR_LWW", "COLOR_MAX", "COLOR_SHADED_EDGE", "COLOR_SS_HAS_FROZEN_FEAT", "COLOR_SS_FROZEN_COMPONENT", "COLOR_SS_FAILED_ITEMS", "COLOR_SS_PACKAGED", "COLOR_SS_RECENT_SEARCH", "COLOR_SS_INTCH_GROUP_MEMBERS", "COLOR_SS_REPLACED_COMPS", "COLOR_SS_FT_INSTANCES", "COLOR_SS_FT_GENERICS", "COLOR_SS_HAS_PROPROGRAM", "COLOR_SS_HAS_PROPROGRAM_INPUT", "COLOR_SS_DRIVEN_BY_PROPROGRAM", "COLOR_SS_MODULE_NODES", "COLOR_SS_CURRENT_DESIGN_SOL", "COLOR_SS_NONCURRENT_DESIGN_SOL", "COLOR_SS_REPRESENTATIVE_DESIGN_SOL", "COLOR_SS_CONF_ASM_NODES", "COLOR_SS_CHILD_OF_MODIFIED_IN_DMA"),
                        "One of the StdColor constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        StdColor stdColor = Marshal.in(ctx, params.get("stdColor"),
                "StdColor", StdColor.class, "stdColor");
        return Marshal.result(ctx, target.GetRGBFromStdColor(stdColor), "ColorRGB");
    }
}
