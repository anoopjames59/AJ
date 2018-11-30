define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"ier/messages",
	"ier/constants",
	"ier/model/admin/Config",
	"ecm/Messages",
	"dojo/text!./templates/DDContainerSettingsPane.html",
	"dijit/form/NumberTextBox", // template
	"dijit/form/Select", // template
	"ecm/widget/HoverHelp", // template
	"idx/layout/TitlePane" // template
	
], function(declare, lang, dom_class, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin, messages, ier_constants, Config, ecm_messages, template, NumberTextBox, Select){
return declare([_Widget, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: template,

	config: null,
	dirty: false,
	valid: true,

	_messages: messages,
	ecmMessages: ecm_messages,

	postCreate: function(){
		this.inherited(arguments);
		
		this.connect(this._restoreDefaultsButton, "onClick", "_restoreDefaults");
		
		this.controls = {
			"defensibleSweepThreadCount": this._threadCountTextBox,
			"defensibleSweepQueryPageSize": this._queryPageSizeTextBox,
			"defensibleSweepUpdateBatchSize": this._updateBatchSizeTextBox,
			"defensibleSweepContentSizeLimit": this._contentSizeLimitTextBox,
			"defensibleSweepLinkCacheSizeLimit": this._linkCacheSizeLimitTextBox,
			"defensibleSweepOnHoldContainerCacheSize": this._onHoldContainerCacheSizeLimitTextBox
		};
		
		this.controlDefaults = {
				"defensibleSweepThreadCount": "1",
				"defensibleSweepQueryPageSize": "10000",
				"defensibleSweepUpdateBatchSize": "100",
				"defensibleSweepContentSizeLimit": "200000",
				"defensibleSweepLinkCacheSizeLimit": "100000",
				"defensibleSweepOnHoldContainerCacheSize": "100000"
		};
		
		for(var i in this.controls){
			this.connect(this.controls[i], "onChange", function(){ this._validate(true); });
		}	
	},
	
	_restoreDefaults: function(){
		for(var i in this.controls){
			var control = this.controls[i];
			if(control instanceof NumberTextBox)
				this._setValue(control, parseInt(this.controlDefaults[i]));
			
			if(control instanceof Select)
				this._setValue(control, String(this.controlDefaults[i]));
		}
		
		this._validate(false);
	},

	save: function(){
		if(this.config){
			for(var i in this.controls){
				this.config.set(i, this.controls[i].get("value") || "");
			}
		}
		
		for(var i in this.controls){
			ecm.model.desktop.defensibleSweepSettings[i] = this.config.get(i);
		}
	},

	reset: function(){
		for(var i in this.controls){
			var control = this.controls[i];
			if(control instanceof NumberTextBox)
				this._setValue(control, parseInt(this.config && this.config.get(i) || this.controlDefaults[i]));
			
			if(control instanceof Select)
				this._setValue(control, String(this.config && this.config.get(i)) || String(this.controlDefaults[i]));
		}
		
		this._validate(false);
	},
	
	_setValue: function(widget, value){
		var intermediateChanges = widget.intermediateChanges;
		widget.intermediateChanges = false;
		widget.set("value", value, false);
		widget.intermediateChanges = intermediateChanges;
	},

	_validate: function(dirty){
		this.valid = true;
		
		for(var i in this.controls){
			var control = this.controls[i];
			if(control.isValid && !control.isValid())
				this.valid = false;
		}
		this.dirty = dirty;
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}

});
});
