define([
	"dojo/_base/array",
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/aspect",
	"dojo/dom-class",
	"dojo/dom-construct",
	"dijit/form/Select",
	"ecm/model/SearchConfiguration",
	"ecm/widget/search/SearchBuilder",
	"ecm/widget/search/SearchInDropDown",
	"ier/constants",
	"ier/messages",
	"ier/model/SearchTemplate",
	"ier/util/util",
	"ier/widget/listView/gridModules/RowContextMenu",
	"ier/widget/listView/modules/DocInfo",
	"ier/widget/listView/modules/FilePlanToolbar"
], function(array, declare, lang, aspect, dom_class, dom_construct, Select, SearchConfiguration, SearchBuilder, SearchInDropDown,
	constants, messages, SearchTemplate, util, RowContextMenu, DocInfo, FilePlanToolbar) {

var type2Label = {};
type2Label[constants.ClassName_Record] = messages.record;
type2Label[constants.ClassName_RecordCategory] = messages.recordCategory;
type2Label[constants.ClassName_RecordFolder] = messages.recordFolder;
type2Label[constants.ClassName_Volume] = messages.volume;

var type2NameProp = {};
type2NameProp[constants.ClassName_Record] = constants.Property_DocumentTitle;
type2NameProp[constants.ClassName_RecordCategory] = constants.Property_RecordCategoryName;
type2NameProp[constants.ClassName_RecordFolder] = constants.Property_RecordFolderName;
type2NameProp[constants.ClassName_Volume] = constants.Property_VolumeName;

var nameProp2Type = {};
nameProp2Type[constants.Property_DocumentTitle] = constants.ClassName_Record;
nameProp2Type[constants.Property_RecordCategoryName] = constants.ClassName_RecordCategory;
nameProp2Type[constants.Property_RecordFolderName] = constants.ClassName_RecordFolder;
nameProp2Type[constants.Property_VolumeName] = constants.ClassName_Volume;

var defColumns = [constants.Property_LastModifier, constants.Property_DateLastModified];

return declare("ier.widget.search.SearchBuilder", SearchBuilder, {

	postCreate: function() {
		// customize search definition
		var searchBuilder = this;
		lang.mixin(this.searchDefinition, {
			createSearchTemplate: function(params){
				var searchTemplate = new SearchTemplate(params);
				searchTemplate.application = constants.Search_application;
				return searchTemplate;
			},
			getSearchConfiguration: function(){
				var searchConfig = SearchConfiguration.getSearchConfiguration({repository: this.repository});
				return lang.mixin(searchConfig, {
					getRootClassId: function(objectType){
						return searchBuilder._searchType || constants.ClassName_Record;
					},
					getDefaultAttributeDefinitionId: function(objectType){
						return type2NameProp[this.getRootClassId(objectType)];
					},
					getNameProperty: function(objectType){
						return this.getDefaultAttributeDefinitionId(objectType);
					},
					getSearchDefaultColumns: function(objectType){
						return [this.getNameProperty(objectType)].concat(defColumns);
					},
					isTextSearchTypeSupported: function(textSearchType){
						return true;
					}
				});
			}
		});
		this.own(aspect.before(this.searchDefinition, "setRepository",  function(repository){
			delete searchBuilder._searchType; // for default search type to be used on reset
		}));
		this.connect(this.searchDefinition, "setRepository", function(repository){
			var searchTemplate = this.searchDefinition.searchTemplate;
			var contentClass = searchTemplate.className && searchTemplate.getSearchContentClass();
			if(contentClass){
				if(contentClass.allAttributeDefinitions){
					this._initSearchType(contentClass.allAttributeDefinitions);
					this._initTextSearch();
				}else{
					contentClass.retrieveAttributeDefinitionsForSearches(lang.hitch(this, function(ads){
						this._initSearchType(ads);
						this._initTextSearch();
					}));
				}
			}else{
				this._initTextSearch();
			}
			this.connect(this.searchDefinition.contentClassSelector, "onContentClassSelected", this._initTextSearch);
		});
		var text = this.searchDefinition._textSearchText;
		if(text){
			var resultsDisplayOptions = this.searchDefinition.resultsDisplayOptions;
			this.connect(text, "onKeyUp", function(){
				var searchTemplate = this.searchDefinition.searchTemplate;
				var resultsDisplay = resultsDisplayOptions.getResultsDisplay();
				if(searchTemplate && searchTemplate.isNew() && resultsDisplay && !resultsDisplay.saved){ // not saved, that is, still with default options
					if(text.get("value")){
						if(resultsDisplay.sortBy != resultsDisplayOptions.RANK){
							// default to sort by rank
							resultsDisplay.sortBy = resultsDisplayOptions.RANK;
							resultsDisplay.sortAsc = false;
						}
					}else{
						if(resultsDisplay.sortBy == resultsDisplayOptions.RANK){
							// reset to sort by name property
							resultsDisplayOptions.processSearchResultsDisplay(resultsDisplay, false);
						}
					}
				}
			});
		}
		this.searchDefinition._disableTextSearchText = function(){}; // text search is controlled by _initTextSearch()

		// customize search-in drop-down
		this.searchDefinition.searchInDropDown.validateRepository = function(repository){
			if(repository.isIERLoaded()){
				return repository.isFilePlanRepository();
			}else{
				var valid = false;
				var searchInDropDown = null;
				repository.loadIERRepository(function(r){
					valid = r && r.isFilePlanRepository();
					if(valid && searchInDropDown){ // validateRepository() might have already returned false
						var store = searchInDropDown.repositorySelect && searchInDropDown.repositorySelect.store;
						var index = array.indexOf(searchInDropDown.getAvailableRepositoriesForSelection(), repository);
						if(store && index >= 0){
							store.newItem({value: index, label: repository.name});
						}
					}
				});
				searchInDropDown = this;
				return valid;
			}
		};
		this.searchDefinition.searchInDropDown.setRoot = function(root){
			root && root.retrieveItem(constants.Id_RecordsManagementFolder, lang.hitch(this, function(item){
				SearchInDropDown.prototype.setRoot.call(this, item);
				this.repositorySelect.onChange = lang.hitch(this, function(repositoryId){
					var repository = this.getAvailableRepositoriesForSelection()[repositoryId];
					repository && repository.retrieveItem(constants.Id_RecordsManagementFolder, lang.hitch(this, function(item){
						this._rootObject = item;
						this._folderSelector.setRoot(item);
						this.repositoryTeamspaceName = repository.name;
					}));
				});
			}));
		};

		// customize search class selector
		this.own(aspect.before(this.searchDefinition.contentClassSelector, "setRepository", function(repository){
			if(repository){
				for(var type in type2Label){
					var contentClass = repository.getContentClass(type);
					if(contentClass && contentClass.name == contentClass.id){
						contentClass.name = type2Label[type];
					}
				}
			}
			this.allowMultipleClasses = false;
		}));
		this.own(aspect.before(this.searchDefinition.contentClassSelector, "setSelected", function(contentClasses, multiple){
			this.allowMultipleClasses = false;
		}));

		// create search type select
		this._searchType = constants.ClassName_Record;
		this._searchTypeSelect = new Select({"class": "ierSearchTypeSelect", options: [
			{value: constants.ClassName_Record, label: messages.record, selected: true},
			{value: constants.ClassName_RecordCategory, label: messages.recordCategory},
			{value: constants.ClassName_RecordFolder, label: messages.recordFolder},
			{value: constants.ClassName_Volume, label: messages.volume}
		]});
		var node = this.searchDefinition.searchOptionContainer;
		dom_construct.create("label", {"for": this._searchTypeSelect.id, "class": "ierSearchTypeLabel",
			innerHTML: messages.search_for + ":"}, node);
		this._searchTypeSelect.placeAt(node);
		this.connect(this._searchTypeSelect, "onChange", function(){
			this._searchType = this._searchTypeSelect.get("value");
			var objectType = (this._searchType == constants.ClassName_Record ?
				SearchConfiguration.prototype.OBJECT_TYPE.DOCUMENT : SearchConfiguration.prototype.OBJECT_TYPE.FOLDER);
			var options = this.searchDefinition._moreOptions.getSelectedOptions() || {};
			if(objectType != options.objectType){
				options.objectType = objectType;
				this.searchDefinition._moreOptions.setSelectedOptions(options);
			}
			this.searchDefinition.contentClassSelector.defaultToFirstItem = true;
			this.searchDefinition.contentClassSelector.setRootClassId(this._searchType);
		});

		this.inherited(arguments);
		dom_class.add(this.searchContainerNode, "ierCenterPane");
	},

	getContentListModules: function(){
		var modules = this.inherited(arguments);
		util.replaceModule(modules, "toolbar", FilePlanToolbar);
		util.replaceModule(modules, "rightPane", {moduleClass: DocInfo, showPreview: false});
		return modules;
	},

	getContentListGridModules: function(){
		var modules = this.inherited(arguments);
		util.replaceModule(modules, "rowContextMenu", RowContextMenu);
		return modules;
	},

	_initSearchType: function(ads){
		var searchType;
		if(array.some(ads || [], function(ad){
			return !!(searchType = nameProp2Type[ad.id]);
		}) && searchType != this._searchType){
			this._searchType = searchType;
			this._searchTypeSelect.set("value", searchType, false); // do not call onChange
			var contentClassSelector = this.searchDefinition.contentClassSelector;
			if(contentClassSelector && contentClassSelector.rootClassId && contentClassSelector.rootClassId != searchType){
				contentClassSelector.setRootClassId(searchType);
			}
		}
	},

	_initTextSearch: function(){
		var node = this.searchDefinition.textSearchContainer.parentNode;
		if(node){
			var resultsDisplayOptions = this.searchDefinition.resultsDisplayOptions;
			resultsDisplayOptions.setTextSearchEnabled(false);
			if(!this._searchType || this._searchType == constants.ClassName_Record){
				var text = this.searchDefinition._textSearchText;
				text && text.set("disabled", true);
				this._textSearchRepository = this.searchDefinition.repository || this.repository;
				this._textSearchRepository.retrieveAssociatedContentRepositories(function(items){
					var hasVerity = array.some(items || [], function(item) {return item.textSearchType == constants.Search_CBRType_Verity;});
					var hasCascade = array.some(items || [], function(item) {return item.textSearchType == constants.Search_CBRType_Cascade;});
					var optimizationNotSupported = array.some(items || [], function(item) 
							{return (item.textSearchOptimization == constants.Search_CBR_Dynamic_Switching &&
									 item.textSearchRankOverride == constants.Search_CBR_QueryRankOverride_Required);});

					if (!optimizationNotSupported && ((hasVerity && !hasCascade) || (!hasVerity && hasCascade))) {
						text && text.set("disabled", false);
						resultsDisplayOptions.setTextSearchEnabled(true);
					}
				});
				dom_class.toggle(node, "dijitHidden", false);
			}else{
				dom_class.toggle(node, "dijitHidden", true);
			}
		}
	}

});
});
