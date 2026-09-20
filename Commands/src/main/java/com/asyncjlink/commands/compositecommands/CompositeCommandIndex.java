package com.asyncjlink.commands.compositecommands;

import com.asyncjlink.commands.Command;
import com.asyncjlink.commands.compositecommands.assembly.AssemblyAddComponentCommand;
import com.asyncjlink.commands.compositecommands.assembly.AssemblyCheckInterferenceCommand;
import com.asyncjlink.commands.compositecommands.assembly.AssemblyGetBOMCommand;
import com.asyncjlink.commands.compositecommands.assembly.AssemblyReplaceComponentCommand;
import com.asyncjlink.commands.compositecommands.creation.DrawingCreateStandardViewsCommand;
import com.asyncjlink.commands.compositecommands.creation.SessionCreateDrawingCommand;
import com.asyncjlink.commands.compositecommands.creation.SessionCreateModelCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingDeleteNotesCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingDeleteSymbolsCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingDeleteTablesCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingGetNotesCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingGetSymbolsCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingGetTablesCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingSetModelsCommand;
import com.asyncjlink.commands.compositecommands.drawing.DrawingSetNoteTextCommand;
import com.asyncjlink.commands.compositecommands.family.ModelGetFamilyTableCommand;
import com.asyncjlink.commands.compositecommands.family.ModelSetFamilyTableCellsCommand;
import com.asyncjlink.commands.compositecommands.features.SolidDeleteFeaturesCommand;
import com.asyncjlink.commands.compositecommands.features.SolidResumeFeaturesCommand;
import com.asyncjlink.commands.compositecommands.features.SolidSuppressFeaturesCommand;
import com.asyncjlink.commands.compositecommands.inspection.ModelAuditCommand;
import com.asyncjlink.commands.compositecommands.inspection.ModelGetWhereUsedCommand;
import com.asyncjlink.commands.compositecommands.inspection.PartCreateMaterialWithPropertiesCommand;
import com.asyncjlink.commands.compositecommands.inspection.PartSetMaterialCommand;
import com.asyncjlink.commands.compositecommands.inspection.SolidFindFeaturesCommand;
import com.asyncjlink.commands.compositecommands.inspection.SolidGetMassPropertyReportCommand;
import com.asyncjlink.commands.compositecommands.lifecycle.ModelCreateBackupCommand;
import com.asyncjlink.commands.compositecommands.lifecycle.ModelSaveCheckedCommand;
import com.asyncjlink.commands.compositecommands.parametric.DrawingGetDimensionsCommand;
import com.asyncjlink.commands.compositecommands.parametric.DrawingSetDimensionTolerancesCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelEvaluateExpressionCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelGetDimensionsCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelGetParametersCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelGetRelationsCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelRegenerateAndReportCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelSetDimensionsCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelSetParametersCommand;
import com.asyncjlink.commands.compositecommands.parametric.ModelSetRelationsCommand;
import com.asyncjlink.commands.compositecommands.presentation.AssemblySetExplodedViewCommand;
import com.asyncjlink.commands.compositecommands.presentation.DrawingCheckUpToDateCommand;
import com.asyncjlink.commands.compositecommands.presentation.DrawingExportCommand;
import com.asyncjlink.commands.compositecommands.presentation.ModelCaptureImageCommand;
import com.asyncjlink.commands.compositecommands.presentation.ModelExportPackageCommand;
import com.asyncjlink.commands.compositecommands.presentation.ModelSetLayerStatusCommand;
import com.asyncjlink.commands.compositecommands.presentation.ModelSetViewCommand;
import com.asyncjlink.commands.compositecommands.selection.ModelFindItemsCommand;
import com.asyncjlink.commands.compositecommands.selection.ModelHighlightItemsCommand;
import com.asyncjlink.commands.compositecommands.selection.SessionGetSelectionCommand;
import com.asyncjlink.commands.compositecommands.session.SessionEraseModelsCommand;
import com.asyncjlink.commands.compositecommands.session.SessionGetConfigCommand;
import com.asyncjlink.commands.compositecommands.session.SessionGetContextCommand;
import com.asyncjlink.commands.compositecommands.session.SessionListDirectoryCommand;
import com.asyncjlink.commands.compositecommands.session.SessionLoadModelCommand;
import com.asyncjlink.commands.compositecommands.session.SessionSaveAllCommand;
import com.asyncjlink.commands.compositecommands.session.SessionSetWorkingDirectoryCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * The hand-maintained index of every composite command.
 *
 * <p>Unlike {@code RawCommandIndex}, which the generator rewrites on every run, this list is
 * maintained by hand: adding a composite means adding a line here. That is deliberate — the
 * generator deletes and rewrites the whole {@code rawcommands} tree, and anything it owned would be
 * lost.
 *
 * <p>A composite that collides with a raw command name fails at startup:
 * {@code CommandRegistry.add} rejects duplicates, so {@code --selfcheck} catches it immediately
 * rather than letting one silently shadow the other.
 */
public final class CompositeCommandIndex {

    private CompositeCommandIndex() {
    }

    /** Instantiates every composite command. */
    public static List<Command> all() {
        List<Command> out = new ArrayList<>();

        // Session and workspace
        out.add(new SessionGetContextCommand());
        out.add(new SessionGetConfigCommand());
        out.add(new SessionListDirectoryCommand());
        out.add(new SessionSetWorkingDirectoryCommand());
        out.add(new SessionLoadModelCommand());
        out.add(new SessionSaveAllCommand());
        out.add(new SessionEraseModelsCommand());

        // Creation
        out.add(new SessionCreateModelCommand());
        out.add(new SessionCreateDrawingCommand());

        // Addressing and selection
        out.add(new SessionGetSelectionCommand());
        out.add(new ModelFindItemsCommand());
        out.add(new ModelHighlightItemsCommand());

        // Parametric change
        out.add(new ModelGetParametersCommand());
        out.add(new ModelSetParametersCommand());
        out.add(new ModelGetDimensionsCommand());
        out.add(new ModelSetDimensionsCommand());
        out.add(new ModelGetRelationsCommand());
        out.add(new ModelSetRelationsCommand());
        out.add(new ModelEvaluateExpressionCommand());
        out.add(new ModelRegenerateAndReportCommand());

        // Feature operations
        out.add(new SolidSuppressFeaturesCommand());
        out.add(new SolidResumeFeaturesCommand());
        out.add(new SolidDeleteFeaturesCommand());

        // Assembly
        out.add(new AssemblyGetBOMCommand());
        out.add(new AssemblyCheckInterferenceCommand());
        out.add(new AssemblyAddComponentCommand());
        out.add(new AssemblyReplaceComponentCommand());

        // Inspection and reporting
        out.add(new SolidGetMassPropertyReportCommand());
        out.add(new SolidFindFeaturesCommand());
        out.add(new ModelAuditCommand());
        out.add(new ModelGetWhereUsedCommand());

        // Safety and lifecycle
        out.add(new ModelCreateBackupCommand());
        out.add(new ModelSaveCheckedCommand());

        // Export and display
        out.add(new ModelExportPackageCommand());
        out.add(new ModelSetViewCommand());
        out.add(new ModelCaptureImageCommand());
        out.add(new ModelSetLayerStatusCommand());
        out.add(new DrawingCheckUpToDateCommand());

        // Newly proposed (see docs/proposed-commands.md)
        out.add(new PartSetMaterialCommand());
        out.add(new PartCreateMaterialWithPropertiesCommand());
        out.add(new AssemblySetExplodedViewCommand());
        out.add(new ModelGetFamilyTableCommand());
        out.add(new ModelSetFamilyTableCellsCommand());
        out.add(new DrawingGetDimensionsCommand());
        out.add(new DrawingSetDimensionTolerancesCommand());
        out.add(new DrawingSetModelsCommand());
        out.add(new DrawingGetNotesCommand());
        out.add(new DrawingSetNoteTextCommand());
        out.add(new DrawingDeleteNotesCommand());
        out.add(new DrawingGetSymbolsCommand());
        out.add(new DrawingDeleteSymbolsCommand());
        out.add(new DrawingGetTablesCommand());
        out.add(new DrawingDeleteTablesCommand());
        out.add(new DrawingExportCommand());
        out.add(new DrawingCreateStandardViewsCommand());

        return out;
    }

    /** How many composites exist, for {@code --stats} and the self-check. */
    public static int count() {
        return all().size();
    }
}
