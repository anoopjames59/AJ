define([ 
      "dojo/_base/declare",
      "dojo/_base/lang",
      "ecm/model/Desktop",
      "ecm/model/AsyncTask",
      "ecm/model/AsyncTaskInstance",
      "ecm/widget/listView/gridModules/RowContextMenu",
  	  "ier/constants",
      "ier/util/menu",
      "ier/model/_BaseEntityObject"
], function(dojo_declare, dojo_lang, ecm_model_Desktop, ecm_model_AsyncTask, ecm_model_AsyncTaskInstance, ecm_widget_listView_RowContextMenu, ier_constants,
		ier_util_menu, ier_model_BaseEntityObject) {

return dojo_declare("ier.widget.listView.modules.RowContextMenu", [ ecm_widget_listView_RowContextMenu ], {
	/**
	 * Obtain the right menu type when right clicking on a particular item
	 */
	getContextMenuItemsType: function(items) {
		if(items && items.length > 0) {
			var item = items[0];
			if(item && (item instanceof ecm_model_AsyncTask || item instanceof ecm_model_AsyncTaskInstance)){
				return ier_util_menu.getTaskContextMenuType(items);
			}
			else
				return ier_util_menu.getContainersAndRecordsContextMenuType(items);
		}
	},

	/**
	 * Override loading the context menu by grabbing it directly from desktop instead and providing our own getContextMenuItemsType method.  This is harder
	 * to do from ResultSet since we don't always create our own resultSet object
	 */
	loadContextMenu: function(selectedItems, callback) {
		//before loading the context menu for the item, if the item is an async task or async task instance, add TaskUser privileges if the user already has taskAdmin privileges
		for(var i in selectedItems){
			var item = selectedItems[i];
			if(item && (item instanceof ecm_model_AsyncTask || item instanceof ecm_model_AsyncTaskInstance)){
				if(item.hasPrivilege(ier_constants.Privilege_TaskAdmin)){
					item[ier_constants.Privilege_TaskUser] = true;
				}
			}
		}
		
		if (callback) {
			ecm_model_Desktop.loadMenuActions(this.getContextMenuItemsType(selectedItems), callback);
		}
		
		
	}

});});
