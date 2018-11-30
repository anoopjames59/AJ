define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang, ecm_model_Request, ier_constants, ier_util){

/**
 * @name ier.model.RecordContainerMixin
 * @class Represents a container in RM like Record Categories, Record Folders, Record Volumes.
 */
return dojo_declare("ier.model.RMContainerMixin", null, {
	/** @lends ier.model.RMContainerMixin.prototype */

	/**
	 * Whether this record container is a defensible disposal container
	 */
	isDefensibleDisposal: function() {
		var triggerPropertyName = this.attributes[ier_constants.Property_RMRetentionTriggerPropertyName];
		var retentionPeriod = this.attributes[ier_constants.Property_RMRetentionPeriod];
		return (triggerPropertyName != null && triggerPropertyName.length > 0 && retentionPeriod != null && retentionPeriod.length > 0 );
	}
});});
