define ([
     "dojo/_base/declare",
     "dojo/dom-style",
     "dojo/dom-construct",
     "dojo/date/locale",
     "ier/constants",
     "ier/messages",
     "ier/widget/tasks/TaskDetailsPane",
     "dojo/text!./templates/TaskDetailsPane.html",
     "ecm/widget/PropertyGrid" // in content
], function(dojo_declare, dojo_domStyle, dojo_construct, dojo_date_locale,
		ier_constants, ier_messages, ier_widget_TaskDetailsPane, templateString, ecm_grid_PropertyGrid){
	
return dojo_declare("ier.widget.tasks.ReportTaskDetailsPane", [ier_widget_TaskDetailsPane], {
	
	createRendering: function(item) {
		this.logEntry("createRendering");
		this.inherited(arguments);
		
		if(this.informationPane){
			var reportParametersTab = this.informationPane.getTab("reportParameters");
			if(reportParametersTab)
				reportParametersTab.createRendering(item);
		}
		this.logExit("createRendering");
	}
});});