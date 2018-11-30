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
/*    */ 
/*    */ public enum RMPrincipalSearchSortType
/*    */ {
/* 19 */   None(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 24 */   Ascending(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 29 */   Descending(2);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private RMPrincipalSearchSortType(int intValue) {
/* 34 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 44 */     return this.intValue;
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
/*    */   public static RMPrincipalSearchSortType getInstanceFromInt(int intValue)
/*    */   {
/* 58 */     if (intValue == None.intValue)
/* 59 */       return None;
/* 60 */     if (intValue == Ascending.intValue)
/* 61 */       return Ascending;
/* 62 */     if (intValue == Descending.intValue) {
/* 63 */       return Descending;
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMPrincipalSearchSortType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */