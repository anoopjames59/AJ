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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SchedulePropagation
/*    */ {
/* 28 */   NoPropagation(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 34 */   ToAllInheritors(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */   ToImmediateSubContainersAndAllInheritors(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 50 */   ForceInheritanceUponNonAssignedSubContainers(3);
/*    */   
/*    */   static {
/* 53 */     ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 56 */     for (SchedulePropagation sheduleProp : values())
/*    */     {
/* 58 */       ByIntValueMap.put(Integer.valueOf(sheduleProp.intValue), sheduleProp);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private SchedulePropagation(int intValue)
/*    */   {
/* 66 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 76 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Map<Integer, SchedulePropagation> ByIntValueMap;
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */   public static SchedulePropagation getInstanceFromInt(int intValue)
/*    */   {
/* 90 */     return (SchedulePropagation)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\SchedulePropagation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */