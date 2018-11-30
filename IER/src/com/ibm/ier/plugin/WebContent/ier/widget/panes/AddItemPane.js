define([
    	"dojo/_base/declare",
    	"dojo/dom-construct",
    	"dojo/_base/lang",
    	"dojo/io-query",
    	"dojo/dom-style",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ecm/widget/FolderSelectorCallback",
    	"ecm/widget/UnselectableFolder",
    	"dojo/text!./templates/AddItemPane.html",
    	"ecm/widget/_FolderSelectorDropDown", //in template
    	"ecm/widget/ValidationTextBox", //in template
    	"ier/widget/search/SearchInDropDown", //in template
    	"idx/layout/TitlePane", //in template
    	"ier/widget/panes/EntityItemPropertiesPane", //in template
    	"ier/widget/panes/EntityItemSecurityPane" //in template
], function(dojo_declare, dojo_construct, dojo_lang, dojo_ioQuery, dojo_style, ier_constants, ier_messages, ier_widget_dialog_IERBaseDialogPane, 
		ecm_widget_FolderSelectorCallback, ecm_widget_UnselectableFolder, templateString){

/**
 * A pane that shows a file plan selector with properties and security for adding a new document
 * 
 * @name ier.widget.panes.AddItemPane"
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.AddItemPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.AddItemPane */

	templateString: templateString,
	constants: ier_constants,
	_messages: ier_messages,
	_saveInFolder: null,
	documentTitle: null,
	title: ier_messages.baseDialog_addItemTitle,
	showPermissionPane: true,
	defaultClass: ier_constants.ClassName_Document,

	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		if(!this.showPermissionPane){
			dojo_style.set(this.securityTitlePane.domNode, "display", "none");
		}
		
		// Set permission to check for folder add.
		var folderSelectorCallback = new ecm_widget_FolderSelectorCallback("privAddToFolder", ier_messages.noPermissionAdd);
		this.folderSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);
		
		this.connect(this.folderSelector, "onFolderSelected", function(folder){
			this._saveInFolder = folder;
			
			//SeearchInDropDown doesn't change the repository after a different one has been selected.  It only changes the underlying folder selector one
			if(folder && folder.item){
				this.folderSelector.repository = folder.item.repository;
			}
			
			if(this.folderSelector.repository != this.repository || !this._entityItemPropertiesPaneCreated){
				this.repository = this.folderSelector.repository;
				
				this._entityItemPropertiesPaneCreated = true;
				this._entityItemPropertiesPane.createRendering({
					repository: this.repository,
					rootClassId : ier_constants.ClassName_Document, 
					defaultNameProperty: ier_constants.Property_DocumentTitle,
					parentFolder: folder.item,
					defaultClass: this.defaultClass,
				});
			}
			this.onInputChange();
		});
		
		//Sets up and renders the security pane after the property pane has finished rendering
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {			
			this.contentClass = this._entityItemPropertiesPane.getContentClass();
			var properties = this._entityItemPropertiesPane.getProperties();
			this._entityItemPropertiesPane.setPropertyValue(ier_constants.Property_DocumentTitle, this.documentTitle);

			if(this.showPermissionPane){
				//renders the security pane
				this._entityItemSecurityPane.createRendering(this.repository, null, this._saveInFolder.item, this.contentClass, properties);
			}
			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.onInputChange();
			this.resize();
		});
		
		//Render the attributes.  Allow attributes to be changed before appearing
		this.connect(this._entityItemPropertiesPane, "onRenderAttributes", function(attributes, deferArray) {			
			this.onRenderAttributes(attributes, deferArray);
		});

		this.logExit("postCreate()");
	},
	
	/**
	 * Event invoked when report entry form attributes are about to be rendered
	 */
	onRenderAttributes: function(attributes, deferArray){
		
	},
	
	/**
	 * Creates the rendering for the pane
	 * @param repository
	 */
	createRendering: function(repository, defaultFolder) {
		this.logEntry("createRendering()");
		if(repository != this.repository){
			this.repository = repository;
			this.clearValues();
			
			this.folderSelector.setRoot(this.repository);
			
			//var unselectableFolders = [];
			
			//var p8repositoryId = repository.attributes ? repository.attributes.p8RepositoryId : null;
			//if(p8repositoryId) {
			//	var recordsManagmentFolderDocId = ier_constants.ClassName_RecordsManagementFolder + "," +  p8repositoryId + "," + ier_constants.Id_RecordsManagementFolder;
				//unselectableFolders.push(new ecm_widget_UnselectableFolder(recordsManagmentFolderDocId, false, ier_messages.declareRecordDialog_notAllowedToDeclareToFolder));
			//}
			//this.folderSelector.setUnselectableFolders(unselectableFolders);
			
			this.setDefaultFolder(defaultFolder);
			this.onInputChange();
		}
		
		this.logExit("createRendering()");
	},
	
	setDefaultFolder: function(folder){
		if(folder){
			this.repository.retrieveItem(folder, dojo_lang.hitch(this, function(itemRetrieved){
				this.folderSelector.setSelected(itemRetrieved);
				this.contentClass = this._entityItemPropertiesPane.getContentClass();
				this.onInputChange();
			}));
		}
	},
	
	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		return this._saveInFolder != null && this.contentClass != null && this._entityItemSecurityPane.validate() && this._entityItemPropertiesPane.validate();
	},
	
	clearValues: function() {
		this._saveInFolder = null;
		this.contentClass = null;
	},
	
	getSavedInFolder: function() {
		return this._saveInFolder;
	},
	
	getRootDownloadLinkURL: function() {
		var queryParams = {
				desktop: ecm.model.desktop.id,
				repositoryId: this._saveInFolder.item.repository.id
			};
		
		var rootPath = ecm.model.desktop._cServicesUrl || "/navigator";
		var linkUrl = rootPath + "/bookmark.jsp?" + dojo_ioQuery.objectToQuery(queryParams);

		return (window.location.protocol + "//" + window.location.host + linkUrl);
	},
	
	setDocumentTitle: function(value){
		this.documentTitle = value;
		this._entityItemPropertiesPane.setPropertyValue(ier_constants.Property_DocumentTitle, value);
	},
	
	getDocumentTitle: function() {
		return this._entityItemPropertiesPane.getPropertyValue(ier_constants.Property_DocumentTitle);
	},
	
	getProperties: function(){
		return this._entityItemPropertiesPane.getProperties();
	},
	
	getClassName: function() {
		var contentClass = this._entityItemPropertiesPane.getContentClass();
		if(contentClass)
			return contentClass.id;
		else
			return null;
	},
	
	getPermissions: function() {
		return this._entityItemSecurityPane.getPermissions();
	}
});});