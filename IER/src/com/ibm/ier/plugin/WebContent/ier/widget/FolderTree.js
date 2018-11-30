define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"ecm/widget/FolderTree",
	"ecm/widget/Tree",
	"ecm/model/ContentItem",
	"ier/constants",
	"ier/util/menu",
	"ier/util/util",
	"ier/model/FolderTreeModel",
	"ier/model/_BaseEntityObject",
	"ier/widget/Tree",
	"dojo/text!./templates/FolderTree.html"
], function(dojo_declare, dojo_lang, dojo_construct, ecm_widget_FolderTree, ecm_widget_Tree, ecm_model_ContentItem,
		ier_constants, ier_util_menu, ier_util, ier_model_FolderTreeModel, ier_model_BaseEntityObject, ier_widget_Tree, templateString){

var folderTree = dojo_declare("ier.widget.FolderTree", [ ecm_widget_FolderTree ], {
	templateString: templateString,
	
	//RecordsManager
	rootFolderId: ier_constants.Id_RecordsManagementFolder,
	//set of cache items that are updated.  These items are updated and should be applied accordingly in the appropriate locations.
	//there's a timestamp that should be compared with to know that this item is newer and not stale.
	itemsUpdated: {},
	
	postCreate: function() {
		this.inherited(arguments);
	},
	
	setRepository: function(repository){
		this.inherited(arguments);
		
		if(this.itemsUpdateHandler)
			this.disconnect(this.itemsUpdateHandler);
		
		this.itemsUpdateHandler = this.connect(this.repository, "onItemsUpdated", function(item){
			this.itemsUpdated[item.id] = item;
		});
	},
	
	createTree: function(params) {
		//new way going forward without using private variables
		if(params){
			params.getRowClass = dojo_lang.hitch(this, this.getTreeRowClass);
			
			//override the tree's model.  We can not use this.treeModel which ICN overrides later on and does not allow the user to update
			//use this.newTreeModel instead if it's provided
			if(this.newTreeModel){
				params.model = this.newTreeModel;
			}
			return new ier_widget_Tree(params);
		}
	},
	
	getContextMenuType: function(targetNode) {
		var item = targetNode.item;
		return ier_util_menu.getContainerContextMenuType(item, true);
	},
	
	onContextMenuCreated: function(actionMenu, items) {
		if(actionMenu && items){
			var addRecordCategoryButton = actionMenu.getMenu(ier_constants.Action_AddRecordCategory);
			var addRecordFolderButton = actionMenu.getMenu(ier_constants.Action_AddRecordFolder);
			var addRecordVolumeButton = actionMenu.getMenu(ier_constants.Action_AddRecordVolume);
			
			if(items.length > 0 && addRecordCategoryButton && addRecordFolderButton && addRecordVolumeButton){
				var item = items[0];
				//if there's an update to the item, apply it
				if(this.itemsUpdated[item.id] != null && this.itemsUpdated[item.id].getTimeStamp() > item.getTimeStamp())
					item.setAllowedRMTypes(this.itemsUpdated[item.id].getAllowedRMTypes());
				ier_util_menu.updateAddContainersToolbar(items[0], addRecordCategoryButton, addRecordFolderButton, addRecordVolumeButton);
			}
		}
	}
});

return folderTree;
});
