define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ecm/model/_ModelObject"
], function(dojo_declare, dojo_lang, ecm_model_ModelObject){

/**
 * @name ier.model.RecentQuickSearch
 * @class Represents a recent quick search on the folder tree
 * @augments ecm.model._ModelObject
 */
return dojo_declare("ier.model.RecentQuickSearch", [ecm_model_ModelObject], {
	/** @lends ecm.model._ModelObject.prototype */

	/*
	 * data for the recent quick search
	 */
	data: null,
	
	searchQueryString: null,
	
	repository: null,

	/**
	 * Constructor.  All the values are automatically set and inherited from the parent class.
	 */
	constructor: function(arguments) {
		this.itemsClicked = [];
	},
	
	getRepository: function(){
		return this.repository;
	},
	
	setRepository: function(repository){
		this.repository = repository;
	},
	
	addClickedItem: function(docId, onComplete){
		var item = this.getItemClicked(docId);
		if(!item){
			this.repository.retrieveItem(docId, dojo_lang.hitch(this, function(item){
				item.idPrefix = this.searchQueryString;
				this.itemsClicked.push(item);
				if(onComplete){
					onComplete(item);
				}
			}));
		}
		else {
			if(onComplete)
				onComplete(item);
		}
	},
	
	getItemClicked: function(id){
		for(var i in this.itemsClicked){
			var item = this.itemsClicked[i];
			if(item.id == id)
				return item;
		}
	},
	
	getItemsClicked: function(){
		return this.itemsClicked;
	},
	
	hasChildren: function(){
		return(this.itemsClicked.length > 0);
	}
});});
