define([
	"dojo/_base/declare",
	"dojo/dom-class",
	"ier/messages",
	"ier/widget/dialog/IERBaseDialog",
	"dojo/text!./templates/AboutDialogContent.html"
], function(dojo_declare, dojo_class, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.AboutDialog
 * @class Provides a dialog describing the application, including version information.
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.AboutDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.AboutDialog.prototype */

	contentString: contentString,

	postCreate: function() {
		this.inherited(arguments);
		dojo_class.add(this.domNode, "ecmAboutDialog");
		this.buildLevel.appendChild(document.createTextNode(ier_messages.build_version));
		this.buildDate.appendChild(document.createTextNode(ier_messages.build_date));
		this.setTitle(ier_messages.about_dialog_title);
		this.setWidth(600);
	}
});});
