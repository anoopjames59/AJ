define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/_base/Deferred",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/messages",
    	"ier/model/RecordVolumeContainerMixin",
    	"ier/widget/dialog/IERBaseDialog",
    	"ier/util/util",
    	"dojo/text!./templates/AddRecordVolumeDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"idx/layout/TitlePane", // in content
    	"ier/widget/panes/EntityItemGeneralPane", // in content
    	"ier/widget/panes/EntityItemPropertiesPane", // in template
    	"ier/widget/panes/EntityItemSecurityPane" // in template
], function(dojo_declare, dojo_lang, dojo_deferred, ecm_model_desktop, ecm_model_Request, ier_constants, ier_messages, ier_model_RecordVolumeContainerMixin, 
		ier_dialog_IERBaseDialog, ier_util, contentString){

/**
 * @name ier.widget.dialog.AddRecordVolumeDialog
 * @class Provides an interface to add record categories
 * @augments ecm.widget.dialog.BaseDialog
 */
return dojo_declare("ier.widget.dialog.AddRecordVolumeDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.AddRecordVolumeDialog.prototype */

	contentString: contentString,

	parentFolder: null,

	postCreate: function() {
		this.inherited(arguments);
		this.addChildPane(this._entityItemGeneralPane);
		this.addChildPane(this._entityItemPropertiesPane);
		if(ecm_model_desktop.showSecurity){
			this.addChildPane(this._entityItemSecurityPane);
		}else{
			this._entityItemSecurityPane.getParent().domNode.style.display = "none";
		}
		this.addButton(ier_messages.baseDialog_addButton, "_onClickAdd", true, true);
	},
		
	/**
	 * Shows the AddRecordVolumeDialog
	 * @param repository
	 * @param parentFolder - folder that the current document resides in
	 */
	show: function(repository, parentFolder, item) {
		this.inherited("show", []);
		
		this.logEntry("show()");
		this.parentFolder = parentFolder;
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
			this.set("title", ier_messages.addRecordVolumeDialog_editTitle);
			this.getDefaultButton().set("label", this._ierMessages.baseDialog_updateButton);
		} else {
			this.set("title", ier_messages.addRecordVolumeDialog_title);
			this.getDefaultButton().set("label", this._ierMessages.baseDialog_addButton);
		}
		
		this.setIntroText(ier_messages.addRecordVolumeDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh07.htm"));
		
		this.setResizable(true);
		
		//renders the other panes
		this._entityItemGeneralPane.createRendering(this.repository, this.parentFolder);
		this._entityItemPropertiesPane.createRendering({
			repository: this.repository,
			parentFolder: this.parentFolder,
			rootClassId : ier_constants.ClassName_Volume, 
			defaultNameProperty: ier_constants.Property_VolumeName, 
			entityType: ier_constants.EntityType_RecordVolume,
			hideContentClassSelector: true,
			item: this.item
		});
		this._entityItemPropertiesPane.enableAndSetNamingPattern(ier_constants.Property_VolumeName, null);

		//Connects and change existing attributes for the property pane before they are rendered
		this.connect(this._entityItemPropertiesPane, "onRenderAttributes", function(attributes, deferArray) {
			this._entityItemPropertiesPane.setReviewerAndCurrentDate(attributes, deferArray);
			
			//automatically prefill the next volume's name
			var deferred = new dojo_deferred();
			deferArray.push(deferred);
			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
				if(response.name){
					this.volumeName = response.name;
					var volumeAttr = this._entityItemPropertiesPane.getAttributeDefinition(ier_constants.Property_VolumeName, attributes);
					if(volumeAttr){
						volumeAttr.defaultValue = this.volumeName;
					}
				}
				deferred.resolve();
			}));
			params.requestParams[ier_constants.Param_Id] = this.parentFolder.id;
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetNextVolumeName, ier_constants.PostEncoding, params);
			
			this.logExit("getNamingPatternName");
			this._entityItemPropertiesPane.setReviewerAndCurrentDate(attributes, deferArray);
		});
		
		//Sets up and renders the security pane after the property pane has finished rendering
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {			
			var contentClass = this._entityItemPropertiesPane.getContentClass();
			var properties = this._entityItemPropertiesPane.getProperties();
			
			//renders the security pane
			this._entityItemSecurityPane.createRendering(this.repository, null, this.parentFolder, contentClass, properties);
			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});

		
		this.logExit("_renderDialog");
	},

	_addEntityItem: function(className, criterias, permissions) {
		this.logEntry("_addEntityItem");
		
		if(this.parentFolder.isInstanceOf(ier_model_RecordVolumeContainerMixin))
			this.parentFolder.addRecordVolume(className, criterias, permissions, dojo_lang.hitch(this, function(recordVolume){
				
				if (this.parentFolder) {
					this.parentFolder.refresh();
				}
				
				this.onCancel();
			}));
		
		this.logExit("_addEntityItem");
	},

	_onClickAdd: function() {
		this.logEntry("_onClickAdd");
		
		if (this.validateInput()) {
			var properties = this._entityItemPropertiesPane.getProperties();
			var permissions = this._entityItemSecurityPane.getPermissions();
			var className = this._entityItemPropertiesPane.getContentClass().id;

			this._addEntityItem(className, properties, permissions);
		}
		
		this.logExit("_onClickAdd");
	}
});});
