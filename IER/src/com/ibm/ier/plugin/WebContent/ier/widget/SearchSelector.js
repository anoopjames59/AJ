define([
    	"dojo/_base/declare",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dijit/form/Button",
    	"dijit/form/TextBox",
    	"ecm/Messages",
    	"ecm/widget/SearchSelector"
], function(dojo_declare, dojo_construct, dojo_style, dijit_form_Button, dijit_form_TextBox, ecm_messages, ecm_widget_SearchSelector){

/**
 * @name ier.widget.SearchSelector
 * @class Provides a tree or dropdown interface to select a search template. This widget will display the available
 *        search templates, in either a tree (default) or a drop-down. For some servers, templates can be within
 *        folders, in which case this widget will allow traversal into the folder to find and select a particular
 *        template.
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget.SearchSelector", [ ecm_widget_SearchSelector ], {
	/** @lends ecm.widget.SearchSelector.prototype */
	widgetsInTemplate: true,

	/**
	 * Creates the dijit rendering.
	 */
	_createRendering: function() {
		if (this.toolbarEnabled) {
			this._createToolbar();
		} else if (this._toolbarNode) {
			dojo_style.set(this._toolbarNode, "display", "none");
		}

		if (this.selectorStyle == "tree") {
			this._createTree();
		} else if (this.selectorStyle == "dropdown") {
			this._createDropDown();
		}
	},

	/**
	 * Creates a toolbar.
	 */
	_createToolbar: function() {
		if (this._toolbarNode) {
			dojo_style.set(this._toolbarNode, "display", "");
			return;
		}

		this._toolbarNode = dojo_construct.create("div", {
			"class": "toolbar"
		});
		
		this._secondToolBarNode = dojo_construct.create("div", {
			"class": "toolbar"
		});
		
		this._quickSearchTextBox = new dijit_form_TextBox({ size: 10});
		dojo_style.set(this._quickSearchTextBox.domNode, "margin-right", "5px");
		
		this._quickSearchButton = new dijit_form_Button({ label: "Search"});
		this.connect(this._quickSearchButton, "onClick", "_onQuickSearchButtonClick");
		
		this._toolbarNode.appendChild(this._quickSearchTextBox.domNode);
		this._toolbarNode.appendChild(this._quickSearchButton.domNode);
		
		this._newSearchButton = new dijit_form_Button({
			label: ecm_messages.new_search
		});
		this.connect(this._newSearchButton, "onClick", "onNewSearchButtonClick");
		this._secondToolBarNode.appendChild(this._newSearchButton.domNode);

		this.domNode.appendChild(this._toolbarNode);
		this.domNode.appendChild(this._secondToolBarNode);
	},
	
	_onQuickSearchButtonClick: function(){
		this.onQuickSearchButtonClick(this._quickSearchTextBox.get("value"));
	},
	
	//event when the quick search button is clicked
	onQuickSearchButtonClick: function(searchString){
		
	},

	destroy: function() {
		this._quickSearchTextBox.destroy();
		this._quickSearchButton.destroy();
		this._secondToolBarNode.destroy();

		this.inherited(arguments);
	}
});});
