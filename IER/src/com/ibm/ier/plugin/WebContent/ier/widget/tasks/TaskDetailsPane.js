define ([
     "dojo/_base/declare",
     "dojo/dom-style",
     "dojo/dom-construct",
     "dojo/date/locale",
     "dijit/layout/ContentPane",
     "dijit/_TemplatedMixin",
 	 "dijit/_WidgetsInTemplateMixin",
 	 "ecm/LoggerMixin",
     "ier/constants",
     "ier/messages",
     "dojo/text!./templates/TaskDetailsPane.html",
     "ecm/widget/PropertyGrid" // in content
], function(dojo_declare, dojo_domStyle, dojo_construct, dojo_date_locale, dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin,
		ier_constants, ier_messages, templateString, ecm_grid_PropertyGrid){
	
return dojo_declare("ier.widget.tasks.TaskDetailsPane", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	templateString: templateString,
	widgetsInTemplate: true,
	
	createRendering: function(item) {
		this.logEntry("createRendering");
		
		if(item != this.item){
			this.item = item;
			
			this._createTaskDetailsGrid(item);
		}

		this.logExit("createRendering");
	},
	
	_createTaskDetailsGrid: function(item){
		if(this.taskPropGrid)
			this.taskPropGrid.destroy();
		
		var data = {};
		var resources = {};
		var propertiesArray = [];
		
		for(var name in item.attributes) {
			var value = item.attributes[name];
			var label = this.getLabel(name);
			value = this.getValue(name, value);
			if(value && name != "taskRequest" && name != "errors" && label != null){
				data[name] = value;
				resources[name + "Label"] = label || name;
				propertiesArray.push(name);
			}
		}
		
		var taskProperties = propertiesArray.join(",");
		this.taskPropGrid = new ecm_grid_PropertyGrid({
			data: data,
			properties: taskProperties,
			labelKeySuffix: "Label",
			resources: resources
		});
		this.taskPropGrid.startup();
		this.taskPropGrid.placeAt(this.gridContainer, "first");
		this.resize();
	},
	
	getLabel: function(name){
		if(name == "ier_reportName"){
			return ier_messages.reportDefDialog_reportNameFieldName;
		}
		else {
			var message = ier_messages[name];
			if(message)
				return message;
		}
		
		return null;
	},
	
	getValue: function(name, value){
		if(name == "type"){
			return this.getTaskTypeDisplayName(value);
		}
		else 
			return this.item.getDisplayValue(name);
	},
	
	getTaskTypeDisplayName: function(type){
		if(type == ier_constants.TaskType_ReportClass)
			return ier_messages.reports;
		else if(type == ier_constants.TaskType_DispositionSweepClass)
			return ier_messages.dispositionSweeps;
		else if(type == ier_constants.TaskType_HoldSweepClass)
			return ier_messages.holdSweeps;
		else
			return type;
	},
	
	_addValue: function(name, item, data, resources, propertiesArray){
		var value = item.taskRequest.specificTaskRequest[name];
		var label = this.getLabel(name);
		
		if(value){
			data[name] = value;
			resources[name + "Label"] = label || name;
			propertiesArray.push(name);
		}
	}
});});