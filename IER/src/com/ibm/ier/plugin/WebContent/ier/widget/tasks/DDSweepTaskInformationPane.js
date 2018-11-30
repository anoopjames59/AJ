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
     "ier/widget/tasks/TaskParametersPane",
 	 "ier/widget/tasks/DDSweepTaskResultsPane",
 	 "ier/widget/tasks/TaskErrorPane",
 	 "ier/widget/tasks/TaskExecutionRecordPane",
     "dojo/text!./templates/TaskInformationPane.html",
     "ier/widget/tasks/ReportTaskDetailsPane", //in template
     "idx/layout/HeaderPane" // in template
], function(dojo_declare, dojo_lang, dojo_connect, dojo_style, dojo_geo, dijit_layout_ContentPane, ecm_model_AsyncTask, ecm_model_AsyncTaskInstance, 
		ier_constants, ier_messages, ier_widget_TaskInformationPane, TaskDetailsPane, TaskParametersPane, DDSweepTaskResultsPane, TaskErrorPane, TaskEexecutionRecordPane,
		templateString){
	
return dojo_declare("ier.widget.tasks.DDSweepTaskInformationPane", [ier_widget_TaskInformationPane], {
	templateString: templateString,
	widgetsInTemplate: true,
	
	setUpTabs: function(){
		this.detailsTab = new TaskDetailsPane({
			UUID: "details",
			title: ier_messages.taskPane_previewDetails,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.detailsTab);
		
		this.parametersTab = new TaskParametersPane({
			UUID: "parameters",
			title: ier_messages.taskPane_previewParameters,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.parametersTab);
		
		this.resultsTab = new DDSweepTaskResultsPane({
			UUID: "results",
			title: ier_messages.taskPane_previewResults,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.resultsTab);
		
		this.errorsTab = new TaskErrorPane({
			UUID: "errors",
			title: ier_messages.taskPane_previewErrors,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.errorsTab);
		
		this.taskInstancesTab = new TaskEexecutionRecordPane({
			UUID: "asyncTaskInstances",
			title: ier_messages.taskPane_previewExecutionRecords,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.taskInstancesTab);
	}
		
});});