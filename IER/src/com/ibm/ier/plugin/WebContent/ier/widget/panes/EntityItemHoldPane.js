define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/model/ResultSet",
	"ecm/model/Request",
	"ier/widget/dialog/IERBaseDialogPane",
	"dojo/text!./templates/EntityItemHoldPane.html",
	"ier/widget/listView/ContentList"
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, ier_util, ier_model_ResultSet, ecm_model_Request, ier_widget_dialog_IERBaseDialogPane, templateString) {
	return dojo_declare("ier.widget.panes.EntityItemHoldPane", [ier_widget_dialog_IERBaseDialogPane], {
		templateString: templateString,
		
		isLoaded: function() {
			return this._isLoaded;
		},
		
		createRendering: function(repository, item) {
			this.logEntry("createRendering");

			this.repository = repository;
			this.item = item;

			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response){
				if(response){
					response.repository = this.repository;
					var resultSet = new ier_model_ResultSet(response);
					var structure = resultSet.structure;
					if(structure){
						var cells = structure.cells && structure.cells[0];
						if(cells){
							cells.splice(4, 1); 
						}
					}
					this._contentList.setResultSet(resultSet);
					this.resize();
					this._isLoaded = true;
				}
			}));

			params.requestParams[ier_constants.Param_EntityId] = this.item.id;
			params.requestParams[ier_constants.Param_Type] = this.item.ierContentType;

			ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, ier_constants.Service_GetHoldsForEntity, ier_constants.PostEncoding, params);

			this.logExit("createRendering");
		},

		resize: function() {
			this._contentList.resize();
		}
	});
});