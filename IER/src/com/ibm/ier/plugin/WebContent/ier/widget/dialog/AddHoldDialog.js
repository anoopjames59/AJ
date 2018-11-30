define([
	"dojo/_base/declare",
	"dojo/aspect",
	"dojo/dom-class",
	"dojo/dom-geometry",
	"ier/widget/dialog/HoldDialog",
	"dojo/text!./templates/AddHoldDialogContent.html",
	"dijit/layout/ContentPane", // template
	"dijit/layout/TabContainer",
	"idx/layout/TitlePane" // template
], function(declare, aspect, domClass, domGeom, HoldDialog, AddHoldDialogContent_html){
return declare(HoldDialog, {
	contentString: AddHoldDialogContent_html,
	
	postCreate: function(){
		this.inherited(arguments);
		var _this = this;
		this.own(this._propertyTitlePane.watch("open", function(prop, old, current){
			_this.defer(_this.resize, _this._propertyTitlePane.duration * 2);
		}), this._conditionTitlePane.watch("open", function(prop, old, current){
			_this.defer(function(){
				if(current){
					var selectedNode = _this._conditionPane._conditionTabContainer.selectedChildWidget.domNode;
					domClass.replace(selectedNode, "dijitVisible", "dijitHidden");
				}
				_this.resize();
			}, _this._conditionTitlePane.duration * 2);
		}));
	},
	resize: function(){
		this.inherited(arguments);
		var contentAreaSize = domGeom.getMarginBox(this.contentArea);
		var propertyTitlePaneSize = {h: (this._propertyTitlePane.open ? 213 : 53)};
		var conditionHeight = contentAreaSize.h - propertyTitlePaneSize.h;
		this._conditionTitlePane.resize({h: Math.max(0, conditionHeight - 10)});
		this._conditionPane._conditionTabContainer.resize({h: Math.max(0, conditionHeight - 73)});
	},
});
});
