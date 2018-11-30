define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dijit/_TemplatedMixin", 
	"dijit/_WidgetsInTemplateMixin",
	"dijit/layout/BorderContainer",
	"ier/messages",
	"ier/model/admin/Config",
	"dojo/text!./templates/SettingsPane.html",
	"dijit/form/Button", // template
	"dijit/layout/ContentPane", // template
	"dijit/layout/TabContainer", // template
	"ier/widget/admin/ReportSettingsPane", // template
	"ier/widget/admin/DDContainerSettingsPane" // template
], function(declare, lang, dom_class, _TemplatedMixin, _WidgetsInTemplateMixin, BorderContainer, messages, Config, SettingsPane_html){
return declare([BorderContainer, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: SettingsPane_html,
	gutters: false,

	desktopId: "",
	dirty: false,

	_messages: messages,

	buildRendering: function(){
		this.inherited(arguments);

		if(this.desktopId){
			this._titleNode.innerHTML = messages.admin_desktop_label + ": <b>" + (this.title || "") + "</b>";
			this._deskNode.style.display = "none";
		}else{
			this._titleNode.style.display = "none";
			this._deskNode.innerHTML = messages.admin_settingsDesc;
			dom_class.add(this._barNode, "ierAdminBarLast");
		}
		this.connect(this._saveAndCloseButton, "onClick", function(){ this._save(true); });
		this.connect(this._saveButton, "onClick", function(){ this._save(false); });
		this.connect(this._resetButton, "onClick", this._reset);
		this.connect(this._closeButton, "onClick", this._close);
	},

	startup: function(){
		this.inherited(arguments);

		this._validate(false); // initial state

		var name = (this.desktopId ? "desktop" : "settings");
		Config.getConfig(name, this.desktopId || null, lang.hitch(this, function(config){
			this._config = config;
			this._generalPane.set("config", config);
			this._reportPane.set("config", config);
			this._ddContainerSettingsPane.set("config", config);
			this._reset();

			this.connect(this._generalPane, "onChange", function(){ this._validate(true); });
			this.connect(this._reportPane, "onChange", function(){ this._validate(true); });
			this.connect(this._ddContainerSettingsPane, "onChange", function(){ this._validate(true); });
		}));
	},

	_save: function(close){
		this._generalPane.save();
		this._reportPane.save();
		this._ddContainerSettingsPane.save();

		Config.saveConfig(this._config, lang.hitch(this, function(){
			this._validate(false);
			if(close){
				this._close();
			}
		}));
	},

	_close: function(){
		var parent = this.getParent();
		if(parent && parent.closeChild){
			parent.closeChild(this);
		}
	},

	_reset: function(){
		this._generalPane.reset();
		this._reportPane.reset();
		this._ddContainerSettingsPane.reset();

		this._validate(false);
	},

	_validate: function(dirty){
		this.dirty = dirty;
		var valid = this._generalPane.valid && this._reportPane.valid && this._ddContainerSettingsPane.valid;
		this._saveButton.set("disabled", !dirty || !valid);
		this._saveAndCloseButton.set("disabled", !dirty || !valid);
		this._resetButton.set("disabled", !dirty);
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}

});
});
