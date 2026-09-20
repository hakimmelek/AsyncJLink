/*
 * GENERATED FILE -- DO NOT EDIT.
 *
 * Produced by tools/generate_commands.py from docs/jlink-api-asynchronous.md.
 * Edit the generator, not this file; every run overwrites the whole rawcommands tree.
 */

package com.asyncjlink.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Every type the J-Link dictionary declares, with the classification that decides how it
 * crosses the JSON boundary. 930 entries.
 *
 * <p>Registration is split across several methods because a single static initialiser holding
 * this many entries would exceed the JVM's 64KB per-method bytecode limit.
 */
public final class TypeRegistry {

    private TypeRegistry() {
    }

    private static final Map<String, TypeKind> KINDS = new HashMap<>();
    private static final Map<String, String> PACKAGES = new HashMap<>();
    private static final Map<String, Class<?>> CLASSES = new ConcurrentHashMap<>();

    static {
        register0();
        register1();
        register2();
        register3();
        register4();
        register5();
        register6();
        register7();
    }

    private static void register0() {
        put("ACIS3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("ASSEMTreeCFGImportInstructions", "pfcModel", TypeKind.DATA);
        put("ActionListener", "pfcBase", TypeKind.LISTENER);
        put("ActionListener_u", "pfcBase", TypeKind.DATA);
        put("ActionListeners", "pfcBase", TypeKind.SEQUENCE);
        put("ActionSource", "pfcBase", TypeKind.DATA);
        put("ActionSources", "pfcBase", TypeKind.SEQUENCE);
        put("AngleDimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("Arc", "pfcGeometry", TypeKind.LIVE);
        put("ArcDescriptor", "pfcGeometry", TypeKind.DATA);
        put("ArgValue", "pfcArgument", TypeKind.SEQUENCE);
        put("ArgValueType", "pfcArgument", TypeKind.ENUM);
        put("Argument", "pfcArgument", TypeKind.DATA);
        put("Arguments", "pfcArgument", TypeKind.SEQUENCE);
        put("Arrow", "pfcGeometry", TypeKind.LIVE);
        put("ArrowDescriptor", "pfcGeometry", TypeKind.DATA);
        put("Assembly", "pfcAssembly", TypeKind.LIVE);
        put("AssemblyConfiguration", "pfcExport", TypeKind.ENUM);
        put("AsyncActionListener", "pfcAsyncConnection", TypeKind.LISTENER);
        put("AsyncActionListener_u", "pfcAsyncConnection", TypeKind.DATA);
        put("AsyncConnection", "pfcAsyncConnection", TypeKind.DATA);
        put("Attachment", "pfcDetail", TypeKind.DATA);
        put("AttachmentType", "pfcDetail", TypeKind.ENUM);
        put("Attachments", "pfcDetail", TypeKind.SEQUENCE);
        put("Axis", "pfcGeometry", TypeKind.LIVE);
        put("BOMExportInstructions", "pfcModel", TypeKind.DATA);
        put("BSpline", "pfcGeometry", TypeKind.LIVE);
        put("BSplineDescriptor", "pfcGeometry", TypeKind.DATA);
        put("BSplinePoint", "pfcGeometry", TypeKind.DATA);
        put("BSplinePoints", "pfcGeometry", TypeKind.SEQUENCE);
        put("BaseDimension", "pfcDimension", TypeKind.LIVE);
        put("BaseDimensions", "pfcDimension", TypeKind.SEQUENCE);
        put("BaseParameter", "pfcModelItem", TypeKind.LIVE);
        put("BaseSession", "pfcSession", TypeKind.LIVE);
        put("BitmapImageExportInstructions", "pfcWindow", TypeKind.DATA);
        put("CADDSExportInstructions", "pfcExport", TypeKind.DATA);
        put("CATIAFacetsExportInstructions", "pfcModel", TypeKind.DATA);
        put("CATIAModel3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("CATIASession3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("CGMExportType", "pfcModel", TypeKind.ENUM);
        put("CGMFILEExportInstructions", "pfcModel", TypeKind.DATA);
        put("CGMScaleType", "pfcModel", TypeKind.ENUM);
        put("CableDisplayStyle", "pfcBase", TypeKind.ENUM);
        put("CableParamsFileInstructions", "pfcModel", TypeKind.DATA);
        put("CableParamsImportInstructions", "pfcModel", TypeKind.DATA);
        put("CatiaCGR3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("CatiaPart3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("CatiaProduct3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("CheckinOptions", "pfcServer", TypeKind.DATA);
        put("CheckoutOptions", "pfcServer", TypeKind.DATA);
        put("Child", "pfcObject", TypeKind.LIVE);
        put("Circle", "pfcGeometry", TypeKind.LIVE);
        put("CircleDescriptor", "pfcGeometry", TypeKind.DATA);
        put("ClearanceData", "pfcInterference", TypeKind.LIVE);
        put("ColorRGB", "pfcBase", TypeKind.DATA);
        put("ColumnCreateOption", "pfcTable", TypeKind.DATA);
        put("ColumnCreateOptions", "pfcTable", TypeKind.SEQUENCE);
        put("ColumnJustification", "pfcTable", TypeKind.ENUM);
        put("CommandAccess", "pfcCommand", TypeKind.ENUM);
        put("CompModelReplace", "pfcComponentFeat", TypeKind.LIVE);
        put("ComponentConstraint", "pfcComponentFeat", TypeKind.DATA);
        put("ComponentConstraintType", "pfcComponentFeat", TypeKind.ENUM);
        put("ComponentConstraints", "pfcComponentFeat", TypeKind.SEQUENCE);
        put("ComponentDimensionShowInstructions", "pfcAssembly", TypeKind.DATA);
        put("ComponentFeat", "pfcComponentFeat", TypeKind.LIVE);
        put("ComponentFeats", "pfcComponentFeat", TypeKind.SEQUENCE);
        put("ComponentPath", "pfcAssembly", TypeKind.LIVE);
        put("ComponentPaths", "pfcAssembly", TypeKind.SEQUENCE);
        put("ComponentType", "pfcComponentFeat", TypeKind.ENUM);
        put("CompositeCurve", "pfcGeometry", TypeKind.LIVE);
        put("CompositeCurveDescriptor", "pfcGeometry", TypeKind.DATA);
        put("Cone", "pfcGeometry", TypeKind.LIVE);
        put("ConeDescriptor", "pfcGeometry", TypeKind.DATA);
        put("ConfigImportInstructions", "pfcModel", TypeKind.DATA);
        put("ConnectionId", "pfcAsyncConnection", TypeKind.DATA);
        put("ConnectorParamExportInstructions", "pfcModel", TypeKind.DATA);
        put("ConnectorParamsImportInstructions", "pfcModel", TypeKind.DATA);
        put("ConstraintAttributes", "pfcComponentFeat", TypeKind.DATA);
        put("Contour", "pfcGeometry", TypeKind.LIVE);
        put("ContourTraversal", "pfcGeometry", TypeKind.ENUM);
        put("Contours", "pfcGeometry", TypeKind.SEQUENCE);
        put("CoonsCornerPoints", "pfcGeometry", TypeKind.SEQUENCE);
        put("CoonsPatch", "pfcGeometry", TypeKind.LIVE);
        put("CoonsPatchDescriptor", "pfcGeometry", TypeKind.DATA);
        put("CoonsUVDerivatives", "pfcGeometry", TypeKind.SEQUENCE);
        put("CoordAxis", "pfcBase", TypeKind.ENUM);
        put("CoordSysExportInstructions", "pfcModel", TypeKind.DATA);
        put("CoordSysFeat", "pfcCoordSysFeat", TypeKind.LIVE);
        put("CoordSystem", "pfcGeometry", TypeKind.LIVE);
        put("CopyInstructions", "pfcModel", TypeKind.DATA);
        put("CreateNewSimpRepInstructions", "pfcSimpRep", TypeKind.DATA);
        put("CriticalDistanceData", "pfcInterference", TypeKind.LIVE);
        put("CurvatureData", "pfcGeometry", TypeKind.DATA);
        put("Curve", "pfcGeometry", TypeKind.LIVE);
        put("CurveDescriptor", "pfcGeometry", TypeKind.DATA);
        put("CurveDescriptors", "pfcGeometry", TypeKind.SEQUENCE);
        put("CurveFeat", "pfcCurveFeat", TypeKind.LIVE);
        put("CurveStartPoint", "pfcCurveFeat", TypeKind.ENUM);
        put("CurveType", "pfcGeometry", TypeKind.ENUM);
        put("CurveXYZData", "pfcGeometry", TypeKind.DATA);
        put("Curves", "pfcGeometry", TypeKind.SEQUENCE);
        put("CustomCheckInstructions", "pfcModelCheck", TypeKind.DATA);
        put("CustomCheckResults", "pfcModelCheck", TypeKind.DATA);
        put("Cylinder", "pfcGeometry", TypeKind.LIVE);
        put("CylinderDescriptor", "pfcGeometry", TypeKind.DATA);
        put("CylindricalSplineSurface", "pfcGeometry", TypeKind.LIVE);
        put("CylindricalSplineSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("DWG3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("DWGImport2DInstructions", "pfcModel", TypeKind.DATA);
        put("DWGSetupExportInstructions", "pfcModel", TypeKind.DATA);
        put("DWGSetupImportInstructions", "pfcModel", TypeKind.DATA);
        put("DXF3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("DXFExportInstructions", "pfcModel", TypeKind.DATA);
        put("DXFImport2DInstructions", "pfcModel", TypeKind.DATA);
        put("DatumAxisConstraint", "pfcDatumAxisFeat", TypeKind.DATA);
        put("DatumAxisConstraintType", "pfcDatumAxisFeat", TypeKind.ENUM);
        put("DatumAxisConstraints", "pfcDatumAxisFeat", TypeKind.SEQUENCE);
        put("DatumAxisDimensionConstraint", "pfcDatumAxisFeat", TypeKind.DATA);
        put("DatumAxisDimensionConstraints", "pfcDatumAxisFeat", TypeKind.SEQUENCE);
        put("DatumAxisFeat", "pfcDatumAxisFeat", TypeKind.LIVE);
    }

    private static void register1() {
        put("DatumCsysDimConstraintType", "pfcCoordSysFeat", TypeKind.ENUM);
        put("DatumCsysDimensionConstraint", "pfcCoordSysFeat", TypeKind.DATA);
        put("DatumCsysDimensionConstraints", "pfcCoordSysFeat", TypeKind.SEQUENCE);
        put("DatumCsysOffsetType", "pfcCoordSysFeat", TypeKind.ENUM);
        put("DatumCsysOnSurfaceType", "pfcCoordSysFeat", TypeKind.ENUM);
        put("DatumCsysOrientByMethod", "pfcCoordSysFeat", TypeKind.ENUM);
        put("DatumCsysOrientMoveConstraint", "pfcCoordSysFeat", TypeKind.DATA);
        put("DatumCsysOrientMoveConstraintType", "pfcCoordSysFeat", TypeKind.ENUM);
        put("DatumCsysOrientMoveConstraints", "pfcCoordSysFeat", TypeKind.SEQUENCE);
        put("DatumCsysOriginConstraint", "pfcCoordSysFeat", TypeKind.DATA);
        put("DatumCsysOriginConstraints", "pfcCoordSysFeat", TypeKind.SEQUENCE);
        put("DatumPlaneAngleConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneConstraintType", "pfcDatumPlaneFeat", TypeKind.ENUM);
        put("DatumPlaneConstraints", "pfcDatumPlaneFeat", TypeKind.SEQUENCE);
        put("DatumPlaneDefaultXConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneDefaultYConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneDefaultZConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneFeat", "pfcDatumPlaneFeat", TypeKind.LIVE);
        put("DatumPlaneNormalConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneOffsetConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneOffsetCoordSysConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneParallelConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneSectionConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneTangentConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPlaneThroughConstraint", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("DatumPointConstraint", "pfcDatumPointFeat", TypeKind.DATA);
        put("DatumPointConstraintType", "pfcDatumPointFeat", TypeKind.ENUM);
        put("DatumPointDimensionConstraint", "pfcDatumPointFeat", TypeKind.DATA);
        put("DatumPointDimensionConstraints", "pfcDatumPointFeat", TypeKind.SEQUENCE);
        put("DatumPointFeat", "pfcDatumPointFeat", TypeKind.LIVE);
        put("DatumPointPlacementConstraint", "pfcDatumPointFeat", TypeKind.DATA);
        put("DatumPointPlacementConstraints", "pfcDatumPointFeat", TypeKind.SEQUENCE);
        put("DatumSide", "pfcBase", TypeKind.ENUM);
        put("DefaultAsyncActionListener", "pfcAsyncConnection", TypeKind.LISTENER);
        put("DefaultDisplayListener", "pfcDisplay", TypeKind.LISTENER);
        put("DefaultFeatureActionListener", "pfcFeature", TypeKind.LISTENER);
        put("DefaultFileOpenRegisterListener", "pfcUI", TypeKind.LISTENER);
        put("DefaultFileSaveRegisterListener", "pfcUI", TypeKind.LISTENER);
        put("DefaultJLinkTaskListener", "pfcJLink", TypeKind.LISTENER);
        put("DefaultLayerImportFilter", "pfcImport", TypeKind.DATA);
        put("DefaultModelActionListener", "pfcModel", TypeKind.LISTENER);
        put("DefaultModelCheckCustomCheckListener", "pfcModelCheck", TypeKind.LISTENER);
        put("DefaultModelEventActionListener", "pfcModel", TypeKind.LISTENER);
        put("DefaultPopupmenuListener", "pfcUI", TypeKind.LISTENER);
        put("DefaultRelationFunctionListener", "pfcRelations", TypeKind.LISTENER);
        put("DefaultSessionActionListener", "pfcSession", TypeKind.LISTENER);
        put("DefaultSolidActionListener", "pfcSolid", TypeKind.LISTENER);
        put("DefaultUICommandAccessListener", "pfcCommand", TypeKind.LISTENER);
        put("DefaultUICommandActionListener", "pfcCommand", TypeKind.LISTENER);
        put("DefaultUICommandBracketListener", "pfcCommand", TypeKind.LISTENER);
        put("DeleteOperation", "pfcFeature", TypeKind.LIVE);
        put("Dependencies", "pfcModel", TypeKind.SEQUENCE);
        put("Dependency", "pfcModel", TypeKind.DATA);
        put("DescriptorContainer", "pfcSession", TypeKind.DATA);
        put("DescriptorContainer2", "pfcSession", TypeKind.DATA);
        put("DetailCreateInstructions", "pfcDetail", TypeKind.DATA);
        put("DetailEntityInstructions", "pfcDetail", TypeKind.DATA);
        put("DetailEntityItem", "pfcDetail", TypeKind.LIVE);
        put("DetailGroupInstructions", "pfcDetail", TypeKind.DATA);
        put("DetailGroupItem", "pfcDetail", TypeKind.LIVE);
        put("DetailItem", "pfcDetail", TypeKind.LIVE);
        put("DetailItemOwner", "pfcDetail", TypeKind.DATA);
        put("DetailItems", "pfcDetail", TypeKind.SEQUENCE);
        put("DetailLeaders", "pfcDetail", TypeKind.DATA);
        put("DetailNoteInstructions", "pfcDetail", TypeKind.DATA);
        put("DetailNoteItem", "pfcDetail", TypeKind.LIVE);
        put("DetailOLEObject", "pfcDetail", TypeKind.LIVE);
        put("DetailSymbolDefInstructions", "pfcDetail", TypeKind.DATA);
        put("DetailSymbolDefItem", "pfcDetail", TypeKind.LIVE);
        put("DetailSymbolGroup", "pfcDetail", TypeKind.LIVE);
        put("DetailSymbolGroupInstructions", "pfcDetail", TypeKind.DATA);
        put("DetailSymbolGroupOption", "pfcDetail", TypeKind.ENUM);
        put("DetailSymbolGroups", "pfcDetail", TypeKind.SEQUENCE);
        put("DetailSymbolInstInstructions", "pfcDetail", TypeKind.DATA);
        put("DetailSymbolInstItem", "pfcDetail", TypeKind.LIVE);
        put("DetailText", "pfcDetail", TypeKind.DATA);
        put("DetailTextLine", "pfcDetail", TypeKind.DATA);
        put("DetailTextLines", "pfcDetail", TypeKind.SEQUENCE);
        put("DetailTexts", "pfcDetail", TypeKind.SEQUENCE);
        put("DetailType", "pfcDetail", TypeKind.ENUM);
        put("DetailVariantText", "pfcDetail", TypeKind.DATA);
        put("DetailVariantTexts", "pfcDetail", TypeKind.SEQUENCE);
        put("Diagram", "pfcDiagram", TypeKind.LIVE);
        put("DimDisplayMode", "pfcBase", TypeKind.ENUM);
        put("DimTolISODIN", "pfcDimension", TypeKind.DATA);
        put("DimTolLimits", "pfcDimension", TypeKind.DATA);
        put("DimTolPlusMinus", "pfcDimension", TypeKind.DATA);
        put("DimTolSymSuperscript", "pfcDimension", TypeKind.DATA);
        put("DimTolSymmetric", "pfcDimension", TypeKind.DATA);
        put("DimTolerance", "pfcDimension", TypeKind.DATA);
        put("DimToleranceType", "pfcDimension", TypeKind.ENUM);
        put("Dimension", "pfcDimension", TypeKind.LIVE);
        put("Dimension2D", "pfcDimension2D", TypeKind.LIVE);
        put("Dimension2Ds", "pfcDimension2D", TypeKind.SEQUENCE);
        put("DimensionAngleOptions", "pfcDimension2D", TypeKind.DATA);
        put("DimensionLinAOCTangentType", "pfcDimension2D", TypeKind.ENUM);
        put("DimensionPointType", "pfcDimension2D", TypeKind.ENUM);
        put("DimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("DimensionSenseType", "pfcDimension2D", TypeKind.ENUM);
        put("DimensionSenses", "pfcDimension2D", TypeKind.SEQUENCE);
        put("DimensionShowInstructions", "pfcDimension", TypeKind.DATA);
        put("DimensionType", "pfcDimension", TypeKind.ENUM);
        put("DirectorySelectionOptions", "pfcUI", TypeKind.DATA);
        put("Display", "pfcDisplay", TypeKind.LIVE);
        put("DisplayList2D", "pfcDisplay", TypeKind.LIVE);
        put("DisplayList3D", "pfcDisplay", TypeKind.LIVE);
        put("DisplayListener", "pfcDisplay", TypeKind.LISTENER);
        put("DisplayListener_u", "pfcDisplay", TypeKind.DATA);
        put("DisplayStatus", "pfcLayer", TypeKind.ENUM);
        put("DisplayStatuses", "pfcLayer", TypeKind.SEQUENCE);
        put("DisplayStyle", "pfcBase", TypeKind.ENUM);
        put("Dll", "pfcProToolkit", TypeKind.LIVE);
        put("DotsPerInch", "pfcWindow", TypeKind.ENUM);
        put("Drawing", "pfcDrawing", TypeKind.LIVE);
        put("DrawingCreateError", "pfcExceptions", TypeKind.UNKNOWN);
        put("DrawingCreateErrorType", "pfcExceptions", TypeKind.ENUM);
        put("DrawingCreateErrors", "pfcExceptions", TypeKind.SEQUENCE);
        put("DrawingCreateOption", "pfcDrawing", TypeKind.ENUM);
        put("DrawingCreateOptions", "pfcDrawing", TypeKind.SEQUENCE);
    }

    private static void register2() {
        put("DrawingDimCreateInstructions", "pfcDimension2D", TypeKind.DATA);
        put("DrawingDimensionShowInstructions", "pfcView2D", TypeKind.DATA);
        put("DrawingFormat", "pfcDrawingFormat", TypeKind.LIVE);
        put("EPSImageExportInstructions", "pfcWindow", TypeKind.DATA);
        put("Edge", "pfcGeometry", TypeKind.LIVE);
        put("EdgeEvalData", "pfcGeometry", TypeKind.DATA);
        put("Edges", "pfcGeometry", TypeKind.SEQUENCE);
        put("Ellipse", "pfcGeometry", TypeKind.LIVE);
        put("EllipseDescriptor", "pfcGeometry", TypeKind.DATA);
        put("EmptyDimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("Envelope2D", "pfcBase", TypeKind.SEQUENCE);
        put("ExplodedState", "pfcAssembly", TypeKind.LIVE);
        put("Export2DOption", "pfcModel", TypeKind.DATA);
        put("Export2DSheetOption", "pfcModel", TypeKind.ENUM);
        put("Export3DInstructions", "pfcExport", TypeKind.DATA);
        put("ExportInstructions", "pfcModel", TypeKind.DATA);
        put("ExportType", "pfcModel", TypeKind.ENUM);
        put("ExternalData", "pfcExternal", TypeKind.SEQUENCE);
        put("ExternalDataAccess", "pfcExternal", TypeKind.LIVE);
        put("ExternalDataClass", "pfcExternal", TypeKind.LIVE);
        put("ExternalDataClasses", "pfcExternal", TypeKind.SEQUENCE);
        put("ExternalDataSlot", "pfcExternal", TypeKind.LIVE);
        put("ExternalDataSlots", "pfcExternal", TypeKind.SEQUENCE);
        put("ExternalDataType", "pfcExternal", TypeKind.ENUM);
        put("FIATExportInstructions", "pfcModel", TypeKind.DATA);
        put("FacetControlFlag", "pfcModel", TypeKind.ENUM);
        put("FacetControlFlags", "pfcModel", TypeKind.SEQUENCE);
        put("FamColComp", "pfcFamily", TypeKind.LIVE);
        put("FamColCompModel", "pfcFamily", TypeKind.LIVE);
        put("FamColDimension", "pfcFamily", TypeKind.LIVE);
        put("FamColExternalRef", "pfcFamily", TypeKind.LIVE);
        put("FamColFeature", "pfcFamily", TypeKind.LIVE);
        put("FamColGTol", "pfcFamily", TypeKind.LIVE);
        put("FamColGroup", "pfcFamily", TypeKind.LIVE);
        put("FamColIParNote", "pfcFamily", TypeKind.LIVE);
        put("FamColInhFeatPart", "pfcFamily", TypeKind.LIVE);
        put("FamColMergePart", "pfcFamily", TypeKind.LIVE);
        put("FamColModelItem", "pfcFamily", TypeKind.LIVE);
        put("FamColParam", "pfcFamily", TypeKind.LIVE);
        put("FamColSim", "pfcFamily", TypeKind.LIVE);
        put("FamColSystemParam", "pfcFamily", TypeKind.LIVE);
        put("FamColTolMinus", "pfcFamily", TypeKind.LIVE);
        put("FamColTolPlus", "pfcFamily", TypeKind.LIVE);
        put("FamColTolPlusMinus", "pfcFamily", TypeKind.LIVE);
        put("FamColUDF", "pfcFamily", TypeKind.LIVE);
        put("FamilyColumnType", "pfcFamily", TypeKind.ENUM);
        put("FamilyMember", "pfcFamily", TypeKind.LIVE);
        put("FamilyTableColumn", "pfcFamily", TypeKind.LIVE);
        put("FamilyTableColumns", "pfcFamily", TypeKind.SEQUENCE);
        put("FamilyTableRow", "pfcFamily", TypeKind.LIVE);
        put("FamilyTableRows", "pfcFamily", TypeKind.SEQUENCE);
        put("FaminstanceVerifyStatus", "pfcFamily", TypeKind.ENUM);
        put("FeatIdExportInstructions", "pfcModel", TypeKind.DATA);
        put("FeatInfoExportInstructions", "pfcModel", TypeKind.DATA);
        put("Feature", "pfcFeature", TypeKind.LIVE);
        put("FeatureActionListener", "pfcFeature", TypeKind.LISTENER);
        put("FeatureActionListener_u", "pfcFeature", TypeKind.DATA);
        put("FeatureCopyType", "pfcFeature", TypeKind.ENUM);
        put("FeatureCreateInstructions", "pfcFeature", TypeKind.DATA);
        put("FeatureGroup", "pfcFeature", TypeKind.LIVE);
        put("FeatureGroups", "pfcFeature", TypeKind.SEQUENCE);
        put("FeatureOperation", "pfcFeature", TypeKind.LIVE);
        put("FeatureOperations", "pfcFeature", TypeKind.SEQUENCE);
        put("FeaturePattern", "pfcFeature", TypeKind.LIVE);
        put("FeaturePlacement", "pfcFeature", TypeKind.LIVE);
        put("FeatureStatus", "pfcFeature", TypeKind.ENUM);
        put("FeatureType", "pfcFeature", TypeKind.ENUM);
        put("Features", "pfcFeature", TypeKind.SEQUENCE);
        put("FileListOpt", "pfcSession", TypeKind.ENUM);
        put("FileOpenOptions", "pfcUI", TypeKind.DATA);
        put("FileOpenRegisterListener", "pfcUI", TypeKind.LISTENER);
        put("FileOpenRegisterListener_u", "pfcUI", TypeKind.DATA);
        put("FileOpenRegisterOptions", "pfcUI", TypeKind.DATA);
        put("FileOpenShortcut", "pfcUI", TypeKind.DATA);
        put("FileOpenShortcuts", "pfcUI", TypeKind.SEQUENCE);
        put("FileSaveOptions", "pfcUI", TypeKind.DATA);
        put("FileSaveRegisterListener", "pfcUI", TypeKind.LISTENER);
        put("FileSaveRegisterListener_u", "pfcUI", TypeKind.DATA);
        put("FileSaveRegisterOptions", "pfcUI", TypeKind.DATA);
        put("FileUIOptions", "pfcUI", TypeKind.DATA);
        put("FilletSurface", "pfcGeometry", TypeKind.LIVE);
        put("FilletSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("FolderAssignment", "pfcServer", TypeKind.DATA);
        put("FolderAssignments", "pfcServer", TypeKind.SEQUENCE);
        put("Font", "pfcDisplay", TypeKind.DATA);
        put("ForeignSurface", "pfcGeometry", TypeKind.LIVE);
        put("ForeignSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("FreeAttachment", "pfcDetail", TypeKind.DATA);
        put("FunctionReturn", "pfcProToolkit", TypeKind.DATA);
        put("GeneralDatumPoint", "pfcDatumPointFeat", TypeKind.DATA);
        put("GeneralDatumPoints", "pfcDatumPointFeat", TypeKind.SEQUENCE);
        put("GeneralViewCreateInstructions", "pfcView2D", TypeKind.DATA);
        put("GeomCurve", "pfcGeometry", TypeKind.LIVE);
        put("GeomExportFlags", "pfcModel", TypeKind.DATA);
        put("GeomExportInstructions", "pfcModel", TypeKind.DATA);
        put("GeometryFlags", "pfcExport", TypeKind.DATA);
        put("GlobalEvaluator", "pfcInterference", TypeKind.LIVE);
        put("GlobalInterference", "pfcInterference", TypeKind.LIVE);
        put("GlobalInterferences", "pfcInterference", TypeKind.SEQUENCE);
        put("GraphicsMode", "pfcDisplay", TypeKind.ENUM);
        put("GroupPattern", "pfcFeature", TypeKind.LIVE);
        put("HorizontalJustification", "pfcDetail", TypeKind.ENUM);
        put("IGES3DExportInstructions", "pfcModel", TypeKind.DATA);
        put("IGES3DNewExportInstructions", "pfcExport", TypeKind.DATA);
        put("IGESFileExportInstructions", "pfcModel", TypeKind.DATA);
        put("IGESImport2DInstructions", "pfcModel", TypeKind.DATA);
        put("IGESSectionImportInstructions", "pfcModel", TypeKind.DATA);
        put("Import2DInstructions", "pfcModel", TypeKind.DATA);
        put("ImportAction", "pfcImport", TypeKind.ENUM);
        put("ImportFeatAttr", "pfcModel", TypeKind.DATA);
        put("ImportInstructions", "pfcModel", TypeKind.DATA);
        put("ImportType", "pfcModel", TypeKind.ENUM);
        put("ImportedLayer", "pfcImport", TypeKind.LIVE);
        put("InclusionFlags", "pfcExport", TypeKind.DATA);
        put("Inertia", "pfcSolid", TypeKind.SEQUENCE);
        put("IntegerOId", "pfcObject", TypeKind.DATA);
        put("InterferenceVolume", "pfcInterference", TypeKind.LIVE);
        put("IntfACIS", "pfcModel", TypeKind.DATA);
        put("IntfAI", "pfcModel", TypeKind.DATA);
        put("IntfCDRS", "pfcModel", TypeKind.DATA);
    }

    private static void register3() {
        put("IntfCatiaCGR", "pfcModel", TypeKind.DATA);
        put("IntfCatiaPart", "pfcModel", TypeKind.DATA);
        put("IntfCatiaProduct", "pfcModel", TypeKind.DATA);
        put("IntfDXF", "pfcModel", TypeKind.DATA);
        put("IntfDataSource", "pfcModel", TypeKind.DATA);
        put("IntfICEM", "pfcModel", TypeKind.DATA);
        put("IntfIges", "pfcModel", TypeKind.DATA);
        put("IntfJT", "pfcModel", TypeKind.DATA);
        put("IntfNeutral", "pfcModel", TypeKind.DATA);
        put("IntfNeutralFile", "pfcModel", TypeKind.DATA);
        put("IntfParaSolid", "pfcModel", TypeKind.DATA);
        put("IntfProductView", "pfcModel", TypeKind.DATA);
        put("IntfSTL", "pfcModel", TypeKind.DATA);
        put("IntfStep", "pfcModel", TypeKind.DATA);
        put("IntfType", "pfcModel", TypeKind.ENUM);
        put("IntfUG", "pfcModel", TypeKind.DATA);
        put("IntfVDA", "pfcModel", TypeKind.DATA);
        put("IntfVRML", "pfcModel", TypeKind.DATA);
        put("InventorExportInstructions", "pfcModel", TypeKind.DATA);
        put("JLinkApplication", "pfcJLink", TypeKind.LIVE);
        put("JLinkTaskListener", "pfcJLink", TypeKind.LISTENER);
        put("JLinkTaskListener_u", "pfcJLink", TypeKind.DATA);
        put("JPEGImageExportInstructions", "pfcWindow", TypeKind.DATA);
        put("JT3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("Layer", "pfcLayer", TypeKind.LIVE);
        put("LayerExportOptions", "pfcExport", TypeKind.DATA);
        put("LayerImportFilter", "pfcImport", TypeKind.DATA);
        put("LayerImportFilter_u", "pfcImport", TypeKind.DATA);
        put("Layers", "pfcLayer", TypeKind.SEQUENCE);
        put("Layout", "pfcLayout", TypeKind.LIVE);
        put("LengthUnitType", "pfcBase", TypeKind.ENUM);
        put("LengthUnits", "pfcBase", TypeKind.DATA);
        put("LinAOCTangentDimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("Line", "pfcGeometry", TypeKind.LIVE);
        put("LineDescriptor", "pfcGeometry", TypeKind.DATA);
        put("MFG", "pfcMFG", TypeKind.LIVE);
        put("MFGCLExportInstructions", "pfcModel", TypeKind.DATA);
        put("MFGFeatCLExportInstructions", "pfcModel", TypeKind.DATA);
        put("MFGOperCLExportInstructions", "pfcModel", TypeKind.DATA);
        put("Markup", "pfcMarkup", TypeKind.LIVE);
        put("MassProperty", "pfcSolid", TypeKind.DATA);
        put("MassUnitType", "pfcBase", TypeKind.ENUM);
        put("Material", "pfcPart", TypeKind.LIVE);
        put("MaterialExportInstructions", "pfcModel", TypeKind.DATA);
        put("MaterialOId", "pfcPart", TypeKind.DATA);
        put("MaterialProperty", "pfcPart", TypeKind.DATA);
        put("MaterialPropertyType", "pfcPart", TypeKind.ENUM);
        put("MaterialType", "pfcPart", TypeKind.ENUM);
        put("Materials", "pfcPart", TypeKind.SEQUENCE);
        put("Matrix3D", "pfcBase", TypeKind.SEQUENCE);
        put("MedusaExportInstructions", "pfcModel", TypeKind.DATA);
        put("MessageButton", "pfcUI", TypeKind.ENUM);
        put("MessageButtons", "pfcUI", TypeKind.SEQUENCE);
        put("MessageDialogOptions", "pfcUI", TypeKind.DATA);
        put("MessageDialogType", "pfcUI", TypeKind.ENUM);
        put("Model", "pfcModel", TypeKind.LIVE);
        put("Model2D", "pfcModel2D", TypeKind.LIVE);
        put("ModelActionListener", "pfcModel", TypeKind.LISTENER);
        put("ModelActionListener_u", "pfcModel", TypeKind.DATA);
        put("ModelCheckCustomCheckListener", "pfcModelCheck", TypeKind.LISTENER);
        put("ModelCheckCustomCheckListener_u", "pfcModelCheck", TypeKind.DATA);
        put("ModelCheckInstructions", "pfcModelCheck", TypeKind.DATA);
        put("ModelCheckMode", "pfcModelCheck", TypeKind.ENUM);
        put("ModelCheckResults", "pfcModelCheck", TypeKind.DATA);
        put("ModelDescriptor", "pfcModel", TypeKind.DATA);
        put("ModelDescriptors", "pfcModel", TypeKind.SEQUENCE);
        put("ModelEventActionListener", "pfcModel", TypeKind.LISTENER);
        put("ModelEventActionListener_u", "pfcModel", TypeKind.DATA);
        put("ModelInfoExportInstructions", "pfcModel", TypeKind.DATA);
        put("ModelItem", "pfcModelItem", TypeKind.LIVE);
        put("ModelItemOId", "pfcModelItem", TypeKind.DATA);
        put("ModelItemOwner", "pfcModelItem", TypeKind.LIVE);
        put("ModelItemType", "pfcModelItem", TypeKind.ENUM);
        put("ModelItemTypes", "pfcModelItem", TypeKind.SEQUENCE);
        put("ModelItems", "pfcModelItem", TypeKind.SEQUENCE);
        put("ModelOId", "pfcModel", TypeKind.DATA);
        put("ModelType", "pfcModel", TypeKind.ENUM);
        put("ModelUnits", "pfcBase", TypeKind.DATA);
        put("Models", "pfcModel", TypeKind.SEQUENCE);
        put("MouseButton", "pfcSession", TypeKind.ENUM);
        put("MouseStatus", "pfcSession", TypeKind.DATA);
        put("NEUTRALFileExportInstructions", "pfcExport", TypeKind.DATA);
        put("NURBSSurface", "pfcGeometry", TypeKind.LIVE);
        put("NURBSSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("NamedModelItem", "pfcModelItem", TypeKind.LIVE);
        put("NewModelImportType", "pfcImport", TypeKind.ENUM);
        put("NonRegisteredServer", "pfcServer", TypeKind.LIVE);
        put("Note", "pfcNote", TypeKind.LIVE);
        put("OId", "pfcObject", TypeKind.DATA);
        put("Object", "pfcObject", TypeKind.LIVE);
        put("OffsetAttachment", "pfcDetail", TypeKind.DATA);
        put("OffsetCurveDirection", "pfcCurveFeat", TypeKind.ENUM);
        put("OperationType", "pfcModel", TypeKind.ENUM);
        put("OrientationHint", "pfcDimension2D", TypeKind.ENUM);
        put("Outline2D", "pfcBase", TypeKind.SEQUENCE);
        put("Outline3D", "pfcBase", TypeKind.SEQUENCE);
        put("PDFAnnotMode", "pfcExport", TypeKind.ENUM);
        put("PDFColorDepth", "pfcExport", TypeKind.ENUM);
        put("PDFExportInstructions", "pfcExport", TypeKind.DATA);
        put("PDFExportMode", "pfcExport", TypeKind.ENUM);
        put("PDFFontStrokeMode", "pfcExport", TypeKind.ENUM);
        put("PDFHiddenLineMode", "pfcExport", TypeKind.ENUM);
        put("PDFLayerMode", "pfcExport", TypeKind.ENUM);
        put("PDFLinecap", "pfcExport", TypeKind.ENUM);
        put("PDFLinejoin", "pfcExport", TypeKind.ENUM);
        put("PDFOption", "pfcExport", TypeKind.DATA);
        put("PDFOptionType", "pfcExport", TypeKind.ENUM);
        put("PDFOptions", "pfcExport", TypeKind.SEQUENCE);
        put("PDFParameterMode", "pfcExport", TypeKind.ENUM);
        put("PDFPrintingMode", "pfcExport", TypeKind.ENUM);
        put("PDFRestrictOperationsMode", "pfcExport", TypeKind.ENUM);
        put("PDFSaveMode", "pfcExport", TypeKind.ENUM);
        put("PDFSelectedViewMode", "pfcExport", TypeKind.ENUM);
        put("PDFU3DLightingMode", "pfcExport", TypeKind.ENUM);
        put("PDFU3DRenderMode", "pfcExport", TypeKind.ENUM);
        put("ParaSolid3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("ParamMode", "pfcTable", TypeKind.ENUM);
        put("ParamOId", "pfcModelItem", TypeKind.DATA);
        put("ParamType", "pfcSession", TypeKind.ENUM);
        put("ParamValue", "pfcModelItem", TypeKind.SEQUENCE);
    }

    private static void register4() {
        put("ParamValueType", "pfcModelItem", TypeKind.ENUM);
        put("ParamValues", "pfcModelItem", TypeKind.SEQUENCE);
        put("Parameter", "pfcModelItem", TypeKind.LIVE);
        put("ParameterDriverType", "pfcModelItem", TypeKind.ENUM);
        put("ParameterEnumeration", "pfcModelItem", TypeKind.DATA);
        put("ParameterLimit", "pfcModelItem", TypeKind.DATA);
        put("ParameterLimitType", "pfcModelItem", TypeKind.ENUM);
        put("ParameterOwner", "pfcModelItem", TypeKind.LIVE);
        put("ParameterRange", "pfcModelItem", TypeKind.DATA);
        put("ParameterRestriction", "pfcModelItem", TypeKind.DATA);
        put("ParameterSelectionContext", "pfcModelItem", TypeKind.ENUM);
        put("ParameterSelectionContexts", "pfcModelItem", TypeKind.SEQUENCE);
        put("ParameterSelectionOptions", "pfcModelItem", TypeKind.DATA);
        put("Parameters", "pfcModelItem", TypeKind.SEQUENCE);
        put("ParametricAttachment", "pfcDetail", TypeKind.DATA);
        put("Parent", "pfcObject", TypeKind.LIVE);
        put("Part", "pfcPart", TypeKind.LIVE);
        put("Placement", "pfcBase", TypeKind.ENUM);
        put("Plane", "pfcGeometry", TypeKind.LIVE);
        put("PlaneDescriptor", "pfcGeometry", TypeKind.DATA);
        put("PlotInstructions", "pfcModel", TypeKind.DATA);
        put("PlotPageRange", "pfcModel", TypeKind.ENUM);
        put("PlotPaperSize", "pfcModel", TypeKind.ENUM);
        put("Point", "pfcGeometry", TypeKind.LIVE);
        put("Point2D", "pfcBase", TypeKind.SEQUENCE);
        put("Point2Ds", "pfcBase", TypeKind.SEQUENCE);
        put("Point3D", "pfcBase", TypeKind.SEQUENCE);
        put("Point3Ds", "pfcBase", TypeKind.SEQUENCE);
        put("PointDescriptor", "pfcGeometry", TypeKind.DATA);
        put("PointDimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("PointToAngleDimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("Polygon", "pfcGeometry", TypeKind.LIVE);
        put("PolygonDescriptor", "pfcGeometry", TypeKind.DATA);
        put("Popupmenu", "pfcUI", TypeKind.LIVE);
        put("PopupmenuListener", "pfcUI", TypeKind.LISTENER);
        put("PopupmenuListener_u", "pfcUI", TypeKind.DATA);
        put("PopupmenuOptions", "pfcUI", TypeKind.DATA);
        put("PrincipalAxes", "pfcSolid", TypeKind.SEQUENCE);
        put("PrintMdlOption", "pfcExport", TypeKind.DATA);
        put("PrintPlacementOption", "pfcExport", TypeKind.DATA);
        put("PrintPrinterOption", "pfcExport", TypeKind.DATA);
        put("PrintSaveMethod", "pfcExport", TypeKind.ENUM);
        put("PrintSheets", "pfcExport", TypeKind.ENUM);
        put("PrintSize", "pfcExport", TypeKind.DATA);
        put("PrinterInstructions", "pfcExport", TypeKind.DATA);
        put("PrinterPCFOptions", "pfcExport", TypeKind.DATA);
        put("ProductViewExportInstructions", "pfcExport", TypeKind.DATA);
        put("ProductViewExportOptions", "pfcExport", TypeKind.DATA);
        put("ProductViewFormat", "pfcExport", TypeKind.ENUM);
        put("ProgramExportInstructions", "pfcModel", TypeKind.DATA);
        put("ProgramImportInstructions", "pfcModel", TypeKind.DATA);
        put("ProjectionViewCreateInstructions", "pfcView2D", TypeKind.DATA);
        put("Quilt", "pfcGeometry", TypeKind.LIVE);
        put("RasterDepth", "pfcWindow", TypeKind.ENUM);
        put("RasterImageExportInstructions", "pfcWindow", TypeKind.DATA);
        put("RasterType", "pfcWindow", TypeKind.ENUM);
        put("RefDimension", "pfcDimension", TypeKind.LIVE);
        put("RegenInstructions", "pfcSolid", TypeKind.DATA);
        put("RelCriterion", "pfcSession", TypeKind.ENUM);
        put("RelationExportInstructions", "pfcModel", TypeKind.DATA);
        put("RelationFunctionArgument", "pfcRelations", TypeKind.DATA);
        put("RelationFunctionArguments", "pfcRelations", TypeKind.SEQUENCE);
        put("RelationFunctionListener", "pfcRelations", TypeKind.LISTENER);
        put("RelationFunctionListener_u", "pfcRelations", TypeKind.DATA);
        put("RelationFunctionOptions", "pfcRelations", TypeKind.DATA);
        put("RelationImportInstructions", "pfcModel", TypeKind.DATA);
        put("RelationOwner", "pfcModelItem", TypeKind.LIVE);
        put("RenderExportInstructions", "pfcModel", TypeKind.DATA);
        put("ReorderAfterOperation", "pfcFeature", TypeKind.LIVE);
        put("ReorderBeforeOperation", "pfcFeature", TypeKind.LIVE);
        put("Report", "pfcReport", TypeKind.LIVE);
        put("RestrictionType", "pfcModelItem", TypeKind.ENUM);
        put("ResumeOperation", "pfcFeature", TypeKind.LIVE);
        put("RetrieveExistingSimpRepInstructions", "pfcSimpRep", TypeKind.DATA);
        put("RetrieveModelOptions", "pfcSession", TypeKind.DATA);
        put("RevolvedSurface", "pfcGeometry", TypeKind.LIVE);
        put("RevolvedSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("RotationDegree", "pfcTable", TypeKind.ENUM);
        put("RoundFeat", "pfcRoundFeat", TypeKind.LIVE);
        put("RuledSurface", "pfcGeometry", TypeKind.LIVE);
        put("RuledSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("STEP2DExportInstructions", "pfcModel", TypeKind.DATA);
        put("STEP3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("STEPExportInstructions", "pfcModel", TypeKind.DATA);
        put("STEPImport2DInstructions", "pfcModel", TypeKind.DATA);
        put("STLASCIIExportInstructions", "pfcModel", TypeKind.DATA);
        put("STLBinaryExportInstructions", "pfcModel", TypeKind.DATA);
        put("SWAsm3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("SWPart3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("ScreenTransform", "pfcBase", TypeKind.DATA);
        put("Section2D", "pfcSection", TypeKind.LIVE);
        put("Selection", "pfcSelect", TypeKind.LIVE);
        put("SelectionBuffer", "pfcSelect", TypeKind.LIVE);
        put("SelectionEvaluator", "pfcInterference", TypeKind.LIVE);
        put("SelectionOptions", "pfcSelect", TypeKind.DATA);
        put("SelectionPair", "pfcSelect", TypeKind.DATA);
        put("Selections", "pfcSelect", TypeKind.SEQUENCE);
        put("Server", "pfcServer", TypeKind.LIVE);
        put("ServerAutoresolveOption", "pfcServer", TypeKind.ENUM);
        put("ServerDependency", "pfcServer", TypeKind.ENUM);
        put("ServerIncludeInstances", "pfcServer", TypeKind.ENUM);
        put("ServerLocation", "pfcServer", TypeKind.LIVE);
        put("ServerObjectStatus", "pfcServer", TypeKind.DATA);
        put("Servers", "pfcServer", TypeKind.SEQUENCE);
        put("Session", "pfcSession", TypeKind.LIVE);
        put("SessionActionListener", "pfcSession", TypeKind.LISTENER);
        put("SessionActionListener_u", "pfcSession", TypeKind.DATA);
        put("SheetData", "pfcSheet", TypeKind.DATA);
        put("SheetInfo", "pfcSheet", TypeKind.DATA);
        put("SheetOrientation", "pfcSheet", TypeKind.ENUM);
        put("SheetOwner", "pfcSheet", TypeKind.DATA);
        put("ShrinkwrapExportInstructions", "pfcSolid", TypeKind.DATA);
        put("ShrinkwrapFacetedFormat", "pfcShrinkwrap", TypeKind.ENUM);
        put("ShrinkwrapFacetedFormatInstructions", "pfcShrinkwrap", TypeKind.DATA);
        put("ShrinkwrapFacetedPartInstructions", "pfcShrinkwrap", TypeKind.DATA);
        put("ShrinkwrapMergedSolidInstructions", "pfcShrinkwrap", TypeKind.DATA);
        put("ShrinkwrapMethod", "pfcShrinkwrap", TypeKind.ENUM);
        put("ShrinkwrapModelExportInstructions", "pfcShrinkwrap", TypeKind.DATA);
        put("ShrinkwrapSTLInstructions", "pfcShrinkwrap", TypeKind.DATA);
        put("ShrinkwrapSurfaceSubsetInstructions", "pfcShrinkwrap", TypeKind.DATA);
    }

    private static void register5() {
        put("ShrinkwrapVRMLInstructions", "pfcShrinkwrap", TypeKind.DATA);
        put("SimpRep", "pfcSimpRep", TypeKind.LIVE);
        put("SimpRepAction", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepActionType", "pfcSimpRep", TypeKind.ENUM);
        put("SimpRepCompItemPath", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepExclude", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepFeatItemPath", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepGeom", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepGraphics", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepInclude", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepInstructions", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepItem", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepItemPath", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepItems", "pfcSimpRep", TypeKind.SEQUENCE);
        put("SimpRepNone", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepReverse", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepSubstitute", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepSymb", "pfcSimpRep", TypeKind.DATA);
        put("SimpRepType", "pfcSimpRep", TypeKind.ENUM);
        put("SimpReps", "pfcSimpRep", TypeKind.SEQUENCE);
        put("SliceExportData", "pfcModel", TypeKind.DATA);
        put("Solid", "pfcSolid", TypeKind.LIVE);
        put("SolidActionListener", "pfcSolid", TypeKind.LISTENER);
        put("SolidActionListener_u", "pfcSolid", TypeKind.DATA);
        put("SolidGeometryLayerItem", "pfcLayer", TypeKind.LIVE);
        put("SphericalSplineSurface", "pfcGeometry", TypeKind.LIVE);
        put("SphericalSplineSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("Spline", "pfcGeometry", TypeKind.LIVE);
        put("SplineDescriptor", "pfcGeometry", TypeKind.DATA);
        put("SplinePoint", "pfcGeometry", TypeKind.DATA);
        put("SplinePointDimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("SplinePoints", "pfcGeometry", TypeKind.SEQUENCE);
        put("SplineSurface", "pfcGeometry", TypeKind.LIVE);
        put("SplineSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("SplineSurfacePoint", "pfcGeometry", TypeKind.DATA);
        put("SplineSurfacePoints", "pfcGeometry", TypeKind.SEQUENCE);
        put("SplitCurveSide", "pfcCurveFeat", TypeKind.ENUM);
        put("SpoolImportInstructions", "pfcModel", TypeKind.DATA);
        put("StdColor", "pfcBase", TypeKind.ENUM);
        put("StdColors", "pfcBase", TypeKind.SEQUENCE);
        put("StdLineStyle", "pfcBase", TypeKind.ENUM);
        put("StringOId", "pfcObject", TypeKind.DATA);
        put("SubstAsmRep", "pfcSimpRep", TypeKind.DATA);
        put("SubstEnvelope", "pfcSimpRep", TypeKind.DATA);
        put("SubstInterchg", "pfcSimpRep", TypeKind.DATA);
        put("SubstPrtRep", "pfcSimpRep", TypeKind.DATA);
        put("SubstType", "pfcSimpRep", TypeKind.ENUM);
        put("Substitution", "pfcSimpRep", TypeKind.DATA);
        put("SuppressOperation", "pfcFeature", TypeKind.LIVE);
        put("SurfXYZData", "pfcGeometry", TypeKind.DATA);
        put("Surface", "pfcGeometry", TypeKind.LIVE);
        put("SurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("SurfaceDescriptors", "pfcGeometry", TypeKind.SEQUENCE);
        put("SurfaceExtents", "pfcGeometry", TypeKind.DATA);
        put("SurfaceOrientation", "pfcGeometry", TypeKind.ENUM);
        put("SurfaceType", "pfcGeometry", TypeKind.ENUM);
        put("Surfaces", "pfcGeometry", TypeKind.SEQUENCE);
        put("SymbolDefAttachment", "pfcDetail", TypeKind.DATA);
        put("SymbolDefAttachmentType", "pfcDetail", TypeKind.ENUM);
        put("SymbolDefAttachments", "pfcDetail", TypeKind.SEQUENCE);
        put("SymbolDefHeight", "pfcDetail", TypeKind.ENUM);
        put("SymbolGroupFilter", "pfcDetail", TypeKind.ENUM);
        put("TIFFImageExportInstructions", "pfcWindow", TypeKind.DATA);
        put("Table", "pfcTable", TypeKind.LIVE);
        put("TableCell", "pfcTable", TypeKind.DATA);
        put("TableCreateInstructions", "pfcTable", TypeKind.DATA);
        put("TableInfo", "pfcTable", TypeKind.DATA);
        put("TableOwner", "pfcTable", TypeKind.DATA);
        put("TableRetrieveInstructions", "pfcTable", TypeKind.DATA);
        put("TableSizeType", "pfcTable", TypeKind.ENUM);
        put("Tables", "pfcTable", TypeKind.SEQUENCE);
        put("TabulatedCylinder", "pfcGeometry", TypeKind.LIVE);
        put("TabulatedCylinderDescriptor", "pfcGeometry", TypeKind.DATA);
        put("TangentEdgeDisplayStyle", "pfcBase", TypeKind.ENUM);
        put("TangentIndexDimensionSense", "pfcDimension2D", TypeKind.DATA);
        put("TerminationStatus", "pfcAsyncConnection", TypeKind.ENUM);
        put("Text", "pfcGeometry", TypeKind.LIVE);
        put("TextDescriptor", "pfcGeometry", TypeKind.DATA);
        put("TextReference", "pfcDetail", TypeKind.DATA);
        put("TextStyle", "pfcBase", TypeKind.DATA);
        put("ToleranceTableType", "pfcDimension", TypeKind.ENUM);
        put("Torus", "pfcGeometry", TypeKind.LIVE);
        put("TorusDescriptor", "pfcGeometry", TypeKind.DATA);
        put("Transform3D", "pfcBase", TypeKind.DATA);
        put("Transform3Ds", "pfcBase", TypeKind.SEQUENCE);
        put("TransformedSurface", "pfcGeometry", TypeKind.LIVE);
        put("TransformedSurfaceDescriptor", "pfcGeometry", TypeKind.DATA);
        put("TriangulationInstructions", "pfcExport", TypeKind.DATA);
        put("UDFAssemblyIntersection", "pfcUDFCreate", TypeKind.DATA);
        put("UDFAssemblyIntersections", "pfcUDFCreate", TypeKind.SEQUENCE);
        put("UDFCustomCreateInstructions", "pfcUDFCreate", TypeKind.DATA);
        put("UDFDependencyType", "pfcUDFCreate", TypeKind.ENUM);
        put("UDFDimension", "pfcUDFGroup", TypeKind.LIVE);
        put("UDFDimensionDisplayType", "pfcUDFCreate", TypeKind.ENUM);
        put("UDFDimensions", "pfcUDFGroup", TypeKind.SEQUENCE);
        put("UDFExternalReference", "pfcUDFCreate", TypeKind.DATA);
        put("UDFExternalReferences", "pfcUDFCreate", TypeKind.SEQUENCE);
        put("UDFGroupCreateInstructions", "pfcUDFGroup", TypeKind.DATA);
        put("UDFOrientation", "pfcUDFCreate", TypeKind.ENUM);
        put("UDFOrientations", "pfcUDFCreate", TypeKind.SEQUENCE);
        put("UDFPromptCreateInstructions", "pfcUDFGroup", TypeKind.DATA);
        put("UDFReference", "pfcUDFCreate", TypeKind.DATA);
        put("UDFReferences", "pfcUDFCreate", TypeKind.SEQUENCE);
        put("UDFScaleType", "pfcUDFCreate", TypeKind.ENUM);
        put("UDFVariantDimension", "pfcUDFCreate", TypeKind.DATA);
        put("UDFVariantPatternParam", "pfcUDFCreate", TypeKind.DATA);
        put("UDFVariantValue", "pfcUDFCreate", TypeKind.DATA);
        put("UDFVariantValueType", "pfcUDFCreate", TypeKind.ENUM);
        put("UDFVariantValues", "pfcUDFCreate", TypeKind.SEQUENCE);
        put("UG3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("UICommand", "pfcCommand", TypeKind.LIVE);
        put("UICommandAccessListener", "pfcCommand", TypeKind.LISTENER);
        put("UICommandAccessListener_u", "pfcCommand", TypeKind.DATA);
        put("UICommandActionListener", "pfcCommand", TypeKind.LISTENER);
        put("UICommandActionListener_u", "pfcCommand", TypeKind.DATA);
        put("UICommandBracketListener", "pfcCommand", TypeKind.LISTENER);
        put("UICommandBracketListener_u", "pfcCommand", TypeKind.DATA);
        put("UVOutline", "pfcBase", TypeKind.SEQUENCE);
        put("UVParams", "pfcBase", TypeKind.SEQUENCE);
        put("UVParamsSequence", "pfcBase", TypeKind.SEQUENCE);
    }

    private static void register6() {
        put("UVVector", "pfcBase", TypeKind.SEQUENCE);
        put("Unit", "pfcUnits", TypeKind.LIVE);
        put("UnitConversionFactor", "pfcUnits", TypeKind.DATA);
        put("UnitConversionOptions", "pfcUnits", TypeKind.DATA);
        put("UnitDimensionConversion", "pfcUnits", TypeKind.ENUM);
        put("UnitSystem", "pfcUnits", TypeKind.LIVE);
        put("UnitSystemType", "pfcUnits", TypeKind.ENUM);
        put("UnitSystems", "pfcUnits", TypeKind.SEQUENCE);
        put("UnitType", "pfcBase", TypeKind.ENUM);
        put("Units", "pfcUnits", TypeKind.SEQUENCE);
        put("UnsupportedAttachment", "pfcDetail", TypeKind.DATA);
        put("UploadBaseOptions", "pfcServer", TypeKind.DATA);
        put("UploadOptions", "pfcServer", TypeKind.DATA);
        put("VDA3DExportInstructions", "pfcExport", TypeKind.DATA);
        put("VDAExportInstructions", "pfcModel", TypeKind.DATA);
        put("VRMLDirectExportInstructions", "pfcModel", TypeKind.DATA);
        put("VRMLExportInstructions", "pfcModel", TypeKind.DATA);
        put("VRMLModelExportInstructions", "pfcModel", TypeKind.DATA);
        put("Vector2D", "pfcBase", TypeKind.SEQUENCE);
        put("Vector3D", "pfcBase", TypeKind.SEQUENCE);
        put("Vector3Ds", "pfcBase", TypeKind.SEQUENCE);
        put("VerticalJustification", "pfcDetail", TypeKind.ENUM);
        put("View", "pfcView", TypeKind.LIVE);
        put("View2D", "pfcView2D", TypeKind.LIVE);
        put("View2DCreateInstructions", "pfcView2D", TypeKind.DATA);
        put("View2DType", "pfcView2D", TypeKind.ENUM);
        put("View2Ds", "pfcView2D", TypeKind.SEQUENCE);
        put("ViewDisplay", "pfcView2D", TypeKind.DATA);
        put("ViewOId", "pfcView", TypeKind.DATA);
        put("ViewOwner", "pfcView", TypeKind.LIVE);
        put("Views", "pfcView", TypeKind.SEQUENCE);
        put("WSExportOptions", "pfcSession", TypeKind.DATA);
        put("WSImportExportMessage", "pfcSession", TypeKind.DATA);
        put("WSImportExportMessageType", "pfcSession", TypeKind.ENUM);
        put("WSImportExportMessages", "pfcSession", TypeKind.SEQUENCE);
        put("Window", "pfcWindow", TypeKind.LIVE);
        put("WindowOId", "pfcWindow", TypeKind.DATA);
        put("Windows", "pfcWindow", TypeKind.SEQUENCE);
        put("WireListImportInstructions", "pfcModel", TypeKind.DATA);
        put("WorkspaceDefinition", "pfcServer", TypeKind.DATA);
        put("WorkspaceDefinitions", "pfcServer", TypeKind.SEQUENCE);
        put("XBadArgument", "pfcExceptions", TypeKind.UNKNOWN);
        put("XBadExternalData", "pfcExceptions", TypeKind.UNKNOWN);
        put("XBadGetArgValue", "pfcExceptions", TypeKind.UNKNOWN);
        put("XBadGetExternalData", "pfcExceptions", TypeKind.UNKNOWN);
        put("XBadGetParamValue", "pfcExceptions", TypeKind.UNKNOWN);
        put("XBadOutlineExcludeType", "pfcExceptions", TypeKind.UNKNOWN);
        put("XCancelProEAction", "pfcExceptions", TypeKind.UNKNOWN);
        put("XCannotAccess", "pfcExceptions", TypeKind.UNKNOWN);
        put("XEmptyString", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataBadDataArgs", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataBadKeyByFlag", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataClassOrSlotExists", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataEmptySlot", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataInvalidObjType", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataInvalidObject", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataInvalidSlotName", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataNamesTooLong", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataSlotNotFound", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataStreamTooLarge", "pfcExceptions", TypeKind.UNKNOWN);
        put("XExternalDataTKError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XInAMethod", "pfcExceptions", TypeKind.UNKNOWN);
        put("XInvalidEnumValue", "pfcExceptions", TypeKind.UNKNOWN);
        put("XInvalidFileName", "pfcExceptions", TypeKind.UNKNOWN);
        put("XInvalidFileType", "pfcExceptions", TypeKind.UNKNOWN);
        put("XInvalidModelItem", "pfcExceptions", TypeKind.UNKNOWN);
        put("XInvalidSelection", "pfcExceptions", TypeKind.UNKNOWN);
        put("XJLinkApplicationException", "pfcExceptions", TypeKind.UNKNOWN);
        put("XJLinkApplicationInactive", "pfcExceptions", TypeKind.UNKNOWN);
        put("XJLinkTaskExists", "pfcExceptions", TypeKind.UNKNOWN);
        put("XJLinkTaskNotFound", "pfcExceptions", TypeKind.UNKNOWN);
        put("XMethodForbidden", "pfcExceptions", TypeKind.UNKNOWN);
        put("XModelNotInSession", "pfcExceptions", TypeKind.UNKNOWN);
        put("XNegativeNumber", "pfcExceptions", TypeKind.UNKNOWN);
        put("XNumberTooLarge", "pfcExceptions", TypeKind.UNKNOWN);
        put("XPFC", "pfcExceptions", TypeKind.UNKNOWN);
        put("XProdevError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XProeWasNotConnected", "pfcAsyncConnection", TypeKind.DATA);
        put("XSecCutType", "pfcXSection", TypeKind.ENUM);
        put("XSecCutobjType", "pfcXSection", TypeKind.ENUM);
        put("XSecType", "pfcXSection", TypeKind.DATA);
        put("XSection", "pfcXSection", TypeKind.LIVE);
        put("XSections", "pfcXSection", TypeKind.SEQUENCE);
        put("XSequenceTooLong", "pfcExceptions", TypeKind.UNKNOWN);
        put("XStringTooLong", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAbort", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAmbiguous", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppBadDataPath", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppBadEncoding", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppCommunicationFailure", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppCreoBarred", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppExcessCallbacks", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppInitializationFailed", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppNewVersion", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppNoLicense", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppStartupFailed", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppTooOld", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAppVersionMismatch", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitAuthenticationFailure", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBadContext", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBadDimAttach", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBadInputs", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBadSrfCrv", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBsplMultiInnerKnots", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBsplNonStdEndKnots", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBsplUnsuitableDegree", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitBusy", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCantAccess", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCantModify", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCantOpen", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCantWrite", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCheckLastError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCheckOmitted", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCheckoutConflict", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCommError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitContinue", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCreateViewBadExplode", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCreateViewBadModel", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCreateViewBadParent", "pfcExceptions", TypeKind.UNKNOWN);
    }

    private static void register7() {
        put("XToolkitCreateViewBadSheet", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitCreateViewBadType", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitDeadLock", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitDllInactive", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitDllInitializeFailed", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitDrawingCreateErrors", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitEmpty", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitFound", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitGeneralError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInUse", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitIncomplete", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidDir", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidFile", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidItem", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidMatrix", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidName", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidPtr", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidReference", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitInvalidType", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitLineTooLong", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitMaxLimitReached", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitMsgFmtError", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitMsgNoTrans", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitMsgNotFound", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitMsgTooLong", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitMsgUserQuit", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNeedsUnlock", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNoChange", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNoCoordSystem", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNoLicense", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNoPermission", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNotDisplayed", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNotExist", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNotFound", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNotImplemented", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitNotValid", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitObsoleteFunc", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitOutOfMemory", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitOutOfRange", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitOutdated", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitPickAbove", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitRegenerateAgain", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitSuppressedParents", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitUnattachedFeats", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitUnavailableSection", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitUnrecognizedErrorCode", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitUnsupported", "pfcExceptions", TypeKind.UNKNOWN);
        put("XToolkitUserAbort", "pfcExceptions", TypeKind.UNKNOWN);
        put("XUnimplemented", "pfcExceptions", TypeKind.UNKNOWN);
        put("XUnknownModelExtension", "pfcExceptions", TypeKind.UNKNOWN);
        put("XUnusedValue", "pfcExceptions", TypeKind.UNKNOWN);
        put("pfcArgument", "pfcArgument", TypeKind.DATA);
        put("pfcAssembly", "pfcAssembly", TypeKind.DATA);
        put("pfcAsyncConnection", "pfcAsyncConnection", TypeKind.DATA);
        put("pfcBase", "pfcBase", TypeKind.DATA);
        put("pfcComponentFeat", "pfcComponentFeat", TypeKind.DATA);
        put("pfcCoordSysFeat", "pfcCoordSysFeat", TypeKind.DATA);
        put("pfcDatumAxisFeat", "pfcDatumAxisFeat", TypeKind.DATA);
        put("pfcDatumPlaneFeat", "pfcDatumPlaneFeat", TypeKind.DATA);
        put("pfcDatumPointFeat", "pfcDatumPointFeat", TypeKind.DATA);
        put("pfcDetail", "pfcDetail", TypeKind.DATA);
        put("pfcDimension", "pfcDimension", TypeKind.DATA);
        put("pfcDimension2D", "pfcDimension2D", TypeKind.DATA);
        put("pfcExport", "pfcExport", TypeKind.DATA);
        put("pfcExternal", "pfcExternal", TypeKind.DATA);
        put("pfcGeometry", "pfcGeometry", TypeKind.DATA);
        put("pfcInterference", "pfcInterference", TypeKind.DATA);
        put("pfcModel", "pfcModel", TypeKind.DATA);
        put("pfcModelCheck", "pfcModelCheck", TypeKind.DATA);
        put("pfcModelItem", "pfcModelItem", TypeKind.DATA);
        put("pfcPart", "pfcPart", TypeKind.DATA);
        put("pfcRelations", "pfcRelations", TypeKind.DATA);
        put("pfcSelect", "pfcSelect", TypeKind.DATA);
        put("pfcServer", "pfcServer", TypeKind.DATA);
        put("pfcSession", "pfcSession", TypeKind.DATA);
        put("pfcShrinkwrap", "pfcShrinkwrap", TypeKind.DATA);
        put("pfcSimpRep", "pfcSimpRep", TypeKind.DATA);
        put("pfcSolid", "pfcSolid", TypeKind.DATA);
        put("pfcTable", "pfcTable", TypeKind.DATA);
        put("pfcUDFCreate", "pfcUDFCreate", TypeKind.DATA);
        put("pfcUDFGroup", "pfcUDFGroup", TypeKind.DATA);
        put("pfcUI", "pfcUI", TypeKind.DATA);
        put("pfcUnits", "pfcUnits", TypeKind.DATA);
        put("pfcView", "pfcView", TypeKind.DATA);
        put("pfcView2D", "pfcView2D", TypeKind.DATA);
        put("pfcWindow", "pfcWindow", TypeKind.DATA);
        put("stringseq", "cipjava", TypeKind.SEQUENCE);
        put("intseq", "cipjava", TypeKind.SEQUENCE);
        put("realseq", "cipjava", TypeKind.SEQUENCE);
    }

    private static void put(String name, String pkg, TypeKind kind) {
        KINDS.put(name, kind);
        PACKAGES.put(name, pkg);
    }

    /** How {@code typeName} travels over JSON. Unregistered names report {@code UNKNOWN}. */
    public static TypeKind kind(String typeName) {
        if (typeName == null) {
            return TypeKind.UNKNOWN;
        }
        switch (typeName) {
            case "void":
                return TypeKind.VOID;
            case "String":
            case "int":
            case "Integer":
            case "double":
            case "Double":
            case "boolean":
            case "Boolean":
                return TypeKind.PRIMITIVE;
            default:
                break;
        }
        TypeKind k = KINDS.get(typeName);
        return k == null ? TypeKind.UNKNOWN : k;
    }

    /** The J-Link package a type belongs to, e.g. {@code pfcSolid}. */
    public static String packageOf(String typeName) {
        return PACKAGES.get(typeName);
    }

    /** The fully-qualified Java name, or {@code null} for primitives and java.lang types. */
    public static String javaFqn(String typeName) {
        String pkg = PACKAGES.get(typeName);
        if (pkg == null) {
            return null;
        }
        if (pkg.equals("cipjava")) {
            return "com.ptc.cipjava." + typeName;
        }
        return "com.ptc.pfc." + pkg + "." + typeName;
    }

    /** Resolves and caches the {@link Class} for a J-Link type name. */
    public static Class<?> classFor(String typeName) {
        Class<?> cached = CLASSES.get(typeName);
        if (cached != null) {
            return cached;
        }
        String fqn = javaFqn(typeName);
        if (fqn == null) {
            throw new CommandException(
                    "Unknown J-Link type '" + typeName + "'", "internal");
        }
        try {
            Class<?> c = Class.forName(fqn);
            CLASSES.put(typeName, c);
            return c;
        } catch (ClassNotFoundException e) {
            throw new CommandException(
                    "pfcasync.jar does not contain " + fqn + ". Check that the jar on the classpath matches the Creo version in paths.yaml.",
                    "internal", e);
        }
    }

    /** The {@code pfcXxx} class that holds a package's static factories. */
    public static Class<?> packageClassFor(String typeName) {
        String pkg = PACKAGES.get(typeName);
        if (pkg == null || pkg.equals("cipjava")) {
            return null;
        }
        try {
            return Class.forName("com.ptc.pfc." + pkg + "." + pkg);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /** Number of registered types; used by the build-time consistency check. */
    public static int size() {
        return KINDS.size();
    }
}
