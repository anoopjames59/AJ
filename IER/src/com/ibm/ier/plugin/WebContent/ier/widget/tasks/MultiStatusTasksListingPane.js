define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dojo/data/ItemFileReadStore",
	"dojo/date/locale",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/string",
	"ecm/widget/dialog/ConfirmationDialog",
	"ecm/model/Request",
	"ecm/LoggerMixin",
	"ecm/widget/listView/modules/Bar",
	"ier/util/util",
	"ier/constants",
	"ier/messages",
	"ier/widget/admin/config",
	"ier/widget/listView/gridModules/RowContextMenu",
	"ier/widget/listView/modules/TaskFilter",
	"ier/widget/listView/modules/Toolbar",
	"ier/widget/tasks/TasksListingPane"
], function(dojo_declare, dojo_array, dojo_lang, dojo_data_ItemFileReadStore, dojo_date_locale, dojo_construct, dojo_style, dojo_string, 
		ecm_widget_dialog_ConfirmationDialog, ecm_model_Request, ecm_LoggerMixin, ecm_widget_listView_Bar,
		ier_util, ier_constants, ier_messages, ier_admin_config, 
		ier_widget_listView_RowContextMenu, ier_widget_listView_TaskFilter, ier_widget_listView_Toolbar, ier_widget_TasksListingPane){


/**
 * A pane for displaying various status categories of tasks such as Scheduled, InProgress, and All.
 * It makes use of a TaskResultSet and ContentList to display the grid.  It makes use of various Bar filters as well to provide filtering capabilities.
 */
return dojo_declare("ier.widget.tasks.MultiStatusTasksListingPane", [ier_widget_TasksListingPane], {

	ier_messages: ier_messages,
	
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
