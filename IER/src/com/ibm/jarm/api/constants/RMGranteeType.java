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
/*    */ public enum RMGranteeType
/*    */ {
/* 15 */   User(2000), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 20 */   Group(2001), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 26 */   Unknown(0);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private RMGranteeType(int intValue)
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
/*    */   public static RMGranteeType getInstanceFromInt(int intValue)
/*    */   {
/* 55 */     RMGranteeType result = Unknown;
/* 56 */     if (intValue == User.intValue) {
/* 57 */       result = User;
/* 58 */     } else if (intValue == Group.intValue) {
/* 59 */       result = Group;
/*    */     }
/* 61 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMGranteeType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */