/*    */ package com.ibm.ier.plugin.menus.menutypes;
/*    */ 
/*    */ import com.ibm.ecm.extension.PluginMenuType;
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
/*    */ public class IERFailedTaskInstanceContextMenuType
/*    */   extends PluginMenuType
/*    */ {
/*    */   public static final String ID = "IERFailedTaskInstanceContextMenuType";
/*    */   
/*    */   public String getId()
/*    */   {
/* 36 */     return "IERFailedTaskInstanceContextMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName(Locale locale)
/*    */   {
/* 47 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_FAILED_TASK_INSTANCE_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getTooltip(Locale locale)
/*    */   {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_FAILED_TASK_INSTANCE_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isToolbar()
/*    */   {
/* 68 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\menutypes\IERFailedTaskInstanceContextMenuType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */