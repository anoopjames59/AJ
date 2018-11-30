/**
 * Licensed Materials - Property of IBM (C) Copyright IBM Corp. 2012 US Government Users Restricted Rights - Use,
 * duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/_base/connect",
	"dojo/_base/array",
	"dojo/aspect",
	"dojo/dom-class",
	"dojo/dom-style",
	"dojo/string",
	"dijit/_TemplatedMixin",
	"dijit/_WidgetsInTemplateMixin",
	"dijit/layout/ContentPane",
	"dijit/popup",
	"dijit/focus",
	"ecm/model/SelectedFolder",
	"ecm/widget/DropDownDialog",
	"ecm/widget/CompositeButton",
	"ier/widget/MultipleFoldersDropDown",
	"ier/messages",
	"ier/util/util",
	"dojo/text!./templates/MultipleFoldersSelector.html",
	"dijit/form/Button" //template
], function(dojo_declare, dojo_lang, connect, array, dojo_aspect, domClass, domStyle, dojo_string, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin, dijit_layout_ContentPane, 
		dijit_popup, dijit_focus, SelectedFolder, DropDownDialog, CompositeButton, MultipleFoldersDropDown, ier_messages, ier_util, template) {
/**
 * @name ecm.widget.UserGroupSelector
 * @class Provides a widget that is used to select a user or group. This widget can be configured to display both
 *        users and groups or only users. The widget can also be configured to support multiple selections or only
 *        single selections.
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget.MultipleFoldersSelector", [dijit_layout_ContentPane, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin], {
	templateString: template,
	widgetsInTemplate: true,
	repository: null,
	_selectedItems: null,
	messages: ier_messages,
	
	disabled: false,
	
	constructor: function(){
		this._selectedItems = {};
	},
	
	postCreate: function(){
		this.button.set("disabled", this.disabled);
		
		this.connect(this.button, "onClick", function(){
        	dijit_popup.open({
				parent: this,
				popup: this.multiFoldersSelectorDialog,
				around: this.button.domNode,
				onCancel: dojo_lang.hitch(this, function() {
					dijit_popup.close(this.multiFoldersSelectorDialog);
					this.button.focus();
				})
			});
	    });
		
		this.connect(ecm.model.desktop, "onDisplayStatusDialog", dojo_lang.hitch(this, function(){
			this._pagingInProgress = true;
		}));
		
		this.connect(ecm.model.desktop, "onHideStatusDialog", dojo_lang.hitch(this, function(){
			this._pagingInProgress = false;
		}));
	},
	
	createRendering: function(repository){
		this.repository = repository;
		
		this.multiFoldersSelectorDropDown = new MultipleFoldersDropDown({
			repository: this.repository
		});
		this.multiFoldersSelectorDialog = new DropDownDialog({
			style: "height: 100%;",
			onMouseLeave:dojo_lang.hitch(this, function(evt) {
				if(!this._pagingInProgress)
					this.multiFoldersSelectorDialog.onCancel();
			}),
			onOpen: dojo_lang.hitch(this, function(evt){
				this.multiFoldersSelectorDropDown.resize();
				this.multiFoldersSelectorDropDown.focus();
			}),
			onClickCancel: dojo_lang.hitch(this, function(evt) {
				this.multiFoldersSelectorDialog.onCancel();
			}),
			onClickApply: dojo_lang.hitch(this, function(evt) {
				this.multiFoldersSelectorDialog.onCancel();
			}),
			onClickOK: dojo_lang.hitch(this, function(evt) {
				this.multiFoldersSelectorDialog.onCancel();
			})});
		this.multiFoldersSelectorDialog.set("content", this.multiFoldersSelectorDropDown);
		
		this.connect(this.multiFoldersSelectorDropDown, "onFolderSelected", function(item){
			this.addSelectedFolder(item);
		});
	},
	
	addSelectedFolder: function(item){
		if(item && item.isSelectable != false && this._selectedItems[item.id] == null){
			item.isSelectable = false;
			
			this._triggerTreeItemOnChange(item);
			
			var folderButton = null;
			var selectedFolder = new SelectedFolder(item, false, this.repository, true);
			folderButton = new CompositeButton({
				item: item,
				iconClass: ier_util.getIconClass(item),
				iconAltText: item.name,
				label: item.name,
				tooltip: selectedFolder.path,
				actionIconClass: "removeIcon",
				actionAltText: dojo_string.substitute(ier_messages.objectSelector_remove, [item.name]),
				onActionButtonClick: dojo_lang.hitch(this, function(){
					this.removeSelectedFolder(folderButton.item);
					folderButton.destroy();
				}),
				onTitleClick: dojo_lang.hitch(this, function(){
					ecm.model.desktop.getActionsHandler()["actionIERProperties"](this.repository, [item]);
				}),
				style: "margin-right:3px; margin-bottom: 3px;",
				"class": "ierObjectSelector noFocus dijitInline"
			});
			this._selectedFoldersSection.appendChild(folderButton.domNode);
			this.multiFoldersSelectorDropDown.addUnselectableFolder(item);
			this._selectedItems[item.id] = item;
			this.onFolderAdded(item);
			this.onChange(item);
			this.multiFoldersSelectorDialog.onCancel();
		}
	},
	
	removeSelectedFolder: function(item){
		if(item && this._selectedItems[item.id] != null){
			this.multiFoldersSelectorDropDown.removeUnselectableFolder(item);
			item.isSelectable = true;
			this._selectedItems[item.id] = null;
			delete this._selectedItems[item.id];
			this.onFolderRemoved(item);
			this.onChange(item);
			this._triggerTreeItemOnChange(item);
		}
	},
	
	_triggerTreeItemOnChange: function(item){
		//a hack to cause the folder selector to reload and set the non selectable row class
		/*if(this.multiFoldersSelectorDropDown._folderSelector && this.multiFoldersSelectorDropDown._folderSelector._folderTree){
			if(this.multiFoldersSelectorDropDown._folderSelector._folderTree.treeModel){
				this.multiFoldersSelectorDropDown._folderSelector._folderTree.treeModel.onChange(item);
			}
		}*/
	},
	
	destroy: function(){
		if(this.multiFoldersSelectorDialog)
			this.multiFoldersSelectorDialog.destroy();
		
		if(this.multiFoldersSelectorDropDown)
			this.multiFoldersSelectorDropDown.destroy();
		
		this.inherited(arguments);
	},
	
	_getDisabledAttr: function(){
		return this.disabled;
	},
	
	_setDisabledAttr: function(disabled){
		this.button.set("disabled", disabled);
	},
	
	/**
	 * Event invoked when a folder is added
	 */
	onFolderAdded: function(item){
		
	},
	
	/**
	 * Event invoked when a folder is removed
	 */
	onFolderRemoved: function(item){
		
	},
	
	/**
	 * Event invoked whenever there's a change to the selection of folders.
	 */
	onChange: function(item){
		
	},
	
	/**
	 * Return all the selected items in a map
	 */
	getSelectedItems: function(){
		return this._selectedItems;
	},
	
	_setValueAttr: function(value){
		var ids = value.split(",");
		for(var i in ids){
			var id = ids[i];
			this.repository.retrieveItem(id, dojo_lang.hitch(this, function(itemRetrieved){
				this.addSelectedFolder(itemRetrieved);
			}));
		}
	},
	
	getSelectedNames: function(){
		var items = [];
		for(var i in this._selectedItems){
			var item = this._selectedItems[i];
			items.push(item.name);
		}
		return ier_util.arrayToString(items);
	},
	
	/**
	 * Return a comma divded list of the P8 Guide Id of all the selected items
	 */
	_getValueAttr: function(){
		var items = [];
		for(var i in this._selectedItems){
			var item = this._selectedItems[i];
			items.push(ier_util.getGuidId(item.id));
		}
		return ier_util.arrayToString(items);
	}
});});
