define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/EntityItemRepositorySecurityPane.html",
    	"dijit/layout/ContentPane", // in template
    	"ecm/widget/SecurityPane" // in template
], function(dojo_declare, dojo_lang, dojo_class, ier_messages, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * @name ier.widget.EntityItemRepositorySecurityPane
 * @class Provides an editable view of the security settings of an item.
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.EntityItemRepositorySecurityPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.EntityItemRepositorySecurityPane.prototype */

	templateString: templateString,
	_permissions: null,
	_isReadOnly: true,

	_repositoryTemplate: {
		privileges: [
			{
				id: "FULL_CONTROL",
				labelKey: "full_control_privilege",
				mask: 569769984,
				required: [
					{
						level: "MODIFY_PROPERTIES",
						required: false
					}
				],
				levels: [
					{
						level: "OWNER_CONTROL",
						required: false
					}
				]
			},
			{
				id: "EDIT",
				labelKey: "edit_privilege",
				mask: 15728640,
				levels: [
					{
						level: "MODIFY_PROPERTIES",
						required: false
					}
				]
			}
		],
		levels: [
			{
				id: "OWNER_CONTROL",
				name: ier_messages.repository_security_owner_control,
				mask: 569769984
			},
			{
				id: "MODIFY_PROPERTIES",
				name: ier_messages.repository_security_modify_properties,
				mask: 15728640
			}
		]
	},

	postCreate: function() {
		this.inherited(arguments);
		this._securityPane.enableShowPermissionTypeIcons = true;
		this._securityPane.enableFolderPropagationOptions = true;
		this._securityPane.enableDocumentPermissionPropagationOptions = true;

		var self = this;
		this._securityPane._loadPrivileges = function() {
			this._levels = self._repositoryTemplate.levels;
			this._privileges = self._repositoryTemplate.privileges;
		};
	},
	
	/**
	 * Creates the rendering for the pane
	 * @param repository
	 */
	createRendering: function(repository) {
		this.logEntry("createRendering()");
		
		this.repository = repository;
		this._retrievePermissions();

		this.logExit("createRendering()");
	},

	hide: function() {
		dojo_class.add(this._securityPane.domNode, "dijitHidden");
	},
	
	isValidationRequired: function() {
		return false;
	},
	
	_retrievePermissions: function() {
		if (this.repository) {
			this.repository.retrievePermissions(dojo_lang.hitch(this, this._processPermissions));
		}
	},

	_processPermissions: function(permissions) {
		this._securityPane.reset();
		this._securityPane.setRepository(this.repository);
		this._permissions = permissions;
		
		this._renderPermissions(this._permissions, this._isReadOnly);
	},

	_renderPermissions: function(permissions, isReadOnly) {
		this._securityPane.renderPermissions(permissions, null, null, false, isReadOnly, true, true);
	},

	getPermissions: function() {
		return this._securityPane.getPermissions();
	},

	resize: function() {
		this._contentPane.resize();
	}
});});
