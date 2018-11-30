define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/connect",
	"dojo/_base/event",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/keys",
	"dijit/form/Select",
	"ecm/widget/dialog/MessageDialog",
	"ecm/widget/layout/_RepositorySelectorMixin",
	"ecm/widget/layout/_LaunchBarPane",
	"ier/constants",
	"ier/messages",
	"ier/model/FilePlan",
	"ier/util/dialog",
	"dojo/text!./templates/ConfigurePane.html",
	"ier/widget/listView/gridModules/RowContextMenu",
	"ier/widget/listView/modules/DocInfo",
	"ier/widget/listView/modules/ConfigureToolbar",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane", // in template
	"idx/layout/BorderContainer", // in template
	"ecm/widget/Breadcrumb", // in template
	"ier/widget/listView/ContentList", // in template
	"ier/widget/FilePlanSearchBar" // in template
], function(dojo_declare, dojo_array, dojo_connect, dojo_event, dojo_lang, dojo_class, dojo_construct, dojo_style, dojo_keys, dijit_form_Select,
		ecm_widget_dialog_MessageDialog, ecm_widget_layout_RepositorySelectorMixin,
		ecm_widget_layout_LaunchBarPane, ier_constants, ier_messages, ier_model_FilePlan, ier_util_dialog, templateString, ier_widget_listView_RowContextMenu, ier_widget_listView_DocInfo,
		ier_widget_listView_ConfigureToolbar){

/**
 * @name ier.widget.layout.ConfigurePane
 * @class
 * @augments ecm.widget.layout.LaunchBarPane
 */
return dojo_declare("ier.widget.layout.ConfigurePane", [ecm_widget_layout_LaunchBarPane, ecm_widget_layout_RepositorySelectorMixin], {
	/** @lends ier.widget.layout.ConfigurePane.prototype */

	templateString: templateString,
	_messages: ier_messages,
	widgetsInTemplate: true,
	resultSet: null,

	postCreate: function() {
		this.logEntry("postCreate");
		
		this.inherited(arguments);
		this.doContentListConnections();
		
		this.setRepositoryTypes("p8");
		this.createRepositorySelector();
		this.doRepositorySelectorConnections();
		
		this.connect(ecm.model.desktop, "onChange", function(modelObject){
			if(this.repository && this.repository.isFilePlanRepository()){
				this._onFilePlanChanged(modelObject, dojo_lang.hitch(this, function(filePlan){
					if(filePlan.deleted){
						this.repository.clearFilePlans();
					}
				}));
			}
		});

		this.logExit("postCreate");
	},
	
	setBreadCrumbData: function(){
		var configureLinksWidget = this.getConfigureLinksSelectorMenuForBreadCrumb();
		
		var breadcrumbData = new Array();
		breadcrumbData.push({
			domNode: this.repositorySelector.domNode,
			preserveDom: true,
			title: ier_messages.repository
		});
		
		breadcrumbData.push({
			domNode: configureLinksWidget,
			preserveDom: true,
			title: ier_messages.configure
		});
		this.breadcrumb.setData(breadcrumbData);
	},
	
	getContentListGridModules: function() {
		var array = [];
		array.push(ier_widget_listView_RowContextMenu);
		return array;
	},
	
	getContentListModules: function() {
		var array = [];
		array.push({moduleClass: ier_widget_listView_ConfigureToolbar});
		array.push({
			moduleClass: ier_widget_listView_DocInfo,
			showPreview: false
		});
		return array;
	},

	destroy: function(){
		if(this._repositoryOnConfigureHandle){
			this.disconnect(this._repositoryOnConfigureHandle);
			delete this._repositoryOnConfigureHandle;
		}
		
		this.inherited(arguments);
	},

	/**
	 * Method called by parent container to pass additional parameters to this pane.
	 * 
	 * @param params
	 *            Contains a handle to the teamspace object to use when displaying this pane.
	 */
	setParams: function(params) {
		this.logEntry("setParams");

		if(params && params.entityType && params.className && params.repository){
			this.entityType = params.entityType;
			this.setClassName(params.className);
			this.repository = params.repository;
			
			this.setRepository(this.repository, true);
		}

		this.logExit("setParams");
	},
	
	getConfigureLinksSelectorMenuForBreadCrumb: function() {
	
		var selectOptions = [];
		
		selectOptions.push({label: '<div class="onHoldIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_holds, value: ier_constants.ClassName_Hold});
		selectOptions.push({label: '<div class="locationIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_locations, value: ier_constants.ClassName_Location});
		selectOptions.push({label: '<div class="namingPatternIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_namingPatterns, value: ier_constants.ClassName_NamingPattern});
		selectOptions.push({label: '<div class="filePlanIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_filePlans, value: ier_constants.ClassName_FilePlan});
		selectOptions.push({label: '<div class="reportIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_reportDefinitions, value: ier_constants.ClassName_ReportDefinition});
		selectOptions.push({label: '<div class="dispositionScheduleIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_dispositionSchedules, value: ier_constants.ClassName_DispositionSchedule});
		selectOptions.push({label: '<div class="dispositionActionIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_actions, value: ier_constants.ClassName_DispositionAction});
		selectOptions.push({label: '<div class="dispositionTriggerIcon configureIconSelectOptions"></div>' + ier_messages.configurePane_triggers, value: ier_constants.ClassName_DispositionTrigger});
		
		this.linkDropdown = new dijit_form_Select({
			options: selectOptions,
			id: "configureSelector_" + this.id,
			"aria-label": ier_messages.configure
		});
		dojo_class.add(this.linkDropdown.dropDown.domNode, "ecmScrollMenu");
		
		this.connect(this.linkDropdown, "onChange", function(value) {
			if(value != this.className){
				this.setClassName(value);
				this.setRepository(this.repository, true);
				this._clearFilter();
			}
		});
		
		var node = dojo_construct.create("div");
		dojo_construct.place(this.linkDropdown.domNode, node, "only");
		
		return node;
	},
	
	_clearFilter: function(){
		//clear filter value when a new custom object type is selected
		this._filterString = null;
		this._filePlanSearchBar.set("value", "");
	},

	loadContent: function() {
		this.logEntry("loadContent");
		
		this.folderContents.setContentListModules(this.getContentListModules());
		this.folderContents.setGridExtensionModules(this.getContentListGridModules());
		this.setBreadCrumbData();
		
		//this handles retrieving the default layout repository, login, and setting it as the repository if no repository has been set yet.
		this.setPaneDefaultLayoutRepository();
		
		if(this.repository){
			if(!this.className){
				this.setClassName(ier_constants.ClassName_Hold);
			}
			this.setRepository(this.repository, true);
		}
				
		this.isLoaded = true;

		this.logExit("loadContent");
	},

	/**
	 * Resets the content of this pane.
	 */
	reset: function() {
		this.logEntry("reset");

		this.setRepository(this.repository, true);

		this.logExit("reset");
	},

	setRepository: function(repository) {
		this.logEntry("setRepository");
		
		if(repository && this.selected){
			if(repository.isIERLoaded()){
				this._connectToConnections(repository);
				this._setRepositoryCompleted(repository);
			} else {
				repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
					this._connectToConnections(repository);
					this._setRepositoryCompleted(repository);
				}));
			}
		}
		
		//if we previously had a repository and now it's being nulled out, it means we are logging out.  Reset the entire contentlist.
		if(repository == null && this.repository){
			this.folderContents.reset();
			
			if(this.linkDropdown)
				this.linkDropdown.destroy();
		}

		this.logExit("setRepository");
	},
	
	_connectToConnections:function (repository){
		if(this._repositoryOnConfigureHandle){
			this.disconnect(this._repositoryOnConfigureHandle);
		}
		
		if(repository){
			this._repositoryOnConfigureHandle = this.connect(repository, "onConfigure", dojo_lang.hitch(this, function(repository, items){
				this._setRepositoryCompleted(repository, items, true);
			}));
		}
	},

	_setRepositoryCompleted: function(repository, items, noClearFilter){
		if (repository != null) {
			if(repository.isFilePlanRepository()){
				this.repository = repository;
				var refresh = true;
				if(items){
					var className = this.className;
					refresh = dojo_array.some(items, function(item){
						if(item && item.getClassName() == className){
							return true;
						}
					});
				}
				if(refresh){
					if(!noClearFilter)
						this._clearFilter();
					this._retrieveCustomObjects();
				}
			}
			else {
				ier_util_dialog.showMessage(ier_messages.no_fileplans_available);
				this.repositorySelector.getDropdown().set("value", this.repository.id);
			}
		} else {
			this.folderContents.setRepository(null);
		}
	},
	
	_onFilePlanChanged: function(modelObject, callback){
		var func = dojo_lang.hitch(this, function(changedModel){
			if(changedModel instanceof ier_model_FilePlan){
				callback(changedModel);
			}
		});
		if(dojo_lang.isArray(modelObject)){
			dojo_array.forEach(modelObject, func);
		} else {
			func(modelObject);
		}
	},

	setClassName: function(className){
		this.className = className;

		this.linkDropdown.set("value", this.className);
	},
	
	/**
	 * Retrieves the filterString value that the user typed in the search textbox and uses it to retrieve a filtered list of custom objects
	 * @param value
	 */
	_onSearchButtonClicked: function(value){
		this._filterString = value;
		this._retrieveCustomObjects();
	},
	
	_retrieveCustomObjects: function(sortColum){
		var entityType = this.className == ier_constants.ClassName_ReportDefinition ? ier_constants.EntityType_ReportDefinition : null;
		this.repository.retrieveObjects(this.className, this._filterString, dojo_lang.hitch(this, function(resultSet){
			resultSet.sortFunc = dojo_lang.hitch(this, this._retrieveCustomObjects);
			this.resultSet = resultSet;
			this._setToolbarDef();
			this.folderContents.setResultSet(resultSet);
			resultSet.onChange(resultSet);
			this.resize();
			}), true, null, entityType, sortColum);
	},

	_setToolbarDef: function(){
		if(this.className && this.resultSet){
			if(this.className == ier_constants.ClassName_Hold)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERHoldsConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_Location)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERLocationsConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_DispositionSchedule)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERDispositionSchedulesConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_DispositionAction)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERActionsConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_DispositionTrigger)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERTriggersConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_FilePlan)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERFilePlansConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_NamingPattern)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERNamingPatternsConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_RecordType)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERRecordTypesConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_TransferMapping)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERTranferMappingsConfigureToolbarMenu;
			if(this.className == ier_constants.ClassName_ReportDefinition)
				this.resultSet.toolbarDef = ier_constants.MenuType_IERReportDefinitionsConfigureToolbarMenu;
		}
	},

	doContentListConnections: function() {
		this.logEntry("doContentListConnections");
		
		this.connect(this._filePlanSearchBar, "onSearchButtonClicked", "_onSearchButtonClicked");

		this.logExit("doContentListConnections");
	}
});});
