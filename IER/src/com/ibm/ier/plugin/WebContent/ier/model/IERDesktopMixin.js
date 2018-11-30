define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ier/constants",
	"ier/model/IERRepositoryMixin",
	"ier/model/ReportResult",
	"ier/util/util",
	"ier/widget/admin/config"
], function(dojo_declare, dojo_lang, ecm_model_desktop, ecm_model_Request, ier_constants, ier_model_IERRepositoryMixin, ier_model_ReportResult,
		ier_util, ier_widget_admin_config){

/**
 * @name ier.model.IERDesktopMixin
 * @class Represents an extension of the ecm.model.Desktop for IER usage.
 */
return dojo_declare("ier.model.IERDesktopMixin", null, {
	/** @lends ier.model.IERDesktop.prototype */
	
	/**
	 * All related report settings for the desktop
	 * cognosGatewayServerName
	 * cognosDispatchServletServerName
	 * cognosReportPath
	 */
	reportSettings: null,
	
	/**
	 * All defensible disposal sweep settings for the desktop
	 */
	defensibleSweepSettings: null,
	
	constructor: function() {
		this.declaredClass = "ecm.model.Desktop";
		this.appLayoutName = "ier_widget_layout_IERAppLayout";
		this._currentFilePlan = null;
		this._currentFilePlanId = null;
		this.reportResults = [];
		this.reportSettings = {};
		this.defensibleSweepSettings = {};
	},

	/**
	 * Returns the current fileplan being browsed.  It will return null if no file plans have been browsed yet.
	 * @returns
	 */
	getCurrentFilePlan: function(){
		return this._currentFilePlan;
	},
	
	/**
	 * Returns the ID of the current fileplan being browsed. If no fileplan is being browsed yet, default fileplan Id will be returned instead.  The default fileplan Id will be a P8 id
	 * and not a docId.
	 */
	getCurrentFilePlanId: function(){
		if(!this._currentFilePlanId)
			return ier_constants.Id_FilePlanFolder;
		else
			return this._currentFilePlanId;
	},
	
	/**
	 * Sets the current fileplan being browsed
	 * @param filePlan
	 */
	setCurrentFilePlan: function(filePlan){
		if(filePlan){
			this._currentFilePlan = filePlan;
			this._currentFilePlanId = filePlan.id;
		}
	},
	
	/**
	 * This is called to clear any cached items pertaining to the current user's session.  
	 * IMPORTANT: If anything was cached or stored for the user,
	 * it must be nullified here!
	 */
	clearIERSessionContent: function(){
		this._currentFilePlan = null;
		this._currentFilePlanId = null;
		this.reportResults = [];
		
		//loop through and unset all the loaded repositories to clear out any information
		var repositories = this.getP8Repositories();
		if (repositories) {
			for ( var i in repositories) {
				var repository = repositories[i];
				if(repository.isIERLoaded && repository.isIERLoaded == true){
					repository.isIERLoaded = false;
				}
			}
		}
	},
	
	getIERAppLayoutName: function() {
		return this.appLayoutName;
	},
	
	/**
	 * Get report results for the current user
	 */
	getReportResults: function(callback) {
		var reportResults = [];
		var params = ier_util.getDefaultParams(ecm_model_desktop.getAuthenticatingRepository(), dojo_lang.hitch(this, function(response)
		{	
			if(response.reportResults){
				for (var i in response.reportResults) {
					reportResults.push(response.reportResults[i]);
				}
			}
			if(callback){
				callback(reportResults);
			}
		}));
		
		params.requestParams[ier_constants.Param_UserId] = ecm_model_desktop.getAuthenticatingRepository().userId;
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetReportResult, ier_constants.PostEncoding, params);
	},
	
	refreshReportResults: function(callback){
		this.reportResults = [];
		this.getReportResults(callback);
	},
	
	/**
	 * Return a list of non-enabled IER repositories.  The repositories can only be p8 and has to be connected.
	 */
	getNonEnabledIERRepositoryIds: function(){
		var repositories = ecm_model_desktop.repositories;
		var nonEnabledIERRepositories = [];
		for ( var i in repositories)
		{
			var repository = repositories[i];
			if(repository.connected && repository.type == "p8" && !repository.isIEREnabled){
				nonEnabledIERRepositories.push(repository.id);
			}
		}
		return nonEnabledIERRepositories;
	},
	
	/**
	 * Return a list of IER-enabled repositories.
	 * @returns {Array}
	 */
	getIERRepositories: function() {
		var repositories = ecm_model_desktop.repositories;
		var result = [];
		for ( var i in repositories)
		{
			var repository = repositories[i];
			if(repository.connected && repository.isIEREnabled());
				result.push(repository);
		}
		return result;
	},
	
	/**
	 * Return a list of only p8 repositories.  These repositories may not have been IER-enabled yet.
	 * @returns {Array}
	 */
	getP8Repositories: function() {
		var repositories = ecm_model_desktop.repositories;
		var result = [];
		for ( var i in repositories)
		{
			var repository = repositories[i];
			if(repository.type == "p8"){
				result.push(repository);
			}
		}
		return result;
	},
	
	/**
	 * Return a list of only p8 repositories.  These repositories may not have been IER-enabled yet.
	 * @returns {Array}
	 */
	getRepositoryFromSymbolicAndConnection: function(objectStoreName, connectionUrl) {
		var repositories = ecm_model_desktop.repositories;
		for(var i in repositories){
			var repo = repositories[i];
			if(repo.objectStoreName == objectStoreName && repo.serverName == connectionUrl)
				return repo;
		}
		
		return ecm_model_desktop.getRepositoryOfObjectStore(objectStoreName);
	},
	
	/**
	 * Loads the array of repositories by making a call to grab all the additional set of information related to IER.
	 * @param repositoryIds
	 * @param onComplete
	 */
	loadRepositories: function(repositoryIds, onComplete){
		var params = null;
		if(repositoryIds.length == 1)
			params = ier_util.getDefaultParams(repositoryIds[0]);
		else {
			params = ier_util.getDefaultParams(repositoryIds[0]);
			params.requestParams[ier_constants.Param_RepositoryIds] = ier_util.arrayToString(repositoryIds);
		}
		params["requestCompleteCallback"] = dojo_lang.hitch(this, function(response)
		{
			var repositories = [];
			if (response.servers) {
				for ( var i in response.servers) {
					var repositoryJSON = response.servers[i];
					var repository = ecm_model_desktop.getRepository(repositoryJSON.repositoryId);
					if(repository)
						repository.setIERRepositoryInfo(repositoryJSON);
					else
						repositories.push(repositoryJSON);
				}
				
				if(onComplete)
					onComplete(repositories);
				}
		});
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetRepositoryAttributes, ier_constants.PostEncoding, params);
	},
	
	/**
	 * Save any additional IER-related settings onto the desktop.
	 */
	saveAdditionalAttributes: function(response){
		//reports related settings
		this.reportSettings.cognosGatewayServerName = response.cognosGatewayServerName;
		this.reportSettings.cognosDispatchServletServerName = response.cognosDispatchServletServerName;
		this.reportSettings.cognosReportPath = response.cognosReportPath;
		this.reportSettings.cognosNamespace = response.cognosReportNamespace;
			
		this.defensibleSweepSettings.defensibleSweepThreadCount = response.defensibleSweepThreadCount;
		this.defensibleSweepSettings.defensibleSweepQueryPageSize = response.defensibleSweepQueryPageSize;
		this.defensibleSweepSettings.defensibleSweepUpdateBatchSize = response.defensibleSweepUpdateBatchSize;
		this.defensibleSweepSettings.defensibleSweepContentSizeLimit = response.defensibleSweepContentSizeLimit;
		this.defensibleSweepSettings.defensibleSweepLinkCacheSizeLimit = response.defensibleSweepLinkCacheSizeLimit;
		this.defensibleSweepSettings.defensibleSweepOnHoldContainerCacheSize = response.defensibleSweepOnHoldContainerCacheSize;
	}
});});