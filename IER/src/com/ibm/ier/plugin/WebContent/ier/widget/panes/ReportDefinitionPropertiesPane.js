define([
"dojo/_base/declare",
"dojo/_base/lang",
"dojo/_base/array",
"dojo/dom-class",
"dojo/dom-geometry",
"dojo/dom-style",
"dojo/data/ItemFileWriteStore",
"ecm/widget/FilterTextBox",
"ecm/widget/SloshBucket",
"ier/constants",
"ier/messages",
"ier/util/util",
"ier/widget/dialog/IERBaseDialogPane",
"dojo/text!./templates/ReportDefinitionPropertiesPane.html"
], function(dojo_declare, dojo_lang, dojo_array, domClass, geometry, domStyle, ItemFileWriteStore, ecm_widget_filterBox, ecm_widget_sloshBucket,
		ier_constants, ier_messages, ier_util, ier_widget_IERBaseDialogPane, templateString) {	

	/**
	 * @name ier.widget.panes.ReportDefinitionPropertiesPane
	 * @class Provides the basic properties pane for selecting multiple items from available list to selected list.
	 * @augments ier.widget.dialog.IERBaseDialogPane
	 */
return dojo_declare("ier.widget.panes.ReportDefinitionPropertiesPane", [ier_widget_IERBaseDialogPane], {
	/** @lends ier.widget.panes.ReportDefinitionPropertiesPane.prototype */
	templateString: templateString,
	widgetsInTemplate: true,
	ier_messages: ier_messages,
	_availableItems: [],
	
	postCreate: function() {
		this.inherited(arguments);
		
		this.title = ier_messages.reportDefinitionDialog_ReportProperties;

		this._sloshBucket.hideAvailableOnAdd = true;
		this._sloshBucket.hasSorting = true;
		this._sloshBucket.tooltipClass = this;

		this.connect(this._filterBox, "_onInput", "_filter");
		this.connect(this._filterBox, "_setValueAttr", "_filter");
		this.connect(this._sloshBucket, "_onClickRemove", "onChange");
		this.connect(this._sloshBucket, "_onClickAdd", "onChange");		
		this.connect(this._sloshBucket, "_onClickUp", "onChange");		
		this.connect(this._sloshBucket, "_onClickDown", "onChange");		
	},
		
	createRendering: function(repository, reportDefinition) {
		this.logEntry("createRendering");
		this.repository = repository;				
		domStyle.set(this._filterBox.domNode, "display", "");
		domStyle.set(this._sloshBucket.domNode, "display", "");

		var contentClass = this.repository.getContentClass(ier_constants.ClassName_ReportHold);

		this._availableItems=[];
		this.repository.retrievePropertyDescriptions(contentClass, dojo_lang.hitch(this, function(properties) {
			for (var i in properties) {
				//if (properties[i].description == "RMReport")
				this._availableItems.push({label: properties[i].displayname, value: properties[i].symbolicname});					
			}
			
			this._availableItems.sort(function(a,b) {
				if(a.label < b.label)     return -1;     
				else if(a.label > b.label)     return 1;     
				else return 0;     
			});
			
			this.loadSelectableGrid(this._availableItems);
			
			var selectedItems =[], valueArray = {};//[];
			if (reportDefinition) {
				var params = reportDefinition.getParameters();
				if (params) {					
					dojo_array.forEach(params, function(param) {
						selectedItems.push({label: param.displayname, value: param.symname});
						//valueArray.push(param.symname);
						valueArray[param.symname] = param.symname;
					});
				}
			}
			this.loadSelectedGrid(selectedItems, valueArray);			
			this.onCompleteRendering();
		}));
		
		this.logEntry("createRendering");
	},
	
	/**
	 * Load the SloshBucket available grid
	 */
	loadSelectableGrid: function(availableItems) {
		this.logEntry("loadSelectableGrid");
		/*
		var availableItems=[
{label:"End Date", value:"end_date"},
{label:"RM Entity Type", value:"rm_entity_type"},
{label:"End Date2", value:"end_date2"},
{label:"End Date3", value:"end_date3"},
{label:"Start Data", value:"start_date"},
{label:"User Name", value:"user_name"},
{label:"File Plan Browse", value:"fileplan_browse"//, tooltip:"File Plan Browse tooltip"
	}];*/
		
		this._availableItemsStore = new ItemFileWriteStore({
			data: {
				items: availableItems
			},
			clearOnClose: true
		});

		var structure = [
			{
				field: "label",
				name: ier_messages.reportDefinitionDialog_Available,
				width: "100%"
			}
		];
		this._sloshBucket.setAvailableGridModel(this._availableItemsStore, structure);
		this._sloshBucket.filter({});
		this.connect(this._sloshBucket._availableData, "onRowDblClick", "onChange");
		this.logExit("loadSelectableGrid");
	},

	/**
	 * Load the SloshBucket selected grid
	 */
	loadSelectedGrid: function(selectedItems, valueArray, fixedItems) {
		this.logEntry("loadSelectedGrid");
		this._selectedItemsStore = new ItemFileWriteStore({
			data: {
				items: selectedItems
			},
			clearOnClose: true
		});

		var structure = [
			{
				field: "label",
				name: ier_messages.reportDefinitionDialog_Selected,
				width: "100%"
			}
		];
		this._sloshBucket.setSelectedValuesGridModel(this._selectedItemsStore, structure, valueArray, fixedItems);
		this.connect(this._sloshBucket._valuesGrid, "onRowDblClick", "onChange");
		this.logExit("loadSelectedGrid");
	},

	getSelectedItems: function() {
		this.logEntry("getSelectedItems");
		var selectedGriditems = this._sloshBucket.getData(this._sloshBucket.getSelectedValuesGrid());
		var newItems = [];
		for ( var i = 0; i < selectedGriditems.length; i++) {
			newItems.push(selectedGriditems[i]);
		}

		this.logExit("getSelectedItems");

		return newItems;

	},

	/**
	 * Filter default columns by label
	 */
	_filter: function() {
		this.logEntry("_filter");
		var filterData = this._filterBox.get("value");

		// Filter grid on text changes
		if (this._filterData != filterData) {
			this._filterData = filterData;
			this._sloshBucket.filter({
				label: filterData + "*"
			});
		}
		this.logExit("_filter");
	},

	resize: function(changeSize) {
		//this.inherited(arguments);
		var size = geometry.getContentBox(this.domNode);
		var filterBoxSize = geometry.getMarginBox(this._filterBox);
		this._sloshBucket.resize({
			w: 600,//size.w,
			h: 200 //size.h - filterBoxSize.h
		});
	},

	setEditable: function(editable) {
		this._sloshBucket.setEditable(editable);
	},

	onChange: function() {
	},
	
	onCompleteRendering: function() {		
	}
});});