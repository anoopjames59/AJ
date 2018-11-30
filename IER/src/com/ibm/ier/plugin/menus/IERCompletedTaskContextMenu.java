/*    */ package com.ibm.ier.plugin.menus;
/*    */ 
/*    */ import com.ibm.ecm.extension.PluginMenu;
/*    */ import com.ibm.ecm.extension.PluginMenuItem;
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
/*    */ public class IERCompletedTaskContextMenu
/*    */   extends PluginMenu
/*    */ {
/*    */   public String getId()
/*    */   {
/* 37 */     return "Default" + getMenuType();
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
/* 48 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_COMPLETED_TASK_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 59 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_COMPLETED_TASK_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMenuType()
/*    */   {
/* 67 */     return "IERCompletedTaskContextMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PluginMenuItem[] getMenuItems()
/*    */   {
/* 76 */     return new PluginMenuItem[] { new PluginMenuItem("IERTaskOpen"), new PluginMenuItem("IERTaskDelete") };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\IERCompletedTaskContextMenu.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */