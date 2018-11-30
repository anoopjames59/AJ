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
/*    */ public class ScheduleDDContainerConversionAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 44 */     return "IERScheduleConversionToDDContainers";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 48 */     return "actionConvertToDDContainer";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 52 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 56 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_SCHEDULE_DDCONTAINER_CONVERSION, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 60 */     return "privTaskAdminPermission,privIERRecordsAdminAndManager";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 64 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 68 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 72 */     return true;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\ScheduleDDContainerConversionAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */