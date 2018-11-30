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
/*    */ public class RemoveHoldOnViewEntitiesAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_REMOVE_HOLD_ON_VIEWENTITIES = "privIERRemoveHold";
/*    */   
/*    */   public String getId()
/*    */   {
/* 46 */     return "IERRemoveHoldOnViewEntities";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 50 */     return "actionIERRemoveHoldOnViewEntities";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 54 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_REMOVE_HOLD_ON_VIEWENTITIES, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 62 */     return "privIERRemoveHold";
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
/* 77 */   public String getActionModelClass() { return "ier/model/actions/RemoveHoldOnViewEntitiesAction"; }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\RemoveHoldOnViewEntitiesAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */