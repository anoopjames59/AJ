define([
	"dojo/_base/declare",
	"dojo/dom-class",
	"dojo/store/Memory",
	"gridx/Grid",
	"gridx/core/model/cache/Async",
	"gridx/modules/ColumnResizer",
	"gridx/modules/Focus",
	"gridx/modules/Menu",
	"gridx/modules/SingleSort",
	"gridx/modules/extendedSelect/Row"
], function(declare, dom_class, Memory, Grid, Async, ColumnResizer, Focus, Menu, SingleSort, extendedSelectRow){
return declare(Grid, {

	cacheClass: Async,
	selectRowTriggerOnCell: true,

	sortable: true,
	selectable: true,
	compact: true,
	wholeRow: true,
	alternatingRows: true,

	buildRendering: function(){
		if(!this.store){
			this.store = new Memory();
		}
		if(!this.structure){
			this.structure = [{field: "id", name: "&nbsp;"}];
		}
		if(!this.modules){
			this.modules = [ColumnResizer, Focus, Menu];
		}
		if(this.sortable){
			this.modules.push(SingleSort);
		}
		if(this.selectable){
			this.modules.push(extendedSelectRow);
		}

		this.inherited(arguments);

		dom_class.toggle(this.domNode, "compact", this.compact);
		dom_class.toggle(this.domNode, "gridxWholeRow", this.wholeRow);
		dom_class.toggle(this.domNode, "gridxAlternatingRows", this.alternatingRows);
	},

	startup: function(){
		this.inherited(arguments);

		if(this.select && this.select.row){
			this.connect(this.select.row, "onSelectionChange", "onSelectionChange");
		}
	},

	getSelected: function(){
		if(this.select && this.select.row){
			return this.select.row.getSelected();
		}else{
			return [];
		}
	},

	deselectAll: function(){
		if(this.select && this.select.row){
			this.select.row.clear();
		}
	},

	onSelectionChange: function(){
	}

});
});
