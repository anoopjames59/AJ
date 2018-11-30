define([
	"dojo/_base/array",
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/Messages",
	"ecm/model/Desktop",
	"ecm/widget/layout/AdminPane", //call ICN's admin pane layer to force load the gzipped version with all the admin subclasses
	"ecm/widget/admin/model/_AdminObjectBase",
	"ecm/widget/dialog/ConfirmationDialog",
	"ecm/widget/layout/_LaunchBarPane",
	"ier/messages",
	"ier/model/admin/Config",
	"ier/util/dialog",
	"ier/widget/admin/RepositoryPane",
	"ier/widget/admin/SettingsPane",
	/*"ier/widget/admin/UserRolePane",*/
	"dojo/text!./templates/AdminPane.html",
	"ecm/widget/admin/AdminTabs", // template
	"ecm/widget/admin/AdminTree" // template
], function(array, declare, lang, ecm_messages, Desktop, AdminPane, _AdminObjectBase, ConfirmationDialog, _LaunchBarPane,
	messages, Config, util_dialog, RepositoryPane, SettingsPane, /*UserRolePane,*/ AdminPane_html){

var toc = [
	{id: "settings", name: messages.admin_settings, iconClass: "adminIconSettings", widgetClass: SettingsPane},
	{id: "desktops", name: messages.admin_desktops, iconClass: "adminIconDesktops"},
	{id: "repositories", name: messages.admin_repositories, iconClass: "adminIconRepositories"}
	/*{id: "userRole", name: messages.admin_userRoles, iconClass: "adminIconSettings", widgetClass: UserRolePane}*/
];

var AdminObject = declare(_AdminObjectBase, {

	getLabel: function() {
		return this.label || this.name;
	},

	getIconClass: function(){
		return this.iconClass;
	},

	mayHaveChildren: function(){
		if(this.id == "desktops" || this.id == "repositories"){
			return true;
		}else{
			return this.inherited(arguments);
		}
	},

	getChildren: function(onComplete){
		if(this.id == "desktops"){
			var treeModel = this.getTreeModel();
			Config.listConfig("desktop", function(list){
				var children = array.map(list || [], function(config){
					var object = new AdminObject(config.id, config.name);
					object.label = config.get("name");
					object.iconClass = "adminIconDesktop";
					object.widgetClass = SettingsPane;
					object.params = {desktopId: config.id};
					object.setTreeModel(treeModel);
					return object;
				});
				onComplete(children);
			});
		}else if(this.id == "repositories"){
			var treeModel = this.getTreeModel();
			Config.listConfig("repository", function(list){
				var children = array.map(list || [], function(config){
					var object = new AdminObject(config.id, config.name);
					object.label = config.get("name");
					object.iconClass = "adminIconRepositoryP8";
					object.widgetClass = RepositoryPane;
					object.params = {repositoryId: config.id};
					object.setTreeModel(treeModel);
					return object;
				});
				onComplete(children);
			});
		}else{
			this.inherited(arguments);
		}
	},

	onClick: function(){
		this.getTreeModel().onSelect(this);
	}

});

var AdminTreeModel = declare(null, {

	constructor: function(){
		this.root = new AdminObject("root");
		this.root.setTreeModel(this);

		array.forEach(toc, function(item){
			var object = new AdminObject(item.id, item.name);
			object.iconClass = item.iconClass;
			object.widgetClass = item.widgetClass;
			object.params = item.params;
			this.root.addChild(object);
		}, this);
	},

	destroy: function(){
	},

	getRoot: function(onItem){
		onItem(this.root);
	},

	mayHaveChildren: function(item){
		return item.mayHaveChildren();
	},

	getChildren: function(item, onComplete){
		item.getChildren(onComplete);
	},

	isItem: function(item){
		return item instanceof AdminObject;
	},

	fetchItemByIdentity: function(args){
	},

	getIdentity: function(item){
		return item.getIdentity();
	},

	getLabel: function(item) {
		return item.getLabel();
	},

	onChange: function(item){
	},

	onChildrenChange: function(parent, children){
	},

	onSelect: function(item){
	}

});

return declare(_LaunchBarPane, {

	templateString: AdminPane_html,

	postCreate: function(){
		this.inherited(arguments);

		this._model = new AdminTreeModel();
		this.connect(this._model, "onSelect", function(item){
			if(!item.widget && item.widgetClass){
				var title = item.label || item.name;
				var params = lang.mixin({title: title, iconClass: item.iconClass}, item.params);
				var widget = item.widget = new item.widgetClass(params);
				widget.connect(widget, "onClose", function(){
					if(widget.dirty){
						var dialog = new ConfirmationDialog({title: ecm_messages.close, text: ecm_messages.admin_prompt_close_without_save,
							buttonLabel: ecm_messages.yes, cancelButtonLabel: ecm_messages.no, cancelButtonDefault: true,
							onExecute: function(){
								widget.dirty = false;
								widget.getParent().closeChild(widget);
							}});
						dialog.show();
						util_dialog.manage(dialog);
						return false;
					}else{
						delete item.widget;
					}
				});
				widget.connect(widget, "onChange", function(){
					if(widget.controlButton){
						var mark = widget.dirty ? "* " : "";
						widget.controlButton.set("label", mark + title);
					}
				});
				widget.connect(widget, "onSelect", function(){
					// stub
				});
			}
			if(item.widget){
				this._tabs.openTab(item.widget);
			}
		});
		this._tree.setModel(this._model);
	},

	startup: function(){
		this.inherited(arguments);

		// select first
		this._model.root.getChildren(function(children){
			children[0].onClick();
		});
	}

});
});
