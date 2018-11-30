/*     */ package com.ibm.jarm.api.property;
/*     */ 
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.api.util.Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class RMPropertyFilter
/*     */ {
/*  25 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.Api);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public static final RMPropertyFilter MinimumPropertySet = new UnmodifiablePropertyFilter(new RMPropertyFilter());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  39 */   public static final RMPropertyFilter AllNonObjectPropertySet = new UnmodifiablePropertyFilter(new RMPropertyFilter());
/*     */   
/*  41 */   private static final List<String> EMPTY_STR_LIST = Collections.emptyList();
/*  42 */   private static final List<RMFilterElement> EMPTY_FE_LIST = Collections.emptyList();
/*     */   
/*     */   private Integer globalMaxRecursion;
/*     */   
/*     */   private Long globalMaxContentSize;
/*     */   
/*     */   private Boolean globalLevelDependents;
/*     */   
/*     */   private Integer globalPageSize;
/*     */   private List<String> excludedPropertyNames;
/*     */   private List<RMFilterElement> includeFilterElements;
/*     */   
/*     */   public RMPropertyFilter()
/*     */   {
/*  56 */     Tracer.traceMethodEntry(new Object[0]);
/*  57 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public void addExcludeProperty(String symbolicNames)
/*     */   {
/*  70 */     Tracer.traceMethodEntry(new Object[] { symbolicNames });
/*  71 */     Util.ckInvalidStrParam("symbolicName", symbolicNames);
/*     */     
/*  73 */     if (this.excludedPropertyNames == null)
/*     */     {
/*  75 */       this.excludedPropertyNames = new ArrayList(1);
/*     */     }
/*  77 */     this.excludedPropertyNames.add(symbolicNames);
/*  78 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addIncludeProperty(RMFilterElement filterElement)
/*     */   {
/*  90 */     Tracer.traceMethodEntry(new Object[] { filterElement });
/*  91 */     Util.ckNullObjParam("filterElement", filterElement);
/*     */     
/*  93 */     if (this.includeFilterElements == null)
/*     */     {
/*  95 */       this.includeFilterElements = new ArrayList(1);
/*     */     }
/*     */     
/*  98 */     this.includeFilterElements.add(filterElement);
/*  99 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public void addIncludeProperty(Integer maxRecursion, Long maxContentSize, Boolean levelDependents, String symbolicNames, Integer pageSize)
/*     */   {
/* 126 */     Tracer.traceMethodEntry(new Object[] { maxRecursion, maxContentSize, levelDependents, symbolicNames, pageSize });
/* 127 */     RMFilterElement fe = new RMFilterElement(maxRecursion, maxContentSize, levelDependents, symbolicNames, pageSize);
/* 128 */     addIncludeProperty(fe);
/* 129 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public void addIncludeType(Integer maxRecursion, Long maxContentSize, Boolean levelDependents, RMFilteredPropertyType propertyType, Integer pageSize)
/*     */   {
/* 155 */     Tracer.traceMethodEntry(new Object[] { maxRecursion, maxContentSize, levelDependents, propertyType, pageSize });
/* 156 */     RMFilterElement fe = new RMFilterElement(maxRecursion, maxContentSize, levelDependents, propertyType, pageSize);
/* 157 */     addIncludeProperty(fe);
/* 158 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getExcludePropertyNames()
/*     */   {
/* 169 */     Tracer.traceMethodEntry(new Object[0]);
/* 170 */     List<String> result = EMPTY_STR_LIST;
/* 171 */     if (this.excludedPropertyNames != null)
/*     */     {
/* 173 */       result = Collections.unmodifiableList(this.excludedPropertyNames);
/*     */     }
/*     */     
/* 176 */     Tracer.traceMethodExit(new Object[] { result });
/* 177 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<RMFilterElement> getIncludeProperties()
/*     */   {
/* 189 */     Tracer.traceMethodEntry(new Object[0]);
/* 190 */     List<RMFilterElement> result = EMPTY_FE_LIST;
/* 191 */     if (this.includeFilterElements != null)
/*     */     {
/* 193 */       result = Collections.unmodifiableList(this.includeFilterElements);
/*     */     }
/*     */     
/* 196 */     Tracer.traceMethodExit(new Object[] { result });
/* 197 */     return result;
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
/*     */   public void setLevelDependents(boolean levelDependents)
/*     */   {
/* 211 */     this.globalLevelDependents = Boolean.valueOf(levelDependents);
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
/*     */   public void setLevelDependents(Boolean levelDependents)
/*     */   {
/* 226 */     this.globalLevelDependents = levelDependents;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean getLevelDependents()
/*     */   {
/* 235 */     return this.globalLevelDependents;
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
/*     */   public void setMaxRecursion(int maxRecursion)
/*     */   {
/* 248 */     this.globalMaxRecursion = Integer.valueOf(maxRecursion);
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
/*     */   public void setMaxRecursion(Integer maxRecursion)
/*     */   {
/* 262 */     this.globalMaxRecursion = maxRecursion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getMaxRecursion()
/*     */   {
/* 272 */     return this.globalMaxRecursion;
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
/*     */   public void setMaxContentSize(long maxContentSize)
/*     */   {
/* 286 */     this.globalMaxContentSize = Long.valueOf(maxContentSize);
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
/*     */   public void setMaxContentSize(Long maxContentSize)
/*     */   {
/* 300 */     this.globalMaxContentSize = maxContentSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Long getMaxContentSize()
/*     */   {
/* 310 */     return this.globalMaxContentSize;
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
/*     */   public void setPageSize(int pageSize)
/*     */   {
/* 324 */     this.globalPageSize = Integer.valueOf(pageSize);
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
/*     */   public void setPageSize(Integer pageSize)
/*     */   {
/* 338 */     this.globalPageSize = pageSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getPageSize()
/*     */   {
/* 347 */     return this.globalPageSize;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\api\property\RMPropertyFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */