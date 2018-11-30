define([
    	"dojo/_base/declare",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/AdvanceRetentionDispositionPane.html",
    	"dijit/form/Button", // in template
    	"dijit/form/Select", // in template
    	"dijit/form/ValidationTextBox" // in template
], function(dojo_declare, ier_widget_dialog_IERBaseDialogPane, templateString){
	
/**
 * The advance disposition pane
 * 
 * @name ier.widget.panes.AdvanceRetentionDispositionPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.AdvanceRetentionDispositionPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.AdvanceRetentionDispositionPane */

	templateString: templateString,

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
		
	},
	
	isValidationRequired: function() {
		return false;
	}
});});
