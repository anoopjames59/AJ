define ([
"dojo/_base/declare",
"dojo/_base/lang",
"ecm/model/SearchTemplate",
"ier/util/util",
"ecm/model/Request",
"ecm/model/ResultSet",
"ier/constants"
], function (declare, dojo_lang, ecm_model_searchtemplate, ier_util, ecm_model_Request, ecm_model_ResultSet, ier_constants) {
	var SearchTemplate = declare("ier.model.SearchTemplate", [ecm_model_searchtemplate], {
		
		/**
		 * Perform a search using the (filled in) search template.
		 * 
		 * @param callback
		 *            A callback function to be called after the search has been performed. Passes a
		 *            {@link ecm.model.ResultSet} as a parameter.
		 * @param sortProperty
		 *            A string value holding the property name to used as the sort property
		 * @param descending
		 *            A flag indicating the sort direction (ie. ascending or descending)
		 */
		search: function(callback, sortProperty, descending) 
		{	
			// uncomment this line and comment the rest of code to use ICN search implementation.
			//this.inherited(arguments);
			
			var params = ier_util.getDefaultParams(this.repository, dojo_lang.hitch(this, function(response) {	
							this._searchCompleted(response, callback);
			}));
	
			params.requestParams["templateID"] = this.id;
			params.requestParams["vsId"] = this.vsId || "";
			params.requestParams["criterias"] = this.getQueryString();			
			if(sortProperty){
				params.requestParams.order_by = sortProperty;
				params.requestParams.order_descending = descending || false;
			}
			params["requestBody"] = this.searchCriteria ? this.toJson(true) : "{}";
			
			var request = ecm_model_Request.postPluginService(ier_constants.ApplicationPlugin, 
					"ierSearch", ier_constants.PostEncoding, params);
			
			return request;
		},
	
		_searchCompleted: function(response, callback) {
			response.repository = this.repository;
			response.parentFolder = this;
			response.searchTemplate = this;

			var results = new ecm.model.ResultSet(response);
			results._moreOptions = this.moreOptions;
			if (callback) {
				callback(results);
			}
		}

	});

	SearchTemplate.createFromJSON = function(json, repository, resultSet, parent){
		dojo_lang.mixin(json, {
			repository: repository,
			resultSet: resultSet,
			parent: parent
		});

		if(json.id){
			var attributes = {};
			var attributeTypes = {};
			var attributeFormats = {};
			var attributeDisplayValues = {};
			var attributeReadOnly = {};
			var attributeItems = {};
			for(var i in json.attributes){
				var attr = json.attributes[i];
				attributes[i] = attr[0];
				if(attr.length > 1){
					attributeTypes[i] = attr[1];
				}
				if(attr.length > 2){
					attributeFormats[i] = attr[2];
				}
				if(attr.length > 3){
					attributeDisplayValues[i] = attr[3];
				}
				attributeReadOnly[i] = (attr.length > 4 && attr[4] === true);
				if (attr.length > 5 && attr[5] != null && attr[5].rows) {
					attributeItems[i] = ecm.model.ContentItem.createFromJSON(attr[5].rows[0], this.repository, null);
				}
			}

			dojo_lang.mixin(json, {
				attributes: attributes,
				attributeTypes: attributeTypes,
				attributeFormats: attributeFormats,
				attributeDisplayValues: attributeDisplayValues,
				attributeReadOnly: attributeReadOnly,
				attributeItems: attributeItems
			});
		}else{
			dojo_lang.mixin(json, {
				id: ["StoredSearch", json.repository_id, json.template_id].join(","), // generate model ID
				name: json.template_label,
				description: json.template_desc
			});
		}

		return new SearchTemplate(json);
	};

	return SearchTemplate;
});
