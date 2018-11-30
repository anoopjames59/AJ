define([
	"dojo/_base/declare",
	"dojo/dom-style",
	"ier/constants",
	"ier/messages",
	"ier/util/property",
	"ier/widget/dialog/IERBaseDialogPane",
	"ecm/MessagesMixin",
	"idx/grid/PropertyGrid",
	"dojo/text!./templates/EntityItemDetailPane.html",
	"ecm/widget/TitlePane" // in content
	], function(declare, domStyle, ierConstants, messages, ier_util_property, _BaseDialogPane, _MessagesMixin, PropertyGrid, templateStr){
	return declare("ier.widget.panes.EntityItemDetailPane", [_BaseDialogPane, _MessagesMixin], {
		templateString: templateStr,
		
		createRendering: function(item, attributes){
			this.logEntry("createRendering");
			this.item = item;
			this._renderSystemProps(attributes);
			this._renderDispositionProps(attributes);
			this._renderVitalProps(attributes);
			this._renderPhysicalProps(attributes);
			this._renderAdditionalProps(attributes);
			this.logExit("createRendering");
		},
		
		_renderSystemProps: function(attributes) {
			this.logEntry("_renderSystemProps");
			var data = {};
			for(var i=0; i<attributes.length; i++){
				var name = attributes[i].id;
				if (this.item.isSystemProperty(name)) {
					data[name] = this._getDisplayValue(attributes[i]);
				}
			}
			this._appendPropGrid(data, this._systemPropPane.containerNode);
			this.logExit("_renderSystemProps");
		},
		
		_renderDispositionProps: function(attributes) {
			this.logEntry("_renderDispositionProps");
			var data = {};
			for(var i=0; i<attributes.length; i++){
				var name = attributes[i].id;
				if(ier_util_property.isDispositionGroupProperty(name)){
					data[name] = this._getDisplayValue(attributes[i]);
				}
			}
			this._appendPropGrid(data, this._dispositionPane.containerNode);
			this.logExit("_renderDispositionProps");
		},
		
		_renderVitalProps: function(attributes) {
			this.logEntry("_renderVitalProps");
			var data = {};
			for(var i=0; i<attributes.length; i++){
				var name = attributes[i].id;
				if(ier_util_property.isVitalRecordGroupProperty(name)){
					data[name] = this._getDisplayValue(attributes[i]);
				}
			}
			this._appendPropGrid(data, this._vitalPane.containerNode);
			this.logExit("_renderVitalProps");
		},
		
		_renderPhysicalProps: function(attributes) {
			this.logEntry("_renderPhysicalProps");
			var data = {};
			for(var i=0; i<attributes.length; i++){
				var name = attributes[i].id;
				if(ier_util_property.isPhysicalGroupProperty(name)){
					data[name] = this._getDisplayValue(attributes[i]);
				}
			}
			this._appendPropGrid(data, this._physicalPane.containerNode);
			this.logExit("_renderPhysicalProps");
		},
		
		_renderAdditionalProps: function(attributes) {
			this.logEntry("_renderAdditionalProps");
			if(this.item.isFolder()){
				this._additionalPane.set("title", messages.entityItemDetailPane_label_additionalRecordContainerDetails);
			}
			var data = {};
			for(var i=0; i<attributes.length; i++){
				var name = attributes[i].id;
				if((attributes[i].hidden || (attributes[i].system && !this.item.isSystemProperty(name)))
						&& !ier_util_property.isDispositionGroupProperty(name)
						&& !ier_util_property.isVitalRecordGroupProperty(name)
						&& !ier_util_property.isPhysicalGroupProperty(name)
						&& !ier_util_property.isEntityItemNotDisplayedProperty(name)){
					data[name] = this._getDisplayValue(attributes[i]);
				}
			}
			this._appendPropGrid(data, this._additionalPane.containerNode);
			this.logExit("_renderAdditionalProps");
		},
		
		_appendPropGrid: function(props, target) {
			var properties = new Array();
			var resources = {};
			for(var i in props){
				properties.push(i);
				var label = this.item.getAttrLabel(i);
				if (label && label.replace) {
					label = label.replace(/&/g, '&amp;').replace(/</g, '&lt;');
				}
				resources[i+"Label"] = label || i;
			}
			properties = properties.join(",");
			this._dispoPropGrid = new PropertyGrid({
				data: props,
				properties: properties,
				labelKeySuffix: "Label",
				resources: resources
			});
			this._dispoPropGrid.placeAt(target, "first");
			domStyle.set(this._dispoPropGrid.domNode, "height", "100%");
			this._dispoPropGrid.startup();
		},
		
		_getDisplayValue: function(attribute){
			var name = attribute.id;
			var value = this.item.getDisplayValue(name);
			if(attribute.dataType == "xs:object"){
				if(value != null){
					var split = value.split(",");
					if(split.length == 3)
						return split[2];
				}
			}
			return value;		
		}
	});
});