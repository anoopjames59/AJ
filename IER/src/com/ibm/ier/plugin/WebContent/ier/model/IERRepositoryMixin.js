define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ecm/widget/dialog/LoginDialog",
	"ier/constants",
	"ier/model/FilePlanRepositoryMixin",
	"ier/model/RecordEntryTemplate",
	"ier/util/dialog",
	"ier/util/util",
	"ier/model/SearchTemplate"
], function(dojo_declare, dojo_lang, dojo_connect, ecm_model_desktop, ecm_model_Request, ecm_widget_dialog_LoginDialog,
		ier_constants, ier_model_FilePlanRepositoryMixin, ier_model_RecordEntryTemplate, ier_util_dialog, ier_util, SearchTemplate){

/**
 * @name ier.model.IERRepositoryMixin
 * @class Represents a mixin class to ecm.model.Repository
 */
return dojo_declare("ier.model.IERRepositoryMixin", null, {
	
	/**
	 * Current user directories {@link ecm.model.Directory}
	 */
	currentUserDirectories: null,
	
	/**
	 * Current authenticating user {@link ecm.model.User}
	 */
	currentUser: null,
	
	/**
	 * Default file plan for this repository
	 */
	defaultFilePlan: null,
	
	/**
	 * The default report out save directory.  This is used for report tasks.
	 */
	reportOutputSaveDirectory: null,
	
	/**
	 * DefensibleSweep settings for the repository
	 * 
	 * defensibleSweepAlwaysDeclareRecord
	 * defensibleSweepAlwaysShowDeclareResult
	 */
	defensibleSweepSettings: null,
	
	/**
	 * Do not place variables that are objects or arrays.  They will be static.
	 * Initialize them inside of the constructor instead.
	 */

	/**
	 * Constructor for mixin
	 */
	constructor: function(id, name, recordType) {
		this.declaredClass = "ecm.model.Repository";

		this.id = id;
		this.name = name;
		this.recordRepositoryType = recordType;

		//variables that are obtained after a logon..should be nulled out below in onDisconnect()
		this._recordEntryTemplates = [];
		// stores and caches all associated content repositories (ROS)
		this._contentRepositories = [];

		this.ierLoaded = false; //indicates the repository has successfully loaded
		this.isIERLoading = false; //indicates that the repository is in the process of loading
		
		this.defensibleSweepSettings = {};
	},

	/**
	 * Loads the current repository with information when it needs to be used.  This is to prevent the repository object 
	 * from loading and obtaining all the information initially.
	 * @param onComplete
	 * @param desktopId - optional parameter to perform the login if necessary to a different desktop
	 * @param onCancel - function to invoke if a login is shown and the user cancels out of it
	 */
	loadIERRepository: function(onComplete, desktopId, onCancel){

		var repository = this.getRepository(this.id);
		if(repository.connected && !repository.isIERLoaded()){
			
			var recordType = repository.getRecordType();
			//if it's a plain or content repository, there's nothing else to load.  Set the isLoaded and return.
			if(recordType && (recordType == ier_constants.RepositoryType_Plain || recordType == ier_constants.RepositoryType_Content)){
				this.ierLoaded = true;
				
				if(onComplete)
					onComplete(repository);
			}
			else {
				//if the information is available in the repository attributes which is set on login, use it
				var repositoryAttributes = repository.attributes;
				if(repositoryAttributes && repositoryAttributes.recordRepositoryType && repositoryAttributes.recordDatamodelType && repositoryAttributes.fileplans){
					this.setIERRepositoryInfo(repositoryAttributes);
					
					if(onComplete)
						onComplete(repository);
				}
				else {
					
					//if the repository has not started loading already
					if(!this.isIERLoading){
						this.isIERLoading = true;

						//if not, then obtain it by making another service call to load a single repository
						ecm_model_desktop.loadRepositories([repository.id], dojo_lang.hitch(this, function(repositories){
							if(onComplete)
								onComplete(repository);
						}));
					}
				}
				
			}
		} else {
			if(!repository.connected){
				var loginDialog = new ecm_widget_dialog_LoginDialog();
				if(desktopId)
					loginDialog.setDesktopId(desktopId);
				
				var cancelHandler = null;
				cancelHandler = dojo_connect.connect(loginDialog, "onCancel", dojo_lang.hitch(this, function(){
					if(onCancel){
						onCancel();
					}
					dojo_connect.disconnect(cancelHandler);
				}));
				
				loginDialog.connectToRepository(repository, dojo_lang.hitch(this, function(){
					this.loadIERRepository(onComplete);
					dojo_connect.disconnect(cancelHandler);
				}));
				
				ier_util_dialog.manage(loginDialog);
			} else {
				if(repository.isIERLoaded() && onComplete){
					onComplete(repository);
				}
			}
		}
	},
	
	/**
	 * Sets the IER repository information
	 * @param repo
	 * @param repositoryJSON
	 */
	setIERRepositoryInfo: function(repositoryJSON){
		var repository = this.getRepository(this.id);
		
		//any additional attributes set in the repositoryJSON is automatically mixed into the repository's attributes
		if(repositoryJSON.additionalAttributes)
			dojo_declare.safeMixin(repository, repositoryJSON.additionalAttributes);
		
		if(repositoryJSON.securityRunDate)
			repository.securityRunDate = repositoryJSON.securityRunDate;
		
		//set the rest of the repository's types and privileges
		repository.setRecordType(repositoryJSON.recordRepositoryType);
		repository.setRecordDatamodelType(repositoryJSON.recordDatamodelType);
		
		//set repository settings
		repository.defaultFilePlan = repositoryJSON.defaultFilePlan;
		repository.reportOutputSaveDirectory = repositoryJSON.reportOutputSaveDirectory;
		
		//set defensible disposal settings
		repository.defensibleSweepSettings.defensibleSweepAlwaysDeclareRecord = repositoryJSON.defensibleSweepAlwaysDeclareRecord;
		repository.defensibleSweepSettings.defensibleSweepAlwaysShowDeclareResult = repositoryJSON.defensibleSweepAlwaysShowDeclareResult;
		repository.defensibleSweepSettings.defensibleDisposalRecordContainerId = repositoryJSON.defensibleDisposalRecordContainerId;
		repository.defensibleSweepSettings.defensibleDisposalWorkflowId = repositoryJSON.defensibleDisposalWorkflowId;
		
		this._setupPrivileges(repository, repositoryJSON.privileges);
		this._mixinFilePlanRepository(repositoryJSON.fileplans);
		repository.ierLoaded = true;
		repository.isIERLoading = false;
		repository.onIERLoaded(repository);
	},
	
	_setupPrivileges: function(repository, privilegesJSON){
		if(privilegesJSON)
			dojo_declare.safeMixin(repository.privileges, privilegesJSON);
	},
	
	/**
	 * Mix in file plan repository information
	 * @param repository
	 */
	_mixinFilePlanRepository: function(filePlansJSON){
		var repository = this.getRepository(this.id);
		var recordType = repository.getRecordType();
		if(recordType != null) {
			if (recordType == ier_constants.RepositoryType_FilePlan || recordType == ier_constants.RepositoryType_Combined){
				var ierRepository = new ier_model_FilePlanRepositoryMixin(repository.id, repository.name, recordType, repository.getRecordDatamodelType(), repository, filePlansJSON);
				dojo_declare.safeMixin(repository, ierRepository);
			}
		}
	},

	/**
	 * Determines if the current IER repository object has loaded all of its information.
	 * Only use a repository if it's loaded.  If it's not loaded, call the loadIERRepository() function before continuing use.
	 * @returns
	 */
	isIERLoaded: function(){
		return this.ierLoaded;
	},

	/**
	 * Event invoked when the repository has successfully loaded all of its information
	 */
	onIERLoaded: function(repository) {

	},

	/**
	 * Returns the repository type: Plain, Content, FilePlan, Combined
	 * @returns
	 */
	getRepositoryType: function(){
		return this.recordRepositoryType;
	},

	/**
	 * Returns whether this repository is a combined object store
	 * @returns {Boolean}
	 */
	isCombined: function() {
		return this.getRepositoryType() == "Combined";
	},

	/**
	 * Returns whether this repository is a content repository.
	 * @returns {Boolean}
	 */
	isContentRepository: function() {
		return (this.getRepositoryType() == "Combined" || this.getRepositoryType() == "Content");
	},

	/**
	 * Returns whether this repository is a file plan repository
	 * @returns {Boolean}
	 */
	isFilePlanRepository: function() {
		return (this.getRepositoryType() == "Combined" || this.getRepositoryType() == "FilePlan");
	},

	/**
	 * Returns whether this repository is a plain repository
	 * @returns {Boolean}
	 */
	isPlainRepository: function() {
		return (this.getRepositoryType() == "Plain");
	},

	/**
	 * Returns the repository object itself
	 * @returns
	 */
	getRepository: function() {
		return ecm_model_desktop.getRepository(this.id);
	},

	/**
	 * Retrieves all the record entry templates available for the current repository
	 * @param callback
	 * @param filter
	 * @param folderId
	 * @returns
	 */
	retrieveRecordEntryTemplates: function(callback) {
		this.logEntry("retrieveRecordEntryTemplates");

		if(this._recordEntryTemplates && this._recordEntryTemplates.length == 0) {

			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				this._recordEntryTemplates = [];
				this._recordEntryTemplates.push(new ier_model_RecordEntryTemplate(null, ""));

				for ( var i in response.datastore.items) {
					var entryTemplateJSON = response.datastore.items[i];
					var entryTemplate = new ier_model_RecordEntryTemplate({
						id: entryTemplateJSON.template_id,
						name: entryTemplateJSON.template_name,
						description: entryTemplateJSON.template_desc,
						repository: this
					});
					this._recordEntryTemplates.push(entryTemplate);
				}
				if (callback) {
					callback(this._recordEntryTemplates);
				}
			}));
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetRecordEntryTemplates, ier_constants.PostEncoding, params); 
		}
		else {
			if (callback) {
				callback(this._recordEntryTemplates);
			}
		}

		this.logExit("retrieveRecordEntryTemplates");
	},

	retrieveAssociatedContentRepositories: function(callback) {
		this.logEntry("retrieveAssociatedContentRepositories");
		
		if(this._contentRepositories == null || this._contentRepositories.length == 0) {
			
			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				this._contentRepositories = [];
				for (var i in response.datastore.items) {
					var contentRepositoryJSON = response.datastore.items[i];							
					this._contentRepositories.push({ displayName : contentRepositoryJSON.displayName, 
						value : contentRepositoryJSON.name, textSearchType: contentRepositoryJSON.CBRSearchType,
						textSearchOptimization: contentRepositoryJSON.CBRQueryOptimization,
						textSearchRankOverride: contentRepositoryJSON.CBRQueryRankOverride});				
				}
				if (callback) {
					callback(this._contentRepositories);
				}
			}));
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetAssociatedContentRepositories, ier_constants.PostEncoding, params);
		}
		else {
			if(callback)
				callback(this._contentRepositories);
		}
			
		this.logExit("retrieveAssociatedContentRepositories");
	},
	
	retrieveIERSearchTemplates: function(callback, filter) {
		this.logEntry("retrieveIERSearchTemplates");
		if (!filter) {
			filter = "ier";
		}
		if (this.templates && this.templatesFilter == filter) {
			if (callback) {
				callback(this.templates);
			}
		} else {
			
			var params = ier_util.getDefaultParams(this.getRepository(), 
				dojo_lang.hitch(this, function(response) {
					var repository = this.getRepository(this.id);
					repository._retrieveIERSearchTemplatesCompleted(response, callback);

			}));
			
			var request = ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, 
					"ierRetrieveIERSearchTemplates", ier_constants.PostEncoding, params);
			this.templatesFilter = filter;
		}
		this.logExit("retrieveIERSearchTemplates");
		return request;
	},

	_retrieveIERSearchTemplatesCompleted: function(response, callback) {
		this.logEntry("_retrieveIERSearchTemplatesCompleted");
		var items = response.datastore.items;
		this.templates = [];
		for ( var i in items) {
			items[i].repository_id = response.repositoryId;
			var template = SearchTemplate.createFromJSON(items[i], this);
			this.templates.push(template);
		}
		if (callback) {
			callback(this.templates);
		}
		this.logExit("_retrieveIERSearchTemplatesCompleted");
	},

	retrieveIERSearchTemplate: function(docId, vsId, version, callback, errorback) {
		this.logEntry("retrieveIERSearchTemplate");
		var self = this;
		var requestParams = {
			repositoryId: this.id,
			docid: docId,
			template_name: "StoredSearch"  // template_name is used in P8RetrieveItemsActionPreOverride as class name.
		};
		if (vsId) {
			requestParams.vsId = vsId;
		}
		if (version) {
			requestParams.version = version;
		}
		var request = ecm.model.Request.invokeService("getContentItems", this.type, requestParams, dojo_lang.hitch(this, function(response) {
			self._retrieveIERSearchTemplateCompleted(response, callback);
		}), false, false, errorback);

		this.logExit("retrieveIERSearchTemplate");
		return request;
	},

	_retrieveIERSearchTemplateCompleted: function(response, callback) {
		this.logEntry("_retrieveIERSearchTemplateCompleted");
		var item = response.rows[0];
		var template = SearchTemplate.createFromJSON(item, this);
		if (callback) {
			callback(template);
		}
		this.logExit("_retrieveIERSearchTemplateCompleted");
	},

	retrieveObjectStorePermissions: function(callback) {
		this.logEntry("retrieveObjectStorePermissions");
		// Retrieve permission from the server

		if (this.objectStorePermissions) {
			if (callback) {
				callback(this.objectStorePermissions);
			}
		} else {
			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				this.objectStorePermissions = ecm.model.Permission.createFromJSON(response.acl);
				if (callback) {
					callback(this.objectStorePermissions);
				}
			}));
			params.requestParams[ier_constants.Param_FilePlanRepositoryId] = this.id;

			ecm.model.Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetObjectStoreSecurity, ier_constants.PostEncoding, params);
		}

		this.logExit("retrieveObjectStorePermissions");
	},

	/**
	 * Retrieve the permissions for this repository.
	 * 
	 * @param callback
	 *            A callback function called after the permission have been retrieved. Passes an array of
	 *            {@link ecm.model.Permission} objects as a parameter.
	 */
	retrievePermissions: function(callback) {
		this.logEntry("retrievePermissions");
		// Retrieve permission from the server

		if (this.permissions) {
			if (callback) {
				callback(this.permissions);
			}
		} else {
			var params = ier_util.getDefaultParams(this.getRepository(), dojo_lang.hitch(this, function(response) {
				this.permissions = ecm.model.Permission.createFromJSON(response.acl);
				if (response.modifyPermissions) {
					this["modifyPermissions"] = response.modifyPermissions || false;
				}
				if (callback) {
					callback(this.permissions);
				}
			}));
			params.requestParams[ier_constants.Param_FilePlanRepositoryId] = this.id;

			ecm.model.Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetRepositoryPermissions, ier_constants.PostEncoding, params);
		}

		this.logExit("retrievePermissions");
	},

	/**
	 * Obtains the current user (ecm.model.User)
	 */
	getCurrentUser: function(callback){
		if(this.currentUser){
			if(callback)
				callback(this.currentUser);
		} else {
			this.getUser(ecm_model_desktop.getAuthenticatingRepository().userId, dojo_lang.hitch(this, function(user){
				if(user){
					this.currentUser = user;
					if(callback)
						callback(user);
				}
			}));
		}
	},
	
	/**
	 * Obtains the User or Group related to the userId
	 */
	getUser: function(userId, callback){
		var repository = this.getRepository();
		if(this.currentUserDirectories){
			repository.findUsers(dojo_lang.hitch(this, function(users){
				if(callback)
					callback(users[0]);
			}), directories[0], userId, null, null, "1", ier_constants.PrincipalSearchAttribute_ShortName);
		}
		else {
			repository.getDirectories(dojo_lang.hitch(this, function(directories){
				this.curentUserDirectories = directories;
				if(directories && directories.length > 0){
					for(var i in directories){
						var directory = directories[i];
						var user = null;
						if(user == null){
							repository.findUsers(dojo_lang.hitch(this, function(users){
								if(users && users[0]){
									user = users[0];
								}
								if(callback)
									callback(user);
							}), directory, userId, null, null, "1", ier_constants.PrincipalSearchAttribute_ShortName);
						}
					}
					
				}
			}));
		}
	},
	
	/**
	 * Returns whether the user who logged into this repository is a records administrator or manager
	 */
	isRecordsManagerOrAdministrator: function(){
		return (this.attributes && this.attributes.isRecordsAdministratorAndManager == true);
	},

	/**
	 * Disconnects the repository by removing all the privileges and clearing out all session related variables
	 */
	onDisconnected: function(repository) {

		//null out the variables when the session ends
		this._recordEntryTemplates = [];
		this._contentRepositories = [];
		
		//remove all privileges stored from the previous user
		for(var property in this){
			if(ier_util.startsWith(property, "privIER")){
				delete this[property];
			}
		}

		this.ierLoaded = false;
		
		if(this.onIERLogOff){
			this.onIERLogOff();
		}
	}
});});
