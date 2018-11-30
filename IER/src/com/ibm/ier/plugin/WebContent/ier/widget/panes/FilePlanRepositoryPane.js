define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"ecm/widget/layout/_RepositorySelectorMixin",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"dojo/text!./templates/FilePlanRepositoryPane.html"
], function(dojo_declare, dojo_lang, ecm_widget_layout_RepositorySelectorMixin,
		ier_widget_dialog_IERBaseDialogPane, templateString){

/**
 * General pane for file plan dialog
 */
return dojo_declare("ier.widget.panes.FilePlanRepositoryPane", [ier_widget_dialog_IERBaseDialogPane, ecm_widget_layout_RepositorySelectorMixin], {

	templateString: templateString,

	setRepository: function(repository) {
		this.repository = repository;
		if(repository)
			this.repositorySelector.getDropdown().set("value", repository.id);
	},

	/**
	 * Loads the content of this pane.
	 */
	loadContent: function() {
		this.logEntry("loadContent");

		this.logExit("loadContent");
	},

	/**
	 * Renders the pane
	 * @param repository
	 */
	createRendering: function(repository, item, isReadOnly) {
		this.logEntry("createRendering");

		this.createRepositorySelector();
		this.setRepositoryTypes("p8");
		this.doRepositorySelectorConnections();
		this.addChildWidget(this.repositorySelector);

		//this handles retrieving the default layout repository, login, and setting it as the repository if no repository has been set yet.
		this.setPaneDefaultLayoutRepository();

		// If there is more than one repository in the list, show the selector to the user.
		if(this.repositorySelector.getNumRepositories() > 1){
			this._repositorySelectorArea.appendChild(this.repositorySelector.domNode);
		}

		if(repository){
			this.setRepository(repository);
		}

		if(item || isReadOnly){
			this.repositorySelector.getDropdown().set("disabled", true);
		}

		this.logExit("createRendering");
	},

	/**
	 * Destroys the widget
	 */
	destroy: function(){
		if(this.repositorySelector)
			this.repositorySelector.destroyRecursive();
		
		this.inherited(arguments);
	},

	_nop: null
});});