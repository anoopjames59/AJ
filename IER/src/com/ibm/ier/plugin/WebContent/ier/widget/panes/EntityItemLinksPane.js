define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/array",
	"dojo/_base/event",
	"dojo/keys",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ecm/model/Request",
	"ier/model/ResultSet",
	"ier/widget/dialog/IERBaseDialogPane",
	"dijit/layout/BorderContainer",
	"dojo/text!./templates/EntityItemLinksPane.html",
	"dijit/layout/ContentPane",	// in content
	"ier/widget/listView/ContentList",	// in content
	"ier/widget/FilePlanSearchBar"	// in content
], function(dojo_declare, dojo_lang, dojo_array, dojo_event, dojo_keys, ier_constants, ier_messages, ier_util, ecm_model_Request, ier_model_ResultSet, ier_widget_dialog_IERBaseDialogPane, BorderContainer, templateString) {
	return dojo_declare("ier.widget.panes.EntityItemLinksPane", [BorderContainer, ier_widget_dialog_IERBaseDialogPane], {
		templateString: templateString,
		
		postCreate: function() {
			this.inherited(arguments);
			this.connect(this, "onKeyDown", function(evt){
				if(evt.keyCode == dojo_keys.ENTER){
					dojo_event.stop(evt);
				}
			});
		},
		
		isLoaded: function() {
			return this._isLoaded;
		},
		
		createRendering: function(repository, item) {
			this.logEntry("createRendering");
			
			this.repository = repository;
			this.item = item;
			this._updateContentList();
			this.logExit("createRendering");
		},
		
		_updateContentList: function(filterString) {
			this.logEntry("_updateContentList");
			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){
				response = this._customizeResponseObj(response);
				response.repository = this.repository;
				var resultSet = new ier_model_ResultSet(response);
				this._contentList.setResultSet(resultSet);
				this._contentList.resize();
				this._isLoaded = true;
			}));
			params.requestParams[ier_constants.Param_EntityId] = this.item.getGuidId();
			params.requestParams[ier_constants.Param_IsContainer] = this.item.isFolder();
			params.requestParams[ier_constants.Param_FilterString] = filterString;
			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_RetrieveLinks, ier_constants.PostEncoding, params);
			
			this.logExit("_updateContentList");
		},
		
		_customizeResponseObj: function(response) {
			var cols = response.columns.cells[0];
			if(cols.length > 1) {
				//console.info(cols);
				cols[0].formatter = this._formatEntityType;
				cols[0].decorator = undefined;
				cols[0].name = this._messages.entityItemLinksPane_label_EntityType;
				cols[0].field = "ClassName";
				cols[0].width = "15em";
				cols[0].sortable = true;
				cols[2].name = this._messages.entityItemLinksPane_label_LinkTo;
				cols.pop();
				var rows = response.rows;
				var myGuidId = this.item.getGuidId();
				for(var i=0; i<rows.length; i++) {
					var row = rows[i];
					if(myGuidId == row.attributes.Head[0]) {
						row.attributes.Head = row.attributes.Tail;
					} else {
						row.attributes.Tail = row.attributes.Head;
					}
				}
			}
			return response;
		},
		
		_filterContentOnEnter: function(evt) {
			if(evt.keyCode == dojo_keys.ENTER){
				this._updateContentList(filterString);
			}
		},
		
		_emtpyFormatter: function(item) {
			return "";
		},
		
		_formatEntityType: function(item) {
			return item["ClassName"];
		}
	});
});