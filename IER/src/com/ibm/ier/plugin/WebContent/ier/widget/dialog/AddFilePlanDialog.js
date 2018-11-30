define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/model/ResultSet",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/AddFilePlanDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"idx/layout/TitlePane", // in content
    	"ier/widget/panes/FilePlanRepositoryPane", // in content
    	"ier/widget/panes/EntityItemPropertiesPane", // in content
    	"ier/widget/panes/EntityItemSecurityPane" // in content
], function(dojo_declare, dojo_lang, ecm_model_desktop, ecm_model_Request, ier_constants, ier_messages, ier_util, ier_model_ResultSet, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.AddFilePlanDialog
 * @class Provides an interface to add a file plan
 * @augments ecm.widget.dialog.BaseDialog
 */
return dojo_declare("ier.widget.dialog.AddFilePlanDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.AddFilePlanDialog.prototype */

	contentString: contentString,
	_messages: ier_messages,

	postCreate: function() {
		this.inherited(arguments);
		this.addChildPane(this._filePlanRepositoryPane);
		this.addChildPane(this._entityItemPropertiesPane);
		if(ecm_model_desktop.showSecurity){
			this.addChildPane(this._entityItemSecurityPane);
		}else{
			this._entityItemSecurityPane.getParent().domNode.style.display = "none";
		}
		this.addButton(ier_messages.baseDialog_addButton, "_onClickAdd", true, true);
	},

	/**
	 * Shows the AddFilePlanDialog
	 * @param repository
	 */
	show: function(repository) {
		this.inherited("show", arguments);
		
		this.logEntry("show()");
		
		this.repository = repository;
		if(!this.repository || this.repository.isIERLoaded()){
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

		this.set("title", ier_messages.addFilePlanDialog_title);
		this.getDefaultButton().set("label", this._ierMessages.baseDialog_addButton);

		this.setIntroText(ier_messages.addFilePlanDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh01.htm"));
		
		this.setResizable(true);
		
		//renders repository pane
		this._filePlanRepositoryPane.createRendering(this.repository, null, true); // always read-only

		//renders properties pane
		this._entityItemPropertiesPane.createRendering({
			repository: this.repository,
			rootClassId : ier_constants.ClassName_FilePlan, 
			entityType: ier_constants.EntityType_FilePlan,
			hideContentClassSelector: true,
			item: null,
			isReadOnly: this._isReadOnly
		});

		//Connects and change existing attributes for the property pane before they are rendered
		this.connect(this._entityItemPropertiesPane, "onRenderAttributes", function(attributes) {
			for(var index in attributes){
				var attribute = attributes[index];
				if(attribute.id == ier_constants.Property_RetainMetadata){
					attribute.defaultValue = 1; // change default value from 0 to 1
					break;
				}
			}
		});

		//Sets up and renders the security pane after the property pane has finished rendering
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {
			var contentClass = this._entityItemPropertiesPane.getContentClass();
			var properties = this._entityItemPropertiesPane.getProperties();
			
			//renders the security pane
			this._entityItemSecurityPane.createRendering(this.repository, null, null, contentClass, properties, this._isReadOnly);
			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});
		
		this.logExit("_renderDialog");
	},

	_addFilePlan: function(className, criterias, permissions) {
		this.logEntry("_addFilePlan");

		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
			var resultSet = new ier_model_ResultSet(response);
			var items = resultSet.getItems();
			this.repository.clearFilePlans();
			this.repository.onConfigure(this.repository, items);

			this.onCancel();
		}));
		params.requestParams[ier_constants.Param_FilePlanRepositoryId] = this.repository.id;
		params.requestParams[ier_constants.Param_ClassName] = className;
		
		var data = new Object();
		data[ier_constants.Param_Properties] = criterias;
		data[ier_constants.Param_Permissions] = permissions;
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateFilePlan, ier_constants.PostEncoding, params);

		this.logExit("_addFilePlan");
	},

	_onClickAdd: function() {
		this.logEntry("_onClickAdd");
		
		if (this.validateInput()) {
			var properties = this._entityItemPropertiesPane.getProperties();
			var permissions = this._entityItemSecurityPane.getPermissions();
			var className = this._entityItemPropertiesPane.getContentClass().id;

			this._addFilePlan(className, properties, permissions);
		}
		
		this.logExit("_onClickAdd");
	}
});});
