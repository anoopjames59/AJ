define([
    	"dojo/_base/declare",
    	"dojo/_base/lang",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/dom-class",
    	"dojo/store/Memory",
    	"dijit/registry",
    	"ecm/widget/UnselectableFolder",
    	"ecm/widget/FolderSelectorCallback",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"ier/model/DefensibleDisposalSchedule",
    	"dojo/text!./templates/ConvertDefensibleDisposalDispositionPane.html",
    	"ier/widget/panes/DefensibleDisposalDispositionPane", // in template
    	"ier/widget/search/SearchInDropDown"
], function(dojo_declare, dojo_lang, dojo_domConstruct, dojo_style, dojo_class, dojo_store_Memory, dijit_registry, ecm_UnselectableFolder, ecm_FolderSelectorCallback,
		IERBaseDialogPane, ier_constants, ier_messages, ier_util, DefensibleDisposalSchedule, templateString){

/**
 * @name ier.widget.panes.ConvertDefensibleDisposalDispositionPane
 * @class Provides an interface to convert to a defensible disposal schedule
 * @augments ier.widget.dialog.IERBaseDialogPane
 */
return dojo_declare("ier.widget.panes.ConvertDefensibleDisposalDispositionPane", [IERBaseDialogPane], {
	/** @lends ier.widget.panes.ConvertDefensibleDisposalDispositionPane.prototype */

	templateString: templateString,
	widgetsInTemplate: true,
	constants: ier_constants,
	messages: ier_messages,
	
	folder: null,
	
	postCreate: function(){
		this.title = ier_messages.convertDefensibleDisposalDispositionPane_title;
		this.connect(this.defensibleDisposalPane, "onInputChange", "onInputChange");
		
		dojo_style.set(this.defensibleDisposalPane.containerNode, "margin-top", "0px");
		dojo_style.set(this.defensibleDisposalPane.ddScheduleSectionTable, {
			"margin-top": "0px",
			"width": "100%",
			"margin-left": "0px"
		});
	},
	
	/**
	 * Creates the rendering for this pane
	 */
	createRendering: function(repository, item){
		
		// Set permission to check for folder add.
		var folderSelectorCallback = new ecm_FolderSelectorCallback	("privCanConvertToDDContainer", ier_messages.convertNotEligibleForDDConversion);
		this.folderSelector.setIsSelectableCallback(folderSelectorCallback.isSelectableByPermission, folderSelectorCallback);
		
		this.connect(this.folderSelector, "onFolderSelected", function(folder){
			this.folder = folder.item;
			this.repository = this.folderSelector.repository;
			
			this._loadDDPane();
		});
		
		if(!item){	
			this.folderSelector.setRoot(ecm.model.desktop.getAuthenticatingRepository());
		}
		else {
			this.folder = item;
			this.repository = repository;
			this.folderSelector.setRoot(this.repository);
			this.folderSelector.setSelected(this.folder);
		}
	},
	
	_loadDDPane: function(){
		if(this.repository.isIERLoaded()){
			this.defensibleDisposalPane.createRendering({
				repository: this.repository
			});
			this.onInputChange();
		}
		else {
			this.repository.loadIERRepository(dojo_lang.hitch(this, function(){
				this.defensibleDisposalPane.createRendering({
				repository: this.repository
				});
				this.onInputChange();
			}));
		}
	},
	
	validate: function(){
		if(this.folder == null || !this.defensibleDisposalPane.validate()){
			return false;
		}
			
		return true;
	},
	
	/**
	 * Retrieves the defensible schedule item set in this pane
	 */
	_getScheduleAttr: function() {
		return this.defensibleDisposalPane.get("schedule");
	},
	
	setFolder: function(item){
		this.folderSelector.setSelected(item);
	},
	
	/**
	 * Sets the defensible schedule item for this pane
	 */
	_setScheduleAttr: function(schedule) {
		this.defensibleDisposalPane.set("schedule", schedule);
	}
});});
