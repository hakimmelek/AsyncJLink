/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.solid;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcGeometry.CoordSystem;
import com.ptc.pfc.pfcModel.ImportFeatAttr;
import com.ptc.pfc.pfcModel.IntfDataSource;
import com.ptc.pfc.pfcSolid.Solid;

/**
 * Solid.CreateImportFeat &mdash; pfcSolid.
 *
 * <pre>
 * Feature CreateImportFeat(IntfDataSource, CoordSystem, ImportFeatAttr) throws jxthrowable
 * </pre>
 */
public final class SolidCreateImportFeatCommand implements Command {

    @Override public String name() { return "Solid.CreateImportFeat"; }
    @Override public String jlinkPackage() { return "pfcSolid"; }
    @Override public String receiverType() { return "Solid"; }
    @Override public String signature() { return "Feature CreateImportFeat(IntfDataSource, CoordSystem, ImportFeatAttr) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Solid.CreateImportFeat \u2014 pfcSolid")
                .required("target", JsonSchema.handle("Solid"),
                        "The Solid to act on.")
                .optional("intfDataSource", JsonSchema.dataObject("IntfDataSource"),
                        "IntfDataSource options object; its fields are passed to the pfc factory and setters.")
                .optional("coordSystem", JsonSchema.handle("CoordSystem"),
                        "Handle to a CoordSystem, as returned by an earlier command.")
                .optional("importFeatAttr", JsonSchema.dataObject("ImportFeatAttr"),
                        "ImportFeatAttr options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Solid target = Marshal.in(ctx, params.get("target"),
                "Solid", Solid.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        IntfDataSource intfDataSource = Marshal.in(ctx, params.get("intfDataSource"),
                "IntfDataSource", IntfDataSource.class, "intfDataSource");
        CoordSystem coordSystem = Marshal.in(ctx, params.get("coordSystem"),
                "CoordSystem", CoordSystem.class, "coordSystem");
        ImportFeatAttr importFeatAttr = Marshal.in(ctx, params.get("importFeatAttr"),
                "ImportFeatAttr", ImportFeatAttr.class, "importFeatAttr");
        return Marshal.result(ctx, target.CreateImportFeat(intfDataSource, coordSystem, importFeatAttr), "Feature");
    }
}
