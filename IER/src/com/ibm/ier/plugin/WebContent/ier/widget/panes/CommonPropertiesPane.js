define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-class",
	"dojo/dom-style",
	"ecm/widget/CommonPropertiesPane",
	"ecm/widget/dialog/ConfirmationDialog",
	"ecm/Messages",
	"ier/constants",
	"ier/messages",
	"ier/util/property",
	"ier/widget/_FolderSelectorDropDown",
	"ier/widget/ObjectSelector",
	"ier/widget/RadioButtonSelector"
], function(dojo_declare, dojo_lang, dojo_class, dojo_style, ecm_widget_CommonPropertiesPane, ecm_widget_dialog_ConfirmationDialog, ecm_messages,
		ier_constants, ier_messages, ier_util_property, ier_widget_FolderSelectorDropDown, ier_widget_ObjectSelector, ier_widget_RadioButtonSelector){

/**
 * @name ier.widget.panes.CommonPropertiesPane
 * @class
 * @augments dijit._Widget
 */
return dojo_declare("ier.widget.panes.CommonPropertiesPane", [ecm_widget_CommonPropertiesPane], {
	/** @lends ier.widget.panes.CommonPropertiesPane.prototype */

	ier_messages: ier_messages,
	_item: null,
	_enableReportPropertiesSyncUp:true,

	postCreate: function(){
		this.inherited(arguments);
		this._enableReportPropertiesSyncUp=true;
	},
	
	destroy: function(){
		if(this._namingPatternDialog){
			this._namingPatternDialog.destroy();
		}
		if(this._confirmRetainMetadata){
			this._confirmRetainMetadata.destroy();
		}
		this._item = null;
		this.inherited(arguments);
	},

	/**
	 * Determines whether the attribute will be shown
	 */
	isAttributeShown: function(attributeDefinition, item) {
		var show = this.inherited(arguments);
		
		if(show && attributeDefinition && attributeDefinition.contentClass && attributeDefinition.contentClass.id) {
			if (attributeDefinition.contentClass.id == ier_constants.ClassName_DispositionTrigger) {
				var eventType = attributeDefinition.getMetaData(ier_constants.DispositionEventType);
				if(eventType == null)
					show = true;
				else if(eventType == ier_constants.EventType_PredefinedDateTrigger)
					show = ier_util_property.isPredefinedDateEventProperty(attributeDefinition.id);
				else if(eventType == ier_constants.EventType_ExternalEventTrigger)
					show = ier_util_property.isExternalEventProperty(attributeDefinition.id);
				else if(eventType == ier_constants.EventType_InternalEventTrigger)
					show = ier_util_property.isInternalEventProperty(attributeDefinition.id);
				else if(eventType == ier_constants.EventType_RecurringEventTrigger)
					show = ier_util_property.isRecurringEventProperty(attributeDefinition.id);
				else
					show = true;
			}
			else if (attributeDefinition.contentClass.id == ier_constants.ClassName_ReportDefinition) {
				// For report definitions, no need to show Document Title
				if (attributeDefinition.id == ier_constants.Property_DocumentTitle) 
					show = false;
			}
		}
		
		return show;
	},

	_setFolderRoot: function(folderSelector, repository, rootFolderId){
		if(rootFolderId){
			repository.retrieveItem(rootFolderId, dojo_lang.hitch(this, function(itemRetrieved){
				folderSelector.setRoot(itemRetrieved);
			}));
		}
		else
			folderSelector.setRoot(repository);	
	},
	
	/**
	 * Renders the attributes.
	 * 
	 * @param attributeDefinitions
	 *            An array of {@link ecm.model.AttributeDefinition} objects. Provides the definitions for the
	 *            attributes in the <code>item</code> parameter.
	 * @param item
	 *            A {@link ecm.model.ContentItem} object.
	 * @param reason
	 *            The reason for displaying properties. Pass <code>"create"</code> when creating a new content
	 *            item, <code>"checkin"</code> when checking in a content item, <code>"editProperties"</code>
	 *            when editing the properties of a content item, or <code>"multiEditProperties"</code> when
	 *            editing the properties of multiple content items.
	 * @param isReadOnly
	 *            Pass <code>true</code> if the entire item is read only.
	 */
	renderAttributes: function(attributeDefinitions, item, reason, isReadOnly) {
		this.onRenderAttributes(attributeDefinitions, item, reason, isReadOnly);
		this._item = item;
		this.inherited(arguments);
	},
	
	/**
	 * Event invoked when attributes are being rendered in this common properties pane;
	 */
	onRenderAttributes: function(){
		
	},

	/**
	 * Creates the appropriate control based on the provided arguments. This is done so subclasses can override and
	 * provide a different implementation
	 */
	createControl: function(kwArgs, attributeDefinition, callback) 
	{
		var controlWidget = null;
		var repository  = attributeDefinition.contentClass.repository;
		
		var baseConstraints = {
				labelId : kwArgs.id,
				name: kwArgs.name || "",
				label: kwArgs.label || "",
				dataType: kwArgs.dataType,
				readOnly: kwArgs.readOnly,
				required: kwArgs.required
		};

		//override all object types with our own object selector
		if(kwArgs && kwArgs.dataType && kwArgs.dataType == "xs:object"){
			var property = attributeDefinition.id;
			
			var objectSelector = this._createObjectSelector(repository, null, baseConstraints, kwArgs);

			//find the right class based on the property.
			var className = null;
			if(property == ier_constants.Property_Location || property == ier_constants.Property_HomeLocation)
				className = ier_constants.ClassName_Location;
			else if(property == ier_constants.Property_AssociatedRecordType)
				className = ier_constants.ClassName_RecordType;
			else if(property == ier_constants.Property_AssociatedWorkflow){
				className = ier_constants.ClassName_WorkflowDefinition;
				objectSelector.showVersionSelection = true;
			}
			else if(property == ier_constants.Property_NamingPattern){
				className = ier_constants.ClassName_NamingPattern;
				objectSelector.createCreateButton();
				this.connect(objectSelector, "onItemCreate", function(){
					if(this._namingPatternDialog){
						this._namingPatternDialog.destroy();
					}
					// avoid cyclic dependency, assuming AddNamingPatternDialog is already loaded
					var AddNamingPatternDialog = null;
					require(["ier/widget/dialog/AddNamingPatternDialog"], function(ier_dialog_AddNamingPatternDialog){
						AddNamingPatternDialog = ier_dialog_AddNamingPatternDialog;
					});
					if(AddNamingPatternDialog){
						this._namingPatternDialog = new AddNamingPatternDialog();
						this._namingPatternDialog.show(repository);
					}
					return this._namingPatternDialog;
				});
			}
			else
				className = null;
			
			if(className)
				objectSelector.setObjectClassName(className);
			else { //attempt to grab the class name from the required class property of the property description since the class name is unknown
				objectSelector.setPropertyClassName(property);
				objectSelector.contentClass = attributeDefinition.contentClass;
			}

			controlWidget = objectSelector;
		}
		else {
		
			switch (attributeDefinition.id) {
			//Change reviewer into a group selector rather than just a regular textfield
			case ier_constants.Property_Reviewer:
				attributeDefinition.dataType = ier_constants.DataType_User;
				kwArgs.dataType= ier_constants.DataType_User;
				kwArgs.repository  = attributeDefinition.contentClass.repository;

				var control = this.inherited(arguments, [kwArgs, attributeDefinition]);
				if(control && control.value && control.value[0] && control.value[0].displayName){
					control.label = control.value[0].displayName;
				}
				else {
					if(control.value && control.value[0])
						control.label = control.value[0];
				}
				controlWidget = control;
				break;
			case ier_constants.ReportEntry_disposal_schedule:
				var objectSelector = this._createObjectSelector(repository, ier_constants.ClassName_DispositionSchedule, baseConstraints, kwArgs);
				objectSelector.addDialogCssClass("ierMediumDialog");
				controlWidget = objectSelector;
				
				controlWidget = objectSelector;
				break;
			case ier_constants.Property_HoldName:
				if (attributeDefinition.contentClass && (attributeDefinition.contentClass.id == ier_constants.ClassName_ReportHold || 
						attributeDefinition.contentClass.name == ier_constants.ClassName_ReportHold))
				{
					var objectSelector = this._createObjectSelector(repository, ier_constants.ClassName_Hold, baseConstraints, kwArgs);
					objectSelector.addDialogCssClass("ierMediumDialog");
					controlWidget = objectSelector;
				} else {
					controlWidget = this.inherited(arguments, [kwArgs, attributeDefinition]);
				}
				
				break;
			
			case ier_constants.ReportEntry_fileplan_browse:
				var control = new ier_widget_FolderSelectorDropDown(baseConstraints);
				dojo_style.set(control.domNode, "width", "300px");
	
				this._setFolderRoot(control, 
						attributeDefinition.contentClass.repository, 
						ier_constants.Id_RecordsManagementFolder);
		
				if (control)
					this.connect(control, "onFolderSelected", "onChange");
				
				controlWidget = control;
				break;
	
			case ier_constants.ReportEntry_report_type:
				var labels = [];
				labels.push(this.ier_messages.report_detailed);
				labels.push(this.ier_messages.report_summarized);
	
				var control = new ier_widget_RadioButtonSelector(baseConstraints);
				control.setLabels(labels);
				control.createRendering();
		
				if (control)
					this.connect(control, "onChange", "onChange");

				controlWidget = control;
				break;
				
			case ier_constants.ReportEntry_rm_entity_type :
				kwArgs.uniqueValues = true;
				var control = this.inherited(arguments, [kwArgs, attributeDefinition]);
				if (control) // need to add onChange due to a bug in Nexus.
					this.connect(control, "onChange", "onChange");
								
				controlWidget = control;
				break;

			case ier_constants.Property_RetainMetadata:
				kwArgs.valueOptions = [];
				kwArgs.valueOptions.push({
					value: 1,
					label: ecm_messages.false_label
				});
				kwArgs.valueOptions.push({
					value: 0,
					label: ecm_messages.true_label
				});
				var control = this.inherited(arguments, [kwArgs, attributeDefinition]);
				var initialValue = kwArgs.values.length ? kwArgs.values[0] : "";
				if (control){
					this.connect(control, "onChange", function(){
						var value = control.getValue();
						if(value !== initialValue){
							if(value === 0){
								if(this._confirmRetainMetadata){
									this._confirmRetainMetadata.destroy();
								}
								this._confirmRetainMetadata = new ecm_widget_dialog_ConfirmationDialog({
									title: ecm_messages.warning_dialog_title,
									text: ier_messages.retainmetadata_confirmation,
									buttonLabel: ecm_messages.ok,
									onCancel: dojo_lang.hitch(this, function(){
										control.set('displayedValue', initialValue === 1 ? ecm_messages.false_label : "");
									})
								});
								dojo_class.add(this._confirmRetainMetadata.domNode, "ierConfirmationDialog");
								this._confirmRetainMetadata.show();
							}
							this.onPropertyChanged();
						}
					});
				}
				controlWidget = control;
				break;

			default:
				controlWidget = this.inherited(arguments, [kwArgs, attributeDefinition]);
				break;
			}
		}
		
		//var description = attributeDefinition.description;
		//if(description) {
			//controlWidget.promptText = description;
		//}
		
		if (dojo_lang.isFunction(callback)) {
			callback(controlWidget);
		} else {
			return controlWidget;
		}
	},
	
	_createObjectSelector: function(repository, className, baseConstraints, kwArgs){
		var objectSelector = new ier_widget_ObjectSelector(baseConstraints);
		objectSelector.setRepository(repository);
		
		if(className)
			objectSelector.setObjectClassName(className);
		
		//sets the default item if it exists
		if(kwArgs.values && kwArgs.values.length > 0)
			if(kwArgs.values[0]){
				repository.retrieveItem(kwArgs.values[0], dojo_lang.hitch(this, function(itemRetrieved){
					if(!objectSelector._destroyed){
						objectSelector.setSelectedItem(itemRetrieved, true);
						this.onValueSet(itemRetrieved);
					}
				}), className);
			}

		if (objectSelector)
			this.connect(objectSelector, "onChange", "onChange");
		
		return objectSelector;
	},
	
	//TODO: This shouldn't be done here.  CommonPropertiesPane is a common widget for all properties.  Any specific properties synching should be
	//in the properties dialog for the widget itself.  This is a weird place to do this.  We should consider moving this back into ReportDefinition dialog.
	onChange: function(evt) {
		if (evt && evt.type=="keyup") {
			if (evt.target.name == ier_constants.Property_RMReportTitle && this._enableReportPropertiesSyncUp) {
				var srcField = this.getFieldWithName(ier_constants.Property_RMReportTitle);
				var destField = this.getFieldWithName(ier_constants.Property_ReportName);
				
				var srcFieldValue = this.getPropertyValue(ier_constants.Property_RMReportTitle);
				if (srcField && destField)
				{
					this.setPropertyValue(ier_constants.Property_ReportName, srcFieldValue);
				}
			}
			else if (evt.target.name == ier_constants.Property_ReportName) {
				// Once this title field is changed, disable auto sync up.
				this._enableReportPropertiesSyncUp = false;
			}			
		}
	},
	
	/**
	 * Event invoked when a value is set.
	 */
	onValueSet: function(itemRetrieved){
		
	},

	//TODO: This shouldn't be done here.  CommonPropertiesPane is a common widget for all properties.  Any specific properties synching should be
	//in the properties dialog for the widget itself.  This is a weird place to do this.  We should consider moving this back into ReportDefinition dialog.
	enablePropertieValuesSyncUp : function(enabled) {
		this._enableReportPropertiesSyncUp = enabled;
	},

	/**
	 * Used to append new custom fields
	 * @param field
	 */
	appendCustomField: function(field){
		this.getFields().push(field);
	},

	getPropertiesJSON: function(includeReadonly, includeHidden, excludeEmptyValues) {
		var properties = this.inherited(arguments);
		for(var i in properties){
			var prop = properties[i];
			for(var attrDefNdx in this.attributeDefinitions){
				var attrDef = this.attributeDefinitions[attrDefNdx];
				if(prop.name == attrDef.id){
					prop["isShown"] = this.isAttributeShown(attrDef, this._item);
				}
			}
		}
		return properties;
	}
});});
