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
/*    */ public class DisableTaskAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 45 */     return "IERTaskDisable";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 49 */     return "actionTaskDisable";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 53 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 57 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_TASK_DISABLE, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 61 */     return "privTaskUserPermission";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 65 */     return "p8,cm,od";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 69 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 73 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 77 */     return new String[] { "IERScheduledRecurringTaskContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\DisableTaskAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */