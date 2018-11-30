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
/*    */ public enum RMPrincipalSearchAttribute
/*    */ {
/* 19 */   None(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 24 */   ShortName(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 29 */   DisplayName(2);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private RMPrincipalSearchAttribute(int intValue)
/*    */   {
/* 35 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 45 */     return this.intValue;
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
/*    */   public static RMPrincipalSearchAttribute getInstanceFromInt(int intValue)
/*    */   {
/* 59 */     if (intValue == None.intValue)
/* 60 */       return None;
/* 61 */     if (intValue == ShortName.intValue)
/* 62 */       return ShortName;
/* 63 */     if (intValue == DisplayName.intValue) {
/* 64 */       return DisplayName;
/*    */     }
/* 66 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMPrincipalSearchAttribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */