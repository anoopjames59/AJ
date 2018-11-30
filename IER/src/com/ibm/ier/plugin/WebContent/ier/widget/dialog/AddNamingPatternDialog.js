define([
	"dojo/_base/declare",
	"ier/widget/dialog/NamingPatternDialog",
	"dojo/text!./templates/AddNamingPatternDialogContent.html",
	"dijit/layout/ContentPane", // template
	"idx/layout/TitlePane" // template
], function(declare, NamingPatternDialog, AddNamingPatternDialogContent_html){
	return declare(NamingPatternDialog, {contentString: AddNamingPatternDialogContent_html});
});
