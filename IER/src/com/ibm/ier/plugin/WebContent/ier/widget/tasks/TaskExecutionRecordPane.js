define ([
     "dojo/_base/declare",
     "dojo/_base/lang",
     "dojo/dom-style",
     "dojo/dom-class",
     "dojo/dom-construct",
     "dijit/layout/ContentPane",
     "dijit/_TemplatedMixin",
     "dijit/_WidgetsInTemplateMixin",
     "ecm/LoggerMixin",
     "ecm/model/Desktop",
     "ecm/model/AsyncTask",
     "ecm/model/AsyncTaskInstance",
     "ier/constants",
     "ier/messages",
     "ier/widget/listView/gridModules/RowContextMenu",
     "dojo/text!./templates/TaskExecutionRecordPane.html",
     "ier/widget/listView/ContentList" //in template
 ], function(dojo_declare, dojo_lang, dojo_domStyle, dojo_domClass, dojo_construct, dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin,
		 ecm_model_Desktop, ecm_model_AsyncTask, ecm_model_AsyncTaskInstance, ier_constants, ier_messages, ier_widget_listView_RowContextMenu, templateString){

return dojo_declare("ier.widget.tasks.TaskExecutionRecordPane", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	templateString: templateString,
	widgetsInTemplate: true,
	messages: ier_messages,
	
	postCreate: function() {
		this.logEntry("postCreate");
		
		this.inherited(arguments);
		this.contentList.setContentListModules(this.getContentListModules());
		this.contentList.setGridExtensionModules(this.getContentListGridModules());
	
		this.logExit("postCreate");
	},
	
	getContentListModules: function() {
		var array = [];
		return array;
	},
	
	getContentListGridModules: function() {
		var array = [];
		array.push(ier_widget_listView_RowContextMenu);
		return array;
	},

	createRendering: function(item) {
		if(item && this.item != item && (item instanceof ecm_model_AsyncTask)){
			var actionsHandler = ecm_model_Desktop.getActionsHandler();
			if (actionsHandler) {
				actionsHandler.actionTaskOpen(null, [item], dojo_lang.hitch(this, function(item, resultSet) {
					this.contentList.setResultSet(resultSet);
				}), null, item.resultSet, null, true);
			}
		}
		
		this.connect(this.contentList, "onOpenItem", dojo_lang.hitch(this, function(item, openedItemData){
			this.onTaskExecutionRecordOpened(item, openedItemData);
		}));
	},
	
	/**
	 * Event invoked when a task execution record is opened
	 */
	onTaskExecutionRecordOpened: function(item, openedItemData){
		
	}
});});