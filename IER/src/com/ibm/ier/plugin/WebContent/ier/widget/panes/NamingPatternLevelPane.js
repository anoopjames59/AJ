define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"dojo/dom-construct",
    	"dijit/Tooltip",
    	"dijit/form/Button",
    	"dijit/form/NumberSpinner",
    	"dijit/form/Select",
    	"dijit/form/ValidationTextBox",
    	"dijit/registry",
    	"ecm/model/Request",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/NamingPatternLevelPane.html",
    	"ecm/widget/HoverHelp" // in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_class, dojo_construct,
		dijit_Tooltip, dijit_form_Button, dijit_form_NumberSpinner, dijit_form_Select, dijit_form_ValidationTextBox, dijit_registry,
		ecm_model_Request, ier_constants, ier_messages, ier_util, ier_widget_dialog_IERBaseDialogPane, templateString){

return dojo_declare("ier.widget.panes.NamingPatternLevelPane", [ier_widget_dialog_IERBaseDialogPane], {

	templateString: templateString,
	_messages: ier_messages,

	repository: null,
	levels: null,

	postCreate: function(){
		this.inherited(arguments);

		this.connect(this._addButton, "onClick", this._addLevel);
	},

	_setLevelsAttr: function(levels){
		// clear content
		dojo_array.forEach(dijit_registry.findWidgets(this._levelContainer), function(widget){
			widget.destroy();
		});
		dojo_construct.empty(this._levelContainer);

		if(!levels || levels.length === 0){
			levels = [{level: 1, increment: 1}]; // add an empty row
		}else if(levels.length > 1){
			levels.sort(function(a, b){
				return (a && a.level || 0) - (b && b.level || 0);
			});
		}
		dojo_array.forEach(levels, this._createLevel, this);
	},

	_getLevelsAttr: function(){
		var levels = [];

		for(var row = this._levelContainer.firstChild; row; row = row.nextSibling){
			var level = {};
			var cell = row.firstChild;
			var levelBox = dijit_registry.getEnclosingWidget(cell.firstChild);
			level.level = levelBox.get("value");
			if(!level.level){
				continue;
			}
			cell = cell.nextSibling;
			var entityTypeBox = dijit_registry.getEnclosingWidget(cell.firstChild);
			level.entityType = entityTypeBox.get("value");
			if(!level.entityType){
				continue;
			}
			cell = cell.nextSibling;
			var patternBox = dijit_registry.getEnclosingWidget(cell.firstChild);
			level.pattern = patternBox.get("value");
			if(!level.pattern){
				continue;
			}
			cell = cell.nextSibling;
			var incrementBox = dijit_registry.getEnclosingWidget(cell.firstChild);
			var increment = incrementBox.get("value");
			if(increment){
				level.increment = increment;
			}
			levels.push(level);
		}

		return levels;
	},

	_addLevel: function(){
		var level = 1;
		for(var row = this._levelContainer.firstChild; row; row = row.nextSibling){
			var cell = row.firstChild;
			var levelBox = dijit_registry.getEnclosingWidget(cell.firstChild);
			var value = levelBox.get("value");
			if(value >= level){
				level = value + 1;
			}
		}
		this._createLevel({level: level, increment: 1});
		this.onInputChange(this);
	},

	_removeLevel: function(event){
		if(event && event.target){
			var row = event.target.parentNode;
			while(row){
				if(dojo_class.contains(row, "ierNamingPatternLevelRow")){
					break;
				}
				row = row.parentNode;
			}
			if(row){
				dojo_array.forEach(dijit_registry.findWidgets(row), function(widget){
					widget.destroy();
				});
				row.parentNode.removeChild(row);
				this.onInputChange(this);
			}
		}
	},

	_createLevel: function(level){
		level = level || {};

		var row = dojo_construct.create("tr", {"class": "ierNamingPatternLevelRow"});
		var cell = dojo_construct.create("td", {"class": "ierNamingPatternLevelCell"}, row);
		var levelBox = new dijit_form_NumberSpinner({"class": "ierLevelBox", required: true, intermediateChanges: true,
			value: (level.level || ""), constraints: {min: 1, pattern: '#'}, validator: function(){
				var value = this.get("value");
				return value && !(this.invalidLevel && this.invalidLevel == value);
			}});
		dojo_construct.create("label", {"for": levelBox.id,
			innerHTML: ier_messages.namingPatternLevelPane_level, style: "display: none;"}, cell);
		dojo_construct.place(levelBox.domNode, cell);
		cell = dojo_construct.create("td", {"class": "ierNamingPatternLevelCell"}, row);
		var options = [
			{value: ier_constants.ClassName_RecordCategory, label: ier_messages.recordCategory},
			{value: ier_constants.ClassName_RecordFolder, label: ier_messages.recordFolder}
		];
		var entityTypeBox = new dijit_form_Select({"class": "ierEntityTypeBox dijitSelectFixedWidth",
			options: options, sortByLabel: false,
			value: (level.entityType || ier_constants.ClassName_RecordCategory)});
		levelBox.onChange = entityTypeBox.onChange = dojo_lang.hitch(this, function(){
			this._validateLevel();
			this.onInputChange(levelBox);
		});
		dojo_construct.create("label", {"for": entityTypeBox.id,
			innerHTML: ier_messages.namingPatternLevelPane_entityType, style: "display: none;"}, cell);
		dojo_construct.place(entityTypeBox.domNode, cell);
		cell = dojo_construct.create("td", {"class": "ierNamingPatternLevelCell"}, row);
		var patternBox = new dijit_form_ValidationTextBox({"class": "ierPatternBox", required: true,
			value: (level.pattern || ""), validator: function(){
				var value = this.get("value");
				return value && !(this.invalidPattern && this.invalidPattern == value); 
			}});
		patternBox.onChange = dojo_lang.hitch(this, function(){
			this._validatePattern(patternBox);
			this.onInputChange(patternBox);
		});
		dojo_construct.create("label", {"for": patternBox.id,
			innerHTML: ier_messages.namingPatternLevelPane_pattern, style: "display: none;"}, cell);
		dojo_construct.place(patternBox.domNode, cell);
		cell = dojo_construct.create("td", {"class": "ierNamingPatternLevelCell"}, row);
		var incrementBox = new dijit_form_NumberSpinner({"class": "ierIncrementBox",
			value: (level.increment || ""), constraints: {min: 1, pattern: '#'}});
		dojo_construct.create("label", {"for": incrementBox.id,
			innerHTML: ier_messages.namingPatternLevelPane_increment, style: "display: none;"}, cell);
		dojo_construct.place(incrementBox.domNode, cell);
		cell = dojo_construct.create("td", {"class": "ierNamingPatternLevelCell"}, row);
		var removeButton = new dijit_form_Button({"class": "ierRemoveButton",
			iconClass: "ierRemoveIcon", showLabel: false});
		dojo_construct.place(removeButton.domNode, cell);
		this.connect(removeButton, "onClick", this._removeLevel);
		var tooltip = new dijit_Tooltip({connectId: [removeButton.domNode],
			label: ier_messages.namingPatternLevelPane_removeLevel});
		tooltip.startup();
		dojo_construct.place(row, this._levelContainer);
	},

	isValidationRequired: function(){
		return true;
	},

	validate: function(){
		return dojo_array.every(dijit_registry.findWidgets(this._levelContainer), function(widget){
			return !widget.isValid || widget.isValid();
		});
	},

	_validateLevel: function(){
		var map = {};
		for(var row = this._levelContainer.firstChild; row; row = row.nextSibling){
			var cell = row.firstChild;
			var levelBox = dijit_registry.getEnclosingWidget(cell.firstChild);
			if(levelBox.get("invalidLevel")){
				levelBox.set("invalidLevel", 0);
				levelBox.set("invalidMessage", "");
				levelBox.validate(); // clear error
			}
			var level = levelBox.get("value");
			if(!level){
				continue;
			}
			cell = cell.nextSibling;
			var entityTypeBox = dijit_registry.getEnclosingWidget(cell.firstChild);
			var entityType = entityTypeBox.get("value");
			if(!entityType){
				continue;
			}
			if(entityType == ier_constants.ClassName_RecordFolder && level === 1){
				levelBox.set("invalidLevel", level);
				levelBox.set("invalidMessage", ier_messages.namingPatternLevelPane_invalidFolderLevel);
				levelBox._hasBeenBlurred = true;
				levelBox.validate();
				continue;
			}
			var levels = map[entityType];
			if(!levels){
				map[entityType] = [level];
			}else if(dojo_array.indexOf(levels, level) >= 0){
				levelBox.set("invalidLevel", level);
				levelBox.set("invalidMessage", ier_messages.namingPatternLevelPane_duplicateLevel);
				levelBox._hasBeenBlurred = true;
				levelBox.validate();
			}else{
				levels.push(level);
			}
		}
	},

	_validatePattern: function(patternBox){
		patternBox.set("validating", true);
		var value = patternBox.get("value");
		patternBox.set("invalidPattern", "");
		patternBox.set("invalidMessage", "");
		var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){
			patternBox.set("validating", false);
			if(response.message){
				var m = response.message;
				patternBox.set("invalidPattern", value);
				patternBox.set("invalidMessage", m.text || m.userResponse || m.explanation || "");
				patternBox.validate();
				this.onInputChange(patternBox);
			}
		}));
		params.requestParams[ier_constants.Param_PatternString] = value;
		ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_ValidateNamingPattern, ier_constants.PostEncoding, params);
	}

});});
