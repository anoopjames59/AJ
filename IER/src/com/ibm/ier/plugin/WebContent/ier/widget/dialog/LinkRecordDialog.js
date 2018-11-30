define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ecm/widget/ContentClassSelector",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/panes/CommonPropertiesPane",
    	"ier/widget/DocumentSelector",
    	"ier/widget/_FolderSelectorDropDown",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/LinkRecordDialog.html",
    	"dijit/layout/ContentPane", // in content
    	"ecm/widget/HoverHelp", // in content
    	"ecm/widget/TextBox", // in content
    	"dijit/form/DropDownButton",
    	"ier/widget/panes/EntityItemPropertiesPane"
], function(dojo_declare, dojo_lang, dojo_class, ecm_model_desktop, ecm_model_Request, ecm_widget_ContentClassSelector, ier_constants, 
		ier_messages, ier_util, ier_widget_CommonPropertiesPane, ier_widget_DocumentSelector, ier_widget_FolderSelectorDropDown,ier_dialog_IERBaseDialog, contentString,
		dijit_form_DropDownButton){

/**
 * @name ier.widget.dialog.linkRecordDialog
 * @augments ecm.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.linkRecordDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.linkRecordDialog.prototype */
	contentString: contentString,
	widgetsInTemplate: true,
	_record: null,
	_fp_repository_location: null, // {String}
	_fp_link_location: null, // {String}
	_messages: ier_messages,
	_items: null,
	baseClass: "RMLink",
	
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
		this.addChildPane(this._propertiesPane);
		this.okButton = this.addButton(ier_messages.linkRecordDialog_linkButton, "_onClickLink", false, true);
	},

	/**
	 * Shows the link record dialog.
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
			this._renderDialog(repository,"Relation");
		}
		else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog(repository,"Relation");  // "Relation"
			}));
		}

		// For the input fields, need to set the max length based on the record class of the record being moved
/*		this._setPropertyLengths(this.repository);
		var props = items[0];
		this._documentTitle.set("value", ier_messages.linkRecordDialog_documentTitleIntro + props.name);*/
		
		this.logExit("show()");		
	},

	/**
	 * Render the dialog
	 */
	_renderDialog: function(repository, rootClassId){
		this.logEntry("_renderDialog");
		
		// Setup intro text, title and more link
		this.setTitle(ier_messages.linkRecordDialog_title);		
		this.setIntroText(ier_messages.linkRecordDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh28.htm"));
		var linkNode = this.createHrefLinkNode(learnMoreLink, ier_messages.dialog_LearnMoreLink);
		this.introText.appendChild(linkNode);
		
		// Button is disabled until required fields are entered
		//this.okButton.set("disabled", true);
		this.okButton.set("enabled", true);
		
/*		this._contentClassSelector.rootClassId = rootClassId;
		this._contentClassSelector.excludedItems = null;
		this._contentClassSelector.setRepository(repository);
		this._contentClassSelector.selectRootInitially = false;
		this._contentClassSelector.preventSelectRoot = false;*/
		
		if (this._folderSelector) {
			this._folderSelector.destroy();
			this._folderSelector = null;
		}
			
		var baseConstraints = {
				labelId : this.id + "_dispositonScheduleLabel",
				label: ier_messages.entityItemDispositionPane_dispositionInstructions + ":",
				selectButtonLabel: "Select",
				showCreateButton: false
		};
		this._folderSelector = new ier_widget_DocumentSelector(baseConstraints);
		this._folderSelector.setRepository(repository);
		this._folderSelector.setObjectClassName(ier_constants.ClassName_Record);
		this.addChildWidget(this._folderSelectorCell);
		this._folderSelectorCell.appendChild(this._folderSelector.domNode);
		
		this.setResizable(true);
		this._propertiesPane.createRendering({
			repository : this.repository,
			parentFolder: this._parentFolder,
			rootClassId : ier_constants.ClassName_Link, // "Relation",
			defaultNameProperty: ier_constants.Property_LinkName, //"LinkName",
			entityType : ier_constants.EntityType_Link, //"400",
			hideContentClassSelector: false,
			//isCreate : true,
			changeAttributesIndicator: false,
			item: this.item
	});
		

/*		this.connect(this._contentClassSelector, "onChange", function() {			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});*/
		
		//Sets up and renders the security pane after the property pane has finished rendering
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});
		
		this.connect(this._folderSelector, "onFolderSelected", function(folder) {
			this._fp_repository_location = folder.item.id;
			this.okButton.set("disabled", !(this.validate()));				
		});
		
		this.resize();
		this.logExit("_renderDialog");
	},
		/**
		 * Validates the dialog
		 * @returns {Boolean}
		 */
		validate: function() {
			this.logEntry("validate");
			return true;
			this.logEntry("validate");
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
	 * Perform the link action.  Gather up the parameters and call the service
	 * to perform the link.
	 */
	_onClickLink: function() {	
		this.logEntry("onClickLink()");		
		if (this.validate()) {	
			// we only support filing a single container so there should 
			// only be one item in the items array
			var items = this._items;		
			var item = items[0];
			var itemLink = this._propertiesPane._contentClassSelector.selectedContentClass.id;
			
			var serviceParams = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(itemRetrieved){
				//if (itemRetrieved)
				//	itemRetrieved.refresh();
				this.onCancel();
			}));				
			serviceParams.requestParams[ier_constants.Param_RecordId] = item.id;
			serviceParams.requestParams[ier_constants.Param_LinkedRecordId] = "ElectronicRecordInfo,{851410BE-CAB4-45CE-8D28-74831EBBE257},{36C8FC8A-D984-41E8-AF92-7D77EF2CBC2F}";//this._fp_repository_location;
			serviceParams.requestParams[ier_constants.Param_LinkClass] = itemLink;
			serviceParams.requestParams[ier_constants.Param_Name] = this._propertiesPane.getPropertyValue(ier_constants.Property_LinkName);
			serviceParams.requestParams[ier_constants.Param_Description] = this._propertiesPane.getPropertyValue(ier_constants.Property_RMEntityDescription);
			serviceParams.requestParams[ier_constants.Param_LinkReason] = this._propertiesPane.getPropertyValue(ier_constants.Property_ReasonForExtract);			
			
			// Call the Link service
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_LinkRecord, ier_constants.PostEncoding, serviceParams);
		}
		this.logExit("onClickLink()");
	} 	
});});
