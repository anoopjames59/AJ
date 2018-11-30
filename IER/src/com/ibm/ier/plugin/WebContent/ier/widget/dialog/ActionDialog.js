define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ier/constants",
	"ier/messages",
	"ier/widget/dialog/IERBaseDialog",
	//"dojo/text!./templates/AddDialogContent.html",
	"dojo/text!./templates/PropertyDialogContent.html",
	"dijit/layout/ContentPane", // in content
	"idx/layout/TitlePane", // in content
	"ier/widget/panes/EntityItemPropertiesPane" // in content
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.ActionDialog
 * @class Provides an interface to show properties of an action
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.ActionDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.AddActionDialog.prototype */

	contentString: contentString,

	postCreate: function(){
		this.inherited(arguments);

		this.set("title", ier_messages.addActionDialog_title);
		this.setIntroText(ier_messages.addActionDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl());
		this.setResizable(true);

		this.addChildPane(this._propertiesPane);
		this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, true);
		this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
		
		//resizes the pane after the property pane has finished rendering
		this.connect(this._propertiesPane, "onCompleteRendering", function() {				
			this._propertiesPane.resizeCommonProperties();
			this.resize();
		});
	},
		
	/**
	 * Shows the AddActionDialog
	 * @param repository
	 */
	show: function(repository, item){
		this.inherited("show", []);
		
		this.logEntry("show()");
		this.item = item;
		if(!item){
			this.set("title", this._ierMessages.addActionDialog_editTitle);
			this._saveButton.set("label", this._ierMessages.baseDialog_addButton);
			this._applyButton.domNode.style.display = "none";
		}
		
		this.repository = repository;
		if(this.repository.isIERLoaded()){
			this._renderDialog();
		}else{
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
			rootClassId : ier_constants.ClassName_DispositionAction, 
			defaultNameProperty: ier_constants.Property_ActionName, 
			entityType: ier_constants.EntityType_DispositionAction,
			hideContentClassSelector: true,
			item: this.item,
			isReadOnly : true
		});

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
		this.repository.addAction(properties, dojo_lang.hitch(this, function(){
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
