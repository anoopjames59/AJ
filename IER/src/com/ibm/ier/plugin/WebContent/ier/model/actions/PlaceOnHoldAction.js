define([
	"dojo/_base/declare",
	"ecm/model/Action"
], function(dojo_declare, ecm_model_action){

/**
 * @name ier.model.PlaceOnHoldAction
 * @class Check if able to place on hold
 * @augments ecm.model.Action
 */
return dojo_declare("ier.model.actions.PlaceOnHoldAction", [ecm_model_action], {

	canPerformAction: function(repository, itemList, listType, teamspace, resultSet) {
		var canPerform = this.inherited(arguments);
		if(canPerform){
			// return itemList[0].attributes["OnHold"];
			// TODO 
			// need a flag for "Able to place on hold(s) for this item(record container, record)"
			return canPerform;
		}else{
			return canPerform;
		}
	}
});});
