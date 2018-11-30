define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"ier/messages",
	"ier/constants",
	"ier/model/admin/Config",
	"ecm/Messages",
	"dojo/text!./templates/GeneralSettingsPane.html",
	"dijit/form/CheckBox", // template
	"ecm/widget/HoverHelp", // template
	"idx/layout/TitlePane", // template
	"ecm/widget/NumberTextBox" // template
], function(declare, lang, dom_class, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin, messages, ier_constants, Config, ecm_messages, GeneralSettingsPane_html){
return declare([_Widget, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: GeneralSettingsPane_html,

	config: null,
	dirty: false,
	valid: true,

	_messages: messages,
	ecmMessages: ecm_messages,

	postCreate: function(){
		this.inherited(arguments);

		this.connect(this._folderOnlyBox, "onChange", function(){ this._validate(true); });
		this.connect(this._disableSortBox, "onChange", function(){ this._validate(true); });
		this.connect(this._taskManagerLogDirectory, "onChange", function(){ this._validate(true); });
		this.connect(this._cbrPageSize, "onChange", function(){ this._validate(true); });		
	},

	save: function(){
		if(this.config){
			this.config.set("taskManagerLogDirectory", this._taskManagerLogDirectory.get("value") || "");
			this.config.set("browseFolderOnly", this._folderOnlyBox.get("checked") || false);
			this.config.set("browseDisableSort", this._disableSortBox.get("checked") || false);
			this.config.set("cbrPageSize", this._cbrPageSize.get("value") || ier_constants.Search_default_cbrPageSize);
		}
	},

	reset: function(){
		this._setValue(this._taskManagerLogDirectory, this.config && this.config.get("taskManagerLogDirectory") || "");
		this._setCheckedValue(this._folderOnlyBox, this.config && this.config.get("browseFolderOnly") || false);
		this._setCheckedValue(this._disableSortBox, this.config && this.config.get("browseDisableSort") || false);
		this._setValue(this._cbrPageSize, this.config && this.config.get("cbrPageSize") || ier_constants.Search_default_cbrPageSize);
		this._validate(false);
	},

	_setCheckedValue: function(widget, value){
		var intermediateChanges = widget.intermediateChanges;
		widget.intermediateChanges = false;
		widget.set("checked", value, false);
		widget.intermediateChanges = intermediateChanges;
	},
	
	_setValue: function(widget, value){
		var intermediateChanges = widget.intermediateChanges;
		widget.intermediateChanges = false;
		widget.set("value", value, false);
		widget.intermediateChanges = intermediateChanges;
	},

	_validate: function(dirty){
		// disable "disable sort" when "folder only"
		var disabled = !!this._folderOnlyBox.get("checked");
		this._disableSortBox.set("disabled", disabled);
		dom_class.toggle(this._disableSortLabel, "ierAdminLabelDisabled", disabled);

		this.dirty = dirty;
		this.valid = true;
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}

});
});
