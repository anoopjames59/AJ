/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

define([
	"dojo/_base/declare",
	"ecm/widget/search/SearchInDropDown",
	"ecm/widget/FolderSelector",
	"ier/widget/FolderSelector"
], function(dojo_declare, ecm_widget_SearchInDropDown, ecm_widget_FolderSelector, ier_widget_FolderSelector) {

/**
 * @name ecm.widget.search.SearchInDropDown
 * @class Provides an interface to select the scope of a search.
 * @augments dijit._WidgetBase
 */
return dojo_declare("ier.widget.search.SearchInDropDown", [ecm_widget_SearchInDropDown], {
	/**
	 * Only allow repositories to be selected under the same domain
	 */
	allowOnlySameDomainRepositories: false,
	
	useIERFolderSelector: false,

	/**
	 * Returns all the repositories available for selection.
	 *
	 * @public
	 * @return an array of repositories
	 */
	getAvailableRepositoriesForSelection: function() {
		if(this.allowOnlySameDomainRepositories){
			var repositories = [];
			for(var i in ecm.model.desktop.repositories){
				var repository = ecm.model.desktop.repositories[i];
				if(repository.serverName == this.repository.serverName)
					repositories.push(repository);
			}
			return repositories;
		}
		else
			return ecm.model.desktop.repositories;
	},
	
	/**
	 * Creates the folder selector control.
	 * 
	 * @public
	 * @param params
	 *            The parameters used to create the folder selector
	 */
	createFolderSelector: function(params) {
		var methodName = "createFolderSelector";
		this.logEntry(methodName);

		if(this.useIERFolderSelector)
			return new ier_widget_FolderSelector(params);
		else
			return new ecm_widget_FolderSelector(params);

		this.logExit(methodName);
	}
});});
