define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang, ecm_model_Request, ier_constants, ier_util){

/**
 * @name ier.model.RecordContainer
 * @class Represents a container capable of containing instances of a RecordFolder.  This usually applies to RecordCategory and RecordFolder objects.  This class
 * should not be instantiated.
 */
return dojo_declare("ier.model.RecordFolderContainerMixin", null, {
	/** @lends ier.model.RecordFolderContainerMixin.prototype */

	/**
	 * Creates a new record folder with the given parameters
	 * @param className - The symbolic name of the class of the record folder to create
	 * @param criterias - All the properties for the record folder
	 * @param permissions - All the permissions for the record folder
	 * @param dispScheduleId - The disposition schedule id that will be associated with the record folder for disposition
	 * @param callback - The callback to invoke with the newly created record folder
	 */
	addRecordFolder: function(className, criterias, permissions, dispSchedule, additionalLegacyScheduleParameters, callback){

		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){	
			if(callback){
				var recordFolder = ier_util.createBaseEntityItem(response.parent, this.repository, null, this.parent);
				this.updatePropertiesAndAttributes(recordFolder, false);
				this.repository.onItemsUpdated(this);
				callback(recordFolder);
			}
		}));
		params.requestParams[ier_constants.Param_ParentFolderId] = this.id;
		params.requestParams[ier_constants.Param_ClassName] = className;
		
		if(dispSchedule){
			params.requestParams[ier_constants.Param_DispositionScheduleId] = dispSchedule.id;
			dojo_lang.mixin(params.requestParams, additionalLegacyScheduleParameters);
		}
		
		var data = new Object();
		data[ier_constants.Param_Properties] = criterias;
		data[ier_constants.Param_Permissions] = permissions;
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateRecordFolder, ier_constants.PostEncoding, params);
	}
});});
