define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dojo/aspect",
    	"dojo/dom-style",
    	"gridx/modules/SingleSort",
    	"ecm/model/SearchClass",
    	"ecm/model/SearchConfiguration",
    	"ecm/model/admin/AdminConfig",
    	"ecm/widget/dialog/MessageDialog",
    	"ier/constants",
    	"ier/util/dialog",
    	"ier/messages",
    	"ier/model/SearchTemplate",
    	"ier/widget/dialog/IERBaseDialogPane",
    	"ier/widget/listView/gridModules/RowContextMenu",
    	"dojo/text!./templates/HoldConditionPane.html",
    	"dijit/form/Button", // in template
    	"dijit/layout/TabContainer", // in template
    	"dijit/layout/ContentPane", // in template
    	"ier/widget/CommonConditionsForm", // in template
    	"idx/layout/TitlePane", // in template
    	"ecm/widget/listView/ContentList" // in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_aspect, dojo_style, SingleSort,
		ecm_model_SearchClass, ecm_model_SearchConfiguration,
		ecm_model_admin_AdminConfig, ecm_dialog_MessageDialog,
		ier_constants, ier_util_dialog, ier_messages, SearchTemplate, ier_widget_dialog_IERBaseDialogPane, RowContextMenu, templateString){

return dojo_declare("ier.widget.panes.HoldConditionPane", [ier_widget_dialog_IERBaseDialogPane], {

	templateString: templateString,
	_messages: ier_messages,
	_valid1: true,
	_valid2: true,
	_valid3: true,
	_valid4: true,
	_isContentSearchEnabled: true,
	_hasContent: false,

	repository: null,

	_classNames: [ier_constants.ClassName_Record, ier_constants.ClassName_RecordCategory,
		ier_constants.ClassName_RecordFolder, ier_constants.ClassName_Volume],

	_recordDisplayColumn: ["{NAME}", ier_constants.Property_LastModifier, ier_constants.Property_DateLastModified],

	postCreate: function(){
		this.inherited(arguments);

		this.connect(this._previewButton1, "onClick", this._preview1);
		this.connect(this._previewButton2, "onClick", this._preview2);
		this.connect(this._previewButton3, "onClick", this._preview3);
		this.connect(this._previewButton4, "onClick", this._preview4);

		this.own(dojo_aspect.after(this.commonConditionsForm1, "onRenderCondition", dojo_lang.hitch(this, function(condition){
			this._setDefaultAtt(condition, this.commonConditionsForm1, ier_constants.Property_DocumentTitle);
		}), true));
		this.own(dojo_aspect.after(this.commonConditionsForm2, "onRenderCondition", dojo_lang.hitch(this, function(condition){
			this._setDefaultAtt(condition, this.commonConditionsForm2, ier_constants.Property_RecordCategoryName);
		}), true));
		this.own(dojo_aspect.after(this.commonConditionsForm3, "onRenderCondition", dojo_lang.hitch(this, function(condition){
			this._setDefaultAtt(condition, this.commonConditionsForm3, ier_constants.Property_RecordFolderName);
		}), true));
		this.own(dojo_aspect.after(this.commonConditionsForm4, "onRenderCondition", dojo_lang.hitch(this, function(condition){
			this._setDefaultAtt(condition, this.commonConditionsForm4, ier_constants.Property_VolumeName);
		}), true));

		this.own(dojo_aspect.after(this._conditionTabContainer, "_transition", dojo_lang.hitch(this, function(newTab, oldTab){
			this._changeOldTabTitle(oldTab);
			this._changeNewTabTitle(newTab);
		}), true));

		this.own(dojo_aspect.after(this.commonConditionsForm1, "onFormValidate", dojo_lang.hitch(this, function(valid){ this._valid1 = valid; this.onInputChange(); }), true),
				 dojo_aspect.after(this.commonConditionsForm2, "onFormValidate", dojo_lang.hitch(this, function(valid){ this._valid2 = valid; this.onInputChange(); }), true),
				 dojo_aspect.after(this.commonConditionsForm3, "onFormValidate", dojo_lang.hitch(this, function(valid){ this._valid3 = valid; this.onInputChange(); }), true),
				 dojo_aspect.after(this.commonConditionsForm4, "onFormValidate", dojo_lang.hitch(this, function(valid){ this._valid4 = valid; this.onInputChange(); }), true)
		);

		// hide toolbar and breadcrumb on the content list
		dojo_style.set(this._contentList1.topContainer.domNode, "display", "none");
		dojo_style.set(this._contentList2.topContainer.domNode, "display", "none");
		dojo_style.set(this._contentList3.topContainer.domNode, "display", "none");
		dojo_style.set(this._contentList4.topContainer.domNode, "display", "none");
		this._contentList1.setGridExtensionModules([SingleSort, RowContextMenu]);
		this._contentList2.setGridExtensionModules([SingleSort, RowContextMenu]);
		this._contentList3.setGridExtensionModules([SingleSort, RowContextMenu]);
		this._contentList4.setGridExtensionModules([SingleSort, RowContextMenu]);
	},

	_setDefaultAtt: function(condition, commonConditionsForm, defaultProperty){
		var attributeDefinitionFormWid = commonConditionsForm.attributeForm;
		attributeDefinitionFormWid.setDefaultAttributeDefinitionId(defaultProperty);
	},

	_changeNewTabTitle: function(newTab){
		var title = "";
		if(newTab.recordInfo){
			title = ier_messages.record;
		}else if(newTab.recordCategory){
			title = ier_messages.recordCategory;
		}else if(newTab.recordFolder){
			title = ier_messages.recordFolder;
		}else if(newTab.recordVolume){
			title = ier_messages.recordVolume;
		}
		newTab.set("title", title);
	},

	_changeOldTabTitle: function(oldTab){
		var conditionCount = 0;
		var oldCondition = oldTab.getChildren()[0].getCondition();
		if(oldCondition){
			if(oldCondition.criteria){
				conditionCount += oldCondition.criteria.length;
			}
			oldCondition.content ? conditionCount++ : conditionCount;
		}
		var title = "";
		if(oldTab.recordInfo){
			title = ier_messages.record + " (" + conditionCount + ")";
		}else if(oldTab.recordCategory){
			title = ier_messages.recordCategory + " (" + conditionCount + ")";
		}else if(oldTab.recordFolder){
			title = ier_messages.recordFolder + " (" + conditionCount + ")";
		}else if(oldTab.recordVolume){
			title = ier_messages.recordVolume + " (" + conditionCount + ")";
		}
		oldTab.set('title', title);
	},

	_setRepositoryAttr: function(repository){
		this.repository = repository;

		if(repository){
			this.commonConditionsForm1.setRepository(repository);
			this.commonConditionsForm2.setRepository(repository);
			this.commonConditionsForm3.setRepository(repository);
			this.commonConditionsForm4.setRepository(repository);
			this._initTextSearch();
		}
	},

	_setCondition: function(commonConditionsForm, condition, tab, entityTypeName){
		var conditionCount = 0;
		commonConditionsForm.setCondition(condition);
		if(condition.criteria){
			conditionCount += condition.criteria.length;
		}
		condition.content ? conditionCount++ : conditionCount;
		tab.set('title', entityTypeName + " (" + conditionCount + ")");
	},

	_setConditionsAttr: function(conditions){
		var flag = {record: true, recordCategory:true, recordFolder:true, recordVolume:true};
		if(conditions && conditions.length > 0){
			dojo_array.forEach(conditions, dojo_lang.hitch(this, function(con){
				if(con && con.className == this._classNames[0]){
					this._setCondition(this.commonConditionsForm1, con, this._recordTab, ier_messages.record);
					this._checkContent(con);
					flag.record = false;
				}else if(con && con.className == this._classNames[1]){
					this._setCondition(this.commonConditionsForm2, con, this._recordCategoryTab, ier_messages.recordCategory);
					flag.recordCategory = false;
				}else if(con && con.className == this._classNames[2]){
					this._setCondition(this.commonConditionsForm3, con, this._recordFolderTab, ier_messages.recordFolder);
					flag.recordFolder = false;
				}else if(con && con.className == this._classNames[3]){
					this._setCondition(this.commonConditionsForm4, con, this._recordVolumeTab, ier_messages.recordVolume);
					flag.recordVolume = false;
				}
			}));
		}
		if(this.repository){
			if(flag.record){
				this._setEmptyCondition(this.commonConditionsForm1, this._recordTab, this._classNames[0], ier_messages.record);
			}
			if(flag.recordCategory){
				this._setEmptyCondition(this.commonConditionsForm2, this._recordCategoryTab, this._classNames[1], ier_messages.recordCategory);
			}
			if(flag.recordFolder){
				this._setEmptyCondition(this.commonConditionsForm3, this._recordFolderTab, this._classNames[2], ier_messages.recordFolder);
			}
			if(flag.recordVolume){
				this._setEmptyCondition(this.commonConditionsForm4, this._recordVolumeTab, this._classNames[3], ier_messages.recordVolume);
			}
		}
	},
	_setEmptyCondition: function(commonConditionsForm, tab, className, entityTypeName){
		commonConditionsForm.setClassName(className);
		commonConditionsForm.renderCondition(commonConditionsForm.getCondition());
		tab.set('title', entityTypeName + " (0)");
	},
	_getConditionsAttr: function(){
		var conditions = [];
		var condition = null;
		if(this.commonConditionsForm1){
			condition = this.commonConditionsForm1.getCondition();
			if(condition && (condition.criteria || condition.content)){
				condition.criteriaJson = this._getJsonCondition(condition, this._classNames[0], this.commonConditionsForm1);
				conditions.push(condition);
			}
		}
		if(this.commonConditionsForm2){
			condition = this.commonConditionsForm2.getCondition();
			if(condition && condition.criteria){
				condition.criteriaJson = this._getJsonCondition(condition, this._classNames[1], this.commonConditionsForm2);
				conditions.push(condition);
			}
		}
		if(this.commonConditionsForm3){
			condition = this.commonConditionsForm3.getCondition();
			if(condition && condition.criteria){
				condition.criteriaJson = this._getJsonCondition(condition, this._classNames[2], this.commonConditionsForm3);
				conditions.push(condition);
			}
		}
		if(this.commonConditionsForm4){
			condition = this.commonConditionsForm4.getCondition();
			if(condition && condition.criteria){
				condition.criteriaJson = this._getJsonCondition(condition, this._classNames[3], this.commonConditionsForm4);
				conditions.push(condition);
			}
		}
		return conditions;
	},

	_getJsonCondition: function(condition, className, commonConditionsForm){
		var criteria = commonConditionsForm.attributeForm.createSearchCriteriaFromAttributeDefintions();
		var template = this._getSearchTemplate(condition, criteria, className);
		return template.toJson(true);
	},

	_preview1: function(){
		this._preview(this._classNames[0], this.commonConditionsForm1, this._resultPane1, this._contentList1);
	},
	_preview2: function(){
		this._preview(this._classNames[1], this.commonConditionsForm2, this._resultPane2, this._contentList2);
	},
	_preview3: function(){
		this._preview(this._classNames[2], this.commonConditionsForm3, this._resultPane3, this._contentList3);
	},
	_preview4: function(){
		this._preview(this._classNames[3], this.commonConditionsForm4, this._resultPane4, this._contentList4);
	},

	_emptyCondition : function(condition){
		var conditionCount = 0;
		if(condition){
			if(condition.criteria){
				conditionCount += condition.criteria.length;
			}
			condition.content ? conditionCount++ : conditionCount;
		}
		return conditionCount > 0 ? false : true;
	},

	_getSearchTemplate: function(condition, criteria, className){
		var contentClass = this.repository.getContentClass(className);
		var config = ecm_model_SearchConfiguration.getSearchConfiguration({repository: this.repository});
		var template = new SearchTemplate({id: "", name: "", repository: this.repository});
		var objectType = config.OBJECT_TYPE.FOLDER;
		if(className == ier_constants.ClassName_Record){
			objectType = config.OBJECT_TYPE.DOCUMENT;
		}
		var includeSubclasses = true;
		template.objectType = objectType;
		template.setSearchContentClass(contentClass);
		template.includeSubclasses = includeSubclasses;
		var searchClass = new ecm_model_SearchClass({id: contentClass.id, name: contentClass.name,
				objectType: objectType, searchSubclasses: includeSubclasses});
		template.setClasses([searchClass]);
		var objectStore = {id: this.repository.objectStoreName,
				symbolicName: this.repository.objectStoreName,
				displayName: this.repository.objectStoreDisplayName};
		template.objectStores = [objectStore];
		template.folders = []; // TODO: file plan?
		var matchAll = condition.matchAll;
		template.andSearch = matchAll;
		template.searchCriteria = criteria;
		if(className == ier_constants.ClassName_Record && this._isContentSearchEnabled && condition && condition.content && condition.content.type == "CONTENT"){
			var textSearchCriteria = [];
			var textSearch = {"operator":"any", "text":condition.content.value}
			textSearchCriteria.push(textSearch);
			template.textSearchCriteria = textSearchCriteria;
		}

		var columns = dojo_lang.clone(config.getSearchDefaultColumns(objectType));
		if(className == ier_constants.ClassName_Record){
			columns = this._recordDisplayColumn;
		}
		var resultsDisplay = {sortBy: config.getNameProperty(objectType), sortAsc: true, columns: columns};
		if(columns[0] == ecm_model_admin_AdminConfig.PSEUDO_NAME){
			columns[0] = resultsDisplay.sortBy;
		}else if(columns[0] != resultsDisplay.sortBy){
			columns.unshift(resultsDisplay.sortBy);
		}
		dojo_array.forEach(criteria, function(crit){ // add search criteria to result display columns
			var c = crit.id;
			if(c && dojo_array.indexOf(columns, c) < 0){
				columns.push(c);
			}
		});
		template.resultsDisplay = resultsDisplay;
		return template;
	},

	_preview: function(className, commonConditionsForm, resultPane, contentList){

		var condition = commonConditionsForm.getCondition();
		if(this._emptyCondition(condition)){
			var messageDialog = new ecm_dialog_MessageDialog({
				text: ier_messages.holdConditionPane_empty_ondition_message
			});
			messageDialog.startup();
			messageDialog.show();
			ier_util_dialog.manage(messageDialog);
			return;
		}
		var criteria = commonConditionsForm.attributeForm.createSearchCriteriaFromAttributeDefintions();
		var template = this._getSearchTemplate(condition, criteria, className);
		template.isIERHoldCondition = true;

		var request = template.search(dojo_lang.hitch(this, function(resultSet){
			resultPane.set("open", true);
			var structure = resultSet.structure;
			if(structure){
				var cells = structure.cells && structure.cells[0];
				if(cells && cells[2]){
					cells[2].width = "16em";
					cells[2].widthWebKit = "16em";
					cells.splice(0, 1);
				}
			}
			contentList.setResultSet(resultSet);
		}));
	},

	_initTextSearch: function(){
		var _this = this;
		this.repository.retrieveAssociatedContentRepositories(function(items){
			var hasVerity = dojo_array.some(items || [], function(item) {return item.textSearchType == ier_constants.Search_CBRType_Verity;});
			var hasCascade = dojo_array.some(items || [], function(item) {return item.textSearchType == ier_constants.Search_CBRType_Cascade;});
			var optimizationNotSupported = dojo_array.some(items || [], function(item) 
					{return (item.textSearchOptimization == ier_constants.Search_CBR_Dynamic_Switching &&
							 item.textSearchRankOverride == ier_constants.Search_CBR_QueryRankOverride_Required);});

			if(optimizationNotSupported || ((!hasVerity && !hasCascade) || (hasVerity && hasCascade))){
				_this._isContentSearchEnabled = false;
				_this.commonConditionsForm1._contentBox.set("disabled", true);
				_this._hasContent && _this._previewButton1.set("disabled", true)
			}
		});
	},

	_checkContent: function(con){
		this._hasContent = con && con.content && con.content.type == "CONTENT";
		this._hasContent && !this._isContentSearchEnabled && this._previewButton1.set("disabled", true);
	},

	setReadOnly: function(){
		this.commonConditionsForm1.setReadOnly();
		this.commonConditionsForm2.setReadOnly();
		this.commonConditionsForm3.setReadOnly();
		this.commonConditionsForm4.setReadOnly();
	},

	isValid: function(){
		return this._valid1 && this._valid2 && this._valid3 && this._valid4;
	},

	onInputChange: function(){
	}
});});
