/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ 
/*     */ class P8CE_FPOSImplementor extends P8CE_CBRSearchImplementor
/*     */ {
/*     */   public P8CE_FPOSImplementor(Repository fpos, RMContentSearchDefinition srchDef, int pgSize)
/*     */   {
/*  14 */     super(fpos, srchDef, pgSize);
/*  15 */     Tracer.traceMethodEntry(new Object[] { fpos, srchDef, Integer.valueOf(pgSize) });
/*  16 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasNext()
/*     */   {
/*  25 */     Tracer.traceMethodEntry(new Object[0]);
/*  26 */     boolean ret = this.fposInprocPageInfo.hasNextPage();
/*  27 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/*  28 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasPrevious()
/*     */   {
/*  37 */     Tracer.traceMethodEntry(new Object[0]);
/*  38 */     boolean ret = this.fposInprocPageInfo.hasPreviousPage();
/*  39 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/*  40 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initCBRSearch(boolean continuable)
/*     */   {
/*  49 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(continuable) });
/*  50 */     initializePagedFposSearchIterator();
/*  51 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getNextPage(P8CE_CBRPage lastPage)
/*     */     throws Exception
/*     */   {
/*  60 */     Tracer.traceMethodEntry(new Object[] { lastPage });
/*  61 */     Object[] ret = this.fposInprocPageInfo.nextPage();
/*  62 */     P8CE_CBRPage thisPage = makeCBRPage(ret);
/*     */     
/*  64 */     Tracer.traceMethodExit(new Object[] { thisPage });
/*  65 */     return thisPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getPreviousPage(P8CE_CBRPage prevPage, P8CE_CBRPage prev2Page)
/*     */     throws Exception
/*     */   {
/*  74 */     Tracer.traceMethodEntry(new Object[] { prevPage, prev2Page });
/*  75 */     Object[] ret = this.fposInprocPageInfo.previousPage();
/*  76 */     P8CE_CBRPage thisPage = makeCBRPage(ret);
/*     */     
/*  78 */     Tracer.traceMethodExit(new Object[] { thisPage });
/*  79 */     return thisPage;
/*     */   }
/*     */   
/*     */ 
/*     */   private P8CE_CBRPage makeCBRPage(Object[] ret)
/*     */   {
/*  85 */     Tracer.traceMethodEntry(new Object[] { ret });
/*  86 */     P8CE_CBRPage thisPage = new P8CE_CBRPage(this.pageSize);
/*  87 */     if (ret == null)
/*     */     {
/*  89 */       Tracer.traceMethodExit(new Object[] { thisPage });
/*  90 */       return thisPage;
/*     */     }
/*     */     
/*  93 */     for (Object obj : ret)
/*     */     {
/*  95 */       if ((obj instanceof RepositoryRow))
/*     */       {
/*  97 */         RepositoryRow rr = (RepositoryRow)obj;
/*  98 */         Properties props = rr.getProperties();
/*     */         
/* 100 */         P8CE_CBRResultItem item = new P8CE_CBRResultItem(this.jarmRepository, rr);
/* 101 */         item.setScore(0.0D);
/* 102 */         if ((props != null) && (props.isPropertyPresent("Id"))) {
/* 103 */           item.setId(props.getIdValue("Id").toString());
/*     */         }
/* 105 */         if (thisPage.isPageFull()) {
/* 106 */           thisPage.addItemForNextPage(item);
/*     */         } else {
/* 108 */           thisPage.addItem(item);
/*     */         }
/*     */       }
/*     */     }
/* 112 */     Tracer.traceMethodExit(new Object[] { thisPage });
/* 113 */     return thisPage;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_FPOSImplementor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */