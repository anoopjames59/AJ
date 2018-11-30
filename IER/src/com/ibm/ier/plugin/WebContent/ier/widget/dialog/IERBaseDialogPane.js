define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"dijit/layout/ContentPane",
    	"ecm/LoggerMixin",
    	"ecm/widget/HoverHelp",
    	"ier/util/util",
    	"ier/messages"
], function(dojo_declare, dojo_array, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_layout_ContentPane,
		ecm_LoggerMixin, ecm_widget_HoverHelp, ier_util, ier_messages){

/**
 * Base panes for each IER Dialog.  A pane is a collapsable item in any of the dialogs.
 */
return dojo_declare("ier.widget.dialog.IERBaseDialogPane", [ dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	/** @lends ier.widget.dialog.IERBaseDialog.prototype */
	
	widgetsInTemplate: true,
	_messages: ier_messages,
	repository: null,
	
	/**
	 * Whether the pane needs to be validated
	 */
	isValidationRequired: false,
	
	/**
	 * Returns repository
	 * @returns
	 */
	getRepository: function(){
		return this.repository;
	},
	
	/**
	 * Sets repository
	 * @param repository
	 */
	setRepository: function(repository){
		this.repository = repository;
	},
	
	/**
	 * Performs the validation for the pane and returns whether the pane validated successfully or not.  Should be overriden by subclasses to do validation for the pane. 
	 * @returns {Boolean}
	 */
	validate: function() {
		return true;
	},
	
	/**
	 * Returns whether the pane should be validated.  Subclasses should override this method to return the right response for the pane
	 * @returns {Boolean}
	 */
	isValidationRequired: function() {
		return this.isValidationRequired;
	},
	
	/**
	 * Push all widgets to this stack so they will be destroyed automatically.  If not you will need to manually destroy it.  
	 * Do not leave widgets undestroyed.  This will cause memory leaks.
	 */
	addChildWidget: function(widget){
		if(!this._childWidgets)
			this._childWidgets = [];
		
		this._childWidgets.push(widget);
	},
	
	/**
	 * Event that is invoked when a widget has a change in its inputs.  Child panes should trigger this event to get the correct validation in the dialog
	 */
	onInputChange: function(widget) {
		
	},
	
	/**
	 * Only for BaseAccordionContainerWizard
	 * Event that is invoked when the widget pane is selected.
	 */
	onSelected: function(){
		
	},
	
	/**
	 * Event that is invoked when an error occurs
	 */
	onErrorOccurred: function(message, type){
		
	},
	
	/**
	 * Creates a hover help widget with the provided promp text.  Will automatically destroy the widget as well.
	 * @param promptText
	 * @returns {ecm.widget.HoverHelp}
	 */
	createHoverHelp: function(promptText) {
		var hoverHelp = new ecm_widget_HoverHelp({
			message: promptText
		});
		hoverHelp.startup();
		this.addChildWidget(hoverHelp);
		return hoverHelp;
	},
	
	/**
	 * Returns the base URL to use for help pages.
	 * Repeated here because ICN's one is private
	 */
	getHelpUrl: function(forPage) {
		return ier_util.getHelpUrl(forPage);
	},
	
	/**
	 * Creates a HTML link with the provided link and text.  Append this into dialogs or hover helps to create links
	 * @param link
	 * @param text
	 * @returns {String}
	 */
	createHtmlLink: function(link, text){
		return '<a href="' + link + '" target="_blank" title="' + text + '">' + text + '</a>';
	},
	
	/**
	 * Destroys all added widgets.  If they aren't pushed to the _childWidgets, the subclasses must destroy them themselves manually.
	 */
	destroy: function(){
		dojo_array.forEach(this._childWidgets, function(widget){
			if(widget && widget.destroyRecursive)
				widget.destroyRecursive();
		});
		
		this.inherited(arguments);
	}
});});
