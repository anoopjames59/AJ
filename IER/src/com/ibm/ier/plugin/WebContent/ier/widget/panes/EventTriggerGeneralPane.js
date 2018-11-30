define([
    	"dojo/_base/declare",
    	"ier/constants",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/EventTriggerGeneralPane.html",
    	"ecm/widget/HoverHelp", //in templates
    	"dijit/form/Select" //in templates
], function(dojo_declare, ier_constants, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * The general properties pane for Event Triggers
 * 
 * @name ier.widget.EventTriggerGeneralPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.EventTriggerGeneralPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.EventTriggerGeneralPane */

	templateString: templateString,

	constants: ier_constants,
	
	triggerModeType: ier_constants.EventType_InternalEventTrigger,
	
	/**
	 * Creates the panel
	 */
	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		this.connect(this.typeSelector, "onChange", function(value) {
			this.triggerModeType = value;
			this.onTriggerModeChange(value);
		});
				
		this.eventTriggerHoverHelp.message = this._messages.eventTriggerDialog_hoverHelp;
		
		this.logExit("postCreate()");
	},

	/**
	 * Creates the rendering for the pane
	 * @param repository
	 * @param isReadOnly
	 */
	createRendering: function(parameters) {
		this.logEntry("createRendering()");
		
		this.isReadOnly = parameters.isReadOnly;
		this.typeSelector.set("disabled", this.isReadOnly);
		
		this.logExit("createRendering()");
	},
	
	/**
	 * Event invoked when the trigger mode changes
	 */
	onTriggerModeChange: function(mode){
		
	},
	
	/**
	 * Returns the trigger mode type
	 */
	getTriggerModeType: function(){
		return this.triggerModeType;
	},
	
	isValidationRequired: function() {
		return false;
	}
});});
