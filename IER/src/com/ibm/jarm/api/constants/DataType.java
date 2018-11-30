/*     */ package com.ibm.jarm.api.constants;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum DataType
/*     */ {
/*  20 */   Binary(1), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  25 */   Boolean(2), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  30 */   DateTime(3), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   Double(4), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   Guid(5), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  45 */   Integer(6), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   Object(7), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   String(8);
/*     */   
/*     */ 
/*     */   private static Map<Integer, DataType> ByIntValueMap;
/*     */   private int intValue;
/*     */   
/*     */   static
/*     */   {
/*  63 */     ByIntValueMap = new HashMap();
/*     */     
/*     */ 
/*  66 */     for (DataType dataType : values())
/*     */     {
/*  68 */       ByIntValueMap.put(Integer.valueOf(dataType.intValue), dataType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private DataType(int intValue)
/*     */   {
/*  76 */     this.intValue = intValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntValue()
/*     */   {
/*  86 */     return this.intValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DataType getInstanceFromInt(int intValue)
/*     */   {
/* 100 */     return (DataType)ByIntValueMap.get(Integer.valueOf(intValue));
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\DataType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */