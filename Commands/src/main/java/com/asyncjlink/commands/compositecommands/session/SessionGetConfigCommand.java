package com.asyncjlink.commands.compositecommands.session;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;

import java.util.ArrayList;
import java.util.List;

/**
 * Session.GetConfig — read Creo's configuration options.
 *
 * <p>Needed in its own right, and a prerequisite for {@code Session.CreateModel}: the template a new
 * part or assembly should be based on is named by {@code template_solidpart} /
 * {@code template_designasm} and by nothing else the API exposes.
 */
public final class SessionGetConfigCommand extends Composite {

    /** Asked for when the caller names no options of its own. */
    private static final String[] COMMON = {
        "template_solidpart",
        "template_designasm",
        "template_sheetmetalpart",
        "template_drawing",
        "drawing_setup_file",
        "pro_unit_length",
        "pro_unit_mass",
        "search_path_file",
        "start_model_dir",
    };

    @Override public String name() { return "Session.GetConfig"; }
    @Override public String receiverType() { return "Session"; }
    @Override public String signature() { return "GetConfig([options, prefix]) — composite"; }

    @Override
    public String description() {
        return "Read Creo config.pro options by name or prefix. With no arguments it returns the "
                + "options that decide templates, units and search paths.";
    }

    @Override
    public JsonSchema paramSchema() {
        return sessionScoped(name())
                .optional("options", JsonSchema.array(JsonSchema.string()),
                        "Specific option names to read. Defaults to the common template/unit options.")
                .optional("prefix", JsonSchema.string(),
                        "Read every option in the default set whose name starts with this, "
                                + "e.g. \"template_\".");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        List<String> wanted = new ArrayList<>();
        JsonArray asked = params.getArray("options");
        if (asked != null && asked.size() > 0) {
            for (int i = 0; i < asked.size(); i++) {
                wanted.add(String.valueOf(asked.get(i)));
            }
        } else {
            String prefix = params.getString("prefix", null);
            for (String c : COMMON) {
                if (prefix == null || c.startsWith(prefix)) {
                    wanted.add(c);
                }
            }
        }

        JsonArray out = new JsonArray();
        for (String option : wanted) {
            JsonObject entry = JsonObject.of("name", option);
            try {
                String value = ctx.session().GetConfigOption(option);
                if (value == null || value.isEmpty()) {
                    // Creo does not distinguish "unset" from "empty"; saying which one we saw is
                    // more useful than reporting an empty string as if it were a value.
                    entry.put("set", Boolean.FALSE);
                } else {
                    entry.put("set", Boolean.TRUE);
                    entry.put("value", value);
                }
            } catch (jxthrowable | RuntimeException e) {
                entry.put("set", Boolean.FALSE);
                entry.put("error", rootMessage(e));
            }
            out.add(entry);
        }
        return listResult("options", out);
    }
}
