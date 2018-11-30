define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/model/_BaseEntityObject",
	"ier/messages"
], function(dojo_declare, ier_constants, ier_model_BaseEntityObject, ier_messages){

/**
 * @name ier.model.DefensibleDisposalSchedule
 * @class Represents a defensible disposal schedule.  For the new DD schedule, it will only contain values for RMRetentionPeriod
 * and RMRetentionTriggerPropertyName only.
 * @augments ecm.model._ModelObject
 */
var DefensibleDisposalSchedule = dojo_declare("ier.model.DefensibleDisposalSchedule", [ier_model_BaseEntityObject], {
	/** @lends ier.model.DefensibleDisposalSchedule.prototype */
	
	/**
	 * The symbolic name of a property on the record class that will trigger retention
	 */
	retentionTriggerPropertyName: null,
	
	/**
	 * The retention period specified in Years-Months-Days
	 */
	retentionPeriod: null,
	
	constructor: function(arguments){
		if(!arguments.id && !arguments.name){
			this.id = "defensibleDisposalSchedule_" + new Date().getTime();
			this.name = this.retentionTriggerPropertyName;
		}
			
	},
	
	/**
	 * Returns the retention period if this is a DD schedule. Otherwise it will return null.
	 * @param type - pass in "years", "months, "days", "display", or null to retrieve the retention period in those periods.  If it's null, the entire string is
	 * returned.  If it is "display", a more readable form of the retention period is returned
	 * 
	 * @returns
	 */
	getRMRetentionPeriod: function(type){
		if(this.retentionPeriod){
			var splitDates = this.retentionPeriod.split("-");
			if(splitDates.length == 3){
				if(type == "years"){
					return parseInt(splitDates[0], 10);
				} else if(type == "months"){
					return parseInt(splitDates[1], 10);
				} else if(type == "days"){
					return parseInt(splitDates[2], 10);
				} else if(type == "display"){
					return parseInt(splitDates[0], 10) + " " + ier_messages.dispositionPane_years + 
					" - " + parseInt(splitDates[1], 10) + " " + ier_messages.dispositionPane_months  +
					" - " + parseInt(splitDates[2], 10) + " " + ier_messages.dispositionPane_days;
				}
				else{
					return this.retentionPeriod;
				}
			}
		}
		return null;
	},
	
	/**
	 * Returns the trigger property name if this is a DD schedule.  Otherwise it will return null.
	 * @returns
	 */
	getRMRetentionTriggerPropertyName: function(){
		return this.retentionTriggerPropertyName;
	}
});

ier_model_BaseEntityObject.registerClass("DefensibleDisposalSchedule", DefensibleDisposalSchedule);

return DefensibleDisposalSchedule;
});
