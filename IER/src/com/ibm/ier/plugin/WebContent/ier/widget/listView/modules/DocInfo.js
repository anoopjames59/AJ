define([
    	"dojo/_base/declare",
    	"ecm/widget/listView/modules/DocInfo",
    	"ier/widget/panes/ItemPropertiesDisplayPane"
], function(dojo_declare, ecm_widget_listView_modules_DocInfo, ier_widget_ItemPropertiesDisplayPane){
	
return dojo_declare("ier.widget.listView.modules.DocInfo", [ ecm_widget_listView_modules_DocInfo ], {
	
	/**
	 * Override this to create a different properties display pane
	 */
	createItemPropertiesDisplayPane: function() {
		return new ier_widget_ItemPropertiesDisplayPane({
			showSystemProps: this.showSystemProps,
			showPreview: this.showPreview,
			allowEdit: true
		});
	}
});});
