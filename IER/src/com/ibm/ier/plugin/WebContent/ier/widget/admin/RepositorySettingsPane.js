define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dojo/dom-construct",
	"dijit/_TemplatedMixin", 
	"dijit/_WidgetsInTemplateMixin",
	"dijit/layout/ContentPane",
	"ier/constants",
	"ier/messages",
	"ier/model/admin/Config",
	"ier/widget/ObjectSelector",
	"dojo/text!./templates/RepositorySettingsPane.html",
	"dijit/form/Button", // template
	"idx/layout/TitlePane", // template
	"ecm/widget/HoverHelp", // template
	"ier/widget/admin/SystemConfigPane" // template
], function(declare, lang, dom_class, dom_construct, _TemplatedMixin, _WidgetsInTemplateMixin, ContentPane,
	constants, messages, Config, ObjectSelector, RepositorySettingsPane_html){
return declare([ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: RepositorySettingsPane_html,

	repository: null,
	config: null,
	dirty: false,
	valid: true,

	_messages: messages,

	postCreate: function(){
		this.inherited(arguments);

		this._selector = new ObjectSelector({id: this.id + "_selector", labelId: this.id + "_selectorLabel",
			label: messages.admin_defaultFilePlan, objectClassName: constants.ClassName_FilePlan});
		dom_construct.place(this._selector.domNode, this._selectorContainer);

		this.connect(this._saveButton, "onClick", this._save);
		this.connect(this._resetButton, "onClick", this._reset);
		this.connect(this._selector, "onChange", function(){
			this._validate(true);
		});
		this.connect(this._systemConfigPane, "onChange", function(dirty){
			this._validate(dirty);
		});
	},

	_setConfigAttr: function(config){
		this._set("config", config);
		this._reset();
		this._systemConfigPane.set("repository", this.repository);
	},

	_save: function(){
		if(this.config){
			var filePlan = this._selector.selectedItem;
			this.config.set("defaultFilePlan", filePlan && filePlan.getGuidId() || "");
			Config.saveConfig(this.config, lang.hitch(this, function(){
				this._validate(false);
			}));
		}
		this._systemConfigPane.save();
	},

	_reset: function(){
		this._selector.repository = this.repository;
		var defaultFilePlan = this.config && this.config.get("defaultFilePlan");
		var filePlan = defaultFilePlan && this.repository && this.repository.getFilePlan(defaultFilePlan);
		this._selector.setSelectedItem(filePlan);
		this._validate(false);
		this._systemConfigPane.reset();
	},

	_validate: function(dirty){
		this.dirty = dirty;
		this.valid = this._selector.get("value") && this._systemConfigPane.valid;
		this._saveButton.set("disabled", !dirty || !this.valid);
		this._resetButton.set("disabled", !dirty);
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}

});
});
