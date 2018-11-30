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
/*    */ public class AddHoldAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_ADD_HOLD = "privIERAddHold";
/*    */   
/*    */   public String getId()
/*    */   {
/* 47 */     return "IERAddHold";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 51 */     return "actionIERAddHold";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 55 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 59 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_ADD_HOLD, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 63 */     return "privIERAddHold";
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
/* 75 */     return true;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 79 */     return new String[] { "IERHoldsConfigureToolbarMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\AddHoldAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */