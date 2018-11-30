/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"dojo/dnd/Manager", 
	"ecm/model/_ModelObject",
	"ecm/model/Item",
	"ecm/model/Task",
	"ecm/model/Repository",
	"ecm/model/Desktop",
	"ier/model/RecentQuickSearch"
], function(dojo_declare, dojo_lang, dojo_connect, dojo_DNDManager, ecm_model_ModelObject, ecm_model_Item, 
		ecm_model_Task, ecm_model_Repository, ecm_model_desktop, ier_model_RecentQuickSearch) {

/**
 * @name ier.model.FolderTreeModel
 * @class Represents a tree that is used to display the folder hierarchy in a repository. This class implements the
 *        Dojo dijit.tree.model class.
 * @augments ecm.model._ModelObject
 */
var FolderTreeModel = dojo_declare("ier.model.FolderTreeModel", [ecm_model_ModelObject], {
	/** @lends ier.model.FolderTreeModel.prototype */

	/**
	 * Constructs the tree.
	 * 
	 * @param rootItem
	 *            The folder to use as the root of the tree.
	 * @param showFoldersOnly
	 *            If true, only folders display in the tree. If false, both folders and non-folders display.
	 * @param filterType
	 *            Filters the items displayed on the tree (supersedes showFoldersOnly).
	 */
	constructor: function(rootItem, showFoldersOnly, rootFolderId) {
		this.id = "Root:" + rootItem.id;
		this.name = this.id;
		this._loadedItems = {};
		this.repository = null;
		this._rootItem = rootItem;
		this.rootFolderId = rootFolderId;
		this.rootFolder = null;
		this.showFoldersOnly = showFoldersOnly || false;
		this._desktopChangeHandler = dojo_connect.connect(ecm_model_desktop, "onChange", this, "_onDesktopChanged");
	},

	filter: null,

	_repostioryChanged: function(repository) {
		this._onDesktopChanged(repository);
	},

	_onDesktopChanged: function(modelObject) {
		if (modelObject == this._rootItem.repository) { // a full refresh
			if (this._rootItem.repository.connected() == false) { // logoff
				this._loadedItems = [];
				this.reload(this._rootItem);
			} else {
				for ( var i in this._loadedItems) {
					var item = this._loadedItems[i];
					this.reload(item);
				}
			}
		} else if (this.isItem(modelObject) && this.isLoaded(modelObject) && modelObject.repository == this._rootItem.repository) { // a
			// partial
			// refresh
			this.reload(modelObject);
		}
	},

	destroy: function() {
		dojo_connect.disconnect(this._desktopChangeHandler);
	},

	isLoaded: function(item) {
		return this._loadedItems[this.getIdentity(item)] ? true : false;
	},

	// =======================================================================
	// Methods for traversing hierarchy

	getRoot: function(onItem) {
		onItem(this._rootItem);
	},

	mayHaveChildren: function(/* dojo.data.Item */item) {
		if(item instanceof ier.model.RecentQuickSearch && item.hasChildren())
			return false;
		
		if(item instanceof ecm_model_Item)
			return item.isFolder();
		else
			return true;
	},

	fetchNextPage: function(/* dojo.data.ResultSet */pagedResultSet, /* Item */parentItem, /* function */onComplete) {
		// fetch more subnotes for this node
		this.onProcessingStarted(parentItem);
		pagedResultSet.retrieveNextPage(dojo_lang.hitch(this, function() {
			this.replaceChildren(parentItem, pagedResultSet);
			this.onProcessingComplete(parentItem);
			if (onComplete) {
				onComplete();
			}
		}));
	},

	replaceChildren: function(/* dojo.data.Item */parentItem, /* ResultSet */results) {
		var childItems = [].concat(results.getItems());
		this._addPageForwardItem(childItems, results, parentItem); // added
		this.onChildrenChange(parentItem, childItems); // notify the tree to refresh the parent item with a new set of
		// children
	},


	getChildren: function(/* dojo.data.Item */parentItem, /* function(items) */onComplete) {
		var _this = this;
		if (parentItem && parentItem.getRepository && !parentItem.repository.connected) {
			onComplete([]);
		} else {
			this.onProcessingStarted(parentItem);
			
			if(parentItem instanceof ecm_model_Repository){
				var childItems = [];
				this.recentQuickSearchesRoot = new ecm_model_ModelObject({
					id: "rootRecentQuickSearches",
					name: "Recent Quick Searches"
				});
				childItems.push(this.recentQuickSearchesRoot);
				
				parentItem.retrieveItem(this.rootFolderId, dojo_lang.hitch(this, function(rootFolder) {
					this.rootFolder = rootFolder;
					childItems.push(rootFolder);
					onComplete(childItems);
				}));
			}

			//the parent item is root folder for quick searches so just obtain all the quick searches
			else if (parentItem.id == "rootRecentQuickSearches"){
				this._rootItem.getRecentQuickSearches(dojo_lang.hitch(this, function(results){
					var childItems = [].concat(results);
					onComplete(childItems);
				}));
			}
			//the parent item is root folder for quick searches so just obtain all the quick searches
			else if (parentItem instanceof ier_model_RecentQuickSearch){
				var items = parentItem.getItemsClicked();
				onComplete(items);
			}
			else {
				parentItem.retrieveFolderContents(this.showFoldersOnly, function(results) {
					var childItems = [].concat(results.getItems());
					if(parentItem.idPrefix){
						for(var i in childItems){
							var childItem = childItems[i];
							childItem.idPrefix = parentItem.idPrefix;
						}
					}
					_this._addPageForwardItem(childItems, results, parentItem); // added
					_this._loadedItems[_this.getIdentity(parentItem)] = parentItem;
					_this.onProcessingComplete(parentItem);
					onComplete(childItems);
				}, null, false, false, this._teamspaceId, this._filterType);
			}
		}
	},

	_addPageForwardItem: function(/* Item array */children, /* ResultSet */resultSet, /* Item */parentItem) {
		var continuable = resultSet.hasContinuation();
		if (continuable) {
			var id = "continuation_" + new Date().getTime();
			var moreLink = new ecm_model_Item(id, ecm.messages.more_paging_link, resultSet.repository, null, null, resultSet, parentItem);
			moreLink.continuationData = resultSet.getContinuationData();
			moreLink.pagedResultSet = resultSet;
			children.push(moreLink);

			//moreLink reference on the parentItem to find it more quickly
			parentItem.moreLink = moreLink;
		}
	},

	// =======================================================================
	// Inspecting items

	isItem: function(/* anything */something) {
		if (something && something.isInstanceOf && something.isInstanceOf(ecm_model_Item)) {
			return true;
		}
		return false;
	},

	fetchItemByIdentity: function(/* object */keywordArgs) {
		// summary:
		// Given the identity of an item, this method returns the item that has
		// that identity through the onItem callback. Conforming implementations
		// should return null if there is no item with the given identity.
		// Implementations of fetchItemByIdentity() may sometimes return an item
		// from a local cache and may sometimes fetch an item from a remote server.
		// tags:
		// extension
	},

	getIdentity: function(/* item */item) {
		if(item.idPrefix)
			return item.idPrefix + item.id;
		else
			return item.id;
	},

	getLabel: function(/* dojo.data.Item */item) {
		return item.name;
	},

	reload: function(parent) {
		// summary:
		// Will cause a reretrieval of the parent folder
		this.onProcessingStarted(parent);
		var _this = this;

		// Check that the unloadFolderContents function exists.
		if (parent.unloadFolderContents != null) {
			parent.unloadFolderContents();
		}
		
		this.getChildren(parent, function(newChildren) { // re-get the folder's children
			_this.onChildrenChange(parent, newChildren); // notify tree to redisplay the parent with these children
			for ( var i in newChildren) {
				_this.onChange(newChildren[i]);
			}
		});
	},

	// =======================================================================
	// Callbacks

	onChange: function(/* dojo.data.Item */item) {
		// summary:
		// Callback whenever an item has changed, so that Tree
		// can update the label, icon, etc. Note that changes
		// to an item's children or parent(s) will trigger an
		// onChildrenChange() so you can ignore those changes here.
		// tags:
		// callback
	},

	onChildrenChange: function(/* dojo.data.Item */parent, /* dojo.data.Item[] */newChildrenList) {
		// summary:
		// Callback to do notifications about new, updated, or deleted items.
		// tags:
		// callback
	},

	/**
	 * This event is triggered when the model begins processing an item. It is intended to allow tree dijits to
	 * display a progress indicator next to the node for the item.
	 */
	onProcessingStarted: function(item) {
	},

	/**
	 * This event is triggered when the model completes processing an item. It is intended to allow tree dijits to
	 * remove display of progress indicators.
	 */
	onProcessingComplete: function(item) {
	}

});
return FolderTreeModel;
});
