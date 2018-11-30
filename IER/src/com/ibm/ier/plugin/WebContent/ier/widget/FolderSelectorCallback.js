/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
	"dojo/_base/declare"
], function(declare) {

	/**
	 * @name ecm.widget.FolderSelectorCallback
	 * @class This class provides callback functions for the folder selector dijit.
	 */
	return declare("ecm.widget.FolderSelectorCallback", null, {
		/** @lends ecm.widget.FolderSelectorCallback.prototype */

		/**
		 * The permission that will be passed into isSelectableByPermission to check whether a folder can be selected
		 * 
		 * @since 2.0.2
		 */
		permissionToCheck: null,

		/**
		 * The tooltip message that will be presented if a folder can not be selected
		 * 
		 * @since 2.0.2
		 */
		notSelectableTooltip: null,

		/**
		 * Instantiate a FolderSelectorCallback
		 * 
		 * @param permissionToCheck -
		 *            the permission that will be passed into isSelectableByPermission to check whether a folder can be
		 *            selected
		 * @param notSelectableTooltip -
		 *            the tooltip message that will be presented if a folder can not be selected
		 * @since 2.0.2
		 */
		constructor: function(permissionToCheck, notSelectableTooltip) {
			if (permissionToCheck instanceof Object && !permissionToCheck.declaredClass) {
				lang.mixin(this, permissionToCheck);
			} else {
				this.permissionToCheck = permissionToCheck;
				this.notSelectableTooltip = notSelectableTooltip;
			}
		},

		/**
		 * Returns true if the user has the required permission to select the folder, false otherwise.
		 */
		isSelectableByPermission: function(/*ecm.model.ContentItem*/folderItem, /*string*/privilege) {
			var permission = false;

			if (privilege != null && folderItem.hasPrivilege && folderItem.hasPrivilege(privilege) || (folderItem.continuationData != null)) {
				permission = true;
			}

			return permission;
		}
	});
});
