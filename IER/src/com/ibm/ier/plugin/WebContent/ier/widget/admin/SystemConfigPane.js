define([
	"dojo/_base/array",
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/date/locale",
	"dojo/date/stamp",
	"dijit/_TemplatedMixin", 
	"dijit/_Widget",
	"dijit/_WidgetsInTemplateMixin",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/widget/ObjectSelector",
	"dojo/text!./templates/SystemConfigPane.html",
	"ecm/widget/HoverHelp", // template
	"ecm/widget/NumberTextBox", // template
	"ecm/widget/Select" // template
], function(array, declare, lang, locale, stamp, _TemplatedMixin, _Widget, _WidgetsInTemplateMixin,
		constants, messages, util, ObjectSelector, SystemConfigPane_html){
return declare([_Widget, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: SystemConfigPane_html,

	repository: null,
	values: null,
	dirty: false,
	valid: true,

	_messages: messages,

	postCreate: function(){
		this.inherited(arguments);

		this._transferMappingSelector = new ObjectSelector({id: this.id + "_transferMappingSelector", labelId: this.id + "_transferMappingLabel",
			label: messages.admin_systemConfig_exportTransferMapping, objectClassName: constants.ClassName_TransferMapping});
		this._transferMappingSelector.placeAt(this._transferMappingCell);

		this._screeningWorkflowSelector = new ObjectSelector({id: this.id + "_screeningWorkflowSelector", labelId: this.id + "_screeningWorkflowLabel",
			label: messages.admin_systemConfig_screeningWorkflow, objectClassName: constants.ClassName_WorkflowDefinition});
		this._screeningWorkflowSelector.placeAt(this._screeningWorkflowCell);

		this.connect(this._exportConfigSelect, "onChange", function(){ this._validate(true); });
		this.connect(this._transferMappingSelector, "onChange", function(){ this._validate(true); });
		this.connect(this._batchSizeBox, "onChange", function(){ this._validate(true); });
		this.connect(this._screeningWorkflowSelector, "onChange", function(){ this._validate(true); });
		this.connect(this._patternSuffixSelect, "onChange", function(){ this._validate(true); });
	},

	_setRepositoryAttr: function(repository){
		this._set("repository", repository);
		this._transferMappingSelector.repository = repository;
		this._screeningWorkflowSelector.repository = repository;
		repository && repository.retrieveObjects(constants.ClassName_SystemConfiguration, null, lang.hitch(this, function(resultSet){
			var values = this.values = {};
			array.forEach(resultSet && resultSet.items, function(item){
				var name = item.attributes.PropertyName;
				var value = item.attributes.PropertyValue;
				if(name && value){
					values[name] = value;
				}
			});
			this.reset();
		}));
	},

	save: function(){
		this._setValue("Export Configuration", this._exportConfigSelect.get("value") || "");
		var id = this._transferMappingSelector.get("value");
		this._setValue("Export Transfer Mapping", id && util.getGuidId(id) || "0");
		this._setValue("Maximum Batch Size For Workflows", ("" + this._batchSizeBox.get("value")) || "");
		this._setValue("Screening Workflow", this._screeningWorkflowSelector.get("displayedValue") || "Screening Workflow");
		this._setValue("Volume Pattern Suffix", this._patternSuffixSelect.get("value") || "");
		if(this.repository && this.values){
			var systemConfigurations = [];
			for(var name in this.values){
				var value = this.values[name];
				if(value){
					systemConfigurations.push({name: name, value: value});
				}
			}
			this.repository.saveSystemConfigurations(systemConfigurations, lang.hitch(this, function(){
				this._validate(false);
			}));
		}
	},

	reset: function(){
		this._setWidgetValue(this._exportConfigSelect, this._getValue("Export Configuration"));
		this._transferMappingSelector.setSelectedItem(null, true);
		var id = this._getValue("Export Transfer Mapping");
		if(id && id != "0"){
			this.repository && this.repository.retrieveObjects(constants.ClassName_TransferMapping, null, lang.hitch(this, function(resultSet){
				array.some(resultSet && resultSet.items || [], function(item){
					if(item && item.id && util.getGuidId(item.id) == id){
						this._transferMappingSelector.setSelectedItem(item, true);
						return true;
					}
					return false;
				}, this);
			}));
		}
		this._fposSetupNode.innerHTML = this._getValue("FPOS Setup");
		this._versionInfoNode.innerHTML = this._getValue("RM Version Info");
		this._setWidgetValue(this._batchSizeBox, this._getValue("Maximum Batch Size For Workflows"));
		this._screeningWorkflowSelector.setSelectedItem(null, true);
		var name = this._getValue("Screening Workflow");
		if(name){
			this.repository && this.repository.retrieveObjects(constants.ClassName_WorkflowDefinition, name, lang.hitch(this, function(resultSet){
				array.some(resultSet && resultSet.items || [], function(item){
					if(item && item.name == name){
						this._screeningWorkflowSelector.setSelectedItem(item, true);
						return true;
					}
					return false;
				}, this);
			}));
		}
		var value = this._getValue("Security Script Run Date");
		if(value){
			if(value.length >= 15 && value.charAt(4) != "-"){ // classic format
				var d = [value.substring(0, 4), value.substring(4, 6), value.substring(6, 8)].join("-"); // yyyymmdd -> yyyy-mm-dd
				var t = [value.substring(9, 11), value.substring(11, 13), value.substring(13, 15)].join(":"); // hhmmss -> hh:mm:ss
				value = d + "T" + t + value.substring(15);
			}
			var date = stamp.fromISOString(value);
			value = locale.format(date, {formatLength: "short", fullYear: true});
		}
		this._runDateNode.innerHTML = value || "";
		this._setWidgetValue(this._patternSuffixSelect, this._getValue("Volume Pattern Suffix"));
		this._validate(false);
	},

	_getValue: function(name){
		return this.values && this.values[name] || "";
	},

	_setValue: function(name, value){
		if(this.values){
			this.values[name] = value;
		}
	},

	_setWidgetValue: function(widget, value){
		var intermediateChanges = widget.intermediateChanges;
		widget.intermediateChanges = false;
		widget.set("value", value, false);
		widget.intermediateChanges = intermediateChanges;
	},

	_validate: function(dirty){
		this.dirty = dirty;
		this.valid = this._batchSizeBox.isValid();
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}

});
});
