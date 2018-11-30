/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.ibm.jarm.api.collection.PageableSet;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.query.CBRResult;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_RepositoryImpl;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class P8CE_CBRSearch
/*     */ {
/*  17 */   private static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE_CBR);
/*     */   
/*     */   private Repository jarmRepository;
/*  20 */   private ObjectStore filePlanObjectStore = null;
/*     */   
/*     */ 
/*     */   private P8CE_CBRSearchImplementor srchImpl;
/*     */   
/*     */ 
/*     */   private int pageSize;
/*     */   
/*     */ 
/*     */   public P8CE_CBRSearch(Repository repository)
/*     */   {
/*  31 */     Tracer.traceMethodEntry(new Object[] { repository });
/*  32 */     if (repository != null)
/*     */     {
/*  34 */       this.jarmRepository = repository;
/*  35 */       this.filePlanObjectStore = ((P8CE_RepositoryImpl)this.jarmRepository).getJaceObjectStore();
/*     */     }
/*  37 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   public PageableSet<CBRResult> initCBRSearch(RMContentSearchDefinition srchDef, int pageSize, boolean continuable)
/*     */   {
/*  52 */     Tracer.traceMethodEntry(new Object[] { srchDef, Integer.valueOf(pageSize), Boolean.valueOf(continuable) });
/*  53 */     this.pageSize = pageSize;
/*  54 */     this.srchImpl = P8CE_CBRSearchImplementor.createInstance(this.jarmRepository, srchDef, pageSize);
/*  55 */     this.srchImpl.initCBRSearch(continuable);
/*     */     
/*  57 */     PageableSet<CBRResult> ps = new P8CE_CBRPageableSetImpl(this.jarmRepository, this, continuable);
/*     */     
/*  59 */     Tracer.traceMethodExit(new Object[] { ps });
/*  60 */     return ps;
/*     */   }
/*     */   
/*     */   protected ObjectStore getJaceFPOS()
/*     */   {
/*  65 */     return this.filePlanObjectStore;
/*     */   }
/*     */   
/*     */   protected boolean hasNext()
/*     */   {
/*  70 */     Tracer.traceMethodEntry(new Object[0]);
/*  71 */     boolean result = this.srchImpl.hasNext();
/*  72 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  73 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean hasPrevious()
/*     */   {
/*  78 */     Tracer.traceMethodEntry(new Object[0]);
/*  79 */     boolean result = this.srchImpl.hasPrevious();
/*  80 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/*  81 */     return result;
/*     */   }
/*     */   
/*     */   protected List<CBRResult> previousPage()
/*     */   {
/*  86 */     Tracer.traceMethodEntry(new Object[0]);
/*  87 */     List<CBRResult> results = null;
/*     */     
/*  89 */     if (this.srchImpl.hasPrevious()) {
/*  90 */       results = this.srchImpl.previousPage();
/*     */     } else {
/*  92 */       results = new ArrayList();
/*     */     }
/*  94 */     Tracer.traceMethodExit(new Object[] { results });
/*  95 */     return results;
/*     */   }
/*     */   
/*     */   protected List<CBRResult> nextPage()
/*     */   {
/* 100 */     Tracer.traceMethodEntry(new Object[0]);
/* 101 */     List<CBRResult> results = null;
/*     */     
/* 103 */     if (this.srchImpl.hasNext()) {
/* 104 */       results = this.srchImpl.nextPage();
/*     */     } else {
/* 106 */       results = new ArrayList();
/*     */     }
/* 108 */     Tracer.traceMethodExit(new Object[] { results });
/* 109 */     return results;
/*     */   }
/*     */   
/*     */   protected int getPageSize()
/*     */   {
/* 114 */     return this.pageSize;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBRSearch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */