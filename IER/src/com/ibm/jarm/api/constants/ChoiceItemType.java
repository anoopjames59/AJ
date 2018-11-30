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
/*    */ public enum ChoiceItemType
/*    */ {
/* 16 */   Integer(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 22 */   String(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 28 */   IntegerMidNode(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 34 */   StringMidNode(3);
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */   private ChoiceItemType(int intValue)
/*    */   {
/* 41 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 51 */     return this.intValue;
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
/*    */   public static ChoiceItemType getInstanceFromInt(int intValue)
/*    */   {
/* 65 */     ChoiceItemType result = null;
/* 66 */     if (intValue == Integer.intValue) {
/* 67 */       result = Integer;
/* 68 */     } else if (intValue == String.intValue) {
/* 69 */       result = String;
/* 70 */     } else if (intValue == IntegerMidNode.intValue) {
/* 71 */       result = IntegerMidNode;
/* 72 */     } else if (intValue == StringMidNode.intValue) {
/* 73 */       result = StringMidNode;
/*    */     }
/* 75 */     return result;
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\ChoiceItemType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */