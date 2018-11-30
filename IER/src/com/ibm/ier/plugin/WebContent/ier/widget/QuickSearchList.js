define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dojo/_base/event",
    	"dojo/keys",
    	"dojo/data/ItemFileReadStore",
    	"dojo/string",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"dijit/ToolbarSeparator",
    	"dijit/form/Button",
    	"ecm/LoggerMixin",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/ListTypeSelector",
    	"dojo/text!./templates/QuickSearchList.html",
    	"dijit/form/TextBox", // in template
    	"ecm/widget/TitlePane", // in template
    	"ier/widget/TilesList" // in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_event, dojo_keys, dojo_data_ItemFileReadStore, dojo_string,
		dijit_Widget, dijit_TemplatedMxin, dijit_WidgetsInTemplateMixin, dijit_ToolbarSeparator, dijit_form_Button,
		ecm_LoggerMixin, ecm_model_desktop, ecm_model_Request, ier_constants, ier_messages, ier_util, ier_widget_ListTypeSelector, templateString){

/**
 * @name ier.widget.QuickSearchList
 * @class Provides a list of quick search results of 5 items each where users can invoke the more button and grab additional searches.
 * @augments dijit._Widget, dijit._Templated, ecm.LoggerMixin
 */
return dojo_declare("ier.widget.QuickSearchList", [ dijit_Widget, dijit_TemplatedMxin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin ], {
	/** @lends ier.widget.QuickSearchList.prototype */

	templateString: templateString,

	widgetsInTemplate: true,

	/**
	 * Handle to the ecm.messages object
	 */
	messages: null,
	
	store: null,

	constructor: function() {
		this.messages = ier_messages;
	},

	destroy: function() {
		this._quickSearches.setStore(null);
		//this.refreshRepositoriesButton.destroy();
		//this.connectRepositoryButton.destroy();
	},


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
		this.logEntry("postCreate");
		this.inherited(arguments);

	},
	
	setQueryParameters: function(queryString, fileplan, repository, onComplete){
		this.queryString = queryString;
		this.fileplan = fileplan;
		this.repository = repository;
		this.searchBar.set("value", this.queryString);
		
		if(!this.rendered)
			this._createRendering();
		
		this._fetchSearchResults(null, onComplete);
	},
	
	_createRendering: function() {
		this.rendered = true;
		this._quickSearchTitlePane.set("title", dojo_string.substitute(this.messages.quickSearchList_searchInText, [this.queryString, this.fileplan.name]));

		this._createActivityStream();
		
		this.connect(this._quickSearches, "onItemSelected", dojo_lang.hitch(this, function(selectedItem) {
			this._onListItemClickAction(selectedItem);
		}));
		
		this.connect(this._quickSearches, "onListItemDoubleClick", dojo_lang.hitch(this, function(selectedItem) {
			this._onListItemClickAction(selectedItem);
		}));
		
		this.connect(this.searchBar, "onkeydown", dojo_lang.hitch(this, function(e){
			 if(e.keyCode == dojo_keys.ENTER){
				 this._quickSearchButtonClicked();
				 dojo_event.stop(evt);
			 }
         }));
	},
	
	_quickSearchButtonClicked: function() {
		this.setQueryParameters(this.searchBar.get("value"), this.fileplan, this.repository);
	},
	
	_onListItemClickAction: function(selectedItem)
	{
		var item = selectedItem.item;
		if(item.isMore){
			var params = {
					ier_quickSearchStartingPoint: item.endPoint,
					ier_quickSearchMoreType: item.type,
					callback: dojo_lang.hitch(this, function(response) {
						this.store = new dojo_data_ItemFileReadStore({
							data: response,
							urlPreventCache: true,
							clearOnClose: true
						});
						this._quickSearches.setStore(this.store);
					})
			};
			this._fetchSearchResults(params);
		} else {
			this.onItemSelected(selectedItem, this.queryString);
		}
	},
	
	/**
	 * Event invoked when an item is selected
	 */
	onItemSelected: function(selectedItem, queryString){
		
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
					text:"Record", 
					value: "record"
				});
		options.push( {
					text:"Record Category", 
					value: "RecordCategory"
				});
		options.push({
					text:"Record Folder", 
					value: "RecordFolder"
				});
		options.push({
			text:"Record Volume", 
			value: "RecordVolume"
		});
				
		var listTypeSelector = new ier_widget_ListTypeSelector(options);
		this._loadActions(null, listTypeSelector, this._quickSearches);

		this.connect(listTypeSelector, "onChange", function(selected) {
			this._onTypeListSelector(selected);
		});
	},
	
	_onTypeListSelector: function(selected){
		if (this.query == null)
			this.query = {
				"type": selected
			};
		else
			this.query.type = selected;
		this._quickSearches.setQuery(this.query);
	},
	
	setResults: function(data, queryString){
		this.store = new dojo_data_ItemFileReadStore({
			data: data,
			urlPreventCache: true,
			clearOnClose: true
		});
		this._quickSearches.setStore(this.store);
		this.queryString = queryString;
		if(this.queryString)
			this._quickSearchTitlePane.set("title", dojo_string.substitute(this.messages.quickSearchList_searchInText, [this.queryString, this.fileplan.name]));
	},
	
	_fetchSearchResults: function(params, onComplete){
		var self = this;
		if(!params){
			params = {
					callback: function(response) {
						self.store = new dojo_data_ItemFileReadStore({
							data: response,
							urlPreventCache: true,
							clearOnClose: true
						});
						self._quickSearches.setStore(self.store);
						setTimeout(function(){
							//todo: hack ..will need to be fixed
							self._onTypeListSelector("a");
							self._onTypeListSelector("*");
							self._onTypeListSelector("a");
							self._onTypeListSelector("*");
						}, 200);
						
						self._quickSearches.resize();
						if(onComplete)
							onComplete(response);
					}
			};
		}
		
		var requestParams = ier_util.getDefaultParams(this.repository, function(response) {	// success
			if(params.callback)
				params.callback(response);
		});
		requestParams.requestParams[ier_constants.Param_FilePlanId] = this.fileplan.id;
		requestParams.requestParams[ier_constants.Param_QuickSearchMoreType] = params[ier_constants.Param_QuickSearchMoreType];
		requestParams.requestParams[ier_constants.Param_QuickSearchQueryString] = this.queryString;
		requestParams.requestParams[ier_constants.Param_QuickSearchStartingPoint] = params[ier_constants.Param_QuickSearchStartingPoint];

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetQuickSearchResults, ier_constants.PostEncoding, requestParams);
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
			ecm_model_desktop.loadActions(toolbarName, dojo_lang.hitch(this, function(actions) {
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

				/*this.connect(list, "onItemSelected", function(selectedItem) {
					if (buttons) {
						dojo_array.forEach(buttons, function(button) {
							button.set("disabled", false);
						});
					}
				});*/

				list.setActions(buttons);
			}));
		} else {
			list.setActions(buttons);
		}
	}

});});
