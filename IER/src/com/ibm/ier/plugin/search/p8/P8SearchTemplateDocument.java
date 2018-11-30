/*      */ package com.ibm.ier.plugin.search.p8;
/*      */ 
/*      */ import com.filenet.api.collection.AccessPermissionList;
/*      */ import com.filenet.api.collection.ContentElementList;
/*      */ import com.filenet.api.constants.Cardinality;
/*      */ import com.filenet.api.constants.DatabaseType;
/*      */ import com.filenet.api.core.ContentElement;
/*      */ import com.filenet.api.core.ContentTransfer;
/*      */ import com.filenet.api.core.Document;
/*      */ import com.filenet.api.core.Factory.ContentElement;
/*      */ import com.filenet.api.core.Factory.ContentTransfer;
/*      */ import com.filenet.api.core.Factory.User;
/*      */ import com.filenet.api.core.Folder;
/*      */ import com.filenet.api.core.ObjectReference;
/*      */ import com.filenet.api.core.ObjectStore;
/*      */ import com.filenet.api.exception.EngineRuntimeException;
/*      */ import com.filenet.api.exception.ExceptionCode;
/*      */ import com.filenet.api.meta.ClassDescription;
/*      */ import com.filenet.api.meta.PropertyDescription;
/*      */ import com.filenet.api.property.Properties;
/*      */ import com.filenet.api.property.PropertyFilter;
/*      */ import com.filenet.api.security.User;
/*      */ import com.filenet.api.util.Id;
/*      */ import com.ibm.ecm.configuration.Config;
/*      */ import com.ibm.ecm.configuration.FileTypeConfig;
/*      */ import com.ibm.ecm.configuration.RepositoryConfig;
/*      */ import com.ibm.ecm.configuration.SettingsConfig;
/*      */ import com.ibm.ecm.configuration.exception.MissingValueException;
/*      */ import com.ibm.ecm.serviceability.Logger;
/*      */ import com.ibm.ier.plugin.search.util.SearchCriteria;
/*      */ import com.ibm.ier.plugin.search.util.SearchCriterion;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.Macros;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchClass;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchEditProperty;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.SearchFolder;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchCriterion;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.TextSearchType;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplate.UserAction;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ObjectType;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.ResultsDisplay;
/*      */ import com.ibm.ier.plugin.search.util.SearchTemplateBase.VersionOption;
/*      */ import com.ibm.ier.plugin.search.util.WCDateFormat;
/*      */ import com.ibm.json.java.JSON;
/*      */ import com.ibm.json.java.JSONObject;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import org.apache.commons.configuration.ConfigurationException;
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
/*      */ public class P8SearchTemplateDocument
/*      */   extends P8SearchTemplateDocumentBase
/*      */ {
/*      */   private P8SearchDefinition searchDefinition;
/*      */   public static final String TEAMSPACE_PARENT_FOLDER = "/ClbTeamspaces";
/*      */   public static final String TEAMSPACE_PROP_NAME = "ClbTeamspaceName";
/*      */   
/*      */   public P8SearchTemplateDocument(HttpServletRequest request, P8Connection connection, String storedSearchId, String storedSearchVsId, String teamspaceId)
/*      */   {
/*  110 */     super(request, connection, storedSearchId, storedSearchVsId, teamspaceId);
/*      */   }
/*      */   
/*      */   P8SearchTemplateDocument(HttpServletRequest request, P8Connection connection, P8SearchDefinition searchDefinition, P8SearchTemplate searchTemplate) {
/*  114 */     super(request, connection, null, null, null);
/*  115 */     this.request = request;
/*  116 */     this.connection = connection;
/*  117 */     this.searchDefinition = searchDefinition;
/*  118 */     this.searchTemplate = searchTemplate;
/*      */   }
/*      */   
/*      */   public Document save(String name, String description, JSONObject searchTemplateJson) throws Exception {
/*  122 */     return save(name, description, searchTemplateJson, false);
/*      */   }
/*      */   
/*      */   public Document saveAs(String templateName, String description, JSONObject searchTemplateJson, String folderPID, AccessPermissionList permissions) throws Exception {
/*  126 */     return saveAs(templateName, description, searchTemplateJson, folderPID, permissions, false);
/*      */   }
/*      */   
/*      */   public Document getSourceDocument() {
/*  130 */     return this.document;
/*      */   }
/*      */   
/*      */   public SearchTemplateBase getSearchTemplate(JSONObject searchTemplateJson, boolean loadSearchDefinitionOrValidate, boolean autoResolve) throws Exception {
/*  134 */     this.searchDefinition = new P8SearchDefinition();
/*  135 */     this.searchDefinition.setApiIncompatible(true);
/*  136 */     this.searchTemplate = new P8SearchTemplate(this.searchDefinition);
/*  137 */     P8SearchTemplate p8Template = (P8SearchTemplate)this.searchTemplate;
/*      */     
/*  139 */     if (isSearchTemplateIdSet()) {
/*  140 */       loadSearchDefinitionDocument(loadSearchDefinitionOrValidate, false);
/*  141 */       if (loadSearchDefinitionOrValidate)
/*  142 */         loadSearchDefinition(autoResolve);
/*  143 */       p8Template.setDisplayName(this.document.get_Name());
/*  144 */       Properties props = this.document.getProperties();
/*  145 */       Boolean autoRun = Boolean.valueOf(false);
/*  146 */       if (props.isPropertyPresent("IcnAutoRun"))
/*  147 */         autoRun = props.getBooleanValue("IcnAutoRun");
/*  148 */       p8Template.setAutoRun(autoRun != null ? autoRun.booleanValue() : false);
/*  149 */       Boolean showInTree = Boolean.valueOf(false);
/*  150 */       if (props.isPropertyPresent("IcnShowInTree"))
/*  151 */         showInTree = props.getBooleanValue("IcnShowInTree");
/*  152 */       p8Template.setShowInTree(showInTree != null ? showInTree.booleanValue() : false);
/*  153 */       p8Template.setAutoResolved(this.searchDefinition.isAutoResolved());
/*  154 */       p8Template.setInvalidRepository(this.searchDefinition.isInvalidRepository());
/*  155 */       p8Template.setInvalidClass(this.searchDefinition.isInvalidClass());
/*  156 */       p8Template.setInvalidFolder(this.searchDefinition.isInvalidFolder());
/*  157 */       p8Template.setInvalidFileType(this.searchDefinition.isInvalidFileType());
/*  158 */       p8Template.setInvalidProperty(this.searchDefinition.isInvalidProperty());
/*  159 */       p8Template.setInvalidTextSearch(this.searchDefinition.isInvalidTextSearch());
/*      */     }
/*      */     List<PropertyDescription> propertyDescriptions;
/*  162 */     if ((searchTemplateJson != null) && (!searchTemplateJson.isEmpty())) {
/*  163 */       p8Template.fromJSON(searchTemplateJson);
/*  164 */       List<P8SearchDefinition.SearchInObjectStore> objectStores = p8Template.getObjectStores();
/*  165 */       if ((isSearchTemplateIdSet()) && (loadSearchDefinitionOrValidate)) {
/*  166 */         if ((objectStores == null) || (objectStores.size() < 1))
/*  167 */           objectStores = convertP8SearchDefinitionObjectStores();
/*  168 */         P8SearchDefinition.SearchClause clause = this.searchDefinition.getSearchClause(p8Template.getObjectType());
/*  169 */         P8SearchDefinition.ContentCriteria criteria = clause == null ? null : clause.getContentCriteria();
/*  170 */         String osId = (objectStores != null) && (objectStores.size() > 0) ? ((P8SearchDefinition.SearchInObjectStore)objectStores.get(0)).getId() : null;
/*  171 */         SearchTemplate.TextSearchType searchType = criteria == null ? P8TextSearchUtil.getTextSearchType(this.connection, osId, this.request) : criteria.getTextSearchType();
/*  172 */         p8Template.setTextSearchType(searchType);
/*      */       } else {
/*  174 */         if (loadSearchDefinitionOrValidate)
/*  175 */           validateSearchTemplate(autoResolve);
/*  176 */         String osId = (objectStores == null) || (objectStores.size() < 1) ? addDefaultObjectStore().getId() : ((P8SearchDefinition.SearchInObjectStore)objectStores.get(0)).getId();
/*  177 */         SearchTemplate.TextSearchType searchType = P8TextSearchUtil.getTextSearchType(this.connection, osId, this.request);
/*  178 */         p8Template.setTextSearchType(searchType);
/*  179 */         this.searchDefinition.setDatabaseType(this.connection.getObjectStore().get_DatabaseType().getValue());
/*      */       }
/*  181 */     } else if (isSearchTemplateIdSet()) {
/*  182 */       convertToP8SearchTemplate();
/*  183 */       normalizeResultsDisplay();
/*  184 */       if (p8Template.getTextSearchType() == null) {
/*  185 */         String osId = ((P8SearchDefinition.SearchInObjectStore)p8Template.getObjectStores().get(0)).getId();
/*  186 */         SearchTemplate.TextSearchType textSearchType = P8TextSearchUtil.getTextSearchType(this.connection, osId, this.request);
/*  187 */         p8Template.setTextSearchType(textSearchType);
/*      */       }
/*  189 */       P8SearchDefinition.SearchInObjectStore searchObjStore = (P8SearchDefinition.SearchInObjectStore)p8Template.getObjectStores().get(0);
/*  190 */       ObjectStore objStore = P8Util.getObjectStore(this.connection, searchObjStore.getId());
/*  191 */       ClassDescription classDesc = P8Util.getCachedCD(objStore, p8Template.getFirstClassName());
/*  192 */       propertyDescriptions = P8Util.getPropertyDescriptions(classDesc, p8Template.isFirstClassSearchSubclasses(), false, true, false, true);
/*  193 */       for (SearchCriteria criteria : p8Template.getSearchCriteria()) {
/*  194 */         criterion = (SearchCriterion)criteria;
/*  195 */         for (PropertyDescription propertyDesc : propertyDescriptions) {
/*  196 */           if (propertyDesc.get_SymbolicName().equals(criterion.getName())) {
/*  197 */             criterion.setDataType(P8Util.getDataType(propertyDesc));
/*  198 */             criterion.setCardinality(propertyDesc.get_Cardinality().toString());
/*  199 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     SearchCriterion criterion;
/*  205 */     if (this.searchDefinition != null) {
/*  206 */       p8Template.setSearchDefinition(this.searchDefinition);
/*  207 */       p8Template.setProductName(this.searchDefinition.getProductName());
/*      */     }
/*      */     
/*  210 */     return this.searchTemplate;
/*      */   }
/*      */   
/*      */   private P8SearchDefinition.SearchInObjectStore addDefaultObjectStore() {
/*  214 */     ObjectStore os = this.connection.getObjectStore();
/*  215 */     String osId = os.getObjectReference().getObjectIdentity();
/*  216 */     P8SearchDefinition.SearchInObjectStore searchOs = new P8SearchDefinition.SearchInObjectStore(osId, os.get_DisplayName());
/*  217 */     ((P8SearchTemplate)this.searchTemplate).addObjectStore(searchOs);
/*      */     
/*  219 */     return searchOs;
/*      */   }
/*      */   
/*      */   private void normalizeResultsDisplay() {
/*  223 */     RepositoryConfig repositoryConfig = Config.getRepositoryConfig(this.request);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  229 */     String[] columns = ((P8SearchTemplate)this.searchTemplate).getResultsDisplay().getColumns();
/*      */     
/*  231 */     if (columns.length == 0) {
/*  232 */       columns = repositoryConfig.getSearchDefaultColumns();
/*      */     }
/*      */     
/*  235 */     String[] magazineColumns = ((P8SearchTemplate)this.searchTemplate).getResultsDisplay().getMagazineColumns();
/*      */     
/*  237 */     if ((magazineColumns == null) || (magazineColumns.length == 0)) {
/*  238 */       magazineColumns = repositoryConfig.getSearchMagazineDefaultColumns();
/*      */     }
/*      */     
/*  241 */     String sortBy = ((P8SearchTemplate)this.searchTemplate).getResultsDisplay().getSortByProperty();
/*  242 */     boolean sortAsc = ((P8SearchTemplate)this.searchTemplate).getResultsDisplay().getSortAscending();
/*      */     String[] filteredProperties;
/*      */     String nameProperty;
/*      */     String[] filteredProperties;
/*  246 */     if (((P8SearchTemplate)this.searchTemplate).getObjectType() == SearchTemplateBase.ObjectType.document) {
/*  247 */       String nameProperty = repositoryConfig.getDocNameProperty();
/*  248 */       filteredProperties = repositoryConfig.getSearchFilteredDocumentProperties();
/*      */     } else {
/*  250 */       nameProperty = repositoryConfig.getFolderNameProperty();
/*  251 */       filteredProperties = repositoryConfig.getSearchFilteredFolderProperties();
/*      */     }
/*  253 */     Set<String> filteredSet = new HashSet(Arrays.asList(filteredProperties));
/*      */     
/*      */ 
/*  256 */     List<String> normalizedList = new ArrayList();
/*  257 */     for (String selectProp : columns) {
/*  258 */       if (selectProp.equals("{NAME}"))
/*  259 */         selectProp = nameProperty;
/*  260 */       if ((!filteredSet.contains(selectProp)) || (selectProp.equals(nameProperty)))
/*      */       {
/*  262 */         normalizedList.add(selectProp);
/*      */       }
/*  264 */       else if (selectProp.equals(sortBy)) {
/*  265 */         sortBy = nameProperty;
/*  266 */         sortAsc = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  276 */     String[] normalizedColumns = new String[normalizedList.size()];
/*  277 */     SearchTemplateBase.ResultsDisplay rd = new SearchTemplateBase.ResultsDisplay((sortBy != null) && (sortBy.length() > 0) ? sortBy : nameProperty, sortAsc, (String[])normalizedList.toArray(normalizedColumns));
/*      */     
/*      */ 
/*  280 */     List<String> normalizedMagazineList = new ArrayList();
/*  281 */     for (String selectProp : magazineColumns) {
/*  282 */       if (selectProp.equals("{NAME}"))
/*  283 */         selectProp = nameProperty;
/*  284 */       if ((!filteredSet.contains(selectProp)) || (selectProp.equals(nameProperty))) {
/*  285 */         normalizedMagazineList.add(selectProp);
/*      */       }
/*      */     }
/*      */     
/*  289 */     int size = normalizedMagazineList.size();
/*  290 */     if (((size == 0) || (!((String)normalizedMagazineList.get(0)).equals(nameProperty))) && 
/*  291 */       (size == 0)) {
/*  292 */       if (normalizedList.size() > 1) {
/*  293 */         normalizedMagazineList.add(normalizedList.get(1));
/*      */       }
/*  295 */       if (normalizedList.size() > 2) {
/*  296 */         normalizedMagazineList.add(normalizedList.get(2));
/*      */       }
/*  298 */       if (normalizedList.size() > 3) {
/*  299 */         normalizedMagazineList.add(normalizedList.get(3));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  305 */     String[] normalizedMagazineColumns = new String[normalizedMagazineList.size()];
/*  306 */     rd.setMagazineColumns((String[])normalizedMagazineList.toArray(normalizedMagazineColumns));
/*      */     
/*  308 */     ((P8SearchTemplate)this.searchTemplate).setResultsDisplay(rd);
/*      */   }
/*      */   
/*      */   private void loadSearchDefinition(boolean autoResolve) throws Exception {
/*  312 */     String methodName = "loadSearchDefinition";
/*  313 */     Logger.logDebug(P8SearchTemplateDocument.class, methodName, this.request, "loading search definition document: " + this.document.getObjectReference());
/*  314 */     ContentElementList contentElements = this.document.get_ContentElements();
/*  315 */     ContentElement contentElement = (ContentElement)contentElements.get(0);
/*  316 */     ContentTransfer contentTransfer = (ContentTransfer)contentElement;
/*  317 */     InputStream contentStream = contentTransfer.accessContentStream();
/*  318 */     Logger.logDebug(P8SearchTemplateDocument.class, "loadSearchDefinition", this.request, "parsing search template and building criteria objects");
/*  319 */     this.searchDefinition = P8SearchDefinitionParser.parse(contentStream);
/*  320 */     if (contentElements.size() > 1) {
/*  321 */       for (int i = 1; i < contentElements.size(); i++) {
/*  322 */         contentElement = (ContentElement)contentElements.get(i);
/*  323 */         if ("application/json".equals(contentElement.get_ContentType())) {
/*  324 */           JSONObject json = (JSONObject)JSON.parse(((ContentTransfer)contentElement).accessContentStream());
/*  325 */           SearchTemplate.Macros macros = new SearchTemplate.Macros();
/*  326 */           macros.fromJSON((JSONObject)json.get("macros"));
/*  327 */           ((P8SearchTemplate)this.searchTemplate).setMacros(macros);
/*  328 */           break;
/*      */         }
/*      */       }
/*      */     }
/*  332 */     validateSearchDefinition(autoResolve);
/*      */     
/*      */ 
/*  335 */     String useCeApi = this.request.getParameter("searchViaCeApi");
/*  336 */     if ((useCeApi == null) || (useCeApi.isEmpty()) || (!Boolean.valueOf(useCeApi).booleanValue())) {
/*  337 */       this.searchDefinition.setApiIncompatible(true);
/*      */     }
/*  339 */     Logger.logDebug(P8SearchTemplateDocument.class, "loadSearchDefinition", this.request, "searchDefinition: " + this.searchDefinition);
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
/*      */   protected ContentElementList createContentElements()
/*      */     throws Exception
/*      */   {
/*  353 */     ContentElementList contentElementList = Factory.ContentElement.createList();
/*  354 */     ContentTransfer contentTransfer = Factory.ContentTransfer.createInstance();
/*  355 */     contentTransfer.set_ContentType("application/x-filenet-searchtemplate");
/*      */     
/*  357 */     byte[] templateBytes = convertToXml().getBytes("UTF-8");
/*  358 */     contentTransfer.setCaptureSource(new ByteArrayInputStream(templateBytes));
/*  359 */     contentElementList.add(contentTransfer);
/*      */     
/*  361 */     SearchTemplate.Macros macros = ((P8SearchTemplate)this.searchTemplate).getMacros();
/*  362 */     if (macros != null) {
/*  363 */       JSONObject json = new JSONObject();
/*  364 */       json.put("macros", macros.toJSON());
/*  365 */       contentTransfer = Factory.ContentTransfer.createInstance();
/*  366 */       contentTransfer.set_ContentType("application/json");
/*  367 */       templateBytes = json.toString().getBytes("UTF-8");
/*  368 */       contentTransfer.setCaptureSource(new ByteArrayInputStream(templateBytes));
/*  369 */       contentElementList.add(contentTransfer);
/*      */     }
/*      */     
/*  372 */     return contentElementList;
/*      */   }
/*      */   
/*      */   void convertToP8SearchTemplate() {
/*  376 */     List<P8SearchDefinition.SearchClause> searchClauses = this.searchDefinition.getSearchClauses();
/*  377 */     if (searchClauses.size() < 1)
/*  378 */       return;
/*  379 */     for (P8SearchDefinition.SearchClause clause : searchClauses) {
/*  380 */       if (clause.getFrom() == SearchTemplateBase.ObjectType.customobject)
/*  381 */         throw new RuntimeException("search.exception.customObjectSearch");
/*      */     }
/*  383 */     if (searchClauses.size() > 1) {
/*  384 */       throw new RuntimeException("search.exception.unsupportedSearch");
/*      */     }
/*  386 */     convertP8SearchDefinitionObjectStores();
/*  387 */     List<P8SearchDefinition.SearchInFolder> folders = this.searchDefinition.getFolders();
/*  388 */     for (P8SearchDefinition.SearchInFolder folder : folders)
/*  389 */       ((P8SearchTemplate)this.searchTemplate).addFolder(new SearchTemplate.SearchFolder(folder.getId(), folder.getPathName(), folder.isSearchSubfolders(), folder.getView(), folder.getObjectStoreId(), folder.getObjectStoreName(), folder.getItemId()));
/*  390 */     P8SearchDefinition.SearchClause searchClause = (P8SearchDefinition.SearchClause)this.searchDefinition.getSearchClauses().get(0);
/*  391 */     convertP8SearchDefinitionClass(searchClause);
/*  392 */     P8SearchDefinition.ContentCriteria contentCriteria = searchClause.getContentCriteria();
/*  393 */     boolean rank = false;
/*  394 */     if (contentCriteria != null) {
/*  395 */       ((P8SearchTemplate)this.searchTemplate).setTextSearchType(contentCriteria.getTextSearchType());
/*  396 */       convertP8SearchDefinitionContentCriteria(contentCriteria.getContentClause());
/*  397 */       rank = contentCriteria.isShowRank();
/*  398 */       ((P8SearchTemplate)this.searchTemplate).setTextSearchCriteriaLayout(createCriteriaLayout(contentCriteria.getContentClause()));
/*  399 */       ((P8SearchTemplate)this.searchTemplate).setPropertyTextAnded(contentCriteria.getJoin() == P8SearchDefinition.JoinType.inner);
/*      */     }
/*  401 */     P8SearchDefinition.SearchClause commonSearchClause = this.searchDefinition.getSearchClause(SearchTemplateBase.ObjectType.common);
/*  402 */     P8SearchDefinition.Clause commonClause = commonSearchClause == null ? null : commonSearchClause.getWhereClause();
/*  403 */     P8SearchDefinition.Clause whereClause = searchClause.getWhereClause();
/*  404 */     convertP8SearchDefinitionWhereClauseConditions(whereClause, commonClause);
/*  405 */     convertP8SearchDefinitionSelectProperties(searchClause.getSelectProperties(), rank);
/*  406 */     if (this.searchDefinition.isIcnGenerated())
/*  407 */       ((P8SearchTemplate)this.searchTemplate).setAndSearch(P8SearchUtil.isIcnMatchAllSearchClause(searchClause));
/*  408 */     ((P8SearchTemplate)this.searchTemplate).setVersionOption(this.searchDefinition.getVersionSelection());
/*  409 */     ((P8SearchTemplate)this.searchTemplate).setOperatorHidden(!this.searchDefinition.isShowOperators());
/*  410 */     ((P8SearchTemplate)this.searchTemplate).setCriteriaRelationshipHidden(!this.searchDefinition.isShowAndOrConditions());
/*      */   }
/*      */   
/*      */   private P8SearchTemplate.CriteriaLayout createCriteriaLayout(P8SearchDefinition.Clause clause) {
/*  414 */     if ((clause == null) || (!clause.isContainer())) {
/*  415 */       return null;
/*      */     }
/*  417 */     P8SearchDefinition.Container container = (P8SearchDefinition.Container)clause;
/*  418 */     P8SearchTemplate.CriteriaLayout layout = new P8SearchTemplate.CriteriaLayout(container.getJoin().toString());
/*  419 */     List<P8SearchDefinition.Clause> clauses = container.getClauses();
/*  420 */     for (P8SearchDefinition.Clause child : clauses) {
/*  421 */       populateCriteriaLayout(child, layout);
/*      */     }
/*  423 */     if (layout.isEmpty()) {
/*  424 */       layout = null;
/*      */     }
/*  426 */     return layout;
/*      */   }
/*      */   
/*      */   private void populateCriteriaLayout(P8SearchDefinition.Clause clause, P8SearchTemplate.CriteriaLayout parentLayout) {
/*  430 */     if (clause == null)
/*  431 */       return;
/*  432 */     if (clause.isContainer()) {
/*  433 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)clause;
/*  434 */       P8SearchDefinition.Container parent = clause.getContainer();
/*      */       P8SearchTemplate.CriteriaLayout layout;
/*  436 */       if (parent.getJoin() != container.getJoin()) {
/*  437 */         P8SearchTemplate.CriteriaLayout layout = new P8SearchTemplate.CriteriaLayout(container.getJoin().toString());
/*  438 */         parentLayout.addChildLayout(layout);
/*      */       } else {
/*  440 */         layout = parentLayout;
/*      */       }
/*  442 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/*  443 */       for (P8SearchDefinition.Clause child : clauses) {
/*  444 */         if (child.isContainer()) {
/*  445 */           populateCriteriaLayout(child, layout);
/*      */         } else {
/*  447 */           P8SearchDefinition.ClauseItem item = (P8SearchDefinition.ClauseItem)child;
/*  448 */           if (!item.isHidden())
/*  449 */             layout.addItemId(((P8SearchDefinition.ClauseItem)child).getItemId());
/*      */         }
/*      */       }
/*  452 */       if (layout != parentLayout) {
/*  453 */         if ((layout.getItemIdsSize() == 1) && (layout.getChildLayoutsSize() < 1)) {
/*  454 */           String itemId = (String)layout.getItemIds().get(0);
/*  455 */           parentLayout.addItemId(itemId);
/*  456 */           layout.removeItemId(itemId);
/*      */         }
/*  458 */         if (layout.isEmpty())
/*  459 */           parentLayout.removeChildLayout(layout);
/*      */       }
/*      */     } else {
/*  462 */       P8SearchDefinition.ClauseItem item = (P8SearchDefinition.ClauseItem)clause;
/*  463 */       if (!item.isHidden())
/*  464 */         parentLayout.addItemId(item.getItemId());
/*      */     }
/*      */   }
/*      */   
/*      */   private List<P8SearchDefinition.SearchInObjectStore> convertP8SearchDefinitionObjectStores() {
/*  469 */     List<P8SearchDefinition.SearchInObjectStore> objectStores = this.searchDefinition.getObjectStores();
/*  470 */     for (P8SearchDefinition.SearchInObjectStore objectStore : objectStores) {
/*  471 */       P8SearchDefinition.SearchInObjectStore os = new P8SearchDefinition.SearchInObjectStore(objectStore.getId(), objectStore.getName());
/*  472 */       os.setSymbolicName(objectStore.getSymbolicName());
/*  473 */       ((P8SearchTemplate)this.searchTemplate).addObjectStore(os);
/*      */     }
/*      */     
/*  476 */     return ((P8SearchTemplate)this.searchTemplate).getObjectStores();
/*      */   }
/*      */   
/*      */   private void convertP8SearchDefinitionClass(P8SearchDefinition.SearchClause searchClause) {
/*  480 */     SearchTemplateBase.ObjectType objectType = searchClause.getFrom();
/*  481 */     ((P8SearchTemplate)this.searchTemplate).setObjectType(objectType);
/*  482 */     List<P8SearchDefinition.Subclass> subclasses = searchClause.getSubclasses();
/*  483 */     for (P8SearchDefinition.Subclass subclass : subclasses) {
/*  484 */       SearchTemplate.SearchClass searchClass = new SearchTemplate.SearchClass(subclass.getName(), subclass.getDisplayName(), subclass.getObjectType(), subclass.isSearchSubclasses(), subclass.getEditProperty(), subclass.getItemId());
/*  485 */       ((P8SearchTemplate)this.searchTemplate).addClass(searchClass);
/*      */     }
/*      */   }
/*      */   
/*      */   private void convertP8SearchDefinitionWhereClauseConditions(P8SearchDefinition.Clause whereClause, P8SearchDefinition.Clause commonClause) {
/*  490 */     if (whereClause == null) {
/*  491 */       return;
/*      */     }
/*  493 */     if (whereClause.isContainer()) {
/*  494 */       if (whereClause == P8SearchDefinition.COMMON_CLAUSE) {
/*  495 */         convertP8SearchDefinitionWhereClauseConditions(commonClause, null);
/*      */       } else {
/*  497 */         P8SearchDefinition.Container container = (P8SearchDefinition.Container)whereClause;
/*  498 */         List<P8SearchDefinition.Clause> whereClauses = container.getClauses();
/*  499 */         if (P8SearchUtil.isWhereClauseContainerIncludeCondition(container))
/*      */         {
/*  501 */           List<P8SearchDefinition.WhereClauseCondition> conditions = collectWhereClauseConditions(container);
/*  502 */           Iterator<P8SearchDefinition.WhereClauseCondition> i = conditions.iterator();
/*  503 */           String[] values = new String[conditions.size()];
/*  504 */           String[] displayValues = new String[conditions.size()];
/*  505 */           P8SearchDefinition.WhereClauseCondition firstCondition = null;
/*  506 */           int count = 0;
/*  507 */           String itemIds = "";
/*  508 */           while (i.hasNext()) {
/*  509 */             P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)i.next();
/*  510 */             String value = condition.getLiteral();
/*  511 */             String displayValue = condition.getLabel();
/*  512 */             values[count] = value;
/*  513 */             displayValues[count] = (displayValue != null ? displayValue : value);
/*  514 */             if (firstCondition == null)
/*  515 */               firstCondition = condition;
/*  516 */             if (!itemIds.isEmpty())
/*  517 */               itemIds = itemIds + ",";
/*  518 */             itemIds = itemIds + condition.getItemId();
/*  519 */             count++;
/*      */           }
/*      */           
/*  522 */           SearchCriterion criterion = new SearchCriterion();
/*  523 */           criterion.setName(firstCondition.getPropSymbolicName());
/*  524 */           criterion.setDisplayName(firstCondition.getPropName());
/*  525 */           P8SearchDefinition.Operator operator = firstCondition.getSmartOperator();
/*  526 */           if (operator == null) {
/*  527 */             if (container.getJoin() == P8SearchDefinition.Operator.or) {
/*  528 */               operator = P8SearchDefinition.Operator.inany;
/*      */             } else {
/*  530 */               operator = firstCondition.getOperator() == P8SearchDefinition.Operator.neq ? P8SearchDefinition.Operator.notin : P8SearchDefinition.Operator.in;
/*      */             }
/*      */           }
/*  533 */           criterion.setOperator(P8SearchUtil.convertP8SearchDefinitionOperator(operator));
/*  534 */           criterion.setReadOnly(firstCondition.isReadOnly());
/*  535 */           criterion.setHidden(firstCondition.isHidden());
/*  536 */           criterion.setRequired(firstCondition.isRequired());
/*  537 */           criterion.setDataType(P8SearchUtil.convertP8SearchDefinitionDataType(firstCondition.getPropDataType()));
/*  538 */           criterion.setValues(values);
/*  539 */           criterion.setDisplayValues(displayValues);
/*  540 */           criterion.setItemId(itemIds);
/*  541 */           ((P8SearchTemplate)this.searchTemplate).addCriterion(criterion);
/*  542 */         } else if (container.isRange()) {
/*  543 */           P8SearchDefinition.WhereClauseCondition fromCondition = (P8SearchDefinition.WhereClauseCondition)whereClauses.get(0);
/*  544 */           P8SearchDefinition.WhereClauseCondition toCondition = (P8SearchDefinition.WhereClauseCondition)whereClauses.get(1);
/*  545 */           SearchCriterion criterion = new SearchCriterion();
/*  546 */           criterion.setName(fromCondition.getPropSymbolicName());
/*  547 */           criterion.setDisplayName(fromCondition.getPropName());
/*  548 */           P8SearchDefinition.Operator operator = container.getJoin() == P8SearchDefinition.Operator.and ? P8SearchDefinition.Operator.between : P8SearchDefinition.Operator.notbetween;
/*  549 */           criterion.setOperator(P8SearchUtil.convertP8SearchDefinitionOperator(operator));
/*  550 */           criterion.setReadOnly(fromCondition.isReadOnly());
/*  551 */           criterion.setHidden(fromCondition.isHidden());
/*  552 */           criterion.setRequired(fromCondition.isRequired());
/*  553 */           criterion.setDataType(P8SearchUtil.convertP8SearchDefinitionDataType(fromCondition.getPropDataType()));
/*  554 */           criterion.setValues(new String[] { fromCondition.getLiteral(), toCondition.getLiteral() });
/*  555 */           criterion.setItemId(fromCondition.getItemId() + "," + toCondition.getItemId());
/*  556 */           ((P8SearchTemplate)this.searchTemplate).addCriterion(criterion);
/*      */         }
/*      */         else {
/*  559 */           for (P8SearchDefinition.Clause wc : whereClauses)
/*  560 */             convertP8SearchDefinitionWhereClauseConditions(wc, commonClause);
/*      */         }
/*      */       }
/*      */     } else {
/*  564 */       P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)whereClause;
/*  565 */       SearchCriterion criterion = new SearchCriterion();
/*  566 */       criterion.setName(condition.getPropSymbolicName());
/*  567 */       criterion.setDisplayName(condition.getPropName());
/*  568 */       criterion.setOperator(P8SearchUtil.convertP8SearchDefinitionOperator(condition.getSmartOperator() != null ? condition.getSmartOperator() : condition.getOperator()));
/*  569 */       criterion.setReadOnly(condition.isReadOnly());
/*  570 */       criterion.setHidden(condition.isHidden());
/*  571 */       criterion.setRequired(condition.isRequired());
/*  572 */       criterion.setDataType(P8SearchUtil.convertP8SearchDefinitionDataType(condition.getPropDataType()));
/*  573 */       criterion.setValues(new String[] { condition.getLiteral() });
/*  574 */       criterion.setDisplayValues(new String[] { condition.getLabel() });
/*  575 */       criterion.setItemId(condition.getItemId());
/*  576 */       ((P8SearchTemplate)this.searchTemplate).addCriterion(criterion);
/*      */     }
/*      */   }
/*      */   
/*      */   private List<P8SearchDefinition.WhereClauseCondition> collectWhereClauseConditions(P8SearchDefinition.Container container) {
/*  581 */     List<P8SearchDefinition.WhereClauseCondition> conditions = new ArrayList();
/*  582 */     List<P8SearchDefinition.Clause> clauses = container.getClauses();
/*  583 */     Iterator<P8SearchDefinition.Clause> i = clauses.iterator();
/*  584 */     while (i.hasNext()) {
/*  585 */       P8SearchDefinition.Clause clause = (P8SearchDefinition.Clause)i.next();
/*  586 */       if (clause.isContainer()) {
/*  587 */         conditions.addAll(collectWhereClauseConditions((P8SearchDefinition.Container)clause));
/*      */       } else {
/*  589 */         conditions.add((P8SearchDefinition.WhereClauseCondition)clause);
/*      */       }
/*      */     }
/*  592 */     return conditions;
/*      */   }
/*      */   
/*      */   private void convertP8SearchDefinitionSelectProperties(List<P8SearchDefinition.SelectProperty> selectProperties, boolean rank) {
/*  596 */     List<String> columnList = new ArrayList();
/*  597 */     String sortBy = null;
/*  598 */     boolean sortAsc = true;
/*  599 */     if ((selectProperties != null) && (selectProperties.size() > 0))
/*      */     {
/*  601 */       for (P8SearchDefinition.SelectProperty selectProp : selectProperties) {
/*  602 */         if (selectProp != P8SearchDefinition.SELECT_PROPERTY_ALL)
/*      */         {
/*      */ 
/*  605 */           columnList.add(selectProp.getSymbolicName());
/*  606 */           if (selectProp.getSortLevel() == 1) {
/*  607 */             sortBy = selectProp.getSymbolicName();
/*  608 */             sortAsc = !selectProp.isSortOrderDescending();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  613 */     if (rank) {
/*  614 */       sortBy = "Rank";
/*  615 */       sortAsc = false;
/*      */     }
/*      */     
/*  618 */     String[] columns = new String[columnList.size()];
/*  619 */     SearchTemplateBase.ResultsDisplay rd = new SearchTemplateBase.ResultsDisplay(sortBy, sortAsc, (String[])columnList.toArray(columns));
/*  620 */     ((P8SearchTemplate)this.searchTemplate).setResultsDisplay(rd);
/*      */   }
/*      */   
/*      */   private void convertP8SearchDefinitionContentCriteria(P8SearchDefinition.Clause contentClause) {
/*  624 */     if (contentClause == null) {
/*  625 */       return;
/*      */     }
/*  627 */     if (this.searchDefinition.isIcnGenerated()) {
/*  628 */       Collection<P8SearchDefinition.ClauseItem> items = contentClause.getSearchClause().retrieveContentClauseItems().values();
/*  629 */       boolean contentClauseDefined = false;
/*  630 */       boolean propClauseDefined = false;
/*  631 */       for (P8SearchDefinition.ClauseItem i : items) {
/*  632 */         P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)i;
/*  633 */         String propName = item.getPropertySymbolicName();
/*  634 */         if ((!contentClauseDefined) && ((propName == null) || (propName.length() < 1)))
/*  635 */           contentClauseDefined = true;
/*  636 */         if ((!propClauseDefined) && (propName != null) && (propName.length() > 0))
/*  637 */           propClauseDefined = true;
/*      */       }
/*  639 */       if (contentClauseDefined) { SearchTemplate.TextSearchCriterion criterion;
/*      */         SearchTemplate.TextSearchCriterion criterion;
/*  641 */         if (propClauseDefined) {
/*  642 */           criterion = getTextSearchCriterion((P8SearchDefinition.Clause)((P8SearchDefinition.Container)contentClause).getClauses().get(0));
/*      */         } else {
/*  644 */           criterion = getTextSearchCriterion(contentClause);
/*      */         }
/*  646 */         ((P8SearchTemplate)this.searchTemplate).addTextSearchCriterion(criterion);
/*      */       }
/*  648 */       if (propClauseDefined) {
/*  649 */         if (contentClauseDefined) {
/*  650 */           convertSearchDefinitionPropertyContentCriteria((P8SearchDefinition.Clause)((P8SearchDefinition.Container)contentClause).getClauses().get(1));
/*      */         } else {
/*  652 */           convertSearchDefinitionPropertyContentCriteria(contentClause);
/*      */         }
/*      */       }
/*      */     } else {
/*  656 */       convertLegacySearchDefinitionContentCriteria(contentClause);
/*      */     }
/*      */   }
/*      */   
/*      */   private SearchTemplate.TextSearchCriterion getTextSearchCriterion(P8SearchDefinition.Clause contentClause) {
/*  661 */     if (contentClause == null) {
/*  662 */       return null;
/*      */     }
/*  664 */     String text = getText(contentClause);
/*  665 */     SearchTemplate.TextSearchCriterion criterion = new SearchTemplate.TextSearchCriterion();
/*  666 */     criterion.setText(text);
/*  667 */     if (contentClause.isContainer()) {
/*  668 */       criterion.setOperator("");
/*      */     } else {
/*  670 */       P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)contentClause;
/*  671 */       P8SearchDefinition.GroupAction groupAction = item.getGroupAction();
/*  672 */       String op = groupAction == P8SearchDefinition.GroupAction.none ? "" : item.isSearchModifierProximity() ? P8SearchDefinition.GroupAction.near.toString() : item.getGroupAction().toString();
/*  673 */       if (groupAction == P8SearchDefinition.GroupAction.in)
/*  674 */         op = op + ":" + ((P8SearchDefinition.VerityClauseInItem)item).getZone();
/*  675 */       criterion.setOperator(op);
/*  676 */       criterion.setDistance(item.isSearchModifierProximity() ? item.getSearchModifierRange() : 0);
/*  677 */       criterion.setEditProperty(item.getEditProperty());
/*  678 */       criterion.setItemId(item.getItemId());
/*      */     }
/*      */     
/*  681 */     return criterion;
/*      */   }
/*      */   
/*      */   private void convertSearchDefinitionPropertyContentCriteria(P8SearchDefinition.Clause contentClause) {
/*  685 */     if (contentClause == null) {
/*  686 */       return;
/*      */     }
/*  688 */     if (contentClause.isContainer()) {
/*  689 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)contentClause;
/*  690 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/*  691 */       if (clauses.size() == 2) {
/*  692 */         convertSearchDefinitionPropertyContentCriterion((P8SearchDefinition.Clause)clauses.get(0));
/*  693 */         convertSearchDefinitionPropertyContentCriteria((P8SearchDefinition.Clause)clauses.get(1));
/*      */       } else {
/*  695 */         for (P8SearchDefinition.Clause clause : clauses)
/*  696 */           convertSearchDefinitionPropertyContentCriteria(clause);
/*      */       }
/*      */     } else {
/*  699 */       convertSearchDefinitionPropertyContentCriterion(contentClause);
/*      */     }
/*      */   }
/*      */   
/*      */   private void convertSearchDefinitionPropertyContentCriterion(P8SearchDefinition.Clause contentClause) {
/*  704 */     if (contentClause == null) {
/*  705 */       return;
/*      */     }
/*  707 */     SearchTemplate.TextSearchCriterion criterion = getTextSearchCriterion(contentClause);
/*  708 */     if (criterion != null) {
/*  709 */       SearchCriterion sc = new SearchCriterion();
/*  710 */       if (contentClause.isContainer()) {
/*  711 */         sc.setName(retrievePropertySymbolicName(contentClause));
/*      */       } else {
/*  713 */         sc.setItemId(((P8SearchDefinition.ContentClauseItem)contentClause).getItemId());
/*  714 */         sc.setName(((P8SearchDefinition.ContentClauseItem)contentClause).getPropertySymbolicName());
/*      */       }
/*  716 */       sc.setOperator(P8SearchUtil.convertP8SearchDefinitionOperator(P8SearchDefinition.Operator.contains));
/*  717 */       sc.setValues(new String[] { criterion.getText() });
/*  718 */       ((P8SearchTemplate)this.searchTemplate).addCriterion(sc);
/*      */     }
/*      */   }
/*      */   
/*      */   private String retrievePropertySymbolicName(P8SearchDefinition.Clause contentClause) {
/*  723 */     if (contentClause == null) {
/*  724 */       return null;
/*      */     }
/*  726 */     String propName = null;
/*  727 */     if (contentClause.isContainer()) {
/*  728 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)contentClause;
/*  729 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/*  730 */       for (P8SearchDefinition.Clause clause : clauses) {
/*  731 */         propName = retrievePropertySymbolicName(clause);
/*  732 */         if ((propName != null) && (propName.length() > 0))
/*      */           break;
/*      */       }
/*      */     } else {
/*  736 */       return ((P8SearchDefinition.ContentClauseItem)contentClause).getPropertySymbolicName();
/*      */     }
/*      */     
/*  739 */     return propName;
/*      */   }
/*      */   
/*      */   private void convertLegacySearchDefinitionContentCriteria(P8SearchDefinition.Clause contentClause) {
/*  743 */     if (contentClause == null) {
/*  744 */       return;
/*      */     }
/*  746 */     if (contentClause.isContainer()) {
/*  747 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)contentClause;
/*  748 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/*  749 */       for (P8SearchDefinition.Clause clause : clauses)
/*  750 */         convertLegacySearchDefinitionContentCriteria(clause);
/*      */     } else {
/*  752 */       SearchTemplate.TextSearchCriterion criterion = getTextSearchCriterion(contentClause);
/*  753 */       if (criterion != null)
/*  754 */         ((P8SearchTemplate)this.searchTemplate).addTextSearchCriterion(criterion);
/*      */     }
/*      */   }
/*      */   
/*      */   private String getText(P8SearchDefinition.Clause cascadeClause) {
/*  759 */     if (cascadeClause == null) {
/*  760 */       return null;
/*      */     }
/*  762 */     StringBuilder text = new StringBuilder();
/*  763 */     P8SearchDefinition.RequiredState required; if (cascadeClause.isContainer()) {
/*  764 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)cascadeClause;
/*  765 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/*  766 */       for (P8SearchDefinition.Clause clause : clauses) {
/*  767 */         String query = getText(clause);
/*  768 */         if ((query != null) && (query.length() != 0))
/*      */         {
/*  770 */           if (text.length() > 0)
/*  771 */             text.append(" ");
/*  772 */           text.append(query);
/*      */         }
/*      */       }
/*  775 */     } else { P8SearchDefinition.ContentClauseItem item = (P8SearchDefinition.ContentClauseItem)cascadeClause;
/*  776 */       P8SearchDefinition.GroupAction groupAction = item.getGroupAction();
/*  777 */       if (groupAction == P8SearchDefinition.GroupAction.vql) {
/*  778 */         String value = ((P8SearchDefinition.VerityClauseVQLItem)item).getVql();
/*  779 */         if ((value == null) || (value.length() < 1))
/*  780 */           return null;
/*  781 */         text.append(value);
/*      */       } else {
/*  783 */         List<P8SearchDefinition.ContentTerm> terms = item.getContentTerms();
/*  784 */         if ((terms == null) || (terms.size() < 1)) {
/*  785 */           return null;
/*      */         }
/*  787 */         required = item.getRequiredState();
/*  788 */         for (P8SearchDefinition.ContentTerm term : terms) {
/*  789 */           if (text.length() > 0)
/*  790 */             text.append(" ");
/*  791 */           String value = term.getValue();
/*  792 */           if (term.isPhrase())
/*  793 */             value = "\"" + value + "\"";
/*  794 */           if (required == P8SearchDefinition.RequiredState.prohibited)
/*  795 */             value = "-" + value;
/*  796 */           text.append(value);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  801 */     return text.toString();
/*      */   }
/*      */   
/*      */   private String convertToXml() throws Exception {
/*  805 */     StringBuilder xml = new StringBuilder();
/*  806 */     xml.append("<?xml version=\"1.0\"?>");
/*  807 */     xml.append("<storedsearch xmlns=\"http://filenet.com/namespaces/wcm/apps/1.0\">");
/*  808 */     xml.append("<version dtd=\"3.0\" searchobject=\"3\" />");
/*  809 */     xml.append("<product name=\"").append("Navigator").append("\" version=\"").append("2.0.0").append("1.0\" />");
/*  810 */     SearchTemplateBase.VersionOption versionOption = ((P8SearchTemplate)this.searchTemplate).getVersionOption();
/*  811 */     if (versionOption == null)
/*  812 */       versionOption = SearchTemplateBase.VersionOption.none;
/*  813 */     xml.append("<searchspec versionselection=\"").append(versionOption).append("\">");
/*  814 */     xml.append("<searchtype>");
/*  815 */     xml.append("<template showandorconditions=\"true\" showmaxrecords=\"false\" showoperators=\"true\" />");
/*  816 */     xml.append("</searchtype>");
/*      */     
/*  818 */     xml.append("<objectstores mergeoption=\"union\">");
/*  819 */     List<P8SearchDefinition.SearchInObjectStore> objectStores = ((P8SearchTemplate)this.searchTemplate).getObjectStores();
/*  820 */     for (P8SearchDefinition.SearchInObjectStore objectStore : objectStores) {
/*  821 */       xml.append("<objectstore id=\"").append(objectStore.getId()).append("\"").append(" name=\"").append(objectStore.getName()).append("\" />");
/*      */     }
/*  823 */     xml.append("</objectstores>");
/*      */     
/*  825 */     int itemId = 1;
/*  826 */     xml.append("<searchcriteria>");
/*  827 */     xml.append("<folders>");
/*  828 */     List<SearchTemplate.SearchFolder> folders = ((P8SearchTemplate)this.searchTemplate).getFolders();
/*  829 */     if (folders != null) {
/*  830 */       for (SearchTemplate.SearchFolder folder : folders) {
/*  831 */         xml.append("<folder id=\"").append(folder.getId()).append("\" itemid=\"").append(itemId++).append("\" pathname=\"").append(folder.getPathName()).append("\" searchsubfolders=\"").append(folder.isSearchSubfolders()).append("\" view=\"editable\">");
/*  832 */         xml.append("<objectstore id=\"").append(folder.getObjectStoreId()).append("\" />");
/*  833 */         xml.append("</folder>");
/*      */       }
/*      */     }
/*  836 */     xml.append("</folders>");
/*      */     
/*  838 */     SearchTemplateBase.ObjectType objectType = ((P8SearchTemplate)this.searchTemplate).getObjectType();
/*  839 */     xml.append("<searchclauses>");
/*  840 */     xml.append("<searchclause join=\"").append(((P8SearchTemplate)this.searchTemplate).isAndSearch() ? "and" : "or").append("\">");
/*      */     
/*  842 */     xml.append("<select>");
/*      */     
/*  844 */     xml.append("<selectprops>");
/*  845 */     SearchTemplateBase.ResultsDisplay resultsDisplay = ((P8SearchTemplate)this.searchTemplate).getResultsDisplay();
/*  846 */     String sortProperty = null;
/*  847 */     if (resultsDisplay != null) {
/*  848 */       sortProperty = resultsDisplay.getSortByProperty();
/*  849 */       for (String name : resultsDisplay.getColumns()) {
/*  850 */         if (name.equalsIgnoreCase(sortProperty)) {
/*  851 */           xml.append("<selectprop alignment=\"left\" itemid=\"").append(itemId++).append("\" name=\"").append(name).append("\" objecttype=\"").append(objectType).append("\" sortlevel=\"1\" sortorder=\"").append(resultsDisplay.getSortOrder()).append("\" symname=\"").append(name).append("\" />");
/*      */         } else {
/*  853 */           xml.append("<selectprop alignment=\"left\" itemid=\"").append(itemId++).append("\" name=\"").append(name).append("\" objecttype=\"").append(objectType).append("\" sortlevel=\"0\" sortorder=\"none\" symname=\"").append(name).append("\" />");
/*      */         }
/*      */       }
/*      */     }
/*  857 */     xml.append("</selectprops>");
/*  858 */     xml.append("</select>");
/*      */     
/*  860 */     xml.append("<from>");
/*  861 */     xml.append("<class symname=\"").append(objectType).append("\" />");
/*  862 */     xml.append("</from>");
/*      */     
/*  864 */     xml.append("<where>");
/*  865 */     List<SearchCriterion> searchCriteria = ((P8SearchTemplate)this.searchTemplate).getPropertySearchCriteria();
/*  866 */     Iterator<SearchCriterion> itr = searchCriteria.iterator();
/*  867 */     int size = searchCriteria.size();
/*  868 */     int opened = 0;
/*  869 */     for (int i = 0; itr.hasNext(); i++) {
/*  870 */       if (i + 1 < size) {
/*  871 */         xml.append(((P8SearchTemplate)this.searchTemplate).isAndSearch() ? "<and>" : "<or>");
/*  872 */         opened++;
/*      */       }
/*      */       
/*  875 */       SearchCriterion criterion = (SearchCriterion)itr.next();
/*  876 */       String xmlOperator = P8SearchUtil.convertP8SearchTemplateOperator(criterion.getOperator()).toString();
/*  877 */       boolean between = xmlOperator.equals(P8SearchDefinition.Operator.between.toString());
/*  878 */       if ((between) || (xmlOperator.equals(P8SearchDefinition.Operator.notbetween.toString()))) {
/*  879 */         xml.append("<").append(between ? "and" : "or").append(">");
/*  880 */         String[] values = criterion.getValues();
/*  881 */         SearchCriterion from = new SearchCriterion();
/*  882 */         from.setName(criterion.getName());
/*  883 */         from.setOperator(between ? "GREATEROREQUAL" : "LESS");
/*  884 */         from.setDataType(criterion.getDataType());
/*  885 */         if ((values != null) && (values.length > 0) && (values[0] != null))
/*  886 */           from.setValues(new String[] { values[0] });
/*  887 */         convertSearchCriterionToXml(xml, from, objectType, itemId++);
/*  888 */         SearchCriterion to = new SearchCriterion();
/*  889 */         to.setName(criterion.getName());
/*  890 */         to.setOperator(between ? "LESSOREQUAL" : "GREATER");
/*  891 */         to.setDataType(criterion.getDataType());
/*  892 */         if ((values != null) && (values.length > 1) && (values[1] != null))
/*  893 */           to.setValues(new String[] { values[1] });
/*  894 */         convertSearchCriterionToXml(xml, to, objectType, itemId++);
/*  895 */         xml.append("</").append(between ? "and" : "or").append(">");
/*  896 */       } else if ((xmlOperator.equals(P8SearchDefinition.Operator.in.toString())) || (xmlOperator.equals(P8SearchDefinition.Operator.inany.toString())) || (xmlOperator.equals(P8SearchDefinition.Operator.notin.toString())))
/*      */       {
/*  898 */         String join = xmlOperator.equals(P8SearchDefinition.Operator.inany.toString()) ? "or" : "and";
/*  899 */         String name = criterion.getName();
/*      */         
/*  901 */         String customAttributes = "smartoperator=\"" + xmlOperator + "\"";
/*  902 */         String operator; String operator; if (criterion.getCardinality().equals("SINGLE")) {
/*  903 */           operator = xmlOperator.equals(P8SearchDefinition.Operator.notin.toString()) ? "NOTEQUAL" : "EQUAL";
/*      */         } else {
/*  905 */           operator = "IN";
/*      */         }
/*  907 */         String dataType = criterion.getDataType();
/*  908 */         String[] values = criterion.getValues();
/*      */         
/*  910 */         if ((values != null) && (values.length > 0)) {
/*  911 */           int length = values.length;
/*  912 */           int tagCount = 0;
/*  913 */           for (int j = 0; j < length; j++) {
/*  914 */             if (j + 1 < length) {
/*  915 */               xml.append("<").append(join).append(">");
/*  916 */               tagCount++;
/*      */             }
/*  918 */             SearchCriterion c = new SearchCriterion();
/*  919 */             c.setName(name);
/*  920 */             c.setOperator(operator);
/*  921 */             c.setDataType(dataType);
/*  922 */             c.setValues(new String[] { values[j] });
/*  923 */             convertSearchCriterionToXml(xml, c, objectType, itemId++, customAttributes);
/*      */           }
/*  925 */           for (int j = 0; j < tagCount; j++)
/*  926 */             xml.append("</").append(join).append(">");
/*      */         } else {
/*  928 */           xml.append("<").append(join).append(">");
/*  929 */           SearchCriterion c = new SearchCriterion();
/*  930 */           c.setName(name);
/*  931 */           c.setOperator(operator);
/*  932 */           c.setDataType(dataType);
/*  933 */           convertSearchCriterionToXml(xml, c, objectType, itemId++, customAttributes);
/*  934 */           xml.append("</").append(join).append(">");
/*      */         }
/*      */       } else {
/*  937 */         String customAttributes = null;
/*  938 */         if ((xmlOperator.equals(P8SearchDefinition.Operator.like.toString())) || (xmlOperator.equals(P8SearchDefinition.Operator.notlike.toString())) || (xmlOperator.equals(P8SearchDefinition.Operator.startswith.toString())) || (xmlOperator.equals(P8SearchDefinition.Operator.endswith.toString()))) {
/*  939 */           String[] values = criterion.getValues();
/*  940 */           String value = (values != null) && (values.length > 0) ? values[0] : null;
/*  941 */           value = P8SearchUtil.convertLikeValue(value, P8SearchDefinition.Operator.valueOf(xmlOperator));
/*  942 */           values = value != null ? new String[] { value } : null;
/*  943 */           criterion.setValues(values);
/*  944 */           if ((!xmlOperator.equals(P8SearchDefinition.Operator.like.toString())) && (!xmlOperator.equals(P8SearchDefinition.Operator.notlike.toString()))) {
/*  945 */             criterion.setOperator("LIKE");
/*  946 */             customAttributes = "smartoperator=\"" + xmlOperator + "\"";
/*      */           }
/*      */         }
/*  949 */         convertSearchCriterionToXml(xml, criterion, objectType, itemId++, customAttributes);
/*      */       }
/*      */     }
/*  952 */     for (int i = 0; i < opened; i++) {
/*  953 */       xml.append(((P8SearchTemplate)this.searchTemplate).isAndSearch() ? "</and>" : "</or>");
/*      */     }
/*  955 */     xml.append("</where>");
/*      */     
/*  957 */     xml.append("<subclasses>");
/*  958 */     List<SearchTemplate.SearchClass> classes = ((P8SearchTemplate)this.searchTemplate).getClasses();
/*  959 */     for (SearchTemplate.SearchClass sc : classes) {
/*  960 */       String classSymName = sc.getName();
/*  961 */       boolean rootClass = ((objectType == SearchTemplateBase.ObjectType.document) && (classSymName.equals("Document"))) || ((objectType == SearchTemplateBase.ObjectType.folder) && (classSymName.equals("Folder")));
/*  962 */       boolean searchSubclasses = sc.isSearchSubclasses();
/*  963 */       boolean skipSubclass = (rootClass) && (searchSubclasses);
/*  964 */       if (!skipSubclass)
/*  965 */         xml.append("<subclass editproperty=\"editable\" itemid=\"").append(itemId++).append("\" symname=\"").append(classSymName).append("\" name=\"").append(sc.getDisplayName()).append("\" objecttype=\"").append(objectType).append("\" includesubclasses=\"").append(searchSubclasses).append("\" />");
/*      */     }
/*  967 */     xml.append("</subclasses>");
/*      */     
/*  969 */     List<SearchTemplate.TextSearchCriterion> textCriteria = new ArrayList();
/*  970 */     SearchTemplate.TextSearchCriterion contentTextCriteria = ((P8SearchTemplate)this.searchTemplate).getFirstTextSearchCriterion();
/*  971 */     if (contentTextCriteria != null)
/*  972 */       textCriteria.add(contentTextCriteria);
/*  973 */     List<SearchCriterion> textSearchCriteria = ((P8SearchTemplate)this.searchTemplate).getPropertyTextSearchCriteria();
/*  974 */     if ((textSearchCriteria != null) && (!textSearchCriteria.isEmpty())) {
/*  975 */       for (SearchCriterion criterion : textSearchCriteria) {
/*  976 */         String[] values = criterion.getValues();
/*  977 */         String value = (values != null) && (values.length > 0) ? values[0] : null;
/*  978 */         SearchTemplate.TextSearchCriterion textCriterion = new SearchTemplate.TextSearchCriterion(value, P8SearchDefinition.GroupAction.none.toString(), 0);
/*  979 */         textCriteria.add(textCriterion);
/*      */       }
/*      */     }
/*  982 */     if (!textCriteria.isEmpty()) {
/*  983 */       boolean cascade = ((P8SearchTemplate)this.searchTemplate).isCascade();
/*  984 */       boolean rank = (sortProperty != null) && (sortProperty.equalsIgnoreCase("Rank"));
/*  985 */       if (cascade) {
/*  986 */         xml.append("<content dialect=\"lucene\" contentsummary=\"false\" jointype=\"inner\" rank=\"" + rank + "\">");
/*      */       } else {
/*  988 */         xml.append("<veritycontent contentsummary=\"false\" jointype=\"inner\" rank=\"" + rank + "\">");
/*      */       }
/*  990 */       Iterator<SearchTemplate.TextSearchCriterion> itrText = textCriteria.iterator();
/*  991 */       size = textCriteria.size();
/*  992 */       opened = 0;
/*  993 */       for (int i = 0; itrText.hasNext(); i++) {
/*  994 */         if (i + 1 < size) {
/*  995 */           xml.append(cascade ? "<and>" : "<verityand>");
/*  996 */           opened++;
/*      */         }
/*      */         
/*  999 */         SearchTemplate.TextSearchCriterion textCriterion = (SearchTemplate.TextSearchCriterion)itrText.next();
/* 1000 */         P8SearchDefinition.Clause contentClause = P8TextSearchUtil.createContentClause(textCriterion, cascade, null, false);
/* 1001 */         if ((!textSearchCriteria.isEmpty()) && ((contentTextCriteria == null) || (i > 0))) {
/* 1002 */           int pos = contentTextCriteria == null ? i : i - 1;
/* 1003 */           SearchCriterion criterion = (SearchCriterion)textSearchCriteria.get(pos);
/* 1004 */           populatePropertySymbolicName(contentClause, criterion == null ? null : criterion.getName());
/*      */         }
/* 1006 */         xml.append(createXml(contentClause, cascade, itemId++));
/*      */       }
/* 1008 */       for (int i = 0; i < opened; i++)
/* 1009 */         xml.append(cascade ? "</and>" : "</verityand>");
/* 1010 */       xml.append(cascade ? "</content>" : "</veritycontent>");
/*      */     }
/*      */     
/* 1013 */     xml.append("</searchclause>");
/* 1014 */     xml.append("</searchclauses>");
/* 1015 */     xml.append("</searchcriteria>");
/* 1016 */     xml.append("</searchspec>");
/* 1017 */     xml.append("</storedsearch>");
/*      */     
/* 1019 */     return xml.toString();
/*      */   }
/*      */   
/*      */   private String createXml(P8SearchDefinition.Clause contentClause, boolean cascade, int startItemId) throws IOException {
/* 1023 */     if (contentClause == null) {
/* 1024 */       return null;
/*      */     }
/* 1026 */     StringBuilder xml = new StringBuilder();
/* 1027 */     if (contentClause.isContainer()) {
/* 1028 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)contentClause;
/* 1029 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/* 1030 */       List<String> xmls = new ArrayList();
/* 1031 */       for (P8SearchDefinition.Clause clause : clauses) {
/* 1032 */         String childXml = createXml(clause, cascade, startItemId++);
/* 1033 */         if ((childXml != null) && (childXml.length() != 0))
/*      */         {
/* 1035 */           xmls.add(childXml); }
/*      */       }
/* 1037 */       P8SearchDefinition.Operator operator = container.getJoin();
/* 1038 */       boolean and = (operator != null) && (operator == P8SearchDefinition.Operator.and);
/* 1039 */       if (xmls.size() > 1) {
/* 1040 */         wrapContentConditions(xmls, and, cascade, xml);
/* 1041 */       } else if (xmls.size() == 1) {
/* 1042 */         xml.append((String)xmls.get(0));
/*      */       }
/* 1044 */     } else if (cascade) {
/* 1045 */       createCascadeContentItemXml((P8SearchDefinition.ContentClauseItem)contentClause, startItemId++, xml);
/*      */     } else {
/* 1047 */       createVerityContentItemXml((P8SearchDefinition.ContentClauseItem)contentClause, startItemId++, xml);
/*      */     }
/*      */     
/* 1050 */     return xml.toString();
/*      */   }
/*      */   
/*      */   private void wrapContentConditions(List<String> conditions, boolean and, boolean cascade, StringBuilder xml) {
/* 1054 */     String begin = and ? "<verityand>" : cascade ? "<or>" : and ? "<and>" : "<verityor>";
/* 1055 */     String end = and ? "</verityand>" : cascade ? "</or>" : and ? "</and>" : "</verityor>";
/* 1056 */     xml.append(begin);
/* 1057 */     for (String query : conditions)
/* 1058 */       xml.append(query);
/* 1059 */     xml.append(end);
/*      */   }
/*      */   
/*      */   private void createCascadeContentItemXml(P8SearchDefinition.ContentClauseItem item, int itemId, StringBuilder xml) {
/* 1063 */     List<P8SearchDefinition.ContentTerm> terms = item.getContentTerms();
/* 1064 */     if ((terms == null) || (terms.size() < 1)) {
/* 1065 */       return;
/*      */     }
/* 1067 */     P8SearchDefinition.GroupAction groupAction = item.getGroupAction();
/* 1068 */     String propName = item.getPropertySymbolicName();
/* 1069 */     if ((propName != null) && (propName.length() > 0))
/* 1070 */       xml.append("<field name=\"").append(propName).append("\">");
/* 1071 */     xml.append("<item editproperty=\"editable\" itemid=\"").append(itemId).append("\"");
/* 1072 */     if ((groupAction == P8SearchDefinition.GroupAction.all) || (groupAction == P8SearchDefinition.GroupAction.any))
/* 1073 */       xml.append(" groupaction=\"").append(groupAction).append("\"");
/* 1074 */     P8SearchDefinition.RequiredState required = item.getRequiredState();
/* 1075 */     if (item.isSearchModifierProximity()) {
/* 1076 */       int range = item.getSearchModifierRange();
/* 1077 */       if (range < 1)
/* 1078 */         range = 1024;
/* 1079 */       xml.append(" searchmodifier=\"proximity\" searchmodifierrange=\"").append(range).append("\"");
/* 1080 */     } else if (required != P8SearchDefinition.RequiredState.none) {
/* 1081 */       xml.append(" requiredstate=\"").append(required == P8SearchDefinition.RequiredState.required ? "required" : "prohibited").append("\"");
/*      */     }
/* 1083 */     xml.append("><terms>");
/* 1084 */     for (P8SearchDefinition.ContentTerm term : terms) {
/* 1085 */       xml.append("<term");
/* 1086 */       if (term.isPhrase())
/* 1087 */         xml.append(" phrase=\"true\"");
/* 1088 */       xml.append(">").append(term.getValue()).append("</term>");
/*      */     }
/* 1090 */     xml.append("</terms></item>");
/* 1091 */     if ((propName != null) && (propName.length() > 0))
/* 1092 */       xml.append("</field>");
/*      */   }
/*      */   
/*      */   private void createVerityContentItemXml(P8SearchDefinition.ContentClauseItem item, int itemId, StringBuilder xml) {
/* 1096 */     List<P8SearchDefinition.ContentTerm> terms = item.getContentTerms();
/* 1097 */     if ((terms == null) || (terms.size() < 1)) {
/* 1098 */       return;
/*      */     }
/* 1100 */     P8SearchDefinition.GroupAction groupAction = item.getGroupAction();
/* 1101 */     xml.append("<verityitem editproperty=\"editable\" itemid=\"").append(itemId).append("\"").append(" groupaction=\"").append(groupAction).append("\">");
/* 1102 */     xml.append("<usertext>").append(item.getUserText()).append("</usertext>").append("<verityitemdata>");
/* 1103 */     if (groupAction == P8SearchDefinition.GroupAction.near) {
/* 1104 */       xml.append("<veritynear");
/* 1105 */       int distance = item.getSearchModifierRange();
/* 1106 */       if (distance > 0)
/* 1107 */         xml.append(" distance=\"").append(distance).append("\"");
/* 1108 */       xml.append(">");
/* 1109 */     } else if (groupAction != P8SearchDefinition.GroupAction.none) {
/* 1110 */       xml.append("<verity").append(groupAction).append(">");
/*      */     }
/* 1112 */     for (P8SearchDefinition.ContentTerm term : terms) {
/* 1113 */       xml.append("<verityunit");
/* 1114 */       if (term.isProhibited())
/* 1115 */         xml.append(" not=\"true\"");
/* 1116 */       P8SearchDefinition.VeritySearchOperator operator = term.getWordVariation();
/* 1117 */       if (operator != null)
/* 1118 */         xml.append(" wordvariation=\"").append(operator).append("\"");
/* 1119 */       if (term.isPhrase()) {
/* 1120 */         xml.append(">\"").append(term.getValue()).append("\"</verityunit>");
/*      */       } else
/* 1122 */         xml.append(">").append(term.getValue()).append("</verityunit>");
/*      */     }
/* 1124 */     if (groupAction != P8SearchDefinition.GroupAction.none)
/* 1125 */       xml.append("</verity").append(groupAction).append(">");
/* 1126 */     xml.append("</verityitemdata></verityitem>");
/*      */   }
/*      */   
/*      */   private void populatePropertySymbolicName(P8SearchDefinition.Clause contentClause, String propertySymbolicName) {
/* 1130 */     if (contentClause == null) {
/* 1131 */       return;
/*      */     }
/* 1133 */     if (contentClause.isContainer()) {
/* 1134 */       P8SearchDefinition.Container container = (P8SearchDefinition.Container)contentClause;
/* 1135 */       List<P8SearchDefinition.Clause> clauses = container.getClauses();
/* 1136 */       for (P8SearchDefinition.Clause clause : clauses)
/* 1137 */         populatePropertySymbolicName(clause, propertySymbolicName);
/*      */     } else {
/* 1139 */       ((P8SearchDefinition.ContentClauseItem)contentClause).setPropertySymbolicName(propertySymbolicName);
/*      */     }
/*      */   }
/*      */   
/*      */   private void convertSearchCriterionToXml(StringBuilder xml, SearchCriterion criterion, SearchTemplateBase.ObjectType objectType, int itemId) throws Exception {
/* 1144 */     convertSearchCriterionToXml(xml, criterion, objectType, itemId, null);
/*      */   }
/*      */   
/*      */   private void convertSearchCriterionToXml(StringBuilder xml, SearchCriterion criterion, SearchTemplateBase.ObjectType objectType, int itemId, String customAttributes) throws Exception {
/* 1148 */     String xmlOperator = P8SearchUtil.convertP8SearchTemplateOperator(criterion.getOperator()).toString();
/* 1149 */     xml.append("<").append(xmlOperator).append(">");
/* 1150 */     String xmlSymName = criterion.getName();
/* 1151 */     String xmlDataType = P8SearchUtil.convertP8SearchTemplateDataType(criterion.getDataType()).toString();
/* 1152 */     xml.append("<whereprop editproperty=\"editable\" itemid=\"").append(itemId).append("\" name=\"").append(xmlSymName).append("\" objecttype=\"").append(objectType).append("\" symname=\"").append(xmlSymName).append("\"");
/* 1153 */     if (customAttributes != null) {
/* 1154 */       xml.append(" ").append(customAttributes);
/*      */     }
/* 1156 */     xml.append(">");
/* 1157 */     xml.append("<propdesc datatype=\"").append(xmlDataType).append("\" haschoices=\"false\" hasmarkings=\"false\" symname=\"").append(xmlSymName).append("\" />");
/* 1158 */     xml.append("</whereprop>");
/* 1159 */     String xmlDefaultValue = "";
/* 1160 */     String[] values = criterion.getValues();
/* 1161 */     if ((values != null) && (values.length > 0) && (values[0] != null)) {
/* 1162 */       xmlDefaultValue = values[0];
/* 1163 */       if ((!xmlDefaultValue.isEmpty()) && (P8SearchDefinition.DataType.valueOf(xmlDataType) == P8SearchDefinition.DataType.typedate)) {
/* 1164 */         Calendar cal = WCDateFormat.parseW3CDate(xmlDefaultValue, null);
/* 1165 */         xmlDefaultValue = WCDateFormat.convertDateToW3CDate(cal, true);
/*      */       }
/*      */     }
/* 1168 */     xml.append("<literal><![CDATA[").append(xmlDefaultValue).append("]]></literal>");
/* 1169 */     xml.append("</").append(xmlOperator).append(">");
/*      */   }
/*      */   
/*      */   private void validateSearchDefinition(boolean autoResolve) throws Exception {
/* 1173 */     boolean autoResolved = false;
/* 1174 */     boolean invalidRepository = false;
/* 1175 */     boolean invalidFolder = false;
/* 1176 */     boolean invalidClass = false;
/* 1177 */     boolean invalidFileType = false;
/* 1178 */     boolean invalidProperty = false;
/* 1179 */     boolean invalidTextSearch = false;
/*      */     
/* 1181 */     List<P8SearchDefinition.SearchInObjectStore> objectStores = this.searchDefinition.getObjectStores();
/* 1182 */     invalidRepository = validateObjectStores(objectStores, autoResolve);
/* 1183 */     autoResolved = (autoResolved) || (invalidRepository);
/*      */     
/*      */ 
/* 1186 */     List<P8SearchDefinition.SearchInFolder> folders = this.searchDefinition.getFolders();
/* 1187 */     invalidFolder = validateFolders(folders, autoResolve);
/* 1188 */     autoResolved = (autoResolved) || (invalidFolder);
/*      */     
/* 1190 */     ObjectStore os = P8Util.getObjectStore(this.connection, ((P8SearchDefinition.SearchInObjectStore)this.searchDefinition.getObjectStores().get(0)).getId());
/* 1191 */     List<P8SearchDefinition.SearchClause> clauses = this.searchDefinition.getSearchClauses();
/* 1192 */     for (P8SearchDefinition.SearchClause clause : clauses) {
/* 1193 */       SearchTemplateBase.ObjectType objectType = clause.getFrom();
/* 1194 */       List<P8SearchDefinition.Subclass> subclasses = clause.getSubclasses();
/* 1195 */       invalidClass = validateSubclasses(subclasses, os, objectType, autoResolve);
/* 1196 */       autoResolved = (autoResolved) || (invalidClass);
/*      */       
/* 1198 */       if (subclasses.size() == 0) {
/* 1199 */         String className = objectType == SearchTemplateBase.ObjectType.folder ? "Folder" : "Document";
/* 1200 */         String displayName = P8Util.getClassDisplayName(os, className);
/* 1201 */         P8SearchDefinition.Subclass subclass = new P8SearchDefinition.Subclass(className, displayName, objectType, SearchTemplate.SearchEditProperty.editable, null, true);
/* 1202 */         subclasses.add(subclass);
/*      */       }
/*      */       
/* 1205 */       invalidProperty = validateSearchDefinitionCriteria(clause, os, autoResolve);
/* 1206 */       autoResolved = (autoResolved) || (invalidProperty);
/*      */     }
/*      */     
/* 1209 */     int searchType = 0;
/*      */     try {
/* 1211 */       searchType = P8Util.getPropertyIntegerValue(os, "CBRSearchType");
/*      */     }
/*      */     catch (Exception e) {}
/* 1214 */     if (searchType == 0) {
/* 1215 */       for (P8SearchDefinition.SearchClause clause : clauses) {
/* 1216 */         if (clause.isContentCriteriaDefined()) {
/* 1217 */           if (!autoResolve)
/* 1218 */             throw new RuntimeException("search.exception.textSearchNotFound");
/* 1219 */           clause.setContentCriteria(null);
/* 1220 */           autoResolved = true;
/* 1221 */           invalidTextSearch = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1226 */     SearchTemplate.Macros macros = ((P8SearchTemplate)this.searchTemplate).getMacros();
/* 1227 */     if (macros != null) {
/* 1228 */       String[] fileTypes = macros.getFileTypes();
/* 1229 */       if ((fileTypes != null) && (fileTypes.length > 0)) {
/* 1230 */         Map<String, FileTypeConfig> configMap = getFileTypeConfigMap();
/* 1231 */         if (configMap.isEmpty()) {
/* 1232 */           if (!autoResolve)
/* 1233 */             throw new RuntimeException("search.exception.fileTypeConfigNotFound");
/* 1234 */           macros.setFileTypes(null);
/* 1235 */           autoResolved = true;
/* 1236 */           invalidFileType = true;
/*      */         } else {
/* 1238 */           List<String> fileTypeList = new ArrayList();
/* 1239 */           for (String fileType : fileTypes) {
/* 1240 */             if (!configMap.containsKey(fileType)) {
/* 1241 */               if (!autoResolve)
/* 1242 */                 throw new RuntimeException("search.exception.fileTypeConfigNotFound");
/* 1243 */               autoResolved = true;
/* 1244 */               invalidFileType = true;
/* 1245 */             } else if (autoResolve) {
/* 1246 */               fileTypeList.add(fileType);
/*      */             }
/*      */           }
/* 1249 */           if (autoResolved) {
/* 1250 */             macros.setFileTypes((String[])fileTypeList.toArray(new String[fileTypeList.size()]));
/*      */           }
/*      */         }
/*      */       }
/* 1254 */       SearchTemplate.UserAction[] userActions = macros.getUserActions();
/* 1255 */       if (userActions != null) {
/* 1256 */         for (SearchTemplate.UserAction userAction : userActions) {
/* 1257 */           String[] users = userAction.getUsers();
/* 1258 */           if (users != null) {
/* 1259 */             String[] userDisplayNames = new String[users.length];
/* 1260 */             int i = 0;
/* 1261 */             for (String userName : users) {
/* 1262 */               if (userName.equals("{ME}")) {
/* 1263 */                 userDisplayNames[(i++)] = userName;
/*      */               } else {
/*      */                 try
/*      */                 {
/* 1267 */                   User user = Factory.User.fetchInstance(this.connection.getCEConnection(), userName, null);
/* 1268 */                   userDisplayNames[(i++)] = user.get_DisplayName();
/*      */                 } catch (EngineRuntimeException e) {
/* 1270 */                   userDisplayNames[(i++)] = userName;
/*      */                 }
/*      */               }
/*      */             }
/* 1274 */             userAction.setUserDisplayNames(userDisplayNames);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1280 */     this.searchDefinition.setAutoResolved(autoResolved);
/* 1281 */     this.searchDefinition.setInvalidRepository(invalidRepository);
/* 1282 */     this.searchDefinition.setInvalidClass(invalidClass);
/* 1283 */     this.searchDefinition.setInvalidFolder(invalidFolder);
/* 1284 */     this.searchDefinition.setInvalidFileType(invalidFileType);
/* 1285 */     this.searchDefinition.setInvalidProperty(invalidProperty);
/* 1286 */     this.searchDefinition.setInvalidTextSearch(invalidTextSearch);
/* 1287 */     this.searchDefinition.setDatabaseType(os.get_DatabaseType().getValue());
/*      */   }
/*      */   
/*      */   private void validateSearchTemplate(boolean autoResolve) throws Exception {
/* 1291 */     boolean autoResolved = false;
/* 1292 */     boolean invalidRepository = false;
/* 1293 */     boolean invalidFolder = false;
/* 1294 */     boolean invalidClass = false;
/* 1295 */     boolean invalidProperty = false;
/*      */     
/* 1297 */     P8SearchTemplate template = (P8SearchTemplate)this.searchTemplate;
/* 1298 */     List<P8SearchDefinition.SearchInObjectStore> objectStores = template.getObjectStores();
/* 1299 */     invalidRepository = validateObjectStores(objectStores, autoResolve);
/* 1300 */     autoResolved = (autoResolved) || (invalidRepository);
/*      */     
/*      */ 
/* 1303 */     List<SearchTemplate.SearchFolder> folders = template.getFolders();
/* 1304 */     invalidFolder = validateFolders(folders, autoResolve);
/* 1305 */     autoResolved = (autoResolved) || (invalidFolder);
/*      */     
/* 1307 */     ObjectStore os = P8Util.getObjectStore(this.connection, ((P8SearchDefinition.SearchInObjectStore)objectStores.get(0)).getId());
/* 1308 */     SearchTemplateBase.ObjectType objectType = template.getObjectType();
/* 1309 */     List<SearchTemplate.SearchClass> subclasses = template.getClasses();
/* 1310 */     invalidClass = validateSubclasses(subclasses, os, objectType, autoResolve);
/* 1311 */     autoResolved = (autoResolved) || (invalidClass);
/*      */     
/* 1313 */     if (subclasses.size() == 0) {
/* 1314 */       String className = objectType == SearchTemplateBase.ObjectType.folder ? "Folder" : "Document";
/* 1315 */       String displayName = P8Util.getClassDisplayName(os, className);
/* 1316 */       SearchTemplate.SearchClass subclass = new SearchTemplate.SearchClass(className, displayName, objectType, true, SearchTemplate.SearchEditProperty.editable, null);
/* 1317 */       subclasses.add(subclass);
/*      */     }
/*      */     
/* 1320 */     invalidProperty = validateSearchTemplateCriteria(template.getSearchCriteria(), os, autoResolve);
/* 1321 */     autoResolved = (autoResolved) || (invalidProperty);
/*      */     
/* 1323 */     template.setAutoResolved(autoResolved);
/* 1324 */     template.setInvalidRepository(invalidRepository);
/* 1325 */     template.setInvalidClass(invalidClass);
/* 1326 */     template.setInvalidFolder(invalidFolder);
/* 1327 */     template.setInvalidProperty(invalidProperty);
/*      */   }
/*      */   
/*      */   private String getPropertyClassNameOfSearchClause(P8SearchDefinition.SearchClause searchClause) {
/* 1331 */     SearchTemplateBase.ObjectType objectType = searchClause.getFrom();
/* 1332 */     String className = objectType == SearchTemplateBase.ObjectType.folder ? "Folder" : "Document";
/* 1333 */     if (this.searchDefinition.isIcnGenerated()) {
/* 1334 */       List<P8SearchDefinition.Subclass> classes = searchClause.getSubclasses();
/* 1335 */       if ((classes != null) && (classes.size() == 1)) {
/* 1336 */         SearchTemplate.SearchClass subclass = (SearchTemplate.SearchClass)classes.get(0);
/* 1337 */         className = subclass.getName();
/*      */       }
/*      */     }
/*      */     
/* 1341 */     return className;
/*      */   }
/*      */   
/*      */   private boolean validateObjectStores(List<P8SearchDefinition.SearchInObjectStore> objectStores, boolean autoResolve) {
/* 1345 */     if (objectStores == null) {
/* 1346 */       return false;
/*      */     }
/* 1348 */     boolean autoResolved = false;
/* 1349 */     Iterator<P8SearchDefinition.SearchInObjectStore> itrOs = objectStores.iterator();
/* 1350 */     while (itrOs.hasNext()) {
/* 1351 */       P8SearchDefinition.SearchInObjectStore os = (P8SearchDefinition.SearchInObjectStore)itrOs.next();
/*      */       try {
/* 1353 */         ObjectStore ceOs = P8Util.getObjectStore(this.connection, os.getId());
/* 1354 */         os.setId(ceOs.getObjectReference().getObjectIdentity());
/* 1355 */         os.setName(ceOs.get_DisplayName());
/* 1356 */         os.setSymbolicName(ceOs.get_SymbolicName());
/*      */       } catch (EngineRuntimeException e) {
/* 1358 */         ExceptionCode eCode = e.getExceptionCode();
/* 1359 */         if ((eCode == ExceptionCode.E_OBJECT_NOT_FOUND) || (eCode == ExceptionCode.E_OBJECT_DELETED) || (eCode == ExceptionCode.E_BAD_CLASSID)) {
/* 1360 */           if (!autoResolve)
/* 1361 */             throw new RuntimeException("search.exception.folderNotFound");
/* 1362 */           itrOs.remove();
/* 1363 */           autoResolved = true;
/*      */         }
/*      */         else {
/* 1366 */           throw e;
/*      */         }
/*      */       }
/*      */     }
/* 1370 */     if (objectStores.size() < 1) {
/* 1371 */       ObjectStore os = this.connection.getObjectStore();
/* 1372 */       P8SearchDefinition.SearchInObjectStore searchOs = new P8SearchDefinition.SearchInObjectStore(os.get_Id().toString(), os.get_DisplayName());
/* 1373 */       searchOs.setSymbolicName(os.get_SymbolicName());
/* 1374 */       objectStores.add(searchOs);
/*      */     }
/*      */     
/* 1377 */     return autoResolved;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private <T extends SearchTemplate.SearchFolder> boolean validateFolders(List<T> folders, boolean autoResolve)
/*      */   {
/* 1384 */     if (folders == null) {
/* 1385 */       return false;
/*      */     }
/* 1387 */     boolean autoResolved = false;
/* 1388 */     Iterator<T> itr = folders.iterator();
/* 1389 */     while (itr.hasNext()) {
/* 1390 */       T folder = (SearchTemplate.SearchFolder)itr.next();
/*      */       try {
/* 1392 */         ObjectStore os = P8Util.getObjectStore(this.connection, folder.getObjectStoreId());
/* 1393 */         String folderId = folder.getId();
/* 1394 */         if (folderId.equals(SearchTemplate.THIS_TEAMSPACE_ID)) {
/* 1395 */           folder.setPathName(os.get_SymbolicName() + "\\" + folderId);
/*      */         } else {
/* 1397 */           String pathName = P8Util.getFolderPathName(os, folderId);
/* 1398 */           if (pathName.startsWith("/ClbTeamspaces"))
/*      */           {
/*      */ 
/* 1401 */             int prefixIndex = "/ClbTeamspaces".length() + 1;
/* 1402 */             Pattern expression = Pattern.compile("\\d{4}\\/\\d{2}");
/* 1403 */             Matcher matcher = expression.matcher(pathName.substring(prefixIndex, prefixIndex + 7));
/* 1404 */             int endIndex = 0;
/* 1405 */             if (matcher.find()) {
/* 1406 */               endIndex = "/ClbTeamspaces".length() + 9;
/*      */             } else {
/* 1408 */               endIndex = "/ClbTeamspaces".length() + 1;
/*      */             }
/* 1410 */             int index = pathName.indexOf("/", endIndex);
/* 1411 */             String teamspaceFolderPathName = index < 0 ? pathName : pathName.substring(0, index);
/*      */             
/* 1413 */             PropertyFilter propFilter = new PropertyFilter();
/* 1414 */             propFilter.addIncludeProperty(0, null, null, "ClbTeamspaceName", null);
/* 1415 */             Folder ceFolder = (Folder)os.fetchObject("Folder", teamspaceFolderPathName, propFilter);
/* 1416 */             Properties properties = ceFolder.getProperties();
/* 1417 */             String teamspaceName = "/" + properties.getStringValue("ClbTeamspaceName");
/* 1418 */             pathName = teamspaceName + pathName.substring(index);
/*      */           }
/* 1420 */           folder.setPathName(pathName);
/*      */         }
/* 1422 */         folder.setObjectStoreId(os.getObjectReference().getObjectIdentity());
/* 1423 */         folder.setObjectStoreName(os.get_SymbolicName());
/*      */       } catch (EngineRuntimeException e) {
/* 1425 */         ExceptionCode eCode = e.getExceptionCode();
/* 1426 */         if ((eCode == ExceptionCode.E_OBJECT_NOT_FOUND) || (eCode == ExceptionCode.E_OBJECT_DELETED) || (eCode == ExceptionCode.E_BAD_CLASSID)) {
/* 1427 */           if (!autoResolve)
/* 1428 */             throw new RuntimeException("search.exception.folderNotFound");
/* 1429 */           itr.remove();
/* 1430 */           autoResolved = true;
/*      */         }
/*      */         else {
/* 1433 */           throw e;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1438 */     return autoResolved;
/*      */   }
/*      */   
/*      */   private <T extends SearchTemplate.SearchClass> boolean validateSubclasses(List<T> subclasses, ObjectStore objectStore, SearchTemplateBase.ObjectType objectType, boolean autoResolve) {
/* 1442 */     if (subclasses == null) {
/* 1443 */       return false;
/*      */     }
/* 1445 */     boolean autoResolved = false;
/* 1446 */     Iterator<T> itrClasses = subclasses.iterator();
/* 1447 */     while (itrClasses.hasNext()) {
/* 1448 */       T subclass = (SearchTemplate.SearchClass)itrClasses.next();
/*      */       try {
/* 1450 */         subclass.setDisplayName(P8Util.getClassDisplayName(objectStore, subclass.getName()));
/*      */       } catch (EngineRuntimeException e) {
/* 1452 */         ExceptionCode eCode = e.getExceptionCode();
/* 1453 */         if ((eCode == ExceptionCode.E_OBJECT_NOT_FOUND) || (eCode == ExceptionCode.E_OBJECT_DELETED) || (eCode == ExceptionCode.E_BAD_CLASSID)) {
/* 1454 */           if (!autoResolve)
/* 1455 */             throw new RuntimeException("search.exception.classNotFound");
/* 1456 */           itrClasses.remove();
/* 1457 */           autoResolved = true;
/*      */         }
/*      */         else {
/* 1460 */           throw e;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1465 */     return autoResolved;
/*      */   }
/*      */   
/*      */   private boolean validateSearchDefinitionCriteria(P8SearchDefinition.SearchClause searchClause, ObjectStore objectStore, boolean autoResolve) {
/* 1469 */     boolean autoResolved = false;
/* 1470 */     String className = getPropertyClassNameOfSearchClause(searchClause);
/* 1471 */     List<String> commonProps = this.searchDefinition.isIcnGenerated() ? getCommonPropertyNames(searchClause, objectStore) : null;
/* 1472 */     Collection<P8SearchDefinition.ClauseItem> conditions = searchClause.retrieveWhereClauseConditions().values();
/* 1473 */     Iterator<P8SearchDefinition.ClauseItem> itrConditions = conditions.iterator();
/* 1474 */     while (itrConditions.hasNext()) {
/* 1475 */       P8SearchDefinition.WhereClauseCondition condition = (P8SearchDefinition.WhereClauseCondition)itrConditions.next();
/* 1476 */       String symbolicName = condition.getPropSymbolicName();
/* 1477 */       boolean exists = (commonProps == null) || (commonProps.contains(symbolicName));
/* 1478 */       String displayName = exists ? P8Util.getPropertyDisplayName(objectStore, className, symbolicName) : null;
/* 1479 */       if (displayName == null) {
/* 1480 */         if (!autoResolve)
/* 1481 */           throw new RuntimeException("search.exception.propertyNotFound");
/* 1482 */         itrConditions.remove();
/* 1483 */         autoResolved = true;
/*      */       }
/*      */       else {
/* 1486 */         condition.setPropName(displayName);
/*      */         
/* 1488 */         String literal = condition.getLiteral();
/* 1489 */         if ((literal != null) && (literal.length() > 0) && ((symbolicName.equals("Creator")) || (symbolicName.equals("LastModifier")) || (symbolicName.equals("Owner")))) {
/*      */           try
/*      */           {
/* 1492 */             User user = Factory.User.fetchInstance(this.connection.getCEConnection(), literal, null);
/* 1493 */             condition.setLabel(user.get_DisplayName());
/*      */           } catch (EngineRuntimeException e) {
/* 1495 */             condition.setLabel(literal);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1500 */     return autoResolved;
/*      */   }
/*      */   
/*      */   private List<String> getCommonPropertyNames(P8SearchDefinition.SearchClause searchClause, ObjectStore objectStore) {
/* 1504 */     List<P8SearchDefinition.Subclass> classes = searchClause.getSubclasses();
/* 1505 */     List<String> commonPropNames = null;
/* 1506 */     for (P8SearchDefinition.Subclass subclass : classes) {
/* 1507 */       ClassDescription cd = P8Util.getCachedCD(objectStore, subclass.getName());
/* 1508 */       ArrayList<PropertyDescription> props = P8Util.getPropertyDescriptions(cd, subclass.isSearchSubclasses(), false, true, false, true);
/* 1509 */       List<String> propNames = new ArrayList();
/* 1510 */       for (PropertyDescription prop : props)
/* 1511 */         propNames.add(prop.get_SymbolicName());
/* 1512 */       if (commonPropNames == null) {
/* 1513 */         commonPropNames = propNames;
/*      */       } else {
/* 1515 */         commonPropNames.retainAll(propNames);
/*      */       }
/*      */     }
/* 1518 */     return commonPropNames;
/*      */   }
/*      */   
/*      */   private boolean validateSearchTemplateCriteria(List<SearchCriteria> searchCriteria, ObjectStore objectStore, boolean autoResolve) {
/* 1522 */     if (searchCriteria == null) {
/* 1523 */       return false;
/*      */     }
/* 1525 */     boolean autoResolved = false;
/* 1526 */     P8SearchTemplate template = (P8SearchTemplate)this.searchTemplate;
/* 1527 */     String className = template.getFirstClassName();
/* 1528 */     Iterator<SearchCriteria> itrConditions = searchCriteria.iterator();
/* 1529 */     while (itrConditions.hasNext()) {
/* 1530 */       SearchCriterion condition = (SearchCriterion)itrConditions.next();
/* 1531 */       String symbolicName = condition.getName();
/* 1532 */       String[] values = condition.getValues();
/* 1533 */       String literal = (values == null) || (values.length < 1) ? null : values[0];
/* 1534 */       String displayName = P8Util.getPropertyDisplayName(objectStore, className, symbolicName);
/* 1535 */       if (displayName == null) {
/* 1536 */         if (!autoResolve)
/* 1537 */           throw new RuntimeException("search.exception.propertyNotFound");
/* 1538 */         itrConditions.remove();
/* 1539 */         autoResolved = true;
/*      */       }
/*      */       else {
/* 1542 */         condition.setDisplayName(displayName);
/* 1543 */         if ((literal != null) && (literal.length() > 0) && ((symbolicName.equals("Creator")) || (symbolicName.equals("LastModifier")) || (symbolicName.equals("Owner")))) {
/*      */           try
/*      */           {
/* 1546 */             User user = Factory.User.fetchInstance(this.connection.getCEConnection(), literal, null);
/* 1547 */             condition.setDisplayValues(new String[] { user.get_DisplayName() });
/*      */           } catch (EngineRuntimeException e) {
/* 1549 */             condition.setDisplayValues(new String[] { literal });
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1554 */     return autoResolved;
/*      */   }
/*      */   
/*      */   private Map<String, FileTypeConfig> getFileTypeConfigMap() throws ConfigurationException, MissingValueException {
/* 1558 */     String appName = this.request.getParameter("application");
/* 1559 */     if ((appName == null) || (appName.isEmpty()))
/* 1560 */       appName = "navigator";
/* 1561 */     SettingsConfig settingsConfig = Config.getSettingsConfig(appName);
/* 1562 */     return settingsConfig.getFileTypesMap(appName);
/*      */   }
/*      */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\search\p8\P8SearchTemplateDocument.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */