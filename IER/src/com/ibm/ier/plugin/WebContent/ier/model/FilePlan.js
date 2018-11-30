define([
	"dojo/_base/declare",
	"ier/model/_BaseEntityObject",
	"ier/model/RecordCategoryContainerMixin"
], function(dojo_declare, ier_model_BaseEntityObject, ier_model_RecordCategoryContainerMixin){

/**
 * @name ier.model.FilePlan
 * @class Represents a single FilePlan instance.
 * @augments ier.model._BaseEntityObject, ier.model.RecordCategoryContainerMixin
 */
var FilePlan = dojo_declare("ier.model.FilePlan", [ier_model_BaseEntityObject, ier_model_RecordCategoryContainerMixin], {
	/** @lends ecm.model.FilePlan.prototype */

	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
		this.namingPattern = null;
	},
	
	setNamingPattern: function(namingPattern){
		this.namingPattern = namingPattern;
	},
	
	getNamingPattern: function(namingPattern){
		return this.namingPattern;
	},
	
	isNamingPatternEnabled: function(){
		return this.namingPattern != null;
	}
});

ier_model_BaseEntityObject.registerClass("FilePlan", FilePlan);

return FilePlan;
});