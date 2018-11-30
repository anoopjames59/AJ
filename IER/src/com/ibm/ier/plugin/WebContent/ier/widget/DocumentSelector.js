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
    	"ecm/LoggerMixin",
    	"ecm/widget/_SinglePropertyEditorMixin",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/dialog",
    	"ier/widget/ObjectCompositeButton",
    	"ier/widget/dialog/ObjectListDialog",
    	"dojo/text!./templates/DocumentSelector.html",
    	"ecm/widget/Button" // in template
], function(dojo_declare, dojo_lang, dojo_class, dojo_construct, dojo_style,
		dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_focus,
		ecm_LoggerMixin, ecm_widget_SinglePropertyEditorMixin, ier_constants, ier_messages, ier_util_dialog, ier_widget_ObjectCompositeButton,
		ier_widget_dialog_ObjectListDialog, templateString){

return dojo_declare("ier.widget.DocumentSelector", [ecm_LoggerMixin, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_widget_SinglePropertyEditorMixin],{

	templateString: templateString,
	widgetsInTemplate: true,
	objectClassName: ier_constants.ClassName_CustomObject,
	ier_messages: ier_messages,
	selectedItem: null,
	_selectedItemButton: null,
	_dialogCssClass: null,
	_entityType: null,
	showVersionSelection: false, //whether to show version selections
	repository: null,
	selectButtonLabel: ier_messages.select_with_elipsis, //change the select button label
	
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
			this.setDisabled(true);
		
		if(this.noSelectButton)
			dojo_style.set(this._selectButton.domNode, "display", "none");
		else
			this._selectButton.set("label", this.selectButtonLabel);
		
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
	
	addDialogCssClass: function(cssClass){
		this._dialogCssClass = cssClass;
	},
	
	/**
	 * Returns the prompt text for this selector when rendered as part of CommonPropertiesPane
	 */
	getPromptText: function(){
		return ier_messages.objectSelector_promptText;
	},
	
	setDisabled: function(disabled){
		if(this._selectButton)
			this._selectButton.set("disabled", disabled);
		
		if(this._selectedItemButton)
			this._selectedItemButton.setDisabled(disabled);
	},
	
	/**
	 * Sets the selected item
	 * @param item
	 */
	setSelectedItem: function(item){
		if(this._selectedItemButton)
			this._selectedItemButton.setSelectedItem(item);
		else
			this._createCompositeButton(this.selectedItem);
		
		this.selectedItem = item;
	},
	
	/**
	 * Shows the custom object selection dialog
	 */
	_onSelectObject: function() {
		//Create the select object dialog
		
		var objectDialog = new ecm.widget.dialog.SelectObjectDialog({
			selectionMode: "document",
			showVersionSelection: false,
			multiSelect: false,
			repository: this.repository,
			showMultiRepositorySelector: false,
			title: "Select Record"
		});
		objectDialog.setWidth(800);
		objectDialog.setResizable(true);
		
		this.connect(objectDialog, "onSelect", function(selectedItems) {
			if(selectedItems && selectedItems.length > 0){
				if(selectedItems[0] != this.selectedItem){
					this.selectedItem = selectedItems[0];
					this.valueNode.value = this.selectedItem.id;
					this.setSelectedItem(this.selectedItem);
					this.onChange();
					this.onItemSelected(this.selectedItem);
				}
			}
		});
		
		//event invoked when the create button is clicked
		this.connect(objectDialog, "onCreateButtonClicked", "onItemCreate");
		
		if(this._dialogCssClass)
			objectDialog.addDomNodeCSSClass(this._dialogCssClass);
		
		if(this._entityType)
			objectDialog.setEntityType(this._entityType);
		
		if(this.propertyName)
			objectDialog.setPropertyClassName(this.propertyName);

		objectDialog.show(this.repository, this.objectClassName);
		ier_util_dialog.manage(objectDialog);
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
				label = selectedItem.name + " ( Version " + selectedItem.attributes["MajorVersionNumber"] + "." + selectedItem.attributes["MinorVersionNumber"] + " )";
			}
			
			//creates the composite button to represent the selected object
			this._selectedItemButton = new ier_widget_ObjectCompositeButton({
				showRemoveIcon: true,
				selectedItemLabel: label,
				selectedItem: selectedItem,
				repository: this.repository
			});
			
			//this invokes the remove button to clear out the selected value
			this.connect(this._selectedItemButton, "onItemClicked", "onItemClicked");
	
			//this invokes the remove button to clear out the selected value
			this.connect(this._selectedItemButton, "onItemRemoved", dojo_lang.hitch(this, function(selectedItem, event) {
				this.selectedItem = null;
				this.valueNode.value = "";
				this.onChange();
				this.onItemRemoved(event);
			}));
			
			dojo_construct.place(this._selectedItemButton.domNode, this._selectedObjectsContainer, "first");
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
	 * Override the set function to return the right value and labels
	 * @param name
	 * @returns
	 */
	_setDisabledAttr: function(bool) {
		if(this._selectButton){
			if (bool) {
				dojo_class.add(this._selectButton.domNode, "disabled");
			} else {
				dojo_class.remove(this._selectButton.domNode, "disabled");
			}
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
	

