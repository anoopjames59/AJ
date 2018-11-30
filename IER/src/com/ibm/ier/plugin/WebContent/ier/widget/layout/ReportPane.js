define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/event",
	"dojo/dom-style",
	"dojo/dom-construct",
	"dojo/dom-class",
	"ier/widget/TilesList",
	"ier/constants",
	"ier/messages",
	"ier/widget/layout/ReportsFlyoutPane",
	"dojo/text!./templates/ReportPane.html",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane" // in template
], function(dojo_declare, dojo_lang, dojo_event, dojo_domStyle, dojo_domConstruct, dojo_domClass, ier_widget_TilesList,
		ier_constants, ier_messages, ier_widget_layout_ReportsFlyoutPane, templateString){

/**
 * @name ReportPane
 * @class Implement report pane with a list of reports that users can initiate
 * @augments ecm.widget.layout.LaunchBarDialogPane
 */
return dojo_declare("ier.widget.layout.ReportPane", [ier_widget_layout_ReportsFlyoutPane], {
	/** @lends ier.widget.layoutReportsListPane.prototype */

	widgetsInTemplate: true,
	templateString: templateString,
	
	createTileList: function(){
		return new ier_widget_TilesList({
			hideFilterBox: true
		});
	}
});});
