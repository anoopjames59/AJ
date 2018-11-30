define([
	"dojo/_base/declare",
	"ecm/widget/_FolderSelectorDropDown",
	"ecm/widget/_SinglePropertyEditorMixin",
	"ier/widget/FolderSelector",
	"ier/util/util"
], function(dojo_declare, ecm_widget_FolderSelectorDropDown, ecm_widget_SinglePropertyEditorMixin, ier_widget_FolderSelector, ier_util){

/**
 * @name FolderSelectorControlSheet
 * @class
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget._FolderSelectorDropDown", [ecm_widget_FolderSelectorDropDown, ecm_widget_SinglePropertyEditorMixin], {
	/** @lends FolderSelectorControlSheet */
	
	createFolderSelector: function(params) {
		//new way going forward without using private variables
		if(params){
			return new ier_widget_FolderSelector(params);
		}
		//provide for backwards compatibility for ICN R2 for the time being until everyone upgrades to latest R3
		else {
			//Create the FolderSelector widget
			this._folderSelector = new ier_widget_FolderSelector({
				'id': this.id + '_FolderSelector',
				'class': 'folderSelectorDropDownContent',
				'selectRootInitially': this.selectRootInitially,
				'preventSelectRoot': this.preventSelectRoot,
				'preventSelectRootTooltip': this.preventSelectRootTooltip,
				'unselectableFolders': this.unselectableFolders,
				'showIncludeSubfolders': this.showIncludeSubFolders
			});
		}
	},
	
	getValue: function(){
		var folder = this.getSelected();
		if(folder) {
			var folder_id = this.getSelected().item.id;
			return ier_util.getGuidId(folder_id);
		}
	},
	
	/**
	 * Override the get function to return the right value and labels
	 * @param name
	 * @returns
	 */
	get:function(name){
		if(name == "value")
			return this.getValue();
		else if(name == "displayedValue"){
			var selected = this.getSelected();
			if(selected){
				return selected.item.name;
			}
		}
		
		return this.inherited(arguments);
	}
});});