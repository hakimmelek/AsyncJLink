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
 * XSection.SetName &mdash; pfcXSection.
 *
 * <pre>
 * void SetName(String) throws jxthrowable
 * </pre>
 */
public final class XSectionSetNameCommand implements Command {

    @Override public String name() { return "XSection.SetName"; }
    @Override public String jlinkPackage() { return "pfcXSection"; }
    @Override public String receiverType() { return "XSection"; }
    @Override public String signature() { return "void SetName(String) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("XSection.SetName \u2014 pfcXSection")
                .required("target", JsonSchema.handle("XSection"),
                        "The XSection to act on.")
                .optional("value", JsonSchema.string(),
                        "String value.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        XSection target = Marshal.in(ctx, params.get("target"),
                "XSection", XSection.class, "target");
        if (target == null) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        String value = Marshal.in(ctx, params.get("value"),
                "String", String.class, "value");
        target.SetName(value);
        return Marshal.ok();
    }
}
