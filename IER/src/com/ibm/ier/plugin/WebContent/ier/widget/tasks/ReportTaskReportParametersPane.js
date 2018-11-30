define ([
     "dojo/_base/declare",
     "dojo/_base/lang",
     "dojo/dom-style",
     "dojo/dom-construct",
     "dojo/date/locale",
     "ier/constants",
     "ier/messages",
     "ier/util/util",
     "ier/widget/tasks/TaskDetailsPane",
     "dojo/text!./templates/TaskDetailsPane.html",
     "ecm/widget/PropertyGrid" // in content
], function(dojo_declare, dojo_lang, dojo_domStyle, dojo_construct, dojo_date_locale,
		ier_constants, ier_messages, ier_util, ier_widget_TaskDetailsPane, templateString, ecm_grid_PropertyGrid){
	
return dojo_declare("ier.widget.tasks.ReportTaskReportParametersPane", [ier_widget_TaskDetailsPane], {
	templateString: templateString,
	widgetsInTemplate: true,
	
	postCreate: function(){
		var brNode = dojo_construct.create("br");
		dojo_construct.place(brNode, this.gridContainer);
	},
	
	createRendering: function(item) {
		this.logEntry("createRendering");
		
		//obtain the attributes from the report holding class to localize the report criteria strings
		var repository = ier_util.getRepository(item.taskRequest.specificTaskRequest.ier_p8RepositoryId,  item.taskRequest.ceEJBURL);
		if(repository){
			var reportHoldingContentClass = repository.getContentClass(ier_constants.ClassName_ReportHold);
			reportHoldingContentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(attributeDefinitions){
				this.attributes = attributeDefinitions;
				this._createReportPropertiesGrid(item);
				this._createReportOutputPropertiesGrid(item);
			}));
		}
		
		this.logExit("createRendering");
	},
	
	_createReportPropertiesGrid: function(item){
		if(this.reportPropGrid)
			this.reportPropGrid.destroy();
		
		var data = {};
		var resources = {};
		var propertiesArray = [];
		
		if(item.attributes.type == ier_constants.TaskType_ReportClass){
			//TODO: remove..transition period for existing tasks
			if(item.taskRequest && item.taskRequest.specificTaskRequest == null)
				item.taskRequest.specificTaskRequest = item.taskRequest.reportTaskRequest;
			
			
			if(item instanceof ecm.model.AsyncTaskInstance)
				item = item.parent;
			
			//add the report name
			this._addValue("ier_reportName", item, data, resources, propertiesArray);
			
			for(var index in item.taskRequest.specificTaskRequest.criterias) {
				var object = item.taskRequest.specificTaskRequest.criterias[index];
				if(object){
					var attributeDef = this.attributes ? ier_util.getAttributeDefinition(this.attributes, object.name) : null;

					name = object.name;
					label = attributeDef && attributeDef.name ? attributeDef.name : object.label;
					value = object.displayValue ? object.displayValue : object.value;
					
					if(object.dataType == "xs:user"){
						label = ier_messages.username;
						value = value && value[0] ? value[0].displayName: "";
					}
					
					if(object.dataType == "xs:timestamp"){
					    if(value)
						    value = dojo_date_locale.format(new Date(value), {datePattern: "MM/dd/yyyy", timePattern: "hh:mm:ss a"});
					}
					
					if(object.name == ier_constants.ReportEntry_hold_name){
						label = ier_messages.report_holdName;
					}
				}

				if(value){
					data[name] = value;
					resources[name + "Label"] = label || name;
					propertiesArray.push(name);
				}
			}
			
			var taskProperties = propertiesArray.join(",");
			this.reportPropGrid = new ecm_grid_PropertyGrid({
				data: data,
				properties: taskProperties,
				labelKeySuffix: "Label",
				resources: resources
			});
			
			this.reportPropGrid.startup();
			this.reportPropGrid.placeAt(this.gridContainer, "first");
			this.resize();
		}
	},
	
	_createReportOutputPropertiesGrid: function(item){
		if(this.reportOutputPropGrid)
			this.reportOutputPropGrid.destroy();
		
		var data = {};
		var resources = {};
		var propertiesArray = [];
		
		if(item.attributes.type == ier_constants.TaskType_ReportClass){
			//TODO: remove..transition period for existing tasks
			if(item.taskRequest && item.taskRequest.specificTaskRequest == null)
				item.taskRequest.specificTaskRequest = item.taskRequest.reportTaskRequest;
			
			if(item instanceof ecm.model.AsyncTaskInstance)
				item = item.parent;
			
			//add report output information
			this._addValue(ier_constants.Param_SaveInRepository, item, data, resources, propertiesArray);
			this._addValue(ier_constants.Param_SaveInFolderLocationName, item, data, resources, propertiesArray);
			this._addValue(ier_constants.Param_ReportTitle, item, data, resources, propertiesArray);
			
			var taskProperties = propertiesArray.join(",");
			this.reportOutputPropGrid = new ecm_grid_PropertyGrid({
				data: data,
				properties: taskProperties,
				labelKeySuffix: "Label",
				resources: resources
			});
			
			this.reportOutputPropGrid.startup();
			this.reportOutputPropGrid.placeAt(this.gridContainer);
			this.resize();
		}
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
