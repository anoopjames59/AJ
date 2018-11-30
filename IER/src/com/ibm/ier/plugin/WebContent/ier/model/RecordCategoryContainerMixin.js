define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang, ecm_model_Request, ier_constants, ier_util){

/**
 * @name ier.model.RecordContainerMixin
 * @class Represents a container capable of containing instances of a RecordCategory.  This usally applies to FilePlan and RecordCategory objects.  This class should not be 
 * instantiated.
 */
return dojo_declare("ier.model.RecordCategoryContainerMixin", null, {
	/** @lends ier.model.RecordCategoryContainerMixin.prototype */

	/**
	 * Creates a new record category with the given parameters
	 * @param className - The symbolic name of the class of the record category to create
	 * @param criterias - All the properties for the record category
	 * @param permissions - All the permissions for the record category
	 * @param dispScheduleId - The disposition schedule id that will be associated with the record category for disposition
	 * @param additionalLegacyScheduleParameters - Any additional legacy schedule parameters
	 * @param callback - The callback to invoke with the newly created record category 
	 */
	addRecordCategory: function(className, criterias, permissions, dispSchedule, additionalLegacyScheduleParameters, callback){
		
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response)
		{	
			if(callback){
				var recordCategory = ier_util.createBaseEntityItem(response.parent, this.repository, null, this.parent);
				this.updatePropertiesAndAttributes(recordCategory, false);
				this.repository.onItemsUpdated(this);
				callback(recordCategory);
			}
		}));
		
		params.requestParams[ier_constants.Param_ParentFolderId] = this.id;
		params.requestParams[ier_constants.Param_ClassName] = className;
		
		if(dispSchedule){
			if(dispSchedule instanceof ier.model.DefensibleDisposalSchedule){
				params.requestParams[ier_constants.Param_RetentionTriggerPropertyName] = dispSchedule.getRMRetentionTriggerPropertyName();
				params.requestParams[ier_constants.Param_RetentionPeriodYears] = dispSchedule.getRMRetentionPeriod("years");
				params.requestParams[ier_constants.Param_RetentionPeriodMonths] = dispSchedule.getRMRetentionPeriod("months");
				params.requestParams[ier_constants.Param_RetentionPeriodDays] = dispSchedule.getRMRetentionPeriod("days");
			}
			else {
				params.requestParams[ier_constants.Param_DispositionScheduleId] = dispSchedule.id;
				dojo_lang.mixin(params.requestParams, additionalLegacyScheduleParameters);
			}
		}
		
		var data = new Object();
		data[ier_constants.Param_Properties] = criterias;
		data[ier_constants.Param_Permissions] = permissions;
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CreateRecordCategory, ier_constants.PostEncoding, params);
	}
});});
