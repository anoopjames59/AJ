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
/*    */ 
/*    */ public enum DispositionTriggerType
/*    */ {
/* 24 */   PredefinedDateTrigger(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 30 */   ExternalEventTrigger(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 36 */   InternalEventTrigger(3), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 41 */   RecurringEventTigger(4);
/*    */   
/*    */   static {
/* 44 */     ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 47 */     for (DispositionTriggerType triggerType : values())
/*    */     {
/* 49 */       ByIntValueMap.put(Integer.valueOf(triggerType.intValue), triggerType);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private DispositionTriggerType(int intValue)
/*    */   {
/* 57 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 67 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Map<Integer, DispositionTriggerType> ByIntValueMap;
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */   public static DispositionTriggerType getInstanceFromInt(int intValue)
/*    */   {
/* 81 */     return (DispositionTriggerType)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\DispositionTriggerType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */