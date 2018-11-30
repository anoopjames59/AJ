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
/*    */ public class AddEventTriggerAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_ADD_EVENT_TRIGGER = "privIERAddEventTrigger";
/*    */   
/*    */   public String getId()
/*    */   {
/* 47 */     return "IERAddEventTrigger";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 51 */     return "actionIERAddEventTrigger";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 55 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 59 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_ADD_EVENT_TRIGGER, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 63 */     return "privIERAddEventTrigger";
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
/*    */   
/*    */   public String[] getMenuTypes() {
/* 79 */     return new String[] { "IERTriggersConfigureToolbarMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\AddEventTriggerAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */