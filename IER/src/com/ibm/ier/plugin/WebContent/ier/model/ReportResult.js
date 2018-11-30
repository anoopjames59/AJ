define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/LoggerMixin",
	"ecm/model/_ModelObject",
	"ier/constants",
	"ier/util/util"
], function(dojo_declare, dojo_lang, ecm_LoggerMixin, ecm_model_ModelObject, ier_constants, ier_util){

/**
 * @name ier.model.ReportResults
 * @class a model for a report results
 */
return dojo_declare("ier.model.ReportResult",[ecm_model_ModelObject], {

	/** @lends ier.model.ReportResults.prototype */
	
	createDate : null,
	creator : null,
	
	
	isCurrentUserCreator: function() {
		
	},
	
	deleteResults: function() {
		
	}
});});