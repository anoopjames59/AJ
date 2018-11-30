define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/json",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"dojo/string",
    	"ecm/model/Request",
    	"ecm/widget/dialog/ConfirmationDialog",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/util/dialog",
    	"ier/widget/dialog/ObjectListDialog"
], function(dojo_declare, dojo_array, dojo_json, dojo_lang, dojo_dom_class, dojo_string, ecm_model_Request,
		ecm_widget_dialog_ConfirmationDialog, ier_constants, ier_messages, ier_util, ier_util_dialog, ier_dialog_ObjectListDialog){

return dojo_declare("ier.widget.dialog.PlaceOnHoldDialog", [ier_dialog_ObjectListDialog], {

	//boolean whether to handle the double click
	connectonRowDblClick: true,

	postCreate: function(){
		this.inherited(arguments);

		this._objectContentList.multiSelect = true;
		this.title = ier_messages.objectSelector_entitiesOnHold;
		this.setIntroText(ier_messages.objectSelector_placeOnHoldsIntroText);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh19.htm"));
		this.setEntityType(ier_constants.EntityType_Hold);
		this._selectButton.set("label",ier_messages.dialog_placeOnHoldButton);
	},

	show: function(repository, items){
		if(repository){
			this.contentClass = repository.getContentClass(ier_constants.ClassName_Hold);
		}
		this._targetItems = items;

		this.inherited(arguments, [repository, ier_constants.ClassName_Hold]);
	},

	onSelect: function(selectedItems){
		if(this._targetItems && selectedItems && selectedItems.length > 0){
			var targetItems = this._targetItems;
			var params = ier_util.getDefaultParams(this.repository, function(response){
				dojo_array.forEach(targetItems, function(item){
					item.privIERDelete = false;
					item.retrieveAttributes(null, false); // set backgroundRequest to false to show the progress dialog 
				});
			});
			var holdables = dojo_array.map(targetItems, function(item){
				return {id: item.id, entityType: item.getEntityType()};
			});
			params.requestParams[ier_constants.Param_Holdables] = dojo_json.toJson(holdables);
			var holds = dojo_array.map(selectedItems, function(item){
				return {id: item.id};
			});
			params.requestParams[ier_constants.Param_Holds] = dojo_json.toJson(holds);
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_PlaceOnHold, ier_constants.PostEncoding, params);
		}
	},

	onInputChange: function(){
		var selectedObjects = this._objectContentList.getSelectedItems();
		var valid = selectedObjects != null && selectedObjects.length > 0;
		if(valid){
			/*var dynamidHoldIds = [];
			dojo_array.forEach(this._objectContentList.getSelectedItems(), function(item){
				if(item && item.isDynamicHold()){
					dynamidHoldIds.push(item.id);
				}
			});
			if(dynamidHoldIds.length > 0){
				var selectRow = this._objectContentList.getGridModule("select").row;
				selectRow.deselectById.apply(selectRow, dynamidHoldIds);
				return this.inherited(arguments);
			}*/
		}
		this.setButtonEnabled(this._selectButton, valid);
		return valid;
	},

	retrieveObjects: function(){
		if(!this.repository.isIERLoaded()){
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(){
				if(this.repository.isIERLoaded()){
					this.retrieveObjects();
				}
			}));
			return;
		}
		this.repository.retrieveObjects(this._objectClass, this._filterString, dojo_lang.hitch(this, function(resultSet){
			if(resultSet){
				var items = resultSet.items;
				if(items){
					// filter holds
					var currentHolds = [];
					dojo_array.forEach(this._targetItems, function(item){
						dojo_array.forEach(item.getValues(ier_constants.Property_Holds), function(hold){
							if(hold && dojo_array.indexOf(currentHolds, hold) < 0){
								currentHolds.push(hold);
							}
						});
					});
					items = dojo_array.filter(items, function(item){
						var active = item.getValue(ier_constants.Property_Active);
						if(!active || active == "false"){
							return false;
						}
						var name = item.getValue(ier_constants.Property_HoldName);
						var filterFlag = dojo_array.indexOf(currentHolds, name) < 0;
						if(filterFlag){
							item.inPlaceOnHold = true;
						}
						return filterFlag;
					});
					resultSet.items = items;
				}

				var structure = resultSet.structure;
				if(structure){
					var cells = structure.cells && structure.cells[0];
					if(cells){
						cells.splice(4, 1); // remove "Active" column
					}
				}
			}

			this._objectContentList.setResultSet(resultSet);
			this.resize();
			this.onInputChange();
		}), true);
	}
});});