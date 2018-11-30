define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/dialog/IERBaseDialog",
    	"dojo/text!./templates/RelocateContainerDialogContent.html",
    	"dijit/layout/BorderContainer", // in template
    	"dijit/layout/ContentPane", // in template
    	"idx/layout/TitlePane", // in template
    	"ier/widget/panes/RelocateContainerGeneralPane" // in template
], function(dojo_declare, dojo_lang, dojo_class, ecm_model_Request,
		ier_constants, ier_messages, ier_util, ier_widget_dialog_IERBaseDialog, contentString){
	
/**
 * @name ier.widget.dialog.RelocateRecordContainerDialog
 * @augments ecm.widget.dialog.IERBaseDialog
 */
return dojo_declare("ier.widget.dialog.RelocateRecordContainerDialog", [ier_widget_dialog_IERBaseDialog], {
	/** @lends ier.widget.dialog.RelocateRecordContainerDialog.prototype */
	contentString: contentString,
	widgetsInTemplate: true,
	_container: null,
	_fp_repository_location: null, // {String}

	/**
	 * Constructor function
	 */
	constructor: function() {
	},

	/**
	 * Actions to perform after the dialog is created.  In this case we
	 * are creating the child panes that are used on this dialog and
	 * adding them to this.  Also setup OK button.
	 */
	postCreate: function() {
		this.inherited(arguments);
		dojo_class.add(this.domNode, "ierSmallDialog");
		this.addChildPane(this._relocateContainerGeneralPane);
		this.okButton = this.addButton(ier_messages.relocateRecordContainerDialog_relocateButton, "_onClickRelocate", false, true);
	},

	/**
	 * Shows the relocate container dialog.
	 * 
	 * @param repository -
	 *            repository to relocate category.
	 * @param items - The items that were selected to be relocated
	 */
	show: function(repository, items) {
		this.logEntry("show()");
		this.inherited("show", []);

		this.repository = repository;
		this.items = items;
		if(this.repository.isIERLoaded()){
			this._renderDialog();
		}
		else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._renderDialog();
			}));
		}

		this._items = items;
		this.logExit("show()");		
	},

	/**
	 * Render the dialog
	 */
	_renderDialog: function(){
		this.logEntry("_renderDialog");
		
		// Setup intro text, title and more link
		this.setTitle(ier_messages.relocateRecordContainerDialog_title);		
		this.setIntroText(ier_messages.relocateRecordContainerDialog_description);
		this.setIntroTextRef(ier_messages.dialog_LearnMoreLink, this.getHelpUrl("frmovh06.htm"));
		
		// Button is disabled until required fields are entered
		this.okButton.set("disabled", true);

		//renders the general pane
		this._relocateContainerGeneralPane.createRendering(this.repository, this, this.items);
		this.resize();
		this.validateInput();
		this.logExit("_renderDialog");
	},


	/**
	 * Perform the relocate action.  Gather up the parameters and call the service
	 * to perform the relocation.
	 */
	_onClickRelocate: function() {	
		this.logEntry("onClickRelocate()");		
		if (this.validateInput()) {	
			// we only support relocating a single container so there should 
			// only be one item in the items array
			// We could support multiple, but could be a performance hit if each of the
			// selected containers has a large number of children
			var items = this._items;		
			var item = items[0];
			
			var serviceParams = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
				var parent = item.parent;				
				if (parent)
					parent.refresh();
				var targetContainer = this._relocateContainerGeneralPane.getTargetContainer();

				this.repository.retrieveItem(targetContainer, dojo_lang.hitch(this, function(itemRetrieved){
					if (itemRetrieved)
						itemRetrieved.refresh();
					this.onCancel();
				}));				
			}));
			serviceParams.requestParams[ier_constants.Param_ContainerId] = item.id;
			serviceParams.requestParams[ier_constants.Param_ReasonForRelocate] = this._relocateContainerGeneralPane.getReasonForRelocate();
			serviceParams.requestParams[ier_constants.Param_DestinationContainer] = this._relocateContainerGeneralPane.getTargetContainer();
			
			// Call the relocate service
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_Relocate, ier_constants.PostEncoding, serviceParams);
		}
		this.logExit("onClickRelocate()");
	} 
});});
