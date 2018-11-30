define([
	"dojo/_base/declare",
	"ecm/widget/FolderSelector",
	"ier/widget/FolderTree",
	"dojo/text!./templates/FolderSelector.html",
	"dijit/form/CheckBox" // in template
], function(dojo_declare, ecm_widget_FolderSelector, ier_widget_FolderTree, templateString){

return dojo_declare("ier.widget.FolderSelector", [ ecm_widget_FolderSelector ], {
	// summary:
	//      Input-like widget used for selecting a search folder.
	
	templateString: templateString,
	
	/**
	 * Creates the folder tree
	 */
	createFolderTree: function(params) {
		//new way going forward without using private variables
		if(params){
			return new ier_widget_FolderTree(params);
		}
		//provide for backwards compatibility for ICN R2 for the time being until everyone upgrades to latest R3
		else{
			this._folderTree = new ier_widget_FolderTree({
				'dojoAttachPoint': '_folderTree',
				'showFoldersOnly': 'true',
				'enableCtxMenu': 'false',
				'enableDnd': 'false',
				'class': 'folderTree'
			});
		}
	}
});});
