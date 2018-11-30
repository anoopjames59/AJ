define([
    	"dojo/_base/declare",
    	"dojo/_base/event",
    	"dojo/_base/lang",
    	"dojo/_base/array",
    	"dojo/dom-class",
    	"dojo/dom-style",
    	"dojo/keys",
    	"ecm/Messages",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/dialog",
    	"ier/widget/dialog/IERBaseDialog",
    	"ier/widget/dialog/SelectVersionsDialog",
    	"dojo/text!./templates/ObjectListDialog.html",
    	"ier/widget/listView/gridModules/ObjectListRowContextMenu",
    	"dijit/layout/BorderContainer", // in content
    	"dijit/layout/ContentPane", // in content
    	"ier/widget/listView/ContentList", // in content
    	"ier/widget/FilePlanSearchBar" // in content
], function(dojo_declare, dojo_event, dojo_lang, dojo_array, dojo_class, dojo_style, dojo_keys,
		ecm_messages, ier_constants, ier_messages, ier_util_dialog, ier_dialog_IERBaseDialog, ier_widget_dialog_SelectVersionsDialog, contentString, ier_widget_listView_ObjectListRowContextMenu){

return dojo_declare("ier.widget.dialog.ObjectListDialog", [ier_dialog_IERBaseDialog], {
	contentString: contentString,
	widgetsInTemplate: true,
	
	ier_messages: ier_messages,
	messages: ecm_messages,
	
	_entityType: null,
	
	//indicator whether to show versions selection
	showVersionSelection: false,
	autofocus: true,
	refocus: true,
	
	disableContextMenu: false,
	
	//label for the create button
	createLabel: ier_messages.baseDialog_addButton,
	
	//boolean whether to show create button in the dialog
	showCreateButton: false,

	//boolean whether to handle the double click
	connectonRowDblClick: true,

	postCreate: function() {
		this.inherited(arguments);
		dojo_class.add(this.domNode, "objectListDialog");
		
		this._objectContentList.multiSelect = false;

		this._selectButton = this.addButton(ier_messages.baseDialog_select, "_onSelectItem", true, true);
		
		//a create button that can be overriden to allow the user to create an entity of the item to be selected
		if(this.showCreateButton){
			this._createButton = this.addButton(this.createLabel, "_onCreateButtonClicked", false, false);
			dojo_style.set(this._createButton.domNode, "float:left");
		}

		this.connect(ecm.model.desktop, "onChange", "_onDesktopChanged");
		if(this.connectonRowDblClick){
			this.connect(this._objectContentList, "onRowDblClick", "_onSelectItem");
		}
		this.connect(this._objectContentList, "onRowSelectionChange", "onInputChange");
		this.connect(this._filePlanSearchBar, "onSearchButtonClicked", "_onSearchButtonClicked");

		this._objectContentList.setGridExtensionModules(this.getContentListGridModules());
	},

	getContentListGridModules: function() {
		var array = [];
		if(!this.disableContextMenu)
			array.push(ier_widget_listView_ObjectListRowContextMenu);
		return array;
	},

	/**
	 * Shows the dialog
	 * @param repository
	 * @param items
	 * @param objectClass - className of object to retrieve
	 */

	show: function(repository, objectClass) {
		this.inherited("show", []);
		this._objectClass = objectClass;

		this.repository = repository;
		
		if(!this.title){
			var classTitle = null;
			
			if(!this.propertyName){
				if(this.contentClass)
					classTitle = this.contentClass.name;
				else if(objectClass)
					classTitle = objectClass;
				else 
					classTitle = null;
			}
				
			if(classTitle)
				this.title = ier_messages.objectSelector_title + classTitle;
			else
				this.title = ier_messages.objectSelector_objectTitle;
		}
		this.setTitle(this.title);
		this.retrieveObjects();		
		this.resize();
	},
	
	/**
	 * Filter by name
	 */
	_filterItems: function() {
		this.logEntry("_filterItems");
		var filterData = this.filter.get("value");
		// dont execute the filter if nothing changed
		if (this._filterData != filterData) {
			this._filterData = filterData;
			var resultSet = this._objectContentList.getResultSet();
			
			if(!filterData || filterData.length == 0)
				resultSet.removeFilter();
			else {
				resultSet.applyFilter("*" + filterData + "*");
			}
			this._objectContentList.setResultSet(resultSet);
		}
		this.logExit("_filterItems");
	},

	_onDesktopChanged: function(modelObject){
		var update = false;
		var func = dojo_lang.hitch(this, function(changedModel){
			if(changedModel.getClassName){
				var className = changedModel.getClassName();
				return className && (className == this._objectClass || className == this.propertyName);
			}
		});
		if(dojo_lang.isArray(modelObject)){
			update = dojo_array.some(modelObject, func);
		} else {
			update = func(modelObject);
		}
		if (update){
			this.retrieveObjects();
		}
	},

	/**
	 * Retrieves the filterString value that the user typed in the search textbox and uses it to retrieve a filtered list of custom objects
	 * @param value
	 */
	_onSearchButtonClicked: function(value){
		this._filterString = value;
		this.retrieveObjects();
	},
	
	onInputChange: function(){
		var valid = this.validate();
		this.setButtonEnabled(this._selectButton, valid);
		return valid;
	},

	/**
	 * Responds to when an item has been selected and chosen
	 */
	_onSelectItem: function() {
		var valid = this.onInputChange();
		if(valid){
			var selectedObjects = this._objectContentList.getSelectedItems();
			if(this.showVersionSelection){
				var dialog = new ier_widget_dialog_SelectVersionsDialog();
				
				this.connect(dialog, "onSelected", function(item) {
					this.onSelect([item]);
					dialog.hide();
					this.onCancel();
				});
				
				dialog.show(selectedObjects[0], this.repository);
				ier_util_dialog.manage(dialog);
			}
			else {
				this.onSelect(selectedObjects);
				this.onCancel();
			}
		}
	},
	
	/**
	 * Event invoked when the create button is clicked
	 */
	_onCreateButtonClicked: function(){
		var createDialog = this.onCreateButtonClicked();
		if(createDialog && createDialog.onAdd){
			this.connect(createDialog, "onAdd", function(items){
				this._onDesktopChanged(items);
			});
		}
	},

	onCreateButtonClicked: function(){
		
	},
	
	/**
	 * Sets the entityType of the objects that will be retrieved from this object selector.  By default, the CustomObject entityType will be used.
	 * @param entityType
	 */
	setEntityType: function(entityType){
		this._entityType = entityType;
	},
	
	/**
	 * If the class name is unknown, set the property name so the appropriate class name can be obtained from the requiredClass field of the provided property description
	 * @param propertyName
	 */
	setPropertyClassName: function(propertyName){
		this.propertyName = propertyName;
	},

	/**
	 * Event invoked when an object is selected
	 * @param selectedItem
	 */
	onSelect: function(selectedItems /*[ecm.model.Item]*/){
		//event
	},
	
	/**
	 * Performs the validation for the pane and returns whether the pane validated successfully or not.  Should be overriden by subclasses to do validation for the pane. 
	 * @returns {Boolean}
	 */
	validate: function() {
		var selectedObjects = this._objectContentList.getSelectedItems();
		var valid = selectedObjects != null && selectedObjects.length > 0;
		return valid;
	},
	
	retrieveObjects: function(){
		//property name is used to obtain the required class if class name is unknown.
		var additionalParams = [];
		additionalParams[ier_constants.Param_Type] = this.type;
		
		if(this.propertyName){
			additionalParams[ier_constants.Param_PropertyName] = this.propertyName;
			additionalParams[ier_constants.Param_ClassName] = this.contentClass.id;
		}
		
		this.repository.retrieveObjects(this._objectClass, this._filterString, dojo_lang.hitch(this, function(resultSet){
			this._objectContentList.setResultSet(resultSet);
			this.resize();
			this.onInputChange();
		}), false, additionalParams);
	},
	
	nop: null
});});