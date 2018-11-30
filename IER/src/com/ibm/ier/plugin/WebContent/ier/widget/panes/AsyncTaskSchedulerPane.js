define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/date/stamp",
    	"dojo/aspect",
    	"dijit/registry",
    	"ecm/Messages",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ecm/widget/taskManager/AsyncTaskSchedulerPane",
    	"dojo/text!./templates/AsyncTaskSchedulerPane.html",
    	"dojo/text!./templates/AsyncTaskSchedulerPaneContent.html"
], function(dojo_declare, dojo_lang, dojo_construct, dojo_style, dojo_date_stamp, dojo_aspect, dijit_registry, ecm_messages, ier_constants, ier_messages, ier_widget_dialog_IERBaseDialogPane, 
		ecm_widget_AsyncTaskSchedulerPane, templateString, asyncTaskSchedulerContentPaneString){

/**
 * The task's scheduler pane
 * 
 * @name ier.widget.panes.AsyncTaskSchedulerPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.AsyncTaskSchedulerPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.ReportEntryFormPane */

	templateString: templateString,
	constants: ier_constants,
	messages: ecm_messages,
	ier_messages: ier_messages,
	repository: null,
	title: ier_messages.setSchedule,
	
	/**
	 * whether to show login information or not
	 */
	showLoginInformation: false,
	
	asyncTaskSchedulerPane : null,
	
	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		var pane = null;
		pane = new ecm_widget_AsyncTaskSchedulerPane({
			showLoginInformation: this.showLoginInformation,
			templateString: asyncTaskSchedulerContentPaneString,
			ier_messages: ier_messages
		});
		this.asyncTaskSchedulerPane = pane;
		dojo_construct.place(this.asyncTaskSchedulerPane.domNode, this.domNode);
		this.asyncTaskSchedulerPane.startImmediatelyCheckbox.set("checked", true);
		this.asyncTaskSchedulerPane.startTimeDateTimeTextBox.set("disabled", true);
		this.asyncTaskSchedulerPane.startTimeDateTimeTextBox.dateTextBox.set("label", ier_messages.startDate);
		this.asyncTaskSchedulerPane.startTimeDateTimeTextBox.timeTextBox.set("label", ier_messages.startTime);
		this.asyncTaskSchedulerPane.endTimeDateTimeBox.dateTextBox.set("label", ier_messages.endDate);
		this.asyncTaskSchedulerPane.endTimeDateTimeBox.timeTextBox.set("label", ier_messages.endTime);
		this.asyncTaskSchedulerPane.descriptionTextBox.set("required", false);
		this.asyncTaskSchedulerPane.emailAdressTextBox.set("required", false);
		
		//remove this once reset code for asynctaskscheduler pane is applied
		dojo_aspect.after(this.asyncTaskSchedulerPane, "disableLoginPane", dojo_lang.hitch(this, function(){
			if (this.asyncTaskSchedulerPane.usernameTextBox.get("disabled") == true) {
				this.asyncTaskSchedulerPane.usernameTextBox.reset();
				this.asyncTaskSchedulerPane.passwordTextBox.reset();
			}
		}));
		
		this.connect(this.asyncTaskSchedulerPane, "onInputChange", "onInputChange");
		
		this.logExit("postCreate()");
	},

	/**
	 * Creates the rendering for the pane
	 */
	createRendering: function(repository) {
		this.logEntry("createRendering()");
					
		this.asyncTaskSchedulerPane.requireLoginInformation = true;
		this.asyncTaskSchedulerPane.validateLoginPane();
		this.asyncTaskSchedulerPane.onInputChange();
		
		this.logExit("createRendering()");
	},
	
	_getScheduleAttr: function() {
		return this.asyncTaskSchedulerPane.get("schedule");
	},
	
	_setScheduleAttr: function(schedule) {
		this.asyncTaskSchedulerPane.set("schedule", schedule);
	},
	
	getAsyncTaskName: function(){
		return this.asyncTaskSchedulerPane.nameTextBox.get("value");
	},
	
	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		return this.asyncTaskSchedulerPane.validate();
	}
});});