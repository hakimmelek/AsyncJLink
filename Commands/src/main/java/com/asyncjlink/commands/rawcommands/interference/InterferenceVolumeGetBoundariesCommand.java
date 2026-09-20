/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.interference;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcInterference.InterferenceVolume;

/**
 * InterferenceVolume.GetBoundaries &mdash; pfcInterference.
 *
 * <pre>
 * SurfaceDescriptors GetBoundaries() throws jxthrowable
 * </pre>
 */
public final class InterferenceVolumeGetBoundariesCommand implements Command {

    @Override public String name() { return "InterferenceVolume.GetBoundaries"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "InterferenceVolume"; }
    @Override public String signature() { return "SurfaceDescriptors GetBoundaries() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("InterferenceVolume.GetBoundaries \u2014 pfcInterference")
                .required("target", JsonSchema.handle("InterferenceVolume"),
                        "The InterferenceVolume to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        InterferenceVolume target = Marshal.in(ctx, params.get("target"),
                "InterferenceVolume", InterferenceVolume.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetBoundaries(), "SurfaceDescriptors");
    }
}
