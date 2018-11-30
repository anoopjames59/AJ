define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/string",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ecm/widget/dialog/ConfirmationDialog",
	"ecm/widget/dialog/MessageDialog",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/util/dialog",
	"ier/util/property",
	"ier/widget/dialog/BaseAccordionWizardDialog",
	"ier/widget/panes/ReportEntryFormPane",
	"ier/widget/panes/AsyncTaskSchedulerPane",
	"ier/widget/panes/AddItemPane",
	"ier/widget/dialog/IERBaseDialogPane"
], function(dojo_declare, dojo_lang, dojo_construct, dojo_style, dojo_string, ecm_model_Desktop, ecm_model_Request, ecm_widget_dialog_ConfirmationDialog, ecm_dialog_MessageDialog,
		ier_constants, ier_messages, ier_util, ier_util_dialog, property_util, ier_dialog_BaseAccordionWizardDialog, ier_widget_ReportEntryFormPane, 
		ier_widget_AsyncTaskSchedulerPane, ier_widget_AddItemPane, ier_dialog_IERBaseDialogPane){

/**
 * @name ier.widget.dialog.ScheduleReportWizardDialog
 * @class The dialog to schedule a report task
 * @augments ier.widget.dialog.BaseAccordionWizardDialog
 */
return dojo_declare("ier.widget.dialog.ReScheduleReportWizardDialog", [ier_dialog_BaseAccordionWizardDialog], {
	/** @lends ier.widget.dialog.ScheduleReportWizardDialog.prototype */

	finishButtonLabel: ier_messages.reScheduleReportDialog_finishButton,
	
	/**
	 * The original task request information
	 */
	data: null,
	
	task: null,
		
	postCreate: function(){
		if(!this.data.specificTaskRequest)
			this.data.specificTaskRequest = this.data.reportTaskRequest;
		
		this.set("title", ier_messages.reScheduleReportDialog_title);
		this.setIntroText(ier_messages.reScheduleReportDialog_description);
		this.setResizable(true);
		
		if(!this.task.isTaskRecurring())
			this.finishButtonLabel = ier_messages.reScheduleReportDialog_finishAndRunButton;
		
		this.inherited(arguments);
					
		//report entry form
		this.reportEntryFormPane = new ier_widget_ReportEntryFormPane({
			disabled : true
		});
		this.addChildPane(this.reportEntryFormPane);
			
		this.connect(this.reportEntryFormPane, "onInputChange", function(){
			//setTimeout is needed to get the value onkeyup
			setTimeout(dojo_lang.hitch(this, function(){
				this._validateNextPane();
			}, 0));
		});
		
		this.connect(this.reportEntryFormPane, "onCompleteRendering", function(){
			this._validateNextPane();
		});
		
		this.connect(this.reportEntryFormPane, "onRenderAttributes", function(attributes, deferArray){
			var propertyJSON = this.data.specificTaskRequest.criterias;
			//ugly hack to replace the hold_name with HoldName.  This should have been changed in the report definition
			for(var i in propertyJSON){
	   			var property = propertyJSON[i];
	   			if(property.name == ier_constants.ReportEntry_hold_name){
	   				property.name = ier_constants.Property_HoldName;
	   			}
	   		}

			property_util.setPropertiesFromPropertyJSON(attributes, propertyJSON);
		});
		
		//schedule pane
		this.taskSchedulerPane = new ier_widget_AsyncTaskSchedulerPane({
			disabled : true,
			showLoginInformation: true
		});
		this.addChildPane(this.taskSchedulerPane);
		this.taskSchedulerPane.asyncTaskSchedulerPane.enableRecurrenceRadioButton.set("disabled", true);
		this.taskSchedulerPane.asyncTaskSchedulerPane.intervalNumberSpinner.set("disabled", true);
		this.taskSchedulerPane.asyncTaskSchedulerPane.intervalSelect.set("disabled", true);
		dojo_style.set(this.taskSchedulerPane.asyncTaskSchedulerPane.reoccurenceRow, "display", "none");
		dojo_style.set(this.taskSchedulerPane.asyncTaskSchedulerPane.reoccurenceIntervalRow, "display", "none");
		//temporarily remove hover texts for username/password for the async task scheduler
		if(this.taskSchedulerPane.asyncTaskSchedulerPane.usernameHoverHelp)
			dojo_style.set(this.taskSchedulerPane.asyncTaskSchedulerPane.usernameHoverHelp.domNode, "display", "none");
		
		if(this.taskSchedulerPane.asyncTaskSchedulerPane.passwordHoverHelp)
			dojo_style.set(this.taskSchedulerPane.asyncTaskSchedulerPane.passwordHoverHelp.domNode, "display", "none");
		
		this._setSchedulePane();

		//add item pane
		this.addItemPane = new ier_widget_AddItemPane({
			disabled : true,
			title: ier_messages.scheduleReportDialog_saveReport,
			showPermissionPane: false,
			defaultClass: this.data.specificTaskRequest[ier_constants.Param_ClassName]
		});
		this.addChildPane(this.addItemPane);
		
		this.connect(this.addItemPane, "onRenderAttributes", function(attributes, deferArray){
			property_util.setPropertiesFromPropertyJSON(attributes, this.data.specificTaskRequest.archive_criterias);
		});
		
		this.connect(this.taskSchedulerPane, "onInputChange", function(){
			//setTimeout is needed to get the value onkeyup
			setTimeout(dojo_lang.hitch(this, function(){
				if(this.taskSchedulerPane.validate()){
					this.enablePane(this.addItemPane);
				}
				else {
					this.disablePane(this.addItemPane);
				}
				this.updateButtonsState();
			}, 0));
		});
		
		this.startup();
	},
	
	_validateNextPane: function(){
		if(this.reportEntryFormPane.validate()){
			this.enablePane(this.taskSchedulerPane);
			this.taskSchedulerPane.createRendering(this.repository);
		}
		else {
			this.disablePane(this.taskSchedulerPane);
		}
		this.updateButtonsState();
	},
	
	_setSchedulePane: function(){
		var asyncTaskSchedulerPane = this.taskSchedulerPane.asyncTaskSchedulerPane;
		ier_util.setTaskSchedulerPane(asyncTaskSchedulerPane, ier_util.createTaskSchedule(this.task));
	},

	/**
	 * Shows the AddActionDialog
	 * @param repository
	 */
	show: function(){
		this.inherited("show", []);
		this.logEntry("show()");
		
		var repository = ecm.model.desktop.getRepositoryOfObjectStore(this.data.specificTaskRequest[ier_constants.Param_P8RepositoryId]);
		if(repository){// && repository.serverName == this.data.specificTaskRequest[ier_constants.Param_ServerName]){
			repository.loadIERRepository(dojo_lang.hitch(this, function(){
				this.repository = repository;
				this.enablePane(this.reportEntryFormPane);
				this._getReportDefinition(dojo_lang.hitch(this, function(reportDefinition){
					this.reportDefinition = reportDefinition;
					this.reportEntryFormPane.createRendering(this.repository, this.reportDefinition);
				}));
			}));
		} else {
			this._displayMissingDialog();
		}
		
		this.logExit("show()");
	},
	
	_displayMissingDialog: function(){
		var messageDialog = new ecm_dialog_MessageDialog({
			text: ier_messages.reScheduleReportDialog_missingReportInformation
		});
		messageDialog.show();
		ier_util_dialog.manage(messageDialog);
		this.onCancel();
	},
	
	_getReportDefinition: function(callback){
		var reportDefinitions = this.repository.reportDefinitions;
		if(reportDefinitions){
			this.repository.retrieveReportDefinitions(dojo_lang.hitch(this, function(reportDefinitions){
				var reportDefinition = this.repository.getReportDefinition(this.data.specificTaskRequest[ier_constants.Param_ReportId]);
				if(reportDefinition){
					callback(reportDefinition);
				}
				else {
					for(var i in reportDefinitions){
						var rd = reportDefinitions[i];
						if(rd.title == this.data.specificTaskRequest[ier_constants.Param_ReportName]){
							reportDefinition = rd;
						}
					}
					
					if(reportDefinition)
						callback(reportDefinition);
					else {
						this._displayMissingDialog();
					}
				}
			}));
		} else {
			callback(this.repository.getReportDefinition(this.data.specificTaskRequest[ier_constants.Param_ReportId]));
		}
	},
	
	onPaneSelected: function(pane){
		if(this.getCurrentPane() == this.addItemPane){
			this._createAddItemPane();
		}
	},
	
	onNextButtonClicked: function() {
		if(this.getCurrentPane() == this.addItemPane){
			this._createAddItemPane();
		}
	},
	
	_createAddItemPane: function(){
		var repository = ecm.model.desktop.getRepository(this.data.specificTaskRequest[ier_constants.Param_SaveInRepository]);
		if(!repository)
			repository = this.repository;
		this.addItemPane.createRendering(this.repository, this.data.specificTaskRequest[ier_constants.Param_SaveInFolderLocation]);
		this.addItemPane.setDocumentTitle(this.data.specificTaskRequest[ier_constants.Param_ReportTitle]);
	},
	
	onFinishButtonClicked: function(){
		if(!this.task.isTaskRecurring()){
			var confirmDialog = new ecm_widget_dialog_ConfirmationDialog({
				text: ier_messages.reScheduleReportDialog_runImmediatelyConfirm,
				buttonLabel: ier_messages.reScheduleReportDialog_runImmediately,
				onExecute: dojo_lang.hitch(this, function() {
					this._onFinishClicked();
				})
			});
			confirmDialog .show();		
			ier_util_dialog.manage(confirmDialog);
		}
		else {
			this._onFinishClicked();
		}
	},
	
	_onFinishClicked: function(){
		var schedule = this.taskSchedulerPane.get("schedule");
		var reportCriteria = this.reportEntryFormPane.getPropertiesJSON();
		
		//change report task request parameters
		this.data[ier_constants.Param_UserId] = schedule.username;
		this.data[ier_constants.Param_Password] = schedule.password;
		this.data[ier_constants.Param_EmailAddress] = schedule.email;
		
		var savedInFolder = this.addItemPane.getSavedInFolder();
		this.data.specificTaskRequest[ier_constants.Param_SaveInFolderLocation] = savedInFolder.item.id;
		this.data.specificTaskRequest[ier_constants.Param_SaveInRepository] = savedInFolder.item.repository.id;
		this.data.specificTaskRequest[ier_constants.Param_SaveInFolderLocationName] = savedInFolder.item.name;
		this.data.specificTaskRequest[ier_constants.Param_RootDownloadLinkURL] = this.addItemPane.getRootDownloadLinkURL();
		this.data.specificTaskRequest[ier_constants.Param_ReportTitle] = this.addItemPane.getDocumentTitle();
		this.data.specificTaskRequest[ier_constants.Param_ClassName] = this.addItemPane.getClassName();
		this.data.specificTaskRequest[ier_constants.Param_ReportId] = this.reportDefinition.id;
		this.data.specificTaskRequest[ier_constants.Param_ReportName] = this.reportDefinition.name;

		this.data.specificTaskRequest[ier_constants.Param_Properties] = reportCriteria;
		this.data.specificTaskRequest[ier_constants.Param_ArchiveProperties] = this.addItemPane.getProperties();
		this.task.reschedule(dojo_lang.hitch(this, function(response){
			this.onFinish(response);
			this.task.refresh();
			this.onCancel();
		}), this.data);
	},
	
	/**
	 * Event invoked when the wizard has finished scheduling a report
	 */
	onFinish: function(response){
		
	}
});});
