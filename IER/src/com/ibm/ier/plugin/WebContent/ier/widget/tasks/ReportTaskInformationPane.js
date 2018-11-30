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
     "ier/widget/tasks/ReportTaskDetailsPane",
     "ier/widget/tasks/ReportTaskReportParametersPane",
 	 "ier/widget/tasks/TaskResultsPane",
 	 "ier/widget/tasks/TaskErrorPane",
 	 "ier/widget/tasks/TaskExecutionRecordPane",
 	 "ier/widget/tasks/ReportTaskResultsPane",
     "dojo/text!./templates/TaskInformationPane.html",
     "idx/layout/HeaderPane" // in template
], function(dojo_declare, dojo_lang, dojo_connect, dojo_style, dojo_geo, dijit_layout_ContentPane, ecm_model_AsyncTask, ecm_model_AsyncTaskInstance, 
		ier_constants, ier_messages, ier_widget_TaskInformationPane, ReportTaskDetailsPane, ReportTaskReportParametersPane, TaskResultsPane, TaskErrorPane, TaskEexecutionRecordPane,
		ReportTaskResultsPane, templateString){
	
return dojo_declare("ier.widget.tasks.ReportTaskInformationPane", [ier_widget_TaskInformationPane], {
	
	setUpTabs: function(){
		this.detailsTab = new ReportTaskDetailsPane({
			UUID: "details",
			title: ier_messages.taskPane_previewDetails,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.detailsTab);
		
		this.reportParametersTab = new ReportTaskReportParametersPane({
			UUID: "reportParameters",
			title: ier_messages.taskPane_previewReportParameters,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.reportParametersTab);
		
		this.resultsTab = new ReportTaskResultsPane({
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