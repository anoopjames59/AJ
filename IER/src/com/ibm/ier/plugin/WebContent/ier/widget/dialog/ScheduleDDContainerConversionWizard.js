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
	"ier/widget/panes/ConvertDefensibleDisposalDispositionPane",
	"ier/widget/panes/AsyncTaskSchedulerPane"
], function(dojo_declare, dojo_lang, dojo_construct, dojo_string, dojo_style, dojo_locale, ecm_model_Desktop, ecm_model_Request, ier_constants, ier_messages, ier_util,
		BaseAccordionWizardDialog, ConvertDefensibleDisposalDispositionPane, AsyncTaskSchedulerPane){

/**
 * @name ier.widget.dialog.ScheduleDDContainerConversionWizard
 * @class The dialog schedules a DD container conversion
 * @augments ier.widget.dialog.BaseAccordionWizardDialog
 */
return dojo_declare("ier.widget.dialog.ScheduleDDContainerConversionWizard", [BaseAccordionWizardDialog], {
	/** @lends ier.widget.dialog.ScheduleDDContainerConversionWizard.prototype */

	finishButtonLabel: ier_messages.scheduleDDConversionWizard_finishButton,
	
	task: null,

	postCreate: function(){
		this.inherited(arguments);
		
		this.set("title", ier_messages.scheduleDDConversionWizard_title);
		this.setIntroText(ier_messages.scheduleDDConversionWizard_description);
		this.setResizable(true);

		this.convertDDSchedulePane = new ConvertDefensibleDisposalDispositionPane();
		this.addChildPane(this.convertDDSchedulePane);
		
		//convert dd container pane
		this.connect(this.convertDDSchedulePane, "onInputChange", function(){
			if(this.convertDDSchedulePane.validate()){
				this.enablePane(this.taskSchedulerPane);
				this._setNameAndDescriptionTextBox();
			}
			else {
				this.disablePane(this.taskSchedulerPane);
			}
			this.updateButtonsState();
		});
		
		//schedule pane
		this.taskSchedulerPane = new AsyncTaskSchedulerPane({
			disabled : true,
			showLoginInformation: true,
			title: ier_messages.scheduleDDConversionWizard_setConversionSchedule
		});
		this.addChildPane(this.taskSchedulerPane);
		this.taskSchedulerPane.asyncTaskSchedulerPane.enableRecurrenceRadioButton.set("disabled", true);
		this.taskSchedulerPane.asyncTaskSchedulerPane.intervalNumberSpinner.set("disabled", true);
		this.taskSchedulerPane.asyncTaskSchedulerPane.intervalSelect.set("disabled", true);
		dojo_style.set(this.taskSchedulerPane.asyncTaskSchedulerPane.reoccurenceRow, "display", "none");
		dojo_style.set(this.taskSchedulerPane.asyncTaskSchedulerPane.reoccurenceIntervalRow, "display", "none");

		this.startup();
	},
	
	_setNameAndDescriptionTextBox: function(){
		this.taskSchedulerPane.asyncTaskSchedulerPane.nameTextBox.set("value", dojo_string.substitute(ier_messages.scheduleDDConversionWizard_scheduleName,
			[this.convertDDSchedulePane.folder.name]));
		this.taskSchedulerPane.asyncTaskSchedulerPane.descriptionTextBox.set("value", dojo_string.substitute(ier_messages.scheduleDDConversionWizard_scheduleDescription,
			[this.convertDDSchedulePane.folder.name, dojo_locale.format(new Date(), {
				locale: this.locale,
				selector: "time",
				formatLength: "short"
			})]));
	},
	
	/**
	 * Sets the defensible schedule item for this pane
	 */
	_setTaskScheduleAttr: function(schedule) {
		var asyncTaskSchedulerPane = this.taskSchedulerPane.asyncTaskSchedulerPane;
		ier_util.setTaskSchedulerPane(asyncTaskSchedulerPane, schedule, true);
	},
	
	_setScheduleAttr: function(schedule) {
		this.convertDDSchedulePane.set("schedule", schedule);
	},
	
	/**
	 * Shows the AddActionDialog
	 * @param repository
	 */
	show: function(repository, item){
		this.inherited("show", []);
		this.logEntry("show()");
		
		this.convertDDSchedulePane.createRendering(repository, item);
		
		this.logExit("show()");
	},
	
	onFinishButtonClicked: function(){
		var params = {};
		params[ier_constants.Param_RepositoryId] = this.convertDDSchedulePane.repository.id;
		params[ier_constants.Param_P8RepositoryId] = this.convertDDSchedulePane.repository.objectStoreName;
		params[ier_constants.Param_ContainerId] = this.convertDDSchedulePane.folder.id;
		params[ier_constants.Param_ContainerName] = this.convertDDSchedulePane.folder.name;
		var schedule = this.convertDDSchedulePane.get("schedule");
		params[ier_constants.Param_RetentionTriggerPropertyName] = schedule.getRMRetentionTriggerPropertyName();
		params[ier_constants.Param_RetentionPeriodYears] = schedule.getRMRetentionPeriod("years");
		params[ier_constants.Param_RetentionPeriodMonths] = schedule.getRMRetentionPeriod("months");
		params[ier_constants.Param_RetentionPeriodDays] = schedule.getRMRetentionPeriod("days");
		params[ier_constants.Param_RetentionPeriod] = schedule.getRMRetentionPeriod();
		params[ier_constants.Param_CE_EJB_URL] = this.convertDDSchedulePane.repository.serverName;
		params[ier_constants.Param_ParameterList] = ier_constants.Param_ContainerName + "," + ier_constants.Param_RetentionTriggerPropertyName 
				+ "," + ier_constants.Param_RetentionPeriodYears + "," + ier_constants.Param_RetentionPeriodMonths + "," + ier_constants.Param_RetentionPeriodDays;                 
		params["parent"] = "IER";
		var taskSchedule = this.taskSchedulerPane.get("schedule");
		
		if(!this.task){
			ecm.model.desktop.taskManager.scheduleAsyncTask(taskSchedule, ier_constants.TaskType_DDConversionClass, params, null, 
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
