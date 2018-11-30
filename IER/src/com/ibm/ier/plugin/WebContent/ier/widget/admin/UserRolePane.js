define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/data/ItemFileReadStore",
	"dojo/date/locale",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dijit/Dialog",
	"dijit/TitlePane",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"dijit/form/Button",
	"dijit/form/TextBox",
	"dijit/form/DateTextBox",
	"dijit/Tree",
	"dijit/tree/ForestStoreModel",
	"idx/data/JsonStore",
	"gridx/Grid",
	"gridx/core/model/cache/Async",
	"gridx/tests/support/modules",
	"ecm/widget/dialog/ConfirmationDialog",
	"dojo/text!./templates/UserRolePane.html",
	"dijit/layout/BorderContainer", // template
	"dijit/layout/ContentPane", // template
	"dijit/form/DropDownButton", // template
	"dijit/Menu", // template
	"dijit/form/Select", // template
	"dojox/grid/DataGrid", // template
	"idx/layout/MoveableTabContainer" // in template
], function(declare, dojo_lang, dojo_array, dojo_data_ItemFileReadStore, dojo_date_locale, dojo_construct, dojo_style,
		dijit_Dialog, dijit_TitlePane, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin,
		dijit_form_Button, dijit_form_TextBox, dijit_form_DateTextBox, dijit_Tree, dijit_tree_ForestStoreModel,
		idx_data_JsonStore, idx_gridx_Grid, idx_gridx_Cache, idx_gridx_modules, ecm_widget_dialog_ConfirmationDialog, UserRolePane_html){

var roleDialog = null;
var roleAssociateDialog = null;
var userAssociateDialog = null;

var userRolePane = declare([_Widget, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: UserRolePane_html,

	postCreate: function(){
		this.inherited(arguments);

		// RolePane
		this._roleStore = new idx_data_JsonStore({data: [
			{name: "Disposer", description:"role to dipose entities", func:["Disposition", "Disposition Sweep", "Define disposition events"], users: ["Administrator 1", "User 1", "Admin Group"], expiration:"2013-06-01T12:34:56.000Z", creator:"p8admin", dateCreated:"2012-08-01T12:34:56.000Z"},
			{name: "Viewer", description:"role to view", func:["View"], users: ["Administrator 2", "User 2", "Normal Group"], expiration:"2013-06-01T12:34:56.000Z", creator:"p8admin", dateCreated:"2012-08-01T12:34:56.000Z"},
			{name: "Auditor", description:"role to audit", func:["Auditing", "Auditing reports", "Reporting"], users: ["Administrator 1", "Administrator 2"], expiration:"2013-06-01T12:34:56.000Z", creator:"p8admin", dateCreated:"2012-08-01T12:34:56.000Z"},
			{name: "Administrator", description:"administrator role", func:["Maintain roles", "Move", "Copy", "Delete"], users: ["Administrator 1", "Administrator 2", "Admin Group"], expiration:"2013-06-01T12:34:56.000Z", creator:"p8admin", dateCreated:"2012-08-01T12:34:56.000Z"},
			{name: "User", description:"user role", func:["File", "Search update and view hold reasons"], users: ["User 1", "User 2", "User 3", "Normal Group"], expiration:"2013-06-01T12:34:56.000Z", creator:"p8admin", dateCreated:"2012-08-01T12:34:56.000Z"}
		]});
		this._roleGrid.setStore(this._roleStore);
		this._roleGrid.getCell(2).formatter = function(value, index){
			var store = this.grid.store;
			var item = this.grid.getItem(index);
			var data = store.getItemData(item);
			return data.func && data.func.join ? data.func.join(", ") : data.func;
		};
		this._roleGrid.getCell(4).formatter = function(value, index){
			var store = this.grid.store;
			var item = this.grid.getItem(index);
			var data = store.getItemData(item);
			return data.users && data.users.join ? data.users.join(", ") : data.users;
		};
		this._roleGrid.getCell(3).formatter = function(value){
			if(value){
				value = dojo_date_locale.format(new Date(value), {datePattern: "MM/dd/yyyy", timePattern: "HH:mm:ss"});
			}
			return value;
		};
		this._roleGrid.getCell(6).formatter = function(value){
			if(value){
				value = dojo_date_locale.format(new Date(value), {datePattern: "MM/dd/yyyy", timePattern: "HH:mm:ss"});
			}
			return value;
		};
		var changeRole = function(){
			var items = this._roleGrid.selection.getSelected();
			if(items && items.length){
				this._rolePropertyButton.set("disabled", false);
			}else{
				this._rolePropertyButton.set("disabled", true);
			}
		};
		changeRole.call(this);
		this.connect(this._roleGrid, "onSelectionChanged", changeRole);
		this._roleGrid.queryOptions = {ignoreCase: true};
		var filterRole = function(){
			var query = {};
			var type = this._roleTypeSelect.get("value");
			if(type){
				query.type = type;
			}
			var text = this._roleFilterBox.get("value");
			if(text){
				query.name = "*" + text + "*";
			}
			this._roleGrid.filter(query);
		};
		this.connect(this._roleFilterBox, "onChange", filterRole);
		this.connect(this._roleRefreshButton, "onClick", function(){
			this._roleGrid.selection.deselectAll();
		});
		this.connect(this._roleCreateButton, "onClick", function(){
			if(this._roleDialog){
				this._roleDialog.destroy();
			}
			this._roleDialog = new roleDialog({
				title: "Add Role", "class": "ierAdminDialog"});
			this.connect(this._roleDialog, "onExecute", function(){
				var role = this._roleDialog.get("role");
				this._roleStore.newItem(role);
			});
			this._roleDialog.set("role", null);
			this._roleDialog.show();
		});
		this.connect(this._rolePropertyButton, "onClick", function(){
			if(this._roleDialog){
				this._roleDialog.destroy();
			}
			this._roleDialog = new roleDialog({
				title: "Edit Priviledges", executeLabel: "Update", "class": "ierAdminDialog"});
			this.connect(this._roleDialog, "onExecute", function(){
				var items = this._roleGrid.selection.getSelected();
				var item = items && items[0];
				if(item){
					var role = this._roleDialog.get("role");
					for(var name in role){
						this._roleStore.setValue(item, name, role[name]);
					}
				}
			});
			var items = this._roleGrid.selection.getSelected();
			var role = items && items[0] && this._roleStore.getItemData(items[0]);
			this._roleDialog.set("role", role);
			this._roleDialog.show();
		});
		this.connect(this._roleUserButton, "onClick", function(){
			if(!this._roleAssociateDialog){
				this._roleAssociateDialog = new roleAssociateDialog({
					title: "Edit Membership", executeLabel: "Update", "class": "ierAdminDialog"});
				this.connect(this._roleAssociateDialog, "onExecute", function(){
					var items = this._roleGrid.selection.getSelected();
					var item = items && items[0];
					if(item){
						var users = this._roleAssociateDialog.get("users");
						this._roleStore.setValue(item, "users", users);
					}
				});
			}
			var items = this._roleGrid.selection.getSelected();
			var role = items && items[0] && this._roleStore.getItemData(items[0]);
			this._roleAssociateDialog.set("users", role.users);
			this._roleAssociateDialog.show();
			this._roleAssociateDialog.resize();
		});
		this.connect(this._roleDeleteButton, "onClick", function(){
			if(!this._roleDeleteDialog){
				this._roleDeleteDialog = new ecm_widget_dialog_ConfirmationDialog({
					title: "Delete Role", text: "Do you want to delete the selected role?",
					buttonLabel: "Delete", cancelButtonDefault: true});
				this.connect(this._roleDeleteDialog, "onExecute", function(){
					var store = this._roleStore;
					var items = this._roleGrid.selection.getSelected();
					this._roleGrid.selection.deselectAll();
					dojo_array.forEach(items, function(item){
						store.deleteItem(item);
					});
				});
			}
			this._roleDeleteDialog.show();
		});

		// UserPane
		this._userStore = new idx_data_JsonStore({data: [
			{name: "Administrator 1", type: "User", roles: ["Disposer", "Auditor", "Administrator"]},
			{name: "Administrator 2", type: "User", roles: ["Viewer", "Auditor", "Administrator"]},
			{name: "User 1", type: "User", roles: ["Disposer", "User"]},
			{name: "User 2", type: "User", roles: ["Viewer", "User"]},
			{name: "User 3", type: "User", roles: ["User"]},
			{name: "Normal Group", type: "Group", roles: ["Viewer", "User"]},
			{name: "Admin Group", type: "Group", roles: ["Disposer", "Administrator"]}
		]});
		this._userGrid.setStore(this._userStore);
		this._userGrid.getCell(1).formatter = function(value, index){
			var store = this.grid.store;
			var item = this.grid.getItem(index);
			var data = store.getItemData(item);
			return data.roles && data.roles.join ? data.roles.join(", ") : data.roles;
		};
		var initialize = false;
		var changeUser = function(){
			var items = this._userGrid.selection.getSelected();
			if(items && items.length){
				this._userPropertyButton.set("disabled", false);
				this._userRoleButton.set("disabled", false);
				if(!initialize){
					this._userGridContainer.restore();
					initialize = true;
				}
//				dojo_style.set(this.userPropArea, "display", "");
			}else{
				this._userPropertyButton.set("disabled", true);
				this._userRoleButton.set("disabled", true);
//				dojo_style.set(this.userPropArea, "display", "none");
			}
		};
		changeUser.call(this);
		this.connect(this._userGrid, "onSelectionChanged", changeUser);
		this._userGrid.queryOptions = {ignoreCase: true};
		var filterUser = function(){
			var query = {};
			var type = this._userTypeSelect.get("value");
			if(type){
				query.type = type;
			}
			var text = this._userFilterBox.get("value");
			if(text){
				query.name = "*" + text + "*";
			}
			this._userGrid.filter(query);
		};
		this.connect(this._userFilterBox, "onChange", filterUser);
		this.connect(this._userRefreshButton, "onClick", function(){
			this._userGrid.selection.deselectAll();
		});
		this.connect(this._userRoleButton, "onClick", function(){
			if(!this._userAssociateDialog){
				this._userAssociateDialog = new userAssociateDialog({
					title: "Assign Role", executeLabel: "Update", "class": "ierAdminDialog"});
				this.connect(this._userAssociateDialog, "onExecute", function(){
					var items = this._userGrid.selection.getSelected();
					var item = items && items[0];
					if(item){
						var roles = this._userAssociateDialog.get("roles");
						this._userStore.setValue(item, "roles", roles);
					}
				});
			}
			var items = this._userGrid.selection.getSelected();
			var user = items && items[0] && this._userStore.getItemData(items[0]);
			this._userAssociateDialog.set("roles", user.roles);
			this._userAssociateDialog.show();
			this._userAssociateDialog.resize();
		});
		this.connect(this._userPropertyButton, "onClick", function(){
			// todo
		});
		var filterUserType = function(){
			var query = {};
			var type = this._userTypeSelect.get("value");
			if(type){
				query.type = type;
			}
			this._userGrid.filter(query);
		};
		filterUserType.call(this);
		this.connect(this._userTypeSelect, "onChange", filterUserType);
		this._userRoleStore = new dojo_data_ItemFileReadStore({data: {
			identifier: 'id',
			label: 'name',
			items: [
				{ id: 'ROOT', name:'Sort by Role', children:[ {_reference: 'USER1'}, {_reference: 'USER2'} ] },
				{ id: 'USER1', name:'User 1', children:[ {_reference: 'ROLE1' }, {_reference: 'ROLE2' } ] },
				{ id: 'ROLE1', name:'Disposer', children:[ {_reference: 'TASK1-1' }, {_reference: 'TASK1-2' }, {_reference: 'TASK1-3' } ] },
				{ id: 'TASK1-1', name:'Disposition'},
				{ id: 'TASK1-2', name:'Disposition Sweep'},
				{ id: 'TASK1-3', name:'Define disposition events'},
				{ id: 'ROLE2', name:'User', children:[ {_reference: 'TASK2-1' }, {_reference: 'TASK2-2' } ] },
				{ id: 'TASK2-1', name:'File'},
				{ id: 'TASK2-2', name:'Search update and view hold reasons'},
				{ id: 'USER2', name:'Normal Group', children:[ {_reference: 'ROLE2' }, {_reference: 'ROLE3' } ] },
				{ id: 'ROLE3', name:'Viewer', children:[ {_reference: 'TASK3' } ] },
				{ id: 'TASK3', name:'View'}
			]
		}});
		this._userRoleModel = new dijit_tree_ForestStoreModel({
			store: this._userRoleStore,
			query: {"id": "USER*"},
			rootId: "ROOT",
			childrenAttrs: ["children"]
		});
		this._userTaskStore = new dojo_data_ItemFileReadStore({data: {
			identifier: 'id',
			label: 'name',
			items: [
				{ id: 'ROOT', name:'Sort by Task', children:[ {_reference: 'TASK1-1'}, {_reference: 'TASK1-2'}, {_reference: 'TASK1-3'}, {_reference: 'TASK2-1'}, {_reference: 'TASK2-2'}, {_reference: 'TASK3'} ] },
				{ id: 'USER1', name:'User 1' },
				{ id: 'ROLE1', name:'Disposer', children:[ {_reference: 'USER1' } ] },
				{ id: 'TASK1-1', name:'Disposition', children:[ {_reference: 'ROLE1' } ]  },
				{ id: 'TASK1-2', name:'Disposition Sweep', children:[ {_reference: 'ROLE1' } ] },
				{ id: 'TASK1-3', name:'Define disposition events', children:[ {_reference: 'ROLE1' } ] },
				{ id: 'ROLE2', name:'User', children:[ {_reference: 'USER1' }, {_reference: 'USER2' } ] },
				{ id: 'TASK2-1', name:'File', children:[ {_reference: 'ROLE2' } ] },
				{ id: 'TASK2-2', name:'Search update and view hold reasons', children:[ {_reference: 'ROLE2' } ] },
				{ id: 'USER2', name:'Normal Group' },
				{ id: 'ROLE3', name:'Viewer', children:[ {_reference: 'USER2' } ] },
				{ id: 'TASK3', name:'View', children:[ {_reference: 'ROLE3' } ] }
			]
		}});
		this._userTaskModel = new dijit_tree_ForestStoreModel({
			store: this._userTaskStore,
			query: {"id": "TASK*"},
			rootId: "ROOT",
			childrenAttrs: ["children"]
		});
		this._userPropTree = new dijit_Tree({
			showRoot: false,
	        model: this._userRoleModel
	    });
		dojo_construct.place(this._userPropTree.domNode, this.userPropArea.containerNode);
		var filterRoleSort = function(){
			if(this._userPropTree){
				this._userPropTree.destroy();
			}
			var value = this._roleSortSelect.get("value");
			if(value == "Role"){
				this._userPropTree = new dijit_Tree({
					showRoot: false,
			        model: this._userRoleModel
			    });
			}else{
				this._userPropTree = new dijit_Tree({
					showRoot: false,
			        model: this._userTaskModel
			    });
			}
			dojo_construct.place(this._userPropTree.domNode, this.userPropArea.containerNode);
		};
		this.connect(this._roleSortSelect, "onChange", filterRoleSort);
	}
});

roleDialog = declare("ier.widget.admin.RoleDialog", dijit_Dialog, {

	executeLabel: "Add",

	destroy: function(){
		if(this._privGrid){
			this._privGrid.destroy();
		}
		if(this._executeButton){
			this._executeButton.destroy();
		}
		if(this._cancelButton){
			this._cancelButton.destroy();
		}
		this.inherited(arguments);
	},

	postCreate: function(){
		this.inherited(arguments);

		var contentArea = dojo_construct.create("div", {"class": "dijitDialogPaneContentArea"}, this.containerNode);
		this._rolePane = new dijit_TitlePane({title: "Role"});
		dojo_construct.place(this._rolePane.domNode, contentArea);

		var table = dojo_construct.create("table", {border: 0, cellpadding: 0, cellspacing: 0, style: "border-collapse: collapse;"}, this._rolePane.containerNode);
		var body = dojo_construct.create("tbody", {}, table);

		var row = dojo_construct.create("tr", {}, body);
		var cell = dojo_construct.create("td", {"class": "ierLabelContainer", innerHTML: "Name:"}, row);
		cell = dojo_construct.create("td", {"class": "ierControlContainer"}, row);
		this._roleNameBox = new dijit_form_TextBox({"class": "ierLongBox"});
		dojo_construct.place(this._roleNameBox.domNode, cell);

		row = dojo_construct.create("tr", {}, body);
		cell = dojo_construct.create("td", {"class": "ierLabelContainer", innerHTML: "Description:"}, row);
		cell = dojo_construct.create("td", {"class": "ierControlContainer"}, row);
		this._descriptionBox = new dijit_form_TextBox({"class": "ierLongBox"});
		dojo_construct.place(this._descriptionBox.domNode, cell);

		row = dojo_construct.create("tr", {}, body);
		cell = dojo_construct.create("td", {"class": "ierLabelContainer", innerHTML: "Expiration date:"}, row);
		cell = dojo_construct.create("td", {"class": "ierControlContainer"}, row);
		this._expirationBox = new dijit_form_DateTextBox({});
		dojo_construct.place(this._expirationBox.domNode, cell);

		contentArea = dojo_construct.create("div", {"class": "dijitDialogPaneContentArea"}, this.containerNode);
		this._associatePane = new dijit_TitlePane({title: "Priviledges"});
		dojo_construct.place(this._associatePane.domNode, contentArea);

//		var cell = dojo_construct.create("div", {"class": "ierLabelContainer", innerHTML: "Function:"}, this._associatePane.containerNode);
		var cell = dojo_construct.create("div", {style: "height: 300px;", "class": "ierControlContainer"}, this._associatePane.containerNode);
		var store = new idx_data_JsonStore({data: [
			{name: "Disposition" },
			{name: "Disposition Sweep" },
			{name: "Define disposition events" },
			{name: "View" },
			{name: "Auditing" },
			{name: "Auditing reports" },
			{name: "Reporting" },
			{name: "Maintain roles" },
			{name: "Move" },
			{name: "Copy" },
			{name: "Delete" },
			{name: "File" },
			{name: "Search update and view hold reasons" }
		]});
		this._privGrid = new idx_gridx_Grid({
			id: "privGrid",
			cacheClass: idx_gridx_Cache,
			store: store,
			structure: [
				{id: 'name', field: 'name', name: 'Privilege Name', width: '200px'}
			],
			modules: [
				idx_gridx_modules.Focus,
				idx_gridx_modules.ColumnResizer,
				idx_gridx_modules.RowHeader,
				idx_gridx_modules.IndirectSelect,
				idx_gridx_modules.VirtualVScroller
			].concat([{moduleClass: idx_gridx_modules.ExtendedSelectRow, triggerOnCell: true}])
		});
		dojo_style.set(this._privGrid.domNode, "height", "100%");
		dojo_construct.place(this._privGrid.domNode, cell);
		this._privGrid.startup();

		var actionBar = dojo_construct.create("div", {"class": "dijitDialogPaneActionBar"}, this.containerNode);
		this._executeButton = new dijit_form_Button({label: this.executeLabel});
		this.connect(this._executeButton, "onClick", function(){
			this.onExecute();
		});
		dojo_construct.place(this._executeButton.domNode, actionBar);
		this._cancelButton = new dijit_form_Button({label: "Cancel"});
		this.connect(this._cancelButton, "onClick", function(){
			this.onCancel();
		});
		dojo_construct.place(this._cancelButton.domNode, actionBar);
	},

	_getRoleAttr: function(){
		var name = this._roleNameBox.get("value");
		var description = this._descriptionBox.get("value");
		var date = this._expirationBox.get("value") || new Date();
		var expiration = date.toISOString();
		var items = this._privGrid.select.row.getSelected();
		var data = this._privGrid.store.getData();
		var func = [];
		dojo_array.forEach(items, dojo_lang.hitch(this, function(item){
			if(data[item]){
				func.push(data[item].name);
			}
		}));
		return {name: name, description: description, expiration: expiration, func: func, users: "", creator:"p8admin", dateCreated:new Date().toISOString()};
	},

	_setRoleAttr: function(role){
		role = role || {};
		this._roleNameBox.set("value", role.name || "");
		this._descriptionBox.set("value", role.description || "");
		this._expirationBox.set("value", role.expiration || null);
		var data = this._privGrid.store.getData();
		dojo_array.forEach(role.func, dojo_lang.hitch(this, function(name){
			var index = 0;
			dojo_array.forEach(data, dojo_lang.hitch(this, function(item){
				if(name == item.name){
					this._privGrid.select.row.selectByIndex(index);
				}
				index++;
			}));
			
		}));
	}
});

roleAssociateDialog = declare("ier.widget.admin.RoleAssociateDialog", dijit_Dialog, {

	executeLabel: "Role Associate",

	destroy: function(){
		if(this._userNameGrid){
			this._userNameGrid.destroy();
		}
		if(this._executeButton){
			this._executeButton.destroy();
		}
		if(this._cancelButton){
			this._cancelButton.destroy();
		}
		this.inherited(arguments);
	},

	postCreate: function(){
		this.inherited(arguments);

		var contentArea = dojo_construct.create("div", {"class": "dijitDialogPaneContentArea"}, this.containerNode);
		this._rolePane = new dijit_TitlePane({title: "Users and Groups"});
		dojo_construct.place(this._rolePane.domNode, contentArea);

//		var cell = dojo_construct.create("div", {"class": "ierLabelContainer", innerHTML: "Users and groups:"}, this._rolePane.containerNode);
		var cell = dojo_construct.create("div", {style: "height: 300px;", "class": "ierControlContainer"}, this._rolePane.containerNode);
		var store = new idx_data_JsonStore({data: [
			{name: "Administrator 1" },
			{name: "Administrator 2" },
			{name: "User 1" },
			{name: "User 2" },
			{name: "User 3" },
			{name: "Admin Group" },
			{name: "Normal Group" }
		]});
		this._userNameGrid = new idx_gridx_Grid({
			id: "userNameGrid",
			cacheClass: idx_gridx_Cache,
			store: store,
			structure: [
				{id: 'name', field: 'name', name: 'Name', width: '200px'}
			],
			modules: [
				idx_gridx_modules.Focus,
				idx_gridx_modules.ColumnResizer,
				idx_gridx_modules.RowHeader,
				idx_gridx_modules.IndirectSelect,
				idx_gridx_modules.VirtualVScroller
			].concat([{moduleClass: idx_gridx_modules.ExtendedSelectRow, triggerOnCell: true}])
		});
		dojo_style.set(this._userNameGrid.domNode, "height", "100%");
		dojo_construct.place(this._userNameGrid.domNode, cell);
		this._userNameGrid.startup();

		var actionBar = dojo_construct.create("div", {"class": "dijitDialogPaneActionBar"}, this.containerNode);
		this._executeButton = new dijit_form_Button({label: this.executeLabel});
		this.connect(this._executeButton, "onClick", function(){
			this.onExecute();
		});
		dojo_construct.place(this._executeButton.domNode, actionBar);
		this._cancelButton = new dijit_form_Button({label: "Cancel"});
		this.connect(this._cancelButton, "onClick", function(){
			this.onCancel();
		});
		dojo_construct.place(this._cancelButton.domNode, actionBar);
	},

	_getUsersAttr: function(){
		var items = this._userNameGrid.select.row.getSelected();
		var data = this._userNameGrid.store.getData();
		var users = [];
		dojo_array.forEach(items, dojo_lang.hitch(this, function(item){
			if(data[item]){
				users.push(data[item].name);
			}
		}));
		return users;
	},

	_setUsersAttr: function(users){
		users = users || {};
		var data = this._userNameGrid.store.getData();
		dojo_array.forEach(users, dojo_lang.hitch(this, function(name){
			var index = 0;
			dojo_array.forEach(data, dojo_lang.hitch(this, function(item){
				if(name == item.name){
					this._userNameGrid.select.row.selectByIndex(index);
				}
				index++;
			}));
			
		}));
	}
});

userAssociateDialog = declare("ier.widget.admin.UserAssociateDialog", dijit_Dialog, {

	executeLabel: "User Associate",

	destroy: function(){
		if(this._roleNameGrid){
			this._roleNameGrid.destroy();
		}
		if(this._executeButton){
			this._executeButton.destroy();
		}
		if(this._cancelButton){
			this._cancelButton.destroy();
		}
		this.inherited(arguments);
	},

	postCreate: function(){
		this.inherited(arguments);

		var contentArea = dojo_construct.create("div", {"class": "dijitDialogPaneContentArea"}, this.containerNode);
		this._userPane = new dijit_TitlePane({title: "Roles"});
		dojo_construct.place(this._userPane.domNode, contentArea);

//		var cell = dojo_construct.create("div", {"class": "ierLabelContainer", innerHTML: "Roles:"}, this._userPane.containerNode);
		var cell = dojo_construct.create("div", {style: "height: 300px;", "class": "ierControlContainer"}, this._userPane.containerNode);
		var store = new idx_data_JsonStore({data: [
			{name: "Disposer" },
			{name: "Auditor" },
			{name: "Administrator" },
			{name: "Viewer" },
			{name: "User" }
		]});
		this._roleNameGrid = new idx_gridx_Grid({
			id: "roleNameGrid",
			cacheClass: idx_gridx_Cache,
			store: store,
			structure: [
				{id: 'name', field: 'name', name: 'Role Name', width: '200px'}
			],
			modules: [
				idx_gridx_modules.Focus,
				idx_gridx_modules.ColumnResizer,
				idx_gridx_modules.RowHeader,
				idx_gridx_modules.IndirectSelect,
				idx_gridx_modules.VirtualVScroller
			].concat([{moduleClass: idx_gridx_modules.ExtendedSelectRow, triggerOnCell: true}])
		});
		dojo_style.set(this._roleNameGrid.domNode, "height", "100%");
		dojo_construct.place(this._roleNameGrid.domNode, cell);
		this._roleNameGrid.startup();

		var actionBar = dojo_construct.create("div", {"class": "dijitDialogPaneActionBar"}, this.containerNode);
		this._executeButton = new dijit_form_Button({label: this.executeLabel});
		this.connect(this._executeButton, "onClick", function(){
			this.onExecute();
		});
		dojo_construct.place(this._executeButton.domNode, actionBar);
		this._cancelButton = new dijit_form_Button({label: "Cancel"});
		this.connect(this._cancelButton, "onClick", function(){
			this.onCancel();
		});
		dojo_construct.place(this._cancelButton.domNode, actionBar);
	},

	_getRolesAttr: function(){
		var items = this._roleNameGrid.select.row.getSelected();
		var data = this._roleNameGrid.store.getData();
		var roles = [];
		dojo_array.forEach(items, dojo_lang.hitch(this, function(item){
			if(data[item]){
				roles.push(data[item].name);
			}
		}));
		return roles;
	},

	_setRolesAttr: function(roles){
		roles = roles || {};
		var data = this._roleNameGrid.store.getData();
		dojo_array.forEach(roles, dojo_lang.hitch(this, function(name){
			var index = 0;
			dojo_array.forEach(data, dojo_lang.hitch(this, function(item){
				if(name == item.name){
					this._roleNameGrid.select.row.selectByIndex(index);
				}
				index++;
			}));
			
		}));
	}
});

return userRolePane;
});
