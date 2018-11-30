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
/*    */ public class CancelRemoveHoldRequestAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_CANCEL_REMOVE_HOLD_REQUEST = "privIERCancelRemoveHoldRequest";
/*    */   
/*    */   public String getId()
/*    */   {
/* 46 */     return "IERCancelRemoveHoldRequest";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 50 */     return "actionIERCancelRemoveHoldRequest";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 54 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_CANCEL_REMOVE_HOLD_REQUEST, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 62 */     return "privIERCancelRemoveHoldRequest";
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
/* 78 */     return "ier/model/actions/CancelRemoveHoldRequestAction";
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\CancelRemoveHoldRequestAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */