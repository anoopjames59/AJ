define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-attr",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/DeclareRecordDialog.html",
    	"dijit/layout/ContentPane", // in content
    	"idx/layout/TitlePane", // in content
    	"ier/widget/panes/RecordDeclareGeneralPane", // in content
    	"ier/widget/panes/RecordDeclarePropertiesPane" // in content
], function(dojo_declare, dojo_lang, dojo_attr, ecm_model_Request,
		ier_constants, ier_messages, ier_util, ier_dialog_IERBaseDialog, contentString){

/**
 * Non-Classified Manual Record Declaration Dialog
 */
return dojo_declare("ier.widget.dialog.DeclareRecordDialog", [ier_dialog_IERBaseDialog], {	
	contentString: contentString,
	widgetsInTemplate: true,
	_parentFolder: null,
	_firstFocusItem: this.titleNode,
	tabStart: this.titleNode,
	
	constructor: function() {
	},
	
	postCreate: function() {
		this.logEntry("postCreate()");
		
		this.inherited(arguments);
		this.addChildPane(this._recordDeclareGeneralPane);
		this.addChildPane(this._recordDeclarePropertiesPane);
		this.addButton(ier_messages.declareRecordDialog_declare, "_onClickDeclare", true, true);
		this._firstFocusItem = this.titleNode;
		this.tabStart = this.titleNode;
		
		this.logExit("postCreate()");
	},
	
	/**
	 * Shows the DeclareRecord dialog
	 * @param repository
	 * @param items
	 * @param parentFolder - folder that the current document resides in
	 */
	show: function(repository, items, parentFolder) {
		this.inherited("show", []);
		
		this.logEntry("show()");
		this._parentFolder = parentFolder;
		this._items = items;
		
		this.repository = repository;
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
	 * Render the declare dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");
		
		this.setTitle(ier_messages.declareRecordDialog_title);
		
		this.setIntroText(ier_messages.declareRecordDialog_info);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh12.htm"));

		//renders the other two panes
		this._recordDeclareGeneralPane.createRendering(this.repository, this);
		this._recordDeclarePropertiesPane.createRendering(this.repository, this._items);
		
		//if the length is 1, it means it only contains the blank repository.  If no other repository is available,
		//it means no file plan repositories are available.
		if(this._recordDeclarePropertiesPane.getFilePlanRepositories().length == 1){
			this.setMessage(ier_messages.error_noFilePlanRepositoriesAvailableForDeclare);
		}
		else {
			this.clearMessage();
		}
		
		this.connect(this._recordDeclarePropertiesPane, "onCompleteRendering", function() {
			this._recordDeclarePropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});
		
		this.logExit("_renderDialog");
	},
	
	/**
	 * Performs the actual declaration by making the call to the server
	 */
	_onClickDeclare: function() {
		this.logEntry("onClickDeclare()");
		if (this.validateInput()) {
			var items = this._items;
			var repository = this.repository;
			var properties = this._recordDeclarePropertiesPane.getClassProperties();
			
			var data = new Object();
			data[ier_constants.Param_Properties] = properties;
			
			var serviceParams = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response)
			{	
				this.onCancel();
				
				//disable the declare action by revoking the privilege
				for(var i in items){
					items[i].privileges = items[i].privileges ^ ecm.model.Item.PrivilegeToBitmask["privIERRecordDeclare"];
					items[i].declaredAsRecord = "true";
					items[i].onChange([items[i]]);
				}
			}));
			serviceParams.requestParams[ier_constants.Param_FilePlanRepositoryNexusId] = this._recordDeclarePropertiesPane.getFilePlanRepository().id;
			serviceParams.requestParams[ier_constants.Param_NumberOfDocuments] = items.length;
			serviceParams.requestParams[ier_constants.Param_FilePlanRepositoryFolderLocation] = this._recordDeclarePropertiesPane.getFPRepositoryLocation();
			serviceParams.requestParams[ier_constants.Param_RecordClass] = this._recordDeclarePropertiesPane.getRecordClassSymbolicName();
			serviceParams.requestParams[ier_constants.Param_Properties] = properties;
			serviceParams["requestBody"] = data;
			
			for (var i in items) {
				serviceParams.requestParams[ier_constants.Param_DocId +i] = items[i].id;
			}
			
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_Declare, ier_constants.PostEncoding, serviceParams);
		}
		this.logExit("onClickDeclare()");
	}
});});