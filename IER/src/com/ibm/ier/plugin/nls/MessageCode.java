/*     */ package com.ibm.ier.plugin.nls;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum MessageCode
/*     */ {
/*  22 */   E_APP_UNEXPECTED("error.application.unexpected"), 
/*  23 */   E_CONTAINER_INVALID_NAME("error.container.invalidName"), 
/*  24 */   E_CONTAINER_DUPLICATE_NAME("error.container.duplicateName"), 
/*  25 */   E_RECCAT_UNEXPECTED("error.recordcategory.unexpected"), 
/*  26 */   E_RECCAT_INVALID_LOC("error.recordcategory.invalidLocation"), 
/*  27 */   E_INVALID_CONTENT_REPOSITORY("error.service.invalidContentRepository"), 
/*  28 */   E_CATCLOSE_INVALID_CONTAINER("error.containerclose.invalidContainer"), 
/*  29 */   E_CATCLOSE_UNEXPECTED("error.containerclose.unexpected"), 
/*  30 */   E_DECLARE_INVALID_CONTAINER("error.recordDeclareService.invalidDeclareContainer"), 
/*  31 */   E_DECLARE_READONLY("error.recordDeclareService.readOnly"), 
/*  32 */   E_DELETE_UNEXPECTED("error.delete.unexpected"), 
/*  33 */   E_RELOCATE_INVALID_DEST_CONTAINER("error.relocate.invalidDestContainer"), 
/*  34 */   E_RELOCATE_INVALID_CONTAINER("error.relocate.invalidContainer"), 
/*  35 */   E_RELOCATE_UNEXPECTED("error.relocate.unexpected"), 
/*  36 */   E_CATOPEN_INVALID_CONTAINER("error.containerreopen.invalidContainer"), 
/*  37 */   E_CATOPEN_UNEXPECTED("error.containerreopen.unexpected"), 
/*  38 */   E_FILERECORD_UNEXPECTED("error.filerecord.unexpected"), 
/*  39 */   E_FILERECORD_INVALID_DEST("error.filerecord.invalidDest"), 
/*  40 */   E_MOVERECORD_UNEXPECTED("error.moverecord.unexpected"), 
/*  41 */   E_PLACEONHOLD_UNEXPECTED("error.placeonhold.unexpected"), 
/*  42 */   E_REMOVEHOLD_UNEXPECTED("error.removehold.unexpected"), 
/*  43 */   E_RECFOLDER_UNEXPECTED("error.recordfolder.unexpected"), 
/*  44 */   E_RECFOLDER_INVALID_LOC("error.recordfolder.invalidLocation"), 
/*  45 */   E_EDITRECORD_UNEXPECTED("error.editrecord.unexpected"), 
/*  46 */   E_HOLD_UNEXPECTED("error.hold.unexpected"), 
/*  47 */   E_HOLD_INVALID_NAME("error.hold.invalidName"), 
/*  48 */   E_COPYRECORD_UNEXPECTED("error.copyrecord.unexpected"), 
/*  49 */   E_COPYRECORD_INVALID_DEST("error.coyprecord.invalidDest"), 
/*  50 */   E_LOCATION_UNEXPECTED("error.location.unexpected"), 
/*  51 */   E_LOCATION_INVALID_NAME("error.location.invalidName"), 
/*  52 */   E_NAMING_PATTERN_UNEXPECTED("error.namingpattern.unexpected"), 
/*  53 */   E_NAMING_PATTERN_INVALID_NAME("error.namingpattern.invalidName"), 
/*  54 */   E_UNDECLARE_UNEXPECTED("error.undeclare.unexpected"), 
/*  55 */   E_ACTION_UNEXPECTED("error.action.unexpected"), 
/*  56 */   E_TRIGGER_UNEXPECTED("error.trigger.unexpected"), 
/*  57 */   E_RECVOLUME_UNEXPECTED("error.recordvolume.unexpected"), 
/*  58 */   E_RECVOLUME_INVALID_LOC("error.recordvolume.invalidLocation"), 
/*  59 */   E_REPORTS_FAILD_TO_RUN("error.reports.failedToRun"), 
/*  60 */   E_REPORTDEFINITION_FAILD_TO_SAVE("error.reportdefinition.failedToSave"), 
/*  61 */   E_REPORTS_CONFIG_MISSING("error.reports.reportConfigurationMissing"), 
/*  62 */   E_REPORTS_REPO_NOT_IN_DOMAIN("error.reports.repositoryNotInDomain"), 
/*  63 */   E_LINKRECORD_UNEXPECTED("error.linkrecord.unexpected"), 
/*  64 */   E_FILEPLAN_UNEXPECTED("error.fileplan.unexpected"), 
/*  65 */   E_FILEPLAN_INVALID_LOC("error.fileplan.invalidLocation"), 
/*  66 */   E_FAILED_TO_LIST_TASKS("error.taskManager.filedToListTasks"), 
/*  67 */   E_FAILED_CONNECT_TO_TASK_MGR_SERVICES("error.taskManager.failedToConnectTaskManagerServices"), 
/*  68 */   E_TASK_UNEXPECTED("error.taskManager.unexpected"), 
/*  69 */   E_COPY_UNEXPECTED("error.copy.unexpected"), 
/*  70 */   E_DESKTOP_ID_NOT_UNIQUE("error.desktopIdNotQueue"), 
/*  71 */   E_REPOSITORY_UNEXPECTED("error.repository.unexpected"), 
/*  72 */   E_REPOSITORY_INVALID_LOC("error.repository.invalidLocation"), 
/*  73 */   E_MARKINGSET_MISSING("error.markingset.missing"), 
/*  74 */   E_PRIVILEGE_INSUFFICIENT("error.privilege.insufficient"), 
/*  75 */   E_FAILED_PE_SESSION("error.failedToGetPESession"), 
/*  76 */   E_MISSING_CONN_POINT_NAME("error.missingConnectionPointName"), 
/*  77 */   E_NO_RESULTS_INIT_DISP("error.noResultsReadyForInitiateDisp"), 
/*  78 */   E_INIT_DISP_SOME_FAILURES("error.initiateDispositionSomeFailures"), 
/*  79 */   E_SYSTEM_CONFIGURATION_UNEXPECTED("error.systemConfiguration.unexpected"), 
/*  80 */   E_PLACE_ON_HOLD_SOME_FAILURES("error.placeOnHoldSomeFailures"), 
/*  81 */   E_CONTAINERS_CLOSE_SOME_FAILURES("error.containersCloseSomeFailures"), 
/*  82 */   E_DELETE_SOME_FAILURES("error.deleteSomeFailures"), 
/*     */   
/*  84 */   ACTION_ABOUT("action.About"), 
/*  85 */   ACTION_ADD_RECORDCATEGORY("action.AddRecordCategory"), 
/*  86 */   ACTION_ADD_RECORDFOLDER("action.AddRecordFolder"), 
/*  87 */   ACTION_ADD_RECORDVOLUME("action.AddRecordVolume"), 
/*  88 */   ACTION_ADD_DISPOSITION_SCHEDULE("action.AddDispositionSchedule"), 
/*  89 */   ACTION_ADD_HOLD("action.AddHold"), 
/*  90 */   ACTION_ADD_LOCATION("action.AddLocation"), 
/*  91 */   ACTION_ADD_NAMING_PATTERN("action.AddNamingPattern"), 
/*  92 */   ACTION_ADD_DISPOSITION_ACTION("action.AddDispositionAction"), 
/*  93 */   ACTION_ADD_EVENT_TRIGGER("action.AddEventTrigger"), 
/*  94 */   ACTION_ACTIVATE("action.Activate"), 
/*  95 */   ACTION_ADD_FILEPLAN("action.AddFilePlan"), 
/*  96 */   ACTION_ADD_TO_FAVORITES("action.AddToFavorites"), 
/*  97 */   ACTION_ADD_RECORDTYPE("action.AddRecordType"), 
/*  98 */   ACTION_ADD_TRANFERMAPPING("action.AddTransferMapping"), 
/*  99 */   ACTION_CLOSE("action.Close"), 
/* 100 */   ACTION_COPY("action.Copy"), 
/* 101 */   ACTION_CREATE_LINK("action.CreateLink"), 
/* 102 */   ACTION_DECLARE("action.Declare"), 
/* 103 */   ACTION_DELETE("action.Delete"), 
/* 104 */   ACTION_FILE("action.File"), 
/* 105 */   ACTION_INACTIVATE("action.Inactivate"), 
/* 106 */   ACTION_INITIATEDISP("action.InitiateDisposition"), 
/* 107 */   ACTION_LAUNCH_CHARGE_OUT("action.LaunchChargeOut"), 
/* 108 */   ACTION_MOVE("action.Move"), 
/* 109 */   ACTION_PLACEONHOLD("action.PlaceOnHold"), 
/* 110 */   ACTION_PROPERTIES("action.Properties"), 
/* 111 */   ACTION_REFRESH("action.Refresh"), 
/* 112 */   ACTION_RELOCATE("action.Relocate"), 
/* 113 */   ACTION_REMOVE_HOLD("action.RemoveHold"), 
/* 114 */   ACTION_REMOVE_HOLD_ON_VIEWENTITIES("action.RemoveHoldOnViewEntities"), 
/* 115 */   ACTION_REOPEN("action.Reopen"), 
/* 116 */   ACTION_SCHEDULE_REPORT("action.ScheduleReport"), 
/* 117 */   ACTION_SCHEDULE_DISPOSITION_SWEEP("action.ScheduleDispositionSweep"), 
/* 118 */   ACTION_SCHEDULE_HOLD_SWEEP("action.ScheduleHoldSweep"), 
/* 119 */   ACTION_UNDECLARE("action.Undeclare"), 
/* 120 */   ACTION_VIEW_DOCUMENT_INFO("action.ViewDocumentInfo"), 
/* 121 */   ACTION_OPEN_RECORD_CONTENT("action.OpenRecordContent"), 
/* 122 */   ACTION_VIEW_ENTITIES_ONHOLD("action.ViewEntitiesOnHold"), 
/*     */   
/* 124 */   ACTION_INITIATE_REMOVE_HOLD_REQUEST("action.InitiateRemoveHoldRequest"), 
/* 125 */   ACTION_CANCEL_REMOVE_HOLD_REQUEST("action.CancelRemoveHoldRequest"), 
/* 126 */   ACTION_ACTIVATE_HOLD_SWEEP_PROCESSING("action.ActivateHoldSweepProcessing"), 
/*     */   
/* 128 */   ACTION_DELETE_REPORT_RESULT("action.DeleteReportResult"), 
/* 129 */   ACTION_RECORD_PROPERTIES("action.RecordProperties"), 
/* 130 */   ACTION_ADD_REPORT_DEFINITION("action.AddReportDefinition"), 
/* 131 */   ACTION_RUN_REPORT("action.RunReport"), 
/* 132 */   ACTION_TASK_MODIFYANDRUN("action.TaskModifyAndRun"), 
/* 133 */   ACTION_TASK_MODIFY("action.TaskModify"), 
/* 134 */   ACTION_TASK_ENABLE("action.TaskEnable"), 
/* 135 */   ACTION_TASK_DISABLE("action.TaskDisable"), 
/* 136 */   ACTION_TASK_OPEN("action.TaskOpen"), 
/* 137 */   ACTION_TASK_DOWNLOAD("action.TaskDownload"), 
/* 138 */   ACTION_CONVERT_TO_DD_CONTAINER("action.ConvertToDDContainer"), 
/* 139 */   ACTION_SCHEDULE_DDCONTAINER_CONVERSION("action.ScheduleDDContainerConversion"), 
/* 140 */   ACTION_SCHEDULE_DD_REPORT_TASK("action.ScheduleDDReportTask"), 
/*     */   
/* 142 */   NAME("name"), 
/* 143 */   CLASS("Class"), 
/* 144 */   CONTAINER_ID("containerId"), 
/* 145 */   DECLARE_TOOLBAR_ACTION("declare.toolbar.contentlist"), 
/* 146 */   DECLARE_MENU_ACTION("declare.contextmenu.document"), 
/* 147 */   PLUGIN_LAYOUT_NAME("plugin.layout.name"), 
/*     */   
/* 149 */   FEATURE_REPORTS("feature.reports"), 
/* 150 */   FEATURE_REPORTS_TOOLTIP("feature.reportsTooltip"), 
/*     */   
/* 152 */   ACTIONTYPE_REVIEW("actionType.Review"), 
/* 153 */   ACTIONTYPE_EXPORT("actionType.Export"), 
/* 154 */   ACTIONTYPE_TRANSFER("actionType.Transfer"), 
/* 155 */   ACTIONTYPE_DESTROY("actionType.Destroy"), 
/* 156 */   ACTIONTYPE_INTERIM_TRANSFER("actionType.InterimTransfer"), 
/* 157 */   ACTIONTYPE_CUTOFF("actionType.Cutoff"), 
/* 158 */   ACTIONTYPE_VITAL_REVIEW("actionType.VitalReview"), 
/* 159 */   ACTIONTYPE_AUTODESTROY("actionType.AutoDestroy"), 
/*     */   
/* 161 */   TRIGGERTYPE_INTERNALEVENT("triggerEventType.InternalEvent"), 
/* 162 */   TRIGGERTYPE_EXTERNALEVENT("triggerEventType.ExternalEvent"), 
/* 163 */   TRIGGERTYPE_RECURRINGEVENT("triggerEventType.RecurringEvent"), 
/* 164 */   TRIGGERTYPE_PREDEFINEDDATE("triggerEventType.PredefinedDate"), 
/*     */   
/* 166 */   MENU_ACTIONS_TOOLBAR("menu_actionsToolbar"), 
/* 167 */   MENU_MULTI_STATUS_TASK_TOOLBAR("menu_multiStatusTaskToolbar"), 
/* 168 */   MENU_BROWSE_FILEPLAN_TOOLBAR("menu_browseFilePlanToolbar"), 
/* 169 */   MENU_COMPLETED_TASK_TOOLBAR("menu_completedTaskToolbar"), 
/* 170 */   MENU_REPORT_TASK_TOOLBAR("menu_reportTaskToolbar"), 
/* 171 */   MENU_DISP_TASK_TOOLBAR("menu_dispositionTaskToolbar"), 
/* 172 */   MENU_HOLD_TASK_TOOLBAR("menu_holdTaskToolbar"), 
/* 173 */   MENU_REPORT_RESULTS_CONTEXT("menu_reportResultsContextMenu"), 
/* 174 */   MENU_BANNER_CONTEXT("menu_bannerToolsContextMenu"), 
/* 175 */   MENU_CONFIGURE_FLYOUT_TOOLBAR("menu_configureflyoutToolbar"), 
/* 176 */   MENU_CONTENTLIST_TOOLBAR("menu_contentListToolbar"), 
/* 177 */   MENU_CUSTOMOBJECT_CONTEXT("menu_customObjectContextMenu"), 
/* 178 */   MENU_CUSTOMOBJECTS_CONTEXT("menu_customObjectsContextMenu"), 
/* 179 */   MENU_PLACEONHOLD_CONTEXT("menu_placeOnHoldContextMenu"), 
/* 180 */   MENU_VIEWENTITIESONHOLD_CONTEXT("menu_viewEntitiesOnHoldContextMenu"), 
/* 181 */   MENU_REMOVEHOLD_CONTEXT("menu_removeHoldContextMenu"), 
/* 182 */   MENU_DISPSCHEDULE_TOOLBAR("menu_dispositonSchedulesToolbar"), 
/* 183 */   MENU_ELECT_RECORD_CONTEXT("menu_electronicRecordContextMenu"), 
/* 184 */   MENU_ELECT_RECORD_FOLDER_CONTEXT("menu_electronicRecordFolderContextMenu"), 
/* 185 */   MENU_FILEPLAN_CONTEXT("menu_filePlanContextMenu"), 
/* 186 */   MENU_FILEPLAN_CONFIGURE_CONTEXT("menu_filePlanConfigureContextMenu"), 
/* 187 */   MENU_FILEPLAN_TOOLBAR("menu_filePlansToolbar"), 
/* 188 */   MENU_HOLDSPANE_TOOLBAR("menu_holdsPaneToolbar"), 
/* 189 */   MENU_HOLDSPANE_CONFIGURE_CONTEXT("menu_holdsPaneContextMenu"), 
/* 190 */   MENU_JOBSFLYOUT_TOOLBAR("menu_jobsFlyoutToolbar"), 
/* 191 */   MENU_LOCATIONSPANE_TOOLBAR("menu_locationsPaneToolbar"), 
/* 192 */   MENU_MULTI_CONTAINERS_AND_RECORDS_CONTEXT("menu_multipleContainersAndRecordsContextMenu"), 
/* 193 */   MENU_MULTI_CONTAINERS_CONTEXT("menu_multipleContainersContextMenu"), 
/* 194 */   MENU_MULTI_RECORDS_CONTEXT("menu_multipleRecordsContextMenu"), 
/* 195 */   MENU_NAMINGPATTERN_TOOLBAR("menu_namingPatternsToolbar"), 
/* 196 */   MENU_PHYSBOX_OR_HYBRIDRECORDFOLDER_CONTEXT("menu_physicalBoxOrHybridRecordFolderContextMenu"), 
/* 197 */   MENU_PHYSRECORD_CONTEXT("menu_physicalRecordContextMenu"), 
/* 198 */   MENU_RECORDCATEGORY_CONTEXT("menu_recordCategoryContextMenu"), 
/* 199 */   MENU_TRANSFERMAPPINGS_TOOLBAR("menu_transferMappingsPaneToolbar"), 
/* 200 */   MENU_TRIGGERS_TOOLBAR("menu_triggersPaneToolbar"), 
/* 201 */   MENU_VOLUME_CONTEXT("menu_volumeContextMenu"), 
/* 202 */   MENU_RECORDTYPE_TOOLBAR("menu_recordTypesToolbar"), 
/* 203 */   MENU_REPORTDEF_TOOLBAR("menu_reportDefintionsToolbar"), 
/* 204 */   MENU_REPORTDEF_CONTEXT("menu_reportDefintionsContextMenu"), 
/* 205 */   MENU_COMPLETED_TASK_CONTEXT("menu_completedTaskContextMenu"), 
/* 206 */   MENU_COMPLETED_RECURRING_TASK_CONTEXT("menu_completedRecurringTaskContextMenu"), 
/* 207 */   MENU_COMPLETED_TASK_INSTANCE_CONTEXT("menu_completedTaskInstanceContextMenu"), 
/* 208 */   MENU_FAILED_TASK_CONTEXT("menu_failedTaskContextMenu"), 
/* 209 */   MENU_FAILED_TASK_INSTANCE_CONTEXT("menu_failedTaskInstanceContextMenu"), 
/* 210 */   MENU_SCHEDULED_TASK_CONTEXT("menu_scheduledTaskContextMenu"), 
/* 211 */   MENU_DISABLED_TASK_CONTEXT("menu_disabledTaskContextMenu"), 
/* 212 */   MENU_SCHEDULED_TASK_INSTANCE_CONTEXT("menu_scheduledTaskInstanceContextMenu"), 
/* 213 */   MENU_INPROGRESS_TASK_CONTEXT("menu_inProgressTaskContextMenu"), 
/* 214 */   MENU_INPROGRESS_TASK_INSTANCE_CONTEXT("menu_inProgressTaskInstanceContextMenu"), 
/* 215 */   MENU_FAVORITE_RECORD_CATEGORY_CONTEXT("menu_favoriteRecordCategoryContextMenu"), 
/* 216 */   MENU_FAVORITE_ELECTRONIC_RECORD_FOLDER_CONTEXT("menu_favoriteElectronicRecordFolderContextMenu"), 
/* 217 */   MENU_FAVORITE_PHYSICAL_RECORD_FOLDER_CONTEXT("menu_favoritePhysicalRecordFolderContextMenu"), 
/* 218 */   MENU_FAVORITE_VOLUME_CONTEXT("menu_favoriteVolumeContextMenu"), 
/* 219 */   MENU_FAVORITE_ELECTRONIC_RECORD_CONTEXT("menu_favoriteElectronicRecordContextMenu"), 
/* 220 */   MENU_FAVORITE_PHYSICAL_RECORD_CONTEXT("menu_favoritePhysicalRecordContextMenu"), 
/* 221 */   MENU_SEARCH_ELECTRONIC_RECORD_CONTEXT("menu_searchElectronicRecordContextMenu"), 
/* 222 */   MENU_SEARCH_PHYSICAL_RECORD_CONTEXT("menu_searchPhysicalRecordContextMenu"), 
/* 223 */   MENU_IER_HOLD_CONDITION_RECORD_CONTEXT("menu_ierHoldConditionRecordContextMenu"), 
/* 224 */   MENU_IER_HOLD_CONDITION_CONTAINER_CONTEXT("menu_ierHoldConditionContainerContextMenu"), 
/*     */   
/* 226 */   STATUS_INPROGRESS("statusInProgress"), 
/* 227 */   STATUS_COMPLETED("statusCompleted"), 
/* 228 */   STATUS_PAUSED("statusPaused"), 
/* 229 */   STATUS_FAILED("statusFailed"), 
/* 230 */   STATUS_CANCELED("statusCanceled"), 
/* 231 */   STATUS_SCHEDULED("statusScheduled"), 
/*     */   
/* 233 */   TYPE("type"), 
/* 234 */   DESCRIPTION("description"), 
/* 235 */   USER("user"), 
/* 236 */   START_TIME("startTime"), 
/* 237 */   END_TIME("endTime"), 
/* 238 */   STATUS("status"), 
/* 239 */   IS_RECURRING("isRecurring"), 
/* 240 */   REPEAT_CYCLE("repeatCycle"), 
/*     */   
/* 242 */   REPORT("report"), 
/* 243 */   DISPOSITION_SWEEP("dispositionSweep"), 
/* 244 */   HOLD_SWEEP("holdSweep"), 
/*     */   
/* 246 */   SEARCH_RESULTS_RETURNED("search.results.returned"), 
/* 247 */   SEARCH_RESULTS_RETURNEDONE("search.results.returnedOne");
/*     */   
/*     */ 
/*     */   private String messageKey;
/*     */   
/*     */   private MessageCode(String code)
/*     */   {
/* 254 */     this.messageKey = code;
/*     */   }
/*     */   
/*     */   public String getMessageKey()
/*     */   {
/* 259 */     return this.messageKey;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\nls\MessageCode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */