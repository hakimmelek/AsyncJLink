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
import com.ptc.pfc.pfcBase.DimDisplayMode;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.SetDimensionDisplayMode &mdash; pfcSession.
 *
 * <pre>
 * void SetDimensionDisplayMode(DimDisplayMode) throws jxthrowable
 * </pre>
 */
public final class BaseSessionSetDimensionDisplayModeCommand implements Command {

    @Override public String name() { return "BaseSession.SetDimensionDisplayMode"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void SetDimensionDisplayMode(DimDisplayMode) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.SetDimensionDisplayMode \u2014 pfcSession")
                .optional("dimDisplayMode", JsonSchema.enumOf("DimDisplayMode", "DIM_DISPLAY_NUMERIC", "DIM_DISPLAY_SYMBOLIC"),
                        "One of the DimDisplayMode constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        DimDisplayMode dimDisplayMode = Marshal.in(ctx, params.get("dimDisplayMode"),
                "DimDisplayMode", DimDisplayMode.class, "dimDisplayMode");
        target.SetDimensionDisplayMode(dimDisplayMode);
        return Marshal.ok();
    }
}
