define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/date/stamp",
	"dojo/string",
	"dojo/io-query",
	"ecm/widget/dialog/MessageDialog",
	"ecm/LoggerMixin",
	"ecm/Messages",
	"ecm/model/ContentItem",
	"ecm/model/Desktop",
	"ecm/model/Message",
	"ecm/model/Repository",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/dialog",
	"ier/messages"
], function(dojo_declare, dojo_lang, dojo_date_stamp, dojo_string, dojo_ioQuery, ecm_dialog_MessageDialog, ecm_LoggerMixin, ecm_messages, 
		ecm_model_ContentItem, ecm_model_desktop, ecm_model_Message, ecm_model_Repository, Request, ier_constants, ier_util_dialog, ier_messages){

var _Util = dojo_declare("ier.util._Util", [ecm_LoggerMixin], {

	/**
	 * Return the appropriate css icon class based on className
	 */
	getIconClass: function(item) {
		var iconCss;

		if(item.isIERFavorite && item.item){
			item = item.item;
		}
		var properties = item;
		var attributes = item.attributes;
		var className = properties.template;
		var entityType = item instanceof ier.model._BaseEntityObject ? item.getEntityType() : null;
		if(className == ier_constants.ClassName_RecordsManagementFolder)
			iconCss = "folderIcon";
		else if(className == ier_constants.ClassName_FilePlan || entityType == ier_constants.EntityType_FilePlan)
			iconCss = "filePlanIcon";
		else if(entityType == ier_constants.EntityType_RecordCategory || className == ier_constants.ClassName_RecordCategory)
			iconCss = "recordCategoryIcon";
		else if(entityType == ier_constants.EntityType_ElectronicRecordFolder || className == ier_constants.ClassName_ElectronicRecordFolder)
			iconCss = "recordElectronicFolderIcon";
		else if(entityType == ier_constants.EntityType_HybridRecordFolder || className == ier_constants.ClassName_HybridRecordFolder)
			iconCss = "recordHybridFolderIcon";
		else if(entityType == ier_constants.EntityType_PhysicalRecordFolder || className == ier_constants.ClassName_PhysicalRecordFolder)
			iconCss = "recordPhysicalFolderIcon";
		else if(entityType == ier_constants.EntityType_PhysicalContainer || className == ier_constants.ClassName_PhysicalContainer)
			iconCss = "recordBoxIcon";
		else if(entityType == ier_constants.EntityType_Volume || className == ier_constants.ClassName_Volume)
			iconCss = "volumeIcon";
		else if(entityType == ier_constants.EntityType_Record || entityType == ier_constants.EntityType_ElectronicRecord || className == ier_constants.ClassName_ElectronicRecord)
			iconCss = "electronicRecordIcon";
		else if(entityType == ier_constants.EntityType_PhysicalRecord || className == ier_constants.ClassName_PhysicalRecord)
			iconCss = "physicalRecordIcon";
		else if(entityType == ier_constants.EntityType_EmailRecord || className == ier_constants.ClassName_EmailRecord)
			iconCss = "emailRecordIcon";
		else if(entityType == ier_constants.EntityType_DispositionSchedule || className == ier_constants.ClassName_DispositionSchedule)
			iconCss = "dispositionScheduleIcon";
		else if(entityType == ier_constants.EntityType_Location || className == ier_constants.ClassName_Location)
			iconCss = "locationIcon";
		else if(entityType == ier_constants.EntityType_RecordType || className == ier_constants.ClassName_RecordType)
			iconCss = "recordTypeIcon";
		else if(entityType == ier_constants.EntityType_TransferMapping || className == ier_constants.ClassName_TransferMapping)
			iconCss = "transferMappingIcon";
		else if(entityType == ier_constants.EntityType_DispositionAction || className == ier_constants.ClassName_DispositionAction){
			var actionType = attributes[ier_constants.Property_ActionType];
			
			if(actionType == null)
				iconCss = "dispositionActionIcon";
			else if(actionType == ier_constants.ActionType_Review)
				iconCss = "dispositionActionReviewIcon";
			else if(actionType == ier_constants.ActionType_Export)
				iconCss = "dispositionActionExportIcon";
			else if(actionType == ier_constants.ActionType_Transfer)
				iconCss = "dispositionActionTransferIcon";
			else if(actionType == ier_constants.ActionType_Destroy)
				iconCss = "dispositionActionDestroyIcon";
			else if(actionType == ier_constants.ActionType_Export)
				iconCss = "dispositionActionExportIcon";
			else if(actionType == ier_constants.ActionType_InterimTransfer)
				iconCss = "dispositionActionInterimTransferIcon";
			else if(actionType == ier_constants.ActionType_Cutoff)
				iconCss = "dispositionActionCutoffIcon";
			else if(actionType == ier_constants.ActionType_VitalReview)
				iconCss = "dispositionActionVitalReviewIcon";
			else if(actionType == ier_constants.ActionType_AutoDestroy)
				iconCss = "dispositionActionAutoDestroyIcon";
			else
				iconCss = "dispositionActionIcon";
		}
		else if(entityType == ier_constants.EntityType_DispositionTrigger || className == ier_constants.ClassName_DispositionTrigger){
			var eventType = attributes[ier_constants.Property_EventType];
			
			if(eventType == null)
				iconCss = "dispositionTriggerIcon";
			else if(eventType == ier_constants.EventType_PredefinedDateTrigger)
				iconCss = "dispositionTriggerPredefinedDateIcon";
			else if(eventType == ier_constants.EventType_ExternalEventTrigger)
				iconCss = "dispositionTriggerExternalEventIcon";
			else if(eventType == ier_constants.EventType_InternalEventTrigger)
				iconCss = "dispositionTriggerEventInternalIcon";
			else if(eventType == ier_constants.EventType_RecurringEventTrigger)
				iconCss = "dispositionTriggerEventRecurringIcon";
			else
				iconCss = "dispositionTriggerIcon";
		}
		else if(entityType == ier_constants.EntityType_NamingPattern || className == ier_constants.ClassName_NamingPattern)
			iconCss = "namingPatternIcon";
		else if(entityType == ier_constants.EntityType_Hold || className == ier_constants.ClassName_Hold){
			if(item.isDynamicHold()){
				iconCss = "dynamicHoldIcon";
			}else{
				iconCss = "onHoldIcon";
			}
		}else if(className == ier_constants.ClassName_WorkflowDefinition)
			iconCss = "workflowDefinitionIcon";
		else if(className == ier_constants.ClassName_ReportDefinition)
			iconCss = "reportIcon";
		else
			iconCss = null;

		return iconCss;
	},
	
	/**
	 * Return the appropriate mimetype tooltip for the current item
	 */
	getMimetypeTooltip: function(item) {
		var tooltip;
		
		var properties = item;
		var attributes = item.attributes;
		var className = properties.template;
		var entityType = item instanceof ier.model._BaseEntityObject ? item.getEntityType() : null;
		if(className == ier_constants.ClassName_FilePlan || entityType == ier_constants.EntityType_FilePlan)
			tooltip = ier_messages.fileplan;
		else if(entityType == ier_constants.EntityType_RecordCategory || className == ier_constants.ClassName_RecordCategory)
			tooltip = ier_messages.recordCategory;
		else if(entityType == ier_constants.EntityType_ElectronicRecordFolder || className == ier_constants.ClassName_ElectronicRecordFolder)
			tooltip = ier_messages.electronicRecordFolder;
		else if(entityType == ier_constants.EntityType_HybridRecordFolder || className == ier_constants.ClassName_HybridRecordFolder)
			tooltip = ier_messages.	hybridRecordFolder;
		else if(entityType == ier_constants.EntityType_PhysicalRecordFolder || className == ier_constants.ClassName_PhysicalRecordFolder)
			tooltip = ier_messages.physicalRecordFolder;
		else if(entityType == ier_constants.EntityType_PhysicalContainer || className == ier_constants.ClassName_PhysicalContainer)
			tooltip = ier_messages.box;
		else if(entityType == ier_constants.EntityType_Volume || className == ier_constants.ClassName_Volume)
			tooltip = ier_messages.recordVolume;
		else if(entityType == ier_constants.EntityType_Record || entityType == ier_constants.EntityType_ElectronicRecord || className == ier_constants.ClassName_ElectronicRecord)
			tooltip = ier_messages.electronicRecord;
		else if(entityType == ier_constants.EntityType_PhysicalRecord || className == ier_constants.ClassName_PhysicalRecord)
			tooltip = ier_messages.physicalRecord;
		else if(entityType == ier_constants.EntityType_EmailRecord || className == ier_constants.ClassName_EmailRecord)
			tooltip = ier_messages.emailRecord;
		else if(entityType == ier_constants.EntityType_DispositionSchedule || className == ier_constants.ClassName_DispositionSchedule)
			tooltip = ier_messages.dispositionSchedule;
		else if(entityType == ier_constants.EntityType_Location || className == ier_constants.ClassName_Location)
			tooltip = ier_messages.location;
		else if(entityType == ier_constants.EntityType_RecordType || className == ier_constants.ClassName_RecordType)
			tooltip = ier_messages.recordType;
		else if(entityType == ier_constants.EntityType_TransferMapping || className == ier_constants.ClassName_TransferMapping)
			tooltip = ier_messages.transferMapping;
		else if(entityType == ier_constants.EntityType_DispositionAction || className == ier_constants.ClassName_DispositionAction){
			var actionType = attributes[ier_constants.Property_ActionType];
			
			if(actionType == null)
				tooltip = null;
			else if(actionType == ier_constants.ActionType_Review)
				tooltip = ier_messages.reviewAction;
			else if(actionType == ier_constants.ActionType_Export)
				tooltip = ier_messages.exportAction;
			else if(actionType == ier_constants.ActionType_Transfer)
				tooltip = ier_messages.transferAction;
			else if(actionType == ier_constants.ActionType_Destroy)
				tooltip = ier_messages.destroyAction;
			else if(actionType == ier_constants.ActionType_Export)
				tooltip = ier_messages.exportAction;
			else if(actionType == ier_constants.ActionType_InterimTransfer)
				tooltip = ier_messages.interimTransferAction;
			else if(actionType == ier_constants.ActionType_Cutoff)
				tooltip = ier_messages.cutoffAction;
			else if(actionType == ier_constants.ActionType_VitalReview)
				tooltip = ier_messages.vitalReviewAction;
			else if(actionType == ier_constants.ActionType_AutoDestroy)
				tooltip = ier_messages.autoDestroyAction;
			else
				tooltip = null;
		}
		else if(entityType == ier_constants.EntityType_DispositionTrigger || className == ier_constants.ClassName_DispositionTrigger){
			var eventType = attributes[ier_constants.Property_EventType];
			
			if(eventType == null)
				tooltip = ier_messages.dispositionTrigger;
			else if(eventType == ier_constants.EventType_PredefinedDateTrigger)
				tooltip = ier_messages.predefinedDateTrigger;
			else if(eventType == ier_constants.EventType_ExternalEventTrigger)
				tooltip = ier_messages.externalEventTrigger;
			else if(eventType == ier_constants.EventType_InternalEventTrigger)
				tooltip = ier_messages.internalEventTrigger;
			else if(eventType == ier_constants.EventType_RecurringEventTrigger)
				tooltip = ier_messages.recurringEventTrigger;
			else
				tooltip = ier_messages.dispositionTrigger;
		}
			
		else if(entityType == ier_constants.EntityType_NamingPattern || className == ier_constants.ClassName_NamingPattern)
			tooltip = ier_messages.namingPattern;
		else if(entityType == ier_constants.EntityType_Hold || className == ier_constants.ClassName_Hold){
			if(item.isDynamicHold()){
				tooltip = ier_messages.dynamicHold;
			}else{
				tooltip = ier_messages.hold;
			}
		}else if(className == ier_constants.ClassName_WorkflowDefinition)
			tooltip = ier_messages.workflowDefinition;
		else if(className == ier_constants.ClassName_ReportDefinition)
			tooltip = ier_messages.reportDefinition;				
		else
			tooltip = null;

		return tooltip;
	},
	
	/**
	 * Converts an array to string delimited by commas
	 * @param values
	 * @returns {String}
	 */
	arrayToString: function(values){
		var result = "";
		for ( var i = 0; i < values.length; i++) {
			var value = values[i];
			if (i > 0) {
				result += ",";
			}
			result += value;
		}
		return result;
	},
	
	/**
	 * Returns a new param object with the repositoryId and desktopId params automatically filled
	 */
	getDefaultParams: function(repositoryOrRepositoryId, onFinish){
		var params = new Object();
		var requestParams = new Object();
		if(repositoryOrRepositoryId){
			var repositoryId = repositoryOrRepositoryId instanceof ecm_model_Repository ? repositoryOrRepositoryId.id : repositoryOrRepositoryId;
			requestParams[ier_constants.Param_RepositoryId] = repositoryId;
		}
		requestParams[ier_constants.Param_Desktop] = ecm_model_desktop.id;
		params["requestParams"] = requestParams;
		if(onFinish){
			params["requestCompleteCallback"] = onFinish;
		}
		return params;
	},
	
	/**
	 * Create a single BaseEntity item for IER.  This assumes the JSON is formatted according to the Nexus specification.
	 * 
	 * @param response
	 * @param repository
	 * @param parent
	 * @returns
	 */
	createBaseEntityItem: function(response, repository, resultSet, parent){
		return ecm_model_ContentItem.createFromJSON(response.rows[0], repository, resultSet, parent);
	},
	
	/**
	 * Returns the P8 Guid ID associated with the ID property stored.  Typically, the DOC ID is a combination of {CLASS},{P8REPOSITORY_ID},{P8ID}
	 * @returns
	 */
	getGuidId: function(docId) {
		var ids = docId.split(",");
		if(ids.length == 3)
			return docId.split(",")[2];
		else
			return docId;
	},
	
	/**
	 * Returns the base URL to use for help pages.
	 * Repeated here because ICN's one is private
	 */
	getHelpUrl: function(forPage) {
		return forPage ? ecm.model.desktop.helpUrl + "index.jsp?content=" + ier_constants.HelpContextUrl + forPage : this.helpUrl;
	},
	
	/**
	 * Returns an ISO Date String
	 * @param date
	 * @returns {String}
	 */  
	getISODateString: function(d){  
		return dojo_date_stamp.toISOString(d);
	},
	
	/**
	 * Return an array of cloned attributes
	 * @param attributeDefs
	 * @returns {Array}
	 */
	cloneAttributes: function(attributeDefs){
		var copyAttributes = [];
		for ( var i in attributeDefs) {
			var attrDef = attributeDefs[i];
			copyAttributes.push(attrDef.clone());
		}
		return copyAttributes;
	},
	
	/**
	 * Finds and locates the matching attribute definition from the list
	 * @param attributeDefs
	 * @returns {Array}
	 */
	getAttributeDefinition: function(attributeDefs, id){
		for ( var i in attributeDefs) {
			if(attributeDefs[i].id == id)
				return attributeDefs[i];
		}
		return null;
	},

	/**
	 * Trim and replace new lines (for protecting against MHTML vulnerability)
	 * @param string
	 * @return {String}
	 */
	compactString: function(string){
		if(string){
			string = string.trim().replace(/[\n\r]/g, " ");
		}
		return string;
	},
	
	/**
	 * Creates an instance of {@link ecm.model.Message} for an error, given the message prefix for the error.
	 * 
	 * @param messagePrefix
	 *            The prefix of the set of strings related to the message as stored in the ecm.messages object.
	 * @param inserts
	 *            An array of strings to insert into the message.
	 * @param isBackgroundRequest
	 *            Boolean indicating if the message is being created by an background request.
	 */
	createIERErrorMessage: function(messagePrefix, inserts, isBackgroundRequest) {
		inserts = inserts || [];
		var messageText = ier_messages[messagePrefix] ? dojo_string.substitute(ier_messages[messagePrefix], inserts) : messagePrefix;
		var messageExplanation = ier_messages[messagePrefix + "_explanation"] ? dojo_string.substitute(ier_messages[messagePrefix + "_explanation"], inserts) : "";
		var messageUserResponse = ier_messages[messagePrefix + "_userResponse"] ? dojo_string.substitute(ier_messages[messagePrefix + "_userResponse"], inserts) : "";
		var messageAdminResponse = ier_messages[messagePrefix + "_adminResponse"] ? dojo_string.substitute(ier_messages[messagePrefix + "_adminResponse"], inserts) : "";
		var message = new ecm.model.Message({
			level: 4,
			text: messageText,
			explanation: messageExplanation,
			userResponse: messageUserResponse,
			adminResponse: messageAdminResponse
		});		
		return message;
	},

	isCompatibleIERVersion: function(){
		//this version of declare plug-in requires the minimum version of 2.0.1
		if(ecm.messages.product_version.indexOf("2.0.0") == 0 || ecm.messages.product_version.indexOf("2.0.1") == 0){
			ecm_model_desktop.addMessage(this.createIERErrorMessage("error_incompatitableVersion"));
			return false;
		}
		return true;
	},
	
	/**
	 * Runs a report by opening it in a separate broswer window
	 * @param repository
	 * @param itemId
	 */
	runReport: function(repository, itemId) {
		
		var scriptUrl = ecm.model.Request.getPluginResourceUrl("IERApplicationPlugin", "ier/widget/layout/ReportLayout.js");
		
		var params = [{ name : "layout", 		value: scriptUrl},
					  { name : "repository", 	value: repository.id},
					  { name : "itemId", 		value: itemId},
					  { name : "useLastCache",  value: true}]; 	
		
		var reqParam = "";
		var questionMarkFound = true;
		if(location.href.indexOf("?") == -1)
		{
			reqParam = "?";
			questionMarkFound = false;
		}
		
		for (var i in params) {
			if(!questionMarkFound){
				questionMarkFound = true;
				reqParam += params[i].name + "=" + params[i].value;	
			}
			else {
				reqParam += "&" + params[i].name + "=" + params[i].value;	
			}
		}

		var width = 920;
		var height = 650;
		var xpos = Math.round((window.screen.availWidth - width) / 2);
		var ypos = Math.round((window.screen.availHeight - height) / 2);

		var windowname = "";
		if (params[2].value) // IE browser cannot take -{} as a window name.
			windowname = params[2].value[0].replace(/[-{}]/g, "A");
		
		
		var newwin = window.open(location.href + reqParam, 
				ier_constants.ReportWindowNamePrefix + windowname,
				"titlebar=yes,toolbar=no,resizable=yes,status=no,menubar=no,width=" + width + ",height=" + height + ",left=" + xpos + ",top=" + ypos);
		
		if (window.focus) {
			newwin.focus();
		}
	},

	/**
	 * Retrieves a report title and name by checking it against our javascript resource files
	 * @param key
	 * @param message
	 * @returns
	 */
	getReportMessages: function(key, message){
		if(key){
			key = key.replace(/\./g,'_'); //replace all dots with underscores since our messages can't handle dots
			var messageText = ier_messages[key];
			return messageText ? messageText : message;
		} else
			return message;
	},
	
	/**
	 * Checks whether the original string starts with the new snippet
	 * @param originalString
	 * @param snippet
	 */
	startsWith: function(originalString, snippet){
		return (originalString.subString && originalString.substring(0, snippet.length) === snippet);
	},
	
	/**
	 * Converts the task type
	 */
	convertTaskType: function(type){
		if(type == ier_constants.TaskType_Report)
			return ier_constants.TaskType_ReportClass;
		else if(type == ier_constants.TaskType_DispositionSweep)
			return ier_constants.TaskType_DispositionSweepClass;
		else if(type == ier_constants.TaskType_HoldSweep)
			return ier_constants.TaskType_HoldSweepClass;
		else if(type == ier_constants.TaskType_DefensibleDisposal)
			return ier_constants.TaskType_DDConversionClass + "," + ier_constants.TaskType_DDReportSweepClass;
		else
			return type;
	},
	
	/**
	 * Get the display value of the task status
	 * @param status
	 * @returns
	 */
	getStatusDisplayValue: function(status){
		if(status == ier_constants.TaskStatus_Completed)
			return ier_messages.taskPane_completedStatus;
		else if (status == ier_constants.TaskStatus_Scheduled)
			return ier_messages.taskPane_scheduledStatus;
		else if(status == ier_constants.TaskStatus_Failed)
			return ier_messages.taskPane_failedStatus;
		else if(status == ier_constants.TaskStatus_Paused)
			return ier_messages.taskPane_disabledStatus;
		else
			return ier_messages.taskPane_inProgressStatus;
	},
	
	/**
	 * Get the display value of the task type
	 */
	getTypeDisplayValue: function(handlerClassName){
		if(handlerClassName == ier_constants.TaskType_ReportClass)
			return ier_messages.reports;
		else if (handlerClassName == ier_constants.TaskType_DDConversionClass || handlerClassName == ier_constants.TaskType_DDReportSweepClass)
			return ier_messages.defensibleDisposal;
		else
			return handlerClassName;
	},
	
	/**
	 * Get the display value of the task type
	 */
	getTypeDisplayIcon: function(handlerClassName){
		if(handlerClassName == ier_constants.TaskType_ReportClass)
			return "taskReportIcon";
		else if(handlerClassName == ier_constants.TaskType_DDConversionClass || handlerClassName == ier_constants.TaskType_DDReportSweepClass)
			return "taskBasicScheduleIcon";
		else if(handlerClassName == ier_constants.TaskType_HoldSweepClass)
			return "taskHoldIcon";
	},
	
	/**
	 * Creates an instance of {@link ecm.model.Message} for an error, given the message prefix for the error.
	 * 
	 * @param messagePrefix
	 *            The prefix of the set of strings related to the message as stored in the ecm.messages object.
	 * @param inserts
	 *            An array of strings to insert into the message.
	 * @param isBackgroundRequest
	 *            Boolean indicating if the message is being created by an background request.
	 */
	createIERErrorMessage: function (messagePrefix, inserts, isBackgroundRequest) {
		inserts = inserts || [];
		var messageText = ier_messages[messagePrefix] ? dojo_string.substitute(ier_messages[messagePrefix], inserts) : messagePrefix;
		var messageExplanation = ier_messages[messagePrefix + "_explanation"] ? dojo_string.substitute(ier_messages[messagePrefix + "_explanation"], inserts) : "";
		var messageUserResponse = ier_messages[messagePrefix + "_userResponse"] ? dojo_string.substitute(ier_messages[messagePrefix + "_userResponse"], inserts) : "";
		var messageAdminResponse = ier_messages[messagePrefix + "_adminResponse"] ? dojo_string.substitute(ier_messages[messagePrefix + "_adminResponse"], inserts) : "";
		var message = new ecm.model.Message("", "", 4, messageText, messageExplanation, messageUserResponse, messageAdminResponse, null, isBackgroundRequest);
		return message;
	},

	/**
	 * Replaces a gridx/listView module by a name
	 * @param modules
	 * 		An array of modules
	 * @param name
	 * 		A module name to replace
	 * @param module
	 * 		A new module
	 * @returns
	 * 		true when replaced, otherwise false
	 */
	replaceModule: function(modules, name, module){
		var r = false;
		if(modules && name){
			for(var i = 0; i < modules.length; i++){
				var m = modules[i];
				if(m){
					var p = m.moduleClass && m.moduleClass.prototype || m.prototype;
					var n = p && p.name;
					if(n == name){
						modules[i] = module;
						r = true;
					}else if(n == "bar"){
						r = this.replaceModule(m.top, name, module) || this.replaceModule(m.bottom, name, module);
					}else if(dojo_lang.isArray(m)){
						r = this.replaceModule(m, name, module);
					}
					if(r){
						break;
					}
				}
			}
		}
		return r;
	},
	
	/**
	 * Create a task schedule based on a task's request information
	 * @param task
	 */
	createTaskSchedule: function(task){
		var schedule = {
				recurring : task.taskRequest.specificTaskRequest[ier_constants.Param_IsRecurring],
				interval : task.taskRequest.specificTaskRequest[ier_constants.Param_RepeatCycle],
				startTime : task.taskRequest.specificTaskRequest[ier_constants.Param_StartTime],
				endTime : task.taskRequest.specificTaskRequest[ier_constants.Param_EndTime],
				description : task.taskRequest.specificTaskRequest[ier_constants.Param_Description2],
				name : task.taskRequest.specificTaskRequest[ier_constants.Param_Name2],
				username : task.taskRequest[ier_constants.Param_UserId],
				password : task.taskRequest[ier_constants.Param_Password],
				email : task.taskRequest[ier_constants.Param_EmailAddress],
		};
		return schedule;
	},
	
	/**
	 * Sets the task scheduler pane with schedule information and disable it
	 * @param asyncTaskSchedulerPane
	 * @param schedule
	 */
	setTaskSchedulerPane: function(asyncTaskSchedulerPane, schedule, allowNameAndDescription){
		if(!allowNameAndDescription){
			asyncTaskSchedulerPane.nameTextBox.set("disabled", true);
			asyncTaskSchedulerPane.descriptionTextBox.set("disabled", true);
		}
		
		asyncTaskSchedulerPane.set("schedule", schedule);
		asyncTaskSchedulerPane.startTimeDateTimeTextBox.set("disabled", true);
		asyncTaskSchedulerPane.enableRecurrenceRadioButton.set("disabled", true);
		asyncTaskSchedulerPane.disableRecurrenceRadioButton.set("disabled", true);
		asyncTaskSchedulerPane.intervalNumberSpinner.set("disabled", true);
		asyncTaskSchedulerPane.intervalSelect.set("disabled", true);
		asyncTaskSchedulerPane.startImmediatelyCheckbox.set("disabled", true);
		
		if(schedule.username)
			asyncTaskSchedulerPane.usernameTextBox.set("value", schedule.username);
	},
	
	getClassName: function(property){
		var className = null;
		if(property == ier_constants.Property_Location || property == ier_constants.Property_HomeLocation)
			className = ier_constants.ClassName_Location;
		else if(property == ier_constants.Property_AssociatedRecordType)
			className = ier_constants.ClassName_RecordType;
		else if(property == ier_constants.Property_AssociatedWorkflow){
			className = ier_constants.ClassName_WorkflowDefinition;
		}
		else if(property == ier_constants.Property_NamingPattern){
			className = ier_constants.ClassName_NamingPattern;
		}
		else if(property == ier_constants.Property_DispositionSchedule){
			className = ier_constants.ClassName_DispositionSchedule;
		}
		return className;
	},
	
	getRootDownloadLinkURL: function(repository) {
		var queryParams = {
				desktop: ecm.model.desktop.id,
				repositoryId: repository.id
			};
		
		var rootPath = ecm.model.desktop._cServicesUrl || "/navigator";
		var linkUrl = rootPath + "/bookmark.jsp?" + dojo_ioQuery.objectToQuery(queryParams);

		return (window.location.protocol + "//" + window.location.host + linkUrl);
	},
	
	getRepository: function(p8RepositoryId, connectionURL){
		var repository = ecm.model.desktop.getRepositoryFromSymbolicAndConnection(p8RepositoryId, connectionURL);
		if(repository == null){
			var dialog = new ecm_dialog_MessageDialog({
				text: dojo_string.substitute(ier_messages.fileplanRepositoryNotAvailable, [ p8RepositoryId ])
			});
			dialog.startup();
			dialog.show();
			ier_util_dialog.manage(dialog);
		} else {
			return repository;
		}
	},

	getRealItem: function(item){
		if(item && item.isIERFavorite && item.item){
			item = item.item;
		}
		return item;
	},
	
	/**
	 * Schedules an async task.
	 * 
	 * @param schedule
	 *            A schedule object that contains all the scheduling information. It is usually obtained by using
	 *            the {@link ecm.widget.taskManager.AsyncTaskSchedulerPane} and calling
	 *            AsyncTaskSchedulerPane.get("schedule").
	 * @param handlerClassName
	 *            The handlerClassName of the task that will be scheduled. This is typically the classpath of the
	 *            task that will be executed.
	 * @param parameters
	 *            The parameters to be passed in as part of the requestParams for the request
	 * @param data
	 *            The data parameters to be passed in as part of the requestBody for the request
	 * @param callback
	 *            The callback when the request completes
	 */
	scheduleAsyncTask: function(schedule, handlerClassName, parameters, data, callback) {
		var params = {
			requestParams: {},
			requestBody: {}
		};

		params.requestParams["isRecurring"] = schedule.recurring;
		params.requestParams["repeatCycle"] = schedule.interval;
		params.requestParams["startTime"] = schedule.startTime;
		params.requestParams["endTime"] = schedule.endTime;
		params.requestParams["description"] = schedule.description;
		params.requestParams["name"] = schedule.name;
		params.requestParams["handlerClassName"] = handlerClassName;

		dojo_lang.mixin(params.requestParams, parameters);

		if (!data)
			data = {};
		
		data["userId"] = schedule.username;
		data["password"] = schedule.password;
		data["emailAddress"] = schedule.email;
		
		params["requestBody"] = data;
		params.requestCompleteCallback = dojo_lang.hitch(this, function(response) {
			if (callback)
				callback();
		});

		Request.postServiceAPI("scheduleAsyncTask", null, "text/json", params);
	},

});

var util = new _Util();

return util;
});
