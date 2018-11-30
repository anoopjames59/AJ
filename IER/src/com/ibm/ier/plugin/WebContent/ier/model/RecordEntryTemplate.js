define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/_ModelObject",
	"ecm/model/Request",
	"ecm/model/EntryTemplateOption",
	"ecm/model/EntryTemplatePropertyOptions",
	"ecm/model/AttributeDefinition",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang,
		ecm_model_ModelObject, ecm_model_Request, ecm_model_EntryTemplateOption, ecm_model_EntryTemplatePropertyOptions, ecm_model_AttributeDefinition,
		ier_constants, ier_util){

/**
 * @name ier.model.RecordEntryTemplate
 * @class Represents a RecordEntryTemplate
 * @augments ecm.model._ModelObject
 */
return dojo_declare("ier.model.RecordEntryTemplate", [ecm_model_ModelObject], {
/** @lends ier.model.RecordEntryTemplate.prototype */

	constructor: function(itemJSON) {
		this.logDebug("constructor", "name=" + this.name + " repository=" + this.repository);
		
		this._showRecordClassSelection = new ecm_model_EntryTemplateOption({
			on: true,
			readOnly: true,
			hidden: false
		}); //initialized to enabled & readonly
		this._selectedRecordClassId = null;
		this._selectedRecordClassLabel = null;
		
		this._showClassAndLocationSelectorsStep = true;
		this._showFilePlanLocationSelection = new ecm_model_EntryTemplateOption({
			on: true,
			readOnly: false,
			hidden: false
		}); //initialized to enabled & editable
		this._filePlanFolderLocations  = [];
		this._startingFilePlanLocationId = null;
		this._constrainStartingFilePlanLocation = false;
		
		this._primaryFilePlanLocationId = null;
		
		this._propertiesOptions = [];
		this._showPropertiesStep = true;
		this._retrieved = false;
		this._targetRecordObjectStoreId = null;
		this._targetRecordObjectStoreDisplayName = null;
	},
	
	isRetrieved: function() {
		return this._retrieved;
	},
	
	getPrimaryFilePlanLocationId: function(){
		return this._primaryFilePlanLocationId;
	},
	
	getTargetObjectStoreP8Id: function() {
		return this._targetRecordObjectStoreId;
	},
	
	getTargetRecordObjectStoreDisplayName: function() {
		return this._targetRecordObjectStoreDisplayName;
	},
	
	getRepository: function() {
		return this.repository;
	},

	getDescription: function() {
		return this.description;
	},

	setDescription: function(description) {
		this.description = description;
		this.onChange(this);
	},

	setRepository: function(repository) {
		this.repository = repository;
		this.onChange(this);
	},

	getShowRecordClassSelection: function() {
		return this._showRecordClassSelection;
	},
	
	getShowFilePlanLocationSelection: function() {
		return this._showFilePlanLocationSelection;
	},

	getFilePlanLocations: function() {
		return this._filePlanFolderLocations;
	},

	getStartingFilePlanLocationId: function() {
		return this._startingFilePlanLocationId;
	},

	/**
	 * Constrain to folder selected or its sub-folders for the given file plan starting location
	 * @returns
	 */
	getIsConstrainedToStartingFilePlanLocation: function() {
		return this._constrainStartingFilePlanLocation;
	},

	getShowPropertiesStep: function() {
		return this._showPropertiesStep;
	},
	
	getShowClassAndLocationSelectorsStep: function(){
		return this._showClassAndLocationSelectorsStep;
	},

	getPropertyOptions: function() {
		return this._propertiesOptions;
	},
	
	getSelectedRecordClassId: function() {
		return this._selectedRecordClassId;
	},
	
	getSelectedRecordClassLabel: function() {
		return this._selectedRecordClassLabel;
	},

	retrieveEntryTemplate: function(callback, editMode) {
		if (this._retrieved) {
			if (callback) {
				callback(this);
			}
			this.onEntryTemplateRetrieved(this);
		} else {
			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){
				this._retrieveEntryTemplateCompleted(response, callback);
			}));
			params.requestParams[ier_constants.Param_TemplateName] = this.id;//this._id;
			
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_OpenRecordEntryTemplate, ier_constants.PostEncoding, params);
		}
	},

	_retrieveEntryTemplateCompleted: function(response, callback) {
		this.id = response.id;
		this.name = response.displayName;
		this.setDescription(response.description);
		this._showPropertiesStep = response.showPropertiesStep;
		this._showClassAndLocationSelectorsStep = response.showClassAndLocationSelectorsStep;
		this._selectedRecordClassId = response.selectedRecordClassId;
		this._selectedRecordClassLabel = response.selectedRecordClassLabel;
		this._targetRecordObjectStoreId = response.targetRecordObjectStoreId;
		this._targetRecordObjectStoreDisplayName = response.targetRecordObjectStoreDisplayName;

		if (response.showRecordClassSelection){
			this._showRecordClassSelection = new ecm_model_EntryTemplateOption({
				id: "showRecordClassSelection",
				name: "showRecordClassSelection",
				on: response.showRecordClassSelection.on,
				readOnly: response.showRecordClassSelection.readOnly,
				hidden: response.showRecordClassSelection.hidden
			});
		}
		
		if(response.showFilePlanLocationSelection){
			this._showFilePlanLocationSelection = new ecm_model_EntryTemplateOption({
				id: "showFilePlanLocationSelection",
				name: "showFilePlanLocationSelection",
				on: response.showFilePlanLocationSelection.on,
				readOnly: response.showFilePlanLocationSelection.readOnly,
				hidden: response.showFilePlanLocationSelection.hidden
			});
		}
		
		if (response.filePlanFolderLocations) {
			for ( var i in response.filePlanFolderLocations) {
				var filePlanLocationJSON = response.filePlanFolderLocations[i];
				this._filePlanFolderLocations.push({"folderId" : filePlanLocationJSON.folderId, "objectstore" : filePlanLocationJSON.objectstore});
			}
		}
		
		if(response.constrainStartingFilePlanLocation){
			this._constrainStartingFilePlanLocation = response.constrainStartingFilePlanLocation;
		}
		
		if(response.startingFilePlanLocationId){
			this._startingFilePlanLocationId = response.startingFilePlanLocationId;
		}
		
		if(response.primaryFilePlanLocation){
			this._primaryFilePlanLocationId = response.primaryFilePlanLocation;
		}

		if (response.propertiesOptions) {
			this._propertiesOptions = [];
			for ( var i in response.propertiesOptions) {
				var propOptionsJSON = response.propertiesOptions[i];
				if(propOptionsJSON){
					var propOption = new ecm_model_EntryTemplatePropertyOptions({
						id: propOptionsJSON.name,
						name: propOptionsJSON.name,
						dataType: propOptionsJSON.dataType,
						defaultValue: propOptionsJSON.defaultValue,
						required: propOptionsJSON.required,
						cardinality: propOptionsJSON.cardinality,
						requiredClass: propOptionsJSON.required_template !== undefined ? propOptionsJSON.required_template.template_id : "",
						on: propOptionsJSON.on,
						readOnly: propOptionsJSON.readOnly,
						hidden: propOptionsJSON.hidden
					});
					
					this._propertiesOptions.push(propOption);
				}
			}
		}

		this._retrieved = true;

		if (callback) {
			callback(this);
		}
		this.onRecordEntryTemplateRetrieved(this);
	},
	
	setRecordEntryTemplateAttributeDefs: function(repository, attributeDefs) {
		
		var propsOptions = this.getPropertyOptions();
		if (!propsOptions) {
			return attributeDefs;
		}

		// First create a map by attribute name for faster lookup.
		// Keep the map around for applying property values when the add is executed.
		var origAttrDefsById = {};
		var attrDef;
		for ( var attrDefNdx in attributeDefs) {
			attrDef = attributeDefs[attrDefNdx];
			origAttrDefsById[attrDef.id] = attrDef;
		}
		
		//clone the array of attributes
		var clonedAttributeDefs = attributeDefs.slice(0);

		//clear the array of attributes so it can be reused directly
		attributeDefs.length = 0;
		
		var entryTemplateAttrDef;
		var entryTemplateAttrDefsByName = {};
		var propOptions;
		var defaultValue;
		var isValueRequired;
		var isReadOnly;
		var isHidden;
		var requiredClass;

		// Apply the entry template settings to the attributes.
		// Try to automatically resolve issues with out-of-date entry template settings -
		// changed property data types, deleted properties, new properties, etc.
		for ( var propOptionsNdx in propsOptions) {
			propOptions = propsOptions[propOptionsNdx];

			// Get the full attribute definition for the entry template attribute.
			attrDef = origAttrDefsById[propOptions.id];

			// The entry template attribute may no longer exist in the class or have out of date characteristics.
			// If the attribute is not found or the data type does not match then ignore this entry template attribute.
			// The original attribute definition did not save dates right and used xs:string instead.  So allow this as an exception.
			if (!attrDef || (attrDef.dataType != propOptions.dataType && propOptions.dataType != "xs:date")) {
				continue;
			}

			// Set the default values from the class attribute definition.
			isHidden = attrDef.hidden;
			isReadOnly = attrDef.readOnly;
			isValueRequired = attrDef.required;

			// Apply the entry template hidden option if it is true.
			// The entry template option does not turn off hidden if the class enables hidden.
			if (propOptions.hidden) {
				isHidden = true;
			}

			// Apply the entry template read only option if it is true.
			// The entry template option does not turn off read only if the class enables read only.
			if (propOptions.readOnly) {
				isReadOnly = true;
			}

			// Apply the template required option if it is true.
			// The entry template option does not turn off required if the class enables required.
			if (propOptions.required) {
				isValueRequired = true;
			}

			// If the entry template defines new default values, use them.
			// TBD Remove default values that are no longer valid choices (missing from the associated choice list).
			//     If a hidden or read only property contains an invalid value then an error should be returned.
			//     Might handle this on the server. See DocumentWorkerWizard.java, fetchClassProperties() in XT.
			defaultValue = propOptions.defaultValue;
			
			if (!defaultValue || (defaultValue.length == 0)) {
				// ... else if not, use the attribute definition default values.
				defaultValue = attrDef.defaultValue;
			}

			// If the entry template defines a new required class for the property, use it.
			requiredClass = propOptions.requiredClass;
			if (!requiredClass) {
				requiredClass = attrDef.requiredClass;
			}
			
			entryTemplateAttrDef = attrDef.clone();
			entryTemplateAttrDef.repositoryType = repository.type;
			entryTemplateAttrDef.required = isValueRequired;
			entryTemplateAttrDef.defaultValue = defaultValue;
			entryTemplateAttrDef.readOnly = isReadOnly;
			entryTemplateAttrDef.hidden = isHidden;
			entryTemplateAttrDef.requiredClass = requiredClass;
			
			attributeDefs.push(entryTemplateAttrDef);
			entryTemplateAttrDefsByName[entryTemplateAttrDef.id] = entryTemplateAttrDef;
		}
		
		if(clonedAttributeDefs.length != attributeDefs.length){
			// Now append any attribute definitions that were missing from the template.
			for ( var attrDefNdx in clonedAttributeDefs) {
				attrDef = clonedAttributeDefs[attrDefNdx];
	
				entryTemplateAttrDef = entryTemplateAttrDefsByName[attrDef.id];
	
				if (!entryTemplateAttrDef) {
					if (attrDef.id == "EntryTemplateObjectStoreName") {
						// Extract the entry template OS GUID.
						defaultValue = [ this.id.split(",", 2)[1] ];
					} else if (attrDef.id == "EntryTemplateId") {
						// Extract the entry template item GUID.
						defaultValue = [ this.id.split(",")[2] ];
					} else {
						defaultValue = attrDef.defaultValue;
					}
					
					entryTemplateAttrDef = attrDef.clone();
					entryTemplateAttrDef.repositoryType = repository.type;
					entryTemplateAttrDef.defaultValue = defaultValue;
					
					attributeDefs.push(entryTemplateAttrDef);
					entryTemplateAttrDefsByName[attrDef.id] = entryTemplateAttrDef;
				}			
			}
		}
	},

	onRecordEntryTemplateRetrieved: function(template) {
	},
	
	_nop: null
});});
