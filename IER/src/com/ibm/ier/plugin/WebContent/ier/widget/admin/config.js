define([
	"dojo/_base/declare",
	"dojo/_base/json",
	"dojo/_base/lang",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util",
	"ecm/LoggerMixin"
], function(dojo_declare, dojo_json, dojo_lang, ecm_model_desktop, ecm_model_Request, ier_constants, ier_util, ecm_LoggerMixin){
/**
 * This configuration is used to store values to the configurationString for a plugin.
 * Other than declare, we will be storing everything else in the official config db uder ier/model/admin/config.
 * You should use that instead of this.
 * 
 * The report server names will be moved to there and phased out eventually.
 */
var _AdminConfigurationUtil = dojo_declare("ier.widget.admin._AdminConfigurationUtil", ecm_LoggerMixin, {
	
	configurationString : null,
	configurationInitial : {
		"id": "ier_constants.ApplicationPlugin",
		"selectedFilePlanRepositories": []
	},
	
	SELECTED_FILEPLAN_REPOSITORIES: "selectedFilePlanRepositories",
	REPORT_SERVER_NAME: "reportServerName",
	JDBC_REPORT_DB_DATASOURCE: "JDBCReportDBDataSource",
	
	/**
	 * Returns true if the configuration is loaded.
	 * @returns {Boolean}
	 */
	isConfigurationLoaded: function(){
		return configurationString != null;
	},
	
	getConfigurationString: function() {
		return this.configurationString;
	},
	
	getEscapedConfigurationString: function() {
		if(this.configurationString && this.configurationString.id)
			return escape(JSON.stringify(this.configurationString));
		else
			return JSON.stringify(this.configurationString);
	},
	
	/**
	 * Loads the current plugin configuration and loads the confiiguration string
	 */
	loadConfiguration: function(onComplete, configurationString){
		if(!this.configurationString){
			if(configurationString){
				this.configurationString = dojo_json.fromJson(unescape(configurationString));
				if(onComplete)
					onComplete();
			}
			else {
				ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetAndSavePluginConfiguration, 
						ier_constants.PostEncoding, ier_util.getDefaultParams(ecm_model_desktop.getAuthenticatingRepository(), dojo_lang.hitch(this, function(response)
				{	
					var configString = response.configuration;
					if(configString && configString.length > 0){
						var decoded = unescape(configString);
						if(configString && configString.id)
							configString = dojo_json.fromJson(unescape(decoded));
						else {
							decoded = unescape(configString);
							//if there are single quotes, remove the first and end set of them
							if(decoded && decoded.length > 0 && decoded[0] == "\""){
								decoded = decoded.substring(1, decoded.length - 1);
							}
							configString = dojo_json.fromJson(unescape(decoded));
						}
					}
						
					this._loadConfigurationString(configString);
					if(onComplete)
						onComplete();
				})));
			}
		}
		else {
			if(onComplete)
				onComplete();
		}
	},
	
	_loadConfigurationString: function(configString){
		if(configString && configString.id){
			this.configurationString = configString;
		}
		else {
			this.configurationString = this.configurationInitial;
		}
	},
	
	/**
	 * Sets the array of destignated file plan repository configs
	 * @param reposArray
	 */
	setFilePlanRepositoryConfigsArray: function(filePlanRepositoryConfigsArray){	
		if(filePlanRepositoryConfigsArray)
			this.configurationString[this.SELECTED_FILEPLAN_REPOSITORIES] = filePlanRepositoryConfigsArray;
	},
	
	/**
	 * remove reporting information
	 * @param reposArray
	 */
	removeReportingInformation: function(){	
		if(this.configurationString[this.REPORT_SERVER_NAME])
			delete this.configurationString[this.REPORT_SERVER_NAME];
		if(this.configurationString[this.JDBC_REPORT_DB_DATASOURCE])
			delete this.configurationString[this.JDBC_REPORT_DB_DATASOURCE];
	},

	/**
	 * Returns the report server name
	 * @returns
	 */
	getReportServerName: function(){
		if(this.configurationString)
			return this.configurationString[this.REPORT_SERVER_NAME];
	},
	
	/**
	 * Returns the JDBC name for the report database datasource
	 * @returns
	 */
	getJDBCReportDBDataSource: function() {
		if(this.configurationString)
			return this.configurationString[this.JDBC_REPORT_DB_DATASOURCE];
	},
	
	/**
	 * Clears the array of destignated file plan repository configs
	 */
	clearFilePlanRepositoryConfigsArray: function(){
		this.configurationString[this.SELECTED_FILEPLAN_REPOSITORIES] = [];
	},
	
	/**
	 * Clears the array of destignated file plan repository configs
	 */
	clearConfiguration: function(){
		this.configurationString = null;
	},
	
	/**
	 * Gets the array of file plan repository configs
	 * @returns
	 */
	getFilePlanRepositoryConfigsArray: function(){
		if(this.configurationString)
			return this.configurationString[this.SELECTED_FILEPLAN_REPOSITORIES];
		else
			return [];
	},
	
	/**
	 * Adds a new repository to the array of file plan repository ids.  You must call save.
	 * @param repositoryId
	 */
	addFilePlanRepositoryConfig: function(fileplanRepositoryConfig){
		this.configurationString[this.SELECTED_FILEPLAN_REPOSITORIES].push(fileplanRepositoryConfig);
	},
	
	/**
	 * Returns true if the provided repository id is a saved file plan repository config
	 * @param repositoryId
	 * @returns {Boolean}
	 */
	isSavedFilePlanRepository: function(repositoryId){
		var fpConfigsArray = this.configurationString[this.SELECTED_FILEPLAN_REPOSITORIES];
		for ( var i in fpConfigsArray) {
			var savedFileplanRepositoryConfig = fpConfigsArray[i];
			if(repositoryId == savedFileplanRepositoryConfig.repositoryId)
				return true;
		}
		return false;
	}
});

var adminConfigUtil = new _AdminConfigurationUtil();

return adminConfigUtil;

});
