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
/*    */ public class AddLocationAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_ADD_LOCATION = "privIERAddLocation";
/*    */   
/*    */   public String getId()
/*    */   {
/* 47 */     return "IERAddLocation";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 51 */     return "actionIERAddLocation";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 55 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 59 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_ADD_LOCATION, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 63 */     return "privIERAddLocation";
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
/* 79 */     return new String[] { "IERLocationsConfigureToolbarMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\AddLocationAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */