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
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcInterference.GlobalEvaluator;

/**
 * GlobalEvaluator.SetAssem &mdash; pfcInterference.
 *
 * <pre>
 * void SetAssem(Assembly) throws jxthrowable
 * </pre>
 */
public final class GlobalEvaluatorSetAssemCommand implements Command {

    @Override public String name() { return "GlobalEvaluator.SetAssem"; }
    @Override public String jlinkPackage() { return "pfcInterference"; }
    @Override public String receiverType() { return "GlobalEvaluator"; }
    @Override public String signature() { return "void SetAssem(Assembly) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("GlobalEvaluator.SetAssem \u2014 pfcInterference")
                .required("target", JsonSchema.handle("GlobalEvaluator"),
                        "The GlobalEvaluator to act on.")
                .optional("assembly", JsonSchema.handle("Assembly"),
                        "Handle to a Assembly, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        GlobalEvaluator target = Marshal.in(ctx, params.get("target"),
                "GlobalEvaluator", GlobalEvaluator.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Assembly assembly = Marshal.in(ctx, params.get("assembly"),
                "Assembly", Assembly.class, "assembly");
        target.SetAssem(assembly);
        return Marshal.ok();
    }
}
