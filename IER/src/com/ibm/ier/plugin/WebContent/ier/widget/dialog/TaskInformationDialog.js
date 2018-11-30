define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/TaskInformationDialogContent.html",
    	"ier/widget/tasks/DDSweepTaskInformationPane" // in content
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.TaskErrordialog
 * @class Provides a dialog to display a task error
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.TaskInformationDialog", [ier_dialog_IERBaseDialog], {	
	/** @lends ier.widget.dialog.TaskErrordialog.prototype */

	title: ier_messages.taskPane_previewTitle,
	contentString: contentString,

	item: null,
	
	postCreate: function(){
		this.inherited(arguments);

		this.setResizable(true);
		this.addChildPane(this._infoPane);;
	},

	/**
	 * Shows the Task Error Dialog
	 * @param repository
	 */
	show: function(){
		this.inherited(arguments);
		
		this.logEntry("show()");
		
		this._infoPane.setItem(this.item);
		this.resize();

		this.logExit("show()");
	}
});
});
