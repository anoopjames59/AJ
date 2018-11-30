define([ 
      "dojo/_base/declare",
      "dojo/DeferredList",
  	  "dojo/_base/Deferred",
  	  "dojo/_base/lang",
      "ecm/model/Desktop",
      "ier/widget/listView/gridModules/RowContextMenu",
      "ier/util/menu",
      "ier/util/util"
], function(dojo_declare, dojo_DeferredList, dojo_Deferred, dojo_lang, ecm_model_Desktop, ier_widget_listView_RowContextMenu, ier_util_menu, ier_util) {

return dojo_declare("ier.widget.listView.modules.RowContextMenu", [ ier_widget_listView_RowContextMenu ], {
	
	/**
	 * Override and do nothing.  ObjectLisDialog is already connected to onRowDblClick and will handle the selection. 
	 */
	performDefaultActionForItem: function(selectedItem) {
		//do nothing
	},
	
	getMenu: function(selectedItems, callback) {
		var defArray = [];
		var itemsList = {};
		var savedArguments = arguments;
		if(selectedItems){
			for(var i in selectedItems){
				var item = selectedItems[i];
				if(item && item.noPermissionsLoaded){
					var deferred = new dojo_Deferred();
					defArray.push(deferred);
					item.contextMenuDeferredObj = deferred;
					itemsList[ier_util.getGuidId(item.id)] = item;
					item.repository.retrieveItem(item.id, dojo_lang.hitch(this, function(itemRetrieved){
						//obtain the item from the list so it will not be a different item due to timing issues
						var savedItem = itemsList[ier_util.getGuidId(itemRetrieved.id)];
						savedItem.updatePropertiesAndAttributes(itemRetrieved, true);
						savedItem.contextMenuDeferredObj.resolve();
					}));
				}
			}
			
			if(defArray.length == 0){
				this.inherited(arguments);
			}
			else {
				var defs = new dojo_DeferredList(defArray);
				defs.then(dojo_lang.hitch(this, function(){
					this.inherited("getMenu", savedArguments);
				}));
			}
		}
	}
});});
