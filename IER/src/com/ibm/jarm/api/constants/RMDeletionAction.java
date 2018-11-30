/*    */ package com.ibm.jarm.api.constants;
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
/*    */ public enum RMDeletionAction
/*    */ {
/* 18 */   None(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 25 */   Prevent(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 31 */   Cascade(2);
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */   private RMDeletionAction(int intValue)
/*    */   {
/* 38 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 48 */     return this.intValue;
/*    */   }
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
/*    */   public static RMDeletionAction getInstanceFromInt(int intValue)
/*    */   {
/* 62 */     RMDeletionAction result = null;
/* 63 */     if (intValue == None.intValue) {
/* 64 */       result = None;
/* 65 */     } else if (intValue == Prevent.intValue) {
/* 66 */       result = Prevent;
/* 67 */     } else if (intValue == Cascade.intValue) {
/* 68 */       result = Cascade;
/*    */     }
/* 70 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMDeletionAction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */