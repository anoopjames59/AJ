define([
	"dojo/_base/array",
	"dojo/_base/declare",
	"dojo/dom-class",
	"dijit/form/Select",
	"ecm/model/SearchConfiguration",
	"ecm/widget/search/SearchTab",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/widget/listView/gridModules/RowContextMenu",
	"ier/widget/listView/modules/DocInfo",
	"ier/widget/listView/modules/FilePlanToolbar"
], function(array, declare, dom_class, Select, SearchConfiguration, SearchTab,
	constants, messages, util, RowContextMenu, DocInfo, FilePlanToolbar){

return declare("ier.widget.search.SearchTab", SearchTab, {

	postCreate: function(){
		this.inherited(arguments);
		dom_class.add(this.searchContainerNode, "ierCenterPane");
		this.connect(this.searchForm, "onSearchCriteriaLoad", function(){
			var node = this.searchForm.textSearchFormInputArea.parentNode;
			if(node){
				if(this.searchTemplate.objectType == SearchConfiguration.prototype.OBJECT_TYPE.DOCUMENT){
					var text = this.searchForm._textSearchTexts && this.searchForm._textSearchTexts[0];
					text && text.set("disabled", true);
					this.repository.retrieveAssociatedContentRepositories(function(items){
						if(array.some(items || [], function(item){
							return !!item.textSearchType;
						})){
							text && text.set("disabled", false);
						}
					});
				}else{
					dom_class.toggle(node, "dijitHidden", true);
				}
			}
		});

		this.connect(this.searchResults, "setGridExtensionModules", function(){
			var modules = this.searchResults.getGridExtensionModules();
			util.replaceModule(modules, "rowContextMenu", RowContextMenu);
		});
		this.connect(this.searchResults, "setContentListModules", function(){
			var modules = this.searchResults.getContentListModules();
			util.replaceModule(modules, "toolbar", FilePlanToolbar);
			util.replaceModule(modules, "rightPane", {moduleClass: DocInfo, showPreview: false});
		});
	}

});
});
