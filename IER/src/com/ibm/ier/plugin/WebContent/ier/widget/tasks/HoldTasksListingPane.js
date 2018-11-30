define([
	"dojo/_base/declare",
	"ecm/widget/listView/modules/Bar",
	"ier/constants",
	"ier/messages",
	"ier/widget/listView/modules/TaskFilter",
	"ier/widget/listView/modules/Toolbar",
	"ier/widget/tasks/TasksListingPane"
], function(dojo_declare, ecm_widget_listView_Bar, ier_constants, ier_messages, ier_widget_listView_TaskFilter, ier_widget_listView_Toolbar, ier_widget_TasksListingPane){


/**
 * A pane for displaying various status categories of tasks such as Scheduled, InProgress, and All.
 * It makes use of a TaskResultSet and ContentList to display the grid.  It makes use of various Bar filters as well to provide filtering capabilities.
 */
return dojo_declare("ier.widget.tasks.HoldTasksListingPane", [ier_widget_TasksListingPane], {

	toolbarDef: "IERHoldTaskToolbarMenuType",


	postCreate: function(){
		this.inherited(arguments);
	},
	
	getContentListModules: function() {
		var array = [];
		
		array.push({ moduleClass: ecm_widget_listView_Bar, 
			 top: [ 
			        [ // Table
			          [ // Row 
			            {
			            	moduleClass: ier_widget_listView_Toolbar
			            },
			            {
			            	moduleClass: ier_widget_listView_TaskFilter,
			            	filterSelectOptions:  [
                                { label: ier_messages.taskPane_allTasks, value: "all", selected: true},
                                { label: ier_messages.taskPane_scheduledTasks, value: ier_constants.TaskStatus_Scheduled},
                                { label: ier_messages.taskPane_recurringTasks, value: ier_constants.TaskStatus_Recurring},
                                { label: ier_messages.taskPane_inProgressTasks, value: ier_constants.TaskStatus_InProgress},
                                { label: ier_messages.taskPane_completedTasks, value: ier_constants.TaskStatus_Completed},
                                { label: ier_messages.taskPane_failedTasks, value: ier_constants.TaskStatus_Failed}
                            ]
			            }
			          ]
			        ]
	            ]
		});
		return array;
	},
	
	/**
	 * Connect to the various task filter connections
	 */
	setupTaskFilterConnections: function(){
		if(this._onTaskFilterSelectChangeHandler){
			this.disconnect(this._onTaskFilterSelectChangeHandler);
		}
		
		this._onTaskFilterSelectChangeHandler = this.connect(this.taskFilterModule, "onFilterSelectChange", function(value){
			this.categoryTask.taskStatus = value;
			this.retrieveTasks();
		});
	}
});});
