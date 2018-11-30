/*     */ package com.ibm.jarm.api.property;
/*     */ 
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RMFilterElement
/*     */ {
/*  15 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Integer maxRecursion;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Long maxContentSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Boolean levelDependents;
/*     */   
/*     */ 
/*     */ 
/*     */   private RMFilteredPropertyType filteredPropertyType;
/*     */   
/*     */ 
/*     */ 
/*     */   private String symbolicNames;
/*     */   
/*     */ 
/*     */ 
/*     */   private Integer pageSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMFilterElement(Integer maxRecursion, Long maxContentSize, Boolean levelDependents, RMFilteredPropertyType propertyType, Integer pageSize)
/*     */   {
/*  49 */     Tracer.traceMethodEntry(new Object[] { maxRecursion, maxContentSize, levelDependents, propertyType, pageSize });
/*  50 */     this.maxRecursion = maxRecursion;
/*  51 */     this.maxContentSize = maxContentSize;
/*  52 */     this.levelDependents = levelDependents;
/*  53 */     this.filteredPropertyType = propertyType;
/*  54 */     this.pageSize = pageSize;
/*  55 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */ 
/*     */ 
/*     */   public RMFilterElement(Integer maxRecursion, Long maxContentSize, Boolean levelDependents, String symbolicNames, Integer pageSize)
/*     */   {
/*  85 */     Tracer.traceMethodEntry(new Object[] { maxRecursion, maxContentSize, levelDependents, symbolicNames, pageSize });
/*  86 */     this.maxRecursion = maxRecursion;
/*  87 */     this.maxContentSize = maxContentSize;
/*  88 */     this.levelDependents = levelDependents;
/*  89 */     this.symbolicNames = symbolicNames;
/*  90 */     this.pageSize = pageSize;
/*  91 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getMaxRecursion()
/*     */   {
/* 101 */     return this.maxRecursion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Long getMaxContentSize()
/*     */   {
/* 111 */     return this.maxContentSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean getLevelDependents()
/*     */   {
/* 121 */     return this.levelDependents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMFilteredPropertyType getFilteredPropertyType()
/*     */   {
/* 131 */     return this.filteredPropertyType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSymbolicNames()
/*     */   {
/* 142 */     return this.symbolicNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getPageSize()
/*     */   {
/* 152 */     return this.pageSize;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\property\RMFilterElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */