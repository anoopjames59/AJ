/*     */ package com.ibm.jarm.api.constants;
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
/*     */ public enum RMAccessLevel
/*     */ {
/*  17 */   FullControl(999415), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  23 */   FullControlCustomObject(995603), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  29 */   FullControlDefault(995587), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  35 */   FullControlDocument(998871), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   FullControlFolder(999415), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  46 */   FullControlMarking(234881024), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  51 */   LinkCustomObject(131089), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  56 */   LinkFolder(131121), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  61 */   MajorVersionDocument(132599), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  66 */   MinorVersionDocument(132595), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   Read(131073), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   View(131201), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  82 */   WriteCustomObject(131347), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */   WriteDefault(131331), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   WriteDocument(132531), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  99 */   WriteFolder(135155);
/*     */   
/*     */ 
/*     */   private int intValue;
/*     */   
/*     */   private RMAccessLevel(int intValue)
/*     */   {
/* 106 */     this.intValue = intValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntValue()
/*     */   {
/* 116 */     return this.intValue;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMAccessLevel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */