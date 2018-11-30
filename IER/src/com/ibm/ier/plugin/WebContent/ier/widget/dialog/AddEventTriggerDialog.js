define([
	"dojo/_base/declare",
	"ier/widget/dialog/EventTriggerDialog",
	"dojo/text!./templates/AddDialogContent.html",
	"dijit/layout/ContentPane", // template
	"idx/layout/TitlePane" // template
], function(declare, EventTriggerDialog, AddDialogContent_html){
	return declare(EventTriggerDialog, {contentString: AddDialogContent_html});
});