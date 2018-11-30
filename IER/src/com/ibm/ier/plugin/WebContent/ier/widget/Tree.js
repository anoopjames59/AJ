define([
	"dojo/_base/declare",
	"dojo/_base/lang",
	"dojo/dom-construct",
	"ecm/widget/Tree",
	"ier/constants",
	"ier/util/util",
	"ier/model/_BaseEntityObject"
], function(dojo_declare, dojo_lang, dojo_construct, ecm_widget_Tree, ier_constants, ier_util, ier_model_BaseEntityObject){

return dojo_declare("ier.widget.Tree", [ ecm_widget_Tree ], {
	getIconClass: function(/*dojo.data.Item*/item, /*Boolean*/opened) {
		
		var iconClass = null;
		
		if(item){
			if (opened){
				var className = item.template;
				var entityType = item.isInstanceOf(ier_model_BaseEntityObject) ? item.getEntityType() : null;
				if(className == ier_constants.ClassName_FilePlan || entityType == ier_constants.EntityType_FilePlan)
					iconClass = "filePlanOpenIcon";
				else if(entityType == ier_constants.EntityType_RecordCategory || className == ier_constants.ClassName_RecordCategory)
					iconClass = "recordCategoryOpenIcon";
				else if(entityType == ier_constants.EntityType_ElectronicRecordFolder || className == ier_constants.ClassName_ElectronicRecordFolder)
					iconClass = "recordElectronicFolderOpenIcon";
				else if(entityType == ier_constants.EntityType_HybridRecordFolder || className == ier_constants.ClassName_HybridRecordFolder)
					iconClass = "recordHybridFolderOpenIcon";
				else if(entityType == ier_constants.EntityType_PhysicalRecordFolder || className == ier_constants.ClassName_PhysicalRecordFolder)
					iconClass = "recordPhysicalFolderOpenIcon";
				else if(entityType == ier_constants.EntityType_PhysicalContainer || className == ier_constants.ClassName_PhysicalContainer)
					iconClass = "recordBoxOpenIcon";
				else if(entityType == ier_constants.EntityType_Volume || className == ier_constants.ClassName_Volume)
					iconClass = "volumeOpenIcon";
				else
					iconClass = "openFolderIcon";
			}
			else
				iconClass = ier_util.getIconClass(item);
		}
		
		if(iconClass != null)
			return iconClass;
		else
			return this.inherited(arguments);
	}
});
});
