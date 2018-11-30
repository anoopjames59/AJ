define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dijit/_TemplatedMixin", 
	"dijit/_WidgetsInTemplateMixin",
	"dijit/layout/BorderContainer",
	"ecm/model/Desktop",
	"ier/messages",
	"ier/model/admin/Config",
	"ier/util/dialog",
	"dojo/text!./templates/RepositoryPane.html",
	"dijit/layout/ContentPane", // template
	"dijit/layout/TabContainer", // template
	"ier/widget/admin/DisplayColumnPane", // template
	"ier/widget/admin/DisplayPropertyPane", // template
	"ier/widget/admin/RepositorySecurityPane", // template
	"ier/widget/admin/RepositoryReportPane", // template
	"ier/widget/admin/RepositoryDDSweepPane" // template
], function(declare, lang, _TemplatedMixin, _WidgetsInTemplateMixin, BorderContainer, Desktop, messages, Config, util_dialog, RepositoryPane_html){
return declare("ier.widget.admin.RepositoryPane", [BorderContainer, _TemplatedMixin, _WidgetsInTemplateMixin], {

	templateString: RepositoryPane_html,
	gutters: false,

	repositoryId: "",
	dirty: false,

	_messages: messages,

	buildRendering: function(){
		this.inherited(arguments);

		this._titleNode.innerHTML = messages.admin_repository_label + ": <b>" + this.title + "</b>";
	},

	startup: function(){
		this.inherited(arguments);

		var repository = Desktop.getRepository(this.repositoryId);
		if(repository){
			if(repository.isIERLoaded()){
				this._load(repository);
			}else{
				repository.loadIERRepository(lang.hitch(this, function(repository){
					this._load(repository);
				}));
			}
		}else{
			this._abort();
		}
	},

	_load: function(repository){
		if(repository && repository.isFilePlanRepository()){
			Config.getConfig("repository", repository.id, lang.hitch(this, function(config){
				this._settingsPane.repository = repository;
				this._settingsPane.set("config", config);
				this.connect(this._settingsPane, "onChange", this._validate);
				this._displayColumnPane.repository = repository;
				this._displayColumnPane.set("config", config);
				this._displayPropertyPane.repository = repository;
				this._displayPropertyPane.set("config", config);
				this._systemPropertyPane.repository = repository;
				this._systemPropertyPane.set("config", config);
				this._securityPane.setRepository(repository);
				this._reportPane.setRepository(repository);
				this._reportPane.set("config", config);
				this._ddSweepPane.setRepository(repository);
				this._ddSweepPane.set("config", config);
			}));
		}else{
			this._abort();
		}
	},

	_abort: function(){
		util_dialog.showMessage(messages.no_fileplans_available);
		var parent = this.getParent();
		if(parent && parent.closeChild){
			parent.closeChild(this);	
		}
	},

	_validate: function(dirty){
		this.dirty = dirty;
		this.onChange(dirty);
	},

	onChange: function(dirty){
	}

});
});
