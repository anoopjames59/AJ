define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"dojo/dom-construct",
	"dijit/layout/ContentPane",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"ier/messages",
	"ier/model/admin/Config",
	"ecm/widget/_FolderSelectorDropDown",
	"ecm/widget/FolderSelectorCallback",
	"dojo/text!./templates/RepositoryReportPane.html",
	"ecm/widget/HoverHelp", // template
	"ecm/widget/ValidationTextBox", // template
	"idx/layout/TitlePane" // template
], function(declare, lang, connect, dom_construct, ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin, messages, Config, FolderSelectorDropDown, FolderSelectorCallback,
		ReportSettingsPane_html){
return declare("ier.widget.admin.RepositoryReportPane", [ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: ReportSettingsPane_html,

	repository: null,
	config: null,
	dirty: false,
	valid: true,

	_messages: messages,

	postCreate: function(){
		this.inherited(arguments);

		this._createFolderSelector();
		
		this.connect(this._saveButton, "onClick", this._save);
		this.connect(this._resetButton, "onClick", this._reset);
		this.connect(this._clearButton, "onClick", this._clearValue);
		this._saveButton.set("disabled", true);
		this._resetButton.set("disabled", true);
	},
	
	_createFolderSelector: function(){
		if(this._reportOutputFolderSelector){
			this._reportOutputFolderSelector.destroy();
			connect.disconnect(this._onFolderSelectedHandler);
		}
		
		this._reportOutputFolderSelector = new FolderSelectorDropDown({
			preventSelectRoot : true
		});
		dom_construct.place(this._reportOutputFolderSelector.domNode, this._reportOutputSelectorContainer, "only");
		this._setFolderSelectorCallback();
		
		this._onFolderSelectedHandler = this.connect(this._reportOutputFolderSelector, "onFolderSelected", function(){ 
			this._validate(true); 
			this._clearButton.set("disabled", false);
		});

	},
	
	_setFolderSelectorCallback: function(){
		// Set permission to check for folder add.
		this.folderSelectorCallback = new FolderSelectorCallback("privAddToFolder", messages.noPermissionAdd);
		this._reportOutputFolderSelector.setIsSelectableCallback(this.folderSelectorCallback.isSelectableByPermission, this.folderSelectorCallback);
	},
	
	setRepository: function(repository){
		this.repository = repository;
		this._reportOutputFolderSelector.setRoot(repository);
	},
	
	_setConfigAttr: function(config){
		this._set("config", config);
		this._reset();
	},

	_save: function(){
		if(this.config){
			var folder = this._reportOutputFolderSelector.getSelected();
			if(folder){
				folder = folder.item.id;
			}
			this.config.set("reportOutputSaveDirectory", folder || "");
			Config.saveConfig(this.config, lang.hitch(this, function(){
				this._validate(false);
				
				this.repository.reportOutputSaveDirectory = this.config.get("reportOutputSaveDirectory");
			}));
		}
	},
	
	_clearValue: function(){
		this._createFolderSelector();
		this._clearButton.set("disabled", true);
		this._reportOutputFolderSelector.setRoot(this.repository);
		this._validate(true); 
	},

	_reset: function(){
		this._reportOutputFolderSelector.repository = this.repository;

		var output = this.config.get("reportOutputSaveDirectory");
		var path = output;
		if(output){
			if(lang.isArray(output)){
				path = output[0] + "," + output[1] + "," + output[2];
			}
			this.repository.retrieveItem(path, lang.hitch(this, function(itemRetrieved){
				this._reportOutputFolderSelector.setSelected(itemRetrieved);
			}));
			this._clearButton.set("disabled", false);
		}
		else {
			this._clearValue();
		}
			
		this._validate(false);
	},

	_setValue: function(widget, value){
		var intermediateChanges = widget.intermediateChanges;
		widget.intermediateChanges = false;
		widget.set("value", value, false);
		widget.intermediateChanges = intermediateChanges;
	},

	_validate: function(dirty){
		this.dirty = dirty;
		this._saveButton.set("disabled", !dirty || !this.valid);
		this._resetButton.set("disabled", !dirty);
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}

});
});
