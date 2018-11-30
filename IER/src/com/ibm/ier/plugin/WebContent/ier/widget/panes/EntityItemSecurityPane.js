define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ier/constants",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/EntityItemSecurityPane.html",
    	"dijit/layout/ContentPane", // in template
    	"ecm/widget/SecurityPane" // in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_class, ier_constants, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * @name ier.widget.EntityItemSecurityPane
 * @class Provides an editable view of the security settings of an item.
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.EntityItemSecurityPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.EntityItemSecurityPane.prototype */

	templateString: templateString,
	_parentFolder: null,
	_contentClass: null,
	_permissions: null,
	_isReadOnly: false,
	_item: null,

	postCreate: function() {
		this.inherited(arguments);
		this._securityPane.enableShowPermissionTypeIcons = true;
		this._securityPane.enableFolderPropagationOptions = true;
		this._securityPane.enableDocumentPermissionPropagationOptions = true;
		this.connect(this._securityPane, "onChange", "onSecuritiyChanged");
	},
	
	/**
	 * Creates the rendering for the pane
	 * @param repository
	 * @param item
	 * @param contentClass - the content class to obtain the default instance security from
	 * @param properties - the properties to be used to check for security markings
	 * @parent isReadOnly - optional attribute to set whether to set this security pane as readOnly
	 */
	createRendering: function(repository, item, parentFolder, contentClass, properties, isReadOnly) {
		this.logEntry("createRendering()");
		
		this.repository = repository;
		this._item = item;
		this._parentFolder = parentFolder;
		this._isReadOnly = isReadOnly ? isReadOnly : false;

		if(contentClass)
			this.setContentClass(contentClass);

		if(properties)
			this.setProperties(properties);

		if(item)
			this._retrievePermissions();

		this.logExit("createRendering()");
	},

	hide: function() {
		dojo_class.add(this._securityPane.domNode, "dijitHidden");
	},
	
	isValidationRequired: function() {
		return false;
	},
	
	updateSecurityPolicyTemplate: function() {
		// Get a different template and re-render the permissions if there is security policy
		var securityPolicy = this.getSecurityPolicy();
		if (securityPolicy) {
			this._renderPermissions(this._permissions, null, securityPolicy, this._isReadOnly, true);
		}
	},

	updateParentFolder: function(parentFolder) {
		this._parentFolder = parentFolder;
		parentFolder.retrievePermissions(dojo_lang.hitch(this, function(parentFolderPermissions) {
			this._securityPane.updateParentPermissions(parentFolderPermissions);
		}));
	},


	setContentClass: function(contentClass) {
		if (!this._contentClass || this._contentClass.name != contentClass.name) {
			this._contentClass = contentClass;
			this._markingValues = null;
			if (!this._item) {
				this._permissions = null;
				this._retrievePermissions();
			}
		}
	},
	
	_retrievePermissions: function() {
		if (this._permissions) {
			this._processPermissions(this._permissions);
		} else if (this._item) {
			this._item.permissions = null; // clear cache
			this._item.retrievePermissions(dojo_lang.hitch(this, this._processPermissions));
		} else if (this._contentClass) {
			// If permissions are not already set, use the class default instance permissions.
			this._contentClass.retrieveDefaultInstancePermissions(dojo_lang.hitch(this, this._processPermissions));
		}
	},

	setProperties: function(properties) {
		this._markingValues = null;
		if (this._item){
			var props = this._isReadOnly ? null : properties;
			var markingValues = [];
			var markingProperties = this._contentClass.markingProperties;
			if (markingProperties) {
				var attributes;
				if (props) { // Merge changed properties into default attributes
					attributes = dojo_lang.clone(this._item.attributes); // Don't want to pollute the default attributes
					dojo_array.forEach(props, function(property) {
						if (markingProperties[property.name]) {
							attributes[property.name] = property.value;
						}
					});
				} else {
					attributes = this._item.attributes;
				}
				for (i in attributes) { // Loop through attribute set to pick up hidden attributes too.
					var markings = markingProperties[i];
					if (markings && i !== ier_constants.Property_PreventRMEntityDeletion) {
						var value = attributes[i] instanceof Array ? attributes[i].join() : attributes[i];
						if (value.length > 0) {
							markingValues.push(attributes[i]);
						}
					}
				}
			}
			this._markingValues = markingValues;
		}
	},

	getSecurityPolicy: function() {
		var securityPolicy = null;

		if (this._contentClass && this._contentClass._securityPolicy) {
			securityPolicy = this._contentClass._securityPolicy;
		}

		return securityPolicy;
	},

	_processPermissions: function(permissions) {
		this._securityPane.reset();
		this._securityPane.setRepository(this.repository);
		this._securityPane.setItem(this._item);
		this._permissions = permissions;
		
		var securityPolicy = this.getSecurityPolicy();
		var parentFolder = this._parentFolder;
		if(parentFolder && parentFolder.isIERFavorite){
			parentFolder = parentFolder.item; // use real parent folder for favorite's child
		}
		if(!this._item && parentFolder){
			parentFolder.retrievePermissions(dojo_lang.hitch(this, function(folderPermissions) {
				this._renderPermissions(this._permissions, folderPermissions, securityPolicy, this._isReadOnly, true);
			}));
		}else{
			this._renderPermissions(this._permissions, null, securityPolicy, this._isReadOnly, true);
		}
	},

	_renderPermissions: function(permissions, parentPermissions, securityPolicy, isReadOnly, applySecurityPolicy) {
		var isFolder = true;
		if(this._item){
			isFolder = this._item.isFolder();
		}else if(this._contentClass){
			isFolder = (this._contentClass.id != ier_constants.ClassName_ReportDefinition);
		}
		this._securityPane.renderPermissions(permissions, parentPermissions, securityPolicy, !isFolder, isReadOnly, applySecurityPolicy, true);
		this._securityPane.setMarkingValues(this._markingValues);
	},

	getPermissions: function() {
		return this._securityPane.getPermissions();
	},

	/**
	 * This event is invoked if any securities are changed
	 */
	onSecuritiyChanged: function(){
	},

	resize: function() {
		this._contentPane.resize();
	}
});});
