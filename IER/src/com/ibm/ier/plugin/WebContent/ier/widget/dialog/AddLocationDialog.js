define([
	"dojo/_base/declare",
	"ier/widget/dialog/LocationDialog",
	"dojo/text!./templates/AddDialogContent.html",
	"dijit/layout/ContentPane", // template
	"idx/layout/TitlePane" // template
], function(declare, LocationDialog, AddDialogContent_html){
	return declare(LocationDialog, {contentString: AddDialogContent_html});
});
