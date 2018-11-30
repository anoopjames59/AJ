define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dojo/string",
	"dojo/dom-style",
	"dojo/date/locale",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/widget/dialog/BaseAccordionWizardDialog",
	"ier/widget/panes/DDReportSweepPane",
	"ier/widget/panes/AsyncTaskSchedulerPane"
], function(dojo_declare, dojo_lang, dojo_construct, dojo_string, dojo_style, dojo_locale, ecm_model_Desktop, ecm_model_Request, ier_constants, ier_messages, ier_util,
		BaseAccordionWizardDialog, ScheduleDDReportSweepPane, AsyncTaskSchedulerPane){

/**
 * @name ier.widget.dialog.ScheduleDDSweepReportTaskWizard
 * @class The dialog schedules a DD Report Sweep 
 * @augments ier.widget.dialog.BaseAccordionWizardDialog
 */
return dojo_declare("ier.widget.dialog.ScheduleDDSweepReportTaskWizard", [BaseAccordionWizardDialog], {
	/** @lends ier.widget.dialog.ScheduleDDContainerConversionWizard.prototype */

	finishButtonLabel: ier_messages.scheduleDDReportSweep_scheduleSweep,
	task: null,

	postCreate: function(){
		this.logEntry("postCreate");
		
		this.inherited(arguments);
		
		this.set("title", ier_messages.scheduleDDReportSweepWizard_title);
		this.setIntroText(ier_messages.scheduleDDReportSweepWizard_description);
		this.setResizable(true);

		this.scheduleDDReportSweepPane = new ScheduleDDReportSweepPane();
		this.addChildPane(this.scheduleDDReportSweepPane);
		
		//schedule dd container pane
		this.connect(this.scheduleDDReportSweepPane, "onInputChange", function(){
			if(this.scheduleDDReportSweepPane.validate()){
				this.enablePane(this.taskSchedulerPane);
			}
			else {
				this.disablePane(this.taskSchedulerPane);
			}
			this.updateButtonsState();
		});
		
		//schedule pane
		this.taskSchedulerPane = new AsyncTaskSchedulerPane({
			disabled : true,
			showLoginInformation: true
		});
		this.addChildPane(this.taskSchedulerPane);

		this.startup();
		this.logExit("postCreate");
	},
	
	/**
	 * Sets the defensible schedule item for this pane
	 */
	_setScheduleAttr: function(schedule) {
		var asyncTaskSchedulerPane = this.taskSchedulerPane.asyncTaskSchedulerPane;
		ier_util.setTaskSchedulerPane(asyncTaskSchedulerPane, schedule, true);
	},
	
	/**
	 * Sets the properties for the dd sweep report pane
	 */
	_setPropertiesAttr: function(properties) {
		this.scheduleDDReportSweepPane.set("properties", properties);
	},
	
	/**
	 * Shows the AddActionDialog
	 * @param repository
	 */
	show: function(repository, item){
		this.inherited("show", []);
		this.logEntry("show()");
		
		this.scheduleDDReportSweepPane.createRendering(repository, item);
		
		this.logExit("show()");
	},
	
	onFinishButtonClicked: function(){
		var reportSweepProperties = this.scheduleDDReportSweepPane.get("properties");
		var params = {};
		var repository = this.scheduleDDReportSweepPane.repository;
		params[ier_constants.Param_RepositoryId] = repository.id;
		params[ier_constants.Param_P8RepositoryId] = repository.objectStoreName;
		params[ier_constants.Param_ContainerId] = reportSweepProperties.containerIds;
		params[ier_constants.Param_ContainerName] = reportSweepProperties.containerNames;
		params[ier_constants.Param_CE_EJB_URL] = repository.serverName;
		
		params["advancedDays"] = reportSweepProperties.advancedDays;
		params["reportOnly"] = reportSweepProperties.reportOnly;
		params["connectionPointName"] = reportSweepProperties.connectionPoint;
		params["needApproval"] = reportSweepProperties.needApproval;
		params["containerToDeclareRecordTo"] = reportSweepProperties.containerToDeclareRecordId;
		params["defensibleSweepAlwaysDeclareRecord"] = repository.defensibleSweepSettings.defensibleSweepAlwaysDeclareRecord;
		params["defensibleSweepAlwaysShowDeclareResult"] = repository.defensibleSweepSettings.defensibleSweepAlwaysShowDeclareResult;

		params["defensibleDisposalWorkflowId"] = reportSweepProperties.defensibleDisposalWorkflowId;
		for(var i in ecm_model_Desktop.defensibleSweepSettings){
			params[i] = ecm_model_Desktop.defensibleSweepSettings[i];
		}
		params[ier_constants.Param_RootDownloadLinkURL] = ier_util.getRootDownloadLinkURL(repository);
		params[ier_constants.Param_ParameterList] = ier_constants.Param_ContainerName + "," + "advancedDays" + "," + "reportOnly" 
				+ "," + "connectionPointName" + "," + "needApproval" + "," + "containerToDeclareRecordTo";                 
		params["parent"] = "IER";
		
		var taskSchedule = this.taskSchedulerPane.get("schedule");
		
		if(!this.task){
			ecm.model.desktop.taskManager.scheduleAsyncTask(taskSchedule, ier_constants.TaskType_DDReportSweepClass, params, null, 
					dojo_lang.hitch(this, function(response) {
				this.onFinish(response);
				ecm_model_Desktop.taskManager.onAsyncTaskItemAdded(response);
				this.onCancel();
			}));
		}
		else {
			var taskRequest = this.task.taskRequest;
			taskRequest[ier_constants.Param_UserId] = taskSchedule.username;
			taskRequest[ier_constants.Param_Password] = taskSchedule.password;
			taskRequest[ier_constants.Param_EmailAddress] = taskSchedule.email;
			taskRequest[ier_constants.Param_Name2] = taskSchedule.name;
			taskRequest[ier_constants.Param_Description2] = taskSchedule.description;
			dojo_declare.safeMixin(taskRequest.specificTaskRequest, params);
			
			this.task.reschedule(dojo_lang.hitch(this, function(response){
				this.onFinish(response);
				this.task.refresh();
				this.onCancel();
			}), taskRequest);
		}
	},
	
	/**
	 * Event invoked when the wizard has finished scheduling a report
	 */
	onFinish: function(response){
		
	}
});});
