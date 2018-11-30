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
/*    */ public enum RMWorkflowStatus
/*    */ {
/* 17 */   NotStarted(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 22 */   Started(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 27 */   Completed(3);
/*    */   
/*    */   private static Map<Integer, RMWorkflowStatus> ByIntValueMap;
/*    */   
/* 31 */   static { ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 34 */     for (RMWorkflowStatus dataType : values())
/*    */     {
/* 36 */       ByIntValueMap.put(Integer.valueOf(dataType.intValue), dataType);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private RMWorkflowStatus(int intValue)
/*    */   {
/* 44 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 54 */     return this.intValue;
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
/*    */   public static RMWorkflowStatus getInstanceFromInt(int intValue)
/*    */   {
/* 68 */     return (RMWorkflowStatus)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMWorkflowStatus.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */