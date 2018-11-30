define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/string",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ecm/widget/dialog/ConfirmationDialog",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/model/ResultSet",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/ObjectStoreSecurityDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"idx/layout/TitlePane", // in content
    	"ier/widget/panes/EntityItemObjectStoreSecurityPane" // in content
], function(dojo_declare, dojo_lang, dojo_string, ecm_model_desktop, ecm_model_Request, ecm_widget_dialog_ConfirmationDialog, ier_constants, ier_messages, ier_util, ier_model_ResultSet, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.ObjectStoreSecurityDialog
 * @class Provides an interface to edit security of a repository
 * @augments ecm.widget.dialog.BaseDialog
 */
return dojo_declare("ier.widget.dialog.ObjectStoreSecurityDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.ObjectStoreSecurityDialog.prototype */

	contentString: contentString,
	_messages: ier_messages,
	
	showWarning: false,
	

	postCreate: function() {
		this.inherited(arguments);
		this.addChildPane(this._entityItemSecurityPane);
		this._runButton = this.addButton(ier_messages.baseDialog_runButton, "_onClickRun", true, true);
	},

	/**
	 * Shows the AddFilePlanDialog
	 * @param repository
	 */
	show: function(repository) {
		this.inherited("show", arguments);
		
		this.logEntry("show()");
		
		this.repository = repository;
		
		if(this.showWarning){
			this.setMessage(dojo_string.substitute(ier_messages.noSecurityRunDate_available, [this.repository.name]), "error");
		}
		
		if(!this.repository || this.repository.isIERLoaded()){
			this._renderDialog();
		}
		else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}
		
		this.resize();
		
		this.logExit("show()");
	},
	
	/**
	 * Render the dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");

		this.set("title", ier_messages.objectStoreSecurityDialog_title);

		this.setIntroText(ier_messages.objectStoreSecurityDialog_description);

		this.setResizable(true);

		//renders the security pane
		this._entityItemSecurityPane.createRendering(this.repository, null, null, null, null, this._isReadOnly);

		this.resize();
		this.validateInput();
		
		this.logExit("_renderDialog");
	},

	_saveSecurity: function() {
		this.logEntry("_saveSecurity");

		if(!this.confirmRunSecurity){
			this.confirmRunSecurity = new ecm_widget_dialog_ConfirmationDialog({
				text: ier_messages.objectStoreSecurityDialog_confirmation,
				buttonLabel: ier_messages.baseDialog_runButton,
				onExecute: dojo_lang.hitch(this, function(){
					this._entityItemSecurityPane.saveSecurity(dojo_lang.hitch(this, function(response){
						this.onFinished(response);
						this.onCancel();
					}));
				})
			});
		}
		
		this.confirmRunSecurity.show();

		this.logExit("_saveSecurity");
	},
	
	destroy: function(){
		if(this.confirmRunSecurity)
			this.confirmRunSecurity.destroy();
		
		this.inherited(arguments);
	},
	
	/**
	 * Event thrown if the security was set successfully
	 */
	onFinished: function(response){
		
	},

	_onClickRun: function() {
		this.logEntry("_onClickRun");
		
		if (this.validateInput()) {
			this._saveSecurity();
		}
		
		this.logExit("_onClickRun");
	}
});});
