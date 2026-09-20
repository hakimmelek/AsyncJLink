/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.model2d;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcSimpRep.SimpRep;

/**
 * Model2D.DeleteSimplifiedRep &mdash; pfcModel2D.
 *
 * <pre>
 * void DeleteSimplifiedRep(SimpRep) throws jxthrowable
 * </pre>
 */
public final class Model2DDeleteSimplifiedRepCommand implements Command {

    @Override public String name() { return "Model2D.DeleteSimplifiedRep"; }
    @Override public String jlinkPackage() { return "pfcModel2D"; }
    @Override public String receiverType() { return "Model2D"; }
    @Override public String signature() { return "void DeleteSimplifiedRep(SimpRep) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Model2D.DeleteSimplifiedRep \u2014 pfcModel2D")
                .required("target", JsonSchema.handle("Model2D"),
                        "The Model2D to act on.")
                .optional("simpRep", JsonSchema.handle("SimpRep"),
                        "Handle to a SimpRep, as returned by an earlier command.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model2D target = Marshal.in(ctx, params.get("target"),
                "Model2D", Model2D.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        SimpRep simpRep = Marshal.in(ctx, params.get("simpRep"),
                "SimpRep", SimpRep.class, "simpRep");
        target.DeleteSimplifiedRep(simpRep);
        return Marshal.ok();
    }
}
