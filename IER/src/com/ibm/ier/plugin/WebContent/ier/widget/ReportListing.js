define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-style",
	"dojo/data/ItemFileWriteStore",
	"dijit/_Widget",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"ecm/LoggerMixin",
	"ier/widget/TilesList",
	"dojo/text!./templates/ReportListing.html"
], function(dojo_declare, dojo_lang, dojo_style, dojo_data_ItemFileWriteStore, dijit_Widget, dijit_TemplatedMixin, 
		dijit_WidgetsInTemplateMixin, ecm_LoggerMixin, ier_widget_TilesList, template) {

/**
 * @name ier.widget.ReportListing
 * @class A widget that provides a listing of reports
 * @augments dijit.Widget
 */
return dojo_declare("ier.widget.ReportListing", [dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	/** @lends ier.widget.layoutReportsListPane.prototype */

	widgetsInTemplate: true,
	
	templateString: template,
	
	/**
	 * Report defintions used in this widget
	 */
	reportDefinitions: null,

	/**
	 * Handle to the ier.messages object
	 */
	messages: null,	
	
	/**
	 * The store holding all the reports
	 */
	store: null,

	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);
		
		//add reports listing
		this.tileList = new ier_widget_TilesList({
			style: "height: 100%, width: 100%",
			hideFilterBox: this.hideFilterBox
		});
		
		this.connect(this.tileList, "onItemSelected", "onItemSelected");
		this.connect(this.tileList, "onListItemDoubleClick", "onItemSelected");
		
		if (this.repository && this.repository.connected){
			this.setRepository(this.repository);
		}
		
		this.domNode.appendChild(this.tileList.domNode);
		this.tileList.resize();
		
		this.logExit("postCreate");

	},
	
	/**
	 * Event invoked when a report is select
	 */
	onItemSelected: function(selectedItem) {
		
	},
		
	/**
	 * Sets the repository and retrieves all the report definitions
	 */
	setRepository: function(repository) {
		this.logEntry("setRepository");
		
		if(repository){
			this.tileList.clearList();
			this.repository = repository;
			if(this.repository.isIERLoaded()){
				this.loadReportListing();
			}
			else {
				this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
					this.loadReportListing();
				}));
			}
		}
		
		this.logExit("setRepository");
	},
	
	/**
	 * Loads and creates the listing of reports
	 */
	loadReportListing: function() {
		if(this.repository.isFilePlanRepository()){
			this.repository.retrieveReportDefinitions(dojo_lang.hitch(this, function(reportDefinitions) {
				this.reportDefinitions = reportDefinitions;
				
				this.onReportDefinitionsRetrieved(reportDefinitions);
				this._loadReportListingCompleted(reportDefinitions);
			}));
		}
	},
	
	/**
	 * Refreshes the listing of report definitions
	 */
	refreshListing: function(){
		if(this.repository.isFilePlanRepository()){
			this.repository.reportDefinitions = [];
			this.loadReportListing();
		}
	},
	
	/**
	 * Event invoked when report definitions have been retrieved
	 */
	onReportDefinitionsRetrieved: function(reportDefinitions){
		
	},
	
	/**
	 * Resizes the widget
	 */
	resize: function(){
		this.tileList.resize();
	},
	
	/**
	 * Clears all report definitions
	 */
	clearList: function(){
		this.tileList.clearList();
	},
	
	/**
	 * @private
	 * When the report listing has been completed, set it into the internal tilelist store
	 */
	_loadReportListingCompleted : function(reportDefinitions) {		
		var tempArray = [];
		for (var i = 0; i < reportDefinitions.length; i++) {
			tempArray.push({
				id : reportDefinitions[i].id,
				fpos : this.repository._objectStoreName,
				content : reportDefinitions[i].getDescription(),
				title : reportDefinitions[i].title,
				name: reportDefinitions[i].title,
				originalItem: reportDefinitions[i]
			});
		}
		
		var store = new dojo_data_ItemFileWriteStore({
			data : {identifier : "id", items : tempArray}
		});
		this.tileList.setStore(store);
		
		if (this.tileList.filter)
			this.tileList.filter.set('value', '');
		this.query = {"title": "*"};
		this.tileList.resize();
		this.tileList.setQuery(this.query);
	}
});});
