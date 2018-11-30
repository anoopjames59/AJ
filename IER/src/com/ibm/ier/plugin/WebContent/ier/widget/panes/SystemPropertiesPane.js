define ([
         "dojo/_base/declare",
         "dojo/dom-style",
         "ier/constants",
         "ier/messages",
         "ier/util/property",
         "ier/widget/dialog/IERBaseDialogPane",
         "dojo/text!./templates/SystemPropertiesPane.html",
         "idx/grid/PropertyGrid" // in content
], function(dojo_declare, domStyle, ier_constants, ier_messages, ier_util_property, ier_widget_dialog_IERBaseDialogPane, templateString, PropertyGrid){
	return dojo_declare("ier.widget.panes.SystemPropertiesPane", [ier_widget_dialog_IERBaseDialogPane], {
		templateString: templateString,
		
		createRendering: function(item, attributes) {
			this.logEntry("createRendering");
			this.item = item;
			
			var data = {};
			var resources = {};
			var propertiesArray = [];
			var contentClass = item.getContentClass();
			
			for ( var i = 0; i < attributes.length; i++) {
				var name = attributes[i].id;
				if (this.item.isSystemProperty(name)) {
					var value = this.item.getDisplayValue(name);
					var label = this.item.getAttrLabel(name);
					data[name] = value;
					if (label && label.replace) {
						label = label.replace(/&/g, '&amp;').replace(/</g, '&lt;');
					}
					resources[name + "Label"] = label || name;
					propertiesArray.push(name);
				}
			}
			
			if(propertiesArray.length > 0) {
				var systemProperties = propertiesArray.join(",");
				this._systemPropGrid = new PropertyGrid({
					data: data,
					properties: systemProperties,
					labelKeySuffix: "Label",
					resources: resources
				});
				this._systemPropGrid.placeAt(this._gridContainer, "first");
				domStyle.set(this._systemPropGrid.domNode, "height", "100%");
				this._systemPropGrid.startup();
			}
			
			this.logExit("createRendering");
		}
	});
});