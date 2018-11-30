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
/*    */ public class InitiateRemoveHoldRequestAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_INITIATE_REMOVE_HOLD_REQUEST = "privIERInitiateRemoveHoldRequest";
/*    */   
/*    */   public String getId()
/*    */   {
/* 46 */     return "IERInitiateRemoveHoldRequest";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 50 */     return "actionIERInitiateRemoveHoldRequest";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 54 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_INITIATE_REMOVE_HOLD_REQUEST, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 62 */     return "privIERInitiateRemoveHoldRequest";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 66 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 70 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public String getActionModelClass() {
/* 78 */     return "ier/model/actions/InitiateRemoveHoldRequestAction";
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\InitiateRemoveHoldRequestAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */