define([
	"dojo/_base/declare",
	"dojo/data/ItemFileReadStore",
	"dojo/dom-style",
	"dojo/aspect",
	"dojo/_base/lang",
	"ecm/widget/layout/_LaunchBarPane",
	"ier/widget/TilesList",
	"ecm/model/Request",
	"idx/layout/HeaderPane",
	"ier/messages",
	"dojo/text!./templates/DashboardPane.html"
], function(dojo_declare, dojo_data_ItemFileReadStore, dojo_style, dojo_aspect, dojo_lang, ecm_widget_layout_LaunchBarPane, ecm_widget_TilesList, 
		ecm_model_Request, idx_layout_HeaderPane, ier_messages, templateString){

/**
 * @name ier.widget.layout.DashboardPane
 * @class
 * @augments ecm.widget.layout.LaunchBarPane
 */
return dojo_declare("ier.widget.layout.DashboardPane", [ecm_widget_layout_LaunchBarPane], {
	/** @lends ier.widget.layout.DashboardPane.prototype */

	templateString: templateString,
	_messages: ier_messages,
	widgetsInTemplate: true,

	postCreate: function() {
		this.logEntry("postCreate");
		
		this.inherited(arguments);
		
		this._setUpTilesList(this.widget1_tileList, "ier/widget/test/dashboard.json");
		this._setUpTilesList(this.widget2_tileList, "ier/widget/test/dashboard.json");		
		this._setUpTilesList(this.widget3_tileList, "ier/widget/test/dashboard.json");		
		this._setUpTilesList(this.widget4_tileList, "ier/widget/test/dashboard.json");		
		this._setUpTilesList(this.widget5_tileList, "ier/widget/test/dashboard.json");
		this._setUpTilesList(this.widget6_tileList, "ier/widget/test/dashboard.json");		
	},

	destroy: function(){
		this.inherited(arguments);
	},
	
	_setUpTilesList: function(tileList, url){
		this.logEntry("_setTileListStore");
		this.connect(tileList, "onMaximize", function(){
			tileList.resize();
		});
		this.connect(tileList, "onRestore", function(){
			tileList.resize();
		});
		
		dojo_style.set(tileList.toolbarArea.domNode, "display", "none");
		
		var jsonURL = ecm_model_Request.getPluginResourceUrl("IERApplicationPlugin", url);
		
		var store = new dojo_data_ItemFileReadStore({
			url: jsonURL
		});
		tileList.setStore(store);

		this.logExit("_setTileListStore");
	},

	/**
	 * Method called by parent container to pass additional parameters to this pane.
	 * 
	 * @param params
	 *            Contains a handle to the teamspace object to use when displaying this pane.
	 */
	setParams: function(params) {
		this.logEntry("setParams");

		this.logExit("setParams");
	},

	loadContent: function() {
		this.logEntry("loadContent");
		
		this.isLoaded = true;

		this.logExit("loadContent");
	},

	/**
	 * Resets the content of this pane.
	 */
	reset: function() {
		this.logEntry("reset");

		this.logExit("reset");
	},

	setRepository: function(repository) {
		this.logEntry("setRepository");

		this.logExit("setRepository");
	}
});});
