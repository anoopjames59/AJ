define([
	"dojo/_base/declare",
	"ecm/model/Action"
], function(dojo_declare, ecm_model_action){

/**
 * @name ier.model.actions.DeclareAction
 * @class An action for declaring a record
 * @augments ecm.model.Action
 */
return dojo_declare("ier.model.actions.DeclareAction", [ecm_model_action], {

	canPerformAction: function(repository, itemList, listType, teamspace, resultSet) {
		var canPerform = this.inherited(arguments);
		if(canPerform){
			if(itemList) {
				//disable declare action if more than one item is selected
				if(itemList.length && itemList.length > 1)
					canPerform = false;
			
				var item = itemList[0];
				
				//disable declare if a folder is selected
				if(item && item.isFolder && item.isFolder())
					canPerform = false;
			}
		}
		
		return canPerform;
	}
});});
