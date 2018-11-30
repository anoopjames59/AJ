define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/PropertyDialogContent.html",
    	"dijit/layout/TabContainer", // in content
    	"ier/widget/panes/EntityItemPropertiesPane" // in content
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.LocationDialog
 * @class Provides an interface to add location
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.LocationDialog", [ier_dialog_IERBaseDialog], {	
	/** @lends ier.widget.dialog.LocationDialog.prototype */

	title: ier_messages.locationDialog_title,
	contentString: contentString,

	postCreate: function(){
		this.inherited(arguments);

		this.setIntroText(ier_messages.locationDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh09.htm"));
		this.setResizable(true);
		this.addChildPane(this._propertiesPane);
		this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
		this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, false);
	},

	/**
	 * Shows the LocationDialog
	 * @param repository
	 */
	show: function(repository, item){
		this.inherited(arguments);
		
		this.logEntry("show()");
		
		this.repository = repository;
		this.item = item;
		if(!item){
			this.set("title", this._ierMessages.locationDialog_add_title);
			this._saveButton.set("label", this._ierMessages.baseDialog_addButton);
			this._applyButton.domNode.style.display = "none";
		}

		if(this.repository.isIERLoaded()){
			this._renderDialog();
		}else{
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}
		
		//resizes the pane after the property pane has finished rendering
		this.connect(this._propertiesPane, "onCompleteRendering", function() {				
			this._propertiesPane.resize();
			this.resize();
		});
		
		//Connects and change existing attributes for the property pane before they are rendered
		this.connect(this._propertiesPane, "onRenderAttributes", function(attributes, deferArray) {
			this._propertiesPane.setReviewerAndCurrentDate(attributes, deferArray);
		});

		this.resize();

		this.logExit("show()");
	},
	
	validateInput: function() {
		var valid = this.inherited(arguments);
		this._applyButton.set("disabled", !valid);
		return valid;
	},

	_renderDialog: function(){
		this.logEntry("_renderDialog");

		this._propertiesPane.createRendering({
			repository: this.repository,
			rootClassId : ier_constants.ClassName_Location, 
			defaultNameProperty: ier_constants.Property_LocationName, 
			entityType: ier_constants.EntityType_Location,
			hideContentClassSelector: true,
			changeAttributesIndicator: true,
			item: this.item
		});

		this.logExit("_renderDialog");
	},

	_save: function(close){
		var properties = this._propertiesPane.getProperties();
		var locationId = (this.item && this.item.id);
		this.repository.saveLocation(properties, locationId, dojo_lang.hitch(this, function(){
			if(close){
				this.hide();
			}
		}));
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

});
});
