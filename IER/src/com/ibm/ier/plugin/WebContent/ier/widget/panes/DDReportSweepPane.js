define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dojo/data/ItemFileWriteStore",
    	"dojo/store/DataStore",
    	"dijit/registry",
    	"ecm/model/Desktop",
    	"ecm/widget/UnselectableFolder",
    	"ecm/widget/FolderSelectorCallback",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/ObjectSelector",
    	"ier/model/DefensibleDisposalSchedule",
    	"dojo/text!./templates/DDReportSweepPane.html",
    	"ecm/widget/HoverHelp", //template
    	"idx/form/NumberTextBox", //template
    	"ecm/widget/ValidationTextBox", //template
    	"ier/widget/_FolderSelectorDropDown", //template
    	"ier/widget/FilePlanRepositorySelector", //template
    	"ier/widget/MultipleFoldersSelector" //template
], function(dojo_declare, dojo_lang, dojo_domConstruct, dojo_style, dojo_class, ItemFileWriteStore, DataStore, dijit_registry, Desktop, 
		ecm_UnselectableFolder, ecm_FolderSelectorCallback, IERBaseDialogPane, ier_constants, ier_messages, ier_util, ObjectSelector, DefensibleDisposalSchedule, templateString){

/**
 * @name ier.widget.panes.DDReportSweepPane
 * @class Provides an interface to schedule a report sweep pane
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.DDReportSweepPane", [IERBaseDialogPane], {
	/** @lends ier.widget.panes.DDReportSweepPane.prototype */

	templateString: templateString,
	widgetsInTemplate: true,
	constants: ier_constants,
	messages: ier_messages,
	
	folder: null,
	
	properties: null,
	
	postCreate: function(){
		this.logEntry("postCreate");

		this.title = ier_messages.scheduleDDReportSweep_title;
		
		//sets the p8 repositories to the file plan repositories selector
		this._repositorySelector.setFilePlanRepositories(Desktop.getP8Repositories());
		
		//Switches repositories based on the repository selector selection
		this.connect(this._repositorySelector, "onSelect", dojo_lang.hitch(this, function(repositories){
			var repository = repositories[0];
			if(repository && repository != this.repository){
			
				this.setRepository(repository);
				
				//removes the dummy repository
				if(this._repositorySelector.getDropdown().options[0].value == "_blankRepository"){
					this._repositorySelector.getDropdown().removeOption("_blankRepository");
				}
			}
		}));
		
		//Connects and listens to the change son the report only selector
		this.connect(this._reportOnlySelector, "onChange", dojo_lang.hitch(this, function(value){
			//if the value is true, disable the workflow-related settings
			if(value == "true"){
				this._disableReportOnlyFields(true);
			}
			else {
				//if the repository has changed or has never been initialized
				if(!this.workflowSelector || this.repository != this.workflowSelector.repository || !this.workflowSelector.repository)
					this._createReportOnlyFields();
				
				this._disableReportOnlyFields(false);
			}
		}));
		
		//add a blank repository to the selector so it can be selected
		this._repositorySelector.getDropdown().options.unshift({
			value: "_blankRepository",
			label: ier_messages.reportPane_selectAFilePlanRepositoryLabel
		});
		this._repositorySelector.getDropdown().set("value", "_blankRepository");
		
		// Set permission to check for folder add.
		var folderSelectorCallback = new ecm_FolderSelectorCallback(ier_constants.Privilege_CanDeclareRecordToContainer, ier_messages.declareRecordDialog_notAllowedToDeclareToFolder);
		this._containerRecordSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);
		this.connect(this._containerRecordSelector, "onFolderSelected", "onInputChange");
		
		this._disableFields(true);
		this.logExit("postCreate");
	},
	
	/**
	 * Sets repository for the pane.
	 */
	setRepository: function(repository, callback) {
		if(repository && repository != this.repository){
			if(repository.isIERLoaded()){
				this._setRepository(repository, callback);
			}
			else {
				repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
					this._setRepository(repository, callback);
				}));
			}			
		}
		
		if(repository == null){
			this._disableFields(true);
		}
	},
	
	/**
	 * Internal method to set repository for the pane.  It will create and then enable all the fields.
	 */
	_setRepository: function(repository, callback){
		this.repository = repository;
		
		//enable all the fields
		this._disableFields(false);
		
		//sets the container selector's root and then hide the disabled text box
		this._multipleFoldersSelector.createRendering(this.repository);
		
		if(this._reportOnlySelector.get("value") == "false")
			this._createReportOnlyFields();
		
		//do not show and disable record container selector if the always declare record settings is false
		var declareRecord = this.repository.defensibleSweepSettings.defensibleSweepAlwaysDeclareRecord;
		if(!declareRecord || declareRecord == "false"){
			this._containerRecordSelector.setDisabled(true);
			dojo_style.set(this._containerDeclareRecordToSelectorRow, "display", "none");
		} else {
			this._containerRecordSelector.setDisabled(false);
			dojo_style.set(this._containerDeclareRecordToSelectorRow, "display", "");
		}
		
		if(callback)
			callback(repository);
	},
	
	/**
	 * Creates the rendering for this pane
	 */
	createRendering: function(repository, item){		
		if(this.repository) {
			this._repositorySelector.getDropdown().set("value", this.repository.id);
		}	
	},
	
	/**
	 * Validation for the pane
	 */
	validate: function(){
		if(this.repository == null || this._advancedDaysTextBox.get("value") == null || 
				this._reportOnlySelector.get("value") == null) {
			return false;
		}
		
		this._connectionPointSelector.validate();
		
		if(this._reportOnlySelector.get("value") == "false"){
			if(this._connectionPointSelector.get("value") == null || this._connectionPointSelector.get("value") == "" || this._needApprovalSelector.get("value") == null 
					|| this.workflowSelector.get("value") == null || this.workflowSelector.get("value") == "" || (!this._containerRecordSelector.getDisabled() && this._containerRecordSelector.get("value") == null)) {
				return false;
			}
		}
			
		return true;
	},
	
	isValidationRequired: function() {
		return true;
	},
	
	/**
	 * Disables or enables the major fields
	 */
	_disableFields: function(disable){
		this._advancedDaysTextBox.set("disabled", disable);
		this._reportOnlySelector.set("disabled", disable);
		this._multipleFoldersSelector.set("disabled", disable);
		if(this._reportOnlySelector.get("value") == "false")
			this._disableReportOnlyFields(disable);
	},
	
	/**
	 * Disables or enables the report only fields
	 */
	_disableReportOnlyFields: function(disable){
		this._connectionPointSelector.set("disabled", disable);
		this._needApprovalSelector.set("disabled", disable);
		
		if(this.workflowSelector){
			this.workflowSelector.set("disabled", disable);
		}
		
		if(this._containerRecordSelector){
			if(disable)
				dojo_class.add(this._containerRecordSelector.domNode, "dijitComboBoxDisabled");
			else
				dojo_class.remove(this._containerRecordSelector.domNode, "dijitComboBoxDisabled");
				
			this._containerRecordSelector.setDisabled(disable);
		}
	},
	
	/**
	 * Create the report only fields by setting the folder selectors and connection points
	 */
	_createReportOnlyFields: function(){
		if(this.repository){
			if(this._containerRecordSelector){
				this._containerRecordSelector.setRoot(this.repository);
				dojo_style.set(this._containerRecordSelectorTextBox.domNode, "display", "none");
				dojo_style.set(this._containerRecordSelector.domNode, "display", "");
								
				var propertiesRecordContainerId = this.properties ? this.properties.containerToDeclareRecordId : null;
				var recordContainerId = propertiesRecordContainerId ? propertiesRecordContainerId : this.repository.defensibleSweepSettings.defensibleDisposalRecordContainerId;
				if(recordContainerId){
					this.repository.retrieveItem(recordContainerId, dojo_lang.hitch(this, function(itemRetrieved){
						this._containerRecordSelector.setSelected(itemRetrieved);
					}));
				}
			}
			
			this._createWorkflowSelector(this.repository);
			
			//set the repository's connection point as the only connection point
			this._connectionPoints = null;
			this._setConnectionPoints();
		}
	},
	
	/**
	 * Event thrown when an input changes
	 */
	onInputChange: function() {
		
	},
	
	/**
	 * Creates the workflow selector and sets  it to the new repository
	 */
	_createWorkflowSelector: function(repository){
		if(!this.workflowSelector){
			this.workflowSelector = new ObjectSelector({
				label: ier_messages.scheduleDDReportSweep_containers,
				labelId: this.id + "_workflowSelector",
				showVersionSelection: true,
				type: ier_constants.WorkflowType_BasicSchedule,
				disableContextMenu: true,
			});
			dojo_domConstruct.place(this.workflowSelector.domNode, this._workflowSelectorContainer, "only");
		}
		
		this.workflowSelector.setRepository(repository);
		this.workflowSelector.setObjectClassName(ier_constants.ClassName_WorkflowDefinition);
		
		if (this.workflowSelector)
			this.connect(this.workflowSelector, "onChange", "onInputChange");
		
		//set the default workflow based on the given properties
		//if that's not set, attempt to use the default workflow from the repository settings
		var propertiesWorkflowId = this.properties ? this.properties.defensibleDisposalWorkflowId : null;
		var defaultWorkflowId =  propertiesWorkflowId ? propertiesWorkflowId : this.repository.defensibleSweepSettings.defensibleDisposalWorkflowId;
		if(defaultWorkflowId){
			this.repository.retrieveItem(defaultWorkflowId, dojo_lang.hitch(this, function(itemRetrieved){
				this.workflowSelector.setSelectedItem(itemRetrieved);
			}), "WorkflowDefinition");
		}
		
		dojo_style.set(this._workflowDisabledButton.domNode, "display", "none");
	},
	
	/**
	 * Sets the connection selector by grabbing the connection points that are cached from the repository or grabbing a fresh list
	 */
	_setConnectionPoints: function() {
		if (this._connectionPoints == null) {
			var repositoryId = this.repository.id;
			var params = {
				action: "list",
				repositoryId: repositoryId
			};
			
			ecm.model.Request.invokeServiceAPI("p8/listConnectionPoints", null, {
				requestParams: params,
				requestCompleteCallback: dojo_lang.hitch(this, function(response){
					if (response != null) {
						this._connectionPoints = response.list;
						this._setConnectionPointSelector();
						this._setConnectPointValue();
					}
				})
			});
		} else {
			this._setConnectionPointSelector();
			this._setConnectPointValue();
		}
	},
	
	_setConnectPointValue: function(){
		var connPoint = null;
		
		if(this.properties && this.properties.connectionPoint)
			connPoint = this.properties.connectionPoint;
		else if(this.repository.connectionPoint){
			var cp = this.repository.connectionPoint.split(":");
			if(cp && cp[0])
				connPoint = cp[0];
		}
		else {
			connPoint = null;
		}
			
		this._connectionPointSelector.set("value", connPoint);
	},

	/**
	 * Sets the provided connection points to the connection point selector
	 */
	_setConnectionPointSelector: function() {
		if (this._connectionPointSelector.store && this._connectionPointSelector.store.store) {
			this._connectionPointSelector.store.store.close();
		}
		var storeData = [];
		for ( var i = 0; i < this._connectionPoints.length; i++) {
			var connPoint = this._connectionPoints[i].split(":");
			if(connPoint && connPoint[0]){
				storeData.push({
					"value": connPoint[0],
					"label": connPoint[0]
				});
			}
		}

		this._connectionPointSelector.store = new DataStore({
			"store": new ItemFileWriteStore({
				data: {
					identifier: 'value',
					label: 'label',
					items: storeData
				}
			})
		});
	},
	
	_setPropertiesAttr: function(properties){
		if(properties){
			this.properties = properties;
			this.setRepository(properties.repository, dojo_lang.hitch(this, function(repository){
				this._repositorySelector.getDropdown().set("value", repository.id);
				if(this._multipleFoldersSelector) {
					var propertiesRecordContainerId = this.properties ? this.properties.containerIds : null;
					if(propertiesRecordContainerId){
						this.repository.retrieveItem(propertiesRecordContainerId, dojo_lang.hitch(this, function(itemRetrieved){
							this._multipleFoldersSelector.addSelectedFolder(itemRetrieved);
						}));
					}
				}
				
				this._advancedDaysTextBox.set("value", properties.advancedDays);
				this._reportOnlySelector.set("value", properties.reportOnly);
				this._needApprovalSelector.set("value", properties.needApproval);
			}));
		}
	},
	
	_getPropertiesAttr: function(){
		var reportOnly = this._reportOnlySelector.get("value");
		return {
			repository: this.repository,
			containerIds: this._multipleFoldersSelector.get("value"),
			containerNames: this._multipleFoldersSelector.getSelectedNames(),
			advancedDays: this._advancedDaysTextBox.get("value"),
			reportOnly: this._reportOnlySelector.get("value"),
			connectionPoint: reportOnly == "false" ? this._connectionPointSelector.get("value") : null,
			needApproval: reportOnly == "false" ? this._needApprovalSelector.get("value"): null,
			defensibleDisposalWorkflowId: reportOnly == "false" ? this.workflowSelector.get("value"): null,
			containerToDeclareRecordId: reportOnly == "false" && this._containerRecordSelector ? this._containerRecordSelector.get("value") : null
		};
	}
});});
