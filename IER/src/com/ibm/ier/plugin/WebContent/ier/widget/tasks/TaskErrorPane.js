define ([
     "dojo/_base/declare",
     "dojo/dom-style",
     "dojo/dom-construct",
     "dijit/layout/ContentPane",
     "dijit/_TemplatedMixin",
 	 "dijit/_WidgetsInTemplateMixin",
 	 "ecm/LoggerMixin",
     "ier/constants",
     "ier/messages",
     "dojo/text!./templates/TaskPane.html"
], function(dojo_declare, dojo_domStyle, dojo_construct, dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin,
		ier_constants, ier_messages, templateString){
	
return dojo_declare("ier.widget.tasks.TaskErrorPane", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_LoggerMixin], {
	templateString: templateString,
	widgetsInTemplate: true,
	
	createRendering: function(item) {
		this.logEntry("createRendering");
		this.item = item;
		
		this.errorsContainer = dojo_construct.create("div");
		
		if(item.errors)
		{
			for (var i = item.errors.length; i--;) {
				var error = item.errors[i];
				if(error && error.message){
					dojo_construct.create("pre", {
						"style": "", 
						"class": "taskError",
						"innerHTML": error.message
					}, this.errorsContainer);
				}
			}
			
			dojo_construct.place(this.errorsContainer, this.container, "only");
			
		}
		this.logExit("createRendering");
	}
});});