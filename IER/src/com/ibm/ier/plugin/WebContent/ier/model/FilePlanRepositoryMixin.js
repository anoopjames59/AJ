define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ecm/model/_ModelObject",
	"ier/model/ResultSet",
	"ier/constants",
	"ier/model/ReportDefinition",
	"ier/model/RecentQuickSearch",
	"ier/util/util",
	"ier/messages"
], function(dojo_declare, dojo_array, dojo_lang, ecm_model_desktop, ecm_model_Request, ecm_model_ModelObject,
		ier_model_ResultSet, ier_constants, ier_model_ReportDefinition, ier_model_RecentQuickSearch, ier_util, ier_messages){

/**
 * @name ier.model.FilePlanRepositoryMixin
 * @class Represents an fileplan repository extension of the ecm.model.Repository for IER usage.
 * @augments ecm.model._ModelObject
 */
return dojo_declare("ier.model.FilePlanRepositoryMixin", null, {
	/** @lends ecm.model.Repository.prototype */
	
	/**
	 * Do not place variables that are objects or arrays.  They will be static.
	 * Initialize them inside of the constructor instead.
	 */
	
	/**
	 * Constructor
	 */
	constructor: function(id, name, recordType, datamodelType, repository, fileplansJSON) {
		this.declaredClass = "ecm.model.Repository";
		
		/*****variables that are obtained after a logon..should be nulled out below in onDisconnect() ***/
		
		//stores and caches all fileplans
		this._filePlans = [];
		
		//stores and caches all disposition schedules for a single file plan repository
		this._dispositionSchedules = [];
	
		//stores and caches all hold objects for a single file plan repository
		this._holds = [];
		
		//stores and caches all report definitions for this repository
		this.reportDefinitions = [];
		
		this.recordDatamodelType = datamodelType;
		
		this.recordRepositoryType = recordType;
		
		this.repository = repository;
		
		this.allRecordProperties = null;
		
		//current user (ecm.model.User)
		this._currentUser = null;
		
		this.recentQuickSearches = [];
		
		if(fileplansJSON) {
			this._filePlans = this._populateFilePlans(fileplansJSON);
		}
	},
	
	
	
	getRecentQuickSearches: function(onComplete){
		onComplete(this.recentQuickSearches);
	},
	
	getRecentQuickSearch: function(id){
		for(var i in this.recentQuickSearches){
			var quickSearch = this.recentQuickSearches[i];
			if(quickSearch.id == id)
				return quickSearch;
		}
	},
	
	/**
	 * Return all properties under the Record class and Record's subclasses
	 */
	getAllRecordProperties: function(callback){
		this.logEntry("getAllRecordProperties");
		
		if(this.allRecordProperties)
			callback(this.allRecordProperties);
		else {
			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				if(response && response.properties){
					this.allRecordProperties = response.properties;
					callback(this.allRecordProperties);
				}
			}));
			
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetAllRecordProperties, ier_constants.PostEncoding, params);
		}
		
		this.logExit("getAllRecordProperties");
	},
	
	addRecentQuickSearch: function(id, name, searchQueryString, results){
		var quickSearch = new ier_model_RecentQuickSearch({
			id: id,
			name: name,
			searchQueryString: searchQueryString,
			repository: this.repository,
			data: results
		});
		this.recentQuickSearches.push(quickSearch);
		return quickSearch;
	},

	/**
	 * Retrieves a fileplan
	 * @param filePlanId
	 * @returns
	 */
	getFilePlan: function(p8FilePlanId) {
		if (this._filePlans) {
			for ( var i in this._filePlans) {
				var fileplan = this._filePlans[i];
				if (fileplan.getGuidId() == p8FilePlanId) {
					return fileplan;
				}
			}
		}
		return null;
	},

	/**
	 * Clear fileplans cache
	 */
	clearFilePlans: function(){
		this._filePlans = [];
	},

	/**
	 * Retrieves all fileplans
	 * 
	 * @returns {Array}
	 */
	getFilePlans: function(onCompleted, onCompletedParams){
		this.logEntry("getFilePlans");
		if(this._filePlans == null || this._filePlans.length == 0)
		{
			if(this._reqFilePlans == null){
				this._filePlans = [];
				var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
					var fileplansJSON = response.fileplans;
					if(fileplansJSON){
						this._filePlans = this._populateFilePlans(fileplansJSON);
					}
					this._reqFilePlans = null;
					onCompleted(this._filePlans, onCompletedParams);
				}));

				this._reqFilePlans = ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetFilePlans, ier_constants.PostEncoding, params);
			}else{
				setTimeout(dojo_lang.hitch(this, function(){
					this.getFilePlans(onCompleted, onCompletedParams);
				}), 100);
			}
		}
		else
			onCompleted(this._filePlans, onCompletedParams);
		
		this.logExit("getFilePlans");
	},
	
	/**
	 * Retrieves all disposition schedules from the server
	 * @param onCompleted
	 */
	getDispositionSchedules: function(onCompleted) {
		this.logEntry("retrieveDispositionSchedules");
		if(this._dispositionSchedules == null || this._dispositionSchedules.length == 0)
		{
			this._dispositionSchedules = [];
			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				var dispositionSchedulesJSON = response.dispositionSchedules;
				if(dispositionSchedulesJSON){
					dojo_array.forEach(dispositionSchedulesJSON, function(dispositionScheduleJSON){
						this._dispositionSchedules.push(ier_util.createBaseEntityItem(dispositionScheduleJSON, this.getRepository));
					});
				}
				onCompleted(this._dispositionSchedules);
			}));
			
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetDispositionSchedules, ier_constants.PostEncoding, params);
		}
		else
			onCompleted(this._dispositionSchedules);
		
		this.logExit("retrieveDispositionSchedules");
	},
	
	/**
	 * Retrieves a disposition schedule based on dispositionId
	 * @param dispositionId
	 * @returns
	 */
	getDispositionSchedule: function(dispositionId) {
		if (this._dispositionSchedules) {
			for ( var i in this._dispositionSchedules) {
				var disp = this._dispositionSchedules[i];
				if (disp.id == dispositionId) {
					return disp;
				}
			}
		}
		return null;
	},
	
	// Retrieves all non-system-generated or system-owned properties from a class
	retrievePropertyDescriptions: function(contentClass, callback) 
	{
		this.logEntry("retrievePropertyDescriptions");
		if (contentClass && contentClass.id) {
			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				var properties = [];
				for (var i in response.datastore.items) {
					var items = response.datastore.items[i];
					properties.push({displayname: items.displayName, symbolicname: items.symbolicName, 
						datatype: items.dataType, description: items.template_desc});				
				}
				
				if (callback)
					callback(properties);
			}));
			
			params.requestParams[ier_constants.Param_ClassName] = contentClass.id;

			//var data={};
			//data["class_name"]=contentClass.id;
			//params["requestBody"] = data;
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, "ierGetClassPropertyDescriptions", ier_constants.PostEncoding, params);
		}
		
		this.logExit("retrievePropertyDescriptions");
	},
	
	/**
	 * Retrieves all report definitions in the repository.  Initially only the id, name, and description are obtained. 
	 */
	retrieveReportDefinitions: function(callback) {
		this.logEntry("retrieveReportDefinitions");
		
		if(this.reportDefinitions == null || this.reportDefinitions.length == 0) {
			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				this.reportDefinitions = [];
				for (var i in response.datastore.items) {
					var reportDefinitionJSON = response.datastore.items[i];
					var reportDefinition = new ier_model_ReportDefinition({
						id: reportDefinitionJSON.template_id,
						name: reportDefinitionJSON.template_name,
						title: ier_util.getReportMessages(reportDefinitionJSON.ier_reportdefinition_title_key, reportDefinitionJSON.ier_reportTitle),
						description: ier_util.getReportMessages(reportDefinitionJSON.ier_reportdefinition_desc_key, reportDefinitionJSON.template_desc),
						entryHelp: ier_util.getReportMessages(reportDefinitionJSON.ier_reportdefinition_entryhelp_desc_key, reportDefinitionJSON.ier_reportdefinition_entryhelp),
						repository: this.getRepository()
					});
					this.reportDefinitions.push(reportDefinition);				
				}
				if (callback) {
					callback(this.reportDefinitions);
				}
			}));
			
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, "ierGetReportDefinitions", ier_constants.PostEncoding, params);
		}
		else {
			if(callback)
				callback(this.reportDefinitions);
		}
			
		this.logExit("retrieveReportDefinitions");
	},
	
	/**
	 * Get a report definition cached in the repository
	 */
	getReportDefinition: function(reportId){
		for(var i in this.reportDefinitions){
			var rd = this.reportDefinitions[i];
			if(rd.id == reportId){
				return rd;
			}
		}
	},
	
	/**
	 * Retrieves the custom objects
	 * @param className - class name of the custom objects to retrieve
	 * @returns
	 */
	retrieveObjects: function(className, filterString, callback, showAdditionalColumns, additionalParams, entityType, sortColum) {
		this.logEntry("retrieveObjects");
		
		if (className == "Action")
			className = "Action1";  // DB Table is Action1

		var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response){
			if(response && callback){
				response.repository = this.repository;
				var resultSet = new ier_model_ResultSet(response);
				callback(resultSet);
			}
		}));
		params.requestParams[ier_constants.Param_ClassName] = className;
		params.requestParams[ier_constants.Param_EntityType] = ((entityType) ? entityType : this._entityType);
		
		if(showAdditionalColumns)
			params.requestParams[ier_constants.Param_ShowAdditionalColumns] = "true";
			
		if(additionalParams){
			for(var i in additionalParams){
				params.requestParams[i] = additionalParams[i];
			}
		}
		
		var data = {};
		data[ier_constants.Param_FilterString] = filterString;
		params["requestBody"] = data;
		if(sortColum){
			params.requestParams[ier_constants.Param_OrderBy] = sortColum[0].attribute;
			params.requestParams[ier_constants.Param_OrderByDescending] = sortColum[0].descending;
		}
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetObjects, ier_constants.PostEncoding, params);
	
		this.logExit("retrieveObjects");
	},

	/**
	 * Creates a new hold or update an existing hold with the given parameters
	 * @param properties - All the properties for the hold
	 * @param holdId - The existing hold to update (null for creation) 
	 * @param callback - The callback to invoke when the hold created
	 */
	saveHold: function(item, properties, conditions, holdId, callback){
		this.logEntry("addHold");

		var repository = this.getRepository();
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){	
			if(callback){
				callback();
			}
			if(item && item.retrieveAttributes){
				item.retrieveAttributes(null, false);
			}else{
				this.onConfigure(repository);
			}
		}));

		var data = {};
		data[ier_constants.Param_Properties] = properties;
		if(conditions){
			data[ier_constants.Param_Conditions] = conditions;
		}
		if(holdId){
			data[ier_constants.Param_HoldId] = holdId;
		}
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_SaveHold, ier_constants.PostEncoding, params);

		this.logExit("addHold");
	},

	/**
	 * Obtains conditions for the given object type
	 * @param objectId - The id of the particular object
	 * @param entityType - The entityType of the object
	 * @param callback - The callback to invoke when hold conditions received
	 */
	getObjectConditions: function(objectId, entityType, callback){
		var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
			if(callback){
				callback(response.conditions || []);
		}}));
		
		params.requestParams[ier_constants.Param_Id] = objectId;
		params.requestParams[ier_constants.Param_EntityType] = entityType;
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetObjectConditions, ier_constants.PostEncoding, params);
	},

	/**
	 * Creates a new location or updates an existing location with the given parameters
	 * @param properties - All the properties for the location
	 * @param locationId - The existing location to update (null for creation) 
	 * @param callback - The callback to invoke when the location created
	 */
	saveLocation: function(properties, locationId, callback){
		this.logEntry("addLocation");

		var repository = this.getRepository();
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){	
			if(callback){
				callback();
			}
			this.onConfigure(repository);
		}));
		
		var data = {};
		data[ier_constants.Param_Properties] = properties;
		if(locationId){
			data[ier_constants.Param_LocationId] = locationId;
		}
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_SaveLocation, ier_constants.PostEncoding, params);

		this.logExit("addLocation");
	},
	
	/**
	 * Creates a new naming pattern or updates an existing naming pattern with the given parameters
	 * @param properties - All the properties for the naming pattern
	 * @param patternLevels - Pattern levels for the naming pattern
	 * @param patternId - The existing naming pattern to update (null for creation)
	 * @param callback - The callback to invoke when the naming pattern created
	 */
	saveNamingPattern: function(properties, patternLevels, patternId, callback){
		this.logEntry("addNamingPattern");

		var repository = this.getRepository();
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){	
			var resultSet = new ier_model_ResultSet(response);
			if(callback){
				callback(resultSet);
			}
			this.onConfigure(repository, resultSet.getItems());
		}));
		
		var data = {};
		data[ier_constants.Param_Properties] = properties;
		data[ier_constants.Param_PatternLevels] = patternLevels;
		if(patternId){
			data[ier_constants.Param_PatternId] = patternId;
		}
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_SaveNamingPattern, ier_constants.PostEncoding, params);

		this.logExit("addNamingPattern");
	},

	/**
	 * Obtains naming pattern levels for the given naming pattern
	 * @param patternId - The naming pattern
	 * @param callback - The callback to invoke when naming pattern levels received
	 */
	getNamingPatternLevels: function(patternId, callback){
		var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
			if(callback){
				callback(response.namingPatternLevels || []);
			}
		}));
		params.requestParams[ier_constants.Param_PatternId] = patternId;
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetNamingPatternLevels, ier_constants.PostEncoding, params);
	},

	/**
	 * Save system configurations
	 * @param systemConfigurations - The system configurations
	 * @param callback - The callback to invoke when the system configurations saved
	 */
	saveSystemConfigurations: function(systemConfigurations, callback){
		this.logEntry("saveSystemConfigurations");

		var repository = this.getRepository();
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(){	
			if(callback){
				callback();
			}
			this.onConfigure(repository);
		}));

		var data = {};
		data[ier_constants.Param_SystemConfigurations] = systemConfigurations;
		params["requestBody"] = data;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_SaveSystemConfigurations, ier_constants.PostEncoding, params);

		this.logExit("saveSystemConfigurations");
	},

	/**
	 * Creates a new action with the given parameters
	 * @param properties - All the properties for the action
	 * @param callback - The callback to invoke when the action created
	 */
	addAction: function(properties, callback){
		this.logEntry("addLocation");

		var repository = this.getRepository();
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){	
			if(callback){
				callback();
			}
			this.onConfigure(repository);
		}));
		
		var data = {};
		data[ier_constants.Param_Properties] = properties;
		params["requestBody"] = data;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateAction, ier_constants.PostEncoding, params);

		this.logExit("addLocation");
	},
	
	/**
	 * Creates a new trigger with the given parameters
	 * @param properties - All the properties for the trigger
	 * @param callback - The callback to invoke when the trigger created
	 */
	addTrigger: function(properties, conditions, eventType, callback, additionalParams){
		this.logEntry("addTrigger");

		var repository = this.getRepository();
		var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response){	
			if(callback){
				callback();
			}
			this.onConfigure(repository);
		}));
		
		if(additionalParams){
			for(var i in additionalParams){
				params.requestParams[i] = additionalParams[i];
			}
		}
		
		var data = {};
		data[ier_constants.Param_Properties] = properties;
		data[ier_constants.Param_Conditions] = conditions;
		params["requestBody"] = data;
		params.requestParams[ier_constants.Param_EventType] = eventType;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateTrigger, ier_constants.PostEncoding, params);

		this.logExit("addTrigger");
	},
	
	/**
	 * Retrieves and populates fileplans from server
	 */
	_populateFilePlans: function(fileplansJSON)
	{
		var filePlans = [];
		if (fileplansJSON) {
			dojo_array.forEach(fileplansJSON, dojo_lang.hitch(this, function(fileplanJSON) {
				var filePlan = ier_util.createBaseEntityItem(fileplanJSON, this.getRepository());
				if(fileplanJSON.namingPattern) {
					filePlan.setNamingPattern(ier_util.createBaseEntityItem(fileplanJSON.namingPattern, this.getRepository()));
				}
				
				filePlans.push(filePlan);
			}));
		}
		return filePlans;
	},
	
	/**
	 * Returns the repository object itself
	 * @returns
	 */
	getRepository: function() {
		return this.repository;
	},

	/**
	 * Returns the datamodel type for this repository.  Values are obtained from Jarm and will be: Combined, FilePlan, Plain, Content.  
	 * @returns
	 */
	getDatamodelType: function() {
		return this.recordDatamodelType;
	},
	
	/**
	 * Event called for a disconnect
	 * @param repository
	 */
	onIERLogOff: function(repository) {
		
		/**Null out properties after a session ends**/
		this._filePlans = [];
		this._dispositionSchedules = [];
		this._recordEntryTemplates = [];
		this._holds = [];
		this._reportDefinitions = [];
		this.recordDatamodelType = null;
		this._currentUser = null;
	},

	/**
	 * Event called when configuration changed
	 * @param repository
	 * @param items
	 */
	onConfigure: function(repository, items){
		// if any connections to desktop "onConfigure" exists
		if(ecm_model_desktop.onConfigure){
			ecm_model_desktop.onConfigure(repository, items);
		}
	},
	
	/**
	 * Event called when items are updated
	 */
	onItemsUpdated: function(){
		
	}

});});
