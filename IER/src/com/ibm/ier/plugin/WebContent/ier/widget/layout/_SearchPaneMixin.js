define([
	"dojo/_base/declare",
	"ecm/widget/ActionMenu"
], function(declare, ActionMenu){

var _ActionMenu = declare(ActionMenu, {

	createMenu: function(actions){
		if(actions){
			for(var i = 0; i < actions.length; i++){
				var a = actions[i];
				if(a.id == "Link" || a.id == "SendEmailForSearches"){
					actions.splice(i, 1);
					i--;
				}
			}
		}
		return this.inherited(arguments);
	}

});

return declare(null, {

	setRepository: function(repository){
		this.inherited(arguments);
		this._adaptRepository(this.repository);
	},

	getDefaultLayoutRepository: function(){
		return this._adaptRepository(this.inherited(arguments));
	},

	/**
	 * Promote IER search template methods to ICN search template methods.
	 */
	_adaptRepository: function(repository){
		if(repository && repository.retrieveIERSearchTemplates && repository.retrieveIERSearchTemplate){
			repository.retrieveSearchTemplates = repository.retrieveIERSearchTemplates;
			repository.retrieveSearchTemplate = repository.retrieveIERSearchTemplate;
		}
		return repository;
	},

	_createSearchSelector: function(){
		this.inherited(arguments);
		if(this.searchSelector){
			this.searchSelector._actionMenuCreator = new _ActionMenu();
		}
	}

});
});
