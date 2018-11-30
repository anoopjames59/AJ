define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/_base/json",
	"dojo/string",
	"dojo/dom-construct",
	"dijit/Dialog",
	"dijit/form/RadioButton",
	"dijit/form/Button",
	"ecm/model/ContentItem",
	"ecm/model/Favorite",
	"ecm/model/Request",
	"ecm/widget/dialog/EditPropertiesDialog",
	"ecm/widget/dialog/ConfirmationDialog",
	"ecm/widget/layout/CommonActionsHandler",
	"ecm/widget/dialog/MessageDialog",
	"ier/constants",
	"ier/messages",
	"ier/util/dialog",
	"ier/util/util",
	"ier/widget/admin/config",
	"ier/widget/dialog/AboutDialog",
	"ier/widget/dialog/ActionDialog",
	"ier/widget/dialog/DispositionScheduleDialog",
	"ier/widget/dialog/AddFilePlanDialog",
	"ier/widget/dialog/EventTriggerDialog",
	"ier/widget/dialog/AddHoldDialog",
	"ier/widget/dialog/AddLocationDialog",
	"ier/widget/dialog/AddNamingPatternDialog",
	"ier/widget/dialog/AddRecordCategoryDialog",
	"ier/widget/dialog/AddRecordFolderDialog",
	"ier/widget/dialog/AddRecordVolumeDialog",
	"ier/widget/dialog/CloseRecordContainerDialog",
	"ier/widget/dialog/CopyRecordDialog",
	"ier/widget/dialog/LinkRecordDialog",
	"ier/widget/dialog/DeclareRecordDialog",
	"ier/widget/dialog/DocumentInfoDialog",
	"ier/widget/dialog/FilePlanPropertiesDialog",
	"ier/widget/dialog/FileRecordDialog",
	"ier/widget/dialog/HoldDialog",
	"ier/widget/dialog/ViewEntitiesOnHoldDialog",
	"ier/widget/dialog/LocationDialog",
	"ier/widget/dialog/MoveRecordDialog",
	"ier/widget/dialog/NamingPatternDialog",
	"ier/widget/dialog/PlaceOnHoldDialog",
	"ier/widget/dialog/RemoveHoldDialog",
	"ier/widget/dialog/RelocateRecordContainerDialog",
	"ier/widget/dialog/RecordPropertiesDialog",
	"ier/widget/dialog/RecordCategoryPropertiesDialog",
	"ier/widget/dialog/RecordFolderPropertiesDialog",
	"ier/widget/dialog/RecordVolumePropertiesDialog",
	"ier/widget/dialog/AddReportDefinitionDialog",
	"ier/widget/dialog/ReportDefinitionDialog",
	"ier/widget/dialog/ScheduleReportWizardDialog",
	"ier/widget/dialog/ReScheduleReportWizardDialog",
	"ier/widget/dialog/ScheduleDDContainerConversionWizard",
	"ier/widget/dialog/ScheduleDDSweepReportTaskWizard",
	"ier/model/DefensibleDisposalSchedule",
	"ier/widget/dialog/TaskErrorDialog",
	"ier/widget/dialog/TaskInformationDialog"
], function(dojo_declare, dojo_lang, dojo_array, dojo_json, dojo_string, dojo_construct, Dialog, RadioButton, Button, ecm_model_ContentItem, ecm_model_Favorite, ecm_model_Request, ecm_widget_dialog_EditPropertiesDialog, ecm_widget_dialog_ConfirmationDialog, ecm_widget_layout_CommonActionsHandler,
		ecm_dialog_MessageDialog, ier_constants, ier_messages, ier_util_dialog, ier_util, ier_admin_config, ier_dialog_AboutDialog, ier_dialog_ActionDialog, ier_dialog_DispositionScheduleDialog,
		ier_dialog_AddFilePlanDialog, ier_dialog_EventTriggerDialog, ier_dialog_AddHoldDialog, ier_dialog_AddLocationDialog, ier_dialog_AddNamingPatternDialog,
		ier_dialog_AddRecordCategoryDialog, ier_dialog_AddRecordFolderDialog, ier_dialog_AddRecordVolumeDialog,	ier_dialog_CloseRecordContainerDialog, ier_dialog_CopyRecordDialog,ier_dialog_LinkRecordDialog,
		ier_dialog_DeclareRecordDialog, ier_dialog_DocumentInfoDialog, ier_dialog_FilePlanPropertiesDialog, ier_dialog_FileRecordDialog, ier_dialog_HoldDialog, ier_dialog_ViewEntitiesOnHoldDialog, ier_dialog_LocationDialog,
		ier_dialog_MoveRecordDialog, ier_dialog_NamingPatternDialog, ier_dialog_PlaceOnHoldDialog, ier_dialog_RemoveHoldDialog, ier_dialog_RelocateRecordContainerDialog,
		ier_dialog_RecordPropertiesDialog, ier_dialog_RecordCategoryPropertiesDialog, ier_dialog_RecordFolderPropertiesDialog, ier_dialog_RecordVolumePropertiesDialog, 
		ier_dialog_AddReportDefinitionDialog, ier_dialog_ReportDefinitionDialog, ier_widget_ScheduleReportWizardDialog, ier_widget_ReScheduleReportWizardDialog, ScheduleDDContainerConversionWizard,
		ScheduleDDSweepReportTaskWizard, DefensibleDisposalSchedule, TaskErrorDialog, TaskInformationDialog){

/**
 * @name ier.widget.layout.CommonActionsHandler
 * @class This class provides default implementation for many of the common actions, invoking the dialogs and model
 *        methods as needed.
 */
return dojo_declare("ier.widget.layout.CommonActionsHandler", [ecm_widget_layout_CommonActionsHandler], {
	/** @lends ecm.widget.layout.CommonActionsHandler.prototype */
	
	/**
	 * Shows the about dialog
	 */
	actionIERAbout: function(repository, items) {
		this.logEntry("actionAbout");
		var aboutDialog = new ier_dialog_AboutDialog();
		aboutDialog.startup();
		aboutDialog.show();
		ier_util_dialog.manage(aboutDialog);
		this.logExit("actionAbout");
	},
	
	/**
	 * Opens help information in a new window.
	 * 
	 * @param repository
	 *            Instance of {@link ecm.model.Repository}.
	 * @param items
	 */
	actionHelp: function(repository, items) {
		if(ecm.model.desktop.layout == "ier.widget.layout.IERMainLayout"){
			var left = 100;
			var top = 100;
			var width = 860;
			var height = 600;
			var url = ecm.model.desktop.helpUrl + "index.jsp?content=" + ecm.model.desktop.helpContext;
			window.open(url, "_blank", "status=yes,location=no,scrollbars=no,menubar=no,toolbar=no,personalbar=no,resizable=yes,left=" + left + ",top=" + top + ",width=" + width + ",height=" + height + "\"", true);
		}
		else {
			this.inherited(arguments);
		}
	},
	
	/**
	 * This refreshes the result set that an item or items are contained within. If no items are provided, it will
	 * refresh the repository. If no repository is provided, it will refresh the desktop.
	 * 
	 * @param isConfigure = If refreshing is invoked from the configure pane
	 */
	actionIERRefresh: function(repository, parentFolder, isConfigure, resultSet) {
		this.logEntry("actionIERRefresh");
		
		//for a taskResultSet, there is no repository or parent folder.
		//you just refresh the TaskResultSet
		if(resultSet && resultSet instanceof ecm.model.AsyncTaskResultSet){
			if(parentFolder){
				ecm.model.desktop.onChange(parentFolder);
				parentFolder.contentListResultSet = null;
			}
			
			resultSet.refresh();
			return;
		}
		
		if (parentFolder) {
			if(parentFolder instanceof Array && parentFolder.length > 0)
				parentFolder[0].refresh();
			else
				parentFolder.refresh();
		} else if(isConfigure){
			if(repository)
				repository.onConfigure(repository);
		}
		else if (repository) {
			repository.refresh();
		} else {
			ecm.model.desktop.refresh();
		}
		this.logExit("actionIERRefresh");
	},
	
	/**
	 * This invokes the IER properties dialog
	 * @param repository
	 * @param items
	 */
	actionIERProperties: function(repository, items){
		var item = items && items[0];
		if(item && (item.getEntityType || item.entityType)){
			
			var dialog = null;
			var entityType = (item.getEntityType) ? item.getEntityType() : item.entityType;
			switch(entityType){
			case ier_constants.EntityType_DispositionAction:
				dialog = new ier_dialog_ActionDialog();
				dialog.startup();
				dialog.show(repository, item);
				break;
			case ier_constants.EntityType_Hold:
				dialog = new ier_dialog_HoldDialog();
				dialog.startup();
				dialog.show(repository, item);
				break;
			case ier_constants.EntityType_Location:
				dialog = new ier_dialog_LocationDialog();
				dialog.startup();
				dialog.show(repository, item);
				break;
			case ier_constants.EntityType_DispositionTrigger:
				dialog = new ier_dialog_EventTriggerDialog();
				dialog.startup();
				dialog.show(repository, item);
				break;
			case ier_constants.EntityType_NamingPattern:
				dialog = new ier_dialog_NamingPatternDialog();
				dialog.startup();
				dialog.show(repository, item);
				break;
			case ier_constants.EntityType_DispositionSchedule:
				dialog = new ier_dialog_DispositionScheduleDialog();
				dialog.startup();
				dialog.show(repository, item);
				break;
			case ier_constants.EntityType_RecordCategory:			
				dialog = new ier_dialog_RecordCategoryPropertiesDialog();
				dialog.startup();
				dialog.show(repository, item.parent, item);
				break;
			case ier_constants.EntityType_ElectronicRecordFolder:
			case ier_constants.EntityType_HybridRecordFolder:
			case ier_constants.EntityType_PhysicalRecordFolder:
			case ier_constants.EntityType_PhysicalContainer:
				dialog = new ier_dialog_RecordFolderPropertiesDialog();
				dialog.startup();
				dialog.show(repository, item.parent, item);
				break;
			case ier_constants.EntityType_Volume:
				dialog = new ier_dialog_RecordVolumePropertiesDialog();
				dialog.startup();
				dialog.show(repository, item.parent, item);
				break;
			case ier_constants.EntityType_Record:
			case ier_constants.EntityType_ElectronicRecord:
			case ier_constants.EntityType_EmailRecord:
			case ier_constants.EntityType_PhysicalRecord:
			case ier_constants.EntityType_PDFRecord:
				dialog = new ier_dialog_RecordPropertiesDialog();
				dialog.startup();
				dialog.show(repository, item);
				break;
			case ier_constants.EntityType_FilePlan:
				dialog = new ier_dialog_FilePlanPropertiesDialog();
				dialog.startup();
				dialog.show(repository, item, true);
				break;
			case ier_constants.EntityType_ReportDefinition:
				dialog = new ier_dialog_ReportDefinitionDialog();
				dialog.startup();
				dialog.show(repository, item, false);
				break;
			default:
				break;
			}
			if(dialog){
				ier_util_dialog.manage(dialog);
			}
		}
	},

	/**
	 * This invokes the AddFilePlan dialog
	 * @param repository
	 */
	actionIERAddFilePlan: function(repository, parentFolders) {
		this.logEntry("actionIERAddFilePlan");

		var addFilePlanDialog = new ier_dialog_AddFilePlanDialog();
		addFilePlanDialog.startup();
		addFilePlanDialog.show(repository);
		ier_util_dialog.manage(addFilePlanDialog);

		this.logExit("actionIERAddFilePlan");
	},

	/**
	 * This invokes the AddFilePlan dialog for edit
	 * @param repository
	 */
	actionIEREditFilePlan: function(repository, items) {
		this.logEntry("actionIEREditFilePlan");
		var item = items && items[0];
		if(item){
			var dialog = new ier_dialog_FilePlanPropertiesDialog();
			dialog.startup();
			dialog.show(repository, item);
			ier_util_dialog.manage(dialog);
		}
		this.logExit("actionIEREditFilePlan");
	},

	/**
	 * This invokes the AddRecordCategory dialog
	 * @param repository
	 * @param parentFolders
	 */
	actionIERAddRecordCategory: function(repository, parentFolders) {
		this.logEntry("actionIERAddRecordCategory");

		var addCategoryDialog = new ier_dialog_AddRecordCategoryDialog();
		addCategoryDialog.startup();
		addCategoryDialog.show(repository, parentFolders[0]);
		ier_util_dialog.manage(addCategoryDialog);

		this.logExit("actionIERAddRecordCategory");
	},
	
	/**
	 * This invokes the AddRecordFolder dialog
	 * @param repository
	 * @param parentFolders
	 */
	actionIERAddRecordFolder: function(repository, parentFolders) {
		this.logEntry("actionIERAddRecordFolder");

		var dialog = new ier_dialog_AddRecordFolderDialog();
		dialog.startup();
		dialog.show(repository, parentFolders[0]);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddRecordFolder");
	},

	/**
	 * This invokes the AddRecordVolume dialog
	 * @param repository
	 * @param parentFolders
	 */
	actionIERAddRecordVolume: function(repository, parentFolders) {
		this.logEntry("actionIERAddRecordVolume");

		var dialog = new ier_dialog_AddRecordVolumeDialog();
		dialog.startup();
		dialog.show(repository, parentFolders[0]);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddRecordVolume");
	},
	

	/**
	 * This invokes the AddEventTrigger dialog
	 * @param repository
	 * @param parentFolders
	 */
	actionIERAddEventTrigger: function(repository, parentFolders) {
		this.logEntry("actionIERAddEventTrigger");

		var dialog = new ier_dialog_EventTriggerDialog();
		dialog.startup();
		dialog.show(repository);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddEventTrigger");
	},

	/**
	 * Perform the close action on a list of containers
	 * @param repository - repos where the containers are located
	 * @param items - a list of containers to close
	 */
	actionIERClose: function(repository, items, callback, teamspace, resultSet, parameterMap, action) {
		this.logEntry("actionIERClose");

		var closeRecordContainerDialog = new ier_dialog_CloseRecordContainerDialog();
		closeRecordContainerDialog.startup();
		
		//refresh the parent if the action originated from the FolderTree
		var widget = parameterMap ? parameterMap.widget : null;
		var shouldRefreshParent = widget instanceof ier.widget.FolderTree ? true : false;
		closeRecordContainerDialog.show(repository, items, shouldRefreshParent);
		ier_util_dialog.manage(closeRecordContainerDialog);

		this.logExit("actionIERClose");
	},
	
	/**
	 * Perform the link action on a record
	 * @param repository - repos where the record is located
	 * @param items - the record to link
	 */
	actionIERCreateLink: function(repository, items) {
		this.logEntry("actionIERCreateLink");

		var linkRecordDialog = new ier_dialog_LinkRecordDialog();
		linkRecordDialog.startup();
		linkRecordDialog.show(repository, items);
		ier_util_dialog.manage(linkRecordDialog);

		this.logExit("actionIERCreateLink");
	},

	/**
	 * Perform the reopen action on selected containers
	 * @param repository - repos where the containers are located
	 * @param items - a list of containers to reopen
	 */
	actionIERReopen: function(repository, items, callback, teamspace, resultSet, parameterMap, action) {
		this.logEntry("actionIERReopen");
		
		//refresh the parent if the action originated from the FolderTree
		var widget = parameterMap ? parameterMap.widget : null;
		var shouldRefreshParent = widget instanceof ier.widget.FolderTree ? true : false;
		
		if (items) {
			if (items instanceof Array) {
				for ( var i in items) {
					items[i].reopen(null, shouldRefreshParent);
				}
			}
			else
				items.reopen(null, shouldRefreshParent);			
		}
		this.logExit("actionIERReopen");
	},
	
	/**
	 * Perform the delete action on a list of containers.
	 * @param repository - repos where the containers are located
	 * @param items - a list of containers to delete
	 */
	actionIERDelete: function(repository, items) {
		this.logEntry("actionIERDelete");
		var message = ier_messages.delete_single_confirmation_question;
		if (items.length > 1) {
			message = dojo_string.substitute(ier_messages.delete_multiple_confirmation_question, [ items.length ]);
		}
		//Use the Nexus delete confirmation dialog.
		var confirmDelete = new ecm_widget_dialog_ConfirmationDialog({
			text: message,
			buttonLabel: ier_messages.delete_confirmation_button,
			onExecute: function() {
				// The delete service needs to know the number of items, 
				// a list of the items, and a list of the matching entity types.
				var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
					//TODO:  currently we only support a single item delete.
					//       When Nexus provides a bulk results screen, we will need
					//       to get the bulk results and process
					var successItemIds = response ? response.success : null;
					var successItems = [];
					for(var i in items){
						var p8Id = items[i].id.split(",")[2];
						if(successItems && successItemIds[p8Id]){
							items[i].deleted = true;
							successItems.push(items[i]);
						}
					}
					repository.onChange(successItems);
					// Remove items from favorites (as navigator does in actionDeleteItem())
					ecm.model.desktop.removeFavorites(dojo_array.map(successItems, ecm_model_Favorite.createFromItem));
				}));
				params.requestParams[ier_constants.Param_NumberOfDocuments] = items.length;
				for (var i in items) {
					params.requestParams[ier_constants.Param_DocId +i] = items[i].id;
					var type = (items[i].template == ier_constants.ClassName_ReportDefinition) ? items[i].entityType : items[i].getEntityType();
					params.requestParams[ier_constants.Param_EntityType +i] = type;
				}
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_Delete, ier_constants.PostEncoding, params);
			}
		});
		confirmDelete.startup();
		confirmDelete.show();		
		ier_util_dialog.manage(confirmDelete);
		this.logExit("actionIERDelete");
	},
	
	actionIERRunReport : function(repository, items) {
		this.logEntry("actionIERRunReport");
		if (repository && items) {
			if (items[0].id) { //id format: "RMReportDefintion,classGUID,itemGUID"
				var itemId = (items[0].id.indexOf(",") == -1) ? items[0].id : items[0].id.split(',')[2];
				ier_util.runReport(repository, itemId);
			}
		}		
		this.logExit("actionIERRunReport");
	},
	
	/**
	 * Perform the undeclare action on a list of containers.
	 * @param repository - repos where the records are located
	 * @param items - a list of records to undeclare
	 */
	actionIERUndeclare: function(repository, items) {
		this.logEntry("actionIERUndeclare");
		var message = ier_messages.undeclare_single_confirmation_question;
		if (items.length > 1) {
			message = dojo_string.substitute(ier_messages.undeclare_multiple_confirmation_question, [ items.length ]);
		}
		//Use the Nexus delete confirmation dialog.
		var confirmUndeclare = new ecm_widget_dialog_ConfirmationDialog({
			text: message,
			buttonLabel: ier_messages.undeclare_confirmation_button,
			onExecute: function() {
				// The undeclare service needs to know the number of items, 
				// a list of the items, and a list of the matching entity types.
				var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
					//TODO:  currently we only support a single item delete.
					//       When Nexus provides a bulk results screen, we will need
					//       to get the bulk results and process
					for(var i in items){
						items[i].deleted = true;
					}
					repository.onChange(items);
					// Remove items from favorites (as navigator does in actionDeleteItem())
					ecm.model.desktop.removeFavorites(dojo_array.map(items, ecm_model_Favorite.createFromItem));
				}));
				params.requestParams[ier_constants.Param_NumberOfDocuments] = items.length;
				for (var i in items) {
					params.requestParams[ier_constants.Param_DocId +i] = items[i].id;
				}
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_Undeclare, ier_constants.PostEncoding, params);
			}
		});
		confirmUndeclare.startup();
		confirmUndeclare.show();		
		ier_util_dialog.manage(confirmUndeclare);
		this.logExit("actionIERUndeclare");
	},
	

	/**
	 * Perform the relocate action on a list of containers
	 * @param repository - repos where the containers are located
	 * @param items - a list of containers to relocate
	 */
	actionIERRelocate: function(repository, items) {
		this.logEntry("actionIERRelocate");

		var relocateRecordContainerDialog = new ier_dialog_RelocateRecordContainerDialog();
		relocateRecordContainerDialog.startup();
		relocateRecordContainerDialog.show(repository, items);
		ier_util_dialog.manage(relocateRecordContainerDialog);

		this.logExit("actionIERRelocate");
	},

	/**
	 * Perform the file action on a record
	 * @param repository - repos where the record is located
	 * @param items - the record to file
	 */
	actionIERFile: function(repository, items) {
		this.logEntry("actionIERFile");

		var fileRecordDialog = new ier_dialog_FileRecordDialog();
		fileRecordDialog.startup();
		fileRecordDialog.show(repository, items);
		ier_util_dialog.manage(fileRecordDialog);

		this.logExit("actionIERFile");
	},

	/**
	 * Perform the copy action on a record
	 * @param repository - repos where the record is located
	 * @param items - the record to copy
	 */
	actionIERCopy: function(repository, items) {
		this.logEntry("actionIERCopy");

		var item = items && items[0];
		if(item) {			
			var entityType = (item.getEntityType) ? item.getEntityType() : item.entityType;
			switch(entityType){
			case ier_constants.EntityType_ReportDefinition:
				/*var serviceParams = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){
					this.actionIERRefresh(repository, null, true);
				}));				
				
				serviceParams.requestParams[ier_constants.Param_NumberOfDocuments] = items.length;
				for (var i in items) {
					serviceParams.requestParams[ier_constants.Param_DocId +i] = items[i].id;
					serviceParams.requestParams[ier_constants.Param_EntityType +i] = entityType;
				}

				// Call the copy service
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, 
												ier_constants.Service_Copy, 
												ier_constants.PostEncoding, 
												serviceParams);*/
				
				dialog = new ier_dialog_ReportDefinitionDialog();
				dialog.startup();
				dialog.show(repository, item, true);
				ier_util_dialog.manage(dialog);

				break;
			//TODO currently there are only two entities that do the copy, ReportDefinition and Record. Here default to
			// Record. If more entities do the copy, add/revise the code here.
			default:
				var copyRecordDialog = new ier_dialog_CopyRecordDialog();
				copyRecordDialog.startup();
				copyRecordDialog.show(repository, items);
				ier_util_dialog.manage(copyRecordDialog);
				break;
			}
		}

		this.logExit("actionIERCopy");
	},

	/**
	 * Perform the place on hold action on holdables.
	 * @param repository - repos where the holdables are located
	 * @param items - the holdables to place on hold
	 */
	actionIERPlaceOnHold: function(repository, items){
		this.logEntry("actionIERPlaceOnHold");

		var dialog = new ier_dialog_PlaceOnHoldDialog();
		dialog.startup();
		dialog.show(repository, items);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERPlaceOnHold");
	},

	/**
	 * Perform the remove hold action on holdables.
	 * @param repository - repos where the holdables are located
	 * @param items - the holdables to place on hold
	 */
	actionIERRemoveHold: function(repository, items){
		this.logEntry("actionIERRemoveHold");

		var dialog = new ier_dialog_RemoveHoldDialog();
		dialog.startup();
		dialog.show(repository, items);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERRemoveHold");
	},

	actionIERRemoveHoldOnViewEntities: function(repository, items){
		this.logEntry("actionIERRemoveHoldOnViewEntities");
		var message = ier_messages.remove_hold_single_confirmation_question;
		if (items.length > 1) {
			message = dojo_string.substitute(ier_messages.remove_hold_multiple_confirmation_question, [ items.length ]);
		}
		//Use the Nexus delete confirmation dialog.
		var confirmRemoveHold = new ecm_widget_dialog_ConfirmationDialog({
			text: message,
			buttonLabel: ier_messages.remove_hold_confirmation_button,
			onExecute: function() {
				var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){
					dojo_lang.isFunction(ecm.model.desktop.ierHoldIsRemoved) && ecm.model.desktop.ierHoldIsRemoved(items);
				}));
				var holdables = dojo_array.map(items, function(item){
					return {id: item.id, entityType: item.getEntityType()};
				});
				params.requestParams[ier_constants.Param_Holdables] = dojo_json.toJson(holdables);
				var hold = {id: items[0].removeHold};
				params.requestParams[ier_constants.Param_Holds] = dojo_json.toJson([hold]);
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_RemoveHold, ier_constants.PostEncoding, params);
			}
		});
		confirmRemoveHold.startup();
		confirmRemoveHold.show();		
		ier_util_dialog.manage(confirmRemoveHold);
		this.logExit("actionIERRemoveHoldOnViewEntities");
	},

	actionIERMove: function(repository, items){
		this.logEntry("actionIERMove");

		var moveRecordDialog = new ier_dialog_MoveRecordDialog();
		moveRecordDialog.startup();
		moveRecordDialog.show(repository, items);
		ier_util_dialog.manage(moveRecordDialog);

		this.logExit("actionIERMove");
		
	},

	/**
	 * This invokes the AddHold dialog
	 * @param repository
	 */
	actionIERAddHold: function(repository) {
		this.logEntry("actionIERAddHold");

		var dialog = new ier_dialog_AddHoldDialog();
		dialog.startup();
		dialog.show(repository);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddHold");
	},

	/**
	 * This invokes the ViewEntitiesonHold dialog
	 * @param repository
	 * @param items - hold
	 */
	actionIERViewEntitiesOnHoldDialog: function(repository, items) {
		this.logEntry("actionIERViewEntitiesOnHoldDialog");

		var dialog = new ier_dialog_ViewEntitiesOnHoldDialog();
		dialog.startup();
		dialog.show(repository, items);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERViewEntitiesOnHoldDialog");
	},

	/**
	 * This invokes the Initiate_Remove_Hold_Request
	 * @param repository
	 * @param items - hold
	 */
	actionIERInitiateRemoveHoldRequest: function(repository, items) {
		this.logEntry("actionIERInitiateRemoveHoldRequest");

		var message = ier_messages.initiate_removeHold_request_confirmation_question;

		var confirmdialog = new ecm_widget_dialog_ConfirmationDialog({
			text: message,
			buttonLabel: ier_messages.initiate_removeHold_request_confirmation_button,
			onExecute: function() {
				var item = items[0];
				var holdId = item.id;
				var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){
					item.retrieveAttributes(dojo_lang.hitch(this, function(){
						item.refresh();
					}), false, true);
				}));
				params.requestParams[ier_constants.Param_HoldId] = holdId;
				params.requestParams[ier_constants.Param_Dynamic_Hold_Task] = "initiate_remove_hold_request";
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_DynamicHoldRequstService, ier_constants.PostEncoding, params);
			}
		});
		confirmdialog.startup();
		confirmdialog.show();
		ier_util_dialog.manage(confirmdialog);

		this.logExit("actionIERInitiateRemoveHoldRequest");
	},

	/**
	 * This invokes the Cancel_Remove_Hold_Request
	 * @param repository
	 * @param items - hold
	 */
	actionIERCancelRemoveHoldRequest: function(repository, items) {
		this.logEntry("actionIERCancelRemoveHoldRequest");

		var message = ier_messages.cancel_removeHold_request_confirmation_question;

		var confirmdialog = new ecm_widget_dialog_ConfirmationDialog({
			text: message,
			buttonLabel: ier_messages.cancel_removeHold_request_confirmation_button,
			onExecute: function() {
				var item = items[0];
				var holdId = item.id;
				var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){
					item.retrieveAttributes(dojo_lang.hitch(this, function(){
						item.refresh();
					}), false, true);
				}));
				params.requestParams[ier_constants.Param_HoldId] = holdId;
				params.requestParams[ier_constants.Param_Dynamic_Hold_Task] = "cancel_remove_hold_request";
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_DynamicHoldRequstService, ier_constants.PostEncoding, params);
			}
		});
		confirmdialog.startup();
		confirmdialog.show();
		ier_util_dialog.manage(confirmdialog);

		this.logExit("actionIERCancelRemoveHoldRequest");
	},

	/**
	 * This invokes the Activate Hold Sweep Processing
	 * @param repository
	 * @param items - hold
	 */
	actionIERActivateHoldSweepProcessing: function(repository, items) {
		this.logEntry("actionIERActivateHoldSweepProcessing");

		var message = ier_messages.activate_sweep_hold_processing_confirmation_question;

		var confirmdialog = new ecm_widget_dialog_ConfirmationDialog({
			text: message,
			buttonLabel: ier_messages.activate_sweep_hold_processing_confirmation_button,
			onExecute: function() {
				var item = items[0];
				var holdId = item.id;
				var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){
					item.retrieveAttributes(dojo_lang.hitch(this, function(){
						item.refresh();
					}), false, true);
				}));
				params.requestParams[ier_constants.Param_HoldId] = holdId;
				params.requestParams[ier_constants.Param_Dynamic_Hold_Task] = "activate_sweep_hold_processing";
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_DynamicHoldRequstService, ier_constants.PostEncoding, params);
			}
		});
		confirmdialog.startup();
		confirmdialog.show();
		ier_util_dialog.manage(confirmdialog);

		this.logExit("actionIERActivateHoldSweepProcessing");
	},

	/**
	 * This invokes the AddLocation dialog
	 * @param repository
	 */
	actionIERAddLocation: function(repository) {
		this.logEntry("actionIERAddLocation");

		var dialog = new ier_dialog_AddLocationDialog();
		dialog.startup();
		dialog.show(repository);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddLocation");
	},

	/**
	 * This invokes the AddNamingPattern dialog
	 * @param repository
	 */
	actionIERAddNamingPattern: function(repository) {
		this.logEntry("actionIERAddNamingPattern");

		var dialog = new ier_dialog_AddNamingPatternDialog();
		dialog.startup();
		dialog.show(repository);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddNamingPattern");
	},

	/**
	 * This invokes the AddDispositonSchedule dialog
	 * @param repository
	 */
	actionIERAddDispositionSchedule: function(repository) {
		this.logEntry("actionIERAddDispositionSchedule");

		var dialog = new ier_dialog_AddDispositionScheduleDialog();
		dialog.startup();
		dialog.show(repository);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddDispositionSchedule");
	},
	
	/**
	 * This invokes the AddReportDefinition dialog
	 * @param repository
	 */
	actionIERAddReportDefinition: function(repository) {
		this.logEntry("actionIERAddReportDefinition");

		var dialog = new ier_dialog_AddReportDefinitionDialog();
		dialog.startup();
		dialog.show(repository);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddReportDefinition");
	},
	
	/**
	 * This invokes the AddDispositonSchedule dialog
	 * @param repository
	 */
	actionIERAddDispositionAction: function(repository) {
		this.logEntry("actionIERAddDispositionAction");

		var dialog = new ier_dialog_AddActionDialog();
		dialog.startup();
		dialog.show(repository);
		ier_util_dialog.manage(dialog);

		this.logExit("actionIERAddDispositionAction");
	},

	actionOpenICNDocumentProperty: function(doc){
		var docObjectStoreId = doc.documentObjectStoreId;
		var docId = doc.docId;
		var connectionURL = doc.ceEJBURL;
		var docRepo = ier_util.getRepository(docObjectStoreId, connectionURL);
		if(docRepo != null){
			docRepo.retrieveItem(docId, dojo_lang.hitch(this, function(itemRetrieved){
				var dialog = new ecm_widget_dialog_EditPropertiesDialog();
				dialog.startup();
				dialog.show(itemRetrieved);
				ier_util_dialog.manage(dialog);
			}));
		}
	},

	actionIERViewDocumentInfo: function(repository, items) {
		var item = items[0];
		var recordId = item.id;

		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){
			if(response != null && !response.error){
				var documents = response.documents;
				if(documents.length == 1){
					var doc = documents[0];
					this.actionOpenICNDocumentProperty(doc);
				}else{
					this._actionSelectOneOfDocumentDialog(false, documents, recordId);  // false: show document info
				}
			}else{
				var errorType = response.error;
				if(errorType != null && errorType == "noDocument"){
					var message = ier_messages.noDocumentRecord;
					this._messageDialog && this._messageDialog.destroy();
					this._messageDialog = new ecm_dialog_MessageDialog({
						text: message
					});
					this._messageDialog.startup();
					this._messageDialog.show();
					ier_util_dialog.manage(this._messageDialog);
				}
			}
		}));
		params.requestParams[ier_constants.Param_RecordId] = recordId;
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetDocumentInfo, ier_constants.PostEncoding, params);
	},

	_actionSelectOneOfDocumentDialog: function(viewContent, documents, recordId){ // true: show content, false: show document information
		var dialogRoot = dojo_construct.create("div", {className: "selectDocDialog"});
		var check = true;
		for(var i=0; i<documents.length; i++){
			doc = documents[i];
			var radioDiv = dojo_construct.create("div", {className: "selectDocRadio"}, dialogRoot);

			var idDoc = doc.docId + "_radio_document";
			new RadioButton({
				checked: check,
				name: recordId + "_selectDocument",
				value: i.toString()
			}, idDoc).placeAt(radioDiv);
			check = false;

			dojo_construct.create("label", {
				"for": idDoc,
				innerHTML: doc.docName
			}, radioDiv);
		}
		var buttonArea = dojo_construct.create("div", {className: "selectDocDialogButtonArea"},dialogRoot);
		var okButton = new Button({ label:"OK"}).placeAt(buttonArea);
		var cancelButton = new Button({ label:"Cancel"}).placeAt(buttonArea);
		okButton.on("click", dojo_lang.hitch(this, function(e){
			var radioList = document.getElementsByName(recordId + "_selectDocument");
			var checkedId = null;
			for(var i=0; i<radioList.length; i++){
				if(radioList[i].checked) {
					checkedId = radioList[i].value;
					break;
				}
			}
			checkedId && viewContent ? this.actionOpenICNRecordContent(documents[checkedId - 0]) : this.actionOpenICNDocumentProperty(documents[checkedId - 0]);

			this._docSelectdialog.hide();
		}));
		cancelButton.on("click", dojo_lang.hitch(this, function(e){
			this._docSelectdialog.hide();
		}));
		this._docSelectdialog && this._docSelectdialog.destroy();
		this._docSelectdialog = new Dialog({
			title: ier_messages.selectDocument,
			content: dialogRoot
		});
		this._docSelectdialog.show();
		ier_util_dialog.manage(this._docSelectdialog);
	},

	actionOpenICNRecordContent: function(doc){
		var docObjectStoreId = doc.documentObjectStoreId;
		var docId = doc.docId;
		var connectionURL = doc.ceEJBURL;
		var docRepo = ier_util.getRepository(docObjectStoreId, connectionURL);
		if(docRepo != null){
			docRepo.retrieveItem(docId, dojo_lang.hitch(this, function(itemRetrieved){
				this.actionView(docRepo, [itemRetrieved]);
			}));
			
		}
	},

	actionIEROpenRecordContent: function(repository, items) {
		var item = items[0];
		var recordId = item.id;
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){
			if(response != null && !response.error){
				var documents = response.documents;
				if(documents.length == 1){
					var doc = documents[0];
					this.actionOpenICNRecordContent(doc);
				}else{
					this._actionSelectOneOfDocumentDialog(true, documents, recordId);  // true: show content
				}
			}else{
				var errorType = response.error;
				if(errorType != null && errorType == "noDocument"){
					var message = ier_messages.noDocumentRecord;
					if(this._messageDialog) {
						this._messageDialog.destroy();
					}
					this._messageDialog = new ecm_dialog_MessageDialog({
						text: message
					});
					this._messageDialog.startup();
					this._messageDialog.show();
					ier_util_dialog.manage(this._messageDialog);
				}
			}
		}));
		params.requestParams[ier_constants.Param_RecordId] = recordId;
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetDocumentInfo, ier_constants.PostEncoding, params);
	},

	/**
	 * Deletes a given task
	 */
	actionTaskDelete: function(repository, items, callback, teamspace, resultSet, parameterMap){
		if(items){
			var message = ier_messages.delete_single_confirmation_question;
			if (items.length > 1) {
				message = dojo_string.substitute(ier_messages.delete_multiple_confirmation_question, [ items.length ]);
			}
			//Use the Nexus delete confirmation dialog.
			var confirmDelete = new ecm_widget_dialog_ConfirmationDialog({
				text: message,
				buttonLabel: ier_messages.delete_confirmation_button,
				onExecute: function() {
					for(var i in items){
						var item = items[i];
						if(item instanceof ecm.model.AsyncTask){
							item.deleteTask();
						}
					}
				}
			});
			confirmDelete.startup();
			confirmDelete.show();		
			ier_util_dialog.manage(confirmDelete);
		}
	},
	
	/**
	 * Refreshes a  given task
	 */
	actionTaskRefresh: function(repository, itemList, callback, teamspace, resultSet, parameterMap){
		if(itemList){
			var item = itemList[0];
			if(item instanceof ecm.model.AsyncTask){
				item.detailsLoaded = false;
				item.recurringTaskInstances = null;

				item.getDetails(dojo_lang.hitch(this, function(){
					item.onChange([item]);
				}));
			}
		}
	},
	
	actionTaskDisable: function(repository, itemList, callback, teamspace, resultSet, parameterMap){
		if(itemList){
			var item = itemList[0];
			if(item instanceof ecm.model.AsyncTask && item.isTaskRecurring()){
				item.getDetails(dojo_lang.hitch(this, function(){
					if(item.attributes[ier_constants.Attribute_Status] == ier_constants.TaskStatus_Scheduled)
						item.pause();
					else {
						var messageDialog = new ecm_dialog_MessageDialog({
							text: ier_messages.taskPane_unableToDisable
						});
						messageDialog.startup();
						messageDialog.show();
						ier_util_dialog.manage(messageDialog);
					}
					item.onChange([item]);
				}), true);
				
			}
		}
	},
	
	actionTaskEnable: function(repository, itemList, callback, teamspace, resultSet, parameterMap){
		if(itemList){
			var item = itemList[0];
			if(item instanceof ecm.model.AsyncTask && item.isTaskRecurring()){
				item.getDetails(dojo_lang.hitch(this, function(){
					if(item.attributes[ier_constants.Attribute_Status] == ier_constants.TaskStatus_Paused){
						item.reschedule(dojo_lang.hitch(this, function(){
							item.refresh();
							if(callback)
								callback();
						}));
					}	
					else {
						var messageDialog = new ecm_dialog_MessageDialog({
							text: ier_messages.taskPane_unableToEnable
						});
						messageDialog.startup();
						messageDialog.show();
						ier_util_dialog.manage(messageDialog);
					}
					item.onChange([item]);
				}), true);
			}
		}
	},
	
	actionTaskModify: function(repository, itemList, callback, teamspace, resultSet, parameterMap){
		if(itemList){
			var item = itemList[0];
			if(item instanceof ecm.model.AsyncTask || item instanceof ecm.model.AsyncTaskInstance){								
				if(item.attributes.type == ier_constants.TaskType_ReportClass){
					item.getDetails(dojo_lang.hitch(this, function(){
						var reportDialog = new ier_widget_ReScheduleReportWizardDialog({
							data: item.taskRequest,
							task: item
						});
						reportDialog.startup();
						reportDialog.show();
						ier_util_dialog.manage(reportDialog);
						item.onChange([item]);
					}));
				}
				else if(item.attributes.type == ier_constants.TaskType_DDConversionClass){
					item.getDetails(dojo_lang.hitch(this, function(){
						var dialog = new ScheduleDDContainerConversionWizard({
							task: item
						});
						dialog.set("taskSchedule", ier_util.createTaskSchedule(item));
						
						var retentionPeriod = item.taskRequest.specificTaskRequest[ier_constants.Param_RetentionPeriod];
						if(!retentionPeriod){
							retentionPeriod = item.taskRequest.specificTaskRequest[ier_constants.Param_RetentionPeriodYears] + "-" + item.taskRequest.specificTaskRequest[ier_constants.Param_RetentionPeriodMonths] +
								"-" + item.taskRequest.specificTaskRequest[ier_constants.Param_RetentionPeriodDays];
						}
						var defensibleDisposalSchedule = new DefensibleDisposalSchedule({
							"retentionTriggerPropertyName": item.taskRequest.specificTaskRequest[ier_constants.Param_RetentionTriggerPropertyName],
							"retentionPeriod": retentionPeriod
						});
						
						var p8repositoryId = item.taskRequest.specificTaskRequest[ier_constants.Param_P8RepositoryId];
						var connectionURL = item.taskRequest.specificTaskRequest["ceEJBURL"];
						var containerId = item.taskRequest.specificTaskRequest[ier_constants.Param_ContainerId];
						var repository = ier_util.getRepository(p8repositoryId, connectionURL);
						try {
							repository.retrieveItem(containerId, dojo_lang.hitch(this, function(itemRetrieved){
								dialog.startup();
								dialog.show(repository, itemRetrieved);
								ier_util_dialog.manage(dialog);
								
								dialog.set("schedule", defensibleDisposalSchedule);
							}));
						}
						catch(e){
							dialog.startup();
							dialog.show(repository);
							ier_util_dialog.manage(dialog);
							
							dialog.set("schedule", defensibleDisposalSchedule);
						}
						item.onChange([item]);
					}));
				}
				else if(item.attributes.type == ier_constants.TaskType_DDReportSweepClass){
					item.getDetails(dojo_lang.hitch(this, function(){
						var dialog = new ScheduleDDSweepReportTaskWizard({
							task: item
						});
						
						var p8repositoryId = item.taskRequest.specificTaskRequest[ier_constants.Param_P8RepositoryId];
						var connectionURL = item.taskRequest.specificTaskRequest["ceEJBURL"];
						var repository = ier_util.getRepository(p8repositoryId, connectionURL);
						
						var properties = {
							repository: repository,
							containerIds: item.taskRequest.specificTaskRequest[ier_constants.Param_ContainerId],
							advancedDays: item.taskRequest.specificTaskRequest["advancedDays"],
							reportOnly: item.taskRequest.specificTaskRequest["reportOnly"],
							connectionPoint: item.taskRequest.specificTaskRequest["connectionPointName"],
							needApproval: item.taskRequest.specificTaskRequest["needApproval"],
							containerToDeclareRecordId : item.taskRequest.specificTaskRequest["containerToDeclareRecordTo"],
							defensibleDisposalWorkflowId: item.taskRequest.specificTaskRequest["defensibleDisposalWorkflowId"]
						};
						
						dialog.startup();
						dialog.show(repository);
						
						dialog.set("properties", properties);
						dialog.set("schedule", ier_util.createTaskSchedule(item));

						ier_util_dialog.manage(dialog);
						item.onChange([item]);
					}));
				}
			}
		}
	},
	
	actionTaskDownload: function(repository, itemList, callback, teamspace, resultSet, parameterMap){
		if(itemList){
			var item = itemList[0];
			if(item instanceof ecm.model.AsyncTask || item instanceof ecm.model.AsyncTaskInstance){								
				if(item instanceof ecm.model.AsyncTask){
					item.getDetails(dojo_lang.hitch(this, function(){
						this._actionTaskDownload(item, callback);
						item.onChange([item]);
					}));
				} else {
					this._actionTaskDownload(item, callback);
				}
			}
		}
	},
	
	_actionTaskDownload: function(item, callback){
		if(item.results && item.results.reportResultDocumentId){
			var recordRepository = ier_util.getRepository(item.results.reportResultRepositorySymbolicName, item.taskRequest[ier_constants.Param_CE_EJB_URL]);
			if(recordRepository == null){
				var dialog = new ecm_dialog_MessageDialog({
					text: dojo_string.substitute(ier_messages.fileplanRepositoryNotAvailable, [ item.results.reportResultRepositorySymbolicName ])
				});
				dialog.startup();
				dialog.show();
				ier_util_dialog.manage(dialog);
			} else {
				recordRepository.retrieveItem(item.results.reportResultDocumentId, dojo_lang.hitch(this, function(itemRetrieved){
					ecm.model.desktop.getActionsHandler(dojo_lang.hitch(this, function(actionsHandler) {

						if (actionsHandler) {
							actionsHandler["actionDownload"](recordRepository, [itemRetrieved], callback, null, itemRetrieved.resultSet );											
						}
					}));
				}));
			}
		}
	},
	
	actionTaskOpen: function(repository, itemList, callback, teamspace, resultSet, parameterMap, noRefresh){
		if(itemList){
			var item = itemList[0];
			if(item instanceof ecm.model.AsyncTask || item instanceof ecm.model.AsyncTaskInstance){
				if (this._itemInProgress != item){
					this._itemInProgress = item;
					
					if(item instanceof ecm.model.AsyncTask && item.isTaskRecurring()){
						item.retrieveAsyncTaskInstances(dojo_lang.hitch(this, function(resultSet){
							this._itemInProgress = null;
							if(callback){
								callback(item, resultSet);
							} else {
								ecm.model.desktop.taskManager.onAsyncTaskItemOpened(item, resultSet);
							}
						}));
					}
					else {
						this._itemInProgress = null;
						
						if(item){
							if(item instanceof ecm.model.AsyncTask){
								item.getDetails(dojo_lang.hitch(this, function(){
									this._openTask(item, callback);
									if(!noRefresh)
										item.onChange([item]);
								}));
							} else {
								this._openTask(item, callback);
							}
						}
						else {
							if(callback){
								callback(item, resultSet);
							} else 
								ecm.model.desktop.taskManager.onAsyncTaskItemOpened(item, resultSet);
						}
					}
				}
			}
		}
	},
	
	_openTask: function(item, callback){
		var handlerClass = item.attributes[ier_constants.Attribute_Type];
		if(handlerClass == ier_constants.TaskType_ReportClass && item.results && item.results.reportResultDocumentId){
			var recordRepository = ier_util.getRepository(item.results.reportResultRepositorySymbolicName, item.results.reportResultRepositoryServer);
			recordRepository.retrieveItem(item.results.reportResultDocumentId, dojo_lang.hitch(this, function(itemRetrieved){
				ecm.model.desktop.getActionsHandler(dojo_lang.hitch(this, function(actionsHandler) {
					if (actionsHandler) {
						actionsHandler["actionDownloadAll"](recordRepository, [itemRetrieved], callback);
					}
				}));
			}));
		} else if(handlerClass == ier_constants.TaskType_DDConversionClass && item.results) {
			var repository = ier_util.getRepository(item.results.ier_p8RepositoryId, item.results.ceEJBURL);
			repository.retrieveItem(item.results[ier_constants.Param_ContainerId], dojo_lang.hitch(this, function(itemRetrieved){
				this.actionIERProperties(repository, [itemRetrieved]);
			}));
		}
		else if(handlerClass == ier_constants.TaskType_DDReportSweepClass && item.results) {
			var dialog = new TaskInformationDialog({
				item: item
			});
			dialog.show();
			ier_util_dialog.manage(dialog);
		}
		else if(item.errors && item.errors.length){
			var dialog = new TaskErrorDialog({
				item: item
			});
			dialog.show();
			ier_util_dialog.manage(dialog);
		}
	},
	
	/**
	 * Invokes the schedule disposition sweep dialog
	 */
	actionIERScheduleDispositionSweep: function(repository, itemList, callback, teamspace, resultSet, parameterMap){
		
	},
	
	/**
	 * Invokes the schedule hold sweep dialog
	 */
	actionIERScheduleHoldSweep: function(repository, itemList, callback, teamspace, resultSet, parameterMap) {
		
	},
	
	/**
	 * Invokes the schedule report dialog
	 */
	actionIERScheduleReport: function(repository, itemList, callback, teamspace, resultSet, parameterMap) {
		var reportDialog = new ier_widget_ScheduleReportWizardDialog();
		reportDialog.startup();
		reportDialog.show();
		ier_util_dialog.manage(reportDialog);
	},
	
	/**
	 * Override the ICN's action open 
	 */
	actionOpen: function(item, callback){
		if(item){
			if(item instanceof ier.model.CategoryTask){
				ecm.model.desktop.taskManager.onAsyncTaskItemOpened(item);
			}
			else {
				this.inherited(arguments);
			}
		}
		else {
			this.inherited(arguments);
		}
	},
	
	/**
	 * View record properties from a document declared as a record in IBM Conent Navigator
	 */
	actionRecordProperties: function(repository, itemList, callback, teamspace, resultSet, parameterMap){
		if(repository && itemList && itemList.length > 0){
			var item = itemList[0];
			if(item){
				var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
					if(response.recordId && response.fileplanObjectStore){
						var recordRepository = ecm.model.desktop.getRepositoryOfObjectStore(response.fileplanObjectStore);
						if(recordRepository == null){
							var dialog = new ecm_dialog_MessageDialog({
								text: dojo_string.substitute(ier_messages.fileplanRepositoryNotAvailable, [ response.fileplanObjectStore ])
							});
							dialog.startup();
							dialog.show();
							ier_util_dialog.manage(dialog);
						} else {
							var recordPropertiesDialog = new ier_dialog_RecordPropertiesDialog({
								isReadOnly : true,
								showOnlyProperties: true
							});
							recordRepository.retrieveItem(response.recordId, dojo_lang.hitch(this, function(itemRetrieved){
								recordPropertiesDialog.startup();
								recordPropertiesDialog.show(recordRepository, itemRetrieved);
								ier_util_dialog.manage(recordPropertiesDialog);
							}));
						}
					}
				}));
				params.requestParams[ier_constants.Param_DocId] = item.id;
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetRecordFromDeclaredDocument, ier_constants.PostEncoding, params);
			}
		}
	},
	
	/**
	 * The action to do a manual declare.  This is invoked from Nexus
	 * @param repository
	 * @param items
	 **/
	actionIERDeclare: function(repository, items, callback, workspace, resultSet, parameterMap, actionJS){
		if(isCompatitibleIERVersion(dojo_lang.hitch(this, function(){
			var parentFolder = null;
			if (items && items.length > 0) {
				if (!items[0].isFolder()) {
					parentFolder = items[0].parent;
				} else {
					parentFolder = items[0];
				}
			}
			var declareDialog = new ier.widget.dialog.DeclareRecordDialog();
			declareDialog.startup();
			declareDialog.show(repository, items, parentFolder);
			ier_util_dialog.manage(declareDialog);
		})));
	},
	
	actionConvertToDDContainer: function(repository, items, callback, workspace, resultSet, parameterMap, actionJS){
		this.logEntry("actionConvertToDDContainer");

		var dialog = new ScheduleDDContainerConversionWizard();
		var item = items && items[0] ? items[0] : null;
		dialog.startup();
		dialog.show(repository, item);
		ier_util_dialog.manage(dialog);

		this.logExit("actionConvertToDDContainer");
	},
	
	actionScheduleDDSweepReportTask: function(repository, items, callback, workspace, resultSet, parameterMap, actionJS){
		this.logEntry("actionScheduleDDSweepReportTaskWizard");
		var dialog = new ScheduleDDSweepReportTaskWizard();
		var item = items && items[0] ? items[0] : null;
		dialog.show(repository, item);
		ier_util_dialog.manage(dialog);

		this.logExit("actionScheduleDDSweepReportTaskWizard");
	},
	
	actionIERInitiateDisposition: function(repository, items, callback, workspace, resultSet, parameterMap, actionJS){
		var ids = [];
		var entityTypes = [];
		for(var i in items){
			var item = items[i];
			if(item instanceof ier.model._BaseEntityObject){
				ids.push(ier_util.getGuidId(item.id));
				entityTypes.push(item.getEntityType());
			}
		}
		
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
			if(response.message){
				var messageDialog = new ecm_dialog_MessageDialog({
					text: response.message,
					style: "width: 50%;"
				});
				messageDialog.startup();
				messageDialog.show();
				ier_util_dialog.manage(messageDialog);;
			}
			dojo_array.forEach(items, function(item){
				item.retrieveAttributes(null, true, true); 
			});
		}));
		
		params.requestParams[ier_constants.Param_NumberOfDocuments] = items.length;
		params.requestParams[ier_constants.Param_Id] = ier_util.arrayToString(ids);
		params.requestParams[ier_constants.Param_EntityType] = ier_util.arrayToString(entityTypes);
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, "ierInitiateDispositionService", ier_constants.PostEncoding, params);
	}
    });
});
