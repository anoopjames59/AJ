define([
	"dojo/_base/declare",
	"ier/widget/dialog/DispositionScheduleDialog",
	"dojo/text!./templates/AddDialogContent.html",
	"dijit/layout/ContentPane", // template
	"idx/layout/TitlePane" // template
], function(declare, DispositionScheduleDialog, AddDialogContent_html){
	return declare(DispositionScheduleDialog, {contentString: AddDialogContent_html});
});