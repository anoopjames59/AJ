define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/Request",
	"ier/constants",
	"ier/util/util",
	"ier/model/_BaseEntityObject"
], function(dojo_declare, dojo_lang, ecm_model_Request, ier_constants, ier_util, ier_model_BaseEntityObject){
	
/**
 * @name ier.model.Record
 * @class Represents a Record
 * @augments ier.model._BaseEntityObject
 */
var Record = dojo_declare("ier.model.Record", [ier_model_BaseEntityObject], {
	/** @lends ecm.model.Record.prototype */
	
	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {
		
	},
	
	/**
	 * Moves a record from the current location to the new target location
	 * @param targetLocation - new target location for the record
	 * @param reasonForMove - a reason for moving the record
	 * @param onComplete
	 */
	move: function(targetLocation, reasonForMove, onComplete){
		var repository = this.repository;
		
		var serviceParams = ier_util.getDefaultParams(repository, dojo_lang.hitch(this, function(response) {
			// record is now in destination, so refresh dest
			repository.retrieveItem(targetLocation, dojo_lang.hitch(this, function(itemRetrieved){
				if (itemRetrieved) {
					repository.onChange(itemRetrieved);
					itemRetrieved.refresh();
				}
				this.parent.refresh();
				
				if(onComplete){
					onComplete();
				}
			}));			
		}));
		serviceParams.requestParams[ier_constants.Param_RecordId] = this.id;
		serviceParams.requestParams[ier_constants.Param_SourceContainer] = this.parent.id;
		serviceParams.requestParams[ier_constants.Param_DestinationContainer] = targetLocation;
		serviceParams.requestParams[ier_constants.Param_ReasonForMove] = reasonForMove;
		// Call the relocate service
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_MoveRecord, ier_constants.PostEncoding, serviceParams);
	}
});

ier_model_BaseEntityObject.registerClass("Record", Record);

return Record;
});

