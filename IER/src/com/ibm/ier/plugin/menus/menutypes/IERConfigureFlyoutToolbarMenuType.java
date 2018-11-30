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
/*    */ public class IERConfigureFlyoutToolbarMenuType
/*    */   extends PluginMenuType
/*    */ {
/*    */   public static final String ID = "IERConfigureFlyoutToolbarMenuType";
/*    */   
/*    */   public String getId()
/*    */   {
/* 35 */     return "IERConfigureFlyoutToolbarMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName(Locale locale)
/*    */   {
/* 45 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_CONFIGURE_FLYOUT_TOOLBAR, new Object[0]);
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
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_CONFIGURE_FLYOUT_TOOLBAR, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isToolbar()
/*    */   {
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\menutypes\IERConfigureFlyoutToolbarMenuType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */