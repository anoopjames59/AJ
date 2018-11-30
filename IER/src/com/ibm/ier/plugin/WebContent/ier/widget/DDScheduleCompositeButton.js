define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"dojo/dom-construct",
    	"dojo/string",
    	"dojo/_base/connect",
    	"dijit/focus",
    	"dijit/_Widget",
    	"idx/html",
    	"ecm/LoggerMixin",
    	"ecm/model/Desktop",
    	"ecm/widget/CompositeButton",
    	"ier/messages",
    	"ier/util/util"
], function(dojo_declare, dojo_lang, dojo_class, dojo_construct, dojo_string, dojo_connect, dijit_focus, dijit_Widget, idx_html,
		ecm_LoggerMixin, ecm_model_desktop, ecm_widget_CompositeButton, ier_messages, ier_util){

/**
 * A wrapper class around ecm.widget.CompositeButton to handle DD schedules objects. It displays the mimeType class, 
 * 	with the selected object name and the correct tooltip.
 */
return dojo_declare("ier.widget.DDScheduleCompositeButton", [ecm_LoggerMixin, dijit_Widget],{

	ier_messages: ier_messages,
	_selectedItemButton: null,
	showRemoveIcon: false,
	
	/**
	 * The object item to be displayed as the object in the this composite button
	 */
	selectedItem: null,
	
	/**
	 * Whether this composite button widget should listen to changes and update itself
	 */
	listenToChanges: false,
	
	/**
	 * The parent item that holds a reference to this object item
	 */
	parentItem: null,

	postCreate: function() {
		this.logEntry("postCreate");		
		this.inherited(arguments);
	
		//if there's a default value, automatically create the item
		if(this.selectedItem)
			this._createCompositeButton(this.selectedItem);
		
		/**
		 * Listen to changes on the parent item and update itself.  For example if a record category (parentItem) changed its 
		 * disposition schedule (selectedItem), this will update accordingly
		 */
		if(this.listenToChanges){
			this._onChangeHandler = this.connect(ecm.model.desktop, "onChange", dojo_lang.hitch(function(changedModel){
				if(changedModel && this.parentItem && this.parentItem.id == changedModel.id){
					this.setSelectedItem(this.parentItem.getDefensibleDisposalSchedule());
				}
			}));
		}
		
		dojo_class.add(this.domNode, ["dijit ", "dijitReset", "dijitInline"]);
		
		this.logExit("postCreate");
	},
	
	/**
	 * Sets the selected item
	 * @param item
	 */
	setSelectedItem: function(item){
		if(this._selectedItemButton)
			this._selectedItemButton.destroy();
		
		this.selectedItem = item;
		this._createCompositeButton(this.selectedItem);
	},
	
	/**
	 * Sets whether the object composite button should be disabled
	 */
	setDisabled: function(disabled){
		if(this._selectedItemButton)
			this._selectedItemButton.set("disabled", disabled);
	},
	
	/**
	 * Creates the compositeButton for the current selection.  Connects to a number of event to handle clicking and removing the button
	 * @param selectedItem
	 */
	_createCompositeButton: function(selectedItem){
		if(selectedItem) {
			if(this._selectedItemButton)
				this._selectedItemButton.destroy();
			
			var label = this.selectedItemLabel ? this.selectedItemLabel : selectedItem.name;
			
			//creates the composite button to represent the selected custom object
			this._selectedItemButton = new ecm_widget_CompositeButton({
				label: label,
				disabled: this.readOnly,
				iconClass: "taskBasicScheduleIcon",
				actionIconClass: this.showRemoveIcon ? "removeIcon" : null,
				tooltip: this._getToolTip(selectedItem),
				actionAltText: this.showRemoveIcon ? dojo_string.substitute(ier_messages.objectSelector_remove, [selectedItem.name]) : null
			});
			
			//this invokes the remove button to clear out the selected value
			this.connect(this._selectedItemButton, "onActionButtonClick", function(event) {
				var itemToBeRemoved = this.selectedItem;
				this.selectedItem = null;
				this._selectedItemButton.destroy();
				
				this.onItemRemoved(itemToBeRemoved, event);
			});
			
			dojo_construct.place(this._selectedItemButton.domNode, this.domNode, "only");
		}
	},
	
	/**
	 * Retrieves the tooltip information for the current item.  This consists of ID, Name, and Description
	 * @param item
	 * @returns
	 */
	_getToolTip: function(item) {
		this.logEntry("_getToolTip");
		var tooltip = [];
		if(item.getRMRetentionTriggerPropertyName())
			tooltip.push(this._getToolTipText(ier_messages.retentionTriggerPropertyName + ": " + item.getRMRetentionTriggerPropertyName()));
		
		if(item.getRMRetentionPeriod())
			tooltip.push(this._getToolTipText(ier_messages.entityItemDispositionPane_retentionPeriod + ": " + 
					item.getRMRetentionPeriod("years") + " " + ier_messages.dispositionPane_years + 
					" - " + item.getRMRetentionPeriod("months") + " " + ier_messages.dispositionPane_months  +
					" - " + item.getRMRetentionPeriod("days") + " " + ier_messages.dispositionPane_days ));

		return tooltip.join("");

		this.logExit("_getToolTip");
	},
	
	_getToolTipText: function(name) {
		return "<div style='white-space:nowrap;'>" + idx_html.escapeHTML(name) + "</div>";
	},
	
	_onFocusContainer: function(evt){
		if(this._selectButton)
			dijit_focus.focus(this._selectButton);
	},
	
	/**
	 * Override the get function to return the right value and labels
	 * @param name
	 * @returns
	 */
	get:function(name){
		if(name == "displayedValue" || name == "value")
			return this.selectedItem ? this.selectedItem.id : "";
		else if(name == "label")
			return this.selectedItem ? this.selectedItem.name : "";
		else
			return this.inherited(arguments);
	},
	
	destroy: function(){
		if(this._onChangeHandler)
			dojo_connect.disconnect(this._onChangeHandler);

		this.inherited(arguments);
	},
	
	_nop: null
});});
	

