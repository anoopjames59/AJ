define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dijit/form/Button",
	"ecm/model/Desktop",
	"ecm/widget/FilterTextBox",
	"ecm/widget/layout/_LaunchBarDialogPane",
	"ier/constants",
	"ier/messages",
	"ier/widget/FilePlanSelectorTree",
	"dojo/text!./templates/FlyoutPane.html",
	"dijit/layout/BorderContainer", // in template
	"dijit/layout/ContentPane" // in template
], function(dojo_declare, dojo_lang, dojo_construct, Button, ecm_model_desktop, ecm_widget_FilterTextBox, ecm_widget_layout_LaunchBarDialogPane,
		ier_constants, ier_messages, ier_widget_FilePlanSelectorTree, templateString){

/**
 * @name ier.widget.FilePlanFlyoutPane
 * @class Provides a list of fileplans in a tree format
 * @augments ecm.widget.layout.LaunchBarDialogPane
 */
return dojo_declare("ier.widget.layout.FilePlanFlyoutPane", [ecm_widget_layout_LaunchBarDialogPane], {
	/** @lends ier.widget.layout.FilePlanFlyoutPane.prototype */

	// widgetsInTemplate: Boolean
	// Set this to true if your widget contains other widgets
	widgetsInTemplate: true,

	templateString: templateString,

	/**
	 * Handle to the ecm.messages object
	 */
	messages: ier_messages,
	
	postCreate: function() {
		this.logEntry("postCreate");

		this.inherited(arguments);
		this._createToolbar();

		this.logExit("postCreate");
	},
	
	setRepository: function(repository){
		this.repository = repository;
		
		if (this.selected){
			if(repository.isIERLoaded())
				this._fileplanTree.setRepository(repository);
		}
	},

	/**
	 * Sets the focus
	 */
	focus: function() {
		this.logEntry("focus");
		this._filter.focus();
		this.logExit("focus");
	},

	/**
	 * Resets the content of this pane.
	 */
	reset: function() {
		this.logEntry("reset");

		//setting the repository again in case it was set from syncing and not while it was selected
		this.setRepository(this.repository);
		this.needReset = false;

		this.logExit("reset");
	},

	/**
	 * Loads the content of this pane.
	 */
	loadContent: function() {
		this.logEntry("loadContent");

		if (!this.isLoaded) {
			var self = this;
			
			this._fileplanTree = ier_widget_FilePlanSelectorTree();

			this.bottomPane.domNode.appendChild(this._fileplanTree.domNode);

			this.connect(this._fileplanTree, "onFilePlanSelected", function(repository, fileplan){
				ecm_model_desktop.setCurrentFilePlan(fileplan);
				self.selectContentPane(ier_constants.Feature_IERBrowseFilePlan, {
					fileplan: fileplan,
					repository: repository
				});
				self.onRepositoryChange(self, repository);
				self.closePopup();
			});

			this.isLoaded = true;
		}

		this.logExit("loadContent");
	},
	
	/**
	 * Creates a toolbar.
	 */
	_createToolbar: function() {
		this._toolbarNode = dojo_construct.create("div", {
			"class": "toolbar"
		});
		this.bottomPane.domNode.appendChild(this._toolbarNode);
		
		this._toolbarFilterNode = dojo_construct.create("div", {
			"class": "filterArea",
			"style": "float: none"
		});
		this._toolbarNode.appendChild(this._toolbarFilterNode);
		
		this._refreshButton =  new Button({
	        label: ier_messages.refresh,
	        "style": "float:left;  width:25%;",
	        onClick: dojo_lang.hitch(this, function(){
	        	if(this._fileplanTree){
	        		this._fileplanTree.refreshRoot();
	        	}
	        })
	    });
		
		this._filter = new ecm_widget_FilterTextBox({
			placeholder : ier_messages.fileplanFlyoutPane_filterRepositories,
			"style": "float:right;"
		});
		
		this._toolbarFilterNode.appendChild(this._refreshButton.domNode);
		this._toolbarFilterNode.appendChild(this._filter.domNode);
	
		this._filterData = null;
		this.connect(this._filter, "_onInput", "_filterItems");
		this.connect(this._filter, "_setValueAttr", "_filterItems");
	},
	
	/**
	 * Filters the tree based on the user input.
	 */
	_filterItems: function() {
		this.logEntry("_filterItems");

		var filterData = this._filter.get("value");
		var treeModel = this._fileplanTree.getTreeModel();

		// dont execute the filter if nothing changed
		if (this._filterData != filterData && treeModel) {
			this._filterData = filterData;
			treeModel.applyFilter("*" + filterData + "*");
		}

		this.logExit("_filterItems");
	},
	
	/**
	 * Event invoked when a repository changes
	 */
	onRepositoryChange: function(pane, repository) {

	}
});});
