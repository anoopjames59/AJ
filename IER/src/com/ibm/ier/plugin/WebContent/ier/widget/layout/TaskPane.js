define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dojo/date/locale",
	"dojo/dom-construct",
	"dojo/dom-style",
	"dojo/string",
	"dojo/aspect",
	"dojo/_base/connect",
	"ecm/widget/layout/_LaunchBarPane",
	"ecm/model/Request",
	"ecm/model/Desktop",
	"ecm/model/_ModelObject",
	"ecm/model/AsyncTask",
	"ecm/model/AsyncTaskInstance",
	"ier/util/util",
	"ier/constants",
	"ier/messages",
	"ier/model/TaskTreeModel",
	"ier/widget/tasks/TaskNavigationTree",
	"dojo/text!./templates/TaskPane.html",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane", // in template
	"dijit/layout/StackContainer" //in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_date_locale, dojo_construct, dojo_style, dojo_string, dojo_aspect, dojo_connect,
		ecm_widget_layout_LaunchBarPane, ecm_model_Request, ecm_model_Desktop, ecm_model_ModelObject, ecm_model_AsyncTask, ecm_model_AsyncTaskInstance, ier_util, 
		ier_constants, ier_messages, ier_model_TaskTreeModel, ier_widget_TaskNavigationTree, templateString){

var TaskPane = dojo_declare("ier.widget.layout.TaskPane", [ecm_widget_layout_LaunchBarPane], {

	templateString: templateString,
	widgetsInTemplate: true,
	ier_messages: ier_messages,
	_recurringListingPaneClass: "ier/widget/tasks/RecurringTasksListingPane",
	
	treeModel: new ier_model_TaskTreeModel(),

	postCreate: function(){
		this.inherited(arguments);
		this.panes = {};
		this.taskInformationPanes = {};
		
		this.connect(ecm_model_Desktop, "onLogout", function(repository) {
			if(this.isLoaded){
				this.isLoaded = false;
				this.cleanUp();
			}
		});
		
		this.connect(ecm_model_Desktop, "onLogin", function(repository) {
			if(this.selected)
				this.loadContent();
		});
		
		//creates the navigation tree
		this.taskNavigationTree = new ier_widget_TaskNavigationTree({
			model : this.treeModel,
			showRoot: false,
			style: "height: 100%",
			"aria-label": ier_messages.taskPane_tree
		});
		dojo_construct.place(this.taskNavigationTree.domNode, this.taskMenuContainer.domNode, "only");
		
		//obtains the root
		this.taskNavigationTree.model.getRoot(dojo_lang.hitch(this, function(root){
			this.treeRoot = root;
			this.recurringTaskTreeNode = this.treeRoot.children[2];
		}));
		
		//connect and open task items appropriately
		this.connect(ecm_model_Desktop.taskManager, "onAsyncTaskItemOpened", "openItem");
		
		//connects to the onItemSelected and opens the task when selected
		this.connect(this.taskNavigationTree, "onItemSelected", "_onTreeItemSelected");
		
		//connect and refresh the task list when a task is added
		this.connect(ecm_model_Desktop.taskManager, "onAsyncTaskItemAdded", function(task){
			this.currentTaskListingPane.refresh();
		});
		
		//connect to each tasklisting pane selection to change settings
		dojo_aspect.after(this.taskListingStackContainer, "selectChild", dojo_lang.hitch(this, function(page, animation){
			if(this.currentTaskListingPane && this.currentTaskListingPane.taskFilterModule){
				this.currentTaskListingPane.taskFilterModule.setUserCheckBox();
				this.currentTaskListingPane.taskFilterModule.clearFilter();
			}
			
		}), true);
	},
	
	loadContent: function() {
		if(!this.isLoaded) {
			var authenticatingRepository = ecm_model_Desktop.getAuthenticatingRepository();
			if(authenticatingRepository && !authenticatingRepository.isIERLoaded())
				authenticatingRepository.loadIERRepository(dojo_lang.hitch(this, function(repo){
				this._loadContent();
			}));
			else
				this._loadContent();
		}
	},
	
	_loadContent: function(){
		//initially call the all tasks pane
		this.setCurrentPane("ier/widget/tasks/MultiStatusTasksListingPane");
		this.taskNavigationTree.selectItem(this.treeRoot.children[0]);
		
		//initially create the recurring task pane so it can listen to open events as well
		this.setCurrentPane(this._recurringListingPaneClass, true);
		var recurringTaskPane = this.panes[this._recurringListingPaneClass];
		recurringTaskPane.recurringTaskTreeNode = this.recurringTaskTreeNode;
		this.connectGridRowSelection(recurringTaskPane);
		
		//performs task preview connections 
		this._setupTaskPreviewPaneConnections();
		
		this.isLoaded = true;
	},
	
	/**
	 * When an item is selected in the navigation tree, display the different task view panes if a task category is selected or 
	 * open the task if it's a recurring task
	 */
	_onTreeItemSelected: function(item, node){
		this.selectItem(null);
		
		if(item != this._itemInProgress){
			if(item instanceof ecm_model_AsyncTask && item.isTaskRecurring()){
				this.setCurrentPane(this._recurringListingPaneClass);
				this._itemInProgress = item;
				if(item.parent == this.recurringTaskTreeNode){
					item.retrieveAsyncTaskInstances(dojo_lang.hitch(this, function(resultSet){
						ecm_model_Desktop.taskManager.onAsyncTaskItemOpened(item, resultSet);
						this.taskListingStackContainer.selectChild(this.currentTaskListingPane, false);
						this._itemInProgress = null;
					}));
				} else {
					this.taskListingStackContainer.selectChild(this.currentTaskListingPane, false);
					this._itemInProgress = null;
				}
			} else {
				this.filterTaskListing(item);
			}
		}
	},
	
	/**
	 * Sets up a number of task preview pane connections
	 */
	_setupTaskPreviewPaneConnections: function(){
		// If the pane is already open, then listen to row selection.
		if (this.taskInformationPaneContainer._isOpen) {
			this.connectGridRowSelection();

		} else if (!this.taskInformationPaneContainer._isInitialized) {
			// If the pane is not initialized, then one-time connect to the grid selection event to restore the properties pane.
			// It will disconnect immediately after restoring.
			this._oneTimeRowClickHandle = this.connect(this.currentTaskListingPane.taskContentList, "onRowSelectionChange", function(selectedItems) {
				if(selectedItems && selectedItems.length == 1){
					if (selectedItems[0]) {
						//this.selectItem(selectedItem);
						this.taskListingBorderPane.restore();
						dojo_style.set(this.taskInformationPaneContainer.domNode, "height", "50%");
						this.taskInformationPaneContainer._isInitialized = true;
						dojo_connect.disconnect(this._oneTimeRowClickHandle);
					}
				}
			});
		}

		// When the panel opens, listen for future row selection change. 
		this._onPanelOpenHandler = this.connect(this.taskListingBorderPane, "onPanelOpen", function(region) {
			this.taskInformationPaneContainer._isOpen = true;
			this.connectGridRowSelection();
		});

		// When the panel closes, disconnect from the row selection change.
		this._onPanelCloseHandler = this.connect(this.taskListingBorderPane, "onPanelClose", function(region) {
			this.taskInformationPaneContainer._isOpen = false;
			dojo_connect.disconnect(this.rowClickHandle);
		});
	},
	
	/**
	 * Performs cleanup and disconnects all the handlers
	 */
	cleanUp: function(){
		if(this._onPanelOpenHandler)
			dojo_connect.disconnect(this._onPanelOpenHandler);
		
		if(this._onPanelCloseHandler)
			dojo_connect.disconnect(this._onPanelCloseHandler);
		
		if(this._onOpenItemHandler)
			dojo_connect.disconnect(this._onOpenItemHandler);
		
		if(this.taskNavigationTree){
			this.taskNavigationTree.reload();
		}
		
		if(this.panes){
			for(var i in this.panes){
				if(this.panes[i]){
					this.panes[i].reset();
				}
			}
		}
	},
	
	/**
	 * Sets the current task listing pane.  If it haven't been created yet, create it and add it to the stack container
	 * @param paneClass
	 * @param noSet
	 */
	setCurrentPane: function(paneClass, noSet){
		var currentPane = this.panes[paneClass];
		if(!currentPane){
			require([paneClass], dojo_lang.hitch(this, function(cls) {
				currentPane = new cls();
				this.panes[paneClass] = currentPane;
				this.taskListingStackContainer.addChild(currentPane);
				
				if(!noSet)
					this.currentTaskListingPane = currentPane;
			}));
		} else {
			if(!noSet)
				this.currentTaskListingPane = currentPane;
		}
	},
	
	/**
	 * Filters the list of tasks when a user clicks on a task category
	 */
	filterTaskListing: function(categoryTaskItem){
		this.setCurrentPane(categoryTaskItem.taskListingPaneClass);
		this._retrieveTasks(categoryTaskItem);
	},
	
	/**
	 * Retrieves tasks listing and shows the right listing pane
	 */
	_retrieveTasks: function(categoryTask){
		this.currentTaskListingPane.categoryTask = categoryTask;
		this.currentTaskListingPane.retrieveTasks(dojo_lang.hitch(this, function(resultSet){
			this.taskListingStackContainer.selectChild(this.currentTaskListingPane, false);
			this.connectGridRowSelection();
			this._itemInProgress = null;
		}));
	},
	
	/**
	 * Reconnect the grid row selection after changing the pane listing view
	 */
	connectGridRowSelection: function(pane) {
		var currentPane = pane ? pane : this.currentTaskListingPane;
		if(!currentPane._hasSetRowClickConnection) {
			this.connect(currentPane.taskContentList, "onRowSelectionChange", function(selectedItems){
				if(selectedItems && selectedItems.length == 1){
					if (selectedItems[0]) {
						this.selectItem(selectedItems[0]);
					}
				}
			});
			currentPane._hasSetRowClickConnection = true;
		}
	},
	
	/**
	 * Opens a task or a task instance
	 */
	openItem: function(item, resultSet){
		this.selectItem(null);
		
		//for a recurring task, 
		if(item && item instanceof ecm_model_AsyncTask){
			if(item.isTaskRecurring()){
				var path = [];
				path.push(this.treeRoot);
				path.push(this.recurringTaskTreeNode);
				path.push(item);
				
				if (!this.taskNavigationTree.isPathSelected(path)) {
					this.taskNavigationTree.set('path', path);
				}
				
				var recurringListingPane = this.panes[this._recurringListingPaneClass];
				if(this.currentTaskListingPane != recurringListingPane){
					this.setCurrentPane(this._recurringListingPaneClass);
					this.taskListingStackContainer.selectChild(this.currentTaskListingPane, false);
					this.currentTaskListingPane.openItem();
				}
			}
			else {
				if(this.item.errors && this.item.errors.length > 0){
					this.selectItem(item, 'errors');
				}
				else {
					//for a non-recurring task, open it by exposing the results
					this.selectItem(item, 'results');
				}
			}
		}
		else if(item && item instanceof ecm_model_AsyncTaskInstance){
			this.selectItem(item, 'results');
		}
		else if(item instanceof ier.model.CategoryTask){
			this.filterTaskListing(item);
		}
	},
	
	/**
	 * Obtain the right task information pane to be displayed for this type of task
	 * @param handlerClassName
	 * @param onComplete
	 */
	_getTaskInformationPane: function(handlerClassName, onComplete){
		var taskInformationPane = this.taskInformationPanes[handlerClassName];
		if(!taskInformationPane){
			var informationPaneClass = TaskPane.taskTypeLayoutMapping[handlerClassName] ? TaskPane.taskTypeLayoutMapping[handlerClassName].TaskInformationPane :
				"ier/widget/tasks/TaskInformationPane";
			require([informationPaneClass], dojo_lang.hitch(this, function(cls) {
				taskInformationPane = new cls({
					taskHandlerClass: handlerClassName,
					title: ier_messages.taskPane_previewTitle
				});
				this.taskInformationPanes[handlerClassName] = taskInformationPane;
				this.taskInformationPane = taskInformationPane;
				dojo_construct.place(this.taskInformationPane.domNode, this.taskInformationPaneContainer.domNode, "first");
				
				onComplete(taskInformationPane);
			}));
		} else {
			if(taskInformationPane != this.taskInformationPane){
			    this.taskInformationPane = taskInformationPane;
				dojo_construct.place(taskInformationPane.domNode, this.taskInformationPaneContainer.domNode, "first");
			}
			onComplete(taskInformationPane);
		}
	},
	
	
	/**
	 * Selects a task and obtains the task details
	 */
	selectItem: function(item, tabId){
		if(item){
			if(!this.taskInformationPaneContainer._isInitialized){
				this.taskListingBorderPane.restore();
				dojo_style.set(this.taskInformationPaneContainer.domNode, "height", "50%");
				this.taskInformationPaneContainer._isInitialized = true;
			}
			
			//obtain the correct information pane class to be displayed for this type of task
			//it will be based on the handler's class name
			var _this = this;
			this._getTaskInformationPane(item.attributes.type, function(taskInformationPane){
				this.taskInformationPane = taskInformationPane;
				if(item instanceof ecm_model_AsyncTask || item instanceof ecm_model_AsyncTaskInstance){
					this.taskInformationPane.setItem(item, dojo_lang.hitch(this, function(item){
						_this.taskListingBorderPane.resize();
					}), tabId);
				}
			});
		}
		else {
			if(this.taskInformationPane)
				this.taskInformationPane.setItem(null);
		}
	}
});

TaskPane.taskTypeLayoutMapping = {
		"com.ibm.ier.plugin.tasks.RunReportTask": {
			"TaskInformationPane": "ier/widget/tasks/ReportTaskInformationPane"
		},
		"com.ibm.ier.plugin.tasks.RunDefensibleDisposalReportSweepTask": {
			"TaskInformationPane": "ier/widget/tasks/DDSweepTaskInformationPane"
		}
	};

return TaskPane;
});
