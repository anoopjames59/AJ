/*     */ package com.ibm.jarm.ral.p8ce.cbr;
/*     */ 
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.collection.PageMark;
/*     */ import com.filenet.api.collection.RepositoryRowSet;
/*     */ import com.filenet.api.constants.MergeMode;
/*     */ import com.filenet.api.core.Factory.ObjectStore;
/*     */ import com.filenet.api.core.IndependentObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.filenet.api.property.Properties;
/*     */ import com.filenet.api.property.Property;
/*     */ import com.filenet.api.query.RepositoryRow;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.exception.RMErrorCode;
/*     */ import com.ibm.jarm.api.exception.RMRuntimeException;
/*     */ import com.ibm.jarm.api.query.CBRResult;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.AndOrOper;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.ContentSearchOption;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.OrderBy;
/*     */ import com.ibm.jarm.api.query.RMContentSearchDefinition.SortOrder;
/*     */ import com.ibm.jarm.api.util.JarmTracer;
/*     */ import com.ibm.jarm.api.util.JarmTracer.SubSystem;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_RepositoryImpl;
/*     */ import com.ibm.jarm.ral.p8ce.P8CE_Util;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ abstract class P8CE_CBRSearchImplementor
/*     */ {
/*  40 */   static final JarmTracer Tracer = JarmTracer.getJarmTracer(JarmTracer.SubSystem.RalP8CE_CBR);
/*     */   
/*  42 */   protected ObjectStore[] cbrObjectStores = null;
/*  43 */   protected Repository jarmRepository = null;
/*  44 */   protected ObjectStore filePlanObjectStore = null;
/*  45 */   protected int pageSize = 0;
/*  46 */   protected InProcPagingInfo cbrInprocPageInfo = null;
/*  47 */   protected InProcPagingInfo fposInprocPageInfo = null;
/*     */   
/*  49 */   protected boolean currentPageFilled = true;
/*  50 */   protected int pageCounter = 0;
/*  51 */   protected final ArrayList<P8CE_CBRPage> pageCarryOverList = new ArrayList();
/*     */   protected RMContentSearchDefinition srchDef;
/*     */   
/*     */   P8CE_CBRSearchImplementor(Repository jarmRepository, RMContentSearchDefinition srchDef, int pgSize)
/*     */   {
/*  56 */     Tracer.traceMethodEntry(new Object[] { jarmRepository, srchDef, Integer.valueOf(pgSize) });
/*  57 */     this.jarmRepository = jarmRepository;
/*  58 */     this.filePlanObjectStore = ((P8CE_RepositoryImpl)jarmRepository).getJaceObjectStore();
/*  59 */     this.srchDef = srchDef;
/*  60 */     this.pageSize = pgSize;
/*  61 */     Tracer.traceMethodExit(new Object[0]);
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
/*     */   protected abstract boolean hasNext();
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
/*     */   protected abstract boolean hasPrevious();
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
/*     */   protected abstract void initCBRSearch(boolean paramBoolean);
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
/*     */   protected abstract P8CE_CBRPage getNextPage(P8CE_CBRPage paramP8CE_CBRPage)
/*     */     throws Exception;
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
/*     */   protected abstract P8CE_CBRPage getPreviousPage(P8CE_CBRPage paramP8CE_CBRPage1, P8CE_CBRPage paramP8CE_CBRPage2)
/*     */     throws Exception;
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
/*     */   static P8CE_CBRSearchImplementor createInstance(Repository jarmRepository, RMContentSearchDefinition srchDef, int pgSize)
/*     */   {
/* 133 */     Tracer.traceMethodEntry(new Object[] { jarmRepository, srchDef, Integer.valueOf(pgSize) });
/* 134 */     P8CE_CBRSearchImplementor impl = null;
/* 135 */     if ((srchDef.hasContentQueryDefined()) && (srchDef.getContentSearchOption() == RMContentSearchDefinition.ContentSearchOption.content))
/*     */     {
/* 137 */       if ((srchDef.getOrderBy() == RMContentSearchDefinition.OrderBy.cbrscores) || (srchDef.getOrderBy() == RMContentSearchDefinition.OrderBy.none))
/*     */       {
/* 139 */         if (srchDef.getOperBtwContentAndMetadataSearch() == RMContentSearchDefinition.AndOrOper.and) {
/* 140 */           impl = new P8CE_CBRAndFPOSImplementor(jarmRepository, srchDef, pgSize);
/* 141 */         } else if (srchDef.getOperBtwContentAndMetadataSearch() == RMContentSearchDefinition.AndOrOper.or) {
/* 142 */           impl = new P8CE_CBROrFPOSImplementor(jarmRepository, srchDef, pgSize);
/*     */         }
/*     */         
/*     */       }
/* 146 */       else if (srchDef.getOperBtwContentAndMetadataSearch() == RMContentSearchDefinition.AndOrOper.and) {
/* 147 */         impl = new P8CE_FPOSAndCBRImplementor(jarmRepository, srchDef, pgSize);
/* 148 */       } else if (srchDef.getOperBtwContentAndMetadataSearch() == RMContentSearchDefinition.AndOrOper.or) {
/* 149 */         impl = new P8CE_FPOSOrCBRImplementor(jarmRepository, srchDef, pgSize);
/*     */       }
/*     */       
/*     */     }
/*     */     else {
/* 154 */       impl = new P8CE_FPOSImplementor(jarmRepository, srchDef, pgSize);
/*     */     }
/*     */     
/* 157 */     Tracer.traceMethodExit(new Object[] { impl });
/* 158 */     return impl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<CBRResult> nextPage()
/*     */   {
/* 170 */     Tracer.traceMethodEntry(new Object[0]);
/*     */     try
/*     */     {
/* 173 */       P8CE_CBRPage lastPage = null;
/* 174 */       if ((this.pageCounter > 0) && (this.pageCarryOverList.size() >= this.pageCounter)) {
/* 175 */         lastPage = (P8CE_CBRPage)this.pageCarryOverList.get(this.pageCounter - 1);
/*     */       }
/* 177 */       P8CE_CBRPage thisPage = getNextPage(lastPage);
/*     */       
/* 179 */       this.currentPageFilled = (thisPage == null ? false : thisPage.isPageFull());
/*     */       
/* 181 */       List<CBRResult> rowList = new ArrayList();
/* 182 */       if (thisPage != null) {
/* 183 */         rowList.addAll(thisPage.getItems());
/*     */       }
/*     */       
/* 186 */       if ((thisPage != null) && (this.srchDef.hasContentQueryDefined()))
/*     */       {
/* 188 */         thisPage.removeItems();
/* 189 */         if (this.pageCarryOverList.size() > this.pageCounter) {
/* 190 */           this.pageCarryOverList.set(this.pageCounter, thisPage);
/*     */         } else {
/* 192 */           this.pageCarryOverList.add(thisPage);
/*     */         }
/*     */       }
/* 195 */       this.pageCounter += 1;
/*     */       
/* 197 */       if (Tracer.isMaximumTraceEnabled()) {
/* 198 */         Tracer.traceMaximumMsg("nextPage returns currentPageFilled: {0} and the items: {1}.", new Object[] { Boolean.valueOf(this.currentPageFilled), traceCBRRowset(rowList) });
/*     */       }
/*     */       
/* 201 */       Tracer.traceMethodExit(new Object[] { rowList });
/* 202 */       return rowList;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 206 */       RMRuntimeException engEx = processCSSEngineException(ex);
/* 207 */       if (engEx != null) { throw engEx;
/*     */       }
/* 209 */       RMErrorCode errCode = P8CE_Util.determineErrorCode(RMErrorCode.CBR_SEARCH_FAILED, ex);
/* 210 */       throw P8CE_Util.processJaceException(ex, errCode, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<CBRResult> previousPage()
/*     */   {
/* 223 */     Tracer.traceMethodEntry(new Object[0]);
/* 224 */     this.currentPageFilled = false;
/* 225 */     if (--this.pageCounter < 0) {
/* 226 */       this.pageCounter = 0;
/*     */     }
/* 228 */     P8CE_CBRPage prevPage = null;
/* 229 */     if ((this.pageCounter > 0) && (this.pageCarryOverList.size() >= this.pageCounter)) {
/* 230 */       prevPage = (P8CE_CBRPage)this.pageCarryOverList.get(this.pageCounter - 1);
/*     */     }
/* 232 */     P8CE_CBRPage prev2Page = null;
/* 233 */     if ((this.pageCounter > 1) && (this.pageCarryOverList.size() > this.pageCounter)) {
/* 234 */       prev2Page = (P8CE_CBRPage)this.pageCarryOverList.get(this.pageCounter - 2);
/*     */     }
/*     */     try
/*     */     {
/* 238 */       P8CE_CBRPage thisPage = getPreviousPage(prevPage, prev2Page);
/*     */       
/* 240 */       this.currentPageFilled = (thisPage == null ? false : thisPage.isPageFull());
/*     */       
/* 242 */       List<CBRResult> rowList = new ArrayList();
/* 243 */       if (thisPage != null) {
/* 244 */         rowList.addAll(thisPage.getItems());
/*     */       }
/* 246 */       if (Tracer.isMaximumTraceEnabled()) {
/* 247 */         Tracer.traceMaximumMsg("previousPage returns currentPageFilled: {0} and the items: {1}.", new Object[] { Boolean.valueOf(this.currentPageFilled), traceCBRRowset(rowList) });
/*     */       }
/*     */       
/* 250 */       Tracer.traceMethodExit(new Object[] { rowList });
/* 251 */       return rowList;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 255 */       RMRuntimeException engEx = processCSSEngineException(ex);
/* 256 */       if (engEx != null) { throw engEx;
/*     */       }
/* 258 */       RMErrorCode errCode = P8CE_Util.determineErrorCode(RMErrorCode.CBR_SEARCH_FAILED, ex);
/* 259 */       throw P8CE_Util.processJaceException(ex, errCode, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initializePagedCBRSearch()
/*     */   {
/* 266 */     Tracer.traceMethodEntry(new Object[0]);
/* 267 */     List<ObjectStore> cbrOSList = new ArrayList();
/* 268 */     List<String> rosList = P8CE_Util.getAssociatedROSIDs(this.filePlanObjectStore);
/* 269 */     if (Tracer.isMaximumTraceEnabled()) {
/* 270 */       Tracer.traceMaximumMsg("Associated ROS IDs: {0}.", new Object[] { rosList });
/*     */     }
/* 272 */     for (String cbrObjectStoreId : rosList)
/*     */     {
/* 274 */       Integer searchType = Integer.valueOf(0);
/*     */       try
/*     */       {
/* 277 */         ObjectStore cbrOS = Factory.ObjectStore.fetchInstance(this.filePlanObjectStore.get_Domain(), cbrObjectStoreId, null);
/* 278 */         searchType = cbrOS.getProperties().getInteger32Value("CBRSearchType");
/*     */         
/* 280 */         if (searchType.intValue() != 0)
/*     */         {
/* 282 */           cbrOSList.add(cbrOS);
/*     */ 
/*     */ 
/*     */         }
/* 286 */         else if (Tracer.isMinimumTraceEnabled()) {
/* 287 */           Tracer.traceMinimumMsg("Object Store {0} is not configured with a content search engine.", new Object[] { cbrOS.get_Name() });
/*     */         }
/*     */       }
/*     */       catch (Exception ignored) {}
/*     */     }
/*     */     
/* 293 */     this.cbrObjectStores = new ObjectStore[cbrOSList.size()];
/* 294 */     cbrOSList.toArray(this.cbrObjectStores);
/* 295 */     if (Tracer.isMaximumTraceEnabled()) {
/* 296 */       Tracer.traceMaximumMsg("Associated ROSs with CBR engine: {0}.", new Object[] { cbrOSList });
/*     */     }
/* 298 */     if (isCBRHeterogenous())
/*     */     {
/* 300 */       throw RMRuntimeException.createRMRuntimeException(RMErrorCode.CBR_HETEROGENEOUS_NOT_SUPPORTED, new Object[] { this });
/*     */     }
/* 302 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isCBRHeterogenous()
/*     */   {
/* 309 */     Tracer.traceMethodEntry(new Object[0]);
/* 310 */     if ((this.cbrObjectStores != null) && (this.cbrObjectStores.length > 0))
/*     */     {
/* 312 */       int searchType = 1;
/* 313 */       boolean bHasCascade = false;
/* 314 */       boolean bHasVerity = false;
/* 315 */       for (ObjectStore os : this.cbrObjectStores)
/*     */       {
/*     */         try
/*     */         {
/* 319 */           searchType = os.getProperties().getInteger32Value("CBRSearchType").intValue();
/*     */         }
/*     */         catch (Exception ignored)
/*     */         {
/* 323 */           searchType = 1;
/*     */         }
/*     */         
/*     */ 
/* 327 */         if (searchType == 2) {
/* 328 */           bHasCascade = true;
/* 329 */         } else if (searchType == 1) {
/* 330 */           bHasVerity = true;
/*     */         }
/*     */       }
/* 333 */       if ((bHasCascade) && (bHasVerity))
/*     */       {
/* 335 */         Tracer.traceMethodExit(new Object[] { Boolean.valueOf(true) });
/* 336 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 340 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(false) });
/* 341 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initializeCBRPageIteratorList()
/*     */   {
/* 347 */     Tracer.traceMethodEntry(new Object[0]);
/* 348 */     String cbrStmt = this.srchDef.buildContentQueryStmt();
/* 349 */     if (Tracer.isMaximumTraceEnabled()) {
/* 350 */       Tracer.traceMaximumMsg("CBR Query Statement: {0}.", new Object[] { cbrStmt });
/*     */     }
/*     */     try
/*     */     {
/* 354 */       SearchScope searchScope = new SearchScope(this.cbrObjectStores, MergeMode.INTERSECTION);
/* 355 */       SearchSQL sqlObject = new SearchSQL();
/* 356 */       sqlObject.setQueryString(cbrStmt);
/*     */       
/* 358 */       RepositoryRowSet rowSet = searchScope.fetchRows(sqlObject, Integer.valueOf(this.pageSize), null, Boolean.TRUE);
/* 359 */       if (rowSet != null) {
/* 360 */         this.cbrInprocPageInfo = new InProcPagingInfo(rowSet.pageIterator());
/*     */       }
/*     */       
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 366 */       throw processCBRInitException(ex, cbrStmt, this.cbrObjectStores);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected RepositoryRowSet initializePagedFposSearchIterator()
/*     */   {
/* 374 */     Tracer.traceMethodEntry(new Object[0]);
/* 375 */     RepositoryRowSet results = initializePagedFposSearchIterator(false);
/* 376 */     Tracer.traceMethodExit(new Object[] { results });
/* 377 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */   protected RepositoryRowSet initializePagedFposSearchIterator(boolean useCommonCriteria)
/*     */   {
/* 383 */     Tracer.traceMethodEntry(new Object[] { Boolean.valueOf(useCommonCriteria) });
/* 384 */     RepositoryRowSet rowSet = null;
/* 385 */     String asSearchSQLstmt = useCommonCriteria ? this.srchDef.buildSQLStmtFromCommonCriteria() : this.srchDef.buildSQLStmt();
/*     */     
/* 387 */     if (Tracer.isMaximumTraceEnabled()) {
/* 388 */       Tracer.traceMaximumMsg("FPOS Query Statement: {0}.", new Object[] { asSearchSQLstmt });
/*     */     }
/*     */     try
/*     */     {
/* 392 */       SearchScope searchScope = new SearchScope(this.filePlanObjectStore);
/* 393 */       SearchSQL sqlObject = new SearchSQL();
/* 394 */       sqlObject.setQueryString(asSearchSQLstmt);
/*     */       
/* 396 */       rowSet = searchScope.fetchRows(sqlObject, Integer.valueOf(this.pageSize), null, Boolean.TRUE);
/*     */       
/* 398 */       this.fposInprocPageInfo = new InProcPagingInfo(rowSet.pageIterator());
/*     */       
/*     */ 
/* 401 */       this.fposInprocPageInfo.pageIterator.nextPage();
/* 402 */       PageMark curPageMark = this.fposInprocPageInfo.pageIterator.getPageMark();
/*     */       
/* 404 */       this.fposInprocPageInfo.pageMarkList.clear();
/* 405 */       this.fposInprocPageInfo.pageMarkList.add(curPageMark);
/* 406 */       this.fposInprocPageInfo.curPageMarkIt = null;
/*     */       
/*     */ 
/* 409 */       this.fposInprocPageInfo.pageIterator.reset();
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 413 */       RMErrorCode errCode = P8CE_Util.determineErrorCode(RMErrorCode.SEARCH_INIT_FAILED, ex);
/* 414 */       throw P8CE_Util.processJaceException(ex, errCode, new Object[] { asSearchSQLstmt, this.filePlanObjectStore.get_Name() });
/*     */     }
/* 416 */     Tracer.traceMethodExit(new Object[] { rowSet });
/* 417 */     return rowSet;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected List<String> collectRowSetIds(List<?> rowSet)
/*     */   {
/* 424 */     Tracer.traceMethodEntry(new Object[] { rowSet });
/* 425 */     List<String> rowIds = null;
/* 426 */     Properties props; if (rowSet != null)
/*     */     {
/* 428 */       rowIds = new ArrayList();
/* 429 */       props = null;
/*     */       
/* 431 */       for (Object row : rowSet)
/*     */       {
/* 433 */         if ((row instanceof RepositoryRow))
/*     */         {
/* 435 */           RepositoryRow rr = (RepositoryRow)row;
/* 436 */           props = rr.getProperties();
/*     */         }
/* 438 */         else if ((row instanceof IndependentObject))
/*     */         {
/* 440 */           IndependentObject io = (IndependentObject)row;
/* 441 */           props = io.getProperties();
/*     */         }
/*     */         
/*     */ 
/* 445 */         if ((props != null) && (props.isPropertyPresent("Id"))) {
/* 446 */           rowIds.add(props.getIdValue("Id").toString());
/*     */         }
/*     */       }
/*     */     }
/* 450 */     Tracer.traceMethodExit(new Object[] { rowIds });
/* 451 */     return rowIds;
/*     */   }
/*     */   
/*     */   protected void doNextPageCBRAndFilterFpos(P8CE_CBRPage thisPage, boolean bUseCommonCriteria)
/*     */     throws Exception
/*     */   {
/* 457 */     Tracer.traceMethodEntry(new Object[] { thisPage, Boolean.valueOf(bUseCommonCriteria) });
/* 458 */     while (hasNextPageCBR())
/*     */     {
/* 460 */       Object[] rowArray = this.cbrInprocPageInfo.nextPage();
/* 461 */       if (Tracer.isMaximumTraceEnabled()) {
/* 462 */         Tracer.traceMaximumMsg("cbrInprocPageInfo.nextPage() returns items : {0}.", new Object[] { traceCBRRowset(rowArray) });
/*     */       }
/* 464 */       if ((rowArray == null) || (rowArray.length == 0)) {
/*     */         break;
/*     */       }
/* 467 */       List<String> cbrIdList = new ArrayList();
/* 468 */       Map<String, Double> cbrIdRankMap = new HashMap();
/* 469 */       for (Object obj : rowArray)
/*     */       {
/* 471 */         RepositoryRow row = (RepositoryRow)obj;
/* 472 */         String idStr = row.getProperties().get("RecordInformation").getIdValue().toString();
/*     */         
/* 474 */         if ((row.getProperties().get("Rank") != null) && (row.getProperties().get("Rank").getFloat64Value() != null))
/*     */         {
/* 476 */           Double rank = row.getProperties().get("Rank").getFloat64Value();
/* 477 */           cbrIdRankMap.put(idStr, rank);
/* 478 */           cbrIdList.add(idStr);
/*     */         }
/*     */       }
/* 481 */       Map<String, RepositoryRow> filteredMap = filterFposWithPropertyCondition(cbrIdList, bUseCommonCriteria);
/* 482 */       if (Tracer.isMaximumTraceEnabled())
/*     */       {
/* 484 */         Tracer.traceMaximumMsg("filteredMap returns from filterFposWithPropertyCondition : {0}.", new Object[] { traceCBRRowset(filteredMap.values().toArray()) });
/*     */       }
/*     */       
/*     */ 
/* 488 */       for (String idStr : cbrIdList)
/*     */       {
/* 490 */         if ((filteredMap != null) && (filteredMap.get(idStr) != null))
/*     */         {
/* 492 */           P8CE_CBRResultItem item = null;
/* 493 */           if ((thisPage instanceof P8CE_CBROrFPOSPage))
/*     */           {
/* 495 */             item = new P8CE_CBROrFPOSResultItem(this.jarmRepository, (RepositoryRow)filteredMap.get(idStr));
/* 496 */             item.setScore(((Double)cbrIdRankMap.get(idStr)).doubleValue());
/* 497 */             item.setId(idStr);
/* 498 */             ((P8CE_CBROrFPOSResultItem)item).setFromLeftoverItem(false);
/* 499 */             ((P8CE_CBROrFPOSResultItem)item).setItemSource(P8CE_CBRPage.ItemSources.CBR);
/*     */           }
/*     */           else
/*     */           {
/* 503 */             item = new P8CE_CBRResultItem(this.jarmRepository, (RepositoryRow)filteredMap.get(idStr));
/* 504 */             item.setScore(((Double)cbrIdRankMap.get(idStr)).doubleValue());
/* 505 */             item.setId(idStr);
/*     */           }
/*     */           
/* 508 */           if (thisPage.isPageFull()) {
/* 509 */             thisPage.addItemForNextPage(item);
/*     */           } else {
/* 511 */             thisPage.addItem(item);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 516 */       if (thisPage.isPageFull()) {
/*     */         break;
/*     */       }
/*     */     }
/* 520 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initCBRStartMark(P8CE_CBRPage thisPage, P8CE_CBRPage lastPage, boolean setPageStartMarker)
/*     */   {
/* 526 */     Tracer.traceMethodEntry(new Object[] { thisPage, Boolean.valueOf(setPageStartMarker), Boolean.valueOf(setPageStartMarker) });
/* 527 */     if (lastPage != null) {
/* 528 */       thisPage.addItems(lastPage.getItemsForNextPage());
/*     */     }
/* 530 */     for (P8CE_CBRResultItem item : thisPage.getItems())
/*     */     {
/* 532 */       if ((item instanceof P8CE_CBROrFPOSResultItem)) {
/* 533 */         ((P8CE_CBROrFPOSResultItem)item).setFromLeftoverItem(true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 538 */     if (setPageStartMarker)
/*     */     {
/* 540 */       PageMark startMark = this.cbrInprocPageInfo != null ? this.cbrInprocPageInfo.getCurPageMark() : null;
/* 541 */       if (startMark != null) {
/* 542 */         thisPage.setBegPageMark(startMark);
/*     */       }
/*     */     }
/* 545 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void initFPOSStartMark(P8CE_CBRPage thisPage, P8CE_CBRPage lastPage, boolean setPageStartMarker)
/*     */   {
/* 551 */     Tracer.traceMethodEntry(new Object[] { thisPage, lastPage, Boolean.valueOf(setPageStartMarker) });
/* 552 */     if (lastPage != null)
/* 553 */       thisPage.addItems(lastPage.getItemsForNextPage());
/* 554 */     for (P8CE_CBRResultItem item : thisPage.getItems())
/*     */     {
/* 556 */       if ((item instanceof P8CE_CBROrFPOSResultItem)) {
/* 557 */         ((P8CE_CBROrFPOSResultItem)item).setFromLeftoverItem(true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 562 */     if (setPageStartMarker)
/*     */     {
/* 564 */       PageMark startMark = this.fposInprocPageInfo != null ? this.fposInprocPageInfo.getCurPageMark() : null;
/* 565 */       if (startMark != null) {
/* 566 */         thisPage.setBegPageMark(startMark);
/*     */       }
/*     */     }
/* 569 */     Tracer.traceMethodExit(new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Map<String, RepositoryRow> filterFposWithPropertyCondition(List<String> fposRowIdList, boolean useCommandCriteria)
/*     */   {
/* 575 */     Tracer.traceMethodEntry(new Object[] { fposRowIdList, Boolean.valueOf(useCommandCriteria) });
/* 576 */     Map<String, RepositoryRow> filteredIdMap = new HashMap();
/* 577 */     if ((fposRowIdList == null) || (fposRowIdList.isEmpty()))
/*     */     {
/* 579 */       Tracer.traceMethodExit(new Object[] { filteredIdMap });
/* 580 */       return filteredIdMap;
/*     */     }
/*     */     
/* 583 */     RepositoryRowSet rowSet = null;
/*     */     
/* 585 */     RMContentSearchDefinition filterFposDef = (RMContentSearchDefinition)this.srchDef.clone();
/* 586 */     StringBuffer fposIdFilterBuf = new StringBuffer();
/* 587 */     fposIdFilterBuf.append(filterFposDef.getSqlAlias()).append(".").append("Id").append(" IN (");
/*     */     
/*     */ 
/* 590 */     int rowIdListSize = fposRowIdList.size();
/* 591 */     for (int i = 0; i < rowIdListSize; i++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 597 */       fposIdFilterBuf.append((String)fposRowIdList.get(i));
/* 598 */       if (i < rowIdListSize - 1) {
/* 599 */         fposIdFilterBuf.append(", ");
/*     */       } else
/* 601 */         fposIdFilterBuf.append(")");
/*     */     }
/* 603 */     String whereClause = useCommandCriteria ? filterFposDef.getCommonWhereClause() : filterFposDef.getWhereClause();
/* 604 */     int whereIndex = whereClause.toUpperCase().indexOf("WHERE") + 5;
/* 605 */     String whereCriteria = whereClause.substring(whereIndex);
/* 606 */     StringBuffer newWhereClause = new StringBuffer("WHERE (");
/* 607 */     newWhereClause.append(whereCriteria).append(" ) ").append(RMContentSearchDefinition.AndOrOper.and).append(" (").append(fposIdFilterBuf).append(")");
/*     */     
/* 609 */     filterFposDef.setWhereClause(newWhereClause.toString());
/* 610 */     filterFposDef.setSortOrder(RMContentSearchDefinition.SortOrder.none);
/* 611 */     String filterQuery = filterFposDef.buildSQLStmt();
/* 612 */     if (Tracer.isMaximumTraceEnabled()) {
/* 613 */       Tracer.traceMaximumMsg("FPOS Filter Query Statement: {0}.", new Object[] { filterQuery });
/*     */     }
/* 615 */     SearchScope searchScope = new SearchScope(this.filePlanObjectStore);
/*     */     
/* 617 */     SearchSQL sqlObject = new SearchSQL();
/* 618 */     sqlObject.setQueryString(filterQuery);
/* 619 */     Integer pageSize = Integer.valueOf(fposRowIdList.size());
/* 620 */     Boolean continuable = Boolean.TRUE;
/*     */     
/* 622 */     rowSet = searchScope.fetchRows(sqlObject, pageSize, null, continuable);
/* 623 */     if (rowSet != null)
/*     */     {
/* 625 */       RepositoryRow row = null;
/* 626 */       String idStr = null;
/* 627 */       PageIterator jacePI = rowSet.pageIterator();
/* 628 */       while (jacePI.nextPage())
/*     */       {
/* 630 */         Object[] currentPage = jacePI.getCurrentPage();
/* 631 */         for (Object obj : currentPage)
/*     */         {
/* 633 */           row = (RepositoryRow)obj;
/*     */           
/* 635 */           idStr = row.getProperties().getIdValue("Id").toString();
/* 636 */           filteredIdMap.put(idStr, row);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 641 */     Tracer.traceMethodExit(new Object[] { filteredIdMap });
/* 642 */     return filteredIdMap;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Map<String, Double> filterFposWithCBR(List<String> fposRowIdList, boolean bRankSort)
/*     */   {
/* 648 */     Tracer.traceMethodEntry(new Object[] { fposRowIdList });
/* 649 */     Map<String, Double> filteredIdMap = new HashMap();
/* 650 */     RepositoryRowSet rowSet = null;
/* 651 */     if (fposRowIdList == null)
/*     */     {
/* 653 */       Tracer.traceMethodExit(new Object[] { filteredIdMap });
/* 654 */       return filteredIdMap;
/*     */     }
/*     */     
/* 657 */     String cbrSearchClause = this.srchDef.buildContentQueryStmt(fposRowIdList, bRankSort);
/* 658 */     if (Tracer.isMaximumTraceEnabled()) {
/* 659 */       Tracer.traceMaximumMsg("CBR Filter Query Statement: {0}.", new Object[] { cbrSearchClause });
/*     */     }
/*     */     try
/*     */     {
/* 663 */       SearchScope searchScope = new SearchScope(this.cbrObjectStores, MergeMode.INTERSECTION);
/* 664 */       SearchSQL sqlObject = new SearchSQL();
/* 665 */       sqlObject.setQueryString(cbrSearchClause);
/*     */       
/* 667 */       Integer pageSize = Integer.valueOf(fposRowIdList.size());
/* 668 */       Boolean continuable = Boolean.TRUE;
/* 669 */       rowSet = searchScope.fetchRows(sqlObject, pageSize, null, continuable);
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 673 */       if (isCBRHeterogenous()) {
/* 674 */         throw RMRuntimeException.createRMRuntimeException(RMErrorCode.CBR_HETEROGENEOUS_NOT_SUPPORTED, new Object[] { this });
/*     */       }
/* 676 */       throw processCBRInitException(ex, cbrSearchClause, this.cbrObjectStores);
/*     */     }
/*     */     
/* 679 */     if (rowSet != null)
/*     */     {
/* 681 */       PageIterator jacePI = rowSet.pageIterator();
/* 682 */       while (jacePI.nextPage())
/*     */       {
/* 684 */         Object[] currentPage = jacePI.getCurrentPage();
/* 685 */         for (Object obj : currentPage)
/*     */         {
/* 687 */           RepositoryRow row = (RepositoryRow)obj;
/* 688 */           if ((row.getProperties().get("RecordInformation") != null) && (row.getProperties().get("RecordInformation").getIdValue() != null))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 694 */             String idStr = row.getProperties().get("RecordInformation").getIdValue().toString();
/*     */             
/* 696 */             if ((row.getProperties().get("Rank") != null) && (row.getProperties().get("Rank").getFloat64Value() != null))
/*     */             {
/* 698 */               Double rank = row.getProperties().get("Rank").getFloat64Value();
/* 699 */               filteredIdMap.put(idStr, rank);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 705 */     Tracer.traceMethodExit(new Object[] { filteredIdMap });
/* 706 */     return filteredIdMap;
/*     */   }
/*     */   
/*     */   protected boolean hasNextPageCBR()
/*     */   {
/* 711 */     Tracer.traceMethodEntry(new Object[0]);
/* 712 */     boolean result = this.cbrInprocPageInfo != null ? this.cbrInprocPageInfo.hasNextPage() : false;
/* 713 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 714 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean hasNextPageFPOS()
/*     */   {
/* 719 */     Tracer.traceMethodEntry(new Object[0]);
/* 720 */     boolean result = this.fposInprocPageInfo != null ? this.fposInprocPageInfo.hasNextPage() : false;
/* 721 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 722 */     return result;
/*     */   }
/*     */   
/*     */   protected boolean hasCarryOverItem()
/*     */   {
/* 727 */     Tracer.traceMethodEntry(new Object[0]);
/* 728 */     boolean result = false;
/* 729 */     if ((this.pageCounter <= this.pageCarryOverList.size()) && (this.pageCounter > 0))
/*     */     {
/* 731 */       P8CE_CBRPage page = (P8CE_CBRPage)this.pageCarryOverList.get(this.pageCounter - 1);
/* 732 */       if (page.hasItemsForNextPage()) {
/* 733 */         result = true;
/*     */       }
/*     */     }
/* 736 */     Tracer.traceMethodExit(new Object[] { Boolean.valueOf(result) });
/* 737 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private RMRuntimeException processCBRInitException(Exception ex, String cbrStmt, ObjectStore[] cbrObjectStores)
/*     */   {
/* 743 */     RMRuntimeException engEx = processCSSEngineException(ex);
/* 744 */     if (engEx != null) { return engEx;
/*     */     }
/* 746 */     RMErrorCode errCode = P8CE_Util.determineErrorCode(RMErrorCode.CBR_SEARCH_INIT_FAILED, ex);
/* 747 */     StringBuilder sb = new StringBuilder();
/* 748 */     for (ObjectStore os : cbrObjectStores)
/*     */     {
/* 750 */       sb.append(';').append(os.get_Name());
/*     */     }
/* 752 */     return P8CE_Util.processJaceException(ex, errCode, new Object[] { cbrStmt, sb.toString() });
/*     */   }
/*     */   
/*     */ 
/*     */   private RMRuntimeException processCSSEngineException(Exception ex)
/*     */   {
/* 758 */     if ((ex != null) && ((ex instanceof EngineRuntimeException)))
/*     */     {
/* 760 */       EngineRuntimeException rtEx = (EngineRuntimeException)ex;
/* 761 */       if (rtEx.getExceptionCode() == ExceptionCode.CBR_FULLTEXTROWLIMIT_EXCEEDED)
/*     */       {
/* 763 */         return RMRuntimeException.createRMRuntimeException(RMErrorCode.CBR_SEARCH_OUT_OF_CAPACITY, new Object[] { this });
/*     */       }
/*     */     }
/* 766 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected String traceCBRRowset(List<CBRResult> ret)
/*     */   {
/* 772 */     if (ret == null) return "null";
/* 773 */     List<RepositoryRow> rrList = new ArrayList();
/* 774 */     for (CBRResult item : ret)
/*     */     {
/* 776 */       if ((item instanceof P8CE_CBRResultImpl)) {
/* 777 */         RepositoryRow rr = ((P8CE_CBRResultImpl)item).getP8CEResultRow();
/* 778 */         if (rr != null) rrList.add(rr);
/*     */       }
/*     */     }
/* 781 */     return traceCBRRowset(rrList.toArray());
/*     */   }
/*     */   
/*     */ 
/*     */   protected String traceCBRRowset(Object[] rowSet)
/*     */   {
/* 787 */     StringBuilder sb = new StringBuilder();
/* 788 */     Properties props = null;
/* 789 */     for (Object row : rowSet)
/*     */     {
/* 791 */       if ((row instanceof RepositoryRow)) {
/* 792 */         props = ((RepositoryRow)row).getProperties();
/* 793 */       } else if ((row instanceof IndependentObject)) {
/* 794 */         props = ((IndependentObject)row).getProperties();
/*     */       }
/* 796 */       if ((props != null) && (props.isPropertyPresent("DocumentTitle")))
/*     */       {
/* 798 */         sb.append(props.getStringValue("DocumentTitle")).append(';');
/*     */       }
/* 800 */       else if ((props != null) && (props.isPropertyPresent("Id")))
/*     */       {
/* 802 */         sb.append(props.getIdValue("Id").toString()).append(';');
/*     */       }
/*     */     }
/*     */     
/* 806 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\jarm\ral\p8ce\cbr\P8CE_CBRSearchImplementor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */