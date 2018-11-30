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
/*    */ public enum RMPropertySettability
/*    */ {
/* 19 */   ReadWrite(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 25 */   SettableOnlyBeforeCheckIn(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 31 */   SettableOnlyOnCreate(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 36 */   ReadOnly(3);
/*    */   
/*    */   static {
/* 39 */     ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 42 */     for (RMPropertySettability item : values())
/*    */     {
/* 44 */       ByIntValueMap.put(Integer.valueOf(item.intValue), item);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private RMPropertySettability(int intValue)
/*    */   {
/* 53 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 63 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Map<Integer, RMPropertySettability> ByIntValueMap;
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */   public static RMPropertySettability getInstanceFromInt(int intValue)
/*    */   {
/* 77 */     return (RMPropertySettability)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMPropertySettability.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */