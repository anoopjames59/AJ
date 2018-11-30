/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.collection.PageMark;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.SortOrder;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ 
/*     */ class P8CE_CBROrFPOSImplementor
/*     */   extends P8CE_CBRSearchImplementor
/*     */ {
/*  15 */   P8CE_CBROrFPOSPage curPage = new P8CE_CBROrFPOSPage(this.pageSize);
/*     */   
/*     */   P8CE_CBROrFPOSImplementor(Repository fpos, RMContentSearchDefinition srchDef, int pgSize)
/*     */   {
/*  19 */     super(fpos, srchDef, pgSize);
/*  20 */     Tracer.traceMethodEntry(new Object[] { fpos, srchDef, Integer.valueOf(pgSize) });
/*  21 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initCBRSearch(boolean continuable)
/*     */   {
/*  30 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(continuable) });
/*  31 */     initializePagedCBRSearch();
/*  32 */     initializeCBRPageIteratorList();
/*  33 */     initializePagedFposSearchIterator();
/*  34 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getNextPage(P8CE_CBRPage lastPage)
/*     */     throws Exception
/*     */   {
/*  43 */     Tracer.traceMethodEntry(new Object[] { lastPage });
/*  44 */     P8CE_CBROrFPOSPage thisPage = new P8CE_CBROrFPOSPage(this.pageSize);
/*  45 */     P8CE_CBROrFPOSPage lastCBROrFPOSPage = (P8CE_CBROrFPOSPage)lastPage;
/*  46 */     initCBRStartMark(thisPage, lastPage, false);
/*     */     
/*  48 */     if (this.srchDef.getSortOrder() == RMContentSearchDefinition.SortOrder.asc)
/*     */     {
/*     */ 
/*     */ 
/*  52 */       boolean pageFilled = collectNextFposResults(thisPage);
/*  53 */       if (pageFilled)
/*     */       {
/*  55 */         thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.FPOS);
/*     */       }
/*     */       else
/*     */       {
/*  59 */         if ((lastCBROrFPOSPage == null) || (lastCBROrFPOSPage.getPageStatus() == P8CE_CBRPage.XTransPageStatus.FPOS))
/*     */         {
/*  61 */           if (Tracer.isMaximumTraceEnabled()) {
/*  62 */             Tracer.traceMaximumMsg("Crossing the transition page.", new Object[0]);
/*     */           }
/*  64 */           initializeCBRPageIteratorList();
/*     */           
/*  66 */           if (Tracer.isMaximumTraceEnabled()) {
/*  67 */             Tracer.traceMaximumMsg("initializeCBRPageIteratorList() succeeded.", new Object[0]);
/*     */           }
/*  69 */           thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.Crossing);
/*     */         }
/*     */         else
/*     */         {
/*  73 */           thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.CBR);
/*  74 */           thisPage.resetPageMark();
/*     */         }
/*     */         
/*  77 */         collectNextCBRResults(thisPage, true);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  84 */       boolean pageFilled = collectNextCBRResults(thisPage, true);
/*  85 */       if (pageFilled)
/*     */       {
/*  87 */         thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.CBR);
/*     */       }
/*     */       else
/*     */       {
/*  91 */         if ((lastCBROrFPOSPage == null) || (lastCBROrFPOSPage.getPageStatus() == P8CE_CBRPage.XTransPageStatus.CBR))
/*     */         {
/*  93 */           if (Tracer.isMaximumTraceEnabled()) {
/*  94 */             Tracer.traceMaximumMsg("Crossing the transition page.", new Object[0]);
/*     */           }
/*  96 */           initializePagedFposSearchIterator();
/*     */           
/*  98 */           if (Tracer.isMaximumTraceEnabled()) {
/*  99 */             Tracer.traceMaximumMsg("initializePagedFposSearchIterator() succeeded.", new Object[0]);
/*     */           }
/* 101 */           thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.Crossing);
/*     */         }
/*     */         else
/*     */         {
/* 105 */           thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.FPOS);
/* 106 */           thisPage.resetPageMark();
/*     */         }
/*     */         
/*     */ 
/* 110 */         collectNextFposResults(thisPage);
/*     */       }
/*     */     }
/*     */     
/* 114 */     this.curPage = thisPage;
/*     */     
/* 116 */     Tracer.traceMethodExit(new Object[] { thisPage });
/* 117 */     return thisPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getPreviousPage(P8CE_CBRPage prevPage, P8CE_CBRPage prev2Page)
/*     */     throws Exception
/*     */   {
/* 126 */     Tracer.traceMethodEntry(new Object[] { prevPage, prev2Page });
/*     */     
/* 128 */     P8CE_CBROrFPOSPage thisPage = new P8CE_CBROrFPOSPage(this.pageSize);
/*     */     
/* 130 */     if (prev2Page != null) {
/* 131 */       thisPage.addItems(prev2Page.getItemsForNextPage());
/*     */     }
/* 133 */     for (P8CE_CBRResultItem item : thisPage.getItems())
/*     */     {
/* 135 */       if ((item instanceof P8CE_CBROrFPOSResultItem)) {
/* 136 */         ((P8CE_CBROrFPOSResultItem)item).setFromLeftoverItem(true);
/*     */       }
/*     */     }
/* 139 */     if (this.srchDef.getSortOrder() == RMContentSearchDefinition.SortOrder.asc)
/*     */     {
/*     */ 
/* 142 */       P8CE_CBRPage.ItemSources pmSrc = ((P8CE_CBROrFPOSPage)prevPage).getPageMarkSource();
/* 143 */       if (pmSrc == P8CE_CBRPage.ItemSources.FPOS)
/*     */       {
/* 145 */         PageMark startMark = prevPage.getBegPageMark();
/* 146 */         this.fposInprocPageInfo.setPageStart(startMark, true);
/* 147 */         collectNextFposResults(thisPage);
/*     */         
/* 149 */         if (this.cbrInprocPageInfo != null) {
/* 150 */           this.cbrInprocPageInfo.reset();
/*     */         }
/* 152 */         if (!thisPage.isPageFull())
/*     */         {
/*     */ 
/* 155 */           collectNextCBRResults(thisPage, false);
/* 156 */           thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.Crossing);
/*     */         }
/*     */         else
/*     */         {
/* 160 */           thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.FPOS);
/*     */         }
/*     */       }
/* 163 */       else if (pmSrc == P8CE_CBRPage.ItemSources.CBR)
/*     */       {
/* 165 */         PageMark startMark = prevPage.getBegPageMark();
/* 166 */         this.cbrInprocPageInfo.setPageStart(startMark, false);
/* 167 */         collectNextCBRResults(thisPage, false);
/* 168 */         thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.CBR);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 175 */       P8CE_CBRPage.ItemSources pmSrc = ((P8CE_CBROrFPOSPage)prevPage).getPageMarkSource();
/* 176 */       if (pmSrc == P8CE_CBRPage.ItemSources.CBR)
/*     */       {
/* 178 */         PageMark startMark = prevPage.getBegPageMark();
/* 179 */         this.cbrInprocPageInfo.setPageStart(startMark, true);
/* 180 */         collectNextCBRResults(thisPage, false);
/*     */         
/* 182 */         if (this.fposInprocPageInfo != null) {
/* 183 */           this.fposInprocPageInfo.reset();
/*     */         }
/* 185 */         if (!thisPage.isPageFull())
/*     */         {
/*     */ 
/* 188 */           collectNextFposResults(thisPage);
/* 189 */           thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.Crossing);
/*     */         }
/* 191 */         thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.CBR);
/*     */       }
/* 193 */       else if (pmSrc == P8CE_CBRPage.ItemSources.FPOS)
/*     */       {
/* 195 */         PageMark startMark = prevPage.getBegPageMark();
/* 196 */         this.fposInprocPageInfo.setPageStart(startMark, false);
/* 197 */         collectNextFposResults(thisPage);
/* 198 */         thisPage.setPageStatus(P8CE_CBRPage.XTransPageStatus.FPOS);
/*     */       }
/*     */     }
/*     */     
/* 202 */     this.curPage = thisPage;
/* 203 */     Tracer.traceMethodExit(new Object[] { thisPage });
/* 204 */     return thisPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasNext()
/*     */   {
/* 213 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     
/* 215 */     if (Tracer.isMaximumTraceEnabled())
/*     */     {
/* 217 */       Tracer.traceMaximumMsg("currentPageFilled={0}; hasNextPageFPOS()={1}; hasNextPageCBR()={2}; hasCarryOverItem()={3}.", new Object[] { Boolean.valueOf(this.currentPageFilled), Boolean.valueOf(hasNextPageFPOS()), Boolean.valueOf(hasNextPageCBR()), Boolean.valueOf(hasCarryOverItem()) });
/*     */     }
/*     */     
/*     */ 
/* 221 */     boolean ret = (this.currentPageFilled) && ((hasNextPageFPOS()) || (hasNextPageCBR()) || (hasCarryOverItem()));
/* 222 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/* 223 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasPrevious()
/*     */   {
/* 232 */     Tracer.traceMethodEntry(new Object[0]);
/* 233 */     P8CE_CBRPage.ItemSources src = this.curPage.getItemsSources();
/* 234 */     boolean isAllFromLeftOver = this.curPage.isAllLeftOverItems();
/* 235 */     if (this.fposInprocPageInfo.hasPreviousPage((isAllFromLeftOver) || (src == P8CE_CBRPage.ItemSources.CBR)))
/*     */     {
/* 237 */       Tracer.traceMethodExit(new Object[] { "return true from fposInprocPageInfo.hasPreviousPage()" });
/* 238 */       return true;
/*     */     }
/*     */     
/* 241 */     if (this.cbrInprocPageInfo.hasPreviousPage((isAllFromLeftOver) || (src == P8CE_CBRPage.ItemSources.FPOS)))
/*     */     {
/* 243 */       Tracer.traceMethodExit(new Object[] { "return true from cbrInprocPageInfo.hasPreviousPage()" });
/* 244 */       return true;
/*     */     }
/*     */     
/* 247 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 248 */     return false;
/*     */   }
/*     */   
/*     */   private boolean collectNextFposResults(P8CE_CBRPage thisPage) throws Exception
/*     */   {
/* 253 */     Tracer.traceMethodEntry(new Object[] { thisPage });
/* 254 */     if (hasNextPageFPOS())
/*     */     {
/* 256 */       if ((thisPage.getBegPageMark() == null) && (((P8CE_CBROrFPOSPage)thisPage).getPageMarkSource() == null))
/*     */       {
/* 258 */         PageMark startMark = this.fposInprocPageInfo != null ? this.fposInprocPageInfo.getCurPageMark() : null;
/* 259 */         thisPage.setBegPageMark(startMark);
/* 260 */         ((P8CE_CBROrFPOSPage)thisPage).setPageMarkSource(P8CE_CBRPage.ItemSources.FPOS);
/*     */       }
/*     */       
/* 263 */       Object[] fposRowSet = this.fposInprocPageInfo.nextPage();
/* 264 */       if (Tracer.isMaximumTraceEnabled()) {
/* 265 */         Tracer.traceMaximumMsg("Inside P8CE_CBROrFPOSImplementor::collectNextFposResults, fposInprocPageInfo.nextPage() returns the items: {0}.", new Object[] { traceCBRRowset(fposRowSet) });
/*     */       }
/* 267 */       if (fposRowSet == null) return false;
/* 268 */       for (Object obj : fposRowSet)
/*     */       {
/* 270 */         RepositoryRow rr = (RepositoryRow)obj;
/* 271 */         Properties props = rr.getProperties();
/*     */         
/* 273 */         P8CE_CBROrFPOSResultItem item = new P8CE_CBROrFPOSResultItem(this.jarmRepository, rr);
/* 274 */         if (props.isPropertyPresent("Id"))
/* 275 */           item.setId(props.getIdValue("Id").toString());
/* 276 */         item.setItemSource(P8CE_CBRPage.ItemSources.FPOS);
/* 277 */         item.setFromLeftoverItem(false);
/* 278 */         item.setScore(0.0D);
/*     */         
/* 280 */         if (thisPage.isPageFull()) {
/* 281 */           thisPage.addItemForNextPage(item);
/*     */         } else {
/* 283 */           thisPage.addItem(item);
/*     */         }
/*     */       }
/*     */     }
/* 287 */     boolean result = thisPage.isPageFull();
/* 288 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 289 */     return result;
/*     */   }
/*     */   
/*     */   private boolean collectNextCBRResults(P8CE_CBRPage thisPage, boolean setStartMarker) throws Exception
/*     */   {
/* 294 */     Tracer.traceMethodEntry(new Object[] { thisPage, Boolean.valueOf(setStartMarker) });
/* 295 */     if ((setStartMarker) && (thisPage.getBegPageMark() == null) && (((P8CE_CBROrFPOSPage)thisPage).getPageMarkSource() == null))
/*     */     {
/* 297 */       PageMark startMark = this.cbrInprocPageInfo != null ? this.cbrInprocPageInfo.getCurPageMark() : null;
/* 298 */       thisPage.setBegPageMark(startMark);
/* 299 */       ((P8CE_CBROrFPOSPage)thisPage).setPageMarkSource(P8CE_CBRPage.ItemSources.CBR);
/*     */     }
/*     */     
/* 302 */     doNextPageCBRAndFilterFpos(thisPage, true);
/*     */     
/* 304 */     boolean pageFilled = thisPage.isPageFull();
/* 305 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(pageFilled) });
/* 306 */     return pageFilled;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBROrFPOSImplementor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */