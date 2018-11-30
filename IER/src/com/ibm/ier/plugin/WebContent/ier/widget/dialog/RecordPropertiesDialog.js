define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/dom-style",
	"dojo/string",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ecm/model/Request",
	"ier/widget/dialog/IERBaseDialog",
	"ecm/MessagesMixin",
	"dojo/text!./templates/RecordPropertiesDialogContent.html",
	"idx/grid/PropertyGrid",
	"idx/layout/ContentPane", // in content
	"idx/layout/TitlePane", // in content
	"ier/widget/panes/EntityItemPropertiesPane",	// in content
	"ier/widget/panes/EntityItemSecurityPane",	// in content
	"ier/widget/panes/EntityItemHoldPane",	//in content
	"ier/widget/panes/EntityItemLinksPane",	// in content
	"ier/widget/panes/EntityItemHistoryPane",	//in content
	"ier/widget/panes/EntityItemFoldersFiledInPane",	// in content
	"dijit/layout/TabContainer" // in content
], function(dojo_declare, dojo_lang, dojo_array, dom_style, dojo_string, ier_constants, ier_messages, ier_util, ecm_model_Request, ier_dialog_IERBaseDialog, ecm_MessageMixin, contentString, PropertyGrid){
	return dojo_declare("ier.widget.dialog.RecordPropertiesDialog", [ier_dialog_IERBaseDialog, ecm_MessageMixin], {
	   	contentString: contentString,
		
	   	dlgTitle_editable: ier_messages.recordPropDlg_title_editable,
	   	dlgTitle_readOnly: ier_messages.recordPropDlg_title_readOnly,
	   	
	   	/**
	   	 * Whether this dialog should be displayed in read-only format
	   	 */
	   	isReadOnly : false,
	   	
	   	/**
	   	 * Whether this dialog will only show properties and not holds or links
	   	 */
	   	showOnlyProperties: false,
	   	
		postMixInProperties: function() {
			this.inherited(arguments);
		},
		
		postCreate: function() {
			this.inherited(arguments);
			
			this._classes = [
		   		{type: ier_constants.EntityType_Record, name: ier_constants.ClassName_Record},
		   		{type: ier_constants.EntityType_ElectronicRecord, name: ier_constants.ClassName_ElectronicRecord},
		   		{type: ier_constants.EntityType_EmailRecord, name: ier_constants.ClassName_EmailRecord},
		   		{type: ier_constants.EntityType_PhysicalRecord, name: ier_constants.ClassName_PhysicalRecord},
		   		{type: ier_constants.EntityType_PDFRecord, name: ier_constants.ClassName_Record}
		   	];

			this.addChildPane(this._entityItemPropPane);
			this.addChildPane(this._entityItemSecurityPane);
			this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
			this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, false);

			this.connect(this._entityItemPropPane, "onPropertiesChanged", "_enableButton");
			this.connect(this._entityItemSecurityPane, "onSecuritiyChanged", "_enableButton");
			this.connect(this._entityItemFiledInPane, "onFiledInChanged", "_enableButton");
			this.connect(this, "_onClickApply", "_disableButton");
		},
		
		setDialogMode: function(editable) {
			if(editable) {
				dom_style.set(this._saveButton.domNode, "display", "");
				dom_style.set(this._applyButton.domNode, "display", "");
				this.cancelButton.set("label", ier_messages.baseDialog_cancelButton);
			} else {
				dom_style.set(this._saveButton.domNode, "display", "none");
				dom_style.set(this._applyButton.domNode, "display", "none");
				this.cancelButton.set("label", ier_messages.baseDialog_closeButton);
			}
		},
		
		hasEditablePane: function() {
			return !this.isReadOnly && (this.item.privModifyProperties || this.item.privModifyPermissions);
		},
		
		show: function(repository, item) {
			this.inherited("show", []);
			
			this.logEntry("show()");
			this.item = item;
			this.repository = repository;
			
			//do not show the other tabs if we are only showing properties
			if(this.showOnlyProperties){
				dom_style.set(this._entityItemSecurityPane.controlButton.domNode, "display", "none");
				dom_style.set(this._entityItemFiledInPane.controlButton.domNode, "display", "none");
				dom_style.set(this._entityItemLinksPane.controlButton.domNode, "display", "none");
				dom_style.set(this._entityItemHoldPane.controlButton.domNode, "display", "none");
				dom_style.set(this._entityItemHistoryPane.controlButton.domNode, "display", "none");
			}
			
			this.setDialogMode(this.hasEditablePane());
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
		 * Validates the form and disables the default button if necessary
		 * @returns {Boolean}
		 */
		validateInput: function() {
			var childPanes = this.getChildPanes();
			for(var i in childPanes){
				var childPane = childPanes[i];
				if(childPane.isValidationRequired()){
					var validate = childPane.validate();
					if(!validate){
						this._disableButton();
						return false;
					}
				}
			}
			this._enableButton();
			return true;
		},
		
		_renderDialog: function() {
			this.logEntry("_renderDialog");
			
			this.set("title", ier_messages.record);
			this.setIntroText(ier_messages.recordPropDlg_description);
			
			this.setResizable(true);
			
			this._initPropPane();
			
			//hide and don't load the security, filed in, hold, and links tabs if it's only supposed to show properties
			if(!this.showOnlyProperties){
				this._initSecurityPane();
				this._initFiledInPane();
				this._initHoldPane();
				this._initLinksPane();
				this._initHistoryPane();
			}
			
			this.logExit("_renderDialog");
		},
		
		_initPropPane: function() {
			this.logEntry("_initPropPane");
			
			this._entityItemPropPane.createRendering({
				repository: this.repository,
				parentFolder: this.item.parent,
				rootClassId : this.item.getContentClass().id, 
				defaultNameProperty: ier_constants.Property_Name, 
				entityType: ier_constants.EntityType_Record,
				disableContentClassSelector: true,
				defaultClass: this.item.getContentClass(),
				item: this.item,
				isCreate: false,
				isReadOnly : this.isReadOnly
			});
			
			this.connect(this._entityItemPropPane, "onRenderAttributes", function(attributes, deferArray) {
				this._entityItemDetailPane.createRendering(this.item, attributes);
			});
			
			this.logExit("_initPropPane");
		},
		
		_initFiledInPane: function() {
			this.logEntry("_initFieldInPane");
			this._entityItemFiledInPane.setItem(this.item);
			this._entityItemFiledInPane.render();
			this.logExit("_initFieldInPane");
		},
		
		_initSecurityPane: function() {
			this.logEntry("_initSecurityPane");
			this.connect(this._entityItemPropPane, "onCompleteRendering", function(){
				var contentClass = this._entityItemPropPane.getContentClass();
				var properties = this._entityItemPropPane.getProperties();
				
				if(!this.showOnlyProperties){
					this._entityItemSecurityPane.createRendering(this.repository, this.item, this._parentFolder, contentClass, properties, !this.item.privModifyPermissions);
				}
				
				this._entityItemPropPane.resizeCommonProperties();
				this.resize();
			});
			this.logExit("_initSecurityPane");
		},
		
		_initHoldPane: function() {
			this.logEntry("_initHoldPane");
			this.connect(this._entityItemHoldPane, "onShow", dojo_lang.hitch(this, function(){
				if(!this._entityItemHoldPane.isLoaded()) {
					this._entityItemHoldPane.createRendering(this.repository, this.item);
				}
			}));
			this.logExit("_initHoldPane");
		},
		
		_initLinksPane: function() {
			this.logEntry("_initLinksPane");
			this.connect(this._entityItemLinksPane, "onShow", dojo_lang.hitch(this, function(){
				if(!this._entityItemLinksPane.isLoaded()) {
					this._entityItemLinksPane.createRendering(this.repository, this.item);
				}
			}));
			this.logExit("_initLinksPane");
		},
		_initHistoryPane: function() {
			this.logEntry("_initHistoryPane");
			this.connect(this._entityItemHistoryPane, "onShow", dojo_lang.hitch(this, function(){
				if(!this._entityItemHistoryPane.isLoaded()) {
					this._entityItemHistoryPane.createRendering(this.repository, this.item);
				}
			}));
			this.logExit("_initHistoryPane");
		},
		
		_onClickApply: function(){
			this._submitChanges(false);
		},
		
		_onClickSave: function() {
			this._submitChanges(true);
		},
		
		_submitChanges: function(closeDlg) {
			var className = this._entityItemPropPane.getContentClass().id;
			this.logEntry("_onClickUpdate");
			var properties = this._entityItemPropPane.getProperties();
			var permissions = this._entityItemSecurityPane.getPermissions();
			var securityParentId = this._entityItemFiledInPane.getSecurityParentItemId();
			this._updateEntityItem(className, properties, permissions, null, securityParentId, closeDlg);
			this.logExit("_onClickUpdate");
		},
		
		_updateEntityItem: function(className, criterias, permissions, dispScheduleId, securityParentId, closeDlg) {
			this.logEntry("updateEntityItem");
			
			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
				this.item.permissions = null;
				this.item.retrieveAttributes(null, false);
				if(closeDlg){
					this.onCancel();
				}
			}));
			params.requestParams[ier_constants.Param_RecordId] = this.item.id;
			params.requestParams[ier_constants.Param_RecordClass] = className;
			
			var data = new Object();
			data[ier_constants.Param_Properties] = criterias;
			data[ier_constants.Param_Permissions] = permissions;
			data[ier_constants.Param_SecurityParentItemId] = securityParentId;
			params["requestBody"] = data;
			
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_EditRecord, ier_constants.PostEncoding, params);
			this.logExit("_updateEntityItem");
		},

		_enableButton: function(){
			this._saveButton.set("disabled", false);
			this._applyButton.set("disabled", false); 
		},
		
		_disableButton: function(){
			this._saveButton.set("disabled", true);
			this._applyButton.set("disabled", true); 
		}
	});
});