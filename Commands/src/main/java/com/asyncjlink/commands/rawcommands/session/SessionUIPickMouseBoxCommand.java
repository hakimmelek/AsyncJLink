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
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcSession.Session;

/**
 * Session.UIPickMouseBox &mdash; pfcSession.
 *
 * <pre>
 * Outline3D UIPickMouseBox(Point3D) throws jxthrowable
 * </pre>
 */
public final class SessionUIPickMouseBoxCommand implements Command {

    @Override public String name() { return "Session.UIPickMouseBox"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "Outline3D UIPickMouseBox(Point3D) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIPickMouseBox \u2014 pfcSession")
                .optional("point3D", JsonSchema.sequence("Point3D", JsonSchema.number()),
                        "Array of double.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        Point3D point3D = Marshal.in(ctx, params.get("point3D"),
                "Point3D", Point3D.class, "point3D");
        return Marshal.result(ctx, target.UIPickMouseBox(point3D), "Outline3D");
    }
}
