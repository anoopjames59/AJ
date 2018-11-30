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
/*    */ public class OpenTaskAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 44 */     return "IERTaskOpen";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 48 */     return "actionTaskOpen";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 52 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_TASK_OPEN, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 60 */     return "privTaskUserPermission";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 64 */     return "p8,cm,od";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\OpenTaskAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */