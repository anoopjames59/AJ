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
/*    */ public class IERFilePlanContextMenu
/*    */   extends PluginMenu
/*    */ {
/*    */   public String getId()
/*    */   {
/* 36 */     return "Default" + getMenuType();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName(Locale locale)
/*    */   {
/* 46 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_FILEPLAN_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_FILEPLAN_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getMenuType()
/*    */   {
/* 63 */     return "IERFilePlanContextMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PluginMenuItem[] getMenuItems()
/*    */   {
/* 73 */     return new PluginMenuItem[] { new PluginMenuItem("OpenFolder"), new PluginMenuItem("IERProperties"), new PluginMenuItem("Separator"), new PluginMenuItem("IERRefresh"), new PluginMenuItem("Separator"), new PluginMenuItem("IERAddRecordCategory") };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\IERFilePlanContextMenu.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */