/*     */ package com.ibm.ier.plugin.search.p8;
/*     */ 
/*     */ import com.filenet.api.collection.EngineSet;
/*     */ import com.filenet.api.collection.PageIterator;
/*     */ import com.filenet.api.core.Document;
/*     */ import com.filenet.api.core.IndependentlyPersistableObject;
/*     */ import com.filenet.api.core.ObjectStore;
/*     */ import com.filenet.api.exception.EngineRuntimeException;
/*     */ import com.filenet.api.exception.ExceptionCode;
/*     */ import com.filenet.api.property.PropertyFilter;
/*     */ import com.filenet.api.query.SearchScope;
/*     */ import com.filenet.api.util.Id;
/*     */ import java.lang.reflect.Field;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class P8CeSearchUtilProxy
/*     */ {
/*     */   public static final String STORED_SEARCH = "StoredSearch";
/*     */   private static Boolean cascadeSupported;
/*     */   private static Boolean storedSearchSupported;
/*     */   private static Boolean pageIteratorCheckpointSupported;
/*     */   private static Object maxLengthExceededExceptionCode;
/*     */   
/*     */   public static boolean isCascadeSupported()
/*     */   {
/*  38 */     if (cascadeSupported != null) {
/*  39 */       return cascadeSupported.booleanValue();
/*     */     }
/*     */     try {
/*  42 */       Class<?> cbrSearchType = Class.forName("com.filenet.api.constants.CBRSearchType");
/*  43 */       cbrSearchType.getDeclaredField("TEXT_SEARCH");
/*  44 */       cascadeSupported = Boolean.valueOf(true);
/*     */     } catch (Exception e) {
/*  46 */       cascadeSupported = Boolean.valueOf(false);
/*     */     }
/*     */     
/*  49 */     return cascadeSupported.booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean isStoredSearchSupported() {
/*  53 */     if (storedSearchSupported != null) {
/*  54 */       return storedSearchSupported.booleanValue();
/*     */     }
/*     */     try {
/*  57 */       Class.forName("com.filenet.api.query.StoredSearch");
/*  58 */       Class.forName("com.filenet.api.query.SearchTemplateParameters");
/*  59 */       storedSearchSupported = Boolean.valueOf(true);
/*     */     } catch (Exception e) {
/*  61 */       storedSearchSupported = Boolean.valueOf(false);
/*     */     }
/*     */     
/*  64 */     return storedSearchSupported.booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean isPageIteratorCheckpointSupported() {
/*  68 */     if (cascadeSupported != null) {
/*  69 */       return cascadeSupported.booleanValue();
/*     */     }
/*     */     try {
/*  72 */       PageIterator.class.getDeclaredMethod("getNextPageCheckpoint", new Class[0]);
/*  73 */       pageIteratorCheckpointSupported = Boolean.valueOf(true);
/*     */     } catch (Exception e) {
/*  75 */       pageIteratorCheckpointSupported = Boolean.valueOf(false);
/*     */     }
/*     */     
/*  78 */     return pageIteratorCheckpointSupported.booleanValue();
/*     */   }
/*     */   
/*     */   public static PageIterator resumePageIterator(P8Connection connection, byte[] checkpoint) {
/*  82 */     if (!isPageIteratorCheckpointSupported()) {
/*  83 */       return null;
/*     */     }
/*  85 */     return P8CeSearchUtil.resumePageIterator(connection, checkpoint);
/*     */   }
/*     */   
/*     */   public static byte[] getNextPageCheckpoint(PageIterator pageIterator) {
/*  89 */     if (!isPageIteratorCheckpointSupported()) {
/*  90 */       return null;
/*     */     }
/*  92 */     return P8CeSearchUtil.getNextPageCheckpoint(pageIterator);
/*     */   }
/*     */   
/*     */   public static IndependentlyPersistableObject fetchStoredSearch(ObjectStore objectStore, Id searchId) {
/*  96 */     if (!isStoredSearchSupported()) {
/*  97 */       return null;
/*     */     }
/*  99 */     return P8CeSearchUtil.fetchStoredSearch(objectStore, searchId);
/*     */   }
/*     */   
/*     */   public static Document getStoredSearch(ObjectStore objectStore, String storedSearchId) {
/* 103 */     if (!isStoredSearchSupported()) {
/* 104 */       return null;
/*     */     }
/* 106 */     return P8CeSearchUtil.getStoredSearch(objectStore, storedSearchId);
/*     */   }
/*     */   
/*     */   public static boolean isContentSearchInvalidException(EngineRuntimeException exception) {
/* 110 */     if (!isCascadeSupported()) {
/* 111 */       return false;
/*     */     }
/* 113 */     return P8CeSearchUtil.isContentSearchInvalidException(exception);
/*     */   }
/*     */   
/*     */   public static boolean isMaxLengthExceededException(EngineRuntimeException exception) {
/* 117 */     if (maxLengthExceededExceptionCode == null)
/*     */     {
/*     */       try
/*     */       {
/* 121 */         Field field = ExceptionCode.class.getDeclaredField("DB_QUERY_COLUMN_LENGTH_EXCEEDED");
/* 122 */         Object exceptionCode = field.get(null);
/* 123 */         maxLengthExceededExceptionCode = exceptionCode != null ? exceptionCode : new Object();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 127 */         maxLengthExceededExceptionCode = new Object();
/*     */       }
/*     */     }
/* 130 */     return exception.getExceptionCode() == maxLengthExceededExceptionCode;
/*     */   }
/*     */   
/*     */   public static Object convertToCeSearchParameters(P8Connection connection, P8SearchDefinition.SearchClause searchClause, String sortProp, boolean sortDescending, boolean selectOnly) throws Exception {
/* 134 */     if (!isStoredSearchSupported()) {
/* 135 */       return null;
/*     */     }
/* 137 */     return P8CeSearchUtil.convertToCeSearchParameters(connection, searchClause, sortProp, sortDescending, selectOnly);
/*     */   }
/*     */   
/*     */   public static EngineSet executeStoredSearch(SearchScope searchScope, Document storedSearch, String fromClass, Object searchParameters, int pageSize, PropertyFilter propertyFilter, boolean orderByRank) {
/* 141 */     if (!isStoredSearchSupported()) {
/* 142 */       return null;
/*     */     }
/* 144 */     return P8CeSearchUtil.executeStoredSearch(searchScope, storedSearch, fromClass, searchParameters, pageSize, propertyFilter, orderByRank);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8CeSearchUtilProxy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */