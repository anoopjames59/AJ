/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/event",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dojo/dom-construct",
	"dojo/dom-geometry",
	"dojo/keys",
	"dijit/_Widget",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"dijit/Menu",
	"dijit/MenuItem",
	"dijit/MenuSeparator",
	"dijit/PopupMenuItem",
	"dijit/form/DropDownButton",
	"idx/form/Link",
	"idx/html",
	"ecm/Messages",
	"ecm/LoggerMixin",
	"dojo/text!./templates/TilesListItem.html"
], //
function(dojo_declare, dojo_array, dojo_event, dojo_lang, dojo_domClass, dojo_domConstruct, dojo_domGeom, dojo_keys, dijit_Widget, dijit_TemplatedMixin, 
		dijit_WidgetsInTemplateMixin, dijit_Menu, dijit_MenuItem, dijit_MenuSeparator, dijit_PopupMenuItem, dijit_DropDownButton, idx_Link, 
		idx_Html, ecm_Messages, ecm_LoggerMixin, template) {

/**
 * @name ier.widget.TilesListItem
 * @class Provides a widget that contains a single row item that is displayed in the TilesList widget.
 * @augments dijit._Widget, dijit._TemplatedMixin, dijit._WidgetsInTemplateMixin, ecm.LoggerMixin
 */
return dojo_declare("ier.widget.TilesListItem", [dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	/** @lends ier.widget.TilesListItem.prototype */

	templateString: template,

	widgetsInTemplate: true,

	item: null,

	store: null,

	actionButton: null,

	_resizeHandle: null,

	constructor: function() {
		this.messages = ecm.messages;
	},

	/**
	 * Called after buildRendering() to setup the page.
	 */
	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);
		
		 this.connect(this.domNode, "keyup", function(event) {
	        if(event.keyCode == dojo_keys.ENTER || event.keyCode == dojo_keys.SPACE){
	        	this.onListItemClick(event);
	        }
	    });

		this._createView();

		this.logExit("postCreate");
	},

	/**
	 * Resizes this item.
	 * 
	 * @param {Object}
	 *            newSize
	 * @param {Object}
	 *            currentSize
	 */
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
		if (this.domNode) {
			var titleMarginBox = dojo_domGeom.getMarginBox(this.titleArea);
			var iconMarginBox = dojo_domGeom.getMarginBox(this.iconArea);

			dojo_domGeom.setMarginBox(this.dataArea, {
				h: Math.max(iconMarginBox.h, titleMarginBox.h)
			});
		}
	},

	/**
	 * Sets the item to display in this TilesListItem.
	 * 
	 * @param item
	 *            The item object to display.
	 */
	setItem: function(item) {
		this.logEntry("setItem");

		this.item = item;
		this._createView();

		this.logExit("setItem");
	},

	/**
	 * Sets the {@link dojo.data.ItemFileWriteStore} for the list.
	 */
	setStore: function(store) {
		this.logEntry("setStore");
		this.store = store;
		this.logExit("seStore");
	},

	/**
	 * Returns the item rendered in this list item.
	 * 
	 * @return Returns the item rendered in this list.
	 */
	getItem: function() {
		return this.item;
	},

	/**
	 * Returns the {@link dojo.data.ItemFileWriteStore} for the list.
	 */
	getStore: function() {
		return this.store;
	},

	/**
	 * Creates a the view to display an item in the list.
	 */
	_createView: function() {

		if (this.item && this.store) {
			this.containerNode.title = this.store.getValue(this.item, "name");

			// Add the icon to the leading pane
			if (this.item.iconClass) {
				var icon = "<div class=\"" + this.store.getValue(this.item, "iconClass") + "\">&nbsp;</div>";
				this.iconArea.innerHTML = icon;
			}

			// Create the multi-line content area
			var titleDiv = null;
			if (this.store.getValue(this.item, "titleOnClick")) {
				var titleObj = new idx_Link({
					label: this.item.title,
					onClick: this.store.getValue(this.item, "titleOnClick")
				});
				// Block right-click event, so the browser doesn't show an annoying right-click window
				this.connect(titleObj.domNode, "oncontextmenu", function(evt) {
					dojo_event.stop(evt);
					return false;
				});

				titleDiv = dojo_domConstruct.create("div", {
					'class': "title"
				});
				titleDiv.appendChild(titleObj.domNode);
			} else {
				titleDiv = dojo_domConstruct.create("div", {
					'class': "title",
					innerHTML: this.store.getValue(this.item, "title")
				});
			}

			var descriptionDiv = null;
			if (this.store.getValue(this.item, "description")) {
				descriptionDiv = dojo_domConstruct.create("div", {
					'class': "description",
					innerHTML: this.store.getValue(this.item, "description")
				});
			}

			var contentDiv = null;
			if (this.store.getValue(this.item, "content")) {
				contentDiv = dojo_domConstruct.create("div", {
					'class': "content",
					innerHTML: this.store.getValue(this.item, "content")
				});
			}

			var actionMenuType = this.store.getValue(this.item, "actionMenu");
			if (actionMenuType) {
				this._createActionButton(actionMenuType, contentDiv);
				
				this.connect(this.containerNode, "oncontextmenu", function(evt) {
					dojo_event.stop(evt);
					
					this._loadActions(actionMenuType, dojo_lang.hitch(this, function(menu){
						var coords = (evt.keyCode !== dojo_keys.F10 ? {
							x: evt.pageX,
							y: evt.pageY
						} : null);
						menu._openMyself({
							target: evt.target,
							coords: coords
						});
					}));
				});
			}

			dojo_domConstruct.place(titleDiv, this.titleArea, "only");
			if (descriptionDiv)
				dojo_domConstruct.place(descriptionDiv, this.titleArea, "last");
			if (contentDiv)
				dojo_domConstruct.place(contentDiv, this.titleArea, "last");
		}

	},

	/**
	 * Creates an action button and inserts it in the content area.
	 * 
	 * @param actionMenu
	 *            Name of the menu to load.
	 */
	_createActionButton: function(actionMenu, contentDiv) {
		this.actionButton = new dijit_DropDownButton({
			label: this.messages.actions
		});
		// Block right-click event, so the browser doesn't show an annoying right-click window
		this.connect(this.actionButton.domNode, "oncontextmenu", function(evt) {
			dojo_event.stop(evt);
			return false;
		});

		// to avoid multiple requests for actions, delay the loading of the actions until the drop-down is pressed
		this.actionButton.loadDropDown = dojo_lang.hitch(this, function(evt) {
			this._loadActions(actionMenu, dojo_lang.hitch(this, function(menu){
				this.actionButton.dropDown = menu;
				this.actionButton.openDropDown();
			}));
		});

		dojo_domConstruct.place(dojo_domConstruct.create("span", {
			innerHTML: "&nbsp;&nbsp;|"
		}), contentDiv, "last");
		dojo_domConstruct.place(this.actionButton.domNode, contentDiv, "last");
	},

	_loadActions: function(actionMenu, onComplete) {
		if(this._actionMenu){
			if(onComplete){
				onComplete(this._actionMenu);
			}
		}
		else {
			ecm.model.desktop.loadMenuActions(actionMenu, dojo_lang.hitch(this, function(actions) {
				var menu = new dijit_Menu();
				dojo_array.forEach(actions, dojo_lang.hitch(this, function(action) {
					if (action.id == "Separator") {
						menu.addChild(new MenuSeparator());
					} else {
						this._addActionToMenu(menu, action);
					}
				}));
				
				this._actionMenu = menu;
				
				if(onComplete){
					onComplete(this._actionMenu);
				}
			}));
		}
	},

	_addActionToMenu: function(menu, action) {
		var self = this;
		menu.addChild(new dijit_MenuItem({
			label: action.name,
			onClick: function(evt) {
				action.performAction(null, [self.item]);
			}
		}));
	},

	/**
	 * Handles a click action on this list item.
	 * 
	 * @param e
	 *            Click event object.
	 */
	onListItemClick: function(evt) {
		this.logEntry("_onListItemClick");

		this.logExit("_onListItemClick");
	},

	onListItemDoubleClick: function(evt) {
		this.logEntry("_onListItemDoubleClick");

		this.logExit("_onListItemDoubleClick");
	},

	_onFocus: function(evt) {
		dojo_domClass.add(this.domNode, "ierTilesListItemHover");
	},

	_onBlur: function(evt) {
		dojo_domClass.remove(this.domNode, "ierTilesListItemHover");
	}
});});
