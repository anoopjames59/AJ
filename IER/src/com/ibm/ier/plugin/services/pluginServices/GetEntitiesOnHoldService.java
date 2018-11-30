/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
/*     */ import com.ibm.ier.plugin.mediators.BaseMediator;
/*     */ import com.ibm.ier.plugin.mediators.IERSearchResultsMediator;
/*     */ import com.ibm.ier.plugin.nls.Logger;
/*     */ import com.ibm.ier.plugin.services.IERBasePluginService;
/*     */ import com.ibm.ier.plugin.services.IERBaseService;
/*     */ import com.ibm.ier.plugin.util.ForwardPager;
/*     */ import com.ibm.ier.plugin.util.IERQuery;
/*     */ import com.ibm.ier.plugin.util.IERUtil;
/*     */ import com.ibm.ier.plugin.util.IteratorWrapper;
/*     */ import com.ibm.ier.plugin.util.P8QueryContinuationData;
/*     */ import com.ibm.ier.plugin.util.SessionUtil;
/*     */ import com.ibm.jarm.api.constants.EntityType;
/*     */ import com.ibm.jarm.api.core.FilePlanRepository;
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import com.ibm.jarm.api.query.ResultRow;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.collections.list.SetUniqueList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GetEntitiesOnHoldService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  38 */     return "ierGetEntitiesOnHoldService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator() {
/*  42 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response) throws Exception
/*     */   {
/*  47 */     Logger.logEntry(this, "serviceExecute", request);
/*     */     
/*  49 */     P8QueryContinuationData queryData = new P8QueryContinuationData();
/*  50 */     String entityTypeStr = request.getParameter("ier_entityType");
/*  51 */     EntityType entityType = null;
/*  52 */     if ((entityTypeStr != null) && (entityTypeStr.length() > 0)) {
/*  53 */       entityType = EntityType.getInstanceFromInt(Integer.parseInt(entityTypeStr));
/*     */     }
/*  55 */     if (EntityType.Record == entityType) {
/*  56 */       queryData.returnOnlyFolders = false;
/*  57 */     } else if (EntityType.Container == entityType) {
/*  58 */       queryData.returnOnlyFolders = true;
/*     */     }
/*     */     
/*  61 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*  62 */     mediator.setServerName(request.getParameter("repositoryId"));
/*  63 */     mediator.setPageSize(200);
/*  64 */     mediator.setIsObject(true);
/*  65 */     FilePlanRepository fp_repository = this.baseService.getFilePlanRepository();
/*  66 */     String isSearch = request.getParameter("IS_SEARCH");
/*     */     
/*  68 */     String holdId = IERUtil.getIdFromDocIdString(request.getParameter("ier_holdId"));
/*  69 */     String filterString = request.getParameter("ier_filterString");
/*  70 */     SetUniqueList requestedProperties = null;
/*     */     
/*  72 */     String sql = null;
/*  73 */     if (!queryData.returnOnlyFolders) {
/*  74 */       List<String> documentProps = Arrays.asList(new String[] { "DocumentTitle", "Reviewer", "DateLastModified", "IsDynamicHold", "DateCreated" });
/*     */       
/*  76 */       requestedProperties = SetUniqueList.decorate(new ArrayList(documentProps));
/*     */       
/*  78 */       sql = "SELECT r.[DocumentTitle], r.[Reviewer], r.[Id], r.[MimeType], r.[OnHold], r.[RMEntityType], r.[DateLastModified], rhl.[IsDynamicHold], rhl.[DateCreated] FROM [RecordInfo] r INNER JOIN RecordHoldLink rhl ON r.This = rhl.Head WHERE rhl.Tail = OBJECT('" + holdId + "') ";
/*     */       
/*     */ 
/*     */ 
/*  82 */       if (filterString != null) {
/*  83 */         sql = sql + "AND r.DocumentTitle like '%" + filterString + "%' ";
/*     */       }
/*  85 */       sql = sql + "ORDER BY r.DocumentTitle";
/*  86 */       queryData.documentSQL = sql;
/*  87 */       mediator.setNameProperty("DocumentTitle");
/*  88 */       mediator.setClassName("RecordInfo");
/*     */     } else {
/*  90 */       List<String> folderProps = Arrays.asList(new String[] { "FolderName", "Reviewer", "DateLastModified", "IsDynamicHold", "DateCreated" });
/*     */       
/*  92 */       requestedProperties = SetUniqueList.decorate(new ArrayList(folderProps));
/*     */       
/*  94 */       sql = "SELECT rf.[FolderName], rf.[Reviewer], rf.[RMEntityType], rf.[RecordFolderIdentifier], rf.[Id], rf.[DateLastModified], rf.[OnHold], rhl.[IsDynamicHold], rhl.[DateCreated] FROM [RMFolder] rf INNER JOIN RMFolderHoldLink rhl ON rf.This = rhl.Head WHERE rhl.Tail = OBJECT('" + holdId + "') ";
/*     */       
/*     */ 
/*     */ 
/*  98 */       if (filterString != null) {
/*  99 */         sql = sql + "AND rf.FolderName like '%" + filterString + "%' ";
/*     */       }
/* 101 */       sql = sql + "ORDER BY rf.FolderName";
/* 102 */       queryData.folderSQL = sql;
/* 103 */       mediator.setNameProperty("FolderName");
/*     */     }
/* 105 */     if ("YES".equalsIgnoreCase(isSearch)) {
/* 106 */       Iterator<ResultRow> rowIterator = IERQuery.executeQueryAsRowsIterator(fp_repository, sql, null, 200);
/*     */       
/* 108 */       List<Iterator<ResultRow>> allIterators = new ArrayList();
/* 109 */       if (rowIterator != null) {
/* 110 */         allIterators.add(rowIterator);
/*     */       }
/* 112 */       IteratorWrapper<ResultRow> wrapperIterator = new IteratorWrapper(allIterators);
/* 113 */       ForwardPager<ResultRow> pager = new ForwardPager(wrapperIterator, 200);
/* 114 */       List<ResultRow> rows = pager.loadNextPage();
/*     */       
/* 116 */       if (!pager.isEndReached()) {
/* 117 */         queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/* 118 */         queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/* 119 */         request.getSession().setAttribute(queryData.sessionKey, pager);
/* 120 */         queryData.properties = IERUtil.StringListToString(requestedProperties);
/*     */         
/* 122 */         String data = queryData.saveToString();
/* 123 */         mediator.setContinuationData(data);
/*     */       }
/* 125 */       mediator.setResultRows(rows);
/* 126 */       mediator.setMaxResultsReached(pager.isMaxResultsReached());
/*     */     }
/* 128 */     Collection<RMClassDescription> classDescriptions = findUniqueClassDescriptions(fp_repository, request, queryData.returnOnlyFolders);
/* 129 */     mediator.setClassDescriptions(new ArrayList(classDescriptions));
/* 130 */     mediator.setParentDocID(holdId);
/* 131 */     mediator.setRequestedProperties(requestedProperties);
/* 132 */     mediator.setDisplayColumns(requestedProperties);
/* 133 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   protected static Collection<RMClassDescription> findUniqueClassDescriptions(FilePlanRepository fp_repository, HttpServletRequest request, boolean returnOnlyFolders)
/*     */   {
/* 138 */     Map<String, RMClassDescription> mapOfCDs = new HashMap();
/* 139 */     if (!returnOnlyFolders) {
/* 140 */       RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, "RecordInfo", request);
/* 141 */       String id = fp_repository.getObjectIdentity() + "/" + "RecordInfo";
/* 142 */       mapOfCDs.put(id, cd);
/*     */       
/* 144 */       cd = SessionUtil.getClassDescription(fp_repository, "RecordHoldLink", request);
/* 145 */       id = fp_repository.getObjectIdentity() + "/" + "RecordHoldLink";
/* 146 */       mapOfCDs.put(id, cd);
/*     */     } else {
/* 148 */       RMClassDescription cd = SessionUtil.getClassDescription(fp_repository, "RecordCategory", request);
/* 149 */       String id = fp_repository.getObjectIdentity() + "/" + "RecordCategory";
/* 150 */       mapOfCDs.put(id, cd);
/*     */       
/* 152 */       cd = SessionUtil.getClassDescription(fp_repository, "RecordFolder", request);
/* 153 */       id = fp_repository.getObjectIdentity() + "/" + "RecordFolder";
/* 154 */       mapOfCDs.put(id, cd);
/*     */       
/* 156 */       cd = SessionUtil.getClassDescription(fp_repository, "Volume", request);
/* 157 */       id = fp_repository.getObjectIdentity() + "/" + "Volume";
/* 158 */       mapOfCDs.put(id, cd);
/*     */       
/* 160 */       cd = SessionUtil.getClassDescription(fp_repository, "RMFolderHoldLink", request);
/* 161 */       id = fp_repository.getObjectIdentity() + "/" + "RMFolderHoldLink";
/* 162 */       mapOfCDs.put(id, cd);
/*     */     }
/*     */     
/* 165 */     return mapOfCDs.values();
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\GetEntitiesOnHoldService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */