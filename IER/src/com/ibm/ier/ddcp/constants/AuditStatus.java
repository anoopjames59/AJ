/*    */ package com.ibm.ier.ddcp.constants;
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
/*    */ public enum AuditStatus
/*    */ {
/* 19 */   Success(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 24 */   Failure(55537);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private AuditStatus(int intValue)
/*    */   {
/* 30 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 40 */     return this.intValue;
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
/*    */   public static AuditStatus getInstanceFromInt(int intValue)
/*    */   {
/* 54 */     AuditStatus result = null;
/* 55 */     if (intValue == Success.intValue) {
/* 56 */       result = Success;
/* 57 */     } else if (intValue == Failure.intValue) {
/* 58 */       result = Failure;
/*    */     }
/* 60 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\ddcp\constants\AuditStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */