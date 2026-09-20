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
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcSession.ParamType;
import com.ptc.pfc.pfcSession.Session;

/**
 * Session.UIDisplayFeatureParams &mdash; pfcSession.
 *
 * <pre>
 * void UIDisplayFeatureParams(Selection, ParamType) throws jxthrowable
 * </pre>
 */
public final class SessionUIDisplayFeatureParamsCommand implements Command {

    @Override public String name() { return "Session.UIDisplayFeatureParams"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "void UIDisplayFeatureParams(Selection, ParamType) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIDisplayFeatureParams \u2014 pfcSession")
                .optional("selection", JsonSchema.handle("Selection"),
                        "Handle to a Selection, as returned by an earlier command.")
                .optional("paramType", JsonSchema.enumOf("ParamType", "USER_PARAM", "DIM_PARAM", "PATTERN_PARAM", "DIMTOL_PARAM", "REFDIM_PARAM", "ALL_PARAMS", "GTOL_PARAM", "SURFFIN_PARAM"),
                        "One of the ParamType constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        Selection selection = Marshal.in(ctx, params.get("selection"),
                "Selection", Selection.class, "selection");
        ParamType paramType = Marshal.in(ctx, params.get("paramType"),
                "ParamType", ParamType.class, "paramType");
        target.UIDisplayFeatureParams(selection, paramType);
        return Marshal.ok();
    }
}
