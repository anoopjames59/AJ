define([
	"dojo/_base/declare",
	"dojo/_base/array",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"dojo/dom-class",
	"ecm/model/Desktop",
	"ecm/model/AsyncTask",
	"ecm/widget/listView/ContentList",
	"ecm/Messages",
	"ier/constants",
	"ier/messages",
	"ier/widget/listView/decorators/ColumnDecorators"
], function(dojo_declare, dojo_array, dojo_lang, dojo_construct, dojo_dom_class, ecm_model_Desktop, ecm_model_AsyncTask,
		ecm_widget_listView_ContentList, ecm_messages, ier_constants, ier_messages, ier_listView_decorator_ColumnDecorators){

var contentList = dojo_declare("ier.widget.listView.ContentList", [ecm_widget_listView_ContentList], {

	postCreate: function(){
		this.inherited(arguments);
		
		//performing onchange changes for Tasks specifically
		this.connect(ecm_model_Desktop, "onChange", function(modelObject){
			if (this.grid && dojo_lang.isArray(modelObject)) {
				dojo_array.forEach(modelObject, function(changedModel, i) {
					if (changedModel && changedModel.isInstanceOf && (changedModel.isInstanceOf(ecm_model_AsyncTask))) {
						if (changedModel.deleted) {
							this.grid.store.deleteItem(changedModel);
							this.grid.select.row.clear();
						} else {
							this.grid.store.onSet(changedModel);
						}
					}
				}, this);
			}
		});
		dojo_dom_class.add(this.containerNode, "ierContentList");
	},
	
	setResultSet: function(resultSet) {
		if(resultSet){
			var view = resultSet.structure.cells[0];
			for(var i in view) {
				var cell = view[i];
				if (cell && cell.field == ier_constants.Property_RetainMetadata){
					cell.decorator = this.ierRetainMetadataDecorator;
				}else if(cell && cell.field == ier_constants.Property_SweepState){
					cell.formatter = this.ierSweepStateFormatter;
				}
			}
		}
		this.inherited(arguments);
	},

	ierRetainMetadataDecorator: function(value){
		return value === "0" ? ecm_messages.true_label : value === "1" ? ecm_messages.false_label : value;
	},

	ierSweepStateFormatter: function(row){
		var value = row.SweepState;
		var state = "";
		switch(value){
			case "0":
				state = ier_messages.sweep_state_0_label;
				break;
			case "1":
				var lastSweep = row.LastHoldSweepDate;
				if(lastSweep && lastSweep.length > 0){
					state = ier_messages.sweep_state_1_1_label;
				}else{
					state = ier_messages.sweep_state_1_2_label;
				}
				break;
			case "2":
				state = ier_messages.sweep_state_2_label;
				break;
			case "3":
				state = ier_messages.sweep_state_3_label;
				break;
			case "4":
				state = ier_messages.sweep_state_4_label;
				break;
			default:
				break;
		}
		return state;
	},
	
	/**
	 * Provide a public version of the perform default action for row id function in ICN's contentlist. 
	 *  
	 * Perform default action for the item at the specified row index. Used by the mime type decorator.
	 * 
	 * @param ID
	 *            of the grid row.
	 */
	performDefaultActionForRowId: function(rowId) {
		//call ICN's version if it's available
		if(this._performDefaultActionForRowId){
			this._performDefaultActionForRowId(rowId);
		}
		else {
			var item = this.grid.row(rowId).item();
			if (this.grid.rowContextMenu && this.grid.rowContextMenu.performDefaultActionForItem) {
				this.grid.rowContextMenu.performDefaultActionForItem(item);
			} else {
				this.openItem(item);
			}
		}
	}
});

return contentList;
});