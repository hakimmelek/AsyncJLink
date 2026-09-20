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
import com.ptc.pfc.pfcSession.MouseButton;
import com.ptc.pfc.pfcSession.Session;

/**
 * Session.UIGetNextMousePick &mdash; pfcSession.
 *
 * <pre>
 * MouseStatus UIGetNextMousePick(MouseButton) throws jxthrowable
 * </pre>
 */
public final class SessionUIGetNextMousePickCommand implements Command {

    @Override public String name() { return "Session.UIGetNextMousePick"; }
    @Override public String jlinkPackage() { return "pfcSession"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "MouseStatus UIGetNextMousePick(MouseButton) throws jxthrowable"; }

    @Override
    public JsonSchema paramSchema() {
        return JsonSchema.object()
                .describedAs("Session.UIGetNextMousePick \u2014 pfcSession")
                .optional("mouseButton", JsonSchema.enumOf("MouseButton", "MOUSE_BTN_LEFT", "MOUSE_BTN_MIDDLE", "MOUSE_BTN_RIGHT", "MOUSE_BTN_LEFT_DOUBLECLICK", "MOUSE_BTN_ANY", "MOUSE_BTN_MOVE"),
                        "One of the MouseButton constants.")
                ;
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Session target = ctx.session();
        MouseButton mouseButton = Marshal.in(ctx, params.get("mouseButton"),
                "MouseButton", MouseButton.class, "mouseButton");
        return Marshal.result(ctx, target.UIGetNextMousePick(mouseButton), "MouseStatus");
    }
}
