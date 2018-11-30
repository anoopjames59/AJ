define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"ier/messages",
	"ier/model/admin/Config",
	"dojo/text!./templates/ReportSettingsPane.html",
	"ecm/widget/HoverHelp", // template
	"ecm/widget/ValidationTextBox", // template
	"idx/layout/TitlePane" // template
], function(declare, lang, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin, messages, Config, ReportSettingsPane_html){
return declare([_Widget, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: ReportSettingsPane_html,

	config: null,
	dirty: false,
	valid: true,
	intermediateChangesOff: false,

	_messages: messages,

	postCreate: function(){
		this.inherited(arguments);
		this._reportPathHoverHelp.set("message", messages.admin_reportCognosReportPath_tooltip);
		
		if(this.intermediateChangesOff){
			this._cognosGatewayServerNameBox.intermediateChanges = false;
			this._cognosServletServerNameBox.intermediateChanges = false;
			this._cognosReportPathBox.intermediateChanges = false;
			this._reportEngineDataSourceBox.intermediateChanges = false;
			this._cognosReportNamespace.intermediateChanges = false;
		}

		this.connect(this._cognosGatewayServerNameBox, "onChange", function(){ this._validate(true); });
		this.connect(this._cognosServletServerNameBox, "onChange", function(){ this._validate(true); });
		this.connect(this._cognosReportPathBox, "onChange", function(){ this._validate(true); });
		this.connect(this._reportEngineDataSourceBox, "onChange", function(){ this._validate(true); });
		this.connect(this._cognosReportNamespace, "onChange", function(){ this._validate(true); });
	},

	save: function(){
		if(this.config){
			this.config.set("cognosGatewayServerName", this._cognosGatewayServerNameBox.get("value") || "");
			this.config.set("cognosDispatchServletServerName", this._cognosServletServerNameBox.get("value") || "");
			this.config.set("cognosReportPath", this._cognosReportPathBox.get("value") || "/content/folder[@name='IERReport']");
			this.config.set("reportEngineDataSource", this._reportEngineDataSourceBox.get("value") || "");
			this.config.set("cognosReportNamespace", this._cognosReportNamespace.get("value") || "");
			
			ecm.model.desktop.reportSettings.cognosGatewayServerName = this.config.get("cognosGatewayServerName");
			ecm.model.desktop.reportSettings.cognosDispatchServletServerName = this.config.get("cognosDispatchServletServerName");
			ecm.model.desktop.reportSettings.cognosReportPath = this.config.get("cognosReportPath");
			ecm.model.desktop.reportSettings.cognosNamespace = this.config.get("cognosReportNamespace");
		}
	},

	reset: function(){
		this._setValue(this._cognosGatewayServerNameBox, this.config && this.config.get("cognosGatewayServerName") || "");
		this._setValue(this._cognosServletServerNameBox, this.config && this.config.get("cognosDispatchServletServerName") || "");
		this._setValue(this._cognosReportPathBox, this.config && this.config.get("cognosReportPath") || "/content/folder[@name='IERReport']");
		this._setValue(this._reportEngineDataSourceBox, this.config && this.config.get("reportEngineDataSource") || "");
		this._setValue(this._cognosReportNamespace, this.config && this.config.get("cognosReportNamespace") || "");
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
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}
});
});
