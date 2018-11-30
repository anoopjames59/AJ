define([
    	"dojo/_base/declare",
    	"dojo/_base/event",
    	"dojo/_base/lang",
    	"dojo/keys",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"dijit/focus",
    	"dijit/form/Button",
    	"dijit/registry",
    	"dojox/grid/DataGrid",
    	"ecm/MessagesMixin",
    	"ecm/widget/admin/ActionMenu",
    	"ier/widget/admin/AdminCheckBox",
    	"dojo/text!./templates/AdminGrid.html",
    	"dijit/layout/BorderContainer" // in template
], function(dojo_declare, dojo_event, dojo_lang, dojo_keys, dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_focus, dijit_form_Button, dijit_registry, dojox_grid_DataGrid,
		ecm_MessagesMixin, ecm_widget_admin_ActionMenu, ier_widget_admin_AdminCheckBox, templateString){

/**
 * @name ier.widget.admin.AdminGrid
 * @class Provides a grid for admin.
 */
return dojo_declare("ier.widget.admin.AdminGrid", [ dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, ecm_MessagesMixin ], {
	/** @lends ier.widget.admin.AdminGrid.prototype */

	templateString: templateString,
	widgetsInTemplate: true,

	isResultSetSorted: true,

	postCreate: function() {
		this.inherited(arguments);
	},

	destroy: function() {
		if (this._actionMenuCreator) {
			this._actionMenuCreator.destroy();
		}
		this.inherited(arguments);
	},

	setActions: function(actions) {
		this._actions = actions;
	},

	setData: function(structure, store, actions) {
		this.setActions(actions);

		this._theGrid = new dojox_grid_DataGrid({ //new dojox.grid.EnhancedGrid({
			region: "center",
			store: store,
			structure: structure,
			selectionMode: "extended", // Value must be 'single', 'multiple', or 'extended'.  Default is 'extended'.
			plugins: {
			}
		}, document.createElement("div"));
		if (this.isResultSetSorted) { // If isResultSetSorted, the server returned the items sorted by name, update UI to show the sort indicator
			this.connect(this._theGrid, "_onFetchComplete", function() {
				if (this._theGrid.sortInfo == 0) { // On first display of the results, the sortInfo is 0.
					this._theGrid.sortInfo = 2; // set the sort to the 2nd column - Name column
					this._theGrid.views.views[0].renderHeader();
				}
			});
		}
		this._theGrid.doheaderclick = dojo_lang.hitch(this, "doheaderclick", this._theGrid);
		this.borderContainer.addChild(this._theGrid);
		this._theGrid.startup();
		this.resize();

		this.connect(this._theGrid, "onRowDblClick", function(evt) {
			this.onRowDblClick(this._theGrid.getItem(evt.rowIndex), evt);
		});

		this.connect(this._theGrid.selection, "onChanged", function(evt) {
			this.onSelectionChanged(this._theGrid.selection.getSelected(), evt);
		});

		this.connect(this._theGrid, "onRowContextMenu", "_doContextMenu");
		this.connect(this._theGrid, "onKeyDown", function(evt) {
			if (evt.keyCode === dojo_keys.F10 && evt.shiftKey) {
				this._doContextMenu(evt);
				dojo_event.stop(evt);
			} else if (evt.ctrlKey && evt.keyCode == 65) { // Ctrl A
				dojo_event.stop(evt);
				this._theGrid.selection.selectRange(0, this._theGrid.get('rowCount') - 1);
	
			//special handling for the buttons and checkboxes on the grid
			} else if (evt.keyCode == dojo_keys.ENTER || evt.keyCode == dojo_keys.SPACE) {
				var widget = dijit_registry.getEnclosingWidget(evt.target);
				if(widget && (widget instanceof ier_widget_admin_AdminCheckBox || widget instanceof dijit_form_Button)){
					this.handleClickAction(widget);
				} else {
					if(widget){
						var cellNode = widget.getCellNode(this._theGrid.selection.selectedIndex, evt.target.cellIndex);
						if(cellNode && cellNode.firstChild){
							cellWidget = dijit_registry.getEnclosingWidget(cellNode.firstChild);
							this.handleClickAction(cellWidget);
						}
					}
				}
				this._theGrid.focus.setFocusIndex(this._theGrid.selection.selectedIndex, evt.target.cellIndex);
			}});
	},
	
	//invoke the clickAction defined in these special widgets since onClick seems to get blocked
	handleClickAction: function(widget){
		if(widget && (widget instanceof ier_widget_admin_AdminCheckBox || widget instanceof dijit_form_Button)){
			if(widget.get("disabled") == false) {
				if(widget instanceof ier_widget_admin_AdminCheckBox){
					widget.onClickAction(widget, widget.get("checked"));
				}
				else {
					widget.clickAction(widget);
				}
				if(widget) {
					dijit_focus.focus(widget);
					widget.domNode.focus();
				}
			}
		}
	},
	
	// override
	onAction: function(items, action) {
	},

	// override
	onRowDblClick: function(modelItem, evt) {
	},

	// override
	onSelectionChanged: function(selectedModelItems) {
	},

	clearSelection: function() {
		this._theGrid.selection.clear();
	},

	getSelected: function() {
		return this._theGrid.selection.getSelected();
	},

	setStore: function(store) {
		this._theGrid.store.close();
		this._theGrid.sortInfo = 0; // re-set the sort to no column sorted
		this._theGrid.setStore(store);
	},

	filter: function(data) {
		this.clearSelection();
		this._theGrid.queryOptions = {
			ignoreCase: true
		};
		this._theGrid.filter(data);
	},

	// Called when right click within the grid.  Shows the item(s) content menu.
	_doContextMenu: function(evt) {
		if (evt.cellIndex < 0) {
			return; // If right click on empty right area of the grid, ignore it
		}
		var byKey = (evt.keyCode === dojo_keys.F10);

		// If clicked within the selection, use the selection; otherwise, clear selection & select the row
		if (!byKey && !this._theGrid.selection.isSelected(evt.rowIndex)) {
			this._theGrid.selection.clear();
			this._theGrid.selection.select(evt.rowIndex);
		}

		var items = this.getSelected();
		if (items.length > 0) {
			if (!this._actionMenuCreator) {
				this._actionMenuCreator = new ecm_widget_admin_ActionMenu();
				this.connect(this._actionMenuCreator, "onAction", "onAction");
			}
			var menu = this._actionMenuCreator.createMenu(this._actions, items);
			var coords = (!byKey ? {
				x: evt.pageX,
				y: evt.pageY
			} : null);
			menu._openMyself({
				target: evt.target,
				coords: coords
			});
		}
	},

	// Override grid's doheaderclick to support nosort for a column
	doheaderclick: function(grid, evt) {
		if (evt.cellNode && !evt.cell._props.nosort) {
			grid.setSortIndex(evt.cell.index);
		}
		grid.onHeaderClick(evt);
	},

	resize: function() {
		if(this._theGrid){
			this._theGrid.resize();
		}
		this.borderContainer.resize();
	},
	
	getGrid: function(){
		return this._theGrid;
	},
	
	getStore: function() {
		return this._theGrid.store;
	}
});});
