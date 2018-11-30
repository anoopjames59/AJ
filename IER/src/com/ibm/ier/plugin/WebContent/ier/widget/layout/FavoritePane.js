define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",
	"ecm/model/Desktop",
	"ecm/model/Favorite",
	"ecm/widget/ActionMenu",
	"ecm/widget/FavoritesTree",
	"ecm/widget/layout/FavoritesPane",
    "ecm/widget/listView/gridModules/RowContextMenu",
	"ecm/widget/listView/modules/Toolbar",
	"ier/util/menu",
	"ier/util/util",
	"ier/widget/layout/_SearchPaneMixin",
	"ier/widget/listView/modules/DocInfo",
	"ier/widget/listView/modules/FilePlanToolbar"
], function(declare, lang, dojo_dom_class, desktop, Favorite, ActionMenu, FavoritesTree, FavoritesPane, RowContextMenu, Toolbar,
	menu, util, _SearchPaneMixin, DocInfo, FilePlanToolbar){
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

var _Toolbar = declare(FilePlanToolbar, {

	createToolButtons: function(){
		this._actionMenuCreator = new _ActionMenu();
		this.inherited(arguments);
	},

	getParentFolder: function(){
		var parentFolder = this.inherited(arguments);
		if(parentFolder && parentFolder.isIERFavorite){
			parentFolder = parentFolder.item;
		}
		return parentFolder;
	},

	loadContextMenu: function(selectedItems, callback) {
		desktop.loadMenuActions(menu.getFavoriteContextMenuType(selectedItems), callback);
	},

	onToolbarButtonsCreated: function(){
		if(!this.getParentFolder()){ // root
			Toolbar.prototype.onToolbarButtonsCreated.apply(this, arguments);
		}else{
			this.inherited(arguments);
		}
	},

	updateToolbarState: function(){
		if(!this.getParentFolder()){ // root
			Toolbar.prototype.updateToolbarState.apply(this, arguments);
		}else{
			this.inherited(arguments);
		}
	}

});

var _RowContextMenu = declare(RowContextMenu, {

	loadContextMenu: function(selectedItems, callback) {
		this._actionMenuCreator = new _ActionMenu();
		desktop.loadMenuActions(menu.getFavoriteContextMenuType(selectedItems), callback);
	}

});

return declare(FavoritesPane, {

	constructor: function(){
		var getMimeClass = Favorite.prototype.getMimeClass;
		lang.extend(Favorite, {
			getMimeClass: function(){
				var iconClass = util.getIconClass(this);
				if(!iconClass && !this.item && this.objectId){
					// need to load real item for showing appropriate icon
					if(this.repository){
						_SearchPaneMixin.prototype._adaptRepository.call(this, this.repository);
					}
					this.retrieveFavorite(lang.hitch(this, function(){
						desktop.onFavoriteUpdated([this]);
					}));
				}
				return iconClass || getMimeClass.call(this);
			},
			isIERFavorite: true
		});

		var getIconClass = FavoritesTree.prototype.getIconClass;
		var _onContextMenu = FavoritesTree.prototype._onContextMenu;
		lang.extend(FavoritesTree, {
			getIconClass: function(item){
				var iconClass = null;
				if(item){
					iconClass = (item.isIERFavorite ? item.getMimeClass() : util.getIconClass(item));
				}
				return iconClass || getIconClass.call(this, item);
			},
			getActionsMenuType: function(item){
				return menu.getFavoriteContextMenuType([item]);
			},
			_onContextMenu: function(evt){
				this._actionMenuCreator = new _ActionMenu();
				_onContextMenu.call(this, evt);
			}
		});
	},

	postCreate: function(){
		this.inherited(arguments);
		dojo_dom_class.add(this.domNode, "ierCenterPane");
	},

	getContentListModules: function(){
		var modules = this.inherited(arguments);
		util.replaceModule(modules, "toolbar", _Toolbar);
		util.replaceModule(modules, "rightPane", {moduleClass: DocInfo, showPreview: false});
		return modules;
	},

	getContentListGridModules: function(){
		var modules = this.inherited(arguments);
		util.replaceModule(modules, "rowContextMenu", _RowContextMenu);
		return modules;
	}

});
});
