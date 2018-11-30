define([
	"dojo/_base/declare",
	"ier/widget/dialog/ActionDialog",
	"dojo/text!./templates/AddDialogContent.html",
	"dijit/layout/ContentPane", // template
	"idx/layout/TitlePane" // template
], function(declare, ActionDialog, AddDialogContent_html){
	return declare(ActionDialog, {contentString: AddDialogContent_html});
});