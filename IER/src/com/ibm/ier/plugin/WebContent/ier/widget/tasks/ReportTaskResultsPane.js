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
	
return dojo_declare("ier.widget.tasks.ReportTaskResultsPane", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
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
			var tableNode = dojo_construct.toDom('<table class="propertyTable" role="presentation"></table>');
			var report = item.results.reportResultDocumentId;
			if(report){
				var tr = dojo_construct.create("tr");
				
				var td1 = dojo_construct.create("td", {
					"class" : "propertyRowLabel",
					"style" : "width: 5%;"
				});
				
				var label = dojo_construct.create("label", {
					"for": this.id + "_" + "viewReportResult",
					"innerHTML" : item.taskRequest.specificTaskRequest[ier_constants.Param_ReportTitle]
				});
				td1.appendChild(label);
					
				var td2 = dojo_construct.create("td", {
					"class" : "propertyRowLabel"
				});
					
				var button = new Button({
			        label: ier_messages.taskResultsPane_downloadReport,
			        reportId: report,
			        onClick: dojo_lang.hitch(this, function(evt){
			        	var button = dijit_registry.getEnclosingWidget(evt.target);
			        	var repository = ier_util.getRepository(item.results.reportResultRepositorySymbolicName, item.taskRequest[ier_constants.Param_CE_EJB_URL]);
			        	repository.retrieveItem(button.reportId, dojo_lang.hitch(this, function(itemRetrieved){
							ecm.model.desktop.getActionsHandler(dojo_lang.hitch(this, function(actionsHandler) {
								if (actionsHandler) {
									actionsHandler["actionDownloadAll"](repository, [itemRetrieved]);
								}
								}));
							}));
			        })
			    });
				
					
				var button2 = new Button({
			        label: ier_messages.taskResultsPane_viewReportLink,
			        reportId: report,
			        onClick: dojo_lang.hitch(this, function(evt){
			        	var button = dijit_registry.getEnclosingWidget(evt.target);
			        	var repository = ier_util.getRepository(item.results.reportResultRepositorySymbolicName, item.taskRequest[ier_constants.Param_CE_EJB_URL]);
			        	repository.retrieveItem(button.reportId, dojo_lang.hitch(this, function(itemRetrieved){
							ecm.model.desktop.getActionsHandler(dojo_lang.hitch(this, function(actionsHandler) {
								if (actionsHandler) {
									actionsHandler["actionShowHyperlink"](repository, [itemRetrieved], null, null, null, null);
								}
								}));
							}));
			        })
			    });
				td2.appendChild(button.domNode);
				td2.appendChild(button2.domNode);
				tr.appendChild(td1);
				tr.appendChild(td2);
				tableNode.appendChild(tr);
			}
			resultsContainer.appendChild(tableNode);
			dojo_construct.place(resultsContainer, this.container, "only");
		}
		this.logExit("createRendering");
	}
});});