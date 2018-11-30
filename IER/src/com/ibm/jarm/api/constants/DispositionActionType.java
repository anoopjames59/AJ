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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DispositionActionType
/*    */ {
/* 23 */   Review(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 28 */   Export(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 33 */   Transfer(3), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 38 */   Destroy(4), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 43 */   InterimTransfer(5), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 48 */   Cutoff(6), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 53 */   VitalReview(7), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 58 */   AutoDestroy(8);
/*    */   
/*    */   static {
/* 61 */     ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 64 */     for (DispositionActionType actionType : values())
/*    */     {
/* 66 */       ByIntValueMap.put(Integer.valueOf(actionType.intValue), actionType);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private DispositionActionType(int intValue)
/*    */   {
/* 74 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 84 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Map<Integer, DispositionActionType> ByIntValueMap;
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */   public static DispositionActionType getInstanceFromInt(int intValue)
/*    */   {
/* 98 */     return (DispositionActionType)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\DispositionActionType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */