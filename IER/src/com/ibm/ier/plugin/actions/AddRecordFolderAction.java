/*    */ package com.ibm.ier.plugin.actions;
/*    */ 
/*    */ import com.ibm.ecm.extension.PluginAction;
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
/*    */ public class AddRecordFolderAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_ADD_RECORD_FOLDER = "privIERAddRecordFolder";
/*    */   
/*    */   public String getId()
/*    */   {
/* 53 */     return "IERAddRecordFolder";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 57 */     return "actionIERAddRecordFolder";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 61 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 65 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_ADD_RECORDFOLDER, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 69 */     return "privIERAddRecordFolder";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 73 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 77 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 81 */     return false;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 85 */     return new String[] { "ContentListToolbar", "IERElectronicRecordFolderContextMenuType", "IERFavoriteElectronicRecordFolderContextMenuType", "IERFavoritePhysicalRecordFolderContextMenuType", "IERFavoriteRecordCategoryContextMenuType", "IERFilePlanContextMenuType", "IERPhysicalBoxHybridRecordFolderContextMenuType", "IERRecordCategoryContextMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\AddRecordFolderAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */