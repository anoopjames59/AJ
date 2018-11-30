/*      */ package com.ibm.ier.plugin.search.p8;
/*      */ 
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchClass;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchEditProperty;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchFolder;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchFolderView;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchType;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.VersionOption;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.TreeMap;
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
/*      */ public class P8SearchDefinition
/*      */ {
/*   31 */   public static final SelectProperty SELECT_PROPERTY_ALL = new SelectProperty("*", null, (String)null, 0, (String)null, null);
/*   32 */   public static final Clause COMMON_CLAUSE = new Container();
/*      */   
/*   34 */   private static final String NEW_LINE = System.getProperty("line.separator") + "    ";
/*      */   private String dtdVersion;
/*      */   
/*   37 */   public static enum MergeScope { intersection,  union,  none;
/*      */     
/*      */     private MergeScope() {} }
/*      */   
/*   41 */   public static enum SortOrder { ascending,  descending,  none;
/*      */     
/*      */     private SortOrder() {} }
/*      */   
/*   45 */   private String searchVersion; public static enum DataType { typestring,  typeobject,  typedate,  typebinary,  typeboolean,  typedouble,  typeguid,  typelong;
/*      */     
/*      */     private DataType() {} }
/*      */   
/*   49 */   private String productName; public static enum GroupAction { all,  any,  near,  none,  paragraph,  sentence,  in,  vql;
/*      */     
/*      */     private GroupAction() {} }
/*      */   
/*   53 */   private String productVersion; public static enum RequiredState { prohibited,  required,  none;
/*      */     
/*      */     private RequiredState() {} }
/*      */   
/*   57 */   public static enum SearchModifier { proximity,  boost,  fuzzy;
/*      */     
/*      */     private SearchModifier() {} }
/*      */   
/*   61 */   public static enum JoinType { inner,  leftouter,  fullouter;
/*      */     
/*      */     private JoinType() {} }
/*      */   
/*   65 */   public static enum CbrColumn { rank,  contentsummary;
/*      */     
/*      */     private CbrColumn() {} }
/*      */   private boolean template;
/*   69 */   public static enum Operator { and,  commonplaceholder,  eq,  gt,  gte,  in,  inany,  isnotnull,  isnull,  like,  lt,  lte,  neq,  notin,  notlike,  or,  between,  notbetween,  startswith,  endswith,  contains;
/*      */     
/*      */     private Operator() {} }
/*      */   
/*   73 */   private SearchTemplateBase.VersionOption versionSelection; public static enum VeritySearchOperator { soundex,  stem,  thesaurus,  wildcard,  word,  none;
/*      */     
/*      */     private VeritySearchOperator() {}
/*      */   }
/*      */   
/*      */   private boolean showAndOrConditions;
/*      */   private boolean showMaxRecords;
/*      */   private boolean showOperators;
/*      */   private MergeScope mergeOption;
/*      */   private int maxRecords;
/*      */   private SearchTemplate.TextSearchType textSearchType;
/*      */   private boolean autoResolved;
/*      */   private boolean invalidRepository;
/*      */   private boolean invalidFolder;
/*      */   private boolean invalidClass;
/*      */   private boolean invalidFileType;
/*      */   private boolean invalidProperty;
/*      */   private boolean invalidTextSearch;
/*      */   private int databaseType;
/*      */   private boolean apiIncompatible;
/*      */   private List<SearchInObjectStore> objectStores;
/*      */   private List<SearchInFolder> folders;
/*      */   private List<SearchClause> searchClauses;
/*      */   public P8SearchDefinition()
/*      */   {
/*   98 */     this.objectStores = new ArrayList();
/*   99 */     this.folders = new ArrayList();
/*  100 */     this.searchClauses = new ArrayList();
/*      */   }
/*      */   
/*  103 */   public boolean isInvalidRepository() { return this.invalidRepository; }
/*      */   
/*      */   public void setInvalidRepository(boolean invalidRepository)
/*      */   {
/*  107 */     this.invalidRepository = invalidRepository;
/*      */   }
/*      */   
/*      */   public boolean isInvalidFolder() {
/*  111 */     return this.invalidFolder;
/*      */   }
/*      */   
/*      */   public void setInvalidFolder(boolean invalidFolder) {
/*  115 */     this.invalidFolder = invalidFolder;
/*      */   }
/*      */   
/*      */   public boolean isInvalidClass() {
/*  119 */     return this.invalidClass;
/*      */   }
/*      */   
/*      */   public void setInvalidClass(boolean invalidClass) {
/*  123 */     this.invalidClass = invalidClass;
/*      */   }
/*      */   
/*      */   public boolean isInvalidFileType() {
/*  127 */     return this.invalidFileType;
/*      */   }
/*      */   
/*      */   public void setInvalidFileType(boolean invalidFileType) {
/*  131 */     this.invalidFileType = invalidFileType;
/*      */   }
/*      */   
/*      */   public boolean isInvalidProperty() {
/*  135 */     return this.invalidProperty;
/*      */   }
/*      */   
/*      */   public void setInvalidProperty(boolean invalidProperty) {
/*  139 */     this.invalidProperty = invalidProperty;
/*      */   }
/*      */   
/*      */   public boolean isInvalidTextSearch() {
/*  143 */     return this.invalidTextSearch;
/*      */   }
/*      */   
/*      */   public void setInvalidTextSearch(boolean invalidTextSearch) {
/*  147 */     this.invalidTextSearch = invalidTextSearch;
/*      */   }
/*      */   
/*      */   public String getDtdVersion() {
/*  151 */     return this.dtdVersion;
/*      */   }
/*      */   
/*      */   public void setDtdVersion(String dtdVersion) {
/*  155 */     this.dtdVersion = dtdVersion;
/*      */   }
/*      */   
/*      */   public List<SearchInFolder> getFolders() {
/*  159 */     return this.folders;
/*      */   }
/*      */   
/*      */   public void setFolders(List<SearchInFolder> folders) {
/*  163 */     this.folders = folders;
/*      */   }
/*      */   
/*      */   public void addFolder(SearchInFolder folder) {
/*  167 */     this.folders.add(folder);
/*      */   }
/*      */   
/*      */   public int getMaxRecords() {
/*  171 */     return this.maxRecords;
/*      */   }
/*      */   
/*      */   public void setMaxRecords(int maxRecords) {
/*  175 */     this.maxRecords = maxRecords;
/*      */   }
/*      */   
/*      */   public void setMaxRecords(String maxRecords) {
/*  179 */     this.maxRecords = Integer.parseInt(maxRecords);
/*      */   }
/*      */   
/*      */   public MergeScope getMergeOption() {
/*  183 */     return this.mergeOption;
/*      */   }
/*      */   
/*      */   public boolean isMergeOptionUnion() {
/*  187 */     return (this.mergeOption != null) && (this.mergeOption == MergeScope.union);
/*      */   }
/*      */   
/*      */   public void setMergeOption(String mergeOption) {
/*  191 */     this.mergeOption = (mergeOption == null ? null : MergeScope.valueOf(mergeOption));
/*      */   }
/*      */   
/*      */   public SearchTemplate.TextSearchType getTextSearchType() {
/*  195 */     return this.textSearchType;
/*      */   }
/*      */   
/*      */   public void setTextSearchType(SearchTemplate.TextSearchType textSearchType) {
/*  199 */     this.textSearchType = textSearchType;
/*      */   }
/*      */   
/*      */   public boolean isCascade() {
/*  203 */     return (this.textSearchType == null) || (this.textSearchType == SearchTemplate.TextSearchType.cascade);
/*      */   }
/*      */   
/*      */   public List<SearchInObjectStore> getObjectStores() {
/*  207 */     return this.objectStores;
/*      */   }
/*      */   
/*      */   public List<String> getObjectStoreNames() {
/*  211 */     List<String> osNames = new ArrayList();
/*  212 */     for (SearchInObjectStore sos : this.objectStores) {
/*  213 */       osNames.add(sos.getName());
/*      */     }
/*  215 */     return osNames;
/*      */   }
/*      */   
/*      */   public void setObjectStores(List<SearchInObjectStore> objectStores) {
/*  219 */     this.objectStores = objectStores;
/*      */   }
/*      */   
/*      */   public void addObjectStore(SearchInObjectStore objectStore) {
/*  223 */     this.objectStores.add(objectStore);
/*      */   }
/*      */   
/*      */   public String getProductName() {
/*  227 */     return this.productName;
/*      */   }
/*      */   
/*      */   public void setProductName(String productName) {
/*  231 */     this.productName = productName;
/*      */   }
/*      */   
/*      */   public boolean isIcnGenerated() {
/*  235 */     return (this.productName == null) || (this.productName.equals("Navigator"));
/*      */   }
/*      */   
/*      */   public String getProductVersion() {
/*  239 */     return this.productVersion;
/*      */   }
/*      */   
/*      */   public void setProductVersion(String productVersion) {
/*  243 */     this.productVersion = productVersion;
/*      */   }
/*      */   
/*      */   public List<SearchClause> getSearchClauses() {
/*  247 */     return this.searchClauses;
/*      */   }
/*      */   
/*      */   public void addSearchClause(SearchClause searchClause) {
/*  251 */     searchClause.setSearchDefinition(this);
/*  252 */     this.searchClauses.add(searchClause);
/*      */   }
/*      */   
/*      */   public SearchClause getSearchClause(SearchTemplateBase.ObjectType objectType) {
/*  256 */     SearchClause searchClause = null;
/*  257 */     for (SearchClause sc : this.searchClauses) {
/*  258 */       if (sc.getFrom().equals(objectType)) {
/*  259 */         searchClause = sc;
/*  260 */         break;
/*      */       }
/*      */     }
/*      */     
/*  264 */     return searchClause;
/*      */   }
/*      */   
/*      */   public String getSearchVersion() {
/*  268 */     return this.searchVersion;
/*      */   }
/*      */   
/*      */   public void setSearchVersion(String searchVersion) {
/*  272 */     this.searchVersion = searchVersion;
/*      */   }
/*      */   
/*      */   public boolean isTemplate() {
/*  276 */     return this.template;
/*      */   }
/*      */   
/*      */   public void setTemplate(boolean template) {
/*  280 */     this.template = template;
/*      */   }
/*      */   
/*      */   public boolean isShowAndOrConditions() {
/*  284 */     return this.showAndOrConditions;
/*      */   }
/*      */   
/*      */   public void setShowAndOrConditions(boolean showAndOrConditions) {
/*  288 */     this.showAndOrConditions = showAndOrConditions;
/*      */   }
/*      */   
/*      */   public void setShowAndOrConditions(String showAndOrConditions) {
/*  292 */     this.showAndOrConditions = Boolean.valueOf(showAndOrConditions).booleanValue();
/*      */   }
/*      */   
/*      */   public boolean isShowMaxRecords() {
/*  296 */     return this.showMaxRecords;
/*      */   }
/*      */   
/*      */   public void setShowMaxRecords(boolean showMaxRecords) {
/*  300 */     this.showMaxRecords = showMaxRecords;
/*      */   }
/*      */   
/*      */   public void setShowMaxRecords(String showMaxRecords) {
/*  304 */     this.showMaxRecords = Boolean.valueOf(showMaxRecords).booleanValue();
/*      */   }
/*      */   
/*      */   public boolean isShowOperators() {
/*  308 */     return this.showOperators;
/*      */   }
/*      */   
/*      */   public void setShowOperators(boolean showOperators) {
/*  312 */     this.showOperators = showOperators;
/*      */   }
/*      */   
/*      */   public void setShowOperators(String showOperators) {
/*  316 */     this.showOperators = Boolean.valueOf(showOperators).booleanValue();
/*      */   }
/*      */   
/*      */   public SearchTemplateBase.VersionOption getVersionSelection() {
/*  320 */     return this.versionSelection;
/*      */   }
/*      */   
/*      */   public void setVersionSelection(String versionSelection) {
/*  324 */     this.versionSelection = (versionSelection == null ? null : SearchTemplateBase.VersionOption.valueOf(versionSelection));
/*      */   }
/*      */   
/*      */   public boolean isVersionSelectionAll() {
/*  328 */     return (this.versionSelection != null) && (this.versionSelection.equals(SearchTemplateBase.VersionOption.allversions));
/*      */   }
/*      */   
/*      */   public boolean isVersionSelectionReleased() {
/*  332 */     return (this.versionSelection != null) && (this.versionSelection.equals(SearchTemplateBase.VersionOption.releasedversion));
/*      */   }
/*      */   
/*      */   public boolean isVersionSelectionCurrent() {
/*  336 */     return (this.versionSelection != null) && (this.versionSelection.equals(SearchTemplateBase.VersionOption.currentversion));
/*      */   }
/*      */   
/*      */   public boolean isAutoResolved() {
/*  340 */     return this.autoResolved;
/*      */   }
/*      */   
/*      */   public void setAutoResolved(boolean autoResolved) {
/*  344 */     this.autoResolved = autoResolved;
/*      */   }
/*      */   
/*      */   public int getDatabaseType() {
/*  348 */     return this.databaseType;
/*      */   }
/*      */   
/*      */   public void setDatabaseType(int databaseType) {
/*  352 */     this.databaseType = databaseType;
/*      */   }
/*      */   
/*      */   public boolean isApiIncompatible() {
/*  356 */     return this.apiIncompatible;
/*      */   }
/*      */   
/*      */   public void setApiIncompatible(boolean apiIncompatible) {
/*  360 */     this.apiIncompatible = apiIncompatible;
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/*  365 */     StringBuilder str = new StringBuilder();
/*  366 */     str.append("(dtdVersion = ").append(this.dtdVersion);
/*  367 */     str.append(", searchVersion = ").append(this.searchVersion);
/*  368 */     str.append(", productName = ").append(this.productName);
/*  369 */     str.append(", productVersion = ").append(this.productVersion);
/*  370 */     str.append(", template = ").append(this.template);
/*  371 */     str.append(", versionSelection = ").append(this.versionSelection);
/*  372 */     str.append(", showAndOrConditions = ").append(this.showAndOrConditions);
/*  373 */     str.append(", showMaxRecords = ").append(this.showMaxRecords);
/*  374 */     str.append(", showOperators = ").append(this.showOperators);
/*  375 */     str.append(", mergeOption = ").append(this.mergeOption);
/*  376 */     str.append(", maxRecords = ").append(this.maxRecords);
/*  377 */     str.append(", textSearchType = ").append(this.textSearchType);
/*  378 */     str.append(", autoResolved = ").append(this.autoResolved);
/*  379 */     str.append(", invalidRepository = ").append(this.invalidRepository);
/*  380 */     str.append(", invalidClass = ").append(this.invalidClass);
/*  381 */     str.append(", invalidFolder = ").append(this.invalidFolder);
/*  382 */     str.append(", invalidFileType = ").append(this.invalidFileType);
/*  383 */     str.append(", invalidProperty = ").append(this.invalidProperty);
/*  384 */     str.append(", invalidTextSearch = ").append(this.invalidTextSearch);
/*  385 */     str.append(", databaseType = ").append(this.databaseType);
/*  386 */     str.append(", apiIncompatible = ").append(this.apiIncompatible);
/*  387 */     str.append(", ").append(NEW_LINE).append("OBJECTSTORES = ").append(this.objectStores);
/*  388 */     str.append(", ").append(NEW_LINE).append("FOLDERS = ").append(this.folders);
/*  389 */     str.append(", ").append(NEW_LINE).append("SEARCHCLAUSES = ").append(this.searchClauses).append(")");
/*      */     
/*  391 */     return str.toString();
/*      */   }
/*      */   
/*      */   public static class SearchInObjectStore
/*      */   {
/*      */     private String id;
/*      */     private String name;
/*      */     private String symbolicName;
/*      */     
/*      */     SearchInObjectStore() {}
/*      */     
/*      */     public SearchInObjectStore(String id, String name) {
/*  403 */       this.id = id;
/*  404 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String getId() {
/*  408 */       return this.id;
/*      */     }
/*      */     
/*      */     void setId(String id) {
/*  412 */       this.id = id;
/*      */     }
/*      */     
/*      */     public String getName() {
/*  416 */       return this.name;
/*      */     }
/*      */     
/*      */     public void setName(String name) {
/*  420 */       this.name = name;
/*      */     }
/*      */     
/*      */     public String getSymbolicName() {
/*  424 */       return this.symbolicName;
/*      */     }
/*      */     
/*      */     public void setSymbolicName(String symbolicName) {
/*  428 */       this.symbolicName = symbolicName;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  433 */       StringBuilder str = new StringBuilder();
/*  434 */       str.append("(id = ").append(this.id);
/*  435 */       str.append(", name = ").append(this.name).append(")");
/*      */       
/*  437 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class SearchInFolder extends SearchTemplate.SearchFolder
/*      */   {
/*      */     public SearchInFolder() {}
/*      */     
/*      */     public SearchInFolder(String id, String pathName, boolean searchSubfolders, String view, String objectStoreId) {
/*  446 */       super(pathName, searchSubfolders, view == null ? null : SearchTemplate.SearchFolderView.valueOf(view), objectStoreId, null, null);
/*      */     }
/*      */     
/*      */     public SearchInFolder(String id, String pathName, boolean searchSubfolders, SearchTemplate.SearchFolderView view, String objectStoreId) {
/*  450 */       super(pathName, searchSubfolders, view, objectStoreId, null, null);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  455 */       StringBuilder str = new StringBuilder();
/*  456 */       str.append("(id = ").append(getId());
/*  457 */       str.append(", pathName = ").append(getPathName());
/*  458 */       str.append(", searchSubfolders = ").append(isSearchSubfolders());
/*  459 */       str.append(", itemId = ").append(getItemId());
/*  460 */       str.append(", view = ").append(getView());
/*  461 */       str.append(", objectStoreId = ").append(getObjectStoreId()).append(")");
/*      */       
/*  463 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class SearchClause {
/*  468 */     private List<P8SearchDefinition.SelectProperty> selectProperties = new ArrayList();
/*      */     private SearchTemplateBase.ObjectType from;
/*      */     private P8SearchDefinition.Clause whereClause;
/*      */     private P8SearchDefinition.ContentCriteria contentCriteria;
/*  472 */     private List<P8SearchDefinition.Subclass> subclasses = new ArrayList();
/*  473 */     private Map<String, P8SearchDefinition.ClauseItem> whereClauseItems = null;
/*  474 */     private Map<String, P8SearchDefinition.ClauseItem> contentClauseItems = null;
/*      */     private P8SearchDefinition searchDefinition;
/*      */     private P8SearchDefinition.Operator join;
/*      */     
/*      */     public SearchClause(String from) {
/*  479 */       this.from = (from == null ? null : SearchTemplateBase.ObjectType.valueOf(from));
/*      */     }
/*      */     
/*      */     public SearchClause(SearchTemplateBase.ObjectType from) {
/*  483 */       this.from = from;
/*      */     }
/*      */     
/*      */     public SearchClause() {}
/*      */     
/*      */     public boolean isSortingDefined()
/*      */     {
/*  490 */       Map<String, P8SearchDefinition.SelectProperty> sortProps = getSortedProperties(this.selectProperties);
/*      */       
/*  492 */       return (sortProps != null) && (sortProps.size() > 0);
/*      */     }
/*      */     
/*      */     public SearchTemplateBase.ObjectType getFrom() {
/*  496 */       return this.from;
/*      */     }
/*      */     
/*      */     public void setFrom(String from) {
/*  500 */       this.from = (from == null ? null : SearchTemplateBase.ObjectType.valueOf(from));
/*      */     }
/*      */     
/*      */     public List<P8SearchDefinition.SelectProperty> getSelectProperties() {
/*  504 */       return this.selectProperties;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.SelectProperty getSelectProperty(String propertyName) {
/*  508 */       if (propertyName.startsWith("[")) {
/*  509 */         propertyName = propertyName.substring(1, propertyName.length() - 1);
/*      */       }
/*      */       
/*  512 */       P8SearchDefinition.SelectProperty selectProp = null;
/*  513 */       for (P8SearchDefinition.SelectProperty sp : this.selectProperties) {
/*  514 */         if (sp.getSymbolicName().equals(propertyName)) {
/*  515 */           selectProp = sp;
/*  516 */           break;
/*      */         }
/*      */       }
/*      */       
/*  520 */       return selectProp;
/*      */     }
/*      */     
/*      */     public void setSelectProperties(List<P8SearchDefinition.SelectProperty> selectProperties) {
/*  524 */       this.selectProperties = selectProperties;
/*      */     }
/*      */     
/*      */     public void addSelectProperty(P8SearchDefinition.SelectProperty selectProperty) {
/*  528 */       selectProperty.setSearchClause(this);
/*  529 */       this.selectProperties.add(selectProperty);
/*      */     }
/*      */     
/*      */     public List<P8SearchDefinition.Subclass> getSubclasses() {
/*  533 */       return this.subclasses;
/*      */     }
/*      */     
/*      */     public void setSubclasses(List<P8SearchDefinition.Subclass> subclasses) {
/*  537 */       this.subclasses = subclasses;
/*      */     }
/*      */     
/*      */     public void addSubclass(P8SearchDefinition.Subclass subclass) {
/*  541 */       this.subclasses.add(subclass);
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.Subclass getSubclass(String itemId) {
/*  545 */       if ((itemId == null) || (itemId.isEmpty())) {
/*  546 */         return null;
/*      */       }
/*  548 */       P8SearchDefinition.Subclass subclass = null;
/*  549 */       for (P8SearchDefinition.Subclass sc : this.subclasses) {
/*  550 */         if (itemId.equals(sc.getItemId())) {
/*  551 */           subclass = sc;
/*  552 */           break;
/*      */         }
/*      */       }
/*      */       
/*  556 */       return subclass;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.ContentCriteria getContentCriteria() {
/*  560 */       return this.contentCriteria;
/*      */     }
/*      */     
/*      */     public void setContentCriteria(P8SearchDefinition.ContentCriteria contentCriteria) {
/*  564 */       this.contentCriteria = contentCriteria;
/*  565 */       this.contentClauseItems = null;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.Clause getWhereClause() {
/*  569 */       return this.whereClause;
/*      */     }
/*      */     
/*      */     public void setWhereClause(P8SearchDefinition.Clause whereClause) {
/*  573 */       this.whereClause = whereClause;
/*  574 */       this.whereClauseItems = null;
/*      */     }
/*      */     
/*      */     private void extractClauseItems(P8SearchDefinition.Clause clause, Map<String, P8SearchDefinition.ClauseItem> conditions) {
/*  578 */       if (clause == null) {
/*  579 */         return;
/*      */       }
/*  581 */       if (clause.isContainer()) {
/*  582 */         P8SearchDefinition.Container wcc = (P8SearchDefinition.Container)clause;
/*  583 */         List<P8SearchDefinition.Clause> whereClauses = wcc.getClauses();
/*  584 */         for (P8SearchDefinition.Clause wc : whereClauses)
/*  585 */           extractClauseItems(wc, conditions);
/*      */       } else {
/*  587 */         P8SearchDefinition.ClauseItem condition = (P8SearchDefinition.ClauseItem)clause;
/*  588 */         conditions.put(clause.getItemId(), condition);
/*      */       }
/*      */     }
/*      */     
/*      */     public Map<String, P8SearchDefinition.ClauseItem> retrieveWhereClauseConditions() {
/*  593 */       if (this.whereClauseItems != null) {
/*  594 */         return this.whereClauseItems;
/*      */       }
/*  596 */       this.whereClauseItems = new HashMap();
/*  597 */       extractClauseItems(this.whereClause, this.whereClauseItems);
/*      */       
/*  599 */       return this.whereClauseItems;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.WhereClauseCondition getWhereClauseCondition(String itemId) {
/*  603 */       return (itemId == null) || (itemId.isEmpty()) ? null : (P8SearchDefinition.WhereClauseCondition)retrieveWhereClauseConditions().get(itemId);
/*      */     }
/*      */     
/*      */     public Map<String, P8SearchDefinition.ClauseItem> retrieveContentClauseItems() {
/*  607 */       if (this.contentClauseItems != null) {
/*  608 */         return this.contentClauseItems;
/*      */       }
/*  610 */       this.contentClauseItems = new HashMap();
/*  611 */       if (this.contentCriteria != null) {
/*  612 */         extractClauseItems(this.contentCriteria.getContentClause(), this.contentClauseItems);
/*      */       }
/*  614 */       return this.contentClauseItems;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.ContentClauseItem getContentClauseItem(String itemId) {
/*  618 */       return (itemId == null) || (itemId.isEmpty()) ? null : (P8SearchDefinition.ContentClauseItem)retrieveContentClauseItems().get(itemId);
/*      */     }
/*      */     
/*      */     private void resetItemsCache() {
/*  622 */       this.whereClauseItems = null;
/*  623 */       this.contentClauseItems = null;
/*      */     }
/*      */     
/*      */     public boolean isContentCriteriaDefined() {
/*  627 */       boolean defined = false;
/*  628 */       Collection<P8SearchDefinition.ClauseItem> conditions = retrieveContentClauseItems().values();
/*  629 */       for (P8SearchDefinition.ClauseItem c : conditions) {
/*  630 */         P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)c;
/*  631 */         String text = item.getUserText();
/*  632 */         if ((text != null) && (text.length() > 0)) {
/*  633 */           defined = true;
/*  634 */           break;
/*      */         }
/*      */       }
/*      */       
/*  638 */       return defined;
/*      */     }
/*      */     
/*      */     public boolean isPropertyContentCriteriaDefined() {
/*  642 */       boolean defined = false;
/*  643 */       Collection<P8SearchDefinition.ClauseItem> conditions = retrieveWhereClauseConditions().values();
/*  644 */       for (P8SearchDefinition.ClauseItem c : conditions) {
/*  645 */         P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)c;
/*  646 */         String value = condition.getLiteral();
/*  647 */         defined = (condition.getOperator() == P8SearchDefinition.Operator.contains) && (value != null) && (value.length() > 0);
/*  648 */         if (defined) {
/*      */           break;
/*      */         }
/*      */       }
/*  652 */       return defined;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition getSearchDefinition() {
/*  656 */       return this.searchDefinition;
/*      */     }
/*      */     
/*      */     public void setSearchDefinition(P8SearchDefinition searchDefinition) {
/*  660 */       this.searchDefinition = searchDefinition;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.Operator getJoin() {
/*  664 */       return this.join;
/*      */     }
/*      */     
/*      */     public void setJoin(P8SearchDefinition.Operator join) {
/*  668 */       this.join = join;
/*      */     }
/*      */     
/*      */     public Map<String, P8SearchDefinition.SelectProperty> getSortedProperties(List<P8SearchDefinition.SelectProperty> selectProps) {
/*  672 */       if ((selectProps == null) || (selectProps.size() < 1)) {
/*  673 */         return null;
/*      */       }
/*      */       
/*  676 */       Map<String, P8SearchDefinition.SelectProperty> sortProps = new TreeMap();
/*  677 */       for (P8SearchDefinition.SelectProperty selectProp : selectProps) {
/*  678 */         int sl = selectProp.getSortLevel();
/*      */         
/*  680 */         if (sl > 0) {
/*  681 */           String sortLevel = String.valueOf(sl);
/*  682 */           sortProps.put(sortLevel, selectProp);
/*      */         }
/*      */       }
/*      */       
/*  686 */       return sortProps;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  691 */       StringBuilder str = new StringBuilder();
/*  692 */       str.append("(").append(P8SearchDefinition.NEW_LINE).append("SELECTCLAUSE = ").append(this.selectProperties);
/*  693 */       str.append(", ").append(P8SearchDefinition.NEW_LINE).append("FROMCLAUSE = ").append(this.from);
/*  694 */       str.append(", ").append(P8SearchDefinition.NEW_LINE).append("SUBCLASSES = ").append(this.subclasses);
/*  695 */       str.append(", ").append(P8SearchDefinition.NEW_LINE).append("WHERECLAUSE = ").append(this.whereClause);
/*  696 */       str.append(", ").append(P8SearchDefinition.NEW_LINE).append("CONTENTCLAUSE = ").append(this.contentCriteria);
/*  697 */       str.append(", ").append(P8SearchDefinition.NEW_LINE).append("JOIN = ").append(this.join).append(")");
/*      */       
/*  699 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class SelectProperty
/*      */   {
/*      */     private String symbolicName;
/*      */     private String displayName;
/*      */     private SearchTemplateBase.ObjectType objectType;
/*      */     private int sortLevel;
/*      */     private P8SearchDefinition.SortOrder sortOrder;
/*      */     private String alignment;
/*      */     private String itemId;
/*      */     private P8SearchDefinition.SearchClause searchClause;
/*      */     
/*      */     public SelectProperty() {}
/*      */     
/*      */     public SelectProperty(String symbolicName, String displayName, String objectType, int sortLevel, String sortOrder, String alignment) {
/*  717 */       this.symbolicName = symbolicName;
/*  718 */       this.displayName = displayName;
/*  719 */       this.objectType = (objectType == null ? null : SearchTemplateBase.ObjectType.valueOf(objectType));
/*  720 */       this.sortLevel = sortLevel;
/*  721 */       this.sortOrder = ((sortOrder == null) || (sortOrder.isEmpty()) ? null : P8SearchDefinition.SortOrder.valueOf(sortOrder));
/*  722 */       this.alignment = alignment;
/*      */     }
/*      */     
/*      */     public SelectProperty(String symbolicName, String displayName, SearchTemplateBase.ObjectType objectType, int sortLevel, P8SearchDefinition.SortOrder sortOrder, String alignment) {
/*  726 */       this.symbolicName = symbolicName;
/*  727 */       this.displayName = displayName;
/*  728 */       this.objectType = objectType;
/*  729 */       this.sortLevel = sortLevel;
/*  730 */       this.sortOrder = sortOrder;
/*  731 */       this.alignment = alignment;
/*      */     }
/*      */     
/*      */     public String getAlignment() {
/*  735 */       return this.alignment;
/*      */     }
/*      */     
/*      */     public void setAlignment(String alignment) {
/*  739 */       this.alignment = alignment;
/*      */     }
/*      */     
/*      */     public String getItemId() {
/*  743 */       return this.itemId;
/*      */     }
/*      */     
/*      */     public void setItemId(String itemId) {
/*  747 */       this.itemId = itemId;
/*      */     }
/*      */     
/*      */     public String getDisplayName() {
/*  751 */       return this.displayName;
/*      */     }
/*      */     
/*      */     public void setDisplayName(String displayName) {
/*  755 */       this.displayName = displayName;
/*      */     }
/*      */     
/*      */     public SearchTemplateBase.ObjectType getObjectType() {
/*  759 */       return this.objectType;
/*      */     }
/*      */     
/*      */     public void setObjectType(String objectType) {
/*  763 */       this.objectType = (objectType == null ? null : SearchTemplateBase.ObjectType.valueOf(objectType));
/*      */     }
/*      */     
/*      */     public int getSortLevel() {
/*  767 */       return this.sortLevel;
/*      */     }
/*      */     
/*      */     public void setSortLevel(int sortLevel) {
/*  771 */       this.sortLevel = sortLevel;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.SortOrder getSortOrder() {
/*  775 */       return this.sortOrder;
/*      */     }
/*      */     
/*      */     public void setSortOrder(P8SearchDefinition.SortOrder sortOrder) {
/*  779 */       this.sortOrder = sortOrder;
/*      */     }
/*      */     
/*      */     public boolean isSortOrderDescending() {
/*  783 */       return (this.sortOrder != null) && (this.sortOrder.equals(P8SearchDefinition.SortOrder.descending));
/*      */     }
/*      */     
/*      */     public String getSymbolicName() {
/*  787 */       return this.symbolicName;
/*      */     }
/*      */     
/*      */     public void setSymbolicName(String symbolicName) {
/*  791 */       this.symbolicName = symbolicName;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.SearchClause getSearchClause() {
/*  795 */       return this.searchClause;
/*      */     }
/*      */     
/*      */     public void setSearchClause(P8SearchDefinition.SearchClause searchClause) {
/*  799 */       this.searchClause = searchClause;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/*  804 */       StringBuilder str = new StringBuilder();
/*  805 */       str.append("(symbolicName = ").append(this.symbolicName);
/*  806 */       str.append(", displayName = ").append(this.displayName);
/*  807 */       str.append(", objectType = ").append(this.objectType);
/*  808 */       str.append(", sortLevel = ").append(this.sortLevel);
/*  809 */       str.append(", sortOrder = ").append(this.sortOrder);
/*  810 */       str.append(", alignment = ").append(this.alignment);
/*  811 */       str.append(", itemId = ").append(this.itemId).append(")");
/*      */       
/*  813 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class Clause {
/*      */     private P8SearchDefinition.Container container;
/*      */     private String itemId;
/*      */     private P8SearchDefinition.SearchClause searchClause;
/*      */     
/*      */     public abstract boolean isContainer();
/*      */     
/*      */     public P8SearchDefinition.Container getContainer() {
/*  825 */       return this.container;
/*      */     }
/*      */     
/*      */     public void setContainer(P8SearchDefinition.Container container) {
/*  829 */       this.container = container;
/*      */     }
/*      */     
/*      */     public String getItemId() {
/*  833 */       return this.itemId;
/*      */     }
/*      */     
/*      */     public void setItemId(String itemId) {
/*  837 */       this.itemId = itemId;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.SearchClause getSearchClause() {
/*  841 */       if (this.searchClause == null) {
/*  842 */         P8SearchDefinition.Container c = getContainer();
/*  843 */         this.searchClause = (c == null ? null : c.getSearchClause());
/*      */       }
/*      */       
/*  846 */       return this.searchClause;
/*      */     }
/*      */     
/*      */     public void setSearchClause(P8SearchDefinition.SearchClause searchClause) {
/*  850 */       this.searchClause = searchClause;
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract class ClauseItem extends P8SearchDefinition.Clause
/*      */   {
/*      */     private SearchTemplate.SearchEditProperty editProperty;
/*      */     
/*      */     public ClauseItem() {}
/*      */     
/*      */     public ClauseItem(SearchTemplate.SearchEditProperty editProperty) {
/*  861 */       this.editProperty = editProperty;
/*      */     }
/*      */     
/*      */     public ClauseItem(String editProperty) {
/*  865 */       this.editProperty = (editProperty == null ? null : SearchTemplate.SearchEditProperty.valueOf(editProperty));
/*      */     }
/*      */     
/*      */     public SearchTemplate.SearchEditProperty getEditProperty() {
/*  869 */       return this.editProperty == null ? SearchTemplate.SearchEditProperty.editable : this.editProperty;
/*      */     }
/*      */     
/*      */     public void setEditProperty(String editProperty) {
/*  873 */       this.editProperty = (editProperty == null ? null : SearchTemplate.SearchEditProperty.valueOf(editProperty));
/*      */     }
/*      */     
/*      */     public boolean isHidden() {
/*  877 */       return (this.editProperty != null) && (this.editProperty == SearchTemplate.SearchEditProperty.hidden);
/*      */     }
/*      */     
/*      */     public boolean isReadOnly() {
/*  881 */       return (this.editProperty != null) && (this.editProperty == SearchTemplate.SearchEditProperty.readonly);
/*      */     }
/*      */     
/*      */     public boolean isRequired() {
/*  885 */       return (this.editProperty != null) && (this.editProperty == SearchTemplate.SearchEditProperty.required);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Container extends P8SearchDefinition.Clause {
/*      */     private P8SearchDefinition.Operator join;
/*  891 */     private List<P8SearchDefinition.Clause> clauses = new ArrayList();
/*      */     private boolean include;
/*      */     private boolean range;
/*      */     
/*      */     public Container() {}
/*      */     
/*      */     public Container(String join)
/*      */     {
/*  899 */       this(join == null ? null : P8SearchDefinition.Operator.valueOf(join));
/*      */     }
/*      */     
/*      */     public Container(P8SearchDefinition.Operator join) {
/*  903 */       this.join = join;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.Operator getJoin() {
/*  907 */       return this.join;
/*      */     }
/*      */     
/*      */     public void setJoin(P8SearchDefinition.Operator join) {
/*  911 */       this.join = join;
/*      */     }
/*      */     
/*      */     public boolean isContainer() {
/*  915 */       return true;
/*      */     }
/*      */     
/*      */     public List<P8SearchDefinition.Clause> getClauses() {
/*  919 */       return this.clauses;
/*      */     }
/*      */     
/*      */     public void setClauses(List<P8SearchDefinition.Clause> clauses) {
/*  923 */       this.clauses = clauses;
/*  924 */       resetItemsCache();
/*      */     }
/*      */     
/*      */     public void addClause(P8SearchDefinition.Clause clause) {
/*  928 */       this.clauses.add(clause);
/*  929 */       clause.setContainer(this);
/*  930 */       resetItemsCache();
/*      */     }
/*      */     
/*      */     public void replaceClause(P8SearchDefinition.Clause oldClause, P8SearchDefinition.Clause newClause) {
/*  934 */       for (int i = 0; i < this.clauses.size(); i++) {
/*  935 */         P8SearchDefinition.Clause clause = (P8SearchDefinition.Clause)this.clauses.get(i);
/*  936 */         String itemId = clause.getItemId();
/*  937 */         if ((clause.equals(oldClause)) || ((itemId != null) && (itemId.equals(oldClause.getItemId())))) {
/*  938 */           if (newClause == null) {
/*  939 */             this.clauses.remove(clause); break;
/*      */           }
/*  941 */           this.clauses.set(i, newClause);
/*  942 */           break;
/*      */         }
/*      */       }
/*  945 */       if (newClause != null)
/*  946 */         newClause.setContainer(this);
/*  947 */       resetItemsCache();
/*      */     }
/*      */     
/*      */     private void resetItemsCache() {
/*  951 */       P8SearchDefinition.SearchClause sc = getSearchClause();
/*  952 */       if (sc != null)
/*  953 */         P8SearchDefinition.SearchClause.access$100(sc);
/*      */     }
/*      */     
/*      */     public boolean isInclude() {
/*  957 */       return this.include;
/*      */     }
/*      */     
/*      */     public void setInclude(boolean include) {
/*  961 */       this.include = include;
/*      */     }
/*      */     
/*      */     public boolean isRange() {
/*  965 */       return this.range;
/*      */     }
/*      */     
/*      */     public void setRange(boolean range) {
/*  969 */       this.range = range;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  973 */       StringBuilder str = new StringBuilder();
/*  974 */       str.append("(join = ").append(getJoin());
/*  975 */       str.append(", include = ").append(this.include);
/*  976 */       str.append(", range = ").append(this.range);
/*  977 */       str.append(", ").append(P8SearchDefinition.NEW_LINE).append("    whereClauses = ").append(this.clauses).append(")");
/*      */       
/*  979 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class WhereClauseCondition extends P8SearchDefinition.ClauseItem {
/*      */     private P8SearchDefinition.Operator operator;
/*      */     private String propSymbolicName;
/*      */     private String propName;
/*      */     private SearchTemplateBase.ObjectType propObjectType;
/*      */     private P8SearchDefinition.DataType propDataType;
/*      */     private boolean propHasChoices;
/*      */     private String propChoiceId;
/*      */     private boolean propHasMarkings;
/*      */     private String literal;
/*      */     private String label;
/*  994 */     private boolean valid = true;
/*      */     private P8SearchDefinition.Operator smartOperator;
/*      */     
/*      */     public WhereClauseCondition() {}
/*      */     
/*      */     public WhereClauseCondition(String operator, String propSymbolicName, String propName, String propObjectType, String propDataType, boolean propHasChoices, String propChoiceId, boolean propHasMarkings, String editProperty, String literal)
/*      */     {
/* 1001 */       super();
/* 1002 */       this.operator = (operator == null ? null : P8SearchDefinition.Operator.valueOf(operator));
/* 1003 */       this.propSymbolicName = propSymbolicName;
/* 1004 */       this.propName = propName;
/* 1005 */       this.propObjectType = (propObjectType == null ? null : SearchTemplateBase.ObjectType.valueOf(propObjectType));
/* 1006 */       this.propDataType = (propDataType == null ? null : P8SearchDefinition.DataType.valueOf(propDataType));
/* 1007 */       this.propHasChoices = propHasChoices;
/* 1008 */       this.propChoiceId = propChoiceId;
/* 1009 */       this.propHasMarkings = propHasMarkings;
/* 1010 */       this.literal = literal;
/*      */     }
/*      */     
/*      */     public WhereClauseCondition(P8SearchDefinition.Operator operator, String propSymbolicName, String propName, SearchTemplateBase.ObjectType propObjectType, P8SearchDefinition.DataType propDataType, boolean propHasChoices, String propChoiceId, boolean propHasMarkings, SearchTemplate.SearchEditProperty editProperty, String literal) {
/* 1014 */       super();
/* 1015 */       this.operator = operator;
/* 1016 */       this.propSymbolicName = propSymbolicName;
/* 1017 */       this.propName = propName;
/* 1018 */       this.propObjectType = propObjectType;
/* 1019 */       this.propDataType = propDataType;
/* 1020 */       this.propHasChoices = propHasChoices;
/* 1021 */       this.propChoiceId = propChoiceId;
/* 1022 */       this.propHasMarkings = propHasMarkings;
/* 1023 */       this.literal = literal;
/*      */     }
/*      */     
/*      */     public boolean isContainer() {
/* 1027 */       return false;
/*      */     }
/*      */     
/*      */     public String getLiteral() {
/* 1031 */       return this.literal;
/*      */     }
/*      */     
/*      */     public void setLiteral(String literal) {
/* 1035 */       this.literal = literal;
/*      */     }
/*      */     
/*      */     public String getLabel() {
/* 1039 */       return this.label;
/*      */     }
/*      */     
/*      */     public void setLabel(String label) {
/* 1043 */       this.label = label;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.Operator getOperator() {
/* 1047 */       return this.operator;
/*      */     }
/*      */     
/*      */     public void setOperator(String operator) {
/* 1051 */       this.operator = (operator == null ? null : P8SearchDefinition.Operator.valueOf(operator));
/*      */     }
/*      */     
/*      */     public void setOperator(P8SearchDefinition.Operator operator) {
/* 1055 */       this.operator = operator;
/*      */     }
/*      */     
/*      */     public boolean isOperatorNullOrNotNull() {
/* 1059 */       return (this.operator != null) && ((this.operator == P8SearchDefinition.Operator.isnull) || (this.operator == P8SearchDefinition.Operator.isnotnull));
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.DataType getPropDataType() {
/* 1063 */       return this.propDataType;
/*      */     }
/*      */     
/*      */     public void setPropDataType(String propDataType) {
/* 1067 */       this.propDataType = (propDataType == null ? null : P8SearchDefinition.DataType.valueOf(propDataType));
/*      */     }
/*      */     
/*      */     public boolean isPropHasChoices() {
/* 1071 */       return this.propHasChoices;
/*      */     }
/*      */     
/*      */     public void setPropHasChoices(boolean propHasChoices) {
/* 1075 */       this.propHasChoices = propHasChoices;
/*      */     }
/*      */     
/*      */     public String getPropChoiceId() {
/* 1079 */       return this.propChoiceId;
/*      */     }
/*      */     
/*      */     public void setPropChoiceId(String propChoiceId) {
/* 1083 */       this.propChoiceId = propChoiceId;
/*      */     }
/*      */     
/*      */     public boolean isPropHasMarkings() {
/* 1087 */       return this.propHasMarkings;
/*      */     }
/*      */     
/*      */     public void setPropHasMarkings(boolean propHasMarkings) {
/* 1091 */       this.propHasMarkings = propHasMarkings;
/*      */     }
/*      */     
/*      */     public String getPropName() {
/* 1095 */       return this.propName;
/*      */     }
/*      */     
/*      */     public void setPropName(String propName) {
/* 1099 */       this.propName = propName;
/*      */     }
/*      */     
/*      */     public SearchTemplateBase.ObjectType getPropObjectType() {
/* 1103 */       return this.propObjectType;
/*      */     }
/*      */     
/*      */     public void setPropObjectType(String propObjectType) {
/* 1107 */       this.propObjectType = (propObjectType == null ? null : SearchTemplateBase.ObjectType.valueOf(propObjectType));
/*      */     }
/*      */     
/*      */     public String getPropSymbolicName() {
/* 1111 */       return this.propSymbolicName;
/*      */     }
/*      */     
/*      */     public void setPropSymbolicName(String propSymbolicName) {
/* 1115 */       this.propSymbolicName = propSymbolicName;
/*      */     }
/*      */     
/*      */     public boolean isValid() {
/* 1119 */       return this.valid;
/*      */     }
/*      */     
/*      */     public void setValid(boolean valid) {
/* 1123 */       this.valid = valid;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.Operator getSmartOperator() {
/* 1127 */       return this.smartOperator;
/*      */     }
/*      */     
/*      */     public void setSmartOperator(String smartOperator) {
/* 1131 */       this.smartOperator = (smartOperator == null ? null : P8SearchDefinition.Operator.valueOf(smartOperator));
/*      */     }
/*      */     
/*      */     public int getDatabaseType() {
/* 1135 */       P8SearchDefinition.SearchClause sc = getSearchClause();
/* 1136 */       P8SearchDefinition sd = sc == null ? null : sc.getSearchDefinition();
/*      */       
/* 1138 */       return sd == null ? 0 : sd.getDatabaseType();
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1143 */       StringBuilder str = new StringBuilder();
/* 1144 */       str.append("(operator = ").append(this.operator);
/* 1145 */       str.append(", propSymbolicName = ").append(this.propSymbolicName);
/* 1146 */       str.append(", propName = ").append(this.propName);
/* 1147 */       str.append(", propObjectType = ").append(this.propObjectType);
/* 1148 */       str.append(", propDataType = ").append(this.propDataType);
/* 1149 */       str.append(", propHasChoices = ").append(this.propHasChoices);
/* 1150 */       str.append(", propChoiceId = ").append(this.propChoiceId);
/* 1151 */       str.append(", propHasMarkings = ").append(this.propHasMarkings);
/* 1152 */       str.append(", editProperty = ").append(getEditProperty());
/* 1153 */       str.append(", literal = ").append(this.literal);
/* 1154 */       str.append(", itemId = ").append(getItemId());
/* 1155 */       str.append(", valid = ").append(this.valid).append(")");
/*      */       
/* 1157 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ContentCriteria
/*      */   {
/*      */     private String dialect;
/*      */     private P8SearchDefinition.JoinType join;
/*      */     private boolean showContentSummary;
/*      */     private boolean showRank;
/*      */     private P8SearchDefinition.Clause contentClause;
/*      */     private P8SearchDefinition.SearchClause searchClause;
/*      */     private SearchTemplate.TextSearchType textSearchType;
/*      */     
/*      */     public ContentCriteria() {}
/*      */     
/*      */     public ContentCriteria(String dialect, String join, boolean showContentSummary, boolean showRank, SearchTemplate.TextSearchType textSearchType) {
/* 1174 */       this.dialect = dialect;
/* 1175 */       this.join = (join == null ? null : P8SearchDefinition.JoinType.valueOf(join));
/* 1176 */       this.showContentSummary = showContentSummary;
/* 1177 */       this.showRank = showRank;
/* 1178 */       this.textSearchType = textSearchType;
/*      */     }
/*      */     
/*      */     public String getDialect() {
/* 1182 */       return this.dialect;
/*      */     }
/*      */     
/*      */     public void setDialect(String dialect) {
/* 1186 */       this.dialect = dialect;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.JoinType getJoin() {
/* 1190 */       return this.join;
/*      */     }
/*      */     
/*      */     public void setJoin(String join) {
/* 1194 */       this.join = (join == null ? null : P8SearchDefinition.JoinType.valueOf(join));
/*      */     }
/*      */     
/*      */     public void setJoin(P8SearchDefinition.JoinType join) {
/* 1198 */       this.join = join;
/*      */     }
/*      */     
/*      */     public boolean isShowContentSummary() {
/* 1202 */       return this.showContentSummary;
/*      */     }
/*      */     
/*      */     public void setShowContentSummary(boolean showContentSummary) {
/* 1206 */       this.showContentSummary = showContentSummary;
/*      */     }
/*      */     
/*      */     public void setShowContentSummary(String showContentSummary) {
/* 1210 */       this.showContentSummary = Boolean.valueOf(showContentSummary).booleanValue();
/*      */     }
/*      */     
/*      */     public boolean isShowRank() {
/* 1214 */       return this.showRank;
/*      */     }
/*      */     
/*      */     public void setShowRank(boolean showRank) {
/* 1218 */       this.showRank = showRank;
/*      */     }
/*      */     
/*      */     public void setShowRank(String showRank) {
/* 1222 */       this.showRank = Boolean.valueOf(showRank).booleanValue();
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.Clause getContentClause() {
/* 1226 */       return this.contentClause;
/*      */     }
/*      */     
/*      */     public void setContentClause(P8SearchDefinition.Clause contentClause) {
/* 1230 */       this.contentClause = contentClause;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.SearchClause getSearchClause() {
/* 1234 */       return this.searchClause;
/*      */     }
/*      */     
/*      */     public void setSearchClause(P8SearchDefinition.SearchClause searchClause) {
/* 1238 */       this.searchClause = searchClause;
/*      */     }
/*      */     
/*      */     public boolean isCascade() {
/* 1242 */       return (this.textSearchType == null) || (this.textSearchType == SearchTemplate.TextSearchType.cascade);
/*      */     }
/*      */     
/*      */     public SearchTemplate.TextSearchType getTextSearchType() {
/* 1246 */       return this.textSearchType == null ? SearchTemplate.TextSearchType.cascade : this.textSearchType;
/*      */     }
/*      */     
/*      */     public void setTextSearchType(SearchTemplate.TextSearchType textSearchType) {
/* 1250 */       this.textSearchType = textSearchType;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1255 */       StringBuilder str = new StringBuilder();
/* 1256 */       str.append("(dialect = ").append(this.dialect);
/* 1257 */       str.append(", join = ").append(this.join);
/* 1258 */       str.append(", showContentSummary = ").append(this.showContentSummary);
/* 1259 */       str.append(", showRank = ").append(this.showRank);
/* 1260 */       str.append(", contentClause = ").append(this.contentClause);
/* 1261 */       str.append(", textSearchType = ").append(this.textSearchType == null ? "" : this.textSearchType.toString()).append(")");
/*      */       
/* 1263 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ContentClauseItem extends P8SearchDefinition.ClauseItem {
/*      */     private P8SearchDefinition.GroupAction groupAction;
/*      */     private P8SearchDefinition.SearchModifier searchModifier;
/*      */     private int searchModifierRange;
/*      */     private P8SearchDefinition.RequiredState requiredState;
/*      */     private String userText;
/* 1273 */     private List<P8SearchDefinition.ContentTerm> contentTerms = new ArrayList();
/*      */     private String propertySymbolicName;
/*      */     
/*      */     public ContentClauseItem() {
/* 1277 */       this.userText = "";
/*      */     }
/*      */     
/*      */     public ContentClauseItem(String groupAction, String userText, String editProperty) {
/* 1281 */       super();
/* 1282 */       this.groupAction = ((groupAction == null) || (groupAction.length() < 1) ? P8SearchDefinition.GroupAction.none : P8SearchDefinition.GroupAction.valueOf(groupAction));
/* 1283 */       this.userText = (userText == null ? "" : userText);
/*      */     }
/*      */     
/*      */     public boolean isContainer() {
/* 1287 */       return false;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.GroupAction getGroupAction() {
/* 1291 */       return this.groupAction == null ? P8SearchDefinition.GroupAction.none : this.groupAction;
/*      */     }
/*      */     
/*      */     public void setGroupAction(String groupAction) {
/* 1295 */       this.groupAction = ((groupAction == null) || (groupAction.length() < 1) ? P8SearchDefinition.GroupAction.none : P8SearchDefinition.GroupAction.valueOf(groupAction));
/*      */     }
/*      */     
/*      */     public void setGroupAction(P8SearchDefinition.GroupAction groupAction) {
/* 1299 */       this.groupAction = (groupAction == null ? P8SearchDefinition.GroupAction.none : groupAction);
/*      */     }
/*      */     
/*      */     public boolean isGroupActionAll() {
/* 1303 */       return (this.groupAction != null) && (this.groupAction == P8SearchDefinition.GroupAction.all);
/*      */     }
/*      */     
/*      */     public boolean isGroupActionAny() {
/* 1307 */       return (this.groupAction != null) && (this.groupAction == P8SearchDefinition.GroupAction.any);
/*      */     }
/*      */     
/*      */     public boolean isGroupActionVql() {
/* 1311 */       return (this.groupAction != null) && (this.groupAction == P8SearchDefinition.GroupAction.vql);
/*      */     }
/*      */     
/*      */     public boolean isGroupActionNear() {
/* 1315 */       return (this.groupAction != null) && (this.groupAction == P8SearchDefinition.GroupAction.near);
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.SearchModifier getSearchModifier() {
/* 1319 */       return this.searchModifier;
/*      */     }
/*      */     
/*      */     public void setSearchModifier(String searchModifier) {
/* 1323 */       this.searchModifier = (searchModifier == null ? null : P8SearchDefinition.SearchModifier.valueOf(searchModifier));
/*      */     }
/*      */     
/*      */     public void setSearchModifier(P8SearchDefinition.SearchModifier searchModifier) {
/* 1327 */       this.searchModifier = searchModifier;
/*      */     }
/*      */     
/*      */     public boolean isSearchModifierProximity() {
/* 1331 */       return (this.searchModifier != null) && (this.searchModifier.equals(P8SearchDefinition.SearchModifier.proximity));
/*      */     }
/*      */     
/*      */     public int getSearchModifierRange() {
/* 1335 */       return this.searchModifierRange;
/*      */     }
/*      */     
/*      */     public void setSearchModifierRange(int searchModifierRange) {
/* 1339 */       this.searchModifierRange = searchModifierRange;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.RequiredState getRequiredState() {
/* 1343 */       return this.requiredState == null ? P8SearchDefinition.RequiredState.none : this.requiredState;
/*      */     }
/*      */     
/*      */     public void setRequiredState(String requiredState) {
/* 1347 */       this.requiredState = ((requiredState == null) || (requiredState.length() < 1) ? P8SearchDefinition.RequiredState.none : P8SearchDefinition.RequiredState.valueOf(requiredState));
/*      */     }
/*      */     
/*      */     public void setRequiredState(P8SearchDefinition.RequiredState requiredState) {
/* 1351 */       this.requiredState = (requiredState == null ? P8SearchDefinition.RequiredState.none : requiredState);
/*      */     }
/*      */     
/*      */     public boolean isRequiredStateProhibited() {
/* 1355 */       return (this.requiredState != null) && (this.requiredState.equals(P8SearchDefinition.RequiredState.prohibited));
/*      */     }
/*      */     
/*      */     public List<P8SearchDefinition.ContentTerm> getContentTerms() {
/* 1359 */       return this.contentTerms;
/*      */     }
/*      */     
/*      */     public void setContentTerms(List<P8SearchDefinition.ContentTerm> contentTerms) {
/* 1363 */       this.contentTerms = contentTerms;
/*      */     }
/*      */     
/*      */     public void addContentTerm(P8SearchDefinition.ContentTerm contentTerm) {
/* 1367 */       this.contentTerms.add(contentTerm);
/*      */     }
/*      */     
/*      */     public String getUserText() {
/* 1371 */       return this.userText == null ? "" : this.userText;
/*      */     }
/*      */     
/*      */     public void setUserText(String userText) {
/* 1375 */       this.userText = (userText == null ? "" : userText);
/*      */     }
/*      */     
/*      */     public String getPropertySymbolicName() {
/* 1379 */       return this.propertySymbolicName;
/*      */     }
/*      */     
/*      */     public void setPropertySymbolicName(String propertySymbolicName) {
/* 1383 */       this.propertySymbolicName = propertySymbolicName;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1388 */       StringBuilder str = new StringBuilder();
/* 1389 */       str.append("(groupAction = ").append(this.groupAction);
/* 1390 */       str.append(", searchModifier = ").append(this.searchModifier);
/* 1391 */       str.append(", searchModifierRange = ").append(this.searchModifierRange);
/* 1392 */       str.append(", requiredState = ").append(this.requiredState);
/* 1393 */       str.append(", userText = ").append(this.userText);
/* 1394 */       str.append(", contentTerms = ").append(this.contentTerms);
/* 1395 */       str.append(", itemId = ").append(getItemId());
/* 1396 */       str.append(", editProperty = ").append(getEditProperty());
/* 1397 */       str.append(", propertySymbolicName = ").append(this.propertySymbolicName).append(")");
/*      */       
/* 1399 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ContentTerm {
/*      */     private String value;
/*      */     private P8SearchDefinition.VeritySearchOperator wordVariation;
/*      */     private boolean caseSensitive;
/*      */     private P8SearchDefinition.RequiredState requiredState;
/*      */     private boolean phrase;
/*      */     
/*      */     public ContentTerm() {
/* 1411 */       this.value = "";
/*      */     }
/*      */     
/*      */     public ContentTerm(String value, String wordVariation, boolean caseSensitive, P8SearchDefinition.RequiredState requiredState, boolean phrase) {
/* 1415 */       setValue(value);
/* 1416 */       setWordVariation(wordVariation);
/* 1417 */       setCaseSensitive(caseSensitive);
/* 1418 */       setRequiredState(requiredState);
/* 1419 */       setPhrase(phrase);
/*      */     }
/*      */     
/*      */     public String getValue() {
/* 1423 */       return this.value == null ? "" : this.value;
/*      */     }
/*      */     
/*      */     public void setValue(String value) {
/* 1427 */       this.value = (value == null ? "" : value);
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.VeritySearchOperator getWordVariation() {
/* 1431 */       return this.wordVariation;
/*      */     }
/*      */     
/*      */     public void setWordVariation(String wordVariation) {
/* 1435 */       this.wordVariation = (wordVariation == null ? null : P8SearchDefinition.VeritySearchOperator.valueOf(wordVariation));
/*      */     }
/*      */     
/*      */     public boolean isCaseSensitive() {
/* 1439 */       return this.caseSensitive;
/*      */     }
/*      */     
/*      */     public void setCaseSensitive(boolean caseSensitive) {
/* 1443 */       this.caseSensitive = caseSensitive;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.RequiredState getRequiredState() {
/* 1447 */       return this.requiredState == null ? P8SearchDefinition.RequiredState.none : this.requiredState;
/*      */     }
/*      */     
/*      */     public void setRequiredState(P8SearchDefinition.RequiredState requiredState) {
/* 1451 */       this.requiredState = (requiredState == null ? P8SearchDefinition.RequiredState.none : requiredState);
/*      */     }
/*      */     
/*      */     public boolean isRequired() {
/* 1455 */       return (this.requiredState != null) && (this.requiredState == P8SearchDefinition.RequiredState.required);
/*      */     }
/*      */     
/*      */     public boolean isProhibited() {
/* 1459 */       return (this.requiredState != null) && (this.requiredState == P8SearchDefinition.RequiredState.prohibited);
/*      */     }
/*      */     
/*      */     public boolean isPhrase() {
/* 1463 */       return this.phrase;
/*      */     }
/*      */     
/*      */     public void setPhrase(boolean phrase) {
/* 1467 */       this.phrase = phrase;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1472 */       StringBuilder str = new StringBuilder();
/* 1473 */       str.append("(value = ").append(this.value);
/* 1474 */       str.append(", wordVariation = ").append(this.wordVariation);
/* 1475 */       str.append(", caseSensitive = ").append(this.caseSensitive);
/* 1476 */       str.append(", requiredState = ").append(this.requiredState);
/* 1477 */       str.append(", phrase = ").append(this.phrase).append(")");
/*      */       
/* 1479 */       return str.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class VerityClauseInItem extends P8SearchDefinition.ContentClauseItem
/*      */   {
/*      */     private String zone;
/*      */     
/*      */     public VerityClauseInItem() {}
/*      */     
/*      */     public VerityClauseInItem(String zone) {
/* 1490 */       this.zone = zone;
/*      */     }
/*      */     
/*      */     public String getZone() {
/* 1494 */       return this.zone;
/*      */     }
/*      */     
/*      */     public void setZone(String zone) {
/* 1498 */       this.zone = zone;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.GroupAction getGroupAction() {
/* 1502 */       return P8SearchDefinition.GroupAction.in;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1507 */       return "(zone = " + this.zone + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   public static class VerityClauseVQLItem extends P8SearchDefinition.ContentClauseItem
/*      */   {
/*      */     private String vql;
/*      */     
/*      */     public VerityClauseVQLItem() {}
/*      */     
/*      */     public VerityClauseVQLItem(String vql) {
/* 1518 */       this.vql = vql;
/*      */     }
/*      */     
/*      */     public String getVql() {
/* 1522 */       return this.vql;
/*      */     }
/*      */     
/*      */     public void setVql(String vql) {
/* 1526 */       this.vql = vql;
/*      */     }
/*      */     
/*      */     public P8SearchDefinition.GroupAction getGroupAction() {
/* 1530 */       return P8SearchDefinition.GroupAction.vql;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1535 */       return "(vql = " + this.vql + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Subclass extends SearchTemplate.SearchClass
/*      */   {
/*      */     private boolean unselected;
/*      */     
/*      */     public Subclass() {}
/*      */     
/*      */     public Subclass(String symbolicName, String displayName, String objectType, String editProperty, String itemId, String searchSubclasses) {
/* 1546 */       super(displayName, objectType == null ? null : SearchTemplateBase.ObjectType.valueOf(objectType), searchSubclasses == null ? false : Boolean.valueOf(searchSubclasses).booleanValue(), editProperty == null ? null : SearchTemplate.SearchEditProperty.valueOf(editProperty), itemId);
/*      */     }
/*      */     
/*      */     public Subclass(String symbolicName, String displayName, SearchTemplateBase.ObjectType objectType, SearchTemplate.SearchEditProperty editProperty, String itemId, boolean searchSubclasses) {
/* 1550 */       super(displayName, objectType, searchSubclasses, editProperty, itemId);
/*      */     }
/*      */     
/*      */     public boolean isUnselected() {
/* 1554 */       return this.unselected;
/*      */     }
/*      */     
/*      */     public void setUnselected(boolean unselected) {
/* 1558 */       this.unselected = unselected;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1563 */       StringBuilder str = new StringBuilder();
/* 1564 */       str.append("(symbolicName = ").append(getName());
/* 1565 */       str.append(", displayName = ").append(getDisplayName());
/* 1566 */       str.append(", objectType = ").append(getObjectType());
/* 1567 */       str.append(", editProperty = ").append(getEditProperty());
/* 1568 */       str.append(", itemId = ").append(getItemId());
/* 1569 */       str.append(", unselected = ").append(this.unselected);
/* 1570 */       str.append(", searchSubclasses = ").append(isSearchSubclasses()).append(")");
/*      */       
/* 1572 */       return str.toString();
/*      */     }
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8SearchDefinition.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */