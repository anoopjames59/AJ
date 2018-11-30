define([
	"dojo/_base/declare",
	"dojo/_base/connect",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"ecm/model/Desktop",
	"ecm/widget/layout/BaseLayout",
	"ecm/widget/dialog/StatusDialog",
	"ier/model/FilePlanRepositoryMixin",
	"ier/model/ReportDefinition",
	"dojo/text!./templates/ReportLayout.html",
	"dijit/layout/ContentPane", // in template
	"ier/widget/ReportEntryForm", // in template
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/StackContainer", // in template
	"dijit/layout/ContentPane" // in template
], function(dojo_declare, dojo_connect, dojo_lang, dojo_domClass, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_model_desktop, ecm_widget_layout_BaseLayout, ecm_StatusDialog, 
		ier_model_FilePlanRepositoryMixin, ier_model_ReportDefinition, templateString){

/**
 * @name ier.widget.layout.ReportLayout
 * @class Implements Basic Launch Processor layout
 * @augments ier.widget.layout.ReportLayout
 */

return dojo_declare("ier.widget.layout.ReportLayout", [ecm_widget_layout_BaseLayout, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin], {
	/** @lends ier.widget.layout.ReportLayout.prototype */

	widgetsInTemplate: true,
	templateString: templateString,

	// postCreate() is called after buildRendering().  This is useful to override when 
	// you need to access and/or manipulate DOM nodes included with your widget.
	// DOM nodes and widgets with the dojoAttachPoint attribute specified can now be directly
	// accessed as fields on "this". 
	postCreate: function() {
		this.logEntry("postCreate");
		
		dojo_domClass.add(document.body, "ecmWait");
		
		this.mainStackContainer.selectChild(this.mainPane);
		
		// Set up an error dialog and start monitoring for errors early, so error dialog appears for configuration errors
		this._errorDialog = this.createErrorDialog();
		this._messageAddedHandler = dojo_connect.connect(ecm_model_desktop, "onMessageAdded", this._errorDialog, "messageAddedHandler");

		// Create and register the progress dialog
		this._statusDialog = new ecm_StatusDialog();
		dojo_connect.connect(ecm_model_desktop, "onRequestStarted", this._statusDialog, "requestStartedHandler");
		dojo_connect.connect(ecm_model_desktop, "onRequestCompleted", this._statusDialog, "requestCompletedHandler");
		
		_repositoryid = ecm_model_desktop.getRequestParam("repository");
		this.repository = ecm_model_desktop.getRepository(_repositoryid);
		this._reportid = ecm_model_desktop.getRequestParam("itemId");
		
		//attempts to copy the desktop so we wouldn't have to mix the repositories again didn't work.  
		//so we have to mix it manually again
		var ierRepository = new ier_model_FilePlanRepositoryMixin(this.repository.id, this.repository.name, "", "", this.repository);
		dojo_declare.safeMixin(this.repository, ierRepository);
		
		this._createRendering();
		
		/*
		if(this.repository.isIERLoaded()){
			this._createRendering();
		} else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
				this._createRendering();
			}));
		}*/
		
		this.logExit("postCreate");
	},
	
	_createRendering: function(){
		//create a a fake initial reportDefinition.  It will be loaded later
		var reportDefinition = new ier_model_ReportDefinition({id: this._reportid, name: this._reportid, repository: this.repository});
		
		//render the reportEntryForm pane
		this._reportEntriesPane.createRendering(this.repository, this._reportid, reportDefinition);
		
		this.mainContainer.resize();
		dojo_domClass.remove(document.body, "ecmWait");
	}
});});
