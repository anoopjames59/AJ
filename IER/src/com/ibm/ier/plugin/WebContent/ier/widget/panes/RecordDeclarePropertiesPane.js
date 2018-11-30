define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dojo/dom-attr",
    	"dojo/dom-style",
    	"ecm/model/Desktop",
    	"ecm/model/Repository",
    	"ecm/model/Request",
    	"ecm/widget/ContentClassSelector",
    	"ecm/widget/FolderSelectorCallback",
    	"ecm/widget/UnselectableFolder",
    	"ecm/widget/dialog/MessageDialog",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/dialog",
    	"ier/util/util",
    	"ier/widget/_FolderSelectorDropDown",
    	"ier/widget/FilePlanRepositorySelector",
    	"ier/widget/admin/config",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/RecordDeclarePropertiesPane.html",
    	"dijit/form/TextBox", // in template
    	"ecm/widget/HoverHelp", // in template
    	"ier/widget/panes/CommonPropertiesPane" // in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_attr, dojo_style,
		ecm_model_desktop, ecm_model_Repository, ecm_model_Request, ecm_widget_ContentClassSelector, ecm_widget_FolderSelectorCallback, ecm_widget_UnselectableFolder, ecm_widget_dialog_MessageDialog,
		ier_constants, ier_messages, ier_util_dialog, ier_util, ier_widget_FolderSelectorDropDown, ier_widget_FilePlanRepositorySelector, ier_widget_admin_config,
		ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * Properties pane for record declare dialog
 */
return dojo_declare("ier.widget.panes.RecordDeclarePropertiesPane", [ier_widget_dialog_IERBaseDialogPane], {

	templateString: templateString,
	_recordClass: null, // {ecm.model.ContentClass}
	_recordClassLabel: null, // {String}
	_fp_repository_location: null, // {String}
	_selectedDocumentTitle: null, // {String}
	_filePlanRepositorySelector: null, // {ier.widget._FolderSelectorDropDown}
	defaultDesktopId: ecm_model_desktop.getRequestParam("DeclareDesktop") ? ecm_model_desktop.getRequestParam("declareDesktop") : ier_constants.IERDesktop,
	_defaultFilePlanId: ier_constants.Id_FilePlanFolder,
	_filePlanRepository: null, //{ecm.model.Repository}
	_shouldMapProperties: true, //{Boolean}
	_commonAttributes: null,
	_blankRepository : new ecm_model_Repository("blankRepository", "&nbsp;", "p8", false),
	_itemProperties : null,
	_originalDocument : null,
	_isEmailDocument : false,
   _nameFromNamingPattern : null,
   _namingPatternEnabled: true,
   _isFromEntryTemplate: false,
   shouldMapParentFolderProperties: true,

	postCreate: function() {
		this.logEntry("postCreate");
		
		this.inherited(arguments);

		//detect property changes and do validation
		this.connect(this._commonProperties, "onChange", function(){
			this.onInputChange(this._commonProperties);
		});

		var learnMoreLink = this.getHelpUrl("frmovh12.htm");


		this._repositorySelectorHoverHelp.message = ier_messages.declareRecordDialog_repositoriesHoverHelp;
		this._folderSelectorHoverHelp.message = ier_messages.declareRecordDialog_fileplanLocationHoverHelp;
		this._contentClassSelectorHoverHelp.message = ier_messages.declareRecordDialog_recordClassHoverHelp + 
		"  " + this.createHtmlLink(learnMoreLink, ier_messages.dialog_LearnMoreLink);
		
		this.logExit("postCreate");
	},
	
	/**
	 * Enable or disable setting name based on naming patterns
	 * @param enable
	 */
	setEnabledNamingPattern: function(enable){
		this._namingPatternEnabled = enable;
	},

	/**
	 * Sets the content class for the content class selector.  This should be performed after the content class selector has loaded.
	 * @param classSymbolicName
	 */
	setRecordClass: function(classSymbolicName){
		this.logEntry("setRecordClass");
		
		//get the contentclass and do a comparison and see if the name and id are the same.  If they are, do a real fetch to have the right names.
		var contentClass = this._contentClassSelector.repository.getContentClass(classSymbolicName);
		if(contentClass.id == contentClass.name){
			this._contentClassSelector.repository.retrieveContentClassList(dojo_lang.hitch(this, function(contentClassList){
				contentClass = contentClassList[0];
				this._setRecordClass(contentClass);
			}), [classSymbolicName]);
		}
		else {
			this._setRecordClass(contentClass);
		}
		
		this.logExit("setRecordClass");
	},
	
	_setRecordClass: function(contentClass){
		//if the property is not available immediately on the tree, setSelected will fail 
		//so just set the label for the selector and pass in the content class directly.
		if(this._contentClassSelector.isItemAvailable(contentClass))
			this._contentClassSelector.setSelected(contentClass);
		else {
			if(contentClass.id == contentClass.name && this._recordClassLabel)
				contentClass.name = this._recordClassLabel;

			if(contentClass){
				this._contentClassSelector.setLabel(contentClass.name);
				this._contentClassSelector.onContentClassSelected(contentClass);
			}
		}
	},

	/**
	 * Returns the symbolic name of the selected record class
	 * @returns
	 */
	getRecordClassSymbolicName: function() {
		return this._recordClass.id;
	},

	/**
	 * Provides a label for the current record class
	 */
	setRecordClassLabel: function(label) {
		this._recordClassLabel = label;
	},

	/**
	 * Returns the set of properties of the class
	 * @returns
	 */
	getClassProperties: function() {
		var docProperties = [];

		docProperties = this._commonProperties.getPropertiesJSON(true);

		// getDocumentProperties() does not return read only or hidden properties.
		// Include the default property values for hidden properties or entry template read only properties.
		// The entry template may modify writeable attributes to be read only to the user (in the UI), but values should still
		// be assigned for those attributes based on the class and entry template settings.
		var attrDef;
		var defaultValue;
		for ( var attrDefNdx in this._attributes) {
			attrDef = this._attributes[attrDefNdx];
			defaultValue = attrDef.defaultValue;
			// TBD: ET returns an empty default value if no default value,
			if (attrDef.hidden && defaultValue && (defaultValue.length > 0)) {
				docProperties.push({
					"name": attrDef.id,
					"value": defaultValue
				});
			}
		}

		return docProperties;
	},

	/**
	 * Sets the target location for the file plan repository selector
	 * @param repository
	 * @param folderId
	 */
	setTargetLocation: function(repository, folderId) {
		repository.retrieveItem(folderId, dojo_lang.hitch(this, function(itemRetrieved){
			this._folderSelector.setSelected(itemRetrieved);
		}));
	},

	/**
	 * Sets a property value for the set of properties in the selected record class
	 * @param property
	 * @param value
	 * @returns
	 */
	setPropertyValue: function(property, value) {
		return this._commonProperties.setPropertyValue(property, value);
	},


	/**
	 * Returns the path to file plan repository location selected
	 * @returns
	 */
	getFPRepositoryLocation: function() {
		return this._fp_repository_location;
	},

	/**
	 *  Returns the selected file plan repository
	 * @returns
	 */	
	getFilePlanRepository: function(){
		return this._filePlanRepository;
	},

	/**
	 * Sets the current file plan repository
	 * @param repository
	 */
	setFilePlanRepository: function(repository){
		this._filePlanRepository = repository;
	},

	/**
	 * Returns an instance of the folder selector widget
	 * @returns {ier.widget._FolderSelectorDropDown}
	 */
	getFolderSelectorWidget: function() {
		return this._folderSelector;
	},

	/**
	 * Returns an instance of the record class selector
	 * @returns {ecm.widget.ContentClassSelector}
	 */
	getRecordClassSelectorWidget: function() {
		return this._contentClassSelector;
	},

	/**
	 * Returns the table row of the folder selector widget
	 * @returns
	 */
	getFolderSelectTableRow: function() {
		return this._folderSelectorRow;
	},

	/**
	 * Returns the div surrounding the common properties widget
	 * @returns
	 */
	getCommonPropertiesDiv: function() {
		return this._commonPropertiesDiv;
	},

	/**
	 * Returns the repository selector row
	 */
	getRepositorySelectorRow: function() {
		return this._repositorySelectorRow;
	},

	/**
	 * Returns the table row of the record class selector widget
	 * @returns
	 */
	getRecordClassSelectorTableRow: function() {
		return this._contentClassSelectorRow;
	},

	/**
	 * Set whether to automatically map property values when it makes sense.  This includes document title and email class
	 */
	setAutoMapPropertyValues: function(autoMap){
		this._shouldMapProperties = autoMap;
	},

	/**
	 * Event invoked when the set of attributes is about to be rendered
	 */
	onRenderAttributes: function(attributes){

	},

	/**
	 * Event called when a properties rendering is completed
	 */
	onCompleteRendering: function() {

	},
	
	setIsFromEntryTemplate: function(value) {
		this._isFromEntryTemplate = value;
	},
	
	/**
	 * Returns whether the original document is a physical document without content
	 */
	isPhysicalDocument: function(){
		var documentContent = this._originalDocument.attributes[ier_constants.Property_ContentElementPresent];
		if(documentContent && documentContent.length == 0)
			return true;
		
		if(documentContent && documentContent.length > 0 && documentContent[0] == ier_constants.Mimetype_ExternalDocument)
			return true;
		
		return false;
	},

	/**
	 * Resets properties pane to the original state when first initially invoked
	 */
	resetPropertiesPane: function() {
		this.logEntry("resetPropertiesPane");
		
		//enable the dummy selectors and labels
		dojo_style.set(this._contentClassSelectorDisabled.domNode, "display", "");
		dojo_style.set(this._contentClassSelectorDisabledLabel, "display", "");
		dojo_style.set(this._fileplanLocationDisabledLabel, "display", "");
		dojo_style.set(this._folderSelectorDisabled.domNode, "display", "");

		//hide the real selectors
		if(this._contentClassSelector){
			dojo_style.set(this._contentClassSelector.domNode, "display", "none");
			dojo_style.set(this._contentClassLabel, "display", "none");
			this._contentClassSelector.setDisabled(false);
		}
		if(this._folderSelector){
			dojo_style.set(this._folderSelector.domNode, "display", "none");
			dojo_style.set(this._fileplanLocationLabel, "display", "none");
			this._folderSelector.setDisabled(false);
		}
		dojo_style.set(this._commonPropertiesDiv, "display", "none");
		this._filePlanRepositorySelector.setSelected("0");
		this.resetValues();
		
		this.logExit("resetPropertiesPane");
	},
	
	/**
	 * Reset values
	 */
	resetValues: function(){
		this._fp_repository_location = null;
		this._namingPatternEnabled = true;
		this._contentClass = null;
		this._recordClassLabel = null;
		this._filePlanRepository = null;
		this._shouldMapProperties = true;
		this._commonAttributes = null;
		this._nameFromNamingPattern = null;
		this._isFromEntryTemplate = false;
	},

	/**
	 * Returns the target repository from the list of available file plan repositories
	 * @param p8RepositoryId
	 * @returns
	 */
	getTargetFilePlanRepository: function(p8RepositoryId){
		this.logEntry("getTargetFilePlanRepository");
		
		var repositories = this.getFilePlanRepositories();
		for ( var i in repositories) {
			var repository = repositories[i];
			var currentRepo_p8RepositoryId = repository.objectStoreName;
			if(currentRepo_p8RepositoryId == p8RepositoryId)
				return repository;
		}
		
		this.logExit("getTargetFilePlanRepository");
	},

	/**
	 * Renders the pane
	 * @param repository
	 * @param items
	 */
	createRendering: function(repository, items) {
		this.logEntry("createRendering");
		
		this.repository = repository;
		if(items.length == 1) {
			this._originalDocument = items[0];
			this._itemProperties = this._originalDocument.attributes;
			this._isEmailDocument = this._originalDocument.getContentClass().id == ier_constants.ClassName_Email;
		}
		
		this.originalDocumentTitle = this._originalDocument.name;

		this._filePlanRepositorySelector = new ier_widget_FilePlanRepositorySelector({
		});
		 
		this.addChildWidget(this._filePlanRepositorySelector);

		this._repositorySelector.appendChild(this._filePlanRepositorySelector.domNode);
		this._commonProperties.setRepository(repository);

		//connect onto the repository selector onSelect event.  Set the class and file plan selectors accordingly
		this.connect(this._filePlanRepositorySelector, "onSelect", function(repositories) {
			var repository = repositories[0];
			if(repository.id == this._blankRepository.id){
				this.resetPropertiesPane();
			}
			else {
				//reset the values so unintended data won't be left behind.
				this.resetValues();
				this._filePlanRepository = repository;
				if(repository.isIERLoaded()){
					//renders the folder and class selectors
					this.renderFolderAndClassSelectors(repository);
				} else {
					repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
						this.renderFolderAndClassSelectors(repository);
					}), this.defaultDesktopId);
				}
			}

			this.onInputChange(this._filePlanRepositorySelector);
		});

		this._filePlanRepositorySelector.setFilePlanRepositories(this.getFilePlanRepositories());
		
		//TODO: this was a workaround on problems with nexus widgets and accessibility.  Re-evaluate to see if it's needed since they fixed the defect
		//already
		if(this._filePlanRepositorySelector._dropdown && this._filePlanRepositorySelector._dropdown.focusNode){
			 dojo_attr.set(this._filePlanRepositorySelector._dropdown.focusNode, {
	             "aria-required": true,
	             "aria-invalid": false,
	             "aria-label": ier_messages.declareRecordDialog_repositories,
	 			 "aria-labelledby": this.id + "_repositoriesLabel"
			 });
		}
		
		this.logExit("createRendering");
	},

	/**
	 * Event that gets invoked when the file plan location changes based on user selection
	 * @param filePlanContainerId
	 */
	onFilePlanLocationChanged: function(filePlanContainerId){

	},

	/**
	 * Initial load of the selectors
	 */
	_enableSelectors: function(){
		//disable dummy selectors
		dojo_style.set(this._contentClassSelectorDisabled.domNode, "display", "none");
		dojo_style.set(this._contentClassSelectorDisabledLabel, "display", "none");
		dojo_style.set(this._fileplanLocationDisabledLabel, "display", "none");
		dojo_style.set(this._folderSelectorDisabled.domNode, "display", "none");
		
		//enable the real selectors
		if(this._contentClassSelector){
			dojo_style.set(this._contentClassSelector.domNode, "display", "");
			dojo_style.set(this._contentClassLabel, "display", "");
		}
		if(this._folderSelector){
			dojo_style.set(this._folderSelector.domNode, "display", "");
			dojo_style.set(this._fileplanLocationLabel, "display", "");
		}
		
		if(this._commonPropertiesDiv)
			dojo_style.set(this._commonPropertiesDiv, "display", "");
	},

	/**
	 * Renders the folder and class selectors with the given parameters
	 * @param repository
	 */
	renderFolderAndClassSelectors: function(repository, rootFolderId, recordClassSymbolicName, onFolderSelectorCompleted){
		this.logEntry("renderFolderAndClassSelectors");
		this.loaded = false;
		
		//show a message dialog indicating this is wrong and return
		if(!repository.isFilePlanRepository()){
			ier_util_dialog.showMessage(ier_messages.no_fileplans_available);
			return;
		}
		
		this._recordClassSymbolicName = recordClassSymbolicName;

		this._enableSelectors();

		//folder selector
		if(this._folderSelector == null)
		{
			this._folderSelector = new ier_widget_FolderSelectorDropDown({
				preventSelectRoot : false
			});

			this.addChildWidget(this._folderSelector);
			this._folderSelectorCell.appendChild(this._folderSelector.domNode);
			this._fileplanLocationLabel.setAttribute("for", this._folderSelector.id);
			dojo_style.set(this._fileplanLocationDisabledLabel, "display", "none");
			dojo_style.set(this._fileplanLocationLabel, "display", "");

			this._setFolderSelectorPermissions(repository, dojo_lang.hitch(this, function(){
				this._setFolderRoot(repository, rootFolderId, onFolderSelectorCompleted);
				
				//TODO: this was a workaround on problems with nexus widgets and accessibility.  Re-evaluate to see if it's needed since they fixed the defect
				//already
				if(this._folderSelector._dropdown && this._folderSelector._dropdown.focusNode){
					 dojo_attr.set(this._folderSelector._dropdown.focusNode, {
			             "aria-required": true,
			             "aria-invalid": false,
			             "aria-label": ier_messages.declareRecordDialog_fileplanLocation,
			 			 "aria-labelledby": this.id + "_fileplanLocationLabel"
					 });
				}	
			}));

			this.connect(this._folderSelector, "onFolderSelected", function(folder) {
				var folder_id = folder.item.id;
				var folder_p8Id = ier_util.getGuidId(folder_id);
				if(this._fp_location != folder_id) {
					this._fp_repository_location = folder_id;
					
					//only do anything if naming pattern is enabled
					if(this._namingPatternEnabled) {
							
						//do not make the request on these folders since these folders are usually defaults and record naming patterns can't really exist on these folders
						if(folder_p8Id != ier_constants.Id_RecordsManagementFolder && folder_p8Id != ier_constants.Id_FilePlanFolder
							&& folder_p8Id != ier_constants.Id_RootFolder) {
							
							//obtain the naming pattern if it exists.  This is done on every change folder selection.  This will return whether there is a naming pattern.
							this._getNamingPatternName(this._filePlanRepository, dojo_lang.hitch(this, function(name){
								//set the name based on naming pattern. Only need to call render attributes when the user is picking the file plan locations manually.  If not, it will cause renderAttributes to be called twice.
								if(this.loaded){
									//if a valid name is returned, use it and re-render the attributes with it.  This is needed since we disable the document title due to it.  
									if(name)
										this._renderAttributes(this._commonAttributes);
									else //else only re-reneder the attributes to re-enable the document title if the document title was set successfully from the naming pattern.  This boolean is set in
										//renderAttributes() when the naming pattern is applied
										if(this.documentTitleSetFromNamingProperties)
											this._renderAttributes(this._commonAttributes);
								}
							}));
						}
					}
					
					this.onFilePlanLocationChanged(this._fp_repository_location);
					this.onInputChange(this._folderSelector);
				}
			});
		}
		//this is invoked only if folder selector changes repository.  Redo all the folder permissions since they are cached in the tree.
		else if(this._folderSelector.repository != repository){
			this._setFolderSelectorPermissions(repository, dojo_lang.hitch(this, function(){
				this._setFolderRoot(repository, rootFolderId, onFolderSelectorCompleted);
				
				//TODO: this was a workaround on problems with nexus widgets and accessibility.  Re-evaluate to see if it's needed since they fixed the defect
				//already
				if(this._folderSelector._dropdown && this._folderSelector._dropdown.focusNode){
					 dojo_attr.set(this._folderSelector._dropdown.focusNode, {
			             "aria-required": true,
			             "aria-invalid": false,
			             "aria-label": ier_messages.declareRecordDialog_fileplanLocation,
			 			 "aria-labelledby": this.id + "_fileplanLocationLabel"
					 });
				}	
			}));
		}
		else {
			this._setFolderRoot(repository, rootFolderId, onFolderSelectorCompleted);
		}

		//creating the content class selector for the first time
		//the content class selector also has to be recreated if the repository changes since it doesn't unload all of the tree model objects
		if(this._contentClassSelector == null || this._contentClassSelector.repository != repository){
			this._isRecordClassLoaded = false;
			this._createContentClassSelector(repository, recordClassSymbolicName != null);
		}
		else {
			//setting the content class based on the name provided.
			//if the root class changed, set the new root
			if(this._contentClassSelector.getRootClassId() != ier_constants.ClassName_Record){
				this._isRecordClassLoaded = false;
				this._contentClassSelector.setRootClassId(ier_constants.ClassName_Record);
			}
			else
				//if a class is provided, set it
				if(this._recordClassSymbolicName)
					this.setRecordClass(this._recordClassSymbolicName);
				else
					//if not set a default class accordingly
					this._setDefaultRecordClass();
			}
		
		this.logExit("renderFoldersAndClassSelectors");
	},
	
	/**
	 * Sets the default record class based on the original document.  If it's an email document, set it to the email record class.  if the document has no content, set it to physical.
	 */
	_setDefaultRecordClass: function(){
		if(this._isEmailDocument)
			this.setRecordClass(ier_constants.ClassName_EmailRecord);
		else
			this.setRecordClass(this.isPhysicalDocument() ? ier_constants.ClassName_PhysicalRecord : ier_constants.ClassName_ElectronicRecord);
	},

	_setFolderRoot: function(repository, rootFolderId, onComplete){
		if(rootFolderId){
			repository.retrieveItem(rootFolderId, dojo_lang.hitch(this, function(itemRetrieved){
				this._folderSelector.setRoot(itemRetrieved);
				setTimeout(dojo_lang.hitch(this, function() {
					if(onComplete){
						onComplete();
					}
				}), 100);
				
			}));
		}
		else {
			this._folderSelector.setRoot(repository);
			setTimeout(dojo_lang.hitch(this, function() {
				if(onComplete){
					onComplete();
				}
			}), 100);
		}
	},

	/**
	 * Disable folders based on permissions
	 * @param repository
	 */
	_setFolderSelectorPermissions: function(repository, onComplete){
		this.logEntry("setFolderSelectorPermissions");
		
		// Set permission to check for folder add.
		var folderSelectorCallback = new ecm_widget_FolderSelectorCallback();
		// Set the permission in the callback function needed to add a folder on each parent.
		folderSelectorCallback.isSelectableByPermission.permissionToCheck = ier_constants.Privilege_CanDeclareRecordToContainer;
		folderSelectorCallback.isSelectableByPermission.notSelectableTooltip = ier_messages.declareRecordDialog_notAllowedToDeclareToFolder;
		this._folderSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission);

		//disallow the user from selecting file plans
		var repositoryType = repository.getRecordType();
		if(repositoryType && (repositoryType == ier_constants.RepositoryType_Combined || repositoryType == ier_constants.RepositoryType_FilePlan)){
			repository.getFilePlans(dojo_lang.hitch(this, function(fileplans){
				var unselectableFolders = [];
				
				var p8repositoryId = repository.attributes ? repository.attributes.p8RepositoryId : null;
				if(p8repositoryId) {
					var recordsManagmentFolderDocId = ClassName_RecordsManagementFolder + "," +  p8repositoryId + "," + ier_constants.Id_RecordsManagementFolder;
					unselectableFolders.push(new ecm_widget_UnselectableFolder(recordsManagmentFolderDocId, true, ier_messages.declareRecordDialog_notAllowedToDeclareToFolder));
				}
				for(var i in fileplans){
					var fileplan = fileplans[i];
					unselectableFolders.push(new ecm_widget_UnselectableFolder(fileplan.id, true, ier_messages.declareRecordDialog_notAllowedToDeclareToFilePlans));
				}
				this._folderSelector.setUnselectableFolders(unselectableFolders);

				if(onComplete)
					onComplete();
			}));
		}
		
		this.logExit("setFolderSelectorPermission");
	},

	/**
	 * Create the content class selector
	 * @param repository
	 */
	_createContentClassSelector: function (repository, isFromEntryTemplate){
		this.logEntry("createContentClassSelector");
		
		if(this._contentClassSelector){
			this._contentClassSelectorCell.removeChild(this._contentClassSelector.domNode);
			this._contentClassSelector.destroyRecursive();
		}

		if(this._onContentClassSelectedHandle)
			this.disconnect(this._onContentClassSelectedHandle);

		if(this._onContentClassLoadedHandle)
			this.disconnect(this._onContentClassLoadedHandle);

		//determine the root id.  It is Record usually and EmailRecord if an email document is involved
		var rootClassId = this._isEmailDocument ? ier_constants.ClassName_EmailRecord : ier_constants.ClassName_Record;

		//create the content class selector
		this._contentClassSelector = new ecm_widget_ContentClassSelector({
			repository: repository, 
			selectorStyle: "dropdown", 
			rootClassId: rootClassId, 
			hasAll: false, 
			filterType: "", 
			filterTemplateName: null, 
			"class": "dijitInline", 
			onlySelectable:true
		});
		
		//TODO: this was a workaround on problems with nexus widgets and accessibility.  Re-evaluate to see if it's needed since they fixed the defect
		//already
		if (this._contentClassSelector._dropdown && this._contentClassSelector._dropdown.focusNode) {			
			 dojo_attr.set(this._contentClassSelector._dropdown.focusNode, {
	             "aria-required": true,
	             "aria-invalid": false,
	             "aria-label": ier_messages.declareRecordDialog_recordClass,
	 			 "aria-labelledby": this.id + "_contentClassLabel"
			 });
		}
		 
		this._contentClassLabel.setAttribute("for", this._contentClassSelector.id);
		dojo_style.set(this._contentClassSelectorDisabledLabel, "display", "none");
		dojo_style.set(this._contentClassLabel, "display", "");

		this._onContentClassSelectedHandle = this.connect(this._contentClassSelector, "onContentClassSelected", function(recordClass) {
			//only apply the content class if record class is valid and class selector has successfully loaded.
			if(recordClass && this._isRecordClassLoaded)
			{
				this._recordClass = recordClass;
				this._renderProperties(recordClass, repository);
				this.onInputChange(this._contentClassSelector);
			}
		});

		this._onContentClassLoadedHandle = this.connect(this._contentClassSelector, "onLoaded", function() {
			//must set the content class only after it has finished loaded
			this._isRecordClassLoaded = true;
			
			//if a content class has already been provided, set it.  This is done via record entry template
			if(this._recordClassSymbolicName)
				this.setRecordClass(this._recordClassSymbolicName);
			else 
				//if no class has been set, set a default one
				this._setDefaultRecordClass();
		});

		this.addChildWidget(this._contentClassSelector);
		this._contentClassSelectorCell.appendChild(this._contentClassSelector.domNode);
		
		this.logExit("createContentClassSelector");
	},

	/**
	 * Obtain all the file plan repositories attached to the provided desktop Id and are in the same p8 domain as the current Content Repository
	 * @returns
	 */
	getFilePlanRepositories: function(){
		this.logEntry("getFilePlanRepositories");
		if(this._filePlanRepositories == null){
			this._filePlanRepositories = [];
			this._filePlanRepositories.push(this._blankRepository);
			var fp_repos = ecm_model_desktop.repositories;
			for(var i in fp_repos){
				var fp_repo = fp_repos[i];
				
				//if the file plan repository is in the same domain as the content repository
				if(fp_repo.serverName == this.repository.serverName) {
					
					//if the repository is a file plan repository then display it
					if(ier_widget_admin_config.isSavedFilePlanRepository(fp_repo.id))
						this._filePlanRepositories.push(fp_repo);
				}
			}
		}
		this.logExit("getFilePlanRepositories");
		return this._filePlanRepositories;
	},

	_getNamingPatternName: function(repository, onComplete){
		this.logEntry("getNamingPatternName");
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
			this._nameFromNamingPattern = response.nameFromNamingPattern;

			if(onComplete)
				onComplete(this._nameFromNamingPattern);
		}));
		params.requestParams[ier_constants.Param_EntityId] = this._fp_repository_location;
		params.requestParams[ier_constants.Param_EntityType] = ier_constants.EntityType_Record;
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetEntityNameFromNamingPattern, ier_constants.PostEncoding, params);
		this.logExit("getNamingPatternName");
	},

	/**
	 * Renders the common properties
	 * @param contentClass
	 * @param repository
	 */
	_renderProperties: function(contentClass, repository) {
		this.logEntry("renderProperties");
		this._commonProperties.clearRendering();

		contentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(attributes) {
			var newAttributes = this.cloneAttributes(attributes);
			this._commonAttributes = newAttributes;

			this.onRenderAttributes(newAttributes);
			
			this._renderAttributes(newAttributes);

			this.onInputChange(this._commonAttributes);
			this.onCompleteRendering();
			this.loaded = true;
		}));
		this.logExit("renderProperties");
	},
	
	/**
	 * Renders the attributes
	 */
	_renderAttributes: function(attributes) {
		this.logEntry("renderAttributes");
		
		if(attributes){
			var documentTitleAttribute = this._getAttributeDefinition(ier_constants.Property_DocumentTitle, attributes);
			
			//set the name based on naming pattern if it exists
			if(this._nameFromNamingPattern){
				if(documentTitleAttribute){
					documentTitleAttribute.readOnly = true;
					documentTitleAttribute.defaultValue = this._nameFromNamingPattern;
					this.documentTitleSetFromNamingProperties = true;
				}
			} else {
				//reset the values
				if(this.documentTitleSetFromNamingProperties && documentTitleAttribute && this.loaded){
					this.documentTitleSetFromNamingProperties = false;
					documentTitleAttribute.readOnly = false;
					documentTitleAttribute.defaultValue = "";
				}
			}
	
			if(this._shouldMapProperties){
				//special email record handling.  List values must be set before they are created
				if(this._recordClass.id == ier_constants.ClassName_EmailRecord && this._originalDocument.getContentClass().id == ier_constants.ClassName_Email){
					var toAttributeDef = this._getAttributeDefinition(ier_constants.Property_To, attributes);
					toAttributeDef.defaultValue = ier_util.arrayToString(this._itemProperties[ier_constants.Property_To]);
					
					var ccAttributeDef = this._getAttributeDefinition(ier_constants.Property_CC, attributes);
					ccAttributeDef.defaultValue = ier_util.arrayToString(this._itemProperties[ier_constants.Property_CC]);
					
					var sentOnAttributeDef = this._getAttributeDefinition(ier_constants.Property_SentOn, attributes);
					sentOnAttributeDef.defaultValue = this._itemProperties[ier_constants.Property_SentOn];
					
					var receivedOnAttributeDef = this._getAttributeDefinition(ier_constants.Property_ReceivedOn, attributes);
					receivedOnAttributeDef.defaultValue = this._itemProperties[ier_constants.Property_ReceivedOn];
				}
			}
	
			this._attributes = attributes;
			
			//common properties pane renders the attributes.
			this._commonProperties.renderAttributes(attributes);

	
			if(this._shouldMapProperties)
				this._mapClassProperties();
		}
			
		this.logExit("renderProperties");
	},

	/**
	 * Performs any mapping of properties
	 */
	_mapClassProperties: function(){
		this.logEntry("mapClassProperties");
		
		var documentTitleValue = this._commonProperties.getPropertyValue(ier_constants.Property_DocumentTitle);
		if(documentTitleValue == null || documentTitleValue.length == 0) {
			this.setDocumentTitle(this.originalDocumentTitle);
		}
		//email record
		var documentClass = this._originalDocument.getContentClass().id;
		if(this._recordClass.id == ier_constants.ClassName_EmailRecord && documentClass == ier_constants.ClassName_Email){
			this.setPropertyValue(ier_constants.Property_From, this._itemProperties[ier_constants.Property_From]);
			this.setPropertyValue(ier_constants.Property_Subject, this._itemProperties[ier_constants.Property_Subject]);
		}
		this.logExit("mapClassProperties");
	},
	
	/**
	 * Sets the document title of this record
	 * @param name
	 */
	setDocumentTitle: function(name){
		if(name)
			this.setPropertyValue(ier_constants.Property_DocumentTitle, name);
	},

	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		if(this._recordClass == null || this._filePlanRepository == null || this._filePlanRepository.id == this._blankRepository.id || this._fp_repository_location == null)
			return false;

		var errorField = this._commonProperties.validate();
		return(errorField == null);
	},

	/**
	 * Resizes the common properties pane
	 */
	resizeCommonProperties: function() {
		this._commonProperties.resize();
	},

	/**
	 * Destroys the widget
	 */
	destroy: function(){
		dojo_array.forEach(this._commonAttributes, function(widget){
			if(widget)
				widget.destroyRecursive();
		});
		
		if(this._contentClassSelector)
			this._contentClassSelector.destroyRecursive();
		
		if(this._filePlanRepositorySelector)
			this._filePlanRepositorySelector.destroyRecursive();
		
		if(this._folderSelector)
			this._folderSelector.destroyRecursive();
		
		this.inherited(arguments);
	},

	_getAttributeDefinition: function(id, attributeDefs) {
		for ( var i in attributeDefs) {
			var attrDef = attributeDefs[i];
			if(attrDef.id == id)
				return attrDef;
		}
	},
	
	/**
	 * Return an array of cloned attributes.  Handles setting the default value for properties dialog
	 * @param attributeDefs
	 * @returns {Array}
	 */
	cloneAttributes: function(attributeDefs){
		var copyAttributes = [];
		var itemAttributes = this._originalDocument.attributes;
		var attributesToAvoidMapping = [];
		
		var continueMapping = true;
		for ( var i in attributeDefs) {
			var attrDef = attributeDefs[i];
			var clonedAttributeDef = attrDef.clone();
			copyAttributes.push(clonedAttributeDef);
			continueMapping = true;
			
			//map the properties accordingly by looping through and setting the default values against a clone
			if(itemAttributes != null){
				
				//skip mapping some properties during creation such as name and identifier
				if(this.shouldMapParentFolderProperties){
					//do not map attributes that already has a value or default value
					//do not map hidden or system properties
					if(clonedAttributeDef.system == true || clonedAttributeDef.hidden == true || 
							(clonedAttributeDef.defaultValue && clonedAttributeDef.defaultValue != "") || (clonedAttributeDef.value && clonedAttributeDef.value != "")){
						continueMapping = false;
					} else {
						for(var j in attributesToAvoidMapping){
							if(clonedAttributeDef.id == attributesToAvoidMapping[j]){
								continueMapping = false;
								break;
							}
						}
					}
				}
				
				if(continueMapping && itemAttributes[clonedAttributeDef.id])
					clonedAttributeDef.defaultValue = itemAttributes[clonedAttributeDef.id];
			}
		}
		return copyAttributes;
	},

	_nop: null
});});