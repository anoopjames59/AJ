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
/*    */ public class IERElectronicRecordContextMenu
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
/* 46 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_ELECT_RECORD_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_ELECT_RECORD_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getMenuType()
/*    */   {
/* 63 */     return "IERElectronicRecordContextMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PluginMenuItem[] getMenuItems()
/*    */   {
/* 73 */     return new PluginMenuItem[] { new PluginMenuItem("IEROpenRecordContent"), new PluginMenuItem("IERProperties"), new PluginMenuItem("IERDelete"), new PluginMenuItem("Separator"), new PluginMenuItem("IERFile"), new PluginMenuItem("IERCopy"), new PluginMenuItem("IERMove"), new PluginMenuItem("IERViewDocumentInfo"), new PluginMenuItem("IERPlaceOnHold"), new PluginMenuItem("IERRemoveHold"), new PluginMenuItem("IERUndeclare"), new PluginMenuItem("IERInitiateDisposition"), new PluginMenuItem("Separator"), new PluginMenuItem("Export"), new PluginMenuItem("IERAddToFavorites") };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\IERElectronicRecordContextMenu.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */