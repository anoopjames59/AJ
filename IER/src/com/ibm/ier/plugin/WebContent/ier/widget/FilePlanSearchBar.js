define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/_base/event",
    	"dojo/keys",
    	"dijit/_Widget",
    	"dijit/form/TextBox",
    	"ecm/Messages",
    	"ier/messages",
    	"dojo/text!./templates/FilePlanSearchBar.html"
], function(dojo_declare, dojo_lang, dojo_event, dojo_keys, dijit_Widget, dijit_form_TextBox, ecm_messages, ier_messages, templateString){

/**
 * @name ier.widget.FilePanSearchBar
 * @class Provides a search bar for IER
 * @augments dijit._Widget, dijit._Templated
 */
return dojo_declare("ier.widget.FilePlanSearchBar", [ dijit_form_TextBox ], {
	/** @lends ier.widget.FilePlanSearchBar.prototype */

	templateString: templateString,
	widgetsInTemplate: false,
	trim: true,
	placeHolder: ier_messages.nameContains,
	attributeMap: dojo_lang.delegate(dijit_Widget.prototype.attributeMap, {
		id: "focusNode",
		tabIndex: "focusNode"
	}),

	messages: ecm_messages,

	postCreate: function() {
		this.inherited(arguments);
				
		this.connect(this.textbox, "onkeydown", dojo_lang.hitch(this, function(e){
			 if(e.keyCode == dojo_keys.ENTER){
				 this._quickSearchButtonClicked();
				 dojo_event.stop(e);
			 }
        }));
	},
	
	//event for clicking on the search button
	onSearchButtonClicked: function(value){
		
	},
	
	_quickSearchButtonClicked: function(evt){
		this.onSearchButtonClicked(this.get("value"));
	},

	_nop: null
});});



