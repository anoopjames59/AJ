define([
	"dojo/_base/declare",
	"dojo/_base/event",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/aspect",
	"dijit/form/DropDownButton",
	"dijit/popup",
	"dijit/TooltipDialog",
	"dojox/layout/ResizeHandle",
	"ecm/model/Desktop",
	"ecm/widget/dialog/MessageDialog",
	"ecm/widget/layout/_RepositorySelectorMixin",
	"ecm/widget/layout/_LaunchBarPane",
	"ier/constants",
	"ier/messages",
	"ier/model/FilePlan",
	"ier/util/dialog",
	"ier/util/menu",
	"ier/util/util",
	"ier/widget/layout/FilePlanFlyoutPane",
	"dojo/text!./templates/FilePlanPane.html",
	"ier/widget/listView/gridModules/RowContextMenu",
	"ier/widget/listView/modules/DocInfo",
	"ier/widget/listView/modules/FilePlanToolbar",
	"ier/widget/dialog/ObjectStoreSecurityDialog",
	"ier/widget/FilePlanSearchBar", // in template
	"ier/widget/FolderTree", // in template
	"ier/widget/listView/ContentList", // in template
	"ier/widget/panes/ItemPropertiesDisplayPane", // in template
	"ier/widget/QuickSearchList", //in template
	"dijit/layout/StackContainer" //in template
], function(dojo_declare, dojo_event, dojo_lang, dojo_array, dojo_construct, dojo_style, dojo_aspect, dijit_form_DropDownButton, dijit_popup, dijit_TooltipDialog, dojox_layout_ResizeHandle,
		ecm_model_desktop, ecm_widget_dialog_MessageDialog, ecm_widget_layout_RepositorySelectorMixin, ecm_widget_layout_LaunchBarPane, 
		ier_constants, ier_messages, ier_model_FilePlan, ier_util_dialog, ier_util_menu, ier_util, ier_widget_layout_FilePlanFlyoutPane, templateString, ier_widget_listView_RowContextMenu, 
		ier_widget_listView_DocInfo, ier_widget_listView_FilePlanToolbar, ObjectStoreSecurityDialog){

/**
 * @name ier.widget.layout.FilePlanPane
 * @class
 * @augments ecm.widget.layout.LaunchBarPane
 */
return dojo_declare("ier.widget.layout.FilePlanPane", [ecm_widget_layout_LaunchBarPane, ecm_widget_layout_RepositorySelectorMixin], {
	/** @lends ecm.widget.layout.BrowsePane.prototype */

	templateString: templateString,
	_messages: ier_messages,
	widgetsInTemplate: true,
	defaultFilePlanId: ier_constants.DocId_FilePlanFolder,

	fileplan: null,

	postCreate: function() {
		this.logEntry("postCreate");
		
		this.inherited(arguments);
		this.doBrowseConnections();

		this.connect(ecm_model_desktop, "onLogout", function(repository) {
			this.setRepository(null);
			this.isLoaded = false;
			this.fileplan = null;
			if(this.folderContents){
				this.folderContents.reset();
			}
		});
		
		this.connect(ecm_model_desktop, "onLogin", function(repository) {
			if (this.selected) {
				var repository = this.getDefaultLayoutRepository();

				if (repository && repository.connected) {
					if (!this.isLoaded)
						this.loadContent();
					else
						this.setRepository(repository);
				} else if (repository) {
					this.connectToRepository(repository, function(repo) {
						if (!this.isLoaded)
							this.loadContent();
						else
							this.setRepository(repo);
					});
				}
			}
		});

		this.connect(ecm_model_desktop, "onChange", function(modelObject){
			this._onFilePlanChanged(modelObject, dojo_lang.hitch(this, function(fileplan){
				if(this.fileplan.id == fileplan.id && fileplan.deleted){
					var defaultFilePlan = this.repository.defaultFilePlan || ier_util.getGuidId(this.defaultFilePlanId);
					var fp = this.repository.getFilePlan(defaultFilePlan);
					if(fp){
						this.fileplan = fp;
						this.setRepository(this.repository);
					}else{
						this.fileplan = null;
						this.setRepository(null);
					}
					this.needReset = true;
				}
			}));
		});

		this._createFilePlanSelector();
		this.mainContainer.resize();

		this.logExit("postCreate");
	},

	_onFilePlanChanged: function(modelObject, callback) {
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

	getContentListModules: function() {
		var array = [];
		array.push({moduleClass: ier_widget_listView_FilePlanToolbar});
		array.push({
			moduleClass: ier_widget_listView_DocInfo,
			showPreview: false
		});
		return array;
	},
	
	getContentListGridModules: function() {
		var array = [];
		array.push(ier_widget_listView_RowContextMenu);
		return array;
	},

	/**
	 * Method called by parent container to pass additional parameters to this pane.
	 * 
	 * @param params
	 *            Contains a handle to the teamspace object to use when displaying this pane.
	 */
	setParams: function(params) {
		this.logEntry("setParams");

		if(params) {
			if(this.fileplan != params.fileplan || (this.fileplan == params.fileplan && this.repository != params.repository)) {
				this.fileplan = params.fileplan;
				if(!this.isLoaded){
					this.repository = params.repository;
					this.loadContent();
				}
				else
					this.setRepository(params.repository, true);
			}
		} else {
			if (!this.isLoaded && this.selected && this.repository)
				this.loadContent();
		}
		
		this.logExit("setParams");
	},

	loadContent: function() {
		this.logEntry("loadContent");
		
		this.folderContents.setContentListModules(this.getContentListModules());
		this.folderContents.setGridExtensionModules(this.getContentListGridModules());
		
		//this handles retrieving the default layout repository, login, and setting it as the repository if no repository has been set yet.
		this.setPaneDefaultLayoutRepository();
		
		if (this.repository && this.repository.connected) {
			this.setRepository(this.repository, true);
			this.isLoaded = true;
		}

		this.logExit("loadContent");
	},

	/**
	 * Resets the content of this pane.
	 */
	reset: function() {
		this.logEntry("reset");

		//setting the repository again in case it was set from syncing and not while it was selected
		this.setRepository(this.repository);
		this.needReset = false;

		this.logExit("reset");
	},

	setRepository: function(repository, reload) {
		this.logEntry("setRepository");
		
		//clear the tree and contentlist first before loading the items from the new repositories
		if(repository != this.repository)
			this.clear();

		this.repository = repository;
		
		if(this.selected){
			if(!repository)
				this._setRepositoryCompleted(repository);
			else {
				if(repository.isIERLoaded()){
					this._setRepositoryCompleted(repository);
				} else {
					repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
						this._setRepositoryCompleted(repository);
					}));
				}
			}
		}
		this.logExit("setRepository");
	},

	_setRepositoryCompleted: function(repository){
		if(repository && !repository.isFilePlanRepository()){
			ier_util_dialog.showMessage(ier_messages.no_fileplans_available);
		} else {
			if (repository) {
				//prompt the object store security run dialog if it has not been set yet
				if(repository.securityRunDate == null){
					if(!this.objectStoreSecurityDialog){
						this.objectStoreSecurityDialog = new ObjectStoreSecurityDialog({
							showWarning: true,
						});
						this.connect(this.objectStoreSecurityDialog, "onFinished", dojo_lang.hitch(this, function(response){
							if(response && response.securityRunDate){
								this.repository.securityRunDate = response.securityRunDate;
							}
						}));
						
						this.connect(this.objectStoreSecurityDialog, "onHide", dojo_lang.hitch(this, function(){
							this._setRepositoryCompleted(this.repository);
						}));
					}
					this.objectStoreSecurityDialog.show(this.repository);
				}
				else {
					this._loadFilePlan();
				}
			} else {
				this.folderTree.setRepository(null);
				this.folderContents.setResultSet(null);
				this.breadcrumb.clear();
			}
		}
	},
	
	_loadFilePlan: function(){
		this._filePlanDropDown.loadContent();

		var rootId = null;
		if(this.fileplan)
			rootId = this.fileplan.getGuidId();
		else {
			var defaultFilePlan = this.repository.defaultFilePlan || ier_util.getGuidId(this.defaultFilePlanId);
			this.fileplan = this.repository.getFilePlan(defaultFilePlan);
			ecm_model_desktop.setCurrentFilePlan(this.fileplan);
			rootId = this.fileplan && this.fileplan.getGuidId() || this.defaultFilePlanId;
		}

		this.folderTree.setFolder(this.fileplan);
		this.openItem(this.fileplan);
	},
	
	/**
	 * Clears the browse pane
	 */
	clear: function() {
		if (this.folderTree)
			this.folderTree.setRepository(null);

		if (this.folderContents)
			this.folderContents.setResultSet(null);
	},
	
	destroy: function(){
		if(this.objectStoreSecurityDialog)
			this.objectStoreSecurityDialog.destroy();
		
		this.inherited(arguments);
	},
	
	openItem: function(item){
		this.mainStackContainer.selectChild(this.folderContents);
		if(!this.folderContents.getResultSet() || !this.folderContents.getResultSet().isResultSetForItem(item)) {
			this.folderContents.openItem(item);
			this.mainContainer.resize();
		}
	},

	doBrowseConnections: function() {
		this.logEntry("doBrowseConnections");
		
		// onItemSelected called when user selects a node in the tree
		this.connect(this.folderTree, "onItemSelected", dojo_lang.hitch(this, function(item) {
			this.openItem(item);
		}));
		
		this.connect(this.folderTree, "onOpenItem", dojo_lang.hitch(this, function(item, data) {
			if(data && data.isInstanceOf && (data.isInstanceOf(ecm.model.ResultSet))){
				this.folderContents.setResultSet(data);
				this.mainStackContainer.selectChild(this.folderContents);
			}
			else
				this.openItem(item);
		}));

		this.connect(this.folderContents, "onOpenItem", dojo_lang.hitch(this, function(item, data) {
			if (data.isInstanceOf && data.isInstanceOf(ecm.model.ResultSet) && item.getPath) {
				var path = null;
				path = item.getPath();
				
				var breadcrumbData = [];
				
				for ( var i = 0; i < path.length; i++) {
					var pathItem = path[i];
					
					var label = pathItem.name;
					if(pathItem instanceof ier_model_FilePlan)
						label = pathItem.repository.name + " : " + pathItem.name;
					
					breadcrumbData.push({
						label: label, 
						item: pathItem 
					});
				}
				this.breadcrumb.setData(breadcrumbData);
				this.noRefresh = false;
				
				if (!this.folderTree.isPathSelected(path)) {
					this.folderTree.set("path", path);
				}
			}
		}));

		this.connect(this.breadcrumb, "onClick", function(breadcrumb) {
			this.noRefresh = true;
			var breadcrumbData = this.breadcrumb.getData();

			var path = [];
			for ( var i = 0; i < breadcrumbData.length; i++) {
				path.push(breadcrumbData[i].item);
			}
			
			this.openItem(path[path.length - 1]);
		});
		
		/*this.connect(this.quickSearchList, "onItemSelected", dojo_lang.hitch(this, function(tileListItem, queryString){
			if(tileListItem && (tileListItem.item.entityType != ier_constants.EntityType_Record 
					&& tileListItem.item.entityType != ier_constants.EntityType_ElectronicRecord 
					&& tileListItem.item.entityType != ier_constants.EntityType_PhysicalRecord)){
				var quickSearch = this.repository.getRecentQuickSearch(queryString);
				if(quickSearch){
					quickSearch.addClickedItem(tileListItem.item.docid[0], dojo_lang.hitch(this, function(fetchedItem){
						this.folderTree.treeModel.reload(quickSearch);
						
						var path = [];
						path.push(this.repository);
						path.push(this.folderTree.treeModel.recentQuickSearchesRoot);
						path.push(quickSearch);
						path.push(fetchedItem);
						this.folderTree.set("path", path);
					}));
				}
			}
		}));

		this.connect(this._filePlanSearchBar, "onSearchButtonClicked", function(value) {
			this.mainStackContainer.selectChild(this.quickSearchList);
			this.quickSearchList.setQueryParameters(value, this.fileplan, this.repository, dojo_lang.hitch(this, function(data){
				var quickSearch = this.repository.getRecentQuickSearch(value);
				if(!quickSearch){
					quickSearch = this.repository.addRecentQuickSearch(value, value, value, data);
					this.folderTree.treeModel.reload(this.folderTree.treeModel.recentQuickSearchesRoot);
				}
				var path = [];
				path.push(this.repository);
				path.push(this.folderTree.treeModel.recentQuickSearchesRoot);
				path.push(quickSearch);
				this.folderTree.set("path", path);
			}));
		});*/

		this.logExit("doBrowseConnection");
	},

	_createFilePlanSelector: function(){
		var title = ier_messages.filePlanPane_selectFilePane;
		var dropDown = new ier_widget_layout_FilePlanFlyoutPane({title: title, resizable: true});
		if(dropDown.topPane){
			dojo_style.set(dropDown.topPane.domNode, "display", "none");
		}
		var dialog = new dijit_TooltipDialog({
			"class": "ierFlyoutTooltipDialog",
			autofocus: false
		});
		dialog.set("content", dropDown);
		this.connect(dialog, "onOpen", function(){
			dialog.startup();
		});
		this.connect(dropDown, "closePopup", function(){
			dijit_popup.close(dialog);
		});
		this.connect(dropDown, "selectContentPane", function(){
			this.selectContentPane.apply(this, arguments);
		});
		this.connect(dropDown, "onRepositoryChange", function(){
			this.onRepositoryChange.apply(this, arguments);
		});
		var resizeHandle = new dojox_layout_ResizeHandle({targetId: dialog.id});
		dojo_construct.place(resizeHandle.domNode, dialog.domNode);
		var filePlanSelector = new dijit_form_DropDownButton({label: title, showLabel: false,
			"class": "ierFilePlanSelector", iconClass: "filePlanIcon", dropDown: dialog});
		dojo_construct.place(filePlanSelector.domNode, this._filePlanSelectorContainer);

		this.connect(filePlanSelector.domNode, "oncontextmenu", function(evt) {
			dojo_event.stop(evt);
			return false;
		});

		this._filePlanDropDown = dropDown;
	}
});});
