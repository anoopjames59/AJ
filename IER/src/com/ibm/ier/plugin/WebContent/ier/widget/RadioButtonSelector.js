define([
    	"dojo/_base/declare",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"ecm/LoggerMixin",
    	"ecm/widget/_SinglePropertyEditorMixin",
    	"dojo/text!./templates/RadioButtonSelector.html",
    	"dijit/form/RadioButton" // in template
], function(dojo_declare, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin, ecm_widget_SinglePropertyEditorMixin, 
		templateString){

/**
 * @name RadioButtonSelector
 * @class This widget displays radio buttons.
 * @augments none
 * TODO: Fix this class.  It is poorly done.  It's not dynamic at all and everything is hard coded.  Values and names of the radio buttons are not
 * set correctly either.
 */
return dojo_declare("ier.widget.RadioButtonSelector", [ecm_LoggerMixin, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_widget_SinglePropertyEditorMixin], {
	/** @lends ier.widget.RadioButtonSelector.prototype */

	templateString: templateString,
	widgetsInTemplate: true,	

	postCreate: function() {
		this.logEntry("postCreate");		
		this.inherited(arguments);
		this.logExit("postCreate");
	},
	
	_setLabelAttr: function(content) {
		this._legend.innerHTML = content;
	},

	setLabels: function(labels) {
		this._labels = labels;
	},
	
	createRendering: function() {
		this.logEntry("createRendering");	
		
		if (this._labels) {
			this.button1Label.innerHTML = this._labels[0];
			this.button2Label.innerHTML = this._labels[1];
		}

		this.logExit("createRendering");
	},
	
	getValue: function() {
		return "";
	},
	
	_nop: null
});});


