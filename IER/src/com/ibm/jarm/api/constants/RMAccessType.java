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
/*    */ public enum RMAccessType
/*    */ {
/* 15 */   Allow(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 20 */   Deny(2);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private RMAccessType(int intValue)
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
/*    */   public static RMAccessType getInstanceFromInt(int intValue)
/*    */   {
/* 50 */     RMAccessType result = null;
/* 51 */     if (intValue == Allow.intValue) {
/* 52 */       result = Allow;
/* 53 */     } else if (intValue == Deny.intValue) {
/* 54 */       result = Deny;
/*    */     }
/* 56 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMAccessType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */