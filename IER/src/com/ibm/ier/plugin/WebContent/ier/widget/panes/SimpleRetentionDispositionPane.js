define([
    	"dojo/_base/declare",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/SimpleRetentionDispositionPane.html",
    	"dijit/form/NumberTextBox", // in template
    	"dijit/form/Select", // in template
    	"ecm/widget/HoverHelp" // in template
], function(dojo_declare, ier_constants, ier_messages, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * The simple disposition pane
 * 
 * @name ier.widget.panes.SimpleRetentionDispositionPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.SimpleRetentionDispositionPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.SimpleRetentionDispositionPane */

	templateString: templateString,
	messages: ier_messages,
	constants: ier_constants,

	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		this.logExit("postCreate()");
	},

	/**
	 * Creates the rendering for the pane
	 * @param repository
	 */
	createRendering: function(repository) {
		this.logEntry("createRendering()");
		
		this.logExit("createRendering()");
	},
	
	getDispositionEventOffset: function(){
		return this.offset_years.get("value") + ";" + this.offset_months.get("value") + ";" + this.offset_days.get("value");
	},
	
	getDispositionCutoffBase: function(){
		return this._cutoffBaseSelector.get("value");
	},
	
	getDispositionAggregation: function(){
		return this._aggregationSelector.get("value");
	},
	
	getDispositionCutoffAction: function(){
		return this._actionSelector.get("value");
	},
	
	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		if(this.offset_years.get("value") == null || this.offset_months.get("value") == null || this.offset_days.get("value") == null)
			return false;
		else
			return true;
	}
});});
