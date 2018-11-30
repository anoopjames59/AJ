define([
	"dojo/_base/declare",
	"dojo/_base/lang",
//	"ecm/model/_ModelObject",
	"ier/model/_BaseEntityObject",
	"ecm/model/Request",
	"ier/util/util",
	"ier/messages",
	"ier/constants"
], function(dojo_declare, dojo_lang, ier_model_BaseEntityObject/*ecm_model_ModelObject*/, ecm_model_Request, ier_util, ier_messages, ier_constants){

/**
 * A report definition model object stands for a report.  It contains all the necessary information to run the report.
 * @name ier.model.ReportDefinition
 * @class ReportDefinition
 */
//return dojo_declare("ier.model.ReportDefinition",[ecm_model_ModelObject], {
return dojo_declare("ier.model.ReportDefinition",[ier_model_BaseEntityObject], {
	
	//description of the report
	description: null,
	
	//localization key for the description of the report
	descriptionkey: null,
	
	//localization key for the title/name of the report
	titlekey: null,
	
	//entry help for the report
	entryHelp: null,
	
	//localization key for the entry help of the report
	entryHelpkey: null,
	
	queries: null,
	parameters: null,
	isLoaded: false,
	
	/**
	 * The table name for this report.  This table stores all the report results.
	 */
	tableName: null,
	
	constructor : function() {
		this.queries = [];
		this.parameters = [];
	},

	getDescription : function() {
		return this.description;
	},

	setDescription : function(desp) {
		this.description = desp;
	},
	
	setDescriptionLocalizationKey : function(key) {
		this.descriptionkey = key;
	},
	
	getDescriptionLocalizationKey : function() {
		return this.descriptionkey;
	},
	
	setTitleLocalizationKey : function(key) {
		this.titlekey = key;
	},
	
	getTitleLocalizationKey : function() {
		return this.titlekey;
	},
	
	setQueries : function(queries) {
		for (var i in queries) {
			var query = queries[i];
			this.queries.push({"sql": query.query_statement, "entity_type" : query.query_entity_type});
		}
	},

	getQueryNumber : function() {
		return this.queries.length;
	},
	
	getQueries : function() {
		return this.queries;
	},
	
	addParameter : function(symname, displayname, isReq, datatype, cardinality, values)
	{
		var pValues = [];
		if (values) {
			for (var i in values)
			{
				var displayName = values[i].value;
				if (values[i].value_id == "Record")
					displayName = ier_messages.reportDefinitionDialog_Record;
				else if (values[i].value_id == ier_constants.ClassName_RecordCategory)
					displayName = ier_messages.reportDefinitionDialog_RecordCategory;
				else if (values[i].value_id == ier_constants.ClassName_Volume)
					displayName = ier_messages.reportDefinitionDialog_Volume;
				else if (values[i].value_id == ier_constants.ClassName_RecordFolder)
					displayName = ier_messages.reportDefinitionDialog_RecordFolder;

				pValues.push({"value" : values[i].value_id, "displayName" : displayName, "key" : values[i].value_key});
			}
		}
		
		if (cardinality) {
			if (cardinality == "Single") 
				cardinality = "SINGLE";
			else if (cardinality == "List") 
				cardinality = "LIST";
		}
					
		if (pValues.length > 0)
			this.parameters.push({"symname" : symname, 
						"displayname" : displayname,
						"datatype" : datatype,
						"cardinality" : cardinality,
						"isreq" : isReq, 
						"values" : pValues});
		else
			this.parameters.push({"symname" : symname, 
				"displayname" : displayname,
				"datatype" : datatype,
				"cardinality" : cardinality,
				"isreq" : isReq});
	},
	
	getParameterNumber : function() {
		return this.parameters.length;
	},
	
	getParameters : function() {
		return this.parameters;		
	},
	
	setEntryHelp : function(desp) {
		this.entryHelp = desp;
	},
	
	setEntryHelpLocalizationKey : function(key) {
		this.entryHelpkey = key;
	},

	getEntryHelp : function() {
		return this.entryHelp;		
	},
	
	getEntryHelpLocalizationKey : function() {
		return this.entryHelpkey;		
	},
	
	/**
	 * Retrieves choicelists associated with this report definition
	 */
	retrieveChoiceList: function(choiceListId, callback)
	{
		this.logEntry("retrieveChoiceList");

		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) 
		{
			this.choiceListItems = [];
			for (var i in response.datastore.items) {
				var itemJSON = response.datastore.items[i];
				var item = {value: itemJSON.fixedValue, displayName: itemJSON.name};
				this.choiceListItems.push(item);				
			}
			if (callback) {
				callback(this._choiceListItems);
			}
		}));
		params.requestParams[ier_constants.Param_Id] = choiceListId;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetChoiceList, ier_constants.PostEncoding, params);

		this.logExit("retrieveChoiceList");
	},
	
	/**
	 * Loads the rest of the report definition to avoid grabbing all the values initially
	 */
	loadReportDefinition: function(callback)
	{
		this.logEntry("retrieveReport");
		if(!this.isLoaded) {

			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) 
			{
				var reportDefinitionJSON = response.datastore.items[0];
				
				this.tableName = reportDefinitionJSON.tableName;
				
				if (reportDefinitionJSON.ier_reportdefinition_desc_key)
					this.setDescriptionLocalizationKey(reportDefinitionJSON.ier_reportdefinition_desc_key);

				if (reportDefinitionJSON.ier_reportdefinition_title_key)
					this.setTitleLocalizationKey(reportDefinitionJSON.ier_reportdefinition_title_key);

				if (reportDefinitionJSON.ier_reportdefinition_entryhelp_desc_key)
					this.setEntryHelpLocalizationKey(reportDefinitionJSON.ier_reportdefinition_entryhelp_desc_key);

				if (reportDefinitionJSON.ier_reportdefinition_entryhelp)
					this.setEntryHelp(reportDefinitionJSON.ier_reportdefinition_entryhelp);

				if (reportDefinitionJSON.ier_reportdefinition_queries)
					this.setQueries(reportDefinitionJSON.ier_reportdefinition_queries);
				
				this.name = reportDefinitionJSON.template_name;
				this.title = ier_util.getReportMessages(reportDefinitionJSON.ier_reportdefinition_title_key, reportDefinitionJSON.ier_reportTitle);
				this.description = ier_util.getReportMessages(reportDefinitionJSON.ier_reportdefinition_desc_key, reportDefinitionJSON.template_desc);
				this.entryHelp = ier_util.getReportMessages(reportDefinitionJSON.ier_reportdefinition_entryhelp_desc_key, reportDefinitionJSON.ier_reportdefinition_entryhelp);

				if (reportDefinitionJSON.ier_reportdefinition_parameters)
				{
					for (var i in reportDefinitionJSON.ier_reportdefinition_parameters)
					{
						// The team decided to reuse the HoldName property and special-case the code here.
						if (reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_symname == ier_constants.ReportEntry_hold_name) {
							reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_symname = ier_constants.Property_HoldName;
							reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_name = ier_messages.report_holdName;
							reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_datatype = ier_constants.ReportEntry_Param_StringType;
							reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_cardinality = "Single";
						}
						
						this.addParameter(
								reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_symname,
								reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_name,
								reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_is_required,
								reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_datatype,
								reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_cardinality,
								reportDefinitionJSON.ier_reportdefinition_parameters[i].parameter_values);						
					}
				}
				
				this.isLoaded = true;

				if (callback) {
					callback(this);
				}
			}));
			params.requestParams[ier_constants.Param_ReportId] = this.id;
	
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetReportDefintions, ier_constants.PostEncoding, params);
		} else {
			if(callback){
				callback(this);
			}
		}

		this.logExit("retrieveReport");
	}
});});