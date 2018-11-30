/*     */ package com.ibm.ier.plugin;
/*     */ 
/*     */ import com.ibm.ecm.configuration.ApplicationConfig;
/*     */ import com.ibm.ecm.configuration.DesktopConfig;
/*     */ import com.ibm.ecm.configuration.MenuConfig;
/*     */ import com.ibm.ecm.extension.Plugin;
/*     */ import com.ibm.ecm.extension.PluginAction;
/*     */ import com.ibm.ecm.extension.PluginFeature;
/*     */ import com.ibm.ecm.extension.PluginLayout;
/*     */ import com.ibm.ecm.extension.PluginLogger;
/*     */ import com.ibm.ecm.extension.PluginMenu;
/*     */ import com.ibm.ecm.extension.PluginMenuType;
/*     */ import com.ibm.ecm.extension.PluginRequestFilter;
/*     */ import com.ibm.ecm.extension.PluginResponseFilter;
/*     */ import com.ibm.ecm.extension.PluginService;
/*     */ import com.ibm.ecm.extension.PluginServiceCallbacks;
/*     */ import com.ibm.ier.plugin.actions.AboutAction;
/*     */ import com.ibm.ier.plugin.actions.ActivateHoldSweepProcessingAction;
/*     */ import com.ibm.ier.plugin.actions.AddFilePlanAction;
/*     */ import com.ibm.ier.plugin.actions.AddHoldAction;
/*     */ import com.ibm.ier.plugin.actions.AddLocationAction;
/*     */ import com.ibm.ier.plugin.actions.AddNamingPatternAction;
/*     */ import com.ibm.ier.plugin.actions.AddRecordCategoryAction;
/*     */ import com.ibm.ier.plugin.actions.AddRecordFolderAction;
/*     */ import com.ibm.ier.plugin.actions.AddRecordVolumeAction;
/*     */ import com.ibm.ier.plugin.actions.AddReportDefinitionAction;
/*     */ import com.ibm.ier.plugin.actions.AddToFavoritesAction;
/*     */ import com.ibm.ier.plugin.actions.CancelRemoveHoldRequestAction;
/*     */ import com.ibm.ier.plugin.actions.CloseAction;
/*     */ import com.ibm.ier.plugin.actions.ConvertToDDContainerAction;
/*     */ import com.ibm.ier.plugin.actions.CopyAction;
/*     */ import com.ibm.ier.plugin.actions.DeclareAction;
/*     */ import com.ibm.ier.plugin.actions.DeleteAction;
/*     */ import com.ibm.ier.plugin.actions.DeleteTaskAction;
/*     */ import com.ibm.ier.plugin.actions.DisableTaskAction;
/*     */ import com.ibm.ier.plugin.actions.DownloadTaskAction;
/*     */ import com.ibm.ier.plugin.actions.EditFilePlanAction;
/*     */ import com.ibm.ier.plugin.actions.EnableTaskAction;
/*     */ import com.ibm.ier.plugin.actions.FileAction;
/*     */ import com.ibm.ier.plugin.actions.InitiateDispositionAction;
/*     */ import com.ibm.ier.plugin.actions.InitiateRemoveHoldRequestAction;
/*     */ import com.ibm.ier.plugin.actions.ModifyAndRunTaskAction;
/*     */ import com.ibm.ier.plugin.actions.ModifyTaskAction;
/*     */ import com.ibm.ier.plugin.actions.MoveAction;
/*     */ import com.ibm.ier.plugin.actions.OpenRecordContentAction;
/*     */ import com.ibm.ier.plugin.actions.OpenTaskAction;
/*     */ import com.ibm.ier.plugin.actions.PlaceOnHoldAction;
/*     */ import com.ibm.ier.plugin.actions.PropertiesAction;
/*     */ import com.ibm.ier.plugin.actions.RecordPropertiesAction;
/*     */ import com.ibm.ier.plugin.actions.RefreshAction;
/*     */ import com.ibm.ier.plugin.actions.RefreshTaskAction;
/*     */ import com.ibm.ier.plugin.actions.RelocateAction;
/*     */ import com.ibm.ier.plugin.actions.RemoveHoldAction;
/*     */ import com.ibm.ier.plugin.actions.RemoveHoldOnViewEntitiesAction;
/*     */ import com.ibm.ier.plugin.actions.ReopenAction;
/*     */ import com.ibm.ier.plugin.actions.RunReportAction;
/*     */ import com.ibm.ier.plugin.actions.ScheduleDDContainerConversionAction;
/*     */ import com.ibm.ier.plugin.actions.ScheduleDDReportTaskAction;
/*     */ import com.ibm.ier.plugin.actions.ScheduleDispositionSweepAction;
/*     */ import com.ibm.ier.plugin.actions.ScheduleReportAction;
/*     */ import com.ibm.ier.plugin.actions.UndeclareAction;
/*     */ import com.ibm.ier.plugin.actions.ViewDocumentInfoAction;
/*     */ import com.ibm.ier.plugin.actions.ViewEntitiesOnHoldAction;
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.features.IERReportsPluginFeature;
/*     */ import com.ibm.ier.plugin.menus.IERActionsConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERBannerToolsContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERBrowseFilePlanToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERCompletedRecurringTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERCompletedTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERCompletedTaskInstanceContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERCompletedTaskToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERConfigureFlyoutToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERCustomObjectContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERDefensibleDisposalTaskToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERDisabledRecurringTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERDispositionSchedulesConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERElectronicRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERElectronicRecordFolderContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFailedRecurringTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFailedTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFailedTaskInstanceContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFavoriteElectronicRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFavoriteElectronicRecordFolderContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFavoritePhysicalRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFavoritePhysicalRecordFolderContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFavoriteRecordCategoryContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFavoriteVolumeContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFilePlanConfigureContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFilePlanContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERFilePlansConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERHoldConditionContainerContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERHoldConditionRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERHoldTaskToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERHoldsConfigureContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERHoldsConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERInProgressTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERInProgressTaskInstanceContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERLocationsConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERMultiStatusTaskToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERMultipleContainerAndRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERMultipleContainerContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERMultipleCustomObjectsContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERMultipleRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERMultipleTasksContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERNamingPatternsConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERPhysicalBoxHybridRecordFolderContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERPhysicalRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERPlaceOnHoldContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERPropertiesOnlyCustomObjectContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERRecordCategoryContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERRemoveHoldContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERReportDefinitionsConfigureContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERReportDefinitionsConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERReportTaskToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERReportsCompletedTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERReportsCompletedTaskInstanceContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERScheduledRecurringTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERScheduledTaskContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERScheduledTaskInstanceContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERSearchElectronicRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERSearchPhysicalRecordContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERTriggersConfigureToolbarMenu;
/*     */ import com.ibm.ier.plugin.menus.IERViewEntitiesOnHoldContextMenu;
/*     */ import com.ibm.ier.plugin.menus.IERVolumeContextMenu;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERActionsConfigureToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERBrowseFilePlanToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERCompletedRecurringTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERCompletedTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERCompletedTaskInstanceContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERConfigureFlyoutToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERCustomObjectContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERDefensibleDispositionTaskToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERDisabledRecurringTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERDispositionSchedulesConfigureToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERElectronicRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERElectronicRecordFolderContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFailedRecurringTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFailedTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFailedTaskInstanceContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFavoriteElectronicRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFavoriteElectronicRecordFolderContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFavoritePhysicalRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFavoritePhysicalRecordFolderContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFavoriteRecordCategoryContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFavoriteVolumeContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFilePlanConfigureContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERFilePlanContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERHoldConditionContainerContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERHoldConditionRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERHoldTaskToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERHoldsConfigureContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERHoldsConfigureToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERInProgressTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERInProgressTaskInstanceContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERLocationsConfigureToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERMultiStatusTaskToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERMultipleContainerAndRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERMultipleContainerContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERMultipleCustomObjectsContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERMultipleRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERMultipleTasksContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERNamingPatternsConfigureToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERPhysicalBoxHybridRecordFolderContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERPhysicalRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERPlaceOnHoldContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERPropertiesOnlyCustomObjectContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERRecordCategoryContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERRemoveHoldContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERReportDefinitionsConfigureContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERReportDefinitionsConfigureToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERReportTaskToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERReportsCompletedTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERReportsCompletedTaskInstanceContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERScheduledRecurringTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERScheduledTaskContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERScheduledTaskInstanceContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERSearchElectronicRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERSearchPhysicalRecordContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERTriggersConfigureToolbarMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERViewEntitiesOnHoldContextMenuType;
/*     */ import com.ibm.ier.plugin.menus.menutypes.IERVolumeContextMenuType;
/*     */ import com.ibm.ier.plugin.nls.MessageCode;
/*     */ import com.ibm.ier.plugin.nls.MessageResources;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CloseRecordContainerService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.ConditionSearchService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CopyRecordService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CopyService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateActionService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateFilePlanService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateIERDesktopService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateRecordCategoryService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateRecordFolderService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateRecordVolumeService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateSimpleDispositionScheduleService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.CreateTriggerService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.DeclareRecordService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.DeleteReportResultsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.DeleteService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.DynamicHoldRequestService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.EditFilePlanService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.EditRecordContainerService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.EditRecordService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.FileRecordService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetAllRecordPropertiesService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetAndSavePluginConfiguration;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetAssociatedContentRepositoriesService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetChoiceListService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetClassPropertyDescriptionsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetDispositionSchedulesService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetDocumentInfoService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetEntitiesOnHoldService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetEntityNameFromNamingPatternService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetFilePlansService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetHistoryService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetHoldsForAnEntityService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetNamingPatternLevelsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetNextVolumeNameService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetObjectConditionsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetObjectStoreSecurityService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetObjectsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetRecordEntryTemplatesService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetRecordFromDeclaredDocumentService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetReportDefinitionsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetRepositoryAttributesService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetRepositoryPermissionsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetSearchTemplatesService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.GetWorkflowVersionsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.InitiateDispositionService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.LinkRecordService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.MoveRecordService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.ObjectStoreSecurityService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.OpenRecordEntryTemplateService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.PlaceOnHoldService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.QuickSearchService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.RefreshService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.RelocateRecordContainerService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.RemoveHoldService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.ReopenRecordContainerService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.RetrieveLinksOnEntityService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.RunReportService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.SaveHoldService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.SaveLocationService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.SaveNamingPatternService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.SaveReportDefinitionService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.SaveSystemConfigurationsService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.ScheduleReportService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.SearchService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.UndeclareService;
/*     */ import com.ibm.ier.plugin.services.pluginServices.ValidateNamingPatternService;
/*     */ import com.ibm.ier.plugin.services.requestFilters.ConfigurationFilter;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8AddSearchTemplateOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8EditAttributesOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8FindClassesPreOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8OpenFolderPreOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8OpenItemPreOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8RetrieveItemsActionPreOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8RetrieveNextPagePreOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.P8UpdateSearchTemplateOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.RescheduleAsyncTaskOverride;
/*     */ import com.ibm.ier.plugin.services.requestFilters.ScheduleAsyncTaskOverride;
/*     */ import com.ibm.ier.plugin.services.responseFilters.GetAsyncTasksOverride;
/*     */ import com.ibm.ier.plugin.services.responseFilters.GetDesktopActionOverride;
/*     */ import com.ibm.ier.plugin.services.responseFilters.P8LogonActionOverride;
/*     */ import com.ibm.ier.plugin.services.responseFilters.P8OpenContentClassOverride;
/*     */ import com.ibm.ier.plugin.services.responseFilters.P8OpenFolderOverride;
/*     */ import com.ibm.ier.plugin.services.responseFilters.P8OpenItemOverride;
/*     */ import com.ibm.ier.plugin.services.responseFilters.P8OpenSearchTemplateOverride;
/*     */ import com.ibm.ier.plugin.tasks.util.AESEncryptionUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IERApplicationPlugin
/*     */   extends Plugin
/*     */ {
/*     */   private static final String DEFAULT_CONTENTLIST_TOOLBAR = "DefaultContentListToolbar";
/*     */   private static final String DEFAULT_DOC_CONTEXTMENU = "DefaultItemContextMenu";
/*     */   private static final String CONTENTLIST_TOOLBAR_MENUTYPE = "ContentListToolbar";
/*     */   private static final String DOCUMENT_CONTEXTMENU_MENUTYPE = "ItemContextMenu";
/*     */   private static final String IER_DECLARE_CONTENTLIST_TOOLBAR = "IERDeclareContentListToolbar";
/*     */   private static final String IER_DECLARE_DOC_CONTEXTMENU = "IERDeclareItemContextMenu";
/*     */   public static final String IER_APPLICATION_PLUGIN = "IERApplicationPlugin";
/*     */   public static final String IER_APPLICATION_NAME = "IBM Enterprise Records";
/*     */   public static final String IER_DESKTOP_NAME = "IERDesktop";
/*     */   public static final String IER_DESKTOP_ID = "IER";
/*     */   private static final String MENUS = "menus";
/*     */   public static final String VERSION = "5.1.2";
/*     */   public static final int VERSION_NUMBER = 1;
/* 332 */   private static PluginLogger pluginLogger = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginAction[] getActions()
/*     */   {
/* 340 */     return new PluginAction[] { new AboutAction(), new AddFilePlanAction(), new AddLocationAction(), new AddRecordCategoryAction(), new AddRecordFolderAction(), new AddRecordVolumeAction(), new AddToFavoritesAction(), new AddHoldAction(), new AddNamingPatternAction(), new AddReportDefinitionAction(), new CloseAction(), new CopyAction(), new DeclareAction(), new DeleteAction(), new DeleteTaskAction(), new DownloadTaskAction(), new EditFilePlanAction(), new FileAction(), new MoveAction(), new OpenRecordContentAction(), new OpenTaskAction(), new PlaceOnHoldAction(), new DisableTaskAction(), new InitiateDispositionAction(), new PropertiesAction(), new RecordPropertiesAction(), new RefreshAction(), new RefreshTaskAction(), new RelocateAction(), new ReopenAction(), new RemoveHoldAction(), new RemoveHoldOnViewEntitiesAction(), new ModifyTaskAction(), new ModifyAndRunTaskAction(), new EnableTaskAction(), new RunReportAction(), new ScheduleDispositionSweepAction(), new ScheduleReportAction(), new UndeclareAction(), new ViewDocumentInfoAction(), new InitiateRemoveHoldRequestAction(), new CancelRemoveHoldRequestAction(), new ActivateHoldSweepProcessingAction(), new ViewEntitiesOnHoldAction(), new ConvertToDDContainerAction(), new ScheduleDDContainerConversionAction(), new ScheduleDDReportTaskAction() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginService[] getServices()
/*     */   {
/* 409 */     return new PluginService[] { new CreateRecordCategoryService(), new GetRepositoryAttributesService(), new DeclareRecordService(), new GetObjectsService(), new GetDispositionSchedulesService(), new QuickSearchService(), new GetRecordEntryTemplatesService(), new OpenRecordEntryTemplateService(), new GetReportDefinitionsService(), new CloseRecordContainerService(), new GetChoiceListService(), new ReopenRecordContainerService(), new GetFilePlansService(), new GetEntityNameFromNamingPatternService(), new GetAssociatedContentRepositoriesService(), new DeleteService(), new RelocateRecordContainerService(), new FileRecordService(), new PlaceOnHoldService(), new RemoveHoldService(), new GetEntitiesOnHoldService(), new GetDocumentInfoService(), new DynamicHoldRequestService(), new GetHistoryService(), new EditRecordService(), new MoveRecordService(), new CopyRecordService(), new LinkRecordService(), new CreateRecordFolderService(), new SaveHoldService(), new SaveLocationService(), new UndeclareService(), new CreateActionService(), new GetAndSavePluginConfiguration(), new CreateSimpleDispositionScheduleService(), new ConditionSearchService(), new SaveNamingPatternService(), new GetNamingPatternLevelsService(), new ValidateNamingPatternService(), new GetObjectConditionsService(), new CreateRecordVolumeService(), new CreateTriggerService(), new RunReportService(), new GetSearchTemplatesService(), new DeleteReportResultsService(), new CreateFilePlanService(), new EditRecordContainerService(), new SearchService(), new EditFilePlanService(), new GetNextVolumeNameService(), new GetHoldsForAnEntityService(), new GetClassPropertyDescriptionsService(), new ScheduleReportService(), new SaveReportDefinitionService(), new RetrieveLinksOnEntityService(), new GetRecordFromDeclaredDocumentService(), new CopyService(), new GetAllRecordPropertiesService(), new CreateIERDesktopService(), new RefreshService(), new GetObjectStoreSecurityService(), new ObjectStoreSecurityService(), new InitiateDispositionService(), new GetRepositoryPermissionsService(), new GetWorkflowVersionsService(), new SaveSystemConfigurationsService() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginResponseFilter[] getResponseFilters()
/*     */   {
/* 488 */     return new PluginResponseFilter[] { new P8OpenFolderOverride(), new P8OpenContentClassOverride(), new P8OpenItemOverride(), new P8LogonActionOverride(), new GetAsyncTasksOverride(), new GetDesktopActionOverride(), new P8OpenSearchTemplateOverride() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginRequestFilter[] getRequestFilters()
/*     */   {
/* 507 */     return new PluginRequestFilter[] { new P8OpenFolderPreOverride(), new P8RetrieveNextPagePreOverride(), new P8RetrieveItemsActionPreOverride(), new P8FindClassesPreOverride(), new ConfigurationFilter(), new P8OpenItemPreOverride(), new P8AddSearchTemplateOverride(), new P8UpdateSearchTemplateOverride(), new RescheduleAsyncTaskOverride(), new P8EditAttributesOverride(), new ScheduleAsyncTaskOverride() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginMenuType[] getMenuTypes()
/*     */   {
/* 527 */     return new PluginMenuType[] { new IERBrowseFilePlanToolbarMenuType(), new IERRecordCategoryContextMenuType(), new IERVolumeContextMenuType(), new IERFilePlanContextMenuType(), new IERFilePlanConfigureContextMenuType(), new IERPhysicalBoxHybridRecordFolderContextMenuType(), new IERElectronicRecordFolderContextMenuType(), new IERMultipleContainerAndRecordContextMenuType(), new IERMultipleContainerContextMenuType(), new IERElectronicRecordContextMenuType(), new IERPhysicalRecordContextMenuType(), new IERMultipleRecordContextMenuType(), new IERCustomObjectContextMenuType(), new IERPlaceOnHoldContextMenuType(), new IERViewEntitiesOnHoldContextMenuType(), new IERRemoveHoldContextMenuType(), new IERConfigureFlyoutToolbarMenuType(), new IERActionsConfigureToolbarMenuType(), new IERLocationsConfigureToolbarMenuType(), new IERHoldsConfigureToolbarMenuType(), new IERHoldsConfigureContextMenuType(), new IERNamingPatternsConfigureToolbarMenuType(), new IERDispositionSchedulesConfigureToolbarMenuType(), new IERTriggersConfigureToolbarMenuType(), new IERReportDefinitionsConfigureToolbarMenuType(), new IERReportDefinitionsConfigureContextMenuType(), new IERMultiStatusTaskToolbarMenuType(), new IERReportsCompletedTaskContextMenuType(), new IERCompletedTaskContextMenuType(), new IERCompletedRecurringTaskContextMenuType(), new IERFailedTaskContextMenuType(), new IERFailedRecurringTaskContextMenuType(), new IERScheduledTaskContextMenuType(), new IERInProgressTaskContextMenuType(), new IERReportsCompletedTaskInstanceContextMenuType(), new IERCompletedTaskInstanceContextMenuType(), new IERFailedTaskInstanceContextMenuType(), new IERScheduledTaskInstanceContextMenuType(), new IERScheduledRecurringTaskContextMenuType(), new IERDisabledRecurringTaskContextMenuType(), new IERInProgressTaskInstanceContextMenuType(), new IERReportTaskToolbarMenuType(), new IERDefensibleDispositionTaskToolbarMenuType(), new IERHoldTaskToolbarMenuType(), new IERFavoriteRecordCategoryContextMenuType(), new IERFavoriteElectronicRecordFolderContextMenuType(), new IERFavoritePhysicalRecordFolderContextMenuType(), new IERFavoriteVolumeContextMenuType(), new IERFavoriteElectronicRecordContextMenuType(), new IERFavoritePhysicalRecordContextMenuType(), new IERSearchElectronicRecordContextMenuType(), new IERSearchPhysicalRecordContextMenuType(), new IERMultipleTasksContextMenuType(), new IERMultipleCustomObjectsContextMenuType(), new IERPropertiesOnlyCustomObjectContextMenuType(), new IERHoldConditionContainerContextMenuType(), new IERHoldConditionRecordContextMenuType() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginMenu[] getMenus()
/*     */   {
/* 593 */     return new PluginMenu[] { new IERBrowseFilePlanToolbarMenu(), new IERRecordCategoryContextMenu(), new IERVolumeContextMenu(), new IERFilePlanContextMenu(), new IERFilePlanConfigureContextMenu(), new IERPhysicalBoxHybridRecordFolderContextMenu(), new IERElectronicRecordFolderContextMenu(), new IERMultipleContainerAndRecordContextMenu(), new IERMultipleContainerContextMenu(), new IERElectronicRecordContextMenu(), new IERPhysicalRecordContextMenu(), new IERMultipleRecordContextMenu(), new IERBannerToolsContextMenu(), new IERCustomObjectContextMenu(), new IERPlaceOnHoldContextMenu(), new IERViewEntitiesOnHoldContextMenu(), new IERRemoveHoldContextMenu(), new IERConfigureFlyoutToolbarMenu(), new IERActionsConfigureToolbarMenu(), new IERLocationsConfigureToolbarMenu(), new IERHoldsConfigureToolbarMenu(), new IERHoldsConfigureContextMenu(), new IERNamingPatternsConfigureToolbarMenu(), new IERDispositionSchedulesConfigureToolbarMenu(), new IERTriggersConfigureToolbarMenu(), new IERFilePlansConfigureToolbarMenu(), new IERReportDefinitionsConfigureToolbarMenu(), new IERReportDefinitionsConfigureContextMenu(), new IERMultiStatusTaskToolbarMenu(), new IERCompletedTaskContextMenu(), new IERReportsCompletedTaskContextMenu(), new IERFailedTaskContextMenu(), new IERScheduledTaskContextMenu(), new IERInProgressTaskContextMenu(), new IERCompletedTaskInstanceContextMenu(), new IERReportsCompletedTaskInstanceContextMenu(), new IERCompletedRecurringTaskContextMenu(), new IERFailedTaskInstanceContextMenu(), new IERFailedRecurringTaskContextMenu(), new IERScheduledTaskInstanceContextMenu(), new IERScheduledRecurringTaskContextMenu(), new IERDisabledRecurringTaskContextMenu(), new IERInProgressTaskInstanceContextMenu(), new IERReportTaskToolbarMenu(), new IERDefensibleDisposalTaskToolbarMenu(), new IERHoldTaskToolbarMenu(), new IERCompletedTaskToolbarMenu(), new IERFavoriteRecordCategoryContextMenu(), new IERFavoriteElectronicRecordFolderContextMenu(), new IERFavoritePhysicalRecordFolderContextMenu(), new IERFavoriteVolumeContextMenu(), new IERFavoriteElectronicRecordContextMenu(), new IERFavoritePhysicalRecordContextMenu(), new IERSearchElectronicRecordContextMenu(), new IERSearchPhysicalRecordContextMenu(), new IERMultipleTasksContextMenu(), new IERMultipleCustomObjectsContextMenu(), new IERPropertiesOnlyCustomObjectContextMenu(), new IERHoldConditionContainerContextMenu(), new IERHoldConditionRecordContextMenu() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginLayout[] getLayouts()
/*     */   {
/* 664 */     return new PluginLayout[] { new IERPluginLayout() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PluginFeature[] getFeatures()
/*     */   {
/* 678 */     return new PluginFeature[] { new IERReportsPluginFeature() };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getId()
/*     */   {
/* 690 */     return "IERApplicationPlugin";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName(Locale locale)
/*     */   {
/* 699 */     return "IBM Enterprise Records";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getScript()
/*     */   {
/* 708 */     return "ier.js";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCSSFileName()
/*     */   {
/* 717 */     return "ier/widget/resources/ier.css.jgz";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDojoModule()
/*     */   {
/* 727 */     return "ier";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVersion()
/*     */   {
/* 739 */     return "5.1.2 (rec560.177)";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCustomLayoutClass()
/*     */   {
/* 748 */     return "ecm.widget.layout.MainLayout";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getConfigurationDijitClass()
/*     */   {
/* 761 */     return "ier.widget.admin.PluginConfigurationPane";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void applicationInit(HttpServletRequest request, PluginServiceCallbacks callbacks)
/*     */     throws Exception
/*     */   {
/* 771 */     pluginLogger = new PluginLogger(this);
/*     */     try
/*     */     {
/* 774 */       ApplicationConfig appConfig = Config.getApplicationConfig("navigator");
/*     */       
/*     */ 
/* 777 */       if (CreateIERDesktopService.isDesktopUnique(appConfig, "IER")) {
/* 778 */         CreateIERDesktopService.createIERDesktop(appConfig, "IER", "IERDesktop", null);
/*     */       }
/*     */       
/* 781 */       MenuConfig contentListToolbar = Config.getMenuConfig("navigator", "DefaultContentListToolbar", new boolean[0]);
/* 782 */       String[] menuIds = appConfig.getMenusId();
/* 783 */       if (!doesMenuExist(menuIds, "IERDeclareContentListToolbar")) {
/* 784 */         MenuConfig ierDeclareContentListToolbar = Config.getMenuConfig("navigator", "IERDeclareContentListToolbar", new boolean[0]);
/* 785 */         ierDeclareContentListToolbar.setId("IERDeclareContentListToolbar");
/* 786 */         ierDeclareContentListToolbar.setDescription(MessageResources.getLocalizedMessage(MessageCode.DECLARE_TOOLBAR_ACTION, new Object[0]));
/* 787 */         ierDeclareContentListToolbar.setName(MessageResources.getLocalizedMessage(MessageCode.DECLARE_TOOLBAR_ACTION, new Object[0]));
/* 788 */         ierDeclareContentListToolbar.setItems(contentListToolbar.getItems());
/* 789 */         ierDeclareContentListToolbar.setType(contentListToolbar.getType());
/* 790 */         if (contentListToolbar.getTypeLabel() != null)
/* 791 */           ierDeclareContentListToolbar.setTypeLabel(contentListToolbar.getTypeLabel());
/* 792 */         addDeclareAction(ierDeclareContentListToolbar);
/* 793 */         ierDeclareContentListToolbar.save();
/*     */         
/* 795 */         appConfig.addValueToList("menus", "IERDeclareContentListToolbar");
/* 796 */         appConfig.save();
/*     */       }
/*     */       else
/*     */       {
/* 800 */         MenuConfig ierDeclareToolbarMenu = Config.getMenuConfig("navigator", "IERDeclareContentListToolbar", new boolean[0]);
/* 801 */         if (!containsAllDeclareActions(ierDeclareToolbarMenu)) {
/* 802 */           addDeclareAction(ierDeclareToolbarMenu);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 807 */       MenuConfig itemContextMenu = Config.getMenuConfig("navigator", "DefaultItemContextMenu", new boolean[0]);
/* 808 */       if (!doesMenuExist(menuIds, "IERDeclareItemContextMenu")) {
/* 809 */         MenuConfig ierDeclareItemContextMenu = Config.getMenuConfig("navigator", "IERDeclareItemContextMenu", new boolean[0]);
/* 810 */         ierDeclareItemContextMenu.setId("IERDeclareItemContextMenu");
/* 811 */         ierDeclareItemContextMenu.setDescription(MessageResources.getLocalizedMessage(MessageCode.DECLARE_MENU_ACTION, new Object[0]));
/* 812 */         ierDeclareItemContextMenu.setName(MessageResources.getLocalizedMessage(MessageCode.DECLARE_MENU_ACTION, new Object[0]));
/* 813 */         ierDeclareItemContextMenu.setType(itemContextMenu.getType());
/* 814 */         if (itemContextMenu.getTypeLabel() != null)
/* 815 */           ierDeclareItemContextMenu.setTypeLabel(itemContextMenu.getTypeLabel());
/* 816 */         ierDeclareItemContextMenu.setItems(itemContextMenu.getItems());
/* 817 */         addDeclareAction(ierDeclareItemContextMenu);
/* 818 */         ierDeclareItemContextMenu.save();
/*     */         
/* 820 */         appConfig.addValueToList("menus", "IERDeclareItemContextMenu");
/* 821 */         appConfig.save();
/*     */       }
/*     */       else
/*     */       {
/* 825 */         MenuConfig ierDeclareItemContextMenu = Config.getMenuConfig("navigator", "IERDeclareItemContextMenu", new boolean[0]);
/* 826 */         if (!containsAllDeclareActions(ierDeclareItemContextMenu)) {
/* 827 */           addDeclareAction(ierDeclareItemContextMenu);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 832 */       Collection<DesktopConfig> desktopConfigs = appConfig.getDesktopList();
/* 833 */       for (DesktopConfig desktopConfig : desktopConfigs) {
/* 834 */         if ((desktopConfig.getDefault() != null) && (desktopConfig.getDefault().equalsIgnoreCase("yes")))
/*     */         {
/* 836 */           if ((desktopConfig.getProperty("ItemContextMenu").equals("DefaultItemContextMenu")) || (desktopConfig.getProperty("ItemContextMenu").equals("")))
/*     */           {
/* 838 */             desktopConfig.setProperty("ItemContextMenu", "IERDeclareItemContextMenu");
/* 839 */             desktopConfig.save();
/*     */           }
/*     */           
/* 842 */           if ((desktopConfig.getProperty("ContentListToolbar").equals("DefaultContentListToolbar")) || (desktopConfig.getProperty("ContentListToolbar").equals("")))
/*     */           {
/* 844 */             desktopConfig.setProperty("ContentListToolbar", "IERDeclareContentListToolbar");
/* 845 */             desktopConfig.save();
/* 846 */             appConfig.save();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 852 */       com.ibm.ecm.configuration.SettingsConfig settingsConfig = Config.getApplicationConfig().getSettingsConfig();
/* 853 */       settingsConfig.setAdminUsers(appConfig.getSettingsConfig().getAdminUsers());
/* 854 */       settingsConfig.save();
/*     */       
/*     */ 
/*     */ 
/* 858 */       com.ibm.ier.plugin.configuration.SettingsConfig ierSettingsConfig = Config.getSettingsConfig();
/* 859 */       String key = ierSettingsConfig.getAESEncryptionKey();
/* 860 */       if ((key == null) || (key.isEmpty())) {
/* 861 */         ierSettingsConfig.setAESEncryptionKey(AESEncryptionUtil.generateKey());
/* 862 */         ierSettingsConfig.save();
/*     */       }
/*     */     }
/*     */     catch (Exception e) {}
/*     */   }
/*     */   
/*     */   private boolean doesMenuExist(String[] menuIds, String menuId)
/*     */   {
/* 870 */     for (String id : menuIds) {
/* 871 */       if (menuId.equalsIgnoreCase(id))
/* 872 */         return true;
/*     */     }
/* 874 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void addDeclareAction(MenuConfig menuConfig)
/*     */     throws Exception
/*     */   {
/* 882 */     String[] actions = menuConfig.getItems();
/* 883 */     boolean declareActionFound = false;
/* 884 */     boolean viewRecordInformationActionFound = false;
/*     */     
/* 886 */     for (String actionID : actions) {
/* 887 */       if (actionID.equals("IERDeclare")) {
/* 888 */         declareActionFound = true;
/*     */       }
/* 890 */       else if (actionID.equals("IERRecordProperties")) {
/* 891 */         viewRecordInformationActionFound = true;
/*     */       }
/*     */     }
/*     */     
/* 895 */     if (!declareActionFound) {
/* 896 */       String[] newActions = new String[actions.length + 1];
/* 897 */       for (int i = 0; i < actions.length; i++) {
/* 898 */         newActions[i] = actions[i];
/*     */       }
/* 900 */       newActions[actions.length] = "IERDeclare";
/* 901 */       menuConfig.setItems(newActions);
/* 902 */       menuConfig.save();
/*     */     }
/*     */     
/* 905 */     if (!viewRecordInformationActionFound) {
/* 906 */       String[] newActions = new String[actions.length + 1];
/* 907 */       for (int i = 0; i < actions.length; i++) {
/* 908 */         newActions[i] = actions[i];
/*     */       }
/* 910 */       newActions[actions.length] = "IERRecordProperties";
/* 911 */       menuConfig.setItems(newActions);
/* 912 */       menuConfig.save();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean containsAllDeclareActions(MenuConfig menuConfig)
/*     */   {
/* 922 */     String[] actions = menuConfig.getItems();
/*     */     
/* 924 */     boolean declareActionFound = false;
/* 925 */     boolean viewRecordInformationActionFound = false;
/*     */     
/* 927 */     for (String actionID : actions) {
/* 928 */       if (actionID.equals("IERDeclare")) {
/* 929 */         declareActionFound = true;
/*     */       }
/* 931 */       else if (actionID.equals("IERRecordProperties")) {
/* 932 */         viewRecordInformationActionFound = true;
/*     */       }
/*     */     }
/*     */     
/* 936 */     return (declareActionFound) && (viewRecordInformationActionFound);
/*     */   }
/*     */   
/*     */   public static PluginLogger getPluginLogger() {
/* 940 */     if (pluginLogger == null)
/* 941 */       pluginLogger = new PluginLogger("IER");
/* 942 */     return pluginLogger;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\IERApplicationPlugin.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */