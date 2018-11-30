define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-style",
	"dojo/dom-construct",
	"dojo/dom-class",
	"ecm/widget/layout/_RepositorySelectorMixin",
	"ecm/widget/layout/_LaunchBarDialogPane",
	"ier/widget/ReportListing",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"dijit/form/Button",
	"dojo/text!./templates/FlyoutPane.html",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane" // in template
], function(dojo_declare, dojo_lang, dojo_domStyle, dojo_domConstruct, dojo_domClass,
		ecm_widget_layout_RepositorySelectorMixin, ecm_widget_layout_LaunchBarDialogPane, ier_widget_ReportListing,
		ier_constants, ier_messages, ier_util, dijit_Button, templateString, dijit_BorderContainer, dijit_ContentPane) {

/**
 * @name ReportsFlyoutPane
 * @class Implement report flyout pane with a list of reports that users can initiate
 * @augments ecm.widget.layout.LaunchBarDialogPane
 */
return dojo_declare("ier.widget.layout.ReportsFlyoutPane", [ecm_widget_layout_LaunchBarDialogPane, ecm_widget_layout_RepositorySelectorMixin], {
	/** @lends ier.widget.layoutReportsListPane.prototype */

	widgetsInTemplate: true,
	templateString: templateString,
	hideFilterBox: false,
	
	/**
	 * Whether to connect to onItemSelected
	 */
	connectOnItemselected: true,

	/**
	 * Handle to the ier.messages object
	 */
	messages: null,	

	postCreate: function() {
		this.logEntry("postCreate");
		this.inherited(arguments);
		this.setRepositoryTypes("p8");
		this.createRepositorySelector();
		this.doRepositorySelectorConnections();
				
		// If there is more than one repository in the list, show the selector to the user.
		if (this.repositorySelector.getNumRepositories() > 1)
			this.topPane.domNode.appendChild(this.repositorySelector.domNode);
		
		dojo_domClass.add(this.repositorySelector.domNode, "ierReportFilePlanSelector");
		//dojo_domStyle.set(this.repositorySelector.domNode, "float", "left");
		
		 this.refreshButton = new dijit_Button({
	        label: ier_messages.refresh,
	        onClick: dojo_lang.hitch(this, function(){
	        	this.reportListing.refreshListing();
	        })//,
	        //style: "float:right"
	    });
		dojo_domClass.add(this.refreshButton.domNode, "ierRefreshButton");
		 
		 
		this.topPane.domNode.appendChild(this.refreshButton.domNode);
		
		//add the select repository node
		this.noRepositoryNode = dojo_domConstruct.create("div", {
			innerHTML: ier_messages.reportPane_selectAValidFilePlanRepository,
			style: "display:none; text-align:center; margin-top:30px"
		});

		this.noReportNode = dojo_domConstruct.create("div", {
			innerHTML: ier_messages.report_noReportsInstalled,
			style: "display:none; text-align:center; margin-top:30px"
		});
		
		//add reports listing
		this.reportListing = new ier_widget_ReportListing({
			hideFilterBox: this.hideFilterBox
		});
		this.bottomPane.domNode.appendChild(this.noRepositoryNode);
		this.bottomPane.domNode.appendChild(this.noReportNode);
		this.bottomPane.domNode.appendChild(this.reportListing.domNode);
		dojo_domStyle.set(this.reportListing.domNode, "height", "100%");
		this.reportListing.resize();
		
		if(this.connectOnItemselected)
			this.connect(this.reportListing, "onItemSelected", "onItemSelected");
		
		this.connect(this.reportListing, "onReportDefinitionsRetrieved", "onReportDefinitionsRetrieved");
		
		this.logExit("postCreate");

	},
	
	loadContent: function() {
		this.inherited(arguments);
		
		//this handles retrieving the default layout repository, login, and setting it as the repository if no repository has been set yet.
		this.setPaneDefaultLayoutRepository();
		
		if (this.repository && this.repository.connected){
			this.setRepository(this.repository);
		}	
	},
	
	/**
	 * When a report is selected, call utlity method to run report
	 */
	onItemSelected: function(selectedItem)
	{
		ier_util.runReport(this.repository, selectedItem.id);
		this.closePopup();
	},
		
	/**
	 * Sets the repository and loads the report listing
	 */
	setRepository: function(repository) {
		this.logEntry("setRepository");
		
		if(repository){
			this.repository = repository;
			if(this.repository.isIERLoaded()){
				this._setRepository();
			}
			else {
				this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
					this._setRepository();
				}));
			}
		}
		
		this.logExit("setRepository");
	},
	
	_setRepository: function(){
		if(this.repository.isFilePlanRepository()) {		
			dojo_domStyle.set(this.noReportNode, "display", "none");
			dojo_domStyle.set(this.noRepositoryNode, "display", "none");
			dojo_domStyle.set(this.reportListing.domNode, "display", "");
			
			this.isLoaded = true;
			
			this.reportListing.setRepository(this.repository);
			this.reportListing.resize();
			
			//adds a dummy repository to indicate that the user must still pick a repository
			if(this.repositorySelector.getDropdown().options[0].value == "_blankRepository"){
				this.repositorySelector.getDropdown().removeOption("_blankRepository");
			}
			
			this.repositorySelector.getDropdown().set("value", this.repository.id);
			this.refreshButton.set("disabled", false);

		} else {
			//display select repository text
			dojo_domStyle.set(this.noRepositoryNode, "display", "");
			dojo_domStyle.set(this.noReportNode, "display", "none");
			dojo_domStyle.set(this.reportListing.domNode, "display", "none");
			
			//add a blank repository to the selector so it can be selected
			this.repositorySelector.getDropdown().options.unshift({
				value: "_blankRepository",
				label: ier_messages.reportPane_selectAFilePlanRepositoryLabel
			});
			this.repositorySelector.getDropdown().set("value", "_blankRepository");
			this.refreshButton.set("disabled", true);
		}
	},
	
	onReportDefinitionsRetrieved : function(reportDefinitions) {		
		var reportitems = reportDefinitions.length;
		if (reportitems == null || reportitems.length == 0) {
			dojo_domStyle.set(this.noReportNode, "display", "");
			dojo_domStyle.set(this.reportListing.domNode, "display", "none");				
		}
		else {
			dojo_domStyle.set(this.noReportNode, "display", "none");
			dojo_domStyle.set(this.reportListing.domNode, "display", "");	
		}		
	}
});});
