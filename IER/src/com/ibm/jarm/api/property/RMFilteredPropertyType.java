/*     */ package com.ibm.jarm.api.property;
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
/*     */ 
/*     */ public enum RMFilteredPropertyType
/*     */ {
/*  18 */   Any(396), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  23 */   AnyList(397), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  28 */   AnyNonObject(395), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  33 */   AnySingleton(398), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  38 */   ContentData(399), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   EnumOfObject(299), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  48 */   ListOfBinary(201), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   ListOfBoolean(202), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   ListOfDate(203), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  63 */   ListOfDouble(204), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  68 */   ListOfId(205), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  73 */   ListOfLong(206), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  78 */   ListOfObject(207), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  83 */   ListOfString(208), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  88 */   SingletonBinary(101), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  93 */   SingletonBoolean(102), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  98 */   SingletonDate(103), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 103 */   SingletonDouble(104), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 108 */   SingletonId(105), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 113 */   SingletonLong(106), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 118 */   SingletonObject(107), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 123 */   SingletonString(108);
/*     */   
/*     */   private int intValue;
/*     */   
/*     */   private RMFilteredPropertyType(int intValue) {
/* 128 */     this.intValue = intValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntValue()
/*     */   {
/* 138 */     return this.intValue;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\property\RMFilteredPropertyType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */