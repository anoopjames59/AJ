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
/*    */ public class IERPhysicalRecordContextMenu
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
/* 46 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_PHYSRECORD_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_PHYSRECORD_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getMenuType()
/*    */   {
/* 63 */     return "IERPhysicalRecordContextMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PluginMenuItem[] getMenuItems()
/*    */   {
/* 74 */     return new PluginMenuItem[] { new PluginMenuItem("IERProperties"), new PluginMenuItem("IERDelete"), new PluginMenuItem("Separator"), new PluginMenuItem("IERFile"), new PluginMenuItem("IERCopy"), new PluginMenuItem("IERMove"), new PluginMenuItem("IERViewDocumentInfo"), new PluginMenuItem("IEROpenRecordContent"), new PluginMenuItem("IERPlaceOnHold"), new PluginMenuItem("IERRemoveHold"), new PluginMenuItem("IERUndeclare"), new PluginMenuItem("IERInitiateDisposition"), new PluginMenuItem("Separator"), new PluginMenuItem("Export"), new PluginMenuItem("IERAddToFavorites") };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\IERPhysicalRecordContextMenu.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */