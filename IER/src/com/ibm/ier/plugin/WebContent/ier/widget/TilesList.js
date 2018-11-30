define([ "dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dojo/dom-attr",
	"dojo/dom-class",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/keys",
	"dijit/_Widget",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"dijit/registry",
	"ecm/LoggerMixin",
	"ier/widget/TilesListItem",
	"ier/messages",
	"dojo/text!./templates/TilesList.html",
	"ecm/widget/FilterTextBox", //in template,
	"dijit/layout/BorderContainer", //in template,
	"dijit/layout/ContentPane" //in template
], 
function(dojo_declare, dojo_array, dojo_lang, dojo_domAttr, dojo_domClass, dojo_domConstruct, dojo_style, dojo_keys, dijit_Widget, dijit_TemplatedMixin, 
		dijit_WidgetsInTemplateMixin, dijit_registry, ecm_LoggerMixin, ier_widget_TilesListItem, ier_messages, template) {

/**
 * @name ier.widget.TilesList
 * @class Provides a widget that displays content in a tiles list view. This widget can be used for any type of
 *        content.
 * @augments dijit._Widget, dijit._TemplatedMixin, dijit._WidgetsInTemplateMixin, ecm.LoggerMixin
 */
return dojo_declare("ier.widget.TilesList", [dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin ], {
	/** @lends ier.widget.TilesList.prototype */

	templateString: template,

	widgetsInTemplate: true,
	canTabToItem: false,

	/**
	 * A {@link dojo.data.ItemFileReadStore} containing the content of the list.
	 */
	store: null,

	/**
	 * Query to filter the list from the store.
	 */
	query: null,

	/**
	 * Array of {@link ecm.widget.TilesListItem} objects.
	 */
	listItems: null,

	/**
	 * Tracks the selected item.
	 */
	selectedItem: null,

	_resizeHandle: null,
	
	hideFilterBox: false,
	
	sortAttributeKey: null,

	constructor: function() {
		this.messages = ecm.messages;
	},

	/**
	 * Called after buildRendering() to setup the page.
	 */
	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);

		this._fetch();

		this.connect(this.filter, "_onInput", "_filterItems");
		this.connect(this.filter, "_setValueAttr", "_filterItems");
		
		if(this.hideFilterBox)
			dojo_style.set(this.filterArea, "display", "none");

		this.logExit("postCreate");
	},

	destroy: function() {
		this.filter.destroy();
		this.clearList();

		if (this.actions) {
			dojo_array.forEach(this.actions, function(action, index) {
				action.destroy();
			});
		}

		this.inherited(arguments);
	},
	
	postMixInProperties: function() {
		this._filterText = ier_messages.localFilter;//ecm.messages.multicolumn_list_filter_text;
	},
	
	/**
	 * Returns all the items
	 */
	getItems: function() {
		return this.listItems;
	},

	/**
	 * Filter favorites by title
	 */
	_filterItems: function() {
		this.logEntry("_filterItems");
		var filterData = this.filter.get("value");
		// dont execute the filter if nothing changed
		if (this._filterData != filterData) {
			if (this.query == null)
				this.query = {
					"name": "*" + filterData + "*"
				};
			else if (this.query.title)
				this.query.title = "*" + filterData + "*";
			else
				this.query.name = "*" + filterData + "*";

			this._filterData = filterData;
			this._fetch();
		}
		this.logExit("_filterItems");
	},

	resize: function(newSize, currentSize) {
		clearTimeout(this._resizeHandle);
		this._resizeHandle = setTimeout(dojo_lang.hitch(this, function() {
			this._asynchResize(newSize, currentSize);
		}), 100);
	},

	/**
	 * Calls out superclass's resize method.
	 * 
	 * @param {Object}
	 *            newSize
	 * @param {Object}
	 *            currentSize
	 */
	_asynchResize: function(newSize, currentSize) {
		// since this is called asynchronously, domNode may have been destroyed already
		if (this.domNode) {
			this.bc.resize(arguments);
		}
	},

	/**
	 * Sets the store to use for this list.
	 * 
	 * @param jsonStore
	 *            An instance of {@link dojo.data.ItemFileReadStore} containing the content to display.
	 */
	setStore: function(jsonStore) {
		this.logEntry("setStore");

		if (this.store) {
			this.store.close();
			delete this.store;
		}
		this.store = jsonStore;
		this._fetch();

		this.logExit("setStore");
	},

	/**
	 * Adds actions to the toolbar (if any).
	 */
	setActions: function(actions) {
		this.logEntry("_setupActions");

		if (this.actions) {
			dojo_array.forEach(this.actions, function(action, index) {
				action.destroy();
			});
		}

		if (actions != null) {
			var self = this;
			self.actions = actions;

			dojo_array.forEach(this.actions, function(action) {
				dojo_domClass.add(action.domNode, "action");
				dojo_domConstruct.place(action.domNode, self.actionsArea, "last");
				action.startup();
			});
		}

		this.logExit("_setupActions");
	},

	/**
	 * Re-fetch use this query criteria.
	 */
	setQuery: function(query) {
		this.query = query;
		this._fetch();
	},

	/**
	 * Creates the list view.
	 */
	_fetch: function() {
		this.logEntry("_fetch");
		if (this.store != null) {
			try {
				if (this.query != null) {
					this.store.fetch({
						query: this.query,
						queryOptions: {
							ignoreCase: true
						},
						sort: this.sortAttributeKey ? this.sortAttributeKey : null,
						onBegin: dojo_lang.hitch(this, "_onFetchBegin"),
						onComplete: dojo_lang.hitch(this, "_onFetchComplete"),
						onError: dojo_lang.hitch(this, "_onFetchError")
					});
				} else {
					this.store.fetch({
						onBegin: dojo_lang.hitch(this, "_onFetchBegin"),
						onComplete: dojo_lang.hitch(this, "_onFetchComplete"),
						onError: dojo_lang.hitch(this, "_onFetchError"),
						sort: this.sortAttributeKey ? this.sortAttributeKey : null
					});
				}
			} catch (e) {
				this._onFetchError(e);
			}
		}

		this.logExit("_fetch");
	},

	/**
	 * Handles the fetch begin event from the store fetch.
	 */
	_onFetchBegin: function() {
		this.logEntry("_onFetchBegin");

		this.clearList();

		this.logExit("_onFetchBegin");
	},

	/**
	 * Clears the list by destroying all the items.
	 */
	clearList: function() {
		this.logEntry("clearList");

		if (this.listItems && this.listItems.length > 0) {
			for ( var i = 0; i < this.listItems.length; i++) {
				this.listItems[i].destroy();
			}
		}
		this.listItems = [];

		this.logExit("clearList");
	},

	/**
	 * Handles the fetch complete event from the store fetch.
	 * 
	 * @param items
	 *            Items retrieved during the fetch operation.
	 */
	_onFetchComplete: function(items) {
		this.logEntry("_onFetchComplete");

		if (items && items.length > 0) {
			this.listDiv = dojo_domConstruct.create("div", {});

			for ( var i in items) {
				if (items[i]) {
					var listItem = new ier_widget_TilesListItem({
						item: items[i],
						store: this.store,
						id: this.id + items[i].id
					});
					
					this.connect(listItem, "onListItemClick", "_onListItemClick");
					this.connect(listItem, "onListItemDoubleClick", "_onListItemDoubleClick");
					
					if (this.canTabToItem) {
						domAttr.set(listItem.domNode, "tabindex", 0);
					}
					this.listItems.push(listItem);
					listItem.setItem(items[i]);

					dojo_domConstruct.place(listItem.domNode, this.listDiv, "last");
				}
			}

			// Add all the items in the list to an in-memory object and finally
			// do only one insert into the DOM tree
			dojo_domConstruct.place(this.listDiv, this.bottomContainer.domNode, "only");
		}
		
		this.onFetchComplete();

		this.logExit("_onFetchComplete");
	},

	/**
	 * Handles the fetch error event from the store fetch.
	 * 
	 * @param e
	 *            Error that occurred while fetching from the store.
	 */
	_onFetchError: function(e) {
		this.logEntry("_onFetchError");

		throw e;
		this.logError(e);
		this.onFetchError(e);

		this.logExit("_onFetchError");
	},
	
	/**
	 * Event invoked when fetch is complete
	 * 
	 * @param e
	 *            Error that occurred while fetching from the store.
	 */
	onFetchComplete: function(e) {
	},

	/**
	 * Allows caller to connect to control the error event from the list.
	 * 
	 * @param e
	 *            Error that occurred while fetching from the store.
	 */
	onFetchError: function(e) {
	},

	/**
	 * Inserts a new item into the list.
	 * 
	 * @param item
	 *            Item to add to the list.
	 */
	addItem: function(item) {
		this.logEntry("addItem");

		if (this.store) {
			if (!this.listDiv) {
				this.listDiv = dojo_domConstruct.create("div", {});
				dojo_domConstruct.place(this.listDiv, this.bottomContainer.domNode, "only");
			}

			var obj = this.store.newItem(item);
			obj.originalItem = item;
			this.store.save();

			var listItem = new ier_widget_TilesListItem({
				item: obj,
				store: this.store,
				id: obj.id
			});
			
			this.connect(listItem, "onListItemClick", "_onListItemClick");
			this.connect(listItem, "onListItemDoubleClick", "_onListItemDoubleClick");
			
			if (this.canTabToItem) {
				domAttr.set(listItem.domNode, "tabindex", 0);
			}
			this.listItems.push(listItem);
			listItem.setItem(obj);

			dojo_domConstruct.place(listItem.domNode, this.listDiv, "last");
		}

		this.logExit("addItem");
	},

	/**
	 * Removes an item from the list
	 */
	removeItem: function(id) {
		this.logEntry("removeItem");

		for ( var i in this.listItems) {
			var item = this.listItems[i].getItem();

			if (id == this.store.getValue(item, "id")) {
				this.store.deleteItem(item);
				this.listItems[i].destroy();
				this.listItems.splice(i, 1);
				this.store.save();
				break;
			}
		}

		this.logExit("removeItem");
	},
	/**
	 * Updates an item from the list
	 */
	updateItem: function(item) {
		this.removeItem(item.id);
		this.addItem(item);
	},
	/**
	 * Returns the selected item.
	 */
	getSelectedItem: function() {
		if(this.selectedItem && this.selectedItem.item){
			return this.store.getValue(this.selectedItem.item, "originalItem");
		}
		return this.selectedItem;
	},

	/**
	 * Event triggered when an item is selected in the list.
	 */
	onItemSelected: function(selectedItem) {
		this.logEntry("onItemSelected");

		this.logExit("onItemSelected");
	},

	_onListItemKey: function(evt) {
		if (evt.charOrCode == dojo_keys.ENTER || evt.charOrCode == " ") {
			this._onListItemClick(evt);
		}
	},

	/**
	 * Handles a click action on the list.
	 * 
	 * @param evt
	 *            Event object.
	 */
	_onListItemClick: function(evt) {
		this.logEntry("_onListItemClick");

		var tilesListItem = dijit_registry.getEnclosingWidget(evt.target);
		if (tilesListItem != null ) {
			if (this.selectedItem && this.selectedItem.domNode)
				dojo_domClass.remove(this.selectedItem.domNode, "ierTilesListItemSelected");

			this.selectedItem = tilesListItem;
			dojo_domClass.add(this.selectedItem.domNode, "ierTilesListItemSelected");

			this.onItemSelected(this.getSelectedItem());
		}

		this.logExit("_onListItemClick");
	},

	/**
	 * Handles a double-click action on the list.
	 * 
	 * @param evt
	 *            Event object.
	 */
	_onListItemDoubleClick: function(evt) {
		this.logEntry("_onListItemDoubleClick");

		var tilesListItem = dijit_registry.getEnclosingWidget(evt.target);
		if (tilesListItem != null ) {
			if (this.selectedItem)
				dojo_domClass.remove(this.selectedItem.domNode, "ierTilesListItemSelected");

			this.selectedItem = tilesListItem;
			dojo_domClass.add(this.selectedItem.domNode, "ierTilesListItemSelected");

			this.onItemSelected(this.selectedItem);
			this.onListItemDoubleClick(this.selectedItem);
		}

		this.logExit("_onListItemDoubleClick");
	},

	/**
	 * Override method for callers to attach to the double-click event.
	 * 
	 * @param selectedItem
	 *            The item double-clicked.
	 */
	onListItemDoubleClick: function(selectedItem) {
	}
});});
