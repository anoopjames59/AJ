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
/*    */ public class IERElectronicRecordFolderContextMenu
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
/* 46 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_ELECT_RECORD_FOLDER_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getDescription(Locale locale)
/*    */   {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.MENU_ELECT_RECORD_FOLDER_CONTEXT, new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getMenuType()
/*    */   {
/* 63 */     return "IERElectronicRecordFolderContextMenuType";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PluginMenuItem[] getMenuItems()
/*    */   {
/* 73 */     return new PluginMenuItem[] { new PluginMenuItem("OpenFolder"), new PluginMenuItem("IERProperties"), new PluginMenuItem("IERDelete"), new PluginMenuItem("Separator"), new PluginMenuItem("IERRelocate"), new PluginMenuItem("IERClose"), new PluginMenuItem("IERReopen"), new PluginMenuItem("IERPlaceOnHold"), new PluginMenuItem("IERRemoveHold"), new PluginMenuItem("IERInitiateDisposition"), new PluginMenuItem("Separator"), new PluginMenuItem("Export"), new PluginMenuItem("IERAddToFavorites"), new PluginMenuItem("Separator"), new PluginMenuItem("IERRefresh"), new PluginMenuItem("IERAddRecordCategory"), new PluginMenuItem("IERAddRecordFolder"), new PluginMenuItem("IERAddRecordVolume") };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\menus\IERElectronicRecordFolderContextMenu.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */