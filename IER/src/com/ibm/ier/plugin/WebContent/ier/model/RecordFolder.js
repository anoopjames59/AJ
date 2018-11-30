define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/model/_BaseEntityObject",
	"ier/model/RecordContainerMixin",
	"ier/model/RecordFolderContainerMixin",
	"ier/model/RecordVolumeContainerMixin",
	"ier/model/RMContainerMixin"
], function(dojo_declare, ier_constants, ier_model_BaseEntityObject, ier_model_RecordContainerMixin, ier_model_RecordFolderContainerMixin, 
		ier_model_RecordVolumeContainerMixin, ier_model_RMContainerMixin){

/**
 * @name ier.model.RecordFolder
 * @class Represents a RecordFolder container in IER
 * @augments ier.model._BaseEntityObject, ier.model.RecordContainerMixin, ier.model.RecordFolderContainerMixin, ier.model.RecordVolumeContainerMixin
 */
var RecordFolder = dojo_declare("ier.model.RecordFolder", [ier_model_BaseEntityObject, ier_model_RecordContainerMixin, ier_model_RecordFolderContainerMixin, 
                                                           ier_model_RecordVolumeContainerMixin, ier_model_RMContainerMixin], {
	/** @lends ecm.model.RecordFolder.prototype */

	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
	}
});

ier_model_BaseEntityObject.registerClass("RecordFolder", RecordFolder);

return RecordFolder;
});
