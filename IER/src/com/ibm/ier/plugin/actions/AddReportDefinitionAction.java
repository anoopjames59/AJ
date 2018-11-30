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
/*    */ public class AddReportDefinitionAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_ADD_REPORT_DEFINITION = "privIERAddReportDefinition";
/*    */   
/*    */   public String getId()
/*    */   {
/* 48 */     return "IERAddReportDefinition";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 52 */     return "actionIERAddReportDefinition";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 56 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 60 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_ADD_REPORT_DEFINITION, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 64 */     return "privIERAddReportDefinition";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 68 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 76 */     return true;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 80 */     return new String[] { "IERReportDefinitionsConfigureToolbarMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\AddReportDefinitionAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */