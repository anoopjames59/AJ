define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ecm/widget/UnselectableFolder",
    	"ecm/widget/FolderSelectorCallback",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/_FolderSelectorDropDown",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/FileRecord.html",
    	"dijit/layout/ContentPane", // in content
    	"ecm/widget/HoverHelp" // in content
], function(dojo_declare, dojo_lang, dojo_class, ecm_model_desktop, ecm_model_Request, ecm_widget_UnselectableFolder, ecm_widget_FolderSelectorCallback,
		ier_constants, ier_messages, ier_util, ier_widget_FolderSelectorDropDown, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.FileRecordDialog
 * @augments ecm.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.FileRecordDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.FileRecordDialog.prototype */
	contentString: contentString,
	widgetsInTemplate: true,
	_record: null,
	_fp_repository_location: null, // {String}
	_messages: ier_messages,
	_items: null,
	
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
		this.okButton = this.addButton(ier_messages.fileRecordDialog_fileButton, "_onClickFile", false, true);
	},

	/**
	 * Shows the file record dialog.
	 * 
	 * @param repository - repository where record is located.
	 * @param items - The record that will be filed - only single record can be specified
	 */
	show: function(repository, items) {
		this.logEntry("show()");
		this.inherited("show", []);

		this.repository = repository;
		this._items = items;
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
		this.setTitle(ier_messages.fileRecordDialog_title);		
		this.setIntroText(ier_messages.fileRecordDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh27.htm"));
		
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
		var folderSelectorCallback = new ecm_widget_FolderSelectorCallback("privCanFileRecordToContainer", 
				ier_messages.fileRecordDialog_notAllowedToFileToContainer);
		this._folderSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);

		// Can't have the same source and target, so make the source folder unselectable
		var sourceContainer = this._items[0].parent.id;
		var unselectableFolders = [];
		var unselectableItem = new ecm_widget_UnselectableFolder(sourceContainer, true, ier_messages.fileRecordDialog_RecordAlreadyFiledHere);
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
			if (this._fp_repository_location == null)
				return false;
			else
				return true;
		},

	/**
	 * Perform the file action.  Gather up the parameters and call the service
	 * to perform the file.
	 */
	_onClickFile: function() {	
		this.logEntry("onClickFile()");		
		if (this.validate()) {	
			// we only support filing a single container so there should 
			// only be one item in the items array
			var items = this._items;		
			var item = items[0];
			
			var serviceParams = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
				// record is now in destination, so refresh dest
				this.repository.retrieveItem(this._fp_repository_location, dojo_lang.hitch(this, function(itemRetrieved){
					if (itemRetrieved)
						itemRetrieved.refresh();
					this.onCancel();
				}));				
			}));
			serviceParams.requestParams[ier_constants.Param_RecordId] = item.id;
			serviceParams.requestParams[ier_constants.Param_DestinationContainer] = this._fp_repository_location;
			
			// Call the file service
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_FileRecord, ier_constants.PostEncoding, serviceParams);
		}
		this.logExit("onClickFile()");
	} 	
});});
