define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/event",
	"dojo/_base/array",
	"dojo/_base/connect",
	"dojo/dom-style",
	"dojo/dom-construct",
	"dojo/dom-class",
	"dojo/data/ItemFileWriteStore",
	"dojo/keys",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"ecm/widget/layout/_RepositorySelectorMixin",
	"ecm/widget/layout/_LaunchBarPane",
	"ier/messages",
	"ier/constants",
	"dojo/text!./templates/ReportDefinitionsPane.html",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane", // in template
	"idx/layout/BorderContainer", // in template
	"ecm/model/Desktop",
	"dijit/ToolbarSeparator",
	"dijit/form/Button",
	"ier/widget/listView/gridModules/RowContextMenu",
	"ier/widget/listView/modules/DocInfo",
	"ier/widget/listView/modules/ConfigureToolbar",
	"ier/widget/listView/ContentList",
	"ier/widget/FilePlanSearchBar",
	"ecm/widget/Breadcrumb", // in template
], function(declare, dojo_lang, dojo_event, dojo_array, dojo_connect, dojo_domStyle, dojo_domConstruct, dojo_domClass, dojo_data_ItemFileWriteStore, 
		dojo_keys, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin, ecm_widget_layout_RepositorySelectorMixin, ecm_widget_layout_LaunchBarPane,
		ier_messages, ier_constants,
		templateString, dijit_BorderContainer, dijit_ContentPane, idx_BorderContainer, ecm_model_desktop, ToolbarSeparator, Button,
		 ier_widget_listView_RowContextMenu, ier_widget_listView_DocInfo, ier_widget_listView_ConfigureToolbar, ier_widget_listView_ContentList,
		 ier_widget_FilePlanSearchBar, ecm_breadcrumb){
	/**
	 * @name ReportDefintionsPane
	 * @class Implement report definitions pane with a list of reports that users can customize
	 * @augments ecm.widget.layout.LaunchBarDialogPane
	 */
	
	return declare("ier.widget.admin.ReportDefinitionsPane", [/*_Widget, _TemplatedMixin, _WidgetsInTemplateMixin, */
	                                                          ecm_widget_layout_LaunchBarPane, ecm_widget_layout_RepositorySelectorMixin], {
		/** @lends ier.widget.admin.ReportDefinitionsPane.prototype */
	templateString: templateString,
	_messages: ier_messages,
	widgetsInTemplate: true,
	resultSet: null,

	postCreate: function(){
		this.inherited(arguments);
		this.doContentListConnections();
		this.folderContents.setContentListModules(this.getContentListModules());
		this.folderContents.setGridExtensionModules(this.getContentListGridModules());
		
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
		
		var breadcrumbData = new Array();
		breadcrumbData.push({
			domNode: this.repositorySelector.domNode,
			preserveDom: true,
			title: ier_messages.repository
		});
		this.breadcrumb.setData(breadcrumbData);


		if (!this.repository){
			this.repository = ecm.model.desktop.getAuthenticatingRepository();
		}	
		this.repositorySelector.getDropdown().set("value", this.repository.id);
		
		this.setClassName(ier_constants.ClassName_ReportDefinition);
		this.setRepository(this.repository);
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
			dojo_connect.disconnect(this._repositoryOnConfigureHandle);
			delete this._repositoryOnConfigureHandle;
		}
		
		this.inherited(arguments);
	},

	loadContent: function() {
		this.logEntry("loadContent");
		
		//this handles retrieving the default layout repository, login, and setting it as the repository if no repository has been set yet.
		this.setPaneDefaultLayoutRepository();
		
		if(this.repository){
			if(!this.className){
				this.setClassName(ier_constants.ClassName_ReportDefinition);
			}
			this.setRepository(this.repository, true);
		}
				
		this.isLoaded = true;

		this.logExit("loadContent");
	},

	reset: function() {
		this.logEntry("reset");

		this.setRepository(this.repository, true);

		this.logExit("reset");
	},

	setRepository: function(repository) {
		this.logEntry("setRepository");

		if(this._repositoryOnConfigureHandle){
			dojo_connect.disconnect(this._repositoryOnConfigureHandle);
			delete this._repositoryOnConfigureHandle;
		}
		if(repository){
			this._repositoryOnConfigureHandle = dojo_connect.connect(repository, "onConfigure", dojo_lang.hitch(this, function(repository){
				this._setRepositoryCompleted(repository);
			}));
		}
		//if(repository && this.selected){
		if(repository){			
			if(repository.isIERLoaded()){
				this._setRepositoryCompleted(repository);
			} else {
				repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
					this._setRepositoryCompleted(repository);
				}));
			}
		}

		this.logExit("setRepository");
	},

	_setRepositoryCompleted: function(repository){
		if (repository != null) {
			if(repository.isFilePlanRepository()){
				this.repository = repository;
				this._retrieveCustomObjects();
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
	},
	
	/**
	 * Retrieves the filterString value that the user typed in the search textbox and uses it to retrieve a filtered list of custom objects
	 * @param value
	 */
	_onSearchButtonClicked: function(value){
		this._filterString = value;
		this._retrieveCustomObjects();
	},
	
	_retrieveCustomObjects: function(){
		this.repository.retrieveObjects(this.className, this._filterString, dojo_lang.hitch(this, function(resultSet){
			this.resultSet = resultSet;
			this._setToolbarDef();
			this.folderContents.setResultSet(resultSet);
			
			this.resize();
		}), true, null, ier_constants.EntityType_ReportDefinition);//"207");
		
		/*this.repository.retrieveReportDefinitions(dojo_lang.hitch(this, function(reportDefinitions) {
			this._setToolbarDef();
			this.folderContents.setResultSet(reportDefinitions);
			
			this.resize();
		}));*/

	},

	_setToolbarDef: function(){
		if(this.resultSet){
			this.resultSet.toolbarDef = ier_constants.MenuType_IERReportDefinitionsConfigureToolbarMenu;
		}
	},

	doContentListConnections: function() {
		this.logEntry("doContentListConnections");
		
		this.connect(this._filePlanSearchBar, "onSearchButtonClicked", "_onSearchButtonClicked");
		
		//enable enter keystroke for searching on the search bar
		this.connect(this._filePlanSearchBar, "onKeyDown", function(evt) {
			if (evt.keyCode == dojo_keys.ENTER) {
				dojo_event.stop(evt);
				this._onSearchButtonClicked(this._filePlanSearchBar.get("value"));
			}
		});

		this.logExit("doContentListConnections");
	}
});
});
