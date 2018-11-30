define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dojo/dom-style",
	"dojo/dom-class",
	"ecm/Messages",
	"ecm/model/Desktop",
	"ecm/model/Feature",
	"ecm/widget/layout/LaunchBarContainer",
	"ecm/widget/layout/_LaunchBarPane",
	"ecm/widget/layout/MainLayout",
	"ecm/widget/dialog/ErrorDialog",
	"ier/constants",
	"ier/messages",
	"ier/model/SearchTemplate",
	"ier/util/util"
], function(dojo_declare, dojo_array, dojo_lang, dojo_style, dojo_class, ecm_messages, ecm_model_desktop, ecm_model_Feature, ecm_widget_layout_LaunchBarPane, ecm_widget_layout_LaunchBarContainer,
		ecm_widget_layout_MainLayout, ErrorDialog, ier_constants, ier_messages, SearchTemplate, ier_util){


/**
 * @name ierwidget.layout.IERMainLayout
 * @class
 * @augments ecm.widget.layout.MainLayout
 */
return dojo_declare("ier.widget.layout.IERMainLayout", [ecm_widget_layout_MainLayout], {
	/** @lends ecm.widget.layout.NavigatorMainLayout.prototype */
	
	//Save off the feature panes since the desktop features do not contain all the information
	configurePaneFeature: null,
	adminPaneFeature: null,
	taskPaneFeature: null,

	postCreate: function() {
		this.inherited(arguments);
		
		this.connect(ecm_model_desktop, "onLogout", function() {
			this._clearSelectedPanes();
		});
		
		this.connect(ecm_model_desktop, "onLogin", function() {
			if (this.defaultFeatureId) {
				var button = this.launchBarContainer.getButtonByID(this.defaultFeatureId);
				
				if(this.launchBarContainer.selectContentPane)
					this.launchBarContainer.selectContentPane(button, this.defaultFeatureId, null);
				
				var defaultFeatureButton = this.launchBarContainer.getButtonByID(this.defaultFeatureId);
				if(defaultFeatureButton && defaultFeatureButton.feature)
					ecm_model_desktop.helpContext = defaultFeatureButton.feature.helpContext;
				
				//obtain the roles available from the authenticating repository
				var authenticatingRepository = ecm_model_desktop.getAuthenticatingRepository();
				var isRecordsManagerOrAdmin = authenticatingRepository && authenticatingRepository.isRecordsManagerOrAdministrator() ? true : false;
				var isTaskFeatureVisible = ecm_model_desktop.taskManager.isTaskUser() || ecm_model_desktop.taskManager.isTaskAdmin(); 

				//remove features if the user has no rights to view
				this.hideOrShowFeaturePane(isTaskFeatureVisible, this.taskPaneFeature);
				this.hideOrShowFeaturePane(isRecordsManagerOrAdmin, this.adminPaneFeature);
				this.hideOrShowFeaturePane(isRecordsManagerOrAdmin, this.configurePaneFeature);
			}
			
			this._setBannerAndLaunchbarState();
		});

		this.connect(this.launchBarContainer, "onFeaturePanelCreated", function(panel){
			this.connect(panel, "onOpenItem", function(item, data){
				item = item.item || item;
				if(item && item.isInstanceOf && item.isInstanceOf(SearchTemplate)
						&& (!data || data.openNewTab === true || data.openNewTab === false)){ // not auto-run
					this._openSearch(item, {tabType: "search", openNewTab: data && data.openNewTab || false});
				}
			});
			this.connect(panel, "onEditItem", function(item){
				item = item.item || item;
				if(item && item.isInstanceOf && item.isInstanceOf(SearchTemplate)){
					this._openSearch(item.item || item, {tabType: "searchbuilder"});
				}
			});
		});
	},
	
	_setBannerAndLaunchbarState: function() {
		if (ecm.model.desktop.getRequestParam("sideChrome") == 0) {//hide banner and launchbar
			if (!dojo_class.contains(this.getBanner().domNode, "dijitHidden")) {
				dojo_class.add(this.getBanner().domNode, "dijitHidden");
			}
			this.launchBarContainer.hideButtonBar();
			this.mainContainer.resize();

		} else if (ecm.model.desktop.getRequestParam("sideChrome") == 1) {//hide banner only
			if (!dojo_class.contains(this.getBanner().domNode, "dijitHidden")) {
				dojo_class.add(this.getBanner().domNode, "dijitHidden");
			}
			this.mainContainer.resize();
		} else if (ecm.model.desktop.getRequestParam("sideChrome") == 2) {//hide launchbar only
			this.launchBarContainer.hideButtonBar();
			this.mainContainer.resize();
		}

	},
	
	/**
	 * Clear all last selected panes for a button so the default pane will be selected
	 */
	_clearSelectedPanes: function() {
		dojo_array.forEach(this.launchBarContainer.getButtons(), function(button) {
			if (button && button.lastSelectedPane) {
				button.lastSelectedPane = null;
			}
		});
	},
	
	onLayoutLoaded: function(){
		this._setupRepositorySyncing();
	},
	
	/**
	 * Creates the error dialog used to render error messages in this layout
	 */
	createErrorDialog: function() {
		return new ErrorDialog({
			messageProductId: "FNRRM"
		});
	},
	
	/**
	 * Returns an array of identifiers of the features supported by this layout.
	 */
	
	getAvailableFeatures: function() {
		return [
				new ecm_model_Feature({
					id: ier_constants.Feature_IERFavorites,
					name: ier_messages.favorites,
					separator: false,
					iconUrl: "favoritesLaunchIcon",
					featureClass: "ier.widget.layout.FavoritePane",
					popupWindowClass: null,
					featureTooltip: ecm_messages.launchbar_favorites,
					popupWindowTooltip: null,
					preLoad: false,
					helpContext: ier_constants.HelpContextUrl + "frmovh00.htm"
				}),
				new ecm_model_Feature({
					id: ier_constants.Feature_IERBrowseFilePlan,
					name: ier_messages.fileplans,
					separator: false,
					iconUrl: "browseLaunchIcon",
					featureClass: "ier.widget.layout.FilePlanPane",
					popupWindowClass: "ier.widget.layout.FilePlanFlyoutPane",
					featureTooltip: ier_messages.fileplans_tooltip,
					popupWindowTooltip: null,
					preLoad: false,
					helpContext: ier_constants.HelpContextUrl + "frmovh00.htm"
				}),
				new ecm_model_Feature({
					id: ier_constants.Feature_IERSearch,
					name: ier_messages.search,
					separator: false,
					iconUrl: "searchLaunchIcon",
					featureClass: "ier.widget.layout.SearchPane",
					popupWindowClass: "ier.widget.layout.SearchFlyoutPane",
					featureTooltip: ecm_messages.launchbar_search,
					popupWindowTooltip: ecm_messages.launchbar_search_popup,
					preLoad: false,
					helpContext: ier_constants.HelpContextUrl + "frmovh22.htm"
				}),
				this.taskPaneFeature = new ecm_model_Feature({
					id: ier_constants.Feature_IERTasks,
					name: ier_messages.tasks,
					separator: false,
					iconUrl: "launcherJobsIcon",
					featureClass: "ier.widget.layout.TaskPane",
					popupWindowClass: null,
					featureTooltip: ier_messages.tasks_tooltip,
					popupWindowTooltip: null,
					preLoad: false,
					helpContext: ier_constants.HelpContextUrl + "frmovh31.htm"
				}),
				this.configurePaneFeature = new ecm_model_Feature({
					id: ier_constants.Feature_IERConfigure,
					name: ier_messages.configure,
					separator: false,
					iconUrl: "launcherConfigureIcon",
					featureClass: "ier.widget.layout.ConfigurePane",
					popupWindowClass: null,
					featureTooltip: ier_messages.configure_tooltip,
					popupWindowTooltip: null,
					preLoad: false,
					helpContext: ier_constants.HelpContextUrl + "frmovh01.htm"
				}),
				this.adminPaneFeature = new ecm_model_Feature({
					id: ier_constants.Feature_IERAdmin,
					name: ier_messages.administration,
					separator: false,
					iconUrl: "adminLaunchIcon",
					featureClass: "ier.widget.layout.AdminiPane",
					featureTooltip: ier_messages.administration_tooltip,
					popupWindowTooltip: null,
					preLoad: false,
					helpContext: ier_constants.HelpContextUrl + "frmovh00.htm"
				}),
				new ecm_model_Feature({
					id: "workPane",
					name: ecm.model.desktop.getConfiguredLabelsvalue("work"),
					separator: false,
					iconUrl: "workLaunchIcon",
					featureClass: "ecm.widget.layout.WorkPane",
					popupWindowClass: "ecm.widget.layout.WorkFlyoutPane",
					featureTooltip: ecm.messages.launchbar_work,
					popupWindowTooltip: ecm.messages.launchbar_work_popup,
					preLoad: false,
					helpContext: ier_constants.HelpContextUrl + "frmovh00.htm"
				})
			];
	},
	
	/**
	 * Sets the feature configuration.
	 */
	setFeatures: function(selectedFeatures, defaultFeature) {
		this.logEntry("setFeatures");

		var features = this.mergeFeatures(this.getAvailableFeatures(), selectedFeatures, defaultFeature);
		
		for ( var index in features) {
			var feature = features[index];
			
			//remove the admin feature which always get added. Should be fixed on the nexus side
			if (feature.id == ier_constants.NexusAdminClientId) {
				features.splice(index, 1);
			}
		}

		//set the default feature as well since it's not done inside MainLayout
		var layout = {
			selectedPane: defaultFeature,
			buttons: features
		};
		this.launchBarContainer.setLayout(layout);
		this.defaultFeatureId = defaultFeature;

		this.onLayoutLoaded();
		this.mainContainer.resize();

		this.logExit("setFeatures");
	},


	/**
	 * Sets the layout configuration.
	 */
	setLayoutConfig: function(layoutConfig, layoutCategories, layoutDefaultCategory) {
		this.inherited(arguments);
	},
	
	/**
	 * Performs repository syncing for all the various panes.
	 */
	_setupRepositorySyncing: function() {
		var panes = this.launchBarContainer.getChildPanes();
		for ( var i in panes) {
			var child = panes[i];
			if (child && child instanceof ecm_widget_layout_LaunchBarPane && child.onRepositoryChange) {
				this.connect(child, "onRepositoryChange", function(currentPane, repository) {
					if(repository.isIERLoaded()){
						this._setAllChildPanesRepositories(panes, currentPane, repository);
					}
					else {
						repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
							this._setAllChildPanesRepositories(panes, currentPane, repository);
						}));
					}
				});
			}
		}
	},
	
	/**
	 * Loops and sets all the child panes' repositories
	 * @param panes
	 * @param currentPane
	 * @param repository
	 */
	_setAllChildPanesRepositories: function(panes, currentPane, repository){
		for(var j in panes){
			var pane = panes[j];
			if(pane.UUID && pane.UUID){
				if(pane.UUID != ier_constants.Feature_IERFavorites && pane.UUID != ier_constants.Feature_IERJobs 
						&& pane.UUID != ier_constants.FeaturePane_IERJobs
						&& pane.UUID != ier_constants.Feature_IERAdmin)
					this._setPaneRepository(currentPane, pane, repository);
			}
		}
	},
	
	/**
	 * Sets the repository on the pane and tells it to reset itself.
	 */
	_setPaneRepository: function(currentPane, pane, repository) {
		if (pane && currentPane != pane) {
			pane.setRepository(repository);
			pane.needReset = true;
		}
	},
	
	/**
	 * Returns whether the specified featureId already exists in the launch bar container or not
	 */
	doesFeatureExistInLaunchBarContainer: function(featureId){
		var buttons = this.launchBarContainer.getButtons();
		return (buttons[featureId] != null);
	},
	
	/**
	 * Add or remove a feature pane based on the boolean permission
	 */
	addOrRemoveFeaturePane: function(permission, feature){
		if(!permission){
			this.launchBarContainer.removeFeatureFromLayout(feature);
		} else {
			if(!this.doesFeatureExistInLaunchBarContainer(feature.id))
				this.launchBarContainer.addFeatureToContainer(feature);
		}
	},
	
	hideOrShowFeaturePane: function(permission, feature){
		var button = this.launchBarContainer.getButtons()[feature.id];

		if(button){
			if(!permission)
				dojo_style.set(button.domNode, "display", "none");
			else
				dojo_style.set(button.domNode, "display", "");
		}
	},

	_openSearch: function(searchTemplate, params){
		var button = this.launchBarContainer.getButtonByID(ier_constants.Feature_IERSearch);
		if(button){
			params = dojo_lang.mixin({selected: true, closable: true}, params);
			params.repository = searchTemplate.repository;
			if(params.tabType == "searchbuilder"){
				params.searchTemplate = searchTemplate;
				params.UUID = searchTemplate.generateUUID();
			}else{ // "search"
				params.searchTemplate = searchTemplate.clone();
			}
			this.launchBarContainer.selectContentPane(button, ier_constants.Feature_IERSearch, params);
		}
	},
	
	/**
	 * Returns a feature from the desktop.
	 * 
	 * @param featureId
	 */
	getFeature: function(featureId) {
		if (ecm_model_desktop.features && featureId) {
			for ( var i in ecm_model_desktop.features) {
				var feat = ecm_model_desktop.features[i];
				if (feat.id == featureId) {
					return feat;
				}
			}
		}
		return null;
	},

});});
