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
/*    */ public enum RetainMetadata
/*    */ {
/* 19 */   AlwaysRetain(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 24 */   NeverRetain(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 29 */   PromptUser(2);
/*    */   
/*    */   private int intValue;
/*    */   
/*    */   private RetainMetadata(int intValue)
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
/*    */   public static RetainMetadata getInstanceFromInt(int intValue)
/*    */   {
/* 59 */     RetainMetadata result = null;
/*    */     
/* 61 */     switch (intValue)
/*    */     {
/*    */     case 0: 
/* 64 */       result = AlwaysRetain;
/* 65 */       break;
/*    */     case 1: 
/* 67 */       result = NeverRetain;
/* 68 */       break;
/*    */     case 2: 
/* 70 */       result = PromptUser;
/*    */     }
/*    */     
/*    */     
/* 74 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RetainMetadata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */