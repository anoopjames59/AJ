define([
    	"dojo/_base/declare",
    	"dojo/dom-class",
    	"dojox/collections/ArrayList",
    	"ecm/model/Desktop",
    	"ecm/widget/RepositorySelector"
], function(dojo_declare, dojo_class, dojox_collections_ArrayList, ecm_model_desktop, ecm_widget_RepositorySelector){

/**
 * @name ier.widget.FilePlanRepositorySelector
 * @class
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget.FilePlanRepositorySelector", [ ecm_widget_RepositorySelector ], {
	/** @lends ecm.widget.RepositorySelector.prototype */

	_filePlanRepositories: [],
	
	postCreate: function() {
		this.inherited(arguments);

		if (this.repositoryTypes != null && this.repositoryTypes.length > 0) {
			this.repositoryTypes = new dojox_collections_ArrayList(this.repositoryTypes.split(","));
		}

		dojo_class.add(this.domNode, "ecmRepositorySelector");
	},
	
	/*_setIdAttr: function(value){
		if (this._dropdown) {
			dojo.attr(this._dropdown, "id", value);
		} else {
			this.inherited(arguments);
		}
	},*/

	setFilePlanRepositories: function(repositories){
		this._filePlanRepositories = repositories;
		
		if (ecm_model_desktop.desktopLoaded) {
			this._createRendering();
		} else {
			this.connect(ecm_model_desktop, "onDesktopLoaded", "_createRendering");
		}
	},

	getRepositories: function() {
		return this._filePlanRepositories;
	}
});});
