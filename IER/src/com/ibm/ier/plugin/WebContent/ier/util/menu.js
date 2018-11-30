define([
	"dojo/_base/declare",
	"dojo/dom-style",
	"ecm/LoggerMixin",
	"ecm/model/AsyncTaskInstance",
	"ier/constants",
	"ier/model/_BaseEntityObject"
], function(dojo_declare, dojo_style, ecm_LoggerMixin, ecm_model_AsyncTaskInstance, ier_constants, ier_model_BaseEntityObject){

var _MenuUtil = dojo_declare("ier.util._MenuUtil", [ecm_LoggerMixin], {
	
	/**
	 * Obtain the correct context menu type based on entity type
	 * @param items
	 * @returns {String}
	 */
	getContainersAndRecordsContextMenuType: function(items, browse){
		var contextMenuType = null;
		if(items.length == 1){
			var item = items[0];
			if(item instanceof ier_model_BaseEntityObject){
				var entityType = item.getEntityType();
				if(item.isFolder()){
					if(item.parent && item.parent.isIERHoldCondition){
						contextMenuType = ier_constants.MenuType_IERHoldConditionContainerContextMenu;
					}else{
						contextMenuType = this.getContainerContextMenuType(item, browse);
					}
				}else if(entityType == ier_constants.EntityType_ElectronicRecord || entityType == ier_constants.EntityType_EmailRecord || entityType == ier_constants.EntityType_PDFRecord ||
					entityType == ier_constants.EntityType_PhysicalRecord) {
					if(item.parent && item.parent.isIERHoldCondition){
						contextMenuType = ier_constants.MenuType_IERHoldConditionRecordContextMenu;
					}else{
						contextMenuType = this.getRecordContextMenuType(item);
					}
				}else if(entityType == ier_constants.EntityType_Hold){
					if(item.inPlaceOnHold){
						contextMenuType = ier_constants.MenuType_IERPlaceOnHoldContextMenu;
					}else if(item.inRemoveHold){
						contextMenuType = ier_constants.MenuType_IERRemoveHoldContextMenu;
					}else{
						contextMenuType = ier_constants.MenuType_IERHoldsConfigureContextMenu;
					}
				}else if(entityType == ier_constants.EntityType_Location || entityType == ier_constants.EntityType_NamingPattern ||
						entityType == ier_constants.EntityType_TransferMapping || entityType == ier_constants.EntityType_RecordType ||
						entityType == ier_constants.EntityType_ReportDefinition)
					contextMenuType = ier_constants.MenuType_IERCustomObjectsContextMenu;
				else if(entityType == ier_constants.EntityType_DispositionSchedule || entityType == ier_constants.EntityType_DispositionAction ||
						entityType == ier_constants.EntityType_DispositionTrigger || item.template == ier_constants.ClassName_WorkflowDefinition)
					contextMenuType = ier_constants.MenuType_IERPropertiesOnlyCustomObjectsContextMenu;
			} 
			else {
				var entityType = item.entityType;
				if (entityType && entityType == ier_constants.EntityType_ReportDefinition)
					contextMenuType = ier_constants.MenuType_IERReportDefinitionsConfigureContextMenu;				
			} 
		}else{
			if(items.length > 1 && items[0] instanceof ier_model_BaseEntityObject){
				var entityType = items[0].getEntityType();
				if(entityType == ier_constants.EntityType_Hold){
					contextMenuType = ier_constants.MenuType_IERHoldsConfigureContextMenu;
				}
				else if(entityType == ier_constants.EntityType_Location || entityType == ier_constants.EntityType_NamingPattern ||
						entityType == ier_constants.EntityType_TransferMapping || entityType == ier_constants.EntityType_RecordType ||
						entityType == ier_constants.EntityType_ReportDefinition)
					contextMenuType = ier_constants.MenuType_IERMultipleCustomObjectsContextMenu;
				else if(entityType == ier_constants.EntityType_DispositionSchedule || entityType == ier_constants.EntityType_DispositionAction ||
						entityType == ier_constants.EntityType_DispositionTrigger)
					contextMenuType = ier_constants.MenuType_IERPropertiesOnlyCustomObjectsContextMenu;
				else{

					var folderFound = false;
					var recordFound = false;
					for ( var i in items) {
						if (entityType && items[i].getEntityType() != entityType) {
							entityType = null; // mixed type
						}
						if (items[i].isFolder()) {
							folderFound = true;
						}
						else {
							recordFound = true;
						}
					}

					if(folderFound && recordFound){
						contextMenuType = ier_constants.MenuType_IERMultipleContainerAndRecordContextMenu;
					}else if(folderFound && !recordFound){
						if (entityType) {
							contextMenuType = this.getContainerContextMenuType(items[0], browse);
						} else {
							contextMenuType = ier_constants.MenuType_IERMultipleContainerContextMenu;
						}
					}else{
						if (entityType) {
							contextMenuType = this.getRecordContextMenuType(items[0]);
						} else {
							contextMenuType = ier_constants.MenuType_IERMultipleRecordContextMenu;
						}
					}
				}
			}
			else if (items.length > 1) {
				var entityType = items[0].entityType;
				if (entityType && entityType == ier_constants.EntityType_ReportDefinition)
					//TODO define context menu for multiple report defintions.
					contextMenuType = ier_constants.MenuType_IERMultipleCustomObjectsContextMenu;					
			}
			
		}
		return contextMenuType;
	},
	
	/**
	 * Obtains the correct record context menu type
	 */
	getRecordContextMenuType: function(item){
		var contextMenuType = null;
		if(item && item instanceof ier_model_BaseEntityObject){
			var entityType = item.getEntityType();
			var parentEntityType = item.parent && item.parent.entityType;
			switch(entityType){
				case ier_constants.EntityType_ElectronicRecord:
				case ier_constants.EntityType_EmailRecord:
				case ier_constants.EntityType_PDFRecord:
					if(parentEntityType){
						contextMenuType = ier_constants.MenuType_IERElectronicRecordContextMenu;
					}else{ // search
						contextMenuType = ier_constants.MenuType_IERSearchElectronicRecordContextMenu;
					}
					break;
				case ier_constants.EntityType_PhysicalRecord:
					if(parentEntityType){
						contextMenuType = ier_constants.MenuType_IERPhysicalRecordContextMenu;
					}else{ // search
						contextMenuType = ier_constants.MenuType_IERSearchPhysicalRecordContextMenu;
					}
					break;
				default:
					contextMenuType = null;
					break;
			}
		}
		return contextMenuType;
	},
	
	/**
	 * Obtains the correct container menu type
	 */
	getContainerContextMenuType: function(item, browse){
		var contextMenuType = null;
		if(item && item instanceof ier_model_BaseEntityObject){
			var entityType = item.getEntityType();
			switch(entityType){
				case ier_constants.EntityType_FilePlan:
					if(browse){
						contextMenuType = ier_constants.MenuType_IERFilePlanContextMenu;
					} else {
						contextMenuType = ier_constants.MenuType_IERFilePlanConfigureContextMenu;
					}
					break;
				case ier_constants.EntityType_RecordCategory:
					contextMenuType = ier_constants.MenuType_IERRecordCategoryContextMenu;
					break;
				case ier_constants.EntityType_HybridRecordFolder:
				case ier_constants.EntityType_PhysicalRecordFolder:
				case ier_constants.EntityType_PhysicalContainer:
					contextMenuType = ier_constants.MenuType_IERPhysicalBoxHybridRecordFolderContextMenu;
					break;
				case ier_constants.EntityType_ElectronicRecordFolder:
					contextMenuType = ier_constants.MenuType_IERElectronicRecordFolderContextMenu;
					break;
				case ier_constants.EntityType_Volume:
					contextMenuType = ier_constants.MenuType_IERVolumeContextMenu;
					break;
				default:
					contextMenuType = null;
					break;
			}
		}
		return contextMenuType;
	},
	
	/**
	 * Update any menus and toolbars by hiding or display the Add Containers actions based on allowedRMEntityTypes
	 */
	updateAddContainersToolbar: function(item, addRecordCategoryButton, addRecordFolderButton, addRecordVolumeButton){
		dojo_style.set(addRecordCategoryButton.domNode, "display", "none");
		dojo_style.set(addRecordFolderButton.domNode, "display", "none");
		dojo_style.set(addRecordVolumeButton.domNode, "display", "none");
		
		if(item && item.getEntityType && item.getEntityType() == ier_constants.EntityType_FilePlan)
			dojo_style.set(addRecordCategoryButton.domNode, "display", "");
		
		if(item && item.getAllowedRMTypes && item.getAllowedRMTypes()){
			var allowedRMContaineesArray = item.getAllowedRMTypes();
			if(allowedRMContaineesArray && allowedRMContaineesArray.length > 0){
				for(var i in allowedRMContaineesArray){
					var allowedContaineeType = allowedRMContaineesArray[i];
					if(allowedContaineeType == ier_constants.EntityType_RecordCategory)
						dojo_style.set(addRecordCategoryButton.domNode, "display", "");
					else if(allowedContaineeType == ier_constants.EntityType_RecordFolder || allowedContaineeType == ier_constants.EntityType_ElectronicRecordFolder ||
							allowedContaineeType == ier_constants.EntityType_PhysicalContainer || allowedContaineeType == ier_constants.EntityType_HybridRecordFolder ||
							allowedContaineeType == ier_constants.EntityType_PhysicalRecordFolder)
						dojo_style.set(addRecordFolderButton.domNode, "display", "");
					else if(allowedContaineeType == ier_constants.EntityType_Volume)
						dojo_style.set(addRecordVolumeButton.domNode, "display", "");
				}
			}
		}
	},
	
	/**
	 * Obtain the correct context menu type for a Task
	 * @param items
	 * @returns {String}
	 */ 
	getTaskContextMenuType: function(items){
		if(items.length == 1){
			var item = items[0];
			var handlerClass = item.attributes[ier_constants.Attribute_Type];

			if(item.isInstanceOf && item.isInstanceOf(ecm.model.AsyncTask)){
				var status = item.attributes.status;
				if(status == ier_constants.TaskStatus_InProgress || status == ier_constants.TaskStatus_Init || status == ier_constants.TaskStatus_Pending){
					return ier_constants.MenuType_IERInProgressTaskContextMenu;
				}
				else if(status == ier_constants.TaskStatus_Completed){
					if(item.isTaskRecurring())
						return ier_constants.MenuType_IERCompletedRecurringTaskContextMenu;
					else {
						if(handlerClass == ier_constants.TaskType_ReportClass)
							return ier_constants.MenuType_IERReportsCompletedTaskContextMenu;
						else
							return ier_constants.MenuType_IERCompletedTaskContextMenu;
					}
				}
				else if(status == ier_constants.TaskStatus_Scheduled){
					if(item.isTaskRecurring())
						return ier_constants.MenuType_IERScheduledRecurringTaskContextMenu;
					else
						return ier_constants.MenuType_IERScheduledTaskContextMenu;
				}
				else if(status == ier_constants.TaskStatus_Failed){
					if(item.isTaskRecurring())
						return ier_constants.MenuType_IERFailedRecurringTaskContextMenu;
					else
						return ier_constants.MenuType_IERFailedTaskContextMenu;
				}
				else if(status == ier_constants.TaskStatus_Paused){
					if(item.isTaskRecurring())
						return ier_constants.MenuType_IERDisabledRecurringTaskContextMenu;
				}
			}

			if(item instanceof ecm_model_AsyncTaskInstance){
				var status = item.attributes.status;
				if(status == ier_constants.TaskStatus_InProgress || status == ier_constants.TaskStatus_Init || status == ier_constants.TaskStatus_Pending){
					return ier_constants.MenuType_IERInProgressTaskInstanceContextMenu;
				}
				else if(status == ier_constants.TaskStatus_Completed){
					if(handlerClass == ier_constants.TaskType_ReportClass)
						return ier_constants.MenuType_IERReportsCompletedTaskInstanceContextMenu;
					else
						return ier_constants.MenuType_IERCompletedTaskInstanceContextMenu;
				}
				else if(status == ier_constants.TaskStatus_Scheduled){
					return ier_constants.MenuType_IERScheduledTaskInstanceContextMenu;
				}
				else if(status == ier_constants.TaskStatus_Failed){
					return ier_constants.MenuType_IERFailedTaskInstanceContextMenu;
				}
			}
		}
		else {
			return ier_constants.MenuType_IERMultipleTasksContextMenu;
		}
	},

	/**
	 * Obtain the correct context menu type for a Favorite
	 * @param items
	 * @returns {String}
	 */
	getFavoriteContextMenuType: function(items){
		var contextMenuType = null;
		if(items && items.length > 0 && items[0].isIERFavorite){
			var fav = items[0];
			var type = fav.item && fav.item.entityType;
			if(!type || type == "-1"){
				type = fav.template;
			}
			switch(type){
			case ier_constants.EntityType_RecordCategory:
			case ier_constants.ClassName_RecordCategory:
				contextMenuType = ier_constants.MenuType_IERFavoriteRecordCategoryContextMenu;
				break;
			case ier_constants.EntityType_ElectronicRecordFolder:
			case ier_constants.ClassName_ElectronicRecordFolder:
				contextMenuType = ier_constants.MenuType_IERFavoriteElectronicRecordFolderContextMenu;
				break;
			case ier_constants.EntityType_HybridRecordFolder:
			case ier_constants.EntityType_PhysicalRecordFolder:
			case ier_constants.EntityType_PhysicalContainer:
			case ier_constants.ClassName_HybridRecordFolder:
			case ier_constants.ClassName_PhysicalRecordFolder:
			case ier_constants.ClassName_PhysicalContainer:
				contextMenuType = ier_constants.MenuType_IERFavoritePhysicalRecordFolderContextMenu;
				break;
			case ier_constants.EntityType_Volume:
			case ier_constants.ClassName_Volume:
				contextMenuType = ier_constants.MenuType_IERFavoriteVolumeContextMenu;
				break;
			case ier_constants.EntityType_Record:
			case ier_constants.EntityType_ElectronicRecord:
			case ier_constants.ClassName_ElectronicRecord:
			case ier_constants.ClassName_EmailRecord:
				contextMenuType = ier_constants.MenuType_IERFavoriteElectronicRecordContextMenu;
				break;
			case ier_constants.EntityType_PhysicalRecord:
			case ier_constants.ClassName_PhysicalRecord:
				contextMenuType = ier_constants.MenuType_IERFavoritePhysicalRecordContextMenu;
				break;
			default:
				if(fav.type == "search"){
					contextMenuType = "FavoriteSearchTemplateContextMenu";
				}else{
					contextMenuType = "FavoriteFolderContextMenu";
				}
				break;
			}
		}else{
			contextMenuType = this.getContainersAndRecordsContextMenuType(items);
		}
		return contextMenuType;
	}

});

var menuUtil = new _MenuUtil();

return menuUtil;
});
