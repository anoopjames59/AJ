define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"ecm/model/Desktop",
	"ecm/model/AsyncTask",
	"ecm/widget/listView/modules/Bar",
	"ier/constants",
	"ier/messages",
	"ecm/widget/listView/modules/Breadcrumb",
	"ier/widget/listView/gridModules/RowContextMenu",
	"ier/widget/listView/modules/TaskFilter",
	"ier/widget/listView/modules/Toolbar",
	"ier/widget/tasks/TasksListingPane",
	"dojo/text!./templates/TasksListingPane.html",
	"ier/widget/listView/ContentList" // in template
], function(dojo_declare, dojo_array, dojo_lang, ecm_model_Desktop, ecm_model_AsyncTask, ecm_widget_listView_Bar,
		ier_constants, ier_messages, ecm_widget_listView_Breadcrumb,
		ier_widget_listView_RowContextMenu, ier_widget_listView_TaskFilter, ier_widget_listView_Toolbar, ier_widget_TasksListingPane, templateString){


/**
 * A pane for displaying various status categories of tasks such as Scheduled, InProgress, and All.
 * It makes use of a TaskResultSet and ContentList to display the grid.  It makes use of various Bar filters as well to provide filtering capabilities.
 */
return dojo_declare("ier.widget.tasks.RecurringTasksListingPane", [ier_widget_TasksListingPane], {

	templateString: templateString,
	widgetsInTemplate: true,
	ier_messages: ier_messages,
	toolbarDef: "IERMultiStatusTaskToolbarMenuType",

	postCreate: function(){
		this.inherited(arguments);
		
		this.connect(ecm_model_Desktop.taskManager, "onAsyncTaskItemOpened", "openItem");
	},
	
	/**
	 * Obtains the content list modules for this pane
	 */
	getContentListModules: function() {
		var array = [];
		
		array.push({ moduleClass: ecm_widget_listView_Bar, 
			 top: [ 
			        [ 
			          [ 
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
			        ], 
			        [ 
			          [ 
			            { 
			            	moduleClass: ecm_widget_listView_Breadcrumb,
			            	includeRootItemName : false
			 	        } 
			          ] 
			 	    ]
                ]
		   });
		
		return array;
	},
	
	/**
	 * Event fired when there's a change in resultset
	 */
	onChangeResultSet: function(resultSet){
		this._addInitialBreadCrumb(resultSet);
	},
	
	_addInitialBreadCrumb: function(){
		 var parent = null;
		 this.breadCrumb = this.taskContentList.getContentListModule("breadcrumb");
		 if(this.breadCrumb){
			 if(this.parentFolder && !(this.parentFolder instanceof ecm_model_AsyncTask)){
			    parent = this.taskContentList.getResultSet().parentFolder;
			}
			else
			    parent = this.categoryTask;
			 
			this.breadCrumb.getBreadcrumb().setData([{label: parent.name, item: parent}]);
		}
	},
	
	/**
	 * Opens an item in the content list
	 */
	openItem: function(item, resultSet){
		if(item instanceof ecm_model_AsyncTask && item.isTaskRecurring()){
			resultSet.toolbarDef = this.toolbarDef;
			this.taskContentList.setResultSet(resultSet);
			
			this.breadCrumb = this.taskContentList.getContentListModule("breadcrumb");
			if(this.breadCrumb){
				var breadcrumbData = [];
				var path = [];
				path.push(this.recurringTaskTreeNode);
				path.push(item);
				
				for ( var i = 0; i < path.length; i++) {						
					breadcrumbData.push({
						label: path[i].name, 
						item: path[i] 
					});
				}
				
				this.breadCrumb.getBreadcrumb().setData(breadcrumbData);
			}
			
			this.onOpenItem(item, resultSet);
		}
	},
	
	/**
	 * Event invoked when a task is opened
	 */
	onOpenItem: function(item, resultSet){
		
	}
});});
