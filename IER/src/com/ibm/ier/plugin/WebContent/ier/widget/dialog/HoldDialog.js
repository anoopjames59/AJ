define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/_base/array",
    	"dojo/dom-class",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/dialog",
    	"ier/widget/dialog/IERBaseDialog",
    	"ecm/widget/dialog/MessageDialog",
    	"dojo/text!./templates/HoldDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"dijit/layout/TabContainer", // in content
    	"ier/widget/panes/EntityItemPropertiesPane", // in content
    	"ier/widget/panes/HoldConditionPane" // in content
], function(dojo_declare, dojo_lang, dojo_array, dojo_class, ier_constants, ier_messages, ier_util_dialog, ier_dialog_IERBaseDialog, ecm_dialog_MessageDialog, contentString){

/**
 * @name ier.widget.dialog.HoldDialog
 * @class Provides an interface to add hold
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.HoldDialog", [ier_dialog_IERBaseDialog], {	
	/** @lends ier.widget.dialog.HoldDialog.prototype */

	title: ier_messages.holdDialog_title,
	contentString: contentString,
	_blockEditHold: false,

	postCreate: function(){
		this.inherited(arguments);

		dojo_class.add(this.domNode, "ierHoldDialog");
		this.setIntroText(ier_messages.holdDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh19.htm"));
		this.setResizable(true);

		this.addChildPane(this._propertiesPane);
		this.addChildPane(this._conditionPane);
		this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", false, true);
		this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", false, false);

		//resizes the pane after the property pane has finished rendering
		this.connect(this._propertiesPane, "onCompleteRendering", function(){
			this._propertiesPane.resize();
			this.resize();
		});
	},

	/**
	 * Shows the HoldDialog
	 * @param repository
	 */
	show: function(repository, item){
		this.inherited(arguments);

		this.logEntry("show()");

		this.repository = repository;
		this.item = item;
		if(!item){
			this.set("title", this._ierMessages.holdDialog_add_title);
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
		
		this.resize();

		this.logExit("show()");
	},

	validateInput: function(){
		if(this._blockEditHold){
			this._applyButton.set("disabled", true);
			this._saveButton.set("disabled", true);
			return false;
		}
		var valid = this.inherited(arguments);
		if(valid && this._conditionPane){
			valid = this._conditionPane.isValid();
		}
		this._applyButton.set("disabled", !valid);
		this._saveButton.set("disabled", !valid);
		return valid;
	},

	_renderPropertiesPane: function(isReadOnly){
		this._propertiesPane.createRendering({
			repository: this.repository,
			rootClassId : ier_constants.ClassName_Hold, 
			defaultNameProperty: ier_constants.Property_HoldName, 
			entityType: ier_constants.EntityType_Hold,
			hideContentClassSelector: true,
			isReadOnly: isReadOnly,
			item: this.item
		});
	},

	_renderDialog: function(){
		this.logEntry("_renderDialog");

		var readOnly = true;
		if(this.item){
			var sweepState = this.item.attributes["SweepState"];
			if(sweepState == 0){
				readOnly = false;
			}else if(sweepState == 1){
				var sweepDate = this.item.attributes["LastHoldSweepDate"];
				if(sweepDate == null || sweepDate == ""){
					readOnly = false;
				}
			}
		}else{
			readOnly = false;
		}

		var holdId = (this.item && this.item.id);
		if(holdId){
			this.repository.getObjectConditions(holdId, ier_constants.EntityType_Hold, dojo_lang.hitch(this, function(conditions){
				this._conditionPane.set("repository", this.repository);
				this._conditionPane.set("conditions", conditions);
				this._blockEditHold = this._checkMetadata(conditions);
				if(!this._blockEditHold){
					this._blockEditHold = this._haveMixedOperators(conditions);
				}
				if(readOnly || this._blockEditHold){
					this._conditionPane.setReadOnly();
				}
				this._renderPropertiesPane(this._blockEditHold);
				if(this._blockEditHold){
					this._applyButton.set("disabled", true);
					this._saveButton.set("disabled", true);
					this._conditionPane._previewButton1.set("disabled", true);

					this._messageDialog && this._messageDialog.destroy();
					this._messageDialog = new ecm_dialog_MessageDialog({
						text: ier_messages.commonConditionForm_metadataContentSearch
					});
					this._messageDialog.startup();
					this._messageDialog.show();
					ier_util_dialog.manage(this._messageDialog);
				}
			}));
		}else{
			this._conditionPane.set("repository", this.repository);
			this._conditionPane.set("conditions", []);
			this._renderPropertiesPane(false);
		}

		this.logExit("_renderDialog");
	},

	_checkMetadata: function(conditions){
		return dojo_array.some(conditions, dojo_lang.hitch(this, function(con){
			return con.className == ier_constants.ClassName_Record && con.content && con.content.type == "METADATA";
		}));
	},

	_haveMixedOperators: function(conditions){
		return dojo_array.some(conditions, dojo_lang.hitch(this, function(con){
			return con.criteria && con.className == ier_constants.ClassName_Record && con.content 
					&& con.content.type == "CONTENT" && con.content.matchAll != con.matchAll;
		}));
	},

	_save: function(close){
		var properties = this._propertiesPane.getProperties();
		var conditions = this._conditionPane.get("conditions");
		var holdId = (this.item && this.item.id);
		this.repository.saveHold(this.item, properties, conditions, holdId, dojo_lang.hitch(this, function(){
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
