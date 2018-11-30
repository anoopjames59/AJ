define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-style",
	"ier/constants",
	"ier/messages",
	"ier/widget/dialog/IERBaseDialog",
	//"dojo/text!./templates/EventTriggerDialogContent.html",
	"dojo/text!./templates/PropertyDialogContent.html",
	"dijit/layout/ContentPane", // in content
	"idx/layout/TitlePane", // in content
	//"ier/widget/panes/EventTriggerGeneralPane", // in content
	"ier/widget/panes/EntityItemPropertiesPane" // in content
	//"ier/widget/panes/EntityItemConditionPane" // in content
], function(dojo_declare, dojo_lang, dojo_domStyle, ier_constants, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.EventTriggerDialog
 * @class Provides an interface to add an event disposition trigger
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.EventTriggerDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.AddTriggerDialog.prototype */

	contentString: contentString,
	
	//trigger mode value
	triggerModeValue: ier_constants.EventType_InternalEventTrigger,
	
	aggregation: null,

	postCreate: function(){
		this.inherited(arguments);

		this.setResizable(true);
		this.addDomNodeCSSClass("ierAddTriggersDialog");

		//this.addChildPane(this._generalPane);
		this.addChildPane(this._propertiesPane);
		this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, true);
		this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
		
		//resizes the pane after the property pane has finished rendering
		this.connect(this._propertiesPane, "onCompleteRendering", function() {				
			this._propertiesPane.resize();
			this.resize();
		});
		
		//this.connect(this._generalPane, "onTriggerModeChange", dojo_lang.hitch(this, function(mode){
			//this.triggerModeValue = mode;
			//this._propertiesPane.renderProperties(this._propertiesPane.contentClass);
			
			//hide condition pane for External Event
			//if(this.triggerModeValue == ier_constants.EventType_InternalEventTrigger)
				//dojo_domStyle.set(this.conditionTitlePane.domNode, "display" , "");
			//else
				//dojo_domStyle.set(this.conditionTitlePane.domNode, "display" , "none");
		//}));
		
		//this._conditionPane.isValidationRequired = true;
	},
		
	/**
	 * Shows the AddActionDialog
	 * @param repository
	 */
	show: function(repository, item){
		this.inherited("show", []);
		
		this.logEntry("show()");
		
		this.item = item;
		if(!this.item){
			this.set("title", ier_messages.eventTriggerDialog_addTitle);
			this._saveButton.set("label", this._ierMessages.baseDialog_addButton);
			this._applyButton.domNode.style.display = "none";
		}else {
			this.set("title", ier_messages.eventTriggerDialog_title);
			//this.addButton(ier_messages.baseDialog_saveButton, "_onClickAdd", true, true);
		}
		
		//this._generalPane.createRendering({
		//	isReadOnly : this.item != null
		//});
		
		this.setIntroText(ier_messages.eventTriggerDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh03.htm"));
		
		this.repository = repository;
		if(this.repository.isIERLoaded()){
			this._renderDialog();
		}else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}
		
		this.logExit("show()");
	},
	
	_renderDialog: function(){
		this.logEntry("_renderDialog");
		
		this._propertiesPane.createRendering({
			repository: this.repository,
			rootClassId : ier_constants.ClassName_DispositionTrigger, 
			defaultNameProperty: ier_constants.Property_DispositionTriggerName, 
			entityType: ier_constants.EntityType_DispositionTrigger,
			hideContentClassSelector: true,
			item: this.item,
			isCreate: this.item ? false : true,
			isReadOnly: true
		});
		
		//var triggerId = (this.item && this.item.id);
		this.triggerModeValue = this.item.attributes[ier_constants.Property_EventType];
		/*if(triggerId){
			this.repository.getObjectConditions(triggerId, ier_constants.EntityType_DispositionTrigger, dojo_lang.hitch(this, function(conditions){				
				this._conditionPane.set("repository", this.repository);
				this._conditionPane.set("conditions", conditions);
			}));
		}else{
			this._conditionPane.set("repository", this.repository);
			this._conditionPane.set("conditions", []);
		}*/
		
		//Connects and change existing attributes for the property pane before they are rendered
		this.connect(this._propertiesPane, "onRenderAttributes", function(attributes) {
			
			//special handling of properties. Internal Event Trigger displays a different set of properties and needs to be mappped accordingly
			if(this.triggerModeValue == ier_constants.EventType_InternalEventTrigger){
				
				for(var index in attributes){
					var attribute = attributes[index];
					if(attribute.id == ier_constants.Property_DispositionTriggerName){
						attribute.name = ier_messages.addInternalEventTriggerDialog_internalEventName;
					}
					else if(attribute.id == ier_constants.Property_RMEntityDescription){
						attribute.name = ier_messages.addInternalEventTriggerDialog_internalDescription;
					}
					else if(attribute.id == ier_constants.Property_Aggregation){
						attribute.required = true;
						attribute.readOnly = false;
						
						//remove the classification scheme choice from the allowed values in aggregation
						var values = attribute.allowedValues;
						var newValues = [];
						for(var i in values){
							var value = values[i];
							if(value != ier_constants.ClassName_FilePlan){
								newValues.push(value);
							}
						}
						attribute.allowedValues = newValues;
					}
					attribute.setMetaData(ier_constants.DispositionEventType, ier_constants.EventType_InternalEventTrigger);
				}
			}
			//external trigger
			else {
				for(var index in attributes){
					var attribute = attributes[index];
					if(attribute.id == ier_constants.Property_DispositionTriggerName){
						attribute.name = ier_messages.addExternalEventTriggerDialog_externalEventName;
					}
					else if(attribute.id == ier_constants.Property_RMEntityDescription){
						attribute.name = ier_messages.addExternalEventTriggerDialog_externalDescription;
					}
	
					attribute.setMetaData(ier_constants.DispositionEventType, ier_constants.EventType_ExternalEventTrigger);
				}
			}
		});
		
		/*this.connect(this._propertiesPane, "onPropertiesChanged", dojo_lang.hitch(this, function(){
			if(this.triggerModeValue == ier_constants.EventType_InternalEventTrigger){
				var aggregation = this._propertiesPane.getPropertyValue(ier_constants.Property_Aggregation);
				if(this.aggregation != null && aggregation != this.aggregation){
					this.aggregation = aggregation;
					this._conditionPane.createRendering(this.repository, this.aggregation);
				}
			}
		}));
		
		this.connect(this._propertiesPane, "onCompleteRendering", dojo_lang.hitch(this, function(){
			if(this.triggerModeValue == ier_constants.EventType_InternalEventTrigger){
				this.aggregation = this._propertiesPane.getPropertyValue(ier_constants.Property_Aggregation);
				this._conditionPane.createRendering(this.repository, this.aggregation);
			}
		}));*/
		
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

		var properties = this._propertiesPane.getProperties();
		//var conditions = this._conditionPane.getConditions();
		this.repository.addTrigger(properties, conditions, this.triggerModeValue, dojo_lang.hitch(this, function(){
			if(close)
				this.hide();
		}));

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
