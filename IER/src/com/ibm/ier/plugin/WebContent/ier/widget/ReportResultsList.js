define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/event",
	"dojo/data/ItemFileReadStore",
	"dojo/date/locale",
	"dojo/dom-style",
	"dojo/dom-construct",
	"dojo/dom-class",
	"dojo/date/stamp",
	"dijit/_Widget",
	"dijit/_TemplatedMixin", 
	"dijit/_WidgetsInTemplateMixin",
	"ecm/LoggerMixin",
	"ecm/widget/dialog/MessageDialog",
	"ecm/model/Desktop",
	"ier/constants",
	"ier/messages",
	"dojo/text!./templates/ReportResultsList.html",
	"dijit/layout/ContentPane", // in template
	"ier/widget/TilesList" //in template
], function(dojo_declare, dojo_lang, dojo_event, dojo_data_ItemFileReadStore, dojo_date_locale, dojo_domStyle, dojo_domConstruct, dojo_domClass, dojo_date_stamp, dijit_Widget, dijit_TemplatedMixin,
		dijit_WidgetsInTemplateMixin, ecm_LoggerMixin, ecm_widget_dialog_MessageDialog, ecm_model_Desktop, ier_constants, ier_messages, templateString){

/**
 * @name ReportResultsList
 * @class Implement report results list
 * @augments ecm.widget.layout.LaunchBarDialogPane
 */
return dojo_declare("ier.widget.layout.ReportResultsList", [dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	/** @lends ier.widget.layoutReportsListPane.prototype */

	widgetsInTemplate: true,
	templateString: templateString,

	/**
	 * Handle to the ier.messages object
	 */
	messages: null,	
	store: null,
	repository: null,

	/**
	 * postCreate()
	 */
	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);
				
		this.connect(ecm_model_Desktop, "onAddReportResults", dojo_lang.hitch(this, function(reportResult){
			this.loadReportResults();
		}));
		
		this.connect(ecm_model_Desktop, "onRemoveReportResults", dojo_lang.hitch(this, function(reportId, reportResult){
			this.loadReportResults();
		}));
		
		this.connect(this._tileList, "onItemSelected", dojo_lang.hitch(this, function(){
			this.onItemSelected();
		}));

		this._tileList.sortAttributeKey = [{ attribute: "rpt_create_date", descending: true}];
		this.logExit("postCreate");

	},
	
	/**
	 * Loads the content of the pane
	 */
	loadContent: function() {
		this.loadReportResults(dojo_lang.hitch(this, function(){
			this.onResultsLoaded(this.getReportResultItems());
		}));
	},
	
	/**
	 * Clears the results list
	 */
	clearContent: function() {
		this._tileList.clearList();
	},
	
	/**
	 * Retrieve all the report result items
	 */
	getReportResultItems: function() {
		return this._tileList.getItems();
	},
	
	/**
	 * Event invoked when results are loaded
	 */
	onResultsLoaded: function(items){
		
	},
	
	/**
	 * Event invoked when an item is selected
	 */
	onItemSelected: function(selectedItem)
	{
		
	},
	
	resize: function(){
		this._tileList.resize();
	},
	
	getSelectedItem: function(){
		return this._tileList.getSelectedItem();
	},

	/**
	 * Loads the report results and sets the results to the tile list widget
	 */
	loadReportResults: function(onComplete) {
		ecm_model_Desktop.getReportResults(dojo_lang.hitch(this, function(reportResults){
			var items = [];
			for (var i in reportResults) {
				var counter = 0;
				var reportResult = reportResults[i];
				var title = reportResult.rpt_title ? reportResult.rpt_title : ier_messages.reportPane_untitledTitle;
				var createDate = dojo_date_locale.format(dojo_date_stamp.fromISOString(reportResult.rpt_create_date),{
				    selector: "date",
				    datePattern: "MMMM d yyyy, h:m a"
				});

				reportResult.id =  this.id + reportResult.resultId + counter;
				reportResult.reportResultId = reportResult.resultId;
				reportResult.reportTitle = reportResult.rpt_title ? reportResult.rpt_title : null;
				reportResult.content = "<b>" + ier_messages.reportPane_createDate + "</b> : " + createDate,
				reportResult.title = title;
				reportResult.name = title;
				reportResult.actionMenu = ier_constants.MenuType_IERReportResultsContextMenu;

				items.push(reportResult);
				
				counter++;
			}
			
			var store = new dojo_data_ItemFileReadStore({
				data : {identifier : "id", items : items}
			});
			this._tileList.setStore(store);
			
			if(onComplete){
				onComplete(items);
			}
		}));
	}
});});
