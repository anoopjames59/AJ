define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dijit/registry",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/util/dialog",
    	"ier/widget/ObjectSelector",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/widget/dialog/AddDispositionScheduleDialog",
    	"ier/model/DefensibleDisposalSchedule",
    	"dojo/text!./templates/EntityItemDispositionPane.html",
    	"dijit/form/TextBox", // in template
    	"dijit/form/Button", // in template
    	"dijit/form/RadioButton", // in template
    	"ecm/widget/HoverHelp", // in template
    	"dijit/form/NumberSpinner", // in template
    	"dijit/form/FilteringSelect", // in template
    	"dijit/form/Select", // in template
    	"ier/widget/panes/DefensibleDisposalDispositionPane", // in template
    	"ier/widget/panes/LegacyDispositionSchedulePane", // in template
    	"ier/widget/DispositionPropagationSelector" // in template

], function(dojo_declare, dojo_lang, dojo_domConstruct, dojo_style, dojo_class, dijit_registry, ier_constants, ier_messages, ier_util, ier_util_dialog, 
		ier_widget_ObjectSelector, ier_widget_dialog_IERBaseDialogPane, ier_dialog_AddDispositionScheduleDialog, DefensibleDisposalSchedule, templateString){

/**
 * @name ier.widget.panes.EntityItemDispositionPane
 * @class Provides an interface to set dispositions
 * @augments ier.widget.panes.EntityItemDispositionPane
 */
return dojo_declare("ier.widget.panes.EntityItemDispositionPane", [ier_widget_dialog_IERBaseDialogPane], {
	/** @lends ier.widget.panes.EntityItemDispositionPane.prototype */

	templateString: templateString,
	widgetsInTemplate: true,
	constants: ier_constants,
	messages: ier_messages,
	_dispositionListDialog: null,
	
	/**
	 * A reference to the Entity's Item's Properties Pane
	 */
	entityItemPropertiesPane: null,
	
	/**
	 * Whether this pane will be in readOnly mode
	 */
	readOnly: false,
	
	/**
	 * The item that this disposition pane will be displayed for
	 */
	item: null,
	
	_isLoaded: false,
	
	externallyManaged: false,

	/**
	 * Creates the rendering for this pane
	 */
	createRendering: function(params){
		if(!this._isLoaded){
			this.repository = params.repository; 
			this.parentFolder = params.parentFolder; 
			this.entityItemPropertiesPane = params.entityItemPropertiesPane;
			this.legacyDispositionSchedule = params.legacyDispositionSchedule; 
			this.item = params.item;
			
			if(!this.item){
				this.dispositionEmbeddedAssistance.innerHTML = ier_messages.entityItemDispositionPane_embeddedAssistance;
			}
			else {
				this.dispositionEmbeddedAssistance.innerHTML = ier_messages.entityItemDispositionPane_legacyScheduleAssistance;
			}
			
			this.connect(this.ddScheduleRadioButton, "onClick", "_scheduleButtonsClicked");
			this.connect(this.legacyScheduleRadioButton, "onClick", "_scheduleButtonsClicked");
			this.connect(this.noneScheduleRadioButton, "onClick", "_scheduleButtonsClicked");

			var externallyManaged = false;
			if (this.item)
				externallyManaged = this.item.attributes[ier_constants.Property_RMExternallyManagedBy];
			
			//if defensible disposal schedules are set
			if(this.item && this.item.isFolder() && this.item.isDefensibleDisposal()){
				this.ddScheduleRadioButton.set("checked", true);
				this.createDDScheduleRendering();
				
				this._defensibleDisposalPane.set("schedule", this.item.getDefensibleDisposalSchedule());
				
				//do not show the embedded assistance
				dojo_style.set(this.dispositionEmbeddedAssistance, "display", "none");
				
				//do not show the other sections
				dojo_style.set(this._legacyScheduleSection, "display", "none");
				dojo_style.set(this._noneScheduleSection, "display", "none");
				
				//hide radio button as well
				dojo_style.set(this.ddScheduleRadioButton, "display", "none");
			} 
			//if legacy disposition schedule is set
			else if(this.legacyDispositionSchedule) {
				this.createLegacyScheduleRendering();
				this.legacyScheduleRadioButton.set("checked", true);
				
				//disable the defensible disposable schedules
				this.disableDefensibleDisposalSchedules(true);
				this.disableDDScheduleRadiosSelector(true);
				
				//do not show the dd schedule section
				dojo_style.set(this._ddScheduleSection, "display", "none");
				//dojo_style.set(this._noneScheduleSection, "display", "none");

			}
			//none set at all.
			else {
				//show dd section when you are creating a new record category
				if(!this.item && this.entityItemPropertiesPane.rootClassId == ier_constants.ClassName_RecordCategory){
					//disable the dd schedules initially
					this.disableDefensibleDisposalSchedules(true);
				} else {
					//do not show it when viewing properties
					dojo_style.set(this._ddScheduleSection, "display", "none");
				}
				
				//only do this for a new item. 
				//check to see if the parent has the legacy schedule set, if it does, auto check the radio button
				//else just disable the radio button
				if(this.parentFolder && !this.item){
					this.parentFolder.getLegacyDispositionSchedule(dojo_lang.hitch(this, function(dispSchedule){
						if(dispSchedule){
							this.createLegacyScheduleRendering();
							this.legacyScheduleRadioButton.set("checked", true);
						} else {
							//initially disable the legacy schedules
							this.disableLegacyDispositionSchedules(true);
						}
					}));
				} else {
					//initially disable the legacy schedules
					this.disableLegacyDispositionSchedules(true);
				}
			}
			
			//if it's externally managed, disable the schedules and set it
			if(externallyManaged){
				this.disableDefensibleDisposalSchedules(true);
				this.disableDDScheduleRadiosSelector(true);
				this.disableNoneScheduleRadioSelector(true);
				this.disableLegacyDispositionSchedules(true);
				this.disableLegacyScheduleRadioSelector(true);
			}
			
			this._isLoaded = true;
		}
	},
	
	isLoaded: function(){
		return this._isLoaded;
	},
	
	/**
	 * Renders the legacy scheduling options
	 */
	createLegacyScheduleRendering: function(){
		this._legacyDispositionPane.createRendering({
			repository: this.repository,
			legacyDispositionSchedule: this.legacyDispositionSchedule,
			item: this.item,
			parentItem: this.parentFolder
		});
		
		this.connect(this._legacyDispositionPane, "onInputChange", "onInputChange");
		this.connect(this._legacyDispositionPane, "onInputChange", "onDispositionChanged");
		
		this._legacyScheduleCreated = true;
	},
	
	/**
	 * Renders the defensable disposal schedule options
	 */
	createDDScheduleRendering: function(){
		this._defensibleDisposalPane.createRendering({
			repository: this.repository
		});
		
		this.connect(this._defensibleDisposalPane, "onInputChange", "onInputChange");
		this.connect(this._defensibleDisposalPane, "onInputChange", "onDispositionChanged");
		
		this._ddScheduleCreated = true;
	},
	
	postCreate: function() {
		this.inherited(arguments);
	},
	
	isValidationRequired: function() {
		return true;
	},
	
	validate: function(){
		if(this.ddScheduleRadioButton.get("checked") == true){
			 if(this._defensibleDisposalPane)
				 return this._defensibleDisposalPane.validate();
		}
		
		if(this.legacyScheduleRadioButton.get("checked") == true){
			 if(this._legacyDispositionPane)
				 return this._legacyDispositionPane.validate();
		}
		return true;
	},
	
	/**
	 * Handles the clicking of the schedules radio buttons
	 */
	_scheduleButtonsClicked: function(evt){
		var target = dijit_registry.getEnclosingWidget(evt.target);
		var legacyChecked = target == this.legacyScheduleRadioButton;
		var ddChecked = target == this.ddScheduleRadioButton;
		if(legacyChecked){
			this.disableLegacyDispositionSchedules(false);
			this.disableDefensibleDisposalSchedules(true);
			this.disableNoneRadioSelector(true);
		} else if(ddChecked){
			this.disableDefensibleDisposalSchedules(false);
			this.disableLegacyDispositionSchedules(true);
			this.disableNoneRadioSelector(true);
		} else {
			this.disableLegacyDispositionSchedules(true);
			this.disableDefensibleDisposalSchedules(true);
			
			//if we have the legacy disposition schedule previously selected in properties view,
			//we should show the propagation select to let users propagate the no schedule to children containers
			if(this.item){
				dojo_style.set(this._dispositionPropagationSelect.domNode, "display", "");
				this._dispositionPropagationSelect.set("disabled", false);
			}
			else {
				dojo_style.set(this._dispositionPropagationSelect.domNode, "display", "none");
				this._dispositionPropagationSelect.set("disabled", true);
			}
		}
		this.onDispositionChanged();
	},
	
	/**
	 * Enables or disables the defensible disposal radio selector
	 */
	disableDDScheduleRadiosSelector: function(disabled){
		if(!disabled) {
			this.ddScheduleRadioButton.set("disabled", false);
			dojo_style.set(this._ddScheduleRadioButtonLabel, "color", "");
		}
		else {
			this.ddScheduleRadioButton.set("disabled", true);
			dojo_class.add(this._ddScheduleRadioButtonLabel, "labelReadOnly");
			dojo_style.set(this._ddScheduleRadioButtonLabel, "color", "gray");
		}
	},
	
	disableNoneRadioSelector: function(disabled){
		this._dispositionPropagationSelect.set("disabled", disabled);
	},
	
	/**
	 * Enables or disables the legacy schedule radio selector
	 */
	disableLegacyScheduleRadioSelector: function(disabled){
		if(!disabled) {
			this.legacyScheduleRadioButton.set("disabled", false);
			dojo_class.add(this._legacyScheduleRadioButtonLabel, "labelReadOnly");
			dojo_style.set(this._legacyScheduleAssistance, "display", "");
		}
		else {
			this.legacyScheduleRadioButton.set("disabled", true);
			dojo_class.remove(this._legacyScheduleRadioButtonLabel, "gray");
			if (this._legacyScheduleAssistance)
			{
				dojo_style.set(this._legacyScheduleAssistance, "display", "none");
			}
		}
	},
	
	/**
	 * Enables or disables the legacy schedule radio selector
	 */
	disableNoneScheduleRadioSelector: function(disabled){
		if(!disabled) {
			this.noneScheduleRadioButton.set("disabled", false);
			dojo_class.add(this._noneScheduleRadioButtonLabel, "labelReadOnly");
		}
		else {
			this.noneScheduleRadioButton.set("disabled", true);
			dojo_class.remove(this._noneScheduleRadioButtonLabel, "gray");
		}
	},
	
	/**
	 * Enables or disables the Defensible Disposal Schedules section
	 */
	disableDefensibleDisposalSchedules: function(disabled) {
		if(!disabled){
			this.ddScheduleRadioButton.set("checked", true);
			
			if(!this._ddScheduleCreated){
				this.createDDScheduleRendering();
			}
			this._defensibleDisposalPane.set("disabled", false);
			
		} else {			
			this._defensibleDisposalPane.set("disabled", true);
		}
	},
	
	/**
	 * Enables or disables the legacy disposition schedule section
	 */
	disableLegacyDispositionSchedules: function(disable) {
		if(!disable){
			this.legacyScheduleRadioButton.set("checked", true);
			
			if(!this._legacyScheduleCreated){
				this.createLegacyScheduleRendering();
			}
			this._legacyDispositionPane.set("disabled", false);
		} else {
			this._legacyDispositionPane.set("disabled", true);
		}
	},
	
	/**
	 * Get the disposition schedule set in this pane
	 */
	getDispositionSchedule: function(){
		if(this.legacyScheduleRadioButton.get("checked") == true){
			return this._legacyDispositionPane.get("schedule");
		} 
		else if(this.ddScheduleRadioButton.get("checked") == true){
			return this._defensibleDisposalPane.get("schedule");
		}
		else
			return null;
	},
	
	getLegacyDispositionScheduleProperties: function(){
		var params = {};
		var createdAndSelected = this.legacyScheduleRadioButton.get("checked") == true && this._legacyScheduleCreated;
		params[ier_constants.Param_SchedulePropagationLevel] = this.getSchedulePropagationLevel();

		if(createdAndSelected){
			params[ier_constants.Property_DispositonAuthority] = createdAndSelected ? this._legacyDispositionPane.get("authority"): null;
			params[ier_constants.Property_DisposalScheduleInheritedFrom] = createdAndSelected ? this._legacyDispositionPane.getDispositionScheduleInheritedFrom(): null;
		}		
		return params;
	},
	
	getSchedulePropagationLevel: function(){
		if(this.legacyScheduleRadioButton.get("checked") == true && this._legacyScheduleCreated)
			return this._legacyDispositionPane.get("propagation");
		else if(this.noneScheduleRadioButton.get("checked") == true && this._dispositionPropagationSelect.get("disabled") == false){
			return this._dispositionPropagationSelect.get("value");
		}
		else
			return null;
	},
	
	getDispositionAuthority: function(){
		return this._legacyScheduleCreated ? this._legacyDispositionPane.get("authority"): null;
	},

	/**
	 * This event is invoked if any disposition is changed
	 */
	onDispositionChanged: function(){
	}
});});
