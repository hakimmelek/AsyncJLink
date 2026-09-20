/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.geometry;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcGeometry.NURBSSurface;

/**
 * NURBSSurface.GetVKnots &mdash; pfcGeometry.
 *
 * <pre>
 * realseq GetVKnots() throws jxthrowable
 * </pre>
 */
public final class NURBSSurfaceGetVKnotsCommand implements Command {

    @Override public String name() { return "NURBSSurface.GetVKnots"; }
    @Override public String jlinkPackage() { return "pfcGeometry"; }
    @Override public String receiverType() { return "NURBSSurface"; }
    @Override public String signature() { return "realseq GetVKnots() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("NURBSSurface.GetVKnots \u2014 pfcGeometry")
                .required("target", JsonSchema.handle("NURBSSurface"),
                        "The NURBSSurface to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        NURBSSurface target = Marshal.in(ctx, params.get("target"),
                "NURBSSurface", NURBSSurface.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetVKnots(), "realseq");
    }
}
