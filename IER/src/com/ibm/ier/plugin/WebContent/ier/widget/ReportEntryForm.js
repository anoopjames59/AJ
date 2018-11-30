define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/_base/unload",
    	"dojo/string",
    	"dojo/_base/array",
    	"dojo/dom-construct",
    	"dojo/dom-class",
    	"dojo/dom-style",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"ecm/Messages",
    	"ecm/model/Message",
    	"ecm/model/AttributeDefinition",
    	"ecm/model/Request",
    	"ecm/widget/dialog/MessageDialog",
    	"ier/constants",
    	"ier/util/util",
    	"ier/util/dialog",
    	"ier/messages",
    	"ier/widget/admin/config",
    	"ier/model/FilePlanRepositoryMixin",
    	"dojo/text!./templates/ReportEntryForm.html",
    	"dijit/form/Button", // in template
    	"dijit/layout/ContentPane", // in template
    	"ier/widget/panes/CommonPropertiesPane" // in template
], function(dojo_declare, dojo_lang, dojo_unload, dojo_string, dojo_array, dojo_domConstruct, dojo_domClass, dojo_domStyle, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, 
		ecm_messages, ecm_Message, ecm_model_AttributeDefinition, ecm_model_Request, ecm_widget_dialog_MessageDialog, ier_constants, ier_util, 
		ier_util_dialog, ier_messages, ier_admin_config, ier_model_FilePlanRepositoryMixin, templateString){

/**
 * @name ReportEntryRenderer
 * @class This widget enable user to specify values to the report entries and launches the report.
 * @augments none
 */
return dojo_declare("ier.widget.ReportEntryForm", [dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin], {
	/** @lends ier.widget.ReportEntryRenderer.prototype */

	widgetsInTemplate: true,
	templateString: templateString,
	ier_messages: ier_messages,
	messages: ecm_messages,
	repository: null,
	reportDefinition: null,
	classAttributeDefinitions: null,
	reportViewer: null,
	
	/**
	 * Whether this form will be presented in a popup
	 */
	isPopup: false,
	
	/**
	 * Whether to show the action bar or not
	 */
	showActionBar: true,

	postCreate: function() {
		this.inherited(arguments);
		
		//validate the property inputs after each change
		this.connect(this._reportEntryPropertiesPane, "onChange", function() {
			this.validateInput();
			this.onChange();
		});
		
		this.connect(this._reportEntryPropertiesPane, "onValueSet", function() {
			this.validateInput();
			this.onChange();
		});		
		
		this.connect(this._reportEntryPropertiesPane, "onCompleteRendering", function() {
			this.resizeCommonProperties();
			this.resize();

			this.onCompleteRendering();
		});
		
		this.connect(this._reportEntryPropertiesPane, "onRenderAttributes", function(attributes) {
			this.onRenderAttributes(attributes);
		});
		
		this.connect(this.backToParams, "click", dojo_lang.hitch(this, function(){
			this.reportViewer.close();
			this.mainStackContainer.selectedChildWidget = null;
			this.mainStackContainer.selectChild(this.mainPane);
		}));
		
		dojo_unload.addOnWindowUnload(dojo_lang.hitch(this, function(){
			if(this.reportViewer && this.isPopup)
				this.reportViewer.close();
		}));
		
		if(!this.showActionBar){
			dojo_domStyle.set(this.actionBar, "display", "none");
		}
	},
	
	/**
	 * Event invoked when attributes are about to be rendered
	 */
	onRenderAttributes: function(attributes, deferArray){
		
	},
	
	/**
	 * Returns repository
	 * @returns
	 */
	getRepository: function(){
		return this.repository;
	},
	
	/**
	 * Returns the correct viewer for this report.  Currently it will return the cognos viewer but later on we will have logic to return other viewers as well
	 */
	getReportViewer: function() {
		var viewerClass = ecm.model.Request.getPluginResourceUrl(ier_constants.ApplicationPlugin, "ier/widget/CognosViewer.js");
		var viewerObject = null;

		require([viewerClass], dojo_lang.hitch(this, function(cls) {
			viewerObject = new cls({
				repository: this.repository,
				reportDefinitionId: this.reportDefinition.id,
				reportName: this.reportDefinition.name,
				tableName: this.reportDefinition.tableName
			});
		}));
		viewerObject.createRendering();
		
		return viewerObject;
	},
	
	/**
	 * Sets repository
	 * @param repository
	 */
	setRepository: function(repository){
		this.repository = repository;
	},

	/**
	 * Creates the rendering for the report entry form
	 */
	createRendering: function(repository, reportId, reportDefinition, onComplete) {		
		this.repository = repository;
		this._reportId = reportId;

		this._reportEntryPropertiesPane.setRepository(this.repository);
		this.reportDefinition = reportDefinition;
		this._attributeDefinitions = [];			

		this._contentClass = this.repository.getContentClass(ier_constants.ClassName_ReportHold);

		this._loadReportDefinition(dojo_lang.hitch(this, function(){
			
			if(this.isPopup){
				//constructs the report viewer
				this.reportViewer = this.getReportViewer();
				dojo_domConstruct.place(this.reportViewer.domNode, this.baseReportViewerContainer, "only");
				
				dojo_domClass.add(this.mainPane.domNode, "mainPane");
			}
			
			this.mainContainer.resize();
			
			if(onComplete)
				onComplete();
		}));		
	},
	
	resize: function(){
		this.inherited(arguments);
		
		this.mainContainer.resize();
	},

	/**
	 * Loads the report definition from the repository
	 */
	_loadReportDefinition: function(onComplete) {
		this.reportDefinition.loadReportDefinition(dojo_lang.hitch(this, function(reportDefinition){
			this._reportTitle.innerHTML = reportDefinition.title;
			
			if(this.isPopup)
				window.document.title=reportDefinition.title;
			
			if (reportDefinition.getEntryHelp())
				this.setIntroText(reportDefinition.getEntryHelp());
			else
				this.setIntroText(dojo_string.substitute(this.ier_messages.report_intro, [reportDefinition.title]));
			
			//obtain the report definition parameters and starts rendering them one by one
			this._reportDefinition = reportDefinition;				
			this._totalParameters = reportDefinition.getParameterNumber();
			this._currentParameterIdx = 0;
			this._renderNextParameter();
			
			if(onComplete)
				onComplete();
		}));		
	},
	
	_getAttributeDefinition: function(id) {
		for ( var i in this.classAttributeDefinitions) {
			var attrDef = this.classAttributeDefinitions[i];
			if(attrDef.id == id)
				return attrDef;
		}
	},
	
	/**
	 * Loops and renders each parameter separately
	 */
	//TODO: Fix this..we shouldn't be hardcoding and rendering each property like this.  It should grab the property values
	//from the Report Holding class and use those attributes.
	_renderNextParameter: function() {
		var parameters = this._reportDefinition.getParameters();
		this._currentParameter = parameters[this._currentParameterIdx];

		switch (this._currentParameter.symname) {
			case ier_constants.ReportEntry_disposal_action_export :
			case ier_constants.ReportEntry_disposal_action:
				this._renderChoiceList(ier_constants.Id_ActionTypeChoiceList);
				break;
			case ier_constants.ReportEntry_user_name :
				this._renderUserName();
				break;
			case ier_constants.ReportEntry_rm_entity_type :
				this._renderEntityType();
				break;
			case ier_constants.ReportEntry_report_type :
				this._renderReportType();
				break;
			case ier_constants.ReportEntry_ros_name :
				this._renderROSName();
				break;
			case ier_constants.ReportEntry_ros_browse :
				this._renderROSBrowse();
				break;
			case ier_constants.ReportEntry_action :
				this._renderAction();
				break;
			case ier_constants.ReportEntry_review_decision : 
				this._renderChoiceList(ier_constants.Id_ReviewDecisionChoiceList);
				break;
			case ier_constants.ReportEntry_application_type :
				this._renderApplicationType();
				break;
			case ier_constants.ReportEntry_fileplan_name : //"fileplan_name":
				this._renderFilePlanNames();
				break;
			default :
				this._renderDefault();
				break;
		}
	},
	
	/**
	 * Sets the introduction text on the dialog. This is text that appears below the title bar that describes how to use
	 * the dialog.
	 */
	setIntroText: function(text) {
		if (this._introText.firstChild) {
			// Update the existing message text.
			this._introText.firstChild.nodeValue = text;
		} else {
			// Create the message text.
			var textNode = document.createTextNode(text);
			this._introText.appendChild(textNode);
		}
	},
	
	/**
	 * Event called when an input changes
	 */
	onChange: function() {
	},

	/**
	 * Event called when a properties rendering is completed
	 */
	onCompleteRendering: function() {
		this.validateInput();
	},

	/**
	 * Validate the inputs of the properties
	 */
	validateInput: function() {
		var errorField = this._reportEntryPropertiesPane.validate();
		
		//TODO may need to validate that end_date >= start_date before launching the report...
		this._runButton.set("disabled", (errorField != null));
		if(ecm.model.desktop.taskManager.serviceURL != null && ecm.model.desktop.taskManager.serviceURL != ""){
			this._scheduleButton.set("disabled", (errorField != null));
		}
		return (errorField == null);
	},

	/**
	 * Resizes the common properties window
	 */
	resizeCommonProperties: function() {
		this._reportEntryPropertiesPane.resize();
	},

	/**
	 * Event invoked when the cancel button is selected.  It will close the popup
	 */
	onCancel: function() {
		window.close(); 
	},
	
	/**
	 * Runs the report by calling into the RunReportService to trigger a cognos report
	 */
	onRun: function() {
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response)
		{	
		    this.reportResultId = response.reportResultId;
		    this.reportViewer.reportResultJobId = this.reportResultId;
		    
		    //call the report viewer to view the report
		    this.reportViewer.view(this.reportResultId, dojo_lang.hitch(this, function(){
			    dojo_domStyle.set(this.backToParams, "display", "");
			    this.mainContainer.resize();
			    this.mainStackContainer.selectChild(this.resultsPane);
			}));
		}));
		params.requestParams[ier_constants.Param_ReportId] = this.reportDefinition.id;
		
		var data = new Object();
		data[ier_constants.Param_Properties] = this.getPropertiesJSON();//this._reportEntryPropertiesPane.getPropertiesJSON(true);
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_RunReport, ier_constants.PostEncoding, params);
	},
	
	/**
	 * Schedules a report to run in the task pane
	 */
	onScheduleReport: function() {
		var reportData = {
				repository : this.repository,
				reportCriteria : this.getPropertiesJSON(),
				reportDefinition :  this.reportDefinition
		};
		
		var reportDialog = new ier.widget.dialog.ScheduleReportWizardDialog({
			reportData: reportData
		});
		reportDialog.show();
		ier_util_dialog.manage(reportDialog);
		
		this.connect(reportDialog, "onFinish", function(){
			this.onCancel();
		});
	},
	
	/**
	 * Returns the properties contained in the report form
	 */
	getPropertiesJSON: function(){
		 var properties = this._reportEntryPropertiesPane.getPropertiesJSON(true);
			
		 if (properties) {
			 dojo_array.forEach(properties, function(property) {
				 if (property.name == ier_constants.Property_HoldName) 
					 property.name = ier_constants.ReportEntry_hold_name;
				 
				//Due to a bug in ICN, the label field of a MultiValueChoicePane refers to the label selected and not the label
				//of the actual property.  The title property is what we want instead
				var field = this._reportEntryPropertiesPane.getFieldWithName(property.name);
				if(field instanceof ecm.widget.DropDownInput){
					property.label = field.get("title");
				}
			 }, this);
		 }
		 
		 return properties;
	},
	
	/**
	 * Renders string or date time parameters
	 */
	_renderDefault: function() {
		switch (this._currentParameter.datatype) {
			case ier_constants.ReportEntry_Param_StringType: //"String" :
				this._renderStringParameter(this._currentParameter, ier_constants.DataType_String);
				break;
			case ier_constants.ReportEntry_Param_DateTimeType: //"DateTime" :
				this._renderDateTimeParameter(this._currentParameter);
				break;
			default:		
				break;
		};
		
		this._onCompleteRenderingParameter();
	},

	/**
	 * Renders special choice list parameters
	 */
	_renderChoiceList: function(choiceListId) {
		//"{56C6D10C-05FB-4CBD-9872-55B3F5DAFC2E}" is the choice list id of Disposition Action.
		this._reportModel.retrieveChoiceList(this.repository.id, choiceListId, dojo_lang.hitch(this, function(items){
			var choiceList = {choices : items};
			if(items.length > 0)
				this._renderStringParameter(this._currentParameter, ier_constants.DataType_String, items[0].value, choiceList);

			this._onCompleteRenderingParameter();
		}));		
	},
		
	/**
	 * Renders a unique paramater dealing with displaying a list of ROS repositories
	 */
	_renderROSName: function() {
		//"{56C6D10C-05FB-4CBD-9872-55B3F5DAFC2E}" is the choice list id of Disposition Action.
		this.repository.retrieveAssociatedContentRepositories(dojo_lang.hitch(this, function(items){
			var choiceList = {choices : items};	
			if(items.length > 0)
				this._renderStringParameter(this._currentParameter, ier_constants.DataType_String, items[0].value, choiceList);

			this._onCompleteRenderingParameter();
		}));		
	},
	
	/**
	 * Renders a unique parameter dealing with displaying a list of file plan names
	 */
	_renderFilePlanNames: function() {
		this.repository.getFilePlans(dojo_lang.hitch(this, function(fileplans) {
			var items = [];
			for (var i in fileplans) {
				items.push({displayName : fileplans[i].attributes.FolderName, 
					value : fileplans[i].attributes.Name});
			}
			
			var choiceList = {choices : items};
			//TODO: This is a workaround for a bug where the property value for browsing a fileplan is missing
			this._currentParameter.displayname = ier_messages.report_specifyFilePlan;
			this._renderStringParameter(this._currentParameter, ier_constants.DataType_String, items[0].value, choiceList);
			this._onCompleteRenderingParameter();			
		}));
	},
	
	/**
	 * Renders a unique parameter allowing browsing in the ROS repository
	 */
	_renderROSBrowse: function() {
		// skip rendering this parameter for now because Nexus R1 does not support ROS/FPOS crossing.
		this._onCompleteRenderingParameter();
	},
	
	/**
	 * Renders a user name parameter which allows users to pick a user name
	 */
	_renderUserName: function() {
		this._renderStringParameter(this._currentParameter, ier_constants.DataType_User);		
		this._onCompleteRenderingParameter();		
	},
	
	/**
	 * Renders a parameter that allows users to select entity types
	 */
	_renderEntityType: function() {
		var choiceList = {choices : this._currentParameter.values};
		this._currentParameter.cardinality = ier_constants.DataType_Cardinality_LIST;//"LIST";
		this._renderStringParameter(this._currentParameter, ier_constants.DataType_String,  null, choiceList);		
		this._onCompleteRenderingParameter();		
	},
	
	/**
	 * Renders an action parameter
	 */
	_renderAction: function() {
		var choiceList = {choices : [{displayName : this.ier_messages.report_action_opened, 
										value : this.ier_messages.report_action_opened}, 
		                               {displayName : this.ier_messages.report_action_closed, 
										value : this.ier_messages.report_action_closed}]};
		
		this._currentParameter.cardinality = ier_constants.DataType_Cardinality_LIST;//"LIST";
		this._renderStringParameter(this._currentParameter, ier_constants.DataType_String,  null, choiceList);		
		this._onCompleteRenderingParameter();		
	},
	
	/**
	 * Renders a parameter to allow users to select report type
	 */
	_renderReportType: function() {
		this._currentParameter.isreq = true;
		this._renderStringParameter(this._currentParameter, ier_constants.DataType_String);
		this._onCompleteRenderingParameter();		
	},
	
	/**
	 * Renders a parameter that allows the user to select different application types
	 */
	_renderApplicationType: function() {
		var applicationTypeItems = [];
		for (var i in ier_constants.ApplicationTypes) {
			applicationTypeItems.push({ displayName : ier_constants.ApplicationTypes[i], 
										value : ier_constants.ApplicationTypes[i]});			
		}
		var applicationTypeList = {choices : applicationTypeItems};
		this._renderStringParameter(this._currentParameter, 
									ier_constants.DataType_String, 
									null, 
									applicationTypeList);
		
		this._onCompleteRenderingParameter();		
	},
	
	/**
	 * Signals that the current parameter is completed and to move to the next parameter
	 */
	_onCompleteRenderingParameter: function() {
		this._currentParameterIdx = this._currentParameterIdx + 1;
		
		if (this._currentParameterIdx < this._totalParameters)
			this._renderNextParameter();
		else if (this._currentParameterIdx == this._totalParameters)  {
			this._reportEntryPropertiesPane.renderAttributes(this._attributeDefinitions, null, "create");				
			this.onCompleteRendering();
		}
	},
	
	/**
	 * Renders a typical string parameter
	 */
	_renderStringParameter: function(parameter, datatype, defaultValue, choiceList) {
		var criterion = new ecm_model_AttributeDefinition({
			id: parameter.symname,
			name: parameter.displayname,
			repositoryType: this.repository.type,
			dataType: (datatype==null) ? ier_constants.DataType_User : datatype,
			required: parameter.isreq,
			defaultValue: (defaultValue == null) ? [] : defaultValue,
			cardinality: parameter.cardinality ? parameter.cardinality : "SINGLE",
			settability: ier_constants.ReportEntry_Settability,
			choiceList: choiceList,
			contentClass: this._contentClass,
			maxLength: 100,
			uniqueValues: true
		});
		this._attributeDefinitions.push(criterion);		
	},
	
	/**
	 * Renders a typical datetime parameter
	 */
	_renderDateTimeParameter: function(parameter) {
		var criterion = new ecm_model_AttributeDefinition({
			id: parameter.symname,
			name: parameter.displayname,
			repositoryType: this.repository.type,
			dataType: ier_constants.DataType_DateTime,
			minValue: ier_constants.ReportEntry_DateTime_MinValue,
			maxValue: ier_constants.ReportEntry_DateTime_MaxValue,
			required: parameter.isreq,
			cardinality: parameter.cardinality ? parameter.cardinality : "SINGLE",
			settability: ier_constants.ReportEntry_Settability,
			contentClass: this._contentClass,
			format: "M/d/yyyy"
		});
		this._attributeDefinitions.push(criterion);
	},
	_nop: null
});});
