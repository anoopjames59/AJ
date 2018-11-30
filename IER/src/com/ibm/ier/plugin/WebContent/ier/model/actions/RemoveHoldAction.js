define([
	"dojo/_base/declare",
	"ecm/model/Action",
	"ier/util/util"
], function(dojo_declare, ecm_model_action, util){

/**
 * @name ier.model.RemoveHoldAction
 * @class Check if the target item has hold
 * @augments ecm.model.Action
 */
return dojo_declare("ier.model.actions.RemoveHoldAction", [ecm_model_action], {

	canPerformAction: function(repository, itemList, listType, teamspace, resultSet) {
		var canPerform = this.inherited(arguments);
		if(canPerform){
			return util.getRealItem(itemList[0]).attributes["OnHold"];
		}else{
			return canPerform;
		}
	}
});});
