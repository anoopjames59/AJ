/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.collection.PageMark;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ class P8CE_FPOSAndCBRImplementor
/*     */   extends P8CE_CBRSearchImplementor
/*     */ {
/*     */   P8CE_FPOSAndCBRImplementor(Repository fpos, RMContentSearchDefinition srchDef, int pgSize)
/*     */   {
/*  18 */     super(fpos, srchDef, pgSize);
/*  19 */     Tracer.traceMethodEntry(new Object[] { fpos, srchDef, Integer.valueOf(pgSize) });
/*  20 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initCBRSearch(boolean continuable)
/*     */   {
/*  29 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(continuable) });
/*  30 */     initializePagedCBRSearch();
/*  31 */     initializePagedFposSearchIterator();
/*  32 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getNextPage(P8CE_CBRPage lastPage)
/*     */     throws Exception
/*     */   {
/*  41 */     Tracer.traceMethodEntry(new Object[] { lastPage });
/*  42 */     P8CE_CBRPage thisPage = new P8CE_CBRPage(this.pageSize);
/*  43 */     doGetNextPage(thisPage, lastPage, true);
/*  44 */     Tracer.traceMethodExit(new Object[] { thisPage });
/*  45 */     return thisPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getPreviousPage(P8CE_CBRPage prevPage, P8CE_CBRPage prev2Page)
/*     */     throws Exception
/*     */   {
/*  54 */     Tracer.traceMethodEntry(new Object[] { prevPage, prev2Page });
/*  55 */     P8CE_CBRPage thisPage = new P8CE_CBRPage(this.pageSize);
/*     */     
/*  57 */     PageMark startMark = prevPage.getBegPageMark();
/*  58 */     this.fposInprocPageInfo.setPageStart(startMark, false);
/*     */     
/*  60 */     doGetNextPage(thisPage, prev2Page, false);
/*  61 */     Tracer.traceMethodExit(new Object[] { thisPage });
/*  62 */     return thisPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasNext()
/*     */   {
/*  71 */     Tracer.traceMethodEntry(new Object[0]);
/*  72 */     if (!hasNextPageFPOS())
/*     */     {
/*  74 */       boolean ret = hasCarryOverItem();
/*  75 */       Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/*  76 */       return ret;
/*     */     }
/*  78 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(this.currentPageFilled) });
/*     */     
/*  80 */     return this.currentPageFilled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasPrevious()
/*     */   {
/*  89 */     Tracer.traceMethodEntry(new Object[0]);
/*  90 */     boolean ret = this.fposInprocPageInfo.hasPreviousPage();
/*  91 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/*  92 */     return ret;
/*     */   }
/*     */   
/*     */   private void doGetNextPage(P8CE_CBRPage thisPage, P8CE_CBRPage lastPage, boolean setPageStartMarker) throws Exception
/*     */   {
/*  97 */     Tracer.traceMethodEntry(new Object[] { thisPage, lastPage, Boolean.valueOf(setPageStartMarker) });
/*  98 */     initFPOSStartMark(thisPage, lastPage, true);
/*     */     
/* 100 */     while (hasNextPageFPOS())
/*     */     {
/* 102 */       Object[] fposRowSet = this.fposInprocPageInfo.nextPage();
/* 103 */       if (Tracer.isMaximumTraceEnabled()) {
/* 104 */         Tracer.traceMaximumMsg("fposInprocPageInfo.nextPage returns items: {0}.", fposRowSet);
/*     */       }
/* 106 */       List<String> fposIdList = collectRowSetIds(Arrays.asList(fposRowSet));
/*     */       
/*     */ 
/* 109 */       Map<String, Double> filteredIdMap = filterFposWithCBR(fposIdList, false);
/* 110 */       if (Tracer.isMaximumTraceEnabled()) {
/* 111 */         Tracer.traceMaximumMsg("filterFposWithCBR returns items: {0}.", new Object[] { filteredIdMap.entrySet() });
/*     */       }
/* 113 */       if (!filteredIdMap.isEmpty())
/*     */       {
/*     */ 
/* 116 */         for (int i = 0; i < fposIdList.size(); i++)
/*     */         {
/* 118 */           String fposID = ((String)fposIdList.get(i)).toString();
/* 119 */           if (filteredIdMap.get(fposID) != null)
/*     */           {
/* 121 */             P8CE_CBRResultItem item = new P8CE_CBRResultItem(this.jarmRepository, (RepositoryRow)fposRowSet[i]);
/* 122 */             item.setScore(((Double)filteredIdMap.get(fposID)).doubleValue());
/* 123 */             item.setId(fposID);
/*     */             
/* 125 */             if (thisPage.isPageFull()) {
/* 126 */               thisPage.addItemForNextPage(item);
/*     */             } else {
/* 128 */               thisPage.addItem(item);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 133 */         if (thisPage.isPageFull())
/*     */           break;
/*     */       }
/*     */     }
/* 137 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_FPOSAndCBRImplementor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */