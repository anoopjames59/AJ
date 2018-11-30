define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ier/constants",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/EntityItemConditionPane.html",
    	"ier/widget/CommonConditionsForm" //in template
], function(dojo_declare, dojo_lang, ier_constants, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * A basic condition pane that can be attached into any dialogs.  It displays a set of conditions that the user can manipulate.
 */
return dojo_declare("ier.widget.panes.EntityItemConditionPane", [ier_widget_dialog_IERBaseDialogPane], {

	templateString: templateString,
	isValidationRequired: false,
	
	postCreate: function(){
		this.inherited(arguments);
		
		this.connect(this.commonConditionsForm, "onFormValidate", this.onInputChange);
	},
	
	createRendering: function(repository, contentClassName){
		this.repository = repository;
		this.contentClassName = contentClassName;
		
		if(repository){
			this.commonConditionsForm.setRepository(repository);
			this.commonConditionsForm.setClassName(contentClassName);
		}
	},
	
	clearRendering: function() {
		if(this.commonConditionsForm)
			this.commonConditionsForm.clear();
	},
	
	getConditions: function() {
		return this.commonConditionsForm.getConditions();
	},
	
	_setConditionsAttr: function(conditions){
		if(conditions){
			this.commonConditionsForm.setConditions(conditions);
		}
		else {
			if(this.repository){
				this.commonConditionsForm.setClassName(this.contentClassName, true);
				this.commonConditionsForm.renderCondition(this.commonConditionsForm.getCondition(this.contentClassName));
			}
		}
	},

	_getConditionsAttr: function(){
		return this.commonConditionsForm.getConditions();
	},
	
	/**
	 * Require at least one condition to be present.  isValidationRequired has to be true to enable this.
	 */
	validate: function(){
		if(this.currentCondition && this.currentCondition.criteria){
			dojo_array.forEach(this.currentCondition.criteria, function(c){
				if(c.value){
					return true;
				}
				else{
					var operator = c.selectedOperator;
					return (operator == "NULL" || operator == "NOTNULL"); 
				}
			});
		}
		return false;
	}
});});
