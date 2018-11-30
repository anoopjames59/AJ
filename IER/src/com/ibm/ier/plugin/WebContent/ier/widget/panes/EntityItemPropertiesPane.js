define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dojo/_base/connect",
    	"dojo/DeferredList",
    	"dojo/_base/Deferred",
    	"ecm/model/Desktop",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/EntityItemPropertiesPane.html",
    	"ecm/widget/ContentClassSelector", // in template
    	"ier/widget/panes/CommonPropertiesPane", // in template
    	"dijit/form/Select" //in template
], function(dojo_declare, dojo_lang, dojo_style, dojo_class, dojo_connect, dojo_DeferredList, dojo_Deferred, ecm_model_desktop, ecm_model_Request,
		ier_constants, ier_messages, ier_util, ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * @name ier.widget.EntityItemPropertiesPane
 * @class Provides the basic properties pane for most entity items.  The property pane consists of a class selector with a set of common properties.  Most clases can just reuse this pane.
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.EntityItemPropertiesPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.EntityItemPropertiesPane.prototype */

	templateString: templateString,
	widgetsInTemplate: true,
	_messages: ier_messages,
	_constants: ier_constants,
	
	/**
	 * A boolean that indicates whether an error occurred
	 */
	errorOccurred: false,
	
	/**
	 * ContentClass that this properties pane is connecting to
	 */
	contentClass: null,
	
	/**
	 * whether properties should be readonly
	 */
	isReadOnly: false,
	
	/**
	 * parent folder for this this current item
	 */
	parentFolder: null,
	
	/**
	 * the default name property for this set of properties
	 */
	defaultNameProperty: null,
	
	/**
	 * entity type for the item
	 */
	entityType: null,
	
	/**
	 * whether naming pattern is enabled for this set of properties
	 */
	isNamingPatternEnabled: false,
	
	/**
	 * If the item already exists, the current item for this property pane.  It is used mainly for viewing properties
	 */
	item: null,
	
	/**
	 * cache of item properties after it's rendered
	 */
	itemProperties: null,
	
	/**
	 * whether the item is being created for the first time.  Should be set to false for viewing properties
	 */
	isCreate: true,
	
	/**
	 * whether to disable the content class selector
	 */
	disableContentClassSelector: false,
	
	/**
	 * whether to map the properties of the parent folder to the current item during creation.  This does not work for viewing the properties of an existing item.
	 */
	shouldMapParentFolderProperties: true,
	
	/**
	 * The root class id that this properties pane is using to render the content class selector
	 */
	rootClassId: null,

	/**
	 * Creates the properties pane
	 * @param repository - repository
	 * @param parentFolder - parentFolder
	 * @param rootClassId - the root class Id to display for the content class selector
	 * @param disableContentClassSelector - disableContentClassSelector
	 * @param defaultClass - class to be selected by default
	 * @param excludedClasses - array of classes to be excluded
	 * @param hideContentClassSelector - whether to hide the content class selector but still show the properties.  Will override isReadOnly flag
	 * @param isCreate - whether the item is being created for the first time
	 * @param isReadOnly - whether to make the entire properties pane read only
	 * @param entityType - entityType of the class to be displayed
	 * @param shouldMapParentFolderProperties - whether to map the properties of the parent folder to the current item during creation
	 */
	createRendering: function(parameters) {
		this.logEntry("createRendering");
		this.errorFields = [];
		
		this.parentFolder = parameters.parentFolder ? parameters.parentFolder : this.parentFolder;
		if(this.parentFolder && this.parentFolder.isIERFavorite && this.parentFolder.item){
			this.parentFolder = this.parentFolder.item; // set real parent folder for favorite's child
		}
		this.defaultNameProperty = parameters.defaultNameProperty ? parameters.defaultNameProperty: this.defaultNameProperty;
		this.entityType = parameters.entityType ? parameters.entityType : this.entityType;
		this.isCreate = parameters.isCreate != null ? parameters.isCreate : this.isCreate;
		this.item = parameters.item ? parameters.item : this.item;
		this.isReadOnly = this._setReadOnly(parameters.isReadOnly);
		this.disableContentClassSelector = parameters.disableContentClassSelector != null ? parameters.disableContentClassSelector : this.disableContentClassSelector;
		this.shouldGetItemAttributes = parameters.shouldGetItemAttributes != null ? parameters.shouldGetItemAttributes : this.shouldGetItemAttributes;
		this.shouldMapParentFolderProperties = parameters.shouldMapParentFolderProperties != null ? parameters.shouldMapParentFolderProperties : this.shouldMapParentFolderProperties;
		this.rootClassId = parameters.rootClassId != null ? parameters.rootClassId : this.rootClassId;
		
		//create the content class selector with the provided class Id
		if(parameters.hideContentClassSelector){
			dojo_style.set(this._contentClassSelector.domNode, "display", "none");
			dojo_style.set(this._contentClassSelectorDisabled.domNode, "display", "none");
			dojo_style.set(this._classLabel, "display", "none");
			dojo_class.remove(this.commonPropertiesContainer, "commonPropertiesMargins");
		} else {
			//show the content class selector and remove the disabled text box
			dojo_style.set(this._contentClassSelectorDisabled.domNode, "display", "none");
			dojo_style.set(this._contentClassSelector.domNode, "display", "");
		}
		
		this._contentClassSelector.rootClassId = parameters.rootClassId;
		this._contentClassSelector.excludedItems = (parameters.excludedClasses ? parameters.excludedClasses : null);
		this.setRepository(parameters.repository);
	
		//must set the content class only after it has finished loaded
		this._contentClassLoadedHandler = this.connect(this._contentClassSelector, "onLoaded", function() {
			var selectedClass = parameters.defaultClass ? parameters.defaultClass : this._contentClassSelector.rootClassId;
			var contentClass = dojo_lang.isString(selectedClass) ? this.repository.getContentClass(selectedClass) : selectedClass;
			
			//set the allows instances to be true for properties view so that a user who does not have rights to create
			//a particular class can still view the properties of the class.  AllowInstances = false was blocking the class selector from
			//loading period.
			contentClass.originalAllowsInstances = contentClass.allowsInstances;
			if(!this.isCreate){
				contentClass.allowsInstances = true;
			}
			
			//on a create and if the allow instances is false, put an error message indicating that the user does not have the permission to create
			//this item
			if(this.isCreate && contentClass.allowsInstances == false){
				this.errorOccurred = true;
				this.onErrorOccurred(ier_messages.noPermissionAdd + " " + ier_messages.needAllowInstanceOnClass);
			}
			this._contentClassSelector.setSelected(contentClass);
		});
		
		//renders the common properties based on the content class selected
		this._contentClassSelectedHandler = this.connect(this._contentClassSelector, "onContentClassSelected", dojo_lang.hitch(this, function(contentClass) {
			this.contentClass = contentClass;
			this._contentClassSelector.setDisabled(this.disableContentClassSelector);
			
			//restores allows instances
			if(contentClass.originalAllowsInstances)
				contentClass.allowsInstances = contentClass.originalAllowsInstances;
			
			//grab the rest of the items attributes before rendering.  This is usually done for properties view.  
			//It's only done if the variable shouldGetItemAttributes is set to true
			if(this.item){
				this.item.retrieveAttributes(dojo_lang.hitch(this, function(){
					
					//retrieve the parent's folder attributes if they have not been retrieved yet
					if(this.parentFolder && this.shouldMapParentFolderProperties && this.parentFolder.isAttributesRetrieved && !this.parentFolder.isAttributesRetrieved()){
						//obtain the parent's to map the properties
						this.parentFolder.retrieveAttributes(dojo_lang.hitch(this, function(){
							this.renderProperties(contentClass);
						}), false, true);
					}
					else
						this.renderProperties(contentClass);
				}), false, true);
			}
			else {
				//retrieve the parent's folder attributes if they have not been retrieved yet
				if(this.parentFolder && this.shouldMapParentFolderProperties && this.parentFolder.isAttributesRetrieved && !this.parentFolder.isAttributesRetrieved()){
					this.parentFolder.retrieveAttributes(dojo_lang.hitch(this, function(){
						this.renderProperties(contentClass);
					}), false, true);
				}
				else
					this.renderProperties(contentClass);
			}

		}));
		
		//detect property changes and do validation
		this._commonPropertiesChangedHandler = this.connect(this._commonProperties, "onChange", function(){
			this.onInputChange(this._commonProperties);
			this.onPropertiesChanged();
		});
		
		this.logExit("createRendering");
	},
	
	_setReadOnly: function(readOnly){
		if(readOnly)
			return readOnly;
		else {
			if(this.item){
				return !this.item.privModifyProperties;
			}
		}
	},
	
	/**
	 * Disconnects all the connect handlers
	 */
	disconnectHandlers: function (){
		if(this._contentClassLoadedHandler)
			dojo_connect.disconnect(this._contentClassLoadedHandler);
		
		if(this._contentClassSelectedHandler)
			dojo_connect.disconnect(this._contentClassSelectedHandler);
		
		if(this._commonPropertiesChangedHandler)
			dojo_connect.disconnect(this._commonPropertiesChangedHandler);
	},

	/**
	 * Renders the properties after the content class has selected.
	 * @param contentClass
	 */
	renderProperties: function(contentClass) {
		this.logEntry("renderProperties");
		//clear the common properties
		this._commonProperties.clearRendering();
		var deferArray = []; //an array to store all the deferred objects
	
		//re-retrieves the attribute definitions for the current content class
		contentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(origAttributes) {
			this._renderProperties(origAttributes, deferArray, contentClass);
		}));
		
		this.logExit("renderProperties");
	},
	
	_renderProperties: function(origAttributes, deferArray, contentClass){
		//clone the attributes so it can be changed and won't affect the attributes cached in contentClass.
		var attributes = this.cloneAttributes(origAttributes);
		
		//modify reviewer for edit properties
		if(!this.isCreate){
			var reviewerAttributeDef = this.getAttributeDefinition(ier_constants.Property_Reviewer, attributes);
			
			//push a deferred object so that it won't start rendering the attributes until this is resolved
			if(reviewerAttributeDef && !contentClass.isRecordClass){
				var reviewerDeferred = new dojo_Deferred();
				deferArray.push(reviewerDeferred);
				var reviewer = null;
				
				reviewerAttributeDef.dataType = ier_constants.DataType_User;
				this.repository.getUser(reviewerAttributeDef.defaultValue, dojo_lang.hitch(this, function(user){
					if(!reviewer){
						if(user){
							reviewer = user;
							reviewerAttributeDef.defaultValue = [user];
						}
						reviewerDeferred.resolve();
					}
				}));
			}
		}
		
		//calls onRenderAttributes to allow users of this pane to modify attributes.
		//the deferArray is used to store deferred objects to delay the attributes rendering
		this.onRenderAttributes(attributes, deferArray, contentClass);
		
		//only continue rendering the attributes once all the deferred objects have resolved
		var defs = new dojo_DeferredList(deferArray);
		defs.then(dojo_lang.hitch(this, function(){
			//if naming pattern is enabled, set the naming pattern
			if(this.isNamingPatternEnabled) {
				this.getNamingPatternName(this.parentFolder.id, this.entityType, this.repository, dojo_lang.hitch(this, function(nameFromNamingPattern){
					if(nameFromNamingPattern){
						this._defaultNamingPatternPropertyAttribute = this.getAttributeDefinition(this._namingPatternPropertyField, attributes);
						this._defaultNamingPatternPropertyAttribute.readOnly = true;
						this._defaultNamingPatternPropertyAttribute.defaultValue = nameFromNamingPattern;
					}
					else
						this._defaultNamingPatternPropertyAttribute = null;
					
					this._renderAttributes(attributes);
				}));
			}
			else {
				this._defaultNamingPatternPropertyAttribute = null;
				this._renderAttributes(attributes);
			}
		}));
	},
	
	/**
	 * Renders the attribtes
	 * @param attributes
	 */
	_renderAttributes: function(attributes){
		//this.item is passed to map some properties automatically during create only.  It shouldn't be used for properties dialog of an existing item.
		//the ICN's common properties pane will ignore this.item when the mode is set to "editProperties".
		this._commonProperties.renderAttributes(attributes, this.isCreate ? this.item : null, this.isCreate ? "create" : "editProperties", this.isReadOnly);
		this.setContainerNameFieldValidation(this.defaultNameProperty);
		this._commonProperties.resize();
		this.onCompleteRendering();
	},
	
	/**
	 * Event invoked when the set of attributes is about to be rendered. Push a deferred object to the deferArray if it needs to do asynchronous processing.
	 * It will wait until that is deferred object is resolved before sending it off to be rendered
	 */
	onRenderAttributes: function(attributes, deferArray, contentClass){
		this.logEntry("onRenderAttributes");
		
		this.logExit("onRenderAttributes");
	},
	
	/**
	 * Sets the reviewer and date property to the current user and date.  This should be called only in the onRenderAttributes and is only done 
	 * on creation.
	 * @param attributes
	 * @param callback
	 */
	setReviewerAndCurrentDate: function(attributes, deferArray){
		if(this.isCreate){
			//add a deferred object to delay the attributes rendering until retrieving the current user is done
			var deferred = new dojo_Deferred();
			//sets the reviewer property for add record category
			this.repository.getCurrentUser(dojo_lang.hitch(this, function(user){
				if(user){
					var reviewerDef = this.getAttributeDefinition(ier_constants.Property_Reviewer, attributes);
					if(reviewerDef)
						reviewerDef.defaultValue = [user];
					deferred.resolve();
				}
			}));
		
			var dateOpened = this.getAttributeDefinition(ier_constants.Property_DateOpened, attributes);
			if(dateOpened){
				dateOpened.defaultValue = (ier_util.getISODateString(new Date()));
			}
			
			deferArray.push(deferred);
		}
	},
	
	/**
	 * Sets and enables naming pattern based on the file plan.  The naming pattern will be set during onRenderAttributes
	 */
	enableAndSetNamingPattern: function(folderName, folderIdentifier){
		//check to see if naming pattern is enabled on the file plan
		var currentFilePlan = ecm_model_desktop.getCurrentFilePlan();
		if(currentFilePlan && currentFilePlan.isNamingPatternEnabled()){
			//if it is, retrieve the naming pattern field and set it appropriately
			var namingPattern = currentFilePlan.getNamingPattern();
			this.isNamingPatternEnabled = true;
			this.setNamingPatternField(namingPattern.isAppliedToName() ? folderName : folderIdentifier);
		}
	},

	/**
	 * This event is invoked when the pane has completed rendering
	 */
	onCompleteRendering: function() {
		// summary:
		// Event called when a properties rendering is completed
	},
	
	/**
	 * This event is invoked if any properties are changed
	 */
	onPropertiesChanged: function(){
		
	},

	/**
	 * Validates the pane
	 * @returns {Boolean}
	 */
	validate: function() {
		this.logEntry("validate");
		
		var errorField = this._commonProperties.validate();
		
		this.logExit("validate");
		
		return(errorField == null && !this.errorOccurred);
	},

	/**
	 * Returns the set of properties for this current class
	 * @returns
	 */
	getProperties: function() {
		this.itemProperties = this._commonProperties.getPropertiesJSON(true);
		//add in the default name property since it might be disabled by naming pattern and getDocumentProperties do not return values for readonly attributes
		//defaultNameAttribute will only be set with a value if a naming pattern was applied
		if(this._defaultNamingPatternPropertyAttribute){
			this.itemProperties.push({
				"name": this._defaultNamingPatternPropertyAttribute.id,
				"value": this._defaultNamingPatternPropertyAttribute.defaultValue
			});
		}
		return this.itemProperties;
	},
	
	/**
	 * Returns the property value
	 */
	getPropertyValue: function(propertyName){
		var properties = this.getProperties();
		for(var i in properties){
			var prop = properties[i];
			if(prop && prop.name == propertyName){
				return prop.value;
			}
		}
	},

	/**
	 * Return the content class selected
	 * @returns
	 */
	getContentClass: function() {
		return this.contentClass;
	},
	
	/**
	 * Returns the default name property set
	 */
	getDefaultNameProperty: function() {
		return this.defaultNameProperty;
	},

	/**
	 * Sets the property value for any of the common properties
	 * @param property
	 * @param value
	 * @returns
	 */
	setPropertyValue: function(property, value) {
		return this._commonProperties.setPropertyValue(property, value);
	},
	
	setNamingPatternField: function(property){
		this._namingPatternPropertyField = property;
	},
	
	enablePropertieValuesSyncUp : function(enabled) {
		this._commonProperties.enablePropertieValuesSyncUp(enabled);
	},
	
	resize: function(){
		this.inherited(arguments);
		this.resizeCommonProperties();
	},

	/**
	 * Resizes the common properties pane
	 */
	resizeCommonProperties: function() {
		this._commonProperties.resize();
	},
	
	/**
	 * Get the specified attribute definition from the list of provided attribute definitions
	 */
	getAttributeDefinition: function(id, attributeDefs) {
		for ( var i in attributeDefs) {
			var attrDef = attributeDefs[i];
			if(attrDef.id == id)
				return attrDef;
		}
	},

	/**
	 * Sets the container name field validation.  This validation includes valid windows path name that CE supports
	 * @param propertyName - the propertyName to validate
	 * @param errorMessage - the error message that will show.  This is optional.  If not provided, a default one will be supplied
	 */
	setContainerNameFieldValidation: function(propertyName, errorMessage){
		var _errorMessage = errorMessage ? errorMessage : ier_messages.baseDialog_invalidFolderName;
		var field = this._commonProperties.getFieldWithName(propertyName);
		if(field){
			field.invalidMessage = _errorMessage;
		}
		
	},
	
	/**
	 * Retrieves the namming pattern associated with this IER entity object
	 * @param idOfParentContainerWithPattern - the id of the container that contains the naming pattern.  This is usually the parent container of this entity object.
	 * @param repository
	 * @param onComplete - callback with the new name
	 */
	getNamingPatternName: function(idOfParentContainerWithPattern, entityType, repository, onComplete){
		this.logEntry("getNamingPatternName");
		
		if(this._nameFromNamingPattern){
			if(onComplete)
				onComplete(this._nameFromNamingPattern);
		}
		else {
			var params = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
				this._nameFromNamingPattern = response.nameFromNamingPattern;
	
				if(onComplete)
					onComplete(this._nameFromNamingPattern);
			}));
			params.requestParams[ier_constants.Param_EntityId] = idOfParentContainerWithPattern;
			params.requestParams[ier_constants.Param_EntityType] = entityType;
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetEntityNameFromNamingPattern, ier_constants.PostEncoding, params);
		}
		this.logExit("getNamingPatternName");
	},

	setRepository: function(repository){
		this._contentClassSelector.setRepository(repository);
		this.repository = repository;
		this._commonProperties.setRepository(repository);
	},
	
	/**
	 * Return an array of cloned attributes.  Handles setting the default value for properties dialog
	 * @param attributeDefs
	 * @returns {Array}
	 */
	cloneAttributes: function(attributeDefs){
		var copyAttributes = [];
		var itemAttributes = null;
		var attributesToAvoidMapping = [];
		
		//if this is for the properties dialog, map the items accordingly so they will have values from the existing item
		if(!this.isCreate && this.item){
			itemAttributes = this.item.attributes;
		} else {
			//if this is a new item and it's set to map parent folder properties, map to those properties.  This is done typically in the subCategories and recordFolders
			//so users do not have to reinput all the items all the time.
			if(this.isCreate && this.shouldMapParentFolderProperties){
				itemAttributes = this.parentFolder ? this.parentFolder.attributes : null;
				
				//skip mapping some properties during creation such as name and identifier
				attributesToAvoidMapping.push(ier_constants.Property_RecordCategoryIdentifier);
				attributesToAvoidMapping.push(ier_constants.Property_RecordCategoryName);
				attributesToAvoidMapping.push(ier_constants.Property_RecordFolderIdentifier);
				attributesToAvoidMapping.push(ier_constants.Property_RecordFolderName);
				attributesToAvoidMapping.push(ier_constants.Property_VolumeName);
			}
		}
		
		var continueMapping = true;
		for ( var i in attributeDefs) {
			var attrDef = attributeDefs[i];
			var clonedAttributeDef = attrDef.clone();
			
			//set the attribute definition to read only if the attribute is readonly
			if(this.item && this.item.isAttributeReadOnly(attrDef.id)){
				clonedAttributeDef.readOnly = true;
			}
			
			copyAttributes.push(clonedAttributeDef);
			continueMapping = true;
			
			//map the properties accordingly by looping through and setting the default values against a clone
			if(itemAttributes != null){
				
				//skip mapping some properties during creation such as name and identifier
				if(this.isCreate && this.shouldMapParentFolderProperties){
					//do not map attributes that already has a value or default value
					//do not map system or hidden properties
					if(clonedAttributeDef.system == true || clonedAttributeDef.hidden == true || (clonedAttributeDef.defaultValue && clonedAttributeDef.defaultValue != "") || (clonedAttributeDef.value && clonedAttributeDef.value != "")){
						continueMapping = false;
					} else {
						for(var j in attributesToAvoidMapping){
							if(clonedAttributeDef.id == attributesToAvoidMapping[j]){
								continueMapping = false;
								break;
							}
						}
					}
				}
				
				if(continueMapping && itemAttributes[clonedAttributeDef.id])
					clonedAttributeDef.defaultValue = itemAttributes[clonedAttributeDef.id];
			}
		}
		return copyAttributes;
	},
	
	destroy: function(){
		this.errorFields = null;
		this.disconnectHandlers();
		this.inherited(arguments);
	},

	_nop: null
});});
