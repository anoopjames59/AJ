define([
    	"dojo/_base/declare",
    	"dojo/dom-construct",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/widget/panes/AdvanceRetentionDispositionPane",
    	"ier/widget/panes/SimpleRetentionDispositionPane",
    	"dojo/text!./templates/AddScheduleDispositionPane.html", 
    	"dijit/form/Select", // in template
    	"dijit/layout/ContentPane" // in template
], function(dojo_declare, dojo_construct, ier_constants, ier_messages, ier_widget_dialog_IERBaseDialogPane,
		ier_widget_panes_AdvanceRetentionDispositionPane, ier_widget_panes_SimpleRetentionDispositonPane, templateString){

/**
 * The disposition pane for the add disposition dialog
 * 
 * @name ier.widget.AddScheduleDispositionPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.AddScheduleDispositionPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.AddScheduleDispositionPane */

	templateString: templateString,
	constants: ier_constants,
	messages: ier_messages,
	_parentFolder: null,
	_currentRetentionPane: null,
	retentionModeValue: ier_constants.DispositionMode_SimpleRetention,

	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		this.connect(this._modeSelector, "onChange", function(value) {
			this.retentionModeValue = value;
			
			if(value == ier_constants.DispositionMode_SimpleRetention){
				this._showSimpleRetentionPane();
			} else {
				this._showAdvancedRetentionPane();
			}
		});
		
		this.logExit("postCreate()");
	},
	
	_showSimpleRetentionPane: function(){
		if(!this.simpleRetentionDispositionPane){
			this.simpleRetentionDispositionPane = new ier_widget_panes_SimpleRetentionDispositonPane();
			this.simpleRetentionDispositionPane.createRendering();
		}
		this._currentRetentionPane = this.simpleRetentionDispositionPane;
		dojo_construct.place(this.simpleRetentionDispositionPane.domNode, this.dispositionModePane.domNode, "only");

	},
	
	_showAdvancedRetentionPane: function() {
		if(!this.advanceRetentionDispositionPane){
			this.advanceRetentionDispositionPane = new ier_widget_panes_AdvanceRetentionDispositionPane();
			this.advanceRetentionDispositionPane.createRendering();
		}
		this._currentRetentionPane = this.advanceRetentionDispositionPane;
		dojo_construct.place(this.advanceRetentionDispositionPane.domNode, this.dispositionModePane.domNode, "only");
	},

	/**
	 * Creates the rendering for the pane
	 * @param repository
	 */
	createRendering: function(repository) {
		this.logEntry("createRendering()");
		
		if(this.retentionModeValue == ier_constants.DispositionMode_SimpleRetention)
			this._showSimpleRetentionPane();
		else {
			_showAdvancedRetentionPane();
		}
		this._modeSelector.value = this.retentionModeValue;
		
		this.logExit("createRendering()");
	},
	
	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		return this._currentRetentionPane.validate();
	},
	
	getSavedInFolder: function() {
		return this._saveInFolder;
	},
	
	getRetentionPane: function() {
		return this._currentRetentionPane;
	},
	
	getRetentionMode: function(){
		return this.retentionModeValue;
	}
});});