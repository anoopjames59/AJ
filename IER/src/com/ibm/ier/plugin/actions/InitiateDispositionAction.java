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
/*    */ public class InitiateDispositionAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_CAN_INIT_DISP = "privCanInitiateDisposition";
/*    */   
/*    */   public String getId()
/*    */   {
/* 46 */     return "IERInitiateDisposition";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 50 */     return "actionIERInitiateDisposition";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 54 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_INITIATEDISP, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 62 */     return "privCanInitiateDisposition";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 66 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 70 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\InitiateDispositionAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */