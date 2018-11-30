define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/util/util",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/EntityItemObjectStoreSecurityPane.html",
    	"dijit/layout/ContentPane", // in template
    	"ier/widget/panes/ObjectStoreSecurityPane" // in template
], function(dojo_declare, dojo_lang, dojo_class, ecm_model_Request, ier_constants, ier_util, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * @name ier.widget.panes.EntityItemObjectStoreSecurityPane
 * @class Provides an editable view of the security settings of a repository.
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.EntityItemObjectStoreSecurityPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.EntityItemObjectStoreSecurityPane.prototype */

	templateString: templateString,
	_permissions: null,
	_isReadOnly: false,

	postCreate: function() {
		this.inherited(arguments);
	},

	/**
	 * Creates the rendering for the pane
	 * @param repository
	 * @parent isReadOnly - optional attribute to set whether to set this security pane as readOnly
	 */
	createRendering: function(repository, isReadOnly) {
		this.logEntry("createRendering()");

		this.repository = repository;
		this._isReadOnly = isReadOnly ? isReadOnly : false;
		this.connect(this._securityPane, "onChange", "onInputChange");

		this._retrievePermissions();

		this.logExit("createRendering()");
	},

	hide: function() {
		dojo_class.add(this._securityPane.domNode, "dijitHidden");
	},

	_retrievePermissions: function() {
		if (this._permissions) {
			this.processPermissions(this._permissions);
		} else if (this.repository) {
			this.repository.permissions = null; // clear cache
			this.repository.retrieveObjectStorePermissions(dojo_lang.hitch(this, this.processPermissions));
		}
	},

	processPermissions: function(permissions) {
		this._securityPane.reset();
		this._securityPane.setRepository(this.repository);
		if(permissions)
			this._permissions = permissions;
		this._renderPermissions(this._permissions, this._isReadOnly);
	},

	_renderPermissions: function(permissions, isReadOnly) {
		this._securityPane.renderPermissions(permissions, isReadOnly);
	},

	getPermissions: function() {
		return this._securityPane.getPermissions();
	},
	
	saveSecurity: function(callback){
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
			this.repository.objectStorePermissions = null; // clear cache
			if(callback)
				callback(response);
		}));

		var data = new Object();
		data[ier_constants.Param_Permissions] = this.getPermissions();
		params["requestBody"] = data;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_ObjectStoreSecurity, ier_constants.PostEncoding, params);
	}
});});
