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
	"ecm/widget/dialog/ConfirmationDialog",
	"ier/constants",
	"ier/messages",
	"ier/model/admin/Config",
	"ier/util/dialog",
	"ier/util/property",
	"ier/widget/admin/Grid",
	"ier/widget/dialog/IERBaseDialog",
	"dojo/text!./templates/DisplayPropertyPane.html",
	"dojo/text!./templates/DisplayPropertyDialogContent.html",
	"dijit/Menu", // template
	"dijit/MenuItem", // template
	"dijit/form/Button", // template
	"dijit/layout/ContentPane", // template
	"ecm/widget/ContentClassSelector", // template
	"ecm/widget/FilterTextBox", // template
	"ecm/widget/SloshBucket" // template
], function(array, declare, lang, Memory, string, _TemplatedMixin, _WidgetsInTemplateMixin, BorderContainer,
		JsonStore, Request, ConfirmationDialog,
		constants, messages, Config, util_dialog, util_property, Grid, IERBaseDialog, DisplayPropertyPane_html, DisplayPropertyDialogContent_html){
function newStore(data){
	return new JsonStore({identifier: "value", label: "name", data: data || []});
}

var Dialog = declare(IERBaseDialog, {
	
	contentString: DisplayPropertyDialogContent_html,
	title: messages.admin_displayProperties,

	repository: null,
	config: null,
	store: null,
	object: null,
	type: "",

	_messages: messages,

	buildRendering: function(){
		this.inherited(arguments);

		this.addDomNodeCSSClass("ierSmallDialog");
		this._saveButton = this.addButton(messages.admin_save, "_onSave", false, true);

		this._propertySelector.hideAvailableOnAdd = true;
		this._availableStructure = [{field: "name", name: messages.admin_availableProperties, width: "100%"}];
		this._selectedStructure = [{field: "name", name: messages.admin_selectedProperties, width: "100%"}];

		this.connect(this._classSelector, "onContentClassSelected", "_onClassSelected");
		this.connect(this._filterBox, "onChange", "_onFilterChange");
		this.connect(this._restoreButton, "onClick", "_onRestore");
	},

	_setObjectAttr: function(object){
		this._set("object", object);

		this._classSelector.repository = this.repository;
		var id = object && object.id || "";
		this._classSelector.rootClassId = ""; // force reset below
		this._classSelector.setRootClassId(id || this.type);
		var label = object && object.name || " "; 
		this._classSelector.setLabel(label);
		this._classSelector.setDisabled(!!id);
		this._filterBox.set("value", "");

		this._propertySelector.setAvailableGridModel(newStore(), this._availableStructure);
		this._propertySelector.setSelectedValuesGridModel(newStore(), this._selectedStructure);
		if(id){
			this._loadAvailableProperties();
		}
	},

	_loadAvailableProperties: function(){
		if(this.repository && this.object){
			Request.invokeService("openContentClass", this.repository.type, {
				repositoryId: this.repository.id, objectStoreId: this.repository.objectStoreName,
				template_name: this.object.id, ier_config: true
			}, lang.hitch(this, function(response){
				var data = [];
				array.forEach(response.criterias, function(c){
					var name = c.name;
					if(!c.system && !c.hidden && !util_property.isRMSystemProperty(name) &&
						!util_property.isDispositionGroupProperty(name) && !util_property.isVitalRecordGroupProperty(name)){
						data.push({value: name, name: c.label || name});
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
			var names = this.object.names;
			var selected = {};
			var data = array.map(this.object.props || [], function(p){
				selected[p] = p;
				return {value: p, name: names[p] || p};
			});
			this._propertySelector.setSelectedValuesGridModel(newStore(data), this._selectedStructure, selected);
			this._propertySelector.filter({});
		}
	},

	_getDefaultProperties: function(baseClassId, superClassOnly){
		if(this.store && this.object){
			// look up super-class's properties
			var props = null;
			var names = null;
			if(baseClassId){
				var baseClass = this.repository.getContentClass(baseClassId, this._classSelector.objectStore);
				if(baseClass){
					function findClass(superClass, subClass){
						var subClasses = superClass.subClasses && superClass.subClasses.all || [];
						for(var i = 0; i < subClasses.length; i++){
							var c = subClasses[i];
							if(c){
								if(c.id == subClass.id){
									return (superClassOnly ? [] : [c]); // if restoring, super-class only
								}
								var a = findClass(c, subClass);
								if(a){
									a.push(c);
									return a;
								}
							}
						}
						return null;
					}
					array.some(findClass(baseClass, this.object) || (superClassOnly ? [] : [this.object]), function(c){
						var object = this.store.get(c.id);
						if(object){
							props = lang.clone(object.props);
							names = lang.clone(object.names);
							return true;
						}else{
							return false;
						}
					}, this);
				}
			}
			this.object.props = props || [];
			this.object.names = names || {};
		}
	},

	_onClassSelected: function(c){
		if(c && c.id){
			this.object = {id: c.id, name: c.name};
			this._getDefaultProperties(this.type);

			this._classSelector.setLabel(c.name || "");
			this._propertySelector.setAvailableGridModel(newStore(), this._availableStructure);
			this._propertySelector.setSelectedValuesGridModel(newStore(), this._selectedStructure);
			this._loadAvailableProperties();
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
				var props = config.get(this.object.id + "DisplayProperties");
				if(props && props.length > 0){
					this.object.props = config.get(this.object.id + "DisplayProperties");
					this.object.names = config.get(this.object.id + "DisplayNames");
					this._loadSelectedProperties();
				}else{
					if(this.type){
						this._getDefaultProperties(this.type, true);
					}else{
						this._getDefaultProperties(constants.ClassName_RecordFolder, true); // try with container
						if(!this.object.props || this.object.props.length === 0){
							this._getDefaultProperties(constants.ClassName_Record, true); // retry with record
						}
					}
					this._loadSelectedProperties();
				}
			}));
		}
	},

	_onSave: function(){
		if(this.config && this.object){
			var data = this._propertySelector.getData(this._propertySelector.getSelectedValuesGrid());
			if(data && data.length > 0){
				var names = this.object.names;
				this.object.props = array.map(data, function(d){
					var p = d.value;
					names[p] = names[p] || d.name;
					return p;
				});
				this.config.set(this.object.id + "DisplayProperties", this.object.props);
				Config.saveConfig(this.config, lang.hitch(this, function(){
					this.onSave(this.object);
					this.hide();
				}));
			}
		}
	},

	onSave: function(config){
	}

});

return declare([BorderContainer, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: DisplayPropertyPane_html,
	gutters: false,

	repository: null,
	config: null,

	_messages: messages,

	buildRendering: function(){
		this.inherited(arguments);
		this._supportingWidgets = [];

		var structure = [
			{field: "name", name: messages.admin_class, width: "20%"},
			{field: "props", name: messages.admin_displayProperties, width: "80%", sortable: false, formatter: function(object){
				var names = object.names;
				return array.map(object.props, function(p){
					return names[p] || p;
				}).join(", ");
			}}
		];
		this._grid = new Grid({structure: structure, sortInitialOrder: {colId: 1}});
		this._grid.placeAt(this._gridContainer.containerNode);

		this.connect(this._addFolderButton, "onClick", "_onAddFolder");
		this.connect(this._addRecordButton, "onClick", "_onAddRecord");
		this.connect(this._editButton, "onClick", "_onEdit");
		this.connect(this._deleteButton, "onClick", "_onDelete");
		this.connect(this._addFolderMenu, "onClick", "_onAddFolder");
		this.connect(this._addRecordMenu, "onClick", "_onAddRecord");
		this.connect(this._editMenu, "onClick", "_onEdit");
		this.connect(this._deleteMenu, "onClick", "_onDelete");
		this.connect(this._grid, "onSelectionChange", "_onSelectionChange");
		this._grid.menu.bind(this._menu, {hookPoint: "row"});
		this._onConfigChange();
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
		this._onConfigChange();
	},

	_getData: function(){
		var data = [];
		if(this.config){
			var values = this.config.values;
			for(var key in values){
				var index = key.indexOf("DisplayProperties");
				if(index > 0){
					var id = key.substring(0, index);
					var name = values[id + "DisplayName"] || id;
					var props = values[key] || [];
					var names = values[id + "DisplayNames"] || {};
					data.push({id: id, name: name, props: props, names: names});
				}
			}
		}
		return data;
	},

	_getDialog: function(){
		if(!this._dialog){
			this._dialog = new Dialog({repository: this.repository, config: this.config, store: this._grid.store});
			this.connect(this._dialog, "onSave", "_onSave");
		}
		return this._dialog;
	},

	_onAddFolder: function(){
		var dialog = this._getDialog();
		dialog.type = constants.ClassName_RecordFolder;
		dialog.set("object", null);
		dialog.show();
	},

	_onAddRecord: function(){
		var dialog = this._getDialog();
		dialog.type = constants.ClassName_Record;
		dialog.set("object", null);
		dialog.show();
	},

	_onEdit: function(){
		var selected = this._grid.getSelected();
		var dialog = this._getDialog();
		var object = this._grid.store.get(selected[0]);
		dialog.type = "";
		dialog.set("object", object);
		dialog.show();
	},

	_onDelete: function(){
		if(this.config){
			var selected = this._grid.getSelected();
			var predefined = this.config.get("predefinedClasses") || [];
			var list = [];
			array.forEach(selected, function(id){
				if(array.indexOf(predefined, id) < 0){
					list.push(id);
				}
			});
			if(list.length > 0){
				var repository = this.repository;
				var config = this.config;
				var grid = this._grid;
				var text = (list.length > 1 ?  string.substitute(messages.delete_multiple_confirmation_question, [list.length]) :
						messages.delete_single_confirmation_question);
				var dialog = new ConfirmationDialog({text: text, buttonLabel: messages.delete_confirmation_button, onExecute: function(){
					array.forEach(list, function(id){
						config.set(id + "DisplayProperties", null);
					});
					Config.saveConfig(config, function(){
						grid.deselectAll();
						var store = grid.store;
						array.forEach(list, function(id){
							store.remove(id);
						});
						repository.refresh(); // clear cache
					});
				}});
				dialog.show();
				util_dialog.manage(dialog);
			}else{
				util_dialog.showMessage(messages.admin_cannotDeletePredefined);
			}
		}
	},

	_onSave: function(object){
		if(object){
			var store = this._grid.store;
			if(store.get(object.id)){
				store.put(object);
			}else{
				store.add(object);
			}
			this.repository.refresh(); // clear cache
		}
	},

	_onConfigChange: function(){
		var disabled = !this.config;
		this._addFolderButton.set("disabled", disabled);
		this._addRecordButton.set("disabled", disabled);
		this._addFolderMenu.set("disabled", disabled);
		this._addRecordMenu.set("disabled", disabled);
	},

	_onSelectionChange: function(){
		var selected = this._grid.getSelected();
		var disabled = selected.length !== 1;
		this._editButton.set("disabled", disabled);
		this._editMenu.set("disabled", disabled);
		disabled = selected.length === 0;
		this._deleteButton.set("disabled", disabled);
		this._deleteMenu.set("disabled", disabled);
	}

});
});
