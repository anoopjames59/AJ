define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/_base/sniff",
    	"dojo/dom-style",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"dijit/form/Button",
    	"dijit/registry",
    	"ecm/Messages",
    	"ecm/model/_ModelStore",
    	"ecm/model/Action",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ecm/model/admin/ApplicationConfig",
    	"ecm/widget/admin/AdminLoginDialog",
    	"ecm/widget/admin/PluginConfigurationPane",
    	"ecm/widget/dialog/MessageDialog",
    	"ier/widget/layout/AdminiPane", //forceload the compressed version
    	"ier/constants",
    	"ier/messages",
    	"ier/util/dialog",
    	"ier/util/util",
    	"ier/widget/admin/AdminCheckBox",
    	"ier/widget/admin/config",
    	"ier/model/admin/Config",
    	"dojo/text!./templates/PluginConfigurationPane.html",
    	"dijit/layout/BorderContainer", // in template
    	"dijit/layout/ContentPane", // in template
    	"ecm/widget/FilterTextBox", // in template
    	"ier/widget/admin/AdminGrid", // in template
    	"idx/layout/MoveableTabContainer", //in template
    	"ier/widget/admin/ReportSettingsPane", //in template
    	"ier/widget/admin/AddDesktopSettingsPane" //in template
], function(dojo_declare, dojo_lang, dojo_sniff, dojo_style, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_form_Button, dijit_registry,
		ecm_messages, ecm_model_ModelStore, ecm_model_Action, ecm_model_desktop, ecm_model_Request, ecm_model_admin_appCfg,
		ecm_widget_admin_AdminLoginDialog, ecm_widget_admin_PluginConfigurationPane, ecm_widget_dialog_MessageDialog, ier_widget_AdminiPane,
		ier_constants, ier_messages, ier_util_dialog, ier_util, ier_widget_admin_AdminCheckBox, ier_widget_admin_config, ier_model_admin_Config,
		templateString){
	

/**
 * @name ier.widget.admin.PluginConfigurationPane
 * @class
 * @augments ecm.widget.admin.PluginConfigurationPane
 */
return dojo_declare("ier.widget.admin.PluginConfigurationPane", [ ecm_widget_admin_PluginConfigurationPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin ], {
	/** @lends ier.widget.admin.PluginConfigurationPane.prototype */

	templateString: templateString,
	widgetsInTemplate: true,
	messages: ier_messages,
	ecmMessages: ecm_messages,
	adminConfig: null,
	
	IS_CONNECTED: "isConnected",
	IS_FILEPLAN_REPOSITORY: "isFilePlanRepository",

	constructor: function() {
		this._availableRepositories = [];
		this.adminConfig = ier_widget_admin_config;
	},
	
	//null out implementation
	startup: function() {
		
	},

	/**
	 * Called by administration to load the configuration settings into the interface. Plug-in writers should override
	 * this method to load the settings for the plug-in's configuration into the configuration interface. This might
	 * also be called by administration to reset the settings displayed in the configuration interface.
	 * 
	 * @param onComplete
	 *            A function to be invoked when the load completes.
	 */
	load: function(onComplete) {
		if(ier_util.isCompatibleIERVersion()){
			if(this.adminConfig){
				this.adminConfig.loadConfiguration(null, this.configurationString);
				var fpConfigsArray = this.adminConfig.getFilePlanRepositoryConfigsArray();
				this.savedFPPRepositoryIds = [];
				for(var i in fpConfigsArray){
					this.savedFPPRepositoryIds.push(fpConfigsArray[i].repositoryId);
				}
				this.initialize();
				
				ier_model_admin_Config.getConfig("settings", null, dojo_lang.hitch(this, function(config){
					this.config = config;
					//copy the old admin config values to new config
					//we are not storing these values in the config database rather than the configuration string
					var newCognosGatewayServerName = config.get("cognosGatewayServerName");
					if((newCognosGatewayServerName == null || newCognosGatewayServerName == "") && 
							(this.adminConfig.getReportServerName() && this.adminConfig.getReportServerName() != "")){
						
						this.config.set("cognosGatewayServerName", this.adminConfig.getReportServerName());
						newCognosGatewayServerName =  this.adminConfig.getReportServerName();
					}
					
					var newReportJDBCSource = config.get("reportEngineDataSource");
					if((newReportJDBCSource == null || newReportJDBCSource == "") && 
							(this.adminConfig.getJDBCReportDBDataSource() && this.adminConfig.getJDBCReportDBDataSource() != "")){
						
						this.config.set("reportEngineDataSource", this.adminConfig.getJDBCReportDBDataSource());
						newReportJDBCSource = this.adminConfig.getJDBCReportDBDataSource();
					}
					
					this.reportSettings.set("config", config);
					this.reportSettings.reset();
				}));
			}
		}
		
		this.connect(this.reportSettings, "onChange", function(){
			this._saveData();
			this.onSaveNeeded(true);
		});
		
		this.tabContainer.startup();
		
		if(onComplete)
			onComplete();
	},
	
	/**
	 * Called by administration to validate the current values in the plug-in configuration interface. Plug-in writers
	 * should override this method, returning false if any values are not valid, and also focus on the field and
	 * indicate the invalid field.
	 */
	validate: function() {
		return true;
	},

	/**
	 * Called by administration to save the configuration settings displayed by this interface. Plug-in writers should
	 * override this method to save the settings for the plug-in's configuration interface into the configuration
	 * database.
	 * 
	 * @param onComplete
	 *            A function to be invoked when the load completes.
	 */
	save: function(onComplete) {
		if(this.adminConfig){
			var fpRepoConfigs = [];
			for(var i in this._repositories){
				var repoConfig = this._repositories[i];
				if(repoConfig.getValue(this.IS_FILEPLAN_REPOSITORY) == "true")
					fpRepoConfigs.push({
						repositoryId: repoConfig.id
					});
			}
			this.adminConfig.setFilePlanRepositoryConfigsArray(fpRepoConfigs);
			
			this.reportSettings.save();
			ier_model_admin_Config.saveConfig(this.config, dojo_lang.hitch(this, function(){
				//remove the old reporting information from the configuration string
				this.adminConfig.removeReportingInformation();
				//save the configuration string
				this.configurationString = this.adminConfig.getEscapedConfigurationString();
			}));
		}
	},
	
	postCreate: function() {
		this.inherited(arguments);

		this.connect(this._filter, "_onInput", "_filterData");
		this.connect(this._filter, "_setValueAttr", "_filterData");
	},
	
	/**
	 * Load all the repository configs that can be tagged as file plan repositories
	 * @param onComplete
	 */
	loadIERRepositoryConfigs: function(onComplete) {
		ecm_model_admin_appCfg.getRepositoryObjects(dojo_lang.hitch(this, function(reposObjects) {
			var repositories = [];
			this.commonDomains = [];
			for(var i in reposObjects){
				var repoObject = reposObjects[i];
				if(repoObject.getType() == "p8"){
					this._addConnectionInfoToRepositoryConfigs(repoObject);
					repositories.push(repoObject);
				}
			}
			this._repositories = repositories;
			if (onComplete) {
				onComplete();
			}
		}));
	},
	
	/**
	 * Retrieve the specified repository config 
	 * @param repositoryId
	 * @returns
	 */
	getRepositoryConfig: function(repositoryId){
		for(var i in this._repositories){
			var repoObject = this._repositories[i];
			if(repoObject.id == repositoryId){
				return repoObject;
			}
		}
		return null;
	},
	
	/**
	 * Add connection info to the repository config
	 * @param repositoryConfig
	 */
	_addConnectionInfoToRepositoryConfigs: function(repositoryConfig){
		//check to see if a repository is already authenticated for the desktop
		var repository = ecm_model_desktop.getRepository(repositoryConfig.id);
		if(repository && repository.connected){
			repositoryConfig.setValue(this.IS_CONNECTED, "true");
			this.commonDomains.push(repository.serverName);
		} else{
			//set servers as connected that are in the same domain
			var serverName = repositoryConfig.getServerName();
			for(var i in this.commonDomains){
				if(serverName == this.commonDomains[i]){
					repositoryConfig.setValue(this.IS_CONNECTED, "true");
					break;
				}
			}
		}
			
		for(var i in this.savedFPPRepositoryIds){
			if(repositoryConfig.id == this.savedFPPRepositoryIds[i]){
				repositoryConfig.setValue(this.IS_FILEPLAN_REPOSITORY, "true");
				break;
			}
		}
	},

	_getRepositories: function(callback) {
		callback(this._repositories);
	},
	
	initialize: function() {
		this.loadIERRepositoryConfigs(dojo_lang.hitch(this, this._initializeGrid));
	},

	_initializeGrid: function() {
		var structure = [ {
			name: " ",
			width: dojo_sniff.isWebKit ? "30px" : "16px",
			nosort: true,
			headerClasses: "nosort",
			formatter: dojo_lang.hitch(this, "_iconFormatter")
		}, {
			field: "name",
			name: ier_messages.pluginConfiguration_displayName,
			width: "auto"
		}, {
			field: "id",
			name: ier_messages.pluginConfiguration_ID,
			width: "auto"
		}, {
			field: "serverName",
			name: ier_messages.pluginConfiguration_server_name_heading,
			width: "auto"
		}, {
			field: this.IS_CONNECTED,
			name: this.messages.pluginConfiguration_connectionStatus,
			width: "auto",
			formatter: dojo_lang.hitch(this, "_connectionFormatter"),
			editable: true
		}, {
			field: this.IS_FILEPLAN_REPOSITORY,
			name: this.messages.pluginConfiguration_showInDeclare,
			width: "auto",
			formatter: dojo_lang.hitch(this, "_isFilePlanRepositoryFormatter"),
			editable: true
		} ];

		this._theStore = new ecm_model_ModelStore(this, this._getRepositories);
		this._theGrid.setData(structure, this._theStore, this.getMenuActions());
		this.connect(this._theGrid, "onAction", "_onAction");

		this._theGrid.resize();
		this.borderContainer.resize();
		this.tabContainer.resize();
	},
	
	getMenuActions: function() {
		var actions = [];
		actions.push(new ecm_model_Action("ierplugin_setAsFilePlan", this.messages.pluginConfiguration_setAsFilePlanRepository));
		return actions;
	},
	
	_onAction: function(items, action) {
		if (action.id == "ierplugin_setAsFilePlan") {
			var repositoryIds = [];
			for ( var i in items) {
				var repositoryConfig = items[i];
				var isConnected = repositoryConfig.getValue(this.IS_CONNECTED) == "true";
				if(isConnected)
					repositoryIds.push(repositoryConfig.id);
			}
			
			if(repositoryIds.length > 0){
				this.loadRepositories(repositoryIds, dojo_lang.hitch(this, function(repositoryObjs){
					for ( var i in repositoryObjs) {
						var repoObj = repositoryObjs[i];
						var repositoryConfig = null;
						
						for(var j in items){
							if(items[j].id == repoObj.repositoryId){
								repositoryConfig = items[j];
								break;
							}
						}
						
						if(repositoryConfig && repoObj){
							if(repoObj && (repoObj.recordRepositoryType == "Combined" || repoObj.recordRepositoryType == "FilePlan")){
		        				repositoryConfig.setValue(this.IS_FILEPLAN_REPOSITORY, "true");
		    					this._theGrid.getStore().onSet(repositoryConfig);
		        			}
		        			else {
		        				//set it to false to indicate that the repository has been checked and will not need to be rechecked.
		        				repositoryConfig.setValue(this.IS_FILEPLAN_REPOSITORY, "false");
		        				
		        				if(items.length == 1){
		        					ier_util_dialog.showMessage(ier_messages.pluginConfiguration_fileplanRepositoryNotAdded);
		        				}
		        				
		        				this._theGrid.getStore().onSet(repositoryConfig);
		        			}
						}
					}
        			
        		}));
			}
		}
	},

	_onReset: function() {
		this._filter.reset();
		this._theGrid.clearSelection();
		if (this._theStore) {
			this.loadIERRepositoryConfigs(dojo_lang.hitch(this, function() {
				this._theStore = new ecm_model_ModelStore(this, this._getRepositories);
				this._theGrid.setStore(this._theStore);
			}));
		}
	},

	_filterData: function() {
		var value = this._filter.get("value");
		this._theGrid.filter({
			name: "*" + value + "*"
		});
	},
	
	_saveData: function() {
		//due to a bug in Nexus where save doesn't work when the plugin is initially loaded for the first time.
		//we are explicitly saving everytime.
		if(!this.configurationString || this.configurationString.length == 0 || this.initialLoad){
			//if configurationString is every null, it means that the plugin is loading for the very first time. 
			this.initialLoad = true;
    		this.save();
    	}
	},

	_iconFormatter: function(data, rowIndex, cell) {
		var IconValue = function(iconClass, hover) {
			this.iconClass = iconClass;
			this.hover = hover;
		};
		IconValue.prototype = new Object();
		IconValue.prototype.toString = function() {
			return '<img role="presentation" class="' + this.iconClass + '" title="' + this.hover + '" alt="' + this.hover + '" src="dojo/resources/blank.gif" />';
		};
		IconValue.prototype.replace = function() {
			return this;
		};
		var item = cell.grid.getItem(rowIndex);
		return new IconValue(item.getTypeIconClass(), item.getTypeString());
	},
	
	/**
	 * Formatter for the isFilePlanRepositoryForDeclare column
	 * @param data
	 * @param rowIndex
	 * @param cell
	 * @returns {___checkBox0}
	 */
	_isFilePlanRepositoryFormatter: function(data, rowIndex, cell){
		var self = this;
		var repositoryConfig = cell.grid.getItem(rowIndex);
		var checked = repositoryConfig.getValue(this.IS_FILEPLAN_REPOSITORY) == "true";
		var isConnected = repositoryConfig.getValue(this.IS_CONNECTED) == "true";
		
		var checkBox = new ier_widget_admin_AdminCheckBox({
			label: repositoryConfig.name
		});
		checkBox.set("checked", checked);
		this.connect(checkBox, "onClickAction", function(checkbox, state) {
				var checked = checkbox.get("checked");
				
				//set the state of the checbox if it's not provided by the click action
				if(state != null && state == checked){
					checkbox.set("checked", !state);
					checked = checkbox.get("checked");
				}
					
	        	if((checked || checked == "true")){
	        		if(repositoryConfig.getValue(this.IS_FILEPLAN_REPOSITORY) == "false"){
	        			ier_util_dialog.showMessage(ier_messages.pluginConfiguration_fileplanRepositoryNotAdded);
        				checkbox.set("checked", false);
        				dojo_style.set(checkbox.domNode, "display", "none");
	        		}
	        		else {
		        		this.loadRepositories([repositoryConfig.id], dojo_lang.hitch(this, function(repositories){
		        			if(repositories && (repositories[0].recordRepositoryType == "Combined" || repositories[0].recordRepositoryType == "FilePlan")){
		        				repositoryConfig.setValue(this.IS_FILEPLAN_REPOSITORY, "true");
		        				
		        				this._saveData();
		        			}
		        			else {
		        				//set it to false to indicate that the repository has been checked and will not need to be rechecked.
		        				repositoryConfig.setValue(this.IS_FILEPLAN_REPOSITORY, "false");
		        				ier_util_dialog.showMessage(ier_messages.pluginConfiguration_fileplanRepositoryNotAdded);
		        				checkbox.set("checked", false);
		        				dojo_style.set(checkbox.domNode, "display", "none");
		        			}
		        		}));
	        		}
	        	}
	        	else {
	        		//set it to null so it can be reevaluated
    				repositoryConfig.setValue(this.IS_FILEPLAN_REPOSITORY, null);
    				
    				this._saveData();
	        	}
	        	
	        	self.onSaveNeeded(true);
	    });
		
		if(!isConnected)
			checkBox.set("disabled", true);
		else
			checkBox.set("disabled", false);
		
		checkBox._destroyOnRemove = true;
		
		if(repositoryConfig.getValue(this.IS_FILEPLAN_REPOSITORY) == "false"){
			dojo_style.set(checkBox.domNode, "display", "none");
		}
		
		return checkBox;
	},
	
	/**
	 * Formatter for connection status
	 * @param data
	 * @param rowIndex
	 * @param cell
	 * @returns
	 */
	_connectionFormatter: function(data, rowIndex, cell){
		var grid = cell.grid;
		var repositoryConfig = cell.grid.getItem(rowIndex);
		var isConnected = repositoryConfig.getValue(this.IS_CONNECTED) == "true";
		if(isConnected) {
			repositoryConfig.buttonCellWidgetView = null;
			return '<div align="center"><span style="text-align:center;font-color:green">' + ier_messages.pluginConfiguration_connected + '</span></div>';
		}
		else {
			var button = new dijit_form_Button({
			id: this.id + "_connectionButton_" + repositoryConfig.id + new Date().getTime(),
			label: ecm_messages.connect,
			//in order to solve the accessibilty problem of invoking the button within the dojo grid, I had to define
			//a separate click action so it can be invoked based on keyboard commands.  OnClick is blocked on purpose by browsers
		    //for security reasons.
			clickAction: dojo_lang.hitch(this, function(button){
				this.connectToRepository(repositoryConfig, dojo_lang.hitch(this, function(evt){
					repositoryConfig.setValue(this.IS_CONNECTED, "true");
					grid.store.onSet(repositoryConfig);
					
					for(var i = 0; i < grid.rowCount; i++){
						var currentCellConfig = grid.getItem(i);
						if(currentCellConfig.getValue(this.IS_CONNECTED) != "true"){
							for(var j in self.commonDomains){
								if(currentCellConfig.getServerName() == self.commonDomains[j]){
									currentCellConfig.setValue(this.IS_CONNECTED, "true");
									grid.store.onSet(currentCellConfig);
								}
							}
						}
					}
					setTimeout(function(grid) {
						grid.focus.setFocusIndex(grid.selection.selectedIndex, 4);
					}, 500, grid);
				}));
			}),
		    onClick: dojo_lang.hitch(this, function(evt){
		    	var button = dijit_registry.getEnclosingWidget(evt.target);
		    	if(button.clickAction){
		    		button.clickAction(button);
		    	}
		    })});
			button._destroyOnRemove = true;
			button.domNode.setAttribute("style", "text-align:center; margin:0 auto; width: 100%");
			return button;
		}
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
			if (response.servers) {
				for ( var i in response.servers) {
					var repositoryJSON = response.servers[i];
					var repositoryObj = {
						repositoryId : repositoryJSON.repositoryId,
						recordRepositoryType : repositoryJSON.recordRepositoryType,
						recordDatamodelType : repositoryJSON.recordDatamodelType
					};
					repositories.push(repositoryObj);
				}
				
				if(onComplete)
					onComplete(repositories);
				}
		});
		
		var repositories = [];
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetRepositoryAttributes, ier_constants.PostEncoding, params);
	},
	
	connectToRepository: function(repositoryConfig, onComplete) {
		if (!this._loginDialog) {
			this._loginDialog = new ecm_widget_admin_AdminLoginDialog();
		}
		this._loginDialog.show(repositoryConfig.getType(), repositoryConfig.id, repositoryConfig.name, this.getLogonParams(repositoryConfig), 
				dojo_lang.hitch(this, function(response) {
					this.commonDomains.push(repositoryConfig.getServerName());
					if(onComplete){
						onComplete();
					}
		}));
	},
	
	getLogonParams: function(repositoryConfig) {
		var params = {};
		params.displayName = repositoryConfig.name;
		if (repositoryConfig.getType() == "p8") {
			//server name can be in an array if it's an HA environment
			var serverName = repositoryConfig.getServerName();
			if(dojo_lang.isArray(serverName)){
				for(var i in serverName.length){
					params.serverName = params.serverName + serverName[i];
				}
			}
			else
				params.serverName = serverName;
			params.objectStore = repositoryConfig.getObjectStore();
			params.objectStoreDisplayName = repositoryConfig.getObjectStoreDisplayName();
			params.protocol = repositoryConfig.getProtocol();
		}
		return params;
	}

});});
