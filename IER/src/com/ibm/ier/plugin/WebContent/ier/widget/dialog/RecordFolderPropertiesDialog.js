define([
	"dojo/_base/declare",
	"dojo/string",
	"dojo/_base/lang",
	"dojo/_base/array",
	"ier/constants",
	"ier/messages",
	"ier/widget/dialog/RecordContainerPropertiesDialog"
], function(dojo_declare, dojo_string, dojo_lang, dojo_array, ier_constants, ier_messages, ier_dialog_IERBaseDialog){
		return dojo_declare("ier.widget.dialog.RecordFolderPropertiesDialog", [ier_dialog_IERBaseDialog], {
			
			showDispositionPane: true,
			
			_classes: [
		   		{type: ier_constants.EntityType_ElectronicRecordFolder, name: ier_constants.ClassName_ElectronicRecordFolder},
		   		{type: ier_constants.EntityType_PhysicalContainer, name: ier_constants.ClassName_PhysicalContainer, id: "PhysicalBox"},
		   		{type: ier_constants.EntityType_HybridRecordFolder, name: ier_constants.ClassName_HybridRecordFolder},
		   		{type: ier_constants.EntityType_PhysicalRecordFolder, name: ier_constants.ClassName_PhysicalRecordFolder}
		   	],
			
			_renderDialog: function() {
				this.logEntry("_renderDialog");
				
				this.set("title", ier_messages.recordFolder);
				this.setResizable(true);

				//renders the other panes
				this._entityItemPropertiesPane.createRendering({
					repository: this.repository,
					parentFolder: this._parentFolder,
					rootClassId : this.item.getContentClass().id, 
					defaultNameProperty: ier_constants.Property_RecordFolderName, 
					entityType: ier_constants.EntityType_RecordFolder,
					disableContentClassSelector: true,
					defaultClass: this.item.getContentClass(),
					item: this.item,
					isCreate: false,
					shouldGetItemAttributes : true
				});
				
				this.connect(this._entityItemDispositionPane, "onShow", dojo_lang.hitch(this, function(){
					if(!this._entityItemDispositionPane.isLoaded()) {
						this.item.getLegacyDispositionSchedule(dojo_lang.hitch(this, function(dispSchedule){
							this._entityItemDispositionPane.createRendering({
								repository: this.repository, 
								parentFolder: this._parentFolder, 
								entityItemPropertiesPane: this._entityItemPropertiesPane, 
								legacyDispositionSchedule: dispSchedule,
								item: this.item
							});
						}));
					}
				}));				

				//Connects and change existing attributes for the property pane before they are rendered
				this.connect(this._entityItemPropertiesPane, "onRenderAttributes", function(attributes) {
					this._entityItemDetailPane.createRendering(this.item, attributes);
				});
				
				//Sets up and renders the security pane after the property pane has finished rendering
				this.connect(this._entityItemPropertiesPane, "onCompleteRendering", function() {			
					var contentClass = this._entityItemPropertiesPane.getContentClass();
					var properties = this._entityItemPropertiesPane.getProperties();
					
					//renders the security pane
					this._entityItemSecurityPane.createRendering(this.repository, this.item, this._parentFolder, contentClass, properties, !this.item.privModifyPermissions);
					
					this._entityItemPropertiesPane.resizeCommonProperties();
					this.resize();
				});
				
				this._initHoldPane();
				this._initLinksPane();
				this._initHistoryPane();

				this.logExit("_renderDialog");
			}
		});
});