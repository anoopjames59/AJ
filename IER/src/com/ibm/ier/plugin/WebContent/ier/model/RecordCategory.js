define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/model/_BaseEntityObject",
	"ier/model/RecordContainerMixin",
	"ier/model/RecordCategoryContainerMixin",
	"ier/model/RecordFolderContainerMixin",
	"ier/model/RMContainerMixin",
	"ier/model/DefensibleDisposalSchedule"
], function(dojo_declare, ier_constants, ier_model_BaseEntityObject, ier_model_RecordContainerMixin, ier_model_RecordCategoryContainerMixin, 
		ier_model_RecordFolderContainerMixin, ier_model_RMContainerMixin, DefensibleDisposalSchedule){

/**
 * @name ier.model.RecordContainer
 * @class Represents a RecordCategory in IER
 * @augments ier.model.RecordContainerMixin, ier.model.RecordFolderContainerMixin, ier.model._BaseEntityObject
 */
var RecordCategory = dojo_declare("ier.model.RecordCategory", [ier_model_BaseEntityObject, ier_model_RecordContainerMixin, ier_model_RecordCategoryContainerMixin, 
                                                               ier_model_RecordFolderContainerMixin, ier_model_RMContainerMixin], {
	/** @lends ier.model.RecordCategory.prototype */

	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {	
	},
	
	getDefensibleDisposalSchedule: function(){
		if(this.isDefensibleDisposal()){
			if(!this.defensibleDisposalSchedule){
				this.defensibleDisposalSchedule = new DefensibleDisposalSchedule({
					"retentionTriggerPropertyName": this.attributes[ier_constants.Property_RMRetentionTriggerPropertyName],
					"retentionPeriod": this.attributes[ier_constants.Property_RMRetentionPeriod]
				});
			}
			return this.defensibleDisposalSchedule;
		}
	},
	
	refresh: function(){
		this.defensibleDisposalSchedule = null;
		this.inherited(arguments);
	}
});

ier_model_BaseEntityObject.registerClass("RecordCategory", RecordCategory);

return RecordCategory;
});
