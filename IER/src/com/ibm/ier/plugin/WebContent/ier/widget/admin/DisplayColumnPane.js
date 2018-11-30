define([
	"dojo/_base/array",
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/store/Memory",
	"dojo/string",
	"dijit/_TemplatedMixin", 
	"dijit/_WidgetsInTemplateMixin",
	"dijit/layout/BorderContainer",
	"idx/data/JsonStore",
	"ecm/model/Request",
	"ier/constants",
	"ier/messages",
	"ier/model/admin/Config",
	"ier/widget/admin/Grid",
	"ier/widget/dialog/IERBaseDialog",
	"dojo/text!./templates/DisplayColumnPane.html",
	"dojo/text!./templates/DisplayColumnDialogContent.html",
	"dijit/Menu", // template
	"dijit/MenuItem", // template
	"dijit/form/Button", // template
	"dijit/form/TextBox", // template
	"dijit/layout/ContentPane", // template
	"ecm/widget/FilterTextBox", // template
	"ecm/widget/SloshBucket" // template
], function(array, declare, lang, Memory, string, _TemplatedMixin, _WidgetsInTemplateMixin, BorderContainer,
		JsonStore, Request,
		constants, messages, Config, Grid, IERBaseDialog, DisplayColumnPane_html, DisplayColumnDialogContent_html){
function newStore(data){
	return new JsonStore({identifier: "value", label: "name", data: data || []});
}

var Dialog = declare(IERBaseDialog, {
	
	contentString: DisplayColumnDialogContent_html,
	title: messages.admin_displayColumns,

	repository: null,
	config: null,
	object: null,

	_messages: messages,

	buildRendering: function(){
		this.inherited(arguments);

		this.addDomNodeCSSClass("ierSmallDialog");
		this._saveButton = this.addButton(messages.admin_save, "_onSave", false, true);

		this._propertySelector.hideAvailableOnAdd = true;
		this._availableStructure = [{field: "name", name: messages.admin_availableProperties, width: "100%"}];
		this._selectedStructure = [{field: "name", name: messages.admin_selectedProperties, width: "100%"}];

		this.connect(this._filterBox, "onChange", "_onFilterChange");
		this.connect(this._restoreButton, "onClick", "_onRestore");
	},

	_setObjectAttr: function(object){
		this._set("object", object);

		this._listNode.innerHTML = object && object.name || "";
		this._filterBox.set("value", "");

		this._propertySelector.setAvailableGridModel(newStore(), this._availableStructure);
		this._propertySelector.setSelectedValuesGridModel(newStore(), this._selectedStructure);
		this._loadAvailableProperties();
	},

	_loadAvailableProperties: function(){
		if(this.repository && this.object){
			Request.invokeService("openContentClass", this.repository.type, {
				repositoryId: this.repository.id, objectStoreId: this.repository.objectStoreName,
				template_name: this.object.id, ier_config: true
			}, lang.hitch(this, function(response){
				var data = [];
				array.forEach(response.criterias, function(c){
					if(!c.hidden || c.name == constants.Property_SweepState){
						data.push({value: c.name, name: c.label || c.name});
					}
				});
				data.sort(function(a, b){
					return (a.name > b.name ? 1 : -1);
				});
				this._propertySelector.setAvailableGridModel(newStore(data), this._availableStructure);
				this._loadSelectedProperties();
			}));
		}
	},

	_loadSelectedProperties: function(){
		if(this.object){
			var columns = this.object.columns;
			var names = this.object.names;
			var selected = {};
			var data = array.map(columns, function(c){
				selected[c] = c;
				return {value: c, name: names[c] || c};
			});
			this._propertySelector.setSelectedValuesGridModel(newStore(data), this._selectedStructure, selected);
			this._propertySelector.filter({});
		}
	},

	_onFilterChange: function() {
		var value = this._filterBox.get("value");
		this._propertySelector.filter({name: "*" + value + "*"});
	},

	_onRestore: function() {
		if(this.object){
			Config.getConfig("repository", "default", lang.hitch(this, function(config){
				this.object = {id: this.object.id, name: this.object.name};
				this.object.columns = config.get(this.object.id + "DisplayColumns");
				this.object.names = config.get(this.object.id + "DisplayNames");
				this._loadSelectedProperties();
			}));
		}
	},

	_onSave: function(){
		if(this.config && this.object){
			var data = this._propertySelector.getData(this._propertySelector.getSelectedValuesGrid());
			if(data && data.length > 0){
				var names = this.object.names;
				this.object.columns = array.map(data, function(d){
					var c = d.value;
					names[c] = names[c] || d.name;
					return c;
				});
				this.config.set(this.object.id + "DisplayColumns", this.object.columns);
				Config.saveConfig(this.config, lang.hitch(this, function(){
					this.onSave(this.object);
					this.hide();
				}));
			}
		}
	},

	onSave: function(object){
	}

});

return declare([BorderContainer, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: DisplayColumnPane_html,
	gutters: false,

	repository: null,
	config: null,

	_messages: messages,

	buildRendering: function(){
		this.inherited(arguments);
		this._supportingWidgets = [];

		var structure = [
			{field: "name", name: messages.admin_list, width: "20%"},
			{field: "columns", name: messages.admin_displayColumns, width: "80%", sortable: false, formatter: function(object){
				var names = object.names;
				return array.map(object.columns, function(c){
					return names[c] || c;
				}).join(", ");
			}}
		];
		this._grid = new Grid({structure: structure, sortInitialOrder: {colId: 1}});
		this._grid.placeAt(this._gridContainer.containerNode);

		this.connect(this._editButton, "onClick", "_onEdit");
		this.connect(this._editMenu, "onClick", "_onEdit");
		this.connect(this._grid, "onSelectionChange", "_onSelectionChange");
		this._grid.menu.bind(this._menu, {hookPoint: "row"});
		this._onSelectionChange();
	},

	destroy: function(){
		if(this._dialog){
			this._dialog.destroyRecursive();
		}

		this.inherited(arguments);
	},

	_setConfigAttr: function(config){
		this._set("config", config);
		this._grid.setStore(new Memory({data: this._getData()}));
	},

	_getData: function(){
		var data = [];
		if(this.config){
			var values = this.config.values;
			for(var key in values){
				var index = key.indexOf("DisplayColumns");
				if(index > 0){
					var id = key.substring(0, index);
					var name;
					if(id == constants.ClassName_Container){
						name = messages.admin_browseListName;
					}else{
						name = messages["admin_configureListName_" + id] || values[id + "DisplayName"] || id; 
					}
					var columns = values[key] || [];
					var names = values[id + "DisplayNames"] || {};
					data.push({id: id, name: name, columns: columns, names: names});
				}
			}
		}
		return data;
	},

	_getDialog: function(){
		if(!this._dialog){
			this._dialog = new Dialog({repository: this.repository, config: this.config});
			this.connect(this._dialog, "onSave", "_onSave");
		}
		return this._dialog;
	},

	_onEdit: function(){
		var selected = this._grid.getSelected();
		var dialog = this._getDialog();
		var object = this._grid.store.get(selected[0]);
		dialog.set("object", object);
		dialog.show();
	},

	_onSave: function(object){
		this._grid.store.put(object);
	},

	_onSelectionChange: function(){
		var selected = this._grid.getSelected();
		var disabled = selected.length !== 1;
		this._editButton.set("disabled", disabled);
		this._editMenu.set("disabled", disabled);
	}

});
});
