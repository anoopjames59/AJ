/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.collection.PageMark;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ class P8CE_FPOSOrCBRImplementor extends P8CE_CBRSearchImplementor
/*     */ {
/*     */   P8CE_FPOSOrCBRImplementor(Repository fpos, RMContentSearchDefinition srchDef, int pgSize)
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
/*  32 */     initializePagedFposSearchIterator(true);
/*  33 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getNextPage(P8CE_CBRPage lastPage)
/*     */     throws Exception
/*     */   {
/*  42 */     Tracer.traceMethodEntry(new Object[] { lastPage });
/*  43 */     P8CE_CBRPage thisPage = new P8CE_CBRPage(this.pageSize);
/*  44 */     initFPOSStartMark(thisPage, lastPage, true);
/*     */     
/*  46 */     while (hasNextPageFPOS())
/*     */     {
/*  48 */       Object[] fposRowSet = this.fposInprocPageInfo.nextPage();
/*  49 */       if (Tracer.isMaximumTraceEnabled())
/*     */       {
/*  51 */         Tracer.traceMaximumMsg("fposInprocPageInfo.nextPage returns items: {0}.", new Object[] { traceCBRRowset(fposRowSet) });
/*     */       }
/*     */       
/*  54 */       if (fposRowSet.length == 0) break;
/*  55 */       List<String> commonCriteriaFposIdList = collectRowSetIds(Arrays.asList(fposRowSet));
/*     */       
/*     */ 
/*  58 */       Map<String, RepositoryRow> filteredFposIdMap = null;
/*  59 */       Map<String, Double> filteredCBRIdMap = null;
/*  60 */       if (!this.srchDef.isCBRConditionOnly()) {
/*  61 */         filteredFposIdMap = filterFposWithPropertyCondition(commonCriteriaFposIdList, false);
/*     */       }
/*  63 */       if (Tracer.isMaximumTraceEnabled())
/*     */       {
/*  65 */         if (filteredFposIdMap == null) {
/*  66 */           Tracer.traceMaximumMsg("no items returned after filtering FPOS on the metadata conditions.", new Object[0]);
/*     */         } else {
/*  68 */           Tracer.traceMaximumMsg("Items returned after filtering FPOS on the metadata conditions: {0}.", filteredFposIdMap.entrySet().toArray());
/*     */         }
/*     */       }
/*     */       
/*  72 */       List<String> lookforCBRMatchIdList = null;
/*  73 */       if (filteredFposIdMap != null)
/*     */       {
/*  75 */         lookforCBRMatchIdList = new ArrayList();
/*  76 */         for (String id : commonCriteriaFposIdList)
/*     */         {
/*  78 */           if (!filteredFposIdMap.containsKey(id)) {
/*  79 */             lookforCBRMatchIdList.add(id);
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/*  84 */         lookforCBRMatchIdList = commonCriteriaFposIdList;
/*     */       }
/*     */       
/*  87 */       if ((lookforCBRMatchIdList != null) && (!lookforCBRMatchIdList.isEmpty())) {
/*  88 */         filteredCBRIdMap = filterFposWithCBR(lookforCBRMatchIdList, true);
/*     */       }
/*  90 */       if (Tracer.isMaximumTraceEnabled())
/*     */       {
/*  92 */         if (filteredCBRIdMap == null) {
/*  93 */           Tracer.traceMaximumMsg("no items returned after filtering ROSs on the cbr conditions.", new Object[0]);
/*     */         } else {
/*  95 */           Tracer.traceMaximumMsg("Items returned after filtering ROSs on the cbr conditions: {0}.", filteredCBRIdMap.entrySet().toArray());
/*     */         }
/*     */       }
/*  98 */       for (Object row : fposRowSet)
/*     */       {
/* 100 */         RepositoryRow rr = (RepositoryRow)row;
/* 101 */         Properties props = rr.getProperties();
/*     */         
/*     */ 
/* 104 */         if ((props != null) && (props.isPropertyPresent("Id")))
/*     */         {
/* 106 */           String fposID = props.getIdValue("Id").toString();
/* 107 */           if (((filteredFposIdMap != null) && (filteredFposIdMap.get(fposID) != null)) || ((filteredCBRIdMap != null) && (filteredCBRIdMap.get(fposID) != null)))
/*     */           {
/*     */ 
/* 110 */             P8CE_CBRResultItem item = new P8CE_CBRResultItem(this.jarmRepository, rr);
/*     */             
/* 112 */             if ((filteredCBRIdMap != null) && (filteredCBRIdMap.get(fposID) != null)) {
/* 113 */               item.setScore(((Double)filteredCBRIdMap.get(fposID)).doubleValue());
/*     */             } else {
/* 115 */               item.setScore(0.0D);
/*     */             }
/* 117 */             item.setId(fposID);
/*     */             
/* 119 */             if (thisPage.isPageFull()) {
/* 120 */               thisPage.addItemForNextPage(item);
/*     */             } else {
/* 122 */               thisPage.addItem(item);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 128 */       if (thisPage.isPageFull()) {
/*     */         break;
/*     */       }
/*     */     }
/* 132 */     Tracer.traceMethodExit(new Object[] { thisPage });
/* 133 */     return thisPage;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected P8CE_CBRPage getPreviousPage(P8CE_CBRPage prevPage, P8CE_CBRPage prev2Page)
/*     */     throws Exception
/*     */   {
/* 142 */     Tracer.traceMethodEntry(new Object[] { prevPage, prev2Page });
/*     */     
/* 144 */     PageMark startMark = prevPage.getBegPageMark();
/* 145 */     this.fposInprocPageInfo.setPageStart(startMark, false);
/*     */     
/* 147 */     P8CE_CBRPage page = getNextPage(prev2Page);
/* 148 */     Tracer.traceMethodExit(new Object[] { page });
/* 149 */     return page;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasNext()
/*     */   {
/* 158 */     Tracer.traceMethodEntry(new Object[0]);
/* 159 */     if (Tracer.isMaximumTraceEnabled())
/*     */     {
/* 161 */       Tracer.traceMaximumMsg("currentPageFilled={0}; hasNextPageFPOS()={1}; hasNextPageCBR()={2}; hasCarryOverItem()={3}.", new Object[] { Boolean.valueOf(this.currentPageFilled), Boolean.valueOf(hasNextPageFPOS()), Boolean.valueOf(hasNextPageCBR()), Boolean.valueOf(hasCarryOverItem()) });
/*     */     }
/*     */     
/*     */ 
/* 165 */     boolean ret = (this.currentPageFilled) && ((hasNextPageFPOS()) || (hasNextPageCBR()) || (hasCarryOverItem()));
/* 166 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(ret) });
/* 167 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean hasPrevious()
/*     */   {
/* 176 */     Tracer.traceMethodEntry(new Object[0]);
/* 177 */     if (this.fposInprocPageInfo.hasPreviousPage())
/*     */     {
/* 179 */       Tracer.traceMethodExit(new Object[] { "return true from fposInprocPageInfo.hasPreviousPage()" });
/* 180 */       return true;
/*     */     }
/*     */     
/* 183 */     if (this.cbrInprocPageInfo.hasPreviousPage())
/*     */     {
/* 185 */       Tracer.traceMethodExit(new Object[] { "return true from cbrInprocPageInfo.hasPreviousPage()" });
/* 186 */       return true;
/*     */     }
/*     */     
/* 189 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 190 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_FPOSOrCBRImplementor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */