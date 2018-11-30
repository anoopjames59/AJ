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
/*    */ public class ViewDocumentInfoAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 48 */     return "IERViewDocumentInfo";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 52 */     return "actionIERViewDocumentInfo";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 56 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 60 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_VIEW_DOCUMENT_INFO, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 64 */     return "privViewProperties";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 68 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 76 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 80 */     return new String[] { "IERElectronicRecordContextMenuType", "IERFavoriteElectronicRecordContextMenuType", "IERFavoritePhysicalRecordContextMenuType", "IERPhysicalRecordContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\ViewDocumentInfoAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */