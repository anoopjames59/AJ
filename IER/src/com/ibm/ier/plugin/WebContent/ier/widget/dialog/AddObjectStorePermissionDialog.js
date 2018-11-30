define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"dojo/_base/array",
	"dojo/_base/kernel",
	"dojo/dom-construct",
	"dojo/dom-class",
	"dijit/Tooltip",
	"dijit/layout/BorderContainer",
	"dijit/layout/ContentPane",
	"dijit/form/Select",
	"ecm/widget/dialog/BaseDialog",
	"ecm/LoggerMixin",
	"ecm/Messages",
	"ecm/model/Desktop",
	"ecm/model/Permission",
	"ecm/model/UserGroup",
	"ecm/widget/UserGroupSelector",
	"ier/messages",
	"idx/html",
	"dojo/text!./templates/AddObjectStorePermissionDialogContent.html"
], function(declare, lang, connect, array, kernel, construct, domClass, Tooltip, BorderContainer, ContentPane, Select, BaseDialog, LoggerMixin, Messages, Desktop, Permission, UserGroup, UserGroupSelector, ier_messages, idxHtml, template) {
	/**
	 * @private Part of the ObjectStoreSecurityPane widget.
	 * @name ier.widget.dialog.AddObjectStorePermissionDialog
	 * @class Provides a dialog box that is used to add permissions to a repository.
	 * @augments ecm.widget.dialog.BaseDialog
	 */
	return declare("ier.widget.dialog.AddObjectStorePermissionDialog", [
		BaseDialog,
		LoggerMixin
	], {
		/** @lends ier.widget.dialog.AddObjectStorePermissionDialog.prototype */

		// Path to the template
		contentString: template,
		widgetsInTemplate: true,

		_messages: ecm.messages,

		postCreate: function() {
			this.logEntry("postCreate");
			this.inherited(arguments);
			domClass.add(this.domNode, "ecmAddPermissionDialog");
			this.setTitle(this._messages.add_permissions_label);
			// Set the cancel button label to Close
			this.cancelButtonLabel = this._messages.close;
			this._applyButton = this.addButton(this._messages.apply_button_label, "_onApply", false, false);
			this._addButton = this.addButton(this._messages.add_permission_add_label, "_onAdd", false, false);
			this.setResizable(true);

			if (this._selector)
				this._selector.destroy();
			this._selector = new UserGroupSelector({
				defaultScope: "groups",
				selectedItems: [],
				showPseudoGroups: this.repository._isP8(),
				filteredItems: this.filteredItems
			});
			this._userGroupSelector.set("content", this._selector);
			this._selector.startup();
			this._selector.createRendering(this.repository);

			connect.connect(this._selector, "onChange", this, "_onSelectedItemsChanged");
			this._onSelectedItemsChanged();

			this.logExit("postCreate");
		},

		/**
		 * @private
		 */
		_onApply: function() {
			var selectedItems = this._selector.getSelectedItems();
			var permissions = this._getPermissions(selectedItems);
			if (this.filteredItems) {
				this.filteredItems = this.filteredItems.concat(selectedItems);
				this._selector.setFilteredItems(this.filteredItems);
			}
			this._selector.clearSelectedItems();
			this._onSelectedItemsChanged();
			if (this.callback) {
				this.callback(permissions);
			}
		},

		/**
		 * @private
		 */
		_onAdd: function() {
			this._onApply();
			this.onCancel();
		},

		/**
		 * Sets the focus in the form input.
		 */
		focus: function() {
			this.logEntry("focus");
			this._selector.focus();
			this.logExit("focus");
		},

		/**
		 * @private
		 */
		_getPermissions: function(selectedItems) {
			this.logEntry("getPermissions");
			var permissions = [];

			array.forEach(selectedItems, function(entry, index) {
				var granteeType = (entry.isInstanceOf && entry.isInstanceOf(UserGroup)) ? Permission.GRANTEE_TYPE.GROUP : Permission.GRANTEE_TYPE.USER;
				permissions.push(new Permission({
					id: entry.id,
					name: entry.name,
					displayName: entry.displayName,
					granteeType: granteeType
				}));
			});

			this.logExit("getPermissions");

			return permissions;
		},

		/**
		 * @private
		 */
		_onSelectedItemsChanged: function() {
			if (this._selector.getSelectedItems().length > 0) {
				this._applyButton.set("disabled", false);
				this._addButton.set("disabled", false);
			} else {
				this._applyButton.set("disabled", true);
				this._addButton.set("disabled", true);
			}
		},

		/**
		 * Cleans up the widget.
		 */
		destroy: function() {
			this._applyButton.destroy();
			this._addButton.destroy();
			this._selector.destroy();
			this.filteredItems = null;
			this.inherited(arguments);
		}

	});
});
