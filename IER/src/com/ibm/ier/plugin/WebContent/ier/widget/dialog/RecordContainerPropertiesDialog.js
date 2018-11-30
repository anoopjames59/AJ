define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"ier/constants",
	"ier/messages",
	"dojo/dom-style",
	"dojo/string",
	"ier/widget/dialog/IERBaseDialog",
	"dojo/text!./templates/RecordContainerPropertiesDialogContent.html",
	"dijit/layout/ContentPane", // in content
	"dijit/layout/TabContainer", // in content
	"ier/widget/panes/EntityItemDispositionPane", // in content
	"ier/widget/panes/EntityItemPropertiesPane", // in content
	"ier/widget/panes/EntityItemSecurityPane", // in content
	"ier/widget/panes/EntityItemDetailPane", // in content
	"ier/widget/panes/EntityItemLinksPane", // in content
	"ier/widget/panes/EntityItemHoldPane", // in content
	"ier/widget/panes/EntityItemHistoryPane" // in content
], function(dojo_declare, dojo_lang, ier_constants, ier_messages, dom_style, dojo_string, ier_dialog_IERBaseDialog, contentString){
		return dojo_declare("ier.widget.dialog.RecordContainerPropertiesDialog", [ier_dialog_IERBaseDialog], {
			
			contentString: contentString,
			_parentFolder: null,
			_updateOnClose: false,
			showDispositionPane: false,
			
			postCreate: function() {
				this.inherited(arguments);
				this.addChildPane(this._entityItemPropertiesPane);
				this.addChildPane(this._entityItemSecurityPane);
				if(this.showDispositionPane)
					this.addChildPane(this._entityItemDispositionPane);
				
				this.addChildPane(this._entityItemLinksPane);
				this.addChildPane(this._entityItemHoldPane);
				this.addChildPane(this._entityItemHistoryPane);
				this._saveButton = this.addButton(ier_messages.baseDialog_saveButton, "_onClickSave", true, true);
				this._applyButton = this.addButton(ier_messages.baseDialog_applyButton, "_onClickApply", true, false);
				this.connect(this, "onCancel", dojo_lang.hitch(this, "_refreshOnClose"));

				this.connect(this, "_onClickApply", "_disableButton");
			},
			
			setDialogMode: function(editable) {
				if(editable) {
					dom_style.set(this._saveButton.domNode, "display", "");
					dom_style.set(this._applyButton.domNode, "display", "");
					this.cancelButton.set("label", ier_messages.baseDialog_cancelButton);
				} else {
					dom_style.set(this._saveButton.domNode, "display", "none");
					dom_style.set(this._applyButton.domNode, "display", "none");
					this.cancelButton.set("label", ier_messages.baseDialog_closeButton);
				}
			},
			
			// Override
			hasEditablePane: function() {
				return (this.item.privModifyProperties || this.item.privModifyPermissions);
			},
			
			show: function(repository, parentFolder, item) {
				this.inherited("show", arguments);
				
				this.logEntry("show");
				this._parentFolder = parentFolder;
				this.item = item;
				
				//do not show disposition tab
				if(!this.showDispositionPane){
					dom_style.set(this._entityItemDispositionPane.controlButton.domNode, "display", "none");
				}
				
				//if it is externally managed, disable all the panes
				var externallyManaged = this.item.attributes[ier_constants.Property_RMExternallyManagedBy];
				if(externallyManaged && externallyManaged.length > 0){
					this.setMessage(dojo_string.substitute(ier_messages.externallyManaged, [externallyManaged]), "info");
				}
				this.setDialogMode(this.hasEditablePane());
				
				this.repository = repository;
				
				if(this.repository.isIERLoaded()){
					this._renderDialog();
					this.resize();
				} else {
					this.repository.loadIERRepository(dojo_lang.hitch(this, function(repository){
						this._renderDialog();
						this.resize();
					}));
				}
				this.logExit("show");
			},
			
			/**
			 * Validates the form and disables the default button if necessary
			 * @returns {Boolean}
			 */
			validateInput: function() {
				var childPanes = this.getChildPanes();
				for(var i in childPanes){
					var childPane = childPanes[i];
					if(childPane.isValidationRequired()){
						var validate = childPane.validate();
						if(!validate){
							this._disableButton();
							return false;
						}
					}
				}
				this._enableButton();
				return true;
			},
			
			_renderDialog: function() {
			},
			
			_initHoldPane: function() {
				this.logEntry("_initHoldPane");
				this.connect(this._entityItemHoldPane, "onShow", dojo_lang.hitch(this, function(){
					if(!this._entityItemHoldPane.isLoaded()) {
						this._entityItemHoldPane.createRendering(this.repository, this.item);
					}
				}));
				this.logExit("_initHoldPane");
			},
			
			_initLinksPane: function() {
				this.logEntry("_initLinksPane");
				this.connect(this._entityItemLinksPane, "onShow", dojo_lang.hitch(this, function(){
					if(!this._entityItemLinksPane.isLoaded()) {
						this._entityItemLinksPane.createRendering(this.repository, this.item);
					}
				}));
				this.logExit("_initLinksPane");
			},

			_initHistoryPane: function() {
				this.logEntry("_initHistoryPane");
				this.connect(this._entityItemHistoryPane, "onShow", dojo_lang.hitch(this, function(){
					if(!this._entityItemHistoryPane.isLoaded()) {
						this._entityItemHistoryPane.createRendering(this.repository, this.item);
					}
				}));
				this.logExit("_initHistoryPane");
			},

			_onClickApply: function() {
				this._updateProperties(false);
			},
			
			_onClickSave: function() {
				this._updateProperties(true);
			},
			
			_refreshOnClose: function() {
				if(this._updateOnClose) {
					var parent = this.item.parent;
					if(parent){
						parent.refresh();
					}
					this._updateOnClose = false;
				}
			},
			
			_updateProperties: function(closeOnComplete) {
				this.logEntry("_onClickUpdate");
				if(this.validateInput()) {
					var className = this._entityItemPropertiesPane.getContentClass().id;
					var properties = this._entityItemPropertiesPane.getProperties();
					var permissions = this._entityItemSecurityPane.getPermissions();
					var dispSchedule = this._entityItemDispositionPane ? this._entityItemDispositionPane.getDispositionSchedule() : null;
					var legacyScheduleParams = this._entityItemDispositionPane.getLegacyDispositionScheduleProperties();
					
					
					this.item.updateRecordContainerProperties(className, properties, permissions, dispSchedule, legacyScheduleParams, closeOnComplete,
							closeOnComplete ? dojo_lang.hitch(this, function(){
								this._updateOnClose = false;
								this._updatePropertiesCompleted(className, properties, permissions, dispSchedule, legacyScheduleParams);
								this.onCancel();
							}) : dojo_lang.hitch(this, function(){
								this._updatePropertiesCompleted(className, properties, permissions, dispSchedule, legacyScheduleParams);
								this._updateOnClose = true;
							}));
				}
				this.logExit("_onClickUpdate");
			},
			
			_updatePropertiesCompleted: function(className, properties, permissions, dispSchedule, legacyScheduleParams){
				//null out the cache for legacy disposition schedules if it exists since we are saving without a schedule anymore
				if(this.item && legacyScheduleParams == null){
					if(this.item.getIERObjectItem(ier_constants.Property_DispositionSchedule) != null){
						this.item.attributeItems[ier_constants.Property_DispositionSchedule] = null;
					}
				}
			},
			
			_enableButton: function(){
				this._saveButton.set("disabled", false);
				this._applyButton.set("disabled", false); 
			},
			
			_disableButton: function(){
				this._saveButton.set("disabled", true);
				this._applyButton.set("disabled", true); 
			}
		});
});