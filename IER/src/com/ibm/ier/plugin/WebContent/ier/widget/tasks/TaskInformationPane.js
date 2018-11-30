define ([
     "dojo/_base/declare",
     "dojo/_base/lang",
     "dojo/_base/connect",
     "dojo/dom-style",
     "dojo/dom-geometry",
     "dijit/layout/ContentPane",
     "dijit/_TemplatedMixin",
 	 "dijit/_WidgetsInTemplateMixin",
 	 "ecm/LoggerMixin",
 	 "ecm/model/AsyncTask",
 	 "ecm/model/AsyncTaskInstance",
     "ier/constants",
     "ier/messages",
     "ier/widget/tasks/TaskDetailsPane",
     "ier/widget/tasks/TaskParametersPane",
 	 "ier/widget/tasks/TaskResultsPane",
 	 "ier/widget/tasks/TaskErrorPane",
 	 "ier/widget/tasks/TaskExecutionRecordPane",
     "dojo/text!./templates/TaskInformationPane.html",
     "idx/layout/HeaderPane", // in template
     "dijit/layout/TabContainer" // in template
], function(dojo_declare, dojo_lang, dojo_connect, dojo_style, dojo_geo, dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin,
		ecm_model_AsyncTask, ecm_model_AsyncTaskInstance, ier_constants, ier_messages, TaskDetailsPane, TaskParametersPane, TaskResultsPane, TaskErrorPane, TaskEexecutionRecordPane,
		templateString){
	
return dojo_declare("ier.widget.tasks.TaskInformationPane", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	
	templateString: templateString,
	widgetsInTemplate: true,
	ier_messages: ier_messages,
	
	/**
	 * The task this information pane will be showing about
	 */
	item: null,
	
	/**
	 * The handler's class that this task information pane supports
	 */
	taskHandlerClass: null,
	
	postCreate: function(){
		this.inherited(arguments);
		
		this.taskPreviewTabContainer.startup();
		this.setUpTabs();
		
		if(this.item){
			this.setItem(this.item);
		}
		
		this.connect(ecm.model.desktop, "onChange", function(item){
			if(item && item[0] && item[0] instanceof ecm.model.AsyncTask){
				if(item[0] == this.item){
					//refresh the information on the item
					this.item = null;
					this.setItem(item[0], null, null, true);
				}
			}
		});
		
		/**
		 * Only render the results pane tab if the user selects on results
		 */
		this.resultsPaneHandler = this.connect(this.taskPreviewTabContainer, "selectChild", dojo_lang.hitch(this, function (evt) {
			var selectedTab = this.taskPreviewTabContainer.selectedChildWidget;
			if (selectedTab == this.taskInstancesTab) {
				this.taskInstancesTab.createRendering(this.item);
			}
		}));
	},
	
	setUpTabs: function(){
		this.detailsTab = new TaskDetailsPane({
			UUID: "details",
			title: ier_messages.taskPane_previewDetails,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.detailsTab);
		
		this.parametersTab = new TaskParametersPane({
			UUID: "parameters",
			title: ier_messages.taskPane_previewParameters,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.parametersTab);
		
		this.resultsTab = new TaskResultsPane({
			UUID: "results",
			title: ier_messages.taskPane_previewResults,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.resultsTab);
		
		this.errorsTab = new TaskErrorPane({
			UUID: "errors",
			title: ier_messages.taskPane_previewErrors,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.errorsTab);
		
		this.taskInstancesTab = new TaskEexecutionRecordPane({
			UUID: "asyncTaskInstances",
			title: ier_messages.taskPane_previewExecutionRecords,
			informationPane: this
		});
		this.taskPreviewTabContainer.addChild(this.taskInstancesTab);
	},
	
	setItem: function(item, onComplete, tabIdToOpen, ignoreTabOpen){
		if(item == null){
			dojo_style.set(this.noItemSelected, "display", "");
			dojo_style.set(this.taskPreviewTabContainer.domNode, "display", "none");
		}
		else {
			dojo_style.set(this.noItemSelected, "display", "none");
			dojo_style.set(this.taskPreviewTabContainer.domNode, "display", "");
			
			if(item && this.item != item){
				this.item = item;
				
				this.getDetails(this.item, dojo_lang.hitch(this, function(item){
					if(this.detailsTab)
						this.detailsTab.createRendering(this.item);
					
					if(this.parametersTab)
						this.parametersTab.createRendering(this.item);
					
					//if the task completes, show the results tab
					if(this.resultsTab){
						if(this.item.results){
							dojo_style.set(this.resultsTab.controlButton.domNode, "display", "");
							this.resultsTab.createRendering(item);
						}
						else {
							dojo_style.set(this.resultsTab.controlButton.domNode, "display", "none");
						}
					}
					
					if(this.errorsTab){
						//if the task has errors, show the errors tab
						if(this.item.errors && this.item.errors.length > 0){
							dojo_style.set(this.errorsTab.controlButton.domNode, "display", "");
							this.errorsTab.createRendering(item);
						}
						else {
							dojo_style.set(this.errorsTab.controlButton.domNode, "display", "none");
						}
					}
					
					if(this.taskInstancesTab){
						//if the task is recurring, show the execution records tab
						if(this.item.isTaskRecurring && this.item.isTaskRecurring()){
							dojo_style.set(this.taskInstancesTab.controlButton.domNode, "display", "");
						}
						else {
							dojo_style.set(this.taskInstancesTab.controlButton.domNode, "display", "none");
						}
					}
					
					if(!ignoreTabOpen){
						if(tabIdToOpen){
							var tab = this.getTab(tabIdToOpen);
							if(tab && this.taskPreviewTabContainer.selectedChildWidget != tab)
								this.taskPreviewTabContainer.selectChild(tab);
							else
								this.taskPreviewTabContainer.selectChild(this.detailsTab);
						}
						else
							this.taskPreviewTabContainer.selectChild(this.detailsTab);
					}
					
					this.taskPreviewTabContainer.resize();
					
					if(onComplete)
						onComplete(this.item);
				}));
			}
		}
	},
	
	tabHasChild: function(child){
		var children = this.taskPreviewTabContainer.getChildren();
		for(var i in children){
			if(children[i] == child)
				return true;
		}
		return false;
	},
	
	getTab: function(uuid){
		var children = this.taskPreviewTabContainer.getChildren();
		for(var i in children){
			if(children[i].UUID == uuid)
				return children[i];
		}
		return null;
	},
	
	addTab: function(contentPane){
		this.taskPreviewTabContainer.addChild(contentPane);
	},
	
	getDetails: function(item, onComplete){
		if(item instanceof ecm_model_AsyncTask){
			if(!item.detailsLoaded){
				this.item.getDetails(dojo_lang.hitch(this, function(){
					this.item.onChange([this.item]);
					if(onComplete)
						onComplete(this.item);
				}));
			}
			else {
				if(onComplete)
					onComplete(this.item);
			}
		}else {
			if(onComplete)
				onComplete(this.item);
		}
	}
});});