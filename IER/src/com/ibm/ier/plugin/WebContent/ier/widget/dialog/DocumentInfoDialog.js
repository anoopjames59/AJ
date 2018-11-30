define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"dojo/dom-style",
    	"ecm/widget/dialog/EditPropertiesDialog",
    	"ier/messages"
], function(dojo_declare, lang, domClass, domStyle, ecm_dialog_EditPropertiesDialog, ier_messages){

/**
 * @name ier.widget.dialog.DocumentInfoDialog
 * @class Provides a dialog for document information for IER
 * @augments ecm.widget.dialog.EditpropertiesDialog
 */
var documentInfoDialog = dojo_declare("ier.widget.dialog.DocumentInfoDialog", [ecm_dialog_EditPropertiesDialog], {
	/** @lends ier.widget.dialog.DocumentInfoDialog.prototype */
		
	postCreate: function() {
		this.inherited(arguments);
		this.set("title", ier_messages.DocumentInfoDialog_objectTitile);
		this.setIntroText(ier_messages.DocumentInfoDialog_objectIntro);
	},
	/**
	 * Show the document information dialog.
	 */
	show: function(item, callback, teamspace) {
		this._item = item;

		if (this._item) {
			this._callback = callback;
			this._itemEditPane._editPropertiesDialog = this;
			this._itemEditPane.setItem(item, lang.hitch(this, function() {
				//this._viewContentButton.set("disabled", !this._canViewItem(this._item));
				if (this._canViewItem(this._item)) {
					domClass.remove(this._viewContentButton.domNode, "dijitHidden");
				} else {
					domClass.add(this._viewContentButton.domNode, "dijitHidden");
				}
				this.inherited("show", []);				
				domStyle.set(this._breadcrumbContainer.domNode, "display", "none");
				domStyle.set(this._saveButton, "display", "none");
				this.resize();
			}), teamspace);
		}
	}
});

return documentInfoDialog;
});
