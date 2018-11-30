/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.collection.PageMark;
/*     */ import com.ibm.jarm.api.collection.RMPageIterator;
/*     */ import com.ibm.jarm.api.collection.RMPageMark;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_RMPageIteratorImpl<T>
/*     */   implements RMPageIterator<T>
/*     */ {
/*  22 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */   private Repository repository;
/*     */   
/*     */   private PageIterator jacePageIterator;
/*     */   
/*     */   private IGenerator<T> generator;
/*     */   
/*     */   P8CE_RMPageIteratorImpl(Repository repository, PageIterator jacePageIterator, IGenerator<T> generator)
/*     */   {
/*  32 */     Tracer.traceMethodEntry(new Object[] { repository, jacePageIterator, generator });
/*  33 */     this.repository = repository;
/*  34 */     this.jacePageIterator = jacePageIterator;
/*  35 */     this.generator = generator;
/*  36 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<T> getCurrentPage()
/*     */   {
/*  44 */     Tracer.traceMethodEntry(new Object[0]);
/*  45 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/*  48 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/*  50 */       List<T> resultList = null;
/*  51 */       Object[] jaceObjects = this.jacePageIterator.getCurrentPage();
/*  52 */       if (jaceObjects != null)
/*     */       {
/*  54 */         resultList = new ArrayList(jaceObjects.length);
/*  55 */         for (Object jaceObject : jaceObjects)
/*     */         {
/*  57 */           T jarmObject = this.generator.create(this.repository, jaceObject);
/*  58 */           if (jarmObject != null) {
/*  59 */             resultList.add(jarmObject);
/*     */           }
/*     */         }
/*     */       }
/*  63 */       Tracer.traceMethodExit(new Object[] { resultList });
/*  64 */       return resultList;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/*  68 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/*  72 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PAGE_ITERATOR_CURRENT_PAGE_ERR, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/*  76 */       if (establishedSubject) {
/*  77 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getElementCount()
/*     */   {
/*  86 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     try
/*     */     {
/*  89 */       int result = this.jacePageIterator.getElementCount();
/*     */       
/*  91 */       Tracer.traceMethodExit(new Object[] { Integer.valueOf(result) });
/*  92 */       return result;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  96 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PAGE_ITERATOR_ELEMENT_COUNT_ERR, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMPageMark getPageMark()
/*     */   {
/* 105 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     try
/*     */     {
/* 108 */       RMPageMark result = new P8CE_RMPageMarkImpl(this.jacePageIterator.getPageMark());
/*     */       
/* 110 */       Tracer.traceMethodExit(new Object[] { result });
/* 111 */       return result;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 115 */       throw P8CE_Util.processJaceException(ex, RMErrorCode.RAL_PAGE_ITERATOR_PAGE_MARK_ERROR, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPageSize()
/*     */   {
/* 124 */     return this.jacePageIterator.getPageSize();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean nextPage()
/*     */   {
/* 132 */     Tracer.traceMethodEntry(new Object[0]);
/* 133 */     boolean establishedSubject = false;
/*     */     try
/*     */     {
/* 136 */       establishedSubject = P8CE_Util.associateSubject();
/*     */       
/* 138 */       long startTime = System.currentTimeMillis();
/* 139 */       boolean result = this.jacePageIterator.nextPage();
/* 140 */       long stopTime = System.currentTimeMillis();
/* 141 */       Integer elementCountIndicator = null;
/* 142 */       if (Tracer.isMediumTraceEnabled())
/*     */       {
/* 144 */         elementCountIndicator = result ? Integer.valueOf(this.jacePageIterator.getElementCount()) : null;
/*     */       }
/* 146 */       Tracer.traceExtCall("PageIterator.nextPage", startTime, stopTime, elementCountIndicator, Boolean.valueOf(result), new Object[0]);
/*     */       
/* 148 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 149 */       return result;
/*     */     }
/*     */     catch (RMRuntimeException ex)
/*     */     {
/* 153 */       throw ex;
/*     */     }
/*     */     catch (Exception cause)
/*     */     {
/* 157 */       throw P8CE_Util.processJaceException(cause, RMErrorCode.RAL_PAGE_ITERATOR_NEXT_PAGE_ERR, new Object[0]);
/*     */     }
/*     */     finally
/*     */     {
/* 161 */       if (establishedSubject) {
/* 162 */         P8CE_Util.disassociateSubject();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset(RMPageMark rmPageMark)
/*     */   {
/* 171 */     Tracer.traceMethodEntry(new Object[] { rmPageMark });
/* 172 */     PageMark jacePageMark = ((P8CE_RMPageMarkImpl)rmPageMark).getJacePageMark();
/* 173 */     this.jacePageIterator.reset(jacePageMark);
/* 174 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 182 */     Tracer.traceMethodEntry(new Object[0]);
/* 183 */     this.jacePageIterator.reset();
/* 184 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPageSize(int pageSize)
/*     */   {
/* 192 */     this.jacePageIterator.setPageSize(pageSize);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_RMPageIteratorImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */