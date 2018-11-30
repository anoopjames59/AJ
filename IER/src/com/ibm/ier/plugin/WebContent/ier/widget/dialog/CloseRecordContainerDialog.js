define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/Messages",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/CloseRecordContainerDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"ecm/widget/ValidationTextBox" // in content
], function(dojo_declare, dojo_lang, dojo_class, ecm_messages, ier_constants, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.CloseRecordContainerDialog
 * @class
 * @augments dijit.Dialog
 */
return dojo_declare("ier.widget.dialog.CloseRecordContainerDialog", [ier_dialog_IERBaseDialog], {

	contentString: contentString,
	widgetsInTemplate: true,
	_container: null,

	constructor: function() {
	},

	postCreate: function() {
		this.inherited(arguments);
		dojo_class.add(this.domNode, "ierVerySmallDialog");
		this.okButton = this.addButton(ier_messages.closeRecordContainerDialog_closeButton, "_onClickClose", false, true);
	},

	/**
	 * Shows the dialog.
	 * 
	 * @param repository -
	 *            repository to close category.
	 * @param callback -
	 *            callback function when container is closed.
	 */
	show: function(repository, items) {
		this.inherited("show", []);
		this.setTitle(ier_messages.closeRecordContainerDialog_title);		
		this.setIntroText(ier_messages.closeRecordContainerDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh08.htm"));
		
		this.okButton.set("disabled", true);
		this.repository = repository;
		this._items = items;
		
		this._reasonForClose.set("missingMessage", ecm_messages.property_missingMessage);
		//this._getReasonLength(repository);
		
		var promptMessage = ecm_messages.properties_type_string_tooltip;
		promptMessage += ", " + ecm_messages.properties_maxLength_tooltip + 64;
	},

	_onNameChange: function() {
		this.okButton.set("disabled", this._reasonForClose.get("value").length > 0 ? false : true);
	},
	
	_getReasonLength: function(repository){
		var contentClass = repository.getContentClass(ier_constants.ClassName_RecordCategory);
		contentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(attributeDefinitions){
			for ( var i in attributeDefinitions) {
				var attributeDefinition = attributeDefinitions[i];
				if (attributeDefinition.id == ier_constants.Property_ReasonForClose) {
					this._reasonForClose.set("maxLength", attributeDefinition.maxLength);
					break;
				}
			}

		}));
	},
	
	
	_onClickClose: function() {		
		var reasonForClose = this._reasonForClose.get("value");	
		var items = this._items;		
		for (var i in items) {									
			items[i].close(reasonForClose, dojo_lang.hitch(this, function(recordContainer){
				recordContainer.onChange([recordContainer]);
				this.onCancel(); 		
			}));
		}
	}
});});
