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
	
return dojo_declare("ier.widget.tasks.TaskParametersPane", [ier_widget_TaskDetailsPane], {
	templateString: templateString,
	widgetsInTemplate: true,
	
	createRendering: function(item) {
		this.logEntry("createRendering");
		
		this._createPropertiesGrid(item);

		this.logExit("createRendering");
	},
	
	_createPropertiesGrid: function(item){
		if(this.propGrid)
			this.propGrid.destroy();
		
		var data = {};
		var resources = {};
		var propertiesArray = [];
	
		if(item instanceof ecm.model.AsyncTaskInstance)
			item = item.parent;
		
		var parameters = item.taskRequest.specificTaskRequest[ier_constants.Param_ParameterList].split(",");
		for(var index in parameters) {
			var parameter = parameters[index];
			if(parameter){
				var name = parameter;
				var label = ier_messages[parameter];
				var value = item.taskRequest.specificTaskRequest[parameter];
				
				if(value){
					data[name] = value;
					resources[name + "Label"] = label || name;
					propertiesArray.push(name);
				}
			}
		}
		
		var taskProperties = propertiesArray.join(",");
		this.propGrid = new ecm_grid_PropertyGrid({
			data: data,
			properties: taskProperties,
			labelKeySuffix: "Label",
			resources: resources
		});
		
		this.propGrid.startup();
		this.propGrid.placeAt(this.gridContainer, "first");
		this.resize();
	}
});});
