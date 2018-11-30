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
/*    */ public class CreateLinkAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 52 */     return "IERCreateLink";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 56 */     return "actionIERCreateLink";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 60 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 64 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_CREATE_LINK, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 68 */     return "privModifyProperties";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 72 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 76 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 80 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 84 */     return new String[] { "IERElectronicRecordContextMenuType", "IERElectronicRecordFolderContextMenuType", "IERFavoriteElectronicRecordContextMenuType", "IERFavoriteElectronicRecordFolderContextMenuType", "IERFavoritePhysicalRecordContextMenuType", "IERFavoritePhysicalRecordFolderContextMenuType", "IERPhysicalBoxHybridRecordFolderContextMenuType", "IERPhysicalRecordContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\CreateLinkAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */