/*    */ package com.ibm.jarm.ral.p8ce.cbr;
/*    */ 
/*    */ import com.filenet.api.collection.PageMark;
/*    */ import com.ibm.jarm.api.core.Repository;
/*    */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*    */ import com.ibm.jarm.api.util.JarmTracer;
/*    */ 
/*    */ class P8CE_CBRAndFPOSImplementor
/*    */   extends P8CE_CBRSearchImplementor
/*    */ {
/*    */   P8CE_CBRAndFPOSImplementor(Repository fpos, RMContentSearchDefinition srchDef, int pgSize)
/*    */   {
/* 13 */     super(fpos, srchDef, pgSize);
/* 14 */     Tracer.traceMethodEntry(new Object[] { fpos, srchDef, Integer.valueOf(pgSize) });
/* 15 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initCBRSearch(boolean continuable)
/*    */   {
/* 24 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(continuable) });
/* 25 */     initializePagedCBRSearch();
/* 26 */     initializeCBRPageIteratorList();
/* 27 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected P8CE_CBRPage getNextPage(P8CE_CBRPage lastPage)
/*    */     throws Exception
/*    */   {
/* 36 */     Tracer.traceMethodEntry(new Object[] { lastPage });
/* 37 */     P8CE_CBRPage thisPage = new P8CE_CBRPage(this.pageSize);
/* 38 */     doGetNextPage(thisPage, lastPage, true);
/* 39 */     Tracer.traceMethodExit(new Object[] { thisPage });
/* 40 */     return thisPage;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected P8CE_CBRPage getPreviousPage(P8CE_CBRPage prevPage, P8CE_CBRPage prev2Page)
/*    */     throws Exception
/*    */   {
/* 49 */     Tracer.traceMethodEntry(new Object[] { prevPage, prev2Page });
/* 50 */     P8CE_CBRPage thisPage = new P8CE_CBRPage(this.pageSize);
/*    */     
/* 52 */     PageMark startMark = prevPage.getBegPageMark();
/* 53 */     this.cbrInprocPageInfo.setPageStart(startMark, false);
/*    */     
/* 55 */     if (Tracer.isMaximumTraceEnabled()) {
/* 56 */       Tracer.traceMaximumMsg("Inside P8CE_CBRAndFPOSImplementor::getPreviousPage, set STARTING PAGE MARK: {0}.", new Object[] { startMark });
/*    */     }
/* 58 */     doGetNextPage(thisPage, prev2Page, false);
/* 59 */     Tracer.traceMethodExit(new Object[] { thisPage });
/* 60 */     return thisPage;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean hasNext()
/*    */   {
/* 69 */     Tracer.traceMethodEntry(new Object[0]);
/* 70 */     if (!hasNextPageCBR())
/*    */     {
/* 72 */       boolean ret = hasCarryOverItem();
/* 73 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/* 74 */       return ret;
/*    */     }
/*    */     
/*    */ 
/* 78 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(this.currentPageFilled) });
/* 79 */     return this.currentPageFilled;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean hasPrevious()
/*    */   {
/* 88 */     Tracer.traceMethodEntry(new Object[0]);
/* 89 */     boolean ret = this.cbrInprocPageInfo.hasPreviousPage();
/* 90 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/* 91 */     return ret;
/*    */   }
/*    */   
/*    */   protected void doGetNextPage(P8CE_CBRPage thisPage, P8CE_CBRPage lastPage, boolean setPageStartMarker) throws Exception
/*    */   {
/* 96 */     Tracer.traceMethodEntry(new Object[] { thisPage, lastPage, Boolean.valueOf(setPageStartMarker) });
/* 97 */     initCBRStartMark(thisPage, lastPage, setPageStartMarker);
/* 98 */     doNextPageCBRAndFilterFpos(thisPage, false);
/* 99 */     Tracer.traceMethodExit(new Object[0]);
/*    */   }
/*    */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBRAndFPOSImplementor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */