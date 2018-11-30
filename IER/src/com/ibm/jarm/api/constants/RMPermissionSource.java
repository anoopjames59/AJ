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
/*    */ public enum RMPermissionSource
/*    */ {
/* 19 */   Direct(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 24 */   Default(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 29 */   Template(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 34 */   Parent(3), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 39 */   Proxy(255);
/*    */   
/*    */   static {
/* 42 */     ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 45 */     for (RMPermissionSource permSource : values())
/*    */     {
/* 47 */       ByIntValueMap.put(Integer.valueOf(permSource.getIntValue()), permSource);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private RMPermissionSource(int intValue)
/*    */   {
/* 55 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 65 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Map<Integer, RMPermissionSource> ByIntValueMap;
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */   public static RMPermissionSource getInstanceFromInt(int intValue)
/*    */   {
/* 79 */     return (RMPermissionSource)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMPermissionSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */