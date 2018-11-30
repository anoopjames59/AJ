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
return dojo_declare("ier.widget.tasks.CompletedTasksListingPane", [ier_widget_TasksListingPane], {
	
	ier_messages: ier_messages,
	toolbarDef: "IERCompletedTaskToolbarMenuType",

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
    	                       { label: ier_messages.taskPane_filterAllTypes, value: "all", selected: true},
                               { label: ier_messages.reports, value: ier_constants.TaskType_Report},
                               { label: ier_messages.defensibleDisposal, value: ier_constants.TaskType_DefensibleDisposal}
                            ]
			            }
			          ]
			        ]
	            ]
		});
		return array;
	}
});});
