define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/event",
    	"dojo/_base/json",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dojo/keys",
    	"dojo/string",
    	"dijit/form/Button",
    	"dijit/form/TextBox",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/util/dialog",
    	"ier/model/ResultSet",
    	"ecm/model/Request",
    	"ecm/model/Desktop",
    	"ier/widget/listView/gridModules/ObjectListRowContextMenu",
    	"ier/widget/ListTypeSelector",
    	"ier/widget/dialog/ObjectListDialog",
    	"ier/widget/dialog/IERBaseDialog",
    	"ecm/widget/dialog/ConfirmationDialog",
    	"ecm/widget/dialog/MessageDialog"
], function(dojo_declare, dojo_array, dojo_event, dojo_json, dojo_lang, dojo_construct, dojo_style, dojo_class, dojo_keys, dojo_string, dijit_form_Button, dijit_form_TextBox, ier_constants, ier_messages, 
		ier_util, ier_util_dialog, ier_model_ResultSet, ecm_model_Request, ecm_model_Desktop, RowContextMenu, ier_ListTypeSelector, ier_dialog_ObjectListDialog, ier_dialog_IERBaseDialog, ecm_widget_dialog_ConfirmationDialog, 
		ecm_dialog_MessageDialog){

var _RowContextMenu = dojo_declare(RowContextMenu, {
	loadContextMenu: function(selectedItems, callback) {
		ecm.model.desktop.loadMenuActions(ier_constants.MenuType_IERViewEntitiesOnHoldContextMenu, callback);
	}
});

return dojo_declare("ier.widget.dialog.ViewEntitiesOnHoldDialog", [ier_dialog_ObjectListDialog], {

	_filterEntityType: ier_constants.EntityType_Record,
	_isFirstOpenDialog: true,
	_isDynamicHold: false,

	postCreate: function(){
		ier_dialog_IERBaseDialog.prototype.postCreate.apply(this,arguments);

		this.title = ier_messages.viewEntitiesOnholdDialog_objectTitile;
		this.setEntityType(ier_constants.EntityType_HybridRecordFolder);

		this.connect(this._objectContentList, "onRowSelectionChange", "onInputChange");
		this.connect(ecm.model.desktop, "ierHoldIsRemoved", "_holdIsRemoved");
		this._objectContentList.setGridExtensionModules(this.getContentListGridModules());
	},

	destroy: function(){
		this._listTypeSelector && this._listTypeSelector.destroy();
		this.inherited(arguments);
	},

	onInputChange: function(){
		var selectedObjects = this._objectContentList.getSelectedItems();
		var valid = selectedObjects != null && selectedObjects.length > 0;
		if(valid && this._isDynamicHold){
			valid = dojo_array.some(selectedObjects, function(item){
				return item && item.attributes.IsDynamicHold == false;
			});
		}
		this.setButtonEnabled(this._removeHoldButton, valid);
		return valid;
	},

	_renderingFilterArea: function(){

		this._searchCriteria = dojo_construct.create("div");

		var holdImg = dojo_construct.create("img", {
			"class": this._isDynamicHold? "dynamicHoldIcon" :"onHoldIcon",
			alt: this._isDynamicHold? ier_messages.dynamicHold: ier_messages.hold,
			title: this._isDynamicHold? ier_messages.dynamicHold: ier_messages.hold,
			src: "dojo/resources/blank.gif"
		});
		this._searchCriteria.appendChild(holdImg);

		var holdNameNode = dojo_construct.create("span", {
			"class": "viewEntitiesOnHoldEntityName",
			innerHTML: dojo_string.substitute(ier_messages.viewEntitiesOnholdDialog_searchInHold, {name: this._targetItems[0].name})
		});
		this._searchCriteria.appendChild(holdNameNode);

		var options = [];
		options.push({text:ier_messages.record, value: ier_constants.EntityType_Record, isSelected: "true"});
		options.push({text:ier_messages.container, value: ier_constants.EntityType_Container});
		this._listTypeSelector = new ier_ListTypeSelector(options);
		this._listTypeSelector.listTypeSelectionLabel.innerHTML = ier_messages.viewEntitiesOnholdDialog_searchFor + ":";
		this._searchCriteria.appendChild(this._listTypeSelector.domNode);

		this._quickSearchTextBox = new dijit_form_TextBox({"class": "viewEntitiesOnHoldSearchTextBox"});
		var searchForm = dojo_construct.create("div", {"class": "viewEntitiesOnHoldExplainText"});
		var searchExplanationText = dojo_construct.create("label", {
			"for": this._quickSearchTextBox.id,
			innerHTML: ier_messages.viewEntitiesOnholdDialog_searchExplanation + ":"
		});
		searchForm.appendChild(searchExplanationText);
		this._searchCriteria.appendChild(searchForm);

		this._quickSearchButton = new dijit_form_Button({label: ier_messages.viewEntitiesOnholdDialog_searchButtonLabel});
		this._searchCriteria.appendChild(this._quickSearchTextBox.domNode);
		this._searchCriteria.appendChild(this._quickSearchButton.domNode);
		var removeHoldButtonContainer = dojo_construct.create("div", {"class": "viewEntitiesOnHoldRemove"}, this._searchCriteria);
		this._removeHoldButton = new dijit_form_Button({disabled: true, label: ier_messages.dialog_removeHoldButton});
		removeHoldButtonContainer.appendChild(this._removeHoldButton.domNode);

		dojo_class.remove(this.filterArea, "filterArea"); // disable text-align style
		this._filePlanSearchBar.domNode.style.display = "none";
		this.filterArea.appendChild(this._searchCriteria);

		this.connect(this._quickSearchButton, "onClick",  function(){
			this._filterString = this._quickSearchTextBox.get("value");
			this._filterEntityType = this._listTypeSelector.getSelection();
			this.retrieveObjects();
		});
		//enable enter keystroke for searching on the search bar
		this.connect(this._quickSearchTextBox, "onKeyDown", function(evt) {
			if (evt.keyCode == dojo_keys.ENTER) {
				dojo_event.stop(evt);
				this._filterString = this._quickSearchTextBox.get("value");
				this._filterEntityType = this._listTypeSelector.getSelection();
				this.retrieveObjects();
			}
		});
		this.connect(this._removeHoldButton, "onClick", "_removeHold");
	},
	_removeHold: function(){
		var valid = this.onInputChange();
		if(valid){
			var selectedItems = this._objectContentList.getSelectedItems();
			var _this = this;
			var includingDynamic = dojo_array.some(selectedItems, function(item){
				return item.attributes.IsDynamicHold;
			}, this);
			if(includingDynamic){
				this._messageDialog && this._messageDialog.destroy();
				this._messageDialog = new ecm_dialog_MessageDialog({
					text: ier_messages.viewEntitiesOnholdDialog_cannot_remove_hold
				});
				this._messageDialog.startup();
				this._messageDialog.show();
				ier_util_dialog.manage(this._messageDialog);
				return;
			}
			this._confirmRemoveHold && this._confirmRemoveHold.destroy();
			this._confirmRemoveHold = new ecm_widget_dialog_ConfirmationDialog({
				text: dojo_string.substitute(ier_messages.remove_hold_multiple_confirmation_question, [ selectedItems.length ]),
				buttonLabel: ier_messages.remove_hold_confirmation_button,
				onExecute: function(){
					var params = ier_util.getDefaultParams(_this.repository, dojo_lang.hitch(this, function(response) {
						_this.retrieveObjects();
					}));
					var holdables = dojo_array.map(selectedItems, function(item){
						return {id: item.id, entityType: item.getEntityType()};
					});
					params.requestParams[ier_constants.Param_Holdables] = dojo_json.toJson(holdables);
					var hold = {id: _this._targetItems[0].id};
					params.requestParams[ier_constants.Param_Holds] = dojo_json.toJson([hold]);
					ecm_model_Request.invokePluginService(ier_constants.ApplicationPlugin, ier_constants.Service_RemoveHold, params);
				}
			});
			this._confirmRemoveHold.show();
			ier_util_dialog.manage(this._confirmRemoveHold);
		}
	},
	show: function(repository, items){
		if(repository){
			this.contentClass = repository.getContentClass(ier_constants.ClassName_RecordFolder);
		}
		if(items && items[0]){
			this._isDynamicHold = items[0].isDynamicHold();
		}
		this._targetItems = items;
		this._renderingFilterArea();
		this.inherited(arguments, [repository, ier_constants.ClassName_RecordFolder]);
	},

	retrieveObjects: function(){
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){
			if(response){
				this._objectContentList.emptyMessage = ier_messages.viewEntitiesOnholdDialog_emptyMessage;
				if(this._isFirstOpenDialog){
					this._isFirstOpenDialog = false;
					response.rows = [];
					this._objectContentList.emptyMessage = " ";
				}
				response.repository = this.repository;
				var resultSet = new ier_model_ResultSet(response);
				var items = resultSet.items;
				if(items){
					dojo_array.forEach(items, dojo_lang.hitch(this,function(item){
						item.removeHold = this._targetItems[0].id;
					}));
				}
				this._objectContentList.setResultSet(resultSet);
				this.resize();
				this.onInputChange();
			}
		}));
		params.requestParams[ier_constants.Param_HoldId] = this._targetItems[0].id;
		params.requestParams[ier_constants.Param_EntityType] = this._filterEntityType;
		params.requestParams[ier_constants.Param_FilterString] = this._filterString;
		if(this._isFirstOpenDialog){
			params.requestParams["IS_SEARCH"]= "NO";
		}else{
			params.requestParams["IS_SEARCH"]= "YES";
		}
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetEntitiesOnHold, ier_constants.PostEncoding, params);
	},

	_holdIsRemoved: function(modelObject){
		this.retrieveObjects();
	},

	getContentListGridModules: function(){
		var modules = this.inherited(arguments);
		ier_util.replaceModule(modules, "rowContextMenu", _RowContextMenu);
		return modules;
	}
});});