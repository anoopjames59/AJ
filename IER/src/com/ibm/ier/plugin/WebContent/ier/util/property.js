define([
	"dojo/_base/declare",
	"ecm/LoggerMixin",
	"ier/constants"
], function(dojo_declare, ecm_LoggerMixin, ier_constants){

var _PropertyUtil = dojo_declare("ier.util._PropertyUtil", [ecm_LoggerMixin], {
	
	DispositionGroupProperties: [ ier_constants.Property_DispositionSchedule, ier_constants.Property_CutoffDate, ier_constants.Property_CurrentPhaseExecutionDate, 
	                              ier_constants.Property_CurrentPhaseDecisionDate, ier_constants.Property_CurrentPhaseAction, ier_constants.Property_CurrentPhaseReviewDecision, 
	                              ier_constants.Property_LastSweepDate],

    VitalRecordGroupProperties: [ ier_constants.Property_VitalRecordDeclareDate, ier_constants.Property_VitalRecordDescription, ier_constants.Property_VitalRecordDisposalTrigger, 
	                        ier_constants.Property_VitalRecordNextReviewDate, ier_constants.Property_VitalRecordReviewAction, ier_constants.Property_IsVitalRecord],
	                                       
    PhysicalGroupProperties: [ ier_constants.Property_Location, ier_constants.Property_HomeLocation, ier_constants.Property_ChargeOutTo, 
                               ier_constants.Property_ChargeOutStatus],
                               
    BasicScheduleProperties: [ ier_constants.Property_RMRetentionTriggerPropertyName, ier_constants.Property_RMRetentionPeriod],
                                       
	EntityItemNotDisplayedProperties: [ ier_constants.Property_IsDeleted],
	
	//a map of all the RM System Properties.  A map is used for faster lookup.
	RMSystemProperties: { "AccessionTo": true,
	                      "AssociatedCutOffDisposalTriggers": true,
	                      "AssociatedFolders": true,
	                      "AssociatedHybridRecordFolderHomeLocations": true,
	                      "AssociatedMarkerHomeLocations": true,
	                      "AssociatedPhase": true,
	                      "AssociatedPhases": true,
	                      "AssociatedPhyContainerHomeLocations": true,
	                      "AssociatedPhyRecordFolderHomeLocations": true,
	                      "AssociatedRCHomeLocations": true,
	                      "AssociatedRCLocations": true,
	                      "AssociatedRCVitalDisposalTriggers": true,
	                      "AssociatedRCVitalReview": true,
	                      "AssociatedRFLocations": true,
	                      "AssociatedRFVitalDisposalTriggers": true,
	                      "AssociatedRILocations": true,
	                      "AssociatedRIVitalDisposaltriggers": true,
	                      "AssociatedRIVitalReview": true,
	                      "AssociatedRecordCategoryActions": true,
	                      "AssociatedRecordFolderActions": true,
	                      "AssociatedRecordInfoActions": true,
	                      "AssociatedRecordPtr": true,
	                      "AssociatedRecords": true,
	                      "AssociatedVolumeActions": true,
	                      "AssociatedVolumeHomeLocations": true,
	                      "AssociatedVolumeLocations": true,
	                      "AssociatedVolumeVitalDisposalTriggers": true,
	                      "AssociatedVolumeVitalDisposalTriggers": true,
	                      "AssociatedVolumeVitalReview": true,
	                      "AssocitedRFVitalReview": true,
	                      "AuthorisingStatute" : true,
	                      "CanDeclare": true,
	                      "ChargeOutStatus": true,
	                      "ChargeOutTo": true,
	                      "ClosedBy": true,
	                      "ConditionXML": true,
	                      "CurrentActionType": true,
	                      "CurrentPhaseAction": true,
	                      "CurrentPhaseDecisionDate": true,
	                      "CurrentPhaseExecutionDate": true,
	                      "CurrentPhaseExecutionStatus": true,
	                      "CurrentPhaseExportDestination": true,
	                      "CurrentPhaseExportFormat": true,
	                      "CurrentPhaseID": true,
	                      "CurrentPhaseReviewComments": true,
	                      "CurrentPhaseReviewDecision": true,
	                      "CutoffDate": true,
	                      "CutoffInheritedFrom": true,
	                      "DateClosed": true,
	                      "DateDeclared": true,
	                      "DateLastDeclaration": true,
	                      "DateOfLastReview": true,
	                      "DisposalAuthorizedBy": true,
	                      "DisposalDate": true,
	                      "DisposalInstruction": true,
	                      "DisposalPhases": true,
	                      "DisposalScheduleAllocationDate": true,
	                      "DisposalScheduleInheritedFrom": true,
	                      "GuideID": true,
	                      "Holds": true,
	                      "Inactive": true,
	                      "IntelligentRetentionNumber": true,
	                      "IsDeleted": true,
	                      "IsDynamicHold": true,
	                      "IsTriggerChanged": true,
	                      "IsFinalPhaseCompleted": true,
	                      "LastHoldSweepDate": true,
	                      "LastPatternIndex": true,
	                      "LastRetrievalofRecord": true,
	                      "LastReviewedBy": true,
	                      "LastRunDate": true,
	                      "LastSweepDate": true,
	                      "Level": true,
	                      "LinkedTo": true,
	                      "OOTBReportID": true,
	                      "OnHold": true,
	                      "ParentGUID": true,
	                      "PatternPtr": true,
	                      "PhaseDecisionDate": true,
	                      "PhaseExecutionDate": true,
	                      "PhasePtr": true,
	                      "PreventRMEntityDeletion": true,
	                      "RMExternallyManagedBy": true,
	                      "ReOpenedBy": true,
	                      "ReOpenedDate": true,
	                      "ReasonForClose": true,
	                      "ReasonForDelete": true,
	                      "ReasonForInactivate": true,
	                      "ReasonForOutcomeOfLastReview": true,
	                      "ReasonForReclassification": true,
	                      "RecalculatePhaseRetention": true,
	                      "ReceiptStatus": true,
	                      "RecordInformation": true,
	                      "RecordedDocuments": true,
	                      "ReviewComments": true,
	                      "ReviewDate": true,
	                      "ReviewDecision": true,
	                      "ReviewerDetails": true,
	                      "SupercededDate": true,
	                      "SweepAuditXML": true,
	                      "SweepState": true,
	                      "VitalRecordDeclareDate": true,
	                      "VitalRecordNextReviewDate": true,
	                      "VitalRecordReviewComments": true,
	                      "VitalRecordReviewDate": true,
	                      "VitalSweepDate": true,
	                      "VitalWorkflowStatus": true
	},

	InternalEventTrigger: [ier_constants.Property_DispositionTriggerName, ier_constants.Property_RMEntityDescription, ier_constants.Property_Aggregation],
	
	ExternalEventTrigger: [ier_constants.Property_DispositionTriggerName, ier_constants.Property_RMEntityDescription, ier_constants.Property_ExternalEventOccurenceDate],
	
	RecurringEventTrigger: [ier_constants.Property_DispositionTriggerName, ier_constants.Property_RMEntityDescription, ier_constants.Property_DateTime, ier_constants.Property_Frequency],

	PredefinedDateEventTrigger: [ier_constants.Property_DispositionTriggerName, ier_constants.Property_RMEntityDescription, ier_constants.Property_DateTime],

	/**
	 * Determines whether a property is a physical group property
	 */
	isPhysicalGroupProperty: function(propertyName){
		for(var i in this.PhysicalGroupProperties){
			var property = this.PhysicalGroupProperties[i];
			if(property == propertyName)
				return true;
		}
		return false;
	},
   	
   	/**
	 * Determines whether a property is a vital record group property
	 */
	isVitalRecordGroupProperty: function(propertyName){
		for(var i in this.VitalRecordGroupProperties){
			var property = this.VitalRecordGroupProperties[i];
			if(property == propertyName)
				return true;
		}
		return false;
	},
   	
	isBasicScheduleGroupProperty: function(propertyName){
		for(var i in this.BasicScheduleProperties){
			var property = this.BasicScheduleProperties[i];
			if(property == propertyName)
				return true;
		}
		return false;
	},
	
	/**
	 * Determines whether a property is a Record Category Disposition property
	 */
	isDispositionGroupProperty: function(propertyName){
		for(var i in this.DispositionGroupProperties){
			var property = this.DispositionGroupProperties[i];
			if(property == propertyName)
				return true;
		}
		return false;
	},
	
	/**
	 * Returns the list of Record Category Disposition properties
	 */
	getDispositionGroupProperties:function (){
		return this.DispositionGroupProperties;
	},
	
	/**
	 * Determines whether a property should not be displayed in the item property list
	 */
	isEntityItemNotDisplayedProperty: function(propertyName){
		for(var i in this.EntityItemNotDisplayedProperties){
			var property = this.EntityItemNotDisplayedProperties[i];
			if(property == propertyName)
				return true;
		}
		return false;
	},
	
	/**
	 * Determines whether this is an RM System property.  RM System properties are typically used within the RM system and should not be edited by outside users
	 * @param propertyName
	 */
	isRMSystemProperty: function(propertyName){
		return this.RMSystemProperties[propertyName] == true;
	},

	/**
   	 * Determines whether a property is a Recurring Event Trigger
   	 */
   	isRecurringEventProperty: function(propertyName){
   		for(var i in this.RecurringEventTrigger){
   			var property = this.RecurringEventTrigger[i];
   			if(property == propertyName)
   				return true;
   		}
   		return false;
   	},
   	
   	/**
   	 * Determines whether a property is an External Event Trigger
   	 */
   	isExternalEventProperty: function(propertyName){
   		for(var i in this.ExternalEventTrigger){
   			var property = this.ExternalEventTrigger[i];
   			if(property == propertyName)
   				return true;
   		}
   		return false;
   	},
   	
   	/**
   	 * Determines whether a property is an Internal Event Trigger
   	 */
   	isInternalEventProperty: function(propertyName){
   		for(var i in this.InternalEventTrigger){
   			var property = this.InternalEventTrigger[i];
   			if(property == propertyName)
   				return true;
   		}
   		return false;
   	},
   	
   	/**
   	 * Determines whether a property is an Internal Event Trigger
   	 */
   	isPredefinedDateEventProperty: function(propertyName){
   		for(var i in this.PredefinedDateEventTrigger){
   			var property = this.PredefinedDateEventTrigger[i];
   			if(property == propertyName)
   				return true;
   		}
   		return false;
   	},

   	hasDispositionGroupProperty: function(item){
   		switch(item && item.entityType){
   		case ier_constants.EntityType_RecordCategory:
   		case ier_constants.EntityType_RecordFolder:
   		case ier_constants.EntityType_Volume:
   		case ier_constants.EntityType_ElectronicRecordFolder:
   		case ier_constants.EntityType_HybridRecordFolder:
   		case ier_constants.EntityType_PhysicalContainer:
   		case ier_constants.EntityType_PhysicalRecordFolder:
   		case ier_constants.EntityType_Record:
   		case ier_constants.EntityType_ElectronicRecord:
   		case ier_constants.EntityType_EmailRecord:
   		case ier_constants.EntityType_PhysicalRecord:
   		case ier_constants.EntityType_PDFRecord:
   			return true;
   		default:
   			return false;
   		}
   	},
   	
   	/**
   	 * Set the provided attribute definitions from the propertyJSON.  This is used to reset properties that have been saved.
   	 * @param attributeDefs
   	 * @param propertyJSON
   	 */
   	setPropertiesFromPropertyJSON: function(attributeDefs, propertyJSON){
   		for ( var i in attributeDefs) {
			var attrDef = attributeDefs[i];
			var property = this._getPropertyFromPropertyJSON(propertyJSON, attrDef.id);
			if(attrDef && property)
				attrDef.defaultValue = property.value;
   		}
   	},
   	
   	_getPropertyFromPropertyJSON: function(propertyJSON, attributeName){
   		for(var i in propertyJSON){
   			var property = propertyJSON[i];
   			if(property.name == attributeName){
   				return property;
   			}
   		}
   	}

});

var propertyUtil = new _PropertyUtil();

return propertyUtil;
});
