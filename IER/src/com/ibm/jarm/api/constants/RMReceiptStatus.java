/*    */ package com.ibm.jarm.api.constants;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public enum RMReceiptStatus
/*    */ {
/* 19 */   None(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 24 */   Attached(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 30 */   ParentTransferred(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 36 */   ParentDestroyed(3), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */   ParentDeclassified(4), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 48 */   ParentDowngraded(5), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 54 */   ParentDeleted(6);
/*    */   
/*    */   private static Map<Integer, RMReceiptStatus> ByIntValueMap;
/*    */   
/* 58 */   static { ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 61 */     for (RMReceiptStatus dataType : values())
/*    */     {
/* 63 */       ByIntValueMap.put(Integer.valueOf(dataType.intValue), dataType);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private RMReceiptStatus(int intValue)
/*    */   {
/* 71 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 81 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static RMReceiptStatus getInstanceFromInt(int intValue)
/*    */   {
/* 95 */     return (RMReceiptStatus)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMReceiptStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */