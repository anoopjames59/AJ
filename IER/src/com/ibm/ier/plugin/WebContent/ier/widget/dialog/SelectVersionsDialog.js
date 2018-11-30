define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/model/Request",
    	"ier/messages",
    	"ier/util/util",
    	"ier/constants",
    	"ier/model/ResultSet",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/SelectVersionsDialogContent.html",
    	"dijit/layout/ContentPane", // in content
    	"ier/widget/listView/ContentList" // in content
], function(dojo_declare, dojo_lang, dojo_class, Request, ier_messages, ier_util, ier_constants, ResultSet, ier_dialog_IERBaseDialog, contentString){

/**
 * @name ier.widget.dialog.SelectVersionsDialog
 * @class Provides an interface to select versions of an entity item
 * @augments ier.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.SelectVersionsDialog", [ier_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.SelectVersionsDialog.prototype */

	contentString: contentString,
	selectedObject: null,

	postCreate: function(){
		this.inherited(arguments);
		dojo_class.add(this.domNode, "ierMediumDialog");

		this.set("title", ier_messages.selectVersionDialog_title);
		this.setResizable(true);
		
		this.workflowVersionsList.multiSelect = false;

		this._selectButton = this.addButton(ier_messages.baseDialog_select, "_onClickSelect", true, true);
	},
		
	/**
	 * Shows the select version dialog
	 * @param repository
	 */
	show: function(item, repository){
		this.inherited("show", []);
		
		this.logEntry("show()");
		
		this.item = item;
		this.repository = repository;
		
		this._getWorkflowVersions();
		
		this.logExit("show()");
	},
	
	_setupGridConnections: function(){
		if(this._onSelectionChangeHandler){
			this.disconnect(this._onSelectionChangeHandler);
		}
		
		if(this._onRowDblClickHandler){
			this.disconnect(this._onRowDblClickHandler);
		}
		
		this._onSelectionChangeHandler = this.connect(this.workflowVersionsList, "onRowSelectionChange", function(selectedItems) {
			this.selectedObject = selectedItems[0];
			this.validateInput();
		});
		
		this._onRowDblClickHandler = this.connect(this.workflowVersionsList, "onRowDblClick", function(item) {
			this.onSelected(item);
			this.validateInput();
			this.hide();
		});
	},
	
	_getWorkflowVersions: function(){
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
			var resultSet = new ResultSet(response);
			this.workflowVersionsList.setResultSet(resultSet);
			this._setupGridConnections();
		}));
		
		params.requestParams[ier_constants.Param_EntityId] = this.item.id;
		Request.postPluginService(ier_constants.ApplicationPlugin, "ierGetWorkflowVersions", ier_constants.PostEncoding, params); 
	},
	
	/**
	 * Event invoked when a version item is selected
	 * @param item
	 */
	onSelected: function(item){
		
	},
	
	/**
	 * Performs the validation for the pane and returns whether the pane validated successfully or not.  Should be overriden by subclasses to do validation for the pane. 
	 * @returns {Boolean}
	 */
	validateInput: function() {
		if(this._selectButton)
			this.setButtonEnabled(this._selectButton, this.selectedObject != null);
	},
	
	_onClickSelect: function(){
		this.logEntry("_onClickSelect");
		
		this.onSelected(this.selectedObject);
		this.hide();

		this.logExit("_onClickSelect");
	}

});});
