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
/*    */ public class UndeclareAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_UNDECLARE = "privIERDelete";
/*    */   
/*    */   public String getId()
/*    */   {
/* 51 */     return "IERUndeclare";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 55 */     return "actionIERUndeclare";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 59 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 63 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_UNDECLARE, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 67 */     return "privIERDelete";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 71 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 75 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 79 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 83 */     return new String[] { "IERElectronicRecordContextMenuType", "IERFavoriteElectronicRecordContextMenuType", "IERFavoritePhysicalRecordContextMenuType", "IERPhysicalRecordContextMenuType", "IERMultipleRecordContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\UndeclareAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */