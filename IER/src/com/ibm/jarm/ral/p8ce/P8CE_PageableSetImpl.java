/*     */ package com.ibm.jarm.ral.p8ce;
/*     */ 
/*     */ import com.filenet.api.collection.EngineSet;
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.collection.RMPageIterator;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class P8CE_PageableSetImpl<T>
/*     */   implements PageableSet<T>
/*     */ {
/*  22 */   private static JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE);
/*     */   
/*     */   private Repository repository;
/*     */   private EngineSet jaceEngineSet;
/*  26 */   private boolean supportsPaging = false;
/*     */   private IGenerator<T> generator;
/*     */   
/*     */   <G extends IGenerator<T>> P8CE_PageableSetImpl(Repository repository, EngineSet jaceEngineSet, boolean supportsPaging, G generator)
/*     */   {
/*  31 */     Tracer.traceMethodEntry(new Object[] { repository, jaceEngineSet, Boolean.valueOf(supportsPaging), generator });
/*  32 */     this.repository = repository;
/*  33 */     this.jaceEngineSet = jaceEngineSet;
/*  34 */     this.supportsPaging = supportsPaging;
/*  35 */     this.generator = generator;
/*  36 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean supportsPaging()
/*     */   {
/*  44 */     return this.supportsPaging;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  52 */     return this.jaceEngineSet.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<T> iterator()
/*     */   {
/*  60 */     Tracer.traceMethodEntry(new Object[0]);
/*  61 */     Iterator<T> result = new BaseIterator();
/*  62 */     Tracer.traceMethodExit(new Object[] { result });
/*  63 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public RMPageIterator<T> pageIterator()
/*     */   {
/*  71 */     Tracer.traceMethodEntry(new Object[0]);
/*  72 */     if (!this.supportsPaging)
/*     */     {
/*  74 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.RAL_PAGING_NOT_SUPPORTED, new Object[0]);
/*     */     }
/*     */     
/*  77 */     PageIterator jacePageIterator = this.jaceEngineSet.pageIterator();
/*  78 */     RMPageIterator<T> result = new P8CE_RMPageIteratorImpl(this.repository, jacePageIterator, this.generator);
/*     */     
/*  80 */     Tracer.traceMethodExit(new Object[] { result });
/*  81 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer totalCount()
/*     */   {
/*  89 */     Tracer.traceMethodEntry(new Object[0]);
/*  90 */     Integer result = null;
/*  91 */     Method getTotalCountMethod = null;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  96 */       Class<?> jacePIClass = PageIterator.class;
/*  97 */       getTotalCountMethod = jacePIClass.getMethod("getTotalCount", new Class[0]);
/*  98 */       if (getTotalCountMethod != null)
/*     */       {
/* 100 */         PageIterator jacePageIterator = this.jaceEngineSet.pageIterator();
/* 101 */         result = (Integer)getTotalCountMethod.invoke(jacePageIterator, new Object[0]);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 109 */     Tracer.traceMethodExit(new Object[] { result });
/* 110 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   class BaseIterator
/*     */     implements Iterator<T>
/*     */   {
/*     */     private Iterator<IndependentObject> jaceObjectIterator;
/*     */     
/*     */ 
/*     */     BaseIterator() {}
/*     */     
/*     */     public boolean hasNext()
/*     */     {
/* 124 */       P8CE_PageableSetImpl.Tracer.traceMethodEntry(new Object[0]);
/* 125 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 128 */         establishedSubject = P8CE_Util.associateSubject();
/*     */         
/* 130 */         boolean result = getJaceObjectIterator().hasNext();
/* 131 */         P8CE_PageableSetImpl.Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 132 */         return result;
/*     */       }
/*     */       finally
/*     */       {
/* 136 */         if (establishedSubject) {
/* 137 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public T next()
/*     */     {
/* 146 */       P8CE_PageableSetImpl.Tracer.traceMethodEntry(new Object[0]);
/* 147 */       boolean establishedSubject = false;
/*     */       try
/*     */       {
/* 150 */         establishedSubject = P8CE_Util.associateSubject();
/*     */         
/*     */ 
/* 153 */         Object jaceObject = getJaceObjectIterator().next();
/* 154 */         T result = P8CE_PageableSetImpl.this.generator.create(P8CE_PageableSetImpl.this.repository, jaceObject);
/* 155 */         P8CE_PageableSetImpl.Tracer.traceMethodExit(new Object[] { result });
/* 156 */         return result;
/*     */       }
/*     */       finally
/*     */       {
/* 160 */         if (establishedSubject) {
/* 161 */           P8CE_Util.disassociateSubject();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void remove()
/*     */     {
/* 170 */       P8CE_PageableSetImpl.Tracer.traceMethodEntry(new Object[0]);
/* 171 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.API_OPERATION_NOT_SUPPORTED, new Object[] { "RMPageIterator.Iterator.remove", "any", "any" });
/*     */     }
/*     */     
/*     */ 
/*     */     private Iterator<IndependentObject> getJaceObjectIterator()
/*     */     {
/* 177 */       P8CE_PageableSetImpl.Tracer.traceMethodEntry(new Object[0]);
/* 178 */       if (this.jaceObjectIterator == null)
/*     */       {
/* 180 */         this.jaceObjectIterator = P8CE_PageableSetImpl.this.jaceEngineSet.iterator();
/*     */       }
/*     */       
/* 183 */       P8CE_PageableSetImpl.Tracer.traceMethodExit(new Object[] { this.jaceObjectIterator });
/* 184 */       return this.jaceObjectIterator;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\P8CE_PageableSetImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */