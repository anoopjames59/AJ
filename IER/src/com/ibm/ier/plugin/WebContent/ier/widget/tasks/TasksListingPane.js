define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"dijit/layout/ContentPane",
	"ecm/model/Desktop",
	"ecm/model/AsyncTaskResultSet",
	"ecm/LoggerMixin",
	"ier/util/util",
	"ier/constants",
	"ier/messages",
	"ier/widget/listView/gridModules/RowContextMenu",
	"dojo/text!./templates/TasksListingPane.html",
	"ier/widget/listView/ContentList" // in template
], function(dojo_declare, dojo_array, dojo_lang, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_layout_ContentPane, 
		ecm_model_Desktop, ecm_model_AsyncTaskResultSet, ecm_LoggerMixin, ier_util, ier_constants, ier_messages, 
		ier_widget_listView_RowContextMenu, templateString){


/**
 * A pane for displaying various status categories of tasks such as Scheduled, InProgress, and All.
 * It makes use of a TaskResultSet and ContentList to display the grid.  It makes use of various Bar filters as well to provide filtering capabilities.
 */
return dojo_declare("ier.widget.tasks.TasksListingPane", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {

	templateString: templateString,
	widgetsInTemplate: true,
	ier_messages: ier_messages,
	
	/**
	 * the toolbar definition for the content list toolbar
	 */
	toolbarDef: "IERMultiStatusTaskToolbarMenuType",

	postCreate: function(){
		this.inherited(arguments);
		this.setupTaskGrid();
	},
	
	/**
	 * Obtains the content list grid modules for this pane
	 */
	getContentListGridModules: function() {
		var array = [];
		array.push(ier_widget_listView_RowContextMenu);
		return array;
	},
	
	/**
	 * Obtains the content list modules for this pane.
	 */
	getContentListModules: function() {
	},
	
	/*
	 * Sets up the grid by setting the various content list modules
	 */
	setupTaskGrid: function() {
		this.taskContentList.setContentListModules(this.getContentListModules());
		this.taskContentList.setGridExtensionModules(this.getContentListGridModules());
		this.taskContentList.multiSelect = true;
	},
	
	reset: function(){
		this.categoryTask = null;
		
		if(this.taskContentList){
			this.taskContentList.reset();
			this.setupTaskGrid();
		}
	},
	
	/**
	 * Retrieves tasks for the current listing pane
	 */
	retrieveTasks: function(onComplete){
		var params = {};
		params[ier_constants.Param_UserId] = ecm.model.desktop.taskManager.showCurrentUserOnly ? ecm.model.desktop.getAuthenticatingRepository().userId : null;
		params[ier_constants.Param_TaskType] = ier_util.convertTaskType(this.categoryTask.taskType);
		params[ier_constants.Param_TaskStatus] = this.categoryTask.taskStatus && this.categoryTask.taskStatus == "all" ? "" : this.categoryTask.taskStatus;
		params[ier_constants.Param_IsRecurring] = this.categoryTask.isRecurring;
		params[ier_constants.Param_Parent] = this.categoryTask.parent;
		params[ier_constants.Param_NameFilter] = this.categoryTask.nameFilter;

		ecm_model_Desktop.taskManager.retrieveAsyncTasks(params, dojo_lang.hitch(this, function(response){
			response.repository = ecm_model_Desktop.getAuthenticatingRepository();
			response.parentFolder = this.categoryTask;
			var resultSet = new ecm_model_AsyncTaskResultSet(response);
			resultSet.toolbarDef = this.toolbarDef;
			resultSet.userId = this.categoryTask.taskUserId;
			resultSet.taskType = ier_util.convertTaskType(this.categoryTask.taskType);
			resultSet.taskStatus = this.categoryTask.taskStatus;
			resultSet.isRecurring = this.categoryTask.isRecurring;
			resultSet.parentFilter = this.categoryTask.parent;
			resultSet.nameFilter = this.categoryTask.nameFilter;
			this.taskContentList.setResultSet(resultSet);
			this.setupTaskFilterConnections();
			this.onChangeResultSet(resultSet);
			this.categoryTask.contentListResultSet = resultSet;
			if(onComplete)
				onComplete(resultSet);
		}));
	},

	/**
	 * Event fired when there's a change in resultset
	 */
	onChangeResultSet: function(resultSet){
		
	},
	
	/**
	 * refresh the content list
	 */
	refresh: function(){
		this.taskContentList.getResultSet().refresh();
	},
	
	/**
	 * Connect to the various task filter connections
	 */
	setupTaskFilterConnections: function(){
		if(this._clearTaskFilterHandler){
			this.disconnect(this._clearTaskFilterHandler);
		}
		
		if(this._onTaskFilterSelectChangeHandler){
			this.disconnect(this._onTaskFilterSelectChangeHandler);
		}
		
		if(this._onTaskFilterHandler){
			this.disconnect(this._onTaskFilterHandler);
		}
		
		if(this._onUserCheckboxClickedHandler){
			this.disconnect(this._onUserCheckboxClickedHandler);
		}
		
		if(this._onResultSetChangedHandler){
			this.disconnect(this._onResultSetChangedHandler);
		}
		
		this.taskFilterModule = this.taskContentList.getContentListModule("taskFilter");
		
		this._onTaskFilterHandler = this.connect(this.taskFilterModule, "onTextFilter", function(value){
			if(this.categoryTask.nameFilter != value){
				if(value != null || value != "")
					this.categoryTask.nameFilter = value;
				else
					this.categoryTask.nameFilter = null;
				
				this.retrieveTasks();
			}
		});
		
		this._onTaskFilterSelectChangeHandler = this.connect(this.taskFilterModule, "onFilterSelectChange", function(value){
			 if(this.categoryTask){
				 this.categoryTask.taskType = value;
				 this.retrieveTasks();
			 }
		});
		
		this._onUserCheckboxClickedHandler = this.connect(this.taskFilterModule, "onUserCheckboxClicked", function(event){
			if(event && event.currentTarget && event.currentTarget.checked == true){
				this.categoryTask.taskUserId = ecm_model_Desktop.getAuthenticatingRepository().userId;
				ecm.model.desktop.taskManager.showCurrentUserOnly = true;
			}
			else {
				this.categoryTask.taskUserId = null;
				ecm.model.desktop.taskManager.showCurrentUserOnly = false;
			}
			
			this.retrieveTasks();
		});
		
		this._onResultSetChangedHandler = this.connect(this.taskCOntentList, "onSetResultSet", "onChangeResultSet");
	}
});});
