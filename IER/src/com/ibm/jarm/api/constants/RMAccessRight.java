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
/*     */ 
/*     */ public enum RMAccessRight
/*     */ {
/*  18 */   AddMarking(33554432), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  24 */   ChangeState(1024), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  30 */   CreateInstance(256), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  36 */   Delete(65536), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  42 */   Link(16), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */   MajorVersion(4), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   MinorVersion(64), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   None(0), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   Read(1), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   ReadACL(131072), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   RemoveMarking(67108864), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */   Unlink(32), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  88 */   UseMarking(134217728), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   ViewContent(128), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */   Write(2), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 107 */   WriteACL(262144), 
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 113 */   WriteAnyOwner(524288);
/*     */   
/*     */ 
/*     */   private int intValue;
/*     */   
/*     */   private RMAccessRight(int intValue)
/*     */   {
/* 120 */     this.intValue = intValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getIntValue()
/*     */   {
/* 130 */     return this.intValue;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\constants\RMAccessRight.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */