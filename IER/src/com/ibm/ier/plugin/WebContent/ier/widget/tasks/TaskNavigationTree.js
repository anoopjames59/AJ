/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

define([
	"dojo/_base/declare",
	"dojo/_base/event",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dojo/dom-geometry",
	"dojo/dom-class",
	"dojo/keys",
	"dojo/window",
	"dijit/registry",
	"ecm/widget/Tree",
	"ecm/model/Desktop",
	"ecm/model/AsyncTask",
	"ecm/widget/ActionMenu",
	"ier/model/TaskTreeModel",
	"ier/constants"
],
function(dojo_declare, dojo_event, dojo_lang,  dojo_construct, dojo_domGeom, dojo_class, dojo_keys, dojo_window, dijit_registry, ecm_widget_Tree, 
		ecm_model_Desktop, ecm_model_AsyncTask, ecm_widget_ActionMenu, ier_model_TaskTreeModel, ier_constants) {

/**
 * @name ier.widget.tasks.TaskNavigationTree
 * @class Provides a widget that is navigate different categories of tasks
 * @augments dijit.Tree
 */
return dojo_declare("ier.widget.tasks.TaskNavigationTree", [ecm_widget_Tree], {
	/** @lends ier.widget.tasks.TaskNavigationTree.prototype */
	
	showMissingFolderMessage : false,

	postCreate: function() {
		this.inherited(arguments);
		
		dojo_class.add(this.domNode, "ecmFolderTree"); // not really a folder tree, but use same styles
		this.connect(this.model, "onProcessingComplete", "_onProcessingComplete");
		this.connect(this, "onClick", "_onTreeClick");
	},
	
	reload: function(){
		this.model.reload(this.model.rootObject);
	},

	/**
	 * Handle when the model has completed processing an item.
	 * 
	 * @param item
	 *            The item.
	 */
	_onProcessingComplete: function(item) {
		// This returns the tree node's progress indicator back normal if errors occur during retrieve of children
		var node = this.getNodesByItem(item)[0];
		if (node != null) {
			node.unmarkProcessing();
			node.state = "UNCHECKED";
			node._expandNodeDeferred = null;
		}
	},
	
	/**
	 * Selects the provided item
	 */
	selectItem: function(item){
		this.set('selectedItems', [item]);
		var itemNodes = this.getNodesByItem(item);
		var node = itemNodes && itemNodes.length > 0 ? itemNodes[0] : null;
		if (node) {
			dojo_window.scrollIntoView(node.rowNode);
		}
		this.onClick(item, node);
	},

	/**
	 * @private Called when the tree is clicked.
	 */
	_onTreeClick: function(item, node, evt) {
		if (item.continuationData) {
			node.labelNode.innerHTML = "<span class='dijitContentPaneLoading'></span>";

			//wipe out the more link reference on the parent node since it's being expanded right now
			if (item.parent.moreLink) {
				delete item.parent.moreLink;
			}

			// make sure after the request comes back we scroll back to the last node
			var onComplete = dojo_lang.hitch(this, function() {
				dojo_window.scrollIntoView(parent.lastChild);
				this.onPagingComplete();
			});
			this.onPagingStart();
			this.model.fetchNextPage(item.pagedResultSet, item.parent, onComplete);
		} else {
			this.onItemSelected(item, node);
		}
	},

	/**
	 * Event indicating item opened.
	 * 
	 * @param item
	 *            The item to open.
	 * @param resultSet
	 *            A {@link ecm.model.ResultSet} object that contains the item's result set.
	 */
	onOpenItem: function(item, resultSet) {
	},

	/**
	 * Event fired indicating the item is selected.
	 * 
	 * @param item
	 *            The item that is selected.
	 * @param node
	 *            The node that is selected.
	 */
	onItemSelected: function(item, node) {
	},

	/**
	 * Event fired indicating the paging start.
	 */
	onPagingStart: function() {
	},

	/**
	 * Event fired indicating paging complete.
	 */
	onPagingComplete: function() {
	},

	/**
	 * @private Event fired when the items change.
	 * @param modelObjects
	 *            The modelObjects that changed.
	 */
	_onChangeItems: function(modelObjects) {
		
	},

	/**
	 * @private Returns the tree row node for the input node.
	 * @param inputNode
	 *            The input node.
	 */
	_getTreeRowNode: function(inputNode) {
		var node = inputNode;
		while (node) {
			if (dojo_class.contains(node, "dijitTreeRow")) {
				break;
			}
			node = node.parentElement;
		}
		return node ? node : inputNode;
	},


	/**
	 * Event fired when refresh.
	 * 
	 * @param items
	 *            The items.
	 */
	onRefresh: function(items) {
	},

	/**
	 * Returns the icon class for the specified item.
	 * 
	 * @param item
	 *            The item in the tree.
	 * @param opened
	 *            Boolean indicating if opened.
	 */
	getIconClass: function(/*dojo.data.Item*/item, /*Boolean*/opened) {
		var iconClass = null;
		
		if (item instanceof ecm_model_AsyncTask && item.isTaskRecurring()) {
			iconClass = "taskRecurringIcon";
		} 
		else {
			iconClass = "tasksIcon";

			if(item.id == ier_constants.TaskCategories_InProgressTasks){
				return "taskStatusInProgressIcon";
			}
			else if(item.id == ier_constants.TaskCategories_CompletedTasks){
				return "taskStatusCompletedIcon";
			}
			else if(item.id == ier_constants.TaskCategories_ScheduledTasks){
				return "taskStatusScheduledIcon";
			}
			else if(item.id == ier_constants.TaskCategories_PausedTasks){
				return "taskStatusPausedIcon";
			}
			else if(item.id == ier_constants.TaskCategories_FailedTasks){
				return "taskStatusFailedIcon";
			}
			else if(item.id == ier_constants.TaskCategories_RecurringTasks){
				return "taskRecurringIcon";
			}
			else if(item.id == ier_constants.TaskCategories_ReportTasks){
				return "taskReportIcon";
			}
			else if(item.id == ier_constants.TaskCategories_DefensibleDisposalTasks){
				return "taskBasicScheduleIcon";
			}
			else if(item.isMoreLink){
				iconClass = "taskRecurringIcon";
			}
		}
			
		return iconClass;
	}
});});
