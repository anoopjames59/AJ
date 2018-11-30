define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dojo/dom-style",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/widget/dialog/BaseAccordionWizardDialog",
	"ier/widget/panes/ReportEntryFormPane",
	"ier/widget/panes/ReportListingPane",
	"ier/widget/panes/AsyncTaskSchedulerPane",
	"ier/widget/panes/AddItemPane",
	"ier/widget/dialog/IERBaseDialogPane"
], function(dojo_declare, dojo_lang, dojo_construct, dojo_style, ecm_model_Desktop, ecm_model_Request, ier_constants, ier_messages, ier_util,
		ier_dialog_BaseAccordionWizardDialog, ier_widget_ReportEntryFormPane, ier_widget_ReportListingPane, ier_widget_AsyncTaskSchedulerPane,
		ier_widget_AddItemPane, ier_dialog_IERBaseDialogPane){

/**
 * @name ier.widget.dialog.ScheduleReportWizardDialog
 * @class The dialog to schedule a report task
 * @augments ier.widget.dialog.BaseAccordionWizardDialog
 */
return dojo_declare("ier.widget.dialog.ScheduleReportWizardDialog", [ier_dialog_BaseAccordionWizardDialog], {
	/** @lends ier.widget.dialog.ScheduleReportWizardDialog.prototype */

	finishButtonLabel: ier_messages.scheduleReportDialog_scheduleReport,
	
	/**
	 * Report data for this scheduling wizard.  If this report data is supplied, the first two steps for selecting the report will be not
	 * be displayed.  If it is not supplied, additional steps are shown to gather the data.
	 */
	reportData: null,
		
	postCreate: function(){
		this.inherited(arguments);
		
		this.set("title", ier_messages.taskPane_scheduleReport);
		//this.setIntroText(ier_messages.scheduleReportDialog_description);
		this.setResizable(true);

		if(!this.reportData){
			//report listing pane
			this.reportListingPane = new ier_widget_ReportListingPane();
			this.addChildPane(this.reportListingPane);
			
			//report entry form
			this.reportEntryFormPane = new ier_widget_ReportEntryFormPane({
				disabled : true
			});
			this.addChildPane(this.reportEntryFormPane);
			
			this.connect(this.reportEntryFormPane, "onInputChange", function(){
				if(this.reportEntryFormPane.validate()){
					this.enablePane(this.taskSchedulerPane);
					this.taskSchedulerPane.createRendering(this.reportEntryFormPane.repository);
				}
				else {
					this.disablePane(this.taskSchedulerPane);
				}
				this.updateButtonsState();
			});
		}
		
		//schedule pane
		this.taskSchedulerPane = new ier_widget_AsyncTaskSchedulerPane({
			disabled : true,
			showLoginInformation: true,
			title: ier_messages.scheduleReportDialog_setScheduleForReport
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
		
		//schedule pane
		this.addItemPane = new ier_widget_AddItemPane({
			disabled : true,
			title: ier_messages.scheduleReportDialog_saveReport
		});
		this.addChildPane(this.addItemPane);
		
		this.connect(this.taskSchedulerPane, "onInputChange", function(){
			if(this.taskSchedulerPane.validate()){
				this.enablePane(this.addItemPane);
			}
			else {
				this.disablePane(this.addItemPane);
			}
			this.updateButtonsState();
		});
		
		this.startup();
	},
	
	/**
	 * Shows the AddActionDialog
	 * @param repository
	 */
	show: function(){
		this.inherited("show", []);
		this.logEntry("show()");
		
		if(!this.reportData){
			this.reportListingPane.createRendering();
			
			this.connect(this.reportListingPane, "onItemSelected", function(reportDefinition){
				this.reportEntryFormPane.createRendering(this.reportListingPane.getRepository(), reportDefinition);
				this.addItemPane.clearValues();
				this.selectNextPane();
			});
		}
		else {
			this.enablePane(this.taskSchedulerPane);
			this.taskSchedulerPane.createRendering(this.reportData.repository);
		}
		
		this.logExit("show()");
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
		var repository = this.reportData && this.reportData.repository ? this.reportData.repository : this.reportEntryFormPane.repository;
		this.addItemPane.createRendering(repository, repository.reportOutputSaveDirectory);
		var reportDefinition = this.reportData && this.reportData.reportDefinition ? this.reportData.reportDefinition : this.reportEntryFormPane.reportDefinition;
		//replace invalid characters and only allow a string less than 255 characters
		this.reportTitle = reportDefinition.name + " - " + this.taskSchedulerPane.getAsyncTaskName();
		this.reportTitle = this.reportTitle.substring(0, 255);
		this.addItemPane.setDocumentTitle(this.reportTitle);
	},
	
	onFinishButtonClicked: function(){
		if(!this.reportData){
			this.reportData = {
					repository : this.reportEntryFormPane.repository,
					reportCriteria : this.reportEntryFormPane.getPropertiesJSON(),
					reportDefinition :  this.reportEntryFormPane.reportDefinition
			};
		}
		
		var repository = this.reportData.repository;
		var schedule = this.taskSchedulerPane.get("schedule");
		var reportCriteria = this.reportData.reportCriteria;
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
			this.onFinish(response);
			ecm_model_Desktop.taskManager.onAsyncTaskItemAdded(response);
			this.onCancel();
		}));
		
		//schedule parameters
		var data = new Object();
		params["requestParams"][ier_constants.Param_IsRecurring] = schedule.recurring;
		params["requestParams"][ier_constants.Param_RepeatCycle] = schedule.interval;
		params["requestParams"][ier_constants.Param_StartTime] = schedule.startTime;
		params["requestParams"][ier_constants.Param_EndTime] = schedule.endTime;
		params["requestParams"][ier_constants.Param_Description2] = schedule.description;
		params["requestParams"][ier_constants.Param_Name2] = schedule.name;
		data[ier_constants.Param_UserId] = schedule.username;
		data[ier_constants.Param_Password] = schedule.password;
		data[ier_constants.Param_EmailAddress] = schedule.email;
		
		//report parameters
		params["requestParams"][ier_constants.Param_ReportId] = this.reportData.reportDefinition.id;
		params["requestParams"][ier_constants.Param_ReportName] = this.reportData.reportDefinition.name;
		params["requestParams"][ier_constants.Param_RepositoryId] = repository.id;
		params["requestParams"][ier_constants.Param_P8RepositoryId] = repository.objectStoreName;
		params["requestParams"][ier_constants.Param_ServerName] = repository.serverName;
		params["requestParams"][ier_constants.Param_TableName] = this.reportData.reportDefinition.tableName;
		
		var savedInFolder = this.addItemPane.getSavedInFolder();
		params["requestParams"][ier_constants.Param_SaveInFolderLocation] = savedInFolder.item.id;
		params["requestParams"][ier_constants.Param_SaveInFolderLocationName] = savedInFolder.item.name;
		params["requestParams"][ier_constants.Param_SaveInRepository] = savedInFolder.item.repository.id;
		params["requestParams"][ier_constants.Param_RootDownloadLinkURL] = this.addItemPane.getRootDownloadLinkURL();
		params["requestParams"][ier_constants.Param_ReportTitle] = this.addItemPane.getDocumentTitle() || this.reportTitle;
		params["requestParams"][ier_constants.Param_ClassName] = this.addItemPane.getClassName();

		
		data[ier_constants.Param_Properties] = reportCriteria;
		data[ier_constants.Param_ArchiveProperties] = this.addItemPane.getProperties();
		data[ier_constants.Param_Permissions] = this.addItemPane.getPermissions();
		params["requestBody"] = data;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_ScheduleReportService, ier_constants.PostEncoding, params);
	},
	
	/**
	 * Event invoked when the wizard has finished scheduling a report
	 */
	onFinish: function(response){
		
	}
});});
