define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/model/_BaseEntityObject"
], function(dojo_declare, ier_constants, ier_model_BaseEntityObject){

/**
 * @name ier.model.DispositionSchedule
 * @class Represents a legacy disposition schedule
 * @augments ecm.model._ModelObject
 */
var DispositionSchedule = dojo_declare("ier.model.DispositionSchedule", [ier_model_BaseEntityObject], {
	/** @lends ier.model.LegacyDispositionSchedule.prototype */
	
	/**
	 * Returns the disposition authority property
	 * 
	 * @returns
	 */
	getDispositionAuthority: function(){
		return this.attributes[ier_constants.Property_DispositonAuthority];
	}
});

ier_model_BaseEntityObject.registerClass("DispositionSchedule", DispositionSchedule);

return DispositionSchedule;
});
