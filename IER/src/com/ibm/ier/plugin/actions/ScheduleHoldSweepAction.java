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
/*    */ public class ScheduleHoldSweepAction
/*    */   extends PluginAction
/*    */ {
/*    */   public String getId()
/*    */   {
/* 46 */     return "IERScheduleHoldSweep";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 50 */     return "actionIERScheduleHoldSweep";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 54 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 58 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_SCHEDULE_HOLD_SWEEP, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 62 */     return "privTaskUserPermission";
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
/* 74 */     return true;
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 78 */     return new String[] { "IERMultiStatusTaskToolbarMenuType", "IERReportTaskToolbarMenuType" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\ScheduleHoldSweepAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */