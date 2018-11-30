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
/*     */ public enum DataModelType
/*     */ {
/*  20 */   Base(603, "Base"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  25 */   DoDBase(601, "DOD-5015.2"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  30 */   DoDClassified(604, "DOD-5015.2 Classified"), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   PRO2002(602, "PRO-2002");
/*     */   
/*     */   static {
/*  38 */     ByIntValueMap = new HashMap();
/*  39 */     ByLabelValueMap = new HashMap();
/*     */     
/*     */ 
/*  42 */     for (DataModelType dataModelType : values())
/*     */     {
/*  44 */       ByIntValueMap.put(Integer.valueOf(dataModelType.getIntValue()), dataModelType);
/*  45 */       ByLabelValueMap.put(dataModelType.getLabel().toUpperCase(), dataModelType);
/*     */     }
/*     */     
/*  48 */     ByLabelValueMap.put("DOD_5015.2CHAPTER4", DoDClassified);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Map<Integer, DataModelType> ByIntValueMap;
/*     */   
/*     */   private DataModelType(int intValue, String label)
/*     */   {
/*  57 */     this.intValue = intValue;
/*  58 */     this.label = label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Map<String, DataModelType> ByLabelValueMap;
/*     */   
/*     */ 
/*     */   public int getIntValue()
/*     */   {
/*  68 */     return this.intValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLabel()
/*     */   {
/*  78 */     return this.label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int intValue;
/*     */   
/*     */ 
/*     */   private String label;
/*     */   
/*     */   public static DataModelType getInstanceFromInt(int intValue)
/*     */   {
/*  90 */     return (DataModelType)ByIntValueMap.get(Integer.valueOf(intValue));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DataModelType getInstanceFromString(String label)
/*     */   {
/* 102 */     DataModelType result = null;
/* 103 */     if (label != null)
/*     */     {
/* 105 */       result = (DataModelType)ByLabelValueMap.get(label.trim().toUpperCase());
/*     */     }
/*     */     
/* 108 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\DataModelType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */