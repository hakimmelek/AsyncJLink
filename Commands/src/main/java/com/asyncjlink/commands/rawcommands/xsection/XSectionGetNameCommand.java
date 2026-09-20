/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands.rawcommands.xsection;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Marshal;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcXSection.XSection;

/**
 * XSection.GetName &mdash; pfcXSection.
 *
 * <pre>
 * String GetName() throws jxthrowable
 * </pre>
 */
public final class XSectionGetNameCommand implements Command {

    @Override public String name() { return "XSection.GetName"; }
    @Override public String jlinkPackage() { return "pfcXSection"; }
    @Override public String receiverType() { return "XSection"; }
    @Override public String signature() { return "String GetName() throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("XSection.GetName \u2014 pfcXSection")
                .required("target", JsonSchema.handle("XSection"),
                        "The XSection to act on.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        XSection target = Marshal.in(ctx, params.get("target"),
                "XSection", XSection.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        return Marshal.result(ctx, target.GetName(), "String");
    }
}
