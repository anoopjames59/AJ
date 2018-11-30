define([
    	"dojo/_base/declare",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/EntityItemGeneralPane.html",
    	"ecm/widget/_FolderSelectorDropDown" // in template
], function(dojo_declare, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * The general properties pane for most entity items creation dialog.  This pane consists of a single FolderSelector widget that is preset to the parent's folder.  It 
 * is usually disabled.
 * 
 * @name ier.widget.EntityItemGeneralPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.EntityItemGeneralPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.EntityItemGeneralPane */

	templateString: templateString,
	_parentFolder: null,
	_saveInFolder: null,

	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		this.logExit("postCreate()");
	},

	/**
	 * Creates the rendering for the pane
	 * @param repository
	 */
	createRendering: function(repository, parentFolder) {
		this.logEntry("createRendering()");
		
		this.repository = repository;
		this._parentFolder = parentFolder;
		
		//Set and disable the folder selectors.  This is used to let the user see where they are creating the new folder in
		if(parentFolder){
			this._folderSelectorDropDown.setRoot(parentFolder);
			this._folderSelectorDropDown.setDisabled(true);
			//this._folderSelectorDropDown.setSelected(parentFolder);
		}
			
		this.connect(this._folderSelectorDropDown, "onFolderSelected", function(folder) {
			this._saveInFolder = folder;
			this._folderSelectorDropDown.setDisabled(true);
		});
		
		this.logExit("createRendering()");
	},
	
	isValidationRequired: function() {
		return false;
	},
	
	getSavedInFolder: function() {
		return this._saveInFolder;
	}
});});
