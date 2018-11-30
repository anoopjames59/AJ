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
/*    */ public enum RMMergeMode
/*    */ {
/* 14 */   Intersection(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 20 */   Union(1);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private RMMergeMode(int intValue)
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
/*    */   public static RMMergeMode getInstanceFromInt(int intValue)
/*    */   {
/* 50 */     RMMergeMode result = null;
/* 51 */     if (intValue == Intersection.intValue) {
/* 52 */       result = Intersection;
/* 53 */     } else if (intValue == Union.intValue) {
/* 54 */       result = Union;
/*    */     }
/* 56 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMMergeMode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */