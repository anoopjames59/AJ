define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"dojo/text!./templates/AdminCheckBox.html",
    	"dijit/form/CheckBox" // in template
], function(dojo_declare, dojo_lang, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, templateString){
	
/**
 * @name ier.widget.admin.AdminCheckBox
 * @class Provides a checkbox for AdminGrid
 * @augments dijit._Widget, dijit._Templated
 */
return dojo_declare("ier.widget.admin.AdminCheckBox", [  dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin ], {
	/** @lends ier.widget.admin.AdminCheckBox.prototype */

	templateString: templateString,
	widgetsInTemplate: true,
	label: "",
	
	attributeMap: dojo_lang.delegate(dijit_Widget.prototype.attributeMap, {
		id: "focusNode",
		tabIndex: "focusNode"
	}),
	
	postCreate: function() {
		this.inherited(arguments);
		
	},
	
	/**
	 * Event fired when the checkbox is clicked
	 * @param checkbox
	 * @param state
	 */
	onClickAction: function(checkbox, state){
		
	},
	
	onClick: function(evt){
		this.onClickAction(this._checkbox);
	},
	
	get: function(name){
		return this._checkbox.get(name);
	},
	
	set: function(name, value){
		this._checkbox.set(name, value);
	},
	
	_nop: null
});});



