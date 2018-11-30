/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

define([ "dojo/_base/declare", //
   "dojo/data/util/sorter", //
   "ecm/model/ResultSet",
   "ier/util/menu"
], function(dojo_declare, dojo_data_util_Sorter, ecm_model_ResultSet, ier_util_menu) {

/**
 * @name ier.model.ResultSet
 * @class Represents a set of search results, folder contents, or other items that are returned by a query to the
 *        content server. A <code>ResultSet</code> object contains data about the items found by the query and
 *        about how these items are to be displayed.
 *        <p>
 *        To iterate through the items in a code>ResultSet</code> object efficiently, use the <code>getStore</code>
 *        method with a widget that supports Dojo datastores.
 *        </p>
 * @augments ecm.model.ResultSet
 */
return dojo_declare("ier.model.ResultSet", [ ecm_model_ResultSet ], {

	/**
	 * Returns the menu type to be used for the actions menu
	 */
	getActionsMenuItemsType: function(items) {
		return ier_util_menu.getContainersAndRecordsContextMenuType(items);
	},
	
	/**
	 * Get the menutype definition for the toolbar
	 */
	getToolbarDef: function() {
		if (this.toolbarDef) {
			return this.toolbarDef;
		} else {
			var toolbarDef = "ContentListToolbar";
			return toolbarDef;
		}
	},

	doSort: function(params, callback, store){
		if(this.sortFunc && this.continuationData){
			this.sortFunc(params);
			return;
		}
		if(!this.parentFolder && !this.searchTemplate){ // configuration
			if(params && store && this.items){
				this.items.sort(dojo_data_util_Sorter.createSortFunction(params, {
					comparatorMap: store.comparatorMap,
					getValue: function(item, attribute) {
						// _ModelStore.getValue() returns display value, instead of raw value suitable for sorting by date
						var type = item && item.getValue && item.getAttributeType && item.getAttributeType(attribute);
						if(type == "xs:timestamp"){
							return item.getValue(attribute);
						}else{
							return store.getValue(item, attribute);
						}
					}
				}));
				if(callback){
					callback(this);
				}
				return;
			}
		}
		this.inherited(arguments);
	}

});});
