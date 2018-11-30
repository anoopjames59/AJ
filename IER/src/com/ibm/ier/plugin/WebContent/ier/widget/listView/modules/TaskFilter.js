/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dojo/dom-class",
	"dojo/dom-style",
	"dojo/keys",
	"dijit/form/CheckBox",
	"dijit/form/Select",
	"dijit/form/Button",
	"ecm/widget/listView/modules/_Module",
	"ecm/model/Desktop",
	"ier/messages",
	"ier/constants",
	"ier/widget/FilePlanSearchBar"
],

function(dojo_declare, dojo_lang, dojo_construct, dojo_class, dojo_style, dojo_keys, dijit_form_CheckBox, dijit_form_Select, dojo_Button, 
		ecm_listView_Module, ecm_model_desktop, ier_messages, ier_constants, ier_widget_FilePlanSearchBar) {

/**
 * @name ier.widget.listView.modules.TaskFilter
 * @class This content list module provides filter capability via a FilterTextBox and a FilteringSelect
 * @augments ecm.widget.listView.modules._Module
 */
return dojo_declare("ier.widget.listView.modules.TaskFilter", [ ecm_listView_Module ], {
	/** @lends ier.widget.listView.modules.TaskFilter.prototype */

	name: 'taskFilter',
	
	showUserOnlyCheckbox: true,
	
	filterSelectOptions: null,
	
	showFilterButton: true,

	getAPIPath: function() {
		return {
			taskFilter: this
		};
	},

	/**
	 * Preload listens to events.
	 */
	preload: function() {
		this.domNode = dojo_construct.create("span", {"class": "filterData"});
		
		if(this.showUserOnlyCheckbox)
		{
			//user only checkbox
			this.userCheckBox = new dijit_form_CheckBox({
				name: "userCheckbox",
				id: this.contentList.id + "_userCheckbox",
				value: true,
				style: "vertical-align: middle;"
			});
			dojo_construct.place(this.userCheckBox.domNode, this.domNode);
			this.connect(this.userCheckBox, "onClick", "onUserCheckboxClicked");
			
			this.setUserCheckBox();

			//Create the user checkbox label
			dojo_construct.create("label", {
				"for": this.contentList.id + "_userCheckbox",
				innerHTML: ier_messages.taskPane_currentUserTasksOnly,
				style: "margin-right: 10px;"
			}, this.domNode);
		}
		
		this.filterSelect = new dijit_form_Select({
			id: this.contentList.id + "_filterSelect",
            name: "filterSelect",
            options: this.filterSelectOptions,
            style: "margin-right: 7px;"
        });
		dojo_construct.place(this.filterSelect.domNode, this.domNode);
		
		this.connect(this.filterSelect, "onChange", function(value) {
			this.onFilterSelectChange(value);
		});
		
		//Create the user checkbox label
		dojo_construct.create("label", {
			"for": this.contentList.id + "_filterSelect",
			innerHTML: ier_messages.taskPane_currentUserTasksOnly,
			style: "display:none"
		}, this.domNode);

		// Create the filter text box
		this.filterTextBox = new ier_widget_FilePlanSearchBar({
			style: "margin-right: 10px;",
			placeHolder: ier_messages.nameContains,
			"aria-label": ier_messages.search_for
		});
		this.connect(this.filterTextBox, "onSearchButtonClicked", "onTextFilter");
		dojo_construct.place(this.filterTextBox.domNode, this.domNode);
	},

	/**
	 * Destroy.
	 */
	destroy: function() {
		var t = this;
		t._cleanUp();
		t.inherited(arguments);
	},

	/**
	 * @private Cleans up the widgets created.
	 */
	_cleanUp: function() {
		var t = this;
		if (t.filterTextBox) {
			t.filterTextBox.destroy();
		}
		
		if(t.userCheckBox){
			t.userCheckBox.destroy();
		}
		
		if(t.filterSelect){
			t.filterSelect.destroy();
		}
	},
	
	/**
	 * Clears the filter.
	 */
	clearFilter: function() {
		var t = this;
		t.filterTextBox.set("value", "");
		t.onTextFilter("");
		t.onClearFilter();
	},
	
	reset: function(){
		this.filterTextBox.set("value", "");
		this.userCheckBox.set("checked", false);
		if(this.filterSelect && this.filterSelectOptions)
			this.filterSelect.set("value", this.filterSelectOptions[0].value);
	},

	/**
	 * Event invoked when the user enters values into the text filter box
	 */
	onTextFilter: function(value) {
		
	},
	
	/**
	 * Event invoked when the user clears the filter box
	 */
	onClearFilter: function() {
		
	},
	
	/**
	 * Event invoked when the user changes the filter select box
	 */
	onFilterSelectChange: function(value) {
		
	},
	
	/**
	 * Event invoked when the user clicks on the user checkbox
	 */
	onUserCheckboxClicked: function(value){
		
	},
	
	setUserCheckBox: function(){
		//automatically disable the user checkbox and check it if the user is just a task user and not a task admin
		//that user can only see its own tasks
		var isTaskUserOnly = !ecm_model_desktop.taskManager.isTaskAdmin() && ecm_model_desktop.taskManager.isTaskUser();
		if(isTaskUserOnly){
			this.userCheckBox.set("checked", true);
			this.userCheckBox.set("disabled", true);
		} else {
			this.userCheckBox.set("checked", ecm_model_desktop.taskManager.showCurrentUserOnly);		
		}
	}
});});
