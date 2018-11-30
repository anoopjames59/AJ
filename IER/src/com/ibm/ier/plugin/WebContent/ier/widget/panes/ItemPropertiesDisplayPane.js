define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-style",
	"dojo/dom-class",
	"idx/layout/TitlePane",
	"ecm/widget/ItemPropertiesDisplayPane",
	"ecm/Messages",
	"ier/constants",
	"ier/messages",
	"ier/util/property",
	"ier/widget/panes/CommonPropertiesPane"
], function(dojo_declare, dojo_lang, dojo_style, dojo_class, idx_layout_TitlePane, ecm_widget_ItemPropertiesDisplayPane, ecm_messages,
		ier_constants, ier_messages, ier_util_property){

/**
 * @name ier.widget.ItemPropertiesDisplayPane
 * @class Provides a display-only view of the properties of an item.
 * @augments ecm.widget.ItemPropertiesDisplayPane
 */
return dojo_declare("ier.widget.panes.ItemPropertiesDisplayPane", [ ecm_widget_ItemPropertiesDisplayPane ], {
	/** @lends ier.widget.ItemPropertiesDisplayPane.prototype */

	showPreview: false,
	openDispositionPane: false,
	commonPropertiesPaneClass: "ier/widget/panes/CommonPropertiesPane",
	allowEdit: true,
	
	postCreate: function() {
		this.inherited(arguments);
		
		if(this.displayBorderContainer){
			dojo_style.set(this.displayBorderContainer.domNode, "display", "none");
		}
		
		if(this.displayPropertiesTopPanel){
			dojo_style.set(this.displayPropertiesTopPanel.domNode, "display", "none");
		}
		
		if(this.displayPropertiesCenterPanel){
			dojo_style.set(this.displayPropertiesCenterPanel.domNode, "padding", "0");
		}
		
		dojo_class.add(this.domNode, "ierEntityitemDetailsPane");

		this.connect(this.propertiesPane, "onLoaded", function(){
			var item = this.getItem();
			if(item && dojo_lang.isFunction(this._getDispositionProperties) && dojo_lang.isFunction(this._createLabelValue)){
				var contentClass = item.getContentClass();
				contentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(attributeDefinitons, childComponentDefinitions) {
					if(ier_util_property.hasDispositionGroupProperty(item)){
						var propPane = this.propertiesPane;
						var node = propPane.itemPropertiesPane.customPropertiesPane;
						if(propPane._dispositonTitlePane){
							propPane._dispositonTitlePane.destroy();
						}else{
							propPane.connect(propPane, "destroy", function(){
								if(propPane._dispositonTitlePane){
									propPane._dispositonTitlePane.destroy();
								}
							});
							var parent = node.parentNode;
							parent.removeChild(node);
							parent.appendChild(node); // set node as the last child
						}
						var dispositonTitlePane = new idx_layout_TitlePane({
							"class": "propertyGridTitlePane",
							title: ier_messages.itemPropertiesDisplayPane_dispositionProperties,
							open: false
						});	
						propPane._dispositonTitlePane = dispositonTitlePane;
						var pairs = this._getDispositionProperties(attributeDefinitons);
						this._createLabelValue(dispositonTitlePane, pairs);
						node.appendChild(dispositonTitlePane.domNode);
					}
					
					this.resize();
				}));
			}
		});
	},
	
	/**
	 * Determines whether to show a specific attribute definition for the item.
	 * a) not system owned
	   b) not hidden
	   c) not disposition properties
	   d) not vital record properties
	   e) not physical properties 
	   f) not duplicate properties - like FolderName which is the same as RecordCategoryName
	 */
	isItemPropertyShown: function(attributeDefiniton) {
		return !attributeDefiniton.hidden && !attributeDefiniton.system && !ier_util_property.isDispositionGroupProperty(attributeDefiniton.id) &&
		!ier_util_property.isVitalRecordGroupProperty(attributeDefiniton.id) &&
		!ier_util_property.isEntityItemNotDisplayedProperty(attributeDefiniton.id);
	},
	
	/**
	 * Determines whether the current item is read-only. The editing capabilities for this properties display pane
	 * will be turned off if true is returned.
	 * 
	 * @param item
	 *            The item to be displayed
	 * @since 2.0.2
	 */
	isItemReadOnly: function(item) {
		return (this.allowEdit && !item.privModifyProperties)
				|| item instanceof ier.model.DispositionSchedule
				|| item.template == ier_constants.ClassName_DispositionAction
				|| item.template == ier_constants.ClassName_DispositionTrigger
				|| item.template == ier_constants.ClassName_ReportDefinition;
	},

	onCompleteRendering: function() {
		var item = this.getItem();
		if(item){
			var contentClass = item.getContentClass();
			contentClass.retrieveAttributeDefinitions(dojo_lang.hitch(this, function(attributeDefinitons, childComponentDefinitions) {
				if(ier_util_property.hasDispositionGroupProperty(item)){
					var dispositonTitlePane = new idx_layout_TitlePane({
						"class": "propertyGridTitlePane",
						title: ier_messages.itemPropertiesDisplayPane_dispositionProperties,
						open: this.openDispositionPane
						});	
					var pairs = this._getDispositionProperties(attributeDefinitons);
					this.addCustomTitlePane(dispositonTitlePane, pairs);
				}
				
				this.resize();
			}));
		}
		
		var titlePanes = this.getTitlePanes();
		if(titlePanes){
			for ( var i in titlePanes) {
				var titlePane = titlePanes[i];
				if (titlePane) {
					if(titlePane.title == ier_messages.itemPropertiesDisplayPane_dispositionProperties){
						this._toggleTitlePane(titlePane, this.openDispositionPane);
					}
					
					if(titlePane.title == ecm_messages.system_properties_title){
						this._toggleTitlePane(titlePane, this.showSystemProps);
					}
					
					//toggles the desktop cache
					this.connect(titlePane, "toggle", function(){
						if(titlePane.title == ier_messages.itemPropertiesDisplayPane_dispositionProperties)
							this.openDispositionPane = !this.openDispositionPane;
						
						if(titlePane.title == ecm_messages.system_properties_title)
							this.showSystemProps = !this.showSystemProps;
						
					});
				}
			}
		}
	},
	
	_toggleTitlePane: function(titlePane, open){
		if(open){
			if (!titlePane.open)
				titlePane.toggle();
		}
		else{
			if (titlePane.open)
				titlePane.toggle();
		}
	},

	// get the list of disposition properties
	_getDispositionProperties: function(attributeDefinitions) {
		var item = this.getItem();
		var isDDContainer = false;
		if(item && item.isInstanceOf && item.isInstanceOf(ier.model.RMContainerMixin) && item.isDefensibleDisposal()){
			isDDContainer = true;
		}
		
		var pairs = [];
		for ( var i in attributeDefinitions) {
			var attributeDefinition = attributeDefinitions[i];
			if((isDDContainer && ier_util_property.isBasicScheduleGroupProperty(attributeDefinition.id)) || 
				(!isDDContainer && ier_util_property.isDispositionGroupProperty(attributeDefinition.id))) { 
				var value = this.getItemAttributeValue(attributeDefinition);
				var name = attributeDefinition.name;
				
				if(attributeDefinition.id == ier_constants.Property_RMRetentionPeriod){
					value = item.getDefensibleDisposalSchedule().getRMRetentionPeriod("display");
					name = ier_messages.entityItemDispositionPane_retentionPeriod;
				}
				
				if(attributeDefinition.id == ier_constants.Property_RMRetentionTriggerPropertyName){
					name = ier_messages.retentionTriggerPropertyName;
				}
				
				pairs.push([ name, value, attributeDefinition.id ]);
			}
		}
		return pairs;
	},

	/**
	 * Modify item's properties display values
	 */
	getItemAttributeValue: function(attributeDefinition) {
		var id = attributeDefinition.id;
		var item = this.getItem();

		var value = this.inherited(arguments);
		
		if(id == ier_constants.Property_RetainMetadata){
			value = value === "0" ? ecm_messages.true_label : value === "1" ? ecm_messages.false_label : value;
		}
		
		//for object values, obtain the name
		if(attributeDefinition.dataType == "xs:object"){
			value = item.getDisplayValue(id);
		}
		
		return value;
	}
});});
