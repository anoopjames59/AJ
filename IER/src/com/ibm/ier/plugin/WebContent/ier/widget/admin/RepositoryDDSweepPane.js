define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"dojo/dom-construct",
	"dojo/dom-class",
	"dijit/layout/ContentPane",
	"dijit/_TemplatedMixin", 
	"dijit/_WidgetsInTemplateMixin",
	"ier/messages",
	"ier/constants",
	"ier/model/admin/Config",
	"ier/widget/_FolderSelectorDropDown",
	"ecm/widget/FolderSelectorCallback",
	"ier/widget/ObjectSelector",
	"dojo/text!./templates/RepositoryDDSweepPane.html",
	"ecm/widget/HoverHelp", // template
	"ecm/widget/ValidationTextBox", // template
	"idx/layout/TitlePane" // template
], function(declare, lang, connect, dom_construct, dom_class, dijit_layout_ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin, messages, ier_constants, Config, FolderSelectorDropDown, FolderSelectorCallback,
		ObjectSelector, template){
return declare("ier.widget.admin.RepositoryDDSweepPane", [dijit_layout_ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: template,

	repository: null,
	config: null,
	dirty: false,
	valid: true,

	_messages: messages,

	postCreate: function(){
		this.inherited(arguments);

		this._createFolderSelector();
		
		this.connect(this._alwaysDeclaredRecordSelect, "onChange", function(){
			if(this._alwaysDeclaredRecordSelect.get("value") == "true"){
				this._disableRecordContainerSelector(false);
			}
			else {
				this._disableRecordContainerSelector(true);
			}
			this._validate(true);
		});
		
		this.connect(this._alwaysShowDeclareResultSelect, "onChange", this._validate);
		this.connect(this._saveButton, "onClick", this._save);
		this.connect(this._resetButton, "onClick", this._reset);
		this.connect(this._clearButton, "onClick", this._clearValue);
		this._saveButton.set("disabled", true);
		this._resetButton.set("disabled", true);
	},
	
	_disableRecordContainerSelector: function(disable){
		if(disable){
			dom_class.add(this._recordContainerDirectorySelector.domNode, "dijitComboBoxDisabled");
			this._recordContainerDirectorySelector.setDisabled(true);
			this._clearButton.set("disabled", true);
		}
		else {
			dom_class.remove(this._recordContainerDirectorySelector.domNode, "dijitComboBoxDisabled");
			this._recordContainerDirectorySelector.setDisabled(false);
			this._clearButton.set("disabled", false);
		}
		this._alwaysShowDeclareResultSelect.set("disabled", disable);
	},
	
	/**
	 * Creates the workflow selector and sets  it to the new repository
	 */
	_createWorkflowSelector: function(repository){
		if(!this._defaultDDWorkflowSelector){
			this._defaultDDWorkflowSelector = new ObjectSelector({
				id: this.id + "_defaultDDWorkflow",
				label: messages.repositoryDDSweepPane_defaultWorkflow,
				labelId: this.id + "_defaultDDWorkflowLabel",
				showVersionSelection: true,
				type: ier_constants.WorkflowType_BasicSchedule
			});
			dom_construct.place(this._defaultDDWorkflowSelector.domNode, this._defaultDDWorkflowSelectorContainer, "only");
		}
		
		this._defaultDDWorkflowSelector.setRepository(repository);
		this._defaultDDWorkflowSelector.setObjectClassName(ier_constants.ClassName_WorkflowDefinition);
		
		if (this._defaultDDWorkflowSelector)
			this.connect(this._defaultDDWorkflowSelector, "onChange", function(){ 
				this._validate(true); 
			});
	},
	
	_createFolderSelector: function(){
		if(this._recordContainerDirectorySelector){
			this._recordContainerDirectorySelector.destroy();
			connect.disconnect(this._onFolderSelectedHandler);
		}
		
		this._recordContainerDirectorySelector = new FolderSelectorDropDown({
			preventSelectRoot : true
		});
		dom_construct.place(this._recordContainerDirectorySelector.domNode, this._recordContainerDirectorySelectorContainer, "only");
		
		// Set permission to check for folder add.
		var folderSelectorCallback = new FolderSelectorCallback(ier_constants.Privilege_CanDeclareRecordToContainer, messages.declareRecordDialog_notAllowedToDeclareToFolder);
		this._recordContainerDirectorySelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);
		
		this._onFolderSelectedHandler = this.connect(this._recordContainerDirectorySelector, "onFolderSelected", function(){ 
			this._validate(true); 
			this._clearButton.set("disabled", false);
		});
	},
	
	setRepository: function(repository){
		this.repository = repository;
		this._recordContainerDirectorySelector.setRoot(repository);
		this._createWorkflowSelector(repository);
	},
	
	_setConfigAttr: function(config){
		this._set("config", config);
		this._reset();
	},

	_save: function(){
		if(this.config){
			if(!this._recordContainerDirectorySelector.disabled){
				var folder = this._recordContainerDirectorySelector.getSelected();
				if(folder){
					folder = folder.item.id;
				}
				this.config.set("defensibleDisposalRecordContainerId", folder || "");
			}
			else
				this.config.set("defensibleDisposalRecordContainerId", "");
			
			var workflow = this._defaultDDWorkflowSelector.get("value");
			this.config.set("defensibleDisposalWorkflowId", workflow || "");
			
			this.config.set("defensibleSweepAlwaysDeclareRecord", this._alwaysDeclaredRecordSelect.get("value"));
			
			if(!this._alwaysShowDeclareResultSelect.disabled)
				this.config.set("defensibleSweepAlwaysShowDeclareResult", this._alwaysShowDeclareResultSelect.get("value"));
			else
				this.config.set("defensibleSweepAlwaysShowDeclareResult", "");
			
			Config.saveConfig(this.config, lang.hitch(this, function(){
				this._validate(false);
				
				this.repository.defensibleSweepSettings.defensibleSweepAlwaysDeclareRecord = this.config.get("defensibleSweepAlwaysDeclareRecord");
				this.repository.defensibleSweepSettings.defensibleSweepAlwaysShowDeclareResult = this.config.get("defensibleSweepAlwaysShowDeclareResult");
				this.repository.defensibleSweepSettings.defensibleDisposalRecordContainerId = this.config.get("defensibleDisposalRecordContainerId");
				this.repository.defensibleSweepSettings.defensibleDisposalWorkflowId = this.config.get("defensibleDisposalWorkflowId");
			}));
		}
	},
	
	_clearValue: function(){
		this._createFolderSelector();
		this._clearButton.set("disabled", true);
		this._recordContainerDirectorySelector.setRoot(this.repository);
		this._validate(true); 
	},

	_reset: function(){
		if(this.config){
			this._recordContainerDirectorySelector.repository = this.repository;
			var output = this.config.get("defensibleDisposalRecordContainerId");
			var path = output;
			if(output){
				if(lang.isArray(output)){
					path = output[0] + "," + output[1] + "," + output[2];
				}
				this.repository.retrieveItem(path, lang.hitch(this, function(itemRetrieved){
					this._recordContainerDirectorySelector.setSelected(itemRetrieved);
				}));
				this._clearButton.set("disabled", false);
			}
			else {
				this._clearValue();
			}
			
			output = this.config.get("defensibleDisposalWorkflowId");
			path = output;
			if(output){
				if(lang.isArray(output)){
					path = output[0] + "," + output[1] + "," + output[2];
				}
				if(this._defaultDDWorkflowSelector){
					this.repository.retrieveItem(path, lang.hitch(this, function(itemRetrieved){
						this._defaultDDWorkflowSelector.setSelectedItem(itemRetrieved);
					}), "WorkflowDefinition");
				}
			} else {
				this._defaultDDWorkflowSelector.clearItem();
			}
			
			var value = this.config.get("defensibleSweepAlwaysDeclareRecord");
			this._alwaysDeclaredRecordSelect.set("value", value != null ? String(value) : "true");
			value = this.config.get("defensibleSweepAlwaysShowDeclareResult");
			this._alwaysShowDeclareResultSelect.set("value", value != null ? String(value) : "false");
			
			if(this._alwaysDeclaredRecordSelect.get("value") == "true"){
				this._disableRecordContainerSelector(false);
			}
			else {
				this._disableRecordContainerSelector(true);
			}
				
			this._validate(false);
		}
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
