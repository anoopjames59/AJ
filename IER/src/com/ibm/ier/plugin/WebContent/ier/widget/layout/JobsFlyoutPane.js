define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dojo/data/ItemFileReadStore",
	"dijit/form/Button",
	"dijit/ToolbarSeparator",
	"ecm/model/Desktop",
	"ecm/model/Request",
	"ecm/widget/layout/_LaunchBarDialogPane",
	"ier/widget/TilesList",
	"ier/messages",
	"ier/widget/ListTypeSelector",
	"dojo/text!./templates/FlyoutPane.html",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane" // in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_data_ItemFileReadStore, dijit_form_Button, dijit_ToolbarSeparator,
		ecm_model_desktop, ecm_model_Request, ecm_widget_layout_LaunchBarDialogPane, ier_widget_TilesList, ier_messages, ier_widget_ListTypeSelector, templateString){

/**
 * @name ier.widget.layout.JobsListPane
 * @class Provides a list of jobs
 * @augments ecm.widget.layout.LaunchBarDialogPane
 */
return dojo_declare("ier.widget.layout.JobsFlyoutPane", [ecm_widget_layout_LaunchBarDialogPane], {
	/** @lends ier.widget.layout.JobsListPane.prototype */

	// widgetsInTemplate: Boolean
	// Set this to true if your widget contains other widgets
	widgetsInTemplate: true,

	templateString: templateString,

	/**
	 * Handle to the ecm.messages object
	 */
	messages: ier_messages,
	
	store: null,
	
	/**
	 * postCreate() is called after buildRendering(). This is useful to override when you need to access and/or
	 * manipulate DOM nodes included with your widget. DOM nodes and widgets with the dojoAttachPoint attribute
	 * specified can now be directly accessed as fields on "this". Common operations for postCreate:
	 * <ul>
	 * <li>Access and manipulate DOM nodes created in buildRendering()</li>
	 * <li>Add new DOM nodes or widgets</li>
	 * </ul>
	 */
	postCreate: function() {
		
	},
	
	loadContent: function() {
		this.inherited(arguments);
		if (!this.isLoaded) {
			this._createActivityStream();
			this.isLoaded = true;
		}
	},
	
	/**
	 * Creates an activity stream for the home pane.
	 */
	_createActivityStream: function() {
		var options = [];
		options.push( {
					text:"All", 
					value: "*",
					isSelected: "true"
					});
		options.push( {
					text:"DispositionSweep", 
					value: "DispositionSweep"
				});
		options.push( {
					text:"HoldSweep", 
					value: "HoldSweep"
				});
		options.push({
			text:"Report", 
			value: "Report"
		});


		//add reports listing
		this._tileList = new ier_widget_TilesList();
		this.bottomPane.domNode.appendChild(this._tileList.domNode);
		
		var listTypeSelector = new ier_widget_ListTypeSelector(options);
		this._loadActions("JobFlyoutToolbar", listTypeSelector, this._tileList);

		this.connect(listTypeSelector, "onChange", function(selected) {
			if (this.query == null)
				this.query = {
					"type": selected
				};
			else
				this.query.type = selected;
			this._tileList.setQuery(this.query);
		});
		
		var jsonURL = ecm_model_Request.getPluginResourceUrl("IERApplicationPlugin", "ier/widget/test/status.json");
		
		var store = new dojo_data_ItemFileReadStore({
			url: jsonURL
		});
		this._tileList.setStore(store);
	},


	/**
	 * Loads actions for the favorites, repositories, teamspaces and templates list.
	 * 
	 * @param toolbarName
	 *            Name of the toolbar to load from the desktop.
	 * @param selector
	 *            Selector dijit to place in the action area (if any).
	 * @param list
	 *            TilesList object to update.
	 */
	_loadActions: function(toolbarName, selector, list) {
		var buttons = [];

		if (selector) {
			buttons.push(selector);
		}

		if (toolbarName) {
			ecm_model_desktop.loadMenuActions(toolbarName, dojo_lang.hitch(this, function(actions) {
				dojo_array.forEach(actions, function(action) {
					if (action.id == "Separator") {
						var separator = new dijit_ToolbarSeparator();
						buttons.push(separator);
					} else {
						var toolbarButton = new dijit_form_Button({
							label: action.name
						});
						toolbarButton.action = action;

						if (selector != null && selector.declaredClass == "ecm.widget.RepositorySelector") {
							this.connect(toolbarButton, "onClick", function() {
								var selectedItems = [];
								if (list.getSelectedItem() != null)
									selectedItems.push(list.getSelectedItem().item);

								var dropDown = selector.getDropdown();
								if (dropDown != null) {
									action.performAction(ecm_model_desktop.getRepository(dropDown.get("value")), selectedItems);
								}
							});
						} else {
							this.connect(toolbarButton, "onClick", function() {
								if (list.getSelectedItem() != null) {
									var selectedItems = [];
									selectedItems.push(list.getSelectedItem().item);

									action.performAction(ecm_model_desktop.getRepository(list.getSelectedItem().item.repositoryId), selectedItems);
								}
							});
						}

						buttons.push(toolbarButton);
					}
				});

				list.setActions(buttons);
			}));
		} else {
			list.setActions(buttons);
		}
	}
});});
