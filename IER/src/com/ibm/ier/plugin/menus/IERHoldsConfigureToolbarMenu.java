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
/*    */ public class IERHoldsConfigureToolbarMenu
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
/* 48 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_HOLDSPANE_TOOLBAR, new Object[0]);
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
/* 59 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_HOLDSPANE_TOOLBAR, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMenuType()
/*    */   {
/* 67 */     return "IERHoldsConfigureToolbarMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PluginMenuItem[] getMenuItems()
/*    */   {
/* 76 */     return new PluginMenuItem[] { new PluginMenuItem("IERRefresh"), new PluginMenuItem("Separator"), new PluginMenuItem("IERAddHold"), new PluginMenuItem("Separator"), new PluginMenuItem("IERProperties"), new PluginMenuItem("IERDelete"), new PluginMenuItem("IERViewEntitiesOnHold") };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\IERHoldsConfigureToolbarMenu.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */