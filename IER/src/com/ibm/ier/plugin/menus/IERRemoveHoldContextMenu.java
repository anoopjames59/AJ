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
/*    */ public class IERRemoveHoldContextMenu
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
/* 46 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_REMOVEHOLD_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_REMOVEHOLD_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getMenuType()
/*    */   {
/* 63 */     return "IERRemoveHoldContextMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public PluginMenuItem[] getMenuItems()
/*    */   {
/* 71 */     return new PluginMenuItem[] { new PluginMenuItem("IERProperties") };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\IERRemoveHoldContextMenu.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */