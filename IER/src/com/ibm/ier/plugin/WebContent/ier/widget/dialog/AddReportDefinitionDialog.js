define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ier/constants",
	"ier/messages",
	"ier/widget/dialog/IERBaseDialog",
	"ier/widget/dialog/ReportDefinitionDialog",
	"dijit/layout/ContentPane", // in content
	"idx/layout/TitlePane", // in content
	"ier/widget/panes/EntityItemPropertiesPane", // in content
	"ier/widget/panes/EntityItemSecurityPane", // in content
	"ier/widget/panes/ReportDefinitionQueryPane", // in template
	"ier/widget/panes/ReportDefinitionPropertiesPane" // in template
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_dialog_IERBaseDialog,
		ier_dialog_ReportDefinitionDialog){
	return dojo_declare("ier.widget.dialog.AddReportDefinitionDialog", [ier_dialog_ReportDefinitionDialog], {	
		/** @lends ier.widget.dialog.AddReportDefinitionDialog.prototype */
		ier_messages: ier_messages,

		showSecurity: true,

		postCreate: function(){
			this.inherited(arguments);
			
			this.showSecurity = ecm.model.desktop.showSecurity;
		}
	});
});