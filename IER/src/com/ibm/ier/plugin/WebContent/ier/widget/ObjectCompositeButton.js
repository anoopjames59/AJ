define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"dojo/dom-construct",
    	"dojo/dom-style",
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
], function(dojo_declare, dojo_lang, dojo_class, dojo_construct, dojo_style, dojo_string, dojo_connect, dijit_focus, dijit_Widget, idx_html,
		ecm_LoggerMixin, ecm_model_desktop, ecm_widget_CompositeButton, ier_messages, ier_util){

/**
 * A wrapper class around ecm.widget.CompositeButton to handle objects. It displays the mimeType class, with the selected object name and the correct tooltip.
 */
return dojo_declare("ier.widget.ObjectCompositeButton", [ecm_LoggerMixin, dijit_Widget],{

	ier_messages: ier_messages,
	_selectedItemButton: null,
	repository: null,
	showRemoveIcon: false,
	selectedItemLabel: null,
	
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
	parentItemColumn: null,

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
			this._onChangeHandler = this.connect(ecm.model.desktop, "onChange", dojo_lang.hitch(this, function(changedModel){
				if(changedModel && this.parentItem && this.parentItem.id == changedModel.id){
					this.parentItem.fetchIERObjectItem(this.parentItemColumn, dojo_lang.hitch(this, function(value){
						if(value != this.selectedItem ){
							this.setSelectedItem(value);
						}	
					}));				
				}
			}));
		}
		
		dojo_class.add(this.domNode, ["dijit ", "dijitReset", "dijitInline"]);
		
		this.logExit("postCreate");
	},
	
	/**
	 * Returns repository
	 * @returns
	 */
	getRepository: function(){
		return this.repository;
	},
	
	/**
	 * Sets repository
	 * @param repository
	 */
	setRepository: function(repository){
		this.repository = repository;
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
	 * Returns the selected item
	 */
	getSelectedItem: function(){
		return this.selectedItem;
	},
	
	/**
	 * Sets the label for the object composite button
	 */
	setLabel: function(label){
		this.selectedItemLabel = label;
	},
	
	/**
	 * Sets whether the object composite button should be disabled
	 */
	setDisabled: function(disabled){
		if(this._selectedItemButton && this.selectedItem){
			this._selectedItemButton.set("disabled", disabled);
			
			if(this._selectedItemButton._actionFocusNode){
				if(disabled)
					dojo_style.set(this._selectedItemButton._actionFocusNode, "display", "none");
				else
					dojo_style.set(this._selectedItemButton._actionFocusNode, "display", "");
			}
			
			if(this._selectedItemButton._focusNode && this._selectedItemButton._focusNode.childNodes[0]){
				if(disabled)
					dojo_style.set(this._selectedItemButton._focusNode.childNodes[0], "color", "gray");
				else
					dojo_style.set(this._selectedItemButton._focusNode.childNodes[0], "color", "");
			}
		}
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
				iconClass: ier_util.getIconClass(selectedItem) + " objectIcon",
				actionIconClass: this.showRemoveIcon ? "removeIcon" : null,
				tooltip: this._getToolTip(selectedItem),
				actionAltText: this.showRemoveIcon ? dojo_string.substitute(ier_messages.objectSelector_remove, [selectedItem.name]) : null
			});
			
			//This is invoked when the title is clicked
			this.connect(this._selectedItemButton, "onTitleClick", function(event){
				if(this.selectedItem){
					//if the object is not fully loaded, then retrieve the item first. 
					//This is in case that a dummy object with minimial properties is set as a selecte item - like the case in ContentList to minimize extra
					//processing
					var actionsHandler = ecm_model_desktop.getActionsHandler();
					var action = "actionIERProperties";
					if(!this.selectedItem.isIERLoaded || !this.selectedItem.isIERLoaded()){
						this.repository.retrieveItem(this.selectedItem.id, dojo_lang.hitch(this, function(itemRetrieved){
							this.selectedItem = itemRetrieved;
							this.selectedItem.IERLoaded = true;
							if (actionsHandler && actionsHandler[action]) {
								actionsHandler[action](this.repository, [this.selectedItem]);
							}
						}), this.selectedItem.template);
					} else {
						if (actionsHandler && actionsHandler[action]) {
							this.selectedItem.repository = this.repository;
							actionsHandler[action](this.repository, [this.selectedItem]);
						}
					}
					
					this.onItemClicked(this.selectedItem, event);
				}
			});
			
			//this invokes the remove button to clear out the selected value
			this.connect(this._selectedItemButton, "onActionButtonClick", function(event) {
				var itemToBeRemoved = this.selectedItem;
				this.clearItem();
				this.onItemRemoved(itemToBeRemoved, event);
			});
			
			dojo_construct.place(this._selectedItemButton.domNode, this.domNode, "only");
		}
	},
	
	clearItem: function(){
		this.selectedItem = null;
		this._selectedItemButton.destroy();
	},
	
	/**
	 * Retrieves the tooltip information for the current item.  This consists of ID, Name, and Description
	 * @param item
	 * @returns
	 */
	_getToolTip: function(item) {
		this.logEntry("_getToolTip");
		var tooltip = [];
		tooltip.push(this._getToolTipText(dojo_string.substitute(ier_messages.objectSelector_name, [item.name])));
		if(item.getRMDescription && item.getRMDescription())
			tooltip.push(this._getToolTipText(dojo_string.substitute(ier_messages.objectSelector_description, [item.getRMDescription()])));

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
	 * Sets the right custom object class name
	 * @param objectType
	 */
	setObjectClassName: function(objectClassName){
		this.objectClassName = objectClassName;
	},
	
	/**
	 * Override the get function to return the right value and labels
	 * @param name
	 * @returns
	 */
	get:function(name){
		if(name == "displayedValue" || name == "value")
			return this.getValue();
		else if(name == "label")
			return this.selectedItem ? this.selectedItem.name : "";
		else
			return this.inherited(arguments);
	},
	
	/**
	 * Returns the current selected value.  This is usually the GUID/ID of the object.
	 * @returns
	 */
	getValue: function(){
		return this.selectedItem ? this.selectedItem.id : "";
	},
	
	/**
	 * Event invoked when the item is removed
	 */
	onItemRemoved: function(selectedItem, event){
		
	},
	
	/**
	 * Event invoked when an item is clicked on
	 * @param selectedItem
	 */
	onItemClicked: function(selectedItem, event){
		
	},
	
	destroy: function(){
		if(this._onChangeHandler)
			dojo_connect.disconnect(this._onChangeHandler);

		this.inherited(arguments);
	},
	
	_nop: null
});});
	

