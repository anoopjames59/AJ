define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ecm/widget/FolderSelectorCallback",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/_FolderSelectorDropDown",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/CopyRecordDialog.html",
    	"dijit/layout/ContentPane", // in content
    	"ecm/widget/HoverHelp", // in content
    	"ecm/widget/TextBox", // in content
    	"ecm/widget/ValidationSimpleTextarea" //in content
], function(dojo_declare, dojo_lang, dojo_class, ecm_model_desktop, ecm_model_Request, ecm_widget_FolderSelectorCallback,
		ier_constants, ier_messages, ier_util, ier_widget_FolderSelectorDropDown, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.CopyRecordDialog
 * @augments ecm.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.CopyRecordDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.CopyRecordDialog.prototype */
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
		this.okButton = this.addButton(ier_messages.copyRecordDialog_copyButton, "_onClickCopy", false, true);
		this._description.set("value", "");
	},

	/**
	 * Shows the copy record dialog.
	 * 
	 * @param repository - repository where record is located.
	 * @param items - The record that will be copied - only single record can be specified
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

		// For the input fields, need to set the max length based on the record class of the record being moved
		this._setPropertyLengths(this.repository);
		var props = items[0];
		this._documentTitle.set("value", ier_messages.copyRecordDialog_documentTitleIntro + props.name);
		
		this.logExit("show()");		
	},

	/**
	 * Render the dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");
		
		// Setup intro text, title and more link
		this.setTitle(ier_messages.copyRecordDialog_title);		
		
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
			this._fp_repository_location = folder.item;
			this.okButton.set("disabled", !(this.validate()));				
		});
		
		this.resize();
		this.logExit("_renderDialog");
	},

	/**
	 * Sets the root item in the folder selector tree
	 * @param repository - FPOS being used
	 * @param rootFolderId - the file plan node which will be the root
	 */
	_setFolderRoot: function(repository, rootFolderId){
		this.logEntry("_setFolderRoot");
		if(rootFolderId){
			repository.retrieveItem(rootFolderId, dojo_lang.hitch(this, function(itemRetrieved){
				this._folderSelector.setRoot(itemRetrieved);
			}));
		}
		else
			this._folderSelector.setRoot(repository);	
		this.logExit("_setFolderRoot");
		},
	

	/**
	 * Disable folders based on permissions
	 * @param repository
	 */
	_setFolderSelectorPermissions: function(repository, onComplete){
		this.logEntry("setFolderSelectorPermissions");
		
		// Set permission to check for folder add.
		var folderSelectorCallback = new ecm_widget_FolderSelectorCallback(ier_constants.Privilege_CanDeclareRecordToContainer, 
				ier_messages.copyRecordDialog_notAllowedToCopyToContainer);
		this._folderSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);

		if(onComplete)
			onComplete();
		this.logExit("setFolderSelectorPermission");
		},

		/**
		 * Validates the dialog
		 * @returns {Boolean}
		 */
		validate: function() {
			this.logEntry("validate");
			var result = false;
			if (this._fp_repository_location != null)
				result = true;
			this.logEntry("validate");
			return result;
		},

		/**
		 * Procedure to setup the DocumentTitle and description fields.  Need
		 * to get the length of the fields defined in the CE and the localized names
		 * @param repository - the repository being used
		 */
		_setPropertyLengths: function(repository){
			this.logEntry("_setPropertyLengths");
			var contentClass = repository.getContentClass(this._items[0].getClassName());
			contentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(attributeDefinitions){
				for ( var i in attributeDefinitions) {
					var attributeDefinition = attributeDefinitions[i];
					if (attributeDefinition.id == ier_constants.Property_RMEntityDescription) {
						this._description.set("maxLength", attributeDefinition.maxLength);
					}
					if (attributeDefinition.id == ier_constants.Property_DocumentTitle) {
						this._documentTitle.set("maxLength", attributeDefinition.maxLength);
					}
				}
			}));
			this.logExit("_setPropertyLengths");
		},
		
	/**
	 * Perform the copy action.  Gather up the parameters and call the service
	 * to perform the copy.
	 */
	_onClickCopy: function() {	
		this.logEntry("onClickCopy()");		
		if (this.validate()) {	
			// we only support filing a single container so there should 
			// only be one item in the items array
			var items = this._items;		
			var item = items[0];
			
			var serviceParams = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(itemRetrieved){
				if (this._fp_repository_location)
					this._fp_repository_location.refresh();
				this.onCancel();
			}));				
			serviceParams.requestParams[ier_constants.Param_RecordId] = item.id;
			serviceParams.requestParams[ier_constants.Param_DestinationContainer] = this._fp_repository_location.id;
			serviceParams.requestParams[ier_constants.Param_Name] = this._documentTitle.get("value");
			serviceParams.requestParams[ier_constants.Param_Description] = this._description.get("value");
			
			// Call the copy service
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CopyRecord, ier_constants.PostEncoding, serviceParams);
		}
		this.logExit("onClickCopy()");
	} 	
});});
