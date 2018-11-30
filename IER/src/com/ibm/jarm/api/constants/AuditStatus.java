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
/*    */ public enum AuditStatus
/*    */ {
/* 15 */   Success(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 20 */   Failure(55537);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private AuditStatus(int intValue)
/*    */   {
/* 26 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 36 */     return this.intValue;
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
/* 50 */     AuditStatus result = null;
/* 51 */     if (intValue == Success.intValue) {
/* 52 */       result = Success;
/* 53 */     } else if (intValue == Failure.intValue) {
/* 54 */       result = Failure;
/*    */     }
/* 56 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\AuditStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */