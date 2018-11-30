define([
	"dojo/_base/declare",
	"dojo/_base/kernel",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"dojo/_base/array",
	"dojo/dom-construct",
	"dojo/dom-geometry",
	"dojo/query",
	"dojo/string",
	"dojo/dom-geometry",
	"dijit/_Widget",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"ecm/LoggerMixin",
	"ecm/widget/Button",
	"ecm/widget/CompositeButton",
	"ecm/widget/HoverHelp",
	"ecm/model/Permission",
	"idx/html",
	"ier/messages",
	"ier/widget/dialog/AddObjectStorePermissionDialog",
	"dojo/text!./templates/ObjectStoreSecurityPane.html"
], function(declare, kernel, lang, connect, array, construct, domGeom, query, string, geometry, _Widget, _TemplatedMixin, _WidgetsInTemplateMixin, LoggerMixin, Button, CompositeButton, HoverHelp, Permission, idxHtml, ier_messages, AddObjectStorePermissionDialog, template) {

	/**
	 * @name ier.widget.panes.ObjectStoreSecurityPane
	 * @class
	 * @augments dijit._Widget
	 */
	return declare("ier.widget.panes.ObjectStoreSecurityPane", [
		_Widget,
		_TemplatedMixin,
		_WidgetsInTemplateMixin,
		LoggerMixin
	], {
	/** @lends ier.widget.panes.ObjectStoreSecurityPane.prototype */

		_template: {
			privileges: [
			    {
			    	id: "RMCLASSGUIDEADMINS",
			    	label: ier_messages.record_classificationAdministrator,
			    	securityGroup: 0
			    },
				{
					id: "RMADMINS",
					label: ier_messages.record_administrator,
					securityGroup: 1
				},
				{
					id: "RMMANAGERS",
					label: ier_messages.record_manager,
					securityGroup: 2
				},
				{
					id: "RMPRIVUSERS",
					label: ier_messages.record_privileged_user,
					securityGroup: 3
				},
				{
					id: "RMUSERS",
					label: ier_messages.record_user,
					securityGroup: 4
				}
			],
		},

		templateString: template,
		widgetsInTemplate: true,

		/**
		 * Indicates if the security pane should be resized to fit within its parent container.
		 */
		fitToParent: false,

		_messages: ecm.messages,
		_isReadonly: true,

		/**
		 * Sets the repository of the selected item.
		 * 
		 * @param repository
		 *            Instance of {@link ecm.model.Repository}
		 */
		setRepository: function(repository) {
			this.repository = repository;
		},

		/**
		 * Displays the permissions of the current selected item.
		 * 
		 * @param permissions
		 *            An array of {@link ecm.model.Permission}. The array contains the current permissions of the
		 *            selected item.
		 * @param isReadonly
		 *            A boolean value indicating if the permissions are shown as read only.
		 */
		renderPermissions: function(permissions, isReadonly) {
			this.logEntry("renderPermission");

			this._isReadonly = isReadonly;

			this._categorizePermission(permissions);

			if(this._addButtons){
				array.forEach(this._addButtons, function(button) {
					button.set("disabled", isReadonly);
				});
			}
			this.logExit("renderPermission");
		},

		/**
		 * @private
		 */
		_normalize: function(permissions) {
			this.logEntry("_normalize");

			this._sortPermissions(permissions);

			var normalizeList = [];
			array.forEach(permissions, function(permission) {
				var filteredPermission = null;
				array.some(normalizeList, function(item) {
					if (item.grantee.id == permission.id && item.securityGroup == permission.securityGroup) {
						filteredPermission = item;
					}
				});
				var listItem;
				if (filteredPermission) {
					listItem = filteredPermission;
				} else {
					listItem = {
						grantee: permission.grantee,
						granteeType: permission.granteeType,
						securityGroup: permission.securityGroup
					};
					normalizeList.push(listItem);
				}
			}, this);

			this.logExit("_normalize");

			return normalizeList;
		},

		/**
		 * @private
		 */
		_sortPermissions: function(permissions) {
			var sortArray = function(a, b) {
				if (a.displayName > b.displayName) {
					return 1;
				}
				if (a.displayName < b.displayName) {
					return -1;
				}
				return 0;
			};
			// Sort permissions
			permissions.sort(sortArray);
		},

		/**
		 * @private
		 */
		_categorizePermission: function(permissions) {

			this.logEntry("_categorizePermission");

			this._privilegeList = [];
			
			//remove the dod classified groups if it's not the right datamodel type
			if(this.repository.recordDatamodelType != "DoDClassified"){
				delete this._template.privileges[0];
			}
			
			this._loadPrivileges(this.repository);

			// Create privilege bucket to assign the permission.
			array.forEach(this._privileges, function(privilege) {
				if(privilege){
					this._privilegeList.push({
						id: privilege.id,
						name: privilege.label,
						securityGroup: privilege.securityGroup,
						permissions: []
					});
				}
			}, this);

			// Loop through the permissions and assign it to the appropriate privilege bucket.				
			var normalizePermissionList = this._normalize(permissions);
			if (normalizePermissionList.length > 0) {
				array.forEach(normalizePermissionList, function(permissionItem) {
					this._assignItem(permissionItem); // Assign the item to privilege bucket					
				}, this);
			}

			this._renderPermissionGrouping(this._privilegeList);

			this.logExit("_categorizePermission");

			return this._privilegeList;
		},

		/**
		 * @private
		 */
		_loadPrivileges: function(repository) {
			this._privileges = repository._isP8() ? this._template.privileges : [];
		},

		/**
		 * @private
		 */
		_renderPermissionGrouping: function(permissionList) {
			this.logEntry("_renderPermissionGrouping");
			// Renders privilege categories and their members
			var self = this;

			self._clear();
			self._addButtons = [];
			self._widgets = [];
			var membersDiv = construct.create("div");
			construct.place(membersDiv, self._centerPane, "only");
			array.forEach(permissionList, function(item, index) {
				if (self.repository._isP8()) {
					var div = construct.create("div", {
						'class': 'privilegeContainer'
					}, membersDiv);
					var leftPaneDiv = construct.create("div", {
						'class': 'leftPane',
						innerHTML: item.name + ":"
					}, div);

					var buttonDiv = construct.create("div", {
						'class': 'buttonPane',
					}, div);
					var button = new dijit.form.Button({
						label: ier_messages.add_with_elipsis
					});
					self._addButtons.push(button);
					buttonDiv.appendChild(button.domNode);
					connect.connect(button, "onClick", function(evt){
						self._onAdd(evt, item);
					});

					var members = construct.create("div", {
						'class': 'rightPane'
					}, div);

					array.forEach(item.permissions, function(perm, index) {
						var member = construct.create("div", {
							'class': "member"
						}, members);

						var button;
						var buttonLabel = self.repository._isCM() ? perm.permission.grantee.id : perm.permission.grantee.displayName;

						button = new ecm.widget.CompositeButton({
							label: buttonLabel,
							//disabled: true,
							actionIconClass: "removeIcon",
							actionAltText: string.substitute(self._messages.remove_member, [
								buttonLabel
							])
						});

						// Connect to the remove button of the composite button to handle deleting permission
						connect.connect(button, "_onActionButtonClick", function(evt) {
							// remove from privilege bucket
							item.permissions.splice(index, 1);
							self._renderPermissionGrouping(self._privilegeList);
							if(self._addButtons && self._addButtons[0]){
								self._addButtons[0].focus();
							}
							self.onChange();
						});

						member.appendChild(button.domNode); // Add member
						self._widgets.push(button);
					});

					members.appendChild(construct.create("div", {
						'class': "member",
						innerHTML: "&nbsp;"
					}));
				}
			});

			this.resize();

			this.logExit("_renderPermissionGrouping");
		},

		/**
		 * @private
		 */
		_onAdd: function(evt, item) {
			var addObjectStorePermissionDialog = new AddObjectStorePermissionDialog({
				repository: this.repository,
				filteredItems: this._getCurrentGrantees(item.securityGroup),
				callback: lang.hitch(this, function(permissions){
					this._addPermissions(item.securityGroup, permissions);
				})
			});

			addObjectStorePermissionDialog.show();
		},

		/**
		 * @private
		 */
		_getCurrentGrantees: function(securityGroup) {
			this.logEntry("_getCurrentGrantees");
			var currentGrantees = [];
			array.some(this._privilegeList, function(privilege) {
				if(privilege.securityGroup == securityGroup){
					array.forEach(privilege.permissions, function(entry) {
						currentGrantees.push(entry.permission.grantee);
					});
					return true;
				}
			});

			return currentGrantees;

			this.logExit("_getCurrentGrantees");
		},

		/**
		 * @private
		 */
		_addPermissions: function(privilegeId, permissions) {
			this.logEntry("_addPermissions");
			var privilege = null;
			array.some(this._privilegeList, function(item) {
				if (item.securityGroup == privilegeId) {
					privilege = item;
					return true;
				}
			});

			if (privilege) {
				array.forEach(permissions, function(permission) {
					var item = this._getItem(privilege, permission);
					if (!item) {
						var itemPermission = {
							grantee: permission.grantee,
							granteeType: permission.granteeType,
							securityGroup: privilege.securityGroup
						};
						// Add to the category list
						privilege.permissions.push({
							permission: itemPermission
						});
					}
				}, this);

				this.onChange();
			}
			this._renderPermissionGrouping(this._privilegeList);
			this.logExit("_addPermissions");
		},

		/**
		 * @private
		 */
		_assignItem: function(item) {
			this.logEntry("_assignItem");
			array.some(this._privileges, function(privilege, index) {
				var privilegeBucket = this._privilegeList[index];
				if (item.securityGroup == privilegeBucket.securityGroup) {
					privilegeBucket.permissions.push({
						permission: item
					});
					return true;
				}
			}, this);

			this.logExit("_assignItem");
		},

		/**
		 * @private
		 */
		_getItem: function(privilege, permission) {
			this.logEntry("_getItem");
			var item = null;
			array.some(privilege.permissions, function(entry) {
				if (entry.permission.grantee.id == permission.id) {
					item = entry;
					return true;
				}
			});

			this.logExit("_getItem");

			return item;
		},

		/**
		 * @private
		 */
		_removeItem: function(id) {
			this.logEntry("_removeItem");
			array.some(this._privilegeList, function(privilege) {
				var filteredPermissions = array.filter(privilege.permissions, function(entry) {
					return entry.permission.grantee.id != id;
				});

				if (filteredPermissions.length != privilege.permissions.length) {
					privilege.permissions = filteredPermissions;
					return true;
				}
			});

			this.logExit("_removeItem");
		},

		/**
		 * @private
		 */
		_createHoverHelp: function(hoverHelpText, hoverHelpLink) {
			var hoverHelp = new ecm.widget.HoverHelp({
				message: hoverHelpText,
				isFocusable: true,
				href: hoverHelpLink ? hoverHelpLink : null
			});
			hoverHelp.startup();
			if (!this._hoverHelpArray) {
				this._hoverHelpArray = [];
			}
			this._hoverHelpArray.push(hoverHelp);
			return hoverHelp;
		},

		/**
		 * Resizes the widget
		 */
		resize: function() {
			this.logEntry("resize");

			// Resize the left pane
			var width = 0;
			var items = query(".leftPane", this.domNode);
			array.forEach(items, function(item) {
				var w = geometry.getMarginBox(item).w;
				if (w > width) {
					width = w;
				}
			});

			if (width > 0) {
				array.forEach(items, function(item) {
					geometry.setMarginBox(item, {
						w: width
					});
				});
			}

			this.logExit("resize");
		},

		/**
		 * @private
		 */
		_clear: function() {
			this.logEntry("clear");
			if (this._addButtons) {
				array.forEach(this._addButtons, function(button) {
					if (!button._destroyed) {
						button.destroyRecursive();
					}
					delete button;
				});
				this._addButtons = null;
			}
			if (this._widgets) {
				array.forEach(this._widgets, function(item) {
					if (!item._destroyed) {
						item.destroyRecursive();
					}
					delete item;
				});
				this._widgets = null;
			}
			if (this._centerPane && this._centerPane.firstChild)
				this._centerPane.removeChild(this._centerPane.firstChild);
			this.logExit("clear");
		},

		/**
		 * Cleans up the widget.
		 */
		destroy: function() {
			this._clear();
			if (this._hoverHelpArray) {
				array.forEach(this._hoverHelpArray, function(item) {
					if (!item._destroyed) {
						item.destroyRecursive();
					}
					delete item;
				});
			}
			this._privileges = null;

			this.inherited(arguments);
		},

		/**
		 * Returns the current permission array.
		 * 
		 * @return Returns an array of {@link ecm.model.Permission}
		 */
		getPermissions: function() {
			this.logEntry("getPermissions");
			var permissions = null;
			if (!this._isReadonly) {
				permissions = this._getPermissions();
			}
			this.logExit("getPermissions");
			return permissions;
		},

		_getPermissions: function() {
			this.logEntry("_getPermissions");
			var permissionArray = [];
			array.forEach(this._privilegeList, function(privilege) {
				array.forEach(privilege.permissions, function(entry) {
					var jsonPermission = {};
					jsonPermission.granteeName = entry.permission.grantee.name;
					jsonPermission.granteeType = entry.permission.granteeType;
					jsonPermission.securityGroup = entry.permission.securityGroup;
					permissionArray.push(jsonPermission);
				});
			});

			this.logExit("_getPermissions");

			return permissionArray;
		},

		/**
		 * An event that gets trigger when a permission item has been modified, added, or removed from the permission
		 * array.
		 */
		onChange: function() {
		},

		/**
		 * Resets the security pane to its default values.
		 */
		reset: function() {
			this._isReadonly = true;
			this._privilegeList = [];
			if(this._addButtons){
				array.forEach(this._addButtons, function(button) {
					button.set("disabled", true);
				});
			}
			this._clear();
		}
	});
});
