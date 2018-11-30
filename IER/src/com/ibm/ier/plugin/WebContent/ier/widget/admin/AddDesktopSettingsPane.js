define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/string",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"ecm/widget/dialog/MessageDialog",
	"ecm/model/Request",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/model/admin/Config",
	"dojo/text!./templates/AddDesktopSettingsPane.html",
	"ecm/widget/HoverHelp", // template
	"ecm/widget/ValidationTextBox", // template
	"idx/layout/TitlePane", // template
	"dijit/form/Button", // template
	"ecm/widget/HoverHelp" // template
], function(declare, lang, string, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin, MessageDialog, Request, ier_constants, messages, ier_util, Config, AddDesktopSettingsPane_html){
return declare([_Widget, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: AddDesktopSettingsPane_html,

	config: null,
	dirty: false,
	valid: true,

	_messages: messages,

	postCreate: function(){
		this.inherited(arguments);
		
		this._desktopId.pattern = '([a-zA-Z0-9])*';
		this._desktopId.invalidMessage = this._messages.admin_addDesktopPane_idInvalid;

		this._desktopName.pattern = '([^"*|<>?:\\\\/])*';
		this._desktopName.invalidMessage = this._messages.admin_addDesktopPane_invalidName;
		
		this.connect(this._addButton, "onClick", "addDesktop");

		this.connect(this._desktopName, "onChange", "_onNameInputChange");
		this.connect(this._desktopId, "onChange", "_onInputChange");
		this.connect(this._desktopDescription, "onChange", "_onInputChange");
	},

	addDesktop: function(){
		var serviceParams = ier_util.getDefaultParams(null, lang.hitch(this, function(response)
		{	
			var dialog = new MessageDialog({
				text: string.substitute(this._messages.admin_addDesktopPane_success, [ this._desktopName.get("value") ])
			});
			dialog.show();
			this._desktopId.set("value", "");
			this._desktopName.set("value", "");
			this._desktopDescription.set("value", "");
		}));
		serviceParams.requestParams[ier_constants.Param_Id] = this._desktopId.get("value");
		serviceParams.requestParams[ier_constants.Param_Name] = this._desktopName.get("value");
		serviceParams.requestParams[ier_constants.Param_Description] = this._desktopDescription.get("value");
		
		Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateIERDesktop, ier_constants.PostEncoding, serviceParams);
	},
	
	_onNameInputChange: function(){
		this._desktopId.set("value", this._getIdValueFromName(this._desktopName.get("value")));
	},
	
	_onInputChange: function(){
		this._addButton.set("disabled", !this.validate());
		this.onChange();
	},
	
	_setValue: function(widget, value){
		var intermediateChanges = widget.intermediateChanges;
		widget.intermediateChanges = false;
		widget.set("value", value, false);
		widget.intermediateChanges = intermediateChanges;
	},

	validate: function(){
		if(this._desktopName.get("value") == null || this._desktopName.get("value").length == 0 || 
				this._desktopId.get("value") == null || this._desktopId.get("value").length == 0)
			return false;
		
		return true;
	},
	
	_getIdValueFromName: function(name) {
		var id = "";
		if (name) {
			for ( var i = 0; i < name.length; i++) {
				var ch = name[i].charCodeAt();
				if ((ch > 47 && ch < 58) || (ch > 64 && ch < 91) || (ch > 96 && ch < 123)) {
					id += name[i];
				}
			}
		}
		return id;
	},

	onChange: function(){
	}

});
});
