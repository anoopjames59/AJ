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
/*    */ public class DeclareAction
/*    */   extends PluginAction
/*    */ {
/*    */   public static final String PRIV_RECORD_DECLARE = "privIERRecordDeclare";
/*    */   public static final String PRIV_CAN_DECLARE_RECORD_TO_CONTAINER = "privCanDeclareRecordToContainer";
/*    */   
/*    */   public String getId()
/*    */   {
/* 50 */     return "IERDeclare";
/*    */   }
/*    */   
/*    */   public String getActionFunction() {
/* 54 */     return "actionIERDeclare";
/*    */   }
/*    */   
/*    */   public String getIcon() {
/* 58 */     return "";
/*    */   }
/*    */   
/*    */   public String getName(Locale locale) {
/* 62 */     return MessageResources.getLocalizedMessage(locale, MessageCode.ACTION_DECLARE, new Object[0]);
/*    */   }
/*    */   
/*    */   public String getPrivilege() {
/* 66 */     return "privIERRecordDeclare";
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
/*    */   
/*    */   public String getActionModelClass() {
/* 82 */     return "ier/model/actions/DeclareAction";
/*    */   }
/*    */   
/*    */   public String[] getMenuTypes() {
/* 86 */     return new String[] { "ContentListToolbar", "ItemContextMenu" };
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\actions\DeclareAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */