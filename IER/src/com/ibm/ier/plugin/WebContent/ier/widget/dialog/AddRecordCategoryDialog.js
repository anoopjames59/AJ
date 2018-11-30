define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ecm/model/Desktop",
    	"ier/constants",
    	"ier/messages",
    	"ier/model/RecordCategoryContainerMixin",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/AddRecordCategoryDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"idx/layout/TitlePane", // in content
    	"ier/widget/panes/EntityItemDispositionPane", // in content
    	"ier/widget/panes/EntityItemGeneralPane", // in content
    	"ier/widget/panes/EntityItemPropertiesPane", // in content
    	"ier/widget/panes/EntityItemSecurityPane" // in content
], function(dojo_declare, dojo_lang, ecm_model_desktop, ier_constants, ier_messages, ier_model_RecordCategoryContainerMixin, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.AddRecordCategoryDialog
 * @class Provides an interface to add record categories
 * @augments ecm.widget.dialog.BaseDialog
 */
return dojo_declare("ier.widget.dialog.AddRecordCategoryDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.AddRecordCategoryDialog.prototype */

	contentString: contentString,
	_messages: ier_messages,
	_parentFolder: null,

	postCreate: function() {
		this.inherited(arguments);
		this.addChildPane(this._entityItemGeneralPane);
		this.addChildPane(this._entityItemPropertiesPane);
		this.addChildPane(this._entityItemDispositionPane);
		if(ecm_model_desktop.showSecurity){
			this.addChildPane(this._entityItemSecurityPane);
		}else{
			this._entityItemSecurityPane.getParent().domNode.style.display = "none";
		}
		this.addButton(ier_messages.baseDialog_addButton, "_onClickAdd", true, true);
	},
		
	/**
	 * Shows the AddRecordCategoryDialog
	 * @param repository
	 * @param parentFolder - folder that the current document resides in
	 */
	show: function(repository, parentFolder, item) {
		this.inherited("show", []);
		
		this.logEntry("show()");
		this._parentFolder = parentFolder;
		this.item = item;
		
		this.repository = repository;
		if(this.repository.isIERLoaded()){
			this._renderDialog();
		}
		else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}
		
		this.resize();
		
		this.logExit("show()");
	},
	
	/**
	 * Render the dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");
		
		if(this.item){
			this.set("title", ier_messages.addRecordCategoryDialog_editTitle);
			this.getDefaultButton().set("label", this._ierMessages.baseDialog_updateButton);
		} else {
			this.set("title", ier_messages.addRecordCategoryDialog_title);
			this.getDefaultButton().set("label", this._ierMessages.baseDialog_addButton);
		}
		
		this.setIntroText(ier_messages.addRecordCategoryDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh04.htm"));
		
		this.setResizable(true);
		
		//renders the other panes
		this._entityItemGeneralPane.createRendering(this.repository, this._parentFolder);
		this._entityItemPropertiesPane.createRendering({
			repository: this.repository,
			parentFolder: this._parentFolder,
			rootClassId : ier_constants.ClassName_RecordCategory, 
			defaultNameProperty: ier_constants.Property_RecordCategoryName, 
			entityType: ier_constants.EntityType_RecordCategory,
			hideContentClassSelector: true,
			item: this.item
		});
		this._entityItemPropertiesPane.enableAndSetNamingPattern(ier_constants.Property_RecordCategoryName, ier_constants.Property_RecordCategoryIdentifier);
		this._entityItemDispositionPane.createRendering({
			repository: this.repository,
			parentFolder: this._parentFolder, 
			entityItemPropertiesPane: this._entityItemPropertiesPane
		});

		//Connects and change existing attributes for the property pane before they are rendered
		this.connect(this._entityItemPropertiesPane, "onRenderAttributes", function(attributes, deferArray) {
			this._entityItemPropertiesPane.setReviewerAndCurrentDate(attributes, deferArray);
		});
		
		//Sets up and renders the security pane after the property pane has finished rendering
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {			
			var contentClass = this._entityItemPropertiesPane.getContentClass();
			var properties = this._entityItemPropertiesPane.getProperties();
			
			//renders the security pane
			this._entityItemSecurityPane.createRendering(this.repository, null, this._parentFolder, contentClass, properties);
			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});

		
		this.logExit("_renderDialog");
	},

	_onClickAdd: function() {
		this.logEntry("_onClickAdd");
		
		if (this.validateInput()) {
			var properties = this._entityItemPropertiesPane.getProperties();
			var permissions = this._entityItemSecurityPane.getPermissions();
			var className = this._entityItemPropertiesPane.getContentClass().id;
			var dispSchedule = this._entityItemDispositionPane.getDispositionSchedule();
			var legacyScheduleParams = this._entityItemDispositionPane.getLegacyDispositionScheduleProperties();
			
			if(this._parentFolder.isInstanceOf(ier_model_RecordCategoryContainerMixin))
				this._parentFolder.addRecordCategory(className, properties, permissions, dispSchedule, legacyScheduleParams, dojo_lang.hitch(this, function(recordcategory){
					
					if (this._parentFolder) {
						this._parentFolder.refresh();
					}
					
					this.onCancel();
				})
			);
		}
		
		this.logExit("_onClickAdd");
	}
});});
