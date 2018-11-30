define([
	"dojo/_base/declare",
	"ecm/model/Action",
	"ier/util/util"
], function(dojo_declare, ecm_model_action, util){

/**
 * @name ier.model.RemoveHoldOnViewEntitiesAction
 * @class Check if the the dynamic hold is applied manually or hold sweep
 * @augments ecm.model.Action
 */
return dojo_declare("ier.model.actions.RemoveHoldOnViewEntitiesAction", [ecm_model_action], {

	canPerformAction: function(repository, itemList, listType, teamspace, resultSet) {
		var canPerform = this.inherited(arguments);
		if(canPerform){
			var isDynamicHold = util.getRealItem(itemList[0]).attributes["IsDynamicHold"];
			return !isDynamicHold;
		}else{
			return canPerform;
		}
	}
});});
