define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dojo/aspect",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/widget/layout/ReportsFlyoutPane",
    	"dojo/text!./templates/IERBaseDialogPane.html"
], function(dojo_declare, dojo_lang, dojo_construct, dojo_style, dojo_class, dojo_aspect, ier_constants, ier_messages, ier_widget_dialog_IERBaseDialogPane, 
		ier_widget_ReportsFlyoutPane, templateString){

/**
 * The report listing pane.  It lists all the report definitions available for selection.  It is being used by the scheduler report wizard.
 * 
 * @name ier.widget.panes.ReportListingPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.ReportListingPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.ReportListingPane */

	templateString: templateString,
	
	constants: ier_constants,
	messages: ier_messages,
	
	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		this.title = ier_messages.scheduleReportDialog_selectReport;
		
		this.logExit("postCreate()");
	},
	
	/**
	 * Creates the rendering for the pane
	 * @param repository
	 */
	createRendering: function() {
		this.logEntry("createRendering()");
		
		this.reportPane = new ier_widget_ReportsFlyoutPane({
			hideFilterBox: true,
			style: "height: 100%; width: 100%",
			connectOnItemselected: false
		});
		
		dojo_construct.place(this.reportPane.domNode, this.containerNode, "only");
		dojo_class.remove(this.reportPane.domNode, "ecmFlyoutPane"); 
		dojo_class.remove(this.reportPane.domNode, "ierFlyoutPane");
		
		this.connect(this.reportPane, "onReportDefinitionsRetrieved", function(){
			this.resize();
		});
		
		this.connect(this.reportPane.reportListing, "onItemSelected", dojo_lang.hitch(this, function(reportDefinition){
			this.onItemSelected(reportDefinition);
		}), true);
		
		this.reportPane.loadContent();
		this.resize();
		
		this.logExit("createRendering()");
	},
	
	/**
	 * Event invoked when item is selected
	 */
	onItemSelected: function(reportDefinition){
		
	},
	
	getRepository: function(){
		return this.reportPane.repository;
	},
	
	resize: function(){
		this.inherited(arguments);
		
		if(this.reportPane){
			this.reportPane.reportListing.resize();
			this.reportPane.borderContainer.resize();
		}
	}
});});