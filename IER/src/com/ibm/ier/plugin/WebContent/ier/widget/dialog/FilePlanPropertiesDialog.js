define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-style",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ecm/model/Request",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/FilePlanPropertiesDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"dijit/layout/TabContainer", // in content
    	"ier/widget/panes/EntityItemPropertiesPane", // in content
    	"ier/widget/panes/EntityItemSecurityPane" // in content
], function(dojo_declare, dojo_lang, dom_style, ier_constants, ier_messages, ier_util, ecm_model_Request, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.FilePlanPropertiesDialog
 * @class Provides an interface to edit or view a file plan
 * @augments ecm.widget.dialog.BaseDialog
 */
return dojo_declare("ier.widget.dialog.FilePlanPropertiesDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.FilePlanPropertiesDialog.prototype */

	contentString: contentString,
	_messages: ier_messages,
	_item: null,
	_isReadOnly: false,

	postCreate: function() {
		this.inherited(arguments);
		this.addChildPane(this._entityItemPropertiesPane);
		this.addChildPane(this._entityItemSecurityPane);
		this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
		this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, false);
	},

	setDialogMode: function(editable) {
		if(editable) {
			dom_style.set(this._saveButton.domNode, "display", "");
			dom_style.set(this._applyButton.domNode, "display", "");
			this.cancelButton.set("label", ier_messages.baseDialog_cancelButton);
		} else {
			dom_style.set(this._saveButton.domNode, "display", "none");
			dom_style.set(this._applyButton.domNode, "display", "none");
			this.cancelButton.set("label", ier_messages.baseDialog_closeButton);
		}
	},

	hasEditablePane: function() {
		return (this._item.privModifyProperties || this._item.privModifyPermissions);
	},

	/**
	 * Shows the FilePlanPropertiesDialog
	 * @param repository
	 */
	show: function(repository, item, isReadOnly) {
		this.inherited("show", arguments);

		this.logEntry("show()");
		this._item = item;
		this._isReadOnly = isReadOnly ? isReadOnly : false;

		this.setDialogMode(this.hasEditablePane() || !isReadOnly);

		this.repository = repository;
		if(!this.repository || this.repository.isIERLoaded()){
			this._renderDialog();
		}
		else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}
		
		this.resize();
		
		this.logExit("show()");
	},

	validateInput: function() {
		var isValid = this.inherited(arguments);
		this._applyButton.set("disabled", !isValid);
		return isValid;
	},

	/**
	 * Render the dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");

		this.set("title", ier_messages.fileplan);

		this.setIntroText(ier_messages.addFilePlanDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh01.htm"));
		
		this.setResizable(true);

		//renders properties pane
		this._entityItemPropertiesPane.createRendering({
			repository: this.repository,
			rootClassId : ier_constants.ClassName_FilePlan, 
			entityType: ier_constants.EntityType_FilePlan,
			hideContentClassSelector: true,
			item: this._item,
			isReadOnly: this._isReadOnly || !this._item.privModifyProperties
		});

		//Sets up and renders the security pane after the property pane has finished rendering
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {
			var contentClass = this._entityItemPropertiesPane.getContentClass();
			var properties = this._entityItemPropertiesPane.getProperties();
			
			//renders the security pane
			this._entityItemSecurityPane.createRendering(this.repository, this._item, null, contentClass, properties, this._isReadOnly);
			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});
		
		this.logExit("_renderDialog");
	},

	_editFilePlan: function(className, criterias, permissions, callback) {
		this.logEntry("_editFilePlan");

		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
			this.repository.clearFilePlans();
			this._item.retrieveAttributes(dojo_lang.hitch(this, function(){
				this._item.permissions = null;
				this._item.refresh();
				// To refresh only the changed row in the grid in ConfigurePane,
				// DO NOT call this.repository.onConfigure since it will reload whole the grid
				if(ecm.model.desktop.onConfigure){
					ecm.model.desktop.onConfigure(this.repository, [this._item]);
				}
			}), false, true);

			if(callback){
				callback();
			}
		}));
		params.requestParams[ier_constants.Param_FilePlanRepositoryId] = this.repository.id;
		params.requestParams[ier_constants.Param_EntityId] = this._item.id;
		params.requestParams[ier_constants.Param_ClassName] = className;
		
		var data = new Object();
		data[ier_constants.Param_Properties] = criterias;
		data[ier_constants.Param_Permissions] = permissions;
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_EditFilePlan, ier_constants.PostEncoding, params);

		this.logExit("_editFilePlan");
	},

	_updateProperties: function(closeOnComplete) {
		this.logEntry("_updateProperties");

		if (this.validateInput()) {
			var properties = this._entityItemPropertiesPane.getProperties();
			var permissions = this._entityItemSecurityPane.getPermissions();
			var className = this._entityItemPropertiesPane.getContentClass().id;

			this._editFilePlan(className, properties, permissions, closeOnComplete ? dojo_lang.hitch(this, "onCancel") : null);
		}

		this.logExit("_updateProperties");
	},

	_onClickApply: function() {
		this.logEntry("_onClickApply");

		this._updateProperties(false);

		this.logExit("_onClickApply");
	},
	
	_onClickSave: function() {
		this.logEntry("_onClickSave");

		this._updateProperties(true);

		this.logExit("_onClickSave");
	}
});});
