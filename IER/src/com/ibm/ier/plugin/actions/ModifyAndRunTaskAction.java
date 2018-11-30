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
/*    */ public class ModifyAndRunTaskAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 46 */     return "IERTaskModifyAndRun";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 50 */     return "actionTaskModify";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 54 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_TASK_MODIFYANDRUN, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 62 */     return "privTaskUserPermission";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 66 */     return "p8,cm,od";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 70 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 78 */     return new String[] { "IERFailedTaskContextMenuType", "IERScheduledTaskContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\ModifyAndRunTaskAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */