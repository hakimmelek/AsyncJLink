package com.asyncjlink.commands.compositecommands.assembly;

import com.asyncjlink.commands.CreoContext;
import com.asyncjlink.commands.compositecommands.Composite;
import com.asyncjlink.json.JsonArray;
import com.asyncjlink.json.JsonObject;
import com.asyncjlink.json.JsonSchema;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcInterference.GlobalEvaluator;
import com.ptc.pfc.pfcInterference.GlobalInterference;
import com.ptc.pfc.pfcInterference.GlobalInterferences;
import com.ptc.pfc.pfcInterference.InterferenceVolume;
import com.ptc.pfc.pfcInterference.pfcInterference;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcSelect.SelectionPair;

/**
 * Assembly.CheckInterference — global interference, with volumes.
 *
 * <p>Raw, this means building an evaluator, computing, then unpacking each pair and each volume by
 * hand. Volumes are optional because computing them is far more expensive than finding the pairs.
 */
public final class AssemblyCheckInterferenceCommand extends Composite {

    @Override public String name() { return "Assembly.CheckInterference"; }
    @Override public String receiverType() { return "Assembly"; }
    @Override public String signature() { return "CheckInterference(target[, computeVolumes]) — composite"; }

    @Override
    public String description() {
        return "Find every interfering pair of components in an assembly, optionally with the "
                + "interference volume for each. One call instead of evaluator plumbing.";
    }

    @Override
    public JsonSchema paramSchema() {
        return targeted(name(), "Assembly", "The assembly to check.")
                .optional("computeVolumes", JsonSchema.bool(),
                        "Compute the interference volume per pair. Expensive; default false.")
                .optional("exactComputation", JsonSchema.bool(),
                        "Ask Creo for an exact rather than approximate analysis. Default true.");
    }

    @Override
    public JsonObject execute(CreoContext ctx, JsonObject params) throws jxthrowable {
        Assembly assembly = requireAssembly(ctx, params);
        boolean computeVolumes = params.getBoolean("computeVolumes", false);
        boolean exact = params.getBoolean("exactComputation", true);

        GlobalEvaluator evaluator = pfcInterference.CreateGlobalEvaluator(assembly);
        if (evaluator == null) {
            return JsonObject.of(
                    "assembly", modelRef(ctx, (Model) assembly),
                    "ok", Boolean.FALSE,
                    "error", "Creo would not create an interference evaluator for this assembly");
        }

        GlobalInterferences found = evaluator.ComputeGlobalInterference(exact);
        JsonArray pairs = new JsonArray();
        if (found != null) {
            for (int i = 0; i < found.getarraysize(); i++) {
                GlobalInterference gi = found.get(i);
                if (gi == null) {
                    continue;
                }
                JsonObject entry = new JsonObject();
                entry.putIfPresent("components", describePair(ctx, gi));
                if (computeVolumes) {
                    entry.putIfPresent("volume", volume(gi));
                }
                pairs.add(entry);
            }
        }

        return JsonObject.of(
                "assembly", modelRef(ctx, (Model) assembly),
                "interfering", Boolean.valueOf(pairs.size() > 0),
                "pairCount", Integer.valueOf(pairs.size()),
                "pairs", pairs);
    }

    private static JsonArray describePair(CreoContext ctx, GlobalInterference gi) {
        try {
            SelectionPair pair = gi.GetSelParts();
            if (pair == null) {
                return null;
            }
            JsonArray out = new JsonArray();
            out.add(describeSide(ctx, pair.GetSel1()));
            out.add(describeSide(ctx, pair.GetSel2()));
            return out;
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }

    private static JsonObject describeSide(CreoContext ctx, Selection sel) {
        if (sel == null) {
            return null;
        }
        JsonObject o = JsonObject.of(
                "$handle", ctx.handles().handleFor(sel, "Selection"),
                "$type", "Selection");
        try {
            o.putIfPresent("model", modelRef(ctx, sel.GetSelModel()));
        } catch (jxthrowable | RuntimeException ignored) {
            // The pair is still useful without the owning model resolved.
        }
        return o;
    }

    private static Double volume(GlobalInterference gi) {
        try {
            InterferenceVolume vol = gi.GetVolume();
            return vol == null ? null : Double.valueOf(vol.ComputeVolume());
        } catch (jxthrowable | RuntimeException e) {
            return null;
        }
    }
}
