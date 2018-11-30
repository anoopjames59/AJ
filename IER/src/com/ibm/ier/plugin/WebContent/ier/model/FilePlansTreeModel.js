define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/_base/connect",
	"dojo/data/util/filter",
	"ecm/model/_ModelObject",
	"ecm/model/Desktop",
	"ecm/model/Repository",
	"ier/messages",
	"ier/model/FilePlan",
	"ier/util/dialog"
], function(dojo_declare, dojo_lang, dojo_array, dojo_connect, dojo_data_util_filter, ecm_model_ModelObject, ecm_model_desktop, ecm_model_Repository,
		ier_messages, ier_model_FilePlan, ier_util_dialog){

/**
 * @name ier.model.FileplansTreeModel
 * @class Implements a dijit.tree.model that can be used to provide a tree display of the fileplans.
 * @augments ecm.model._ModelObject
 */
return dojo_declare("ier.model.FilePlansTreeModel", ecm_model_ModelObject, {
	/** @lends ier.model.FileplansTreeModel.prototype */

	constructor: function() {
		this.logEntry("constructor");
		
		this._id = "filePlanTreeModelId";
		this._name = this._id;
		this._rootNode = new ecm_model_ModelObject("RepositoryRoot", "");

		this._desktopChangeHandler = dojo_connect.connect(ecm_model_desktop, "onChange", this, function(modelObject){
			this._onFilePlanChanged(modelObject, dojo_lang.hitch(this, function(filePlan){
				if(filePlan.deleted){
					this.reloadNode(filePlan.repository);
				}
			}));
		});
		this._desktopConfigureHandler = dojo_connect.connect(ecm_model_desktop, "onConfigure", this, function(repository, items){
			this._onFilePlanChanged(items, dojo_lang.hitch(this, function(filePlan){
				this.reloadNode(repository); // for adding
				this.reloadNode(filePlan); // for editing
			}));
		});

		this.logExit("constructor");
	},

	_onFilePlanChanged: function(modelObject, callback) {
		var func = dojo_lang.hitch(this, function(changedModel){
			if(changedModel instanceof ier_model_FilePlan){
				setTimeout(dojo_lang.hitch(this, function(){
					callback(changedModel);
				}));
			}
		});
		if(dojo_lang.isArray(modelObject)){
			dojo_array.forEach(modelObject, func);
		} else {
			func(modelObject);
		}
	},

	destroy: function() {
		dojo_connect.disconnect(this._desktopChangeHandler);
		dojo_connect.disconnect(this._desktopConfigureHandler);

		this.inherited(arguments);
	},

	/**
	 * Returns the root
	 */
	getRoot: function(onItem) {
		onItem(this._rootNode);
	},
	
	/**
	 * Applies the given filter to the tree model.
	 * 
	 * @param filter
	 *            Simple filter string to apply.
	 */
	applyFilter: function(filter) {
		this.logEntry("applyFilter");

		this.logDebug("Filter=" + filter);

		if (filter && filter.length > 0) {
			// Generate a regular expression from the simple filter string, which ignores case.
			this.filter = dojo_data_util_filter.patternToRegExp(filter, true);
		} else {
			this.filter = null;
		}

		this.reload();

		this.logExit("applyFilter");
	},
	
	reload: function() {
		var self = this;
		this.getChildren(this._rootNode, function(newChildren) {
			self.onChildrenChange(self._rootNode, newChildren); // notify tree to redisplay the parent with these children
		});
	},

	/**
	 * Reloads the specified node.
	 */
	reloadNode: function(item) {
		var self = this;
		if (item) {
			if(item instanceof ier_model_FilePlan){
				// When name of a file plan is changed,
				// onChildrenChange should not be called
				// because it will reuse the existing tree node
				// and won't refresh label of the node.
				// To update label of the node corresponding to the edited file plan,
				// onChange should be called.
				this.onChange(item);
			}else{
				this.getChildren(item, function(newChildren) {
					self.onChildrenChange(item, newChildren); // notify tree to redisplay the parent with these children
				});
			}
		}
	},
	
	isLoaded: function(item) {
		this.logEntry("isLoaded");
		
		if(item instanceof ier_model_FilePlan)
			return true;
		else
			return false;
		
		this.logExit("isLoaded");
	},

	mayHaveChildren: function(/* dojo.data.Item */item) {
		this.logEntry("mayHaveChildren");
		
		if (item == this._rootNode) {
			return true;
		}
		if (item instanceof ier_model_FilePlan) {
			return false;
		} else {
			return true;
		}
		
		this.logExit("mayHaveChildren");
	},

	getChildren: function(/* dojo.data.Item */parentItem, /* function(items) */onComplete) {
		this.logEntry("getChildren");
		
		if (parentItem == this._rootNode) {
			this._filterItems(ecm_model_desktop.getP8Repositories(), onComplete);
		}
		else if(parentItem instanceof ecm_model_Repository){
			var repository = parentItem;
			if(repository.isIERLoaded()){
				this._getFilePlans(repository, onComplete);
			}
			else {
				repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
					this._getFilePlans(repository, onComplete);
				}), null, dojo_lang.hitch(this, function(){
					onComplete(null);
				}));
			}
			
		} else {
			this._getFilePlans(null, onComplete);
		}
		
		this.logExit("getChildren");
	},
	
	_getFilePlans: function(repository, onComplete){
		if(repository && repository.isFilePlanRepository())
			repository.getFilePlans(this._filterItems, onComplete);
		else {
			ier_util_dialog.showMessage(ier_messages.no_fileplans_available);
			if(onComplete)
				onComplete();
		}
	},
	
	_filterItems: function(items, onComplete){
		var childItems = [];
		var nameMatch = false;
		
		for(var i in items) {
			var item = items[i];
			if (!this.filter) {
				childItems.push(item);
			}
			else {
				if (item.name) {
					nameMatch = item.name.match(this.filter);
				}
				
				if(nameMatch){
					childItems.push(item);
				}
			}
		}
	
		onComplete(childItems);
	},

	// =======================================================================
	// Inspecting items

	isItem: function(item) {
		this.logEntry("isItem");
		
		if (item == this._rootNode) {
			return true;
		}

		if (item) {
			if (item instanceof ecm_model_Repository || item instanceof ier_model_FilePlan){
				return true;
			}
		}
		
		this.logExit("isItem");
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
		return item.id;
	},

	getLabel: function(/* dojo.data.Item */item) {
		return item.name;
	},

	// =======================================================================
	// Write interface

	newItem: function(/* dojo.dnd.Item */args, /* Item */parent, /* int? */insertIndex) {
		// summary:
		// Creates a new item. See `dojo.data.api.Write` for details on args.
		// tags:
		// extension
	},

	pasteItem: function(/* Item */childItem, /* Item */oldParentItem, /* Item */newParentItem, /* Boolean */bCopy) {
		// summary:
		// Move or copy an item from one parent item to another.
		// Used in drag & drop.
		// If oldParentItem is specified and bCopy is false, childItem is removed from oldParentItem.
		// If newParentItem is specified, childItem is attached to newParentItem.
		// tags:
		// extension
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
	}

});});
