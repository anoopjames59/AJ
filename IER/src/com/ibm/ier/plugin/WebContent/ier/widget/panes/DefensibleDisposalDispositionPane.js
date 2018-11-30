define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dojo/store/Memory",
    	"dijit/registry",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/model/DefensibleDisposalSchedule",
    	"dojo/text!./templates/DefensibleDisposalDispositionPane.html",
    	"dijit/form/Button", // in template
    	"ecm/widget/HoverHelp", // in template
    	"dijit/form/NumberSpinner", // in template
    	"dijit/form/FilteringSelect" // in template
], function(dojo_declare, dojo_lang, dojo_domConstruct, dojo_style, dojo_class, dojo_store_Memory, dijit_registry, IERBaseDialogPane, 
		ier_constants, ier_messages, ier_util, DefensibleDisposalSchedule, templateString){

/**
 * @name ier.widget.panes.DefensibleDisposalDispositionPane
 * @class Provides an interface to set defensible disposal schedules
 * @augments ier.widget.panes.DefensibleDisposalDispositionPane
 */
return dojo_declare("ier.widget.panes.DefensibleDisposalDispositionPane", [IERBaseDialogPane], {
	/** @lends ier.widget.panes.DefensibleDisposalDispositionPane.prototype */

	templateString: templateString,
	widgetsInTemplate: true,
	constants: ier_constants,
	messages: ier_messages,
	
	/**
	 * Whether this pane will be in readOnly mode
	 */
	readOnly: false,
	
	/**
	 * The defensible disposal schedule
	 */
	defensibleScheduleItem: null,
	
	
	/**
	 * Creates the rendering for this pane
	 */
	createRendering: function(params){
		this.repository = params.repository != null ? params.repository : null; 
		this.item = params.item != null ? params.item : null;
			
		if(!this.readOnly){
			this._getAllAvailableRecordProperties(dojo_lang.hitch(this, function(options){
				
				this.retentionTriggerPropertyNameSelect.reset();
				this.retentionTriggerPropertyNameSelect.store = null;
				
				var store = new dojo_store_Memory({
					data: {
						identifier: "value",
						label: "label",
						items: options
					}
			    });

				this.retentionTriggerPropertyNameSelect.store = store;
				
				//if defensible disposal schedules are set
				if(this.defensibleScheduleItem){
					this.set("schedule", this.defensibleScheduleItem);
				}
				
				if(this.retentionTriggerPropertyNameSelect.getValue())
					this.retentionTriggerPropertyNameSelect.filter(this.retentionTriggerPropertyNameSelect.getValue());
			}));
		}
		else {			
			//if defensible disposal schedules are set
			if(this.defensibleScheduleItem){
				this.set("schedule", this.defensibleScheduleItem);
			}
		}
	},
	
	/**
	 * Retrieves the defensible schedule item set in this pane
	 */
	_getScheduleAttr: function() {
		var triggerPropertyName = this.retentionTriggerPropertyNameSelect.get("value");
		var retentionPeriodYear = this.retentionPeriod_years.get("value");
		var retentionPeriodMonth = this.retentionPeriod_months.get("value");
		var retentionPeriodDays = this.retentionPeriod_days.get("value");
		return new DefensibleDisposalSchedule({
			"retentionTriggerPropertyName": triggerPropertyName,
			"retentionPeriod": retentionPeriodYear + "-" + retentionPeriodMonth + "-" + retentionPeriodDays
		});
	},
	
	/**
	 * Sets the defensible schedule item for this pane
	 */
	_setScheduleAttr: function(schedule) {
		this.defensibleScheduleItem = schedule;
		this.retentionTriggerPropertyNameSelect.set("value", this.defensibleScheduleItem.getRMRetentionTriggerPropertyName());
		this.retentionPeriod_years.set("value", this.defensibleScheduleItem.getRMRetentionPeriod("years"));
		this.retentionPeriod_months.set("value", this.defensibleScheduleItem.getRMRetentionPeriod("months"));
		this.retentionPeriod_days.set("value", this.defensibleScheduleItem.getRMRetentionPeriod("days"));
	},
	
	_getAllAvailableRecordProperties: function(callback){
		this.logEntry("_getAllAvailableRecordProperties");
		
		this.repository.getAllRecordProperties(dojo_lang.hitch(this, function(properties) {
			var recordProps = [];
			if(properties){
				for(var i in properties){
					var property = properties[i];
					if(property.dataType == "xs:timestamp"){
						recordProps.push({
							label: property.name,
							value: String(property.id)
						});
					}
				}
			}
			callback(recordProps);
		}));
			
		this.logExit("_getAllAvailableRecordProperties");
	},
	
	validate: function(){
		var triggerPropertyName = this.retentionTriggerPropertyNameSelect.get("value");
		
		if(triggerPropertyName == null || triggerPropertyName.length == 0){
			return false;
		}
		
		return true;
	},
	
	isValidationRequired: function() {
		return true;
	},
	
	/**
	 * Enables or disables the Defensible Disposal Schedules section
	 */
	_setDisabledAttr: function(disabled) {
		if(!disabled){			
			dojo_class.remove(this.rententionTriggerPropertyNameLabel, "labelReadOnly");
			dojo_class.remove(this.retentionPeriodLabel, "labelReadOnly");
			dojo_class.remove(this.retentionPeriodLabel_years, "labelReadOnly");
			dojo_class.remove(this.retentionPeriodLabel_months, "labelReadOnly");
			dojo_class.remove(this.retentionPeriodLabel_days, "labelReadOnly");
			
		} else {			
			dojo_class.add(this.rententionTriggerPropertyNameLabel, "labelReadOnly");			
			dojo_class.add(this.retentionPeriodLabel, "labelReadOnly");
			dojo_class.add(this.retentionPeriodLabel_years, "labelReadOnly");
			dojo_class.add(this.retentionPeriodLabel_months, "labelReadOnly");
			dojo_class.add(this.retentionPeriodLabel_days, "labelReadOnly");
		}
		this.retentionPeriod_years.set("disabled", disabled);
		this.retentionPeriod_months.set("disabled", disabled);
		this.retentionPeriod_days.set("disabled", disabled);
		this.retentionTriggerPropertyNameSelect.set("disabled", disabled);
		this.retentionPeriodHoverHelp.set("disabled", disabled);
		this.triggerPropertyNameHoverHelp.set("disabled", disabled);
	}
});});
