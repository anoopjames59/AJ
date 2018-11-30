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
/*    */ public enum RMCardinality
/*    */ {
/* 15 */   Single(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 20 */   Enumeration(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 25 */   List(2);
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */   private RMCardinality(int intValue)
/*    */   {
/* 32 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 42 */     return this.intValue;
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
/*    */   public static RMCardinality getInstanceFromInt(int intValue)
/*    */   {
/* 56 */     RMCardinality result = null;
/* 57 */     if (intValue == Single.intValue) {
/* 58 */       result = Single;
/* 59 */     } else if (intValue == List.intValue) {
/* 60 */       result = List;
/* 61 */     } else if (intValue == Enumeration.intValue) {
/* 62 */       result = Enumeration;
/*    */     }
/* 64 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMCardinality.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */