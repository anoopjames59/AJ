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
/*    */ public class RecordPropertiesAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 44 */     return "IERRecordProperties";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 48 */     return "actionRecordProperties";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 52 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_RECORD_PROPERTIES, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 60 */     return "";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 64 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public String getActionModelClass() {
/* 76 */     return "ier/model/actions/RecordPropertiesAction";
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 80 */     return new String[] { "ItemContextMenu" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\RecordPropertiesAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */