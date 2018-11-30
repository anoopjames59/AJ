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
/*    */ public class AddToFavoritesAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 49 */     return "IERAddToFavorites";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 53 */     return "actionAddToFavorites";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 57 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 61 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_ADD_TO_FAVORITES, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 65 */     return "";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 69 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 73 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 77 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 81 */     return new String[] { "IERElectronicRecordContextMenuType", "IERElectronicRecordFolderContextMenuType", "IERPhysicalBoxHybridRecordFolderContextMenuType", "IERPhysicalRecordContextMenuType", "IERRecordCategoryContextMenuType", "IERVolumeContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\AddToFavoritesAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */