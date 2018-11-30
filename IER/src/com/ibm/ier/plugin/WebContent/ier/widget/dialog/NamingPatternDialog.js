define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ier/constants",
    	"ier/messages",
    	"ier/model/ResultSet",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/NamingPatternDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"dijit/layout/TabContainer", // in content
    	"ier/widget/panes/EntityItemPropertiesPane", // in content
    	"ier/widget/panes/EntityItemSecurityPane", // in content
    	"ier/widget/panes/NamingPatternLevelPane" // in content
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_model_ResultSet, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.NamingPatternDialog
 * @class Provides an interface to add naming patterns
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.NamingPatternDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.NamingPatternDialog.prototype */

	title: ier_messages.namingPatternDialog_title,
	contentString: contentString,

	postCreate: function(){
		this.inherited(arguments);

		this.setIntroText(this._ierMessages.namingPatternDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh02.htm"));
		this.setResizable(true);
		
		this.addChildPane(this._entityItemPropertiesPane);
		this.addChildPane(this._levelPane);
		this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
		this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, false);
	},

	/**
	 * Shows the NamingPatternDialog
	 * @param repository
	 */
	show: function(repository, item){
		this.inherited(arguments);
		
		this.logEntry("show()");

		this.repository = repository;
		this.item = item;
		if(!item){
			this.set("title", this._ierMessages.namingPatternDialog_add_title);
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
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {				
			this._entityItemPropertiesPane.resize();
			this.resize();
		});

		this.resize();

		this.logExit("show()");
	},

	validateInput: function() {
		var valid = this.inherited(arguments);
		this._applyButton.set("disabled", !valid);
		return valid;
	},

	/**
	 * Render the dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");

		//renders the other panes
		this._entityItemPropertiesPane.createRendering({
			repository: this.repository,
			rootClassId : ier_constants.ClassName_NamingPattern, 
			defaultNameProperty: ier_constants.Property_PatternName, 
			entityType: ier_constants.EntityType_NamingPattern,
			hideContentClassSelector: true,
			item: this.item
		});

		//Sets up and renders the security pane after the property pane has finished rendering
		this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {			
			this._entityItemPropertiesPane.resizeCommonProperties();
			this.resize();
			this.validateInput();
		});

		var patternId = (this.item && this.item.id);
		if(patternId){
			this.repository.getNamingPatternLevels(patternId, dojo_lang.hitch(this, function(levels){
				this._levelPane.set("repository", this.repository);
				this._levelPane.set("levels", levels);
			}));
		}else{
			this._levelPane.set("repository", this.repository);
			this._levelPane.set("levels", []);
		}

		this.logExit("_renderDialog");
	},

	/**
	 * Event invoked when a naming pattern is saved
	 * @param items
	 */
	onAdd: function(items){
		//event
	},

	_save: function(close){
		if(this.validateInput()){
			var properties = this._entityItemPropertiesPane.getProperties();
			var patternLevels = this._levelPane.get("levels");
			var patternId = (this.item && this.item.id);
			this.repository.saveNamingPattern(properties, patternLevels, patternId, dojo_lang.hitch(this, function(resultSet){
				this.onAdd(resultSet.getItems());
				if(close){
					this.hide();
				}
			}));
		}
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
