define ([
     "dojo/_base/declare",
     "dojo/_base/json",
     "dojo/_base/lang",
     "dojo/dom-style",
     "dojo/dom-construct",
     "dijit/registry",
     "dijit/layout/ContentPane",
     "dijit/_TemplatedMixin",
 	 "dijit/_WidgetsInTemplateMixin",
 	 "dijit/form/Button",
 	 "ecm/LoggerMixin",
 	 "ecm/widget/dialog/MessageDialog",
     "ier/constants",
     "ier/messages",
 	"ier/util/util",
     "dojo/text!./templates/TaskPane.html"
], function(dojo_declare, dojo_json, dojo_lang, dojo_domStyle, dojo_construct, dijit_registry, dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, 
		Button, ecm_LoggerMixin, ecm_dialog_MessageDialog, ier_constants, ier_messages, ier_util, templateString){
	
return dojo_declare("ier.widget.tasks.DDSweepTaskResultsPane", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	templateString: templateString,
	widgetsInTemplate: true,
	
	createRendering: function(item) {
		this.logEntry("createRendering");
		this.item = item;
		
		var resultsContainer = dojo_construct.create("div", {
			"class": "ierCommonPropertiesPane ecmCommonPropertiesPane"
		});
		
		if(item.results)
		{
			if(item.results.reports && !(item.results.reports instanceof Object))
				item.results.reports = dojo_json.fromJson(item.results.reports);
			if(item.results.reports){
				var tableNode = dojo_construct.toDom('<table class="propertyTable" role="presentation"></table>');
				for (var i in item.results.reports) {
					var report = item.results.reports[i];
					if(report){
						var tr = dojo_construct.create("tr");
						
						var td1 = dojo_construct.create("td", {
							"class" : "propertyRowLabel",
							"style" : "width: 5%;"
						});
						
						var label = dojo_construct.create("label", {
							"for": this.id + "_" + "viewReportResult",
							"innerHTML" : i
						});
						td1.appendChild(label);
							
						var td2 = dojo_construct.create("td", {
							"class" : "propertyRowLabel"
						});
							
						var button = new Button({
					        label: ier_messages.taskResultsPane_downloadAllReports,
					        reportId: report,
					        onClick: dojo_lang.hitch(this, function(evt){
					        	var button = dijit_registry.getEnclosingWidget(evt.target);
					        	var repository = ier_util.getRepository(item.results[ier_constants.Param_P8RepositoryId], item.results[ier_constants.Param_CE_EJB_URL]);
					        	repository.retrieveItem(button.reportId, dojo_lang.hitch(this, function(itemRetrieved){
									ecm.model.desktop.getActionsHandler(dojo_lang.hitch(this, function(actionsHandler) {
										if (actionsHandler) {
											actionsHandler["actionDownloadAll"](repository, [itemRetrieved]);
										}
										}));
									}), ier_constants.ClassName_Transcript);
					        })
					    });
						
							
						var button2 = new Button({
					        label: ier_messages.taskResultsPane_viewReportLink,
					        reportId: report,
					        onClick: dojo_lang.hitch(this, function(evt){
					        	var button = dijit_registry.getEnclosingWidget(evt.target);
					        	var repository = ier_util.getRepository(item.results[ier_constants.Param_P8RepositoryId], item.results[ier_constants.Param_CE_EJB_URL]);
					        	repository.retrieveItem(button.reportId, dojo_lang.hitch(this, function(itemRetrieved){
									ecm.model.desktop.getActionsHandler(dojo_lang.hitch(this, function(actionsHandler) {
										if (actionsHandler) {
											actionsHandler["actionShowHyperlink"](repository, [itemRetrieved], null, null, null, null);
										}
										}));
									}), ier_constants.ClassName_Transcript);
					        })
					    });
						td2.appendChild(button.domNode);
						td2.appendChild(button2.domNode);
						tr.appendChild(td1);
						tr.appendChild(td2);
						tableNode.appendChild(tr);
					}
				}
				resultsContainer.appendChild(tableNode);
				dojo_construct.place(resultsContainer, this.container, "only");
			}
			
			//no reports..just include completed message
			if(item.results.taskResultMessage){
				if(this.nodeMessage)
					delete this.nodeMessage;
				
				//replace it with a localized message for the current user
				//check for the english comparison for now..will remove in the future.  It should be using the message key.
				var message = null;
				if(item.results.taskResultMessageKey)
					message = ier_messages[item.results.taskResultMessageKey];
				
				if(message == null){
					message = item.results.taskResultMessage;
					if(message == "The basic schedule workflow launched successfully.")
						message = ier_messages.basicScheduleWorkflowLaunchedSuccess;
					if(message == "The sweep completed successfully, but there are no records to process and include in the report.")
						message = ier_messages.basicScheduleNoRecordsToProcess;
				}
					
				this.nodeMessage = dojo_construct.create("pre", {
					"style": "", 
					"class": "taskError",
					"innerHTML": message
				});
				
				//take over the container if there are not report results
				if(item.results.reports)
					dojo_construct.place(this.nodeMessage, this.container, "first");
				else
					dojo_construct.place(this.nodeMessage, this.container, "only");
				
			}
		}
		this.logExit("createRendering");
	}
});});