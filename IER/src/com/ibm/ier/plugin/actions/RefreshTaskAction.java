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
/*    */ public class RefreshTaskAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 49 */     return "IERTaskRefresh";
/*    */   }
/*    */   
/*    */   public String getActionFunction()
/*    */   {
/* 54 */     return "actionTaskRefresh";
/*    */   }
/*    */   
/*    */   public String getIcon()
/*    */   {
/* 59 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale)
/*    */   {
/* 64 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_REFRESH, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege()
/*    */   {
/* 69 */     return "privTaskUserPermission";
/*    */   }
/*    */   
/*    */   public String getServerTypes()
/*    */   {
/* 74 */     return "p8,cm,od";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc()
/*    */   {
/* 79 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal()
/*    */   {
/* 84 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes()
/*    */   {
/* 89 */     return new String[] { "IERInProgressTaskContextMenuType", "IERScheduledTaskContextMenuType", "IERInProgressTaskInstanceContextMenuType", "IERScheduledTaskInstanceContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\RefreshTaskAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */