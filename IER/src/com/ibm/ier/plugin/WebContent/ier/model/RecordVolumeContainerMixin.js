define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang, ecm_model_Request, ier_constants, ier_util){

/**
 * @name ier.model.RecordVolumeContainerMixin
 * @class Represents a container capable of containing instances of a RecordVolume.  This usually applies to RecordFolder objects.  This class should not be 
 * instantiated.
 */
return dojo_declare("ier.model.RecordVolumeContainerMixin", null, {
	/** @lends ier.model.RecordVolumeContainerMixin.prototype */

	/**
	 * Creates a new record volume with the given parameters
	 * @param className - The symbolic name of the class of the record volume to create
	 * @param criterias - All the properties for the record volume
	 * @param permissions - All the permissions for the record volume
	 * @param callback - The callback to invoke with the newly created record volume 
	 */
	addRecordVolume: function(className, criterias, permissions, callback){
		
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response)
		{	
			if(callback){
				var recordVolume = ier_util.createBaseEntityItem(response.parent, this.repository, null, this.parent);
				this.updatePropertiesAndAttributes(recordVolume, false);
				this.repository.onItemsUpdated(this);
				callback(recordVolume);
			}
		}));
		params.requestParams[ier_constants.Param_ParentFolderId] = this.id;
		params.requestParams[ier_constants.Param_ClassName] = className;
		
		var data = new Object();
		data[ier_constants.Param_Properties] = criterias;
		data[ier_constants.Param_Permissions] = permissions;
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateRecordVolume, ier_constants.PostEncoding, params);
	}
});});
