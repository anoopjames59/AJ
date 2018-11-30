define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/aspect",
	"dojo/dom-class",
	"ecm/model/ContentClass",
	"ecm/model/Request",
	"ecm/widget/layout/SearchPane",
	"ier/messages",
	"ier/util/dialog",
	"ier/widget/layout/_SearchPaneMixin",
	"ier/widget/QuickSearchList", // tab
	"ier/widget/search/SearchBuilder", //tab
	"ier/widget/search/SearchTab" // tab
], function(declare, lang, aspect, dom_class, ContentClass, Request, SearchPane, messages, util_dialog, _SearchPaneMixin){

/**
 * @name ier.widget.layout.SearchPane
 * @class Provides the IER search pane allowing you to do new searches via a search builder or quick searches
 * @augments ecm.widget.layout.SearchPane
 */
return declare("ier.widget.layout.SearchPane", [SearchPane, _SearchPaneMixin], {

	constructor: function(){
		// work around for ICN defect not to send "searches" filter_type when includeSubclassDefinitions is false
		var retrieveAttributeDefinitionsForSearches = ContentClass.prototype.retrieveAttributeDefinitionsForSearches;
		lang.extend(ContentClass, {
			retrieveAttributeDefinitionsForSearches: function(){
				var signal = aspect.before(Request, "invokeService", function(serviceName, serverType, params){
					if(params){
						params.filter_type = "searches";
					}
				});
				try{
					retrieveAttributeDefinitionsForSearches.apply(this, arguments);
				}finally{
					if(signal && signal.remove){
						signal.remove();
					}
				}
			}
		});
	},

	buildRendering: function(){
		this.inherited(arguments);

		dom_class.add(this.domNode, "ierSearchPane");
	},

	postCreate: function(){
		this.tabs["ierQuickSearchList"] = { contentClass: "ier.widget.QuickSearchList"};
		this.tabs["searchbuilder"] = {contentClass: "ier.widget.search.SearchBuilder"};
		this.tabs["search"] = {contentClass: "ier.widget.search.SearchTab"};

		this.inherited(arguments);

		this.connect(this, "onRepositoryChange", function(pane, repository){
			if(repository){
				if(repository.isIERLoaded()){
					this._validateRepository(repository);
				}else{
					repository.loadIERRepository(lang.hitch(this, function(repository){
						this._validateRepository(repository);
					}));
				}
			}
		});
	},

	_validateRepository: function(repository){
		if(repository && repository.isFilePlanRepository()){
			this._filePlanRepository = repository;
		}else{
			util_dialog.showMessage(messages.no_fileplans_available);
			repository = this._filePlanRepository || this.getDefaultLayoutRepository();
			if(repository && repository.isFilePlanRepository()){
				this.repositorySelector.getDropdown().set("value", repository.id);
			}
		}
	}

});
});
