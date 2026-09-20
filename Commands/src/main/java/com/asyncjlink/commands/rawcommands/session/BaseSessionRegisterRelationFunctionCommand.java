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
import com.ptc.pfc.pfcRelations.RelationFunctionListener;
import com.ptc.pfc.pfcRelations.RelationFunctionOptions;
import com.ptc.pfc.pfcSession.BaseSession;
import com.ptc.pfc.pfcSession.Session;

/**
 * BaseSession.RegisterRelationFunction &mdash; pfcSession.
 *
 * <pre>
 * void RegisterRelationFunction(String, RelationFunctionListener, RelationFunctionOptions) throws jxthrowable
 * </pre>
 *
 * <p>Catalogued but not invocable: it requires a live RelationFunctionListener callback.
 */
public final class BaseSessionRegisterRelationFunctionCommand implements Command {

    @Override public String name() { return "BaseSession.RegisterRelationFunction"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "BaseSession"; }
    @Override public String signature() { return "void RegisterRelationFunction(String, RelationFunctionListener, RelationFunctionOptions) throws jxthrowable"; }

    @Override public boolean isInvocable() { return false; }
    @Override public String unsupportedReason() { return "it takes a RelationFunctionListener callback, which has no JSON representation; drive it from an in-process J-Link application instead"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("BaseSession.RegisterRelationFunction \u2014 pfcSession")
                .optional("value", JsonSchema.string(),
                        "String value.")
                .optional("relationFunctionListener", JsonSchema.dataObject("RelationFunctionListener"),
                        "RelationFunctionListener callback (not supplyable over JSON).")
                .optional("relationFunctionOptions", JsonSchema.dataObject("RelationFunctionOptions"),
                        "RelationFunctionOptions options object; its fields are passed to the pfc factory and setters.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        throw CommandException.unsupported(
                "BaseSession.RegisterRelationFunction", unsupportedReason());
    }
}
