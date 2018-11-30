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
/*    */ public enum RMPrincipalSearchType
/*    */ {
/* 23 */   None(0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 29 */   Custom(1), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 34 */   PrefixMatch(2), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 39 */   SuffixMatch(3), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 44 */   Contains(4), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 49 */   Exact(5);
/*    */   
/*    */   static {
/* 52 */     ByIntValueMap = new HashMap();
/*    */     
/*    */ 
/* 55 */     for (RMPrincipalSearchType searchType : values())
/*    */     {
/* 57 */       ByIntValueMap.put(Integer.valueOf(searchType.getIntValue()), searchType);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   private RMPrincipalSearchType(int intValue)
/*    */   {
/* 64 */     this.intValue = intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getIntValue()
/*    */   {
/* 74 */     return this.intValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Map<Integer, RMPrincipalSearchType> ByIntValueMap;
/*    */   
/*    */ 
/*    */   private int intValue;
/*    */   
/*    */ 
/*    */   public static RMPrincipalSearchType getInstanceFromInt(int intValue)
/*    */   {
/* 88 */     return (RMPrincipalSearchType)ByIntValueMap.get(Integer.valueOf(intValue));
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMPrincipalSearchType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */