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
/*    */ public class CloseAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_CLOSE_CONTAINER = "privIERCloseContainer";
/*    */   
/*    */   public String getId()
/*    */   {
/* 55 */     return "IERClose";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 59 */     return "actionIERClose";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 63 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 67 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_CLOSE, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 71 */     return "privIERCloseContainer";
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
/* 87 */     return new String[] { "IERElectronicRecordFolderContextMenuType", "IERFavoriteElectronicRecordFolderContextMenuType", "IERFavoritePhysicalRecordFolderContextMenuType", "IERFavoriteVolumeContextMenuType", "IERFavoriteRecordCategoryContextMenuType", "IERMultipleContainerContextMenuType", "IERPhysicalBoxHybridRecordFolderContextMenuType", "IERRecordCategoryContextMenuType", "IERVolumeContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\CloseAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */