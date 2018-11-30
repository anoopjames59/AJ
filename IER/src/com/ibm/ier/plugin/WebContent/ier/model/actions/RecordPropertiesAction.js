define([
	"dojo/_base/declare",
	"ecm/model/Action"
], function(dojo_declare, ecm_model_action){

/**
 * @name ier.model.actions.RecordPropertiesAction
 * @class An action for viewing record properties from a document declared as a record in IBM Content Navigator
 * @augments ecm.model.Action
 */
return dojo_declare("ier.model.actions.RecordPropertiesAction", [ecm_model_action], {

	canPerformAction: function(repository, itemList, listType, teamspace, resultSet) {
		var canPerform = this.inherited(arguments);
		if(canPerform){
			if(itemList) {
				//disable declare action if more than one item is selected
				if(itemList.length && itemList.length > 1)
					canPerform = false;
			
				var item = itemList[0];
				
				//only enable view record properties if the document is declared as a record
				if(item.declaredAsRecord)
					canPerform = true;
				else
					canPerform = false;
			}
		}
		
		return canPerform;
	}
});});
