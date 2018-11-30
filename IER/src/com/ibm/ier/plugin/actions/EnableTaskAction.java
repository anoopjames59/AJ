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
/*    */ public class EnableTaskAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 45 */     return "IERTaskEnable";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 49 */     return "actionTaskEnable";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 53 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 57 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_TASK_ENABLE, new Object[0]);
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
/* 77 */     return new String[] { "IERDisabledRecurringTaskContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\EnableTaskAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */