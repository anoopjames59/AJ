define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/_base/event",
	"dojo/keys",
	"dojo/dom-style",
	"dojo/dom-geometry",
	"dojo/string",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/model/ResultSet",
	"ecm/model/Request",
	"ecm/Messages",
	"ier/widget/dialog/IERBaseDialogPane",
	"dojo/text!./templates/EntityItemHistoryPane.html",
	"ecm/widget/DatePicker",
	"dijit/form/TextBox",
	"ier/widget/listView/ContentList"
], function(dojo_declare, dojo_lang, dojo_array, dojo_event, dojo_keys, domStyle, domGeometry, dojo_string, ier_constants, ier_messages, ier_util, ier_model_ResultSet, ecm_model_Request, 
		ecm_messages, ier_widget_dialog_IERBaseDialogPane, templateString) {
	return dojo_declare("ier.widget.panes.EntityItemHistoryPane", [ier_widget_dialog_IERBaseDialogPane], {
		templateString: templateString,
		_ierMessages: ier_messages,
		startDate: "",
		endDate: "",
		filterField: "",
		filterStr: "",
		filterActionType: "",
		filterStatus: "",
		_isSearch: false,
		_isRemoved: false,

		isLoaded: function() {
			return this._isLoaded;
		},
		constructor: function() {
			this.inherited(arguments);
			this._dateFormat = ecm.model.desktop.valueFormatter.getDefaultFormat("xs:date").replace(/'/g, "\\'");
			this._invalidDateMessage = dojo_string.substitute(ecm_messages.property_date_invalid, [
				this._dateFormat
			]).replace(/'/g, "\\'");
		},
		postCreate: function(){
			this.inherited(arguments);
			this.connect(this, "onKeyDown", function(evt){
				if(evt.keyCode == dojo_keys.ENTER){
					dojo_event.stop(evt);
				}
			});
			this.connect(this._searchButton, "onClick", "_doSearch");
			this.connect(this._resetButton, "onClick", "_reset");
			this.connect(this._filterTypeSelect, "onChange", "_changeFilter");
			this.connect(this._searchCriteria, "toggle", "resize");
		},
		createColumnHeader: function(){
			var cols = new Array();
			cols.push({
				name: "",
				widthWebKit: "1.0em",
				width: "1.0em"
			});
			cols.push({
				name: this._ierMessages.historyPane_action,
				widthWebKit: "15.0em",
				width: "15.0em",
				sortable: false
			});
			cols.push({
				name: this._ierMessages.historyPane_dateTime,
				widthWebKit: "15.0em",
				width: "15.0em"
			});
			cols.push({
				name: this._ierMessages.historyPane_initiator,
				widthWebKit: "15.0em",
				width: "15.0em"
			});
			cols.push({
				name: this._ierMessages.historyPane_status,
				widthWebKit: "15.0em",
				width: "15.0em"
			});
			var cell = new Array();
			cell[0] = cols;
			var columns = new Object();
			columns.cells = cell;
			var response = new Object();
			response.columns = columns;
			response.rows = [];
			response.repository = this.repository;
			var resultSet = new ier_model_ResultSet(response);
			if (!this._isSearch){
				this._contentList.emptyMessage = this._ierMessages.historyPane_searchMessage;
			}
			this._contentList.setResultSet(resultSet);
			this._contentList.resize();
			if (!this._isSearch){
				this._contentList.emptyMessage = this._ierMessages.historyPane_emptyMessage;
			}
		},
		createRendering: function(repository, item) {
			this.logEntry("createRendering");
			repository && (this.repository = repository);
			item && (this.item = item);

			if (!this._isSearch){
				if(item.getEntityType() == ier_constants.EntityType_ElectronicRecord && !this._isRemoved){
					this._actionTypeSelect.removeOption("FileEvent");
					this._isRemoved = true;
				}
				this.createColumnHeader();
			}else{
				this.load();
			}
			this._isLoaded = true;
			this.logExit("createRendering");
		},
		load: function(sortColum){
			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {
				if(response != null){
					if(response.rows.length == 0){
						this.createColumnHeader();
					} else{
						response = this._customizeResponseObj(response);
						response.repository = this.repository;
						var resultSet = new ier_model_ResultSet(response);
						resultSet.sortFunc = dojo_lang.hitch(this, this.load);
						this._contentList.setResultSet(resultSet);
						this._contentList.resize();
						
					}
				}
			}));

			params.requestParams[ier_constants.Param_EntityId] = this.item.id;
			if(sortColum){
				params.requestParams[ier_constants.Param_OrderBy] = sortColum[0].attribute;
			}
			params.requestParams[ier_constants.Param_StartDate] = this.startDate;
			params.requestParams[ier_constants.Param_EndDate] = this.endDate;
			params.requestParams[ier_constants.Param_FilterField] = this.filterField;
			params.requestParams[ier_constants.param_FilterActionType] = this.filterActionType;
			params.requestParams[ier_constants.param_FilterStatus] = this.filterStatus;
			params.requestParams[ier_constants.Param_FilterString] = this.filterStr;
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetHistory, ier_constants.PostEncoding, params);
		},

		resize: function() {
			var historyPaneSize = domGeometry.getMarginBox(this._historyPane);
			if(historyPaneSize){
				var searchCriteriaSize = 69;
				if(this._searchCriteria.open){
					searchCriteriaSize = 150;
				}
				var contentListSize = historyPaneSize.h - searchCriteriaSize;
				domGeometry.setMarginBox(this._contentList.domNode, {
					h: contentListSize > 0 ? contentListSize : 0
				});
			}

			this._contentList.resize();
			this._searchCriteria.resize();
		},
		_validateDateRange: function(){
			this._dateRangeError.innerHTML = "";
			var isValid = true;
			if(this._startDatePicker.isValid() && this._endDatePicker.isValid()){
				var startD = this._startDatePicker.getValue();
				var endD = this._endDatePicker.getValue();
				if(startD && startD !== "" && endD && endD !== "" && startD > endD){
					this._dateRangeError.innerHTML = this._ierMessages.historyPane_dateRangeError;
					isValid = false;
				}
			}
			this._searchButton.set("disabled", !isValid);
		},

		_changeFilter: function(){
			var filter = this._filterTypeSelect.get("value");
			if(filter == "ActionType"){
				domStyle.set(this._actionTypeSelect.domNode, "display", "");
				domStyle.set(this._filterBox.domNode, "display", "none");
				domStyle.set(this._statusSelect.domNode, "display", "none");
			}else if(filter == "EventStatus"){
				domStyle.set(this._statusSelect.domNode, "display", "");
				domStyle.set(this._filterBox.domNode, "display", "none");
				domStyle.set(this._actionTypeSelect.domNode, "display", "none");
			}else{
				domStyle.set(this._filterBox.domNode, "display", "");
				domStyle.set(this._statusSelect.domNode, "display", "none");
				domStyle.set(this._actionTypeSelect.domNode, "display", "none");
			}
		},
		_doSearch: function() {
			this.filterField = this._filterTypeSelect.get("value");
			this.filterStr = this._filterBox.get("value");
			this.filterActionType = this._actionTypeSelect.get("value");
			this.filterStatus = this._statusSelect.get("value");
			this.startDate = this._startDatePicker.getValue();
			this.endDate = this._endDatePicker.getValue();
			this._isSearch = true;
			this.createRendering(this.repository, this.item);
		},
		_reset: function() {
			this._startDatePicker.reset();
			this._endDatePicker.reset();
			this._filterTypeSelect.reset();
			this._filterBox.reset();
			this._actionTypeSelect.reset();
			this._statusSelect.reset();
			this._dateRangeError.innerHTML = "";
		},
		_customizeResponseObj: function(response) {
			var cols = response.columns.cells[0];
			if(cols.length > 1) {
				var col0 = new Object();
				col0.name = "";
				col0.widthWebKit = "1.0em";
				col0.width = "1.0em";
				cols[0] = col0;

				cols[1].decorator = undefined;
				cols[1].dataType = "xs:string";
				cols[1].setCellValue = undefined;
				cols[1].widgetsInCell = undefined;
				cols[1].sortable = false;
				cols[4].decorator = dojo_lang.hitch (this, this._decorateStatus);

				// set name of columns
				cols[1].name = this._ierMessages.historyPane_action;
				cols[2].name = this._ierMessages.historyPane_dateTime;
				cols[3].name = this._ierMessages.historyPane_initiator;
				cols[4].name = this._ierMessages.historyPane_status;
			}
			return response;
		},
		_decorateStatus: function(item) {
			if(item == "0"){
				item = this._ierMessages.historyPane_success;
			}else{
				item = this._ierMessages.historyPane_failure;
			}
			return item;
		}
	});
});
