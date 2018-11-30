define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/dom-class",
	"dojo/dom-geometry",
	"dojo/dom-style",	
	"dojo/keys",
	"ecm/model/Request",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	//"ier/widget/dialog/IERBaseDialog",
	"ier/widget/dialog/BaseAccordionWizardDialog",
	"ecm/model/AttributeDefinition",
	"ier/model/ReportDefinition",
	//"dojo/text!./templates/ReportDefinitionDialogContent.html",
	//"dijit/layout/ContentPane", // in content
	//"dijit/layout/TabContainer", // in content
	"ier/widget/panes/EntityItemPropertiesPane", // in content
	"ier/widget/panes/EntityItemSecurityPane", // in content
	"ier/widget/panes/ReportDefinitionQueryPane", // in template
	"ier/widget/panes/ReportDefinitionPropertiesPane" // in template
], function(dojo_declare, dojo_lang, dojo_array, domClass, geometry, domStyle, dojo_keys, ecm_model_Request,
		ier_constants, ier_messages, ier_util, ier_dialog_BaseAccordionWizardDialog,//ier_dialog_IERBaseDialog, 
		ecm_model_AttributeDefinition, ier_model_ReportDefinition,
		ier_widget_EntityItemPropertiesPane, ier_widget_EntityItemSecurityPane, 
		ier_widget_ReportDefinitionQueryPane, ier_widget_ReportDefinitionPropertiesPane) {//}, contentString){

/**
 * @name ier.widget.dialog.ReportDefinitionDialog
 * @class Provides an interface to add report definition
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.ReportDefinitionDialog", [ier_dialog_BaseAccordionWizardDialog], {
//return dojo_declare("ier.widget.dialog.ReportDefinitionDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.ReportDefinitionDialog.prototype */

	//contentString: contentString,
	//entityProperties: null,
	widgetsInTemplate: true,
	_reportDefinitionFolder: null,
	ier_messages:ier_messages,
	_reportDefinition: null,
	finishButtonLabel: ier_messages.baseDialog_addButton,
	_isDirty: false,
	_isCopy: false,
	showSecurity: true,

	postCreate: function(){
		this.inherited(arguments);
		
		this._isDirty = false;
		this.setIntroText(ier_messages.reportDefDialog_intro); 
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh16.htm"));
		this.setResizable(true);

		this._descriptionPane = new ier_widget_EntityItemPropertiesPane();
		this._descriptionPane.title = ier_messages.reportDefinitionDialog_ReportDescription;
		
		this._propertiesPane = new ier_widget_ReportDefinitionPropertiesPane({disabled : true});
		this._sqlPane = new ier_widget_ReportDefinitionQueryPane({disabled : true});

		this._securityPane = new ier_widget_EntityItemSecurityPane({disabled : true});
		this._securityPane.title = ier_messages.reportDefinitionDialog_Security;
		
		this.addChildPane(this._descriptionPane);
		this.addChildPane(this._propertiesPane);
		this.addChildPane(this._sqlPane);
		if(this.showSecurity){
			this.addChildPane(this._securityPane);
		}
		
		this.connect(this._descriptionPane, "onRenderAttributes", 
				dojo_lang.hitch(this, function(attributes, deferArray, contentClass)
		{
			var reportTitleIdx = -1;
			var despIdx = -1;
			dojo_array.forEach(attributes, function(item, idx) {
				if (item.id == ier_constants.Property_RMReportTitle) {
					item.required = true;
					reportTitleIdx = idx;
				}
				else if (item.id == ier_constants.Property_Description)
					despIdx = idx;
			});

			// Swap the report title attribute to attributes[0] (ie the top of the attributes array)
			if (reportTitleIdx!=-1) {
				attributes[0] = attributes.splice(reportTitleIdx, 1, attributes[0])[0];
			}
			// Swap the desp attribute to attributes[1]
			if (despIdx!=-1) {
				attributes[1] = attributes.splice(despIdx, 1, attributes[1])[0];
			}
			
			if (this._isCopy) {
				if (this._reportDefinition)
				    this._reportDefinition.name= this._reportDefinition.name + "(2)";//ier_constants.ReportDefinitionCopy + this._reportDefinition.name;	

				if (this._descriptionPane.item && this._descriptionPane.item.attributes)				
					this._descriptionPane.item.attributes[ier_constants.Property_RMReportTitle] = 
						this._descriptionPane.item.attributes[ier_constants.Property_RMReportTitle] + "(2)";
						//ier_constants.ReportDefinitionCopy + this._descriptionPane.item.attributes[ier_constants.Property_RMReportTitle];
			}

			// Add Report name as first.
			var reportNameDef = this._renderStringParameter(ier_constants.Property_ReportName, 
							ier_messages.reportDefDialog_reportNameFieldName, 
							true, 
							contentClass,
							((this._reportDefinition) ? this._reportDefinition.name : null));//,
							//(this._reportDefinition != null));
			attributes.splice(1, 0, reportNameDef);
			
			// Add Entry Help and DBTableName as the last.
			var entryHelp = this._renderStringParameter("EntryHelp", 
							ier_messages.reportDefDialog_entryHelpFieldName, 
							false, 
							contentClass,
							((this._reportDefinition) ? this._reportDefinition.getEntryHelp() : null));
			
			var dbTableName = this._renderStringParameter("DatabaseTableName", 
							ier_messages.reportDefDialog_dbTableFieldName, 
							true, 
							contentClass,
							((this._reportDefinition) ? this._reportDefinition.tableName : null));
			attributes.push(entryHelp);
			attributes.push(dbTableName);			
		}));						
				
		this.connect(this._descriptionPane, "onInputChange", dojo_lang.hitch(this, function(){
			this._isDirty = true;
			if(this._descriptionPane.validate()){
				this.enablePane(this._propertiesPane);
				this.enablePane(this._sqlPane);
				this.enablePane(this._securityPane);
			}
			else {
				this.disablePane(this._propertiesPane);
				this.disablePane(this._sqlPane);
				this.disablePane(this._securityPane);
			}
			this.updateButtonsState();
		}));
		
		this.connect(this._descriptionPane, "onKeyDown", function(evt) {
			if (evt.keyCode == dojo_keys.ENTER) {
				evt.stopPropagation();
			}
		});

		this.connect(this._propertiesPane, "onChange", "_onReportPropertiesChange");
		//this.connect(this._sqlPane, "onChange", "_onSqlPaneChange");
	},
		
	
	/**
	 * Shows the ReportDefinitionDialog
	 * @param repository
	 */
	show: function(repository, item, isCopy) { //}, entityProperties){
		this.inherited(arguments);//this.inherited("show", []);		
		this.logEntry("show()");
		this._isDirty = false;
		this._isCopy = isCopy;
		
		if (item) {
			this.item = item;
			var docid = (item.docid != null) ? item.docid : ier_util.getGuidId(item.id);
			this._reportDefinition = new ier_model_ReportDefinition({id: docid, name: item.name, repository: repository});
		}

		//this.entityProperties = entityProperties;
		if (this._isCopy) {
			this.set("title", ier_messages.reportDefDialog_copyReportTitle);
			this.getDefaultButton().set("label", this._ierMessages.reportDefDialog_copyButton);				
		}
		else {
			if(this._reportDefinition){
				this.set("title", ier_messages.reportDefDialog_editReportTitle);
				this.getDefaultButton().set("label", this._ierMessages.baseDialog_updateButton);				
			}else{
				this.set("title", ier_messages.reportDefDialog_addReportTitle);
				this.getDefaultButton().set("label", this._ierMessages.baseDialog_addButton);
			}
		}
		this.repository = repository;
		if(this.repository.isIERLoaded()){
			this._renderDialog();//retentionModeValue);
		} else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}
		
		this.resize();
		
		this.logExit("show()");
	},
		
	_renderStringParameter: function(ID, Label, isReq, contentClass, defaultValue, readonly, choiceList) {
		var criterion = new ecm_model_AttributeDefinition({
			id: ID,
			name: Label,
			label: Label,
			repositoryType: this.repository.type,
			dataType: ier_constants.DataType_String, //"xs:string",
			required: isReq,
			defaultValue: (defaultValue == null) ? [] : defaultValue,
			cardinality: "SINGLE",
			settability: ier_constants.ReportEntry_Settability, //"readWrite",
			choiceList: choiceList,
			contentClass: contentClass,
			readOnly: (readonly) ? true : false,
			maxLength: 256
		});
		return criterion;		
	},

	_renderDialog: function() { //retentionModeValue){
		this.logEntry("_renderDialog");
		this._renderPaneIdx = 0;
		if (this._reportDefinition) 
			this._reportDefinition.loadReportDefinition(dojo_lang.hitch(this, function(reportDefinition) {
				this._reportDefinition = reportDefinition;
				this._renderNextPane(this._renderPaneIdx);
			}));
		else 
			this._renderNextPane(this._renderPaneIdx);

		this.logExit("_renderDialog");
	},
	
	_renderNextPane: function(paneIdx){
		this.logEntry("_renderNextPane");
		
		switch (paneIdx) {
		case 0:
			//Completes the rendering the properties pane
			this.connect(this._descriptionPane, "onCompleteRendering", dojo_lang.hitch(this, function() {						
				this._descriptionPane.resizeCommonProperties();	
				this._descriptionPane.onInputChange();
				this.resize();
				this.validateInput();
				
				var namePrefilled = this._descriptionPane.getPropertyValue(ier_constants.Property_ReportName);
				var titlePrefilled = this._descriptionPane.getPropertyValue(ier_constants.Property_RMReportTitle);
				if (namePrefilled != titlePrefilled) {
					this._descriptionPane.enablePropertieValuesSyncUp(false);
				}
				this._renderNextPane((++this._renderPaneIdx));
			}));
			
			this._descriptionPane.createRendering({
				repository: this.repository,
				rootClassId : ier_constants.ClassName_ReportDefinition, 
				defaultNameProperty: ier_constants.Property_RMReportTitle, 
				entityType: ier_constants.EntityType_ReportDefinition,
				hideContentClassSelector: true,
				item: this.item
			});			

			break;
		case 1:			
			//this._propertiesPane.set("open", true);
			this.connect(this._propertiesPane, "onCompleteRendering", dojo_lang.hitch(this, function() {
				this._renderNextPane((++this._renderPaneIdx));
			}));
			this._propertiesPane.createRendering(this.repository, this._reportDefinition);
			break;
		case 2:
			//this._sqlPane.set("open", true);
			this.connect(this._sqlPane, "onCompleteRendering", dojo_lang.hitch(this, function() {
				this._renderNextPane((++this._renderPaneIdx));
			}));
			
			this._sqlPane.createRendering(this.repository, this._reportDefinition);			
			break;
		case 3:
			//renders the security pane
			var contentClass = this.repository.getContentClass(ier_constants.ClassName_ReportDefinition);
			this._securityPane.createRendering(this.repository, (this._reportDefinition ? this._reportDefinition : null), null, contentClass);		
			this._onCompleteRendering();
			break;
		default:
			break;
		}					
		this.logExit("_renderNextPane");
	},
	
	_onReportPropertiesChange: function() {
		this.logEntry("_onReportPropertiesChange");
		this._isDirty = true;
		var items = this._propertiesPane.getSelectedItems();
		this._sqlPane.setGridItems(items);
		
		//this._enableUpdateButton(true);		
		
		this.logExit("_onReportPropertiesChange");
	},
	
	/*
	_onSqlPaneChange: function() {
		this.logEntry("_onSqlPaneChange");
		this._isDirty = true;
		this._enableUpdateButton(true);		
		this.logExit("_onSqlPaneChange");		
	},
	
	_enableUpdateButton: function(enabled) {
		if(this._reportDefinition){
			this.setButtonEnabled(this.childButtons[0], enabled);
		}
	},
	
	*/
	
	_validateSQLInput: function() {
		return this._sqlPane._validateInput();
	},
	
/*	validateInput: function() {
		if (this._reportDefinition) {
			var _validFields = this.inherited(arguments);
			return _validFields && this._isDirty;
		}
		else
			return this.inherited(arguments);
	},
*/
	_addReportDefinition: function(properties, queries, permissions) {
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){	
			this.hide();
			this.repository.onConfigure(this.repository);
		}));
				
		var data = {};
		
		if (!this._isCopy && this._reportDefinition != null) {
			data[ier_constants.Param_Id] = this._reportDefinition.id;
		}
		data[ier_constants.Param_ReportDefinitionProperties] = properties;
		data[ier_constants.Param_Properties] = queries;
		data[ier_constants.Param_Permissions] = permissions;
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_SaveReportDefinition, ier_constants.PostEncoding, params);
	},
	
	_onClickAdd: function(){
		this.logEntry("_onClickAdd");
		
		if (this._validateSQLInput()) {
			var properties = this._descriptionPane.getProperties();
			var queries = this._sqlPane.getQueries();
			var permissions = this._securityPane.getPermissions();

			this._addReportDefinition(properties, queries, permissions);
		}
		
		this.logExit("_onClickAdd");
	},
		
	onFinishButtonClicked: function() {
		this._onClickAdd();
	},

	_onCompleteRendering: function() {
		this._isDirty = false;
		this._sqlPane.setTitlePaneFocusNodeHeight();
		//this._enableUpdateButton(false);
		this.onCompleteRendering();
	},
	
	onCompleteRendering: function() {
	}
});});
