/*     */ package com.ibm.ier.plugin.util;
/*     */ 
/*     */ import com.filenet.api.collection.EngineSet;
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.constants.MergeMode;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchSQL;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.ibm.ecm.serviceability.Logger;
/*     */ import com.ibm.ier.plugin.search.p8.P8CeSearchUtilProxy;
/*     */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*     */ import com.ibm.ier.plugin.search.p8.P8Query;
/*     */ import com.ibm.ier.plugin.search.p8.P8Util;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.servlet.ServletRequest;
/*     */ 
/*     */ public class P8Iterator
/*     */   implements Iterator<Object>
/*     */ {
/*     */   private final ServletRequest request;
/*     */   private final P8Connection connection;
/*     */   private final P8QueryContinuationData continuationData;
/*     */   private final int pageSize;
/*     */   private int fetchedItemsCount;
/*     */   private PageIterator pageIterator;
/*     */   private Iterator<Object> iterator;
/*     */   
/*     */   public P8Iterator(ServletRequest request, P8Connection connection, P8QueryContinuationData continuationData, int pageSize)
/*     */   {
/*  34 */     this.request = request;
/*  35 */     this.connection = connection;
/*  36 */     this.continuationData = continuationData;
/*  37 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/*  41 */     if ((this.iterator == null) || (!this.iterator.hasNext())) {
/*  42 */       retrieveNextPage();
/*     */     }
/*  44 */     return (this.iterator != null) && (this.iterator.hasNext());
/*     */   }
/*     */   
/*     */   public Object next() {
/*  48 */     if (!hasNext()) {
/*  49 */       throw new NoSuchElementException();
/*     */     }
/*  51 */     return this.iterator.next();
/*     */   }
/*     */   
/*     */   private void retrieveNextPage() {
/*  55 */     if (this.pageIterator == null) {
/*  56 */       this.pageIterator = startPageIterator();
/*     */     }
/*  58 */     if ((this.pageIterator != null) && (this.pageIterator.nextPage())) {
/*  59 */       this.iterator = getCurrentPage();
/*     */     } else {
/*  61 */       reset();
/*  62 */       int remaining = this.pageSize - this.fetchedItemsCount;
/*  63 */       if ((!this.continuationData.returnOnlyFolders) && (!this.continuationData.queriedAll) && (remaining > 0)) {
/*  64 */         this.pageIterator = search(true, remaining);
/*  65 */         if ((this.pageIterator != null) && (this.pageIterator.nextPage()))
/*  66 */           this.iterator = getCurrentPage();
/*  67 */         this.continuationData.queriedAll = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/*  73 */     this.iterator = null;
/*  74 */     if (this.pageIterator != null)
/*  75 */       this.pageIterator.reset();
/*  76 */     this.pageIterator = null;
/*     */   }
/*     */   
/*     */   public Integer getTotalCount() {
/*  80 */     Integer result = Integer.valueOf(0);
/*  81 */     if (this.pageIterator != null) {
/*  82 */       result = this.pageIterator.getTotalCount();
/*     */     }
/*  84 */     return result;
/*     */   }
/*     */   
/*     */   private Iterator<Object> getCurrentPage() {
/*  88 */     if (this.pageIterator == null) {
/*  89 */       return null;
/*     */     }
/*  91 */     Object[] objects = this.pageIterator.getCurrentPage();
/*  92 */     this.fetchedItemsCount += objects.length;
/*     */     
/*  94 */     return Arrays.asList(objects).iterator();
/*     */   }
/*     */   
/*     */   private PageIterator startPageIterator() {
/*  98 */     if (this.continuationData.checkpoint == null) {
/*  99 */       return search(false);
/*     */     }
/* 101 */     return resume();
/*     */   }
/*     */   
/*     */   PageIterator resume()
/*     */   {
/* 106 */     Logger.logEntry(this, "resume", this.request);
/* 107 */     if (this.continuationData.checkpoint == null) {
/* 108 */       return null;
/*     */     }
/* 110 */     PageIterator pi = P8CeSearchUtilProxy.resumePageIterator(this.connection, this.continuationData.checkpoint);
/* 111 */     pi.setPageSize(this.pageSize);
/* 112 */     Logger.logExit(this, "resume", this.request);
/*     */     
/* 114 */     return pi;
/*     */   }
/*     */   
/*     */   private PageIterator search(boolean secondQuery) {
/* 118 */     return search(secondQuery, this.pageSize);
/*     */   }
/*     */   
/*     */   private PageIterator search(boolean secondQuery, int currentPageSize) {
/* 122 */     if (currentPageSize < 1)
/* 123 */       return null;
/*     */     PageIterator pi;
/*     */     PageIterator pi;
/* 126 */     if (isStoredSearchSet()) {
/* 127 */       pi = executeStoredSearch(secondQuery, currentPageSize);
/*     */     } else {
/* 129 */       pi = runSql(secondQuery, currentPageSize);
/*     */     }
/* 131 */     return pi;
/*     */   }
/*     */   
/*     */   private boolean isStoredSearchSet() {
/* 135 */     return ((this.continuationData.ceDocumentSearchParams != null) || (this.continuationData.ceFolderSearchParams != null)) && (this.continuationData.ceStoredSearch != null);
/*     */   }
/*     */   
/*     */   private PageIterator runSql(boolean secondQuery, int currentPageSize) {
/* 139 */     String sql = isSearchingForFolders(secondQuery) ? this.continuationData.folderSQL : this.continuationData.documentSQL;
/* 140 */     if ((sql == null) || (sql.length() < 1)) {
/* 141 */       return null;
/*     */     }
/* 143 */     return invokeSql(sql, secondQuery, currentPageSize);
/*     */   }
/*     */   
/*     */   PageIterator invokeSql(String sql, boolean secondQuery, int currentPageSize) {
/* 147 */     Logger.logEntry(this, "invokeSql", this.request);
/* 148 */     if ((sql == null) || (sql.length() < 1)) {
/* 149 */       return null;
/*     */     }
/* 151 */     Logger.logDebug(this, "invokeSql", this.request, "sql: " + sql);
/* 152 */     SearchSQL search = new SearchSQL(sql);
/* 153 */     PropertyFilter pf = isSearchingForFolders(secondQuery) ? P8Query.getFolderPropertyFilter() : P8Query.getDocumentPropertyFilter();
/* 154 */     SearchScope searchScope = getSearchScope();
/* 155 */     EngineSet resultSet = this.continuationData.orderByRank ? searchScope.fetchRows(search, Integer.valueOf(currentPageSize), pf, Boolean.TRUE) : searchScope.fetchObjects(search, Integer.valueOf(currentPageSize), pf, Boolean.TRUE);
/* 156 */     Logger.logExit(this, "invokeSql", this.request);
/*     */     
/* 158 */     return resultSet.pageIterator();
/*     */   }
/*     */   
/*     */   private PageIterator executeStoredSearch(boolean secondQuery, int currentPageSize) {
/* 162 */     Object searchParams = isSearchingForFolders(secondQuery) ? this.continuationData.ceFolderSearchParams : this.continuationData.ceDocumentSearchParams;
/* 163 */     if (searchParams == null) {
/* 164 */       return null;
/*     */     }
/* 166 */     return invokeStoredSearch(searchParams, secondQuery, currentPageSize);
/*     */   }
/*     */   
/*     */   PageIterator invokeStoredSearch(Object searchParams, boolean secondQuery, int currentPageSize) {
/* 170 */     Logger.logEntry(this, "invokeStoredSearch", this.request);
/* 171 */     if (searchParams == null) {
/* 172 */       return null;
/*     */     }
/* 174 */     SearchScope searchScope = getSearchScope();
/* 175 */     boolean searchFolders = isSearchingForFolders(secondQuery);
/* 176 */     String fromClass = searchFolders ? "Folder" : "Document";
/* 177 */     PropertyFilter pf = searchFolders ? P8Query.getFolderPropertyFilter() : P8Query.getDocumentPropertyFilter();
/* 178 */     EngineSet resultsRowtSet = P8CeSearchUtilProxy.executeStoredSearch(searchScope, this.continuationData.ceStoredSearch, fromClass, searchParams, currentPageSize, pf, this.continuationData.orderByRank);
/* 179 */     if (resultsRowtSet == null)
/* 180 */       return runSql(secondQuery, currentPageSize);
/* 181 */     Logger.logExit(this, "invokeStoredSearch", this.request);
/*     */     
/* 183 */     return resultsRowtSet.pageIterator();
/*     */   }
/*     */   
/*     */   private boolean isSearchingForFolders(boolean secondQuery) {
/* 187 */     return (this.continuationData.returnOnlyFolders) || ((!this.continuationData.descending) && (!secondQuery)) || ((this.continuationData.descending) && (secondQuery));
/*     */   }
/*     */   
/*     */   private SearchScope getSearchScope()
/*     */   {
/* 192 */     String[] osIds = this.continuationData.getObjectStoreIds();
/* 193 */     SearchScope searchScope; SearchScope searchScope; if (osIds.length > 1) {
/* 194 */       ObjectStore[] ceStores = new ObjectStore[osIds.length];
/* 195 */       for (int i = 0; i < osIds.length; i++) {
/* 196 */         ObjectStore ceStore = P8Util.getObjectStore(this.connection, osIds[i]);
/* 197 */         ceStores[i] = ceStore;
/*     */       }
/* 199 */       searchScope = new SearchScope(ceStores, this.continuationData.mergeUnion ? MergeMode.UNION : MergeMode.INTERSECTION);
/*     */     } else {
/* 201 */       searchScope = new SearchScope(P8Util.getObjectStore(this.connection, osIds[0]));
/*     */     }
/*     */     
/* 204 */     return searchScope;
/*     */   }
/*     */   
/*     */   public void remove() {
/* 208 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public byte[] getNextPageCheckpoint() {
/* 212 */     return P8CeSearchUtilProxy.getNextPageCheckpoint(this.pageIterator);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\P8Iterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */