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
/*    */ public enum RMSecurityProxyType
/*    */ {
/* 16 */   None(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 21 */   Full(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 26 */   Inheritance(2);
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */   private RMSecurityProxyType(int intValue)
/*    */   {
/* 33 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 43 */     return this.intValue;
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
/*    */   public static RMSecurityProxyType getInstanceFromInt(int intValue)
/*    */   {
/* 57 */     RMSecurityProxyType result = None;
/* 58 */     if (intValue == Full.intValue) {
/* 59 */       result = Full;
/* 60 */     } else if (intValue == Inheritance.intValue) {
/* 61 */       result = Inheritance;
/*    */     }
/* 63 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMSecurityProxyType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */