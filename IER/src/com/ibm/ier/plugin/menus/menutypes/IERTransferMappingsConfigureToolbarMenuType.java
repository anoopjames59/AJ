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
/*    */ public class IERTransferMappingsConfigureToolbarMenuType
/*    */   extends PluginMenuType
/*    */ {
/*    */   public static final String ID = "IERTransferMappingsConfigureToolbarMenuType";
/*    */   
/*    */   public String getId()
/*    */   {
/* 35 */     return "IERTransferMappingsConfigureToolbarMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName(Locale locale)
/*    */   {
/* 45 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_TRANSFERMAPPINGS_TOOLBAR, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getTooltip(Locale locale)
/*    */   {
/* 55 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_TRANSFERMAPPINGS_TOOLBAR, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isToolbar()
/*    */   {
/* 64 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\menutypes\IERTransferMappingsConfigureToolbarMenuType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */