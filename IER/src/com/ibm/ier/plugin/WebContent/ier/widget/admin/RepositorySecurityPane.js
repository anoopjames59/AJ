define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"ecm/widget/dialog/ConfirmationDialog",
	"ecm/widget/dialog/MessageDialog",
	"ier/constants",
	"ier/messages",
	"ier/util/dialog",
	"ier/model/admin/Config",
	"ier/widget/dialog/ObjectStoreSecurityDialog",
	"dojo/text!./templates/RepositorySecurityPane.html",
	"dijit/form/Button", // template
	"idx/layout/TitlePane", // template
	"ier/widget/panes/EntityItemObjectStoreSecurityPane" // template
], function(declare, lang, dom_construct, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin, ecm_widget_dialog_ConfirmationDialog, ecm_dialog_MessageDialog, constants, messages, ier_util_dialog,
		Config, ier_widget_dialog_ObjectStoreSecurityDialog, RepositorySecurityPane_html){
return declare([_Widget, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: RepositorySecurityPane_html,

	repository: null,
	config: null,

	_messages: messages,

	postCreate: function(){
		this.inherited(arguments);

		this.connect(this._runButton, "onClick", this._runSecurityScript);
		this.connect(this._entityItemObjectStoreSecurityPane, "onInputChange", this.onChange);
		this.connect(this._restoreDefaultsButton, "onClick", this._restoreDefaults);
		this._runButton.set("disabled", true);
		this._restoreDefaultsButton.set("disabled", true);
	},

	setRepository: function(repository){
		this.repository = repository;

		this._entityItemObjectStoreSecurityPane.createRendering(repository);
	},
	
	_restoreDefaults: function(){
		this._entityItemObjectStoreSecurityPane.processPermissions();
		this._restoreDefaultsButton.set("disabled", true);
	},

	_runSecurityScript: function(){
		if(!this.confirmRunSecurity){
			this.confirmRunSecurity = new ecm_widget_dialog_ConfirmationDialog({
				text: messages.objectStoreSecurityDialog_confirmation,
				buttonLabel: messages.baseDialog_runButton,
				onExecute: lang.hitch(this, function(){
					this._entityItemObjectStoreSecurityPane.saveSecurity(lang.hitch(this, function(){
						var dialog = new ecm_dialog_MessageDialog({
							text: messages.admin_securityScriptSuccessful
						});
						dialog.show();
						ier_util_dialog.manage(dialog);
						this._runButton.set("disabled", true);
						this._restoreDefaultsButton.set("disabled", true);
					}));
				})
			});
		}
		this.confirmRunSecurity.show();
	},

	onChange: function(dirty){
		if(this._validatePermissions()){
			this._runButton.set("disabled", false);
		}
		else
			this._runButton.set("disabled", true);
		
		this._restoreDefaultsButton.set("disabled", false);
	},

	onShow: function(){
		this.inherited(arguments);
		this._entityItemObjectStoreSecurityPane.resize();
	},

	destroy: function(){
		if(this.confirmRunSecurity)
			this.confirmRunSecurity.destroy();
		
		this.inherited(arguments);
	},
	
	_validatePermissions: function(){
		var permissions = this._entityItemObjectStoreSecurityPane.getPermissions();
		var permissionTypes = [];
		for(var i in permissions){
			var permission = permissions[i];
			if(permission && permission.securityGroup){
				permissionTypes[permission.securityGroup] = true;
			}
		}
		
		if(this.repository.recordDatamodelType == "DoDClassified" && permissionTypes[0] != true)
			return false;
		
		if(permissionTypes[1] != true || permissionTypes[2] != true || permissionTypes[3] != true || permissionTypes[4] != true)
			return false;
		
		return true;
	}
});
});
