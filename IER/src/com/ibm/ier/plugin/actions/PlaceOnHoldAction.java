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
/*    */ public class PlaceOnHoldAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_PLACE_ON_HOLD = "privIERPlaceOnHold";
/*    */   
/*    */   public String getId()
/*    */   {
/* 46 */     return "IERPlaceOnHold";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 50 */     return "actionIERPlaceOnHold";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 54 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_PLACEONHOLD, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 62 */     return "privIERPlaceOnHold";
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
/*    */   
/* 77 */   public String getActionModelClass() { return "ier/model/actions/PlaceOnHoldAction"; }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\PlaceOnHoldAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */