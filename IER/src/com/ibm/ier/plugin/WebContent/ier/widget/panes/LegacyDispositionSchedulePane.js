define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dojo/store/Memory",
    	"dijit/registry",
    	"ier/widget/ObjectSelector",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/model/DispositionSchedule",
    	"dojo/text!./templates/LegacyDispositionSchedulePane.html",
    	"dijit/form/Button", // in template
    	"ecm/widget/HoverHelp", // in template
    	"dijit/form/NumberSpinner", // in template
    	"dijit/form/FilteringSelect", // in template
    	"dijit/form/CheckBox", // in template
    	"ier/widget/DispositionPropagationSelector" // in template
], function(dojo_declare, dojo_lang, dojo_domConstruct, dojo_style, dojo_class, dojo_store_Memory, dijit_registry, ier_widget_ObjectSelector, IERBaseDialogPane, 
		ier_constants, ier_messages, ier_util, DefensibleDisposalSchedule, templateString){

/**
 * @name ier.widget.panes.LegacyDispositionSchedulePane
 * @class Provides an interface to set the legacy disposition schedule
 * @augments ier.widget.panes.LegacyDispositionSchedulePane
 */
return dojo_declare("ier.widget.panes.LegacyDispositionSchedulePane", [IERBaseDialogPane], {
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
	 * The legacy disposition schedule
	 */
	legacyDispositionSchedule: null,
	
	/**
	 * Current item that the legacy disposition schedule is set to
	 */
	item: null,
	
	/**
	 * The parent item of the current item the legacy disposition is supposed to be set to.  This is used to automatically set child items with 
	 * inheriting schedules
	 */
	parentItem: null,
	
	/**
	 * Creates the rendering for this pane.  By default, the schedule propapgation settings are not shown.  Only if another disposition schedule is set, the propagation settings are shown.
	 */
	createRendering: function(params){
		this.repository = params.repository; 
		this.item = params.item;
		var legacyDispositionSchedule = params.legacyDispositionSchedule;
		this.parentItem = params.parentItem;
		
		//hide the inherited checkboxes if the parent item is the file plan since there are no schedules available
		if(this.parentItem && (this.parentItem instanceof ier.model.FilePlan))
			this._hideInheritedCheckbox(true);
		else
			this._hideInheritedCheckbox(false);
		
		var baseConstraints = {
				labelId : this.id + "_dispositonScheduleLabel",
				label : ier_messages.entityItemDispositionPane_dispositionInstructions + ":",
				selectButtonLabel : this.messages.entityItemDispositionPane_browseSchedules,
				showCreateButton : false,
				readOnly : this.readOnly
		};
		
		var objectSelector = new ier_widget_ObjectSelector(baseConstraints);
		this.objectSelector = objectSelector;
		objectSelector.setRepository(this.repository);
		objectSelector.setObjectClassName(ier_constants.ClassName_DispositionSchedule);
		
		this.connect(objectSelector, "onItemSelected", function(item){
			this.set("schedule", item);
		});
		
		this.connect(objectSelector, "onItemCreate", function(){
			this._onCreateSchedule();
		});
		
		this.connect(objectSelector, "onItemRemoved", function(){
			this._hidePropagationSettings(true);
			this.onInputChange();
		});
		
		//listen to the schedule checkbox being clicked.  It should disable the object selector and reset it to the parent's schedule if available
		this.connect(this._inheritedScheduleCheckBox, "onChange", function(){
			var checked = this._inheritedScheduleCheckBox.get("checked");
			
			//reset the disposition schedule to the parent's one if inherited is checked
			if(checked == true){
				//if parent legacy disposition schedule doesn't exist, grab it.
				if(!this.parentLegacyDispositionSchedule) {
					this.parentItem.getLegacyDispositionSchedule(dojo_lang.hitch(this, function(dispSchedule){
						if(dispSchedule){
							this.parentLegacyDispositionSchedule = dispSchedule;
							this.objectSelector.setSelectedItem(this.parentLegacyDispositionSchedule);
							this.dispositionScheduleInheritedFrom = this.parentItem.getGuidId();
							this._dispositionPropagationSelect.set("disabled", false);
							this.objectSelector.set("disabled", checked);
							this.onInputChange();
						}
					}));
				}else
					this.objectSelector.setSelectedItem(this.parentLegacyDispositionSchedule);
			}
			
			this.objectSelector.set("disabled", checked);
			this._dispositionPropagationSelect.set("disabled", false);
			this.onInputChange();
		});
		
		//check to see if the current item is inheriting the schedule, if it is, check the inheriting checkbox
		if(this.item){
			if(this.item.attributes[ier_constants.Property_DisposalScheduleInheritedFrom]){
				this._hideInheritedCheckbox(false);
				this._inheritedScheduleCheckBox.set("checked", true);
				
				//set the original parent schedule to the current one so it can be reset back to it if the user clicks on inherited checkbox
				this.parentLegacyDispositionSchedule = legacyDispositionSchedule;
				this.dispositionScheduleInheritedFrom = this.item.attributes[ier_constants.Property_DisposalScheduleInheritedFrom];	
			}
		}
			
		//if this is a new item being created, automatically inherit the schedule and fill in the inherited checkbox
		if(!this.item){
			//initially hide the inherited checkboxes.  Only if the parent item has an inherited schedule, then show it.
			this._hideInheritedCheckbox(true);
			
			if(this.parentItem && !(this.parentItem instanceof ier.model.FilePlan) && !legacyDispositionSchedule) {
				//save off the parent disposition schedule to reset it if inherited is checked
				this.parentItem.getLegacyDispositionSchedule(dojo_lang.hitch(this, function(dispSchedule){
				    if(dispSchedule){
				        this._hideInheritedCheckbox(false);
				        this._inheritedScheduleCheckBox.set("checked", true);
				
    					this.parentLegacyDispositionSchedule = dispSchedule;
    					legacyDispositionSchedule = this.parentLegacyDispositionSchedule;
    					if(legacyDispositionSchedule)
    					    this.dispositionScheduleInheritedFrom = this.parentItem.getGuidId();
    				}
				}));
			}
		}
		
		//if a legacy disposition schedule is set, it is usually in properties view and we just set it directly
		if(legacyDispositionSchedule){
			this.set("schedule", legacyDispositionSchedule);
		}
		
		//disable the object selector if it is inheriting from the parent
		if(this.dispositionScheduleInheritedFrom){
			this.objectSelector.set("disabled", true);
		}
		
		//hide propagation settings initially
		this._hidePropagationSettings(true);
		
		dojo_style.set(this._dispositionTextBox.domNode, "display", "none");
		dojo_domConstruct.place(objectSelector.domNode, this._dispositionObjectValueRow);
	},
	
	_hideInheritedCheckbox: function(hide){
		if(hide){
			dojo_style.set(this._inheritedScheduleCheckBox.domNode, "display", "none");
			dojo_style.set(this._inheritedScheduleCheckboxLabel, "display", "none");
		}
		else {
			dojo_style.set(this._inheritedScheduleCheckBox.domNode, "display", "");
			dojo_style.set(this._inheritedScheduleCheckboxLabel, "display", "");
		}
	},
	
	/**
	 * Retrieves the defensible schedule item set in this pane
	 */
	_getScheduleAttr: function() {
		//return the parent's disposition schedule if inherited is checked
		if(this._inheritedScheduleCheckBox.get("checked"))
			return this.parentLegacyDispositionSchedule;
		else
			return this.legacyDispositionSchedule;
	},
	
	_getAuthorityAttr: function(){
		return this._dispositionAuthorityTextBox.get("value");
	},
	
	_setAuthorityAttr: function(level){
		this._dispositionAuthorityTextBox.set("value", level);
	} ,
	
	_getPropagationAttr: function(){
		if(this._dispositionPropagationSelect.get("disabled") == false)
			return this._dispositionPropagationSelect.get("value");
		else
			return null;
	},
	
	_setPropagationAttr: function(level){
		this._dispositionPropagationSelect.set("value", level);
	} ,
	
	getDispositionScheduleInheritedFrom: function(){
		//only return that id of the parent container that this schedule is inherited from if inherited checkbox is checked
		if(this._inheritedScheduleCheckBox.get("checked"))
			return this.dispositionScheduleInheritedFrom;
		else
			return null;
	} ,
	
	/**
	 * Sets the defensible schedule item for this pane
	 */
	_setScheduleAttr: function(schedule) {
		if(!schedule || !this.legacyDispositionSchedule || (this.legacyDispositionSchedule && schedule && this.legacyDispositionSchedule.id != schedule.id)){
			this.legacyDispositionSchedule = schedule;
			
			if(schedule){
				//schedule's name sometimes is null..need to figure out later
				if(!schedule.name)
					schedule.name = schedule.attributes[ier_constants.Property_DispositionScheduleName];
			
				//obtain the disposition authority from the item if it's properties, if not, from the schedule
				var authority = this.item ? this.item.attributes[ier_constants.Property_DispositonAuthority] : schedule.getDispositionAuthority();
				
				//if the schedule was changed, preset the disposition authority.  Detect this by checking to see if the disposition schedule on the item is different
				if(this.item && this.item.attributes[ier_constants.Property_DispositionSchedule] != schedule.id)
					authority = schedule.getDispositionAuthority();
				
				this._dispositionAuthorityTextBox.set("value", authority);
			}
			
			if(this.objectSelector && this.objectSelector.selectedItem != schedule){
				this.objectSelector.setSelectedItem(schedule);
			}
			
			this._hidePropagationSettings(false);
			this.onInputChange();
		}
	},
	
	/**
	 * Hides the propagation settings by default.  If the disposition schedule changes, show it.
	 */
	_hidePropagationSettings: function(hide){
		if(hide){
			dojo_style.set(this._propagationSection, "display", "none");
		} else {
			//only show the propagation section if it is in properties view
			if(this.item)
				dojo_style.set(this._propagationSection, "display", "");
		}
		this._dispositionPropagationSelect.set("disabled", hide);
	},
	
	/**
	 * Handles when a user creates a new legacy disposition schedule
	 */
	_onCreateSchedule: function() {
		//obtain the container's name if it's available
		var containerName = null;
		if(this.entityItemPropertiesPane){
			containerName = this.entityItemPropertiesPane.getPropertyValue(this.entityItemPropertiesPane.getDefaultNameProperty());
		}
		var dialog = new ier_dialog_AddDispositionScheduleDialog();
		dialog.addDomNodeCSSClass("ierBigDialog");
		dialog.show(this.repository, null, null, {
			"DisposalScheduleName": containerName 
		});
		ier_util_dialog.manage(dialog);
	},
	
	validate: function(){
		if(this.objectSelector){
			if(this.objectSelector.getValue() == null || this.objectSelector.getValue().length == 0){
				return false;
			}
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
			dojo_class.remove(this._dispositionInstructionsLabel, "labelReadOnly");
			dojo_class.remove(this._dispositionAuthorityLabel, "labelReadOnly");
			dojo_style.set(this._inheritedScheduleCheckboxLabel, "color", "");
		} else {
			dojo_class.add(this._dispositionInstructionsLabel, "labelReadOnly");
			dojo_class.add(this._dispositionAuthorityLabel, "labelReadOnly");
			dojo_style.set(this._inheritedScheduleCheckboxLabel, "color", "gray");
		}
		
		this._dispositionInstructionsCreateButton.set("disabled", disabled);
		this._dispositionAuthorityTextBox.set("disabled", disabled);
		this.dispositionAuthorityHoverHelp.set("disabled", disabled);
		this._inheritedScheduleCheckBox.set("disabled", disabled);
		
		var checked = this._inheritedScheduleCheckBox.get("checked");
		if(this.objectSelector){
			this.objectSelector.set("disabled", disabled || checked);
		}
		this._dispositionPropagationSelect.set("disabled", disabled || checked);
	}
});});
