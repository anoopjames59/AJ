define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ecm/Messages",
    	"ecm/model/Desktop",
    	"ecm/widget/UnselectableFolder",
    	"ecm/widget/FolderSelectorCallback",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/_FolderSelectorDropDown",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/RelocateContainerGeneralPane.html",
    	"dijit/form/ValidationTextBox", // in template
    	"ecm/widget/HoverHelp" // in template
], function(dojo_declare, dojo_lang, ecm_messages, ecm_model_desktop, ecm_widget_UnselectableFolder, ecm_widget_FolderSelectorCallback,
		ier_constants, ier_messages, ier_widget_FolderSelectorDropDown, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * @name ier.widget.panes.RelocateContainerGeneralPane
 * @class
 * @augments dijit.Dialog
 */
return dojo_declare("ier.widget.panes.RelocateContainerGeneralPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.RelocateContainerGeneralPane */
	
	templateString: templateString,
	widgetsInTemplate: true,
	_container: null,
	_fp_repository_location: null, // {String}

	constructor: function() {
	},

	/**
	 * called after the dialog is created
	 */
	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		this.logExit("postCreate()");
	},
	
	/**
	 * Create the rendering of the dialog.  Set all of the text fields
	 * Create the drop down dialog for selecting the destination folder
	 * @param repository - the repository we are using
	 * @param relocateDialog - the parent dialog
	 * @param items - the item that we are relocating
	 */
	createRendering: function(repository, relocateDialog, items) {
		this.logEntry("createRendering()");
		this._items = items;
		this._relocateDialog = relocateDialog;
		this._reasonForRelocate.set("missingMessage", ecm_messages.property_missingMessage);
		this._setReasonLength(repository);
		
		if (this._folderSelector) {
			this._folderSelector.destroy();
			this._folderSelector = null;
		}
		this._folderSelector = new ier_widget_FolderSelectorDropDown();
		this._folderSelector.selectRootInitially = false;
		this._folderSelector.preventSelectRoot = false;
		
		this.addChildWidget(this._folderSelectorCell);
		this._folderSelectorCell.appendChild(this._folderSelector.domNode);
		// Set permission to check for folder add.
		this._setFolderSelectorPermissions(repository, dojo_lang.hitch(this, function(){
			this._setFolderRoot(repository, ecm_model_desktop.getCurrentFilePlanId());
		}));
		
		this.connect(this._folderSelector, "onFolderSelected", function(folder) {
			this._fp_repository_location = folder.item.id;
			this.onInputChange(this._folderSelector);
		});
		
		
		this.logExit("createRendering()");
	},
	
	/**
	 * Set the root folder for the folder selection dialog.  This would
	 * be the file plan
	 * @param repository
	 * @param rootFolderId
	 */
	_setFolderRoot: function(repository, rootFolderId){
		if(rootFolderId){
			repository.retrieveItem(rootFolderId, dojo_lang.hitch(this, function(itemRetrieved){
				this._folderSelector.setRoot(itemRetrieved);
			}));
		}
		else
			this._folderSelector.setRoot(repository);	
	},
	

	/**
	 * Update when the reason has changed
	 */
	_onNameChange: function() {
		this.onInputChange(this._reasonForRelocate);
	},
	
	/** 
	 * Calculate the length of the reason field.  The admin can
	 * change the length of this field
	 * @param repository - the repository where the record resides
	 */
	_setReasonLength: function(repository){
		var contentClass = repository.getContentClass(ier_constants.ClassName_RecordCategory);
		contentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(attributeDefinitions){
			for ( var i in attributeDefinitions) {
				var attributeDefinition = attributeDefinitions[i];
				if (attributeDefinition.id == ier_constants.Property_ReasonForReclassification) {
					this._reasonForRelocate.set("maxLength", attributeDefinition.maxLength);
					break;
				}
			}

		}));
	},
	
	/**
	 *  Returns the reason for relocate
	 * @returns the reason that the user specified
	 */	
	getReasonForRelocate: function(){
		return this._reasonForRelocate.get("value");
	},
	
	/**
	 * Returns the target container that the user specified
	 */
	getTargetContainer: function(){
		return this._fp_repository_location;
	},
	
	/**
	 * Disable folders based on permissions
	 * @param repository
	 */
	_setFolderSelectorPermissions: function(repository, onComplete){
		this.logEntry("setFolderSelectorPermissions");
		
		// Set permission to check for folder add.
		var permission = null;
		// Set the permission in the callback function needed to relocate a container.
		if (this._items[i].getEntityType() == ier_constants.EntityType_RecordCategory)
			permission = ier_constants.Privilege_AddRecordCategory;
		else
			permission = ier_constants.Privilege_AddRecordFolder;
		
		var folderSelectorCallback = new ecm_widget_FolderSelectorCallback(permission, ier_messages.relocateRecordContainerDialog_notAllowedToRecloateToFolde);
		this._folderSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);
		
		// Can't have the same source and target, so make the source folder unselectable
		var sourceContainer = this._items[0].parent.id;
		var unselectableFolders = [];
		var unselectableItem = new ecm_widget_UnselectableFolder(sourceContainer, true, ier_messages.relocateRecordContainerDialog_SourceAndTargetContainerMustBeDifferent);
		unselectableFolders.push(unselectableItem);
		var unselectableItem = new ecm_widget_UnselectableFolder(this._items[0].id, true, ier_messages.relocateRecordContainerDialog_ChildOfSourceCannotBeSelected);
		unselectableItem.allowSelectChildren = false;
		unselectableFolders.push(unselectableItem);
		this._folderSelector.setUnselectableFolders(unselectableFolders);

		if(onComplete)
			onComplete();
		this.logExit("setFolderSelectorPermission");
		},
	
	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		if (this._reasonForRelocate.get("value").length == 0 || this._fp_repository_location == null)
			return false;
		else
			return true;
	}
});});