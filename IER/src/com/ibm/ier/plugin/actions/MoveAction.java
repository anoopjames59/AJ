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
/*    */ 
/*    */ public class MoveAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_CAN_MOVE_RECORD = "privCanMoveRecord";
/*    */   public static final String PRIV_CAN_MOVE_RECORD_TO_CONTAINER = "privCanMoveRecordToContainer";
/*    */   
/*    */   public String getId()
/*    */   {
/* 50 */     return "IERMove";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 54 */     return "actionIERMove";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 58 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 62 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_MOVE, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 66 */     return "privCanMoveRecord";
/*    */   }
/*    */   
/*    */   public String getServerTypes() {
/* 70 */     return "p8";
/*    */   }
/*    */   
/*    */   public boolean isMultiDoc() {
/* 74 */     return false;
/*    */   }
/*    */   
/*    */   public boolean isGlobal() {
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\MoveAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */