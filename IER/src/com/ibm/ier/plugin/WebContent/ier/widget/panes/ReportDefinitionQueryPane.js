define([
"dojo/_base/declare",
"dojo/_base/lang",
"dojo/_base/array",
"dojo/_base/connect",
"dojo/_base/event",
"dojo/keys",
"dojo/dom-class",
"dojo/dom-geometry",
"dojo/dom-style",
"dojo/data/ItemFileWriteStore",
"idx/data/JsonStore",
"ier/constants",
"ier/messages",
"ier/util/util",
"ier/widget/dialog/IERBaseDialogPane",
"ier/util/dialog",
"dojo/text!./templates/ReportDefinitionQueryPane.html",
"dijit/layout/ContentPane",	// in template
"dojox/grid/DataGrid", // in template
"dojox/grid/cells/_base", // in template
"dijit/form/SimpleTextarea"
], function(dojo_declare, dojo_lang, dojo_array, dojo_connect, dojo_event, dojo_keys, domClass, geometry, domStyle, ItemFileWriteStore, idx_data_JsonStore,
		ier_constants, ier_messages, ier_util, ier_widget_IERBaseDialogPane, ier_util_dialog, templateString) {	

	/**
	 * @name ier.widget.panes.ReportDefinitionQueryPane
	 * @class Provides the basic properties pane for selecting multiple items from available list to selected list.
	 * @augments ier.widget.dialog.IERBaseDialogPane
	 */
return dojo_declare("ier.widget.panes.ReportDefinitionQueryPane", [ier_widget_IERBaseDialogPane], {
	/** @lends ier.widget.panes.ReportDefinitionQueryPane.prototype */
	templateString: templateString,
	widgetsInTemplate: true,
	_paras: [],
	ier_messages: ier_messages,

	postCreate: function() {
		this.inherited(arguments);	
		this.title = ier_messages.reportDefinitionDialog_ReportSQL;
		
		dojo_connect.connect(this, "onKeyDown", function(evt) {
			if (evt.keyCode == dojo_keys.ENTER) {
				evt.stopPropagation();
			}
		});
		
		this.setTitlePaneFocusNodeHeight();
	},		

	createRendering: function(repository, reportDefinition) {
		this.repository = repository;				
		domStyle.set(this._paraGrid.domNode, "display", "");
		domStyle.set(this.commonSQLPane.domNode, "display", "");
		domStyle.set(this._sqlArea2.domNode, "display", "none");							

		var selectedItems =[];
		if (reportDefinition) {
			var params = reportDefinition.getParameters();
			if (params) {					
				dojo_array.forEach(params, function(param) {
					selectedItems.push({label: param.displayname, value: param.symname, isreq: param.isreq});
				});
			}
		};		
		this.setGridItems(selectedItems);
		
		if (reportDefinition) {
			var queries = reportDefinition.getQueries();
			if (queries && queries.length > 0) {
				for (var i = 0; i < queries.length; i++) {
					var query = queries[i];
					if (query.entity_type == ier_constants.ClassName_RecordCategory)
						this._sqlRecordCateogryArea.set('value', query.sql[0]);
					else if (query.entity_type == ier_constants.ClassName_RecordFolder)
						this._sqlRecordFolderArea.set('value', query.sql[0]);
					else if (query.entity_type == "Record")
						this._sqlRecordArea.set('value', query.sql[0]);
					else if (query.entity_type == ier_constants.ClassName_Volume)
						this._sqlVolArea.set('value', query.sql[0]);
					else  {
						this._sqlArea.set('value', query.sql[0]); 
						if (query.sql && query.sql.length > 1) {
							domStyle.set(this._sqlArea2.domNode, "display", "");
							this._sqlArea2.set('value', query.sql[1]);
							this._sqlAreaTitle.innerHTML = ier_messages.reportDefinitionDialog_SQLQueryStatements + ":";
						} else
							domStyle.set(this._sqlArea2.domNode, "display", "none");							
					}
				}
			}
		}

		this.onCompleteRendering();
	},
	
	setGridItems: function(items) {
		var gridItems = [];
		
		if (this._paraGrid.store) {
			this._paraGrid.store.fetch({
				onComplete: dojo_lang.hitch(this, "_fetchStoreData") 
			});
		}

		if (items)  {
			for (var i=0; i < items.length; i++) {
				var isreq = (items[i].isreq) ? items[i].isreq : false;
				for (var j=0; j < this._paras.length; j++) {
					if (items[i].value == this._paras[j].name)
						isreq = this._paras[j].isreq;
				}
				
				// The team decided to reuse the HoldName property and special-case on the code.
				if (items[i].value == ier_constants.Property_HoldName) 
					items[i].value = ier_constants.ReportEntry_hold_name;	

				gridItems.push({propertyName:items[i].label, symName:items[i].value, isReq:isreq});
			}
		}
		
		this._availableItemsStore = new idx_data_JsonStore({data: gridItems});
		this._paraGrid.setStore(this._availableItemsStore);
		
		var _hasEntityTypeProperty = dojo_array.some(gridItems, function(item) {
			return (item.symName == ier_constants.ReportEntry_rm_entity_type);//"rm_entity_type");
		});
		
		domStyle.set(this.commonSQLPane.domNode, "display", (_hasEntityTypeProperty ? "none" : ""));
		domStyle.set(this.rmentitySQLPane.domNode, "display", (_hasEntityTypeProperty ? "" : "none"));
	},
	
	_fetchStoreData: function(items) {
		this._paras = [];
		dojo.forEach(items, dojo_lang.hitch(this, function(item, index) {			
			var propertyName = item.propertyName[0];
			var symName = item.symName[0];
			var isReq = item.isReq[0];
			
			if (symName == ier_constants.ReportEntry_rm_entity_type) {
				var entityTypes = [];
				var sqlRec = this._sqlRecordArea.get("value").trim();
				var sqlCate = this._sqlRecordCateogryArea.get("value").trim();
				var sqlFolder = this._sqlRecordFolderArea.get("value").trim();
				var sqlVol = this._sqlVolArea.get("value").trim();
				if (sqlRec.length > 0)
					entityTypes.push({name:"Record", label:ier_messages.reportDefinitionDialog_Record});
				if (sqlCate.length > 0)
					entityTypes.push({name:ier_constants.ClassName_RecordCategory, label:ier_messages.reportDefinitionDialog_RecordCategory});
				if (sqlFolder.length > 0)
					entityTypes.push({name:ier_constants.ClassName_RecordFolder, label:ier_messages.reportDefinitionDialog_RecordFolder});
				if (sqlVol.length > 0)
					entityTypes.push({name:ier_constants.ClassName_Volume, label:ier_messages.reportDefinitionDialog_Volume});
				
				this._paras.push({label: propertyName, name: symName, isreq: isReq, value: entityTypes});						
			}
			else
				this._paras.push({label: propertyName, name: symName, isreq: isReq});
			
		}));
	},
	
	getQueries: function() {
		this.logEntry("loadSelectableGrid");

		this._paraGrid.store.fetch({
			onComplete: dojo_lang.hitch(this, "_fetchStoreData") 
		});
		
		var _hasEntityTypeProperty = dojo_array.some(this._paras, function(item) {
			return (item.name == ier_constants.ReportEntry_rm_entity_type);
		});
		
		var sqls = [];
		if (_hasEntityTypeProperty) {
			var sqlRec = this._sqlRecordArea.get("value").trim();
			var sqlCate = this._sqlRecordCateogryArea.get("value").trim();
			var sqlFolder = this._sqlRecordFolderArea.get("value").trim();
			var sqlVol = this._sqlVolArea.get("value").trim();
			
			if (sqlRec.length > 0)
				sqls.push({sql: [sqlRec], entity: "Record"});
			if (sqlCate.length > 0)
				sqls.push({sql: [sqlCate], entity: ier_constants.ClassName_RecordCategory});
			if (sqlFolder.length > 0)
				sqls.push({sql: [sqlFolder], entity: ier_constants.ClassName_RecordFolder});
			if (sqlVol.length > 0)
				sqls.push({sql: [sqlVol], entity: ier_constants.ClassName_Volume});
		}
		else {
			var sqlstmt2 = this._getSecondSQL();
			if (sqlstmt2 != null && sqlstmt2.length > 0)
				sqls.push({sql: [this._sqlArea.get("value").trim(), sqlstmt2], entity: ""});
			else
				sqls.push({sql: [this._sqlArea.get("value").trim()], entity: ""});
		}
		
		var queries = {parameters: this._paras, sqlstmts: sqls};
		this.logExit("loadSelectableGrid");

		return queries;
	},

	// Second SQL box is for updating OOTB report definitions. No second sql box shown on creating report definitions.
	_getSecondSQL: function() {
		var area2Display = domStyle.get(this._sqlArea2.domNode, "display");
		if (area2Display==null || area2Display == "none") return null;
		
		var sql2 = this._sqlArea2.get("value");
		if (sql2) sql2 = sql2.trim();
		return sql2;
	},
	
	_validateInput: function() {
		var _valid = false;		
		if (this._paraGrid.store) {
			this._paraGrid.store.fetch({
				onComplete: dojo_lang.hitch(this, "_fetchStoreData") 
			});
		}

		var _hasEntityTypeProperty = false;
		if (this._paras) {
			_hasEntityTypeProperty = dojo_array.some(this._paras, function(item) {
				return (item.name == ier_constants.ReportEntry_rm_entity_type);
			});
		}

		if (_hasEntityTypeProperty) {
			var sqlRec = this._sqlRecordArea.get("value").trim();
			var sqlCate = this._sqlRecordCateogryArea.get("value").trim();
			var sqlFolder = this._sqlRecordFolderArea.get("value").trim();
			var sqlVol = this._sqlVolArea.get("value").trim();
			
			_valid = ((sqlRec!=null && sqlRec.length>0) || 
						(sqlCate!=null && sqlCate.length>0) ||
						(sqlFolder!=null && sqlFolder.length>0) ||
						(sqlVol!=null && sqlVol.length>0));
		}
		else {
			var sqlCommon = this._sqlArea.get("value").trim();
			var sqlCommon2 = this._getSecondSQL();
			_valid = ((sqlCommon!=null && sqlCommon.length>0) || (sqlCommon2!=null && sqlCommon2.length>0));
		}
		
		if (!_valid) {
			if (_hasEntityTypeProperty) 
				ier_util_dialog.showMessage(ier_messages.reportDefDialog_noSQLError1);				
			else
				ier_util_dialog.showMessage(ier_messages.reportDefDialog_noSQLError2);
		}
		
		return _valid;
	},
	
	setTitlePaneFocusNodeHeight: function() {
		// When the surrounding containers are not yet sized, 
		// the title pane focus node height is set to 0 and the label is not displayed.
		// Check for this condition and set the height to the standard size.
		if (this._recordTitlePane.focusNode) {
			var _h = domStyle.get(this._recordTitlePane.focusNode, "height");
			if (_h == 0)
				domStyle.set(this._recordTitlePane.focusNode, "height", "15px");
		}
		if (this._recordCategoryTitlePane.focusNode) {
			var _h = domStyle.get(this._recordCategoryTitlePane.focusNode, "height");
			if (_h == 0)
				domStyle.set(this._recordCategoryTitlePane.focusNode, "height", "15px");
		}
		if (this._recordFolderTitlePane.focusNode) {
			var _h = domStyle.get(this._recordFolderTitlePane.focusNode, "height");
			if (_h == 0)
				domStyle.set(this._recordFolderTitlePane.focusNode, "height", "15px");
		}
		if (this._volumeTitlePane.focusNode) {
			var _h = domStyle.get(this._volumeTitlePane.focusNode, "height");
			if (_h == 0)
				domStyle.set(this._volumeTitlePane.focusNode, "height", "15px");
		}		
	},
	
	resize: function(changeSize) {
		//this.inherited(arguments);
		var size = geometry.getContentBox(this.domNode);
		
		this._paraGrid.resize({
			w: 300, h:144
		});
	},
	
	onCompleteRendering: function() {		
	}	
});
});