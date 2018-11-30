/*     */ package com.ibm.ier.plugin.services.pluginServices;
/*     */ 
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
/*     */ import com.ibm.jarm.api.meta.RMClassDescription;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.collections.list.SetUniqueList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RetrieveLinksOnEntityService
/*     */   extends IERBasePluginService
/*     */ {
/*     */   public String getId()
/*     */   {
/*  38 */     return "ierRetrieveLinksOnEntityService";
/*     */   }
/*     */   
/*     */   protected BaseMediator createMediator()
/*     */   {
/*  43 */     return new IERSearchResultsMediator();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serviceExecute(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*  50 */     Logger.logEntry(this, "serviceExecute", request);
/*  51 */     P8QueryContinuationData queryData = new P8QueryContinuationData();
/*  52 */     EntityType entityType = EntityType.RMLink;
/*     */     
/*  54 */     IERSearchResultsMediator mediator = (IERSearchResultsMediator)getMediator();
/*  55 */     mediator.setServerName(request.getParameter("repositoryId"));
/*  56 */     mediator.setPageSize(200);
/*  57 */     FilePlanRepository fp_repository = this.baseService.getFilePlanRepository();
/*  58 */     mediator.setDisplayColumns(Arrays.asList(new String[] { "ClassName", "LinkName", "Head", "Creator", "DateCreated", "LastModifier", "DateLastModified", "ClassName", "Tail" }));
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
/*  70 */     SetUniqueList requestedProperties = SetUniqueList.decorate(new ArrayList(Arrays.asList(new String[] { "RMEntityType", "LinkName", "Head", "Creator", "DateCreated", "LastModifier", "DateLastModified", "ClassName", "Tail" })));
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
/*  82 */     Iterator<BaseEntity> entityIterator = null;
/*  83 */     String entityId = request.getParameter("ier_entityId");
/*  84 */     String filterString = request.getParameter("ier_filterString");
/*  85 */     IERQuery fq = createQueryForRetrieveLinks(fp_repository, entityId, filterString);
/*  86 */     entityIterator = fq.executeQueryAsObjectsIterator(entityType);
/*  87 */     List<Iterator<BaseEntity>> allIterators = new ArrayList();
/*  88 */     if (entityIterator != null) {
/*  89 */       allIterators.add(entityIterator);
/*     */     }
/*  91 */     IteratorWrapper<BaseEntity> wrapperIterator = new IteratorWrapper(allIterators);
/*  92 */     ForwardPager<BaseEntity> pager = new ForwardPager(wrapperIterator, 200);
/*  93 */     List<BaseEntity> hits = pager.loadNextPage();
/*  94 */     Collection<RMClassDescription> classDescriptions = findUniqueClassDescriptions(hits, fp_repository, request);
/*  95 */     mediator.setClassDescriptions(new ArrayList(classDescriptions));
/*     */     
/*  97 */     if (!pager.isEndReached()) {
/*  98 */       queryData.sessionKey = Long.toString(System.currentTimeMillis(), 36);
/*  99 */       queryData.itemsToSkip = pager.getNumberOfItemsRetrieved();
/* 100 */       request.getSession().setAttribute(queryData.sessionKey, pager);
/* 101 */       queryData.properties = IERUtil.StringListToString(requestedProperties);
/*     */       
/* 103 */       String data = queryData.saveToString();
/* 104 */       mediator.setContinuationData(data);
/*     */     }
/*     */     
/* 107 */     IERSearchResultsBean searchResultsBean = null;
/* 108 */     if (queryData.returnOnlyFolders) {
/* 109 */       searchResultsBean = new IERSearchResultsBean(hits, true);
/*     */     } else {
/* 111 */       searchResultsBean = new IERSearchResultsBean(hits, false);
/*     */     }
/* 113 */     mediator.setSearchResultsBean(searchResultsBean);
/* 114 */     mediator.setMaxResultsReached(pager.isMaxResultsReached());
/* 115 */     mediator.setRequestedProperties(requestedProperties);
/* 116 */     mediator.setNameProperty("LinkName");
/* 117 */     mediator.setSortByProperty("LinkName");
/* 118 */     mediator.setIsObject(true);
/*     */     
/* 120 */     Logger.logExit(this, "serviceExecute", request);
/*     */   }
/*     */   
/*     */   protected IERQuery createQueryForRetrieveLinks(FilePlanRepository fp_repository, String entityId, String filterString) {
/* 124 */     IERQuery fq = new IERQuery();
/*     */     
/* 126 */     List<String> props = Arrays.asList(new String[] { "LinkName", "Head", "Creator", "DateCreated", "LastModifier", "DateLastModified", "Tail" });
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 135 */     fq.setFromClause("Link");
/* 136 */     fq.setRequestedProperties(props);
/* 137 */     StringBuilder whereClause = new StringBuilder();
/* 138 */     boolean doFilter = (filterString != null) && (!filterString.isEmpty());
/* 139 */     whereClause.append("(Head = Object('");
/* 140 */     whereClause.append(entityId);
/* 141 */     whereClause.append("') OR Tail = Object('");
/* 142 */     whereClause.append(entityId);
/* 143 */     whereClause.append("')");
/* 144 */     whereClause.append(")");
/*     */     
/* 146 */     whereClause.append(" AND RMEntityType <> 400");
/* 147 */     if (doFilter) {
/* 148 */       whereClause.append(" AND LinkName like '%");
/* 149 */       whereClause.append(filterString);
/* 150 */       whereClause.append("%'");
/*     */     }
/*     */     
/* 153 */     fq.setWhereClause(whereClause.toString());
/* 154 */     fq.setRepository(fp_repository);
/* 155 */     fq.setPageSize(200);
/* 156 */     fq.setRepository(fp_repository);
/* 157 */     return fq;
/*     */   }
/*     */ }


/* Location:              D:\Share\IERApplicationPlugin.jar!\com\ibm\ier\plugin\services\pluginServices\RetrieveLinksOnEntityService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */