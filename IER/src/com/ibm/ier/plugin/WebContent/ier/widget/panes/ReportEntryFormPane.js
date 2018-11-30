define([
    	"dojo/_base/declare",
    	"dojo/dom-construct",
    	"dojo/dom-class",
    	"dojo/dom-style",
    	"dojo/_base/lang",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/widget/ReportEntryForm",
    	"dojo/text!./templates/IERBaseDialogPane.html"
], function(dojo_declare, dojo_construct, dojo_class, dojo_style, dojo_lang, ier_constants, ier_messages, ier_widget_dialog_IERBaseDialogPane, ier_widget_ReportEntryForm, 
		templateString){

/**
 * The pane that presents a report entry form.  It is used by the report scheduler wizard to enter parameters for selected reports
 * 
 * @name ier.widget.ReportEntryFormPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.ReportEntryFormPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.ReportEntryFormPane */

	templateString: templateString,
	constants: ier_constants,
	messages: ier_messages,
	
	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		this.title = ier_messages.scheduleReportDialog_setParametersForReport;
		this.disabled = true;
		
		this.reportEntryForm = new ier_widget_ReportEntryForm({
			showActionBar: false
		});
		
		this.connect(this.reportEntryForm, "onChange", function(){
			this.onInputChange();
		});
		
		this.connect(this.reportEntryForm, "onCompleteRendering", function(attributes, deferArray) {
			this.onCompleteRendering();
		});
		
		this.connect(this.reportEntryForm, "onRenderAttributes", function(attributes, deferArray) {
			this.onRenderAttributes(attributes, deferArray);
		});
		
		this.logExit("postCreate()");
	},

	/**
	 * Creates the rendering for the pane
	 * @param repository
	 */
	createRendering: function(repository, reportDefinition) {
		this.logEntry("createRendering()");
		
		this.reportDefinition = reportDefinition;
		
		dojo_construct.place(this.reportEntryForm.domNode, this.containerNode, "first");
		dojo_style.set(this.reportEntryForm.mainStackContainer.domNode, "overflow-y", "auto");
		dojo_style.set(this.reportEntryForm.resultsPane.domNode, "display", "none");
		
		this.repository = repository;
		this.reportEntryForm.createRendering(this.repository, reportDefinition.id, reportDefinition, dojo_lang.hitch(this, function(){
			this.reportEntryForm.resize();
			this.resize();
		}));
	
		this.logExit("createRendering()");
	},
	
	/**
	 * Event invoked when the pane has completed rendering
	 */
	onCompleteRendering: function(){
		
	},
	
	/**
	 * Event invoked when report entry form attributes are about to be rendered
	 */
	onRenderAttributes: function(attributes, deferArray){
		
	},
	
	/**
	 * Returns the properties json generated in the report entry form
	 */
	getPropertiesJSON: function() {
		return this.reportEntryForm.getPropertiesJSON();
	},
	
	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		if(this.reportEntryForm)
			return this.reportEntryForm.validateInput();
		else
			return false;
	}
});});