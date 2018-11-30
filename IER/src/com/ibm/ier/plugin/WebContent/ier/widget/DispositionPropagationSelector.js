define([
    	"dojo/_base/declare",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"ecm/LoggerMixin",
    	"ier/messages",
    	"ier/constants",
    	"dojo/text!./templates/DispositionPropagationSelector.html",
    	"dijit/form/Select", // in template
    	"dijit/layout/ContentPane", // in template
    	"ecm/widget/HoverHelp" // in template
], function(dojo_declare, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin, ier_messages, ier_constants, templateString){

/**
 * @name ier.widget.DispositionPropagationSelector
 * @class
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget.DispositionPropagationSelector", [ dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin ], {
	/** @lends ier.widget.DispositionPropagationSelector.prototype */

	templateString: templateString,

	widgetsInTemplate: true,
	constants: ier_constants,
	messages: ier_messages,

	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);
		
		//change the propagation hover help according to the new disposition level selected
		this.connect(this._dispositionPropagationSelect, "onChange", function(value){
			this._setPropagationHoverHelp();
		});
		
		this._setPropagationHoverHelp();
	},

	/**
	 * Event invoked when the select is changed
	 */
	onChange: function(){
		
	},
	
	_setPropagationHoverHelp: function(){
		var level = this._dispositionPropagationSelect.get("value");
		if(level == ier_constants.SchedulePropagation_None){
			this.propagationHoverHelp.set("message", ier_messages.entityItemDispositionPane_noPropagationHoverHelp);
		} else if(level == ier_constants.SchedulePropagation_ToAllInheritingEntities){
			this.propagationHoverHelp.set("message", ier_messages.entityItemDispositionPane_allInheritorsHoverHelp);
		} else if(level == ier_constants.SchedulePropagation_ToImmediateSubContainersAndInheritingEntities) {
			this.propagationHoverHelp.set("message", ier_messages.entityItemDispositionPane_allImmediateSubcontainersAndInheritorsHoverHelp);
		}
		 else if(level == ier_constants.SchedulePropagation_ToAllNonAssignedSubContainers) {
			this.propagationHoverHelp.set("message", ier_messages.entityItemDispositionPane_allNonAssignedSubcontainersHoverHelp);
		}
	},
	
	_getDisabledAttr: function() {
		return this._dispositionPropagationSelect.get("disabled");
	},
	
	_setDisabledAttr: function(disabled) {
		this.propagationHoverHelp.set("disabled", disabled);
		this._dispositionPropagationSelect.set("disabled", disabled);
	},
	
	_getValueAttr: function(){
		return this._dispositionPropagationSelect.get("value");
	},
	
	_setValueAttr: function(level){
		this._dispositionPropagationSelect.set("value", level);
	},
	
	_nop: null
});});
