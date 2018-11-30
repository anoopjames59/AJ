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
/*    */ public class OpenRecordContentAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 48 */     return "IEROpenRecordContent";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 52 */     return "actionIEROpenRecordContent";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 56 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 60 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_OPEN_RECORD_CONTENT, new Object[0]);
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


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\OpenRecordContentAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */