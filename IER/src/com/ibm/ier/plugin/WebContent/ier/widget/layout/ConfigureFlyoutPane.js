define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/data/ItemFileReadStore",
	"dijit/ToolbarSeparator",
	"dijit/form/Button",
	"ecm/model/Desktop",
	"ecm/widget/layout/_RepositorySelectorMixin",
	"ecm/widget/layout/_LaunchBarDialogPane",
	"ier/widget/TilesList",
	"ier/constants",
	"ier/messages",
	"dojo/text!./templates/FlyoutPane.html",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane" // in template
], function(dojo_declare, dojo_lang, dojo_data_ItemFileReadStore, dijit_ToolbarSeparator, dijit_form_Button,
		ecm_model_desktop, ecm_widget_layout_RepositorySelectorMixin, ecm_widget_layout_LaunchBarDialogPane, ier_widget_TilesList,
		ier_constants, ier_messages, templateString){

/**
 * @name ier.widget.layout.ConfigureFlyoutPane
 * @class
 * @augments ecm.widget.layout.LaunchBarDialogPane
 */
return dojo_declare("ier.widget.layout.ConfigureFlyoutPane", [ecm_widget_layout_LaunchBarDialogPane, ecm_widget_layout_RepositorySelectorMixin], {
	/** @lends ier.widget.layout.ConfigureFlyoutPane.prototype */

	templateString: templateString,

	widgetsInTemplate: true,
	
	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);

		this.createRepositorySelector();
		this.setRepositoryTypes("p8");
		this.doRepositorySelectorConnections();

		// If there is more than one repository in the list, show the selector to the user.
		if (this.repositorySelector.getNumRepositories() > 1)
			this.topPane.domNode.appendChild(this.repositorySelector.domNode);

		this.logExit("postCreate");
	},
	

	setRepository: function(repository) {
		this.repository = repository;
		if(this.repository)
			this.repositorySelector.getDropdown().set("value", this.repository.id);

	},

	/**
	 * Loads the content of this pane.
	 */
	loadContent: function() {
		this.logEntry("loadContent");
		
		//this handles retrieving the default layout repository, login, and setting it as the repository if no repository has been set yet.
		this.setPaneDefaultLayoutRepository();
		
		if(this.repository){
			this.setRepository(this.repository);
			this.tileList = ier_widget_TilesList();
	
			this.bottomPane.domNode.appendChild(this.tileList.domNode);
			this.tileList.startup();
			this.tileList.setStore(this._getLinksStore());
			
			//remove filter
			this.tileList.filter.destroy();
			
			this._loadActions();
	
			this.connect(this.tileList, "onItemSelected", function(selectedItem) {
				var item = selectedItem.item;
				this.selectContentPane(ier_constants.Feature_IERConfigure, {
					repository: this.repository,
					id: item.id[0],
					entityType: item.entityType[0],
					className: item.name[0]
				});
				this.closePopup();
			});
	
			this.isLoaded = true;
			this.needReset = false;
		}

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

	/**
	 * Retrieves a workspaces store from the given repository.
	 */
	_getLinksStore: function(repository) {
		this.logEntry("_getLinksStore");
		
		var items = [];
		items.push(this._createEntry(ier_constants.ClassName_DispositionSchedule, ier_constants.EntityType_DispositionSchedule, "dispositionScheduleIcon", ier_messages.configurePane_dispositionSchedules));
		items.push(this._createEntry(ier_constants.ClassName_Hold, ier_constants.EntityType_Hold, "onHoldIcon", ier_messages.configurePane_holds));
		items.push(this._createEntry(ier_constants.ClassName_Location, ier_constants.EntityType_Location, "locationIcon", ier_messages.configurePane_locations));
		items.push(this._createEntry(ier_constants.ClassName_DispositionAction, ier_constants.EntityType_DispositionAction, "dispositionActionIcon", ier_messages.configurePane_actions));
		items.push(this._createEntry(ier_constants.ClassName_DispositionTrigger, ier_constants.EntityType_DispositionTrigger, "dispositionTriggerIcon", ier_messages.configurePane_triggers));
		items.push(this._createEntry(ier_constants.ClassName_NamingPattern, ier_constants.EntityType_NamingPattern, "namingPatternIcon", ier_messages.configurePane_namingPatterns));

		var store = new dojo_data_ItemFileReadStore({
			data: {
				items: items,
				identifier: 'id'
			}
		});

		this.logExit("_getLinksStore");
		return store;
	},
	
	_createEntry: function (className, entityType, iconClass, title){
		var entry = {
			id: entityType,
			name: className,
			entityType: entityType,
			iconClass: iconClass,
			title: title
		};
		return entry;
	},

	/**
	 * Loads actions for the repository list.
	 */
	_loadActions: function() {
		this.logEntry("_loadActions");
		var buttons = [];
		var self = this;

		ecm_model_desktop.loadActions(ier_constants.MenuType_IERConfigureFlyoutToolbarMenu, dojo_lang.hitch(this, function(actions) {
			for ( var i in actions) {
				var action = actions[i];

				if (action.id == "Separator") {
					var separator = new dijit_ToolbarSeparator();
					buttons.push(separator);
				} else {
					var toolbarButton = new dijit_form_Button({
						label: action.name
					});
					toolbarButton.action = action;

					this.connect(toolbarButton, "onClick", function() {
						toolbarButton.action.performAction(self.repository);
					});

					buttons.push(toolbarButton);
				}
			}

			self.tileList.setActions(buttons);
		}));

		this.logExit("_loadActions");
	}
});});
