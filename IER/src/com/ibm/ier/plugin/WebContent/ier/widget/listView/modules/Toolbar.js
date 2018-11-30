define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dijit/registry",
    	"ecm/widget/listView/modules/Toolbar",
    	"ecm/model/Desktop",
    	"ecm/widget/ActionMenu",
    	"ier/constants",
    	"ier/util/menu"
], function(dojo_declare, dojo_array, dojo_lang, dijit_registry, ecm_widget_listView_modules_Toolbar, ecm_model_Desktop, ActionMenu, ier_constants, ier_util_menu){
	
return dojo_declare("ier.widget.listView.modules.Toolbar", [ ecm_widget_listView_modules_Toolbar ], {
	
	getParentFolder: function(){
		return this.getResultSet().parentFolder;
	},
	
	getRepository: function() {
		return this.getResultSet().repository;
	},
	
	getResultSet: function() {
		return this.contentList.getResultSet();
	},
	
	/**
	 * Override the load context menu to find the right actions menu
	 */
	loadContextMenu: function(selectedItems, callback) {
		var menu = null;
		var item = selectedItems? selectedItems[0] : null;
		//do it ourselves because not all items have a repository.  A repository is needed for the items
		if(item && (item instanceof ecm.model.AsyncTask || item instanceof ecm.model.AsyncTaskInstance)){
			
			//before loading the context menu for the item, if the item is an async task or async task instance, add TaskUser privileges if the user already has taskAdmin privileges
			for(var i in selectedItems){
				var item = selectedItems[i];
				if(item && (item instanceof ecm.model.AsyncTask || item instanceof ecm.model.AsyncTaskInstance)){
					if(item.hasPrivilege(ier_constants.Privilege_TaskAdmin)){
						item[ier_constants.Privilege_TaskUser] = true;
					}
				}
			}
			
			menu = ier_util_menu.getTaskContextMenuType(selectedItems);
			ecm_model_Desktop.loadMenuActions(menu, dojo_lang.hitch(this, function(actions){
				if(!this.actionMenu)
					this.actionMenu = new ActionMenu();
				this.actionMenu.createMenu(actions, selectedItems, "", null, ecm.model.desktop.getAuthenticatingRepository(), null, this.contentList.getResultSet(), {
					widget: this.contentList
				});
				this.onContextMenuCreated(this.actionMenu, selectedItems);
				dojo_array.forEach(this.actionMenu.getChildren(), function(child) {
					this.actionsButton.dropDown.addChild(child);
				}, this);
			}));
		}
		else {
			menu = ier_util_menu.getContainersAndRecordsContextMenuType(selectedItems);
			ecm_model_Desktop.loadMenuActions(menu, callback);
		}
	},
	
	/**
	 * Event invoked when toolbar buttons are created
	 */
	onToolbarButtonsCreated: function(toolbarButtons) {
		this.inherited(arguments);
		
		//sets up global action with a different onclick handler and appropriate privileges
		dojo_array.forEach(this.getToolbarButtons(), function(toolbarButton){
			var action = toolbarButton.action;
			
			//special case for refresh
			if(action.id == ier_constants.Action_Refresh)
				this.setupRefreshAction(this.findToolbarButton(toolbarButtons, ier_constants.Action_Refresh), this.isConfigure);
			else if(action && action.global){
				this.setupGlobalAction(this.findToolbarButton(toolbarButtons, action.id));
			}		
		}, this);
	},

	/**
	 * Change the action performed for adding containers to get the parent folder instead of selected items
	 * @param button
	 */
	setupAddContainerButtons: function(button){
		if(button) {
			var onClickHandler = function(evt) {
				var button = dijit_registry.getEnclosingWidget(evt.target);
				button.action.performAction(this.getRepository(), this.getParentFolder() ? [ this.getParentFolder() ] : null, null, null, this.getResultSet());
			};
			button.onClick = null;
			this.connect(button, "onClick", onClickHandler);
		}
	},
	
	/**
	 * Sets up the refresh action by changing the onClickHandler by giving it a different set of parameter values
	 */
	setupRefreshAction: function(refreshButton, isConfigure){
		if (refreshButton) {
			var onClickHandler = function(evt) {
				var button = dijit_registry.getEnclosingWidget(evt.target);
				button.action.performAction(this.getRepository(), this.getParentFolder(), isConfigure, this.getResultSet());
			};
			refreshButton.onClick = null;
			this.connect(refreshButton, "onClick", onClickHandler);
		}
	},

	/**
	 * Sets up a global action by enabling or disabling it by comparing the action's privileges against the repository's privileges
	 */
	setupGlobalAction: function(button){
		if(button && button.action.id != ier_constants.Action_Refresh){
			button.onClick = null;
			this.connect(button, "onClick", function(){
				button.action.performAction(this.getRepository(), null, null, null, this.getResultSet());
			});
			
			this._handleGlobalActions(button);
		}
	},
	
	/**
	 * Retrieves a specific toolbar button if it exists
	 */
	findToolbarButton: function(toolbarButtons, actionId) {
		var toolbarButton = null;
		if (toolbarButtons) {
			for ( var i in toolbarButtons) {
				if (toolbarButtons[i].action.id == actionId) {
					toolbarButton = toolbarButtons[i];
					break;
				}
			}
		}

		return toolbarButton;
	},
	
	_handleGlobalActions: function(button){
		var action = button.action;
		var canPerform = true;
		var privileges = {};
		var repository = this.getRepository();
		if(repository){
			privileges = repository ? repository.privileges : null;
		}
		privileges["privTaskAdminPermission"] = ecm_model_Desktop.taskManager.isTaskAdmin();
		//if you are a task admin, you are basically a task user as well
		privileges["privTaskUserPermission"] =  ecm_model_Desktop.taskManager.isTaskUser() || ecm_model_Desktop.taskManager.isTaskAdmin();

		//you need to have all the privileges
		if(action && action.privileges){
			for ( var j = 0; j < action.privileges.length; j++) {
				var privilege = action.privileges[j];
				if (privileges && privileges[privilege] != true) {
					canPerform = false;
				}
			}
		}
		else
			canPerform = false;
		
		button.set("disabled", !canPerform);
	},
	
	/**
	 * Overriding the updateToolbarState to handle special cases for global actions
	 * @param action
	 */
	updateToolbarState: function(action) {
		this.inherited(arguments);
		
		dojo_array.forEach(this.getToolbarButtons(), function(toolbarButton){
			var action = toolbarButton.action;
			
			//Add container buttons
			if(action && action.global && action.privileges && action.id != ier_constants.Action_Refresh){
				this._handleGlobalActions(toolbarButton);
			}
		}, this);
	}
});});
