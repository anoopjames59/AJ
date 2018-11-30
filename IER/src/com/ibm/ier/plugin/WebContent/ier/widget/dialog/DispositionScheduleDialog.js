define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/widget/dialog/IERBaseDialog",
	"dojo/text!./templates/PropertyDialogContent.html",
	"dijit/layout/ContentPane", // in content
	"idx/layout/TitlePane", // in content
	"ier/widget/panes/EntityItemPropertiesPane" // in content
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_util, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.DispositionScheduleDialog
 * @class Provides an interface to show properties of disposition schedules
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.DispositionScheduleDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.DispositionScheduleDialog.prototype */

	contentString: contentString,
	entityProperties: null,

	postCreate: function(){
		this.inherited(arguments);

		this.setIntroText(ier_messages.addDispositionScheduleDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh11.htm"));
		this.setResizable(true);

		this.addChildPane(this._propertiesPane);
		this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, false);
		this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
	},
		
	/**
	 * Shows the AddDispositionScheduleDialog
	 * @param repository
	 */
	show: function(repository, item, retentionModeValue, entityProperties){
		this.inherited("show", []);
		
		this.logEntry("show()");
		
		this.item = item;
		this.entityProperties = entityProperties;
		
		if(item) {
			this.set("title", ier_messages.addDispositionScheduleDialog_editTitle);
			//this._applyButton.domNode.style.display = "none";
		} else {
			this._saveButton.set("label", this._ierMessages.baseDialog_addButton);
			this.set("title", ier_messages.addDispositionScheduleDialog_title);
		}
		
		this.repository = repository;
		if(this.repository.isIERLoaded()){
			this._renderDialog();
		} else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}
		
		this.resize();
		
		this.logExit("show()");
	},
	
	_renderDialog: function(retentionModeValue){
		this.logEntry("_renderDialog");
		
		this._propertiesPane.createRendering({
			repository: this.repository,
			rootClassId : ier_constants.ClassName_DispositionSchedule, 
			defaultNameProperty: ier_constants.Property_DispositionScheduleName, 
			entityType: ier_constants.EntityType_DispositionSchedule,
			hideContentClassSelector: true,
			item: this.item,
			isReadOnly : true
		});
		
		//if(retentionModeValue)
		//	this._dispositionPane = retentionModeValue;
		//this._dispositionPane.createRendering(this.repositorys);
		
		//Connects and change existing attributes for the property pane before they are rendered
		/*this.connect(this._propertiesPane, "onRenderAttributes", dojo_lang.hitch(this, function(attributes) {
			for(var i in this.entityProperties){
				var propertyValue = this.entityProperties[i];
				this._propertiesPane.setPropertyValue(i, propertyValue);
			}
			
			if(callback)
				callback();
		}));*/
		
		//Completes the rendering the properties pane
		this.connect(this._propertiesPane, "onCompleteRendering", dojo_lang.hitch(this, function() {						
			this._propertiesPane.resizeCommonProperties();
			
			this.resize();
			this.validateInput();
		}));

		this.logExit("_renderDialog");
	},
	
	validateInput: function() {
		//var valid = this.inherited(arguments);
		//this._applyButton.set("disabled", !valid);
		//return valid;
		return false;
	},

	_save: function(close){
		this.logEntry("_onClickAdd");
		
		/*var mode = this._dispositionPane.getRetentionMode();
		if(mode == ier_constants.DispositionMode_SimpleRetention){
			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){	
				if(close){
					this.hide();
				}
				this.repository.onConfigure(this.repository);
			}));
			params.requestParams[ier_constants.Param_DispositionEventOffset] = this._dispositionPane.getRetentionPane().getDispositionEventOffset();
			params.requestParams[ier_constants.Param_DispositionEventCutoffBase] = this._dispositionPane.getRetentionPane().getDispositionCutoffBase();
			params.requestParams[ier_constants.Param_DispositionAggregationType] = this._dispositionPane.getRetentionPane().getDispositionAggregation();
			params.requestParams[ier_constants.Param_DispositionCutoffAction] = this._dispositionPane.getRetentionPane().getDispositionCutoffAction();
			
			var data = {};
			data[ier_constants.Param_Properties] = this._propertiesPane.getProperties();
			params["requestBody"] = data;
			
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateSimpleDispositionSchedule, ier_constants.PostEncoding, params);
		}*/

		this.logExit("_onClickAdd");
	},
	
	_onClickApply: function(){
		this.logEntry("_onClickApply");

		this._save(false);

		this.logExit("_onClickApply");
	},

	_onClickSave: function(){
		this.logEntry("_onClickSave");

		this._save(true);

		this.logExit("_onClickSave");
	}

});});
