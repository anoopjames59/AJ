define([
	"dojo/_base/declare",
	"ier/model/_BaseEntityObject"
], function(dojo_declare, ier_model_BaseEntityObject){
	
/**
 * @name ier.model.Location
 * @class Represents a single Location object on the server.
 * @augments ecm.model._ModelObject
 */
var Location = dojo_declare("ier.model.Location", [ier_model_BaseEntityObject], {
	/** @lends ecm.model.Location.prototype */
	
	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
	}
});

ier_model_BaseEntityObject.registerClass("Location", Location);

return Location;
});
