define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dijit/registry",
    	"ecm/model/Desktop",
    	"ier/widget/listView/modules/Toolbar",
    	"ier/constants",
    	"ier/util/menu"
], function(dojo_declare, dojo_array, dojo_lang, dijit_registry, Desktop, ier_widget_listView_modules_Toolbar, ier_constants, ier_util_menu){
	
return dojo_declare("ier.widget.listView.modules.FilePlanToolbar", [ ier_widget_listView_modules_Toolbar ], {
	
	isConfigure: false,
	
	preload: function() {
		this.inherited(arguments);
		
		this.connect(Desktop, "onChange", function(changedModels){
			if(dojo_lang.isArray(changedModels)){
				for(var i in changedModels){
					var changedModel = changedModels[i];
					if(changedModel && this.getParentFolder() && changedModel.id == this.getParentFolder().id){
						this.contentList.getResultSet().parentFolder = changedModel;
						this.updateToolbarState();
					}
				}
			}
		});
	},
	
	/**
	 * Event invoked when toolbar buttons are created
	 */
	onToolbarButtonsCreated: function(toolbarButtons) {
		this.inherited(arguments);
		
		if(this.getParentFolder())
			this.updateContentListToolbar(this.getParentFolder());
		
		this.setupAddContainerButtons(this.findToolbarButton(toolbarButtons, ier_constants.Action_AddRecordCategory));
		this.setupAddContainerButtons(this.findToolbarButton(toolbarButtons, ier_constants.Action_AddRecordFolder));
		this.setupAddContainerButtons(this.findToolbarButton(toolbarButtons, ier_constants.Action_AddRecordVolume));
		this.setupAddContainerButtons(this.findToolbarButton(toolbarButtons, ier_constants.Action_AddFilePlan));
	},
	
	createToolButtons: function() {
		this.contentList.getResultSet().toolbarDef = ier_constants.MenuType_IERBrowseFilePlanToolbarMenu;
		
		this.inherited(arguments);
	},
	
	/**
	 * Overriding the updateToolbarState to handle special cases for AddContainer actions.  They should be disabled if the user has no rights to add to the current container
	 * @param action
	 */
	updateToolbarState: function(action) {
		this.inherited(arguments);
		
		if(this.getParentFolder())
			this.updateContentListToolbar(this.getParentFolder());
				
		//By default since all the add containers buttons are global actions, they are enabled.  Disable them explicitly if the user has no rights to perform the action based
		//on the parent folder and not the selected items.  Without doing this, the actions will be disabled based on the user's current selected item.
		dojo_array.forEach(this.getToolbarButtons(), function(toolbarButton){
			var action = toolbarButton.action;
			var canPerform = false;
			
			//Add container buttons
			if(action && (action.id == ier_constants.Action_AddRecordCategory || action.id == ier_constants.Action_AddRecordFolder 
					|| action.id == ier_constants.Action_AddRecordVolume || action.id == ier_constants.Action_AddFilePlan)){
				
				for ( var j = 0; j < action.privileges.length && !canPerform; j++) {
					var privilege = action.privileges[j];
					var parentFolder = this.getParentFolder();
					if (parentFolder && parentFolder.hasPrivilege(privilege)) {
						canPerform = true;
					}
				}
				toolbarButton.set("disabled", !canPerform);
			}
		}, this);
	},
	
	/**
	 * Update the add record container actions on the toolbar
	 */
	updateContentListToolbar: function(item){
		var addRecordCategoryButton = this.getToolbarButton(ier_constants.Action_AddRecordCategory);
		var addRecordFolderButton = this.getToolbarButton(ier_constants.Action_AddRecordFolder);
		var addRecordVolumeButton = this.getToolbarButton(ier_constants.Action_AddRecordVolume);
		ier_util_menu.updateAddContainersToolbar(item, addRecordCategoryButton, addRecordFolderButton, addRecordVolumeButton);
	}
});});
