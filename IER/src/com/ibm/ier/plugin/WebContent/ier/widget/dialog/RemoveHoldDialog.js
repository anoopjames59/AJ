define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/json",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"dojo/string",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ecm/widget/dialog/ConfirmationDialog",
    	"ecm/widget/dialog/MessageDialog",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/util/dialog",
    	"ier/model/ResultSet",
    	"ier/widget/dialog/ObjectListDialog",
    	"ier/widget/listView/gridModules/ObjectListRowContextMenu"
], function(dojo_declare, dojo_array, dojo_json, dojo_lang, dojo_dom_class, dojo_string, ecm_model_Desktop, ecm_model_Request, ecm_widget_dialog_ConfirmationDialog,
		ecm_dialog_MessageDialog, ier_constants, ier_messages, ier_util, ier_util_dialog, 
		ier_model_ResultSet, ier_dialog_ObjectListDialog, RowContextMenu){

var _RowContextMenu = dojo_declare(RowContextMenu, {
	loadContextMenu: function(selectedItems, callback) {
		ecm.model.desktop.loadMenuActions(ier_constants.MenuType_IERRemoveHoldContextMenu, callback);
	}
});

return dojo_declare("ier.widget.dialog.RemoveHoldDialog", [ier_dialog_ObjectListDialog], {

	//boolean whether to handle the double click
	connectonRowDblClick: true,

	postCreate: function(){
		this.inherited(arguments);

		this._objectContentList.multiSelect = true;
		this.title = ier_messages.objectSelector_removeEntities;
		this.setIntroText(ier_messages.objectSelector_removeHoldsIntroText);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh19.htm"));
		this.setEntityType(ier_constants.EntityType_Hold);
		this._selectButton.set("label",ier_messages.dialog_removeHoldButton);
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
				targetItems[0].retrieveAttributes(null, false); // set backgroundRequest to false to show the progress dialog 
			});
			var holdables = dojo_array.map(targetItems, function(item){
				return {id: item.id, entityType: item.getEntityType()};
			});
			params.requestParams[ier_constants.Param_Holdables] = dojo_json.toJson(holdables);
			var holds = dojo_array.map(selectedItems, function(item){
				return {id: item.id};
			});
			params.requestParams[ier_constants.Param_Holds] = dojo_json.toJson(holds);
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_RemoveHold, ier_constants.PostEncoding, params);
		}
	},

	onInputChange: function(){
		var selectedObjects = this._objectContentList.getSelectedItems();
		var valid = selectedObjects != null && selectedObjects.length > 0;
		this.setButtonEnabled(this._selectButton, valid);
		return valid;
	},

	retrieveObjects: function(){
		this.logEntry("retrieveObjects");
		
		var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response){
			if(response){
				response.repository = this.repository;
				var resultSet = new ier_model_ResultSet(response);
				this._objectContentList.setResultSet(resultSet);
				this.resize();
				this.onInputChange();
			}
		}));
		
		params.requestParams[ier_constants.Param_EntityId] = this._targetItems[0].id;
		params.requestParams[ier_constants.Param_FilterString] = this._filterString;
		params.requestParams[ier_constants.Param_Type] = this._targetItems[0].ierContentType;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetHoldsForEntity, ier_constants.PostEncoding, params);
	},

	getContentListGridModules: function(){
		var modules = this.inherited(arguments);
		ier_util.replaceModule(modules, "rowContextMenu", _RowContextMenu);
		return modules;
	},

	_onDesktopChanged: function(modelObject){
		//override parent method
	},

	/**
	 * override parent method
	 */
	_onSelectItem: function(){
		var valid = this.onInputChange();
		if(valid){
			var selectedItems = this._objectContentList.getSelectedItems();
			if(selectedItems){
				var includingDynamic = dojo_array.some(selectedItems, function(item){
					return item.attributes.IsDynamicHold;
				}, this);
				if(includingDynamic){
					this._messageDialog && this._messageDialog.destroy();
					this._messageDialog = new ecm_dialog_MessageDialog({
						text: ier_messages.removeHoldDialog_cannot_remove_hold
					});
					this._messageDialog.startup();
					this._messageDialog.show();
					ier_util_dialog.manage(this._messageDialog);
					return;
				}
				this.onSelect(selectedItems);
				this.onCancel();
			}
		}
	}
});});