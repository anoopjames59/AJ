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
/*    */ public class RunReportAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_RUN_REPORT = "privIERRunReport";
/*    */   
/*    */   public String getId()
/*    */   {
/* 47 */     return "IERRunReport";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 51 */     return "actionIERRunReport";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 55 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 59 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_RUN_REPORT, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 63 */     return "privIERRunReport";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 67 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 71 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\RunReportAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */