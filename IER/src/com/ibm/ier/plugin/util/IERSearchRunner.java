/*      */ package com.ibm.ier.plugin.util;
/*      */ 
/*      */ import com.filenet.api.constants.DatabaseType;
/*      */ import com.filenet.api.core.Factory.ClassDescription;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.meta.ClassDescription;
/*      */ import com.ibm.ier.plugin.nls.Logger;
/*      */ import com.ibm.ier.plugin.search.p8.P8CeSearchUtilProxy;
/*      */ import com.ibm.ier.plugin.search.p8.P8Connection;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.Clause;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.ClauseItem;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.Container;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.ContentClauseItem;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.ContentCriteria;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.ContentTerm;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.DataType;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.GroupAction;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.JoinType;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.Operator;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.RequiredState;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.SearchClause;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.SearchInFolder;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.SearchInObjectStore;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.SelectProperty;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.SortOrder;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.Subclass;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.VerityClauseInItem;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.VerityClauseVQLItem;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.VeritySearchOperator;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchDefinition.WhereClauseCondition;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchTemplate;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchTemplate.EnumTextSearchOption;
/*      */ import com.ibm.ier.plugin.search.p8.P8SearchUtil;
/*      */ import com.ibm.ier.plugin.search.p8.P8TextSearchUtil;
/*      */ import com.ibm.ier.plugin.search.p8.P8Util;
/*      */ import com.ibm.ier.plugin.search.util.SearchCriteria;
/*      */ import com.ibm.ier.plugin.search.util.SearchCriterion;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchClass;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchEditProperty;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchFolder;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchCriterion;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchType;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ResultsDisplay;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.VersionOption;
/*      */ import com.ibm.jarm.api.collection.CBRPageIterator;
/*      */ import com.ibm.jarm.api.collection.PageableSet;
/*      */ import com.ibm.jarm.api.constants.EntityType;
/*      */ import com.ibm.jarm.api.core.BaseEntity;
/*      */ import com.ibm.jarm.api.core.Repository;
/*      */ import com.ibm.jarm.api.query.CBRResult;
/*      */ import com.ibm.jarm.api.query.RMContentSearchDefinition;
/*      */ import com.ibm.jarm.api.query.RMContentSearchDefinition.AndOrOper;
/*      */ import com.ibm.jarm.api.query.RMContentSearchDefinition.ContentSearchOption;
/*      */ import com.ibm.jarm.api.query.RMContentSearchDefinition.OrderBy;
/*      */ import com.ibm.jarm.api.query.RMContentSearchDefinition.SortOrder;
/*      */ import com.ibm.jarm.api.query.RMSearch;
/*      */ import com.ibm.jarm.api.util.P8CE_Convert;
/*      */ import com.ibm.jarm.ral.p8ce.cbr.P8CE_RMContentSearchDefinition;
/*      */ import java.io.IOException;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.text.ParseException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.UUID;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpSession;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class IERSearchRunner
/*      */ {
/*   83 */   private static final String[] RECORD_SELECT_PROPS = { "Id", "IsReserved", "VersionStatus", "IsDeleted", "VersionSeries", "MajorVersionNumber", "MinorVersionNumber", "MimeType", "RMEntityType", "DocumentTitle", "CurrentPhaseExecutionDate", "CurrentPhaseExecutionStatus", "CutoffDate", "OnHold", "Creator", "RMEntityDescription", "LastModifier", "DateLastModified", "ContentSize", "ClassDescription" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   94 */   private static final String[] RMFOLDER_SELECT_PROPS = { "FolderName", "Id", "ContainerType", "RMEntityType", "IsDeleted", "ReOpenedDate", "DateClosed", "CurrentPhaseExecutionDate", "CurrentPhaseExecutionStatus", "CutoffDate", "OnHold", "IsHiddenContainer", "ClassDescription" };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */   private static final String[] RECORDCATEGORY_SELECT_PROPS = { "RecordCategoryIdentifier", "RecordCategoryName" };
/*      */   
/*      */ 
/*  104 */   private static final String[] RECORDFOLDER_SELECT_PROPS = { "RecordFolderIdentifier", "RecordFolderName" };
/*      */   
/*      */ 
/*  107 */   private static final String[] RECORDVOLUME_SELECT_PROPS = { "VolumeName", "Id" };
/*      */   
/*      */ 
/*  110 */   private static final Map<String, String[]> RMFOLDER_ADDITIONAL_SELECT_PROPS = new HashMap();
/*      */   
/*  112 */   static { RMFOLDER_ADDITIONAL_SELECT_PROPS.put("RecordCategory", RECORDCATEGORY_SELECT_PROPS);
/*  113 */     RMFOLDER_ADDITIONAL_SELECT_PROPS.put("RecordFolder", RECORDFOLDER_SELECT_PROPS);
/*  114 */     RMFOLDER_ADDITIONAL_SELECT_PROPS.put("Volume", RECORDVOLUME_SELECT_PROPS);
/*      */   }
/*      */   
/*  117 */   private String PROPERTY_TABLE_ALIAS = "d";
/*      */   
/*      */   private static final String RMFOLDER_TABLE_ALIAS = "f";
/*      */   
/*      */   private static final String CONTENT_TABLE_ALIAS = "c";
/*      */   
/*      */   private static final String SUMMARY_TABLE_ALIAS = "s";
/*      */   
/*      */   private static final String K2VQL = "K2VQL";
/*      */   private static final String LUCENE = "Lucene";
/*      */   private static final String NONPROPSQL = "nonPropSQL";
/*      */   public static final String RECORD_INFORMATION = "RecordInformation";
/*      */   public static final String AUTO_RUN = "IcnAutoRun";
/*      */   public static final String SHOW_IN_TREE = "IcnShowInTree";
/*      */   public static final String STORED_SEARCH = "StoredSearch";
/*      */   public static final String ENTRY_TEMPLATE_OBJECTSTORE = "EntryTemplateObjectStoreName";
/*      */   public static final String ENTRY_TEMPLATE_ID = "EntryTemplateId";
/*      */   public static final String ENTRY_TEMPLATE_LAUNCHED_WORKFLOW_NUMBER = "EntryTemplateLaunchedWorkflowNumber";
/*      */   private final HttpServletRequest request;
/*      */   private P8SearchTemplate searchTemplate;
/*      */   private P8SearchDefinition searchDefinition;
/*      */   private String storedSearchId;
/*      */   private Repository repository;
/*      */   private P8QueryContinuationData queryData;
/*      */   private P8Connection connection;
/*      */   private RMContentSearchDefinition cbrDefinition;
/*      */   private int pageSize;
/*      */   private Integer totalCount;
/*      */   
/*      */   public Integer getTotalCount()
/*      */   {
/*  148 */     return this.totalCount;
/*      */   }
/*      */   
/*      */   public void setTotalCount(Integer totalCount) {
/*  152 */     this.totalCount = totalCount;
/*      */   }
/*      */   
/*      */   public void setPropertyTableAlias(String tableAlias) {
/*  156 */     this.PROPERTY_TABLE_ALIAS = tableAlias;
/*      */   }
/*      */   
/*      */   public IERSearchRunner(HttpServletRequest request, Repository repository, P8SearchTemplate searchTemplate, String storedSearchId, P8Connection connection) {
/*  160 */     this.request = request;
/*  161 */     this.repository = repository;
/*  162 */     this.searchTemplate = searchTemplate;
/*  163 */     this.searchDefinition = searchTemplate.getSearchDefinition();
/*  164 */     this.storedSearchId = storedSearchId;
/*  165 */     this.connection = connection;
/*      */   }
/*      */   
/*      */   public IERSearchRunner(HttpServletRequest request, P8QueryContinuationData continuationData) {
/*  169 */     this.request = request;
/*  170 */     this.queryData = continuationData;
/*      */   }
/*      */   
/*      */   public List<?> runSearch(String orderBy, boolean orderDescending, int maxResults, int timeout, int countLimit) throws Exception
/*      */   {
/*  175 */     if (this.searchTemplate == null) {
/*  176 */       return null;
/*      */     }
/*      */     
/*  179 */     if ((this.searchTemplate.getResultsDisplay() != null) && ((orderBy == null) || (orderBy.length() == 0))) {
/*  180 */       orderBy = this.searchTemplate.getResultsDisplay().getSortByProperty();
/*  181 */       orderDescending = !this.searchTemplate.getResultsDisplay().getSortAscending();
/*      */     }
/*      */     
/*  184 */     convertToP8SearchDefinition();
/*  185 */     this.cbrDefinition = null;
/*  186 */     this.queryData = new P8QueryContinuationData();
/*  187 */     this.queryData.orderByRank = ((orderBy != null) && (orderBy.equalsIgnoreCase("Rank")));
/*  188 */     this.queryData.descending = orderDescending;
/*      */     
/*  190 */     this.queryData.mergeUnion = this.searchDefinition.isMergeOptionUnion();
/*      */     
/*      */ 
/*  193 */     this.queryData.searchClassNames = this.searchTemplate.getFirstClassName();
/*      */     
/*  195 */     if ((this.storedSearchId != null) && (!this.storedSearchId.isEmpty()) && (!this.searchDefinition.isApiIncompatible()) && (P8CeSearchUtilProxy.isStoredSearchSupported()))
/*      */     {
/*      */ 
/*  198 */       ObjectStore searchStore = P8Util.getObjectStore(this.request, this.connection, this.storedSearchId);
/*  199 */       this.queryData.ceStoredSearch = P8CeSearchUtilProxy.getStoredSearch(searchStore, this.storedSearchId);
/*  200 */       SearchTemplateBase.ObjectType objectType = this.searchTemplate.getObjectType();
/*  201 */       P8SearchDefinition.SearchClause clause = this.searchDefinition.getSearchClause(objectType);
/*  202 */       if (clause != null) {
/*  203 */         Object searchParams = P8CeSearchUtilProxy.convertToCeSearchParameters(this.connection, clause, orderBy, orderDescending, !this.searchDefinition.isTemplate());
/*  204 */         if (objectType == SearchTemplateBase.ObjectType.folder) {
/*  205 */           this.queryData.ceFolderSearchParams = searchParams;
/*  206 */         } else if (objectType == SearchTemplateBase.ObjectType.document)
/*  207 */           this.queryData.ceDocumentSearchParams = searchParams;
/*      */       }
/*      */     } else {
/*  210 */       Map<String, String> listOfSearches = getQueries(orderBy, orderDescending, maxResults, timeout, countLimit);
/*  211 */       for (Map.Entry<String, String> e : listOfSearches.entrySet()) {
/*  212 */         String sqlString = (String)e.getValue();
/*  213 */         String key = (String)e.getKey();
/*  214 */         if (key.equals(SearchTemplateBase.ObjectType.folder.toString())) {
/*  215 */           this.queryData.folderSQL = sqlString;
/*  216 */         } else if (key.equals(SearchTemplateBase.ObjectType.document.toString())) {
/*  217 */           this.queryData.documentSQL = sqlString;
/*  218 */         } else if (!key.equals("nonPropSQL")) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  223 */     Repository repo = getSearchScopeRepository();
/*  224 */     if (repo != null) { this.queryData.objectStoreId = repo.getObjectIdentity();
/*      */     }
/*  226 */     this.pageSize = IERUtil.getSearchPagingSize(this.request, this.cbrDefinition);
/*  227 */     List<?> hits = null;
/*  228 */     ForwardPager<BaseEntity> pager = null;
/*  229 */     if (this.cbrDefinition == null) {
/*  230 */       this.queryData.rmCBRDef = null;
/*  231 */       String queryString = this.searchTemplate.getObjectType() == SearchTemplateBase.ObjectType.document ? this.queryData.documentSQL : this.queryData.folderSQL;
/*  232 */       PageableSet<BaseEntity> pSet = IERQuery.executeQueryAsObjectsPageableSet(repo, queryString, null, this.pageSize, EntityType.Unknown);
/*  233 */       Iterator<BaseEntity> iterator = pSet.iterator();
/*  234 */       pager = new ForwardPager(iterator, this.pageSize);
/*  235 */       hits = pager.loadNextPage();
/*      */       
/*      */ 
/*  238 */       if (countLimit > 0) {
/*      */         try {
/*  240 */           Integer rawValue = pSet.totalCount();
/*  241 */           this.totalCount = Integer.valueOf(rawValue != null ? rawValue.intValue() : hits.size());
/*      */         } catch (NoSuchMethodError ignored) {
/*  243 */           this.totalCount = Integer.valueOf(hits.size());
/*      */         }
/*      */       }
/*      */       
/*  247 */       if (!pager.isEndReached()) {
/*  248 */         this.queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/*  249 */         this.queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/*  250 */         this.request.getSession().setAttribute(this.queryData.sessionKey, pager);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  282 */       this.queryData.rmCBRDef = ((P8CE_RMContentSearchDefinition)this.cbrDefinition);
/*  283 */       RMSearch uut = new RMSearch(repo);
/*  284 */       PageableSet<CBRResult> ps = uut.contentBasedRetrieval(this.cbrDefinition, Integer.valueOf(this.pageSize), Boolean.valueOf(true));
/*      */       
/*  286 */       CBRPageIterator it = (CBRPageIterator)ps.pageIterator();
/*  287 */       pager = new CBRForwardPager(it, this.pageSize);
/*  288 */       hits = pager.loadNextPage();
/*      */       
/*  290 */       if (hits != null) {
/*  291 */         this.totalCount = null;
/*      */       }
/*  293 */       if (!pager.isEndReached()) {
/*  294 */         this.queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/*  295 */         this.queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/*  296 */         this.request.getSession().setAttribute(this.queryData.sessionKey, pager);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  314 */     return hits;
/*      */   }
/*      */   
/*      */   public int getPageSize() {
/*  318 */     return this.pageSize == 0 ? IERUtil.getSearchPagingSize(this.request, this.cbrDefinition) : this.pageSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getQueryContinuationData()
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  382 */     return (this.queryData == null) || (this.queryData.sessionKey == null) ? null : this.queryData.saveToString();
/*      */   }
/*      */   
/*      */   public void setDisplayCols(String[] cols) {
/*  386 */     if (this.queryData != null) {
/*  387 */       this.queryData.properties = IERUtil.arrayToString(cols);
/*      */     }
/*      */   }
/*      */   
/*      */   public Repository getSearchScopeRepository()
/*      */   {
/*  393 */     List<P8SearchDefinition.SearchInObjectStore> objectStores = this.searchTemplate.getObjectStores();
/*  394 */     if (objectStores != null)
/*      */     {
/*  396 */       ObjectStore os = P8Util.getObjectStore(this.connection, ((P8SearchDefinition.SearchInObjectStore)objectStores.get(0)).getId());
/*  397 */       return P8CE_Convert.fromP8CE(os);
/*      */     }
/*  399 */     return this.repository;
/*      */   }
/*      */   
/*      */   private Map<String, String> getQueries(String sortProperty, boolean sortDescending, int maxResult, int timeout, int countLimit) throws Exception {
/*  403 */     List<P8SearchDefinition.SearchInFolder> folders = this.searchDefinition.getFolders();
/*      */     
/*      */ 
/*  406 */     boolean isSearchAddOnInstalled = true;
/*  407 */     Map<String, String> queries = new HashMap();
/*  408 */     P8SearchDefinition.SearchClause commonClause = this.searchDefinition.getSearchClause(SearchTemplateBase.ObjectType.common);
/*  409 */     SearchTemplateBase.VersionOption versionSelection = this.searchDefinition.getVersionSelection();
/*      */     
/*  411 */     P8SearchDefinition.SearchClause searchClause = this.searchDefinition.getSearchClause(SearchTemplateBase.ObjectType.document);
/*  412 */     if (searchClause != null) {
/*  413 */       List<String> additionalPropertiesList = new ArrayList(Arrays.asList(RECORD_SELECT_PROPS));
/*      */       
/*      */ 
/*      */ 
/*  417 */       List<P8SearchDefinition.Subclass> subclasses = searchClause.getSubclasses();
/*  418 */       if ((isSearchAddOnInstalled) && (subclasses != null))
/*      */       {
/*  420 */         for (P8SearchDefinition.Subclass subclass : subclasses) {
/*  421 */           if ((subclass.getName().equals("StoredSearch")) || ((subclass.getName().equals("Document")) && (subclass.isSearchSubclasses()))) {
/*  422 */             additionalPropertiesList.add("IcnAutoRun");
/*  423 */             additionalPropertiesList.add("IcnShowInTree");
/*  424 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  429 */       StringBuilder nonPropSQLStatementBuffer = new StringBuilder();
/*  430 */       String query = getQuery(searchClause, commonClause, additionalPropertiesList, folders, versionSelection, sortProperty, sortDescending, maxResult, timeout, countLimit, nonPropSQLStatementBuffer);
/*      */       
/*  432 */       queries.put(SearchTemplateBase.ObjectType.document.toString(), query);
/*  433 */       queries.put("nonPropSQL", nonPropSQLStatementBuffer.toString());
/*      */     }
/*  435 */     searchClause = this.searchDefinition.getSearchClause(SearchTemplateBase.ObjectType.folder);
/*  436 */     if (searchClause != null) {
/*  437 */       List<String> additionalProperties = new ArrayList(Arrays.asList(RMFOLDER_SELECT_PROPS));
/*  438 */       additionalProperties.addAll(getRMFolderIdentifier(searchClause));
/*      */       
/*  440 */       String query = getQuery(searchClause, commonClause, additionalProperties, folders, null, sortProperty, sortDescending, maxResult, timeout, countLimit, null);
/*  441 */       queries.put(SearchTemplateBase.ObjectType.folder.toString(), query);
/*      */     }
/*      */     
/*  444 */     return queries;
/*      */   }
/*      */   
/*      */   public String getQuery(P8SearchDefinition.SearchClause searchClause, P8SearchDefinition.SearchClause commonClause, List<String> additionalSelectProps, List<P8SearchDefinition.SearchInFolder> folders, SearchTemplateBase.VersionOption versionSelection, String sortProperty, boolean sortDescending, int maxResult, int timeout, int countLimit, StringBuilder nonPropSQLStatement)
/*      */     throws Exception
/*      */   {
/*  450 */     return getQuery(searchClause, commonClause, additionalSelectProps, folders, versionSelection, sortProperty, sortDescending, maxResult, timeout, countLimit, nonPropSQLStatement, false, false);
/*      */   }
/*      */   
/*      */ 
/*      */   public String getQuery(P8SearchDefinition.SearchClause searchClause, P8SearchDefinition.SearchClause commonClause, List<String> additionalSelectProps, List<P8SearchDefinition.SearchInFolder> folders, SearchTemplateBase.VersionOption versionSelection, String sortProperty, boolean sortDescending, int maxResult, int timeout, int countLimit, StringBuilder nonPropSQLStatement, boolean noOrderBy, boolean noFolderClause)
/*      */     throws Exception
/*      */   {
/*  457 */     if (searchClause == null) {
/*  458 */       return null;
/*      */     }
/*  460 */     StringBuilder buffer = new StringBuilder();
/*  461 */     String selectClause = getSelectClause(searchClause.getSelectProperties(), additionalSelectProps, sortProperty, null);
/*  462 */     String from = getFromClause(searchClause);
/*      */     
/*  464 */     StringBuilder where = new StringBuilder();
/*  465 */     StringBuilder nonPropWhere = new StringBuilder();
/*  466 */     String folderClause = !noFolderClause ? getFolderClause(folders, searchClause.getFrom()) : null;
/*  467 */     if (folderClause != null)
/*      */     {
/*  469 */       where.append(folderClause);
/*  470 */       if (nonPropSQLStatement != null) {
/*  471 */         nonPropWhere.append(folderClause);
/*      */       }
/*      */     }
/*  474 */     String whereClause = getWhereClause(searchClause.getWhereClause(), commonClause == null ? null : commonClause.getWhereClause());
/*  475 */     boolean isDeletedPresent = checkIsDeleted(whereClause);
/*  476 */     boolean hasPropertyCriteria = (whereClause != null) && (whereClause.length() > 0);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  484 */     String versionClause = getAdditionalClause(searchClause, isDeletedPresent);
/*  485 */     if ((versionClause != null) && (versionClause.length() > 1)) {
/*  486 */       if (where.length() > 0)
/*  487 */         where.append(" AND ");
/*  488 */       where.append(versionClause);
/*      */       
/*  490 */       if (nonPropSQLStatement != null) {
/*  491 */         if (nonPropWhere.length() > 0)
/*  492 */           nonPropWhere.append(" AND ");
/*  493 */         nonPropWhere.append(versionClause);
/*      */       }
/*      */     }
/*      */     
/*  497 */     String orderByClause = null;
/*  498 */     RMContentSearchDefinition.OrderBy orderby = null;
/*  499 */     if (!noOrderBy) {
/*  500 */       orderByClause = getOrderByClause(sortProperty, sortDescending);
/*  501 */       if (orderByClause == null) {
/*  502 */         orderByClause = getOrderByClause(searchClause.getSelectProperties());
/*      */       }
/*  504 */       orderby = RMContentSearchDefinition.OrderBy.cbrscores;
/*  505 */       if ((orderByClause != null) && (!orderByClause.startsWith("c.[Rank]"))) {
/*  506 */         orderby = RMContentSearchDefinition.OrderBy.metadata;
/*      */       }
/*      */     }
/*  509 */     P8SearchDefinition.ContentCriteria contentCriteria = searchClause.getContentCriteria();
/*  510 */     String contentClause = getContentClause(contentCriteria);
/*  511 */     String contentClauseIdxProp = getContentClauseOnIdxProperties(contentCriteria);
/*      */     
/*  513 */     boolean bHasWhereClause = (whereClause != null) && (whereClause.length() > 0);
/*  514 */     boolean contentClauseDefined = (contentClause != null) && (contentClause.length() > 0);
/*  515 */     boolean contentClauseIdxPropDefined = (contentClauseIdxProp != null) && (contentClauseIdxProp.length() > 0);
/*      */     
/*      */ 
/*  518 */     if ((contentCriteria != null) && (!contentCriteria.isCascade())) {
/*  519 */       P8SearchTemplate.EnumTextSearchOption contentSearchOption = contentClauseDefined ? this.searchTemplate.getTextSearchOption() : null;
/*  520 */       if (contentSearchOption == P8SearchTemplate.EnumTextSearchOption.property) {
/*  521 */         String cbrString = null;
/*  522 */         List<SearchTemplate.TextSearchCriterion> textSearchCriteria = this.searchTemplate.getTextSearchCriteria();
/*  523 */         if ((textSearchCriteria != null) && (textSearchCriteria.size() > 0)) {
/*  524 */           SearchTemplate.TextSearchCriterion criterion = (SearchTemplate.TextSearchCriterion)textSearchCriteria.get(0);
/*  525 */           cbrString = criterion.getText();
/*      */         }
/*      */         
/*  528 */         contentClauseIdxPropDefined = (cbrString != null) && (cbrString.length() > 0);
/*  529 */         if (contentClauseIdxPropDefined) {
/*  530 */           contentClauseDefined = false;
/*  531 */           contentClauseIdxProp = cbrString;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  536 */     if ((bHasWhereClause) || (contentClauseIdxPropDefined)) {
/*  537 */       if (where.length() > 0)
/*  538 */         where.append(" AND ");
/*  539 */       where.append("(");
/*  540 */       if (bHasWhereClause) {
/*  541 */         where.append("(").append(whereClause).append(")");
/*      */       }
/*  543 */       if (contentClauseIdxPropDefined) {
/*  544 */         if (searchClause.getFrom() == SearchTemplateBase.ObjectType.document) {
/*  545 */           from = "(" + from + ")";
/*      */         }
/*      */         
/*  548 */         P8SearchDefinition.JoinType join = P8SearchDefinition.JoinType.inner;
/*  549 */         if ((join != P8SearchDefinition.JoinType.inner) && (whereClause != null) && (whereClause.length() > 0)) {
/*  550 */           join = P8SearchDefinition.JoinType.leftouter;
/*  551 */           from = from + " LEFT OUTER";
/*      */         } else {
/*  553 */           join = P8SearchDefinition.JoinType.inner;
/*  554 */           from = from + " INNER";
/*      */         }
/*  556 */         from = from + " JOIN ContentSearch c on " + this.PROPERTY_TABLE_ALIAS + ".This = " + "c" + ".QueriedObject";
/*      */         
/*  558 */         if (bHasWhereClause)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*  563 */           if (join == P8SearchDefinition.JoinType.inner) {
/*  564 */             where.append(" AND ");
/*      */           } else {
/*  566 */             where.append(" OR ");
/*      */           }
/*      */         }
/*  569 */         where.append(createContentClause(contentClauseIdxProp, contentCriteria.isCascade(), "content"));
/*  570 */         where.append(")");
/*      */         
/*      */ 
/*  573 */         selectClause = selectClause + ", c.[Rank]";
/*      */       }
/*      */       else {
/*  576 */         where.append(")");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  587 */     if ((contentClauseDefined) || (contentClauseIdxPropDefined))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  595 */       if (!noOrderBy) {
/*  596 */         String orderByClauseCBR = getCBROrderByClause(orderByClause);
/*  597 */         this.cbrDefinition = new P8CE_RMContentSearchDefinition("SELECT " + selectClause, "FROM " + from, "WHERE " + where.toString(), " ORDER BY " + orderByClauseCBR, this.PROPERTY_TABLE_ALIAS, contentClause, sortDescending ? RMContentSearchDefinition.SortOrder.desc : RMContentSearchDefinition.SortOrder.asc, orderby, !hasPropertyCriteria);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  609 */       if (this.cbrDefinition != null) {
/*  610 */         if (contentClauseDefined) {
/*  611 */           this.cbrDefinition.setCommonWhereClause("WHERE " + nonPropWhere.toString());
/*      */           
/*  613 */           this.cbrDefinition.setOperBtwContentAndMetadataSearch(RMContentSearchDefinition.AndOrOper.and);
/*      */         }
/*      */         
/*  616 */         this.cbrDefinition.setContentSearchOption(contentClauseDefined ? RMContentSearchDefinition.ContentSearchOption.content : RMContentSearchDefinition.ContentSearchOption.property);
/*      */       }
/*      */     }
/*      */     
/*  620 */     buffer.append("SELECT ");
/*  621 */     if (maxResult > 0) {
/*  622 */       buffer.append("TOP " + maxResult + " ");
/*      */     }
/*  624 */     buffer.append(selectClause);
/*  625 */     buffer.append(" FROM ").append(from);
/*  626 */     if (where.length() > 0)
/*  627 */       buffer.append(" WHERE ").append(where);
/*  628 */     if ((orderByClause != null) && (orderByClause.length() > 0)) {
/*  629 */       buffer.append(" ORDER BY ");
/*  630 */       buffer.append(orderByClause);
/*      */     }
/*      */     
/*  633 */     String countLimitStr = "";
/*  634 */     if (countLimit > 0) {
/*  635 */       countLimitStr = " COUNT_LIMIT " + countLimit + " ";
/*      */     }
/*      */     
/*  638 */     if (timeout > 0) {
/*  639 */       buffer.append(" OPTIONS (TIMELIMIT " + timeout);
/*  640 */       if (countLimit > 0) {
/*  641 */         buffer.append(", " + countLimitStr + ")");
/*      */       } else {
/*  643 */         buffer.append(")");
/*      */       }
/*  645 */     } else if (countLimit > 0) {
/*  646 */       buffer.append(" OPTIONS (" + countLimitStr + ")");
/*      */     }
/*      */     
/*  649 */     Logger.logDebug(this, "getQuery", buffer.toString());
/*      */     
/*  651 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   public List<String> getRMFolderIdentifier(P8SearchDefinition.SearchClause searchClause)
/*      */   {
/*  656 */     List<P8SearchDefinition.Subclass> subclasses = searchClause.getSubclasses();
/*  657 */     if ((subclasses != null) && (subclasses.size() > 0))
/*      */     {
/*  659 */       String subclassName = ((P8SearchDefinition.Subclass)subclasses.get(0)).getName();
/*  660 */       if (RMFOLDER_ADDITIONAL_SELECT_PROPS.get(subclassName) != null) {
/*  661 */         return Arrays.asList((Object[])RMFOLDER_ADDITIONAL_SELECT_PROPS.get(subclassName));
/*      */       }
/*  663 */       ObjectStore p8OS = P8CE_Convert.fromJARM(this.repository);
/*  664 */       ClassDescription cd = Factory.ClassDescription.fetchInstance(p8OS, subclassName, null);
/*  665 */       Iterator<String> k = RMFOLDER_ADDITIONAL_SELECT_PROPS.keySet().iterator();
/*  666 */       while (k.hasNext()) {
/*  667 */         String key = (String)k.next();
/*  668 */         if (cd.describedIsOfClass(key).booleanValue()) {
/*  669 */           return Arrays.asList((Object[])RMFOLDER_ADDITIONAL_SELECT_PROPS.get(key));
/*      */         }
/*      */       }
/*      */     }
/*  673 */     return null;
/*      */   }
/*      */   
/*      */   private String getCBROrderByClause(String orderByClause)
/*      */   {
/*  678 */     String orderByClauseCBR = orderByClause;
/*  679 */     if ((orderByClause != null) && (orderByClause.endsWith(" DESC"))) {
/*  680 */       orderByClauseCBR = orderByClause.substring(0, orderByClause.length() - " DESC".length());
/*  681 */     } else if ((orderByClause != null) && (orderByClause.endsWith(" ASC"))) {
/*  682 */       orderByClauseCBR = orderByClause.substring(0, orderByClause.length() - " ASC".length());
/*      */     }
/*  684 */     return orderByClauseCBR;
/*      */   }
/*      */   
/*      */   private boolean checkIsDeleted(String whereClause) {
/*  688 */     if (whereClause == null) return false;
/*  689 */     String isDeleteCondition = this.PROPERTY_TABLE_ALIAS + ".[" + "IsDeleted" + "]";
/*  690 */     return whereClause.indexOf(isDeleteCondition) != -1;
/*      */   }
/*      */   
/*      */   private String getFromClause(P8SearchDefinition.SearchClause searchClause)
/*      */   {
/*  695 */     StringBuilder clause = new StringBuilder();
/*  696 */     List<P8SearchDefinition.Subclass> subclasses = searchClause.getSubclasses();
/*      */     
/*  698 */     if ((subclasses != null) && (subclasses.size() > 0))
/*      */     {
/*  700 */       String subclassName = ((P8SearchDefinition.Subclass)subclasses.get(0)).getName();
/*  701 */       boolean isSubClassIncluded = ((P8SearchDefinition.Subclass)subclasses.get(0)).isSearchSubclasses();
/*  702 */       if (searchClause.getFrom() == SearchTemplateBase.ObjectType.document)
/*      */       {
/*  704 */         clause.append("(RMFolder ");
/*  705 */         clause.append("f");
/*  706 */         clause.append(" inner join ReferentialContainmentRelationship r on ");
/*  707 */         clause.append("f");
/*  708 */         clause.append(".This=r.Tail) inner join ");
/*  709 */         clause.append(isSubClassIncluded ? subclassName : "DOCUMENT");
/*  710 */         clause.append(" ").append(this.PROPERTY_TABLE_ALIAS);
/*  711 */         clause.append(" on ");
/*  712 */         clause.append(this.PROPERTY_TABLE_ALIAS);
/*  713 */         clause.append(".This=r.Head ");
/*      */       }
/*  715 */       else if (searchClause.getFrom() == SearchTemplateBase.ObjectType.folder) {
/*  716 */         clause.append("[");
/*  717 */         clause.append(isSubClassIncluded ? subclassName : "FOLDER");
/*  718 */         clause.append("] ");
/*  719 */         clause.append(this.PROPERTY_TABLE_ALIAS);
/*      */       }
/*      */     }
/*  722 */     else if ((subclasses != null) && (subclasses.size() > 1))
/*      */     {
/*      */ 
/*  725 */       for (P8SearchDefinition.Subclass sc : subclasses)
/*  726 */         sc.setUnselected(true);
/*      */     }
/*  728 */     return clause.toString();
/*      */   }
/*      */   
/*      */   private String createContentClause(String contentClause, boolean cascade, String scope) {
/*  732 */     String dialect = cascade ? "Lucene" : "K2VQL";
/*  733 */     if ((scope == null) || (scope.length() < 1))
/*  734 */       scope = "*";
/*  735 */     return " Contains(" + scope + ", '" + contentClause + "', '" + dialect + "')";
/*      */   }
/*      */   
/*      */   private String getSelectClause(List<P8SearchDefinition.SelectProperty> selectProps, List<String> additionalSelectProps, String sortProperty, P8SearchDefinition.SearchClause searchClauseSummaryTable) {
/*  739 */     StringBuilder buffer = new StringBuilder();
/*      */     
/*  741 */     if (selectProps != null) {
/*  742 */       for (P8SearchDefinition.SelectProperty selectProp : selectProps) {
/*  743 */         String name = selectProp.getSymbolicName();
/*  744 */         if (buffer.length() > 0)
/*  745 */           buffer.append(", ");
/*  746 */         buffer.append(this.PROPERTY_TABLE_ALIAS + ".[").append(name).append("]");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  751 */     if (additionalSelectProps != null) {
/*  752 */       for (String name : additionalSelectProps)
/*      */       {
/*      */ 
/*  755 */         boolean bFound = false;
/*  756 */         if (selectProps != null) {
/*  757 */           for (P8SearchDefinition.SelectProperty prop : selectProps) {
/*  758 */             if (name.equals(prop.getSymbolicName())) {
/*  759 */               bFound = true;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  764 */         if (!bFound)
/*      */         {
/*  766 */           if (buffer.length() > 0)
/*  767 */             buffer.append(", ");
/*  768 */           buffer.append(this.PROPERTY_TABLE_ALIAS + ".[").append(name).append("]");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  774 */     if (searchClauseSummaryTable != null) {
/*  775 */       List<P8SearchDefinition.SelectProperty> selectSummaryProps = searchClauseSummaryTable.getSelectProperties();
/*  776 */       if (selectSummaryProps != null) {
/*  777 */         for (P8SearchDefinition.SelectProperty selectProp : selectSummaryProps) {
/*  778 */           String name = selectProp.getSymbolicName();
/*  779 */           if (buffer.length() > 0)
/*  780 */             buffer.append(", ");
/*  781 */           buffer.append("s.[").append(name).append("]");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  786 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private String getOrderByClause(String sortProperty, boolean descending) {
/*  790 */     if ((sortProperty == null) || (sortProperty.length() == 0)) {
/*  791 */       return null;
/*      */     }
/*  793 */     if (sortProperty.equals("!MimeTypeIcon")) {
/*  794 */       sortProperty = "MimeType";
/*  795 */     } else if (sortProperty.equals("!Name")) {
/*  796 */       sortProperty = P8Util.getNameProperty(this.request, false);
/*      */     }
/*  798 */     String orderBy = (sortProperty.equalsIgnoreCase("Rank") ? "c" : this.PROPERTY_TABLE_ALIAS) + ".[" + sortProperty + (descending ? "] DESC" : "]");
/*      */     
/*  800 */     return orderBy;
/*      */   }
/*      */   
/*      */   private String getOrderByClause(List<P8SearchDefinition.SelectProperty> selectProps) {
/*  804 */     if ((selectProps == null) || (selectProps.size() < 1))
/*  805 */       return null;
/*  806 */     Map<String, P8SearchDefinition.SelectProperty> sortProps = getSortedProperties(selectProps);
/*  807 */     if ((sortProps == null) || (sortProps.size() < 1)) {
/*  808 */       return null;
/*      */     }
/*  810 */     StringBuilder buffer = new StringBuilder();
/*  811 */     for (P8SearchDefinition.SelectProperty selectProp : sortProps.values()) {
/*  812 */       String name = selectProp.getSymbolicName();
/*  813 */       P8SearchDefinition.SortOrder so = selectProp.getSortOrder();
/*  814 */       if (so != P8SearchDefinition.SortOrder.none) {
/*  815 */         if (buffer.length() > 0)
/*  816 */           buffer.append(", ");
/*  817 */         buffer.append(this.PROPERTY_TABLE_ALIAS + ".[").append(name).append("] ");
/*  818 */         if (so == P8SearchDefinition.SortOrder.ascending) {
/*  819 */           buffer.append("ASC");
/*  820 */         } else if (so == P8SearchDefinition.SortOrder.descending) {
/*  821 */           buffer.append("DESC");
/*      */         }
/*      */       }
/*      */     }
/*  825 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private Map<String, P8SearchDefinition.SelectProperty> getSortedProperties(List<P8SearchDefinition.SelectProperty> selectProps) {
/*  829 */     if ((selectProps == null) || (selectProps.size() < 1)) {
/*  830 */       return null;
/*      */     }
/*      */     
/*  833 */     Map<String, P8SearchDefinition.SelectProperty> sortProps = new TreeMap();
/*  834 */     for (P8SearchDefinition.SelectProperty selectProp : selectProps) {
/*  835 */       int sl = selectProp.getSortLevel();
/*  836 */       if (sl > 0) {
/*  837 */         String sortLevel = String.valueOf(sl);
/*  838 */         sortProps.put(sortLevel, selectProp);
/*      */       }
/*      */     }
/*      */     
/*  842 */     return sortProps;
/*      */   }
/*      */   
/*      */   private String getFolderClause(List<P8SearchDefinition.SearchInFolder> folders, SearchTemplateBase.ObjectType ot) {
/*  846 */     String folderId = null;
/*  847 */     boolean isSearchSubfolders = true;
/*  848 */     if ((folders == null) || (folders.size() < 1)) {
/*  849 */       folderId = "/Records Management";
/*      */     }
/*      */     
/*  852 */     for (P8SearchDefinition.SearchInFolder folder : folders) {
/*  853 */       folderId = folder.getId();
/*  854 */       isSearchSubfolders = folder.isSearchSubfolders();
/*      */     }
/*      */     
/*  857 */     StringBuilder buffer = new StringBuilder();
/*      */     
/*  859 */     if (isSearchSubfolders) {
/*  860 */       if (ot == SearchTemplateBase.ObjectType.document)
/*      */       {
/*  862 */         buffer.append(" ((").append("f").append(".this insubfolder '").append(folderId);
/*  863 */         buffer.append("') or (f.this=Object('").append(folderId).append("'))) ");
/*      */       }
/*      */       else
/*      */       {
/*  867 */         buffer.append(" (").append(this.PROPERTY_TABLE_ALIAS).append(".This insubfolder '").append(folderId).append("') ");
/*      */       }
/*      */     }
/*      */     else {
/*  871 */       buffer.append(" (").append(this.PROPERTY_TABLE_ALIAS).append(".This infolder '").append(folderId).append("') ");
/*      */     }
/*  873 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private String getWhereClause(P8SearchDefinition.Clause whereClause, P8SearchDefinition.Clause commonWhereClause) throws ParseException, IOException {
/*  877 */     if (whereClause == null) {
/*  878 */       return "";
/*      */     }
/*  880 */     StringBuilder buffer = new StringBuilder();
/*  881 */     if (whereClause.isContainer()) {
/*  882 */       if (whereClause == P8SearchDefinition.COMMON_CLAUSE) {
/*  883 */         buffer.append(getWhereClause(commonWhereClause, null));
/*      */       } else {
/*  885 */         P8SearchDefinition.Container wcc = (P8SearchDefinition.Container)whereClause;
/*  886 */         String join = wcc.getJoin().toString();
/*  887 */         List<P8SearchDefinition.Clause> whereClauses = wcc.getClauses();
/*  888 */         boolean wrap = false;
/*  889 */         for (P8SearchDefinition.Clause wc : whereClauses)
/*      */         {
/*  891 */           String c = getWhereClause(wc, commonWhereClause);
/*  892 */           if ((c != null) && (c.length() != 0))
/*      */           {
/*      */ 
/*  895 */             if (buffer.length() > 0) {
/*  896 */               buffer.append(" ").append(join).append(" ");
/*  897 */               wrap = true;
/*      */             }
/*  899 */             buffer.append(c);
/*      */           }
/*      */         }
/*  902 */         if (wrap) {
/*  903 */           buffer.insert(0, "(");
/*  904 */           buffer.append(")");
/*      */         }
/*      */       }
/*      */     } else {
/*  908 */       P8SearchDefinition.WhereClauseCondition wcc = (P8SearchDefinition.WhereClauseCondition)whereClause;
/*  909 */       buffer.append(getWhereClauseCondition(wcc));
/*      */     }
/*      */     
/*  912 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private String getWhereClauseCondition(P8SearchDefinition.WhereClauseCondition whereClauseCondition) throws ParseException, IOException {
/*  916 */     String value = whereClauseCondition.getLiteral();
/*  917 */     if (value == null) {
/*  918 */       value = "";
/*      */     }
/*  920 */     P8SearchDefinition.Operator operator = whereClauseCondition.getOperator();
/*  921 */     P8SearchDefinition.DataType dataType = whereClauseCondition.getPropDataType();
/*  922 */     if ((operator != P8SearchDefinition.Operator.isnull) && (operator != P8SearchDefinition.Operator.isnotnull)) {
/*  923 */       if (value.length() == 0) {
/*  924 */         return "";
/*      */       }
/*  926 */       if (operator == P8SearchDefinition.Operator.contains) {
/*  927 */         SearchTemplate.TextSearchCriterion textCriterion = new SearchTemplate.TextSearchCriterion(value, P8SearchDefinition.GroupAction.none.toString(), 0);
/*  928 */         P8SearchDefinition.SearchClause searchClause = whereClauseCondition.getSearchClause();
/*  929 */         boolean cascade = searchClause.getSearchDefinition().isCascade();
/*  930 */         P8SearchDefinition.Clause contentClause = P8TextSearchUtil.createContentClause(textCriterion, cascade, searchClause, true);
/*  931 */         String contentQuery = createCascadeQuery(contentClause, P8SearchTemplate.EnumTextSearchOption.property);
/*  932 */         return createContentClause(contentQuery, cascade, whereClauseCondition.getPropSymbolicName());
/*      */       }
/*      */       
/*      */ 
/*  936 */       if (dataType == P8SearchDefinition.DataType.typestring) {
/*  937 */         value = "'" + value.replaceAll("'", "''") + "'";
/*  938 */       } else if ((dataType == P8SearchDefinition.DataType.typeobject) && (!value.startsWith("{"))) {
/*  939 */         value = "'" + value + "'";
/*      */       }
/*      */     }
/*  942 */     int databaseType = whereClauseCondition.getDatabaseType();
/*  943 */     if ((databaseType == 1) && ((operator == P8SearchDefinition.Operator.like) || (operator == P8SearchDefinition.Operator.notlike))) {
/*  944 */       value = value.replaceAll("\\[", "[[]");
/*      */     }
/*  946 */     String condition = "";
/*  947 */     String name = this.PROPERTY_TABLE_ALIAS + ".[" + whereClauseCondition.getPropSymbolicName() + "]";
/*  948 */     if ((dataType == P8SearchDefinition.DataType.typedate) && (value.length() > 0)) {
/*  949 */       condition = getDateCondition(name, value, operator);
/*  950 */       if (condition.isEmpty()) {
/*  951 */         Calendar cal = WCDateFormat.parseW3CDate(value, null);
/*  952 */         value = WCDateFormat.getDateString(null, cal.getTimeInMillis(), WCDateFormat.UTC_DATE_FORMAT);
/*      */       }
/*      */     }
/*      */     
/*  956 */     if (condition.length() < 1) {
/*  957 */       switch (operator) {
/*      */       case gt: 
/*  959 */         condition = condition + name + " > " + value;
/*  960 */         break;
/*      */       case gte: 
/*  962 */         condition = condition + name + " >= " + value;
/*  963 */         break;
/*      */       case lt: 
/*  965 */         condition = condition + name + " < " + value;
/*  966 */         break;
/*      */       case lte: 
/*  968 */         condition = condition + name + " <= " + value;
/*  969 */         break;
/*      */       case eq: 
/*  971 */         if (dataType == P8SearchDefinition.DataType.typeobject) {
/*  972 */           condition = condition + name + " = Object(" + value + ")";
/*      */         } else
/*  974 */           condition = condition + name + " = " + value;
/*  975 */         break;
/*      */       case neq: 
/*  977 */         if (dataType == P8SearchDefinition.DataType.typeobject) {
/*  978 */           condition = condition + "NOT (" + name + " = Object(" + value + "))";
/*      */         } else
/*  980 */           condition = condition + name + " <> " + value;
/*  981 */         break;
/*      */       case like: 
/*  983 */         condition = condition + name + " LIKE " + value;
/*  984 */         break;
/*      */       case notlike: 
/*  986 */         condition = condition + "NOT (" + name + " LIKE " + value + ")";
/*  987 */         break;
/*      */       case in: 
/*      */       case inany: 
/*  990 */         condition = condition + value + " IN " + name;
/*  991 */         break;
/*      */       case notin: 
/*  993 */         condition = condition + "NOT (" + value + " IN " + name + ")";
/*  994 */         break;
/*      */       case isnull: 
/*  996 */         condition = condition + name + " IS NULL";
/*  997 */         break;
/*      */       case isnotnull: 
/*  999 */         condition = condition + name + " IS NOT NULL";
/* 1000 */         break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 1005 */     return condition;
/*      */   }
/*      */   
/*      */   private String getDateCondition(String propName, String date, P8SearchDefinition.Operator operator) throws ParseException {
/* 1009 */     Calendar cal = WCDateFormat.parseW3CDate(date, null);
/* 1010 */     String[] dates = WCDateFormat.getDateRangeStrings(null, cal.getTimeInMillis(), WCDateFormat.UTC_DATE_FORMAT);
/*      */     
/* 1012 */     String currentDay = dates[0];
/* 1013 */     String nextDay = dates[1];
/*      */     
/* 1015 */     return getDateCondition(propName, currentDay, nextDay, operator);
/*      */   }
/*      */   
/*      */   private String getDateCondition(String propName, String beginDate, String endDate, P8SearchDefinition.Operator operator) throws ParseException {
/* 1019 */     String condition = "";
/* 1020 */     switch (operator) {
/*      */     case eq: 
/* 1022 */       condition = condition + "(" + propName + " >= " + beginDate + " AND ";
/* 1023 */       condition = condition + propName + " < " + endDate + ")";
/* 1024 */       break;
/*      */     case lte: 
/* 1026 */       condition = condition + propName + " < " + endDate;
/* 1027 */       break;
/*      */     case neq: 
/* 1029 */       condition = condition + "(" + propName + " < " + beginDate + " OR ";
/* 1030 */       condition = condition + propName + " >= " + endDate + ")";
/* 1031 */       break;
/*      */     case gt: 
/* 1033 */       condition = condition + propName + " >= " + endDate;
/* 1034 */       break;
/*      */     case lt: 
/* 1036 */       condition = condition + propName + " < " + beginDate;
/*      */     }
/*      */     
/*      */     
/* 1040 */     return condition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private String getAdditionalClause(P8SearchDefinition.SearchClause sc, boolean isDeletedPresent)
/*      */   {
/* 1206 */     String isClassClause = null;
/* 1207 */     SearchTemplateBase.ObjectType ot = sc.getFrom();
/* 1208 */     List<P8SearchDefinition.Subclass> subclasses = sc.getSubclasses();
/* 1209 */     if ((subclasses != null) && (subclasses.size() >= 1)) {
/* 1210 */       boolean bIncludedSubClass = ((P8SearchDefinition.Subclass)subclasses.get(0)).isSearchSubclasses();
/* 1211 */       if (!bIncludedSubClass) {
/* 1212 */         isClassClause = getIsClassClause((P8SearchDefinition.Subclass)subclasses.get(0));
/*      */       }
/*      */     }
/* 1215 */     StringBuilder clause = new StringBuilder();
/* 1216 */     if (ot == SearchTemplateBase.ObjectType.document)
/*      */     {
/* 1218 */       clause.append("((");
/* 1219 */       clause.append(this.PROPERTY_TABLE_ALIAS);
/*      */       
/* 1221 */       clause.append(".[VersionStatus] = 1)");
/*      */       
/* 1223 */       if (!isDeletedPresent) {
/* 1224 */         clause.append(" AND (");
/* 1225 */         clause.append(this.PROPERTY_TABLE_ALIAS);
/* 1226 */         clause.append(".[").append("IsDeleted").append("] = false)");
/*      */       }
/*      */       
/* 1229 */       if ((isClassClause != null) && (isClassClause.length() > 0))
/* 1230 */         clause.append(" AND (").append(isClassClause).append(")");
/* 1231 */       clause.append(") ");
/*      */     }
/* 1233 */     else if (ot == SearchTemplateBase.ObjectType.folder) {
/* 1234 */       clause.append("((");
/* 1235 */       clause.append(this.PROPERTY_TABLE_ALIAS);
/* 1236 */       clause.append(".[IsHiddenContainer] = false)");
/*      */       
/* 1238 */       if (!isDeletedPresent) {
/* 1239 */         clause.append(" AND (");
/* 1240 */         clause.append(this.PROPERTY_TABLE_ALIAS);
/* 1241 */         clause.append(".[").append("IsDeleted").append("] = false)");
/*      */       }
/*      */       
/*      */ 
/* 1245 */       if ((isClassClause != null) && (isClassClause.length() > 0))
/* 1246 */         clause.append(" AND (").append(isClassClause).append(") ");
/* 1247 */       clause.append(") ");
/*      */     }
/*      */     
/* 1250 */     return clause.toString();
/*      */   }
/*      */   
/*      */   private String getIsClassClause(P8SearchDefinition.Subclass sc)
/*      */   {
/* 1255 */     StringBuilder clause = new StringBuilder();
/* 1256 */     clause.append("IsClass(").append(this.PROPERTY_TABLE_ALIAS).append(", [").append(sc.getName()).append("])");
/* 1257 */     return clause.toString();
/*      */   }
/*      */   
/*      */   private String getContentClause(P8SearchDefinition.ContentCriteria contentCriteria) throws IOException {
/* 1261 */     if (contentCriteria == null) {
/* 1262 */       return null;
/*      */     }
/* 1264 */     if (contentCriteria.isCascade()) {
/* 1265 */       return createCascadeQuery(contentCriteria.getContentClause());
/*      */     }
/* 1267 */     return createVerityQuery(contentCriteria.getContentClause(), contentCriteria.isShowRank());
/*      */   }
/*      */   
/*      */   private String getContentClauseOnIdxProperties(P8SearchDefinition.ContentCriteria contentCriteria) throws IOException {
/* 1271 */     if ((contentCriteria == null) || (!contentCriteria.isCascade())) {
/* 1272 */       return null;
/*      */     }
/*      */     
/* 1275 */     return createCascadeQuery(contentCriteria.getContentClause(), P8SearchTemplate.EnumTextSearchOption.property);
/*      */   }
/*      */   
/*      */   public void convertToP8SearchDefinition()
/*      */     throws Exception
/*      */   {
/* 1281 */     if (this.searchDefinition == null) {
/* 1282 */       this.searchDefinition = new P8SearchDefinition();
/*      */     }
/* 1284 */     SearchTemplateBase.ObjectType objectType = this.searchTemplate.getObjectType();
/* 1285 */     P8SearchDefinition.SearchClause searchClause = this.searchDefinition.getSearchClause(objectType);
/* 1286 */     if (searchClause == null) {
/* 1287 */       searchClause = new P8SearchDefinition.SearchClause(objectType.toString());
/* 1288 */       this.searchDefinition.addSearchClause(searchClause);
/*      */     }
/*      */     
/* 1291 */     convertP8SearchTemplateSelectProperties();
/* 1292 */     convertP8SearchTemplateFolders();
/* 1293 */     convertP8SearchTemplateClass();
/*      */     
/* 1295 */     boolean wasIcnMatchAll = P8SearchUtil.isIcnMatchAllSearchClause(searchClause);
/* 1296 */     Set<String> propItemIds = searchClause.retrieveWhereClauseConditions().keySet();
/* 1297 */     Set<String> textItemIds = searchClause.retrieveContentClauseItems().keySet();
/* 1298 */     convertP8SearchTemplateSearchCriteria();
/* 1299 */     convertP8SearchTemplateTextSearchCriteria();
/* 1300 */     this.searchDefinition.setTextSearchType(this.searchTemplate.getTextSearchType());
/* 1301 */     checkIfSearchClauseIsApiCompatible(searchClause, wasIcnMatchAll, propItemIds, textItemIds);
/*      */     
/* 1303 */     SearchTemplateBase.VersionOption versionOption = this.searchTemplate.getVersionOption();
/* 1304 */     if (versionOption != null)
/* 1305 */       this.searchDefinition.setVersionSelection(versionOption.toString());
/* 1306 */     this.searchDefinition.setDatabaseType(this.connection.getObjectStore().get_DatabaseType().getValue());
/*      */   }
/*      */   
/*      */   private void checkIfSearchClauseIsApiCompatible(P8SearchDefinition.SearchClause searchClause, boolean wasIcnMatchAll, Set<String> propItemIds, Set<String> textItemIds)
/*      */   {
/* 1311 */     P8SearchDefinition sd = searchClause.getSearchDefinition();
/* 1312 */     if (sd.isApiIncompatible()) {
/* 1313 */       return;
/*      */     }
/* 1315 */     boolean incompatible = (this.searchDefinition.isIcnGenerated()) && (P8SearchUtil.isIcnMatchAllSearchClause(searchClause) != wasIcnMatchAll);
/* 1316 */     if (!incompatible)
/* 1317 */       incompatible = !isSearchClauseCompatible(searchClause.retrieveWhereClauseConditions().values(), propItemIds);
/* 1318 */     if (!incompatible) {
/* 1319 */       incompatible = !isSearchClauseCompatible(searchClause.retrieveContentClauseItems().values(), textItemIds);
/*      */     }
/* 1321 */     sd.setApiIncompatible(incompatible);
/*      */   }
/*      */   
/*      */   private boolean isSearchClauseCompatible(Collection<P8SearchDefinition.ClauseItem> conditions, Set<String> itemIds) {
/* 1325 */     boolean compatible = true;
/* 1326 */     if (conditions.size() != itemIds.size()) {
/* 1327 */       compatible = false;
/*      */     } else {
/* 1329 */       for (P8SearchDefinition.ClauseItem item : conditions) {
/* 1330 */         String itemId = item.getItemId();
/* 1331 */         if ((itemId == null) || (itemId.isEmpty()) || (!itemIds.contains(itemId))) {
/* 1332 */           compatible = false;
/* 1333 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1338 */     return compatible;
/*      */   }
/*      */   
/*      */   private boolean isAnyValueSet(String[] values) {
/* 1342 */     if (values == null) {
/* 1343 */       return false;
/*      */     }
/* 1345 */     boolean set = false;
/* 1346 */     for (int i = 0; i < values.length; i++) {
/* 1347 */       if ((values[i] != null) && (!values[i].isEmpty())) {
/* 1348 */         set = true;
/* 1349 */         break;
/*      */       }
/*      */     }
/*      */     
/* 1353 */     return set;
/*      */   }
/*      */   
/*      */   private void convertP8SearchTemplateSelectProperties() {
/* 1357 */     SearchTemplateBase.ObjectType objectType = this.searchTemplate.getObjectType();
/* 1358 */     P8SearchDefinition.SearchClause searchClause = this.searchDefinition.getSearchClause(objectType);
/* 1359 */     P8SearchDefinition.SearchClause searchSummaryClause = this.searchDefinition.getSearchClause(SearchTemplateBase.ObjectType.summaryTable);
/* 1360 */     if (searchSummaryClause == null) {
/* 1361 */       searchSummaryClause = new P8SearchDefinition.SearchClause(SearchTemplateBase.ObjectType.summaryTable.toString());
/* 1362 */       this.searchDefinition.addSearchClause(searchSummaryClause);
/*      */     }
/*      */     
/* 1365 */     SearchTemplateBase.ResultsDisplay display = this.searchTemplate.getResultsDisplay();
/* 1366 */     String[] columns = display != null ? display.getColumns() : null;
/* 1367 */     if ((columns != null) && (columns.length > 0)) {
/* 1368 */       String sortBy = display.getSortByProperty();
/* 1369 */       boolean asc = display.getSortAscending();
/* 1370 */       List<P8SearchDefinition.SelectProperty> selectProps = new ArrayList();
/* 1371 */       for (int i = 0; i < columns.length; i++) {
/* 1372 */         if ((!columns[i].equals("{CLASS}")) && (!P8Util.isSummaryTableProperty(columns[i]))) {
/* 1373 */           P8SearchDefinition.SelectProperty prop = applySelectProperty(searchClause, columns[i], columns[i].equals(sortBy) ? 1 : 0, asc ? P8SearchDefinition.SortOrder.ascending : P8SearchDefinition.SortOrder.descending, null, objectType);
/* 1374 */           selectProps.add(prop);
/*      */         }
/*      */       }
/*      */       
/* 1378 */       String[] magazineColumns = display != null ? display.getMagazineColumns() : null;
/* 1379 */       if ((magazineColumns != null) && (magazineColumns.length > 0)) {
/* 1380 */         List<P8SearchDefinition.SelectProperty> summaryProps = new ArrayList();
/* 1381 */         for (int i = 0; i < magazineColumns.length; i++) {
/* 1382 */           if (P8Util.isSummaryTableProperty(magazineColumns[i])) {
/* 1383 */             P8SearchDefinition.SelectProperty prop = applySelectProperty(searchClause, magazineColumns[i], 0, P8SearchDefinition.SortOrder.descending, null, SearchTemplateBase.ObjectType.summaryTable);
/* 1384 */             summaryProps.add(prop);
/* 1385 */           } else if (!magazineColumns[i].equals("{CLASS}")) {
/* 1386 */             boolean isInColumns = false;
/* 1387 */             for (int j = 0; j < columns.length; j++) {
/* 1388 */               if (magazineColumns[i].equals(columns[j])) {
/* 1389 */                 isInColumns = true;
/* 1390 */                 break;
/*      */               }
/*      */             }
/* 1393 */             if (!isInColumns) {
/* 1394 */               P8SearchDefinition.SelectProperty prop = applySelectProperty(searchClause, magazineColumns[i], magazineColumns[i].equals(sortBy) ? 1 : 0, asc ? P8SearchDefinition.SortOrder.ascending : P8SearchDefinition.SortOrder.descending, null, objectType);
/* 1395 */               selectProps.add(prop);
/*      */             }
/*      */           }
/*      */         }
/* 1399 */         searchSummaryClause.setSelectProperties(summaryProps);
/*      */       }
/* 1401 */       searchClause.setSelectProperties(selectProps);
/*      */     } else {
/* 1403 */       String namePropName = objectType == SearchTemplateBase.ObjectType.document ? "DocumentTitle" : "FolderName";
/* 1404 */       if (this.searchDefinition.isIcnGenerated()) {
/* 1405 */         List<P8SearchDefinition.SelectProperty> selectProps = new ArrayList();
/* 1406 */         selectProps.add(applySelectProperty(searchClause, namePropName, 1, P8SearchDefinition.SortOrder.ascending, null, objectType));
/* 1407 */         List<SearchCriteria> searchCriteria = this.searchTemplate.getSearchCriteria();
/* 1408 */         for (SearchCriteria criteriaEntry : searchCriteria) {
/* 1409 */           SearchCriterion criterion = (SearchCriterion)criteriaEntry;
/* 1410 */           if (!criterion.getName().equalsIgnoreCase(namePropName))
/*      */           {
/* 1412 */             selectProps.add(applySelectProperty(searchClause, criterion.getName(), 0, null, criterion.getDisplayName(), objectType)); }
/*      */         }
/* 1414 */         searchClause.setSelectProperties(selectProps);
/*      */       } else {
/* 1416 */         List<P8SearchDefinition.SelectProperty> selectProps = searchClause.getSelectProperties();
/* 1417 */         P8SearchDefinition.SelectProperty nameProp = searchClause.getSelectProperty(namePropName);
/* 1418 */         if (nameProp == null) {
/* 1419 */           nameProp = new P8SearchDefinition.SelectProperty(namePropName, null, objectType, 0, null, null);
/* 1420 */           Map<String, P8SearchDefinition.SelectProperty> sortProps = getSortedProperties(selectProps);
/* 1421 */           if ((sortProps == null) || (sortProps.size() < 1)) {
/* 1422 */             nameProp.setSortLevel(1);
/* 1423 */             nameProp.setSortOrder(P8SearchDefinition.SortOrder.ascending);
/*      */           }
/* 1425 */           selectProps.add(nameProp);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private P8SearchDefinition.SelectProperty applySelectProperty(P8SearchDefinition.SearchClause searchClause, String propSymbolicName, int sortLevel, P8SearchDefinition.SortOrder sortOrder, String propName, SearchTemplateBase.ObjectType objectType) {
/* 1432 */     P8SearchDefinition.SelectProperty prop = searchClause.getSelectProperty(propSymbolicName);
/* 1433 */     if (prop == null) {
/* 1434 */       prop = new P8SearchDefinition.SelectProperty(propSymbolicName, propName, objectType, sortLevel, sortOrder, null);
/* 1435 */       prop.setItemId(UUID.randomUUID().toString());
/*      */     } else {
/* 1437 */       prop.setSortLevel(sortLevel);
/* 1438 */       prop.setSortOrder(sortOrder);
/*      */     }
/*      */     
/* 1441 */     return prop;
/*      */   }
/*      */   
/*      */   private void convertP8SearchTemplateFolders() {
/* 1445 */     List<SearchTemplate.SearchFolder> folders = this.searchTemplate.getFolders();
/* 1446 */     if (folders == null) {
/* 1447 */       return;
/*      */     }
/* 1449 */     List<P8SearchDefinition.SearchInFolder> searchInFolders = new ArrayList();
/* 1450 */     for (SearchTemplate.SearchFolder folder : folders) {
/* 1451 */       P8SearchDefinition.SearchInFolder searchInFolder = new P8SearchDefinition.SearchInFolder(folder.getId(), folder.getPathName(), folder.isSearchSubfolders(), folder.getView(), folder.getObjectStoreId());
/* 1452 */       searchInFolder.setItemId(folder.getItemId());
/* 1453 */       searchInFolders.add(searchInFolder);
/*      */     }
/* 1455 */     this.searchDefinition.setFolders(searchInFolders);
/*      */   }
/*      */   
/*      */   private void convertP8SearchTemplateClass() {
/* 1459 */     SearchTemplateBase.ObjectType objectType = this.searchTemplate.getObjectType();
/* 1460 */     P8SearchDefinition.SearchClause searchClause = this.searchDefinition.getSearchClause(objectType);
/* 1461 */     List<P8SearchDefinition.Subclass> subclasses = searchClause.getSubclasses();
/* 1462 */     for (P8SearchDefinition.Subclass sc : subclasses)
/* 1463 */       sc.setUnselected(true);
/* 1464 */     List<SearchTemplate.SearchClass> classes = this.searchTemplate.getClasses();
/*      */     
/*      */ 
/* 1467 */     if (P8SearchUtil.isSearchingAllClasses(classes)) {
/* 1468 */       return;
/*      */     }
/* 1470 */     for (SearchTemplate.SearchClass searchClass : classes) {
/* 1471 */       applySubclass(searchClass, searchClause);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void applySubclass(SearchTemplate.SearchClass searchClass, P8SearchDefinition.SearchClause searchClause)
/*      */   {
/* 1493 */     String itemId = searchClass.getItemId();
/* 1494 */     if ((itemId == null) || (itemId.isEmpty())) {
/* 1495 */       itemId = UUID.randomUUID().toString();
/*      */     }
/* 1497 */     P8SearchDefinition.Subclass subclass = searchClause.getSubclass(itemId);
/* 1498 */     if (subclass == null) {
/* 1499 */       subclass = new P8SearchDefinition.Subclass(searchClass.getName(), searchClass.getDisplayName(), searchClass.getObjectType(), searchClass.getEditProperty(), searchClass.getItemId(), searchClass.isSearchSubclasses());
/* 1500 */       searchClause.addSubclass(subclass);
/*      */     } else {
/* 1502 */       subclass.setSearchSubclasses(searchClass.isSearchSubclasses());
/* 1503 */       subclass.setUnselected(false);
/*      */     }
/*      */   }
/*      */   
/*      */   private void convertP8SearchTemplateSearchCriteria() throws Exception {
/* 1508 */     SearchTemplateBase.ObjectType objectType = this.searchTemplate.getObjectType();
/* 1509 */     P8SearchDefinition.SearchClause searchClause = this.searchDefinition.getSearchClause(objectType);
/* 1510 */     List<P8SearchDefinition.Clause> conditions = new ArrayList();
/* 1511 */     List<SearchCriterion> searchCriteria = this.searchTemplate.getPropertySearchCriteria();
/* 1512 */     for (SearchCriteria criteriaEntry : searchCriteria) {
/* 1513 */       SearchCriterion criterion = (SearchCriterion)criteriaEntry;
/* 1514 */       SearchTemplate.SearchEditProperty view = criterion.isRequired() ? SearchTemplate.SearchEditProperty.required : criterion.isReadOnly() ? SearchTemplate.SearchEditProperty.readonly : criterion.isHidden() ? SearchTemplate.SearchEditProperty.hidden : SearchTemplate.SearchEditProperty.editable;
/* 1515 */       String[] values = criterion.getValues();
/* 1516 */       boolean valueSet = isAnyValueSet(values);
/* 1517 */       String criterionOperator = criterion.getOperator();
/* 1518 */       P8SearchDefinition.Operator operator = P8SearchUtil.convertP8SearchTemplateOperator(criterionOperator);
/* 1519 */       P8SearchDefinition.DataType dataType = P8SearchUtil.convertP8SearchTemplateDataType(criterion.getDataType());
/*      */       
/* 1521 */       String itemId = criterion.getItemId();
/* 1522 */       String[] itemIds = itemId == null ? new String[] { "" } : criterion.getItemId().split(",");
/* 1523 */       if ((valueSet) && (isSmartOperator(operator)))
/*      */       {
/* 1525 */         String name = criterion.getName();
/* 1526 */         String displayName = criterion.getDisplayName();
/*      */         P8SearchDefinition.Container container;
/* 1528 */         P8SearchDefinition.Container container; if ((operator == P8SearchDefinition.Operator.between) || (operator == P8SearchDefinition.Operator.notbetween)) {
/* 1529 */           container = explodeBetweenCondition(searchClause, name, displayName, dataType, operator, values, view);
/*      */         } else
/* 1531 */           container = explodeIncludeCondition(searchClause, name, displayName, dataType, operator, values, view, criterion.getCardinality());
/* 1532 */         if (container.getClauses().size() > 0)
/* 1533 */           conditions.add(container);
/* 1534 */         if (!this.searchDefinition.isIcnGenerated())
/* 1535 */           applySmartOperatorConditions(itemIds[0], container, searchClause);
/*      */       } else {
/* 1537 */         String value = (values == null) || (values.length < 1) ? null : values[0];
/* 1538 */         if ((operator == P8SearchDefinition.Operator.like) || (operator == P8SearchDefinition.Operator.notlike) || (operator == P8SearchDefinition.Operator.startswith) || (operator == P8SearchDefinition.Operator.endswith)) {
/* 1539 */           value = P8SearchUtil.convertLikeValue(value, operator);
/* 1540 */           if (operator != P8SearchDefinition.Operator.notlike)
/* 1541 */             operator = P8SearchDefinition.Operator.like;
/*      */         }
/* 1543 */         for (int i = 0; i < itemIds.length; i++) {
/* 1544 */           P8SearchDefinition.WhereClauseCondition condition = applyWhereClauseCondition(searchClause, itemIds[i], operator, value, criterion.getName(), criterion.getDisplayName(), dataType, view, null);
/* 1545 */           value = null;
/* 1546 */           conditions.add(condition);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1551 */     if (this.searchDefinition.isIcnGenerated()) {
/* 1552 */       if (conditions.size() > 1) {
/* 1553 */         P8SearchDefinition.Container container = new P8SearchDefinition.Container(this.searchTemplate.isAndSearch() ? P8SearchDefinition.Operator.and : P8SearchDefinition.Operator.or);
/* 1554 */         container.setClauses(conditions);
/* 1555 */         container.setSearchClause(searchClause);
/* 1556 */         searchClause.setWhereClause(container);
/* 1557 */       } else if (conditions.size() == 1) {
/* 1558 */         searchClause.setWhereClause((P8SearchDefinition.Clause)conditions.get(0));
/*      */       } else {
/* 1560 */         searchClause.setWhereClause(null);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isSmartOperator(P8SearchDefinition.Operator operator) {
/* 1566 */     return (operator == P8SearchDefinition.Operator.between) || (operator == P8SearchDefinition.Operator.notbetween) || (operator == P8SearchDefinition.Operator.in) || (operator == P8SearchDefinition.Operator.notin) || (operator == P8SearchDefinition.Operator.inany);
/*      */   }
/*      */   
/*      */   private void applySmartOperatorConditions(String itemId, P8SearchDefinition.Container container, P8SearchDefinition.SearchClause searchClause) {
/* 1570 */     P8SearchDefinition.WhereClauseCondition condition = searchClause.getWhereClauseCondition(itemId);
/* 1571 */     P8SearchDefinition.Container originalContainer = condition.getContainer();
/* 1572 */     if (originalContainer == null) {
/* 1573 */       P8SearchDefinition.SearchClause clause = condition.getSearchClause();
/* 1574 */       clause.setWhereClause(container);
/* 1575 */     } else if ((originalContainer.isInclude()) || (originalContainer.isRange())) {
/* 1576 */       originalContainer.setJoin(container.getJoin());
/* 1577 */       originalContainer.setClauses(container.getClauses());
/*      */     } else {
/* 1579 */       originalContainer.replaceClause(condition, container);
/*      */     }
/*      */   }
/*      */   
/*      */   private P8SearchDefinition.WhereClauseCondition applyWhereClauseCondition(P8SearchDefinition.SearchClause searchClause, String itemId, P8SearchDefinition.Operator operator, String value, String propSymbolicName, String propName, P8SearchDefinition.DataType propDataType, SearchTemplate.SearchEditProperty editProperty, P8SearchDefinition.Container container) {
/* 1584 */     P8SearchDefinition.WhereClauseCondition condition = searchClause.getWhereClauseCondition(itemId);
/* 1585 */     if (condition == null) {
/* 1586 */       condition = new P8SearchDefinition.WhereClauseCondition(operator, propSymbolicName, propName, searchClause.getFrom(), propDataType, false, null, false, editProperty, value);
/* 1587 */       condition.setSearchClause(searchClause);
/* 1588 */       condition.setItemId(itemId);
/* 1589 */       if (container != null)
/* 1590 */         container.addClause(condition);
/*      */     } else {
/* 1592 */       condition.setOperator(operator);
/* 1593 */       condition.setLiteral(value);
/*      */     }
/*      */     
/* 1596 */     return condition;
/*      */   }
/*      */   
/*      */   private P8SearchDefinition.Container explodeIncludeCondition(P8SearchDefinition.SearchClause searchClause, String name, String displayName, P8SearchDefinition.DataType dataType, P8SearchDefinition.Operator operator, String[] values, SearchTemplate.SearchEditProperty view, String cardinality) throws Exception {
/* 1600 */     P8SearchDefinition.Container container = new P8SearchDefinition.Container(operator == P8SearchDefinition.Operator.inany ? P8SearchDefinition.Operator.or : P8SearchDefinition.Operator.and);
/* 1601 */     container.setSearchClause(searchClause);
/* 1602 */     if (cardinality.equals("SINGLE")) {
/* 1603 */       operator = operator == P8SearchDefinition.Operator.inany ? P8SearchDefinition.Operator.eq : P8SearchDefinition.Operator.neq;
/*      */     }
/* 1605 */     if (values != null) {
/* 1606 */       for (int i = 0; i < values.length; i++) {
/* 1607 */         String value = values[i];
/* 1608 */         applyWhereClauseCondition(searchClause, null, operator, value, name, displayName, dataType, view, container);
/*      */       }
/*      */     }
/* 1611 */     return container;
/*      */   }
/*      */   
/*      */   private P8SearchDefinition.Container explodeBetweenCondition(P8SearchDefinition.SearchClause searchClause, String name, String displayName, P8SearchDefinition.DataType dataType, P8SearchDefinition.Operator operator, String[] values, SearchTemplate.SearchEditProperty view) throws Exception {
/* 1615 */     boolean between = operator == P8SearchDefinition.Operator.between;
/* 1616 */     P8SearchDefinition.Container container = new P8SearchDefinition.Container(between ? P8SearchDefinition.Operator.and.toString() : P8SearchDefinition.Operator.or.toString());
/* 1617 */     if ((values != null) && (values.length > 0)) {
/* 1618 */       container.setSearchClause(searchClause);
/* 1619 */       if ((values[0] != null) && (values.length > 1) && (values[1] != null) && (values[0].equals(values[1]))) {
/* 1620 */         applyWhereClauseCondition(searchClause, null, between ? P8SearchDefinition.Operator.eq : P8SearchDefinition.Operator.neq, values[0], name, displayName, dataType, view, container);
/*      */       } else {
/* 1622 */         if ((values[0] != null) && (!values[0].isEmpty())) {
/* 1623 */           String value = values[0];
/* 1624 */           applyWhereClauseCondition(searchClause, null, between ? P8SearchDefinition.Operator.gte : P8SearchDefinition.Operator.lt, value, name, displayName, dataType, view, container);
/*      */         }
/* 1626 */         if ((values.length > 1) && (values[1] != null) && (!values[1].isEmpty())) {
/* 1627 */           String value = values[1];
/* 1628 */           applyWhereClauseCondition(searchClause, null, between ? P8SearchDefinition.Operator.lte : P8SearchDefinition.Operator.gt, value, name, displayName, dataType, view, container);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1633 */     return container;
/*      */   }
/*      */   
/*      */   private void convertP8SearchTemplateTextSearchCriteria() throws Exception {
/* 1637 */     P8SearchDefinition.SearchClause searchClause = this.searchDefinition.getSearchClause(this.searchTemplate.getObjectType());
/* 1638 */     List<SearchTemplate.TextSearchCriterion> textSearchCriteria = this.searchTemplate.getTextSearchCriteria();
/*      */     
/* 1640 */     String sortProperty = null;
/* 1641 */     if (this.searchTemplate.getResultsDisplay() != null) {
/* 1642 */       sortProperty = this.searchTemplate.getResultsDisplay().getSortByProperty();
/*      */     }
/* 1644 */     boolean sortByRank = (sortProperty != null) && (sortProperty.equalsIgnoreCase("Rank"));
/* 1645 */     P8SearchDefinition.ContentCriteria definedCriteria; if (this.searchDefinition.isIcnGenerated()) {
/* 1646 */       List<SearchCriterion> propTextCriteria = this.searchTemplate.getPropertyTextSearchCriteria();
/* 1647 */       if (((textSearchCriteria == null) || (textSearchCriteria.size() < 1)) && ((propTextCriteria == null) || (propTextCriteria.size() < 1))) {
/* 1648 */         return;
/*      */       }
/* 1650 */       boolean cascade = this.searchTemplate.isCascade();
/* 1651 */       P8SearchDefinition.Clause contentClause = null;
/* 1652 */       P8SearchDefinition.Clause propContentClause = null;
/* 1653 */       if ((textSearchCriteria != null) && (textSearchCriteria.size() > 0)) {
/* 1654 */         SearchTemplate.TextSearchCriterion criterion = (SearchTemplate.TextSearchCriterion)textSearchCriteria.get(0);
/* 1655 */         contentClause = P8TextSearchUtil.createContentClause(criterion, cascade, searchClause, true);
/*      */       }
/* 1657 */       if ((propTextCriteria != null) && (propTextCriteria.size() > 0)) {
/* 1658 */         P8SearchDefinition.Operator op = this.searchTemplate.isAndSearch() ? P8SearchDefinition.Operator.and : P8SearchDefinition.Operator.or;
/* 1659 */         propContentClause = P8TextSearchUtil.createContentClause(propTextCriteria, cascade, searchClause, true, op);
/*      */       }
/* 1661 */       if ((contentClause != null) || (propContentClause != null)) {
/*      */         P8SearchDefinition.Clause clause;
/* 1663 */         if ((contentClause != null) && (propContentClause != null)) {
/* 1664 */           P8SearchDefinition.Clause clause = new P8SearchDefinition.Container(P8SearchDefinition.Operator.and);
/* 1665 */           clause.setSearchClause(searchClause);
/* 1666 */           ((P8SearchDefinition.Container)clause).addClause(contentClause);
/* 1667 */           ((P8SearchDefinition.Container)clause).addClause(propContentClause); } else { P8SearchDefinition.Clause clause;
/* 1668 */           if (contentClause != null) {
/* 1669 */             clause = contentClause;
/*      */           } else
/* 1671 */             clause = propContentClause;
/*      */         }
/* 1673 */         P8SearchDefinition.ContentCriteria contentCriteria = new P8SearchDefinition.ContentCriteria();
/* 1674 */         contentCriteria.setContentClause(clause);
/* 1675 */         contentCriteria.setJoin(P8SearchDefinition.JoinType.inner);
/* 1676 */         contentCriteria.setShowRank(sortByRank);
/* 1677 */         contentCriteria.setTextSearchType(cascade ? SearchTemplate.TextSearchType.cascade : SearchTemplate.TextSearchType.verity);
/* 1678 */         contentCriteria.setSearchClause(searchClause);
/* 1679 */         searchClause.setContentCriteria(contentCriteria);
/*      */       }
/*      */     } else {
/* 1682 */       if ((textSearchCriteria == null) || (textSearchCriteria.size() < 1)) {
/* 1683 */         return;
/*      */       }
/* 1685 */       definedCriteria = searchClause.getContentCriteria();
/* 1686 */       definedCriteria.setShowRank(sortByRank);
/* 1687 */       for (SearchTemplate.TextSearchCriterion criterion : textSearchCriteria) {
/* 1688 */         String itemId = criterion.getItemId();
/* 1689 */         P8SearchDefinition.ContentClauseItem oldClause = searchClause.getContentClauseItem(itemId);
/* 1690 */         P8SearchDefinition.Clause newClause = P8TextSearchUtil.createContentClause(criterion, definedCriteria.isCascade(), searchClause, true);
/* 1691 */         P8SearchDefinition.Container container = oldClause.getContainer();
/* 1692 */         if (container == null) {
/* 1693 */           definedCriteria.setContentClause(newClause);
/*      */         } else
/* 1695 */           container.replaceClause(oldClause, newClause);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private String createCascadeQuery(P8SearchDefinition.Clause cascadeClause) throws IOException {
/* 1701 */     return createCascadeQuery(cascadeClause, P8SearchTemplate.EnumTextSearchOption.content);
/*      */   }
/*      */   
/*      */   private String createCascadeQuery(P8SearchDefinition.Clause cascadeClause, P8SearchTemplate.EnumTextSearchOption txtSearchOption) throws IOException {
/* 1705 */     if (cascadeClause == null) {
/* 1706 */       return null;
/*      */     }
/* 1708 */     StringBuilder textQuery = new StringBuilder();
/* 1709 */     if (cascadeClause.isContainer()) {
/* 1710 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)cascadeClause;
/* 1711 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/* 1712 */       List<String> queries = new ArrayList();
/* 1713 */       for (P8SearchDefinition.Clause clause : clauses)
/*      */       {
/* 1715 */         String query = createCascadeQuery(clause, txtSearchOption);
/* 1716 */         if ((query != null) && (query.length() != 0))
/*      */         {
/* 1718 */           queries.add(query); }
/*      */       }
/* 1720 */       P8SearchDefinition.Operator operator = container.getJoin();
/* 1721 */       boolean and = (operator != null) && (operator == P8SearchDefinition.Operator.and);
/* 1722 */       for (String query : queries) {
/* 1723 */         if (textQuery.length() > 0)
/* 1724 */           textQuery.append(and ? " AND " : " OR ");
/* 1725 */         textQuery.append(query);
/*      */       }
/* 1727 */       if (queries.size() > 1)
/* 1728 */         textQuery.insert(0, "(").append(")");
/*      */     } else {
/* 1730 */       P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)cascadeClause;
/* 1731 */       List<P8SearchDefinition.ContentTerm> terms = item.getContentTerms();
/* 1732 */       if ((terms == null) || (terms.size() < 1)) {
/* 1733 */         return null;
/*      */       }
/* 1735 */       String propName = item.getPropertySymbolicName();
/* 1736 */       boolean fielded = (propName != null) && (propName.length() > 0);
/* 1737 */       if (((fielded) && (txtSearchOption == P8SearchTemplate.EnumTextSearchOption.property)) || ((!fielded) && (txtSearchOption == P8SearchTemplate.EnumTextSearchOption.content)))
/*      */       {
/*      */ 
/*      */ 
/* 1741 */         P8SearchDefinition.RequiredState required = item.getRequiredState();
/* 1742 */         P8SearchDefinition.GroupAction groupAction = item.getGroupAction();
/* 1743 */         for (P8SearchDefinition.ContentTerm term : terms) {
/* 1744 */           if (textQuery.length() > 0) {
/* 1745 */             textQuery.append(" ");
/* 1746 */             textQuery.append(groupAction == P8SearchDefinition.GroupAction.all ? "AND " : "OR ");
/*      */           }
/* 1748 */           String value = term.getValue();
/* 1749 */           if ((value != null) && (!value.isEmpty()))
/*      */           {
/* 1751 */             if ((term.isPhrase()) && (!item.isSearchModifierProximity())) {
/* 1752 */               value = value.replaceAll("\"", "\\\"");
/* 1753 */               value = "\"" + value + "\"";
/*      */             }
/* 1755 */             if (required == P8SearchDefinition.RequiredState.prohibited)
/* 1756 */               value = "-" + value;
/* 1757 */             textQuery.append(value);
/*      */           } }
/* 1759 */         if (textQuery.length() > 0) {
/* 1760 */           if (item.isSearchModifierProximity()) {
/* 1761 */             int range = item.getSearchModifierRange();
/* 1762 */             if (range < 1)
/* 1763 */               range = 1024;
/* 1764 */             for (int i = 0; i < textQuery.length(); i++) {
/* 1765 */               if ((textQuery.charAt(i) == '"') && ((i == 0) || (textQuery.charAt(i - 1) != '\\')))
/* 1766 */                 textQuery.insert(i, "\\");
/*      */             }
/* 1768 */             textQuery.insert(0, "\"").append("\"~").append(range);
/* 1769 */           } else if ((terms.size() > 1) && 
/* 1770 */             (textQuery.length() > 0)) {
/* 1771 */             textQuery.insert(0, "(");
/* 1772 */             textQuery.append(")");
/*      */           }
/*      */           
/* 1775 */           if (fielded) {
/* 1776 */             textQuery.insert(0, propName + ":");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1781 */     return textQuery.toString();
/*      */   }
/*      */   
/*      */   private String createVerityQuery(P8SearchDefinition.Clause verityClause, boolean showRank) {
/* 1785 */     StringBuilder buffer = new StringBuilder();
/* 1786 */     if (verityClause != null) {
/* 1787 */       if (verityClause.isContainer()) {
/* 1788 */         P8SearchDefinition.Container verityContainer = (P8SearchDefinition.Container)verityClause;
/* 1789 */         List<P8SearchDefinition.Clause> verityClauses = verityContainer.getClauses();
/* 1790 */         for (P8SearchDefinition.Clause vc : verityClauses)
/*      */         {
/* 1792 */           String clause = createVerityQuery(vc, showRank);
/* 1793 */           if ((clause != null) && (clause.length() != 0))
/*      */           {
/*      */ 
/* 1796 */             if (buffer.length() > 0)
/* 1797 */               buffer.append(", ");
/* 1798 */             buffer.append(clause);
/*      */           }
/*      */         }
/* 1801 */         if (buffer.length() > 0) {
/* 1802 */           buffer.insert(0, "<" + verityContainer.getJoin() + "> (");
/* 1803 */           buffer.append(")");
/*      */         }
/*      */       }
/*      */       else {
/* 1807 */         P8SearchDefinition.GroupAction groupAction = ((P8SearchDefinition.ContentClauseItem)verityClause).getGroupAction();
/* 1808 */         String clause; String clause; if (groupAction == P8SearchDefinition.GroupAction.in) {
/* 1809 */           clause = createVerityInItemClause((P8SearchDefinition.VerityClauseInItem)verityClause, showRank); } else { String clause;
/* 1810 */           if (groupAction == P8SearchDefinition.GroupAction.vql) {
/* 1811 */             clause = createVerityVQLItemClause((P8SearchDefinition.VerityClauseVQLItem)verityClause);
/*      */           } else
/* 1813 */             clause = createVerityGenericItemClause((P8SearchDefinition.ContentClauseItem)verityClause, showRank); }
/* 1814 */         if ((clause != null) && (clause.length() > 0))
/* 1815 */           buffer.append(clause);
/*      */       }
/*      */     }
/* 1818 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private String createVerityGenericItemClause(P8SearchDefinition.ContentClauseItem verityItem, boolean showRank) {
/* 1822 */     StringBuilder buffer = new StringBuilder();
/* 1823 */     List<P8SearchDefinition.ContentTerm> units = verityItem.getContentTerms();
/* 1824 */     for (P8SearchDefinition.ContentTerm unit : units) {
/* 1825 */       String clause = createVerityUnitClause(unit, showRank);
/* 1826 */       if ((clause != null) && (clause.length() > 0)) {
/* 1827 */         if (buffer.length() > 0)
/* 1828 */           buffer.append(", ");
/* 1829 */         buffer.append(clause);
/*      */       }
/*      */     }
/*      */     
/* 1833 */     if (buffer.length() > 0) {
/* 1834 */       P8SearchDefinition.GroupAction groupAction = verityItem.getGroupAction();
/* 1835 */       String operator = "";
/* 1836 */       if (groupAction == P8SearchDefinition.GroupAction.near) {
/* 1837 */         int distance = verityItem.getSearchModifierRange();
/* 1838 */         operator = distance > 0 ? "<near/" + distance + ">" : "<near>";
/* 1839 */       } else if (groupAction != P8SearchDefinition.GroupAction.none) {
/* 1840 */         operator = "<" + groupAction + ">";
/*      */       }
/* 1842 */       buffer.insert(0, operator + " (");
/*      */       
/* 1844 */       if (showRank)
/* 1845 */         buffer.insert(0, "<Many> ");
/* 1846 */       buffer.append(")");
/*      */     }
/*      */     
/* 1849 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private String createVerityInItemClause(P8SearchDefinition.VerityClauseInItem verityItem, boolean showRank) {
/* 1853 */     String zone = verityItem.getZone();
/* 1854 */     if ((zone == null) || (zone.length() == 0)) {
/* 1855 */       return null;
/*      */     }
/* 1857 */     StringBuilder buffer = new StringBuilder();
/* 1858 */     List<P8SearchDefinition.ContentTerm> units = verityItem.getContentTerms();
/* 1859 */     for (P8SearchDefinition.ContentTerm unit : units) {
/* 1860 */       String clause = createVerityUnitClause(unit, showRank);
/* 1861 */       if ((clause != null) && (clause.length() > 0)) {
/* 1862 */         if (buffer.length() > 0)
/* 1863 */           buffer.append(", ");
/* 1864 */         buffer.append(clause);
/*      */       }
/*      */     }
/*      */     
/* 1868 */     if (buffer.length() > 0) {
/* 1869 */       buffer.insert(0, "(<Any> (");
/* 1870 */       buffer.append(")) ");
/*      */       
/* 1872 */       if (showRank)
/* 1873 */         buffer.append("<Many> ");
/* 1874 */       buffer.append("<In> ");
/* 1875 */       buffer.append(zone);
/*      */     }
/*      */     
/* 1878 */     return buffer.toString();
/*      */   }
/*      */   
/*      */   private String createVerityVQLItemClause(P8SearchDefinition.VerityClauseVQLItem verityItem) {
/* 1882 */     String vql = verityItem.getVql();
/* 1883 */     vql = vql.replaceAll("'", "''");
/*      */     
/* 1885 */     return vql;
/*      */   }
/*      */   
/*      */   private String createVerityUnitClause(P8SearchDefinition.ContentTerm verityUnit, boolean showRank) {
/* 1889 */     String value = verityUnit.getValue();
/* 1890 */     if ((value == null) || (value.length() == 0)) {
/* 1891 */       return null;
/*      */     }
/* 1893 */     StringBuilder buffer = new StringBuilder();
/* 1894 */     if (verityUnit.isCaseSensitive())
/* 1895 */       buffer.append("<Case> ");
/* 1896 */     if (verityUnit.isProhibited())
/* 1897 */       buffer.append("<Not> ");
/* 1898 */     if (verityUnit.isPhrase())
/* 1899 */       value = "\"" + value + "\"";
/* 1900 */     P8SearchDefinition.VeritySearchOperator operator = verityUnit.getWordVariation();
/* 1901 */     if (operator != null) {
/* 1902 */       if (operator == P8SearchDefinition.VeritySearchOperator.soundex) {
/* 1903 */         buffer.append("<Soundex> ");
/* 1904 */       } else if (operator == P8SearchDefinition.VeritySearchOperator.stem) {
/* 1905 */         if (showRank)
/* 1906 */           buffer.append("<Many> ");
/* 1907 */         buffer.append("<Stem> ");
/* 1908 */       } else if (operator == P8SearchDefinition.VeritySearchOperator.thesaurus) {
/* 1909 */         buffer.append("<Thesaurus> ");
/* 1910 */       } else if (operator == P8SearchDefinition.VeritySearchOperator.wildcard) {
/* 1911 */         if (showRank)
/* 1912 */           buffer.append("<Many> ");
/* 1913 */         buffer.append("<Wildcard> ");
/* 1914 */       } else if (operator == P8SearchDefinition.VeritySearchOperator.word) {
/* 1915 */         if (showRank)
/* 1916 */           buffer.append("<Many> ");
/* 1917 */         buffer.append("<Word> ");
/*      */       }
/*      */     }
/* 1920 */     buffer.append(value);
/*      */     
/* 1922 */     return buffer.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public P8SearchDefinition getSearchDefinition()
/*      */   {
/* 1929 */     return this.searchDefinition;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String[] getRecordSelectProps()
/*      */   {
/* 1936 */     return RECORD_SELECT_PROPS;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String[] getRmfolderSelectProps()
/*      */   {
/* 1944 */     return RMFOLDER_SELECT_PROPS;
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\util\IERSearchRunner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */