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
import com.ptc.pfc.pfcBase.StdLineStyle;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.SetLineStyle &mdash; pfcSession.
 *
 * <pre>
 * StdLineStyle SetLineStyle(StdLineStyle) throws jxthrowable
 * </pre>
 */
public final class BaseSessionSetLineStyleCommand implements Command {

    @Override public String name() { return "BaseSession.SetLineStyle"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "StdLineStyle SetLineStyle(StdLineStyle) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.SetLineStyle \u2014 pfcSession")
                .optional("stdLineStyle", JsonSchema.enumOf("StdLineStyle", "LINE_SOLID", "LINE_DOTTED", "LINE_CENTERLINE", "LINE_PHANTOM", "LINE_DASH", "LINE_CTRL_S_L", "LINE_CTRL_L_L", "LINE_CTRL_S_S", "LINE_DASH_S_S", "LINE_PHANTOM_S_S", "LINE_CTRL_MID_L", "LINE_INTMIT_LWW_HIDDEN", "LINE_PDFHIDDEN_LINESTYLE"),
                        "One of the StdLineStyle constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        StdLineStyle stdLineStyle = Marshal.in(ctx, params.get("stdLineStyle"),
                "StdLineStyle", StdLineStyle.class, "stdLineStyle");
        return Marshal.result(ctx, target.SetLineStyle(stdLineStyle), "StdLineStyle");
    }
}
