define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/Messages",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ecm/widget/UnselectableFolder",
    	"ecm/widget/FolderSelectorCallback",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/_FolderSelectorDropDown",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/MoveRecordDialog.html",
    	"dijit/layout/ContentPane", // in content
    	"ecm/widget/HoverHelp", // in content
    	"ecm/widget/ValidationTextBox" // in content
], function(dojo_declare, dojo_lang, dojo_class, ecm_messages, ecm_model_desktop, ecm_model_Request, ecm_widget_UnselectableFolder, ecm_widget_FolderSelectorCallback,
		ier_constants, ier_messages, ier_util, ier_widget_FolderSelectorDropDown, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.MoveRecordDialog
 * @augments ecm.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.MoveRecordDialog", [ier_dialog_IERBaseDialog], {	
	/** @lends ier.widget.dialog.MoveRecordDialog.prototype */
	contentString: contentString,
	widgetsInTemplate: true,
	_record: null,
	_fp_repository_location: null, // {String}
	_messages: ier_messages,
	recordItem: null,
	
	/**
	 * Constructor function
	 */
	constructor: function() {
	},

	/**
	 * Actions to perform after the dialog is created.  
	 * Size dialog and setup OK button.
	 */
	postCreate: function() {
		this.inherited(arguments);
		dojo_class.add(this.domNode, "ierSmallDialog");
		this.okButton = this.addButton(ier_messages.moveRecordDialog_moveButton, "_onClickMove", false, true);
	},

	/**
	 * Shows the move record dialog.
	 * 
	 * @param repository - repository where record is located.
	 * @param items - The record that will be moved - only single record can be specified
	 */
	show: function(repository, items) {
		this.logEntry("show()");
		this.inherited("show", []);

		this.repository = repository;
		this.recordItem = items[0];
		if(this.repository.isIERLoaded()){
			this._renderDialog();
		}
		else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}

		this.logExit("show()");		
	},

	/**
	 * Render the dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");
		
		// Setup intro text, title and more link
		this.setTitle(ier_messages.moveRecordDialog_title);		
		this.setIntroText(ier_messages.moveRecordDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh13.htm"));
		this._reasonForMove.set("missingMessage", ecm_messages.property_missingMessage);
		//this._setReasonLength(this.repository);
		
		// Button is disabled until required fields are entered
		this.okButton.set("disabled", true);

		if (this._folderSelector) {
			this._folderSelector.destroy();
			this._folderSelector = null;
		}
		this._folderSelector = new ier_widget_FolderSelectorDropDown();
		this._folderSelector.selectRootInitially = false;
		this._folderSelector.preventSelectRoot = true;
		
		this.addChildWidget(this._folderSelectorCell);
		this._folderSelectorCell.appendChild(this._folderSelector.domNode);
		// Set permission to check for folder add.
		this._setFolderSelectorPermissions(this.repository, dojo_lang.hitch(this, function(){
			this._setFolderRoot(this.repository, ecm_model_desktop.getCurrentFilePlanId());
		}));
		
		this.connect(this._folderSelector, "onFolderSelected", function(folder) {
			this._fp_repository_location = folder.item.id;
			this.okButton.set("disabled", !(this.validate()));				
		});
		
		this.resize();
		this.logExit("_renderDialog");
	},

	/**
	 * Set the current file plan to be the root for the folder selection dialog
	 * @param repository - the repository we are using
	 * @param rootFolderId - the id of the current file plan
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
	 * Disable folders based on permissions
	 * @param repository
	 */
	_setFolderSelectorPermissions: function(repository, onComplete){
		this.logEntry("setFolderSelectorPermissions");
		
		// Set permission to check for folder add.
		var folderSelectorCallback = new ecm_widget_FolderSelectorCallback(ier_constants.Privilege_CanMoveRecordToContainer, ier_messages.moveRecordDialog_notAllowedToMoveToContainer);
		this._folderSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);

		// Can't have the same source and target, so make the source folder unselectable
		var sourceContainer = this.recordItem.parent.id;
		var unselectableFolders = [];
		var unselectableItem = new ecm_widget_UnselectableFolder(sourceContainer, true, ier_messages.moveRecordDialog_SourceCannotBeDestination);
		unselectableFolders.push(unselectableItem);
		this._folderSelector.setUnselectableFolders(unselectableFolders);

		if(onComplete)
			onComplete();
		this.logExit("setFolderSelectorPermission");
		},

		/**
		 * Validates the dialog
		 * @returns {Boolean}
		 */
		validate: function() {
			if (this._reasonForMove.get("value").length == 0 || this._fp_repository_location == null)
				return false;
			else
				return true;
		},

		/**
		 * Perform update when the reason has changed
		 */
		_onNameChange: function() {
			this.okButton.set("disabled", !(this.validate()));	
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
						this._reasonForMove.set("maxLength", attributeDefinition.maxLength);
						break;
					}
				}

			}));
		},

		
	/**
	 * Perform the move action.  Gather up the parameters and call the service
	 * to perform the move.
	 */
	_onClickMove: function() {	
		this.logEntry("onClickmove()");		
		if (this.validate()) {	
			this.recordItem.move(this._fp_repository_location, this._reasonForMove.get("value"), dojo_lang.hitch(this, function(){
				this.onCancel();
			}));
		}
		this.logExit("onClickmove()");
	} 	
});});
