define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang, ecm_model_Request, ier_constants, ier_util){

/**
 * @name ier.model.RecordContainer
 * @class Represents a container capable of containing instances of a Record.  This usally applies to RecordCategory, RecordFolder, and RecordVolume objects.
 * This class should not be instantiated.
 */
return dojo_declare("ier.model.RecordContainerMixin", null, {
	/** @lends ier.model.RecordContainerMixin.prototype */

	/**
	 * Close a  record category with the given parameters
	 * @param reasonForClose - the reason why the container is being closed.  Must be non-null non-empty string
	 * @param callback - The callback to invoke with the newly created record category 
	 */
	close: function(reasonForClose, callback, shouldUpdateParent){
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
			this.refreshIERStates(dojo_lang.hitch(this, function(){
				this.refresh();
				if(this.parent && this.parent.unloadFolderContents){
					this.parent.unloadFolderContents();
				}
				if(callback) {
					callback(this);
				}
			}));
		}));
		
		params.requestParams[ier_constants.Param_ReasonForClose] = reasonForClose;
		params.requestParams[ier_constants.Param_ContainerId] = this.id;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_CloseRecordContainer, ier_constants.PostEncoding, params);
	},

	/**
	 * Reopen a  record category with the given parameters
	 * @param callback - The callback to invoke with the newly created record category 
	 */
	reopen: function(callback, shouldUpdateParent){
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
			this.refreshIERStates(dojo_lang.hitch(this, function(){
				this.refresh();
				if(this.parent && this.parent.unloadFolderContents){
					this.parent.unloadFolderContents();
				}
				if(callback) {
					callback(this);
				}
			}));
		}));
		params.requestParams[ier_constants.Param_ContainerId] = this.id;

		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_ReopenRecordContainer, ier_constants.PostEncoding, params);
	},
	
	updateRecordContainerProperties: function(className, criteria, permissions, dispSchedule, additionalLegacyScheduleParameters, doRefresh, callback) {
		this.logEntry("_updateEntityItem");
		
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){
			this.permissions = null;
			this.retrieveAttributes(dojo_lang.hitch(this, function(){
				if(callback) {
					this.refresh();
					callback(this);
				}
			}), false, true);
		}));
		params.requestParams[ier_constants.Param_EntityId] = this.id;
		params.requestParams[ier_constants.Param_ClassName] = className;
		
		if(dispSchedule){
			if(dispSchedule instanceof ier.model.DefensibleDisposalSchedule && this instanceof ier.model.RecordCategory){
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
		//add the legacy schedule parameters in, in case it's trying to clear the legacy schedule with a certain propagation level
		else
			dojo_lang.mixin(params.requestParams, additionalLegacyScheduleParameters);

		
		var data = new Object();
		data[ier_constants.Param_Properties] = criteria;
		data[ier_constants.Param_Permissions] = permissions;
		params["requestBody"] = data;
		
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_EditRecordContainer, ier_constants.PostEncoding, params);
		
		this.logExit("_updateEntityItem");
	}
});});
