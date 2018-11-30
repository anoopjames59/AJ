/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.configuration.Config;
/*     */ import com.ibm.ier.plugin.configuration.RepositoryConfig;
/*     */ import com.ibm.ier.plugin.configuration.SettingsConfig;
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.ForwardPager;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.ier.plugin.util.IERSearchResultsBean;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.IteratorWrapper;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.BaseEntity;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.core.Repository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.collections.list.SetUniqueList;
/*     */ import org.apache.commons.lang.StringUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpenContainerService
/*     */   extends IERBasePluginService
/*     */ {
/*  59 */   private static Set<String> sortableFolderProperties = new HashSet();
/*  60 */   protected int pageSize = 200;
/*     */   
/*     */   static {
/*  63 */     sortableFolderProperties.add("FolderName");
/*  64 */     sortableFolderProperties.add("Id");
/*  65 */     sortableFolderProperties.add("LastModifier");
/*  66 */     sortableFolderProperties.add("DateLastModified");
/*  67 */     sortableFolderProperties.add("ContainerType");
/*     */   }
/*     */   
/*     */   public String getId() {
/*  71 */     return "ierOpencontainerService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  75 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void setPageSize(int pageSize) {
/*  79 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */   private String getOrderByField(HttpServletRequest request, String sortKey, boolean forFolders, boolean descending) {
/*  83 */     if ((sortKey == null) || ((forFolders) && (!sortableFolderProperties.contains(sortKey))) || (sortKey.equals("{NAME}"))) {
/*  84 */       sortKey = "!Name";
/*     */     }
/*     */     
/*  87 */     if (sortKey.equals("!MimeTypeIcon")) {
/*  88 */       sortKey = forFolders ? "ContainerType" : "MimeType";
/*  89 */     } else if (sortKey.equals("!Name")) {
/*  90 */       sortKey = IERUtil.getNameProperty(request, forFolders);
/*     */     }
/*     */     
/*  93 */     String result = sortKey + (descending ? " DESC" : "");
/*  94 */     return result;
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*     */   {
/*  99 */     String methodName = "executeAction";
/* 100 */     Logger.logEntry(this, methodName, request);
/* 101 */     P8QueryContinuationData queryData = new P8QueryContinuationData();
/*     */     
/* 103 */     String filterType = request.getParameter("filter_type");
/* 104 */     queryData.returnOnlyFolders = ((filterType != null) && (filterType.equalsIgnoreCase("folderSearch")));
/*     */     
/* 106 */     String serverName = request.getParameter("repositoryId");
/* 107 */     String folderId = request.getParameter("docid");
/* 108 */     String orderBy = request.getParameter("order_by");
/* 109 */     boolean disableSort = false;
/* 110 */     String descendingOrderParam = request.getParameter("order_descending");
/* 111 */     queryData.descending = StringUtils.equals(descendingOrderParam, "true");
/*     */     
/* 113 */     Logger.logDebug(this, methodName, request, "folderID: " + folderId);
/*     */     
/* 115 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/* 116 */     mediator.setServerName(serverName);
/* 117 */     mediator.setPageSize(this.pageSize);
/*     */     try
/*     */     {
/* 120 */       String desktopId = this.servletRequest.getParameter("desktop");
/* 121 */       SettingsConfig settingsConfig = desktopId != null ? Config.getDesktopConfig(desktopId) : Config.getSettingsConfig();
/* 122 */       if (settingsConfig.getBrowseFolderOnly()) {
/* 123 */         queryData.returnOnlyFolders = true;
/*     */       }
/* 125 */       FilePlanRepository fp_repository = this.baseService.getFilePlanRepository();
/* 126 */       String realFolderId = IERUtil.getObjectIdentity(folderId);
/* 127 */       if (realFolderId.equals("/")) {
/* 128 */         queryData.returnOnlyFolders = true;
/*     */       }
/* 130 */       if ((!queryData.returnOnlyFolders) && (orderBy == null)) {
/* 131 */         disableSort = settingsConfig.getBrowseDisableSort();
/*     */       }
/*     */       
/* 134 */       List<String> folderProps = Arrays.asList(new String[] { "FolderName", "AGGREGATION", "Reviewer", "CurrentPhaseExecutionDate", "CurrentPhaseExecutionStatus", "CurrentPhaseDecisionDate", "CurrentPhaseAction", "CurrentPhaseReviewDecision", "LastSweepDate", "RecordFolderIdentifier", "RecordCategoryIdentifier", "RMEntityType", "AllowedRMContainees", "AllowedRMTypes", "CurrentPhaseAction", "OnHold", "Holds", "Id", "DateLastModified", "IsDeleted", "IncrementedBy", "DisposalSchedule", "DateCreated", "RMExternallyManagedBy", "RMRetentionPeriod", "RMRetentionTriggerPropertyName" });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */       List<String> columns = null;
/* 143 */       RepositoryConfig config = Config.getRepositoryConfig(serverName);
/* 144 */       if (config != null) {
/* 145 */         String[] folderColumns = config.getDisplayColumns("RMFolder");
/* 146 */         if (folderColumns != null) {
/* 147 */           columns = Arrays.asList(folderColumns);
/* 148 */           folderProps = new ArrayList(folderProps);
/* 149 */           for (String c : columns) {
/* 150 */             if ((folderProps.indexOf(c) < 0) && (!c.equals("{NAME}")) && (!c.equals("ContainerId"))) {
/* 151 */               folderProps.add(c);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 157 */       SetUniqueList requestedProperties = SetUniqueList.decorate(new ArrayList(folderProps));
/* 158 */       IERQuery dq = new IERQuery();
/* 159 */       dq.setFromClause("RMFolder");
/* 160 */       dq.setRequestedProperties(folderProps);
/*     */       
/*     */ 
/* 163 */       StringBuilder whereClause = new StringBuilder("Parent = Object('");
/* 164 */       whereClause.append(realFolderId);
/* 165 */       whereClause.append("') AND IsHiddenContainer = false AND ");
/* 166 */       whereClause.append("FolderName");
/* 167 */       whereClause.append(" <> 'Locations' AND (IsDeleted IS NULL OR IsDeleted <> true)");
/*     */       
/* 169 */       dq.setWhereClause(whereClause.toString());
/* 170 */       String sortProperty = getOrderByField(this.servletRequest, orderBy, true, queryData.descending);
/* 171 */       dq.setOrderByClause(sortProperty);
/* 172 */       queryData.folderSQL = dq.generateSQL();
/*     */       
/* 174 */       if ((!queryData.returnOnlyFolders) && (
/* 175 */         (filterType == null) || (!filterType.equalsIgnoreCase("searchAndFolderSearch"))))
/*     */       {
/*     */ 
/* 178 */         List<String> documentProps = Arrays.asList(new String[] { "DocumentTitle", "AGGREGATION", "Reviewer", "CurrentPhaseExecutionStatus", "Id", "Creator", "DateCreated", "MimeType", "OnHold", "Holds", "RMEntityType", "CurrentPhaseExecutionDate", "CurrentPhaseAction", "DateLastModified", "IsDeleted" });
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 183 */         requestedProperties.addAll(documentProps);
/* 184 */         IERQuery fq = new IERQuery();
/* 185 */         fq.setFromClause("RecordInfo");
/* 186 */         fq.setRequestedProperties(documentProps);
/* 187 */         fq.setRepository(fp_repository);
/* 188 */         fq.setWhereClause("This INFOLDER ('" + realFolderId + "') AND (" + "IsDeleted" + " IS NULL OR " + "IsDeleted" + " <> true)");
/* 189 */         if (!disableSort) {
/* 190 */           String sortPropertyForDocuments = getOrderByField(this.servletRequest, orderBy, false, queryData.descending);
/* 191 */           fq.setOrderByClause(sortPropertyForDocuments);
/*     */         }
/* 193 */         queryData.documentSQL = fq.generateSQL();
/*     */       }
/*     */       
/*     */ 
/* 197 */       ForwardPager<BaseEntity> pager = executeQuery(queryData, requestedProperties, fp_repository);
/* 198 */       List<BaseEntity> hits = pager.loadNextPage();
/*     */       
/* 200 */       if (!pager.isEndReached()) {
/* 201 */         queryData.folderId = folderId;
/* 202 */         queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/* 203 */         queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/* 204 */         request.getSession().setAttribute(queryData.sessionKey, pager);
/* 205 */         queryData.properties = IERUtil.StringListToString(requestedProperties);
/*     */         
/* 207 */         String data = queryData.saveToString();
/* 208 */         mediator.setContinuationData(data);
/*     */       }
/*     */       
/* 211 */       Collection<RMClassDescription> classDescriptions = findUniqueClassDescriptions(hits, fp_repository, request);
/* 212 */       mediator.setClassDescriptions(new ArrayList(classDescriptions));
/* 213 */       mediator.setServerName(serverName);
/* 214 */       IERSearchResultsBean searchResultsBean = new IERSearchResultsBean(hits, true);
/* 215 */       mediator.setSearchResultsBean(searchResultsBean);
/* 216 */       mediator.setParentDocID(folderId);
/* 217 */       mediator.setMaxResultsReached(pager.isMaxResultsReached());
/* 218 */       mediator.setRequestedProperties(requestedProperties);
/*     */       
/* 220 */       if (columns == null) {
/* 221 */         columns = Arrays.asList(new String[] { "Name", "Reviewer", "DisposalSchedule", "CurrentPhaseExecutionDate", "DateLastModified" });
/*     */       }
/* 223 */       mediator.setDisplayColumns(columns);
/* 224 */       mediator.setSortByProperty(orderBy);
/*     */       
/* 226 */       Logger.logDebug(this, methodName, request, " after adding search results to session.");
/*     */     }
/*     */     catch (Exception e) {
/* 229 */       e.printStackTrace();
/*     */     }
/*     */     
/* 232 */     Logger.logExit(this, methodName, request);
/*     */   }
/*     */   
/*     */   protected ForwardPager<BaseEntity> executeQuery(P8QueryContinuationData queryData, List<String> requestedProperties, Repository repository) {
/* 236 */     Iterator<BaseEntity> documentsIterator = null;Iterator<BaseEntity> foldersIterator = null;
/*     */     
/* 238 */     if (StringUtils.isNotEmpty(queryData.documentSQL)) {
/* 239 */       documentsIterator = IERQuery.executeQueryAsObjectsIterator(repository, queryData.documentSQL, requestedProperties, this.pageSize, EntityType.Record);
/*     */     }
/*     */     
/* 242 */     if (StringUtils.isNotEmpty(queryData.folderSQL)) {
/* 243 */       foldersIterator = IERQuery.executeQueryAsObjectsIterator(repository, queryData.folderSQL, requestedProperties, this.pageSize, EntityType.Container);
/*     */     }
/*     */     
/* 246 */     List<Iterator<BaseEntity>> allIterators = new ArrayList();
/* 247 */     if (queryData.returnOnlyFolders) {
/* 248 */       allIterators.add(foldersIterator);
/*     */     }
/* 250 */     else if (queryData.descending) {
/* 251 */       if (documentsIterator != null)
/* 252 */         allIterators.add(documentsIterator);
/* 253 */       if (foldersIterator != null)
/* 254 */         allIterators.add(foldersIterator);
/*     */     } else {
/* 256 */       if (foldersIterator != null)
/* 257 */         allIterators.add(foldersIterator);
/* 258 */       if (documentsIterator != null) {
/* 259 */         allIterators.add(documentsIterator);
/*     */       }
/*     */     }
/* 262 */     IteratorWrapper<BaseEntity> wrapperIterator = new IteratorWrapper(allIterators);
/*     */     
/* 264 */     ForwardPager<BaseEntity> pager = new ForwardPager(wrapperIterator, this.pageSize);
/*     */     
/* 266 */     return pager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean validateRequest(HttpServletRequest request, HttpServletResponse response)
/*     */   {
/* 274 */     String serverName = request.getParameter("repositoryId");
/* 275 */     String pid = request.getParameter("docid");
/*     */     
/*     */ 
/* 278 */     Logger.logDebug(this, "executeAction", request, "Request Parameter: serverName = " + serverName);
/* 279 */     Logger.logDebug(this, "executeAction", request, "Request Parameter: documentID = " + pid);
/*     */     
/* 281 */     return (serverName != null) && (pid != null);
/*     */   }
/*     */   
/*     */   protected void logFDC(HttpServletRequest request, HttpServletResponse response, Object param, String methodName) {
/* 285 */     String serverName = request.getParameter("repositoryId");
/* 286 */     String itemTypeName = request.getParameter("template");
/* 287 */     String pid = request.getParameter("docid");
/*     */     
/*     */ 
/* 290 */     Logger.logInfo(this, methodName, request, "Request Parameter: serverName = " + serverName);
/* 291 */     Logger.logInfo(this, methodName, request, "Request Parameter: itemTypeName = " + itemTypeName);
/* 292 */     Logger.logInfo(this, methodName, request, "Request Parameter: documentID = " + pid);
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\OpenContainerService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */