define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/TaskErrorDialogContent.html",
    	"ier/widget/tasks/TaskErrorPane" // in content
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.TaskErrordialog
 * @class Provides a dialog to display a task error
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.TaskErrorDialog", [ier_dialog_IERBaseDialog], {	
	/** @lends ier.widget.dialog.TaskErrordialog.prototype */

	title: ier_messages.taskPane_previewErrors,
	contentString: contentString,

	item: null,
	
	postCreate: function(){
		this.inherited(arguments);

		this.setResizable(true);
		this.addChildPane(this._errorPane);;
	},

	/**
	 * Shows the Task Error Dialog
	 * @param repository
	 */
	show: function(){
		this.inherited(arguments);
		
		this.logEntry("show()");
		
		this._errorPane.createRendering(this.item);
		this.resize();

		this.logExit("show()");
	}
});
});
