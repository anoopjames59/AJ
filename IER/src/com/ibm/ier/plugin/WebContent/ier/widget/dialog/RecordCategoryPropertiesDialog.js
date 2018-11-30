define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/string",
	"ier/constants",
	"ier/messages",
	"ier/widget/dialog/RecordContainerPropertiesDialog"
], function(dojo_declare, dojo_lang, dojo_string, ier_constants, ier_messages, ier_dialog_RecordContainerPropertiesDialog){
		return dojo_declare("ier.widget.dialog.RecordCategoryPropertiesDialog", [ier_dialog_RecordContainerPropertiesDialog], {
			
			showDispositionPane: true,
			
			_renderDialog: function() {
				this.logEntry("_renderDialog");
				
				this.set("title", ier_messages.recordCategory);				
				this.setResizable(true);
				
				// render the other panes
				this._entityItemPropertiesPane.createRendering({
					repository: this.repository,
					parentFolder: this._parentFolder,
					rootClassId : ier_constants.ClassName_RecordCategory, 
					defaultNameProperty: ier_constants.Property_RecordCategoryName, 
					entityType: ier_constants.EntityType_RecordCategory,
					hideContentClassSelector: false,
					disableContentClassSelector: true,
					item: this.item,
					isCreate: false
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