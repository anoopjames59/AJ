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
/*    */ public class RelocateAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_CAN_RELOCATE_CONTAINER = "privIERRelocateContainer";
/*    */   
/*    */   public String getId()
/*    */   {
/* 55 */     return "IERRelocate";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 59 */     return "actionIERRelocate";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 63 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 67 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_RELOCATE, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 71 */     return "privIERRelocateContainer";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 75 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 79 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 83 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 87 */     return new String[] { "IERElectronicRecordFolderContextMenuType", "IERFavoriteElectronicRecordFolderContextMenuType", "IERFavoritePhysicalRecordFolderContextMenuType", "IERFavoriteVolumeContextMenuType", "IERFavoriteRecordCategoryContextMenuType", "IERMultipleContainerContextMenuType", "IERPhysicalBoxHybridRecordFolderContextMenuType", "IERRecordCategoryContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\RelocateAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */