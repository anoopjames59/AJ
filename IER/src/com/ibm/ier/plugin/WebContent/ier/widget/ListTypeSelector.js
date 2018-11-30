define([
    	"dojo/_base/declare",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"ecm/LoggerMixin",
    	"ecm/Messages",
    	"dojo/text!./templates/ListTypeSelector.html",
    	"dijit/form/Select" // in template
], function(dojo_declare, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin, ecm_messages, templateString){

/**
 * @name ier.widget.ListTypeSelector
 * @class
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget.ListTypeSelector", [ dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin ], {
	/** @lends ier.widget.ListTypeSelector.prototype */

	templateString: templateString,

	widgetsInTemplate: true,
	
	options: null,

	/**
	 * Handle to the ecm.messages object
	 */
	messages: null,

	constructor: function(options) {
		this.messages = ecm_messages;
		this.options = options;
	},

	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);
		this.connect(this.listTypeSelection, "onChange", "onChange");
		
		for (var i = 0; i < this.options.length; i++)
		{
			var option = this.options[i];
			this._addSelectOption(option.text, option.value, option.isSelected);
		}

		this.logExit("postCreate");
	},
	
	_addSelectOption: function(text, value, isSelected) 
	{
	    if (this.listTypeSelection)
	    {
	    	var option = document.createElement("option");
	    	option.value = value;
	    	option.label = text;
	    	if(isSelected)
	    		option.selected = isSelected;
	    	this.listTypeSelection.addOption(option);
	    }
	},

	/**
	 * What you got?
	 */
	getSelection: function() {
		this.logEntry("getSelection", "value is= ", this.listTypeSelection.get("value"));

		return this.listTypeSelection.get("value");
	},

	/**
	 * You can use dojo.connect to hook up to onChange event from internal select control but this is easier.
	 */
	onChange: function() {
	},

	_nop: null
});});
