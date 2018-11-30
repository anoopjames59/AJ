/*    */ package com.ibm.ier.plugin.actions;
/*    */ 
/*    */ import com.ibm.ecm.extension.PluginAction;
/*    */ import com.ibm.ier.plugin.nls.MessageCode;
/*    */ import com.ibm.ier.plugin.nls.MessageResources;
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefreshAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 67 */     return "IERRefresh";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 71 */     return "actionIERRefresh";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 75 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 79 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_REFRESH, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 83 */     return "";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 87 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 91 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 95 */     return true;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 99 */     return new String[] { "IERActionsConfigureToolbarMenuType", "IERCompletedTaskContextMenuType", "ContentListToolbar", "IERDefensibleDispositionTaskToolbarMenuType", "IERDispositionSchedulesConfigureToolbarMenuType", "IERElectronicRecordFolderContextMenuType", "IERFavoriteElectronicRecordFolderContextMenuType", "IERFavoritePhysicalRecordFolderContextMenuType", "IERFavoriteVolumeContextMenuType", "IERFavoriteRecordCategoryContextMenuType", "IERFilePlanContextMenuType", "IERFilePlansConfigureToolbarMenuType", "IERHoldsConfigureToolbarMenuType", "IERHoldTaskToolbarMenuType", "IERLocationsConfigureToolbarMenuType", "IERMultipleContainerContextMenuType", "IERMultiStatusTaskToolbarMenuType", "IERNamingPatternsConfigureToolbarMenuType", "IERPhysicalBoxHybridRecordFolderContextMenuType", "IERRecordCategoryContextMenuType", "IERVolumeContextMenuType", "IERReportDefinitionsConfigureToolbarMenuType", "IERReportTaskToolbarMenuType", "IERTransferMappingsConfigureToolbarMenuType", "IERTriggersConfigureToolbarMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\RefreshAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */