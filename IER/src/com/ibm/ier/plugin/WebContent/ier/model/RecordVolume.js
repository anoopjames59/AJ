define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/model/_BaseEntityObject",
	"ier/model/RecordContainerMixin",
	"ier/model/RMContainerMixin"
], function(dojo_declare, ier_constants, ier_model_BaseEntityObject, ier_model_RecordContainerMixin, ier_model_RMContainerMixin){

/**
 * @name ier.model.RecordVolume
 * @class Represents a RecordVolume container in IER
 * @augments ier.model._BaseEntityObject, ier.model.RecordCatainerMixin
 */
var RecordVolume = dojo_declare("ier.model.RecordVolume", [ier_model_BaseEntityObject, ier_model_RecordContainerMixin, ier_model_RMContainerMixin], {
	/** @lends ecm.model.RecordVolume.prototype */

	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
	}
});

ier_model_BaseEntityObject.registerClass("RecordVolume", RecordVolume);

return RecordVolume;
});
