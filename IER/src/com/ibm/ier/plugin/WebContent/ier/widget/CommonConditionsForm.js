define([
    	"dojo/_base/declare",
    	"dojo/_base/array",
    	"dojo/_base/lang",
    	"dojo/_base/json",
    	"dojo/aspect",
    	"dojo/data/ItemFileReadStore",
    	"dojo/dom-class",
    	"dojo/dom-construct",
    	"dojo/dom-style",
    	"dojo/DeferredList",
    	"dojo/_base/Deferred",
    	"dijit/_Widget",
    	"dijit/_TemplatedMixin", 
    	"dijit/_WidgetsInTemplateMixin",
    	"ecm/model/SearchCriterion",
    	"ecm/model/SearchConfiguration",
    	"ecm/model/_searchUtils",
    	"ecm/widget/search/AttributeDefinitionsForm",
    	"ier/constants",
    	"ier/messages",
    	"ier/util/util",
    	"dojo/text!./templates/CommonConditionsForm.html",
    	"dijit/form/Button", // in template
    	"dijit/form/RadioButton", // in template
    	"dijit/form/Select" // in template
], function(dojo_declare, dojo_array, dojo_lang, dojo_json, dojo_aspect, ItemFileReadStore, dom_class, dojo_construct, dojo_style, DeferredList, Deferred, dijit_Widget, dijit_TemplatedMixin, 
		dijit_WidgetsInTemplateMixin, ecm_model_SearchCriterion, SearchConfiguration, utils, ecm_widget_search_AttributeDefinitionsForm,
		ier_constants, ier_messages, ier_util, templateString){

return dojo_declare("ier.widget.CommonConditionsForm", [dijit_Widget, dijit_TemplatedMixin, dijit_WidgetsInTemplateMixin], {

	widgetsInTemplate: true,
	templateString: templateString,
	_messages: ier_messages,

	repository: null,

	/**
	 * The AttributeDefinitionsForm storing all the conditions
	 */
	attributeForm: null,

	/**
	 * Whether to show the content contains dialog and container
	 */
	showContentContains: false,

	/**
	 * The current conditions object that caches and holds all the queries
	 */
	condition: null,

	_contentClassName: null,

	_contentClass: null,  

	_contentTypes: ["CONTENT", "METADATA"],

	readOnly: false,

	postCreate: function(){
		this.inherited(arguments);

		this.connect(this._addPropertyButton, "onClick", "addProperty");

		if(arguments.repository){
			this.setRepository(arguments.repository);
		}
		if(arguments.className){
			this.setClassName(arguments.className);
		}
	},

	setRepository: function(repository) {
		this.repository = repository;
	},

	setClassName: function(contentClassName) {
		if(this.repository){
			this._contentClassName = contentClassName;
			this._contentClass = this.repository.getContentClass(contentClassName);

			if(!this.condition){
				this.condition = {className: contentClassName};
			}
		}
	},

	clear: function() {
		if(this.attributeForm){
			this.attributeForm.destroyRecursive();
		}
		this.attributeForm = null;
		delete this.condition;
		this.condition = null;
	},

	setCondition: function(condition){
		this.condition = condition;
		if(this.repository){
			if(!this.condition){
				this.condition = {className: this._contentClassName};
			}
			this.renderCondition(this.condition);
		}
	},

	getCondition: function(){
		if(!this.condition){
			this.condition = this._contentClassName && {className: this._contentClassName};
		}
		this.saveCondition(this.condition.className);
		return this.condition;
	},

	removeCondition: function(){
		this.condition = null;
	},
	
	/**
	 * If it's a user property and it's a value coming from the old UI, make a call and obtain the real user and place it as values for the criteria.
	 */
	_normalizeUserCriterion: function(isJson, criteria, criterion, callback){
		criterion.selectedOperator = criteria.operator;
		var searchConfig = SearchConfiguration.getSearchConfiguration();
		if (searchConfig.isUserProperty(criteria.name, "p8")){
			if(!isJson){
				this.repository.getUser(criteria.values, dojo_lang.hitch(this, function(user){
					criterion.values = [user];
					callback(criterion);
				}));
			}
			else
				callback(criterion);
		}
		else
			callback(criterion);
	},

	renderCondition: function(condition){
		this.condition = condition;
		var className = condition.className;
		if(condition){
			if(className){
				if(!this.attributeForm){
					this.attributeForm = new ecm_widget_search_AttributeDefinitionsForm({getWidth: function(){
						return 0; // auto resize
					}, onFormValidate: dojo_lang.hitch(this, function(valid){
							this.onFormValidate(valid);
					})});
					this.own(dojo_aspect.after(this.attributeForm, "onFinishLoadingAttributes", dojo_lang.hitch(this, function(){
						this.readOnly && this.setReadOnly();
					})));
					this.own(dojo_aspect.after(this.attributeForm, "onChange", dojo_lang.hitch(this, function(){
						this.removeOperators();
						this.onChange();
					})));
				}
				dojo_construct.place(this.attributeForm.domNode, this._propertyContainer, "only");
				this.onRenderCondition(condition);
				var objectType = (className == ier_constants.ClassName_Record ? "document" : "folder");
				var criteria = [];
				var deferArray = [];
				if(condition.criteria && condition.criteria.length > 0){
					for(var i in condition.criteria){
						var c = condition.criteria[i];
						var conditionDeferred = new Deferred();
						var criterion = new ecm_model_SearchCriterion(c.name);
						var isJsonValues = false;
						//json values are saved only for the new UI.  They are not always available if a hold comes from the old UI.
						if(c.jsonValues){
							var value = dojo_json.fromJson(c.jsonValues);
							criterion.values = value;
							isJsonValues = true;
						}
						else{
							criterion.values = c.values;
						}
						
						deferArray.push(conditionDeferred);
						criterion.deferredObjRef = conditionDeferred;
						this._normalizeUserCriterion(isJsonValues, c, criterion, dojo_lang.hitch(this, function(crit){
							criteria.push(crit);
							crit.deferredObjRef.resolve();	
						}));
					}
				}
				
				var defs = new DeferredList(deferArray);
				defs.then(dojo_lang.hitch(this, function(){
					if(!this._contentClass){
						this._contentClass = this.repository.getContentClass(className);
					}

					this.onFilterCriteria(condition, criteria);
					this.attributeForm.setContentClass(this._contentClass, true, objectType, criteria);

					if(condition.content && this.showContentContains){
						condition.content.type == "CONTENT" && this._contentBox.set("value", condition.content.value);
					}else{
						this._contentBox.set("value", "");
					}
					var contentDisplay = (this.showContentContains && className == ier_constants.ClassName_Record ? "" : "none");
					dojo_style.set(this._contentContainer, "display", contentDisplay);
					
					if(condition.matchAll === false){
						this.matchAnyButton.set("checked", true);
					}else{
						this.matchAllButton.set("checked", true);
					}
					this.condition = condition;
				}));
			}
		}
	},

	/**
	 * Event invoked when this.attributeForm is changed
	 */
	onChange: function(){
	},

	/**
	 * Event invoked when the conditions are being saved
	 */
	onSaveCondition: function(contentClass){
		
	},

	/**
	 * Event invoked when the form is validated
	 */
	onFormValidate: function(valid){
		
	},

	/**
	 * Event called when a conditions is being rendered
	 */
	onRenderCondition: function(condition) {
		
	},

	/**
	 * Event called before criteria are displayed.  It can be used to filter the available criteria
	 */
	onFilterCriteria: function(condition, criteria){
		
	},

	saveCondition: function(className){
		var condition = this.condition;
		if(className){
			var criteria = (this.attributeForm && this.attributeForm.createSearchCriteriaFromAttributeDefintions());
			criteria = dojo_array.filter((criteria || []), function(c){
				if(c.value){
					return true;
				}else{
					var operator = c.selectedOperator;
					return (operator == "NULL" || operator == "NOTNULL"); 
				}
			});
			var contentValue = null;
			if(className == ier_constants.ClassName_Record && this.showContentContains){
				contentValue = this._contentBox.get("value");
			}
			if(criteria.length === 0 && !contentValue){ // empty
				if(condition){
					this.removeCondition();
					this.condition = {className: className};
				}
			}else{
				criteria = dojo_array.map(criteria, function(c){
					return {name: c.id, operator: c.selectedOperator, values: c.values, type:c.dataType};
				});
				var matchAll = !!this.matchAllButton.get("checked");
				if(condition){
					condition.criteria = criteria;
					condition.matchAll = matchAll;
					delete condition.content;
				}else{
					condition = {className: className, criteria: criteria, matchAll: matchAll};
				}
				if(contentValue){
					condition.content = {value: contentValue, type: this._contentTypes[0]};
				}
			}
		}
		this.onSaveCondition();
	},

	addProperty: function(){
		if(this.condition){
			if(this.attributeForm) {
				this.attributeForm.addOneMoreAttributeDefinition();
			}
		}
	},

	setReadOnlyFlag: function(){
		this.readOnly = true;
	},

	removeOperators: function(){
		dojo_array.forEach(this.attributeForm.getChildren(), function(attWidget){
			if(attWidget._operatorSelect){
				attWidget._operatorSelect.store = null;
				var operatioms = dojo_array.filter(utils.getAvailableOperators(attWidget.attributeDefinition), function(oper){
					return oper != "NULL" && oper != "CONTAINS";
				}, this);
				var operatorOptions = utils.getOperatorSelectOptions(operatioms);
				var opStore = new ItemFileReadStore({
					data: {
						identifier: "value",
						label: "label",
						items: operatorOptions
					}
				});
				opStore._forceLoad();
				attWidget._operatorSelect && attWidget._operatorSelect.set("store", opStore);
				if(attWidget._operatorSelect.get("value") == "NULL" || attWidget._operatorSelect.get("value") == "CONTAINS"){
					attWidget._operatorSelect.set("value", operatioms[0]);
				}
			}
		}, this);
	},

	setReadOnly: function(){
		this.readOnly = true;
		dom_class.add(this.domNode, "ierConditionLabelDisabled");
		this._addPropertyButton.set("disabled", true);
		this._contentBox.set("disabled", true);
		this.matchAllButton.set("disabled", true);
		this.matchAnyButton.set("disabled", true);
		if(this.attributeForm){
			var attWidgets = this.attributeForm.getChildren();

			dojo_array.forEach(attWidgets, dojo_lang.hitch(this, function(attWidget){
				if(attWidget._allNamesSelect){
					attWidget._allNamesSelect.set("disabled", true);
				}
				if(attWidget._hoverHelp){
					attWidget._hoverHelp.set("disabled", true);
				}
				if(attWidget._operatorSelect){
					attWidget._operatorSelect.set("disabled", true);
				}
				var fields = attWidget.getFields();
				dojo_array.forEach(fields, dojo_lang.hitch(this, function(f){
					f.set("disabled", true);
				}));
				dojo_style.set(attWidget.controlButtons, "display", "none");
			}));
		}
	}
});});
