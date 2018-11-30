/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"dojo/dom-class",
	"dojo/dom-style",
	"dijit/layout/ContentPane",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"dijit/focus",
	"ecm/widget/UnselectableFolder",
	"ier/messages",
	"ier/widget/FolderSelector",
	"dojo/text!./templates/MultipleFoldersDropDown.html"
	], function(dojo_declare, dojo_lang, dojo_connect, dojo_domClass, dojo_domStyle, ContentPane, _TemplatedMixin, _WidgetsInTemplateMixin, dijit_focus, UnselectableFolder, 
			ier_messages, FolderSelector, template) {
/**
 */
return dojo_declare("ier.widget.MultipleFoldersDropDown", [
	ContentPane,
	_TemplatedMixin,
	_WidgetsInTemplateMixin], {
	
	templateString: template,
	widgetsInTemplate: true,
	
	repository: null,
	
	unselectableFolders: null,

	/**
	 * Constructor
	 */
	constructor: function() {
		this.unselectableFolders = [];
	},

	postCreate: function() {
		this.inherited(arguments);
				
		this._folderSelector = new FolderSelector({
			preventSelectRoot: true,
			showIncludeSubfolders: false,
			selectRootInitially: false,
			"style": "inline"
		});
		this._folderSelector.startup();

		dojo_domStyle.set(this._folderSelector.domNode, "height", "350px");
		dojo_domStyle.set(this._folderSelector.domNode, "width", "300px");
		
		this.connect(this._folderSelector, "onFolderSelect", function(folder){
			this.addUnselectableFolder(folder);
			this.onFolderSelected(folder);
		});
		
		this._folderSelector.setRoot(this.repository);
		this.containerNode.appendChild(this._folderSelector.domNode);
	},
	
	/**
	 * Add an unselectable folder for the folder selector
	 */
	addUnselectableFolder: function(item){
		this.unselectableFolders.push(new UnselectableFolder(item.id, false));
		this._folderSelector.setUnselectableFolders(this.unselectableFolders);
	},
	
	removeUnselectableFolder: function(item){
		for(var i in this.unselectableFolders){
			if(this.unselectableFolders[i].id == item.id){
				this.unselectableFolders.splice(i);
			}
		}
		this._folderSelector.setUnselectableFolders(this.unselectableFolders);
	},
	
	focus: function(){
		this._folderSelector.selectRootNode();
	},
	
	resize: function(){
	    if(this._folderSelector)
		    this._folderSelector.resize();
		    
		this.inherited(arguments);
	},
	
	/**
	 * Event invoked when a folder is selected
	 */
	onFolderSelected: function(item){
	}
});});
