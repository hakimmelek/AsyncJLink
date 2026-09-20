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
import com.ptc.pfc.pfcInterference.GlobalInterference;
import com.ptc.pfc.pfcInterference.InterferenceVolume;

/**
 * GlobalInterference.SetVolume &mdash; pfcInterference.
 *
 * <pre>
 * void SetVolume(InterferenceVolume) throws jxthrowable
 * </pre>
 */
public final class GlobalInterferenceSetVolumeCommand implements Command {

    @Override public String name() { return "GlobalInterference.SetVolume"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "GlobalInterference"; }
    @Override public String signature() { return "void SetVolume(InterferenceVolume) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("GlobalInterference.SetVolume \u2014 pfcInterference")
                .required("target", JsonSchema.handle("GlobalInterference"),
                        "The GlobalInterference to act on.")
                .optional("interferenceVolume", JsonSchema.handle("InterferenceVolume"),
                        "Handle to a InterferenceVolume, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        GlobalInterference target = Marshal.in(ctx, params.get("target"),
                "GlobalInterference", GlobalInterference.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        InterferenceVolume interferenceVolume = Marshal.in(ctx, params.get("interferenceVolume"),
                "InterferenceVolume", InterferenceVolume.class, "interferenceVolume");
        target.SetVolume(interferenceVolume);
        return Marshal.ok();
    }
}
