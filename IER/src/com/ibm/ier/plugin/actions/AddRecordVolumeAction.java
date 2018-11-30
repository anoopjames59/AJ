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
/*    */ public class AddRecordVolumeAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_ADD_RECORD_VOLUME = "privIERAddRecordVolume";
/*    */   
/*    */   public String getId()
/*    */   {
/* 45 */     return "IERAddRecordVolume";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 49 */     return "actionIERAddRecordVolume";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 53 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 57 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_ADD_RECORDVOLUME, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 61 */     return "privIERAddRecordVolume";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 65 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 69 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 73 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\AddRecordVolumeAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */