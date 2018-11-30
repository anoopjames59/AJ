define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/data/ItemFileWriteStore",
    	"dojo/dom-style",
    	"dojo/keys",
    	"dojo/store/DataStore",
    	"dojo/string",
    	"dijit/Tooltip",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/RecordDeclareGeneralPane.html",
    	"dijit/form/FilteringSelect", // in template
    	"ecm/widget/HoverHelp" // in template
], function(dojo_declare, dojo_lang, dojo_data_ItemFileWriteStore, dojo_style, dojo_keys, dojo_store_DataStore, dojo_string, dijit_Tooltip,
		ier_constants, ier_messages, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * The general properties for the record declaration pane.  It contains a record entry template selector that can set and prefill a number of properties.  
 * @name ier.widget.RecordDeclareGeneralPane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.RecordDeclareGeneralPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.RecordDeclareGeneralPane */

	templateString: templateString,
	_entryTemplateSelectorToolTip: null,
	_entryTemplates: null,
	_declareDialog: null,

	postCreate: function() {
		this.logEntry("postCreate()");
		this.inherited(arguments);
		
		var learnMoreLink = ier_constants.IER_RootHelpLink + "/topic/com.ibm.p8.fimo.user.doc/ae_help/workplace/fimo_wp_entrytemplate.htm";
		
		this._entryTemplateSelectorHoverHelp.message = ier_messages.declareRecordDialog_recordEntryTemplateHoverHelp + 
			"  " + this.createHtmlLink(learnMoreLink, ier_messages.dialog_LearnMoreLink);
		
		this.logExit("postCreate()");
	},

	/**
	 * Creates the rendering for the pane
	 * @param repository
	 * @param declareDialog
	 */
	createRendering: function(repository, declareDialog) {
		this.logEntry("createRendering()");
		
		this.repository = repository;
		this._declareDialog = declareDialog;
		
		this.createRecordEntryTemplateSelectorTooltip();
		
		this.logExit("createRendering()");
	},
	
	/**
	 * Loads all the available record entry templates for the current repository.  
	 */
	loadRecordEntryTemplates: function(){
		this.logEntry("loadRecordEntryTemplates()");
		
		this._entryTemplateSelector.reset();
		this._entryTemplateSelector.store = null;
		
		this.repository.retrieveRecordEntryTemplates(dojo_lang.hitch(this, function(entryTemplates) {
			this._entryTemplates = entryTemplates;
			var templateItems = [];
			for ( var i = 0; i < entryTemplates.length; i++) {
				var name = entryTemplates[i].id == null ? "" : entryTemplates[i].name;
				templateItems.push({
					label: name,
					value: String(i)
				});
			}
			
			var templateItemsStore = new dojo_store_DataStore({
				"store": new dojo_data_ItemFileWriteStore({
					data: {
						identifier: "value",
						label: "label",
						items: templateItems
					}
				})
			});

			this._entryTemplateSelector.store = templateItemsStore;
			this._entryTemplateSelector.set("disabled", false);
			if(this._entryTemplateSelector.getValue())
				this._entryTemplateSelector.filter(this._entryTemplateSelector.getValue());
			else {
				this._entryTemplateSelector.focus();
				this._entryTemplateSelector.loadDropDown();
			}
		}));
		
		this.logExit("loadRecordEntryTemplates()");
	},

	/**
	 * Invoked when a different record entry template is selected
	 */
	_onRecordEntryTemplateSelectorChange: function() {
		this.logEntry("onRecordEntryTemplateSelectorChange)");
		
		// Changing the entry template. Make sure the tooltip for the old selection is closed.
		this.clearRecordEntryTemplateSelectorTooltip();
		
		this._declareDialog.clearMessage();

		var selectedEntryTemplate = null;

		if (this._entryTemplates) {
			var value = this._entryTemplateSelector.get("value");

			if (value) {
				var i = Number(value);
				if (i >= 0 && i < this._entryTemplates.length) {
					selectedEntryTemplate = this._entryTemplates[i];
				}
			}
		}
		
		//selected empty entry template
		if(!selectedEntryTemplate || selectedEntryTemplate.id == null){
			this._entryTemplate = null;
			var propertiesPane = this._declareDialog._recordDeclarePropertiesPane;
			dojo_style.set(propertiesPane.getRepositorySelectorRow(), "display", "");
			dojo_style.set(propertiesPane.getCommonPropertiesDiv(), "display", "none");
			dojo_style.set(propertiesPane.getFolderSelectTableRow(), "display", "");
			dojo_style.set(propertiesPane.getRecordClassSelectorTableRow(), "display", "");
			propertiesPane.resetPropertiesPane();
		}
		else {
			// If an entry template was selected and it is not the current template...
			if (selectedEntryTemplate && (!this._entryTemplate || (selectedEntryTemplate.id != this._entryTemplate.id))) {
				// Clear the currently selected entry template.
				this._entryTemplate = selectedEntryTemplate;
				if(selectedEntryTemplate.isRetrieved()){
					var targetRepository = this._declareDialog._recordDeclarePropertiesPane.getTargetFilePlanRepository(selectedEntryTemplate.getTargetObjectStoreP8Id());
					this.applyEntryTemplate(targetRepository);
				}
				else {
					// Retrieve the full entry template.	
					selectedEntryTemplate.retrieveEntryTemplate(dojo_lang.hitch(this, function(entryTemplate) {
						this._entryTemplate = entryTemplate;
						var propertiesPane = this._declareDialog._recordDeclarePropertiesPane;
						var targetRepository = propertiesPane.getTargetFilePlanRepository(this._entryTemplate.getTargetObjectStoreP8Id());
						
						if(!targetRepository)
							//targetRepository is not available
							this._declareDialog.setMessage(dojo_string.substitute(ier_messages.declareRecordDialog_entryTemplateRepositoryNotFound, 
									[this._entryTemplate.getTargetRecordObjectStoreDisplayName()]), "warning");
						else {				
							if(!targetRepository.isIERLoaded()){
								targetRepository.loadIERRepository(dojo_lang.hitch(this, function(repository){
									this.applyEntryTemplate(repository);
								}));
							}
							else {
								this.applyEntryTemplate(targetRepository);
							}
						}
						
					}));
				}
			}
		}
		
		this.onInputChange(this._entryTemplateSelector);
		
		this.logExit("onRecordEntryTemplateSelectorChange");
	},

	/**
	 * Invoked when the record entry template selector is clicked
	 */
	_onRecordEntryTemplateSelectorInvoked: function(evt) {
		//only load the entry templates if there is no keyCode so it's not a key press.
		//if there's a keycode, then it's a key press and it should only load based on ENTER, SPACE, or DOWN_ARROW
		if(!evt.keyCode || (evt.keyCode == dojo_keys.ENTER || evt.keyCode == dojo_keys.SPACE || evt.keycode == dojo_keys.DOWN_ARROW)){
			//only load all the record entry templates if invoked manually.
			//this way it won't load entry templates if users aren't using them.
			if (!this._entryTemplates) {
				this.loadRecordEntryTemplates();
			}
		}
	},
	
	/**
	 * Applies the record entry template
	 * @param targetRepository
	 */
	applyEntryTemplate: function(targetRepository) {
		this.logEntry("applyEntryTemplate");
		
		this.setRecordEntryTemplateSelectorTooltip(this._entryTemplate);
		
		var propertiesPane = this._declareDialog._recordDeclarePropertiesPane;
		var constrainToStartingFilePlanLocation = this._entryTemplate.getIsConstrainedToStartingFilePlanLocation();
		var showClassAndLocationSelectorsStep = this._entryTemplate.getShowClassAndLocationSelectorsStep();
		var showPropertiesStep = this._entryTemplate.getShowPropertiesStep();
		var showFilePlanLocationSelection = this._entryTemplate.getShowFilePlanLocationSelection();
		var primaryFilePlanLocationId = this._entryTemplate.getPrimaryFilePlanLocationId();
		var showRecordClassSelection = this._entryTemplate.getShowRecordClassSelection();
		var startingFilePlanLocation = this._entryTemplate.getStartingFilePlanLocationId();
		var filePlanLocations = this._entryTemplate.getFilePlanLocations();
		
		if(this.isLogDebug())
			this.logDebug("applyEntryTemplate", "constrainToStartingFilePlanLocation=" + constrainToStartingFilePlanLocation + " showClassAndLocationSelectorsStep=" + showClassAndLocationSelectorsStep + " startingFilePlanLocation=" + startingFilePlanLocation + " showRecordClassSelection=" + showRecordClassSelection);

		propertiesPane.resetValues();
		
		propertiesPane.setIsFromEntryTemplate(true);
		
		//set file plan repository
		propertiesPane.setFilePlanRepository(targetRepository);
		
		//recordClassId
		var selectedRecordClassId = this._entryTemplate.getSelectedRecordClassId();
		propertiesPane.setRecordClassLabel(this._entryTemplate.getSelectedRecordClassLabel());
		
		//modify the attributes according to property options
		this._entryTemplateOnRenderHandler = this.connect(propertiesPane, "onRenderAttributes", function(attributes){
			if(this._entryTemplate)
				this._entryTemplate.setRecordEntryTemplateAttributeDefs(targetRepository, attributes);
			
			this.disconnect(this._entryTemplateOnRenderHandler);
		});
		
		//Since the folder selector tree does not open to a selected location due to technical issues (it will be required to open up multiple nodes at once), there's
		//really no way to support starting file plan locaion.  The only way is if constrainedToStartingFilePlan location is selected as well and use that new location as the root.
		//If there's a starting file plan location and no constraint is set a warning message.
		//if there's a starting file plan location and there's a constraint to it, set it as the root.
		if(startingFilePlanLocation && !constrainToStartingFilePlanLocation)
			this._declareDialog.setMessage(ier_messages.declareRecordDialog_ignoreStartingFilePlanLocation, "warning");
		var newFilePlanLocationRoot = (startingFilePlanLocation && constrainToStartingFilePlanLocation) ? startingFilePlanLocation : null;
		
		//renders the folder and class selectors and then the subsequent properties
		propertiesPane.renderFolderAndClassSelectors(targetRepository, newFilePlanLocationRoot, selectedRecordClassId, dojo_lang.hitch(this, function() {
			
			//make fileplan location selector hidden, readonly, or visible
			var folderSelector = propertiesPane.getFolderSelectorWidget();
			if(showFilePlanLocationSelection.hidden == true || showClassAndLocationSelectorsStep == false)
				dojo_style.set(propertiesPane.getFolderSelectTableRow(), "display", "none");
			else if(showFilePlanLocationSelection.readOnly == true){
				folderSelector.setDisabled(true);
				dojo_style.set(propertiesPane.getFolderSelectTableRow(), "display", "");
			}
			else {
				folderSelector.setDisabled(false);
				dojo_style.set(propertiesPane.getFolderSelectTableRow(), "display", "");
			}
			
			//if there's a file location set, use it
			if(primaryFilePlanLocationId != null){
				propertiesPane.setTargetLocation(targetRepository, primaryFilePlanLocationId);
				
				//add a warning that other file plan locations will be ignored.
				if(filePlanLocations.length > 1 )
					this._declareDialog.setMessage(ier_messages.declareRecordDialog_multipleFileplanLocationsIgnored, "warning");
			}
			//else if there's a starting file location use that and set it.  It's not working right since the folderSelector widget doesn't support automatically having a path to the starting location.
			else if(startingFilePlanLocation)
				propertiesPane.setTargetLocation(targetRepository, startingFilePlanLocation);
		}));
		
		//end of entry template rendering
		this._entryTemplateOnCompleteHandler = this.connect(propertiesPane, "onCompleteRendering", function(attributes){
			this.disconnect(this._entryTemplateOnCompleteHandler );
		});
		
		//remove repository selector
		dojo_style.set(propertiesPane.getRepositorySelectorRow(), "display", "none");
		
		//remove the propertes panes if necessary
		if(showPropertiesStep == false)
			dojo_style.set(propertiesPane.getCommonPropertiesDiv(), "display", "none");
		else 
			dojo_style.set(propertiesPane.getCommonPropertiesDiv(), "display", "");
		
		//make content class selector hidden, readyonly, or visible
		var recordClassSelector = propertiesPane.getRecordClassSelectorWidget();
		if(showRecordClassSelection.hidden == true || showClassAndLocationSelectorsStep == false)
			dojo_style.set(propertiesPane.getRecordClassSelectorTableRow(), "display", "none");
		else if(showRecordClassSelection.readOnly == true){
			recordClassSelector.setDisabled(true);
			dojo_style.set(propertiesPane.getRecordClassSelectorTableRow(), "display", "");
		}
		else {
			recordClassSelector.setDisabled(false);
			dojo_style.set(propertiesPane.getRecordClassSelectorTableRow(), "display", "");
		}
		
		this.logExit("applyEntryTemplate");
	},

	setRecordEntryTemplateSelectorTooltip: function(entryTemplate) {
		// Make sure the currently active tooltip is closed and cleared.
		this.clearRecordEntryTemplateSelectorTooltip();

		this.entryTemplateToolTip = this._entryTemplateSelectorToolTip;
		if (entryTemplate && entryTemplate.getDescription()) {
			this._entryTemplateSelectorToolTip.setAttribute("label", entryTemplate.getDescription());
		}
	},
	
	clearRecordEntryTemplateSelectorTooltip: function(){
		if (this._entryTemplateSelectorToolTip) {
			this._entryTemplateSelectorToolTip.close();
			this._entryTemplateSelectorToolTip.setAttribute("label", "");
		}
	},
	
	createRecordEntryTemplateSelectorTooltip: function(){
		if (!this._entryTemplateSelectorToolTip) {
			// Create the entry template tooltip and associate it with the entry template selector here 
			// so as not to override the required tooltip message that is displayed when an entry template 
			// has not been selected.
			this._entryTemplateSelectorToolTip = new dijit_Tooltip({
				position: [ "above", "below", "after", "before" ]
			});
			// Display the tooltip when the control gets focus.
			this.connect(this._entryTemplateSelector, "_onFocus", function() {
				if (this._entryTemplateSelectorToolTip.label.length > 0) {
					this._entryTemplateSelectorToolTip.open(this._entryTemplateSelector.domNode);
				}
			});
			
			this.connect(this._entryTemplateSelector, "_onBlur", function() {
				this._entryTemplateSelectorToolTip.close();
			});
			
			this.connect(this._entryTemplateSelector, "onMouseOver", function() {
				if (this._entryTemplateSelectorToolTip.label.length > 0) {
					this._entryTemplateSelectorToolTip.open(this._entryTemplateSelector.domNode);
				}
			});
			
			this.connect(this._entryTemplateSelector, "onMouseOut", function() {
				this._entryTemplateSelectorToolTip.close();
			});
		}
	},
	
	isValidationRequired: function() {
		return false;
	},
	
	destroy: function(){
		if (this._entryTemplateSelectorToolTip) {
			this._entryTemplateSelectorToolTip.close();
			this._entryTemplateSelectorToolTip.destroy();
			this._entryTemplateSelectorToolTip = null;
		}
		
		this.inherited(arguments);
	}
});});
