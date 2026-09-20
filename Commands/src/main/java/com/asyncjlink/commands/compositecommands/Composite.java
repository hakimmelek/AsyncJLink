package com.asyncjlink.commands.compositecommands;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.CommandException;
import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.Enums;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.Features;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcModelItem.ModelItems;
import com.ptc.pfc.pfcModelItem.NamedModelItem;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcModelItem.ParamValue;
import com.ptc.pfc.pfcModelItem.pfcModelItem;
import com.ptc.pfc.pfcPart.Material;
import com.ptc.pfc.pfcPart.Part;
import com.ptc.pfc.pfcSolid.Solid;
import com.ptc.pfc.pfcUnits.UnitSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Base class for the hand-written composite commands.
 *
 * <p>A composite does in one call what a J-Link user would otherwise do with anywhere from five to
 * several hundred raw calls. They exist for two reasons: {@code creoctl} cannot chain, because
 * handles die when the process exits, and an agent choosing between 937 near-identical tools chooses
 * badly.
 *
 * <p>Composites are indistinguishable from raw commands to call — same {@code Receiver.Method}
 * naming, same {@code --target} rule, same result envelope. What separates them is
 * {@link #jlinkPackage()}, which reports {@value #PACKAGE} rather than a {@code pfc*} package, so
 * {@code --list} groups them separately and a caller can always tell whether a command is one API
 * call or a workflow.
 *
 * <p>Unlike {@code rawcommands/}, this tree is written by hand and is never touched by
 * {@code tools/generate_commands.py}.
 *
 * <h2>Round trips are the budget</h2>
 *
 * <p>Asynchronous J-Link runs out of process: every accessor — {@code GetName}, {@code GetId},
 * {@code GetStatus} — is an IPC call to Creo costing on the order of tens of milliseconds. Measured
 * on a 187-feature part, a command making six calls per feature takes over 90 seconds and looks
 * hung; the same walk making one call per feature answers in about five.
 *
 * <p>So a composite that iterates is judged by calls per item, not by lines of code:
 * <ul>
 *   <li>Read each value once and pass it along — see
 *       {@link #itemRef(CreoContext, ModelItem, String)} — rather than re-reading it downstream.
 *   <li>Apply the cheapest filter first, and apply {@code limit} <em>before</em> the per-item reads,
 *       not after.
 *   <li>Put anything costing an extra call per item behind an opt-in flag, so the default stays
 *       fast and the caller chooses to pay for detail.
 * </ul>
 */
public abstract class Composite implements Command {

    /** The pseudo-package composites are listed under, in place of a {@code pfc*} one. */
    public static final String PACKAGE = "asyncjlink";

    @Override
    public String jlinkPackage() {
        return PACKAGE;
    }

    /**
     * One line saying what the composite does. Shown by the MCP tool list in place of the J-Link
     * signature a raw command would carry.
     */
    @Override
    public abstract String description();

    // ---- target resolution ----------------------------------------------

    /** Schema entry for the standard {@code target} field. */
    protected static JsonSchema targeted(String name, String jlinkType, String doc) {
        return JsonSchema.object()
                .describedAs(name + " — " + PACKAGE)
                .required("target", JsonSchema.handle(jlinkType), doc);
    }

    /** Schema for a composite that acts on the session and therefore takes no target. */
    protected static JsonSchema sessionScoped(String name) {
        return JsonSchema.object().describedAs(name + " — " + PACKAGE);
    }

    protected static Model requireModel(CreoContext ctx, JsonObject params) throws jxthrowable {
        String ref = params.getString("target", null);
        if (ref == null || ref.isEmpty()) {
            throw new CommandException("Field 'target' is required", "invalid_params");
        }
        Object handle = ctx.handles().lookup(ref);
        if (handle instanceof Model) {
            return (Model) handle;
        }
        Model m = ctx.resolveModel(ref);
        if (m == null) {
            throw new CommandException(
                    "Field 'target': no model named '" + ref + "' is in session. "
                            + "Retrieve it first with Session.LoadModel, or pass a handle.",
                    "unknown_handle");
        }
        return m;
    }

    protected static Solid requireSolid(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model m = requireModel(ctx, params);
        if (!(m instanceof Solid)) {
            throw new CommandException(
                    "Field 'target' must be a part or an assembly, but '" + params.getString("target", "")
                            + "' is a " + typeOf(m), "invalid_params");
        }
        return (Solid) m;
    }

    protected static Assembly requireAssembly(CreoContext ctx, JsonObject params) throws jxthrowable {
        Model m = requireModel(ctx, params);
        if (!(m instanceof Assembly)) {
            throw new CommandException(
                    "Field 'target' must be an assembly, but '" + params.getString("target", "")
                            + "' is a " + typeOf(m), "invalid_params");
        }
        return (Assembly) m;
    }

    protected static String typeOf(Object o) {
        String n = o.getClass().getSimpleName();
        if (n.endsWith("_i") || n.endsWith("_u")) {
            n = n.substring(0, n.length() - 2);
        }
        return n;
    }

    // ---- identification --------------------------------------------------

    /** Handle, name and type for a model — the shape every composite uses to refer to one. */
    protected static JsonObject modelRef(CreoContext ctx, Model m) throws jxthrowable {
        if (m == null) {
            return null;
        }
        JsonObject o = JsonObject.of(
                "$handle", ctx.handles().handleFor(m, typeOf(m)),
                "$type", typeOf(m));
        o.putIfPresent("fullName", m.GetFullName());
        o.putIfPresent("commonName", safeCommonName(m));
        o.putIfPresent("instanceName", safeInstanceName(m));
        return o;
    }

    private static String safeCommonName(Model m) {
        try {
            return m.GetCommonName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static String safeInstanceName(Model m) {
        try {
            return m.GetInstanceName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    /** Handle, id, name and type for any model item. Costs three Creo round trips. */
    protected static JsonObject itemRef(CreoContext ctx, ModelItem item) throws jxthrowable {
        return itemRef(ctx, item, safeName(item));
    }

    /**
     * As {@link #itemRef(CreoContext, ModelItem)}, but reusing a name the caller already read.
     *
     * <p>Every J-Link accessor is a round trip to Creo, costing tens of milliseconds, and a command
     * that walks a few hundred items pays that per call. Fetching the name once and passing it here
     * rather than letting this method fetch it again is the difference between a command that
     * answers in seconds and one that looks hung.
     */
    protected static JsonObject itemRef(CreoContext ctx, ModelItem item, String knownName)
            throws jxthrowable {
        if (item == null) {
            return null;
        }
        JsonObject o = JsonObject.of(
                "$handle", ctx.handles().handleFor(item, typeOf(item)),
                "$type", typeOf(item));
        o.put("id", Integer.valueOf(item.GetId()));
        o.putIfPresent("name", knownName);
        o.putIfPresent("itemType", Enums.toJson(item.GetType()));
        return o;
    }

    protected static String safeName(ModelItem item) {
        try {
            return item.GetName();
        } catch (jxthrowable | RuntimeException e) {
            // Plenty of item types have no name of their own; that is not an error.
            return null;
        }
    }

    /**
     * Name of a {@code NamedModelItem}.
     *
     * <p>Separate from {@link #safeName(ModelItem)} because {@code NamedModelItem} extends
     * {@code Child}, not {@code ModelItem} — a {@code Parameter} is named but is not a model item.
     */
    protected static String namedItemName(NamedModelItem item) {
        try {
            return item.GetName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    protected static String featureStatus(Feature f) {
        try {
            return Enums.toJson(f.GetStatus());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    // ---- listing ---------------------------------------------------------

    /** Every feature of a solid, visible ones and otherwise. */
    protected static List<Feature> features(Solid solid) throws jxthrowable {
        List<Feature> out = new ArrayList<>();
        Features fs = solid.ListFeaturesByType(Boolean.FALSE, null);
        if (fs == null) {
            return out;
        }
        for (int i = 0; i < fs.getarraysize(); i++) {
            Feature f = fs.get(i);
            if (f != null) {
                out.add(f);
            }
        }
        return out;
    }

    /** Every model item of one type. */
    protected static List<ModelItem> items(Model model, ModelItemType type) throws jxthrowable {
        List<ModelItem> out = new ArrayList<>();
        ModelItems list = model.ListItems(type);
        if (list == null) {
            return out;
        }
        for (int i = 0; i < list.getarraysize(); i++) {
            ModelItem item = list.get(i);
            if (item != null) {
                out.add(item);
            }
        }
        return out;
    }

    protected static List<String> strings(stringseq seq) throws jxthrowable {
        List<String> out = new ArrayList<>();
        if (seq == null) {
            return out;
        }
        for (int i = 0; i < seq.getarraysize(); i++) {
            out.add(seq.get(i));
        }
        return out;
    }

    protected static stringseq toStringseq(JsonArray array) throws jxthrowable {
        stringseq seq = stringseq.create();
        if (array != null) {
            for (int i = 0; i < array.size(); i++) {
                Object v = array.get(i);
                seq.append(v == null ? "" : String.valueOf(v));
            }
        }
        return seq;
    }

    protected static JsonArray toJsonArray(List<String> values) {
        JsonArray out = new JsonArray();
        for (String v : values) {
            out.add(v);
        }
        return out;
    }

    // ---- parameter values ------------------------------------------------

    /** Unwraps a {@code ParamValue} into a plain JSON scalar. */
    protected static Object paramValue(ParamValue pv) throws jxthrowable {
        if (pv == null) {
            return null;
        }
        String kind = Enums.toJson(pv.Getdiscr());
        if ("PARAM_STRING".equals(kind)) {
            return pv.GetStringValue();
        }
        if ("PARAM_INTEGER".equals(kind)) {
            return Integer.valueOf(pv.GetIntValue());
        }
        if ("PARAM_BOOLEAN".equals(kind)) {
            return Boolean.valueOf(pv.GetBoolValue());
        }
        if ("PARAM_DOUBLE".equals(kind)) {
            return Double.valueOf(pv.GetDoubleValue());
        }
        if ("PARAM_NOTE".equals(kind)) {
            return Integer.valueOf(pv.GetNoteId());
        }
        return null;
    }

    protected static String paramValueType(ParamValue pv) {
        try {
            return pv == null ? null : Enums.toJson(pv.Getdiscr());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    /**
     * Builds a {@code ParamValue} from a JSON scalar.
     *
     * <p>When {@code existing} is given, its type wins — writing the string {@code "12"} into an
     * existing double parameter should produce a double, not silently change the parameter's type.
     * That coercion is reported by the caller rather than done quietly.
     */
    protected static ParamValue toParamValue(Object json, ParamValue existing, String field)
            throws jxthrowable {
        String want = existing == null ? null : paramValueType(existing);
        if (want == null) {
            if (json instanceof Boolean) {
                return pfcModelItem.CreateBoolParamValue(((Boolean) json).booleanValue());
            }
            if (json instanceof Number) {
                double d = ((Number) json).doubleValue();
                if (d == Math.rint(d) && !Double.isInfinite(d)) {
                    return pfcModelItem.CreateIntParamValue((int) d);
                }
                return pfcModelItem.CreateDoubleParamValue(d);
            }
            return pfcModelItem.CreateStringParamValue(json == null ? "" : String.valueOf(json));
        }
        try {
            if ("PARAM_STRING".equals(want)) {
                return pfcModelItem.CreateStringParamValue(json == null ? "" : String.valueOf(json));
            }
            if ("PARAM_INTEGER".equals(want)) {
                return pfcModelItem.CreateIntParamValue((int) asDouble(json, field));
            }
            if ("PARAM_DOUBLE".equals(want)) {
                return pfcModelItem.CreateDoubleParamValue(asDouble(json, field));
            }
            if ("PARAM_BOOLEAN".equals(want)) {
                return pfcModelItem.CreateBoolParamValue(asBoolean(json, field));
            }
            if ("PARAM_NOTE".equals(want)) {
                return pfcModelItem.CreateIntParamValue((int) asDouble(json, field));
            }
        } catch (NumberFormatException e) {
            throw new CommandException(
                    "Parameter '" + field + "' is " + want + " but the value given was '" + json + "'",
                    "invalid_params");
        }
        return pfcModelItem.CreateStringParamValue(json == null ? "" : String.valueOf(json));
    }

    /**
     * Sets several parameters on a model at once, name/value pairs from JSON, reporting per
     * parameter whether it was created, set or rejected (a type mismatch, most commonly).
     *
     * <p>Shared by {@code Session.CreateModel} and {@code Session.CreateDrawing}: a drawing format's
     * title-block notes resolve their {@code &SYMBOL} text against the parameters of the model the
     * drawing documents, not the drawing itself, so filling in a format's fields is the same
     * operation as filling in a new part's — set the parameters, on whichever model actually owns
     * them, before the format tries to read them.
     */
    protected static JsonArray applyParameters(Model model, JsonObject wanted) throws jxthrowable {
        JsonArray out = new JsonArray();
        for (String key : wanted.keys()) {
            JsonObject entry = JsonObject.of("name", key);
            Object value = wanted.get(key);
            try {
                Parameter existing = model.GetParam(key);
                if (existing == null) {
                    ParamValue pv = toParamValue(value, null, key);
                    model.CreateParam(key, pv);
                    entry.put("action", "created");
                } else {
                    ParamValue pv = toParamValue(value, existing.GetValue(), key);
                    existing.SetValue(pv);
                    entry.put("action", "set");
                }
                entry.put("value", value);
            } catch (jxthrowable | RuntimeException e) {
                entry.put("action", "failed");
                entry.put("error", rootMessage(e));
            }
            out.add(entry);
        }
        return out;
    }

    private static double asDouble(Object json, String field) {
        if (json instanceof Number) {
            return ((Number) json).doubleValue();
        }
        if (json == null) {
            throw new CommandException("Parameter '" + field + "' needs a number", "invalid_params");
        }
        return Double.parseDouble(String.valueOf(json).trim());
    }

    private static boolean asBoolean(Object json, String field) {
        if (json instanceof Boolean) {
            return ((Boolean) json).booleanValue();
        }
        String s = String.valueOf(json).trim();
        if (s.equalsIgnoreCase("true") || s.equals("1")) {
            return true;
        }
        if (s.equalsIgnoreCase("false") || s.equals("0")) {
            return false;
        }
        throw new CommandException(
                "Parameter '" + field + "' needs true or false", "invalid_params");
    }

    // ---- regeneration ----------------------------------------------------

    /**
     * Regenerates a solid and reports what broke.
     *
     * <p>Every mutating composite ends with this. In Creo a change can succeed at the API level and
     * still leave the model broken — the call returns cleanly and the damage only surfaces several
     * operations later — so a mutation that does not report regeneration status trains its caller to
     * keep building on bad geometry.
     */
    protected static JsonObject regenerate(CreoContext ctx, Solid solid) {
        JsonObject out = new JsonObject();
        try {
            solid.Regenerate(null);
            out.put("regenerated", Boolean.TRUE);
        } catch (jxthrowable | RuntimeException e) {
            out.put("regenerated", Boolean.FALSE);
            out.put("regenerationError", rootMessage(e));
        }
        JsonArray failed = failedFeatures(ctx, solid);
        out.put("failedFeatureCount", Integer.valueOf(failed.size()));
        if (failed.size() > 0) {
            out.put("failedFeatures", failed);
        }
        return out;
    }

    /** Features left in a state that should worry the caller, named rather than merely counted. */
    protected static JsonArray failedFeatures(CreoContext ctx, Solid solid) {
        JsonArray out = new JsonArray();
        try {
            for (Feature f : features(solid)) {
                String status = featureStatus(f);
                if (status == null || "FEAT_ACTIVE".equals(status)) {
                    continue;
                }
                if (status.equals("FEAT_SUPPRESSED") || status.equals("FEAT_INACTIVE")
                        || status.equals("FEAT_FAMILY_TABLE_SUPPRESSED")
                        || status.equals("FEAT_SIMP_REP_SUPPRESSED")
                        || status.equals("FEAT_PROGRAM_SUPPRESSED")) {
                    // Suppressed on purpose is not a failure; it is reported by Model.Audit instead.
                    continue;
                }
                JsonObject item = itemRef(ctx, f);
                item.put("status", status);
                out.add(item);
            }
        } catch (jxthrowable | RuntimeException e) {
            // Reporting is best-effort: the mutation already happened, and losing the report should
            // not turn a successful change into an exception.
            out.add(JsonObject.of("error", rootMessage(e)));
        }
        return out;
    }

    // ---- config-sourced paths ----------------------------------------------

    /**
     * Splits a template/model path read from {@code config.pro} into a directory and a bare file
     * name Creo will actually accept, expanding environment tokens and stripping a trailing revision
     * number along the way.
     *
     * <p>{@code config.pro} values routinely look like {@code "$PRO_DIRECTORY\templates\c_drawing.drw"}
     * or {@code "D:\gabarits\start_asm.asm.12"} — Creo expands the {@code $VAR} token and tolerates
     * the trailing {@code .NN} revision itself when a person fills in the New dialog, but
     * {@code ModelDescriptor_CreateFromFileName}, {@code GetModelFromFileName} and
     * {@code CreateDrawingFromTemplate} do not: passed either form verbatim they fail with
     * {@code XUnknownModelExtension} or {@code XStringTooLong} instead of resolving. The directory
     * half (index 0, {@code null} if the value was already a bare name) is meant for
     * {@link #inDirectory}; the bare name (index 1) is what the J-Link call itself should receive.
     */
    protected static String[] splitTemplatePath(CreoContext ctx, String rawValue) {
        String stripped = sanitizeConfigPath(ctx, rawValue);
        int cut = Math.max(stripped.lastIndexOf('\\'), stripped.lastIndexOf('/'));
        if (cut < 0) {
            return new String[] {null, stripped};
        }
        return new String[] {stripped.substring(0, cut), stripped.substring(cut + 1)};
    }

    /**
     * As {@link #splitTemplatePath}, but without splitting off a directory — for a J-Link call whose
     * template parameter is documented/observed to want a resolvable name rather than a path, and
     * where {@link #inDirectory} is unsafe to use because the call also needs a second, already-open
     * model to stay resolvable (changing directory to reach the template can make that model
     * unreachable instead). {@code CreateDrawingFromTemplate} is exactly this case: on this
     * environment it was found to crash the whole async connection outright, rather than raise an
     * ordinary error, when given a bare name while cd'd away from the documented model — so this
     * expands and strips the value but leaves resolving it up to Creo's own search path.
     */
    protected static String sanitizeConfigPath(CreoContext ctx, String rawValue) {
        String expanded = expandEnvTokens(ctx, rawValue);
        return expanded.replaceAll("\\.[0-9]+$", "");
    }

    private static String expandEnvTokens(CreoContext ctx, String value) {
        Matcher m = Pattern.compile("\\$([A-Za-z_][A-Za-z0-9_]*)").matcher(value);
        StringBuffer out = new StringBuffer();
        while (m.find()) {
            String replacement = envValue(ctx, m.group(1));
            m.appendReplacement(out, Matcher.quoteReplacement(replacement != null ? replacement : m.group()));
        }
        m.appendTail(out);
        return out.toString();
    }

    private static String envValue(CreoContext ctx, String name) {
        try {
            String v = ctx.session().GetEnvironmentVariable(name);
            if (v != null && !v.isEmpty()) {
                return v;
            }
        } catch (jxthrowable | RuntimeException e) {
            // Fall through to the JVM's own environment.
        }
        return System.getenv(name);
    }

    /** An operation that runs with the session's working directory temporarily changed. */
    protected interface InDirectoryAction<T> {
        T run() throws jxthrowable;
    }

    /**
     * Runs {@code action} with the session's working directory changed to {@code directory},
     * restoring the previous directory afterwards even if {@code action} throws.
     *
     * <p>{@code ModelDescriptor_CreateFromFileName} and {@code CreateDrawingFromTemplate} resolve a
     * bare file name against the working directory and Creo's search path, not against a directory
     * baked into the name itself — a template that lives outside both has to be reached this way.
     * {@code directory} may be {@code null} (from {@link #splitTemplatePath}, when the value was
     * already a bare name), in which case {@code action} just runs where it is.
     */
    protected static <T> T inDirectory(CreoContext ctx, String directory, InDirectoryAction<T> action)
            throws jxthrowable {
        if (directory == null || directory.isEmpty()) {
            return action.run();
        }
        String previous;
        try {
            previous = ctx.session().GetCurrentDirectory();
        } catch (jxthrowable | RuntimeException e) {
            previous = null;
        }
        ctx.session().ChangeDirectory(directory);
        try {
            return action.run();
        } finally {
            if (previous != null) {
                try {
                    ctx.session().ChangeDirectory(previous);
                } catch (jxthrowable | RuntimeException e) {
                    // Best-effort restore; failing to put the working directory back should not mask
                    // whatever action() returned or threw.
                }
            }
        }
    }

    // ---- misc ------------------------------------------------------------

    protected static boolean dryRun(JsonObject params) {
        return params.getBoolean("dryRun", false);
    }

    protected static String unitSystem(Solid solid) {
        try {
            UnitSystem us = solid.GetPrincipalUnits();
            return us == null ? null : us.GetName();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    protected static Material currentMaterial(Solid solid) {
        if (!(solid instanceof Part)) {
            return null;
        }
        try {
            return ((Part) solid).GetCurrentMaterial();
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    /** Compiles a caller-supplied name filter. Accepts {@code *} and {@code ?} as wildcards. */
    protected static Pattern glob(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (c == '*') {
                sb.append(".*");
            } else if (c == '?') {
                sb.append('.');
            } else {
                sb.append(Pattern.quote(String.valueOf(c)));
            }
        }
        try {
            return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            throw new CommandException("Bad name pattern '" + pattern + "'", "invalid_params");
        }
    }

    protected static boolean matches(Pattern p, String value) {
        return p == null || (value != null && p.matcher(value).matches());
    }

    protected static String rootMessage(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null) {
            c = c.getCause();
        }
        String m = c.getMessage();
        return m == null || m.isEmpty() ? c.getClass().getSimpleName() : m;
    }

    /** The standard success envelope for composites that return a list. */
    protected static JsonObject listResult(String key, JsonArray values) {
        return JsonObject.of("count", Integer.valueOf(values.size()), key, values);
    }
}
