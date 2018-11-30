define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-class",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"dijit/focus",
    	"dijit/form/Button",
    	"ecm/LoggerMixin",
    	"ecm/widget/_SinglePropertyEditorMixin",
    	"ier/constants",
    	"ier/messages",
    	"ier/widget/ObjectCompositeButton",
    	"ier/widget/dialog/ObjectListDialog",
    	"dojo/text!./templates/ObjectSelector.html",
    	"ecm/widget/Button" // in template
], function(dojo_declare, dojo_lang, dojo_class, dojo_construct, dojo_style,
		dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_focus, dijit_form_Button,
		ecm_LoggerMixin, ecm_widget_SinglePropertyEditorMixin, ier_constants, ier_messages, ier_widget_ObjectCompositeButton,
		ier_widget_dialog_ObjectListDialog, templateString){

return dojo_declare("ier.widget.ObjectSelector", [ecm_LoggerMixin, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_widget_SinglePropertyEditorMixin],{

	templateString: templateString,
	widgetsInTemplate: true,
	objectClassName: ier_constants.ClassName_CustomObject,
	ier_messages: ier_messages,
	selectedItem: null,
	_selectedItemButton: null,
	_createButton: null,
	_objectDialog: null,
	_dialogCssClass: null,
	_entityType: null,
	
	//whether to show version selections
	showVersionSelection: false, 
	
	repository: null,
	
	//change the select button label
	selectButtonLabel: ier_messages.select_with_elipsis, 
	
	//provide an alternative label for the selected item itself
	label: null,
	
	//the type that is passed into the getObjectsService
	type: null,
	
	//whether to disable the context menu for each row in the object list dialog
	disableContextMenu: false,
	
	//label for the create button
	createLabel: ier_messages.baseDialog_addButton,
	
	//boolean whether to show create button in the dialog
	showCreateButton: false,
	
	attributeMap: dojo_lang.delegate(dijit_Widget.prototype.attributeMap, {
		id: "focusNode",
		tabIndex: "focusNode"
	}),

	postCreate: function() {
		this.logEntry("postCreate");		
		this.inherited(arguments);
	
		//if there's a default value, automatically create the item
		if(this.selectedItem)
			this._createCompositeButton(this.selectedItem);
		
		if(this.name)
			this.valueNode.name = this.name;
		
		if(this.readOnly)
			this.set("disabled", true);
		
		if(this.noSelectButton)
			dojo_style.set(this._selectButton.domNode, "display", "none");
		else
			this._selectButton.set("label", this.selectButtonLabel);
		
		this.logExit("postCreate");
	},
	
	destroy: function(){
		if(this._selectedItemButton){
			this._selectedItemButton.destroy();
		}
		if(this._createButton){
			this._createButton.destroy();
		}
		if(this._objectDialog){
			this._objectDialog.destroy();
		}
		this.inherited(arguments);
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
	
	addDialogCssClass: function(cssClass){
		this._dialogCssClass = cssClass;
	},
	
	/**
	 * Returns the prompt text for this selector when rendered as part of CommonPropertiesPane
	 */
	getPromptText: function(){
		return ier_messages.objectSelector_promptText;
	},
	
	/**
	 * Sets the selected item
	 * @param item
	 */
	setSelectedItem: function(item, initial){
		if(this._selectedItemButton){
			if(this.showVersionSelection){
				this._selectedItemButton.selectedItemLabel = this._getVersionLabel(item);
			}
				
			this._selectedItemButton.setSelectedItem(item);
		}
			
		else
			this._createCompositeButton(item);
		
		this.selectedItem = item;
		this.onItemSelected(this.selectedItem);
		if(!initial){
			this.onChange();
		}
	},
	
	_getVersionLabel: function(selectedItem){
		if(this.showVersionSelection){
			var label = selectedItem.name + " ( " + ier_messages.version + " " + selectedItem.attributes["MajorVersionNumber"] + "." 
				+ (selectedItem.attributes["MinorVersionNumber"] ? selectedItem.attributes["MinorVersionNumber"] : "0") + " )";
			
			return label;
		}
		return null;
	},
	
	/**
	 * Shows the custom object selection dialog
	 */
	_onSelectObject: function() {
		if(this._objectDialog){
			this._objectDialog.destroy();
		}

		this._objectDialog = new ier_widget_dialog_ObjectListDialog({
			contentClass : this.contentClass,
			showVersionSelection : this.showVersionSelection,
			showCreateButton : this.showCreateButton,
			createLabel : this.createLabel,
			type: this.type,
			disableContextMenu: this.disableContextMenu
		});
		this.connect(this._objectDialog, "onSelect", "_onSelectItem");
		
		//event invoked when the create button is clicked
		this.connect(this._objectDialog, "onCreateButtonClicked", "onItemCreate");
		
		if(this._dialogCssClass)
			this._objectDialog.addDomNodeCSSClass(this._dialogCssClass);
		
		if(this._entityType)
			this._objectDialog.setEntityType(this._entityType);
		
		if(this.propertyName)
			this._objectDialog.setPropertyClassName(this.propertyName);

		this._objectDialog.show(this.repository, this.objectClassName);
	},

	/**
	 * Event invoked when items are selected on the object selection dialog
	 * @param selectedItems
	 */
	_onSelectItem: function(selectedItems) {
		if(selectedItems && selectedItems.length > 0){
			if(selectedItems[0] != this.selectedItem){
				this.selectedItem = selectedItems[0];
				this.valueNode.value = this.selectedItem.id;
				this.setSelectedItem(this.selectedItem);
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
			
			var label = null;
			if(this.showVersionSelection){
				label = this._getVersionLabel(selectedItem);
			}
			
			//creates the composite button to represent the selected object
			this._selectedItemButton = new ier_widget_ObjectCompositeButton({
				showRemoveIcon: true,
				selectedItemLabel: label,
				selectedItem: selectedItem,
				repository: this.repository,
				readOnly: this.readOnly
			});
			
			//this invokes the remove button to clear out the selected value
			this.connect(this._selectedItemButton, "onItemClicked", "onItemClicked");
	
			//this invokes the remove button to clear out the selected value
			this.connect(this._selectedItemButton, "onItemRemoved", dojo_lang.hitch(this, function(selectedItem, event) {
				this.clearItem();
				this.onItemRemoved(event);
			}));
			
			dojo_construct.place(this._selectedItemButton.domNode, this._selectedObjectsContainer, "first");
		}
	},
	
	clearItem: function(){
		if(this._selectedItemButton){
			this._selectedItemButton.clearItem();
		}
		this.selectedItem = null;
		this.valueNode.value = "";
		this.onChange();
	},

	/**
	 * Creates the button to create an entity
	 */
	createCreateButton: function(){
		this.showCreateButton = true;
		if(this._createButton){
			this._createButton.destroy();
		}
		this._createButton = new dijit_form_Button({
			label: ier_messages.objectSelector_createEntities
		});
		if(this.readOnly){
			this._createButton.set("disabled", true);
		}
		this.connect(this._createButton, "onClick", "_onCreateButtonClicked");
		dojo_construct.place(this._createButton.domNode, this._selectedObjectsContainer);
	},

	_onCreateButtonClicked: function(){
		var createDialog = this.onItemCreate();
		if(createDialog && createDialog.onAdd){
			this.connect(createDialog, "onAdd", function(items){
				this._onSelectItem(items);
			});
		}
	},

	_onFocusContainer: function(evt){
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
	 * If the class name is unknown, set the property name so the appropriate class name can be obtained from the requiredClass field of the provided property description
	 * @param propertyName
	 */
	setPropertyClassName: function(propertyName){
		this.propertyName = propertyName;
	},
	
	/**
	 * Sets the entityType of the objects that will be retrieved from this object selector.  By default, the CustomObject entityType will be used.
	 * @param entityType
	 */
	setEntityType: function(entityType){
		this._entityType = entityType;
	},
	
	/**
	 * Override the get function to return the right value and labels
	 * @param name
	 * @returns
	 */
	get:function(name){
		if(name == "value")
			return this.getValue();
		else if(name == "displayedValue")
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
	 * Override the set function to return the right value and labels
	 * @param name
	 * @returns
	 */
	_setDisabledAttr: function(bool) {
		if(bool){
			dojo_class.add(this.domNode, "dijitDisabled");
		}
		else
			dojo_class.remove(this.domNode, "dijitDisabled");
		
		if(this._selectButton){
			if (bool) {
				dojo_class.add(this._selectButton.domNode, "disabled");
			} else {
				dojo_class.remove(this._selectButton.domNode, "disabled");
			}
			this._selectButton.set("disabled", bool);
		}
		if(this._createButton){
			if (bool) {
				dojo_class.add(this._createButton.domNode, "disabled");
			} else {
				dojo_class.remove(this._createButton.domNode, "disabled");
			}
			this._createButton.set("disabled", bool);
		}
		
		if(this._selectedItemButton){
			this._selectedItemButton.setDisabled(bool);
		}
	},
	
	/**
	 * Event invoked when an item is selected
	 * @param selectedItem
	 */
	onItemSelected: function(selectedItem){
		
	},
	
	/**
	 * Event invoked when the create button is invoked
	 */
	onItemCreate: function(){
		
	},
	
	/**
	 * Event invoked when an item is clicked on
	 * @param selectedItem
	 */
	onItemClicked: function(selectedItem, event){
		
	},
	
	/**
	 * Event invoked when an item is removed
	 * @param selectedItem
	 */
	onItemRemoved: function(event){
		
	},
	
	/**
	 * Event invoked when the selection changes
	 */
	onChange: function(){
		
	},
	
	/**
	 * Determines if the field is valid
	 */
	isValid: function(){
		if(this.required)
			return (this.selectedItem != null);
		
		return true;
	},
	
	_nop: null
});});
	

