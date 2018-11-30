define([
	"dojo/_base/declare",
	"ecm/model/Action"
], function(dojo_declare, ecm_model_action){

/**
 * @name ier.model.InitiateRemoveHoldRequestAction
 * @class Check the hold sweep state
 * @augments ecm.model.Action
 */
return dojo_declare("ier.model.actions.InitiateRemoveHoldRequestAction", [ecm_model_action], {

	canPerformAction: function(repository, itemList, listType, teamspace, resultSet) {
		var canPerform = this.inherited(arguments);
		if(canPerform){
			return itemList[0].attributes["SweepState"] == 1 ? true: false;
		}else{
			return canPerform;
		}
	}
});});
