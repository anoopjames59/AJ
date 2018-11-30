define ([
     "dojo/_base/declare",
     "dojo/_base/lang",
     "dojo/_base/connect",
     "dojo/dom-style",
     "dojo/dom-geometry",
     "dijit/layout/ContentPane",
 	 "ecm/model/AsyncTask",
 	 "ecm/model/AsyncTaskInstance",
     "ier/constants",
     "ier/messages",
     "ier/widget/tasks/TaskInformationPane",
     "ier/widget/tasks/TaskDetailsPane",
 	 "ier/widget/tasks/TaskResultsPane",
 	 "ier/widget/tasks/TaskErrorPane",
 	 "ier/widget/tasks/TaskExecutionRecordPane",
     "dojo/text!./templates/TaskInformationPane.html"
], function(dojo_declare, dojo_lang, dojo_connect, dojo_style, dojo_geo, dijit_layout_ContentPane, ecm_model_AsyncTask, ecm_model_AsyncTaskInstance, 
		ier_constants, ier_messages, ier_widget_TaskInformationPane, TaskDetailsPane, TaskResultsPane, TaskErrorPane, TaskEexecutionRecordPane,
		templateString){
	
return dojo_declare("ier.widget.tasks.DDConversionTaskInformationPane", [ier_widget_TaskInformationPane], {
	templateString: templateString,
	widgetsInTemplate: true,
	
	setUpTabs: function(){
		this.detailsTab = new TaskDetailsPane({
			UUID: "details",
			title: ier_messages.taskPane_previewDetails,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.detailsTab);
		
		this.parametersTab = new TaskResultsPane({
			UUID: "results",
			title: ier_messages.taskPane_previewResults,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.parametersTab);
		
		this.errorsTab = new TaskErrorPane({
			UUID: "errors",
			title: ier_messages.taskPane_previewErrors,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.errorsTab);
	}
		
});});