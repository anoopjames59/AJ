define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/ContentItem",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang, ecm_model_ContentItem, Request, ier_constants, ier_util){


/**
 * @name ier.model._BaseEntityObject
 * @class The base class of all IER model objects. Other classes within the model inherit this class, which provides
 *        some common properties and logging.
 * @public
 */
var _BaseEntityObject = dojo_declare("ier.model._BaseEntityObject", [ecm_model_ContentItem], {
	/** @lends ier.model._BaseEntityObject.prototype */

	/**
	 * Constructs the model object.
	 * @param id
	 *            A unique identifier for the content item.
	 * @param name
	 *            A name for the content item. The name can be displayed in the user interface.
	 * @param repository
	 *            An instance of {@link ecm.model.Repository} for the repository containing the content item.
	 * @param attributes
	 *            An object containing the values of the attributes of the content item.
	 * @param properties
	 *            An object containing additional properties of the content item. This includes privileges and other
	 *            properties.
	 * @param resultSet
	 *            An instance of {@link ecm.model.ResultSet} for the result set that this content item is a member of,
	 *            if any.
	 * @param parent
	 *            An instance of {@link ecm.model.ContentItem} for the content item that this item is a child of, if
	 *            any.
	 * @param attributeType
	 *            An object containing the types of the attributes.
	 * @param attributeFormats
	 *            An object containing the formats of the attributes.
	 * @param objectStoreId
	 *            A unique identifier of the object store for the P8 content item.
	 */
	constructor: function(id, name, repository, attributes, properties, resultSet, parent, attributeTypes, attributeFormats, objectStoreId) {
		this.timestamp = new Date().getTime();
	},
	
	/**
	 * Returns the timestamp of when this entity object was created in the cache.
	 */
	getTimeStamp: function(){
		return this.timestamp;
	},
	
	/**
	 * Returns whether the item is fully loaded with all properties and attributes.  There are cases where stubs are created for
	 * performance reasons
	 */
	isIERLoaded: function(){
		return this.IERLoaded == true && this.getEntityType() != null;
	},
	
	/**
	 * Refresh the properties of this current entity item by making a call to the server and updating them.
	 */
	refreshProperties: function() {
		this.repository.retrieveItem(this.id, dojo_lang.hitch(this, function(item){
			this.update(item);
		}), this.getClassName());
	},

	/**
	 * Provides the RM description of this entity object
	 * @returns
	 */
	getRMDescription: function(){
		return this.attributes[ier_constants.Property_RMEntityDescription];
	},

	/**
	 * Provides the entity type of this entity object.
	 */
	getEntityType: function() {
		return this["entityType"];
	},
	
	/**
	 * Provides the allowed RMTypes array
	 */
	getAllowedRMTypes: function() {
		return this.attributes[ier_constants.Property_AllowedRMTypes];
	},
	
	setAllowedRMTypes: function(types){
		this.attributes[ier_constants.Property_AllowedRMTypes] = types;
	},

	/**
	 * Provides the class name of this entity object.
	 * @returns
	 */
	getClassName: function() {
		return this["template"];
	},

	/**
	 * Provides the mimetype of this entity object
	 * @returns
	 */
	getMimeType: function() {
		return this.getContentType();
	},

	/**
	 * Returns the P8 Guid ID associated with the ID property stored.  Typically, the ContentItem ID is a combination of {CLASS},{P8REPOSITORY_ID},{P8ID}
	 * @returns
	 */
	getGuidId: function() {
		return this.id.split(",")[2];
	},
	
	getCreateDate: function() {
		return this.attributes[ier_constants.Property_CreateDate];
	},
	
	/**
	 * Obtains an object item value by retrieving it from the cache
	 * @returns
	 */
	getIERObjectItem: function(property) {
		if(this.attributes[property]){
			var objectItem = this.getItemValue(property);
			if(objectItem){
				if(!objectItem.name)
					objectItem.name = this.getDisplayValue(property);
				
				if(!objectItem.name)
					objectItem.name = this.nameProperty ? this.attributes[this.nameProperty] : null;
					
				return objectItem;
			}
		}
		return null;
	},
	
	/**
	 * Makes an attempt to obtain the object item value by looking at the cache first.  If it doesn't exist, retrieve it from fetching.
	 */
	fetchIERObjectItem: function(property, callback){
		var objectItem = this.getIERObjectItem(property);
		var itemId = this.attributes[property];
		if(objectItem && objectItem.id == itemId && objectItem.attributes){
			callback(objectItem);
		}	
		else {
			if(itemId){
				var className = null;
				if(itemId.indexOf(",") == -1)
					className = ier_util.getClassName(property);
					
				this.repository.retrieveItem(itemId, dojo_lang.hitch(this, function(itemRetrieved){
					itemRetrieved.IERLoaded = true;
					this.attributeItems[property] = itemRetrieved;
					callback(itemRetrieved);
				}), className);
			}
			else {
				callback(null);
			}
		}
	},
	
	/**
	 * Obtain the legacy disposition schedule within this item
	 * @returns
	 */
	getLegacyDispositionSchedule: function(callback) {
		this.fetchIERObjectItem(ier_constants.Property_DispositionSchedule, callback);
	},
	
	/**
	 * Retrieves all attributes for the item. Depending on how the item was originally created, it may only contain
	 * a subset of the attributes. This function will retrieve any additional missing attributes.
	 * 
	 * @param callback
	 *            A function that is called when the attribute retrieve has completed.
	 * @param backgroundRequest
	 *            A boolean value indicating whether this request should be run in the back ground (can be null).
	 */
	retrieveAttributes: function(callback, backgroundRequest) {
		//tag that the item has its attributes retrieved already
		this.inherited(arguments);
		//this.ierAttributesRetrieved = true;
	},
	
	isAttributesRetrieved: function(){
		return this.ierAttributesRetrieved;
	},
	
	/**
	 * Provides a different update where it will loop through and only replace what has changed.  This is especially for attributes so that the entire set of attributes wouldn't be replaced
	 * 
	 * Updates this item from the contents of the passed-in item. The update is only allowed if it is for the same
	 * document. The context of this item (result set, parent, and repository) is preserved.
	 */
	updatePropertiesAndAttributes: function(item, refresh) {
		var skipRefresh = refresh != null ? !refresh : true;
		
		//save off the old attributes
		var attributes = this.attributes;
		var attributeTypes = this.attributeTypes;
		var attributeFormats = this.attributeFormats;

		//update item by taking in all the new properties and the new attributes
		this.update(item, true);

		//merge the old attributes and the new attributes back together - allowing any items in the new attributes to override the old one
		dojo_declare.safeMixin(attributes, this.attributes);
		dojo_declare.safeMixin(attributeTypes, this.attributeTypes);
		dojo_declare.safeMixin(attributeFormats, this.attributeFormats);

		//replace the new attributes with the merged set of attributes
		this.attributes = attributes;
		this.attributeTypes = attributeTypes;
		this.attributeFormats = attributeFormats;
		if (!skipRefresh) {
			this.onChange([
				this
			]);
		}
	},
	
	/**
	 * Performs a refresh by gathering new information from the current item and the parent. This is used to update areas where a single change will cause multiple
	 * other areas to change.  For instance after closing a folder, the refresh will update the current item's attributes and properties and also the parent folder so that
	 * everything will be in synced.
	 */
	refreshIERStates: function(callback, refresh){
		this.logEntry("refreshIERStates");

		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){
			if(response){
				if(response.item){
					this.updatePropertiesAndAttributes(ier_util.createBaseEntityItem(response.item, this.repository, null, this.parent));
				}
				
				if(response.parent && this.parent && this.parent instanceof ier.model._BaseEntityObject){
					this.parent.updatePropertiesAndAttributes(ier_util.createBaseEntityItem(response.parent, this.repository, null, this.parent.parent), !refresh);
				} else {
					if(this.parent instanceof ecm.model.Favorite){
						this.parent.retrieveFavorite();
					}
				}
				
				if(refresh)
					this.refresh();
				
				if(callback){
					callback();
				}					
			}
		}));
		params.requestParams[ier_constants.Param_ClassName] = this.template;
		params.requestParams[ier_constants.Param_Id] = this.id;
		params.requestParams[ier_constants.Param_ParentFolderId] = this.parent ? this.parent.id : null;
		params.requestParams[ier_constants.Param_ParentClassName] = this.parent ? this.parent.template : null;

		Request.postPluginService(ier_constants.ApplicationPlugin, "ierRefreshService", ier_constants.PostEncoding, params);
	
		this.logExit("refreshIERStates");
	},
	
	/**
	 * There's a bug in refresh from ICN where the onChange isn't passed as an array and is ignored.
	 */
	refresh: function() {
		this.logEntry("refresh");
		
		this.inherited(arguments);
		this.onChange([this]);
		
		this.logExit("refresh");
	}
});

// sub-class registry
var entityObjectClasses = {};
_BaseEntityObject.registerClass = function(name, ctor){
	entityObjectClasses[name] = ctor;
};

/**
 * Provide a factory method for creating model items specific to IER.
 * 
 * Constructs an IER base content item given a JSON representation of the item.
 * 
 * @param itemJSON
 *            The JSON representation of the content item.
 * @param repository
 *            An instance of {@link ecm.model.Repository} for the item.
 * @param resultSet
 *            An instance of {@link ecm.model.ResultSet} if this item is from a result set.
 * @param parent
 *            An instance of {@link ecm.model.ContentItem} for the parent of this item (if known).
 */
_BaseEntityObject.createFromJSON = function(itemJSON, repository, resultSet, parent) {
	var properties = itemJSON;
	var className = properties.template;
	var entityType = itemJSON.entityType ? itemJSON.entityType : null;
	var mimeType = itemJSON.CEMimeType ? itemJSON.CEMimeType : null;
	var item = null;

	if(entityType == ier_constants.EntityType_FilePlan || className == ier_constants.ClassName_FilePlan)
		item = new entityObjectClasses.FilePlan(itemJSON);
	else if(entityType == ier_constants.EntityType_RecordCategory || className == ier_constants.ClassName_RecordCategory)
		item = new entityObjectClasses.RecordCategory(itemJSON);
	else if(entityType == ier_constants.EntityType_ElectronicRecordFolder || className == ier_constants.ClassName_ElectronicRecordFolder)
		item = new entityObjectClasses.RecordFolder(itemJSON);
	else if(entityType == ier_constants.EntityType_HybridRecordFolder || className == ier_constants.ClassName_HybridRecordFolder)
		item = new entityObjectClasses.RecordFolder(itemJSON);
	else if(entityType == ier_constants.EntityType_PhysicalRecordFolder || className == ier_constants.ClassName_PhysicalRecordFolder)
		item = new entityObjectClasses.RecordFolder(itemJSON);
	else if(entityType == ier_constants.EntityType_PhysicalContainer || className == ier_constants.ClassName_PhysicalContainer)
		item = new entityObjectClasses.RecordFolder(itemJSON);
	else if(entityType == ier_constants.EntityType_Volume || className == ier_constants.ClassName_Volume)
		item = new entityObjectClasses.RecordVolume(itemJSON);
	else if(entityType == ier_constants.EntityType_ElectronicRecord || entityType == ier_constants.EntityType_PhysicalRecord || entityType == ier_constants.EntityType_EmailRecord 
			|| className == ier_constants.ClassName_ElectronicRecord || className == ier_constants.ClassName_PhysicalRecord || className == ier_constants.ClassName_EmailRecord ||
			mimeType == ier_constants.Mimetype_ElectronicRecord || mimeType == ier_constants.Mimetype_EmailRecord || mimeType == ier_constants.Mimetype_PhysicalRecord)
		item = new entityObjectClasses.Record(itemJSON);
	else if(entityType == ier_constants.EntityType_DispositionSchedule || className == ier_constants.ClassName_DispositionSchedule)
		item = new entityObjectClasses.DispositionSchedule(itemJSON);
	else if(entityType == ier_constants.EntityType_Location || className == ier_constants.ClassName_Location)
		item = new entityObjectClasses.Location(itemJSON);
	else if(entityType == ier_constants.EntityType_NamingPattern || className == ier_constants.ClassName_NamingPattern)
		item = new entityObjectClasses.NamingPattern(itemJSON);
	else if(entityType == ier_constants.EntityType_Hold || className == ier_constants.ClassName_Hold)
		item = new entityObjectClasses.Hold(itemJSON);
	else if(className == ier_constants.ClassName_RecordType || className == ier_constants.ClassName_DispositionTrigger 
			|| className == ier_constants.ClassName_TransferMapping || className == ier_constants.ClassName_DispositionAction)
		item = new _BaseEntityObject(itemJSON);
	else if(className == ier_constants.ClassName_WorkflowDefinition && entityType != null && entityType != "")
		item = new _BaseEntityObject(itemJSON);

	if(item){
		item.IERLoaded = true;
	}
	
	return item;
};

if(ecm_model_ContentItem.registerFactory){
	ecm_model_ContentItem.registerFactory(_BaseEntityObject);
}

return _BaseEntityObject;
});
