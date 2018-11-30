define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/model/_BaseEntityObject"
], function(dojo_declare, ier_constants, ier_model_BaseEntityObject){

/**
 * @name ier.model.Hold
 * @class Represents a single Hold object on the server.
 * @augments ecm.model._ModelObject
 */
var Hold = dojo_declare("ier.model.Hold", ier_model_BaseEntityObject, {
	/** @lends ecm.model.Hold.prototype */
	
	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
	},

	retrieveEntitiesOnHold: function(){
		
	},
	
	isActive: function() {
		return this.attributes[ier_constants.Property_Active];
	},
	
	getHoldType: function() {
		return this.attributes[ier_constants.Property_HoldType];
	},
	
	getHoldReason: function() {
		return this.attributes[ier_constants.Property_HoldReason];
	},	

	isDynamicHold: function() {
		var conditionXML = this.attributes[ier_constants.Property_ConditionXML];
		return conditionXML != null && conditionXML.length > 0;
	}
});

ier_model_BaseEntityObject.registerClass("Hold", Hold);

return Hold;
});
